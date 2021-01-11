# MDM Operations

MDM links are managed by MDM Operations. These operations are supplied by a [plain provider](/docs/server_plain/resource_providers.html#plain-providers) called [MdmProvider](/hapi-fhir/apidocs/hapi-fhir-server-mdm/ca/uhn/fhir/mdm/provider/MdmProviderDstu3Plus.html).

In cases where the operation changes data, if a resource id parameter contains a version (e.g. `Patient/123/_history/1`), then the operation will fail with a 409 CONFLICT if that is not the latest version of that resource.  This feature can be used to prevent update conflicts in an environment where multiple users are working on the same set of mdm links.

## Query links

Use the `$mdm-query-links` operation to view MDM links. The results returned are based on the parameters provided. All parameters are optional.  This operation takes the following parameters:

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Type</th>
            <th>Cardinality</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>goldenResourceId</td>
            <td>String</td>
            <td>0..1</td>
            <td>
                The id of the Golden Resource (e.g. Golden Patient Resource).
            </td>
        </tr>
        <tr>
            <td>resourceId</td>
            <td>String</td>
            <td>0..1</td>
            <td>
                The id of the source resource (e.g. Patient resource).
            </td>
        </tr>
        <tr>
            <td>matchResult</td>
            <td>String</td>
            <td>0..1</td>
            <td>
                MATCH, POSSIBLE_MATCH or NO_MATCH.
            </td>
        </tr>
        <tr>
            <td>linkSource</td>
            <td>String</td>
            <td>0..1</td>
            <td>
                AUTO, MANUAL.
            </td>
        </tr>
    </tbody>
</table>

### Example

Use an HTTP GET like `http://example.com/$mdm-query-links?matchResult=POSSIBLE_MATCH` or an HTTP POST to the following URL to invoke this operation:

```url
http://example.com/$mdm-query-links
```

The following request body could be used to find all POSSIBLE_MATCH links in the system:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "matchResult",
    "valueString": "POSSIBLE_MATCH"
  } ]
}
```

This operation returns a `Parameters` resource that looks like the following:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "link",
    "part": [ {
      "name": "goldenResourceId",
      "valueString": "Patient/123"
    }, {
      "name": "sourceResourceId",
      "valueString": "Patient/456"
    }, {
      "name": "matchResult",
      "valueString": "POSSIBLE_MATCH"
    }, {
      "name": "linkSource",
      "valueString": "AUTO"
    }, {
      "name": "eidMatch",
      "valueBoolean": false
    }, {
      "name": "hadToCreateNewResource",
      "valueBoolean": false
    }, {
      "name": "score",
      "valueDecimal": 1.8
    } ]
  } ]
}
```

## Query Duplicate Golden Resources

Use the `$mdm-duplicate-golden-resources` operation to request a list of duplicate Golden Resources. 
This operation takes no parameters.

### Example

Use an HTTP GET to the following URL to invoke this operation:

```url
http://example.com/$mdm-duplicate-golden-resources
```

This operation returns `Parameters` similar to `$mdm-query-links`:


```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "link",
    "part": [ {
      "name": "goldenResourceId",
      "valueString": "Patient/123"
    }, {
      "name": "sourceResourceId",
      "valueString": "Patient/456"
    }, {
      "name": "matchResult",
      "valueString": "POSSIBLE_DUPLICATE"
    }, {
      "name": "linkSource",
      "valueString": "AUTO"
    } ]
  } ]
}
```

## Unduplicate Golden Resources

Use the `$mdm-not-duplicate` operation to mark duplicate Golden Resources as not duplicates.
This operation takes the following parameters:

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Type</th>
            <th>Cardinality</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>goldenResourceId</td>
            <td>String</td>
            <td>1..1</td>
            <td>
                The id of the Golden Resource.
            </td>
        </tr>
        <tr>
            <td>resourceId</td>
            <td>String</td>
            <td>1..1</td>
            <td>
                The id of the source resource that has a possible duplicate link to.
            </td>
        </tr>
    </tbody>
</table>

### Example

