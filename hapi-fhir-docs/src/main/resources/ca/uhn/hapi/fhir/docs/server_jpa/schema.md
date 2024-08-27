# HAPI FHIR JPA Schema

**This page is a work in progress. It is not yet comprehensive.**

It contains a description of the tables within the HAPI FHIR JPA database. Note that columns are shown using Java datatypes as opposed to SQL datatypes, because the exact SQL datatype used will vary depending on the underlying database platform. The schema creation scripts can be used to determine the underlying column types. 

# Background: Persistent IDs (PIDs)

The HAPI FHIR JPA schema relies heavily on the concept of internal persistent IDs on tables, using a Java type of Long (8-byte integer, which translates to an *int8* or *number(19)* on various database platforms).

Many tables use an internal persistent ID as their primary key, allowing the flexibility for other more complex business identifiers to be changed and minimizing the amount of data consumed by foreign key relationships. These persistent ID columns are generally assigned using a dedicated database sequence on platforms which support sequences.

The persistent ID column is generally called `PID` in the database schema, although there are exceptions.

<a name="HFJ_RESOURCE"/>

# HFJ_RESOURCE: Resource Master Table

<img src="/hapi-fhir/docs/images/jpa_erd_resources.svg" alt="Resources" style="width: 100%; max-width: 600px;"/>

The HFJ_RESOURCE table indicates a single resource of any type in the database. For example, the resource `Patient/1` will have exactly one row in this table, representing all versions of the resource.

## Columns

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>PARTITION_ID</td>
            <td></td>
            <td>Integer</td>
            <td>Nullable</td>
            <td>
                This is the optional partition ID, if the resource is in a partition. See <a href="/hapi-fhir/docs/server_jpa_partitioning/partitioning.html">Partitioning</a>.  
            </td>        
        </tr>
        <tr>
            <td>PARTITION_DATE</td>
            <td></td>
            <td>Timestamp</td>
            <td>Nullable</td>
            <td>
                This is the optional partition date, if the resource is in a partition. See <a href="/hapi-fhir/docs/server_jpa_partitioning/partitioning.html">Partitioning</a>.  
            </td>        
        </tr>
        <tr>
            <td>RES_VER</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                This is the current version ID of the resource. Will contain <code>1</code> when the resource is first
                created, <code>2</code> the first time it is updated, etc.  
                This column is equivalent to the <b>HFJ_RES_VER.RES_VER</b>
                column, although it does not have a foreign-key dependency in order to allow selective expunge of versions
                when necessary. Not to be confused with <b>RES_VERSION</b> below.   
            </td>
        </tr>
        <tr>
            <td>RES_VERSION</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                This column contains the FHIR version associated with this resource, using a constant drawn
                from <a href="/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/context/FhirVersionEnum.html">FhirVersionEnum</a>.
                Not to be confused with <b>RES_VER</b> above.
            </td>
        </tr>
        <tr>
            <td>RES_TYPE</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                Contains the resource type (e.g. <code>Patient</code>) 
            </td>        
        </tr>
        <tr>
            <td>FHIR_ID</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                Contains the FHIR Resource id element.  Either the PID, or the client-assigned id. 
            </td>        
        </tr>
        <tr>
            <td>HASH_SHA256</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                This column contains a SHA-256 hash of the current resource contents, exclusive of resource metadata.
                This is used in order to detect NO-OP writes to the resource. 
            </td>        
        </tr>
        <tr>
            <td>RES_PUBLISHED</td>
            <td></td>
            <td>Timestamp</td>
            <td></td>
            <td>
                Contains the date that the first version of the resource was created. 
            </td>        
        </tr>
        <tr>
            <td>RES_UPDATED</td>
            <td></td>
            <td>Timestamp</td>
            <td></td>
            <td>
                Contains the date that the most recent version of the resource was created. 
            </td>        
        </tr>
        <tr>
            <td>RES_DELETED_AT</td>
            <td></td>
            <td>Timestamp</td>
            <td>Nullable</td>
            <td>
                If the most recent version of the resource is a delete, this contains the timestamp at which 
                the resource was deleted. Otherwise, contains <i>NULL</i>. 
            </td>        
        </tr>
    </tbody>
