# Cookbook Name:: java
# Recipe:: ibm_tar
#
# Copyright 2013, Opscode, Inc.
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

require 'uri'

source_url = node['java']['ibm']['url']
jdk_uri = ::URI.parse(source_url)
jdk_filename = ::File.basename(jdk_uri.path)

unless valid_ibm_jdk_uri?(source_url)
  raise "You must set the attribute `node['java']['ibm']['url']` to a valid URI"
end

unless jdk_filename =~ /\.(tar.gz|tgz)$/
  raise "The attribute `node['java']['ibm']['url']` must specify a .tar.gz file"
end

remote_file "#{Chef::Config[:file_cache_path]}/#{jdk_filename}" do
  source source_url
  mode 00755
  if node['java']['ibm']['checksum']
    checksum node['java']['ibm']['checksum']
    action :create
  else
    action :create_if_missing
  end
  notifies :create, "directory[create-java-home]", :immediately
  notifies :run, "execute[untar-ibm-java]", :immediately
end

directory "create-java-home" do
  path node['java']['java_home']
  mode 00755
  recursive true
end

java_alternatives 'set-java-alternatives' do
  java_location node['java']['java_home']
  default node['java']['set_default']
  case node['java']['jdk_version'].to_s
  when "6"
    bin_cmds node['java']['ibm']['6']['bin_cmds']
  when "7"
    bin_cmds node['java']['ibm']['7']['bin_cmds']
  end
  action :nothing
end

execute "untar-ibm-java" do
  cwd Chef::Config[:file_cache_path]
  command "tar xzf ./#{jdk_filename} -C #{node['java']['java_home']} --strip 1"
  notifies :set, 'java_alternatives[set-java-alternatives]', :immediately
  creates "#{node['java']['java_home']}/jre/bin/java"
end

include_recipe "java::set_java_home"
