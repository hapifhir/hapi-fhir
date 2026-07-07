# `ValueSet` Pre-Expansion

A `ValueSet` defines its membership through `compose.include` / `compose.exclude` rules rather than an
explicit list of codes. *Expansion* is the act of resolving those rules against the referenced
CodeSystems to produce the concrete set of codes the `ValueSet` contains. The JPA server supports two
expansion strategies, and chooses between them automatically depending on what is available:

- **Persisted pre-expansion** — the expanded concept list is computed ahead of time by a background
  job and stored in the database (`TRM_VALUESET_CONCEPT`), keyed to the `ValueSet` entity in
  `TRM_VALUESET`. Subsequent `$expand` and `$validate-code` calls read directly from these tables,
  avoiding the cost of resolving the compose rules on every request. This scales to very large
  ValueSets.
- **In-memory expansion** — when a persisted pre-expansion is not available (or not yet complete),
  the server falls back to expanding the `ValueSet` on the fly, in memory, for that single request. The
  result is not persisted.

The two strategies are complementary: pre-expansion is an optimization, and in-memory expansion is
the always-available fallback that guarantees correctness even before (or without) pre-expansion.

<a id="how-expansion-is-chosen"></a>

## How Expansion Is Chosen

For both `$expand` and `$validate-code`, the server looks up the `ValueSet` in the terminology
tables and inspects its [expansion status](#expansion-statuses):

- If the `ValueSet` **is not present** in the terminology tables, the server performs an **in-memory**
  expansion.
- If the `ValueSet` **is present but its status is not `EXPANDED`** (e.g. still queued, in progress, or
  failed), the server also falls back to **in-memory** expansion for that request.
- If the `ValueSet` **is present and `EXPANDED`**, the server serves the request from the **persisted**
  pre-expanded concepts.

In-memory expansion is provided by `InMemoryTerminologyServerValidationSupport`, which expands
ValueSets and validates codes entirely in memory. Because it does not touch the terminology tables,
it works the same whether or not pre-expansion is enabled — it is simply slower for large ValueSets,
which is the problem pre-expansion exists to solve.


<a id="background-pre-expansion-job"></a>

## The Background Pre-Expansion Job

Pre-expansion runs as an asynchronous job rather than something requested directly. What queues that
job depends on which resource changed:

- **Creating or updating a `ValueSet`** queues a pre-expansion job for that `ValueSet` immediately
  after the transaction commits.
- **Creating or updating a `CodeSystem`** invalidates the pre-calculated expansion of any
  already-`EXPANDED` (or in-progress) `ValueSet` that references it, resetting its status to
  `NOT_EXPANDED`. This does **not** by itself queue a new job — the `ValueSet` stays `NOT_EXPANDED`
  until something else triggers expansion, such as a subsequent `ValueSet` update, an `$expand`
  request (which falls back to in-memory expansion but does not queue pre-expansion), or an explicit
  [`$invalidate-expansion`](#invalidate-expansion) call.
- **Calling [`$invalidate-expansion`](#invalidate-expansion)** on a `ValueSet` explicitly queues a
  pre-expansion job for it, regardless of its current status.

Whichever way it is queued, the job performs the same steps:

1. Sets the ValueSet's status to `EXPANSION_IN_PROGRESS`.
2. Resolves the `compose` rules and writes the resulting concepts to `TRM_VALUESET_CONCEPT` (with
   designations in `TRM_VALUESET_C_DESIGNATION`).
3. On success, sets the status to `EXPANDED` and records the completion time.
4. On failure, sets the status to `FAILED_TO_EXPAND` and persists the failure reason in the
   `EXPANSION_ERROR` column of `TRM_VALUESET`.

The job is skipped while deferred terminology entities (e.g. a large CodeSystem still being loaded)
are being processed, so that ValueSets are not expanded against incomplete CodeSystems.

The expanded data lives in the database and is reused across server restarts.

<a id="expansion-statuses"></a>

## Expansion Statuses

Each `ValueSet` has an expansion status stored in the `TRM_VALUESET` table:

| Status | Meaning |
|---|---|
| `NOT_EXPANDED` | Queued for expansion but not yet attempted |
| `EXPANSION_IN_PROGRESS` | Currently being expanded |
| `EXPANDED` | Successfully expanded; served from the persisted concept tables |
| `FAILED_TO_EXPAND` | Expansion failed; see `EXPANSION_ERROR` for reason |
| `NOT_ACTIVE` | `ValueSet` is inactive and will not be expanded |

<a id="operations"></a>

## Operations

ValueSet expansion is exercised through a mix of FHIR-standard and HAPI-specific operations:

| Operation | Source | Purpose |
|---|---|---|
| [`$expand`](#expand) | [FHIR standard](http://hl7.org/fhir/valueset-operation-expand.html) | Expand a `ValueSet` and return its concepts. Served from the persisted pre-expansion when available. |
| `$validate-code` | [FHIR standard](http://hl7.org/fhir/valueset-operation-validate-code.html) | Validate that a code is a member of a `ValueSet`. Uses the persisted pre-expansion when the `ValueSet` is `EXPANDED` (see [How Expansion Is Chosen](#how-expansion-is-chosen)). |
| [`$hapi.fhir.expansion-status`](#expansion-status) | HAPI-specific | Report the pre-expansion status of all ValueSets. |
| [`$invalidate-expansion`](#invalidate-expansion) | HAPI-specific | Reset a ValueSet's status and queue it for re-expansion. |

<a id="expand"></a>

### Expanding a ValueSet: `$expand`

`$expand` is the standard FHIR ValueSet expansion operation — its general behavior and full parameter
list are defined in the [FHIR specification](http://hl7.org/fhir/valueset-operation-expand.html). Only
the JPA-server-specific aspects are described here.

**Pre-expansion serving.** When the target `ValueSet` has been pre-expanded (status `EXPANDED`), the
JPA server returns the concepts directly from the persisted terminology tables; otherwise it falls
back to an in-memory expansion for that request (see [How Expansion Is Chosen](#how-expansion-is-chosen)).

**Supported parameters.** The JPA server implements a subset of the standard parameters: `url`,
`valueSet`, `valueSetVersion`, `filter`, `context`, `contextDirection`, `offset`, `count`, and
`displayLanguage`. In addition it accepts one HAPI-specific extension parameter:

| Parameter | Type | Description                                                                                                                                                                                                 |
|---|---|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `includeHierarchy` | boolean | HAPI-specific. When `true`, concepts are nested under their parent's `contains` (a tree) instead of a flat list. This affects the structure of the output only — the same concepts are returned either way. |

<a id="expansion-status"></a>

### Checking Expansion Status: `$hapi.fhir.expansion-status`

The `ValueSet/$hapi.fhir.expansion-status` operation returns the expansion status of all ValueSets
in the repository as a `Parameters` resource. It is the primary way to find which ValueSets failed
to expand and why, without requiring database access or log searching.

#### Request

```
GET [base]/ValueSet/$hapi.fhir.expansion-status
```

#### Parameters

| Parameter | Type | Description |
|---|---|---|
| `expansionStatus` | code (repeatable) | Filter by [status](#expansion-statuses). Multiple values use OR logic. If omitted, all statuses are returned. |
| `url` | uri | Filter by `ValueSet` URL. Default: starts-with match (case-insensitive). Supports `:contains` and `:exact` modifiers. |
| `name` | string | Filter by `ValueSet` name. Same modifier support as `url`. |
| `_count` | integer | Page size (default 10, min 1, max 50). See `DatabaseBackedPagingProvider` in [Paging](/docs/server_plain/paging.html).|
| `_offset` | integer | Pagination offset (default 0) |

#### Response

```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "summary",
      "part": [
        { "name": "total", "valueInteger": 1250 },
        { "name": "hasMore", "valueBoolean": false },
        { "name": "expanded", "valueInteger": 1100 },
        { "name": "notExpanded", "valueInteger": 80 },
        { "name": "expansionInProgress", "valueInteger": 5 },
        { "name": "failedToExpand", "valueInteger": 60 },
        { "name": "notActive", "valueInteger": 5 }
      ]
    },
    {
      "name": "valueSet",
      "part": [
        { "name": "url", "valueUri": "http://loinc.org/vs/LL1000-0" },
        { "name": "name", "valueString": "My ValueSet" },
        { "name": "version", "valueString": "2.77" },
        { "name": "resourceId", "valueString": "ValueSet/12345" },
        { "name": "expansionStatus", "valueCode": "FAILED_TO_EXPAND" },
        { "name": "expansionTimestamp", "valueDateTime": "2026-06-01T10:30:00Z" },
        { "name": "errorMessage", "valueString": "HAPI-0702: Unable to expand ValueSet because CodeSystem could not be found: http://hl7.org/fhir/sid/icd-10-cm" }
      ]
    }
  ]
}
```

The `summary` part always reflects the entire repository regardless of filters. `hasMore` is `true`
when additional pages of results exist. `errorMessage` is only present on `FAILED_TO_EXPAND`
entries (or `NOT_EXPANDED` entries where a prior attempt failed and the `ValueSet` was subsequently
invalidated).

<a id="invalidate-expansion"></a>

### Invalidating Expansion: `$invalidate-expansion`

`$invalidate-expansion` is a HAPI-specific operation (it is not part of the FHIR standard). It
resets a ValueSet's status to `NOT_EXPANDED` and immediately queues a new pre-expansion job for it.
The `EXPANSION_ERROR` from the previous failure is preserved on the `NOT_EXPANDED` entry so the
failure reason remains visible while re-expansion is pending.

```http request
POST [base]/ValueSet/[id]/$invalidate-expansion
```

<a id="troubleshooting-expansion-failures"></a>

## Troubleshooting Expansion Failures

<a id="finding-failed-valuesets"></a>

### Finding failed ValueSets

```http request
GET [base]/ValueSet/$hapi.fhir.expansion-status?expansionStatus=FAILED_TO_EXPAND
```

Each returned `ValueSet` entry includes an `errorMessage` field with the failure reason, for
example: `Unable to expand ValueSet because CodeSystem could not be found: http://...`.

<a id="common-failure-reasons"></a>

### Common failure reasons

- **CodeSystem not loaded** — The `ValueSet` references a CodeSystem that has not been uploaded to the
  server. Upload the CodeSystem and then invalidate the failed ValueSets to trigger re-expansion.
- **Unsupported filter** — The `ValueSet` uses a filter operator not supported by the server for that
  CodeSystem type.
- **Circular reference** — The CodeSystem contains circular concept hierarchies.

<a id="triggering-re-expansion"></a>

### Triggering re-expansion

After resolving the root cause, use [`$invalidate-expansion`](#invalidate-expansion)
on each affected `ValueSet` to reset its status to `NOT_EXPANDED` and immediately queue a new
pre-expansion job.
