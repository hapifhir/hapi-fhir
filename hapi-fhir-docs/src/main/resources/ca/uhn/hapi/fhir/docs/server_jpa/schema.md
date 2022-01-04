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

<a name="HFJ_FORCED_ID"/>

# HFJ_FORCED_ID: Client Assigned/Visible Resource IDs

By default, the **HFJ_RESOURCE.RES_ID** column is used as the resource ID for all server-assigned IDs. For example, if a Patient resource is created in a completely empty database, it will be assigned the ID `Patient/1` by the server and RES_ID will have a value of 1.

However, when client-assigned IDs are used, these may contain text values to allow a client to create an ID such
as `Patient/ABC`. When a client-assigned ID is given to a resource, a row is created in the **HFJ_RESOURCE** table. When
an **HFJ_FORCED_ID** row exists corresponding to the equivalent **HFJ_RESOURCE** row, the RES_ID value is no longer
visible or usable by FHIR clients and it becomes purely an internal ID to the JPA server.

If the server has been configured with
a [Resource Server ID Strategy](/apidocs/hapi-fhir-storage/undefined/ca/uhn/fhir/jpa/api/config/DaoConfig.html#setResourceServerIdStrategy(ca.uhn.fhir.jpa.api.config.DaoConfig.IdStrategyEnum))
of [UUID](/apidocs/hapi-fhir-storage/undefined/ca/uhn/fhir/jpa/api/config/DaoConfig.IdStrategyEnum.html#UUID), or the
server has been configured with
a [Resource Client ID Strategy](/apidocs/hapi-fhir-storage/undefined/ca/uhn/fhir/jpa/api/config/DaoConfig.html#setResourceClientIdStrategy(ca.uhn.fhir.jpa.api.config.DaoConfig.ClientIdStrategyEnum))
of [ANY](/apidocs/hapi-fhir-storage/undefined/ca/uhn/fhir/jpa/api/config/DaoConfig.ClientIdStrategyEnum.html#ANY)
the server will create a Forced ID for all resources (not only resources having textual IDs).

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

## Columns

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
            <td></td>
            <td>
                This is the name of the search parameter being indexed. 
            </td>        
        </tr>
        <tr>
            <td>RES_TYPE</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                This is the name of the resource being indexed.
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

For any FHIR Search Parameter of type *date* that generates a database index, a row in the *HFJ_SPIDX_DATE* table will be created.

## Columns

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
