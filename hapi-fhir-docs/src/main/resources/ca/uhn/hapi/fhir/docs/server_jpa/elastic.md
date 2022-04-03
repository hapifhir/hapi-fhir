# HAPI FHIR JPA Lucene/Elasticsearch Indexing

The HAPI JPA Server supports optional indexing via Hibernate Search when configured to use Lucene or Elasticsearch.
This is required to support the `_content`, or `_text` search parameters.

# Experimental Extended Lucene/Elasticsearch Indexing

Additional indexing is implemented for simple search parameters of type token, string, and reference.
These implement the basic search, as well as several modifiers:
This **experimental** feature is enabled via the `setAdvancedLuceneIndexing()` property of DaoConfig.

## String search

The Extended Lucene string search indexing supports the default search, as well as the modifiers defined in https://www.hl7.org/fhir/search.html#string.
- Default searching matches by prefix, insensitive to case or accents
- `:exact` matches the entire string, matching case and accents
- `:contains` extends the default search to match any substring of the text
- `:text` provides a rich search syntax as using the Simple Query Syntax as defined by 
[Lucene](https://lucene.apache.org/core/8_10_1/queryparser/org/apache/lucene/queryparser/simple/SimpleQueryParser.html) and 
[Elasticsearch](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-simple-query-string-query.html#simple-query-string-syntax).

## Token search

The Extended Lucene Indexing supports the default token search by code, system, or system+code, 
as well as with the `:text` modifier.
The `:text` modifier provides the same Simple Query Syntax used by string `:text` searches.
See https://www.hl7.org/fhir/search.html#token.

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
Note: This does not support the $meta-add or $meta-delete operations.
