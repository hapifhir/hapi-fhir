# Rest Operations: Search

This page describes how to add various FHIR search features to your resource/plain providers. 

# Search with No Parameters

The following example shows a search with no parameters. This operation should return all resources of a given type (this obviously doesn't make sense in all contexts, but does for some resource types).

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|searchAll}}
``` 

Example URL to invoke this method:

```url
http://fhir.example.com/Patient
```

# Search Parameters: String Introduction

To allow a search using given search parameters, add one or more parameters to your search method and tag these parameters as either [@RequiredParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/RequiredParam.html) or [@OptionalParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/OptionalParam.html).

This annotation takes a "name" parameter which specifies the parameter's name (as it will appear in the search URL). FHIR defines standardized parameter names for each resource, and these are available as constants on the individual HAPI resource classes.

Parameters which take a string as their format should use the [StringParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/param/StringParam.html) type. They may also use normal java `String`, although it is not possible to use modifiers such as the `:exact` modifier in that case.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|searchStringParam}}
``` 

Example URL to invoke this method:

```url
http://fhir.example.com/Patient?family=SMITH
```

# Search Parameters: Token/Identifier

The "token" type is used for parameters which have two parts, such as an identifier (which has a system URI, as well as the actual identifier) or a code (which has a code system, as well as the actual code). For example, the search below can be used to search by identifier (e.g. search for an MRN).

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|searchIdentifierParam}}
``` 

Example URL to invoke this method:

```url
http://fhir.example.com/Patient?identifier=urn:foo|7000135
```

# Search Parameters: Date (Simple)

The FHIR specification provides a syntax for specifying dates+times (but for simplicity we will just say dates here) as search criteria.

Dates may be optionally prefixed with a qualifier. For example, the string `=ge2011-01-02` means any date on or after 2011-01-02.

To accept a qualified date parameter, use the [DateParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/param/DateParam.html) parameter type.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|dates}}
``` 

Example URL to invoke this method:

```url
http://fhir.example.com/Observation?birthdate=gt2011-01-02
```

Invoking a client of this type involves the following syntax:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|dateClient}}
``` 

<a name="DATE_RANGES" />

# Search Parameters: Date (Ranges)

A common scenario in searches is to allow searching for resources with values (i.e timestamps) within a range of dates.

FHIR allows for multiple parameters with the same key, and interprets these as being an ***AND*** set. So, for example, a range of `&date=gt2011-01-01&date=lt2011-02-01` can be interpreted as any date within January 2011.

The following snippet shows how to accept such a range, and combines it with a specific identifier, which is a common scenario. (i.e. Give me a list of observations for a specific patient within a given date range). This is accomplished using a single parameter of type [DateRangeParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/param/DateRangeParam.html).

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|dateRange}}
``` 

Example URL to invoke this method:

```url
http://fhir.example.com/Observation?subject.identifier=7000135&date=gt2011-01-01&date=lt2011-02-01
```

Invoking a client of this type involves the following syntax:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|dateClient}}
``` 

## Unbounded Date Ranges

Note that when using a date range parameter, it is also possible for the client to request an *unbounded* range. In other words, a range that only a start date and no end date, or vice versa.

An example of this might be the following URL, which refers to any Observation resources for the given MRN and having a date after 2011-01-01.

```url
http://fhir.example.com/Observation?subject.identifier=7000135&date=gt2011-01-01
```

When such a request is made of a server (or to make such a request from a client), the `getLowerBound()` or `getUpperBound()` property of the [DateRangeParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/param/DateRangeParam.html) object will be set to `null`.

# Search Parameters: Quantity

Quantity parameters allow a number with units and a comparator.

The following snippet shows how to accept such a range, and combines it with a specific identifier, which is a common scenario. (i.e. Give me a list of observations for a specific patient within a given date range)

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|quantity}}
``` 

Example URL to invoke this method:

```url
http://fhir.example.com/Observation?value-quantity=lt123.2||mg|http://unitsofmeasure.org
```

