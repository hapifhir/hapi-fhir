# Clinical Reasoning

## Introduction

The FHIR [Clinical Reasoning module](http://www.hl7.org/fhir/clinicalreasoning-module.html) provides resources and operations to enable the representation, distribution, and evaluation of clinical knowledge artifacts such as clinical decision support rules, quality measures, public health indicators, order sets, and clinical protocols.

Clinical Reasoning involves the ability to represent and encode clinical knowledge in a very broad sense so that it can be integrated into clinical systems. This encoding may be as simple as controlling whether a particular section of an order set appears based on the conditions that a patient has, or it may be as complex as representing the care pathway for a patient with multiple conditions.

The HAPI FHIR server provides support for Clinical Reasoning on FHIR by implementing several of the operations described in the Clinical Reasoning module and derivative IGs.

## Get Started &#128498;

To get up and running with HAPI Clinical Reasoning, you can enable it using the `hapi.properties` file in the [JPA Server Starter](/hapi-fhir/docs/server_jpa/get_started.html) by setting `hapi.fhir.cr.enabled` key to `true`. If you are running your own server follow the instructions below
to [enable it in HAPI FHIR directly](#settings).

Once you've enabled Clinical Reasoning processing, the next step is to load the appropriate knowledge artifact resources
into your server. See examples linked in the submodules below.

## Operations

HAPI provides implementations for some operations in DSTU3 and R4:

[Measure](measure.html) - eCQMs, dQMs, and other health indicators

[CQL](cql.html) - Direct evaluation of CQL

## Settings

There are two Spring beans available that enable Clinical Reasoning processing in HAPI. You can enable CQL processing by
importing the appropriate version for your server configuration.

* `ca.uhn.fhir.cr.config.CrDstu3Config`
* `ca.uhn.fhir.cr.config.CrR4Config`

## Configuration

The `ca.uhn.fhir.cr.config.CrProperties` class is used to configure the Clinical Reasoning module in HAPI.
The `CrProperties` class has a master switch called `enabled` that determines whether the Clinical Reasoning operations
are loaded or not. If you're using the HAPI FHIR JPA Starter server, this can be configured by setting:

| Key                    | Values            | Default | Description                                                         |
|------------------------|-------------------|---------|---------------------------------------------------------------------|
| `hapi.fhir.cr.enabled` | `true` or `false` | `true`  | enables or disables the Clinical Reasoning operations of HAPI FHIR. |


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
