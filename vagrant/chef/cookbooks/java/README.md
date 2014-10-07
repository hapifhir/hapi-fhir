java
=====

This cookbook installs a Java JDK/JRE. It defaults to installing
OpenJDK, but it can also install Oracle and IBM JDKs.

Usage
-----

Simply include the `java` recipe wherever you would like Java installed, such as a run list (`recipe[java]`) or a cookbook (`include_recipe 'java'`). By default, OpenJDK 6 is installed. The `install_flavor` attribute is used to determine which JDK to install (OpenJDK, Oracle, IBM, or Windows), and `jdk_version` specifies which version to install (currently 6 and 7 are supported for all JDK types, 8 for Oracle only).

### Examples

To install Oracle Java 7 (note that when installing Oracle JDK, `accept_oracle_download_terms` must be set -- see below for details):
```ruby
name "java"
description "Install Oracle Java"
default_attributes(
  "java" => {
    "install_flavor" => "oracle",
    "jdk_version" => "7",
    "oracle" => {
      "accept_oracle_download_terms" => true
    }
  }
)
run_list(
  "recipe[java]"
)
```

To install IBM flavored Java:
```ruby
name "java"
description "Install IBM Java on Ubuntu"
default_attributes(
  "java" => {
    "install_flavor" => "ibm",
    "ibm" => {
      "accept_ibm_download_terms" => true,
      "url" => "http://fileserver.example.com/ibm-java-x86_64-sdk-7.0-4.1.bin",
      "checksum" => "The SHA256 checksum of the bin"
    }
  }
)
run_list(
  "recipe[java]"
)
```

Requirements
-----

Chef 0.10.10+ and Ohai 6.10+ for `platform_family` use.

### Platform

* Debian, Ubuntu
* CentOS, Red Hat, Fedora, Scientific, Amazon, XenServer
* ArchLinux
* FreeBSD
* SmartOS
* Windows

Attributes
-----

See `attributes/default.rb` for default values.

* `node['java']['install_flavor']` - Flavor of JVM you would like
installed (`oracle`, `oracle_rpm`, `openjdk`, `ibm`, `windows`), default `openjdk`
on Linux/Unix platforms, `windows` on Windows platforms.
* `node['java']['jdk_version']` - JDK version to install, defaults to
  `'6'`.
* `node['java']['java_home']` - Default location of the
  "`$JAVA_HOME`".
* `node['java']['set_etc_environment']` - Optionally sets
  JAVA_HOME in `/etc/environment` for  Default `false`.
* `node['java']['openjdk_packages']` - Array of OpenJDK package names
  to install in the `java::openjdk` recipe. This is set based on the
  platform.
* `node['java']['tarball']` - Name of the tarball to retrieve from
your internal repository, default `jdk1.6.0_29_i386.tar.gz`
* `node['java']['tarball_checksum']` - Checksum for the tarball, if
you use a different tarball, you also need to create a new sha256
checksum
* `node['java']['jdk']` - Version and architecture specific attributes
for setting the URL on Oracle's site for the JDK, and the checksum of
the .tar.gz.
* `node['java']['oracle']['accept_oracle_download_terms']` - Indicates
  that you accept Oracle's EULA
* `node['java']['windows']['url']` - The internal location of your
  java install for windows
* `node['java']['windows']['package_name']` - The package name used by
  windows_package to check in the registry to determine if the install
  has already been run
* `node['java']['windows']['checksum']` - The checksum for the package to
  download on Windows machines (default is nil, which does not perform
  checksum validation)
* `node['java']['ibm']['url']` - The URL which to download the IBM
  JDK/SDK. See the `ibm` recipe section below.
* `node['java']['ibm']['accept_ibm_download_terms']` - Indicates that
  you accept IBM's EULA (for `java::ibm`)
* `node['java']['oracle_rpm']['type']` - Type of java RPM (`jre` or `jdk`), default `jdk`
* `node['java']['oracle_rpm']['package_version']` - optional, can be set 
  to pin a version different from the up-to-date one available in the YUM repo,
  it might be needed to also override the node['java']['java_home'] attribute 
  to a value consistent with the defined version
* `node['java']['oracle_rpm']['package_name']` - optional, can be set 
  to define a package name different from the RPM published by Oracle.
* `node['java']['accept_license_agreement']` - Indicates that you accept
  the EULA for openjdk package installation.
* `node['java']['set_default']` - Indicates whether or not you want the
  JDK installed to be default on the system.  Defaults to true.

Recipes
-----

### default

Include the default recipe in a run list or recipe to get `java`.  By default
the `openjdk` flavor of Java is installed, but this can be changed by
using the `install_flavor` attribute. By default on Windows platform
systems, the `install_flavor` is `windows`.

