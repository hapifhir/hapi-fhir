This release has breaking changes.
* Hibernate Search mappings for Terminology entities have been upgraded, which requires terminology freetext reindexing.

To recreate Terminology freetext indexes use CLI command: `reindex-terminology`
