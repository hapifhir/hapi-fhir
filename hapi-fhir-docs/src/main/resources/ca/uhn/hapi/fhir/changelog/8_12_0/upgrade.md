# Upgrade Notes

## Removal of Deprecated Default-Partition API Methods

This release removes a set of previously-deprecated methods related to default-partition resolution. Each of these
methods assumed that a `null` partition ID meant "the default partition", which silently ignored any non-null
configured default partition ID. Callers must migrate to the replacements below so that the configured default
partition ID is honored.

| Removed method | Replacement |
| -------------- | ----------- |
| `RequestPartitionId.defaultPartition()` | `RequestPartitionId.defaultPartition(IDefaultPartitionSettings)` (or `IDefaultPartitionSettings.getDefaultRequestPartitionId()`) |
| `RequestPartitionId.defaultPartition(LocalDate)` | `RequestPartitionId.defaultPartition(IDefaultPartitionSettings)`, applying the partition date separately if required |
| `PartitionablePartitionId.toRequestPartitionId(PartitionablePartitionId)` | `PartitionablePartitionId.toRequestPartitionId(PartitionablePartitionId, IDefaultPartitionSettings)` |

`IDefaultPartitionSettings` is implemented by `PartitionSettings`, so most call sites can pass the `PartitionSettings`
instance they already hold.
