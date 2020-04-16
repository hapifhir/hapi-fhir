# HAPI FHIR JPA Schema

**This page is a work in progress. It is not yet comprehensive.**

It contains a description of the tables within the HAPI FHIR JPA database. Note that columns are shown using Java datatypes as opposed to SQL datatypes, because the exact SQL datatype used will vary depending on the underlying database platform. 

# Concepts: PID

The HAPI FHIR JPA schema relies heavily on the concept of internal persistent IDs on tables, using a Java type of Long (8-byte integer, which translates to an *int8* or *number(19)* on various database platforms).

Many tables use an internal persistent ID as their primary key, allowing the flexibility for other more complex business identifiers to be changed and minimizing the amount of data consumed by foreign key relationships. These persistent ID columns are generally assigned using a dedicated database sequence on platforms which support sequences.

The persistent ID column is generally called `PID` in the database schema, although there are exceptions.

# Individual Resources: HFJ_RESOURCE, HFJ_RES_VER, HFJ_FORCED_ID

<img src="/hapi-fhir/docs/images/jpa_erd_resources.png" alt="Resources" align="right"/>

<a name="HFJ_RESOURCE"/>

# HFJ_RESOURCE: Resource Master Table

The HFJ_RESOURCE table indicates a single resource of any type in the database. For example, the resource `Patient/1` will have exactly one row in this table, representing all versions of the resource.


<a name="HFJ_RES_VER"/>

# HFJ_RES_VER: Resource Versions and Contents

The HFJ_RES_VER table contains individual versions of a resource. If the resource `Patient/1` has 3 versions, there will be 3 rows in this table.

The complete raw contents of the resource is stored in the `RES_TEXT` column, using the encoding specified in the `RES_ENCODING` column.

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

However, when client-assigned IDs are used, these may contain text values in order to allow a client to create an ID such as `Patient/ABC`. When a client-assigned ID is given to a resource, a row is created in the **HFJ_RESOURCE** table. When an **HFJ_FORCED_ID** row exists corresponding to the equivalent **HFJ_RESOURCE** row, the RES_ID value is no longer visible or usable by FHIR clients and it becomes purely an internal ID to the JPA server.

If the server has been configured with a [Resource Server ID Strategy](/apidocs/hapi-fhir-jpaserver-api/undefined/ca/uhn/fhir/jpa/api/config/DaoConfig.html#setResourceServerIdStrategy(ca.uhn.fhir.jpa.api.config.DaoConfig.IdStrategyEnum)) of [UUID](/apidocs/hapi-fhir-jpaserver-api/undefined/ca/uhn/fhir/jpa/api/config/DaoConfig.IdStrategyEnum.html#UUID), or the server has been configured with a [Resource Client ID Strategy](/apidocs/hapi-fhir-jpaserver-api/undefined/ca/uhn/fhir/jpa/api/config/DaoConfig.html#setResourceClientIdStrategy(ca.uhn.fhir.jpa.api.config.DaoConfig.ClientIdStrategyEnum)) of [ANY](/apidocs/hapi-fhir-jpaserver-api/undefined/ca/uhn/fhir/jpa/api/config/DaoConfig.ClientIdStrategyEnum.html#ANY) the server will create a Forced ID for all resources (not only resources having textual IDs).


<a name="HFJ_RES_LINK"/>

# HFJ_RES_LINK: Search Links

<img src="/hapi-fhir/docs/images/jpa_erd_resources.png" alt="Resources" align="right"/>

When a resource is created or updated, it is indexed for searching. Any search parameters of type [Reference](http://hl7.org/fhir/search.html#reference) are resolved, and one or more rows may be created in the **HFJ_RES_LINK** table.

## Columns

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Datatype</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>PID</td>
            <td>Long</td>
            <td>
                Holds the persistent ID 
            </td>        
        </tr>
        <tr>
            <td>SRC_PATH</td>
            <td>String</td>
            <td>
                Contains the FHIRPath expression within the source resource containing the path to the target resource, as supplied by the SearchParameter resource that defined the link.  
            </td>        
        </tr>
        <tr>
            <td>SRC_RESOURCE_ID</td>
            <td>Long</td>
            <td>
                Contains a FK reference to the resource containing the link to the target resource. 
            </td>        
        </tr>
        <tr>
            <td>TARGET_RESOURCE_ID</td>
            <td>Long</td>
            <td>
                Contains a FK reference to the resource that is the target resource. 
            </td>        
        </tr>
    </tbody>
</table>     

<a name="indexes"/>

# HFJ_SPIDX_xxx: Search Indexes

The HFJ_SPIDX (Search Parameter Index) tables are used to index resources for searching. When a resource is created or updated, a set of rows in these tables will be added. These are used for finding appropriate rows to return when performing FHIR searches.
