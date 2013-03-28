namespace ThoughtWorks.CruiseControl.Core.Util
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel;
    using System.Diagnostics;
    using System.IO;
    using System.Runtime.CompilerServices;
    using System.Text;
    using System.Threading;
 public class ProcessExecutor
 {
  public event EventHandler<ProcessOutputEventArgs> ProcessOutput;
        [Obsolete("Use the stream override instead")]
        public virtual ProcessResult Execute(ProcessInfo processInfo)
        {
            string projectName = Thread.CurrentThread.Name;
            using (var outputWriter = new StringWriter())
            {
                using (var errorWriter = new StringWriter())
                {
                    using (RunnableProcess p = new RunnableProcess(processInfo, projectName, outputWriter, errorWriter))
                    {
                        p.ProcessOutput += ((sender, e) => OnProcessOutput(e));
                        ProcessMonitor.MonitorProcessForProject(p.Process, projectName);
                        ProcessResult run = p.Run();
                        ProcessMonitor.RemoveMonitorForProject(projectName);
                        run = new ProcessResult(
                            outputWriter.GetStringBuilder().ToString(),
                            errorWriter.GetStringBuilder().ToString(),
                            run.ExitCode,
                            run.TimedOut,
                            run.Failed);
                        outputWriter.Flush();
                        errorWriter.Flush();
                        return run;
                    }
                }
            }
        }
        public virtual ProcessResult Execute(ProcessInfo processInfo, Stream outputStream, Stream errorStream)
        {
            return this.Execute(processInfo, outputStream, errorStream, false);
        }
  public virtual ProcessResult Execute(ProcessInfo processInfo, Stream outputStream, Stream errorStream, bool generateXml)
  {
   string projectName = Thread.CurrentThread.Name;
            TextWriter innerOutputWriter = new StreamWriter(outputStream ?? new MemoryStream(), UTF8Encoding.UTF8);
            TextWriter innerErrorWriter = new StreamWriter(errorStream ?? new MemoryStream(), UTF8Encoding.UTF8);
            if (generateXml)
            {
                innerOutputWriter = new XmlStreamWriter(innerOutputWriter, null);
                innerErrorWriter = new XmlStreamWriter(innerErrorWriter, null);
            }
            using (var outputWriter = innerOutputWriter)
            {
                using (var errorWriter = innerErrorWriter)
                {
                    using (RunnableProcess p = new RunnableProcess(processInfo, projectName, outputWriter, errorWriter))
                    {
                        p.ProcessOutput += ((sender, e) => OnProcessOutput(e));
                        ProcessMonitor.MonitorProcessForProject(p.Process, projectName);
                        ProcessResult run = p.Run();
                        ProcessMonitor.RemoveMonitorForProject(projectName);
                        outputWriter.Flush();
                        outputWriter.Close();
                        errorWriter.Flush();
                        errorWriter.Close();
                        return run;
                    }
                }
            }
  }
  public static void KillProcessCurrentlyRunningForProject(string name)
  {
   ProcessMonitor monitor = ProcessMonitor.ForProject(name);
   if (monitor == null)
   {
    Log.Debug(string.Format("Request to abort process currently running for project {0}, but no process is currently running.", name));
   }
   else
   {
    monitor.KillProcess();
   }
  }
  protected virtual void OnProcessOutput(ProcessOutputEventArgs eventArgs)
  {
   EventHandler<ProcessOutputEventArgs> handler = this.ProcessOutput;
   if (handler == null)
    return;
   handler(this, eventArgs);
  }
  private class RunnableProcess : IDisposable
  {
   public event EventHandler<ProcessOutputEventArgs> ProcessOutput;
   private readonly string projectName;
   private readonly ProcessInfo processInfo;
   private readonly Process process;
   private readonly TextWriter stdOutput;
   private readonly EventWaitHandle outputStreamClosed = new ManualResetEvent(false);
            private readonly TextWriter stdError;
   private readonly EventWaitHandle errorStreamClosed = new ManualResetEvent(false);
   private readonly EventWaitHandle processExited = new ManualResetEvent(false);
   private Thread supervisingThread;
   public RunnableProcess(ProcessInfo processInfo, string projectName, TextWriter outputWriter, TextWriter errorWriter)
   {
    this.projectName = projectName;
    this.processInfo = processInfo;
    process = processInfo.CreateProcess();
                this.stdOutput = outputWriter;
                this.stdError = errorWriter;
   }
   public ProcessResult Run()
   {
    bool hasTimedOut = false;
    bool hasExited = false;
    StartProcess();
    try
    {
     hasExited = WaitHandle.WaitAll(new WaitHandle[] { errorStreamClosed, outputStreamClosed, processExited }, processInfo.TimeOut, true);
     hasTimedOut = !hasExited;
     if (hasTimedOut) Log.Warning(string.Format(
                        "Process timed out: {0} {1}.  Process id: {2}. This process will now be killed.",
                        processInfo.FileName,
                        ProcessArgumentBuilder.GenerateSanitisedArguments(processInfo.Arguments),
                        process.Id));
    }
    catch (ThreadAbortException)
    {
     Log.Info(string.Format(
      "Thread aborted while waiting for '{0} {1}' to exit. Process id: {2}. This process will now be killed.",
                        processInfo.FileName,
                        ProcessArgumentBuilder.GenerateSanitisedArguments(processInfo.Arguments),
                        process.Id));
     throw;
    }
    catch (ThreadInterruptedException)
    {
     Log.Debug(string.Format(
      "Process interrupted: {0} {1}.  Process id: {2}. This process will now be killed.",
                        processInfo.FileName,
                        ProcessArgumentBuilder.GenerateSanitisedArguments(processInfo.Arguments),
                        process.Id));
    }
    finally
    {
     if (!hasExited)
     {
      Kill();
     }
    }
    int exitcode = process.ExitCode;
    bool failed = !processInfo.ProcessSuccessful(exitcode);
                return new ProcessResult(string.Empty, string.Empty, exitcode, hasTimedOut, failed);
   }
   private void StartProcess()
   {
                Log.Debug(string.Format(
                            "Starting process [{0}] in working directory [{1}] with arguments [{2}]",
                            process.StartInfo.FileName,
                            process.StartInfo.WorkingDirectory,
                            ProcessArgumentBuilder.GenerateSanitisedArguments(process.StartInfo.Arguments)));
                process.StartInfo.Arguments = ProcessArgumentBuilder.GenerateApplicationArguments(process.StartInfo.Arguments);
    process.OutputDataReceived += StandardOutputHandler;
    process.ErrorDataReceived += ErrorOutputHandler;
    process.Exited += ExitedHandler;
    process.EnableRaisingEvents = true;
    supervisingThread = Thread.CurrentThread;
    try
    {
     bool isNewProcess = process.Start();
     if (!isNewProcess) Log.Warning("Reusing existing process...");
    }
    catch (Win32Exception e)
    {
     string filename = Path.Combine(process.StartInfo.WorkingDirectory, process.StartInfo.FileName);
     string msg = string.Format("Unable to execute file [{0}].  The file may not exist or may not be executable.", filename);
     throw new IOException(msg, e);
    }
    WriteToStandardInput();
    process.BeginOutputReadLine();
    process.BeginErrorReadLine();
   }
   private void Kill()
   {
    const int WAIT_FOR_KILLED_PROCESS_TIMEOUT = 10000;
    Log.Debug(string.Format("Sending kill to process {0} and waiting {1} seconds for it to exit.", process.Id, WAIT_FOR_KILLED_PROCESS_TIMEOUT / 1000));
    CancelEventsAndWait();
    try
    {
     KillUtil.KillPid(process.Id);
     if (!process.WaitForExit(WAIT_FOR_KILLED_PROCESS_TIMEOUT))
      throw new CruiseControlException(
       string.Format(@"The killed process {0} did not terminate within the allotted timeout period {1}.  The process or one of its child processes may not have died.  This may create problems when trying to re-execute the process.  It may be necessary to reboot the server to recover.",
        process.Id,
        WAIT_FOR_KILLED_PROCESS_TIMEOUT));
     Log.Warning(string.Format("The process has been killed: {0}", process.Id));
    }
    catch (InvalidOperationException)
    {
     Log.Warning(string.Format("Process has already exited before getting killed: {0}", process.Id));
    }
   }
   private void CancelEventsAndWait()
   {
    process.EnableRaisingEvents = false;
    process.Exited -= ExitedHandler;
    process.CancelErrorRead();
    process.CancelOutputRead();
    WaitHandle.WaitAll(new WaitHandle[] { errorStreamClosed, outputStreamClosed }, 1000, true);
   }
   private void WriteToStandardInput()
   {
    if (process.StartInfo.RedirectStandardInput)
    {
     process.StandardInput.Write(processInfo.StandardInputContent);
     process.StandardInput.Flush();
     process.StandardInput.Close();
    }
   }
   private void ExitedHandler(object sender, EventArgs e)
   {
    processExited.Set();
   }
   private void StandardOutputHandler(object sender, DataReceivedEventArgs outLine)
   {
    try
    {
     CollectOutput(outLine.Data, stdOutput, outputStreamClosed);
     if (!string.IsNullOrEmpty(outLine.Data))
      OnProcessOutput(new ProcessOutputEventArgs(ProcessOutputType.StandardOutput, outLine.Data));
    }
    catch (Exception e)
    {
     Log.Error(e);
     Log.Error(string.Format("[{0} {1}] Exception while collecting standard output", projectName, processInfo.FileName));
     supervisingThread.Interrupt();
    }
   }
   private void ErrorOutputHandler(object sender, DataReceivedEventArgs outLine)
   {
    try
    {
     CollectOutput(outLine.Data, stdError, errorStreamClosed);
     if (!string.IsNullOrEmpty(outLine.Data))
      OnProcessOutput(new ProcessOutputEventArgs(ProcessOutputType.ErrorOutput, outLine.Data));
    }
    catch (Exception e)
    {
     Log.Error(e);
     Log.Error(string.Format("[{0} {1}] Exception while collecting error output", projectName, processInfo.FileName));
     supervisingThread.Interrupt();
    }
   }
   private void CollectOutput(string output, TextWriter collector, EventWaitHandle streamReadComplete)
   {
    if (output == null)
    {
     streamReadComplete.Set();
     return;
    }
    collector.WriteLine(output);
    Log.Debug(string.Format("[{0} {1}] {2}", projectName, processInfo.FileName, output));
   }
   void IDisposable.Dispose()
   {
    outputStreamClosed.Close();
    errorStreamClosed.Close();
    processExited.Close();
    process.Dispose();
   }
   public Process Process
   {
    get { return process; }
   }
   protected virtual void OnProcessOutput(ProcessOutputEventArgs eventArgs)
   {
    EventHandler<ProcessOutputEventArgs> handler = this.ProcessOutput;
    if (handler == null)
     return;
    handler(this, eventArgs);
   }
  }
  private class ProcessMonitor
  {
   private static readonly IDictionary<string, ProcessMonitor> processMonitors = new Dictionary<string, ProcessMonitor>();
   [MethodImpl(MethodImplOptions.Synchronized)]
   public static ProcessMonitor ForProject(string projectName)
   {
    return processMonitors.ContainsKey(projectName) ? processMonitors[projectName] : null;
   }
   [MethodImpl(MethodImplOptions.Synchronized)]
   public static void MonitorProcessForProject(Process process, string projectName)
   {
    processMonitors[projectName] = new ProcessMonitor(process, projectName);
   }
   [MethodImpl(MethodImplOptions.Synchronized)]
   public static void RemoveMonitorForProject(string projectName)
   {
    processMonitors.Remove(projectName);
   }
   private readonly Process process;
   private readonly string projectName;
   private ProcessMonitor(Process process, string projectName)
   {
    this.process = process;
    this.projectName = projectName;
   }
   public void KillProcess()
   {
    KillUtil.KillPid(process.Id);
    Log.Info(string.Format("{0}: ------------------------------------------------------------------", projectName));
    Log.Info(string.Format("{0}: ---------The Build Process was successfully aborted---------------", projectName));
    Log.Info(string.Format("{0}: ------------------------------------------------------------------", projectName));
   }
  }
 }
}
