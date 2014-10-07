default['yum']['mysql57-community']['repositoryid'] = 'mysql57-community'
default['yum']['mysql57-community']['gpgkey'] = 'https://raw.githubusercontent.com/someara/yum-mysql-community/master/files/default/mysql_pubkey.asc'
default['yum']['mysql57-community']['description'] = 'MySQL 5.7 Community Server'
default['yum']['mysql57-community']['failovermethod'] = 'priority'
default['yum']['mysql57-community']['gpgcheck'] = true
default['yum']['mysql57-community']['enabled'] = true

case node['platform_family']
when 'rhel'
  case node['platform']
  when 'amazon'
    case node['platform_version'].to_i
    when 2013
      default['yum']['mysql57-community']['baseurl'] = 'http://repo.mysql.com/yum/mysql-5.7-community/el/6/$basearch/'
    when 2014
      default['yum']['mysql57-community']['baseurl'] = 'http://repo.mysql.com/yum/mysql-5.7-community/el/6/$basearch/'
    end
  when 'redhat'
    case node['platform_version'].to_i
    when 5
      # Real Redhat identifies $releasever as 5Server and 6Server
      default['yum']['mysql57-community']['baseurl'] = 'http://repo.mysql.com/yum/mysql-5.7-community/el/5/$basearch/'
    when 6
      default['yum']['mysql57-community']['baseurl'] = 'http://repo.mysql.com/yum/mysql-5.7-community/el/6/$basearch/'
    when 7
      default['yum']['mysql57-community']['baseurl'] = 'http://repo.mysql.com/yum/mysql-5.7-community/el/7/$basearch/'
    end
  else # other rhel
    default['yum']['mysql57-community']['baseurl'] = 'http://repo.mysql.com/yum/mysql-5.7-community/el/$releasever/$basearch/'
  end
when 'fedora'
  default['yum']['mysql57-community']['baseurl'] = 'http://repo.mysql.com/yum/mysql-5.7-community/fc/$releasever/$basearch/'
end