OpenJDK is the default because of licensing changes made upstream by
Oracle. See notes on the `oracle` recipe below.

NOTE: In most cases, including just the default recipe will be sufficient.
It's possible to include the install_type recipes directly, as long as
the necessary attributes (such as java_home) are set.

### set_attributes_from_version

Sets default attributes based on the JDK version. This is included by `default.rb`. This logic must be in
a recipe instead of attributes/default.rb. See [#95](https://github.com/agileorbit-cookbooks/java/pull/95)
for details.

### default_java_symlink

Updates /usr/lib/jvm/default-java to point to JAVA_HOME.

### purge_packages

Purges deprecated Sun Java packages.

### openjdk

This recipe installs the `openjdk` flavor of Java. It also uses the
`alternatives` system on RHEL/Debian families to set the default Java.

On platforms such as SmartOS that require the acceptance of a license
agreement during package installation, set
`node['java']['accept_license_agreement']` to true in order to indicate
that you accept the license.

### oracle

This recipe installs the `oracle` flavor of Java. This recipe does not
use distribution packages as Oracle changed the licensing terms with
JDK 1.6u27 and prohibited the practice for both RHEL and Debian family
platforms.

As of 26 March 2012 you can no longer directly download the JDK from
Oracle's website without using a special cookie. This cookbook uses
that cookie to download the oracle recipe on your behalf, however the
`java::oracle` recipe forces you to set either override the
`node['java']['oracle']['accept_oracle_download_terms']` to true or
set up a private repository accessible by HTTP.

override the `accept_oracle_download_terms` in, e.g., `roles/base.rb`
```ruby
    default_attributes(
      :java => {
         :oracle => {
           "accept_oracle_download_terms" => true
         }
       }
    )
```

For both RHEL and Debian families, this recipe pulls the binary
distribution from the Oracle website, and installs it in the default
`JAVA_HOME` for each distribution. For Debian, this is
`/usr/lib/jvm/default-java`. For RHEl, this is `/usr/lib/jvm/java`.

After putting the binaries in place, the `java::oracle` recipe updates
`/usr/bin/java` to point to the installed JDK using the
`update-alternatives` script. This is all handled in the `java_ark`
LWRP.

### oracle_i386

This recipe installs the 32-bit Java virtual machine without setting
it as the default. This can be useful if you have applications on the
same machine that require different versions of the JVM.

This recipe operates in a similar manner to `java::oracle`.

### oracle_rpm

This recipe installs the Oracle JRE or JDK provided by a custom YUM
repositories.
It also uses the `alternatives` system on RHEL families to set
the default Java.

### windows

Because there is no easy way to pull the java msi off oracle's site,
this recipe requires you to host it internally on your own http repo.

**IMPORTANT NOTE**

If you use the `windows` recipe, you'll need to make sure you've uploaded
the `aws` and `windows` cookbooks. As of version 1.18.0, this cookbook
references them with `suggests` instead of `depends`, as they are only
used by the `windows` recipe.

### ibm

The `java::ibm` recipe is used to install the IBM version of Java.
Note that IBM requires you to create an account *and* log in to
download the binary installer for your platform. You must accept the
license agreement with IBM to use their version of Java. In this
cookbook, you indicate this by setting
`node['java']['ibm']['accept_ibm_download_terms']` to `true`. You must
also host the binary on your own HTTP server to have an automated
installation. The `node['java']['ibm']['url']` attribute must be set
to a valid https/http URL; the URL is checked for validity in the recipe.

At this time the `java::ibm` recipe does not support multiple SDK
installations.

Resources/Providers
-----

### java_ark

This cookbook contains the `java_ark` LWRP. Generally speaking this
LWRP is deprecated in favor of `ark` from the
[ark cookbook](https://github.com/opscode-cookbooks/ark), but it is
still used in this cookbook for handling the Oracle JDK installation.

By default, the extracted directory is extracted to
`app_root/extracted_dir_name` and symlinked to `app_root/default`

#### Actions

- `:install`: extracts the tarball and makes necessary symlinks
- `:remove`: removes the tarball and run update-alternatives for all
  symlinked `bin_cmds`

#### Attribute Parameters

- `url`: path to tarball, .tar.gz, .bin (oracle-specific), and .zip
  currently supported
- `checksum`: SHA256 checksum, not used for security but avoid
  redownloading the archive on each chef-client run
- `app_home`: the default for installations of this type of
  application, for example, `/usr/lib/tomcat/default`. If your
  application is not set to the default, it will be placed at the same
  level in the directory hierarchy but the directory name will be
   `app_root/extracted_directory_name + "_alt"`
- `app_home_mode`: file mode for app_home, is an integer
- `bin_cmds`: array of binary commands that should be symlinked to
  `/usr/bin`, examples are mvn, java, javac, etc. These cmds must be in
  the `bin` subdirectory of the extracted folder. Will be ignored if this
  `java_ark` is not the default
- `owner`: owner of extracted directory, set to "root" by default
- `group`: group of extracted directory, set to `:owner` by default
- `default`: whether this the default installation of this package,
  boolean true or false

#### Examples
```ruby
# install jdk6 from Oracle
java_ark "jdk" do
    url 'http://download.oracle.com/otn-pub/java/jdk/6u29-b11/jdk-6u29-linux-x64.bin'
    checksum  'a8603fa62045ce2164b26f7c04859cd548ffe0e33bfc979d9fa73df42e3b3365'
    app_home '/usr/local/java/default'
    bin_cmds ["java", "javac"]
    action :install
end
```
### java_alternatives

The `java_alternatives` LWRP uses `update-alternatives` command
to set and unset command alternatives for various Java tools
such as java, javac, etc.

#### Actions

- `:set`: set alternatives for Java tools
- `:unset`: unset alternatives for Java tools

#### Attribute Parameters

- `java_location`: Java installation location.
- `bin_cmds`: array of Java tool names to set or unset alternatives on.
- `default`: whether to set the Java tools as system default. Boolean, defaults to `true`.
- `priority`: priority of the alternatives. Integer, defaults to `1061`.

#### Examples
```ruby
# set alternatives for java and javac commands
java_alternatives "set java alternatives" do
    java_location '/usr/local/java'
    bin_cmds ["java", "javac"]
    action :set
end
```

Production Deployment with Oracle Java
-----
Oracle has been known to change the behavior of its download site frequently. It is recommended you store the archives on an artifact server or s3 bucket. You can then override the attributes in a cookbook, role, or environment:

```ruby
default['java']['jdk_version'] = '7'
default['java']['install_flavor'] = 'oracle'
default['java']['jdk']['7']['x86_64']['url'] = 'http://artifactory.example.com/artifacts/jdk-7u65-linux-x64.tar.gz'
default['java']['oracle']['accept_oracle_download_terms'] = true
```

Recommendations for inclusion in community cookbooks
-----

This cookbook is a dependency for many other cookbooks in the Java/Chef sphere. Here are some guidelines for including it into other cookbooks:

### Allow people to not use this cookbook
Many users manage Java on their own or have systems that already have java installed. Give these users an option to skip this cookbook, for example:
```ruby
include_recipe 'java' if node['maven']['install_java']
```

This would allow a users of the maven cookbook to choose if they want the maven cookbook to install java for them or leave that up to the consumer.

Another good example is from the [Jenkins Cookbook Java recipe](https://github.com/opscode-cookbooks/jenkins/commit/ca2a69d982011dc1bec6a6d0ee4da5c1a1599864).

### Pinning to major version of cookbook and Java
This cookbook follows semver. It is recommended to pin at the major version of this cookbook when including it in other cookbooks, eg: `depends 'java', '~> 1.0'`

It is acceptable to set the `node['java']['jdk_version']` to a specific version if required for your software to run, eg software xyz requires Java 8 to run. Refrain from pinning to specific patches of the JDK to allow users to consume security updates.

Development
-----

This cookbook uses
[test-kitchen](https://github.com/opscode/test-kitchen) for
integration tests and
[ChefSpec/RSpec](https://github.com/sethvargo/chefspec) for unit tests.
See [TESTING.md](https://github.com/agileorbit-cookbooks/java/blob/master/TESTING.md) for testing instructions.

At this time due to licensing concerns, the IBM recipe is not set up
in test kitchen. If you would like to test this locally, copy
.kitchen.yml to .kitchen.local.yml and add the following suite:
```yml
suites:
- name: ibm
  run_list: ["recipe[java]"]
  attributes:
    java:
      install_flavor: "ibm"
      ibm:
        accept_ibm_download_terms: true
        url: "http://jenkins/ibm-java-x86_64-sdk-7.0-4.1.bin"
        checksum: the-sha256-checksum
```

Log into the IBM DeveloperWorks site to download a copy of the IBM
Java SDK you wish to use/test, host it on an internal HTTP server, and
calculate the SHA256 checksum to use in the suite.

License and Author
-----

* Author: Seth Chisamore (<schisamo@opscode.com>)
* Author: Bryan W. Berry (<bryan.berry@gmail.com>)
* Author: Joshua Timberman (<joshua@opscode.com>)
* Author: Eric Helgeson (<erichelgeson@gmail.com>)

Copyright: 2014, Agile Orbit, LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
