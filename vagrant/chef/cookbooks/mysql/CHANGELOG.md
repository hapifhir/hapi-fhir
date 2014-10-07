mysql Cookbook CHANGELOG
========================
This file is used to list changes made in each version of the mysql cookbook.

v5.5.3 (2014-09-24)
------------------
- Reverting back to Upstart on Ubuntu 14.04

v5.5.2 (2014-09-8)
------------------
- Reverting commit that broke Debian pass_string

v5.5.1 (2014-09-2)
------------------
- Switching Ubuntu service provider to use SysVinit instead of Upstart

v5.5.0 (2014-08-27)
-------------------
- Adding package version and action parameters to mysql_service resource
- Fixing Debian pass_string

v5.4.4 (2014-08-27)
-------------------
- Changing module namespace to MysqlCookbook

v5.4.3 (2014-08-25)
-------------------
- More refactoring. Moving helper function bits into resource parsed_parameters

v5.4.2 (2014-08-25)
-------------------
- Moving provider local variables into definitions for RHEL provider

v5.4.1 (2014-08-25)
-------------------
- Refactoring resources into the LWRP style with parsed parameters
- Moving provider local variables into definitions

v5.4.0 (2014-08-25)
-------------------
- #212 - support for centos-7 (mysql55 and mysql56)
- Adding (untested) Debian-6 support
- Adding Suse support to metadata.rb
- Adding ability to change MySQL root password
- Added libmysqlclient-devel package to SuSE client provider
- Appeasing AppArmor
- Reducing duplication in client provider

v5.3.6 (2014-06-18)
-------------------
- Fixing pid path location. Updating tests to include real RHEL


v5.3.4 (2014-06-16)
-------------------
- Fixing specs for Amazon Linux server package names


v5.3.2 (2014-06-16)
-------------------
- Fixing Amazon Linux support


v5.3.0 (2014-06-11)
-------------------
- #189 - Fix server_repl_password description
- #191 - Adding support for server55 and server56 on el-6
- #193 - Fix syntax in mysql_service example
- #199 - Adding Suse support


v5.2.12 (2014-05-19)
--------------------
PR #192 - recipes/server.rb should honor parameter node['mysql']['version']


v5.2.10 (2014-05-15)
--------------------
- COOK-4394 - restore freebsd support


v5.2.8 (2014-05-15)
-------------------
- [COOK-4653] - Missing mySQL 5.6 support for Ubuntu 14.04


v5.2.6 (2014-05-07)
-------------------
- [COOK-4625] - Fix password resource parameter consumption on Debian and Ubuntu
- Fix up typos and version numbers in PLATFORMS.md
- Fix up specs from COOK-4613 changes


v5.2.4 (2014-05-02)
-------------------
- [COOK-4613] - Fix permissions on mysql data_dir to allow global access to mysql.sock


v5.2.2 (2014-04-24)
-------------------
- [COOK-4564] - Using positive tests for datadir move


v5.2.0 (2014-04-22)
-------------------
- [COOK-4551] - power grants.sql from resource parameters


v5.1.12 (2014-04-21)
--------------------
- [COOK-4554] - Support for Debian Sid


v5.1.10 (2014-04-21)
--------------------
- [COOK-4565] Support for Ubuntu 14.04
- [COOK-4565] Adding Specs and TK platform
- Removing non-LTS 13.10 specs and TK platform


v5.1.8 (2014-04-12)
-------------------
Adding Ubuntu 13.04 to Platforminfo


v5.1.6 (2014-04-11)
-------------------
- [COOK-4548] - Add template[/etc/mysql/debian.cnf] to Ubuntu provider


v5.1.4 (2014-04-11)
-------------------
- [COOK-4547] - Shellescape server_root_password


v5.1.2 (2014-04-09)
-------------------
- [COOK-4519] - Fix error in run_dir for Ubuntu
- [COOK-4531] - Fix pid and run_dir for Debian


v5.1.0 (2014-04-08)
-------------------
[COOK-4523] - Allow for both :restart and :reload


