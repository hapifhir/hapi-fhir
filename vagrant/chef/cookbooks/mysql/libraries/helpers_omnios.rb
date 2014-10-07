module MysqlCookbook
  module Helpers
    module OmniOS
      def base_dir
        base_dir = "/opt/mysql#{pkg_ver_string}"
        base_dir
      end

      def include_dir
        include_dir = "/opt/mysql#{pkg_ver_string}/etc/mysql/conf.d"
        include_dir
      end

      def my_cnf
        case new_resource.parsed_version
        when '5.5'
          my_cnf = "#{base_dir}/etc/my.cnf"
        when '5.6'
          my_cnf = "#{base_dir}/my.cnf"
        end
        my_cnf
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
        pid_file = '/var/run/mysql/mysql.pid'
        pid_file
      end

      def pkg_ver_string
        pkg_ver_string = new_resource.parsed_version.gsub('.', '')
        pkg_ver_string
      end

      def prefix_dir
        prefix_dir = "/opt/mysql#{pkg_ver_string}"
        prefix_dir
      end

      def run_dir
        run_dir = '/var/run/mysql'
        run_dir
      end

      def socket_file
        socket_file = '/tmp/mysql.sock'
        socket_file
      end
    end
  end
end
