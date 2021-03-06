using System;
namespace ProcessHacker.Native.Security
{
    [Flags]
    public enum LsaPolicyAccess : uint
    {
        ViewLocalInformation = 0x00000001,
        ViewAuditInformation = 0x00000002,
        GetPrivateInformation = 0x00000004,
        TrustAdmin = 0x00000008,
        CreateAccount = 0x00000010,
        CreateSecret = 0x00000020,
        CreatePrivilege = 0x00000040,
        SetDefaultQuotaLimits = 0x00000080,
        SetAuditRequirements = 0x00000100,
        AuditLogAdmin = 0x00000200,
        ServerAdmin = 0x00000400,
        LookupNames = 0x00000800,
        Notification = 0x00001000,
        All = StandardRights.Required | ViewLocalInformation | ViewAuditInformation |
            GetPrivateInformation | TrustAdmin | CreateAccount | CreateSecret |
            CreatePrivilege | SetDefaultQuotaLimits | SetAuditRequirements |
            AuditLogAdmin | ServerAdmin | LookupNames,
        GenericRead = StandardRights.Read | ViewAuditInformation | GetPrivateInformation,
        GenericWrite = StandardRights.Write | TrustAdmin | CreateAccount | CreateSecret |
            CreatePrivilege | SetDefaultQuotaLimits | SetAuditRequirements |
            AuditLogAdmin | ServerAdmin,
        GenericExecute = StandardRights.Execute | ViewLocalInformation | LookupNames
    }
}
