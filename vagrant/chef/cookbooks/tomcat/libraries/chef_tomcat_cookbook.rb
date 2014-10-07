#
# Cookbook Name:: tomcat
# Library:: chef_tomcat_cookbook
#
# Author:: Jamie Winsor (<jamie@vialstudios.com>)
#
# Copyright 2010-2012, Opscode, Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

class Chef
  module TomcatCookbook
    class TomcatCookbookError < StandardError; end
    class InvalidUserDataBagItem < TomcatCookbookError
      attr_reader :item

      def initialize(item)
        @item = item
      end

      def to_s
        msg = "The item you provided: #{item.inspect} in the #{USERS_DATA_BAG} was invalid. Items "
        msg << "require an 'id', 'password', and 'roles' field. Consult the documentation for further instructions."
      end
    end

    class TomcatUserDataBagNotFound < TomcatCookbookError
      def to_s
        "Please create a data bag named '#{USERS_DATA_BAG}' and try again."
      end
    end

    USERS_DATA_BAG = 'tomcat_users'

    class << self
      # Returns a array of data bag items for the users in the Tomcat Users
      # data bag. All items are validated before returning.
      #
      # @raise [TomcatCookbook::InvalidUserDataBagItem] if an invalid item
      #   was found in the Tomcat users data bag.
      #
      # @return [Array<Chef::DataBagItem>, Array<Chef::EncryptedDataBagItem>]
      def users
        @users ||= find_users
      end

      # Returns an array of roles assigned to the users in the Tomcat Users
      # data bag.
      #
      # @raise [TomcatCookbook::InvalidUserDataBagItem] if an invalid item was
      #   found in the Tomcat users data bag.
      #
      # @return [Array<String>]
      def roles
        users.map { |item| item['roles'] }.flatten.uniq
      end

      private

      def find_users
        users = if Chef::Config[:solo]
                  data_bag = Chef::DataBag.load(USERS_DATA_BAG)
                  data_bag.keys.map do |name|
            Chef::DataBagItem.load(USERS_DATA_BAG, name)
          end
                else
                  begin
                    items = Chef::Search::Query.new.search(USERS_DATA_BAG)[0]
                  rescue Net::HTTPServerException => e
                    raise TomcatUserDataBagNotFound if e.message.match(/404/)
                    raise e
                  end
                  decrypt_items(items)
                end

        users.each { |user| validate_user_item(user) }
        users
      end

      def validate_user_item(user)
        if user['id'].empty? || user['id'].nil? &&
            user['password'].empty? || user['password'].nil? &&
            user['roles'].nil? || !user['roles'].is_a?(Array)

          fail InvalidUserDataBagItem.new(user), 'Invalid User Databag Item'
        end
      end

      def decrypt_items(items)
        items.map do |item|
          EncryptedDataBagItem.new(item, encrypted_secret)
        end
      end

      def encrypted_secret
        @encrypted_secret ||= EncryptedDataBagItem.load_secret
      end
    end
  end
end
