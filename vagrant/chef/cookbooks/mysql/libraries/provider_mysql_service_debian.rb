require 'chef/provider/lwrp_base'
require 'shellwords'
require_relative 'helpers_debian'

class Chef
  class Provider
    class MysqlService
      class Debian < Chef::Provider::MysqlService
        use_inline_resources if defined?(use_inline_resources)

        def whyrun_supported?
          true
        end

        include MysqlCookbook::Helpers::Debian

        action :create do
          package 'debconf-utils' do
            action :install
          end

          directory '/var/cache/local/preseeding' do
            owner 'root'
            group 'root'
            mode '0755'
            action :create
            recursive true
          end

          template '/var/cache/local/preseeding/mysql-server.seed' do
            cookbook 'mysql'
            source 'debian/mysql-server.seed.erb'
            owner 'root'
            group 'root'
            mode '0600'
            variables(:config => new_resource)
            action :create
            notifies :run, 'execute[preseed mysql-server]', :immediately
          end

          execute 'preseed mysql-server' do
            command '/usr/bin/debconf-set-selections /var/cache/local/preseeding/mysql-server.seed'
            action :nothing
          end

          # package automatically initializes database and starts service.
          # ... because that's totally super convenient.
          package new_resource.parsed_package_name do
            action :install
          end

          # service
          service 'mysql' do
            provider Chef::Provider::Service::Init::Debian
            supports :restart => true
            action [:start, :enable]
          end

          execute 'assign-root-password' do
            cmd = "#{prefix_dir}/bin/mysqladmin"
            cmd << ' -u root password '
            cmd << Shellwords.escape(new_resource.parsed_server_root_password)
            command cmd
            action :run
            only_if "#{prefix_dir}/bin/mysql -u root -e 'show databases;'"
          end

          template '/etc/mysql_grants.sql' do
            cookbook 'mysql'
            source 'grants/grants.sql.erb'
            owner 'root'
            group 'root'
            mode '0600'
            variables(:config => new_resource)
            action :create
            notifies :run, 'execute[install-grants]'
          end

          execute 'install-grants' do
            cmd = "#{prefix_dir}/bin/mysql"
            cmd << ' -u root '
            cmd << "#{pass_string} < /etc/mysql_grants.sql"
            command cmd
            action :nothing
            notifies :run, 'execute[create root marker]'
          end

          template '/etc/mysql/debian.cnf' do
            cookbook 'mysql'
            source 'debian/debian.cnf.erb'
            owner 'root'
            group 'root'
            mode '0600'
            variables(:config => new_resource)
            action :create
          end

          #
          directory include_dir do
            owner 'mysql'
            group 'mysql'
            mode '0750'
            recursive true
            action :create
          end

          directory run_dir do
            owner 'mysql'
            group 'mysql'
            mode '0755'
            action :create
            recursive true
          end

          directory new_resource.parsed_data_dir do
            owner 'mysql'
            group 'mysql'
            mode '0750'
            recursive true
            action :create
          end

          template '/etc/mysql/my.cnf' do
            if new_resource.parsed_template_source.nil?
              source "#{new_resource.parsed_version}/my.cnf.erb"
              cookbook 'mysql'
            else
              source new_resource.parsed_template_source
            end
            owner 'mysql'
            group 'mysql'
            mode '0600'
            variables(
              :data_dir => new_resource.parsed_data_dir,
              :pid_file => pid_file,
              :socket_file => socket_file,
              :port => new_resource.parsed_port,
              :include_dir => include_dir
              )
            action :create
            notifies :run, 'bash[move mysql data to datadir]'
            notifies :restart, 'service[mysql]'
          end

          bash 'move mysql data to datadir' do
            user 'root'
            code <<-EOH
              service mysql stop \
              && mv /var/lib/mysql/* #{new_resource.parsed_data_dir}
              EOH
            creates "#{new_resource.parsed_data_dir}/ibdata1"
            creates "#{new_resource.parsed_data_dir}/ib_logfile0"
            creates "#{new_resource.parsed_data_dir}/ib_logfile1"
            action :nothing
          end

          execute 'create root marker' do
            cmd = '/bin/echo'
            cmd << " '#{Shellwords.escape(new_resource.parsed_server_root_password)}'"
            cmd << ' > /etc/.mysql_root'
            cmd << ' ;/bin/chmod 0600 /etc/.mysql_root'
            command cmd
            action :nothing
          end
        end
      end

      action :restart do
        service 'mysql' do
          provider Chef::Provider::Service::Init::Debian
          supports :restart => true
          action :restart
        end
      end

      action :reload do
        service 'mysql' do
          provider Chef::Provider::Service::Init::Debian
          action :reload
        end
      end
    end
  end
end
