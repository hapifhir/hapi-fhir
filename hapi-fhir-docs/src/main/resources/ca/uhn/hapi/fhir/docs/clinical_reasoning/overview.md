# Clinical Reasoning

## Overview

Clinical Reasoning (CR) is ability to represent, encode, and evaluate clinical knowledge so that it can be integrated into clinical systems. In other words, clinical reasoning is the ability to store and run business logic that is relevant to clinical settings. This may be as simple as controlling whether a particular section of an order set appears based on the conditions that a patient has, or it may be as complex as representing the care pathway for a patient with multiple conditions.

The FHIR [Clinical Reasoning module](http://www.hl7.org/fhir/clinicalreasoning-module.html) specifies a foundational set of FHIR resources and associated operations that allow a FHIR repository to perform clinical reasoning on clinical data. Some use cases include:

* Prospective/Retrospective Analytics
  * Quality Measures
  * Gaps in Care
* Clinical Decision Support
* Payer/Provider Data Exchange
* Prior Authorization

There are additional IGs outside the FHIR CR module that define further requirements and behavior for other Clinical Reasoning use cases. Some examples include:

* [Structured Data Capture IG](https://build.fhir.org/ig/HL7/sdc/)
* [Clinical Guidelines IG](https://hl7.org/fhir/uv/cpg/)
* [Quality Measures IG](http://hl7.org/fhir/us/cqfmeasures/)
* [Canonical Resource Management Infrastructure IG](https://build.fhir.org/ig/HL7/crmi-ig/index.html)

## HAPI FHIR

The HAPI FHIR server includes support for storing all the Clinical Reasoning resources defined in the FHIR CR module, including `Measure`, `PlanDefinition`, `ActivityDefinition` and so on. Additionally, HAPI includes an embedded [CQL](/hapi-fhir/docs/clinical_reasoning/cql.html) engine that allows it to process clinical logic encoded in a standard representation.

HAPI also includes a [Quality Measure](/hapi-fhir/docs/clinical_reasoning/measures.html) engine that can evaluate clinical quality measures.

See the [CQL](/hapi-fhir/docs/clinical_reasoning/cql.html) and [Measure](/hapi-fhir/docs/clinical_reasoning/measures.html) documentation for further details.
