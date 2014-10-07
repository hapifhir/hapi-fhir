# Author:: Joshua Timberman (<joshua@opscode.com>)
# Cookbook Name:: java
# Recipe:: set_java_home
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

ruby_block  "set-env-java-home" do
  block do
    ENV["JAVA_HOME"] = node['java']['java_home']
  end
  not_if { ENV["JAVA_HOME"] == node['java']['java_home'] }
end

directory "/etc/profile.d" do
  mode 00755
end

file "/etc/profile.d/jdk.sh" do
  content "export JAVA_HOME=#{node['java']['java_home']}"
  mode 00755
end

if node['java']['set_etc_environment']
  ruby_block "Set JAVA_HOME in /etc/environment" do
    block do
      file = Chef::Util::FileEdit.new("/etc/environment")
      file.insert_line_if_no_match(/^JAVA_HOME=/, "JAVA_HOME=#{node['java']['java_home']}")
      file.search_file_replace_line(/^JAVA_HOME=/, "JAVA_HOME=#{node['java']['java_home']}")
      file.write_file
    end
  end
end
