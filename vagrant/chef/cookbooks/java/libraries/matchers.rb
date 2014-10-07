if defined?(ChefSpec)
  def set_java_alternatives(resource_name)
    ChefSpec::Matchers::ResourceMatcher.new(:java_alternatives, :set, resource_name)
  end
end