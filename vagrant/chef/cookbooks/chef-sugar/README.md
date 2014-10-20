Chef::Sugar
================
[![Gem Version](http://img.shields.io/gem/v/chef-sugar.svg)][gem]
[![Build Status](http://img.shields.io/travis/sethvargo/chef-sugar.svg)][travis]
[![Dependency Status](http://img.shields.io/gemnasium/sethvargo/chef-sugar.svg)][gemnasium]
[![Code Climate](http://img.shields.io/codeclimate/github/sethvargo/chef-sugar.svg)][codeclimate]
[![Gittip](http://img.shields.io/gittip/sethvargo.svg)][gittip]

[gem]: https://rubygems.org/gems/chef-sugar
[travis]: http://travis-ci.org/sethvargo/chef-suguar
[gemnasium]: https://gemnasium.com/sethvargo/chef-sugar
[codeclimate]: https://codeclimate.com/github/sethvargo/chef-sugar
[gittip]: https://www.gittip.com/sethvargo

Chef Sugar is a Gem & Chef Recipe that includes series of helpful sugar of the Chef core and other resources to make a cleaner, more lean recipe DSL, enforce DRY principles, and make writing Chef recipes an awesome experience!


Installation
------------
If you want to develop/hack on chef-sugar, please see the Contributing.md.

If you are using Berkshelf, add `chef-sugar` to your `Berksfile`:

```ruby
cookbook 'chef-sugar'
```

Otherwise, you can use `knife` or download the tarball directly from the community site:

```ruby
knife cookbook site install chef-sugar
```


Usage
-----
In order to use Chef Sugar in your Chef Recipes, you'll first need to include it:

```ruby
include_recipe 'chef-sugar::default'
```

Alternatively you can put it in a base role or recipe and it will be included subsequently.

Requiring the Chef Sugar Gem will automatically extend the Recipe DSL, `Chef::Resource`, and `Chef::Provider` with helpful convenience methods.

### Module Method
If you are working outside of the Recipe DSL, you can use the module methods instead of the Recipe DSL. In general, the module methods have the same name as their Recipe-DSL counterparts, but require the node object as a parameter. For example:

In a Recipe:

```ruby
# cookbook/recipes/default.rb
do_something if windows?
```

In a Library as a singleton:

```ruby
# cookbook/libraries/default.rb
def only_on_windows(&block)
  yield if Chef::Sugar::PlatformFamily.windows?(@node)
end
```

In a Library as a Mixin:

```ruby
# cookbook/libraries/default.rb
include Chef::Sugar::PlatformFamily

def only_on_windows(&block)
  yield if windows?(@node)
end
```


API
---
**Note:** For the most extensive API documentation, please see the YARD documentation.

### Architecture
**Note:** Some of the architecture commands begin with an underscore (`_`) because Ruby does not permit methods to start with a numeric.

- `_64_bit?`
- `_32_bit?`

#### Examples
```ruby
execute 'build[my binary]' do
  command '...'
  not_if  { _64_bit? }
end
```

### Cloud
- `azure?`
- `cloud?`
- `ec2?`
- `eucalyptus?`
- `gce?`
- `linode?`
- `openstack?`
- `cloudstack?`
- `rackspace?`

#### Examples
```ruby
template '/tmp/config' do
  variables(
    # See also: best_ip_for
    ipaddress: cloud? ? node['local_ipv4'] : node['public_ipv4']
  )
end
```

### Core Extensions
**Note:** Core extensions are **not** included by default. You must require the `chef/sugar/core_extensions` module manually to gain access to these APIs:

```ruby
require 'chef/sugar/core_extensions'
```

- `String#satisfies?`
- `String#satisfied_by?`
- `Array#satisfied_by?`
- `Object#blank?`

#### Examples
```ruby
# Checking version constraints
'1.0.0'.satisfies?('~> 1.0') #=> true
'~> 1.0'.satisfied_by?('1.0') #=> true
```

```ruby
# Check for an object's presence
''.blank? #=> true
['hello'].blank? #=> false
```

### Data Bag
- `encrypted_data_bag_item` - a handy DSL method for loading encrypted data bag items the same way you load a regular data bag item; this requires `Chef::Config[:encrypted_data_bag_secret]` is set!
- `encrypted_data_bag_item_for_environment` - find the data bag entry for the current node's Chef environment.

#### Examples
```ruby
encrypted_data_bag_item('accounts', 'hipchat')
```

```ruby
encrypted_data_bag_item_for_environment('accounts', 'github')
```

### Attributes
Chef Sugar adds more Chef-like DSL to attribute definitions. Instead of using the Ruby hash syntax, you can define attributes using nested namespaces. This DSL may be more friendly to non-Ruby developers. It can safely be mixed-and-matched with the standard syntax.

```ruby
# This is functionally the same as default['apache2']['config']['root'] = '/var/www'
namespace 'apache2' do
  namespace 'config' do
    root '/var/www'
  end
end
```

```ruby
# Specify multiple keys instead of nesting namespaces
namespace 'apache2', 'config' do
  root '/var/www'
end
```

```ruby
# Specify different nested precedence levels
namespace 'apache2', precedence: normal do
  namespace 'config', precedence: override do
    root '/var/www' #=> override['apache2']['config']['root'] = '/var/www'
  end
end
```

### Constraints
- `constraints` - create a new constraint (or requirement) that can be used to test version validations.
- `chef_version` - (DSL only) a wrapper for `version(Chef::VERSION)`
- `version` - create a new version that can be used to test constraint validation.

#### Examples
```ruby
# Check if a version is satisfied by a constraint
version('1.2.3').satisfies?('~> 1.2.0')
```

```ruby
# Check if a constraint is satisfied by a version
constraint('~> 1.2.0').satisfied_by?('1.2.3')
```

```ruby
# Support multiple constraints
version('1.2.3').satisfies?('> 1.2', '< 2.0')
constraint('> 1.2', '< 2.0').satisfied_by?('1.2.3')
```

```ruby
# Only perform an operation if Chef is at a certain version
package 'apache2' do
  not_if { chef_version.satisfies?('~> 11.0') } # Ignore Chef 11
end
```

### Kernel
- `require_chef_gem` - "safely" require a gem. Loading a gem with Chef is sometimes difficult and confusing. The errors that Chef produces are also sometimes not very intuitive. In the event you require a gem to exist on the system, you can use `require_chef_gem`, which will attempt to require the gem and then produce helpful output if the gem is not installed:

        Chef could not load the gem `#{name}'! You may need to install the gem
        manually with `gem install #{name}', or include a recipe before you can
        use this resource. Please consult the documentation for this cookbook
        for proper usage.

#### Examples
```ruby
# LWRP
require_chef_gem 'pry'
```

```ruby
class Chef
  class Provider
    class MyProvider > Provider
      require_chef_gem 'pry'
    end
  end
end
```

### IP
- `best_ip_for` - determine the best IP address for the given "other" node, preferring local IP addresses over public ones.

#### Examples
```ruby
redis = search('node', 'role:redis').first

template '/tmp/config' do
  variables(
    ipaddress: best_ip_for(redis)
  )
end
```

### Node
- `deep_fetch` - safely fetch a nested attribute.
- `deep_fetch!` - fetch a nested attribute, raising a more semantic error if the key does not exist.
- `in?` - determine if the node is in the given Chef environment.

#### Examples
```ruby
credentials = if in?('production')
                Chef::EncryptedDataBag.new('...')
              else
                data_bag('...')
              end
```

```ruby
node.deep_fetch('apache2', 'config', 'root') => node['apache2']['config']['root']
```

### Platform
- `amazon_linux?`
- `centos?`
- `linux_mint?`
- `oracle_linux?`
- `redhat_enterprise_linux?`
- `scientific_linux?`
- `ubuntu?`
- `solaris2?`
- `aix?`
- `smartos?`
- `omnios?`

There are also a series of dynamically defined matchers that map named operating system release versions and comparison operators in the form "#{platform}\_#{operator}\_#{name}?". For example:

- `debian_after_squeeze?`
- `linuxmint_after_or_at_olivia?`
- `mac_os_x_lion?`
- `ubuntu_before_lucid?`
- `ubuntu_before_or_at_maverick?`

To get a full list, run the following in IRB:

```ruby
require 'chef/sugar'
puts Chef::Sugar::Platform.instance_methods
```

#### Examples
```ruby
if ubuntu?
  execute 'apt-get update'
end
```

### Platform Family
- `arch_linux?`
- `debian?`
- `fedora?`
- `freebsd?`
- `gentoo?`
- `linux?`
- `mac_os_x?`
- `openbsd?`
- `rhel?`
- `slackware?`
- `suse?`
- `windows?`

#### Examples
```ruby
node['attribute'] = if windows?
                      'C:\Foo\BarDrive'
                    else
                      '/foo/bar_drive'
                    end
```

### Ruby
**Note:** The applies to the Ruby found at `node['languages']['ruby']`.

- `ruby_20?`
- `ruby_19?`

#### Examples
```ruby
log 'This has been known to fail on Ruby 2.0' if ruby_20?
```

### Run Context
- `includes_recipe?` - determines if the current run context includes the recipe

```ruby
if includes_recipe?('apache2::default')
  apache_module 'my_module' do
    # ...
  end
end
```

### Shell
- `which`
- `dev_null`
- `installed?`
- `installed_at_version?`
- `version_for`

#### Examples
```ruby
log "Using `mongo` at `#{which('mongo')}`"

if installed?('apt')
  execute 'apt-get update'
end

execute 'install[thing]' do
  command "... 2>&1 #{dev_null}"
  not_if  { installed_at_version?('thing', node['thing']['version']) }
end

log "Skipping git install, version is at #{version_for('mongo', '-v')}"
```

### Vagrant
- `vagrant?`

#### Examples
```ruby
http_request 'http://...' do
  not_if { vagrant? }
end
```

### Virtualization
- `lxc?`
- `vmware?`

#### Examples
```ruby
service 'ntpd' do
  action [:enable, :start]
  not_if { lxc? }
end
```

### Filters
- `compile_time` - accepts a block of resources to run at compile time
- `before` - insert resource in the collection before the given resource
- `after` - insert resource in the collection after the given resource

#### Examples
```ruby
compile_time do
  package 'apache2'
end

# This is equivalent to
package 'apache2' do
  action :nothing
end.run_action(:install)
```

```ruby
before 'service[apache2]' do
  log 'I am before the apache 2 service fires!'
end
```

```ruby
after 'service[apache2]' do
  log 'I am after the apache 2 service fires!'
end
```


License & Authors
-----------------
- Author: Seth Vargo (sethvargo@gmail.com)

```text
Copyright 2013-2014 Seth Vargo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
