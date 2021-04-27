# Client Examples

This page contains examples of how to use the client to perform complete tasks. If you have an example you could contribute, we'd love to hear from you!

# Transaction With Conditional Create

The following example demonstrates a common scenario: How to create a new piece of data for a Patient (in this case, an Observation) where the identifier of the Patient is known, but the ID is not.

In this scenario, we want to look up the Patient record and reference it from the newly created Observation. In the event that no Patient record already exists with the given identifier, a new one will be created and the Observation will reference it. This is known in FHIR as a [Conditional Create](http://hl7.org/fhir/http.html#ccreate).
 
**JSON**:

```json
{
  "resourceType": "Bundle",
  "type": "transaction",
  "entry": [ {
    "fullUrl": "urn:uuid:3bc44de3-069d-442d-829b-f3ef68cae371",
    "resource": {
      "resourceType": "Patient",
      "identifier": [ {
        "system": "http://acme.org/mrns",
        "value": "12345"
      } ],
      "name": [ {
        "family": "Jameson",
        "given": [ "J", "Jonah" ]
      } ],
      "gender": "male"
    },
    "request": {
      "method": "POST",
      "url": "Patient",
      "ifNoneExist": "identifier=http://acme.org/mrns|12345"
    }
  }, {
    "resource": {
      "resourceType": "Observation",
      "status": "final",
      "code": {
        "coding": [ {
          "system": "http://loinc.org",
          "code": "789-8",
          "display": "Erythrocytes [#/volume] in Blood by Automated count"
        } ]
      },
      "subject": {
        "reference": "urn:uuid:3bc44de3-069d-442d-829b-f3ef68cae371"
      },
      "valueQuantity": {
        "value": 4.12,
        "unit": "10 trillion/L",
        "system": "http://unitsofmeasure.org",
        "code": "10*12/L"
      }
    },
    "request": {
      "method": "POST",
      "url": "Observation"
    }
  } ]
}
```

**XML**:

```xml
<Bundle xmlns="http://hl7.org/fhir">
   <type value="transaction"/>
   <entry>
      <fullUrl value="urn:uuid:47709cc7-b3ec-4abc-9d26-3df3d3d57907"/>
      <resource>
         <Patient xmlns="http://hl7.org/fhir">
            <identifier>
               <system value="http://acme.org/mrns"/>
               <value value="12345"/>
            </identifier>
            <name>
               <family value="Jameson"/>
               <given value="J"/>
               <given value="Jonah"/>
            </name>
            <gender value="male"/>
         </Patient>
      </resource>
      <request>
         <method value="POST"/>
         <url value="Patient"/>
         <ifNoneExist value="identifier=http://acme.org/mrns|12345"/>
      </request>
   </entry>
   <entry>
      <resource>
         <Observation xmlns="http://hl7.org/fhir">
            <status value="final"/>
            <code>
               <coding>
                  <system value="http://loinc.org"/>
                  <code value="789-8"/>
                  <display value="Erythrocytes [#/volume] in Blood by Automated count"/>
               </coding>
            </code>
            <subject>
               <reference value="urn:uuid:47709cc7-b3ec-4abc-9d26-3df3d3d57907"/>
            </subject>
            <valueQuantity>
               <value value="4.12"/>
               <unit value="10 trillion/L"/>
               <system value="http://unitsofmeasure.org"/>
               <code value="10*12/L"/>
            </valueQuantity>
         </Observation>
      </resource>
      <request>
         <method value="POST"/>
         <url value="Observation"/>
      </request>
   </entry>
</Bundle>
```

The server responds with the following response. Note that the ID of the already existing patient is returned, and the ID of the newly created Observation is too.

```xml
<Bundle xmlns="http://hl7.org/fhir">
   <id value="dd1f75b8-e472-481e-97b3-c5eebb99a5e0"/>
   <type value="transaction-response"/>
   <link>
      <relation value="self"/>
      <url value="http://fhirtest.uhn.ca/baseDstu2"/>
   </link>
   <entry>
      <response>
         <status value="200 OK"/>
         <location value="Patient/966810/_history/1"/>
         <etag value="1"/>
         <lastModified value="2015-10-29T07:25:42.465-04:00"/>
      </response>
   </entry>
   <entry>
      <response>
         <status value="201 Created"/>
         <location value="Observation/966828/_history/1"/>
         <etag value="1"/>
         <lastModified value="2015-10-29T07:33:28.047-04:00"/>
      </response>
   </entry>
</Bundle>
```

To produce this transaction in Java code:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ClientTransactionExamples.java|conditional}}
```

# Fetch all Pages of a Bundle

This following example shows how to load all pages of a bundle by fetching each page one-after-the-other and then joining the results.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/BundleFetcher.java|loadAll}}
```

# Create Composition and Generate Document

This example shows how to generate a Composition resource with two linked resources, then apply the server `$document` operation to generate a document based on this composition.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/CreateCompositionAndGenerateDocument.java|CreateCompositionAndGenerateDocument}}
```


