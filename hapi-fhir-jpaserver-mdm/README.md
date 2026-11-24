<!-- Created by Claude Opus 5.5 -->
# HAPI FHIR JPA Server MDM

This README covers how MDM processes sources concurrently without creating duplicate golden resources: the Match
Claim design. The rest of the module is documented in its javadoc and in the
[MDM documentation](https://hapifhir.io/hapi-fhir/docs/server_jpa_mdm/mdm.html). The test plan for this design is in
`src/test/java/ca/uhn/fhir/jpa/mdm/svc/concurrency/package-info.java`.

## The Problem

MDM processes one source at a time per unit of work: search for candidates, decide, then link the source to a golden
resource or create a new one.

With several MDM consumers or several nodes, two units of work for sources that match
each other can both search before either commits.

Each sees no golden resource for the other, so each creates its own.

Result: two golden resources where one is expected.

## The Solution: Match Claims

A unit of work first inserts a **Match Claim** for everything its decision depends on.

A Match Claim is a row in `MPI_MATCH_CLAIM`, stored as `MdmMatchClaimEntity`, that records that one unit of work is processing
a given source or EID. It is not the act of claiming: a unit of work *claims* a key by inserting a Match Claim for it, and only then searches.

The `MPI_MATCH_CLAIM` table's primary key is a hash of the Match Claim's canonical key
(the text that names what is claimed, always the same for the same resource or EID, e.g. `PID|Patient|1234`; see the table below).

Two units of work that claim the same key cannot both hold it.

The second insert blocks inside the database (no busy-waiting: the thread sits in the JDBC call) until the first
transaction ends:

* the first commits: the second insert fails with a duplicate key;
* the first rolls back: its row is gone, so the second insert succeeds and that unit of work carries on;
* the database's lock timeout expires first: the second insert fails with a lock timeout or deadlock.

A duplicate key, lock timeout or deadlock is reported as a Match Claim conflict.
The loser retries, and its new search sees what the winner committed. This is the pattern
conditional creates use with the search URL table.

Units of work that share no Match Claim never touch the same row, so sources that don't match each other still run in
parallel.

### What Is Claimed

The keys are built by `MdmMatchClaimKeySvc`. In each unit of work the **source** is the resource being processed and
the **candidates** are what its search finds.

| Match Claim | Key | Taken | Why |
|---|---|---|---|
| The source | `PID\|<type>\|<pid>` | before the search | serializes two units of work for the same source |
| Each external EID of the source | `EID\|<type>\|<system>\|<value>` | before the search | sources sharing an EID must agree on one golden resource |
| Each candidate source with no MATCH link yet | `PID\|<type>\|<pid>` | on a restart, before the repeated search | the candidate may be mid-processing on another thread |

The Match Claim on a candidate and that candidate's own source Match Claim have the same key, so a unit of work that finds an unlinked
candidate collides with whichever unit of work is processing that candidate.

Candidates that already have a MATCH link do not need to be claimed: their golden resource is committed,
so the outcome is already stable.

#### On Partitions

Match Claims are not scoped by partition. The same source or EID can reach MDM with its partition given by id, by name or
not at all, so a partition-scoped key could let two competing units of work miss each other. PIDs are unique across
partitions. A shared EID value in two partitions only makes those units of work wait for each other; each search
still runs in its own partition. Hash collisions likewise only cause extra waiting.

### Life of a Match Claim

- **Created:** inserted at the start of the unit of work's transaction (`IMdmMatchClaimSvc.claim`), in sorted key
  order so that two units of work claiming overlapping sets don't deadlock. The insert is flushed at once, so a
  conflict surfaces before the search rather than at commit.
- **Held:** until the transaction ends. If it rolls back, the insert is undone and the Match Claim is gone.
- **Kept after commit:** a committed Match Claim is not deleted. The next unit of work for the same key takes it over.
  - The hash names *what* is claimed (the key) and never changes. The token is a random number that names *which*
    Match Claim holds the key; every new Match Claim gets a fresh one, like a version number.
  - Takeover: `findExistingClaims` reads the stale row `(hash H, token T1)`. `claim` then deletes the row matching H
    *and* T1, and inserts `(H, T2)`.
  - Finding an old row is not a collision. A collision is two units of work trying to hold a row for H at the same
    time:
    - Plain race: another unit of work has inserted `(H, …)` and not committed. The insert hits the primary key,
      waits, then conflicts.
    - Takeover race: two units of work both read `(H, T1)`. The first deletes T1 and inserts `(H, T3)`. The second's
      delete matches nothing, because T1 is gone, and its insert hits `(H, T3)`, then conflicts.
  - The token check is what makes the takeover race safe: without it, the second unit of work would delete the first
    one's new row as if it were stale, and both would hold the key.
- **Purged:** `MdmMatchClaimMaintenanceSvcImpl` runs a clustered job every minute that deletes Match Claims older than
  `matchClaimRetentionMillis` (default 10 minutes). The retention must be longer than the longest MDM unit of work.

### Sequence: Two Sources Sharing an EID

The common case. A and B arrive together and both claim `EID|npi-1`.

```mermaid
sequenceDiagram
    participant A as Thread A (source A)
    participant DB as MPI_MATCH_CLAIM
    participant B as Thread B (source B)
    A->>DB: insert EID|npi-1, PID|A (sorted, flush)
    DB-->>A: ok, A holds Match Claims on EID|npi-1 and PID|A
    B->>DB: insert EID|npi-1, PID|B (flush)
    Note over B,DB: B's insert waits on A's uncommitted row
    A->>A: search, no golden resource, create GR-1, link A
    A->>DB: commit
    DB-->>B: duplicate key, so ResourceVersionConflictException
    Note over B: roll back, retry (the first retry runs at once)
    B->>DB: read tokens, take over EID|npi-1, insert PID|B
    DB-->>B: ok
    B->>B: search finds GR-1 by EID, link B to GR-1
    B->>DB: commit
```

### Sequence: Matching Sources without EIDs (Candidate Restart)

A and B match on name and birth date only, so their initial Match Claims don't overlap. The Match Claim on the candidate forces the
collision.

```mermaid
sequenceDiagram
    participant A as Thread A (source A)
    participant DB as MPI_MATCH_CLAIM
    participant B as Thread B (source B)
    A->>DB: insert PID|A
    B->>DB: insert PID|B
    A->>A: search finds candidate B, no MATCH link yet, unclaimed
    Note over A: roll back (a restart, not a conflict), run again with PID|B added
    B->>B: search finds candidate A, unlinked, unclaimed
    Note over B: roll back (a restart, not a conflict), run again with PID|A added
    A->>DB: insert PID|A, PID|B (sorted)
    DB-->>A: ok
    B->>DB: insert PID|A, PID|B (sorted)
    Note over B,DB: waits on PID|A, held by A
    A->>A: search, B still has no golden resource, create GR-1, link A
    A->>DB: commit
    DB-->>B: duplicate key, retry
    B->>B: search finds A on GR-1, link B to GR-1
    B->>DB: commit
```

Which thread wins depends on timing; either way there is one golden resource. A unit of work restarts at most
`MAX_CLAIM_RESTARTS` (5) times. If it still finds unclaimed candidates after that, it adds them to its Match Claims and
fails the attempt with a conflict, so it is retried like any other conflict (see "When the Limits Run Out").

### Retries and Restarts

`MdmMatchLinkSvc.updateMdmLinksForMdmSource` chooses the path:

| Situation | Path | On a Match Claim conflict                                                                                                                                 |
|---|---|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| Match Claims disabled (`matchClaimsEnabled=false`) | `doMdmUpdateWithoutClaims` | n/a (no conflicts; possibly duplicate GRs created)                                                                                                        |
| No transaction active (the MDM channel consumer) | `doMdmUpdateWithClaims`, one new transaction per attempt | rolls back and retries, up to `maxRetriesOnConflict` (default 10), backing off with jitter                                                                |
| Already in a transaction (`$mdm-update-link` re-matching a source left with no MATCH) | `doMdmUpdateInExistingTransaction`, joins it | can't restart someone else's transaction: claims new candidates in place, searches once more, and throws `ResourceVersionConflictException` to the caller |

Each attempt starts from a checkpoint of the `MdmTransactionContext` and a fresh copy of the source, so a rolled-back
attempt leaves no links, log messages or HAPI EIDs behind.

### When the Limits Run Out

| Limit | What happens |
|---|---|
| Conflict retries (`maxRetriesOnConflict`), consumer path | The last conflict is thrown. Every attempt rolled back, so the source has no links. `MdmMessageListener` logs the error and does not rethrow, so the message is not redelivered. The source stays unprocessed until it is resubmitted (`$mdm-submit`) or updated. |
| Restarts (`MAX_CLAIM_RESTARTS`, 5) | The attempt fails with a conflict instead of deciding without the remaining candidates. The retry claims them too. If new candidates keep appearing, each attempt fails the same way until the conflict retries run out (row above), so the worst case is an unprocessed source, never a duplicate golden resource. |
| Joined transaction (`$mdm-update-link`) | No retries at all: the first conflict throws `ResourceVersionConflictException` to the caller, and the whole link update rolls back. |

## Settings

`matchClaimsEnabled`, `matchClaimRetentionMillis` and `maxRetriesOnConflict` on `IMdmSettings`. The user-facing
description is under "Concurrent MDM Processing" in `server_jpa_mdm/mdm_details.md`.

## Key Classes

| Class | Module | Role |
|---|---|---|
| `IMdmMatchClaimSvc`, `MdmMatchClaimKey`, `NoOpMdmMatchClaimSvc` | `hapi-fhir-server-mdm` | the Match Claim contract and key |
| `MdmMatchClaimSvcJpaImpl`, `MdmMatchClaimEntity`, `MdmMatchClaimMaintenanceSvcImpl` | `hapi-fhir-jpaserver-base` | JPA storage and the purge job |
| `MdmMatchClaimKeySvc`, `MdmMatchLinkSvc` | this module | which keys to claim, and the attempt/retry/restart loop |
