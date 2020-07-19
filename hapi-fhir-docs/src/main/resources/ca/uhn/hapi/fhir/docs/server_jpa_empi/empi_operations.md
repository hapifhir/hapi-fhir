# EMPI Operations

Several operations exist that can be used to manage EMPI links. These operations are supplied by a [plain provider](/docs/server_plain/resource_providers.html#plain-providers) called [EmpiProvider](/hapi-fhir/apidocs/hapi-fhir-server-empi/ca/uhn/fhir/empi/provider/EmpiProviderR4.html).

In cases where the operation changes data, if a resource id parameter contains a version (e.g. `Person/123/_history/1`), then the operation will fail with a 409 CONFLICT if that is not the latest version of that resource.  This could be used to prevent update conflicts in an environment where multiple users are working on the same set of empi links.

## Query links

Ue the `$empi-query-links` operation to view empi links.  The results returned are based on the parameters provided.  All parameters are optional.  This operation takes the following parameters:

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
            <td>personId</td>
            <td>String</td>
            <td>0..1</td>
            <td>
                The id of the Person resource.
            </td>
        </tr>
        <tr>
            <td>targetId</td>
            <td>String</td>
            <td>0..1</td>
            <td>
                The id of the Patient or Practitioner resource.
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

Use an HTTP GET like `http://example.com/$empi-query-link?matchResult=POSSIBLE_MATCH` or an HTTP POST to the following URL to invoke this operation:

```url
http://example.com/$empi-query-link
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
      "name": "personId",
      "valueString": "Person/123"
    }, {
      "name": "targetId",
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

## Querying links via the Person resource

Alternatively, you can query Empi links by querying Person resources directly.  Empi represents links in Person resources using the following mapping:

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>EMPI matchResult</th>
            <th>EMPI linkSource</th>
            <th>Person link.assurance</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>NO_MATCH</td>
            <td>MANUAL</td>
            <td>No link present</td>
        </tr>
        <tr>
            <td>POSSIBLE_MATCH</td>
            <td>AUTO</td>
            <td>level2</td>
        </tr>
        <tr>
            <td>MATCH</td>
            <td>AUTO</td>
            <td>level3</td>
        </tr>
        <tr>
            <td>MATCH</td>
            <td>MANUAL</td>
            <td>level4</td>
        </tr>
    </tbody>
</table>

For example, you can use the following HTTP GET to find all Person resources that have POSSIBLE_MATCH links:

```
http://example.com/Person?assurance=level2
```

## Query Duplicate Persons

Use the `$empi-duplicate-persons` operation to request a list of duplicate persons.  This operation takes no parameters

### Example

Use an HTTP GET to the following URL to invoke this operation:

```url
http://example.com/$empi-duplicate-persons
```

This operation returns `Parameters` similar to `$empi-query-links`:


```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "link",
    "part": [ {
      "name": "personId",
      "valueString": "Person/123"
    }, {
      "name": "targetId",
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

## Unduplicate Persons

Use the `$empi-not-duplicate` operation to mark duplicate persons as not duplicates.    This operation takes the following parameters:
                                                                                      
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
            <td>personId</td>
            <td>String</td>
            <td>1..1</td>
            <td>
                The id of the Person resource.
            </td>
        </tr>
        <tr>
            <td>targetId</td>
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
    "name": "personId",
    "valueString": "Person/123"
  }, {
    "name": "targetId",
    "valueString": "Person/456"
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

Use the `$empi-update-link` operation to change the `matchResult` update of an empi link. This operation takes the following parameters:

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
            <td>personId</td>
            <td>String</td>
            <td>1..1</td>
            <td>
                The id of the Person resource.
            </td>
        </tr>
        <tr>
            <td>targetId</td>
            <td>String</td>
            <td>1..1</td>
            <td>
                The id of the Patient or Practitioner resource.
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
    "name": "personId",
    "valueString": "Person/123"
  }, {
    "name": "targetId",
    "valueString": "Patient/456"
  }, {
    "name": "matchResult",
    "valueString": "MATCH"
  } ]
}
```

The operation returns the updated `Person` resource.  Note that this is the only way to modify EMPI-managed `Person` resources.

## Merge Persons

The `$empi-merge-persons` operation can be used to merge one Person resource with another.  When doing this, you will need to decide which resource to merge from and which one to merge to.  In most cases, fields will be merged (e.g. names, identifiers, and links will be the union of two).  However when there is a conflict (e.g. birthday), fields in the toPerson will take precedence over fields in the fromPerson

After the merge is complete, `fromPerson.active` is set to `false`.  Also, a new link with assurance level 4 (MANUAL MATCH) will be added pointing from the fromPerson to the toPerson.

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
            <td>fromPersonId</td>
            <td>String</td>
            <td>1..1</td>
            <td>
                The id of the Person resource to merge data from.
            </td>
        </tr>
        <tr>
            <td>toPersonId</td>
            <td>String</td>
            <td>1..1</td>
            <td>
                The id of the Person to merge data into.
            </td>
        </tr>
    </tbody>
</table>

### Example

Use an HTTP POST to the following URL to invoke this operation:

```url
http://example.com/$empi-merge-persons
```

The following request body could be used:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "fromPersonId",
    "valueString": "Person/123"
  }, {
    "name": "toPersonId",
    "valueString": "Patient/128"
  } ]
}
```

This operation returns the merged Person resource.

# Querying The EMPI

When EMPI is enabled, the [$match operation](http://hl7.org/fhir/patient-operation-match.html) will be enabled on the JPA Server.

This operation allows a Patient resource to be submitted to the endpoint, and the system will attempt to find and return any Patient resources that match it according to the matching rules.  

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
      }
    }
  ]
}
```
