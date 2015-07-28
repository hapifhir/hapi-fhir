apt Cookbook
============
[![Gitter](https://badges.gitter.im/Join Chat.svg)](https://gitter.im/chef-cookbooks/apt?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Cookbook Version](https://img.shields.io/cookbook/v/apt.svg)][cookbook]
[![Build Status](https://img.shields.io/travis/opscode-cookbooks/apt.svg)][travis]

[cookbook]: https://community.chef.io/cookbooks/apt
[travis]: https://travis-ci.org/opscode-cookbooks/apt

This cookbook includes recipes to execute apt-get update to ensure the local APT package cache is up to date. There are recipes for managing the apt-cacher-ng caching proxy and proxy clients. It also includes a LWRP for managing APT repositories in /etc/apt/sources.list.d as well as an LWRP for pinning packages via /etc/apt/preferences.d.


Requirements
------------
**Version 2.0.0+ of this cookbook requires Chef 11.0.0 or later**. If your Chef version is earlier than 11.0.0, use version 1.10.0 of this cookbook.

Version 1.8.2 to 1.10.0 of this cookbook requires **Chef 10.16.4** or later.

If your Chef version is earlier than 10.16.4, use version 1.7.0 of this cookbook.

### Platform
Please refer to the [TESTING file](TESTING.md) to see the currently (and passing) tested platforms. The release was tested on:

* Ubuntu 10.04
* Ubuntu 12.04
* Ubuntu 13.04
* Debian 7.1
* Debian 6.0 (have with manual testing)

May work with or without modification on other Debian derivatives.


-------
### default
This recipe manually updates the timestamp file used to only run `apt-get update` if the cache is more than one day old.

This recipe should appear first in the run list of Debian or Ubuntu nodes to ensure that the package cache is up to date before managing any `package` resources with Chef.

This recipe also sets up a local cache directory for preseeding packages.

**Including the default recipe on a node that does not support apt (such as Windows) results in a noop.**

### cacher-client
Configures the node to use the `apt-cacher-ng` server as a client.

#### Bypassing the cache
Occasionally you may come across repositories that do not play nicely when the node is using an `apt-cacher-ng` server. You can configure `cacher-client` to bypass the server and connect directly to the repository with the `cache_bypass` attribute.

To do this, you need to override the `cache_bypass` attribute with an array of repositories, with each array key as the repository URL and value as the protocol to use:

```json
{
    ...,
    'apt': {
        ...,
        'cache_bypass': {
            URL: PROTOCOL
        }
    }
}
```

For example, to prevent caching and directly connect to the repository at `download.oracle.com` via http:

```json
{
    'apt': {
        'cache_bypass': {
            'download.oracle.com': 'http'
        }
    }
}
```

### cacher-ng
Installs the `apt-cacher-ng` package and service so the system can provide APT caching. You can check the usage report at http://{hostname}:3142/acng-report.html.

If you wish to help the `cacher-ng` recipe seed itself, you must now explicitly include the `cacher-client` recipe in your run list **after** `cacher-ng` or you will block your ability to install any packages (ie. `apt-cacher-ng`).

### unattended-upgrades

Installs and configures the `unattended-upgrades` package to provide automatic package updates. This can be configured to upgrade all packages or to just install security updates by setting `['apt']['unattended_upgrades']['allowed_origins']`.

To pull just security updates, you'd set `allowed_origins` to something link `["Ubuntu trusty-security"]` (for Ubuntu trusty) or `["Debian wheezy-security"]` (for Debian wheezy). 


Attributes
----------

### General 
* `['apt']['compile_time_update']` - force the default recipe to run `apt-get update` at compile time.
* `['apt']['periodic_update_min_delay']` - minimum delay (in seconds) beetween two actual executions of `apt-get update` by the `execute[apt-get-update-periodic]` resource, default is '86400' (24 hours)

### Caching

* `['apt']['cacher_ipaddress']` - use a cacher server (or standard proxy server) not available via search
* `['apt']['cacher_interface]` - interface to connect to the cacher-ng service, no default.
* `['apt']['cacher_port']` - port for the cacher-ng service (either client or server), default is '3142'
* `['apt']['cacher_ssl_support']` - indicates whether the cacher supports upstream SSL servers, default is 'false'
* `['apt']['cacher_dir']` - directory used by cacher-ng service, default is '/var/cache/apt-cacher-ng'
* `['apt']['cacher-client']['restrict_environment']` - restrict your node to using the `apt-cacher-ng` server in your Environment, default is 'false'
* `['apt']['compiletime']` - force the `cacher-client` recipe to run before other recipes. It forces apt to use the proxy before other recipes run. Useful if your nodes have limited access to public apt repositories. This is overridden if the `cacher-ng` recipe is in your run list. Default is 'false'
* `['apt']['cache_bypass']` - array of URLs to bypass the cache. Accepts the URL and protocol to  fetch directly from the remote repository and not attempt to cache

### Unattended Upgrades

* `['apt']['unattended_upgrades']['enable']` - enables unattended upgrades, default is false
* `['apt']['unattended_upgrades']['update_package_lists']` — automatically update package list (`apt-get update`) daily, default is true
* `['apt']['unattended_upgrades']['allowed_origins']` — array of allowed apt origins from which to pull automatic upgrades, defaults to a guess at the system's main origin and should almost always be overridden
* `['apt']['unattended_upgrades']['package_blacklist']` — an array of package which should never be automatically upgraded, defaults to none
* `['apt']['unattended_upgrades']['auto_fix_interrupted_dpkg']` — attempts to repair dpkg state with `dpkg --force-confold --configure -a` if it exits uncleanly, defaults to false (contrary to the unattended-upgrades default)
* `['apt']['unattended_upgrades']['minimal_steps']` — Split the upgrade into the smallest possible chunks. This makes the upgrade a bit slower but it has the benefit that shutdown while a upgrade is running is possible (with a small delay). Defaults to false.
* `['apt']['unattended_upgrades']['install_on_shutdown']` — Install upgrades when the machine is shuting down instead of doing it in the background while the machine is running. This will (obviously) make shutdown slower. Defaults to false.
* `['apt']['unattended_upgrades']['mail']` — Send email to this address for problems or packages upgrades. Defaults to no email.
* `['apt']['unattended_upgrades']['mail_only_on_error']` — If set, email will only be set on upgrade errors. Otherwise, an email will be sent after each upgrade. Defaults to true.
* `['apt']['unattended_upgrades']['remove_unused_dependencies']` Do automatic removal of new unused dependencies after the upgrade. Defaults to false.
* `['apt']['unattended_upgrades']['automatic_reboot']` — Automatically reboots *without confirmation* if a restart is required after the upgrade. Defaults to false.
* `['apt']['unattended_upgrades']['dl_limit']` — Limits the bandwidth used by apt to download packages. Value given as an integer in kb/sec. Defaults to nil (no limit).

Libraries
---------
There is an `interface_ipaddress` method that returns the IP address for a particular host and interface, used by the `cacher-client` recipe. To enable it on the server use the `['apt']['cacher_interface']` attribute.

Resources/Providers
-------------------
### `apt_repository`
This LWRP provides an easy way to manage additional APT repositories. Adding a new repository will notify running the `execute[apt-get-update]` resource immediately.

#### Actions
- :add: creates a repository file and builds the repository listing (default)
- :remove: removes the repository file

#### Attribute Parameters
- repo_name: name attribute. The name of the channel to discover
- uri: the base of the Debian distribution
- distribution: this is usually your release's codename...ie something like `karmic`, `lucid` or `maverick`
- components: package groupings... when in doubt use `main`
- arch: constrain package to a particular arch like `i386`, `amd64` or even `armhf` or `powerpc`. Defaults to nil.
- trusted: treat all packages from this repository as authenticated regardless of signature
- deb_src: whether or not to add the repository as a source repo as well - value can be `true` or `false`, default `false`.
- keyserver: the GPG keyserver where the key for the repo should be retrieved
- key: if a `keyserver` is provided, this is assumed to be the fingerprint, otherwise it can be either the URI to the GPG key for the repo, or a cookbook_file.
- key_proxy: if set, pass the specified proxy via `http-proxy=` to GPG.
- cookbook: if key should be a cookbook_file, specify a cookbook where the key is located for files/default. Defaults to nil, so it will use the cookbook where the resource is used.

#### Examples

Add the Zenoss repo:

```ruby
apt_repository 'zenoss' do
  uri        'http://dev.zenoss.org/deb'
  components ['main', 'stable']
end
```

Add the Nginx PPA, autodetect the key and repository url:

```ruby
apt_repository 'nginx-php' do
  uri          'ppa:nginx/stable'
  distribution node['lsb']['codename']
end
```

Add the JuJu PPA, grab the key from the keyserver, and add source repo:

```ruby
apt_repository 'juju' do
  uri 'http://ppa.launchpad.net/juju/stable/ubuntu'
  components ['main']
  distribution 'trusty'
  key 'C8068B11'
  keyserver 'keyserver.ubuntu.com'
  action :add
  deb_src true
end
```

Add the Cloudera Repo of CDH4 packages for Ubuntu 12.04 on AMD64:

```ruby
apt_repository 'cloudera' do
  uri          'http://archive.cloudera.com/cdh4/ubuntu/precise/amd64/cdh'
  arch         'amd64'
  distribution 'precise-cdh4'
  components   ['contrib']
  key          'http://archive.cloudera.com/debian/archive.key'
end
```

Remove Zenoss repo:

```ruby
apt_repository 'zenoss' do
  action :remove
end
```

### `apt_preference`
This LWRP provides an easy way to pin packages in /etc/apt/preferences.d. Although apt-pinning is quite helpful from time to time please note that Debian does not encourage its use without thorough consideration.

Further information regarding apt-pinning is available via http://wiki.debian.org/AptPreferences.

#### Actions
- :add: creates a preferences file under /etc/apt/preferences.d
- :remove: Removes the file, therefore unpin the package

#### Attribute Parameters
- package_name: name attribute. The name of the package
- glob: Pin by glob() expression or regexp surrounded by /.
- pin: The package version/repository to pin
- pin_priority: The pinning priority aka "the highest package version wins"

#### Examples
Pin libmysqlclient16 to version 5.1.49-3:

```ruby
apt_preference 'libmysqlclient16' do
  pin          'version 5.1.49-3'
  pin_priority '700'
end
```

Unpin libmysqlclient16:

```ruby
apt_preference 'libmysqlclient16' do
  action :remove
end
```

Pin all packages from dotdeb.org:

```ruby
apt_preference 'dotdeb' do
  glob         '*'
  pin          'origin packages.dotdeb.org'
  pin_priority '700'
end
```


Usage
-----
Put `recipe[apt]` first in the run list. If you have other recipes that you want to use to configure how apt behaves, like new sources, notify the execute resource to run, e.g.:

```ruby
template '/etc/apt/sources.list.d/my_apt_sources.list' do
  notifies :run, 'execute[apt-get update]', :immediately
end
```

The above will run during execution phase since it is a normal template resource, and should appear before other package resources that need the sources in the template.

Put `recipe[apt::cacher-ng]` in the run_list for a server to provide APT caching and add `recipe[apt::cacher-client]` on the rest of the Debian-based nodes to take advantage of the caching server.

If you want to cleanup unused packages, there is also the `apt-get autoclean` and `apt-get autoremove` resources provided for automated cleanup.


License & Authors
-----------------
- Author:: Joshua Timberman (joshua@chef.io)
- Author:: Matt Ray (matt@chef.io)
- Author:: Seth Chisamore (schisamo@chef.io)

```text
Copyright 2009-2013, Chef Software, Inc.

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
