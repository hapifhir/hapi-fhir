# Rules

HAPI EMPI rules are managed via a single json document.  The following configuration is stored in the rules:

* **candidateSearchParams**: These define fields which must have at least one exact match before two resources are considered for matching.  This is like a list of "pre-searches" that find potential candidates for matches, to avoid the expensive operation of running a match score calculation on all resources in the system.  E.g. you may only wish to consider matching two Patients if they either share at least one identifier in common or have the same birthday.  The HAPI FHIR server executes each of these searches separately and then takes the union of the results, so you can think of these as `OR` criteria that cast a wide net for potential candidates.  In some EMPI systems, these "pre-searches" are called "blocking" searches (since they identify "blocks" of candidates that will be searched for matches).
```json
[ {
    "resourceType" : "Patient",
    "searchParam" : "birthdate"
}, {
    "resourceType" : "Patient",
    "searchParam" : "identifier"
} ]
```

* **candidateFilterSearchParams** When searching for match candidates, only resources that match this filter are considered.  E.g. you may wish to only search for Patients for which active=true.  Another way to think of these filters is all of them are "AND"ed with each candidateSearchParam above.
```json
[ {
    "resourceType" : "Patient",
    "searchParam" : "active",
    "fixedValue" : "true"
} ]
```

* **matchFields** Once the match candidates have been found, they are then each compared to the incoming Patient resource.  This comparison is made across a list of `matchField`s.  Each matchField return `true` or `false` indicating whether the candidate and the incoming Patient match on that field.   
```json
{
    "name" : "given-name-cosine",
    "resourceType" : "Patient",
    "resourcePath" : "name.given",
    "metric" : "COSINE",
    "matchThreshold" : 0.8
}
```

Note that in all the above json, valid options for `resourceType` are `Patient`, `Practitioner`, and `*`. Use `*` if the criteria is identical across both resource types, and you would like to apply the pre-search to both practitioners and patients.

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

There are many edge cases for determining what will happen in merge and update scenarios, which will be provided in future documentation.
