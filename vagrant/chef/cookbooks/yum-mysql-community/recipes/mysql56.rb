#
# Author:: Sean OMeara (<someara@getchef.com>)
# Recipe:: yum-mysql-community::mysql56-community
#
# Copyright 2014, Chef Software, Inc.
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

yum_repository 'mysql56-community' do
  description node['yum']['mysql56-community']['description']
  baseurl node['yum']['mysql56-community']['baseurl']
  mirrorlist node['yum']['mysql56-community']['mirrorlist']
  gpgcheck node['yum']['mysql56-community']['gpgcheck']
  gpgkey node['yum']['mysql56-community']['gpgkey']
  enabled node['yum']['mysql56-community']['enabled']
  cost node['yum']['mysql56-community']['cost']
  exclude node['yum']['mysql56-community']['exclude']
  enablegroups node['yum']['mysql56-community']['enablegroups']
  failovermethod node['yum']['mysql56-community']['failovermethod']
  http_caching node['yum']['mysql56-community']['http_caching']
  include_config node['yum']['mysql56-community']['include_config']
  includepkgs node['yum']['mysql56-community']['includepkgs']
  keepalive node['yum']['mysql56-community']['keepalive']
  max_retries node['yum']['mysql56-community']['max_retries']
  metadata_expire node['yum']['mysql56-community']['metadata_expire']
  mirror_expire node['yum']['mysql56-community']['mirror_expire']
  priority node['yum']['mysql56-community']['priority']
  proxy node['yum']['mysql56-community']['proxy']
  proxy_username node['yum']['mysql56-community']['proxy_username']
  proxy_password node['yum']['mysql56-community']['proxy_password']
  repositoryid node['yum']['mysql56-community']['repositoryid']
  sslcacert node['yum']['mysql56-community']['sslcacert']
  sslclientcert node['yum']['mysql56-community']['sslclientcert']
  sslclientkey node['yum']['mysql56-community']['sslclientkey']
  sslverify node['yum']['mysql56-community']['sslverify']
  timeout node['yum']['mysql56-community']['timeout']
  action :create
end
