# JPA Server

The HAPI FHIR [RestfulServer](/hapi-fhir/docs/server/restful_server.html) module can be used to create a FHIR server endpoint against an arbitrary data source, which could be a database of your own design, an existing clinical system, a set of files, or anything else you come up with.

HAPI also provides a persistence module which can be used to provide a complete RESTful server implementation, backed by a database of your choosing. This module uses the [JPA 2.0](http://en.wikipedia.org/wiki/Java_Persistence_API) API to store data in a database without depending on any specific database technology.

**Important Note:** This implementation uses a fairly simple table design, with a single table being used to hold resource bodies (which are stored as CLOBs, optionally GZipped to save space) and a set of tables to hold search indexes, tags, history details, etc. This design is only one of many possible ways
of designing a FHIR server so it is worth considering whether it is appropriate for the problem you are trying to solve.

# Getting Started

The recommended way to get started with HAPI's JPA server module is
to begin with the starter project. This project can be found at the following link: [https://github.com/hapifhir/hapi-fhir-jpaserver-starter](https://github.com/hapifhir/hapi-fhir-jpaserver-starter)

This project is a fully contained FHIR server, supporting all standard operations (read/create/delete/etc). It bundles an embedded instance of the <a href="http://h2database.com">H2 Java Database</a> so that the server can run without depending on any external database, but it can also be configured to use an installation of Oracle, Postgres, etc.

# Configuration Options

## External/Absolute Resource References

Clients may sometimes post resources to your server that contain absolute resource references. For example, consider the following resource:

```xml

<Patient xmlns="http://hl7.org/fhir">
   <id value="patient-infant-01"/>
   <name>
      <use value="official"/>
      <family value="Miller"/>
      <given value="Samuel"/>
   </name>
   <managingOrganization>
      <reference value="http://example.com/fhir/Organization/123"/>
   </managingOrganization>
</Patient>
```

By default, the server will reject this reference, as only local references are permitted by the server. This can be changed however.

If you want the server to recognize that this URL is actually a local reference (i.e. because the server will be deployed to the base URL `http://example.com/fhir/`) you can configure the server to recognize this URL via the following DaoConfig setting:

```java
@Bean
public DaoConfig daoConfig() {
	DaoConfig retVal = new DaoConfig();
	// ... other config ...
	retVal.getTreatBaseUrlsAsLocal().add("http://example.com/fhir/");
	return retVal;
}
``` 

On the other hand, if you want the server to be configurable to allow remote references, you can set this with the confguration below. Using the `setAllowExternalReferences` means that it will be possible to search for references that refer to these external references.

```java
@Bean
public DaoConfig daoConfig() {
	DaoConfig retVal = new DaoConfig();
	// Allow external references
	retVal.setAllowExternalReferences(true);
	
	// If you are allowing external references, it is recommended to
	// also tell the server which references actually will be local
	retVal.getTreatBaseUrlsAsLocal().add("http://mydomain.com/fhir");
	return retVal;
}
```

## Logical References

In some cases, you may have references which are <i>Logical References</i>,
which means that they act as an identifier and not necessarily as a literal
web address.

A common use for logical references is in references to conformance resources, such as ValueSets, StructureDefinitions, etc. For example, you might refer to the ValueSet `http://hl7.org/fhir/ValueSet/quantity-comparator` from your own resources. In this case, you are not neccesarily telling the server that this is a real address that it should resolve, but rather that this is an identifier for a ValueSet where `ValueSet.url` has the given URI/URL.

HAPI can be configured to treat certain URI/URL patterns as logical by using the DaoConfig#setTreatReferencesAsLogical property (see [JavaDoc](/hapi-fhir/apidocs/hapi-fhir-jpaserver-base/ca/uhn/fhir/jpa/dao/DaoConfig.html#setTreatReferencesAsLogical-java.util.Set-)).

For example:

```java
// Treat specific URL as logical
myDaoConfig.getTreatReferencesAsLogical().add("http://mysystem.com/ValueSet/cats-and-dogs");

// Treat all references with given prefix as logical
myDaoConfig.getTreatReferencesAsLogical().add("http://mysystem.com/mysystem-vs-*");
```

# Search Result Caching

By default, search results will be cached for one minute. This means that if a client performs a search for <code>Patient?name=smith</code> and gets back 500 results, if a client performs the same search within 60000 milliseconds the previously loaded search results will be returned again. This also means that any new Patient resources named "Smith" within the last minute will not be reflected in the results.

Under many normal scenarios this is a n acceptable performance tradeoff, but in some cases it is not. If you want to disable caching, you have two options:

### Globally Disable / Change Caching Timeout

You can change the global cache using the following setting:

```java
myDaoConfig.setReuseCachedSearchResultsForMillis(null);
```

### Disable Cache at the Request Level

Clients can selectively disable caching for an individual request using the Cache-Control header:

```http
Cache-Control: no-cache
```

### Disable Paging at the Request Level

If the client knows that they will only want a small number of results (for example, a UI containing 20 results is being shown and the client knows that they will never load the next page of results) the client
may also use the <code>no-store</code> directive along with a HAPI FHIR extension called <code>max-results</code> in order to specify that only the given number of results should be fetched. This directive disabled paging entirely for the request and causes the request to return immediately when the given number of results is found. This can cause a noticeable performance improvement in some cases.

```http
Cache-Control: no-store, max-results=20
```

# Architecture

<img src="/hapi-fhir/docs/images/jpa_architecture.png" alt="Architecture" align="right"/>

The HAPI JPA Server has the following components:

* **Resource Providers:** A RESTful server [Resource Provider](/hapi-fhir/docs/server/restful_server.html#resource_providers) is provided for each resource type in a given release of FHIR. Each resource provider implements a	[@Search](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Search.html) method implementing the complete set of search parameters defined in the FHIR specification for the given resource type.

   The resource providers also extend a superclass which implements all of the other FHIR methods, such as Read, Create, Delete, etc.
   
   Note that these resource providers are generated as a part of the HAPI build process, so they are not checked into Git. The resource providers do not actually implement any of the logic in searching, updating, etc. They simply receive the incoming HTTP calls (via the RestfulServer) and pass along the incoming requests to the DAOs.
   
* **HAPI DAOs:** The DAOs actually implement all of the database business logic relating to the storage, indexing, and retrieval of FHIR resources, using the underlying JPA API.

* **Hibernate:** The HAPI JPA Server uses the JPA library, implemented by Hibernate. No Hibernate specific features are used, so the library should also work with other providers (e.g. Eclipselink) but it is not tested regularly with them.

* **Database:** The RESTful server uses an embedded Derby database, but can be configured to talk to [any database supported by Hibernate](https://developer.jboss.org/wiki/SupportedDatabases2?_sscc=t).

# Additional Information

* [This page](https://www.openhealthhub.org/t/hapi-terminology-server-uk-snomed-ct-import/592) has information on loading national editions (UK specifically) of SNOMED CT files into the database.

<a name="upgrading"/>

# Upgrading HAPI FHIR JPA

HAPI FHIR JPA is a constantly evolving product, with new features being added to each new version of the library. As a result, it is generally necessary to execute a database migration as a part of an upgrade to HAPI FHIR.

When upgrading the JPA server from one version of HAPI FHIR to a newer version, often there will be changes to the database schema. The **Migrate Database** command can be used to perform a migration from one version to the next.

Note that this feature was added in HAPI FHIR 3.5.0. It is not able to migrate from versions prior to HAPI FHIR 3.4.0. **Please make a backup of your database before running this command!**

The following example shows how to use the migrator utility to migrate between two versions.

```bash
./hapi-fhir-cli migrate-database -d DERBY_EMBEDDED -u "jdbc:derby:directory:target/jpaserver_derby_files;create=true" -n "" -p "" -f V3_4_0 -t V3_5_0
```

You may use the following command to get detailed help on the options:

```bash
./hapi-fhir-cli help migrate-database
```

Note the arguments:

* `-d [dialect]` &ndash; This indicates the database dialect to use. See the detailed help for a list of options
* `-f [version]` &ndash; The version to migrate from
* `-t [version]` &ndash; The version to migrate to

# Oracle Support

Note that the Oracle JDBC drivers are not distributed in the Maven Central repository, so they are not included in HAPI FHIR. In order to use this command with an Oracle database, you will need to invoke the CLI as follows:

```bash
java -cp hapi-fhir-cli.jar ca.uhn.fhir.cli.App migrate-database -d ORACLE_12C -u "[url]" -n "[username]" -p "[password]" -f V3_4_0 -t V3_5_0
```

## Migrating 3.4.0 to 3.5.0+

As of HAPI FHIR 3.5.0 a new mechanism for creating the JPA index tables (HFJ_SPIDX_xxx) has been implemented. This new mechanism uses hashes in place of large multi-column indexes. This improves both lookup times as well as required storage space. This change also paves the way for future ability to provide efficient multi-tenant searches (which is not yet implemented but is planned as an incremental improvement).

This change is not a lightweight change however, as it requires a rebuild of the index tables in order to generate the hashes. This can take a long time on databases that already have a large amount of data.

As a result, in HAPI FHIR JPA 3.6.0, an efficient way of upgrading existing databases was added. Under this new scheme, columns for the hashes are added but values are not calculated initially, database indexes are not modified on the HFJ_SPIDX_xxx tables, and the previous columns are still used for searching as was the case in HAPI FHIR JPA 3.4.0.

In order to perform a migration using this functionality, the following steps should be followed:

* Stop your running HAPI FHIR JPA instance (and remember to make a backup of your database before proceeding with any changes!)
* Modify your `DaoConfig` to specify that hash-based searches should not be used, using the following setting: `myDaoConfig.setDisableHashBasedSearches(true);`
* Make sure that you have your JPA settings configured to not automatically create database indexes and columns using the following setting in your JPA Properties: `extraProperties.put("hibernate.hbm2ddl.auto", "none");`
* Run the database migrator command, including the entry `-x no-migrate-350-hashes` on the command line. For example:

```
./hapi-fhir-cli migrate-database -d DERBY_EMBEDDED -u "jdbc:derby:directory:target/jpaserver_derby_files;create=true" -n "" -p "" -f V3_4_0 -t V3_6_0 -x no-migrate-350-hashes
```

* Rebuild and start your HAPI FHIR JPA server. At this point you should have a working HAPI FHIR JPA 3.6.0 server that is is still using HAPI FHIR 3.4.0 search indexes. Search hashes will be generated for any newly created or updated data but existing data will have null hashes.
* With the system running, request a complete reindex of the data in the database using
an HTTP request such as the following: `POST /$mark-all-resources-for-reindexing`. Note that this is a custom operation built into the HAPI FHIR JPA server. It should be secured in a real deployment, so Authentication is likely required for this call.
* You can track the reindexing process by watching your server logs, but also by using the following SQL executed directly against your database:

```sql
SELECT * FROM HFJ_RES_REINDEX_JOB
```

* When this query no longer returns any rows, the reindexing process is complete.
* At this time, HAPI FHIR should be stopped once again in order to convert it to using the hash based indexes.
* Modify your `DaoConfig` to specify that hash-based searches are used, using the following setting (this is the default setting, so it could also simply be omitted): `myDaoConfig.setDisableHashBasedSearches(false);`
* Execute the migrator tool again, this time omitting the flag option, e.g.

```bash
./hapi-fhir-cli migrate-database -d DERBY_EMBEDDED -u "jdbc:derby:directory:target/jpaserver_derby_files;create=true" -n "" -p "" -f V3_4_0 -t V3_6_0
```
* Rebuild, and start HAPI FHIR JPA again.

# Cascading Deletes

An interceptor called `CascadingDeleteInterceptor` may be registered against the Server. When this interceptor is enabled, cascading deletes may be performed using either of the following:

* The request may include the following parameter: `_cascade=delete`
* The request may include the following header: `X-Cascade: delete`

