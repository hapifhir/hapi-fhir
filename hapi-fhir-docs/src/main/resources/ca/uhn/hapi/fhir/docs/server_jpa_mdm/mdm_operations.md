# MDM Operations

MDM links are managed by MDM Operations. These operations are supplied by a [plain provider](/docs/server_plain/resource_providers.html#plain-providers) called [EmpiProvider](/hapi-fhir/apidocs/hapi-fhir-server-empi/ca/uhn/fhir/empi/provider/EmpiProviderR4.html).

In cases where the operation changes data, if a resource id parameter contains a version (e.g. `Patient/123/_history/1`), then the operation will fail with a 409 CONFLICT if that is not the latest version of that resource.  This feature can be used to prevent update conflicts in an environment where multiple users are working on the same set of empi links.

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
                The id of the target resource (e.g. Patient resource).
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
      "valueString": "Person/123"
    }, {
      "name": "resourceId",
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
      "name": "newPerson",
      "valueBoolean": false
    }, {
      "name": "score",
      "valueDecimal": 1.8
    } ]
  } ]
}
```

## Query Duplicate Golden Resources

Use the `$empi-duplicate-golden-resources` operation to request a list of duplicate golden resources. 
This operation takes no parameters.

### Example

Use an HTTP GET to the following URL to invoke this operation:

```url
http://example.com/$empi-duplicate-golden-resources
```

This operation returns `Parameters` similar to `$mdm-query-links`:


```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "link",
    "part": [ {
      "name": "goldenResourceId",
      "valueString": "Person/123"
    }, {
      "name": "resourceId",
      "valueString": "Person/456"
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

Use the `$empi-not-duplicate` operation to mark duplicate golden resources as not duplicates. 
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
                The id of the Golden resource.
            </td>
        </tr>
        <tr>
            <td>resourceId</td>
            <td>String</td>
            <td>1..1</td>
            <td>
                The id of the Person that personId has a possible duplicate link to.
            </td>
        </tr>
    </tbody>
</table>

### Example

Use an HTTP POST to the following URL to invoke this operation:

```url
http://example.com/$empi-not-duplicate
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

Use the `$mdm-update-link` operation to change the `matchResult` update of an empi link. This operation takes the following parameters:

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
                The id of the Golden resource.
            </td>
        </tr>
        <tr>
            <td>resourceId</td>
            <td>String</td>
            <td>1..1</td>
            <td>
                The id of the target resource.
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

Empi links updated in this way will automatically have their `linkSource` set to `MANUAL`.

### Example

Use an HTTP POST to the following URL to invoke this operation:

```url
http://example.com/$empi-update-link
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
  }, {
    "name": "matchResult",
    "valueString": "MATCH"
  } ]
}
```

The operation returns the updated `Patient` resource.  Note that this is the only way to modify MDM-managed `Patient` resources.

## Merge Persons

<!--- 
    In most cases, fields will be merged (e.g. names, identifiers, and links will be the union of two).
    However when there is a conflict (e.g. birthday), fields in the toPerson will take precedence over fields in the fromPerson
-->
The `$mdm-merge-golden-resources` operation can be used to merge one Golden resource with another. When
doing this, you will need to decide which resource to merge from and which one to merge to. 

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

This operation returns the merged Patient resource.

# Querying The MDM

When MDM is enabled, the [$match operation](http://hl7.org/fhir/patient-operation-match.html) will be enabled on the JPA Server.

This operation allows a Patient resource to be submitted to the endpoint, and the system will attempt to find and return any Patient 
resources that match it according to the matching rules. The response includes a search score field that is calculated by averaging the
number of matched rules against total rules checked for the Patient resource. Appropriate match grade extension is also included. 

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

This might result in a response such as the following:

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

## Clearing MDM Links

The `$mdm-clear` operation is used to batch-delete MDM links and related persons from the database. This operation is meant to
be used during the rules-tuning phase of the MDM implementation so that you can quickly test your ruleset.
It permits the user to reset the state of their MDM system without manual deletion of all related links and golden resources.

After the operation is complete, all targeted MDM links are removed from the system, and their related Golden Resources are
deleted and expunged from the server.

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
            <td>targetType</td>
            <td>String</td>
            <td>0..1</td>
            <td>
                The target Resource type you would like to clear. If omitted, will operate over all links.
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
    "name": "targetType",
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

Call the `$mdm-submit` operation to submit patients and practitioners for MDM processing. In the rules-tuning phase of your setup, you can 
use `$mdm-submit` to apply MDM rules across multiple Resources. An important thing to note is that this operation only submits the 
resources for processing. Actual MDM processing is run asynchronously, and depending on the size
of the affected bundle of resources, may take some time to complete.

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

This operation can also be done at the Instance level. When this is the case, the operations accepts no parameters.
The following are examples of Instance level POSTs, which require no parameters.

```url
http://example.com/Patient/123/$empi-submit
http://example.com/Practitioner/456/$empi-submit
```