v5.0.6 (2014-04-07)
-------------------
- [COOK-4519] - Updating specs to reflect pid file change on Ubuntu


v5.0.4 (2014-04-07)
-------------------
- [COOK-4519] - Fix path to pid file on Ubuntu


v5.0.2 (2014-04-01)
-------------------
- Moving server_deprecated into recipes directory


v5.0.0 (2014-03-31)
-------------------
- Rewriting as a library cookbook
- Exposing mysql_service and mysql_client resources
- User now needs to supply configuration
- Moving attribute driven recipe to server-deprecated


v4.1.2 (2014-02-28)
-------------------
- [COOK-4349] - Fix invalid platform check
- [COOK-4184] - Better handling of Ubuntu upstart service
- [COOK-2100] - Changing innodb_log_file_size tunable results in inability to start MySQL


v4.1.1 (2014-02-25)
-------------------
- **[COOK-2966] - Address foodcritic failures'
- **[COOK-4182] - Template parse failure in /etc/init/mysql.conf (data_dir)'
- **[COOK-4198] - Added missing tunable'
- **[COOK-4206] - create root@127.0.0.1, as well as root@localhost'


v4.0.20 (2014-01-18)
--------------------
* [COOK-3931] - MySQL Server Recipe Regression for Non-LTS Ubuntu Versions
* [COOK-3945] - MySQL cookbook fails on Ubuntu 13.04/13.10
* [COOK-3966] - mysql::server recipe can't find a template with debian 7.x
* [COOK-3985] - Missing /etc/mysql/debian.cnf template on mysql::_server_debian.rb recipe (mysql 4.0.4)
* [COOK-3974] - debian.cnf not updated
* [COOK-4001] - Pull request: Fixes for broken mysql::server on Debian
* [COOK-4071] - Mysql cookbook doesn't work on debian 7.2


v4.0.14
-------
Fixing style cops


