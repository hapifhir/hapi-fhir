#
# Author:: Sean OMeara (<someara@getchef.com>)
# Recipe:: yum::default
#
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

yum_globalconfig '/etc/yum.conf' do
  alwaysprompt node['yum']['main']['alwaysprompt']
  assumeyes node['yum']['main']['assumeyes']
  bandwidth node['yum']['main']['bandwidth']
  bugtracker_url node['yum']['main']['bugtracker_url']
  clean_requirements_on_remove node['yum']['main']['clean_requirements_on_remove']
  color node['yum']['main']['color']
  color_list_available_downgrade node['yum']['main']['color_list_available_downgrade']
  color_list_available_install node['yum']['main']['color_list_available_install']
  color_list_available_reinstall node['yum']['main']['color_list_available_reinstall']
  color_list_available_upgrade node['yum']['main']['color_list_available_upgrade']
  color_list_installed_extra node['yum']['main']['color_list_installed_extra']
  color_list_installed_newer node['yum']['main']['color_list_installed_newer']
  color_list_installed_older node['yum']['main']['color_list_installed_older']
  color_list_installed_reinstall node['yum']['main']['color_list_installed_reinstall']
  color_search_match node['yum']['main']['color_search_match']
  color_update_installed node['yum']['main']['color_update_installed']
  color_update_local node['yum']['main']['color_update_local']
  color_update_remote node['yum']['main']['color_update_remote']
  debuglevel node['yum']['main']['debuglevel']
  diskspacecheck node['yum']['main']['diskspacecheck']
  enable_group_conditionals node['yum']['main']['enable_group_conditionals']
  errorlevel node['yum']['main']['errorlevel']
  exactarch node['yum']['main']['exactarch']
  exclude node['yum']['main']['exclude']
  gpgcheck node['yum']['main']['gpgcheck']
  group_package_types node['yum']['main']['group_package_types']
  groupremove_leaf_only node['yum']['main']['groupremove_leaf_only']
  history_list_view node['yum']['main']['history_list_view']
  history_record node['yum']['main']['history_record']
  history_record_packages node['yum']['main']['history_record_packages']
  http_caching node['yum']['main']['http_caching']
  installonly_limit node['yum']['main']['installonly_limit']
  installonlypkgs node['yum']['main']['installonlypkgs']
  installroot node['yum']['main']['installroot']
  keepalive node['yum']['main']['keepalive']
  keepcache node['yum']['main']['keepcache']
  kernelpkgnames node['yum']['main']['kernelpkgnames']
  localpkg_gpgcheck node['yum']['main']['localpkg_gpgcheck']
  logfile node['yum']['main']['logfile']
  max_retries node['yum']['main']['max_retries']
  mdpolicy node['yum']['main']['mdpolicy']
  metadata_expire node['yum']['main']['metadata_expire']
  mirrorlist_expire node['yum']['main']['mirrorlist_expire']
  multilib_policy node['yum']['main']['multilib_policy']
  obsoletes node['yum']['main']['obsoletes']
  overwrite_groups node['yum']['main']['overwrite_groups']
  password node['yum']['main']['password']
  path node['yum']['main']['path']
  persistdir node['yum']['main']['persistdir']
  pluginconfpath node['yum']['main']['pluginconfpath']
  pluginpath node['yum']['main']['pluginpath']
  plugins node['yum']['main']['plugins']
  protected_multilib node['yum']['main']['protected_multilib']
  protected_packages node['yum']['main']['protected_packages']
  proxy node['yum']['main']['proxy']
  proxy_username node['yum']['main']['proxy_username']
  proxy_password node['yum']['main']['proxy_password']
  username node['yum']['main']['username']
  password node['yum']['main']['password']
  recent node['yum']['main']['recent']
  releasever node['yum']['main']['releasever']
  repo_gpgcheck node['yum']['main']['repo_gpgcheck']
  reset_nice node['yum']['main']['reset_nice']
  rpmverbosity node['yum']['main']['rpmverbosity']
  showdupesfromrepos node['yum']['main']['showdupesfromrepos']
  skip_broken node['yum']['main']['skip_broken']
  ssl_check_cert_permissions node['yum']['main']['ssl_check_cert_permissions']
  sslcacert node['yum']['main']['sslcacert']
  sslclientcert node['yum']['main']['sslclientcert']
  sslclientkey node['yum']['main']['sslclientkey']
  syslog_device node['yum']['main']['syslog_device']
  syslog_facility node['yum']['main']['syslog_facility']
  syslog_ident node['yum']['main']['syslog_ident']
  throttle node['yum']['main']['throttle']
  timeout node['yum']['main']['timeout']
  tolerant node['yum']['main']['tolerant']
  tsflags node['yum']['main']['tsflags']
  action :create
end