# Search Parameters: Resource Reference

Many search parameters refer to resource references. For instance, the Patient parameter "provider" refers to the resource marked as the managing organization for patients.

Reference parameters use the [ReferenceParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/param/ReferenceParam.html) type. Reference parameters are, in their most basic form, just a pointer to another resource. For example, you might want to query for DiagnosticReport resources where the subject (the Patient resource that the report is about) is Patient/123. The following example shows a simple resource reference parameter in use.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|referenceSimple}}
``` 

<a name="filter"/>

# Search Parameters: Filter

To implement the FHIR [_filter](http://hl7.org/fhir/search_filter.html) search style, you may create a parameter of type `StringParam` (or one of the and/or derivatives), as shown below.

```java
@OptionalParam(name=ca.uhn.fhir.rest.api.Constants.PARAM_FILTER)
StringAndListParam theFtFilter
```  

# Chained Resource References

References may also support a "chained" value. This is a search parameter name on the target resource. For example, you might want to search for DiagnosticReport resources by subject, but use the subject's last name instead of their resource ID. In this example, you are chaining "family" (the last name) to "subject" (the patient).

The net result in the query string would look like:

```url
http://fhir.example.com/DiagnosticReport?subject.family=SMITH
```

What this query says is "fetch me all of the DiagnosticReport resources where the **subject** (Patient) of the report has the **family** (name) of 'SMITH'".

There are two ways of dealing with chained parameters in your methods: static chains and dynamic chains. Both are equally valid, although dynamic chains might lead to somewhat more compact and readable code.

<a name="dynamic_chains"/>

## Dynamic Chains

Chained values must be explicitly declared through the use of a whitelist (or blacklist). The following example shows how to declare a report with an allowable chained parameter:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|referenceWithChain}}
``` 

You may also specify the whitelist value of <code>""</code> to allow an empty chain (e.g. the resource ID) and this can be combined with other values, as shown below:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|referenceWithChainCombo}}
``` 

If you are handling multiple types of chained parameters in a single method, you may want to convert the reference parameter type into something more convenient before using its value. The following example shows how to do that.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|referenceWithDynamicChain}}
``` 

## Static Chains

It is also possible to explicitly state a chained value right in the parameter name. This is useful if you want to only support a search by a specific given chained parameter. It has the added bonus that you can use the correct parameter type of the chained parameter (in this case a TokenParameter because the Patient.identifier parameter is a token).

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|referenceWithStaticChain}}
``` 

# Search Parameters: Composite

Composite search parameters incorporate two parameters in a single value. Each of those parameters will themselves have a parameter type.

In the following example, Observation.name-value-date is shown. This parameter is a composite of a string and a date. Note that the composite parameter types (StringParam and DateParam) must be specified in both the annotation's `compositeTypes` field, as well as the generic types for the `CompositeParam` method parameter itself.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|searchComposite}}
``` 

Example URL to invoke this method:

```url
http://fhir.example.com/Observation?name-value-date=PROCTIME$2001-02-02
```

# Combining Multiple Parameters

Search methods may take multiple parameters, and these parameters may (or may not) be optional. To add a second required parameter, annotate the parameter with [@RequiredParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/RequiredParam.html). To add an optional parameter (which will be passed in as `null` if no value is supplied), annotate the method with [@OptionalParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/OptionalParam.html).

You may annotate a method with any combination of as many [@RequiredParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/RequiredParam.html) and as many [@OptionalParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/OptionalParam.html) parameters as you want. It is valid to have only [@RequiredParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/RequiredParam.html) parameters, or only [@OptionalParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/OptionalParam.html) parameters, or any combination of the two.

