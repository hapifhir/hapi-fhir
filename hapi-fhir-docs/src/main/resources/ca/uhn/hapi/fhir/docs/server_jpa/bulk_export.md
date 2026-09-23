# Bulk Export

The JPA server implements the FHIR Bulk Data Access `$export` operation at the system, `Patient` and `Group` levels.

Export is asynchronous: the client kicks off a job using the `Prefer: respond-async` header, polls the URL returned in the `Content-Location` header, and eventually receives a manifest listing one URL per output file.

Export runs as a [Batch2 job](/hapi-fhir/docs/server_jpa_batch/introduction.html).

Each output file is written to a `Binary` resource, and the manifest points at those `Binary` resources.

A given file contains only a single resource type, and large results are split across several files so that no one file exceeds `JpaStorageSettings#getBulkExportFileMaximumSize()` (100 MB by default).

# Output Formats

Clients select an output format using the `_outputFormat` parameter.

By default, only NDJSON is supported and can be specified with any one of the following:
* `application/fhir+ndjson`
* `application/ndjson`
* `ndjson`

Any other value is rejected when the job is submitted, unless a converter has been registered for it.

## Registering a Custom Converter

A server can emit other formats by registering an interceptor hook on the [STORAGE_BULK_EXPORT_RESOURCE_CONVERT](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#STORAGE_BULK_EXPORT_RESOURCE_CONVERT) pointcut.

The hook receives the `BulkExportJobParameters` for the job, including the requested `_outputFormat`, and returns an `IResourceConverter` implementation to handle it.

Returning a converter for a given format is what makes that format valid, so no separate registration of the content type is needed.

The first non-null implementation returned by the hook will be used by the job.

An implementer can even choose to override the default NDJSON if they so choose.

The following example registers a custom converter for `text/csv` and leaves every other format alone:

{{snippet:classpath:/ca/uhn/hapi/fhir/docs/interceptor/BulkExportCsvConverterInterceptor.java|interceptor}}

With this interceptor registered, a client may request `$export?_outputFormat=text/csv`.

## The Converter Contract

`IResourceConverter` has a single method, which receives a batch of already-expanded resources plus the job parameters, and returns the files to store:

```java
ConvertedFiles consume(BulkExportResourceList theResources, BulkExportJobParameters theJobParameters);
```

A few properties of this contract are worth noting:

* The converter is invoked once per batch of resources rather than once per job, so it is called many times over the life of a large export. It is never invoked with an empty resource list, and all resources in a single invocation share the same resource type.
* Each returned `ConvertedFile` becomes one `Binary` resource, and must have its resource type, mime type and bytes all populated. A converter may return several files for a single batch if it needs to split the output.
* Returning no files at all, or a file that is missing any of those three values, fails the job.
* Resources are filtered by the [STORAGE_BULK_EXPORT_RESOURCE_INCLUSION](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#STORAGE_BULK_EXPORT_RESOURCE_INCLUSION) pointcut before the converter sees them, so a converter does not need to repeat that filtering.

## Converter Selection

The hook is consulted in two places: once when the job is submitted, to decide whether the requested `_outputFormat` is supported, and once for each batch of resources as the job runs.

Implementations should therefore be cheap to call and should return a converter consistently for the same parameters.

If several interceptors are registered on the pointcut, they are called in the usual interceptor order and the first non-`null` converter is used.

Because the hook is consulted before the built-in NDJSON converter, an interceptor may also return a converter for an NDJSON content type in order to replace the default NDJSON output.

Note that a job which passed validation may still fail later if the converter becomes unavailable, for example if the server is restarted without the interceptor registered while an export is in progress. In that case the job fails rather than silently falling back to NDJSON.
