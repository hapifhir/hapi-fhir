# Creating A Plain Server

HAPI FHIR provides a built-in mechanism for adding FHIR's RESTful Server capabilities to your applications. The HAPI RESTful Server is Servlet based, so it should be easy to deploy to any of the many compliant containers that exist.

Setup is mostly done using simple annotations, which means that it should be possible to create a FHIR compliant server quickly and easily.

# Defining Resource Providers

The first step in creating a FHIR RESTful Server is to define one or more resource providers. A resource provider is a class which is able to supply exactly one type of resource to be served up.

For example, if you wish to allow your server to serve up Patient, Observation and Location resources, you will need three resource providers.

See [Resource Providers](./resource_providers.html) for information on how to create these classes.

# Deploying

Once you have created your resource providers and your restful server class, you can bundle these into a WAR file and you are ready to deploy to any JEE container (Tomcat, Websphere, Glassfish, etc).

Assuming that you are using a build tool such as Apache Maven to build your project, you can create your WAR file simply by building the project.

```
mvn install
```

Once the build is complete, a file called `[projectname].war` will be found in the `target/` directory. This file can be deployed to the container/application server of your choice. Deploying WAR files to an application server is beyond the scope of this page, but there are many good tutorials on how to do this available on the web.

# Testing Using Jetty

Many of the HAPI FHIR sample projects take advantage of the *Maven Jetty Plugin* to provide an easy testing mechanism. This plugin can be used to automatically compile your code and deploy it to a local server (without needing to install anything additional) using a simple command.

To execute the Maven Jetty Plugin:

```
mvn jetty:run
```

# Server Base URL (Web Address)

The server will return data in a number of places that includes the	complete "identity" of a resource. Identity in this case refers to the web address that a user can use to access the resource.

For instance, if your server is hosted at `http://foo.com/fhir`, and your resource provider returns a Patient resource with the ID `123`, the server should translate that ID to `http://foo.com/fhir/Patient/123`. This complete identity URL is used in response headers (e.g. the `Location` header) as well as in Bundle resource contents (e.g. the Bundle entry "fullUrl" property).

The server will attempt to determine what the base URL should be based on what the request it receives looks like, but if it is not getting the right address you may wish to use a different "address strategy".

## Hardcoded Address Strategy

The simplest way to do this is to configure the server to use a hardcoded base URL, which means that the server won't try to figure out the "http://foo.com/fhir" part of the URL but will instead just use a fixed value you supply. 

The hardcoded address strategy is particularly useful in cases where the server is not aware of the external address used to access it, as is often the case in network architectures where a reverse proxy, API gateway, or other network equipment is present.

