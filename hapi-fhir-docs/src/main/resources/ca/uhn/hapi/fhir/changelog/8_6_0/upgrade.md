## Changes to package installation

A change has been made to package installation where any client-assigned ID included in the resource being installed will be ignored and replaced with a server-assigned ID.
Only SearchParameter resources will continue to retain provided client-assigned IDs.
This change has been made to ensure that multiple versions of a conformance resource do not overwrite each other in the FHIR resource tables.
See [the HAPI-FHIR issue](https://github.com/hapifhir/hapi-fhir/issues/7235) for more information.
