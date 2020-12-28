# Repository Validating Interceptor

When using a HAPI FHIR JPA server as a FHIR Repository, it is often desirable to enforce specific rules about which specific FHIR profiles can or must be used.

For example, if an organization has created a FHIR Repository for the purposes of hosting and serving [US Core](https://www.hl7.org/fhir/us/core/) data, that organization might want to enforce rules that data being stored in the repository must actually declare conformance to US Core Profiles such as the [US Core Patient Profile](https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-patient.html) and [US Core Observation Profile](https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-observation.html).

In this situation, it would also be useful to require that any data being stored in the repository be validated, and rejected if it is not valid.

The **RepositoryValidatingInterceptor** interceptor can be used to easily add this kind of rule to the server.

# Benefits

For a HAPI FHIR JPA Server, the RepositoryValidatingInterceptor is a very powerful option compared to the [Request and Response Validation](/docs/interceptors/built_in_server_interceptors.html#request_and_response_validation) that is also often used for validation. 

The *Request and Response Validation* interceptors examine incoming HTTP payloads (e.g. FHIR creates, FHIR updates, etc.) and apply the validator to them. This approach has its limitations: 

* It may miss validating data that is added or modified through Java API calls as opposed to through the HTTP endpoint.

* It may miss validating data that is added or modified through other interceptors

* It is not able to validate changes coming from operations such as FHIR Patch, since the patch itself may pass validation, but may ultimately result in modifying a resource so that it is no longer valid.

The RepositoryValidatingInterceptor uses the direct storage pointcuts provided by the JPA Server in order to validate data exactly as it will appear in storage.

# Using the Repository Validating Interceptor

