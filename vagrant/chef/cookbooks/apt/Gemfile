source 'https://rubygems.org'

group :lint do
  gem 'foodcritic', '~> 3.0'
  gem 'rubocop', '~> 0.23'
  gem 'rainbow', '< 2.0'
end

group :unit do
  gem 'berkshelf',  '~> 3.0.0.beta6'
  gem 'chefspec',   '~> 4.0'
end

group :kitchen_common do
  gem 'test-kitchen', '~> 1.2'
end

group :kitchen_vagrant do
  gem 'kitchen-vagrant', '~> 0.11'
end

group :kitchen_cloud do
  gem 'kitchen-digitalocean'
  gem 'kitchen-ec2'
end

group :development do
  gem 'ruby_gntp'
  gem 'growl'
  gem 'rb-fsevent'
  gem 'guard', '~> 2.4'
  gem 'guard-kitchen'
  gem 'guard-foodcritic'
  gem 'guard-rspec'
  gem 'guard-rubocop'
  gem 'rake'
end
