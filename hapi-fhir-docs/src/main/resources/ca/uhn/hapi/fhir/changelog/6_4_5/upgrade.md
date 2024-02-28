This release fixes an accidental behaviour that was introduced in 6.4.2. From that version up until now, if a Tag Definition was created with a null `userSelected` element,
it would still be stored as `false` instead of `null`. This release fixes that behaviour, and now correctly stores the value as `null` if it is not specified. If you do not use this field, no action needs to be taken. However, if you do use this field, the `userSelected` elements stored from the installation of version 2023.02.R02 up until now are potentially suspect. The following SQL can be executed to clear the `false` value from this table and replace it with null, if desired:

```sql
update HFJ_TAG_DEF 
set TAG_USER_SELECTED = null
where TAG_USER_SELECTED = 'false'
```
This will wholesale replace all `userSelected` fields.

