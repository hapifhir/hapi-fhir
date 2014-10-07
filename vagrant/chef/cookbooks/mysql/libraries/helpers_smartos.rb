module MysqlCookbook
  module Helpers
    module SmartOS
      def include_dir
        include_dir = "#{prefix_dir}/etc/mysql/conf.d"
        include_dir
      end

      def pass_string
        if new_resource.parsed_server_root_password.empty?
          pass_string = ''
        else
          pass_string = '-p' + Shellwords.escape(new_resource.parsed_server_root_password)
        end

        pass_string = '-p' + ::File.open("#{prefix_dir}/etc/.mysql_root").read.chomp if ::File.exist?("#{prefix_dir}/etc/.mysql_root")
        pass_string
      end

      def pid_file
        pid_file = '/var/mysql/mysql.pid'
        pid_file
      end

      def prefix_dir
        prefix_dir = '/opt/local'
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
