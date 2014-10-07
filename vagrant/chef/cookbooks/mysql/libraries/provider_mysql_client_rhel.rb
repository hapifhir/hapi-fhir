require 'chef/provider/lwrp_base'

class Chef
  class Provider
    class MysqlClient
      class Rhel < Chef::Provider::MysqlClient
        def packages
          %w(mysql mysql-devel)
        end
      end
    end
  end
end
