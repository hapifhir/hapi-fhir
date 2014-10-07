require 'chef/provider/lwrp_base'

class Chef
  class Provider
    class MysqlClient
      class Ubuntu < Chef::Provider::MysqlClient
        def packages
          %w(mysql-client-5.5 libmysqlclient-dev)
        end
      end
    end
  end
end
