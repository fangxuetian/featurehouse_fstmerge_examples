using System;
using System.Runtime.InteropServices;
using ProcessHacker.Native.Api;
using ProcessHacker.Native.Security;
using ProcessHacker.Native.Security.AccessControl;
namespace ProcessHacker.Native.Objects
{
    public sealed class ServiceHandle : ServiceBaseHandle<ServiceAccess>
    {
        public static ServiceHandle FromHandle(IntPtr handle)
        {
            return new ServiceHandle(handle, false);
        }
        public static ServiceHandle OpenWithAnyAccess(string serviceName)
        {
            try
            {
                return new ServiceHandle(serviceName, ServiceAccess.QueryStatus);
            }
            catch
            {
                try
                {
                    return new ServiceHandle(serviceName, (ServiceAccess)StandardRights.Synchronize);
                }
                catch
                {
                    try
                    {
                        return new ServiceHandle(serviceName, (ServiceAccess)StandardRights.ReadControl);
                    }
                    catch
                    {
                        try
                        {
                            return new ServiceHandle(serviceName, (ServiceAccess)StandardRights.WriteDac);
                        }
                        catch
                        {
                            return new ServiceHandle(serviceName, (ServiceAccess)StandardRights.WriteOwner);
                        }
                    }
                }
            }
        }
        internal ServiceHandle(IntPtr handle, bool owned)
            : base(handle, owned)
        { }
        public ServiceHandle(string serviceName)
            : this(serviceName, ServiceAccess.All)
        { }
        public ServiceHandle(string serviceName, ServiceAccess access)
        {
            using (ServiceManagerHandle manager =
                new ServiceManagerHandle(ScManagerAccess.Connect))
            {
                this.Handle = Win32.OpenService(manager, serviceName, access);
                if (this.Handle == IntPtr.Zero)
                {
                    this.MarkAsInvalid();
                    Win32.ThrowLastError();
                }
            }
        }
        public void Control(ServiceControl control)
        {
            ServiceStatus status = new ServiceStatus();
            if (!Win32.ControlService(this, control, out status))
                Win32.ThrowLastError();
        }
        public void Delete()
        {
            if (!Win32.DeleteService(this))
                Win32.ThrowLastError();
        }
        public QueryServiceConfig GetConfig()
        {
            int requiredSize = 0;
            Win32.QueryServiceConfig(this, IntPtr.Zero, 0, out requiredSize);
            using (MemoryAlloc data = new MemoryAlloc(requiredSize))
            {
                if (!Win32.QueryServiceConfig(this, data, data.Size, out requiredSize))
                    Win32.ThrowLastError();
                return data.ReadStruct<QueryServiceConfig>();
            }
        }
        public string GetDescription()
        {
            int retLen;
            Win32.QueryServiceConfig2(this, ServiceInfoLevel.Description, IntPtr.Zero, 0, out retLen);
            using (MemoryAlloc data = new MemoryAlloc(retLen))
            {
                if (!Win32.QueryServiceConfig2(this, ServiceInfoLevel.Description, data, retLen, out retLen))
                    Win32.ThrowLastError();
                return data.ReadStruct<ServiceDescription>().Description;
            }
        }
        public override SecurityDescriptor GetSecurity(SecurityInformation securityInformation)
        {
            return this.GetSecurity(SeObjectType.Service, securityInformation);
        }
        public ServiceStatusProcess GetStatus()
        {
            ServiceStatusProcess status;
            int retLen;
            if (!Win32.QueryServiceStatusEx(this, 0, out status, Marshal.SizeOf(typeof(ServiceStatusProcess)), out retLen))
                Win32.ThrowLastError();
            return status;
        }
        public override void SetSecurity(SecurityInformation securityInformation, SecurityDescriptor securityDescriptor)
        {
            this.SetSecurity(SeObjectType.Service, securityInformation, securityDescriptor);
        }
        public void Start()
        {
            if (!Win32.StartService(this, 0, null))
                Win32.ThrowLastError();
        }
    }
    public enum ServiceAccept : uint
    {
        NetBindChange = 0x10,
        ParamChange = 0x8,
        PauseContinue = 0x2,
        PreShutdown = 0x100,
        Shutdown = 0x4,
        Stop = 0x1,
        HardwareProfileChange = 0x20,
        PowerEvent = 0x40,
        SessionChange = 0x80
    }
    public enum ServiceControl : uint
    {
        Continue = 0x3,
        Interrogate = 0x4,
        NetBindAdd = 0x7,
        NetBindDisable = 0xa,
        NetBindEnable = 0x9,
        NetBindRemove = 0x8,
        ParamChange = 0x6,
        Pause = 0x2,
        Stop = 0x1
    }
    public enum ServiceErrorControl : uint
    {
        Critical = 0x3,
        Ignore = 0x0,
        Normal = 0x1,
        Severe = 0x2
    }
    public enum ServiceFlags : uint
    {
        None = 0,
        RunsInSystemProcess = 0x1
    }
    public enum ServiceInfoLevel : uint
    {
        Description = 1,
        FailureActions = 2,
        DelayedAutoStartInfo = 3,
        FailureActionsFlag = 4,
        SidInfo = 5,
        RequiredPrivilegesInfo = 6,
        PreShutdownInfo = 7,
        TriggerInfo = 8,
        PreferredNode = 9
    }
    public enum ServiceQueryState : uint
    {
        Active = 1,
        Inactive = 2,
        All = 3
    }
    [Flags]
    public enum ServiceQueryType : uint
    {
        Driver = 0xb,
        Win32 = 0x30
    }
    public enum ServiceStartType : uint
    {
        AutoStart = 0x2,
        BootStart = 0x0,
        DemandStart = 0x3,
        Disabled = 0x4,
        SystemStart = 0x1
    }
    public enum ServiceState : uint
    {
        ContinuePending = 0x5,
        PausePending = 0x6,
        Paused = 0x7,
        Running = 0x4,
        StartPending = 0x2,
        StopPending = 0x3,
        Stopped = 0x1
    }
    [Flags]
    public enum ServiceType : uint
    {
        FileSystemDriver = 0x2,
        KernelDriver = 0x1,
        Win32OwnProcess = 0x10,
        Win32ShareProcess = 0x20,
        InteractiveProcess = 0x100
    }
}
