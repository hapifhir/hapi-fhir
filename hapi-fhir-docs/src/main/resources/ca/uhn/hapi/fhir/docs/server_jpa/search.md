# JPA Server Search

## Limitations

The HAPI FHIR JPA Server fully implements most [FHIR search](https://www.hl7.org/fhir/search.html) operations for most versions of FHIR.  However, there are some known limitations of the current implementation.  Here is a partial list of search functionality that is not currently supported in HAPI FHIR:

### Chains within _has

Chains within _has are not currently supported for performance reasons.  For example, this search is not currently supported
```http
https://localhost:8000/Practitioner?_has:PractitionerRole:practitioner:service.type=CHIRO
```

### Location.position "near"

Searching on Location.Position using `near` currently uses a box search, not a radius search.  A box search is a square centred on the provided coordinates with the shortest distance to the edge of the square equal to the distance provided; this means the box search will match more locations than a radius search in the corners.  Currently, distance is assumed to be in km and any provided units are ignored.  Distance must be between 0.0km and 10,000km.

### _filter

The special `_filter` is only partially implemented.


