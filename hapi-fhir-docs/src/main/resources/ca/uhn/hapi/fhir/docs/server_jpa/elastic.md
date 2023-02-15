# HAPI FHIR JPA Lucene/Elasticsearch Indexing

The HAPI JPA Server supports optional indexing via Hibernate Search when configured to use Lucene or Elasticsearch.
This is required to support the `_content`, or `_text` search parameters.

# Experimental Extended Lucene/Elasticsearch Indexing

Additional indexing is implemented for simple search parameters of type token, string, and reference.
These implement the basic search, as well as several modifiers:
This **experimental** feature is enabled via the `setAdvancedHSearchIndexing()` property of JpaStorageSettings.

## Search Parameter Support

Extended Lucene Indexing supports all of the [core search parameter types](https://www.hl7.org/fhir/search.html).
These include:
- Number
- Date/DateTime
- String
- Token
- Reference
- Composite
- Quantity
- URI

## Date search

We support date searches using the eq, ne, lt, gt, ge, and le comparisons.  
See https://www.hl7.org/fhir/search.html#date.

## String search

The Extended Lucene string search indexing supports the default search, as well as `:contains`, `:exact`, and `:text` modifiers.
- The default (unmodified) string search matches by prefix, insensitive to case or accents.
- `:exact` matches the entire string, matching case and accents.
- `:contains` match any substring of the text, ignoring case and accents.
- `:text` provides a rich search syntax as using a [modified Simple Query Syntax](#modified-simple-query-syntax). 

See https://www.hl7.org/fhir/search.html#string.

## Modified Simple Query Syntax

The `:text` modifier for token and string uses a modified version of the Simple Query Syntax provided by
[Lucene](https://lucene.apache.org/core/8_10_1/queryparser/org/apache/lucene/queryparser/simple/SimpleQueryParser.html) and
[Elasticsearch](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-simple-query-string-query.html#simple-query-string-syntax).
Terms are delimited by whitespace, or query punctuation `"'()|+`.  
Literal uses of these characters must be escaped by `&#92;`.
If the query contains any SQS query punctuation, the query is treated as a normal SQS query.
But when the query only contains one or more bare terms, and does not use any query punctuation, a modified syntax is used.
In modified syntax, each search term is converted to a prefix search to match standard FHIR string searching behaviour.
When multiple terms are present, they must all match (i.e. `AND`).
For `OR` behaviour use the `|` operator between terms.
To match only whole words, but not match by prefix, quote bare terms with the `"` or `'` characters.

Examples:

| Fhir Query String | Executed Query    | Matches      | No Match       | Note                                    |
|-------------------|-------------------|--------------|----------------|-----------------------------------------|
| Smit              | Smit*             | John Smith   | John Smi       |                                         |
| Jo Smit           | Jo* Smit*         | John Smith   | John Frank     | Multiple bare terms are `AND`           |
| frank &vert; john | frank &vert; john | Frank Smith  | Franklin Smith | SQS characters disable prefix wildcard  |                               
| 'frank'           | 'frank'           | Frank Smith  | Franklin Smith | Quoted terms are exact match            |

## Token search

The Extended Lucene Indexing supports the default token search by code, system, or system+code, 
as well as with the `:text` modifier.
The `:text` modifier provides the same [modified Simple Query Syntax](#modified-simple-query-syntax) used by string `:text` searches.  
See https://www.hl7.org/fhir/search.html#token.

## Supported Common and Special Search Parameters
| Parameter    | Supported | type   |
|--------------|-----------|--------|
| _id          | no        |        |
| _lastUpdated | yes       | date   | 
| _tag         | yes       | token  |
| _profile     | yes       | URI    |
| _security    | yes       | token  |
| _text        | yes       | string |
| _content     | yes       | string |
| _list        | no        |        |
| _has         | no        |        |
| _type        | no        |        |
| _source      | yes       | URI    |

## ValueSet autocomplete extension

The Extended Lucene Indexing supports an extension of the `$expand` operation on ValueSet with
a new `contextDirection` value of `existing`.  In this mode, the `context` parameter is interpreted
as a SearchParameter reference (by resource type and code), and the `filter` is interpreted as a
query token.  The expansion will contain the most frequent `Coding` values matching the filter.
E.g. the query

    GET /ValueSet/$expand?contextDirection=existing&context=Observation.code:text&filter=press

will return a ValueSet containing the most common values indexed under `Observation.code` whose
display text contains a word starting with "press", such as `http://loinc.org|8478-0` - "Mean blood pressure".
This extension is only valid at the type level, and requires that Extended Lucene Indexing be enabled.

## Resource Storage

As an experimental feature with the extended indexing, the full resource can be stored in the 
search index.  This allows some queries to return results without using the relational database.
Note: This does not support the $meta-add or $meta-delete operations. Full reindexing is required 
when this option is enabled after resources have been indexed.

This **experimental** feature is enabled via the `setStoreResourceInHSearchIndex()` option of JpaStorageSettings.

# Synchronous Writes

ElasticSearch writes are asynchronous by default. This means that when writing to an ElasticSearch instance (independent of HAPI FHIR), the data you write will not be available to subsequent reads for a short period of time while the indexes synchronize.

ElasticSearch states that this behaviour leads to better overall performance and throughput on the system.

This can cause issues, particularly in unit tests where data is being examined shortly after it is written. 

You can force synchronous writing to them in HAPI FHIR JPA by setting the Hibernate Search [synchronization strategy](https://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/#mapper-orm-indexing-automatic-synchronization). This setting is internally setting the ElasticSearch [refresh=wait_for](https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-refresh.html) option. Be warned that this will have a negative impact on overall performance. THE HAPI FHIR TEAM has not tried to quantify this impact but the ElasticSearch docs seem to make a fairly big deal about it.

