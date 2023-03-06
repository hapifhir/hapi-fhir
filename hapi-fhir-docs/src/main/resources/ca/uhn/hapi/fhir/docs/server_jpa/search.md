# JPA Server Search

This page describes features and limitations for performing [FHIR searches](https://hl7.org/fhir/search.html) on the JPA server. 

# Limitations

The HAPI FHIR JPA Server fully implements most [FHIR search](https://hl7.org/fhir/search.html) operations for most versions of FHIR.  However, there are some known limitations of the current implementation.  Here is a partial list of search functionality that is not currently supported in HAPI FHIR:

### Chains within _has

Chains within _has are not currently supported for performance reasons.  For example, this search is not currently supported
```http
https://localhost:8000/Practitioner?_has:PractitionerRole:practitioner:service.type=CHIRO
```

### Location.position "near"

Searching on Location.Position using `near` currently uses a box search, not a radius search.  A box search is a square centred on the provided coordinates with the shortest distance to the edge of the square equal to the distance provided; this means the box search will match more locations than a radius search in the corners.  Currently, distance is assumed to be in km and any provided units are ignored.  Distance must be between 0.0km and 10,000km.

### _filter

The special `_filter` is only partially implemented.


<a name="uplifted-refchains"/>

# Uplifted Refchains and Chaining Performance

FHIR chained searches allow for a search against a reference search parameter to 'chain' into the reference targets and search these targets for a given search criteria.

For example, consider the search:

```url
http://example.org/Encounter?subject.name=Simpson
```

This search returns any Encounter resources where the `Encounter.subject` reference points to a Patient or Group resource satisfying the search `name=Simpson`.

In order to satisfy this search, a two-level SQL JOIN is required in order to satisfy both the reference and the string portions of the search. This search leverages two indexes created by the indexer:

* A reference index satisfying the `subject` search parameter, associated with the _Encounter_ resource. 
* A string index satisfying the `name` search parameter, associated with the _Patient_ and _Group_ resources.

If you are having performance issues when performing chained searches like this one, a feature called **Uplifted Refchains** can be used to create a single index against the Encounter resource. Uplifted refchains promote chained search parameters to create a single index which includes both parts of the chain. In the example above this means:

* A string index satisfying the `subject.name` search parameter, associated with the _Encounter_ resource.

This can be very good for search performance, especially in cases where the second part of the chain (.name) matches a very large number of resources.

## Drawbacks

Using Uplifted Refchains has several drawbacks however, and it is important to consider them before enabling this feature:

* Write speed will typically be slower for the resource type containing the uplifted refchain, since the target needs to be resolved, parsed, and the additional uplifted refchain index rows need to be written.
* Changes to the target data may not be reflected in the chained search. For example, using the `Encounter?subject.name=Simpson` example above, the value of Simpson will be written to the index using the Patient's name at the time that the Encounter resource is written. If the Patient resource's name is subsequently changed to _Flanders_ in an update, the new name will not be reflected in the chained search unless the Encounter resource is reindexed.

## Sorting on Uplifted Refchains

One additional benefit of using this feature is that any chain expressions satisfied by an Uplifted Refchains become candidates for sorting expressions as well.

The following sort expression is generally illegal, but can be executed if an uplifted refchain is defined for this chain expression.

```url
http://example.com/Encounter?sort=subject.name
```

## Defining Uplifted Refchains 

To define an uplifted refchain, the reference search parameter for the first part of the chain must be created or updated in order to add a new extension.

Continuing the example above, this means updating the `Encounter:subject` search parameter, creating it if it does not exist. Be careful not to create a second search parameter if you already have one defined for Encounter:subject. In this case, you must update the existing search parameter and add the new extension to it.

The extension has the following URL:

```url
https://smilecdr.com/fhir/ns/StructureDefinition/searchparameter-uplift-refchain
```

This extension has the following children:

* `code` - Contains a code with the name of the chained search parameter to uplift. 

An example follows:

```json
{
   "resourceType": "SearchParameter",
   "id": "Encounter-subject",
   "extension": [ {
      "url": "https://smilecdr.com/fhir/ns/StructureDefinition/searchparameter-uplift-refchain",
      "extension": [ {
         "url": "code",
         "valueCode": "name"
      } ]
   } ],
   "url": "http://hl7.org/fhir/SearchParameter/Encounter-subject",
   "name": "subject",
   "status": "active",
   "code": "subject",
   "base": [ "Encounter" ],
   "type": "reference",
   "expression": "Encounter.subject",
   "target": [ "Group", "Patient" ]
}
```
