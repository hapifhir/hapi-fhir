# EMPI Implementation Details

This section describes details of how EMPI functionality is implemented in HAPI FHIR.

## Person linking in FHIR

Because HAPI EMPI is implemented on the HAPI JPA Server, it uses the FHIR model to represent roles and links. The following illustration shows an example of how these links work.

<a href="/hapi-fhir/docs/images/empi-links.svg"><img src="/hapi-fhir/docs/images/empi-links.svg" alt="EMPI links" style="margin-left: 15px; margin-bottom: 15px; width: 500px;" /></a>

There are several resources that are used:

* Patient - Represents the record of a person who receives healthcare services
* Person - Represents a master record with links to one or more Patient and/or Practitioner resources that belong to the same person

# Automatic Linking

With EMPI enabled, the basic default behavior of the EMPI is simply to create a new Person record for every Patient that is created such that there is a 1:1 relationship between them. Any relinking is then expected to be done manually (i.e. via the forthcoming empi operations).

In a typical configuration it is often desirable to have links be created automatically using matching rules. For example, you might decide that if a Patient shares the same name, gender, and date of birth as another Patient, you have at least a little confidence that they are the same Person.

This automatic linking is done via configurable matching rules that create links between Patients and Persons.  Based on the strength of the match configured in these rules, the link will be set to either POSSIBLE_MATCH or MATCH.

It is important to note that before a resource is to be processed by EMPI, it is first checked to ensure that it has at least one attribute that the EMPI system cares about, as defined in the `empi-rules.json` file. If the incoming resource has no attributes that the EMPI system cares about, EMPI processing does not occur on it. In this case, no Person is created for them. If in the future that Patient is updated to contain attributes the EMPI system does concern itself with, it will be processed at that time. 
## Design

Below are some simplifying principles HAPI EMPI enforces to reduce complexity and ensure data integrity.

1. When EMPI is enabled on a HAPI FHIR server, any Person resource in the repository that has the "hapi-empi" tag is considered read-only via the FHIR endpoint.  These Person resources are managed exclusively by HAPI EMPI.  Users can only directly change them via special empi operations.  In most cases, users will indirectly change them by creating and updating Patient and Practitioner ("Patient") resources.  For the rest of this document, assume "Person" refers to a "hapi-empi" tagged Person resource.

1. Every Patient in the system has a MATCH link to at most one Person resource.

1. Every Patient resource in the system has a MATCH link to a Person resource unless that Patient has the "no-empi" tag or it has POSSIBLE_MATCH links pending review.

1. The HAPI EMPI rules define a single identifier system that holds the external enterprise id ("EID").  If a Patient has an external EID, then the Person it links to always has the same EID. If a patient has no EID when it arrives, the person created from this patient is given an internal EID.

1. A Person can have both an internal EID(auto-created by HAPI), and an external EID (provided by an external system).

1. Two different Person resources cannot have the same EID.

1. Patient resources are only ever compared to Person resources via this EID.  For all other matches, Patient resources are only ever compared to Patient resources and Practitioner resources are only ever compared to Practitioner resources.

## Links

1. HAPI EMPI manages empi-link records ("links") that link a Patient resource to a Person resource.  When these are created/updated by matching rules, the links are marked as AUTO.  When these links are changed manually, they are marked as MANUAL.

1. Once a link has been manually assigned as NO_MATCH or MATCH, the system will not change it.

1. When a new Patient resource is created/updated it is then compared to all other Patient resources in the repository.  The outcome of each of these comparisons is either NO_MATCH, POSSIBLE_MATCH or MATCH.

1. Whenever a MATCH link is established between a Patient resource and a Person resource, that Patient is always added to that Person resource links.  All MATCH links have corresponding Person resource links and all Person resource links have corresponding MATCH empi-link records.  You can think of the fields of the empi-link records as extra meta-data associated with each Person.link.target.

1. HAPI EMPI stores these extra link details in a table called `MPI_LINK`.

1. Each record in the `MPI_LINK` table corresponds to a `link.target` entry on a Person resource unless it is a NO_MATCH record.  HAPI EMPI uses the following convention for the Person.link.assurance level:
    1. Level 1: POSSIBLE_MATCH
    1. Level 2: AUTO MATCH
    1. Level 3: MANUAL MATCH
    1. Level 4: GOLDEN RECORD

### Possible rule match outcomes:

When a new Patient resource is compared with all other resources of that type in the repository, there are four possible cases:

* CASE 1: No MATCH and no POSSIBLE_MATCH outcomes -> a new Person resource is created and linked to that Patient as MATCH.  All fields are copied from the Patient to the Person.  If the incoming resource has an EID, it is copied to the Person.  Otherwise a new UUID is created and used as the internal EID.

* CASE 2: All of the MATCH Patient resources are already linked to the same Person -> a new Link is created between the new Patient and that Person and is set to MATCH.

* CASE 3: The MATCH Patient resources link to more than one Person -> Mark all links as POSSIBLE_MATCH.  All other Person resources are marked as POSSIBLE_DUPLICATE of this first Person.  These duplicates are manually reviewed later and either merged or marked as NO_MATCH and the system will no longer consider them as a POSSIBLE_DUPLICATE going forward. POSSIBLE_DUPLICATE is the only link type that can have a Person as both the source and target of the link.

* CASE 4: Only POSSIBLE_MATCH outcomes -> In this case, new POSSIBLE_MATCH links are created and await manual assignment to either NO_MATCH or MATCH.

# HAPI EMPI Technical Details

When EMPI is enabled, the HAPI FHIR JPA Server does the following things on startup:

1. It enables the MESSAGE subscription type and starts up the internal subscription engine.
1. It creates two MESSAGE subscriptions, called 'empi-patient' and 'empi-practitioner' that match all incoming Patient and Practitioner resources and send them to an internal queue called "empi".  The JPA Server listens to this queue and links incoming resources to Persons.
1. It registers the `Patient/$match` operation.  See [$match](https://www.hl7.org/fhir/operation-patient-match.html) for a description of this operation.
1. It registers a new dao interceptor that restricts access to EMPI managed Person records.
