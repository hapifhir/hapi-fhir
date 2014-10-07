module MysqlCookbook
  module Helpers
    module Rhel
      def base_dir
        case node['platform_version'].to_i
        when 5
          case new_resource.parsed_version
          when '5.0'
            base_dir = ''
          when '5.1'
            base_dir = '/opt/rh/mysql51/root'
          when '5.5'
            base_dir = '/opt/rh/mysql55/root'
          end
        end
        base_dir
      end

      def include_dir
        case node['platform_version'].to_i
        when 2014, 2013, 7, 6
          include_dir = '/etc/mysql/conf.d'
        when 5
          include_dir = "#{base_dir}/etc/mysql/conf.d"
        end
        include_dir
      end

      def prefix_dir
        case node['platform_version'].to_i
        when 2014, 2013, 7, 6
          prefix_dir =  '/usr'
        when 5
          case new_resource.parsed_version
          when '5.0'
            prefix_dir = '/usr'
          when '5.1'
            prefix_dir = '/opt/rh/mysql51/root/usr'
          when '5.5'
            prefix_dir = '/opt/rh/mysql55/root/usr'
          end
        end
        prefix_dir
      end

      def lc_messages_dir
        case node['platform_version'].to_i
        when 2014, 2013, 7, 6, 5
          lc_messages_dir = nil
        end
        lc_messages_dir
      end

      def run_dir
        case node['platform_version'].to_i
        when 2014, 2013, 7, 6
          run_dir = '/var/run/mysqld'
        when 5
          case new_resource.parsed_version
          when '5.0'
            run_dir = '/var/run/mysqld'
          when '5.1'
            run_dir = '/opt/rh/mysql51/root/var/run/mysqld/'
          when '5.5'
            run_dir = '/opt/rh/mysql55/root/var/run/mysqld/'
          end
        end
        run_dir
      end

      def pass_string
        if new_resource.parsed_server_root_password.empty?
          pass_string = ''
        else
          pass_string = '-p' + Shellwords.escape(new_resource.parsed_server_root_password)
        end

        pass_string = '-p' + ::File.open('/etc/.mysql_root').read.chomp if ::File.exist?('/etc/.mysql_root')
        pass_string
      end

      def pid_file
        case node['platform_version'].to_i
        when 2014, 2013, 7, 6, 5
          pid_file = '/var/run/mysqld/mysql.pid'
        end
        pid_file
      end

      def socket_file
        case node['platform_version'].to_i
        when 2014, 2013, 7, 6, 5
          socket_file = '/var/lib/mysql/mysql.sock'
        end
        socket_file
      end

      def service_name
        case node['platform_version'].to_i
        when 2014, 2013, 7, 6
          service_name = 'mysqld'
        when 5
          case new_resource.parsed_version
          when '5.0'
            service_name = 'mysqld'
          when '5.1'
            service_name = 'mysql51-mysqld'
          when '5.5'
            service_name = 'mysql55-mysqld'
          end
        end
        service_name
      end
    end
  end
end
