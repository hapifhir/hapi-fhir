# HAPI FHIR Client

This module provides a generic, version-independent FHIR client API for interacting with FHIR servers. It supports all FHIR versions using the `FhirContext` abstraction.

## Features
- **Fluent API**: Type-safe search, create, update, and delete operations.
- **Interceptors**: Extensible mechanism for logging, authentication, and custom headers.
- **Resource Parsing**: Automatic JSON/XML serialization using HAPI's core parser.

## Usage

```java
// Create a context and client
FhirContext ctx = FhirContext.forR4();
IGenericClient client = ctx.newRestfulGenericClient("https://hapi.fhir.org/baseR4");

// Perform a search
Bundle results = client
    .search()
    .forResource(Patient.class)
    .where(Patient.FAMILY.matches().value("Smith"))
    .returnBundle(Bundle.class)
    .execute();
```

## Testing
Tests for this module are located in `src/test`. Run them using:
```bash
mvn test -pl hapi-fhir-client
```
