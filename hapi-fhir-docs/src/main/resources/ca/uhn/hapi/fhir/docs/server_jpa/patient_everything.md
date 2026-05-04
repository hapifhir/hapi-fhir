# Patient $everything Operation

The `$everything` operation is a FHIR-defined extended operation that retrieves all resources associated with a given Patient. The operation is defined in the [FHIR specification](http://hl7.org/fhir/operation-patient-everything.html).

HAPI FHIR's JPA server provides a built-in implementation of this operation on both the instance level (`Patient/[id]/$everything`) and the type level (`Patient/$everything`).

# Instance-Level Usage

Retrieve all resources linked to a specific patient:

```http
GET http://fhir.example.com/baseR4/Patient/123/$everything
```

The response is a searchset Bundle containing the Patient resource itself along with all resources that reference it (Observations, Conditions, Encounters, etc.).

# Type-Level Usage

Retrieve resources linked to all patients on the server:

```http
GET http://fhir.example.com/baseR4/Patient/$everything
```

# Parameters

The following parameters are supported:

* `_count=[n]`: (*optional*) Page size for the returned Bundle. Defaults to server-configured page size.
* `_offset=[n]`: (*optional*) Offset into the result set for paging.
* `_since=[date]`: (*optional*) Only return resources updated after the given date/time (`_lastUpdated=ge[date]` equivalent).
* `_type=[comma-separated resource types]`: (*optional*) Filter the linked resources to include only those of the specified resource type(s). See [Filtering by Resource Type](#filtering-by-resource-type) for details and known behavior.
* `_sort=[parameter]`: (*optional*) Sort expression applied to results.
* `_content=[text]`: (*optional*) Full-text content search filter.
* `_mdm=true`: (*optional*) If MDM is enabled, expand the patient set to include all MDM-linked patients before performing the `$everything`. See [MDM Search Expansion](/docs/server_jpa_mdm/mdm_expansion.html) for details.

# Filtering by Resource Type

The `_type` parameter narrows the returned resources to only those whose resource type appears in the comma-separated list. This filter applies to all returned resources, including the Patient anchor resource itself. If `Patient` is not listed in `_type`, the Patient anchor is omitted from the response. To include the Patient resource when using `_type`, add `Patient` to the type list explicitly.

Example — return only Observations linked to Patient/123:

```http
GET http://fhir.example.com/baseR4/Patient/123/$everything?_type=Observation
```

Example — return Observations and Conditions linked to Patient/123:

```http
GET http://fhir.example.com/baseR4/Patient/123/$everything?_type=Observation,Condition
```
