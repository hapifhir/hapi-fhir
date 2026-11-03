# Upgrade Notes

## Code System Name After a SNAPSHOT Terminology Upload

From HAPI FHIR 8.12.0, a SNAPSHOT terminology upload (for example a custom terminology upload) that replaced a
code system URL and version that was already stored could store the code system URL as the name of the new
version, so `CodeSystem/$lookup` returned the URL as `name`. This release stores the `CodeSystem.name` from the
upload instead.

Versions that were loaded this way keep the URL as their name after the upgrade. If `CodeSystem/$lookup` returns
the code system URL as `name`, upload that code system again as a SNAPSHOT after upgrading. An ADD or REMOVE delta
upload does not change the name of the version it updates.

`ITermCodeSystemStorageSvc#startStagingCodeSystemVersion(String, String)` is deprecated. Use
`startStagingCodeSystemVersion(String, String, String)`, which takes the display name of the staging version as a
third parameter; `null` keeps the name already stored for the code system URL.