Use an HTTP POST to the following URL to invoke this operation:

```url
http://example.com/$mdm-not-duplicate
```

The following request body could be used:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "goldenResourceId",
    "valueString": "Patient/123"
  }, {
    "name": "resourceId",
    "valueString": "Patient/456"
  } ]
}
```

When the operation is successful, it returns the following `Parameters`:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "success",
    "valueBoolean": true
  } ]
}
```

## Update Link

Use the `$mdm-update-link` operation to change the `matchResult` update of an mdm link. This operation takes the following parameters:

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Type</th>
            <th>Cardinality</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>goldenResourceId</td>
            <td>String</td>
            <td>1..1</td>
            <td>
                The id of the Golden Resource.
            </td>
        </tr>
        <tr>
            <td>resourceId</td>
            <td>String</td>
            <td>1..1</td>
            <td>
                The id of the source resource.
            </td>
        </tr>
        <tr>
            <td>matchResult</td>
            <td>String</td>
            <td>1..1</td>
            <td>
                Must be either MATCH or NO_MATCH.
            </td>
        </tr>
    </tbody>
</table>

MDM links updated in this way will automatically have their `linkSource` set to `MANUAL`.

### Example

Use an HTTP POST to the following URL to invoke this operation:

```url
http://example.com/$mdm-update-link
```

Any supported MDM type can be used. The following request body shows how to update link on the Patient resource type:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "goldenResourceId",
    "valueString": "Patient/123"
  }, {
    "name": "resourceId",
    "valueString": "Patient/456"
  }, {
    "name": "matchResult",
    "valueString": "MATCH"
  } ]
}
```

The operation returns the updated Golden Resource. For the query above `Patient` resource will be returned.  Note that this is the only way to modify MDM-managed Golden Resources.

## Merge Golden Resources

The `$mdm-merge-golden-resources` operation can be used to merge one Golden Resource with another. When doing this, you will need to decide which resource to merge from and which one to merge to. 

After the merge is complete, `fromGoldenResourceId` will be deactivated by assigning a metadata tag `REDIRECTED`. 

This operation takes the following parameters:

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Type</th>
            <th>Cardinality</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>fromGoldenResourceId</td>
            <td>String</td>
            <td>1..1</td>
            <td>
                The id of the Golden Resource to merge data from.
            </td>
        </tr>
        <tr>
            <td>toGoldenResourceId</td>
            <td>String</td>
            <td>1..1</td>
            <td>
                The id of the Golden Resource to merge data into.
            </td>
        </tr>
    </tbody>
</table>

### Example

Use an HTTP POST to the following URL to invoke this operation:

```url
http://example.com/$mdm-merge-golden-resources
```

The following request body could be used:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "fromGoldenResourceId",
    "valueString": "Patient/123"
  }, {
    "name": "toGoldenResourceId",
    "valueString": "Patient/128"
  } ]
}
```

This operation returns the merged Golden Resource (`toGoldenResourceId`).

# Querying The MDM

## Querying the Patient Resource

