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

actions :create, :delete

default_action :create

# http://linux.die.net/man/5/yum.conf
attribute :alwaysprompt, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :assumeyes, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :bandwidth, :kind_of => String, :regex => /^\d+/, :default => nil
attribute :bugtracker_url, :kind_of => String, :regex => /.*/, :default => nil
attribute :clean_requirements_on_remove, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :cachedir, :kind_of => String, :regex => /.*/, :default => '/var/cache/yum/$basearch/$releasever'
attribute :color, :kind_of => String, :equal_to => %w(always never), :default => nil
attribute :color_list_available_downgrade, :kind_of => String, :regex => /.*/, :default => nil
attribute :color_list_available_install, :kind_of => String, :regex => /.*/, :default => nil
attribute :color_list_available_reinstall, :kind_of => String, :regex => /.*/, :default => nil
attribute :color_list_available_upgrade, :kind_of => String, :regex => /.*/, :default => nil
attribute :color_list_installed_extra, :kind_of => String, :regex => /.*/, :default => nil
attribute :color_list_installed_newer, :kind_of => String, :regex => /.*/, :default => nil
attribute :color_list_installed_older, :kind_of => String, :regex => /.*/, :default => nil
attribute :color_list_installed_reinstall, :kind_of => String, :regex => /.*/, :default => nil
attribute :color_search_match, :kind_of => String, :regex => /.*/, :default => nil
attribute :color_update_installed, :kind_of => String, :regex => /.*/, :default => nil
attribute :color_update_local, :kind_of => String, :regex => /.*/, :default => nil
attribute :color_update_remote, :kind_of => String, :regex => /.*/, :default => nil
attribute :commands, :kind_of => String, :regex => /.*/, :default => nil
attribute :debuglevel, :kind_of => String, :regex => /^\d+$/, :default => '2'
attribute :diskspacecheck, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :distroverpkg, :kind_of => String, :regex => /.*/, :default => nil
attribute :enable_group_conditionals, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :errorlevel, :kind_of => String, :regex => /^\d+$/, :default => nil
attribute :exactarch, :kind_of => [TrueClass, FalseClass], :default => true
attribute :exclude, :kind_of => String, :regex => /.*/, :default => nil
attribute :gpgcheck, :kind_of => [TrueClass, FalseClass], :default => true
attribute :group_package_types, :kind_of => String, :regex => /.*/, :default => nil
attribute :groupremove_leaf_only, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :history_list_view, :kind_of => String, :equal_to => %w(users commands single-user-commands), :default => nil
attribute :history_record, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :history_record_packages, :kind_of => String, :regex => /.*/, :default => nil
attribute :http_caching, :kind_of => String, :equal_to => %w(packages all none), :default => nil
attribute :installonly_limit, :kind_of => String, :regex => [/^\d+/, /keep/], :default => '3'
attribute :installonlypkgs, :kind_of => String, :regex => /.*/, :default => nil
attribute :installroot, :kind_of => String, :regex => /.*/, :default => nil
attribute :keepalive, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :keepcache, :kind_of => [TrueClass, FalseClass], :default => false
attribute :kernelpkgnames, :kind_of => String, :regex => /.*/, :default => nil
attribute :localpkg_gpgcheck, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :logfile, :kind_of => String, :regex => /.*/, :default => '/var/log/yum.log'
attribute :max_retries, :kind_of => String, :regex => /^\d+$/, :default => nil
attribute :mdpolicy, :kind_of => String, :equal_to => %w(instant group:primary group:small group:main group:all), :default => nil
attribute :metadata_expire, :kind_of => String, :regex => [/^\d+$/, /^\d+[mhd]$/, /never/], :default => nil
attribute :mirrorlist_expire, :kind_of => String, :regex => /^\d+$/, :default => nil
attribute :multilib_policy, :kind_of => String, :equal_to => %w(all best), :default => nil
attribute :obsoletes, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :overwrite_groups, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :password, :kind_of => String, :regex => /.*/, :default => nil
attribute :path, :kind_of => String, :regex => /.*/, :name_attribute => true
attribute :persistdir, :kind_of => String, :regex => /.*/, :default => nil
attribute :pluginconfpath, :kind_of => String, :regex => /.*/, :default => nil
attribute :pluginpath, :kind_of => String, :regex => /.*/, :default => nil
attribute :plugins, :kind_of => [TrueClass, FalseClass], :default => true
attribute :protected_multilib, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :protected_packages, :kind_of => String, :regex => /.*/, :default => nil
attribute :proxy, :kind_of => String, :regex => /.*/, :default => nil
attribute :proxy_password, :kind_of => String, :regex => /.*/, :default => nil
attribute :proxy_username, :kind_of => String, :regex => /.*/, :default => nil
attribute :recent, :kind_of => String, :regex => /^\d+$/, :default => nil
attribute :releasever, :kind_of => String, :regex => /.*/, :default => nil
attribute :repo_gpgcheck, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :reset_nice, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :rpmverbosity, :kind_of => String, :equal_to => %w(info critical emergency error warn debug), :default => nil
attribute :showdupesfromrepos, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :skip_broken, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :ssl_check_cert_permissions, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :sslcacert, :kind_of => String, :regex => /.*/, :default => nil
attribute :sslclientcert, :kind_of => String, :regex => /.*/, :default => nil
attribute :sslclientkey, :kind_of => String, :regex => /.*/, :default => nil
attribute :sslverify, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :syslog_device, :kind_of => String, :regex => /.*/, :default => nil
attribute :syslog_facility, :kind_of => String, :regex => /.*/, :default => nil
attribute :syslog_ident, :kind_of => String, :regex => /.*/, :default => nil
attribute :throttle, :kind_of => String, :regex => [/\d+k/, /\d+M/, /\d+G/], :default => nil
attribute :timeout, :kind_of => String, :regex => /^\d+$/, :default => nil
attribute :tolerant, :kind_of => [TrueClass, FalseClass], :default => nil
attribute :tsflags, :kind_of => String, :regex => /.*/, :default => nil
attribute :username, :kind_of => String, :regex => /.*/, :default => nil

attribute :options, :kind_of => Hash
