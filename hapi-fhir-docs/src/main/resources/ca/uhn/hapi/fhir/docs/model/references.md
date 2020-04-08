# Resource References

Resource references are a key part of the HAPI FHIR model, since almost any resource will have references to other resources within it.

The [Reference](/hapi-fhir/apidocs/hapi-fhir-structures-r4/org/hl7/fhir/r4/model/Reference.html) type is the datatype for references. This datatype has a number of properties which help make working with FHIR simple.

The `getReference()` method returns a String that contains the identity of the resource being referenced. This is the item which is most commonly populated when interacting with FHIR. For example, consider the following Patient resource, which contains a reference to an Organization resource:

```json
{
   "resourceType": "Patient",
   "identifier": [{
      "system": "http://example.com/identifiers",
      "value": "12345"
   }],
   "managingOrganization": {
       "reference": "Organization/123"
   }
}
```

Given a Patient resource obtained by invoking a client operation, a call to `String reference = patient.getManagingOrganization().getReference();` returns a String containing `Organization/112`.

Reference also has a place for storing actual resource instances (i.e. an actual [IBaseResource](/hapi-fhir/apidocs/hapi-fhir-base/org/hl7/fhir/instance/model/api/IBaseResource.html) instance), and this can be very useful as shown below.

# References in Client Code

In client code, if a resource reference refers to a resource which was received as a part of the same response, <code>getResource()</code> will be populated with the actual resource. This can happen because either the resource was received as a contained resource, or the resource was received as a separate resource in a bundle.

# References in Server Code

In server code, you will often want to return a resource which contains a link to another resource. Generally these "linked" resources are not actually included in the response, but rather a link to the resource is included and the client may request that resource directly (by ID) if it is needed.

The following example shows a Patient resource being created which will have a link to its managing organization when encoded from a server:

```java
Patient patient = new Patient();
patient.setId("Patient/1333");
patient.addIdentifier("urn:mrns", "253345");
patient.getManagingOrganization().setReference("Organization/124362");
```

## Handling Includes (_include) in a Bundle

Your server code may also wish to add additional resource to a bundle being returned (e.g. because of an _include directive in the client's request).

To do this, you can implement your server method to simply return List<IBaseResource> and then simply add your extra resources to the list. Another technique however, is to populate the reference as shown in the example below, but ensure that the referenced resource has an ID set.

In the following example, the Organization resource has an ID set, so it will not be contained but will rather appear as a distinct entry in any returned bundles. Both resources are added to a bundle, which will then have two entries:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/IncludesExamples.java|addIncludes}}
``` 

This will give the following output:

```xml
<Bundle xmlns="http://hl7.org/fhir">
    <id value="4e151274-2b19-4930-97f2-8427167a176c"/>
    <type value="searchset"/>
    <total value="1"/>
    <link>
        <relation value="fhir-base"/>
        <url value="http://example.com/base"/>
    </link>
    <link>
        <relation value="self"/>
        <url value="http://example.com/base/Patient"/>
    </link>
    <entry>
        <resource>
            <Patient xmlns="http://hl7.org/fhir">
                <id value="1333"/>
                <identifier>
                    <system value="urn:mrns"/>
                    <value value="253345"/>
                </identifier>
                <managingOrganization>
                    <reference value="Organization/65546"/>
                </managingOrganization>
            </Patient>
        </resource>
    </entry>
    <entry>
        <resource>
            <Organization xmlns="http://hl7.org/fhir">
                <id value="65546"/>
                <name value="Test Organization"/>
            </Organization>
        </resource>
        <search>
            <mode value="include"/>
        </search>
    </entry>
</Bundle>
````

## Contained Resources

On the other hand, if the linked resource does not have an ID set, the linked resource will be included in the returned bundle as a "contained" resource. In this case, HAPI itself will define a local reference ID (e.g. `#1`).

```java
// Create an organization, note that the organization does not have an ID
Organization org = new Organization();
org.getName().setValue("Contained Test Organization");

// Create a patient
Patient patient = new Patient();
patient.setId("Patient/1333");
patient.addIdentifier("urn:mrns", "253345");

// Put the organization as a reference in the patient resource
patient.getManagingOrganization().setResource(org);

String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
System.out.println(encoded);
```

This will give the following output:

```xml
<Patient xmlns="http://hl7.org/fhir">
    <contained>
        <Organization xmlns="http://hl7.org/fhir" id="1">
            <name value="Contained Test Organization"/>
        </Organization>
    </contained>
    <identifier>
        <system value="urn:mrns"/>
        <value value="253345"/>
    </identifier>
    <managingOrganization>
        <reference value="#1"/>
    </managingOrganization>
</Patient>
```

Note that you may also "contain" resources manually in your own code if you prefer. The following example shows how to do this:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ResourceRefs.java|manualContained}}
```

# Versioned References

By default, HAPI will strip resource versions from references between resources. For example, if you set a reference to `Patient.managingOrganization` to the value `Patient/123/_history/2`, HAPI will encode this reference as `Patient/123`. 

This is because in most circumstances, references between resources should be versionless (e.g. the reference just points to the latest version, whatever version that might be).

There are valid circumstances however for wanting versioned references. If you need HAPI to emit versioned references, you have a few options:

You can force the parser to never strip versions:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/Parser.java|disableStripVersions}}
``` 

You can also disable this behaviour entirely on the context (so that it will apply to all parsers):

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/Parser.java|disableStripVersionsCtx}}
``` 

You can also configure HAPI to not strip versions only on certain fields. This is desirable if you want versionless references in most places but need them in some places:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/Parser.java|disableStripVersionsField}}
``` 
