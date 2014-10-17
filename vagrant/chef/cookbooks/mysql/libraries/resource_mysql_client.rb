require 'chef/resource/lwrp_base'

class Chef
  class Resource
    class MysqlClient < Chef::Resource::LWRPBase
      self.resource_name = :mysql_client
      actions :create, :delete
      default_action :create
    end
  end
end
