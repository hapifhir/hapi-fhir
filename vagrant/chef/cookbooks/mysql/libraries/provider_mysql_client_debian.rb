require 'chef/provider/lwrp_base'

class Chef
  class Provider
    class MysqlClient
      class Debian < Chef::Provider::MysqlClient
        def packages
          %w(mysql-client libmysqlclient-dev)
        end
      end
    end
  end
end
