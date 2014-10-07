Chef Sugar Changelog
=========================
This file is used to list changes made in each version of the chef-sugar cookbook and gem.

v2.3.0 (2014-09-24)
-------------------
### Improvements
- Add `vmware?` matcher
- Allow the attribute DSL to access parent attributes

### Bug Fixes
- Return `true` or `false` from all Boolean methods (instead of `nil` or truthy values)

v2.2.0 (2014-08-20)
-------------------
### Improvements
- Add `smartos?` matcher
- Add `omnios?` matcher

v2.1.0 (2014-06-26)
-------------------
### Improvements
- Add `solaris2?` matcher
- Add `aix?` matcher
- Add 'lxc?' matcher

### Bug Fixes
- Fix a bug in namespace memoization during attribute initialization

v2.0.0 (2014-06-16)
-------------------
### Breaking
- Remove `not_linux?` method
- Remove `not_windows?` method

### Improvements
- Miscellaneous spelling fixes
- Update a failing unit test for `installed?`
- Add Mac OS X to the list of platforms (Yosemite)
- Upgrade to RSpec 3
- Fix `which` (and `installed?` and `installed_at_version?`) when given an absolute path
- Fix `linux?` check to only return true on real linuxes

v1.3.0 (2014-05-05)
-------------------
- Check both `$stdout` and `$stderr` in `version_for`
- Add additional platform versions
- Make `includes_recipe?` a top-level API (instead of just Node)
- Match on the highest version number instead of direct equality checking on platform versions
- Define `Object#blank?` as a core extension
- Define `String#flush` as a core extension
- Remove Stove

v1.2.6 (2014-03-16)
-------------------
- Fix a bug in `vagrant?` returning false on newer Vagrant versions
- Remove Coveralls

v1.2.4 (2014-03-13)
-------------------
- See (1.2.2), but I botched the release

v1.2.2 (2014-03-13)
-------------------
- Fix a critical bug with `encrypted_data_bag_item` using the wrong key

v1.2.0 (2014-03-09)
-------------------
- Add `namespace` functionality for specifying attributes in a DSL
- Add constraints helpers for comparing version strings
- Add `require_chef_gem` to safely require and degrade if a gem is not installed
- Add `deep_fetch` and `deep_fetch!` to fetch deeply nested keys
- Accept an optional secret key in `encrypted_data_bag_item` helper and raise a helpful error if one is not set (NOTE: this changes the airity of the method, but it's backward-compatible because Ruby is magic)
- Add Stove for releasing
- Updated copyrights for 2014

v1.1.0 (2013-12-10)
-------------------
- Add `cloudstack?` helper
- Add data bag helpers
- Remove foodcritic checks
- Upgrade development gem versions
- Randomize spec order

v1.0.1 (2013-10-15)
-------------------
- Add development recipe
- Add `compile_time`, `before`, and `after` filters

v1.0.0 (2013-10-15)
-------------------
- First public release
