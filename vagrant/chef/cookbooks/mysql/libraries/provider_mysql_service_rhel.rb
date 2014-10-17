require 'chef/provider/lwrp_base'
require 'shellwords'
require_relative 'helpers_rhel'

class Chef
  class Provider
    class MysqlService
      class Rhel < Chef::Provider::MysqlService
        use_inline_resources if defined?(use_inline_resources)

        def whyrun_supported?
          true
        end

        include MysqlCookbook::Helpers::Rhel

        action :create do
          # we need to enable the yum-mysql-community repository to get packages
          unless node['platform_version'].to_i == 5
            case new_resource.parsed_version
            when '5.5'
              recipe_eval do
                run_context.include_recipe 'yum-mysql-community::mysql55'
              end
            when '5.6'
              recipe_eval do
                run_context.include_recipe 'yum-mysql-community::mysql56'
              end
            end
          end

          package new_resource.parsed_package_name do
            action new_resource.parsed_package_action
            version new_resource.parsed_package_version
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
            recursive true
            action :create
          end

          directory new_resource.parsed_data_dir do
            owner 'mysql'
            group 'mysql'
            mode '0755'
            recursive true
            action :create
          end

          service service_name do
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

          template "#{base_dir}/etc/my.cnf" do
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
              :data_dir => new_resource.parsed_data_dir,
              :include_dir => include_dir,
              :lc_messages_dir => lc_messages_dir,
              :pid_file => pid_file,
              :port => new_resource.parsed_port,
              :socket_file => socket_file
              )
            action :create
            notifies :run, 'bash[move mysql data to datadir]'
            notifies :restart, "service[#{service_name}]"
          end

          bash 'move mysql data to datadir' do
            user 'root'
            code <<-EOH
              service #{service_name} stop \
              && for i in `ls #{base_dir}/var/lib/mysql | grep -v mysql.sock` ; do mv #{base_dir}/var/lib/mysql/$i #{new_resource.parsed_data_dir} ; done
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

        action :restart do
          service service_name do
            supports :restart => true
            action :restart
          end
        end

        action :reload do
          service service_name do
            action :reload
          end
        end
      end
    end
  end
end