When MDM is enabled, the [$match operation](http://hl7.org/fhir/patient-operation-match.html) will be enabled on the JPA Server.

This operation allows a Patient resource to be submitted to the endpoint, and the system will attempt to find and return any Patient resources that match it according to the matching rules. The response includes a search score field that is calculated by averaging the number of matched rules against total rules checked for the Patient resource. Appropriate match grade extension is also included. 

For example, the following request may be submitted:

```http
POST /Patient/$match
Content-Type: application/fhir+json; charset=UTF-8

{
    "resourceType":"Parameters",
    "parameter": [
        {
            "name":"resource",
            "resource": {
                "resourceType":"Patient",
                "name": [
                   { "family":"foo" }
                ]
            }
        }
    ]
}
```

Sample response for the Patient match is included below:

```json
{
  "resourceType": "Bundle",
  "id": "0e712adc-6979-4875-bbe9-70b883a955b8",
  "meta": {
    "lastUpdated": "2019-06-06T22:46:43.809+03:30"
  },
  "type": "searchset",
  "entry": [
    {
      "resource": {
        "resourceType": "Patient",
        "id": "3",
        "meta": {
          "versionId": "1",
          "lastUpdated": "2019-06-06T22:46:43.339+03:30"
        },
        "name": [
          {
            "family": "foo",
            "given": [
              "bar"
            ]
          }
        ],
        "birthDate": "2000-01-01"
      },
      "search": {
        "extension": [{
          "url": "http://hl7.org/fhir/StructureDefinition/match-grade",
          "valueCode": "certain"
        }],
        "mode": "match",
        "score": 0.9
      }
    }
  ]
}
```

## Querying the Other Supported MDM Resources via `/$mdm-match`

Query operations on any other supported MDM type is also allowed. This operation will find resources that match the provided parameters according to the matching rules. The response includes a search score field that is calculated by averaging the number of matched rules against total rules checked for the Patient resource. Appropriate match grade extension is also included in the response.

The request below may be submitted to search for `Orgaization` in case it defined as a supported MDM type:

```http
POST /Organization/$mdm-match
Content-Type: application/fhir+json; charset=UTF-8

{
    "resourceType":"Parameters",
    "parameter": [
        {
            "name":"resource",
            "resource": {
                "resourceType":"Orgaization",
                "name": "McMaster Family Practice"
            }
        }
    ]
}
```

MDM will respond with the appropriate resource bundle. 

## Clearing MDM Links

The `$mdm-clear` operation is used to batch-delete MDM links and related Golden Resources from the database. This operation is meant to be used during the rules-tuning phase of the MDM implementation so that you can quickly test your ruleset. It permits the user to reset the state of their MDM system without manual deletion of all related links and Golden Resources.

After the operation is complete, all targeted MDM links are removed from the system, and their related Golden Resources are deleted and expunged from the server.

This operation takes a single optional Parameter.

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Type</th>
            <th>Cardinality</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>sourceType</td>
            <td>String</td>
            <td>0..1</td>
            <td>
                The Source Resource type you would like to clear. If omitted, will operate over all links.
            </td>
        </tr>
    </tbody>
</table>

### Example

Use an HTTP POST to the following URL to invoke this operation:

```url
http://example.com/$mdm-clear
```

The following request body could be used:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "sourceType",
    "valueString": "Patient"
  } ]
}
```

This operation returns the number of MDM links that were cleared. The following is a sample response:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "reset",
    "valueDecimal": 5
  } ]
}
```

## Batch-creating MDM Links

Call the `$mdm-submit` operation to submit patients and practitioners for MDM processing. In the rules-tuning phase of your setup, you can use `$mdm-submit` to apply MDM rules across multiple Resources. An important thing to note is that this operation only submits the resources for processing. Actual MDM processing is run asynchronously, and depending on the size of the affected bundle of resources, may take some time to complete.

After the operation is complete, all resources that matched the criteria will now have at least one MDM link attached to them.

This operation takes a single optional criteria parameter unless it is called on a specific instance.

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Type</th>
            <th>Cardinality</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>criteria</td>
            <td>String</td>
            <td>0..1</td>
            <td>
            The search criteria used to filter resources.  An empty criteria will submit all resources.
            </td>
        </tr>
    </tbody>
</table>

### Example

This operation can be executed at the Server level, Resource level, or Instance level.
Use an HTTP POST to the following URL to invoke this operation with matching criteria:

```url
http://example.com/$mdm-submit
http://example.com/Patient/$mdm-submit
http://example.com/Practitioner/$mdm-submit
```

The following request body could be used:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "criteria",
    "valueString": "birthDate=2020-07-28"
  } ]
}
```
This operation returns the number of resources that were submitted for MDM processing. The following is a sample response:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "submitted",
    "valueDecimal": 5
  } ]
}
```

This operation can also be done at the Instance level. When this is the case, the operations accepts no parameters. The following are examples of Instance level POSTs, which require no parameters.

```url
http://example.com/Patient/123/$mdm-submit
http://example.com/Practitioner/456/$mdm-submit
```
