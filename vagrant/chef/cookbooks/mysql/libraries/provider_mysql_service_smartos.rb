require 'chef/provider/lwrp_base'
require 'shellwords'
require_relative 'helpers_smartos'

class Chef
  class Provider
    class MysqlService
      class Smartos < Chef::Provider::MysqlService
        use_inline_resources if defined?(use_inline_resources)

        def whyrun_supported?
          true
        end

        include MysqlCookbook::Helpers::SmartOS

        action :create do
          package new_resource.parsed_package_name do
            version new_resource.parsed_version
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

          # TODO: support user supplied template
          template "#{prefix_dir}/etc/my.cnf" do
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
            notifies :run, 'bash[move mysql data to datadir]', :immediately
            notifies :restart, 'service[mysql]'
          end

          bash 'move mysql data to datadir' do
            user 'root'
            code <<-EOH
              /usr/sbin/svcadm disable mysql \
              && mv /opt/local/lib/mysql/* #{new_resource.parsed_data_dir}
              EOH
            action :nothing
            creates "#{new_resource.parsed_data_dir}/ibdata1"
            creates "#{new_resource.parsed_data_dir}/ib_logfile0"
            creates "#{new_resource.parsed_data_dir}/ib_logfile1"
          end

          execute 'initialize mysql database' do
            cwd new_resource.parsed_data_dir
            command "#{prefix_dir}/bin/mysql_install_db --datadir=#{new_resource.parsed_data_dir} --user=mysql"
            creates "#{new_resource.parsed_data_dir}/mysql/user.frm"
          end

          template '/opt/local/lib/svc/method/mysqld' do
            cookbook 'mysql'
            source 'smartos/svc.method.mysqld.erb'
            owner 'root'
            group 'root'
            mode '0555'
            variables(
              :data_dir => new_resource.parsed_data_dir,
              :pid_file => pid_file
              )
            action :create
          end

          template '/tmp/mysql.xml' do
            cookbook 'mysql'
            source 'smartos/mysql.xml.erb'
            owner 'root'
            group 'root'
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
            supports :reload => true
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

          template "#{prefix_dir}/etc/mysql_grants.sql" do
            cookbook 'mysql'
            source 'grants/grants.sql.erb'
            owner 'root'
            group 'root'
            mode '0600'
            variables(:config => new_resource)
            action :create
            notifies :run, 'execute[install-grants]', :immediately
          end

          execute 'install-grants' do
            cmd = "#{prefix_dir}/bin/mysql"
            cmd << ' -u root '
            cmd << "#{pass_string} < #{prefix_dir}/etc/mysql_grants.sql"
            command cmd
            action :nothing
            notifies :run, 'execute[create root marker]'
          end

          execute 'create root marker' do
            cmd = '/bin/echo'
            cmd << " '#{Shellwords.escape(new_resource.parsed_server_root_password)}'"
            cmd << " > #{prefix_dir}/etc/.mysql_root"
            cmd << " ;/bin/chmod 0600 #{prefix_dir}/etc/.mysql_root"
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
