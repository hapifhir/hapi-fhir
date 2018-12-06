#
# Author:: Seth Chisamore (<schisamo@opscode.com>)
# Cookbook Name:: java
# Attributes:: default
#
# Copyright 2010, Opscode, Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# default jdk attributes
default['java']['jdk_version'] = '6'
default['java']['arch'] = if node['kernel']['machine'].match?(/x86_64/)
                             "x86_64"
                          else
                             "i586"
                          end
#default['java']['arch'] = kernel['machine'] =~ /x86_64/ ?
default['java']['openjdk_packages'] = []
default['java']['openjdk_version'] = nil
default['java']['accept_license_agreement'] = false
default['java']['set_default'] = true
default['java']['alternatives_priority'] = 1062
default['java']['set_etc_environment'] = false

# the following retry parameters apply when downloading oracle java
default['java']['ark_retries'] = 0
default['java']['ark_retry_delay'] = 2

case node['platform_family']
when "windows"
  default['java']['install_flavor'] = "windows"
  default['java']['windows']['url'] = nil
  default['java']['windows']['checksum'] = nil
  default['java']['windows']['package_name'] = "Java(TM) SE Development Kit 7 (64-bit)"
else
  default['java']['install_flavor'] = "openjdk"
end

case node['java']['install_flavor']
when 'ibm', 'ibm_tar'
  default['java']['ibm']['url'] = nil
  default['java']['ibm']['checksum'] = nil
  default['java']['ibm']['accept_ibm_download_terms'] = false
  default['java']['java_home'] = "/opt/ibm/java"

  default['java']['ibm']['6']['bin_cmds'] = [ "appletviewer", "apt", "ControlPanel", "extcheck", "HtmlConverter", "idlj", "jar", "jarsigner",
                                              "java", "javac", "javadoc", "javah", "javap", "javaws", "jconsole", "jcontrol", "jdb", "jdmpview",
                                              "jrunscript", "keytool", "native2ascii", "policytool", "rmic", "rmid", "rmiregistry",
                                              "schemagen", "serialver", "tnameserv", "wsgen", "wsimport", "xjc" ]

  default['java']['ibm']['7']['bin_cmds'] = node['java']['ibm']['6']['bin_cmds'] + [ "pack200", "unpack200" ]
when 'oracle_rpm'
  # type of java RPM : jdk or jre
  default['java']['oracle_rpm']['type'] = 'jdk'

  # optional, can be overriden to pin to a version different
  # from the up-to-date.
  default['java']['oracle_rpm']['package_version'] = nil

  # optional, some distros re-package the official Oracle's RPM
  # with a different name
  default['java']['oracle_rpm']['package_name'] = nil

  # set the JAVA_HOME path, it may be overriden
  # when a package version is provided.
  default['java']['java_home'] = "/usr/java/latest"
end

# if you change this to true, you can download directly from Oracle
default['java']['oracle']['accept_oracle_download_terms'] = false

# direct download paths for oracle, you have been warned!

# jdk6 attributes
default['java']['jdk']['6']['bin_cmds'] = [ "appletviewer", "apt", "ControlPanel", "extcheck", "HtmlConverter", "idlj", "jar", "jarsigner",
                                            "java", "javac", "javadoc", "javah", "javap", "javaws", "jconsole", "jcontrol", "jdb", "jhat",
                                            "jinfo", "jmap", "jps", "jrunscript", "jsadebugd", "jstack", "jstat", "jstatd", "jvisualvm",
                                            "keytool", "native2ascii", "orbd", "pack200", "policytool", "rmic", "rmid", "rmiregistry",
                                            "schemagen", "serialver", "servertool", "tnameserv", "unpack200", "wsgen", "wsimport", "xjc"]

