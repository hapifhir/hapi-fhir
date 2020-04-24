# Client Examples

This page contains examples of how to use the client to perform complete tasks. If you have an example you could contribute, we'd love to hear from you!

# Transaction With Placeholder IDs

The following example shows how to post a transaction with two resources, where one resource contains a reference to the other. A temporary ID (a UUID) is used as an ID to refer to, and this ID will be replaced by the server by	a permanent ID.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ClientTransactionExamples.java|conditional}}
```
 
This code creates the following transaction bundle:

**JSON**:

```json
{
  "resourceType": "Bundle",
  "type": "transaction",
  "entry": [
    {
      "fullUrl": "urn:uuid:3bc44de3-069d-442d-829b-f3ef68cae371",
      "resource": {
        "resourceType": "Patient",
        "identifier": [
          {
            "system": "http://acme.org/mrns",
            "value": "12345"
          }
        ],
        "name": [
          {
            "family": "Jameson",
            "given": [
              "J",
              "Jonah"
            ]
          }
        ],
        "gender": "male"
      },
      "request": {
        "method": "POST",
        "url": "Patient",
        "ifNoneExist": "identifier=http://acme.org/mrns|12345"
      }
    },
    {
      "resource": {
        "resourceType": "Observation",
        "status": "final",
        "code": {
          "coding": [
            {
              "system": "http://loinc.org",
              "code": "789-8",
              "display": "Erythrocytes [#/volume] in Blood by Automated count"
            }
          ]
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
    }
  ]
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

# Fetch all Pages of a Bundle

This following example shows how to load all pages of a bundle by fetching each page one-after-the-other and then joining the results.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/BundleFetcher.java|loadAll}}
```
