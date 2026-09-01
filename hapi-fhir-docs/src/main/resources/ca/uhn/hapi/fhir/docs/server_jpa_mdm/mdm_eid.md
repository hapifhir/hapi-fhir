# MDM Enterprise Identifiers

An Enterprise Identifier (EID) is an identifier that can be attached to source resources and that, within
the system that issued it, names exactly one real-world entity. The EID systems an implementation uses are
defined per resource type in the MDM Rules file, under
[eidSystems](/hapi-fhir/docs/server_jpa_mdm/mdm_rules.html#eidsystems).
If a source resource with a valid EID is submitted, that EID will be copied over to the Golden Resource that was matched.

A resource type may be identified by a single EID system or by several. Where several are configured, a
resource may carry one EID from each of them - a medical record number and a national provider identifier,
say. These are not competing identities: each one on its own identifies the entity, so a match on **any**
of them is enough to link the resource, and every matching EID is copied to the Golden Resource.
What a resource is not normally allowed to carry is two EIDs issued by the *same* system, since that would
make its identity within that system ambiguous. That is enforced by the **Prevent multiple EIDs** setting
described below, which is enabled by default but may be turned off.

## How a Golden Resource Accumulates EIDs

A Golden Resource holds the EIDs of the source resources linked to it, and gains them as those resources
arrive. A Golden Resource created from a resource carrying only a medical record number acquires the
national provider identifier as soon as a resource carrying both is matched to it - and from that point a
later resource carrying only the national provider identifier resolves to the same Golden Resource. This
applies on create and on update alike.

<p class="helpInfoCalloutBox">
    There is one exception. Where the Golden Resource already holds an EID from a given system, an incoming EID from that <i>same</i> system with a different value is not copied while <b>Prevent multiple EIDs</b> is enabled, since the Golden Resource would then be ambiguous within that system. Such an EID is a contradiction between two issuing authorities rather than new information; it is recorded in the MDM transaction log so that it can be found, but no link or duplicate is raised for it.
</p>

## MDM EID Settings

The [MdmSettings](/hapi-fhir/apidocs/hapi-fhir-server-mdm/ca/uhn/fhir/mdm/rules/config/MdmSettings.html) bean 
contains two EID related settings.  Both are enabled by default.

* **Prevent EID Updates** ([JavaDoc](/hapi-fhir/apidocs/hapi-fhir-server-mdm/ca/uhn/fhir/mdm/rules/config/MdmSettings.html#setPreventEidUpdates(boolean))): If this is enabled, then once an EID is set on a resource, it cannot be changed. If disabled, patients may have their EID updated.

* **Prevent multiple EIDs**: ([JavaDoc](/hapi-fhir/apidocs/hapi-fhir-server-mdm/ca/uhn/fhir/mdm/rules/config/MdmSettings.html#setPreventMultipleEids(boolean))): If this is enabled, then a resource cannot have more than one EID from any single EID system, and incoming resources that break this rule will be rejected. Where several EID systems are configured for a resource type, a resource may carry one EID from each of them; what it may not carry is two EIDs issued by the same system.

<p class="helpInfoCalloutBox">
    <b>Prevent EID Updates</b> is applied per EID system: every EID the resource had before must still be present, so an EID belonging to one system may not be changed or removed even where an EID from another system is left in place. Gaining an EID from a system the resource did not previously use is an addition rather than an update, and remains permitted - it is how a record acquires its second identifier.
</p>

## Matching on Several EID Systems

Where a resource type is identified by more than one EID system, an incoming resource may carry EIDs that
have already been assigned to *different* Golden Resources - typically because the records arrived
separately, before anything indicated that both identifiers belonged to the same real-world person.

In that case MDM does not guess which Golden Resource is correct. It links the incoming resource to each
of them as a POSSIBLE_MATCH, and records a POSSIBLE_DUPLICATE link between the Golden Resources
themselves, so that a data steward can review and, if appropriate, merge them.

## MDM EID Scenarios

MDM EID management follows a complex set of rules to link related source records via their Enterprise Id.  The following diagrams outline how EIDs are replicated from Patient resources to their linked Golden Patient resources under various scenarios according to the values of the EID Settings.

## MDM EID Create Scenarios

<a href="/hapi-fhir/docs/images/empi-create-1.svg"><img src="/hapi-fhir/docs/images/empi-create-1.svg" alt="MDM Create 1" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-create-2.svg"><img src="/hapi-fhir/docs/images/empi-create-2.svg" alt="MDM Create 2" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-create-3.svg"><img src="/hapi-fhir/docs/images/empi-create-3.svg" alt="MDM Create 3" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-create-4.svg"><img src="/hapi-fhir/docs/images/empi-create-4.svg" alt="MDM Create 4" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-create-5.svg"><img src="/hapi-fhir/docs/images/empi-create-5.svg" alt="MDM Create 5" style="margin-left: 15px; margin-bottom: 15px;" /></a>

## MDM EID Update Scenarios

<a href="/hapi-fhir/docs/images/empi-update-1.svg"><img src="/hapi-fhir/docs/images/empi-update-1.svg" alt="MDM Update 1" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-update-2.svg"><img src="/hapi-fhir/docs/images/empi-update-2.svg" alt="MDM Update 2" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-update-3.svg"><img src="/hapi-fhir/docs/images/empi-update-3.svg" alt="MDM Update 3" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-update-4.svg"><img src="/hapi-fhir/docs/images/empi-update-4.svg" alt="MDM Update 4" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-update-5.svg"><img src="/hapi-fhir/docs/images/empi-update-5.svg" alt="MDM Update 5" style="margin-left: 15px; margin-bottom: 15px;" /></a>

<a href="/hapi-fhir/docs/images/empi-update-6.svg"><img src="/hapi-fhir/docs/images/empi-update-6.svg" alt="MDM Update 6" style="margin-left: 15px; margin-bottom: 15px;" /></a>

