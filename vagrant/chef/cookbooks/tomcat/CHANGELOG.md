tomcat Cookbook CHANGELOG
=========================
This file is used to list changes made in each version of the tomcat cookbook.

v0.16.2 (2014-08-06)
--------------------
- #80 - Fix broken server.xml when not using ssl

v0.16.0 (2014-06-11)
--------------------
- #70 - [COOK-4332] Support running multiple instance
- #73 - Be pedantic on where the data bag secret should be placed.


v0.15.12 (2014-04-23)
---------------------
- [COOK-3745] - Scientific Linux support for Tomcat
- [COOK-4573] - Oracle Linux support for Tomcat
- [COOK-4574] - genkeypair is not a valid parameter to keytool
- [COOK-4575] - update test harness for Ubuntu 14.04'


v0.15.10 (2014-03-27)
---------------------
- [COOK-4487] - Use tomcat group in /etc/default template


v0.15.8 (2014-03-19)
--------------------
- [COOK-4209] - Remove "Host element/XML Validation" in tomcat 7 and above


v0.15.6 (2014-03-12)
--------------------
- [COOK-4301] - Duplicate truststore settings added to java_options every chef-client run


v0.15.4 (2014-02-18)
--------------------
### Improvement
- **[COOK-4258](https://tickets.opscode.com/browse/COOK-4258)** - tomcat: support for Apache mod_jk load balancing with jvmRoute
- **[COOK-3370](https://tickets.opscode.com/browse/COOK-3370)** - Don't install the tomcat manager apps package if we're not going to use it

### Bug
- **[COOK-4257](https://tickets.opscode.com/browse/COOK-4257)** - tomcat: broken on SmartOS SmartMachine images 13.3.0+


v0.15.2
-------
### New Feature
- [COOK-3622] - Add support for Amazon platform to the tomcat cookbook.

### Bug
- [COOK-3379] - Only regenerate keystore and restart tomcat when source files change
- [COOK-1599] - Add retry and delay to tomcat service definition


v0.15.0
-------
### Improvement
- **[COOK-3565](https://tickets.opscode.com/browse/COOK-3565)** - Make server.xml connectors maxThreads params configurable via attributes

### New Feature
- **[COOK-3333](https://tickets.opscode.com/browse/COOK-3333)** - Add SmartOS support


v0.14.4
-------
### Bug
- **[COOK-3378](https://tickets.opscode.com/browse/COOK-3378)** - Use keystore in the port 8443 connector
- **[COOK-3204](https://tickets.opscode.com/browse/COOK-3204)** - Fix hard-coded path to `tomcat-users.xml`
- **[COOK-3203](https://tickets.opscode.com/browse/COOK-3203)** - Support "reload" on Ubuntu 12.04

### Improvement
- **[COOK-3195](https://tickets.opscode.com/browse/COOK-3195)** - Fix error for creating endorsed dir
- **[COOK-3083](https://tickets.opscode.com/browse/COOK-3083)** - Add an attribute to lib directory

v0.14.2
-------
### Bug
- [COOK-3165]: Typo in tomcat attributes/default.rb file for `webapp_dir` attribute on Debian/Ubuntu

v0.14.0
-------
### Sub-task
- [COOK-1808]: Add Support for Tomcat 7 (ubuntu 12.04+, debian 7+)

v0.13.0
-------
### Improvement
- [COOK-2999]: Attributes are "set" and not "default"

### Bug
- [COOK-2421]: Correct name of cookbook in attributes/default.rb
- [COOK-2838]: Fix foodcritic warnings in tomcat cookbook

### New Feature
- [COOK-2422]: Support disabling Tomcat auth
- [COOK-2425]: Add  SSL connector support
- [COOK-2533]: Ability to set loglevel
- [COOK-2736]: Add CATALINA_OPTS for Tomcat start/run options

v0.12.0
-------
- [COOK-1736] - Add AUTHBIND attribute

v0.11.0
-------
- [COOK-1499] - manage tomcat users

v0.10.4
-------
- [COOK-1110] - remove deprecated (by upstream) jpackage recipe
