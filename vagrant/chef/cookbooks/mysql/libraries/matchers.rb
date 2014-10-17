if defined?(ChefSpec)
  def create_mysql_client(resource_name)
    ChefSpec::Matchers::ResourceMatcher.new(:mysql_client, :create, resource_name)
  end

  def delete_mysql_client(resource_name)
    ChefSpec::Matchers::ResourceMatcher.new(:mysql_client, :delete, resource_name)
  end

  def create_mysql_service(resource_name)
    ChefSpec::Matchers::ResourceMatcher.new(:mysql_service, :create, resource_name)
  end

  def enable_mysql_service(resource_name)
    ChefSpec::Matchers::ResourceMatcher.new(:mysql_service, :enable, resource_name)
  end
end
