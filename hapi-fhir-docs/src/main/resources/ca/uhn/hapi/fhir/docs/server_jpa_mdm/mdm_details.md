# MDM Implementation Details

This section describes details of how MDM functionality is implemented in HAPI FHIR.

## Patient linking in FHIR

Because HAPI MDM is implemented on the HAPI JPA Server, it uses the FHIR model to represent roles and links. The 
following illustration shows an example of how these links work.

<a href="/hapi-fhir/docs/images/empi-links.svg"><img src="/hapi-fhir/docs/images/empi-links.svg" alt="MDM links" style="margin-left: 15px; margin-bottom: 15px; width: 500px;" /></a>

There are several resources that are used:

* Patient - Represents the record of a person who receives healthcare services
* Golden Patient - Represents a master record with links to one or more Patient resources that belong to the same real-world patient

# Automatic Linking

With MDM enabled, the default behavior of the MDM is to create a new Golden Patient record for every Patient that is 
created such that there is a 1:1 relationship between them. Any relinking is then expected to be done manually via the 
[MDM Operations](/hapi-fhir/docs/server_jpa_mdm/mdm_operations.html).

In a typical configuration it is often desirable to have links be created automatically using matching rules. For example, 
you might decide that if a Patient shares the same name, gender, and date of birth as another Patient, you have at 
least a little confidence that they are the same Patient.

This automatic linking is done via configurable matching rules that create links among Patients. Based on the strength
 of the match configured in these rules, the link will be set to either POSSIBLE_MATCH or MATCH.

It is important to note that before a resource is processed by MDM, it is first checked to ensure that it has at least
 one attribute that the MDM system cares about, as defined in the `empi-rules.json` file. If the incoming resource has 
 no such attributes, then MDM processing does not occur on it. In this case, no Golden Resource Patient is created for 
 them. If in the future that Patient is updated to contain attributes the MDM system does concern itself with, it will 
 be processed at that time.

## Design

Below are some simplifying principles HAPI MDM follows to reduce complexity and ensure data integrity.

1. When MDM is enabled on a HAPI FHIR server, any Patient resource in the repository that has the "hapi-mdm" tag is
 considered read-only by the FHIR endpoint. These Golden Patient resources are managed exclusively by HAPI MDM. Users 
 can only change them via [MDM Operations](/hapi-fhir/docs/server_jpa_mdm/mdm_operations.html).  In most cases, users 
 will indirectly change them by creating and updating target Patient resources. For the rest of this document, assume
  "Golden Patient" refers to a "HAPI-MDM" tagged Patient resource.

1. Every Patient in the system has a MATCH link to at most one Golden Patient resource.

1. The only Patient resources in the system that do not have a MATCH link are those that have the 'NO-MDM' tag or 
those that have POSSIBLE_MATCH links pending review.

1. The HAPI MDM rules define a single identifier system that holds the external enterprise id ("EID"). If a Patient has 
an external EID, then the Golden Patient it links to always has the same EID. If a patient has no EID when it arrives, 
a unique UUID will be assigned as that Patient's EID.

1. A Golden Patient record can have both an internal EID (auto-created by HAPI), and an external EID (provided by an 
external system).

1. Two different Golden Patient resources cannot have the same EID.

1. Patient resources are only ever compared to Golden Patient resources via this EID. For all other matches, Patient 
resources are only ever compared to Patient resources.

## Links

1. HAPI MDM manages mdm-link records ("links") that link a Patient resource to a Golden Patient resource. When these are
 created/updated by matching rules, the links are marked as AUTO.  When these links are changed manually, they are 
 marked as MANUAL.

1. Once a link has been manually assigned as NO_MATCH or MATCH, the system will not change it.

1. When a new Patient resource is created/updated it is then compared to all other Patient resources in the repository. 
The outcome of each of these comparisons is either NO_MATCH, POSSIBLE_MATCH or MATCH.

1. HAPI MDM stores these extra link details in a table called `MPI_LINK`.

<!---
1. Whenever a MATCH link is established between a Patient resource and a Golden Patient resource, that Patient is always
 added to that Golden Patient resource links.  All MATCH links have corresponding Golden Patient resource links and all 
 Golden Patient resource links have corresponding MATCH mdm-link records. You can think of the fields of the mdm-link 
 records as extra meta-data associated with each Person.link.target.

1. Each record in the `MPI_LINK` table corresponds to a `link.target` entry on a Person resource unless it is a NO_MATCH record.  HAPI MDM uses the following convention for the Person.link.assurance level:
    1. Level 1: POSSIBLE_MATCH
    1. Level 2: AUTO MATCH
    1. Level 3: MANUAL MATCH
    1. Level 4: GOLDEN RECORD
-->

### Possible rule match outcomes:

When a new Patient resource is compared with all other resources of that type in the repository, there are four possible outcomes:

<!---
All fields are copied from the Patient to the Golden Patient.
-->

* CASE 1: No MATCH and no POSSIBLE_MATCH outcomes -> a new Golden Patient resource is created and linked to that Patient as MATCH.
  If the incoming resource has an EID, it is copied to the Golden Patient. Otherwise a new UUID is generated and used as the internal EID.

* CASE 2: All of the MATCH Patient resources are already linked to the same Golden Patient -> a new Link is created between the new Patient and that Golden Patient and is set to MATCH.

* CASE 3: The MATCH Patient resources link to more than one Golden Patient -> Mark all links as POSSIBLE_MATCH.  All other Golden Patient resources are marked 
as POSSIBLE_DUPLICATE of this first Golden Patient. These duplicates are manually reviewed later and either merged or marked as NO_MATCH and the system will 
no longer consider them as a POSSIBLE_DUPLICATE going forward. POSSIBLE_DUPLICATE is the only link type that can have a Golden Patient as both the source and target of the link.

* CASE 4: Only POSSIBLE_MATCH outcomes -> In this case, new POSSIBLE_MATCH links are created and await manual reassignment to either NO_MATCH or MATCH.

# HAPI MDM Technical Details

When MDM is enabled, the HAPI FHIR JPA Server does the following things on startup:

1. It enables the MESSAGE subscription type and starts up the internal subscription engine.
1. It creates two MESSAGE subscriptions, called 'empi-patient' and 'empi-practitioner' that match all incoming Patient and Practitioner resources and send them to an internal queue called "empi".  The JPA Server listens to this queue and links incoming resources to Persons.
1. The [MDM Operations](/hapi-fhir/docs/server_jpa_mdm/mdm_operations.html) are registered with the server.
1. It registers a new dao interceptor that restricts access to MDM managed Golden Patient records.
