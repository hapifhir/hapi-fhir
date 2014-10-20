#
# Author:: Bryan W. Berry (<bryan.berry@gmail.com>)
# Cookbook Name:: java
# Recipe:: oracle
#
# Copyright 2011, Bryan w. Berry
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
arch = node['java']['arch']

case node['java']['jdk_version'].to_s
when "6"
  tarball_url = node['java']['jdk']['6'][arch]['url']
  tarball_checksum = node['java']['jdk']['6'][arch]['checksum']
  bin_cmds = node['java']['jdk']['6']['bin_cmds']
when "7"
  tarball_url = node['java']['jdk']['7'][arch]['url']
  tarball_checksum = node['java']['jdk']['7'][arch]['checksum']
  bin_cmds = node['java']['jdk']['7']['bin_cmds']
when "8"
  tarball_url = node['java']['jdk']['8'][arch]['url']
  tarball_checksum = node['java']['jdk']['8'][arch]['checksum']
  bin_cmds = node['java']['jdk']['8']['bin_cmds']
end

if tarball_url =~ /example.com/
  Chef::Application.fatal!("You must change the download link to your private repository. You can no longer download java directly from http://download.oracle.com without a web broswer")
end

include_recipe "java::set_java_home"

java_ark "jdk" do
  url tarball_url
  default node['java']['set_default']
  checksum tarball_checksum
  app_home java_home
  bin_cmds bin_cmds
  alternatives_priority node['java']['alternatives_priority']
  retries node['java']['ark_retries']
  retry_delay node['java']['ark_retry_delay']
  action :install
end

if node['java']['set_default'] and platform_family?('debian')
  include_recipe 'java::default_java_symlink'
end
