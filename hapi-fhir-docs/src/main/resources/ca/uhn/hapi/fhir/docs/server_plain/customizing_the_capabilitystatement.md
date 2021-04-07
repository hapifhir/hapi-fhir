# Customizing the CapabilityStatement

Per the FHIR specification, any compliant FHIR REST server must support the FHIR [Capabilities](http://hl7.org/fhir/http.html#capabilities) operation. This operation is typically invoked by clients by requesting `[baseUrl]/metadata` from the server.

The Capabilities operation requires the server to return a valid [CapabilityStatement](http://hl7.org/fhir/capabilitystatement.html) resource describing the supported resources, operations, search parameters, and other capabilities of the server. 

The HAPI FHIR [RestfulServer](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/RestfulServer.html) will automatically generate a CapabilityStatement which describes its abilities.

You can customize the generated CapabiliityStatement by creating a [server interceptor](/hapi-fhir/docs/interceptors/server_interceptors.html) and registering it against the server. This interceptor should implement a hook method for the [SERVER_CAPABILITY_STATEMENT_GENERATED](/hapi-fhir/apidocs/hapi-fhir-base/undefined/ca/uhn/fhir/interceptor/api/Pointcut.html#SERVER_CAPABILITY_STATEMENT_GENERATED) pointcut.

An example is shown below:

```java
@Interceptor
public class CapabilityStatementCustomizer {

   @Hook(Pointcut.SERVER_CAPABILITY_STATEMENT_GENERATED)
   public void customize(IBaseConformance theCapabilityStatement) {

      // Cast to the appropriate version
      CapabilityStatement cs = (CapabilityStatement) theCapabilityStatement;

      // Customize the CapabilityStatement as desired
      cs
         .getSoftware()
         .setName("Acme FHIR Server")
         .setVersion("1.0")
         .setReleaseDateElement(new DateTimeType("2021-02-06"));

   }

}
```
