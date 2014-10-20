module MysqlCookbook
  module Helpers
    module FreeBSD
      def base_dir
        base_dir = '/usr/local'
        base_dir
      end

      def include_dir
        include_dir = '/usr/local/etc/mysql/conf.d'
        include_dir
      end

      def my_cnf
        my_cnf = '/usr/local/etc/my.cnf'
        my_cnf
      end

      def pid_file
        pid_file = '/var/db/mysql/mysqld.pid'
        pid_file
      end

      def prefix_dir
        prefix_dir = '/usr/local'
        prefix_dir
      end

      def rc_name
        rc_name = 'mysql-server'
        rc_name
      end

      def run_dir
        run_dir = '/var/run/mysqld'
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

      def socket_file
        socket_file = '/tmp/mysqld.sock'
        socket_file
      end
    end
  end
end
