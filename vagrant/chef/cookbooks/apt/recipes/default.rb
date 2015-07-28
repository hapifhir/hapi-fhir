#
# Cookbook Name:: apt
# Recipe:: default
#
# Copyright 2008-2013, Chef Software, Inc.
# Copyright 2009, Bryan McLellan <btm@loftninjas.org>
#
# Licensed under the Apache License, Version 2.0 (the 'License');
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an 'AS IS' BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# On systems where apt is not installed, the resources in this recipe are not
# executed. However, they _must_ still be present in the resource collection
# or other cookbooks which notify these resources will fail on non-apt-enabled
# systems.

Chef::Log.debug 'apt is not installed. Apt-specific resources will not be executed.' unless apt_installed?

first_run_file = File.join(Chef::Config[:file_cache_path], 'apt_compile_time_update_first_run')

file '/var/lib/apt/periodic/update-success-stamp' do
  owner 'root'
  group 'root'
  only_if { apt_installed? }
  action :nothing
end

# If compile_time_update run apt-get update at compile time
if node['apt']['compile_time_update'] && (!::File.exist?('/var/lib/apt/periodic/update-success-stamp') || !::File.exist?(first_run_file))
  e = bash 'apt-get-update at compile time' do
    code <<-EOH
      apt-get update
      touch #{first_run_file}
    EOH
    ignore_failure true
    only_if { apt_installed? }
    action :nothing
    notifies :touch, 'file[/var/lib/apt/periodic/update-success-stamp]', :immediately
  end
  e.run_action(:run)
end

# Updates 'apt-get update' timestamp after each update success
directory '/etc/apt/apt.conf.d' do
  recursive true
end

cookbook_file '/etc/apt/apt.conf.d/15update-stamp' do
  source '15update-stamp'
end

# Run apt-get update to create the stamp file
execute 'apt-get-update' do
  command 'apt-get update'
  ignore_failure true
  only_if { apt_installed? }
  not_if { ::File.exist?('/var/lib/apt/periodic/update-success-stamp') }
  notifies :touch, 'file[/var/lib/apt/periodic/update-success-stamp]', :immediately
end

# For other recipes to call to force an update
execute 'apt-get update' do
  command 'apt-get update'
  ignore_failure true
  only_if { apt_installed? }
  action :nothing
  notifies :touch, 'file[/var/lib/apt/periodic/update-success-stamp]', :immediately
end

# Automatically remove packages that are no longer needed for dependencies
execute 'apt-get autoremove' do
  command 'apt-get -y autoremove'
  only_if { apt_installed? }
  action :nothing
end

# Automatically remove .deb files for packages no longer on your system
execute 'apt-get autoclean' do
  command 'apt-get -y autoclean'
  only_if { apt_installed? }
  action :nothing
end

execute 'apt-get-update-periodic' do
  command 'apt-get update'
  ignore_failure true
  only_if do
    apt_installed? &&
      ::File.exist?('/var/lib/apt/periodic/update-success-stamp') &&
      ::File.mtime('/var/lib/apt/periodic/update-success-stamp') < Time.now - node['apt']['periodic_update_min_delay']
  end
  notifies :touch, 'file[/var/lib/apt/periodic/update-success-stamp]', :immediately
end

%w(/var/cache/local /var/cache/local/preseeding).each do |dirname|
  directory dirname do
    owner 'root'
    group 'root'
    mode 00755
    action :create
    only_if { apt_installed? }
  end
end
