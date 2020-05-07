# Partition Mapping Operations

Several operations exist that can be used to manage the existence of partitions. These operations are supplied by a [plain provider](/docs/server_plain/resource_providers.html#plain-providers) called [PartitionManagementProvider](/hapi-fhir/apidocs/hapi-fhir-jpaserver-base/ca/uhn/fhir/jpa/partition/PartitionManagementProvider.html).

Before a partition can be used, it must be registered using these methods.

## Creating a Partition

The `$partition-management-create-partition` operation can be used to create a new partition. This operation takes the following parameters:

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
            <td>id</td>
            <td>Integer</td>
            <td>1..1</td>
            <td>
                The numeric ID for the partition. This value can be any integer, positive or negative or zero. It must not be a value that has already been used. 
            </td>
        </tr>
        <tr>
            <td>name</td>
            <td>Code</td>
            <td>1..1</td>
            <td>
                A code (string) to assign to the partition. 
            </td>
        </tr>
        <tr>
            <td>description</td>
            <td>String</td>
            <td>0..1</td>
            <td>
                An optional description for the partition. 
            </td>
        </tr>
    </tbody>
</table>

### Example

An HTTP POST to the following URL would be used to invoke this operation:

```url
http://example.com/$partition-management-create-partition 
```

The following request body could be used:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "id",
    "valueInteger": 123
  }, {
    "name": "name",
    "valueCode": "PARTITION-123"
  }, {
    "name": "description",
    "valueString": "a description"
  } ]
}
```

## Updating a Partition

The `$partition-management-update-partition` operation can be used to update an existing partition. This operation takes the following parameters:

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
            <td>id</td>
            <td>Integer</td>
            <td>1..1</td>
            <td>
                The numeric ID for the partition to update. This ID must already exist. 
            </td>
        </tr>
        <tr>
            <td>name</td>
            <td>Code</td>
            <td>1..1</td>
            <td>
                A code (string) to assign to the partition. Note that it is acceptable to change the name of a partition, but this should be done with caution since partition names may be referenced by URLs, caches, etc.
            </td>
        </tr>
        <tr>
            <td>description</td>
            <td>String</td>
            <td>0..1</td>
            <td>
                An optional description for the partition. 
            </td>
        </tr>
    </tbody>
</table>

### Example

An HTTP POST to the following URL would be used to invoke this operation:

```url
http://example.com/$partition-management-create-partition 
```

The following request body could be used:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "id",
    "valueInteger": 123
  }, {
    "name": "name",
    "valueCode": "PARTITION-123"
  }, {
    "name": "description",
    "valueString": "a description"
  } ]
}
```

## Deleting a Partition

The `$partition-management-delete-partition` operation can be used to delete an existing partition. This operation takes the following parameters:

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
            <td>id</td>
            <td>Integer</td>
            <td>1..1</td>
            <td>
                The numeric ID for the partition to update. This ID must already exist. 
            </td>
        </tr>
    </tbody>
</table>

### Example

An HTTP POST to the following URL would be used to invoke this operation:

```url
http://example.com/$partition-management-delete-partition 
```

The following request body could be used:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "id",
    "valueInteger": 123
  } ]
}
```

