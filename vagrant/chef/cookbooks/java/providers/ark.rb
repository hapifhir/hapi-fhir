#
# Author:: Bryan W. Berry (<bryan.berry@gmail.com>)
# Cookbook Name:: java
# Provider:: ark
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

require 'chef/mixin/shell_out'
include Chef::Mixin::ShellOut

def whyrun_supported?
  true
end

def parse_app_dir_name url
  file_name = url.split('/')[-1]
  # funky logic to parse oracle's non-standard naming convention
  # for jdk1.6
  if file_name =~ /^(jre|jdk|server-jre).*$/
    major_num = file_name.scan(/\d/)[0]
    update_token = file_name.scan(/u(\d+)/)[0]
    update_num = update_token ? update_token[0] : "0"
    # pad a single digit number with a zero
    if update_num.length < 2
      update_num = "0" + update_num
    end
    package_name = (file_name =~ /^server-jre.*$/) ? "jdk" : file_name.scan(/[a-z]+/)[0]
    if update_num == "00"
      app_dir_name = "#{package_name}1.#{major_num}.0"
    else
      app_dir_name = "#{package_name}1.#{major_num}.0_#{update_num}"
    end
  else
    app_dir_name = file_name.split(/(.tgz|.tar.gz|.zip)/)[0]
    app_dir_name = app_dir_name.split("-bin")[0]
  end
  [app_dir_name, file_name]
end

def oracle_downloaded?(download_path, new_resource)
  if ::File.exists? download_path
    require 'digest'
    if new_resource.checksum =~ /^[0-9a-f]{32}$/
      downloaded_md5 =  Digest::MD5.file(download_path).hexdigest
      downloaded_md5 == new_resource.checksum
    else
      downloaded_sha =  Digest::SHA256.file(download_path).hexdigest
      downloaded_sha == new_resource.checksum
    end
  else
    return false
  end
end

