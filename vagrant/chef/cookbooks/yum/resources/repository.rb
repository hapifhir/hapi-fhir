#
# Cookbook Name:: yum
# Resource:: repository
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

actions :create, :delete, :add, :remove, :makecache

default_action :create

# http://linux.die.net/man/5/yum.conf
attribute :baseurl, :kind_of => String, :regex => /.*/, :default => nil
attribute :cost, :kind_of => String, :regex => /^\d+$/, :default => nil
attribute :description, :kind_of => String, :regex => /.*/, :default => 'Ye Ole Rpm Repo'
attribute :enabled, :kind_of => [TrueClass, FalseClass], :default => true
attribute :enablegroups, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :exclude, :kind_of => String, :regex => /.*/, :default => nil
attribute :failovermethod, :kind_of => String, :equal_to => %w(priority roundrobin), :default => nil
attribute :fastestmirror_enabled, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :gpgcheck, :kind_of => [TrueClass, FalseClass], :default => true
attribute :gpgkey, :kind_of => [String, Array], :regex => /.*/, :default => nil
attribute :http_caching, :kind_of => String, :equal_to => %w(packages all none), :default => nil
attribute :include_config, :kind_of => String, :regex => /.*/, :default => nil
attribute :includepkgs, :kind_of => String, :regex => /.*/, :default => nil
attribute :keepalive, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :make_cache, :kind_of => [TrueClass, FalseClass], :default => true
attribute :max_retries, :kind_of => String, :regex => /.*/, :default => nil
attribute :metadata_expire, :kind_of => String, :regex => [/^\d+$/, /^\d+[mhd]$/, /never/], :default => nil
attribute :mirrorexpire, :kind_of => String, :regex => /.*/, :default => nil
attribute :mirrorlist, :kind_of => String, :regex => /.*/, :default => nil
attribute :mirror_expire, :kind_of => String, :regex => [/^\d+$/, /^\d+[mhd]$/], :default => nil
attribute :mirrorlist_expire, :kind_of => String, :regex => [/^\d+$/, /^\d+[mhd]$/], :default => nil
attribute :mode, :default => '0644'
attribute :priority, :kind_of => String, :regex => /^(\d?[0-9]|[0-9][0-9])$/, :default => nil
attribute :proxy, :kind_of => String, :regex => /.*/, :default => nil
attribute :proxy_username, :kind_of => String, :regex => /.*/, :default => nil
attribute :proxy_password, :kind_of => String, :regex => /.*/, :default => nil
attribute :username, :kind_of => String, :regex => /.*/, :default => nil
attribute :password, :kind_of => String, :regex => /.*/, :default => nil
attribute :report_instanceid, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :repositoryid, :kind_of => String, :regex => /.*/, :name_attribute => true
attribute :skip_if_unavailable, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :source, :kind_of => String, :regex => /.*/, :default => nil
attribute :sslcacert, :kind_of => String, :regex => /.*/, :default => nil
attribute :sslclientcert, :kind_of => String, :regex => /.*/, :default => nil
attribute :sslclientkey, :kind_of => String, :regex => /.*/, :default => nil
attribute :sslverify, :kind_of => [TrueClass, FalseClass], :default => true
attribute :timeout, :kind_of => String, :regex => /^\d+$/, :default => nil

attribute :options, :kind_of => Hash

alias_method :url, :baseurl
alias_method :keyurl, :gpgkey
