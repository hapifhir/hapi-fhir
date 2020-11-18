# MDM Enterprise Identifiers

An Enterprise Identifier (EID) is a unique identifier that can be attached to target resources. Each implementation is 
expected to use exactly one EID system for incoming resources, defined in the MDM Rules file. If a Patient with a valid 
EID is submitted, that EID will be copied over to the Golden Patient resource that was matched. In the case that the 
incoming Patient had no EID assigned, an internal EID will be created for it. There are thus two classes of EID:
 * Internal EIDs, created by HAPI-MDM, and 
 * External EIDs, provided by the submitted resources.

## MDM EID Settings

The [EmpiSettings](/hapi-fhir/apidocs/hapi-fhir-server-empi/ca/uhn/fhir/empi/rules/config/EmpiSettings.html) bean 
contains two EID related settings.  Both are enabled by default.

* **Prevent EID Updates** ([JavaDoc](/hapi-fhir/apidocs/hapi-fhir-server-empi/ca/uhn/fhir/empi/rules/config/EmpiSettings.html#setPreventEidUpdates(boolean))): If this is enabled, then once an EID is set on a resource, it cannot be changed. If disabled, patients may have their EID updated.

* **Prevent multiple EIDs**: ([JavaDoc](/hapi-fhir/apidocs/hapi-fhir-server-empi/ca/uhn/fhir/empi/rules/config/EmpiSettings.html#setPreventMultipleEids(boolean))): If this is enabled, then a resource cannot have more than one EID, and incoming resources that break this rule will be rejected.

## MDM EID Scenarios

MDM EID management follows a complex set of rules to link related Patient records via their Enterprise Id.  The following 
diagrams outline how EIDs are replicated from Patient resources to their linked Golden Patient resources under various
 scenarios according to the values of the EID Settings.

## MDM EID Create Scenarios

<a href="/hapi-fhir/docs/images/empi-create-1.svg"><img src="/hapi-fhir/docs/images/empi-create-1.svg" alt="MDM Create 1" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-create-2.svg"><img src="/hapi-fhir/docs/images/empi-create-2.svg" alt="MDM Create 1" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-create-3.svg"><img src="/hapi-fhir/docs/images/empi-create-3.svg" alt="MDM Create 1" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-create-4.svg"><img src="/hapi-fhir/docs/images/empi-create-4.svg" alt="MDM Create 1" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-create-5.svg"><img src="/hapi-fhir/docs/images/empi-create-5.svg" alt="MDM Create 1" style="margin-left: 15px; margin-bottom: 15px;" /></a>

## MDM EID Update Scenarios

<a href="/hapi-fhir/docs/images/empi-update-1.svg"><img src="/hapi-fhir/docs/images/empi-update-1.svg" alt="MDM Update 1" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-update-2.svg"><img src="/hapi-fhir/docs/images/empi-update-2.svg" alt="MDM Update 2" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-update-3.svg"><img src="/hapi-fhir/docs/images/empi-update-3.svg" alt="MDM Update 3" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-update-4.svg"><img src="/hapi-fhir/docs/images/empi-update-4.svg" alt="MDM Update 4" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-update-5.svg"><img src="/hapi-fhir/docs/images/empi-update-5.svg" alt="MDM Update 5" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-update-6.svg"><img src="/hapi-fhir/docs/images/empi-update-6.svg" alt="MDM Update 6" style="margin-left: 15px; margin-bottom: 15px;" /></a>

