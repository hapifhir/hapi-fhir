# Multitenancy

If you wish to allow a single endpoint to support multiple tenants, you may supply the server with a multitenancy provider.

This means that additional logic will be performed during request parsing to determine a tenant ID, which will be supplied to resource providers. This can be useful in servers that have	multiple distinct logical pools of resources hosted on the same infrastructure.

# URL Base Multitenancy

Using URL Base Multitenancy means that an additional element is added to the path of each resource between the server base	URL and the resource name. For example, if your restful server is deployed to `http://acme.org:8080/baseDstu3` and a client wishes to access Patient 123 for Tenant "FOO", the	resource ID (and URL to fetch that resource) would be `http://acme.org:8080/FOO/Patient/123`.

To enable this mode on your server, simply provide the [UrlBaseTenantIdentificationStrategy](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/tenant/UrlBaseTenantIdentificationStrategy.html) to the server as shown below:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/Multitenancy.java|enableUrlBaseTenantIdentificationStrategy}}
```

Your resource providers can then use a RequestDetails parameter to determine the tenant ID:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/Multitenancy.java|resourceProvider}}
```