If you wish to create a server that can accept any combination of a large number of parameters, (this is how the various reference servers behave, as well as the [Public HAPI Test Server](http://hapi.fhir.org)). The easiest way to accomplish this is to simply create one method with all allowable parameters, each annotated as [@OptionalParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/OptionalParam.html).

On the other hand, if you have specific combinations of parameters you wish to support (a common scenario if you are building FHIR on top of existing data sources and only have certain indexes you can use) you could create multiple search methods, each with specific required and optional parameters matching the database indexes.

The following example shows a method with two parameters.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|searchOptionalParam}}
``` 

Example URLs to invoke this method:

```url
http://fhir.example.com/Patient?family=SMITH
http://fhir.example.com/Patient?family=SMITH&given=JOHN
```

# Multi-Valued (AND/OR) Parameters

It is possible to accept multiple values of a single parameter as well. This is useful in cases when you want to return a list of resources with criteria matching a list of possible values. See the [FHIR Specification](http://www.hl7.org/implement/standards/fhir/search.html#combining) for more information.

The FHIR specification allows two types of composite parameters:

* Where a parameter may accept multiple comma separated values within a single value string (e.g.
`?language=FR,NL`) this is treated as an ***OR*** relationship, and the search should return elements matching either one or the other.

* Where a parameter may accept multiple value strings for the same parameter name (e.g. `?language=FR&language=NL`) this is treated as an ***AND*** relationship, and the search should return only elements matching both.

It is worth noting that according to the FHIR specification, you can have an AND relationship combining multiple OR relationships, but not vice-versa. In other words, it's possible to support a search like
`("name" = ("joe" or "john")) AND ("age" = (11 or 12))` but not a search like `("language" = ("en" AND "fr") OR ("address" = ("Canada" AND "Quebec"))`. If you wish to support the latter, you may consider implementing the [_filter parameter](#filter). 

## OR Relationship Query Parameters

To accept a composite parameter, use a parameter type which implements the [IQueryParameterOr](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/model/api/IQueryParameterOr.html) interface.

Each parameter type ([StringParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/param/StringParam.html), [TokenParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/param/TokenParam.html), etc.) has a corresponding parameter which accepts an ***OR*** list of parameters. These types are called "[type]OrListParam", for example: [StringOrListParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/param/StringOrListParam.html) and [TokenOrListParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/param/TokenOrListParam.html).

The following example shows a search for Observation by name, where a list of names may be passed in (and the expectation is that the server will return Observations that match any of these names):

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|searchMultiple}}
``` 

Example URL to invoke this method:

```url
http://fhir.example.com/Observation?name=urn:fakenames|123,urn:fakenames|456
```

## AND Relationship Query Parameters

To accept a composite parameter, use a parameter type which implements the [IQueryParameterAnd](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/model/api/IQueryParameterAnd.html) interface (which in turn encapsulates the corresponding IQueryParameterOr types).

An example follows which shows a search for Patients by address, where multiple string lists may be supplied by the client. For example, the client might request that the address match `("Montreal" OR "Sherbrooke") AND ("Quebec" OR "QC")` using the following query:

```url
http://fhir.example.com/Patient?address=Montreal,Sherbrooke&address=Quebec,QC
```

The following code shows how to receive this parameter using a [StringAndListParameter](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/param/StringAndListParam.html), which can handle an AND list of multiple OR lists of strings.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|searchMultipleAnd}}
``` 

Note that AND parameters join multiple OR parameters together, but the inverse is not true. In other words, it is possible in FHIR to use AND search parameters to specify a search criteria of `(A=1 OR A=2) AND (B=1 OR B=2)` but it is not possible to specify `(A=1 AND B=1) OR (A=2 AND B=2)` (aside from
in very specific cases where a composite parameter has been specifically defined).

# AND Relationship Query Parameters for Dates

Dates are a special case, since it is a fairly common scenario to want to match a date range (which is really just an AND query on two qualified date parameters). See the section on [date ranges](#DATE_RANGES) for an example of a [DateRangeParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/param/DateRangeParam.html).

# Resource Includes (_include)

FHIR allows clients to request that specific linked resources be included as contained resources, which means that they will be "embedded" in a special container called "contained" within the parent resource.

HAPI allows you to add a parameter for accepting includes if you wish to support them for specific search methods.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|pathSpec}}
``` 

