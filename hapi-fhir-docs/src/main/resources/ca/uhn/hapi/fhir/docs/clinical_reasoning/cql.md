# CQL

## Introduction

Clinical Quality Language (CQL) is a high-level, domain-specific language focused on clinical quality and targeted at measure and decision support artifact authors. HAPI embeds a [CQL engine](https://github.com/cqframework/clinical_quality_language) allowing the evaluation of clinical knowledge artifacts that use CQL to describe their logic.

A more detailed description of CQL is available at the [CQL Specification Implementation Guide](https://cql.hl7.org/)

The FHIR [Clinical Reasoning module](http://www.hl7.org/fhir/clinicalreasoning-module.html) defines a set of resources, profiles, operations, etc. that can be used to work with clinical knowledge within FHIR. HAPI provides implementation for some of those operations, described in more detail below.

## Working Example

A complete working example of HAPI CQL can be found in the [JPA Server Starter](/hapi-fhir/docs/server_jpa/get_started.html) project. You may wish to browse its source to see how it is set up.

## Clinical Reasoning Operations

HAPI provides implementations for some operations using CQL in DSTU3 and R4:

[Measure Operations](/hapi-fhir/docs/clinical_reasoning/measures.html)
