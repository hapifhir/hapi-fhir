#
# Author:: Sean OMeara (<someara@getchef.com>)
# Recipe:: yum-mysql-community::mysql57-community
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

yum_repository 'mysql57-community' do
  description node['yum']['mysql57-community']['description']
  baseurl node['yum']['mysql57-community']['baseurl']
  mirrorlist node['yum']['mysql57-community']['mirrorlist']
  gpgcheck node['yum']['mysql57-community']['gpgcheck']
  gpgkey node['yum']['mysql57-community']['gpgkey']
  enabled node['yum']['mysql57-community']['enabled']
  cost node['yum']['mysql57-community']['cost']
  exclude node['yum']['mysql57-community']['exclude']
  enablegroups node['yum']['mysql57-community']['enablegroups']
  failovermethod node['yum']['mysql57-community']['failovermethod']
  http_caching node['yum']['mysql57-community']['http_caching']
  include_config node['yum']['mysql57-community']['include_config']
  includepkgs node['yum']['mysql57-community']['includepkgs']
  keepalive node['yum']['mysql57-community']['keepalive']
  max_retries node['yum']['mysql57-community']['max_retries']
  metadata_expire node['yum']['mysql57-community']['metadata_expire']
  mirror_expire node['yum']['mysql57-community']['mirror_expire']
  priority node['yum']['mysql57-community']['priority']
  proxy node['yum']['mysql57-community']['proxy']
  proxy_username node['yum']['mysql57-community']['proxy_username']
  proxy_password node['yum']['mysql57-community']['proxy_password']
  repositoryid node['yum']['mysql57-community']['repositoryid']
  sslcacert node['yum']['mysql57-community']['sslcacert']
  sslclientcert node['yum']['mysql57-community']['sslclientcert']
  sslclientkey node['yum']['mysql57-community']['sslclientkey']
  sslverify node['yum']['mysql57-community']['sslverify']
  timeout node['yum']['mysql57-community']['timeout']
  action :create
end
