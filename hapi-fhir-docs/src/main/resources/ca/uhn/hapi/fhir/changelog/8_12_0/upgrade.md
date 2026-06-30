# Upgrade Notes

## Removal of Deprecated Default-Partition API Methods

This release removes a set of previously-deprecated methods related to default-partition resolution. Each of these
methods assumed that a `null` partition ID meant "the default partition", which silently ignored any non-null
configured default partition ID. Callers must migrate to the replacements below so that the configured default
partition ID is honored.

| Removed method | Replacement |
| -------------- | ----------- |
| `RequestPartitionId.defaultPartition()` | `RequestPartitionId.defaultPartition(myPartitionSettings)` (or, `myPartitionSettings.getDefaultRequestPartitionId()`) |
| `RequestPartitionId.defaultPartition(LocalDate)` | `RequestPartitionId.defaultPartition(IDefaultPartitionSettings)`, applying the partition date separately if required |
| `PartitionablePartitionId.toRequestPartitionId(PartitionablePartitionId)` | `PartitionablePartitionId.toRequestPartitionId(PartitionablePartitionId, IDefaultPartitionSettings)` |

`IDefaultPartitionSettings` is implemented by `PartitionSettings`, so most call sites can pass the `PartitionSettings`
instance they already hold.

## Deprecation of `HapiTransactionService.executeWithDefaultPartitionInContext`

The static method `HapiTransactionService.executeWithDefaultPartitionInContext(ICallable)` is now deprecated for
removal. It binds a `null` partition ID to the thread, which assumes `null` means "the default partition" and
silently ignores any non-null configured default partition ID. Its behavior is unchanged in this release so existing
callers are not broken.

Callers should migrate to the new instance method
`HapiTransactionService.executeWithConfiguredDefaultPartitionInContext(ICallable)`, which honors the default partition
ID configured in `PartitionSettings`.

## Per-Thread Database Id Pooling

Database id generation can now be configured to allocate ids from a per-thread pool, instead of a single pool shared by
all writer threads on a server. This removes lock contention between concurrent writers when the pool is refilled, and
improves write throughput under high write concurrency.

The single shared pool remains the default, so existing deployments are unaffected on upgrade. Per-thread pooling can
be enabled with `JpaStorageSettings#setIdSequencePoolingStrategy(IdSequencePoolingStrategy.PER_THREAD_POOL)`.

Internal ids have never reflected creation order across servers in a cluster, since each server allocates ids from its
own block. With per-thread pooling this is also true between threads on a single server: an id assigned later on one
thread may be lower than one assigned earlier on another. Code must not treat the numeric internal id as a
creation-order signal; use the last-updated time instead.

### Critical: do not run the shared-pool and per-thread behaviors against the same database at the same time

The shared-pool behavior and the per-thread behavior interpret the same database sequence value differently - the
shared-pool behavior treats it as the top of an id block, the per-thread behavior treats it as the bottom. If both run
concurrently against the same database, the id blocks they hand out **overlap, producing duplicate primary keys and
corrupting data**. This is not a transient or self-correcting condition. You must ensure the two behaviors never operate
against the same database at the same time.

Because the default is unchanged, a simple upgrade does not hit this collision. The risk arises only if you enable
per-thread pooling on a multi-node cluster. To enable it safely, turn it on across the whole cluster in a single
coordinated restart, never node by node, so that the two behaviors never overlap. The single change in interpretation
produces at most a one-time gap of unused ids, which is harmless.
