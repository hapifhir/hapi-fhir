# provider mappings

# client
Chef::Provider::MysqlClient::Debian.provides :mysql_client, platform: "debian"
Chef::Provider::MysqlClient::Fedora.provides :mysql_client, platform: "fedora"
Chef::Provider::MysqlClient::FreeBSD.provides :mysql_client, platform: "freebsd"
Chef::Provider::MysqlClient::Omnios.provides :mysql_client, platform: "omnios"
Chef::Provider::MysqlClient::Rhel.provides :mysql_client, platform: "amazon"
Chef::Provider::MysqlClient::Rhel.provides :mysql_client, platform: "redhat"
Chef::Provider::MysqlClient::Rhel.provides :mysql_client, platform: "centos"
Chef::Provider::MysqlClient::Rhel.provides :mysql_client, platform: "oracle"
Chef::Provider::MysqlClient::Rhel.provides :mysql_client, platform: "scientific"
Chef::Provider::MysqlClient::Smartos.provides :mysql_client, platform: "smartos"
Chef::Provider::MysqlClient::Suse.provides :mysql_client, platform: "suse"
Chef::Provider::MysqlClient::Ubuntu.provides :mysql_client, platform: "ubuntu"

# service
Chef::Provider::MysqlService::Debian.provides :mysql_service, platform: "debian"
Chef::Provider::MysqlService::Fedora.provides :mysql_service, platform: "fedora"
Chef::Provider::MysqlService::FreeBSD.provides :mysql_service, platform: "freebsd"
Chef::Provider::MysqlService::Omnios.provides :mysql_service, platform: "omnios"
Chef::Provider::MysqlService::Rhel.provides :mysql_service, platform: "amazon"
Chef::Provider::MysqlService::Rhel.provides :mysql_service, platform: "redhat"
Chef::Provider::MysqlService::Rhel.provides :mysql_service, platform: "centos"
Chef::Provider::MysqlService::Rhel.provides :mysql_service, platform: "oracle"
Chef::Provider::MysqlService::Rhel.provides :mysql_service, platform: "scientific"
Chef::Provider::MysqlService::Smartos.provides :mysql_service, platform: "smartos"
Chef::Provider::MysqlService::Suse.provides :mysql_service, platform: "suse"
Chef::Provider::MysqlService::Ubuntu.provides :mysql_service, platform: "ubuntu"
