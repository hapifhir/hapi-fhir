# CQL Getting Started

## Introduction

Clinical Quality Language (CQL) is a high-level, domain-specific language focused on clinical quality and targeted at measure and decision support artifact authors. HAPI embeds a [CQL engine](https://github.com/DBCG/cql_engine) allowing the evaluation of clinical knowledge artifacts that use CQL to describe their logic.

A more detailed description of CQL is available at the [CQL Specification Implementation Guide](https://cql.hl7.org/)

The FHIR [Clinical Reasoning module](http://www.hl7.org/fhir/clinicalreasoning-module.html) defines a set of resources, profiles, operations, etc. that can be used to work with clinical knowledge within FHIR. HAPI provides implementation for some of those operations, described in more detail below.

## Working Example

A complete working example of HAPI CQL can be found in the [JPA Server Starter](/hapi-fhir/docs/server_jpa/get_started.html) project. You may wish to browse its source to see how it is set up.

## Overview

To get up and running with HAPI CQL, you can enable it using the `hapi.properties` file in the JPA Server Starter by setting `hapi.fhir.enable_cql` key to `true`. If you are running your own server follow the instructions below to [enable it in HAPI FHIR directly](#cql-settings).

Once you've enabled CQL processing, the next step is to load the appropriate knowledge artifact resources into your server.

## CQL Settings

There are two Spring beans available that add CQL processing to HAPI. You can enable CQL processing by importing the appropriate version for your server configuration.

* `ca.uhn.fhir.cql.config.CqlDstu3Config`
* `ca.uhn.fhir.cql.config.CqlR4Config`

## Clinical Reasoning Operations

HAPI provides implementations for some operations in DSTU3 and R4:

[CQL Measure](cql_measure.html)

## Roadmap

Further development of the CQL capabilities in HAPI is planned:

* Additional features and performance enhancements for Measure evaluation
* Additional FHIR Clinical Reasoning Module operations:
  * Library $evaluate
  * PlanDefinition $apply
* Support for the CPG IG Operations
  * $cql
