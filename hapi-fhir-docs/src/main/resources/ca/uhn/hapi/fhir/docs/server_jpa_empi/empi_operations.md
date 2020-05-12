# EMPI Operations

Several operations exist that can be used to manage EMPI links. These operations are supplied by a [plain provider](/docs/server_plain/resource_providers.html#plain-providers) called [EmpiProvider](/hapi-fhir/apidocs/hapi-fhir-jpaserver-base/ca/uhn/fhir/empi/provider/EmpiProviderR4.html).

## Update Link

The `$empi-update-link` operation can be used to update a link from a Person resource to a Patient or Pracitioner resource. This operation takes the following parameters:

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

### Example

An HTTP POST to the following URL would be used to invoke this operation:

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

## Merge Persons

The `$empi-merge-persons` operation can be used to merge one Person resource with another.  When doing this, you will need to decide which resource to delete and which one to keep.  Data from the personToKeep will be given precedence over data in the personToDelete. This operation takes the following parameters:

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
            <td>personIdToDelete</td>
            <td>String</td>
            <td>1..1</td>
            <td>
                The id of the Person resource to merge data from.  This resource will be deleted after the merge.
            </td>
        </tr>
        <tr>
            <td>personIdToKeep</td>
            <td>String</td>
            <td>1..1</td>
            <td>
                The id of the Person to merge data into. 
            </td>
        </tr>
    </tbody>
</table>

### Example

An HTTP POST to the following URL would be used to invoke this operation:

```url
http://example.com/$empi-merge-persons
```

The following request body could be used:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "personIdToDelete",
    "valueString": "Person/123"
  }, {
    "name": "personIdToKeep",
    "valueString": "Patient/128"
  } ]
}
```
