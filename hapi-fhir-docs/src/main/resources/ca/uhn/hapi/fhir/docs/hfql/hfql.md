# HFQL Driver: SQL For FHIR Repositories

<div class="helpInfoCalloutBox">
This is an <a href="https://smilecdr.com/docs/introduction/maturity_model.html">experimental module</a>. Use with caution. This API is likely to change.
</div>

The HAPI FHIR JPA server can optionally be configured to support SQL-like queries against the FHIR repository. This module is intended for analytical queries. It is not optimized for performance, and may take a long time to produce results.

# Syntax

This module uses a proprietary flavour of SQL that is specific to HAPI FHIR. It is similar to the [Firely Query Language](https://simplifier.net/docs/fql), although it also has differences.  

A simple example query is shown below:

```sql
SELECT
    name[0].family as family, 
    name[0].given[0] as given, 
    birthDate,
    identifier.where(system='http://hl7.org/fhir/sid/us-ssn').value as SSN
FROM
    Patient
WHERE
    active = true
```

See [SQL Syntax](https://smilecdr.com/docs/hfql/sql_syntax.html) for details on this syntax.

# JDBC Driver

When HFQL is enabled on the server, a JDBC-compatible driver is available. This can be used to query the FHIR server directly from a JDBC compliant database browser.

This module has been tested with [DBeaver](https://dbeaver.io/), which is a free and excellent database browser. Other JDBC compatible database tools may also work. Note that not all JDBC API methods have been implemented in the driver, so other tools may use methods that have not yet been implemented. Please let us know in the [Google Group](https://groups.google.com/g/hapi-fhir) if you encounter issues or have suggestions.

The JDBC driver can be downloaded from the [GitHub Releases site](https://github.com/hapifhir/hapi-fhir/releases). It can also be built from sources by executing the following command:

```bash
mvn -DskipTests -P DIST clean install -pl :hapi-fhir-jpaserver-hfql -am
```

To import this driver into your database tool, import the JDBC JAR and use the following settings:

<table class="table table-striped table-condensed">
<thead><tr><th>Setting</th><th>Description</th></tr></thead>
<tbody>
<tr>
<td>Class Name</td><td>ca.uhn.fhir.jpa.fql.jdbc.JdbcDriver</td>
</tr><tr>
<td>URL</td><td>jdbc:hapifhirql:[server_base_url]</td>
</tr><tr>
<td>Username</td><td rowspan="2">If provided, the username/password will be added as an HTTP Basic Authorization header on all requests to the server.</td>
</tr><tr>
<td>Password</td>
</tr>
</tbody>
</table>
