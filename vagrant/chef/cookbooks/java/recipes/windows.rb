#
# Author:: Kendrick Martin (<kendrick.martin@webtrends.com>)
# Cookbook Name:: java
# Recipe:: windows
#
# Copyright 2008-2012 Webtrends, Inc.
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
#

require 'uri'

Chef::Log.fatal("No download url set for java installer.") unless node['java'] && node['java']['windows'] && node['java']['windows']['url']

pkg_checksum = node['java']['windows']['checksum']
aws_access_key_id = node['java']['windows']['aws_access_key_id']
aws_secret_access_key = node['java']['windows']['aws_secret_access_key']

uri = ::URI.parse(node['java']['windows']['url'])
cache_file_path = File.join(Chef::Config[:file_cache_path], File.basename(::URI.unescape(uri.path)))

if aws_access_key_id && aws_secret_access_key
  include_recipe 'aws::default'  # install right_aws gem for aws_s3_file

  aws_s3_file cache_file_path do
    aws_access_key_id aws_access_key_id
    aws_secret_access_key aws_secret_access_key
    checksum pkg_checksum if pkg_checksum
    bucket node['java']['windows']['bucket']
    remote_path node['java']['windows']['remote_path']
    backup false
    action :create
  end
else
  remote_file cache_file_path do
    checksum pkg_checksum if pkg_checksum
    source node['java']['windows']['url']
    backup false
    action :create
  end
end

if node['java'].attribute?("java_home")
  java_home_win = win_friendly_path(node['java']['java_home'])
  # The EXE installer expects escaped quotes, so we need to double escape
  # them here. The final string looks like :
  # /v"/qn INSTALLDIR=\"C:\Program Files\Java\""
  additional_options = "/v\"/qn INSTALLDIR=\\\"#{java_home_win}\\\"\""

  env "JAVA_HOME" do
    value java_home_win
  end

  # update path
  windows_path "#{java_home_win}\\bin" do
    action :add
  end
end


windows_package node['java']['windows']['package_name'] do
  source cache_file_path
  checksum node['java']['windows']['checksum']
  action :install
  installer_type :custom
  options "/s #{additional_options}"
end
