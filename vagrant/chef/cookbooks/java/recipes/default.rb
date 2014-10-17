#
# Author:: Seth Chisamore (<schisamo@opscode.com>)
# Cookbook Name:: java
# Recipe:: default
#
# Copyright 2008-2011, Opscode, Inc.
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

if node['java']['install_flavor'] != 'windows'
  if node['java']['jdk_version'].to_i == 8 and node['java']['install_flavor'] != 'oracle'
    Chef::Application.fatal!("JDK 8 is currently only provided with the Oracle JDK")
  end
end

include_recipe "java::set_attributes_from_version"
include_recipe "java::#{node['java']['install_flavor']}"
