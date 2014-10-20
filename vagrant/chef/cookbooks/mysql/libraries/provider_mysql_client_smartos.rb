require 'chef/provider/lwrp_base'

class Chef
  class Provider
    class MysqlClient
      class Smartos < Chef::Provider::MysqlClient
        def packages
          %w(mysql-client)
        end
      end
    end
  end
end
