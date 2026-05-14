# FHIR Patch

HAPI FHIR's JPA server includes a complete implementation of the [FHIR Patch](https://www.hl7.org/fhir/fhirpatch.html) operation. FHIR Patch allows clients to modify resources by submitting a set of patch operations encoded as a FHIR Parameters resource.

The server supports FHIR Patch as well as [JSON Patch (RFC 6902)](https://tools.ietf.org/html/rfc6902) and XML Patch. This page describes the FHIR Patch format specifically.

# Patch Operations

The FHIR Patch format supports the following operation types:

* **add** - Add a new element or value to a resource
* **insert** - Insert a new element at a specific position in a list
* **delete** - Remove an element from a resource
* **replace** - Replace the value of an existing element
* **move** - Move an element from one position to another within a list

Each operation is encoded as a `parameter` entry with the name `operation`, containing `part` entries that specify the operation type, path, and value.

# Path Syntax

FHIR Patch operations use [FHIRPath](https://www.hl7.org/fhirpath/) expressions to identify the target element. The path must unambiguously identify a single element (unless using filter functions to narrow the target).

## Simple Paths

Simple paths navigate the resource structure using dot notation:

```
Patient.birthDate
Patient.name.family
Observation.code.coding
```

## Filtering with where()

When a resource contains multiple elements of the same type (e.g. multiple identifiers, multiple codings), you can use the `where()` function to filter to a specific element:

```
Observation.code.coding.where(system='http://loinc.org')
Patient.identifier.where(system='http://example.org/mrn')
```

## Subsetting Functions

FHIRPath subsetting functions can be used to target specific elements within a list:

* `first()` - Select the first element
* `last()` - Select the last element
* `single()` - Select the only element (fails if more than one)
* `tail()` - Select all elements except the first
* `skip(n)` - Skip the first n elements
* `take(n)` - Take the first n elements
* `[n]` - Select by index

Example:

```
Appointment.participant.actor.where(reference.startsWith('Patient/')).first()
```

## Extension Paths

Extensions can be targeted using two equivalent syntaxes:

**Function syntax** (shorthand):

```
Patient.extension('http://example.org/fhir/my-extension').value
```

**Where syntax** (explicit):

```
Patient.extension.where(url='http://example.org/fhir/my-extension').value
```

Both syntaxes are equivalent per the FHIRPath specification. Nested extensions are also supported:

```
Patient.extension('http://example.org/outer').extension('http://example.org/inner').value
```

# Replace Example

The following example shows a FHIR Patch that replaces a simple field:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "operation",
    "part": [ {
      "name": "type",
      "valueCode": "replace"
    }, {
      "name": "path",
      "valueString": "Patient.birthDate"
    }, {
      "name": "value",
      "valueDate": "1930-01-01"
    } ]
  } ]
}
```

## Replacing an Extension Value

To replace the value of a specific extension:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "operation",
    "part": [ {
      "name": "type",
      "valueCode": "replace"
    }, {
      "name": "path",
      "valueString": "Patient.extension('http://hl7.org/fhir/StructureDefinition/patient-mothersMaidenName').value"
    }, {
      "name": "value",
      "valueString": "Smith"
    } ]
  } ]
}
```

# Delete Example

The following example deletes an extension from a resource:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "operation",
    "part": [ {
      "name": "type",
      "valueCode": "delete"
    }, {
      "name": "path",
      "valueString": "Patient.extension.where(url='http://example.org/fhir/my-extension')"
    } ]
  } ]
}
```

# FhirPatchBuilder

The `FhirPatchBuilder` utility class can be used to programmatically generate FHIR Patch documents in Java. This is a convenient alternative to manually constructing Parameters resources.

```java
FhirPatchBuilder builder = new FhirPatchBuilder(fhirContext);
IBaseParameters patch = builder
    .replace()
    .path("Patient.birthDate")
    .value(new DateType("1930-01-01"))
    .andThen()
    .build();
```

See the [Bundle Builder](/hapi-fhir/docs/model/bundle_builder.html) documentation for examples of adding FHIR Patch operations to transaction bundles.

# Patch in Transactions

FHIR Patch operations can be included in transaction bundles. See the [Bundle Builder](/hapi-fhir/docs/model/bundle_builder.html) documentation for examples.
