class Chef
  class Provider
    class MysqlClient < Chef::Provider::LWRPBase
      use_inline_resources if defined?(use_inline_resources)

      def whyrun_supported?
        true
      end

      action :create do
        packages.each do |p|
          package p do
            action :install
          end
        end
      end

      action :delete do
        packages.each do |p|
          package p do
            action :remove
          end
        end
      end

      def packages
        %w()
      end
    end
  end
end
