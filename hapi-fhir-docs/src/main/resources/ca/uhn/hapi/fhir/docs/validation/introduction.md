# Validation Introduction

This section contains details on several strategies for validating resources:

* **[Parser Error Handler](./parser_error_handler.html)** validation is validation at runtime during the parsing of a resource. It can be used to catch input data that is impossible to fit into the HAPI data model. For example, it can be used to throw exceptions or display error messages if a resource being parsed contains elements for which there are no appropriate fields in a HAPI data structure. This is useful in order to ensure that no data is being lost during parsing, but is less comprehensive than resource validation against raw text data.

  Parser Validation is extremely fast and lightweight since it happens within the parser and has no dependencies to outside resources.

* **[Instance Validator](./instance_validator.html)** is validation of the raw or parsed resource against the official FHIR validation rules (ie. the official FHIR definitions, expressed as profile resources such as [StructureDefinition](http://hl7.org/fhir/structuredefinition.html) and [ValueSet](http://hl7.org/fhir/valueset.html).

  The Instance Validator can also be used to validate resources against individual Implementation Guides which derive from the core specification (e.g. the [US Core](http://hl7.com/uscore) implementation guide).
  
* **[Schema/Schematron Validation](./schema_validator.html)** is validation using XSD/SCH validation files provided by FHIR. This validator performs well but produces less usable error messages than Profile Validation. It is considered a legacy feature, as the Instance Validator is now mature and preferred.
