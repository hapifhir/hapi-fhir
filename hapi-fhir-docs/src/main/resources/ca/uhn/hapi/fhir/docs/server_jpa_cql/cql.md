# CQL Getting Started

## Introduction

Clinical Quality Language (CQL) is a high-level, domain-specific language focused on clinical quality and targeted at measure and decision support artifact authors. HAPI embeds a [CQL engine](https://github.com/DBCG/cql_engine) allowing the evaluation of clinical knowledge artifacts that use CQL to describe their logic.

A more detailed description of CQL is available at the [CQL Specification Implementation Guide](https://cql.hl7.org/)

The FHIR [Clinical Reasoning module](http://www.hl7.org/fhir/clinicalreasoning-module.html) defines a set of resources, profiles, operations, etc. that can be used to work with clinical knowledge within FHIR. HAPI provides implementation for some of those operations, described in more detail below.

## Working Example

A complete working example of HAPI CQL can be found in the [JPA Server Starter](/hapi-fhir/docs/server_jpa/get_started.html) project. You may wish to browse its source to see how it is set up.

## Overview

To get up and running with HAPI CQL, either enable it using the `hapi.properties` file in the JPA Server Starter, or follow the instructions below to [enable it in HAPI FHIR directly](#cql-settings).

Once you've enabled CQL processing, the next step is to load the appropriate knowledge artifact resources into you server.

## CQL Settings

There are two Spring beans available that and CQL processing to HAPI. You can enable CQL processing by importing the appropriate version for your server configuration.

* `ca.uhn.fhir.cql.config.CqlDstu3Config`
* `ca.uhn.fhir.cql.config.CqlR4Config`

## Operations

HAPI provides implementations for some Measure operations for DSTU3 and R4

### $evaluate-measure

The [$evaluate-measure](http://hl7.org/fhir/measure-operation-evaluate-measure.html) allows the evaluation of a clinical quality measure. This operation is invoked on an instance of a Measure resource:

`http://base/Measure/measureId/$evaluate-measure?subject=124&periodStart=2014-01&periodend=2014-03`

The Measure will be evaluated, including any CQL that is referenced. The CQL evaluation requires that all the supporting knowledge artifacts for a given Measure be loaded on the HAPI server, including `Libaries` and `ValueSets`.
