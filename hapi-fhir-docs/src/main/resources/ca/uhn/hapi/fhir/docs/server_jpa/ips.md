# International Patient Summary (IPS) Generator

The International Patient Summary (IPS) is an international collaborative effort to develop a specification for a health record summary extract. It is specified in the standards EN 17269 and ISO 27269, and supported in FHIR through the [International Patient Summary Implementation Guide](http://hl7.org/fhir/uv/ips/).

In FHIR, an IPS is expressed as a [FHIR Document](https://www.hl7.org/fhir/documents.html). The HAPI FHIR JPA server supports the automated generation of IPS documents through an extensible and customizable engine which implements the [`$summary`](http://hl7.org/fhir/uv/ips/OperationDefinition-summary.html) operation.





