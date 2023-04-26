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
      <tr>
         <td>
            <a href="https://profiles.ihe.net/ITI/BALP/StructureDefinition-IHE.BasicAudit.Read.html">Read</a>
         </td>
         <td>
            Performed when a resource has been read that is not a member of the Patient compartment. 
            Note that
            other extended operations which expose individual resource data may also trigger the creation of
            an AuditEvent with this profile. For example, the <code>$diff</code> operation exposes data within
            a resource, so it will also trigger this event.
         </td>
         <td>
            <a href="https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#STORAGE_PRESHOW_RESOURCES">STORAGE_PRESHOW_RESOURCES</a>
         </td>
      </tr>
      <tr>
         <td>
            <a href="https://profiles.ihe.net/ITI/BALP/StructureDefinition-IHE.BasicAudit.PatientRead.html">PatientRead</a>
         </td>
         <td>
            Performed when a resource has been read that is a member of the Patient compartment.
            Note that
            other extended operations which expose individual resource data may also trigger the creation of
            an AuditEvent with this profile. For example, the <code>$diff</code> operation exposes data within
            a resource, so it will also trigger this event.
         </td>
         <td>
            <a href="https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#STORAGE_PRESHOW_RESOURCES">STORAGE_PRESHOW_RESOURCES</a>
         </td>
      </tr>
      <tr>
         <td>
            <a href="https://profiles.ihe.net/ITI/BALP/StructureDefinition-IHE.BasicAudit.Update.html">Update</a>
         </td>
         <td>
            Performed when a resource has been updated that is not a member of the Patient compartment.
         </td>
         <td>
            <a href="https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#STORAGE_PRECOMMIT_RESOURCE_UPDATED">STORAGE_PRECOMMIT_RESOURCE_UPDATED</a>
         </td>
      </tr>
      <tr>
         <td>
            <a href="https://profiles.ihe.net/ITI/BALP/StructureDefinition-IHE.BasicAudit.PatientUpdate.html">PatientUpdate</a>
         </td>
         <td>
            Performed when a resource has been updated that is a member of the Patient compartment.
         </td>
         <td>
            <a href="https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#STORAGE_PRECOMMIT_RESOURCE_UPDATED">STORAGE_PRECOMMIT_RESOURCE_UPDATED</a>
         </td>
      </tr>
      <tr>
         <td>
            <a href="https://profiles.ihe.net/ITI/BALP/StructureDefinition-IHE.BasicAudit.Delete.html">Delete</a>
         </td>
         <td>
            Performed when a resource has been deleted that is not a member of the Patient compartment.
         </td>
         <td>
            <a href="https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#STORAGE_PRECOMMIT_RESOURCE_DELETED">STORAGE_PRECOMMIT_RESOURCE_DELETED</a>
         </td>
      </tr>
      <tr>
         <td>
            <a href="https://profiles.ihe.net/ITI/BALP/StructureDefinition-IHE.BasicAudit.PatientDelete.html">PatientDelete</a>
         </td>
         <td>
            Performed when a resource has been deleted that is a member of the Patient compartment.
         </td>
         <td>
            <a href="https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#STORAGE_PRECOMMIT_RESOURCE_DELETED">STORAGE_PRECOMMIT_RESOURCE_DELETED</a>
         </td>
      </tr>
      <tr>
         <td>
            <a href="https://profiles.ihe.net/ITI/BALP/StructureDefinition-IHE.BasicAudit.Query.html">Query</a>
         </td>
         <td>
            Performed when a non-patient-oriented search is performed. This refers to a search that is returning
            data that is not in a specific patient compartment.
         </td>
         <td>
            <a href="https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#STORAGE_PRESHOW_RESOURCES">STORAGE_PRESHOW_RESOURCES</a>
         </td>
      </tr>
      <tr>
         <td>
            <a href="https://profiles.ihe.net/ITI/BALP/StructureDefinition-IHE.BasicAudit.PatientQuery.html">PatientQuery</a>
         </td>
         <td>
            Performed when a patient-oriented search is performed. This refers to a search that returns data in
            a specific patient compartment.
         </td>
         <td>
            <a href="https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#STORAGE_PRESHOW_RESOURCES">STORAGE_PRESHOW_RESOURCES</a>
         </td>
      </tr>
   </tbody>
</table>

# Architecture

The HAPI FHIR BALP infrastructure consists of the following components:

* The [BalpAuditCaptureInterceptor](https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-storage/src/main/java/ca/uhn/fhir/jpa/interceptor/balp/BalpAuditCaptureInterceptor.html) is the primary interceptor 
