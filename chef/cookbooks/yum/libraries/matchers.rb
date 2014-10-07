# Matchers for chefspec 3

if defined?(ChefSpec)
  def create_yum_repository(resource_name)
    ChefSpec::Matchers::ResourceMatcher.new(:yum_repository, :create, resource_name)
  end

  def add_yum_repository(resource_name)
    ChefSpec::Matchers::ResourceMatcher.new(:yum_repository, :add, resource_name)
  end

  def delete_yum_repository(resource_name)
    ChefSpec::Matchers::ResourceMatcher.new(:yum_repository, :delete, resource_name)
  end

  def remove_yum_repository(resource_name)
    ChefSpec::Matchers::ResourceMatcher.new(:yum_repository, :remove, resource_name)
  end

  def create_yum_globalconfig(resource_name)
    ChefSpec::Matchers::ResourceMatcher.new(:yum_globalconfig, :create, resource_name)
  end

  def delete_yum_globalconfig(resource_name)
    ChefSpec::Matchers::ResourceMatcher.new(:yum_globalconfig, :delete, resource_name)
  end
end