def download_direct_from_oracle(tarball_name, new_resource)
  download_path = "#{Chef::Config[:file_cache_path]}/#{tarball_name}"
  cookie = "oraclelicense=accept-securebackup-cookie"
  if node['java']['oracle']['accept_oracle_download_terms']
    # install the curl package
    p = package "curl" do
      action :nothing
    end
    # no converge_by block since the package provider will take care of this run_action
    p.run_action(:install)
    description = "download oracle tarball straight from the server"
    converge_by(description) do
       Chef::Log.debug "downloading oracle tarball straight from the source"
       cmd = shell_out!(
                                  %Q[ curl --create-dirs -L --retry #{new_resource.retries} --retry-delay #{new_resource.retry_delay} --cookie "#{cookie}" #{new_resource.url} -o #{download_path} ]
                               )
    end
  else
    Chef::Application.fatal!("You must set the attribute node['java']['oracle']['accept_oracle_download_terms'] to true if you want to download directly from the oracle site!")
  end
end

action :install do
  app_dir_name, tarball_name = parse_app_dir_name(new_resource.url)
  app_root = new_resource.app_home.split('/')[0..-2].join('/')
  app_dir = app_root + '/' + app_dir_name
  if new_resource.group
    app_group = new_resource.group
  else
    app_group = new_resource.owner
  end

  unless new_resource.default
    Chef::Log.debug("processing alternate jdk")
    app_dir = app_dir  + "_alt"
    app_home = new_resource.app_home + "_alt"
  else
    app_home = new_resource.app_home
  end

  unless ::File.exists?(app_dir)
    Chef::Log.info "Adding #{new_resource.name} to #{app_dir}"
    require 'fileutils'

    unless ::File.exists?(app_root)
      description = "create dir #{app_root} and change owner to #{new_resource.owner}:#{app_group}"
      converge_by(description) do
          FileUtils.mkdir app_root, :mode => new_resource.app_home_mode
          FileUtils.chown new_resource.owner, app_group, app_root
      end
    end

    if new_resource.url =~ /^http:\/\/download.oracle.com.*$/
      download_path = "#{Chef::Config[:file_cache_path]}/#{tarball_name}"
      if  oracle_downloaded?(download_path, new_resource)
        Chef::Log.debug("oracle tarball already downloaded, not downloading again")
      else
        download_direct_from_oracle tarball_name, new_resource
      end
    else
      Chef::Log.debug("downloading tarball from an unofficial repository")
      r = remote_file "#{Chef::Config[:file_cache_path]}/#{tarball_name}" do
        source new_resource.url
        checksum new_resource.checksum
        retries new_resource.retries
        retry_delay new_resource.retry_delay
        mode 0755
        action :nothing
      end
      #no converge by on run_action remote_file takes care of it.
      r.run_action(:create_if_missing)
    end

    description = "extract compressed data into Chef file cache path and
                    move extracted data to #{app_dir}"
    converge_by(description) do
       case tarball_name
       when /^.*\.bin/
         cmd = shell_out(
                                  %Q[ cd "#{Chef::Config[:file_cache_path]}";
                                      bash ./#{tarball_name} -noregister
                                    ] )
         unless cmd.exitstatus == 0
           Chef::Application.fatal!("Failed to extract file #{tarball_name}!")
         end
       when /^.*\.zip/
         cmd = shell_out(
                            %Q[ unzip "#{Chef::Config[:file_cache_path]}/#{tarball_name}" -d "#{Chef::Config[:file_cache_path]}" ]
                                  )
         unless cmd.exitstatus == 0
           Chef::Application.fatal!("Failed to extract file #{tarball_name}!")
         end
       when /^.*\.(tar.gz|tgz)/
         cmd = shell_out(
                            %Q[ tar xvzf "#{Chef::Config[:file_cache_path]}/#{tarball_name}" -C "#{Chef::Config[:file_cache_path]}" --no-same-owner]
                                  )
         unless cmd.exitstatus == 0
           Chef::Application.fatal!("Failed to extract file #{tarball_name}!")
         end
       end

       cmd = shell_out(
                          %Q[ mv "#{Chef::Config[:file_cache_path]}/#{app_dir_name}" "#{app_dir}" ]
                                )
       unless cmd.exitstatus == 0
           Chef::Application.fatal!(%Q[ Command \' mv "#{Chef::Config[:file_cache_path]}/#{app_dir_name}" "#{app_dir}" \' failed ])
         end

       # change ownership of extracted files
       FileUtils.chown_R new_resource.owner, app_group, app_root
     end
     new_resource.updated_by_last_action(true)
  end

  #set up .jinfo file for update-java-alternatives
  java_name =  app_home.split('/')[-1]
  jinfo_file = "#{app_root}/.#{java_name}.jinfo"
  if platform_family?("debian") && !::File.exists?(jinfo_file)
    description = "Add #{jinfo_file} for debian"
    converge_by(description) do
      Chef::Log.debug "Adding #{jinfo_file} for debian"
      template jinfo_file do
        cookbook "java"
        source "oracle.jinfo.erb"
        variables(
          :priority => new_resource.alternatives_priority,
          :bin_cmds => new_resource.bin_cmds,
          :name => java_name,
          :app_dir => app_home
        )
        action :create
      end
    end
    new_resource.updated_by_last_action(true)
  end

  #link app_home to app_dir
  Chef::Log.debug "app_home is #{app_home} and app_dir is #{app_dir}"
  current_link = ::File.symlink?(app_home) ? ::File.readlink(app_home) : nil
  if current_link != app_dir
    description = "Symlink #{app_dir} to #{app_home}"
    converge_by(description) do
       Chef::Log.debug "Symlinking #{app_dir} to #{app_home}"
       FileUtils.rm_f app_home
       FileUtils.ln_sf app_dir, app_home
    end
  end

  #update-alternatives
  java_alternatives 'set-java-alternatives' do
    java_location app_home
    bin_cmds new_resource.bin_cmds
    priority new_resource.alternatives_priority
    default new_resource.default
    action :set
  end
end

action :remove do
  app_dir_name, tarball_name = parse_app_dir_name(new_resource.url)
  app_root = new_resource.app_home.split('/')[0..-2].join('/')
  app_dir = app_root + '/' + app_dir_name

  unless new_resource.default
    Chef::Log.debug("processing alternate jdk")
    app_dir = app_dir + "_alt"
    app_home = new_resource.app_home + "_alt"
  else
    app_home = new_resource.app_home
  end

  if ::File.exists?(app_dir)
    java_alternatives 'unset-java-alternatives' do
      java_location app_home
      bin_cmds new_resource.bin_cmds
      action :unset
    end
    description = "remove #{new_resource.name} at #{app_dir}"
    converge_by(description) do
       Chef::Log.info "Removing #{new_resource.name} at #{app_dir}"
       FileUtils.rm_rf app_dir
    end
    new_resource.updated_by_last_action(true)
  end
end
