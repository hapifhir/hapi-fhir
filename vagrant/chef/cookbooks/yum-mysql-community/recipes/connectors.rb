#
# Author:: Sean OMeara (<someara@getchef.com>)
# Recipe:: yum-mysql-community::connectors
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

yum_repository 'mysql-connectors-community' do
  description node['yum']['mysql-connectors-community']['description']
  baseurl node['yum']['mysql-connectors-community']['baseurl']
  mirrorlist node['yum']['mysql-connectors-community']['mirrorlist']
  gpgcheck node['yum']['mysql-connectors-community']['gpgcheck']
  gpgkey node['yum']['mysql-connectors-community']['gpgkey']
  enabled node['yum']['mysql-connectors-community']['enabled']
  cost node['yum']['mysql-connectors-community']['cost']
  exclude node['yum']['mysql-connectors-community']['exclude']
  enablegroups node['yum']['mysql-connectors-community']['enablegroups']
  failovermethod node['yum']['mysql-connectors-community']['failovermethod']
  http_caching node['yum']['mysql-connectors-community']['http_caching']
  include_config node['yum']['mysql-connectors-community']['include_config']
  includepkgs node['yum']['mysql-connectors-community']['includepkgs']
  keepalive node['yum']['mysql-connectors-community']['keepalive']
  max_retries node['yum']['mysql-connectors-community']['max_retries']
  metadata_expire node['yum']['mysql-connectors-community']['metadata_expire']
  mirror_expire node['yum']['mysql-connectors-community']['mirror_expire']
  priority node['yum']['mysql-connectors-community']['priority']
  proxy node['yum']['mysql-connectors-community']['proxy']
  proxy_username node['yum']['mysql-connectors-community']['proxy_username']
  proxy_password node['yum']['mysql-connectors-community']['proxy_password']
  repositoryid node['yum']['mysql-connectors-community']['repositoryid']
  sslcacert node['yum']['mysql-connectors-community']['sslcacert']
  sslclientcert node['yum']['mysql-connectors-community']['sslclientcert']
  sslclientkey node['yum']['mysql-connectors-community']['sslclientkey']
  sslverify node['yum']['mysql-connectors-community']['sslverify']
  timeout node['yum']['mysql-connectors-community']['timeout']
  action :create
end