</table>



<a name="HFJ_RES_VER"/>

# HFJ_RES_VER: Resource Versions and Contents

The HFJ_RES_VER table contains individual versions of a resource. If the resource `Patient/1` has 3 versions, there will be 3 rows in this table.

The complete raw contents of the resource is stored in either the `RES_TEXT` or the `RES_TEXT_VC` column, using the encoding specified in the `RES_ENCODING` column.

## Columns

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>PARTITION_ID</td>
            <td></td>
            <td>Integer</td>
            <td>Nullable</td>
            <td>
                This is the optional partition ID, if the resource is in a partition. See <a href="/hapi-fhir/docs/server_jpa_partitioning/partitioning.html">Partitioning</a>.  
            </td>        
        </tr>
        <tr>
            <td>PARTITION_DATE</td>
            <td></td>
            <td>Timestamp</td>
            <td>Nullable</td>
            <td>
                This is the optional partition date, if the resource is in a partition. See <a href="/hapi-fhir/docs/server_jpa_partitioning/partitioning.html">Partitioning</a>.  
            </td>        
        </tr>
        <tr>
            <td>PID</td>
            <td>PK</td>
            <td>Long</td>
            <td></td>
            <td>
                This is the row persistent ID.  
            </td>        
        </tr>
        <tr>
            <td>RES_ID</td>
            <td>FK to <a href="#HFJ_RESOURCE">HFJ_RESOURCE</a></td>
            <td>Long</td>
            <td></td>
            <td>
                This is the persistent ID of the resource being versioned.
            </td>        
        </tr>
        <tr>
            <td>RES_VER</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                Contains the specific version (starting with 1) of the resource that this row corresponds to. 
            </td>        
        </tr>
        <tr>
            <td>RES_ENCODING</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                Describes the encoding of the resource being used to store this resource in <b>RES_TEXT</b>. See
                <i>Encodings</i> below for allowable values. 
            </td>        
        </tr>
        <tr>
            <td>RES_TEXT</td>
            <td></td>
            <td>byte[] (SQL LOB)</td>
            <td></td>
            <td>
                Contains the actual full text of the resource being stored, stored in a binary LOB.
            </td>        
        </tr>
        <tr>
            <td>RES_TEXT_VC</td>
            <td></td>
            <td>String (SQL VARCHAR2)</td>
            <td></td>
            <td>
                Contains the actual full text of the resource being stored, stored in a textual VARCHAR2 column. Only one of <code>RES_TEXT</code> and <code>RES_TEXT_VC</code> will be populated for any given row. The other column in either case will be <i>null</i>. 
            </td>        
        </tr>
    </tbody>
</table>

## Encodings

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Value</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>JSONC</td>
            <td>
                The resource is serialized using FHIR JSON encoding, and then compressed into a byte stream using GZIP compression. 
            </td>        
        </tr>
    </tbody>
</table>     

## Columns

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>PARTITION_ID</td>
            <td></td>
            <td>Integer</td>
            <td>Nullable</td>
            <td>
                This is the optional partition ID, if the resource is in a partition. See <a href="/hapi-fhir/docs/server_jpa_partitioning/partitioning.html">Partitioning</a>.  
            </td>        
        </tr>
        <tr>
            <td>PARTITION_DATE</td>
            <td></td>
            <td>Timestamp</td>
            <td>Nullable</td>
            <td>
                This is the optional partition date, if the resource is in a partition. See <a href="/hapi-fhir/docs/server_jpa_partitioning/partitioning.html">Partitioning</a>.  
            </td>        
        </tr>
        <tr>
            <td>PID</td>
            <td>PK</td>
            <td>Long</td>
            <td></td>
            <td>
                This is the row persistent ID.  
            </td>        
        </tr>
        <tr>
            <td>RESOURCE_PID</td>
            <td>FK to <a href="#HFJ_RESOURCE">HFJ_RESOURCE</a></td>
            <td>Long</td>
            <td></td>
            <td>
                This is the persistent ID of the resource being versioned.
            </td>        
        </tr>
        <tr>
            <td>FORCED_ID</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                Contains the specific version (starting with 1) of the resource that this row corresponds to. 
            </td>        
        </tr>
        <tr>
            <td>RESOURCE_TYPE</td>
            <td></td>
            <td>String</td>
            <td>
                Contains the string specifying the type of the resource (Patient, Observation, etc).
            </td>
        </tr>
    </tbody>