This strategy is shown in the following example:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExampleProviders.java|addressStrategy}}
```

## Other Strategies

See the	[IServerAddressStrategy](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/IServerAddressStrategy.html)	JavaDoc (specifically the list of "All Known Implementing Classes") to see	other strategies that are available.

<a name="capabilities"/>

# Capability Statement / Server Metadata

The HAPI FHIR RESTful Server will automatically generate a Server [CapabilityStatement](http://hl7.org/fhir/capabilitystatement.html) resource (or a Server [Conformance](https://www.hl7.org/fhir/DSTU2/conformance.html) resource for FHIR DSTU2).

This statement is automatically generated based on the various annotated methods which are provided to the server. This behaviour may be modified by creating a new class containing a method annotated with a [@Metadata annotation](./rest_operations.html#system_capabilities) and then passing an instance of that class to the [setServerConformanceProvider(Object)](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/RestfulServer.html#setServerConformanceProvider(java.lang.Object)) method on your RestfulServer instance.

## Enhancing the Generated CapabilityStatement

If you have a need to add your own content (special extensions, etc.) to your server's conformance statement, but still want to take advantage of HAPI's automatic CapabilityStatement generation, you may wish to extend the built-in generator class.

The generator class is version-specific, so you will need to extend the class appropriate for the version of FHIR you are implementing:

* DSTU3: [ca.uhn.fhir.rest.server.provider.dstu2.ServerConformanceProvider](/hapi-fhir/apidocs/hapi-fhir-structures-dstu2/ca/uhn/fhir/rest/server/provider/dstu2/ServerConformanceProvider.html)
* DSTU3: [org.hl7.fhir.dstu3.hapi.rest.server.ServerCapabilityStatementProvider](/hapi-fhir/apidocs/hapi-fhir-structures-dstu3/org/hl7/fhir/dstu3/hapi/rest/server/ServerCapabilityStatementProvider.html)
* R4 and later: [ca.uhn.fhir.rest.server.provider.ServerCapabilityStatementProvider](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/provider/ServerCapabilityStatementProvider.html)

In your own class extending this class, you can override the `getServerConformance()` method to obtain the built-in conformance statement and then add your own information to it.  


# Controlling Response Contents / Encoding / Formatting

FHIR allows for a number of special behaviours where only certain portions of resources are returned, instead of the entire resource body. These behaviours are automatically supported in HAPI FHIR Plain Server and no additional effort needs to be taken.

The following behaviours are automatically supported by the HAPI server:

<table class="table">
    <thead>
        <tr>
            <td><b>Parameter</b></td>
            <td><b>Description</b></td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>_summary=true</td>
            <td>
                Resources will be returned with any elements not marked as summary elements
                omitted.
            </td>
        </tr>
        <tr>
            <td>_summary=text</td>
            <td>
                Only the narrative portion of returned resources will be returned. For a read/vread 
                operation, the narrative will be served with a content type of <code>text/html</code>.
                for other operations, a Bundle will be returned but resources will only include
                the text element.
            </td>
        </tr>
        <tr>
            <td>_summary=data</td>
            <td>
                The narrative (text) portion of the resource will be omitted.
            </td>
        </tr>
        <tr>
            <td>_summary=count</td>
            <td>
                For a search, only Bundle.count will be returned.
            </td>
        </tr>
        <tr>
            <td>_elements=[element names]</td>
            <td>
                Only the given top level elements of returned resources will be returned, e.g for 
                a Patient search: <code>_elements=name,contact</code>
            </td>
        </tr>
        <tr>
            <td>_pretty=true</td>
            <td>
                Request that the server pretty-print the response (indent the data over multiple lines for easier human readability).
            </td>
        </tr>
    </tbody>
</table>

## Extended Elements Support

The HAPI FHIR server may be configured using the `RestfulServer#setElementsSupport` to enable extended support for the <code>_elements</code> filter.

In standard mode, elements are supported exactly as described in the <a href="http://hl7.org/fhir/search.html#elements">Elements Documentation</a> in the FHIR specification.

In extended mode, HAPI FHIR provides the same behaviour as described in the FHIR specification, but also enabled the following additional options:

* Values may be prepended using Resource names in order to apply the elements filter to multiple resources. For example, the following parameter could be used to apply elements filtering to both the DiagnosticReport and Observation resource in a search result:

    ```url
    http://base/Patient?_elements=DiagnosticReport.subject,DiagnosticReport.result,Observation.value
    ```
    
* Values may be prepended with a wildcard star in order to apply them to all resource types. For example, the following parameter could	be used to include the <code>subject</code> field in all resource types:

    ```url
    http://base/Patient?_elements=*.subject
    ```
    
* Values may include complex paths. For example, the following parameter could be used to include only the code on a coded element:

    ```url
    http://base/Patient?_elements=Procedure.reasonCode.coding.code
    ```
    
* Elements may be excluded using the <code>:exclude</code> modifier on the elements parameter. For example, the following parameter could be used to remove the resource metadata (meta) element from all resources in the response:

    ```url
    http://base/Patient?_elements:exclude=*.meta
    ```
    
    Note that this can be used to suppress the `SUBSETTED` tag which is automatically added to resources when an `_elements` parameter is applied.

