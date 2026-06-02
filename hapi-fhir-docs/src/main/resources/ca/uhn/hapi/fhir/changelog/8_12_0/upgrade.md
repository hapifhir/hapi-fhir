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
