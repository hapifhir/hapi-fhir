#
# Author:: Bryan W. Berry (<bryan.berry@gmail.com>)
# Cookbook Name:: java
# Recipe:: oracle_i386
#
# Copyright 2010-2011, Opscode, Inc.
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

unless node.recipe?('java::default')
  Chef::Log.warn("Using java::default instead is recommended.")

# Even if this recipe is included by itself, a safety check is nice...
  if node['java']['java_home'].nil? or node['java']['java_home'].empty?
    include_recipe "java::set_attributes_from_version"
  end
end

java_home = node['java']["java_home"]

case node['java']['jdk_version'].to_s
when "6"
  tarball_url = node['java']['jdk']['6']['i586']['url']
  tarball_checksum = node['java']['jdk']['6']['i586']['checksum']
  bin_cmds = node['java']['jdk']['6']['bin_cmds']
when "7"
  tarball_url = node['java']['jdk']['7']['i586']['url']
  tarball_checksum = node['java']['jdk']['7']['i586']['checksum']
  bin_cmds = node['java']['jdk']['7']['bin_cmds']
when "8"
  tarball_url = node['java']['jdk']['8']['i586']['url']
  tarball_checksum = node['java']['jdk']['8']['i586']['checksum']
  bin_cmds = node['java']['jdk']['8']['bin_cmds']
end

include_recipe "java::set_java_home"

yum_package "glibc" do
  arch "i686"
  only_if { platform_family?( "rhel", "fedora" ) }
end

java_ark "jdk-alt" do
  url tarball_url
  default node['java']['set_default']
  checksum tarball_checksum
  app_home java_home
  bin_cmds bin_cmds
  retries node['java']['ark_retries']
  retry_delay node['java']['ark_retries']
  action :install
  default false
end

if node['java']['set_default'] and platform_family?('debian')
  include_recipe 'java::default_java_symlink'
end
