yum-mysql-community Cookbook
============

The yum-mysql-community cookbook takes over management of the default
repositoryids shipped with epel-release. It allows attribute
manipulation of `mysql-connectors-community`, `mysql56-community`, and
`mysql57-community-dmr`.

Requirements
------------
* Chef 11 or higher
* yum cookbook version 3.0.0 or higher

Attributes
----------
The following attributes are set by default

``` ruby
default['yum']['mysql-connectors-community']['repositoryid'] = 'mysql-connectors-community'
default['yum']['mysql-connectors-community']['description'] = 'MySQL Connectors Community'
default['yum']['mysql-connectors-community']['baseurl'] = 'http://repo.mysql.com/yum/mysql-connectors-community/el/$releasever/$basearch/'
default['yum']['mysql-connectors-community']['gpgkey'] = 'https://raw.githubusercontent.com/rs-services/equinix-public/master/cookbooks/db_mysql/files/centos/mysql_pubkey.asc'
default['yum']['mysql-connectors-community']['failovermethod'] = 'priority'
default['yum']['mysql-connectors-community']['gpgcheck'] = true
default['yum']['mysql-connectors-community']['enabled'] = true
```

``` ruby
default['yum']['mysql56-community']['repositoryid'] = 'mysql56-community'
default['yum']['mysql56-community']['description'] = 'MySQL 5.6 Community Server'
default['yum']['mysql56-community']['baseurl'] = 'http://repo.mysql.com/yum/mysql56-community/el/$releasever/$basearch/'
default['yum']['mysql56-community']['gpgkey'] = 'https://raw.githubusercontent.com/rs-services/equinix-public/master/cookbooks/db_mysql/files/centos/mysql_pubkey.asc'
default['yum']['mysql56-community']['failovermethod'] = 'priority'
default['yum']['mysql56-community']['gpgcheck'] = true
default['yum']['mysql56-community']['enabled'] = true
```

``` ruby
default['yum']['mysql57-community-dmr']['repositoryid'] = 'mysql57-community-dmr'
default['yum']['mysql57-community-dmr']['description'] = 'MySQL 5.7 Community Server Development Milestone Release'
default['yum']['mysql57-community-dmr']['baseurl'] = 'http://repo.mysql.com/yum/mysql56-community/el/$releasever/$basearch/'
default['yum']['mysql57-community-dmr']['gpgkey'] = 'https://raw.githubusercontent.com/rs-services/equinix-public/master/cookbooks/db_mysql/files/centos/mysql_pubkey.asc'
default['yum']['mysql57-community-dmr']['failovermethod'] = 'priority'
default['yum']['mysql57-community-dmr']['gpgcheck'] = true
default['yum']['mysql57-community-dmr']['enabled'] = true
```

Recipes
-------
* mysql55 - Sets up the mysql56-community repository on supported
  platforms

```ruby
  yum_repository 'mysql55-community' do
    mirrorlist 'http://repo.mysql.com/yum/mysql55-community/el/$releasever/$basearch/'
    description ''
    enabled true
    gpgcheck true
  end
```

* mysql56 - Sets up the mysql56-community repository on supported
  platforms

```ruby
  yum_repository 'mysql56-community' do
    mirrorlist 'http://repo.mysql.com/yum/mysql56-community/el/$releasever/$basearch/'
    description ''
    enabled true
    gpgcheck true
  end
```


* connectors - Sets up the mysql-connectors-community repository on supported
  platforms


Usage Example
-------------
To disable the epel repository through a Role or Environment definition

```
default_attributes(
  :yum => {
    :mysql57-community-dmr => {
      :enabled => {
        false
       }
     }
   }
 )
```

Uncommonly used repositoryids are not managed by default. This is
speeds up integration testing pipelines by avoiding yum-cache builds
that nobody cares about. To enable the epel-testing repository with a
wrapper cookbook, place the following in a recipe:

```
node.default['yum']['mysql57-community-dmr']['enabled'] = true
node.default['yum']['mysql57-community-dmr']['managed'] = true
include_recipe 'mysql57-community-dmr'
```

More Examples
-------------
Point the mysql56-community repositories at an internally hosted server.

```
node.default['yum']['mysql56-community']['enabled'] = true
node.default['yum']['mysql56-community']['mirrorlist'] = nil
node.default['yum']['mysql56-community']['baseurl'] = 'https://internal.example.com/mysql/mysql56-community/'
node.default['yum']['mysql56-community']['sslverify'] = false

include_recipe 'mysql56-community'
```

License & Authors
-----------------
- Author:: Sean OMeara (<someara@opscode.com>)

```text
Copyright:: 2011-2014, Chef Software, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
