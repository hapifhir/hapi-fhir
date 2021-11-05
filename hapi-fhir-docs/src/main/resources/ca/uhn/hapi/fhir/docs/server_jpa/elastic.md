# HAPI FHIR JPA Lucene/Elasticsearch Indexing

The HAPI JPA Server supports optional indexing via Hibernate Search when configured to use Lucene or Elasticsearch.
This is required to support the `_content`, or `_text` search parameters.

# Experimental Advanced Lucene/Elasticsearch Indexing

Additional indexing is implemented for simple search parameters of type token, string, and reference.
These implement the basic search, as well as several modifiers:
This **experimental** feature is enabled via the `setAdvancedLuceneIndexing()` property of DaoConfig.

## String search

The Advanced Lucene string search indexing supports the default search, as well as the modifiers defined in https://www.hl7.org/fhir/search.html#string.
- Default searching matches by prefix, insensitive to case or accents
- `:exact` matches the entire string, matching case and accents
- `:contains` extends the default search to match any substring of the text
- `:text` provides a rich search syntax as using the Simple Query Syntax as defined by 
[Lucene](https://lucene.apache.org/core/8_10_1/queryparser/org/apache/lucene/queryparser/simple/SimpleQueryParser.html) and 
[Elasticsearch](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-simple-query-string-query.html#simple-query-string-syntax).

## Token search

The Advance Lucene indexing supports the default token search by code, system, or system+code, 
as well as with the `:text` modifier.
The `:text` modifier provides the same Simple Query Syntax used by string `:text` searches.
See https://www.hl7.org/fhir/search.html#token.



