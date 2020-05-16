# Enabling EMPI in HAPI FHIR

Follow these steps to enable EMPI on the server:

The [EmpiSettings](/hapi-fhir/apidocs/hapi-fhir-server-empi/ca/uhn/fhir/empi/rules/config/EmpiSettings.html) bean contains configuration settings related to EMPI within the server. To enable Empi, the [setEnabled(boolean)](/hapi-fhir/apidocs/hapi-fhir-server-empi/ca/uhn/fhir/empi/rules/config/EmpiSettings.html#setEnabled(boolean)) property should be enabled.

The following settings can be enabled:

* **Prevent EID Updates** ([JavaDoc](/hapi-fhir/apidocs/hapi-fhir-server-empi/ca/uhn/fhir/empi/rules/config/EmpiSettings.html#setPreventEidUpdates(boolean))): If this is enabled, then once an EID is set on a resource, it cannot be changed.

* **Prevent multiple EIDs**: ([JavaDoc](/hapi-fhir/apidocs/hapi-fhir-server-empi/ca/uhn/fhir/empi/rules/config/EmpiSettings.html#setPreventMultipleEids(boolean))): If this is enabled, then a resource cannot have more than one EID.
