# LastN Operation

HAPI FHIR 5.1.0 introduced preliminary support for the `$lastn` operation described [here](http://hl7.org/fhir/observation-operation-lastn.html).

This implementation of the `$lastn` operation requires an external Elasticsearch server implementation which is used to implement the indexes required by this operation. The following sections describe the current functionality supported by this operation and the configuration needed to enable this operation.

# Functional Overview and Parameters

As described in the [FHIR specification](http://hl7.org/fhir/observation-operation-lastn.html), the `$lastn` can be used to retrieve the most recent or last n=number of observations for one or more subjects. This implementation supports the following search parameters:

* `subject=` or `patient=`: Identifier(s) of patient(s) to return Observation resources for. If not specified, returns most recent observations for all patients.
* `category=`: One or more category code search parameters used to filter Observations.
* `Observation.code=`: One or more `Observation.code` search parameters use to filter and group observations. If not specified, returns most recent observations for all `Observation.code` values.
* `date=`: Date search parameters used to filter Observations by `Observation.effectiveDtm`.
* `max=`: The maximum number of observations to return for each `Observation.code`. If not specified, returns only the most recent observation in each group.

# Limitations

Currently only Elasticsearch version 7.10.0 is officially supported.

Search parameters other than those listed above are currently not supported.

The grouping of Observation resources by `Observation.code` means that the `$lastn` operation will not work in cases where `Observation.code` has more than one coding.

# Deployment and Configuration

The `$lastn` operation is disabled by default. The operation can be enabled by setting the DaoConfig#setLastNEnabled
property (
see [JavaDoc](/hapi-fhir/apidocs/hapi-fhir-storage/ca/uhn/fhir/jpa/api/config/DaoConfig.html#setLastNEnabled(boolean)))
.

In addition, the Elasticsearch client service, `ElasticsearchSvcImpl` will need to be instantiated with parameters
specifying how to connect to the Elasticsearch server, for e.g.:

```java
  @Bean()
  public ElasticsearchSvcImpl elasticsearchSvc() {
     String elasticsearchHost = "localhost:9200";
     String elasticsearchUsername = "elastic";
     String elasticsearchPassword = "changeme";

     return new ElasticsearchSvcImpl(elasticsearchHost, elasticsearchUsername, elasticsearchPassword);
  }
```

The Elasticsearch client service requires that security be enabled in the Elasticsearch clusters, and that an Elasticsearch user be available with permissions to create an index and to index, update and delete documents as needed.

See the [JavaDoc](/hapi-fhir/apidocs/hapi-fhir-jpaserver-base/ca/uhn/fhir/jpa/search/lastn/IElasticsearchSvc.html) for more information regarding the Elasticsearch client service.
