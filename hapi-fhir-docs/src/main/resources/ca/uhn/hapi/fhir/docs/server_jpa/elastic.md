# HAPI FHIR JPA Lucene/Elasticsearch Indexing

HAPI FHIR supports additional indexing when an appropriate indexer is configured. There are two FullText indexers supported: Lucene, and ElasticSearch. These systems do not replace a relational database (RDBMS), which is always required in order to use HAPI FHIR JPA. Instead, they augment the relational database by enabling additional search functionality.

With FullText Indexing enabled, the following Search Parameters may be used:

* `_content` &ndash; Supports Full-Text searching across the textual components in a FHIR resource (i.e. the values of any **String** datatypes found within the resource).
* `_text` &ndash; Supports Full-Text searching across the HTML narrative of the results.

# Performing Fulltext Search in Lucene/Elasticsearch

When enabled, searches for `_text` and `_content` are forwarded to the underlying Hibernate Search engine, which can be backed by either Elasticsearch or Lucene. 
By default, search is supported in the way indicated in the [FHIR Specification on _text/_content Search](https://www.hl7.org/fhir/search.html#_text). This means that 
queries like the following can be evaluated: 

```http request
GET [base]/Observation?_content=cancer OR metastases OR tumor
```
To understand how this works, look at the following example. During ingestion, the fields required for `_content` and `_text` searches are stored in the backing engine, after undergoing normalization and analysis. For example consider this Observation: 

```json
{
  "resourceType" : "Observation",
  "code" : {
    "coding" : [{
      "system" : "http://loinc.org",
      "code" : "15074-8",
      "display" : "Glucose [Moles/volume] in Blood Found during patient's visit!"
    }]
  }
  "valueQuantity" : {
    "value" : 6.3,
    "unit" : "mmol/l",
    "system" : "http://unitsofmeasure.org",
    "code" : "mmol/L"
  }
}
```

In the display section, once parsed and analyzed, will result in the followings tokens being generated to be able to be searched on: 

```json
["glucose", "mole", "volume", "blood", "found", "during", "patient", "visit"]
```

You will notice that plurality is removed, and the text has been normalized, and special characters removed. When searched for, the search terms will be normalized in the same fashion. 

However, the default implementation will not allow you to search for an exact match over a long string that contains special characters or other characters which could be broken apart during tokenization. E.g. an exact match for `_content=[Moles/volume]` would not return this result.

In order to perform such an exact string match in Lucene/Elasticsearch, you should modify the `_text` or `_content` Search Parameter with the `:contains` modifier, as follows: 

```http request
GET [base]/Observation?_content:contains=[Moles/volume]
```

Using `:contains` on the `_text` or `_content` modifies the search engine to perform a direct substring match anywhere within the field.


<a name="customizing-fulltext"/>

# Customizing Content and Text Indexing

By default, HAPI FHIR JPA will index the two FullText parameters using the following algorithm. 

* For all resources, the `_content` parameter is indexed by extracting all of the **string** content (i.e. the values of all string datatypes within the resource) and aggregating them into a single document which is passed to the indexer.
* For all domain resources (all resources with a narrative), the `_text` parameter is indexed by aggregating all non-tag text in the resource narrative into a document which is passed to the indexer.


<a name="selective-enabling"/>

# Selectively Enabling Content and Text Indexing

You can selectively enable indexing and allowing the `_content` and/or `_text` parameters at the resource level by manually uploading a SearchParameter which explicitly specifies the resources which should be indexed.

Note that for this approach to work correctly:

* The canonical URL (SearchParameter.url) value must match the examples below exactly.
* The status (SearchParameter.status) value must be set to `active`.


<a name="ex_selective_content_indexing"/>

## Example: Selective Content Indexing

The following example shows a SearchParameter which limits the `_content` SearchParameter to indexing only specific resource types: 

```json
{
  "resourceType": "SearchParameter",
  "id": "Resource-content",
  "url": "http://hl7.org/fhir/SearchParameter/Resource-content",
  "name": "_content",
  "status": "active",
  "code": "_content",
  "base": [ "Observation", "Patient" ],
  "type": "string",
  "processingMode": "normal"
}
```


<a name="ex_selective_text_indexing"/>

## Example: Selective Text Indexing

The following example shows a SearchParameter which limits the `_text` SearchParameter to indexing only specific resource types:

```json
{
  "resourceType": "SearchParameter",
  "id": "Resource-text",
  "url": "http://hl7.org/fhir/SearchParameter/DomainResource-text",
  "name": "_text",
  "status": "active",
  "code": "_text",
  "base": [ "Observation", "Patient" ],
  "type": "string",
  "processingMode": "normal"
}
```

<a name="ex_completely_disable_individual"/>

## Example: Completely Disable Parameter

If you want to completely disable Content indexing (i.e. indexing the `_content` Search Parameter), you can set the status to `retired`:

```json
{
  "resourceType": "SearchParameter",
  "id": "Resource-content",
  "url": "http://hl7.org/fhir/SearchParameter/Resource-content",
  "name": "_content",
  "status": "retired",
  "code": "_content",
  "base": [ "Resource" ],
  "type": "string",
  "processingMode": "normal"
}
```

If you want to completely disable Text indexing (i.e. indexing the `_text` Search Parameter) you can set the status to `retired`:

```json
{
    "resourceType": "SearchParameter",
    "id": "Resource-text",
    "url": "http://hl7.org/fhir/SearchParameter/DomainResource-text",
    "name": "_text",
    "status": "retired",
    "code": "_text",
    "base": [ "Resource" ],
    "type": "string",
    "processingMode": "normal"
}
```

<a name="fulltext-interceptor"/>

# Customizing Content and Text Indexing by Interceptor

Using an interceptor with the [JPA_INDEX_EXTRACT_FULLTEXT_CONTENT](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#JPA_INDEX_EXTRACT_FULLTEXT_CONTENT) and/or [JPA_INDEX_EXTRACT_FULLTEXT_TEXT](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#JPA_INDEX_EXTRACT_FULLTEXT_TEXT) pointcuts, it is possible to customize the indexed documents, or to selectively disable indexing entirely.

These pointcuts use an object of type [FullTextExtractionRequest](/hapi-fhir/apidocs/hapi-fhir-jpaserver-searchparam/ca/uhn/fhir/jpa/searchparam/fulltext/FullTextExtractionRequest.html) as input, and an object of type [FullTextExtractionResponse](/hapi-fhir/apidocs/hapi-fhir-jpaserver-searchparam/ca/uhn/fhir/jpa/searchparam/fulltext/FullTextExtractionResponse.html) as output. The _JPA_INDEX_EXTRACT_FULLTEXT_CONTENT_ pointcut customizes indexing for the `_content` SearchParameter, and the _JPA_INDEX_EXTRACT_FULLTEXT_TEXT_ pointcut customizes indexing for the `_text` SearchParameter.

## Example

The following example shows an interceptor which enables full-text indexing only for specific resources, and controls which parts of resources are indexed:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/interceptor/FullTextSelectiveIndexingInterceptor.java|interceptor}}
```


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
| Parameter    | Supported | type                   |
|--------------|-----------|------------------------|
| _id          | no        |                        |
| _lastUpdated | yes       | date                   | 
| _tag         | yes       | token                  |
| _profile     | yes       | URI                    |
| _security    | yes       | token                  |
| _text        | yes       | string(R4) special(R5) |
| _content     | yes       | string(R4) special(R5) |
| _list        | no        |                        |
| _has         | no        |                        |
| _type        | no        |                        |
| _source      | yes       | URI                    |

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

# Sorting

It is possible to sort with Lucene indexing and full text searching enabled.  For example, this will work: `Practitioner?_sort=family`.

Also, chained sorts will work: `PractitionerRole?_sort=practitioner.family`.

However, chained sorting _combined_ with full text searches will fail.  For example, this query will fail with an error:  `PractitionerRole?_text=blah&_sort=practitioner.family`
