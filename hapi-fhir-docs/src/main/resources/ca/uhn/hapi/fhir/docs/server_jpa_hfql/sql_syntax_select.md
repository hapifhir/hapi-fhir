# HFQL SQL Syntax: Selects

This module uses a proprietary flavour of SQL that is specific to HAPI FHIR. This syntax is referred to as **HFQL**, the _HAPI FHIR Query Language_.

HFQL is structurally similar to the [Firely Query Language](https://simplifier.net/docs/fql), although it also has differences. It is also designed to be similar to SQL.

HFQL keywords are case-insensitive, although expressions referring to elements within FHIR resources are case-sensitive.

# Selecting Columns

Unlike RDBMS tables, FHIR resources are document based with many repeating elements. Simply selecting columns like in standard SQL can be complicated by this structure. In order to provide flexibility, _SELECT_ expressions can be expressed as [FHIRPath Expressions](https://smilecdr.com/docs/fhir_standard/fhirpath_expressions.html). The _FROM_ keyword indicates which FHIR resource type to search for and return values from.

The following example searches for all Patient resources, and returns their respective ID, family name, and given name in 3 separate columns.

```sql
SELECT id, name.family, name.given
FROM Patient
```

If the FHIRPath expression leads to a repeatable element, the only first matching value is returned. For example, in the expression above the `Patient.name` element has cardinality _HumanName [0..*]_, and the `HumanName.given` element has the cardinality _string [0..*]_. This means that the first non-empty value for family and given will be returned.

You can use array index markers if you want to explicitly select a specific repetition:

```sql
SELECT id, name[0].family, name[0].given[0]
FROM Patient
```

# Controlling Column Names

If you want to control the column name for a given SELECT expression, prefix it with "[name]:". For example:

```sql
SELECT ID: id, 
       FamilyName: name[0].family, 
       GivenName: name[0].given[0],
       MiddleName: name[0].given[1]
FROM Patient
```

# Column Data Types and Choice[x] Elements

The HFQL processor will always try to use the most appropriate SQL datatype for column values that it returns. It does this by inspecting the FHIRPath expression and trying to determine the FHIR datatype associated with the leaf node in the path.

In the case of choice[x] elements, where the datatype can be one of several possibilities, use the FHIRPath [ofType(...)](https://hl7.org/fhirpath/#paths-and-polymorphic-items) operator to select the type you want to return. This also acts as a hint to the HFQL parser about which SQL datatype should be returned. In the example below, only Observation values with a Quantity type are returned, and the `Quantity.value` element is returned. Because this element has a FHIR _decimal_ datatype, this column is returned by the HFQL processor as a SQL Decimal value. 

```sql
SELECT
   id, 
   value.ofType(Quantity).value
FROM
   Observation
```

# Count and Group-By Expressions

The _COUNT_ operator can be used to return counts of various items.

Be warned that this is potentially a very slow operation, since the HFQL executor needs to examine every single Patient resource in the database.

If you are returning any other _SELECT_ expressions, you must also include those expressions in a corresponding _GROUP BY_ clause.

The following example returns a table containing each distinct family name in the database, and the number of times it appears.

```sql
SELECT COUNT(*), name.family
FROM Patient
GROUP BY name.family
```

# Ordering Results

The SQL _ORDER BY_ keywords can be used to suggest a specific order for the search results.

Note that ordering will currently only work on statements that return 10,000 rows or less. This limitation may be removed in a future release.

The following statement searches for Patients with a family name of _Simpson_, counts the frequencies of their first names, and orders by the most frequent names.

```sql
select name.given, count(*)
from Patient
search family = 'Simpson'
group by name.given
order by count(*) desc
```