Example URL to invoke this method:

```url
http://fhir.example.com/DiagnosticReport?identifier=7000135&_include=DiagnosticReport.subject
```

It is also possible to use a String type for the include parameter, which is more convenient if only a single include (or null for none) is all that is required.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|pathSpecSimple}}
``` 

# Reverse Resource Includes (_revinclude)

To add support for reverse includes (via the `_revinclude` parameter), use the same format as with the `_include` parameter (shown above) but add `reverse=true` to the [@IncludeParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/IncludeParam.html) annotation, as shown below.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|revInclude}}
``` 

# Named Queries (_query)

FHIR supports [named queries](http://www.hl7.org/implement/standards/fhir/search.html#advanced), which may have specific behaviour defined. The following example shows how to create a Search operation with a name.

This operation can only be invoked by explicitly specifying the given query name in the request URL. Note that the query does not need to take any parameters.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|searchNamedQuery}}
``` 

Example URL to invoke this method:

```url
http://fhir.example.com/Patient?_query=namedQuery1&someparam=value
```

# Sorting (_sort)

FHIR supports [sorting](http://www.hl7.org/implement/standards/fhir/search.html#sort) according to a specific set of rules.

According to the specification, sorting is requested by the client using a search param as the sort key. For example, when searching Patient resources, a sort key of "given" requests the "given" search param as the sort key. That param maps to the values in the field "Patient.name.given".

Sort specifications can be passed into handler methods by adding a parameter of type [SortSpec](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/api/SortSpec.html), which has been annotated with the [@Sort](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Sort.html) annotation, as shown in the following example:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|sort}}
``` 

Example URL to invoke this method:

```url
http://fhir.example.com/Patient?identifier=urn:foo|123&_sort=given
```

<a name="limiting-results"/>

# Limiting results (`_count`)

FHIR supports [Page Count](http://www.hl7.org/implement/standards/fhir/search.html#count). Count specification may be passed into handler methods with
[@Count](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Count.html) annotation. Count may be used to limit the number
of resources fetched from the database.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|count}}
``` 

Example URL to invoke this method:

```url
http://fhir.example.com/Patient?identifier=urn:foo|123&_count=10
```

# Paging

## Offset paging with `_offset`

**Warning:** Using `_offset` without sorting can result in duplicate entries to show up across the different pages when
following the next page link provided on each page.

HAPI FHIR supports also paging. Offset specification can be passed into handler methods
with [@Offset](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Offset.html) annotation.
This annotation is *not* part of the FHIR standard.

There are two possible ways to use paging. It is possible to define `_offset` parameter in the
request which means that when combined with `_count` the paging is done on the database level. This type of
paging benefits from not having to return so many items from the database when paging items. It's also possible
to define default page size (i.e. default `_count` if not given) and maximum page size (i.e. maximum value
for the `_count` parameter).
See [RestfulServer](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/RestfulServer.html)
for more information.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|offset}}
``` 

Example URL to invoke this method for the first page:

```url
http://fhir.example.com/Patient?identifier=urn:foo|123&_count=10&_offset=0
```
or just
```url
http://fhir.example.com/Patient?identifier=urn:foo|123&_count=10
```

Example URL to invoke this method for the second page:

```url
http://fhir.example.com/Patient?identifier=urn:foo|123&_count=10&_offset=10
```

Note that if the paging provider is configured to be database backed, `_offset=0` behaves differently than no `_offset`. This 
allows the user the choose if he wants offset paging or database backed paging.

## Using paging provider

It is also possible to implement own paging provider (or use implementation bundled in HAPI FHIR). See [Paging](./paging.html) for information on how to use paging provider.

# Adding Descriptions

It is also possible to annotate search methods and/or parameters with the [@Description](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/model/api/annotation/Description.html) annotation. This annotation allows you to add a description of the method and the individual parameters. These descriptions will be placed in the server's conformance statement, which can be helpful to anyone who is developing software against your server.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|searchWithDocs}}
``` 
