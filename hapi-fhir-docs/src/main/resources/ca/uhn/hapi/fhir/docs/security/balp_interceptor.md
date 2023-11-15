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

* The [BalpAuditCaptureInterceptor](https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-storage/src/main/java/ca/uhn/fhir/jpa/interceptor/balp/BalpAuditCaptureInterceptor.html) is the primary interceptor, which you register against a HAPI FHIR [Plain Server](../server_plain/).
* The [IBalpAuditEventSink](https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-storage/src/main/java/ca/uhn/fhir/jpa/interceptor/balp/IBalpAuditEventSink.html) is an interface which receives generated AuditEvents and processes them. Appropriate processing will depend on your use case, but could be storing them locally, transmitting them to a remote server, logging them to a syslog, or even selectively dropping them. See [Audit Event Sink](#audit-event-sink) below.
* The [IBalpAuditContextServices](https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-storage/src/main/java/ca/uhn/fhir/jpa/interceptor/balp/IBalpAuditContextServices.html) is an interface which supplies context information for a given client action. When generating a BALP conformant AuditEvent resource, the BalpAuditCaptureInterceptor will automatically populate most of the AuditEvent with details such as the _entity_ (ie. the resource being accessed or modified) and the _server_ (the FHIR server being used to transmit or store the information). However, other information such as the agent and the user (ie. the FHIR client and the physical user) are not known to HAPI FHIR and must be supplied for each request. This interface supplies these details.

<a name="audit-event-sink"/>

# Audit Event Sink

The BALP [IBalpAuditEventSink](https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-storage/src/main/java/ca/uhn/fhir/jpa/interceptor/balp/IBalpAuditEventSink.html) receives and handles generated audit events.

This interface is designed to support custom implementations, so you can absolutely create your own. HAPI FHIR ships with the following implementation: 

* [AsyncMemoryQueueBackedFhirClientBalpSink](https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-storage/src/main/java/ca/uhn/fhir/jpa/interceptor/balp/AsyncMemoryQueueBackedFhirClientBalpSink.html) uses an HTTP/REST FHIR client to transmit AuditEvents to a FHIR server endpoint. This can be a local or a remote endpoint, and can be a server with any version of FHIR. Messages are transmitted asynchronously using an in-memory queue.

If you create an implementation of this interface that you think would be useful to others, we would welcome community contributions!

<a name="audit-context-services"/>

# Audit Context Services

In order to use this interceptor, you must suply an instance of [IBalpAuditContextServices](https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-storage/src/main/java/ca/uhn/fhir/jpa/interceptor/balp/IBalpAuditContextServices.html). This interface supplies the information about each request that the interceptor cannot determine on its own, such as the identity of the requesting user and the requesting client.

The implementation of this interface for the [public HAPI FHIR server](https://hapi.fhir.org) is available [here](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-jpaserver-uhnfhirtest/src/main/java/ca/uhn/fhirtest/config/FhirTestBalpAuditContextServices.java).

# Example

The following example shows a simple implementation of the Context Services: 

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/BalpExample.java|contextService}}
```

And the following example shows a HAPI FHIR Basic Server with the BALP interceptor wired in:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/BalpExample.java|server}}
```
