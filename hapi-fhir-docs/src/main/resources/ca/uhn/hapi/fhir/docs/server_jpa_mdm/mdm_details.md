# MDM Implementation Details

This section describes details of how MDM functionality is implemented in HAPI FHIR.

# Automatic Linking

With MDM enabled, the default behavior of the MDM is to create a new Golden Record for every source record that is created such that there is a 1:1 relationship between them. Any relinking is then expected to be done manually via the [MDM Operations](/hapi-fhir/docs/server_jpa_mdm/mdm_operations.html).

In a typical configuration it is often desirable to have links created automatically using matching rules. For example, you might decide that if a Patient shares the same name, gender, and date of birth as another Patient, you have at least a little confidence that they are the same Patient.

This automatic linking is done via configurable matching rules that create links between source record and Golden Record. Based on the strength of the match configured in these rules, the link will be set to either POSSIBLE_MATCH or MATCH.

It is important to note that before a resource is processed by MDM, it is first checked to ensure that it has at least one attribute that the MDM system cares about, as defined in the `mdm-rules.json` file. If the incoming resource has no such attributes, then MDM processing does not occur on it. In this case, no Golden Resource is created for this source resource. If in the future the source resource is updated to contain attributes the MDM system does concern itself with, it will be processed at that time.

## Design

Below are some simplifying principles HAPI MDM follows to reduce complexity and ensure data integrity.

1. When MDM is enabled on a HAPI FHIR server, any Golden Resource in the repository that has the "hapi-mdm" tag is considered read-only by the FHIR endpoint. These Golden Resources are managed exclusively by HAPI MDM. Users can only change them via [MDM Operations](/hapi-fhir/docs/server_jpa_mdm/mdm_operations.html).  In most cases, users will indirectly change them by creating and updating the corresponding source resources.

1. Every source resource in the system has a MATCH link to at most one Golden Resource.

1. The only source resources in the system that do not have a MATCH link are those that have the 'NO-MDM' tag or those that have POSSIBLE_MATCH links pending review.

1. The HAPI MDM rules define a single identifier system that holds the external enterprise id ("EID"). If a source resource has an external EID, then the Golden Resource it links to always has the same EID. If a source resource has no EID when it arrives, a unique UUID will be assigned as that source resource's EID.

1. A Golden Resource can have both an internal EID (auto-created by HAPI), and an external EID (provided by an 
external system).

1. Two different Golden Resources cannot have the same EID.

1. Source resources are only ever compared to Golden Resources via this EID.


## Meta Tags

In order for MDM to work, the service adds several pieces of metadata to a given resource. This section explains what MDM does to the resources it processes.
When a resource is created via MDM (a Golden Resource), the following meta tag is added to the resource:

```json
{
  "resourceType": "Patient",
  "meta": {
    "tag": [
      {
        "system": "https://hapifhir.org/NamingSystem/managing-mdm-system",
        "code": "HAPI-MDM"
      }
    ]
  }
}
```

When this tag is present, the resource is considered managed by MDM. Any resource that is MDM-managed is considered read-only by the FHIR endpoint. Changes to it 
can only happen via MDM operations, and attribute survivorship. 

There may be use cases where a resource is ingested into the system but you _don't_ want to perform MDM processing on it. Consider the example of multiple patients being admitted into a hospital as "John Doe". If MDM were performed on this patient, it's 
possible that hundreds of John Doe's could be linked to the same Golden Resource. This would be a very bad situation as they are likely not the same person. To prevent this, you can add the `NO-MDM` tag to the resource. This will prevent MDM from processing it.

```json
{
  "resourceType": "Patient",
  "meta": {
    "tag": [
      {
        "system": "https://hapifhir.org/NamingSystem/managing-mdm-system",
        "code": "NO-MDM"
      }
    ]
  }
}
```


## Links

1. HAPI MDM manages mdm-link records ("links") that link a source resource to a Golden Resource. When these are created/updated by matching rules, the links are marked as AUTO.  When these links are changed manually, they are marked as MANUAL.

1. Once a link has been manually assigned as NO_MATCH or MATCH, the system will not change it.

1. When a new source resource is created/updated it is then compared to all other source resources of the same type in the repository. The outcome of each of these comparisons is either NO_MATCH, POSSIBLE_MATCH or MATCH.

1. HAPI MDM stores these extra link details in a table called `MPI_LINK`.

### Possible rule match outcomes:

When a new source resource is compared with all other resources of the same type in the repository, there are four possible outcomes:

* CASE 1: No MATCH and no POSSIBLE_MATCH outcomes -> a new Golden Resource is created and linked to that source resource as MATCH. If the incoming resource has an EID, it is copied to the Golden Resource. Otherwise a new UUID is generated and used as the internal EID.

* CASE 2: All of the MATCH source resources are already linked to the same Golden Resource -> a new Link is created between the new source resource and that Golden Resource and is set to MATCH.

* CASE 3: The MATCH source resources link to more than one Golden Resource -> Mark all links as POSSIBLE_MATCH.  All other Golden Resources are marked as POSSIBLE_DUPLICATE of this first Golden Resource. These duplicates are manually reviewed later and either merged or marked as NO_MATCH and the system will no longer consider them as a POSSIBLE_DUPLICATE going forward. POSSIBLE_DUPLICATE is the only link type that can have a Golden Resource as both the source and target of the link.

* CASE 4: Only POSSIBLE_MATCH outcomes -> In this case, new POSSIBLE_MATCH links are created and await manual reassignment to either NO_MATCH or MATCH.

# HAPI MDM Technical Details

When MDM is enabled, the HAPI FHIR JPA Server does the following things on startup:

1. It enables the MESSAGE subscription type and starts up the internal subscription engine.
1. It creates MESSAGE subscriptions for each resource type prefixed with 'mdm-'. For example, if MDM supports Patient and Practitioner resource, two subscriptions, called 'mdm-patient' and 'mdm-practitioner' that match all incoming MDM managed resources and send them to an internal queue called "mdm". The JPA Server listens to this queue and links incoming resources to the appropriate Golden Resources.
1. The [MDM Operations](/hapi-fhir/docs/server_jpa_mdm/mdm_operations.html) are registered with the server.
1. It registers a new dao interceptor that restricts access to MDM managed Golden Resource records.
