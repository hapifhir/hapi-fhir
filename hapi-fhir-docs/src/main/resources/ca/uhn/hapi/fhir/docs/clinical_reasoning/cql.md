# CQL

## Introduction

Clinical Quality Language (CQL) is a high-level, domain-specific language focused on clinical quality and targeted at measure and decision support artifact authors. HAPI embeds a [CQL engine](https://github.com/DBCG/cql_engine) allowing the evaluation of clinical knowledge artifacts that use CQL to describe their logic.

A more detailed description of CQL is available at the [CQL Specification Implementation Guide](https://cql.hl7.org/)


## Configuration

These settings apply to all operations in which CQL is used.

| Key                | Values | Default | Description                   |
|--------------------|--------|---------|-------------------------------|
| `hapi.fhir.cr.cql` | N/A    | N/A     | root key for configuring CQL. |

