require 'chef/provider/lwrp_base'

class Chef
  class Provider
    class MysqlClient
      class Suse < Chef::Provider::MysqlClient
        use_inline_resources if defined?(use_inline_resources)

        def whyrun_supported?
          true
        end

        action :create do
          converge_by 'suse pattern' do
            %w(mysql-client libmysqlclient-devel).each do |p|
              package p do
                action :install
              end
            end
          end
        end

        action :delete do
          converge_by 'suse pattern' do
            %w(mysql-client libmysqlclient-devel).each do |p|
              package p do
                action :remove
              end
            end
          end
        end
      end
    end
  end
end
