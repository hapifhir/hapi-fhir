# HAPI FHIR Server

The core server framework for building RESTful FHIR servers. It implements the FHIR RESTful specification, including search, history, and transaction operations.

## Features
- **Resource Providers**: Simple, annotation-based POJO providers for FHIR resources.
- **Interceptor Chain**: Hook into any point of the request lifecycle for authorization or auditing.
- **Flexible Storage**: Can be used with the JPA storage module or custom backends.

## Usage

Define a provider:
```java
public class PatientProvider implements IResourceProvider {
    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }

    @Read
    public Patient read(@IdParam IdType theId) {
        // Implementation here
    }
}
```

## Testing
Tests for this module are located in `src/test`. Run them using:
```bash
mvn test -pl hapi-fhir-server
```
