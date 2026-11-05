# Upgrade Notes

## Code System Name After a SNAPSHOT Terminology Upload

From HAPI FHIR 8.12.0, a SNAPSHOT terminology upload (for example a custom terminology upload) that replaced a
code system URL and version that was already stored could store the code system URL as the name of the new
version, so `CodeSystem/$lookup` returned the URL as `name`. This release stores the `CodeSystem.name` from the
upload instead.

Versions that were loaded this way keep the URL as their name after the upgrade. If `CodeSystem/$lookup` returns
the code system URL as `name`, upload that code system again as a SNAPSHOT after upgrading. An ADD or REMOVE delta
upload does not change the name of the version it updates.

`ITermCodeSystemStorageSvc` has a new method, `startStagingCodeSystemVersion(String, String, String)`, which takes
the display name of the staging version as a third parameter; `null` keeps the name already stored for the code system
URL. Its default implementation calls `startStagingCodeSystemVersion(String, String)` and ignores the name, so an
implementation that overrides only the two-argument method keeps working. Override the new method to store the uploaded
`CodeSystem.name`.

## Conditional References Outside a Transaction

This release updates org.hl7.fhir.core to 6.10.4. The validator now reports a conditional reference, such as
`Patient?identifier=http://example.org|123`, as an error (`REFERENCE_REF_QUERY_NOT_TRANSACTION`) unless the
resource is inside a transaction Bundle, as the FHIR specification requires.

HAPI FHIR JPA servers also resolve conditional references on a plain create or update when inline match URL
references are allowed (`StorageSettings#setAllowInlineMatchUrlReferences`, enabled by default). If such a server
validates requests, for example with the `RequestValidatingInterceptor`, those requests are now rejected. To accept
them again, register the new `ValidationMessageConditionalReferencePostProcessingInterceptor`, which reports these
references as warnings. See
[Modifying Validation Message Severity Via Interceptor](/hapi-fhir/docs/validation/instance_validator.html#validation-message-post-processing-interceptor).
