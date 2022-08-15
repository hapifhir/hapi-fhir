# Repository Validating Interceptor

When using a HAPI FHIR JPA server as a FHIR Repository, it is often desirable to enforce specific rules about which specific FHIR profiles can or must be used.

For example, if an organization has created a FHIR Repository for the purposes of hosting and serving [US Core](https://www.hl7.org/fhir/us/core/) data, that organization might want to enforce rules that data being stored in the repository must actually declare conformance to US Core Profiles such as the [US Core Patient Profile](https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-patient.html) and [US Core Observation Profile](https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-observation.html).

In this situation, it would also be useful to require that any data being stored in the repository be validated, and rejected if it is not valid.

The **RepositoryValidatingInterceptor** interceptor can be used to easily add this kind of rule to the server.

# Benefits and Limitations

For a HAPI FHIR JPA Server, the RepositoryValidatingInterceptor is a very powerful addition to the [Request and Response Validation](/docs/interceptors/built_in_server_interceptors.html#request_and_response_validation) that is also often used for validation. 

## Request and Response Validation

The *Request and Response Validation* interceptors examine incoming HTTP payloads (e.g. FHIR creates, FHIR updates, etc.) and apply the validator to them. This approach has its limitations: 

* It may miss validating data that is added or modified through Java API calls as opposed to through the HTTP endpoint.

* It may miss validating data that is added or modified through other interceptors

* It may provide you with a validated resource in your Java API so that you can make certain reasonable assumptions - eg. a required field does not need a null check. 

* It is not able to validate changes coming from operations such as FHIR Patch, since the patch itself may pass validation, but may ultimately result in modifying a resource so that it is no longer valid.

## Repository Validation

The *Repository Validating Interceptor* uses the direct storage pointcuts provided by the JPA Server in order to validate data exactly as it will appear in storage. In other words, no matter whether data is being written via the HTTP API or by an internal Java API call, the interceptor will catch it.

This means that:

* Repository validation applies to patch operations and validates the results of the resource after a patch is applied (but before the actual results are saved, in case the outcome of the validation operation should roll the operation back)

* Repository validation requires pointcuts that are thrown directly by the storage engine, meaning that it can not be used from a plain server unless the plain server code manually invokes the same pointcuts. 

* Repository validation does *NOT* provide your custom pre-storage business logical layer with any guarantees of the profile as the resource has not been hit by the proper pointcut. This means that you cannot make reasonable profile assumptions in your pre-storage logic handling the resource. 

# Using the Repository Validating Interceptor

Using the repository validating interceptor is as simple as creating a new instance
of [RepositoryValidatingInterceptor](/hapi-fhir/apidocs/hapi-fhir-storage/ca/uhn/fhir/jpa/interceptor/validation/RepositoryValidatingInterceptor.html)
and registering it with the interceptor registry. The only tricky part is initializing your rules, which must be done
using
a [RepositoryValidatingRuleBuilder](/hapi-fhir/apidocs/hapi-fhir-storage/ca/uhn/fhir/jpa/interceptor/validation/RepositoryValidatingRuleBuilder.html)
.

The rule builder must be obtained from the Spring context, as shown below:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RepositoryValidatingInterceptorExamples.java|createSimpleRule}}
```

# Rules: Require Profile Declarations

Use one of the following rule formats to require any resources submitted to the server to declare conformance to the given profiles. 

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RepositoryValidatingInterceptorExamples.java|requireProfileDeclarations}}
```

Note that this rule alone does not actually enforce validation against the specified profiles. It simply requires that any resources submitted to the server contain a declaration that they intend to conform. See the following example:

```json
{
   "resourceType": "Patient",
   "id": "123",
   "meta": {
      "profile": [
         "http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient"
      ]
   }
}
```

<a name="require-validation"/>

# Rules: Require Validation to Declared Profiles

Use the following rule to require that resources of the given type be validated successfully before allowing them to be persisted. For every resource of the given type that is submitted for storage, the `Resource.meta.profile` field will be examined and the resource will be validated against any declarations found there.

This rule is generally combined with the *Require Profile Declarations* above.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RepositoryValidatingInterceptorExamples.java|requireValidationToDeclaredProfiles}}
```

Any resource creates or updates that do not conform to the given profile will be rejected.

## Adjusting Failure Threshold

By default, any validation messages with a severity value of *ERROR* or *FATAL* will result in resource creates or updates being rejected. This threshold can be adjusted however:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RepositoryValidatingInterceptorExamples.java|requireValidationToDeclaredProfilesAdjustThreshold}}
```


## Tagging Validation Failures

By default, resource updates/changes resulting in failing validation will cause the operation to be rolled back. You can alternately configure the rule to allow the change to proceed but add an arbitrary tag to the resource when it is saved. 

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RepositoryValidatingInterceptorExamples.java|requireValidationToDeclaredProfilesTagOnFailure}}
```

## Configuring the Validator

The following snippet shows a number of additional optional settings that can be chained onto the validation rule. 

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RepositoryValidatingInterceptorExamples.java|requireValidationToDeclaredProfilesAdditionalOptions}}
```

# Rules: Disallow Specific Profiles

Rules can declare that a specific profile is not allowed.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RepositoryValidatingInterceptorExamples.java|disallowProfiles}}
```

# Adding Validation Outcome to HTTP Response

If you have included a [Require Validation](#require-validation) rule to your chain, you can add the `ValidationResultEnrichingInterceptor` to your server if you wish to have validation results added to and OperationOutcome objects that are returned by the server.
