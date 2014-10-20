name             'openssl'
maintainer       'Opscode, Inc.'
maintainer_email 'cookbooks@opscode.com'
license          'Apache 2.0'
description      'Provides a library with a method for generating secure random passwords.'
long_description IO.read(File.join(File.dirname(__FILE__), 'README.md'))
version          '2.0.0'

recipe 'openssl', 'Empty, this cookbook provides a library, see README.md'

# chef-sugar greatly reduces the amount of code required to check
# conditionals for the attributes used in the upgrader recipe.
depends 'chef-sugar'
