# Clinical Reasoning

## Introduction

The FHIR [Clinical Reasoning module](http://www.hl7.org/fhir/clinicalreasoning-module.html) defines a set of resources,
profiles, operations, etc. that can be used to work with clinical knowledge within FHIR. HAPI provides implementation
for some of those operations, described in more detail below.

## Operations

HAPI provides implementations for some operations in DSTU3 and R4:

[Measure](measure.html)

[CQL](cql.html)

## Working Example

A complete working example of HAPI Clinical Reasoning can be found in
the [JPA Server Starter](/hapi-fhir/docs/server_jpa/get_started.html) project. You may wish to browse its source to see
how it is set up.

## Overview

To get up and running with HAPI Clinical Reasoning, you can enable it using the `hapi.properties` file in the JPA Server
Starter by setting `hapi.fhir.cr.enabled` key to `true`. If you are running your own server follow the instructions below
to [enable it in HAPI FHIR directly](#settings).

Once you've enabled Clinical Reasoning processing, the next step is to load the appropriate knowledge artifact resources
into your server.

## Settings

There are two Spring beans available that enable Clinical Reasoning processing in HAPI. You can enable CQL processing by
importing the appropriate version for your server configuration.

* `ca.uhn.fhir.cr.config.CrDstu3Config`
* `ca.uhn.fhir.cr.config.CrR4Config`

## Configuration

The `ca.uhn.fhir.cr.config.CrProperties` class is used to configure the Clinical Reasoning module in HAPI.
The `CrProperties` class has a master switch called `enabled` that determines whether the Clinical Reasoning operations
are loaded or not. If you're using the HAPI FHIR JPA Starter server, this can be configured by setting:

| Key | Values           | Default                                                             | Description |
|------------------------|------------------|---------------------------------------------------------------------|-------------|
| `hapi.fhir.cr.enabled` | `true`,`false`   |`true`    | enables or disables the Clinical Reasoning operations of HAPI FHIR.  |


Each of the submodules of Clinical Reasoning has its own configuration. The documentation for that is located on the
pages for each:

[Measure Configuration](measure.html#configuration)

[CQL Configuration](cql.html#configuration)

## Roadmap

Further development of the Clinical Reasoning capabilities in HAPI is planned:

* Additional features and performance enhancements for Measure evaluation
* Additional FHIR Clinical Reasoning Module operations:
   * Library $evaluate
   * PlanDefinition $apply
* Support for the CPG IG Operations
   * $cql
