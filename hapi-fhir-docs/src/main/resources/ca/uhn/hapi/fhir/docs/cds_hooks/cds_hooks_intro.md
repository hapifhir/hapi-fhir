# CDS Hooks

CDS Hooks are services called by CDS Clients (typically Electronic Health Record Systems (EHRs) or other health
information systems). They implement a "hook"-based pattern for invoking decision support from within a clinician's
workflow.

HAPI FHIR implements [Version 1.1 of the CDS Hooks Specification](https://cds-hooks.hl7.org/ballots/2020Sep/).

The HAPI FHIR CDS Hooks Module simplifies the effort for creating CDS Hooks. All you need to do is create a method that
accepts a `CdsServiceRequestJson` parameter and returns a `CdsServiceResponseJson` value and annotate this method with
the `@CdsService` annotation. This annotation and the Json classes and all their subcomponents are available in the
open-source project called `hapi-fhir-server-cds-hooks`. Any FHIR resources in requests and responses are automatically serialized
into hapi-fhir FHIR resource instances for you, so they are easy to work with within your code.

In addition to simplifying the effort to build CDS Hooks, the HAPI FHIR CDS Hooks module also provides the following:

* All access is logged in the HAPI FHIR Audit Trail.
* Authorization is controlled by the HAPI FHIR security framework.
* Management and monitoring capabilities are provided by the HAPI FHIR platform.
* [CDS on FHIR](/docs/cds_hooks/#cds-on-fhir) implementation that auto-generates CDS Services from PlanDefinitions and executes via the $apply operation.

# Auto Prefetch

The HAPI FHIR CDS Hooks module provides a couple of powerful Auto-Prefetch features:

1. If `allowAutoFhirClientPrefetch` is set to `true` in the `@CdsService` annotation on your CDS Service method, then
   before calling your method, HAPI FHIR will compare the prefetch elements declared by your service method in
   the `@CdsService` annotation to the prefetch elements included within the `CdsServiceRequestJson` REST request and if
   it detects any are missing, then HAPI FHIR will use the FHIR endpoint authorization details included within
   the `fhirAuthorization` element in the request to automatically add them to the prefetch before calling your method.
2. Even simpler, if your HAPI FHIR server has a FHIR Storage module, you can optionally add a dependency from your
   CDS Hooks Module on your FHIR Storage module. If you do this, then when HAPI FHIR detects any required prefetch
   elements missing in a request, it will automatically fetch the missing data from your storage module before calling
   your CDS Hooks method. Note in this case, the same credentials used to call the CDS Hooks endpoint are used to
   authorize access to the FHIR Storage module.

## CDS Hooks Auto Prefetch Rules

- If there are no missing prefetch elements, the CDS Hooks service method is called directly with the request.  (Note
  that per the CDS Hooks specification, a value of `null` is not considered to be missing. CDS Hooks clients set a
  prefetch value to `null` to indicate that this prefetch data is known to not exist).
- Otherwise, if a `fhirServer` is included in the request
   - If the `@CdsService` annotation on the service method has `allowAutoFhirClientPrefetch = true`, then HAPI FHIR will
     perform a FHIR REST call to that `fhirServer` endpoint to fetch the missing data.
   - otherwise, the CDS Hooks service method is expected to call the `fhirServer` endpoint itself to retrieve the
     missing data.
- Otherwise, if the CDS Hooks Module declares a dependency on a FHIR Storage Module, then HAPI FHIR will fetch the
  missing data from that FHIR Storage Module.
- Otherwise, the method will fail with HTTP 412 PRECONDITION FAILED (per the CDS Hooks specification).
- The Auto-Prefetch rules can be overridden for individual elements by setting a `source` for the `@CdsServicePrefetch`. 
  HAPI FHIR will attempt to use the `source` strategy for the query instead of following the order above.

# Architecture

The diagram below shows how CDS Hooks work. The box in grey contains *customer code*, which is code that you write.

<img src="/docs/images/cds_hooks.svg" alt="CDS Hooks Architecture" style="width: 1120px;"/>

A CDS Hooks implementation is packaged as a Java JAR file that contains several key components:

* **CDS Service** classes, which implement CDS Hooks *service* and *feedback* methods.
* A **Spring Context Config** class, which is a Spring Framework class used to instantiate and configure the CDS Hooks
  classes.

# CDS Hooks Classes

A CDS Hooks class contains annotated *service* and *feedback* methods. One CDS Hooks class can contain any number of
these methods. A CDS Hooks *service* method is annotated with the `@CdsService` annotation and a CDS Hooks *feedback*
method is annotated with the `@CdsServiceFeedback` annotation. The "value" of these annotations corresponds to the id of
the CDS Hooks service. For example:

A method annotated with `@CdsService(value="example-service")` is accessed at a path
like `https://example.com:8888/cds-services/example-service`

A method annotated with `@CdsServiceFeedback(value="my-service")` is accessed at a path
like `https://example.com:8888/cds-services/my-service/feedback`.

A very basic example is shown below:

```java
{{snippet:file:hapi-fhir-server-cds-hooks/src/test/java/ca.uhn.hapi.fhir.cdshooks/controller/ExampleCdsService.java}}
```

Both of these example methods accept a single json instance parameter (`CdsServiceRequestJson`
and `CdsServiceFeedbackJson` respectively). Alternatively, these methods can accept a single String parameter in which
case the CDS Hooks module will string-encode the instance before calling the method.

# The Spring Context Config Class

This mandatory class is a [Spring Framework](https://springframework.org) Annotation-based Application Context Config
class. It is characterized by having the `@Configuration` annotation on the class itself, as well as having one or more
non-static factory methods annotated with the `@Bean` method, which create instances of your providers (as well as
creating any other utility classes you might need, such as database pools, HTTP clients, etc.).

This class must instantiate a bean named `cdsServices`:

* The `cdsServices` bean method should return a `List<Object>` of classes that contain `@CdsService`
  and/or `@CdsServiceFeedback` annotated methods.

The following example shows a Spring Context Config class that registers the CDS Hooks example above.

```java
@Configuration
public class TestServerAppCtx {

   /**
    * This bean is a list of CDS Hooks classes, each one
    * of which implements one or more CDS-Hook Services.
    */
   @Bean(name = "cdsServices")
   public List<Object> cdsServices(){
      List<Object> retVal = new ArrayList<>();
      retVal.add(new ExampleCdsService());
// add other CDS Hooks classes...
      return retVal;
   }
}
```

# Calling CDS Hooks

Per [Version 1.1 of the CDS Hooks Specification](https://cds-hooks.hl7.org/ballots/2020Sep/), a list of all registered
services is available at a path like `https://example.com:8888/cds-services`. As a convenience, swagger REST
documentation is provided at the root of the endpoint: `https://example.com:8888/`.

# Example Project

A sample CDS Hooks project is available at the following links:

* [cdr-endpoint-cds-hooks-demoproject-1.0.zip](/docs/downloads/cdr-endpoint-cds-hooks-demoproject-1.0.zip)
* [cdr-endpoint-cds-hooks-demoproject-1.0.tar.gz](/docs/downloads/cdr-endpoint-cds-hooks-demoproject-1.0.tar.gz)

# CDS on FHIR

To create CDS Services from PlanDefinitions the dependencies for a FHIR Storage Module, FHIR Endpoint and CQL module must be set.  This will create a listener on the storage module so that any changes to PlanDefinition resources will update the CDS Service cache.

Any PlanDefinition resource with an action that has a trigger of type [named-event](http://hl7.org/fhir/R4/codesystem-trigger-type.html#trigger-type-named-event) will have a CDS Service created using the PlanDefinition.id as the service id and the name of the trigger as the hook that the service is created for per the [CDS on FHIR Specification](https://hl7.org/fhir/clinicalreasoning-cds-on-fhir.html#surfacing-clinical-decision-support).

CDS Services created this way will show up as registered services and can be called just as other services are called. The CDS Service request will be converted into parameters for the [$apply operation](/docs/clinical_reasoning/plan_definitions.html#apply), the results of which are then converted into a CDS Response per the [CDS on FHIR Specification](https://hl7.org/fhir/clinicalreasoning-cds-on-fhir.html#consuming-decision-support).

These CDS Services will take advantage of the [Auto Prefetch](/docs/cds_hooks/#auto-prefetch) feature.  Prefetch data is included as a Bundle in the `data` parameter of the $apply call.

The $apply operation is running against the FHIR Storage Module, so it will also have access to any data stored there.  Any CQL evaluation during the $apply operation that results in a retrieve will always pull from the Bundle and the FHIR Storage Module.  This is done regardless of what data is passed into the prefetch of the service request. 
