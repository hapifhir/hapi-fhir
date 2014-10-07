# Cookbook Name:: java
# Recipe:: set_attributes_from_version
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

# Calculate variables that depend on jdk_version
# If you need to override this in an attribute file you must use
# force_default or higher precedence.

case node['platform_family']
when "rhel", "fedora"
  case node['java']['install_flavor']
  when "oracle"
    node.default['java']['java_home'] = "/usr/lib/jvm/java"
  when "oracle_rpm"
    node.default['java']['java_home'] = "/usr/java/latest"
  else
    node.default['java']['java_home'] = "/usr/lib/jvm/java-1.#{node['java']['jdk_version']}.0"
  end
  node.default['java']['openjdk_packages'] = ["java-1.#{node['java']['jdk_version']}.0-openjdk", "java-1.#{node['java']['jdk_version']}.0-openjdk-devel"]
when "freebsd"
  node.default['java']['java_home'] = "/usr/local/openjdk#{node['java']['jdk_version']}"
  node.default['java']['openjdk_packages'] = ["openjdk#{node['java']['jdk_version']}"]
when "arch"
  node.default['java']['java_home'] = "/usr/lib/jvm/java-#{node['java']['jdk_version']}-openjdk"
  node.default['java']['openjdk_packages'] = ["openjdk#{node['java']['jdk_version']}"]
when "debian"
  node.default['java']['java_home'] = "/usr/lib/jvm/java-#{node['java']['jdk_version']}-#{node['java']['install_flavor']}"
  # Newer Debian & Ubuntu adds the architecture to the path
  if node['platform'] == 'debian' && Chef::VersionConstraint.new(">= 7.0").include?(node['platform_version']) ||
     node['platform'] == 'ubuntu' && Chef::VersionConstraint.new(">= 12.04").include?(node['platform_version'])
    node.default['java']['java_home'] = "#{node['java']['java_home']}-#{node['kernel']['machine'] == 'x86_64' ? 'amd64' : 'i386'}"
  end
  node.default['java']['openjdk_packages'] = ["openjdk-#{node['java']['jdk_version']}-jdk", "openjdk-#{node['java']['jdk_version']}-jre-headless"]
when "smartos"
  node.default['java']['java_home'] = "/opt/local/java/sun6"
  node.default['java']['openjdk_packages'] = ["sun-jdk#{node['java']['jdk_version']}", "sun-jre#{node['java']['jdk_version']}"]
when "windows"
  # Do nothing otherwise we will fall through to the else and set java_home to an invalid path, causing the installer to popup a dialog
else
  node.default['java']['java_home'] = "/usr/lib/jvm/default-java"
  node.default['java']['openjdk_packages'] = ["openjdk-#{node['java']['jdk_version']}-jdk"]
end
