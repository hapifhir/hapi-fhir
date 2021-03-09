# Frequently Asked Questions

# Getting Help

### Where can I ask questions or get help?

Please see [this page](https://github.com/hapifhir/hapi-fhir/wiki/Getting-Help) in the HAPI FHIR Wiki for information on getting help.

# Using HAPI FHIR

### What JDK version does HAPI support?

See the [HAPI FHIR Versions](/docs/getting_started/versions.html) page for information on the current minimum Java version required in order to use HAPI FHIR.

# JPA Server

### I would like to connect to the Derby database using a JDBC database browser (e.g. Squirrel, Toad, DBVisualizer) so that I can access the underlying tables. How do I do that?

By default Derby doesn't actually open any TCP ports for you to connect externally to it. Being an embedded database, it works a bit differently than other databases in that the client actually is the database and there's no outside communication with it possible.

There are a few options available to work around this fact:

* The easiest thing is to just load your data using the FHIR API. E.g. you can use HTTP/REST creates, transactions, etc to load data into your database directly.

* If you want to access the underlying database, the next easiest thing is to configure the database to use a filesystem directory, e.g. `jdbc:derby:directory:target/jpaserver_derby_files;create=true`. You can then shut the server down and use that same URL to connect a derby client (e.g. Squirrel or DBVisualizer) to the same path. You may need to use a fully qualified path instead of a relative one though.

* Another option is to use a different database (e.g. MySQL, Postgres, Oracle, etc.). HAPI's JPA server is based on JPA/Hibernate so it will support any database platform that hibernate supports.

* A final option is to start up Derby in network mode. Doing this is a bit more involved since you need to start the derby server separately, and then use a special URL to connect to it.

# Contributing

### My build is failing with the following error: *The forked VM terminated without properly saying goodbye. VM crash or System.exit called?*

The complete error message typically resembles:

```
Failed to execute goal org.apache.maven.plugins:maven-surefire-plugin:2.19.1:test (default-test) on project hapi-fhir-jpaserver-base: Execution default-test of goal org.apache.maven.plugins:maven-surefire-plugin:2.19.1:test failed: The forked VM terminated without properly saying goodbye. VM crash or System.exit called?
```

This typically means that your build is running out of memory. HAPI's unit tests execute by default in multiple threads (the thread count is determined by the number of CPU cores available) so in an environment with lots of cores but not enough RAM, you may run out. If you are getting this error, try executing the build with the following arguments:

```
mvn -P ALLMODULES,NOPARALLEL install
```

See [Hacking HAPI FHIR](/docs/contributing/hacking_guide.html) for more information on the build process.