v4.0.12
-------
### Bug
- **[COOK-4068](https://tickets.opscode.com/browse/COOK-4068)** - rework MySQL Windows recipe

### Improvement
- **[COOK-3801](https://tickets.opscode.com/browse/COOK-3801)** - Add innodb_adaptive_flushing_method and innodb_adaptive_checkpoint


v4.0.10
-------
fixing metadata version error. locking to 3.0


v4.0.8
------
Locking yum dependency to '< 3'


v4.0.6
------
# Bug
- [COOK-3943] Notifying service restart on grants update


v4.0.4
------
[COOK-3952] - Adding 'recursive true' to directory resources


v4.0.2
------
### BUGS
- Adding support for Amazon Linux in attributes/server_rhel.rb
- Fixing bug where unprivileged users cannot connect over a local socket. Adding integration test.
- Fixing bug in mysql_grants_cmd generation


v4.0.0
------
- [COOK-3928] Heavily refactoring for readability. Moving platform implementation into separate recipes
- Moving integration tests from minitest to serverspec, removing "improper" tests
- Moving many attributes into the ['mysql']['server']['whatever'] namespace
- [COOK-3481] - Merged Lucas Welsh's Windows bits and moved into own recipe
- [COOK-3697] - Adding security hardening attributes
- [COOK-3780] - Fixing data_dir on Debian and Ubuntu
- [COOK-3807] - Don't use execute[assign-root-password] on Debian and Ubuntu
- [COOK-3881] - Fixing /etc being owned by mysql user


v3.0.12
-------
### Bug
- **[COOK-3752](https://tickets.opscode.com/browse/COOK-3752)** - mysql service fails to start in mysql::server recipe


v3.0.10
-------
- Fix a failed release attempt for v3.0.8


v3.0.8
------
### Bug
- **[COOK-3749](https://tickets.opscode.com/browse/COOK-3749)** - Fix a regression with Chef 11-specific features


v3.0.6
------
### Bug
- **[COOK-3674](https://tickets.opscode.com/browse/COOK-3674)** - Fix an issue where the MySQL server fails to set the root password correctly when `data_dir` is a non-default value
- **[COOK-3647](https://tickets.opscode.com/browse/COOK-3647)** - Fix README typo (databas => database)
- **[COOK-3477](https://tickets.opscode.com/browse/COOK-3477)** - Fix log-queries-not-using-indexes not working
- **[COOK-3436](https://tickets.opscode.com/browse/COOK-3436)** - Pull percona repo in compilation phase
- **[COOK-3208](https://tickets.opscode.com/browse/COOK-3208)** - Fix README typo (LitenPort => ListenPort)
- **[COOK-3149](https://tickets.opscode.com/browse/COOK-3149)** - Create my.cnf before installing
- **[COOK-2681](https://tickets.opscode.com/browse/COOK-2681)** - Fix log_slow_queries for 5.5+
- **[COOK-2606](https://tickets.opscode.com/browse/COOK-2606)** - Use proper bind address on cloud providers

### Improvement
- **[COOK-3498](https://tickets.opscode.com/browse/COOK-3498)** - Add support for replicate_* variables in my.cnf


v3.0.4
------
### Bug
- **[COOK-3310](https://tickets.opscode.com/browse/COOK-3310)** - Fix missing `GRANT` option
- **[COOK-3233](https://tickets.opscode.com/browse/COOK-3233)** - Fix escaping special characters
- **[COOK-3156](https://tickets.opscode.com/browse/COOK-3156)** - Fix GRANTS file when `remote_root_acl` is specified
- **[COOK-3134](https://tickets.opscode.com/browse/COOK-3134)** - Fix Chef 11 support
- **[COOK-2318](https://tickets.opscode.com/browse/COOK-2318)** - Remove redundant `if` block around `node.mysql.tunable.log_bin`

v3.0.2
------
### Bug
- [COOK-2158]: apt-get update is run twice at compile time
- [COOK-2832]: mysql grants.sql file has errors depending on attrs
- [COOK-2995]: server.rb is missing a platform_family comparison value

### Sub-task
- [COOK-2102]: `innodb_flush_log_at_trx_commit` value is incorrectly set based on CPU count

v3.0.0
------
**Note** This is a backwards incompatible version with previous versions of the cookbook. Tickets that introduce incompatibility are COOK-2615 and COOK-2617.

- [COOK-2478] - Duplicate 'read_only' server attribute in base and tunable
- [COOK-2471] - Add tunable to set slave_compressed_protocol for reduced network traffic
- [COOK-1059] - Update attributes in mysql cookbook to support missing options for my.cnf usable by Percona
- [COOK-2590] - Typo in server recipe to do with conf_dir and confd_dir
- [COOK-2602] - Add `lower_case_table_names` tunable
- [COOK-2430] - Add a tunable to create a network ACL when allowing `remote_root_access`
- [COOK-2619] - mysql: isamchk deprecated
- [COOK-2515] - Better support for SUSE distribution for mysql cookbook
- [COOK-2557] - mysql::percona_repo attributes missing and key server typo
- [COOK-2614] - Duplicate `innodb_file_per_table`
- [COOK-2145] - MySQL cookbook should remove anonymous and password less accounts
- [COOK-2553] - Enable include directory in my.cnf template for any platform
- [COOK-2615] - Rename `key_buffer` to `key_buffer_size`
- [COOK-2626] - Percona repo URL is being constructed incorrectly
- [COOK-2616] - Unneeded attribute thread_cache
- [COOK-2618] - myisam-recover not using attribute value
- [COOK-2617] - open-files is a duplicate of open-files-limit

v2.1.2
------
- [COOK-2172] - Mysql cookbook duplicates `binlog_format` configuration

v2.1.0
------
- [COOK-1669] - Using platform("ubuntu") in default attributes always returns true
- [COOK-1694] - Added additional my.cnf fields and reorganized cookbook to avoid race conditions with mysql startup and sql script execution
- [COOK-1851] - Support server-id and binlog_format settings
- [COOK-1929] - Update msyql server attributes file because setting attributes without specifying a precedence is deprecated
- [COOK-1999] - Add read_only tunable useful for replication slave servers

v2.0.2
------
- [COOK-1967] - mysql: trailing comma in server.rb platform family

v2.0.0
------
**Important note for this release**

Under Chef Solo, you must set the node attributes for the root, debian and repl passwords or the run will completely fail. See COOK-1737 for background on this.

- [COOK-1390] - MySQL service cannot start after reboot
- [COOK-1610] - Set root password outside preseed (blocker for drop-in mysql replacements)
- [COOK-1624] - Mysql cookbook fails to even compile on windows
- [COOK-1669] - Using platform("ubuntu") in default attributes always returns true
- [COOK-1686] - Add mysql service start
- [COOK-1687] - duplicate `innodb_buffer_pool_size` attribute
- [COOK-1704] - mysql cookbook fails spec tests when minitest-handler cookbook enabled
- [COOK-1737] - Fail a chef-solo run when `server_root_password`, `server_debian_password`, and/or `server_repl_password` is not set
- [COOK-1769] - link to database recipe in mysql README goes to old opscode/cookbooks repo instead of opscode-cookbook organization
- [COOK-1963] - use `platform_family`

v1.3.0
------
**Important note for this release**

This version no longer installs Ruby bindings in the client recipe by default. Use the ruby recipe if you'd like the RubyGem. If you'd like packages from your distribution, use them in your application's specific cookbook/recipe, or modify the client packages attribute. This resolves the following tickets:

- COOK-932
- COOK-1009
- COOK-1384

Additionally, this cookbook now has tests (COOK-1439) for use under test-kitchen.

The following issues are also addressed in this release.

- [COOK-1443] - MySQL (>= 5.1.24) does not support `innodb_flush_method` = fdatasync
- [COOK-1175] - Add Mac OS X support
- [COOK-1289] - handle additional tunable attributes
- [COOK-1305] - add auto-increment-increment and auto-increment-offset attributes
- [COOK-1397] - make the port an attribute
- [COOK-1439] - Add MySQL cookbook tests for test-kitchen support
- [COOK-1236] - Move package names into attributes to allow percona to free-ride
- [COOK-934] - remove deprecated mysql/libraries/database.rb, use the database cookbook instead.
- [COOK-1475] - fix restart on config change

v1.2.6
------
- [COOK-1113] - Use an attribute to determine if upstart is used
- [COOK-1121] - Add support for Windows
- [COOK-1140] - Fix conf.d on Debian
- [COOK-1151] - Fix server_ec2 handling /var/lib/mysql bind mount
- [COOK-1321] - Document setting password attributes for solo

v1.2.4
------
- [COOK-992] - fix FATAL nameerror
- [COOK-827] - `mysql:server_ec2` recipe can't mount `data_dir`
- [COOK-945] - FreeBSD support

v1.2.2
------
- [COOK-826] mysql::server recipe doesn't quote password string
- [COOK-834] Add 'scientific' and 'amazon' platforms to mysql cookbook

v1.2.1
------
- [COOK-644] Mysql client cookbook 'package missing' error message is confusing
- [COOK-645] RHEL6/CentOS6 - mysql cookbook contains 'skip-federated' directive which is unsupported on MySQL 5.1

v1.2.0
------
- [COOK-684] remove mysql_database LWRP

v1.0.8
------
- [COOK-633] ensure "cloud" attribute is available

v1.0.7
------
- [COOK-614] expose all mysql tunable settings in config
- [COOK-617] bind to private IP if available

v1.0.6
------
- [COOK-605] install mysql-client package on ubuntu/debian

v1.0.5
------
- [COOK-465] allow optional remote root connections to mysql
- [COOK-455] improve platform version handling
- externalize conf_dir attribute for easier cross platform support
- change datadir attribute to data_dir for consistency

v1.0.4
------
- fix regressions on debian platform
- [COOK-578] wrap root password in quotes
- [COOK-562] expose all tunables in my.cnf
