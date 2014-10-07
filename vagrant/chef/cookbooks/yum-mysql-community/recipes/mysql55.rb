#
# Author:: Sean OMeara (<someara@getchef.com>)
# Recipe:: yum-mysql-community::mysql55
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

yum_repository 'mysql55-community' do
  description node['yum']['mysql55-community']['description']
  baseurl node['yum']['mysql55-community']['baseurl']
  mirrorlist node['yum']['mysql55-community']['mirrorlist']
  gpgcheck node['yum']['mysql55-community']['gpgcheck']
  gpgkey node['yum']['mysql55-community']['gpgkey']
  enabled node['yum']['mysql55-community']['enabled']
  cost node['yum']['mysql55-community']['cost']
  exclude node['yum']['mysql55-community']['exclude']
  enablegroups node['yum']['mysql55-community']['enablegroups']
  failovermethod node['yum']['mysql55-community']['failovermethod']
  http_caching node['yum']['mysql55-community']['http_caching']
  include_config node['yum']['mysql55-community']['include_config']
  includepkgs node['yum']['mysql55-community']['includepkgs']
  keepalive node['yum']['mysql55-community']['keepalive']
  max_retries node['yum']['mysql55-community']['max_retries']
  metadata_expire node['yum']['mysql55-community']['metadata_expire']
  mirror_expire node['yum']['mysql55-community']['mirror_expire']
  priority node['yum']['mysql55-community']['priority']
  proxy node['yum']['mysql55-community']['proxy']
  proxy_username node['yum']['mysql55-community']['proxy_username']
  proxy_password node['yum']['mysql55-community']['proxy_password']
  repositoryid node['yum']['mysql55-community']['repositoryid']
  sslcacert node['yum']['mysql55-community']['sslcacert']
  sslclientcert node['yum']['mysql55-community']['sslclientcert']
  sslclientkey node['yum']['mysql55-community']['sslclientkey']
  sslverify node['yum']['mysql55-community']['sslverify']
  timeout node['yum']['mysql55-community']['timeout']
  action :create
end
