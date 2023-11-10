# JPA Server Search

This page describes features and limitations for performing [FHIR searches](https://hl7.org/fhir/search.html) on the JPA server.

# Limitations

The HAPI FHIR JPA Server fully implements most [FHIR search](https://hl7.org/fhir/search.html) operations for most versions of FHIR. However, there are some known limitations of the current implementation. Here is a partial list of search functionality that is not currently supported in HAPI FHIR:

### Chains within _has

Chains within _has are not currently supported for performance reasons. For example, this search is not currently supported

```http
https://localhost:8000/Practitioner?_has:PractitionerRole:practitioner:service.type=CHIRO
```

### Location.position "near"

Searching on Location.Position using `near` currently uses a box search, not a radius search. A box search is a square centred on the provided coordinates with the shortest distance to the edge of the square equal to the distance provided; this means the box search will match more locations than a radius search in the corners. Currently, distance is assumed to be in km and any provided units are ignored. Distance must be between 0.0km and 10,000km.

### _filter

The special `_filter` is only partially implemented.

### _pid

The JPA server implements a non-standard special `_pid` which matches/sorts on the raw internal database id.
This sort is useful for imposing tie-breaking sort order in an efficient way.

Note that this is an internal feature that may change or be removed in the future. Use with caution.

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
* Changes to the target data may not be reflected in the chained search. For example, using
  the `Encounter?subject.name=Simpson` example above, the value of Simpson will be written to the index using the Patient's name at the time that the Encounter resource is written. If the Patient resource's name is subsequently changed to _Flanders_ in an update, the new name will not be reflected in the chained search unless the Encounter
  resource is reindexed.

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
   "extension": [
      {
         "url": "https://smilecdr.com/fhir/ns/StructureDefinition/searchparameter-uplift-refchain",
         "extension": [
            {
               "url": "code",
               "valueCode": "name"
            }
         ]
      }
   ],
   "url": "http://hl7.org/fhir/SearchParameter/Encounter-subject",
   "name": "subject",
   "status": "active",
   "code": "subject",
   "base": [
      "Encounter"
   ],
   "type": "reference",
   "expression": "Encounter.subject",
   "target": [
      "Group",
      "Patient"
   ]
}
```

# Document and Message Search Parameters

The FHIR standard defines several Search Parameters on the Bundle resource that are intended to be used for specialized Bundle types.

<table class="table table-striped table-condensed">
   <thead>
      <tr>
      <th>Name</th>
      <th>Type</th>
      <th>FHIRPath Expression</th>
      </tr>
   </thead>
   <tbody>
      <tr>
         <td>composition</td>
         <td>Reference</td>
         <td>Bundle.entry[0].resource as Composition</td>
      </tr>
      <tr>
         <td>message</td>
         <td>Reference</td>
         <td>Bundle.entry[0].resource as MessageHeader</td>
      </tr>
   </tbody>
</table>

Unlike any other search parameters in the FHIR specification, these parameters use a FHIRPath expression that resolves to an embedded Resource (all other Search Parameters in the FHIR specification resolve to a datatype).

These parameters are only intended to be used as a part of a chained search expression, since it would not be meaningful to use them otherwise. For example, the following query could be used in order to use the _composition_ Search Parameter to locate FHIR Documents stored on the server that are an [International Patient Summary](./ips.html). In other words, this searches for Bundle resources where the first resource in the Bundle is a Composition, and that Composition has a `Composition.type` using LOINC code `60591-5`. 

```url
https://hapi.fhir.org/baseR4/Bundle?composition.type=http://loinc.org%7C60591-5
```

In order to use these search parameters, you may create a Search Parameter that includes the fully chained parameter value as the _name_ and _code_ values. In the example above, `composition` is the search parameter name, and `type` is the chain name. The fully chained parameter value is `composition.type`.

You should then use a FHIRPath expression that fully resolves the intended value within the Bundle resource. You may use the `resolve()` function to resolve resources that are contained within the same Bundle. Note that when used in a SearchParameter expression, the `resolve()` function is not able to resolve resources outside of the resource being stored.

```json
{
  "resourceType": "SearchParameter",
  "id": "Bundle-composition-patient-identifier",
  "url": "http://example.org/SearchParameter/Bundle-composition-patient-identifier",
  "name": "composition.patient.identifier",
  "status": "active",
  "code": "composition.patient.identifier",
  "base": [ "Bundle" ],
  "type": "token",
  "expression": "Bundle.entry[0].resource.as(Composition).subject.resolve().as(Patient).identifier"
}
```


In order to use these Search Parameters, you must enable [Uplifted Refchains](#uplifted-refchains) on your server, and modify the SearchParameter resource in order to add Uplifteed Refchain definitions for any chained searches you wish to support.

For example, to modify the _composition_ SearchParameter in order to support the query above, you can use the following resource:

```json
{
   "resourceType": "SearchParameter",
   "id": "Bundle-composition",
   "extension": [
      {
         "url": "https://smilecdr.com/fhir/ns/StructureDefinition/searchparameter-uplift-refchain",
         "extension": [
            {
               "url": "code",
               "valueCode": "type"
            }
         ]
      }
   ],
   "url": "http://hl7.org/fhir/SearchParameter/Bundle-composition",
   "name": "composition",
   "status": "active",
   "code": "composition",
   "base": [
      "Bundle"
   ],
   "type": "reference",
   "expression": "Bundle.entry[0].resource as Composition"
}
```


<a name="chained-sorting"/>

# Chained Sorting

The FHIR specification allows `_sort` expressions to use a comma-separated list of search parameter names in order to influence the sorting on search results.

HAPI FHIR extends this by allowing single-chained expressions as well. So for example, you can request a list of Encounter resources and sort them by the family name of the subject/patient of the Encounter by using the search shown in the example below. In this search, we are looking for all Encounter resources (typically additional search parameters would be used to limit the included Encounter resources), and sorting them by the value of the `family` search parameter on the Patient resource, where the Patient is referenced from the Encounter via the `patient` search parameter.

```url
http://example.org/Encounter?_sort=patient.family
```

Like chained search expressions, the first step in the chain must be a reference SearchParameter (SearchParameter.type = 'reference'). Unlike chained search expressions, only certain search parameter types can be used in the second part of the chain:

* String
* Date
* Token

If the reference search parameter defines multiple target types, it must be qualified with the specific target type you want to use when sorting. For example, the Encounter:subject search parameter can refer to targets of type _Patient_ or _Group_. The following expression **will not work** because the specific target type to use is not clear to the server.

```url
http://example.org/Encounter?_sort=subject.family
```

The following qualified expression adds a type qualifier and will work:

```url
http://example.org/Encounter?_sort=Patient:subject.family
```

## Chained Sort Performance

Chained sorting is more than twice as demanding of database performance.  They involve sorting on an index that is only connected to the primary resource in the search by a multi-level join, and read more data in the database.   Performance of chained sort expressions is highly variable.

In particular, this kind of sorting can be very slow if the search returns a large number of results (e.g. a search for Encounter?sort=patient.name where there is a very large number of Encounter resources and no additional search parameters are limiting the number of included resources).  They are safest when used in smaller collections, and as a secondary sort; as a tie-breaker within another sort.  E.g. `Encounter?practitioner=practitioner-id&date=2023-02&_sort=location,patient.name`.  

In order to improve sorting performance when chained sorts are needed, an [Uplifted Refchain](#uplifted-refchains) can be defined on the SearchParameter. This index will be used for the sorting expression and can improve performance.

# _include and _revinclude order

By default, all _revincludes will be performed first and then all _includes are performed afterwards.  However, if any _revinclude parameters are modified with :iterate (or :recurse for earlier versions of FHIR) then all _include parameters will be evaluated first.
