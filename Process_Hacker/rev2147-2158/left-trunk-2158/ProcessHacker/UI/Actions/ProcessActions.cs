using System;
using System.Collections.Generic;
using System.Windows.Forms;
using ProcessHacker.Common;
using ProcessHacker.Components;
using ProcessHacker.Native;
using ProcessHacker.Native.Api;
using ProcessHacker.Native.Objects;
using ProcessHacker.Native.Security;
namespace ProcessHacker.UI.Actions
{
    public static class ProcessActions
    {
        private enum ElevationAction
        {
            NotRequired,
            Cancel,
            Elevate,
            DontElevate
        }
        private static bool Prompt(IWin32Window window, int[] pids, string[] names,
            string action, string content, bool promptOnlyIfDangerous)
        {
            if (!Properties.Settings.Default.WarnDangerous)
                return true;
            string name = "the selected process(es)";
            if (pids.Length == 1)
                name = names[0];
            else
                name = "the selected processes";
            bool dangerous = false;
            foreach (int pid in pids)
            {
                if (PhUtils.IsDangerousPid(pid))
                {
                    dangerous = true;
                    break;
                }
            }
            bool critical = false;
            foreach (int pid in pids)
            {
                try
                {
                    using (var phandle = new ProcessHandle(pid, ProcessAccess.QueryInformation))
                    {
                        if (phandle.IsCritical())
                        {
                            critical = true;
                            break;
                        }
                    }
                }
                catch
                { }
            }
            if (promptOnlyIfDangerous && !dangerous && !critical)
                return true;
            DialogResult result = DialogResult.No;
            if (OSVersion.HasTaskDialogs)
            {
                TaskDialog td = new TaskDialog();
                td.WindowTitle = "Process Hacker";
                td.MainInstruction = "Do you want to " + action + " " + name + "?";
                td.Content = content;
                if (critical)
                {
                    td.MainIcon = TaskDialogIcon.Warning;
                    td.Content = "You are about to " + action + " one or more CRITICAL processes. " +
                        "Windows is designed to break (crash) when one of these processes is terminated. " +
                        "Are you sure you want to continue?";
                }
                else if (dangerous)
                {
                    td.MainIcon = TaskDialogIcon.Warning;
                    td.Content = "You are about to " + action + " one or more system processes. " +
                        "Doing so will cause system instability. Are you sure you want to continue?";
                }
                if (pids.Length > 1)
                {
                    td.ExpandFooterArea = true;
                    td.ExpandedInformation = "Processes:\r\n";
                    for (int i = 0; i < pids.Length; i++)
                    {
                        bool dangerousPid, criticalPid;
                        dangerousPid = PhUtils.IsDangerousPid(pids[i]);
                        try
                        {
                            using (var phandle = new ProcessHandle(pids[i], ProcessAccess.QueryInformation))
                                criticalPid = phandle.IsCritical();
                        }
                        catch
                        {
                            criticalPid = false;
                        }
                        td.ExpandedInformation += names[i] + " (PID " + pids[i].ToString() + ")" +
                            (dangerousPid ? " (system process) " : "") +
                            (criticalPid ? " (CRITICAL) " : "") +
                            "\r\n";
                    }
                    td.ExpandedInformation = td.ExpandedInformation.Trim();
                }
                td.Buttons = new TaskDialogButton[]
                {
                    new TaskDialogButton((int)DialogResult.Yes, char.ToUpper(action[0]) + action.Substring(1)),
                    new TaskDialogButton((int)DialogResult.No, "Cancel")
                };
                td.DefaultButton = (int)DialogResult.No;
                result = (DialogResult)td.Show(window);
            }
            else
            {
                if (critical)
                {
                    result = MessageBox.Show("You are about to " + action + " one or more CRITICAL processes. " +
                        "Windows is designed to break (crash) when one of these processes is terminated. " +
                        "Are you sure you want to " + action + " " + name + "?",
                        "Process Hacker", MessageBoxButtons.YesNo,
                        MessageBoxIcon.Exclamation, MessageBoxDefaultButton.Button2);
                }
                else if (dangerous)
                {
                    result = MessageBox.Show("You are about to " + action + " one or more system processes. " +
                        "Are you sure you want to " + action + " " + name + "?",
                        "Process Hacker", MessageBoxButtons.YesNo,
                        MessageBoxIcon.Exclamation, MessageBoxDefaultButton.Button2);
                }
                else
                {
                    result = MessageBox.Show("Are you sure you want to " + action + " " + name + "?",
                        "Process Hacker", MessageBoxButtons.YesNo,
                        MessageBoxIcon.Exclamation, MessageBoxDefaultButton.Button2);
                }
            }
            return result == DialogResult.Yes;
        }
        private static ElevationAction PromptForElevation(IWin32Window window, int[] pids, string[] names,
            ProcessAccess access, string elevateAction, string action)
        {
            if (Properties.Settings.Default.ElevationLevel == (int)ElevationLevel.Never)
                return ElevationAction.NotRequired;
            if (
                OSVersion.HasUac &&
                Program.ElevationType == ProcessHacker.Native.Api.TokenElevationType.Limited &&
                KProcessHacker.Instance == null
                )
            {
                try
                {
                    foreach (int pid in pids)
                    {
                        using (var phandle = new ProcessHandle(pid, access))
                        { }
                    }
                }
                catch (WindowsException ex)
                {
                    if (ex.ErrorCode != Win32Error.AccessDenied)
                        return ElevationAction.NotRequired;
                    if (Properties.Settings.Default.ElevationLevel == (int)ElevationLevel.Elevate)
                        return ElevationAction.Elevate;
                    TaskDialog td = new TaskDialog();
                    td.WindowTitle = "Process Hacker";
                    td.MainIcon = TaskDialogIcon.Warning;
                    td.MainInstruction = "Do you want to " + elevateAction + "?";
                    td.Content = "The action cannot be performed in the current security context. " +
                        "Do you want Process Hacker to prompt for the appropriate credentials and " + elevateAction + "?";
                    td.ExpandedInformation = "Error: " + ex.Message + " (0x" + ex.ErrorCode.ToString("x") + ")";
                    td.ExpandFooterArea = true;
                    td.Buttons = new TaskDialogButton[]
                    {
                        new TaskDialogButton((int)DialogResult.Yes, "Elevate\nPrompt for credentials and " + elevateAction + "."),
                        new TaskDialogButton((int)DialogResult.No, "Continue\nAttempt to perform the action without elevation.")
                    };
                    td.CommonButtons = TaskDialogCommonButtons.Cancel;
                    td.UseCommandLinks = true;
                    td.Callback = (taskDialog, args, userData) =>
                    {
                        if (args.Notification == TaskDialogNotification.Created)
                        {
                            taskDialog.SetButtonElevationRequiredState((int)DialogResult.Yes, true);
                        }
                        return false;
                    };
                    DialogResult result = (DialogResult)td.Show(window);
                    if (result == DialogResult.Yes)
                    {
                        return ElevationAction.Elevate;
                    }
                    else if (result == DialogResult.No)
                    {
                        return ElevationAction.DontElevate;
                    }
                    else if (result == DialogResult.Cancel)
                    {
                        return ElevationAction.Cancel;
                    }
                }
            }
            return ElevationAction.NotRequired;
        }
        private static bool ElevateIfRequired(IWin32Window window, int[] pids, string[] names,
            ProcessAccess access, string action)
        {
            ElevationAction result;
            result = PromptForElevation(window, pids, names, access, "elevate the action", action);
            if (result == ElevationAction.NotRequired || result == ElevationAction.DontElevate)
            {
                return false;
            }
            else if (result == ElevationAction.Cancel)
            {
                return true;
            }
            else if (result == ElevationAction.Elevate)
            {
                string objects = "";
                foreach (int pid in pids)
                    objects += pid + ",";
                Program.StartProcessHackerAdmin("-e -type process -action " + action + " -obj \"" +
                    objects + "\" -hwnd " + window.Handle.ToString(), null, window.Handle);
                return true;
            }
            else
            {
                return false;
            }
        }
        private static string GetName(int[] pids, string[] names, int index)
        {
            return "the process \"" + names[index] + "\" with PID " + pids[index].ToString();
        }
        public static bool ShowProperties(IWin32Window window, int pid, string name)
        {
            ElevationAction result;
            if (pid != 4)
            {
                result = PromptForElevation(
                    window,
                    new int[] { pid },
                    new string[] { name },
                    Program.MinProcessQueryRights,
                    "restart Process Hacker elevated",
                    "show properties for"
                    );
            }
            else
            {
                result = ElevationAction.NotRequired;
            }
            if (result == ElevationAction.Elevate)
            {
                Program.StartProcessHackerAdmin("-v -ip " + pid.ToString(), () =>
                {
                    Program.HackerWindow.Exit();
                }, window.Handle);
                return false;
            }
            else if (result == ElevationAction.Cancel)
            {
                return false;
            }
            if (Program.ProcessProvider.Dictionary.ContainsKey(pid))
            {
                try
                {
                    ProcessWindow pForm = Program.GetProcessWindow(Program.ProcessProvider.Dictionary[pid],
                        new Program.PWindowInvokeAction(delegate(ProcessWindow f)
                        {
                            Program.FocusWindow(f);
                        }));
                }
                catch (Exception ex)
                {
                    PhUtils.ShowException("Unable to inspect the process", ex);
                    return false;
                }
            }
            else
            {
                PhUtils.ShowError("Unable to inspect the process because it does not exist.");
            }
            return true;
        }
        public static bool Terminate(IWin32Window window, int[] pids, string[] names, bool prompt)
        {
            bool allGood = true;
            if (ElevateIfRequired(window, pids, names, ProcessAccess.Terminate, "terminate"))
                return false;
            if (prompt && !Prompt(window, pids, names, "terminate",
                "Terminating a process will cause unsaved data to be lost. " +
                "Terminating a system process will cause system instability. " +
                "Are you sure you want to continue?", false))
                return false;
            for (int i = 0; i < pids.Length; i++)
            {
                try
                {
                    using (ProcessHandle phandle =
                        new ProcessHandle(pids[i], ProcessAccess.Terminate))
                        phandle.Terminate();
                }
                catch (Exception ex)
                {
                    allGood = false;
                    if (!PhUtils.ShowContinueMessage(
                        "Unable to terminate " + GetName(pids, names, i),
                        ex
                        ))
                        return false;
                }
            }
            return allGood;
        }
        public static bool TerminateTree(IWin32Window window, int[] pids, string[] names, bool prompt)
        {
            bool allGood = true;
            if (prompt && !Prompt(
                window,
                new int[] { pids[0] },
                new string[] { names[0] + " and its descendants" }, "terminate",
                "Terminating a process tree will cause the process and its descendants to be terminated. " +
                "Are you sure you want to continue?", false
                ))
                return false;
            var processes = Windows.GetProcesses();
            for (int i = 0; i < pids.Length; i++)
            {
                if (!TerminateTree(window, processes, pids[i]))
                    allGood = false;
            }
            return allGood;
        }
        private static bool TerminateTree(IWin32Window window, Dictionary<int, SystemProcess> processes, int pid)
        {
            bool good = true;
            foreach (var process in processes)
            {
                if (process.Value.Process.ProcessId < 4)
                    continue;
                if (process.Value.Process.InheritedFromProcessId.Equals(pid))
                    if (!TerminateTree(window, processes, process.Value.Process.ProcessId))
                        good = false;
            }
            try
            {
                using (ProcessHandle phandle =
                    new ProcessHandle(pid, ProcessAccess.Terminate))
                    phandle.Terminate();
            }
            catch (Exception ex)
            {
                good = false;
                PhUtils.ShowException(
                    "Unable to terminate the process \"" + processes[pid].Name + "\" with PID " + pid.ToString(),
                    ex
                    );
            }
            return good;
        }
        public static void Suspend(IWin32Window window, int[] pids, string[] names, bool prompt)
        {
            if (ElevateIfRequired(window, pids, names, ProcessAccess.SuspendResume, "suspend"))
                return;
            if (prompt && !Prompt(window, pids, names, "suspend",
                "Suspending a process will pause its execution. " +
                "Suspending a system process will cause system instability. " +
                "Are you sure you want to continue?", true))
                return;
            for (int i = 0; i < pids.Length; i++)
            {
                try
                {
                    using (ProcessHandle phandle =
                        new ProcessHandle(pids[i], ProcessAccess.SuspendResume))
                        phandle.Suspend();
                }
                catch (Exception ex)
                {
                    if (!PhUtils.ShowContinueMessage(
                        "Unable to suspend " + GetName(pids, names, i),
                        ex
                        ))
                        return;
                }
            }
        }
        public static void Resume(IWin32Window window, int[] pids, string[] names, bool prompt)
        {
            if (ElevateIfRequired(window, pids, names, ProcessAccess.SuspendResume, "resume"))
                return;
            if (prompt && !Prompt(window, pids, names, "resume",
                "Resuming a process will begin its execution. " +
                "Resuming a system process may lead to system instability. " +
                "Are you sure you want to continue?", true))
                return;
            for (int i = 0; i < pids.Length; i++)
            {
                try
                {
                    using (ProcessHandle phandle =
                        new ProcessHandle(pids[i], ProcessAccess.SuspendResume))
                        phandle.Resume();
                }
                catch (Exception ex)
                {
                    if (!PhUtils.ShowContinueMessage(
                        "Unable to resume " + GetName(pids, names, i),
                        ex
                        ))
                        return;
                }
            }
        }
        public static void ReduceWorkingSet(IWin32Window window, int[] pids, string[] names, bool prompt)
        {
            if (ElevateIfRequired(window, pids, names,
                ProcessAccess.QueryInformation | ProcessAccess.SetQuota, "reduceworkingset"))
                return;
            if (prompt && !Prompt(window, pids, names, "reduce the working set of",
                "Reducing the working set of a process reduces its physical memory consumption. " +
                "Are you sure you want to continue?", true))
                return;
            for (int i = 0; i < pids.Length; i++)
            {
                try
                {
                    using (ProcessHandle phandle =
                        new ProcessHandle(pids[i], ProcessAccess.QueryInformation | ProcessAccess.SetQuota))
                        phandle.EmptyWorkingSet();
                }
                catch (Exception ex)
                {
                    if (!PhUtils.ShowContinueMessage(
                        "Unable to reduce the working set of " + GetName(pids, names, i),
                        ex
                        ))
                        return;
                }
            }
        }
    }
}
