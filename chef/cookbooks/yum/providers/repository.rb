#
# Cookbook Name:: yum
# Provider:: repository
#
# Author:: Sean OMeara <someara@getchef.com>
# Copyright 2013, Chef
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

# In Chef 11 and above, calling the use_inline_resources method will
# make Chef create a new "run_context". When an action is called, any
# nested resources are compiled and converged in isolation from the
# recipe that calls it.

# Allow for Chef 10 support
use_inline_resources if defined?(use_inline_resources)

def whyrun_supported?
  true
end

action :create  do
  # Hack around the lack of "use_inline_resources" before Chef 11 by
  # uniquely naming the execute[yum-makecache] resources. Set the
  # notifies timing to :immediately for the same reasons. Remove both
  # of these when dropping Chef 10 support.

  template "/etc/yum.repos.d/#{new_resource.repositoryid}.repo" do
    if new_resource.source.nil?
      source 'repo.erb'
      cookbook 'yum'
    else
      source new_resource.source
    end
    mode new_resource.mode
    variables(:config => new_resource)
    if new_resource.make_cache
      notifies :run, "execute[yum-makecache-#{new_resource.repositoryid}]", :immediately
      notifies :create, "ruby_block[yum-cache-reload-#{new_resource.repositoryid}]", :immediately
    end
  end

  # get the metadata for this repo only
  execute "yum-makecache-#{new_resource.repositoryid}" do
    command "yum -q makecache --disablerepo=* --enablerepo=#{new_resource.repositoryid}"
    action :nothing
  end

  # reload internal Chef yum cache
  ruby_block "yum-cache-reload-#{new_resource.repositoryid}" do
    block { Chef::Provider::Package::Yum::YumCache.instance.reload }
    action :nothing
  end
end

action :delete do
  file "/etc/yum.repos.d/#{new_resource.repositoryid}.repo" do
    action :delete
    notifies :run, "execute[yum clean #{new_resource.repositoryid}]", :immediately
    notifies :create, "ruby_block[yum-cache-reload-#{new_resource.repositoryid}]", :immediately
  end

  execute "yum clean #{new_resource.repositoryid}" do
    command "yum clean all --disablerepo=* --enablerepo=#{new_resource.repositoryid}"
    only_if "yum repolist | grep -P '^#{new_resource.repositoryid}([ \t]|$)'"
    action :nothing
  end

  ruby_block "yum-cache-reload-#{new_resource.repositoryid}" do
    block { Chef::Provider::Package::Yum::YumCache.instance.reload }
    action :nothing
  end
end

action :makecache do
  execute "yum-makecache-#{new_resource.repositoryid}" do
    command "yum -q makecache --disablerepo=* --enablerepo=#{new_resource.repositoryid}"
    action :run
  end

  ruby_block "yum-cache-reload-#{new_resource.repositoryid}" do
    block { Chef::Provider::Package::Yum::YumCache.instance.reload }
    action :run
  end
end

alias_method :action_add, :action_create
alias_method :action_remove, :action_delete
