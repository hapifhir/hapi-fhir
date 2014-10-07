require 'chef/provider/lwrp_base'
require 'shellwords'
require_relative 'helpers_fedora'

class Chef
  class Provider
    class MysqlService
      class Fedora < Chef::Provider::MysqlService
        use_inline_resources if defined?(use_inline_resources)

        def whyrun_supported?
          true
        end

        include MysqlCookbook::Helpers::Fedora

        action :create do
          package new_resource.parsed_package_name do
            action new_resource.parsed_package_action
            version new_resource.parsed_package_version
          end

          directory include_dir do
            owner 'mysql'
            group 'mysql'
            mode '0750'
            action :create
            recursive true
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
            mode '0755'
            action :create
            recursive true
          end

          service 'mysqld' do
            supports :restart => true
            action [:start, :enable]
          end

          execute 'wait for mysql' do
            command "until [ -S #{socket_file} ] ; do sleep 1 ; done"
            timeout 10
            action :run
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

          template '/etc/my.cnf' do
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
              :include_dir => include_dir,
              :lc_messages_dir => lc_messages_dir,
              :pid_file => pid_file,
              :port => new_resource.parsed_port,
              :prefix_dir => prefix_dir,
              :socket_file => socket_file
              )
            action :create
            notifies :run, 'bash[move mysql data to datadir]'
            notifies :restart, 'service[mysqld]'
          end

          bash 'move mysql data to datadir' do
            user 'root'
            code <<-EOH
              service mysqld stop \
              && for i in `ls /var/lib/mysql | grep -v mysql.sock` ; do mv /var/lib/mysql/$i #{new_resource.parsed_data_dir} ; done
              EOH
            action :nothing
            creates "#{new_resource.parsed_data_dir}/ibdata1"
            creates "#{new_resource.parsed_data_dir}/ib_logfile0"
            creates "#{new_resource.parsed_data_dir}/ib_logfile1"
          end

          execute 'assign-root-password' do
            cmd = "#{prefix_dir}/bin/mysqladmin"
            cmd << ' -u root password '
            cmd << Shellwords.escape(new_resource.parsed_server_root_password)
            command cmd
            action :run
            only_if "#{prefix_dir}/bin/mysql -u root -e 'show databases;'"
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
        service 'mysqld' do
          supports :restart => true
          action :restart
        end
      end

      action :reload do
        service 'mysqld' do
          action :reload
        end
      end
    end
  end
end
