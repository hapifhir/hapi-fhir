#
# Cookbook Name:: apt
# Provider:: repository
#
# Copyright 2010-2011, Chef Software, Inc.
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

use_inline_resources if defined?(use_inline_resources)

def whyrun_supported?
  true
end

# install apt key from keyserver
def install_key_from_keyserver(key, keyserver)
  execute "install-key #{key}" do
    if !node['apt']['key_proxy'].empty?
      command "apt-key adv --keyserver-options http-proxy=#{node['apt']['key_proxy']} --keyserver hkp://#{keyserver}:80 --recv #{key}"
    else
      command "apt-key adv --keyserver #{keyserver} --recv #{key}"
    end
    action :run
    not_if do
      extract_fingerprints_from_cmd('apt-key finger').any? do |fingerprint|
        fingerprint.end_with?(key.upcase)
      end
    end
  end
end

# run command and extract gpg ids
def extract_fingerprints_from_cmd(cmd)
  so = Mixlib::ShellOut.new(cmd, env: { 'LANG' => 'en_US' })
  so.run_command
  so.stdout.split(/\n/).map do |t|
    if z = t.match(/^ +Key fingerprint = ([0-9A-F ]+)/)
      z[1].split.join
    end
  end.compact
end

# install apt key from URI
def install_key_from_uri(uri)
  key_name = uri.split(/\//).last
  cached_keyfile = "#{Chef::Config[:file_cache_path]}/#{key_name}"
  if new_resource.key =~ /http/
    remote_file cached_keyfile do
      source new_resource.key
      mode 00644
      action :create
    end
  else
    cookbook_file cached_keyfile do
      source new_resource.key
      cookbook new_resource.cookbook
      mode 00644
      action :create
    end
  end

  execute "install-key #{key_name}" do
    command "apt-key add #{cached_keyfile}"
    action :run
    not_if do
      installed_keys = extract_fingerprints_from_cmd('apt-key finger')
      proposed_keys = extract_fingerprints_from_cmd("gpg --with-fingerprint #{cached_keyfile}")
      (installed_keys & proposed_keys).sort == proposed_keys.sort
    end
  end
end

# build repo file contents
def build_repo(uri, distribution, components, trusted, arch, add_deb_src)
  components = components.join(' ') if components.respond_to?(:join)
  repo_options = []
  repo_options << "arch=#{arch}" if arch
  repo_options << 'trusted=yes' if trusted
  repo_options = '[' + repo_options.join(' ') + ']' unless repo_options.empty?
  repo_info = "#{uri} #{distribution} #{components}\n"
  repo_info = "#{repo_options} #{repo_info}" unless repo_options.empty?
  repo =  "deb     #{repo_info}"
  repo << "deb-src #{repo_info}" if add_deb_src
  repo
end

def get_ppa_key(ppa_owner, ppa_repo)
  # Launchpad has currently only one stable API which is marked as EOL April 2015.
  # The new api in devel still uses the same api call for +archive, so I made the version
  # configurable to provide some sort of workaround if api 1.0 ceases to exist.
  # See https://launchpad.net/+apidoc/
  launchpad_ppa_api = "https://launchpad.net/api/#{node['apt']['launchpad_api_version']}/~%s/+archive/%s"
  default_keyserver = 'keyserver.ubuntu.com'

  require 'open-uri'
  api_query = format("#{launchpad_ppa_api}/signing_key_fingerprint", ppa_owner, ppa_repo)
  begin
    key_id = open(api_query).read.delete('"')
  rescue OpenURI::HTTPError => e
    error = 'Could not access launchpad ppa key api: HttpError: ' + e.message
    raise error
  rescue SocketError => e
    error = 'Could not access launchpad ppa key api: SocketError: ' + e.message
    raise error
  end

  install_key_from_keyserver(key_id, default_keyserver)
end

# fetch ppa key, return full repo url
def get_ppa_url(ppa)
  repo_schema       = 'http://ppa.launchpad.net/%s/%s/ubuntu'

  # ppa:user/repo logic ported from
  # http://bazaar.launchpad.net/~ubuntu-core-dev/software-properties/main/view/head:/softwareproperties/ppa.py#L86
  return false unless ppa.start_with?('ppa:')

  ppa_name = ppa.split(':')[1]
  ppa_owner = ppa_name.split('/')[0]
  ppa_repo  = ppa_name.split('/')[1]
  ppa_repo  = 'ppa' if ppa_repo.nil?

  get_ppa_key(ppa_owner, ppa_repo)

  format(repo_schema, ppa_owner, ppa_repo)
end

action :add do
  # add key
  if new_resource.keyserver && new_resource.key
    install_key_from_keyserver(new_resource.key, new_resource.keyserver)
  elsif new_resource.key
    install_key_from_uri(new_resource.key)
  end

  file '/var/lib/apt/periodic/update-success-stamp' do
    action :nothing
  end

  execute 'apt-cache gencaches' do
    ignore_failure true
    action :nothing
  end

  execute 'apt-get update' do
    command "apt-get update -o Dir::Etc::sourcelist='sources.list.d/#{new_resource.name}.list' -o Dir::Etc::sourceparts='-' -o APT::Get::List-Cleanup='0'"
    ignore_failure true
    action :nothing
    notifies :run, 'execute[apt-cache gencaches]', :immediately
  end

  if new_resource.uri.start_with?('ppa:')
    # build ppa repo file
    repository = build_repo(
      get_ppa_url(new_resource.uri),
      new_resource.distribution,
      'main',
      new_resource.trusted,
      new_resource.arch,
      new_resource.deb_src
      )
  else
    # build repo file
    repository = build_repo(
      new_resource.uri,
      new_resource.distribution,
      new_resource.components,
      new_resource.trusted,
      new_resource.arch,
      new_resource.deb_src
      )
  end

  file "/etc/apt/sources.list.d/#{new_resource.name}.list" do
    owner 'root'
    group 'root'
    mode 00644
    content repository
    action :create
    notifies :delete, 'file[/var/lib/apt/periodic/update-success-stamp]', :immediately
    notifies :run, 'execute[apt-get update]', :immediately if new_resource.cache_rebuild
  end
end

action :remove do
  if ::File.exist?("/etc/apt/sources.list.d/#{new_resource.name}.list")
    Chef::Log.info "Removing #{new_resource.name} repository from /etc/apt/sources.list.d/"
    file "/etc/apt/sources.list.d/#{new_resource.name}.list" do
      action :delete
    end
  end
end
