# provider mappings

# client
Chef::Platform.set :platform => :debian, :resource => :mysql_client, :provider => Chef::Provider::MysqlClient::Debian
Chef::Platform.set :platform => :fedora, :resource => :mysql_client, :provider => Chef::Provider::MysqlClient::Fedora
Chef::Platform.set :platform => :freebsd, :resource => :mysql_client, :provider => Chef::Provider::MysqlClient::FreeBSD
Chef::Platform.set :platform => :omnios, :resource => :mysql_client, :provider => Chef::Provider::MysqlClient::Omnios
Chef::Platform.set :platform => :rhel, :resource => :mysql_client, :provider => Chef::Provider::MysqlClient::Rhel
Chef::Platform.set :platform => :amazon, :resource => :mysql_client, :provider => Chef::Provider::MysqlClient::Rhel
Chef::Platform.set :platform => :redhat, :resource => :mysql_client, :provider => Chef::Provider::MysqlClient::Rhel
Chef::Platform.set :platform => :centos, :resource => :mysql_client, :provider => Chef::Provider::MysqlClient::Rhel
Chef::Platform.set :platform => :oracle, :resource => :mysql_client, :provider => Chef::Provider::MysqlClient::Rhel
Chef::Platform.set :platform => :scientific, :resource => :mysql_client, :provider => Chef::Provider::MysqlClient::Rhel
Chef::Platform.set :platform => :smartos, :resource => :mysql_client, :provider => Chef::Provider::MysqlClient::Smartos
Chef::Platform.set :platform => :suse, :resource => :mysql_client, :provider => Chef::Provider::MysqlClient::Suse
Chef::Platform.set :platform => :ubuntu, :resource => :mysql_client, :provider => Chef::Provider::MysqlClient::Ubuntu

# service
Chef::Platform.set :platform => :debian, :resource => :mysql_service, :provider => Chef::Provider::MysqlService::Debian
Chef::Platform.set :platform => :fedora, :resource => :mysql_service, :provider => Chef::Provider::MysqlService::Fedora
Chef::Platform.set :platform => :freebsd, :resource => :mysql_service, :provider => Chef::Provider::MysqlService::FreeBSD
Chef::Platform.set :platform => :omnios, :resource => :mysql_service, :provider => Chef::Provider::MysqlService::Omnios
Chef::Platform.set :platform => :amazon, :resource => :mysql_service, :provider => Chef::Provider::MysqlService::Rhel
Chef::Platform.set :platform => :redhat, :resource => :mysql_service, :provider => Chef::Provider::MysqlService::Rhel
Chef::Platform.set :platform => :centos, :resource => :mysql_service, :provider => Chef::Provider::MysqlService::Rhel
Chef::Platform.set :platform => :oracle, :resource => :mysql_service, :provider => Chef::Provider::MysqlService::Rhel
Chef::Platform.set :platform => :scientific, :resource => :mysql_service, :provider => Chef::Provider::MysqlService::Rhel
Chef::Platform.set :platform => :smartos, :resource => :mysql_service, :provider => Chef::Provider::MysqlService::Smartos
Chef::Platform.set :platform => :suse, :resource => :mysql_service, :provider => Chef::Provider::MysqlService::Suse
Chef::Platform.set :platform => :ubuntu, :resource => :mysql_service, :provider => Chef::Provider::MysqlService::Ubuntu
