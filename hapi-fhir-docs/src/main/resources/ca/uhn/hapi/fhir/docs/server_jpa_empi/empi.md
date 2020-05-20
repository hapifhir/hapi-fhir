# Enterprise Master Person Index (EMPI)

HAPI FHIR 5.0.0 introduced preliminary support for **EMPI**.

An EMPI allows for links to be created and maintained between different Patient and/or Practitioner resources. These links are used to indicate the fact that different Patient/Practitioner resources are known or believed to refer to the same actual (real world) person.

These links may be created and updated using different combinations of automatic linking as well as manual linking.

Note: The following sections describe linking between Patient and Person resources. The same information applies for linking between Practitioner and Person, but for readability it is not repeated.

## Working Example

The [hapi-fhir-jpaserver-starter](https://github.com/hapifhir/hapi-fhir-jpaserver-starter) project contains a complete working example of the HAPI EMPI feature and documentation about how to enable and configure it. You may wish to browse its source to see how this works.

## Person linking in FHIR

Because HAPI EMPI is implemented on the HAPI JPA Server, it uses the FHIR model to represent roles and links. The following illustration shows an example of how these links work.

<a href="/hapi-fhir/docs/images/empi-links.svg"><img src="/hapi-fhir/docs/images/empi-links.svg" alt="EMPI links" style="margin-left: 15px; margin-bottom: 15px; width: 500px;" /></a>

There are several resources that are used:

* Patient - Represents the record of a person who receives healthcare services
* Person - Represents a master record with links to one or more Patient and/or Practitioner resources that belong to the same person

# Automatic Linking

With EMPI enabled, the basic default behavior of the EMPI is simply to create a new Person record for every Patient that is created such that there is a 1:1 relationship between them. Any relinking is then expected to be done manually (i.e. via the forthcoming empi operations).

In a typical configuration it is often desirable to have links be created automatically using matching rules. For example, you might decide that if a Patient shares the same name, gender, and date of birth as another Patient, you have at least a little confidence that they are the same Person.

This automatic linking is done via configurable matching rules that create a links between Patients and Persons.  Based on the strength of the match configured in these rules, the link will be set to either POSSIBLE_MATCH or MATCHED.

## Design Principles

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

1. Once a link has been manually assigned as NO_MATCH or MATCHED, the system will not change it.

1. When a new Patient resource is created/updated then it is compared to all other Patient resources in the repository.  The outcome of each of these comparisons is either NO_MATCH, POSSIBLE_MATCH or MATCHED.

1. Whenever a MATCHED link is established between a Patient resource and a Person resource, that Patient is always added to that Person resource links.  All MATCHED links have corresponding Person resource links and all Person resource links have corresponding MATCHED empi-link records.  You can think of the fields of the empi-link records as extra meta-data associated with each Person.link.target.

### Possible rule match outcomes:

When a new Patient resource is compared with all other resources of that type in the repository, there are four possible cases:

* CASE 1: No MATCHED and no POSSIBLE_MATCHED outcomes -> a new Person resource is created and linked to that Patient as MATCHED.  All fields are copied from the Patient to the Person.  If the incoming resource has an EID, it is copied to the Person.  Otherwise a new UUID is created and used as the internal EID.

* CASE 2: All of the MATCHED Patient resources are already linked to the same Person -> a new Link is created between the new Patient and that Person and is set to MATCHED.

* CASE 3: The MATCHED Patient resources link to more than one Person -> Mark all links as POSSIBLE_MATCHED.  All other Person resources are marked as POSSIBLE_DUPLICATE of this first Person.  These duplicates are manually reviewed later and either merged or marked as NO_MATCH and the system will no longer consider them as a POSSIBLE_DUPLICATE going forward. POSSIBLE_DUPLICATE is the only link type that can have a Person as both the source and target of the link.

* CASE 4: Only POSSIBLE_MATCH outcomes -> In this case, empi-link records are created with POSSIBLE_MATCH outcome and await manual assignment to either NO_MATCH or MATCHED.  Person resources are not changed.

# Rules

HAPI EMPI rules are managed via a single json document.  This document contains a version.  empi-links derived from these rules are marked with this version.  The following configuration is stored in the rules:

* **resourceSearchParams**: These define fields which must have at least one exact match before two resources are considered for matching.  This is like a list of "pre-searches" that find potential candidates for matches, to avoid the expensive operation of running a match score calculation on all resources in the system.  E.g. you may only wish to consider matching two Patients if they either share at least one identifier in common or have the same birthday.
```json
[ {
    "resourceType" : "Patient",
    "searchParam" : "birthdate"
}, {
    "resourceType" : "Patient",
    "searchParam" : "identifier"
} ]
```

* **filterSearchParams** When searching for match candidates, only resources that match this filter are considered.  E.g. you may wish to only search for Patients for which active=true.
```json
[ {
    "resourceType" : "Patient",
    "searchParam" : "active",
    "fixedValue" : "true"
} ]
```

* **matchFields** Once the match candidates have been found, they are then each assigned a match vector that marks which fields match.  The match vector is determined by a list of matchFields.  Each matchField defines a name, distance metric, a success threshold, a resource type, and resource path to check. For example:
```json
{
    "name" : "given-name-cosine",
    "resourceType" : "Patient",
    "resourcePath" : "name.given",
    "metric" : "COSINE",
    "matchThreshold" : 0.8
}
```

Note that in all the above json, valid options for `resourceType` are `Patient`, `Practitioner`, and `All`. Use `All` if the criteria is identical across both resource types, and you would like to apply the pre-search to both practitioners and patients.

The following metrics are currently supported:
* JARO_WINKLER
* COSINE
* JACCARD
* NORMALIZED_LEVENSCHTEIN
* SORENSEN_DICE
* STANDARD_NAME_ANY_ORDER
* EXACT_NAME_ANY_ORDER
* STANDARD_NAME_FIRST_AND_LAST
* EXACT_NAME_FIRST_AND_LAST

See [java-string-similarity](https://github.com/tdebatty/java-string-similarity) for a description of the first five metrics.  For the last four, STANDARd means ignore case and accents whereas EXACT must match casing and accents exactly.  Name any order matches first and last names irrespective of order, whereas FIRST_AND_LAST metrics require the name match to be in order.

* **matchResultMap** A map which converts combinations of successful matchFields into an EMPI Match Result score for overall matching of a given pair of resources.

```json
"matchResultMap" : {
    "given-name-cosine" : "POSSIBLE_MATCH",
    "given-name-jaro, last-name-jaro" : "MATCH"
}
```

* **eidSystem**: The external EID system that the HAPI EMPI system should expect to see on incoming Patient resources. Must be a valid URI.

# Enterprise Identifiers

An Enterprise Identifier(EID) is a unique identifier that can be attached to Patients or Practitioners. Each implementation is expected to use exactly one EID system for incoming resources, 
defined in the mentioned `empi-rules.json` file. If a Patient or Practitioner with a valid EID is added to the system, that EID will be copied over to the Person that was matched. In the case that 
the incoming Patient or Practitioner had no EID assigned, an internal EID will be created for it. There are thus two classes of EID. Internal EIDs, created by HAPI-EMPI, and External EIDs, provided 
by the install. 

The following are rules about EID merging based on matches for new or modified Patient Data. 
1. A Person may have 0..1 Internal EIDs. 
2. A Person may have 0..* External EIDs. 
1. An incoming Patient/Practitioner is permitted to have 0..* External EIDs.
1. If a Patient/Practitioner with no External EID is created, and has no matches, a Person will be created, and given an Internal EID. 
1. If a Patient/Practitioner with no External EID is created, and matches an existing Patient/Practitioner, the EIDs will not be modified on the Person.
1. If a Patient/Practitioner with an External EID is created, and has no matches, a Person will be created, and the Patient/Practitioner's External EIDs will be applied to it. 
1. If a Patient/Practitioner with an External EID is created, and matches an existing Patient/Practitioner, there are two possibilities:
    1. If the Person has >=1 External EIDs, and there is a match between the incoming Patient/Practitioner's EIDs and the Person's, the resulting set of EIDs is the union of the original Person's External EIDs, and the incoming Patient/Practitioner's External EIDs. //FIXME EMPI is this the case? ask duncan
    1. If the Person has 0 External EIDs, the Patient/Practitioner's EIDs will be applied to the Person.
    
    
# HAPI EMPI Technical Details

When EMPI is enabled, the HAPI FHIR JPA Server does the following things on startup:

1. HAPI EMPI stores the extra link details in a table called `MPI_LINK`.
1. Each record in an `MPI_LINK` table corresponds to a `link.target` entry on a Person resource.  HAPI EMPI uses the following convention for the Person.link.assurance level:
    1. Level 1: not used
    1. Level 2: POSSIBLE_MATCH
    1. Level 3: AUTO MATCHED
    1. Level 4: MANUAL MATCHED
1. It enables the MESSAGE subscription type and starts up the internal subscription engine.
1. It creates two MESSAGE subscriptions, called 'empi-patient' and 'empi-practitioner' that match all incoming Patient and Practitioner resources and send them to an internal queue called "empi".  The JPA Server listens to this queue and links incoming resources to Persons.
1. It registers the `Patient/$match` operation.  See [$match](https://www.hl7.org/fhir/operation-patient-match.html) for a description of this operation.
1. It registers a new dao interceptor that restricts access to EMPI managed Person records.

