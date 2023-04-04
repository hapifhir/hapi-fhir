# Basic Audit Log Patterns (BALP) Interceptor

The IHE [Basic Audit Log Patterns](https://profiles.ihe.net/ITI/BALP/) implementation guide describes a set of workflows and data models for the creation of [AuditEvent](http://hl7.org/fhir/AuditEvent.html) resources based on user/client actions.

HAPI FHIR provides an interceptor that can be registered against a server, and will observe events on that server and automatically generate AuditEvent resources which are conformant to the appropriate profiles within the BALP specification.

This interceptor implements the following profiles:

<table class="table table-striped">
   <thead>
      <tr>
         <th>BALP Profile</th>
         <th>Trigger</th>
         <th>Triggering Pointcut</th>
      </tr>
   </thead>
   <tbody>
      <tr>
         <td>
            <a href="https://profiles.ihe.net/ITI/BALP/StructureDefinition-IHE.BasicAudit.Create.html">Create</a>
         </td>
         <td>
            Performed when a resource has been created that is not a member of the Patient compartment.
         </td>
         <td>
            <a href="https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#STORAGE_PRECOMMIT_RESOURCE_CREATED">STORAGE_PRECOMMIT_RESOURCE_CREATED</a>
         </td>
      </tr>
      <tr>
         <td>
            <a href="https://profiles.ihe.net/ITI/BALP/StructureDefinition-IHE.BasicAudit.PatientCreate.html">PatientCreate</a>
         </td>
         <td>
            Performed when a resource has been created that is a member of the Patient compartment.
         </td>
         <td>
            <a href="https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#STORAGE_PRECOMMIT_RESOURCE_CREATED">STORAGE_PRECOMMIT_RESOURCE_CREATED</a>
         </td>
      </tr>
   </tbody>
</table>

| BALP Profile | Trigger         | Triggering Pointcut |
|--------------|-----------------|---------------------|
| Create       | Triggered when  |                     |
|              |                 |                     |

