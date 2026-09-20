# Upgrade Notes

## Jackson 3

HAPI FHIR now depends on Jackson 3 instead of Jackson 2. Jackson packages move from
`com.fasterxml.jackson` to `tools.jackson`, and several Jackson defaults changed. See the
[Jackson 3.0 release notes](https://github.com/FasterXML/jackson/wiki/Jackson-Release-3.0).

## Partition security for system-level operations

The `STORAGE_PARTITION_SELECTED` pointcut is now invoked for operations with no resource type (system-level
`_history`, paging, transactions, server-level operations). Interceptors hooking it must handle a `null`
`RuntimeResourceDefinition`, and these operations are now subject to partition security checks.