# x86_64
default['java']['jdk']['6']['x86_64']['url'] = 'http://download.oracle.com/otn-pub/java/jdk/6u45-b06/jdk-6u45-linux-x64.bin'
default['java']['jdk']['6']['x86_64']['checksum'] = '6b493aeab16c940cae9e3d07ad2a5c5684fb49cf06c5d44c400c7993db0d12e8'

# i586
default['java']['jdk']['6']['i586']['url'] = 'http://download.oracle.com/otn-pub/java/jdk/6u45-b06/jdk-6u45-linux-i586.bin'
default['java']['jdk']['6']['i586']['checksum'] = 'd53b5a2518d80e1d95565f0adda54eee229dc5f4a1d1a3c2f7bf5045b168a357'

# jdk7 attributes

default['java']['jdk']['7']['bin_cmds'] = [ "appletviewer", "apt", "ControlPanel", "extcheck", "idlj", "jar", "jarsigner", "java", "javac",
                                            "javadoc", "javafxpackager", "javah", "javap", "javaws", "jcmd", "jconsole", "jcontrol", "jdb",
                                            "jhat", "jinfo", "jmap", "jps", "jrunscript", "jsadebugd", "jstack", "jstat", "jstatd", "jvisualvm",
                                            "keytool", "native2ascii", "orbd", "pack200", "policytool", "rmic", "rmid", "rmiregistry",
                                            "schemagen", "serialver", "servertool", "tnameserv", "unpack200", "wsgen", "wsimport", "xjc"]

# Oracle doesn't seem to publish SHA256 checksums for Java releases, so we use MD5 instead.
# Official checksums for the latest release can be found at http://www.oracle.com/technetwork/java/javase/downloads/java-se-binaries-checksum-1956892.html

# x86_64
default['java']['jdk']['7']['x86_64']['url'] = 'http://download.oracle.com/otn-pub/java/jdk/7u67-b01/jdk-7u67-linux-x64.tar.gz'
default['java']['jdk']['7']['x86_64']['checksum'] = '81e3e2df33e13781e5fac5756ed90e67'

# i586
default['java']['jdk']['7']['i586']['url'] = 'http://download.oracle.com/otn-pub/java/jdk/7u67-b01/jdk-7u67-linux-i586.tar.gz'
default['java']['jdk']['7']['i586']['checksum'] = '715b0e8ba2a06bded75f6a92427e2701'

# jdk8 attributes

default['java']['jdk']['8']['bin_cmds'] = [ "appletviewer", "apt", "ControlPanel", "extcheck", "idlj", "jar", "jarsigner", "java", "javac",
                                            "javadoc", "javafxpackager", "javah", "javap", "javaws", "jcmd", "jconsole", "jcontrol", "jdb",
                                            "jdeps", "jhat", "jinfo", "jjs", "jmap", "jmc", "jps", "jrunscript", "jsadebugd", "jstack",
                                            "jstat", "jstatd", "jvisualvm", "keytool", "native2ascii", "orbd", "pack200", "policytool",
                                            "rmic", "rmid", "rmiregistry", "schemagen", "serialver", "servertool", "tnameserv",
                                            "unpack200", "wsgen", "wsimport", "xjc"]

# Oracle doesn't seem to publish SHA256 checksums for Java releases, so we use MD5 instead.
# Official checksums for the latest release can be found at http://www.oracle.com/technetwork/java/javase/downloads/javase8-binaries-checksum-2133161.html

# x86_64
default['java']['jdk']['8']['x86_64']['url'] = 'http://download.oracle.com/otn-pub/java/jdk/8u20-b26/jdk-8u20-linux-x64.tar.gz'
default['java']['jdk']['8']['x86_64']['checksum'] = 'ec7f89dc3697b402e2c851d0488f6299'

# i586
default['java']['jdk']['8']['i586']['url'] = 'http://download.oracle.com/otn-pub/java/jdk/8u20-b26/jdk-8u20-linux-i586.tar.gz'
default['java']['jdk']['8']['i586']['checksum'] = '5dafdef064e18468f21c65051a6918d7'
