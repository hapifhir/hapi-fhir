require 'chef/provider/lwrp_base'
require 'shellwords'
require_relative 'helpers_omnios'

include Opscode::Mysql::Helpers

class Chef
  class Provider
    class MysqlService
      class Omnios < Chef::Provider::MysqlService
        use_inline_resources if defined?(use_inline_resources)

        def whyrun_supported?
          true
        end

        include MysqlCookbook::Helpers::OmniOS

        action :create do
          package new_resource.parsed_package_name do
            action :install
          end

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

          # data_dir
          directory new_resource.parsed_data_dir do
            owner 'mysql'
            group 'mysql'
            mode '0750'
            action :create
            recursive true
          end

          directory "#{new_resource.parsed_data_dir}/data" do
            owner 'mysql'
            group 'mysql'
            mode '0750'
            action :create
            recursive true
          end

          directory "#{new_resource.parsed_data_dir}/data/mysql" do
            owner 'mysql'
            group 'mysql'
            mode '0750'
            action :create
            recursive true
          end

          directory "#{new_resource.parsed_data_dir}/data/test" do
            owner 'mysql'
            group 'mysql'
            mode '0750'
            action :create
            recursive true
          end

          template my_cnf do
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
              :base_dir => base_dir,
              :include_dir => include_dir,
              :data_dir => new_resource.parsed_data_dir,
              :pid_file => pid_file,
              :socket_file => socket_file,
              :port => new_resource.parsed_port,
              :lc_messages_dir => "#{base_dir}/share"
              )
            action :create
            notifies :run, 'bash[move mysql data to datadir]'
            notifies :restart, 'service[mysql]'
          end

          bash 'move mysql data to datadir' do
            user 'root'
            code <<-EOH
              /usr/sbin/svcadm disable mysql \
              && mv /var/mysql/* #{new_resource.parsed_data_dir}
              EOH
            action :nothing
            creates "#{new_resource.parsed_data_dir}/ibdata1"
            creates "#{new_resource.parsed_data_dir}/ib_logfile0"
            creates "#{new_resource.parsed_data_dir}/ib_logfile1"
          end

          execute 'initialize mysql database' do
            cwd new_resource.parsed_data_dir
            command "#{prefix_dir}/scripts/mysql_install_db --basedir=#{base_dir} --user=mysql"
            creates "#{new_resource.parsed_data_dir}/mysql/user.frm"
          end

          template '/lib/svc/method/mysqld' do
            cookbook 'mysql'
            source 'omnios/svc.method.mysqld.erb'
            cookbook 'mysql'
            owner 'root'
            group 'root'
            mode '0555'
            variables(
              :base_dir => base_dir,
              :data_dir => new_resource.parsed_data_dir,
              :pid_file => pid_file
              )
            action :create
          end

          template '/tmp/mysql.xml' do
            cookbook 'mysql'
            source 'omnios/mysql.xml.erb'
            owner 'root'
            mode '0644'
            variables(:version => new_resource.parsed_version)
            action :create
            notifies :run, 'execute[import mysql manifest]', :immediately
          end

          execute 'import mysql manifest' do
            command 'svccfg import /tmp/mysql.xml'
            action :nothing
          end

          service 'mysql' do
            supports :restart => true
            action [:start, :enable]
          end

          execute 'wait for mysql' do
            command "until [ -S #{socket_file} ] ; do sleep 1 ; done"
            timeout 10
            action :run
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
            retries 5
            retry_delay 2
            action :nothing
            notifies :run, 'execute[create root marker]'
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

        action :restart do
          service 'mysql' do
            supports :restart => true
            action :restart
          end
        end

        action :reload do
          service 'mysql' do
            supports :reload => true
            action :reload
          end
        end
      end
    end
  end
end