</table>

<a name="HFJ_RES_LINK"/>

# HFJ_RES_LINK: Search Links

<img src="/hapi-fhir/docs/images/jpa_erd_resource_links.svg" alt="Resources" style="width: 100%; max-width: 600px;"/>

When a resource is created or updated, it is indexed for searching. Any search parameters of type [Reference](http://hl7.org/fhir/search.html#reference) are resolved, and one or more rows may be created in the **HFJ_RES_LINK** table.

## Columns

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>PARTITION_ID</td>
            <td></td>
            <td>Integer</td>
            <td>Nullable</td>
            <td>
                This is the optional partition ID, if the resource is in a partition. See <a href="/hapi-fhir/docs/server_jpa_partitioning/partitioning.html">Partitioning</a>.
                Note that the partition indicated by the <b>PARTITION_ID</b> and <b>PARTITION_DATE</b> columns refers to the partition
                of the <i>SOURCE</i> resource, and not necessarily the <i>TARGET</i>.  
            </td>
        </tr>
        <tr>
            <td>PARTITION_DATE</td>
            <td></td>
            <td>Timestamp</td>
            <td>Nullable</td>
            <td>
                This is the optional partition date, if the resource is in a partition. See <a href="/hapi-fhir/docs/server_jpa_partitioning/partitioning.html">Partitioning</a>.  
                Note that the partition indicated by the <b>PARTITION_ID</b> and <b>PARTITION_DATE</b> columns refers to the partition
                of the <i>SOURCE</i> resource, and not necessarily the <i>TARGET</i>.  
            </td>        
        </tr>
        <tr>
            <td>PID</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                Holds the persistent ID 
            </td>        
        </tr>
        <tr>
            <td>SRC_PATH</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                Contains the FHIRPath expression within the source resource containing the path to the target resource, as supplied by the SearchParameter resource that defined the link.  
            </td>        
        </tr>
        <tr>
            <td>SRC_RESOURCE_ID</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                Contains a FK reference to the resource containing the link to the target resource. 
            </td>        
        </tr>
        <tr>
            <td>TARGET_RESOURCE_ID</td>
            <td></td>
            <td>Long</td>
            <td>Nullable</td>
            <td>
                Contains a FK reference to the resource that is the target resource. Will not be populated if the link contains
                a reference to an external resource, or a canonical reference.
            </td>        
        </tr>
        <tr>
            <td>TARGET_RESOURCE_URL</td>
            <td></td>
            <td>String</td>
            <td>Nullable</td>
            <td>
                If this row contains a reference to an external resource or a canonical reference, this column will contain
                the absolute URL.  
            </td>        
        </tr>
        <tr>
            <td>SP_UPDATED</td>
            <td></td>
            <td>Timestamp</td>
            <td></td>
            <td>
                Contains the last updated timestamp for this row.  
            </td>        
        </tr>
    </tbody>
</table>     

<a name="search-indexes"/>

# Background: Search Indexes

The HFJ_SPIDX (Search Parameter Index) tables are used to index resources for searching. When a resource is created or updated, a set of rows in these tables will be added. These are used for finding appropriate rows to return when performing FHIR searches. There are dedicated tables for supporting each of the non-reference [FHIR Search Datatypes](http://hl7.org/fhir/search.html): Date, Number, Quantity, String, Token, and URI. Note that Reference search parameters are implemented using the [HFJ_RES_LINK](#HFJ_RES_LINK) table above.

<a name="search-hashes"/>

## Search Hashes

The SPIDX tables leverage "hash columns", which contain a hash of multiple columns in order to reduce index size and improve search performance. Hashes currently use the [MurmurHash3_x64_128](https://en.wikipedia.org/wiki/MurmurHash) hash algorithm, keeping only the first 64 bits in order to produce a LongInt value.

For example, all search index tables have columns for storing the search parameter name (**SP_NAME**) and resource type (**RES_TYPE**). An additional column which hashes these two values is provided, called **HASH_IDENTITY**.

In some configurations, the partition ID is also factored into the hashes.

## Tables

<img src="/hapi-fhir/docs/images/jpa_erd_search_indexes.svg" alt="Search Indexes" style="width: 100%; max-width: 900px;"/>

<a name="HFJ_SPIDX_common"/>

## Common Search Index Columns

The following columns are common to **all HFJ_SPIDX_xxx tables**.

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>PARTITION_ID</td>
            <td></td>
            <td>Integer</td>
            <td>Nullable</td>
            <td>
                This is the optional partition ID, if the resource is in a partition. See <a href="/hapi-fhir/docs/server_jpa_partitioning/partitioning.html">Partitioning</a>.
                Note that the partition indicated by the <b>PARTITION_ID</b> and <b>PARTITION_DATE</b> columns refers to the partition
                of the <i>SOURCE</i> resource, and not necessarily the <i>TARGET</i>.  
            </td>
        </tr>
        <tr>
            <td>PARTITION_DATE</td>
            <td></td>
            <td>Timestamp</td>
            <td>Nullable</td>
            <td>
                This is the optional partition date, if the resource is in a partition. See <a href="/hapi-fhir/docs/server_jpa_partitioning/partitioning.html">Partitioning</a>.  
                Note that the partition indicated by the <b>PARTITION_ID</b> and <b>PARTITION_DATE</b> columns refers to the partition
                of the <i>SOURCE</i> resource, and not necessarily the <i>TARGET</i>.  
            </td>        
        </tr>
        <tr>
            <td>SP_ID</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                Holds the persistent ID 
            </td>        
        </tr>
        <tr>
            <td>RES_ID</td>
            <td>FK to <a href="#HFJ_RESOURCE">HFJ_RESOURCE</a></td>
            <td>Long</td>
            <td></td>
            <td>
                Contains the PID of the resource being indexed.  
            </td>        
        </tr>
        <tr>
            <td>SP_NAME</td>
            <td></td>
            <td>String</td>
            <td>Nullable</td>
            <td>
                This is the name of the search parameter being indexed. 
            </td>        
        </tr>
        <tr>
            <td>RES_TYPE</td>
            <td></td>
            <td>String</td>
            <td>Nullable</td>
            <td>
                This is the name of the resource being indexed.
            </td>        
        </tr>
        <tr>
            <td>HASH_IDENTITY</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                A hash of SP_NAME and RES_TYPE.  Used to narrow the table to a specific SearchParameter during sorting, and some queries.
            </td>        
        </tr>
        <tr>
            <td>SP_UPDATED</td>
            <td></td>
            <td>Timestamp</td>
            <td></td>
            <td>
                This is the time that this row was last updated.
            </td>        
        </tr>
        <tr>
            <td>SP_MISSING</td>
            <td></td>
            <td>boolean</td>
            <td></td>
            <td>
                If this row represents a search parameter that is **not** populated at all in the resource being indexed,
                this will be populated with the value `true`. Otherwise it will be populated with `false`.
            </td>        
        </tr>
    </tbody>
</table>

# HFJ_SPIDX_DATE: Date Search Parameters

For any FHIR Search Parameter of type [*date*](https://www.hl7.org/fhir/search.html#date) that generates a database index, a row in the `HFJ_SPIDX_DATE` table will be created.
Range queries with Date parameters (e.g. `Observation?date=ge2020-01-01`) will query the HASH_IDENTITY, SP_VALUE_LOW_DATE_ORDINAL and/or SP_VALUE_HIGH_DATE_ORDINAL columns.
Range queries with DateTime parameters (e.g. `Observation?date=ge2021-01-01T10:30:00`) will query the HASH_IDENTITY, SP_VALUE_LOW and/or SP_VALUE_HIGH columns.
Sorting is done by the SP_VALUE_LOW column.

## Columns

Note: This table has the columns listed below, but it also has all common columns listed above in [Common Search Index Columns](#HFJ_SPIDX_common).

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>SP_VALUE_LOW</td>
            <td></td>
            <td>Timestamp</td>
            <td>Nullable</td>
            <td>
                This is the lower bound of the date in question. 
                <ul>
                    <li>For a point in time date to millisecond precision (such as an Instant with a value of <code>2020-05-26T15:00:00.000</code>) this represents the exact value.</li>
                    <li>For an instant value with lower precision, this represents the start of the possible range denoted by the value. For example, for a value of <code>2020-05-26</code> this represents <code>2020-05-26T00:00:00.000</code>.</li>
                    <li>For a Period with a lower (start) value present, this column contains that value.</li>
                    <li>For a Period with no lower (start) value present, this column contains a timestamp representing the "start of time".</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td>SP_VALUE_HIGH</td>
            <td></td>
            <td>Timestamp</td>
            <td>Nullable</td>
            <td>
                This is the upper bound of the date in question. 
                <ul>
                    <li>For a point in time date to millisecond precision (such as an Instant with a value of <code>2020-05-26T15:00:00.000</code>) this represents the exact value.</li>
                    <li>For an instant value with lower precision, this represents the start of the possible range denoted by the value. For example, for a value of <code>2020-05-26</code> this represents <code>2020-05-26T23:59:59.999</code>.</li>
                    <li>For a Period with an upper (end) value present, this column contains that value.</li>
                    <li>For a Period with no upper (end) value present, this column contains a timestamp representing the "end of time".</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td>SP_VALUE_LOW_DATE_ORDINAL</td>
            <td></td>
            <td>Integer</td>
            <td>Nullable</td>
            <td>
                This column contains the same Timestamp as <code>SP_VALUE_LOW</code>, but truncated to Date precision and formatted as an integer in the format "YYYYMMDD".
            </td>
        </tr>
        <tr>
            <td>SP_VALUE_HIGH_DATE_ORDINAL</td>
            <td></td>
            <td>Integer</td>
            <td>Nullable</td>
            <td>
                This column contains the same Timestamp as <code>SP_VALUE_HIGH</code>, but truncated to Date precision and formatted as an integer in the format "YYYYMMDD".
            </td>
        </tr>
    </tbody>
</table>

# HFJ_SPIDX_NUMBER: Number Search Parameters

FHIR Search Parameters of type [*number*](https://www.hl7.org/fhir/search.html#number) produce rows in the `HFJ_SPIDX_NUMBER` table.
Range queries and sorting use the HASH_IDENTITY and SP_VALUE columns.

## Columns

Note: This table has the columns listed below, but it also has all common columns listed above in [Common Search Index Columns](#HFJ_SPIDX_common).

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>SP_VALUE</td>
            <td></td>
            <td>Double</td>
            <td>Not nullable</td>
            <td>
                This is the value extracted by the SearchParameter expression. 
            </td>
        </tr>
    </tbody>
</table>



# HFJ_SPIDX_QUANTITY: Quantity Search Parameters

FHIR Search Parameters of type [*quantity*](https://www.hl7.org/fhir/search.html#quantity) produce rows in the `HFJ_SPIDX_QUANTITY` table.
Range queries (e.g. `Observation?valueQuantity=gt100`) with no units provided will query the HASH_IDENTITY and SP_VALUE columns.
Range queries (e.g. `Observation?valueQuantity=gt100||mmHg`) with a unit but not unit-sytem provided will use the HASH_IDENTITY_AND_UNITS and SP_VALUE columns.
Range queries (e.g. `Observation?valueQuantity=gt100|http://unitsofmeasure.org|mmHg`) with a full system and unit will use the HASH_IDENTITY_SYS_UNITS and SP_VALUE columns.
Sorting is done via the HASH_IDENTITY and SP_VALUE columns.

## Columns

Note: This table has the columns listed below, but it also has all common columns listed above in [Common Search Index Columns](#HFJ_SPIDX_common).

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>HASH_IDENTITY_AND_UNITS</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                A hash like HASH_IDENTITY that also includes the SP_UNITS column.
            </td>
        </tr>
        <tr>
            <td>HASH_IDENTITY_SYS_UNITS</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                A hash like HASH_IDENTITY that also includes the SP_SYSTEM and SP_UNITS columns.
            </td>
        </tr>
        <tr>
            <td>SP_SYSTEM</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                The system of the quantity units.  e.g. "http://unitsofmeasure.org".
            </td>
        </tr>
        <tr>
            <td>SP_UNITS</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                The units of the quantity.  E.g. "mg".
            </td>
        </tr>
        <tr>
            <td>SP_VALUE</td>
            <td></td>
            <td>Double</td>
            <td></td>
            <td>
                This is the value extracted by the SearchParameter expression. 
            </td>
        </tr>
    </tbody>
</table>

# HFJ_SPIDX_QUANTITY_NRML: Normalized Quantity Search Parameters

Hapi Fhir supports searching by normalized units when enabled (see https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-jpaserver-model/ca/uhn/fhir/jpa/model/entity/StorageSettings.html#getNormalizedQuantitySearchLevel()).
When this feature is enabled, each row stored in HFJ_SPIDX_QUANTITY to also store a row in HFJ_SPIDX_QUANTITY_NRML in canonical UCUM units.
E.g. a weight recorded in an Observation as 
```
"valueQuantity" : {
    "value" : 172,
    "unit" : "lb_av",
    "system" : "http://unitsofmeasure.org",
    "code" : "[lb_av]"
  },
```
would match the search `Observation?valueQuantity=172`, 
but would also match the search `Observation?valueQuantity=78|http://unitsofmeasure.org|kg`.
The row in HFJ_SPIDX_QUANTITY would contain the value 172 pounds, while the HFJ_SPIDX_QUANTITY_NRML table would hold the equivalent 78 kg value.
Only value searches that provide fully qualified units are eligible for normalized searches.
Sorting only uses the HFJ_SPIDX_QUANTITY table.

## Columns

Same as HFJ_SPIDX_QUANTITY above, except the SP_VALUE, SP_SYSTEM, and SP_UNITS columns hold the converted value in canonical units instead of the value extracted by the SearchParameter.
This table is only used for range queries with a unit which can be converted to canonical units.


# HFJ_SPIDX_STRING: String Search Parameters

FHIR Search Parameters of type [*string*](https://www.hl7.org/fhir/search.html#string) produce rows in the `HFJ_SPIDX_STRING` table.
The default string search matches by prefix, ignoring case or accents.  This uses the HASH_IDENTITY column and a LIKE prefix clause on the SP_VALUE_NORMALIZED columns.
The `:exact` string search matches exactly. This uses only the HASH_EXACT column.
Sorting is done via the HASH_IDENTITY and SP_VALUE_NORMALIZED columns.

## Columns

Note: This table has the columns listed below, but it also has all common columns listed above in [Common Search Index Columns](#HFJ_SPIDX_common).

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>HASH_EXACT</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                A hash like HASH_IDENTITY that also includes the SP_VALUE_EXACT column.
            </td>
        </tr>
        <tr>
            <td>SP_VALUE_NORMALIZED</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                An UPPERCASE string with accents removed.
            </td>
        </tr>
        <tr>
            <td>SP_VALUE_EXACT</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                The extracted string unchanged.
            </td>
        </tr>
    </tbody>
</table>

# HFJ_SPIDX_TOKEN: Token Search Parameters

FHIR Search Parameters of type [*token*](https://www.hl7.org/fhir/search.html#token) extract values of type Coding, code, and others.
These produce rows in the `HFJ_SPIDX_TOKEN` table.
The default token search accepts three parameter formats: matching the code (e.g. `Observation?code=15074-8`), 
matching both system and code (e.g. `Observation?code=http://loinc.org|15074-8`), 
or matching a system with any code  (e.g. `Observation?http://loinc.org|`).
All three are exact searches and use the hashes: HASH_VALUE, HASH_SYS_AND_VALUE, and HASH_SYS respectively.
Sorting is done via the HASH_IDENTITY and SP_VALUE columns.

## Columns

Note: This table has the columns listed below, but it also has all common columns listed above in [Common Search Index Columns](#HFJ_SPIDX_common).

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>HASH_VALUE</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                A hash like HASH_IDENTITY that also includes the SP_VALUE column.
            </td>
        </tr>
        <tr>
            <td>HASH_SYS_AND_VALUE</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                A hash like HASH_IDENTITY that also includes the SP_SYSTEM and SP_VALUE columns.
            </td>
        </tr>
        <tr>
            <td>HASH_SYS</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                A hash like HASH_IDENTITY that also includes the SP_SYSTEM column.
            </td>
        </tr>
        <tr>
            <td>SP_SYSTEM</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                The system of the code.
            </td>
        </tr>
        <tr>
            <td>SP_VALUE</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                This is the bare code value. 
            </td>
        </tr>
    </tbody>
</table>


# HFJ_SPIDX_URI: URI Search Parameters

FHIR Search Parameters of type [*uri*](https://www.hl7.org/fhir/search.html#uri) produce rows in the `HFJ_SPIDX_URI` table.
The default uri search matches the complete uri.  This uses the HASH_URI column for an exact match.
A uri search with the `:above` modifier will match any prefix. This also uses the HASH_URI column, but also tests hashes of every prefix of the query value.
A uri search with the `:below` modifier will match any extension. This query uses the HASH_IDENTITY and a LIKE prefix match of the SP_URI column.
Sorting is done via the HASH_IDENTITY and SP_URI columns.

## Columns

Note: This table has the columns listed below, but it also has all common columns listed above in [Common Search Index Columns](#HFJ_SPIDX_common).

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>HASH_URI</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                A hash like HASH_IDENTITY that also includes the SP_URI column.
            </td>
        </tr>
        <tr>
            <td>SP_URI</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                The uri string extracted by the SearchParameter.
            </td>
        </tr>
    </tbody>
</table>


# HFJ_IDX_CMB_TOK_NU: Combo Non-Unique Search Param

This table is used to index [Non-Unique Combo Search Parameters](https://smilecdr.com/docs/fhir_standard/fhir_search_custom_search_parameters.html#combo-search-index-parameters). 

## Columns

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>PID</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                A unique persistent identifier for the given index row.  
            </td>
        </tr>
        <tr>
            <td>RES_ID</td>
            <td>FK to <a href="#HFJ_RESOURCE">HFJ_RESOURCE</a></td>
            <td>Long</td>
            <td></td>
            <td>
                Contains the PID of the resource being indexed.  
            </td>
        </tr>
        <tr>
            <td>IDX_STRING</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                This column contains a FHIR search expression indicating what is being indexed. For example, if a 
                non-unique combo search parameter is present which indexes a combination of Observation#code and
                Observation#status, this column might contain a value such as
                <code>Observation?code=http://loinc.org|1234-5&status=final</code>
            </td>
        </tr>
        <tr>
            <td>HASH_COMPLETE</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                This column contains a hash of the value in column <code>IDX_STRING</code>.
            </td>
        </tr>
    </tbody>
</table>

<a name="HFJ_IDX_CMP_STRING_UNIQ"/>

# HFJ_IDX_CMP_STRING_UNIQ: Combo Unique Search Param

This table is used to index [Unique Combo Search Parameters](https://smilecdr.com/docs/fhir_standard/fhir_search_custom_search_parameters.html#combo-search-index-parameters).

## Columns

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>PID</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                A unique persistent identifier for the given index row.  
            </td>
        </tr>
        <tr>
            <td>RES_ID</td>
            <td>FK to <a href="#HFJ_RESOURCE">HFJ_RESOURCE</a></td>
            <td>Long</td>
            <td></td>
            <td>
                Contains the PID of the resource being indexed.  
            </td>
        </tr>
        <tr>
            <td>IDX_STRING</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                This column contains a FHIR search expression indicating what is being indexed. For example, if a 
                unique combo search parameter is present which indexes a combination of Observation#code and
                Observation#status, this column might contain a value such as
                <code>Observation?code=http://loinc.org|1234-5&status=final</code>
            </td>
        </tr>
        <tr>
            <td>HASH_COMPLETE</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                This column contains a hash of the value in column <code>IDX_STRING</code>.
            </td>
        </tr>
        <tr>
            <td>HASH_COMPLETE_2</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                This column contains an additional hash of the value in column <code>IDX_STRING</code>, using a
                static salt of the value prior to the hashing. This is done in order to increase the number
                of bits used to hash the index string from 64 to 128.
            </td>
        </tr>
    </tbody>
</table>

