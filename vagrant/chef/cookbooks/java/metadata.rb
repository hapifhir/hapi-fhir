name              "java"
maintainer        "Agile Orbit"
maintainer_email  "info@agileorbit.com"
license           "Apache 2.0"
description       "Installs Java runtime."
long_description  IO.read(File.join(File.dirname(__FILE__), 'README.md'))
version           "1.28.0"

recipe "java::default", "Installs Java runtime"
recipe "java::default_java_symlink", "Updates /usr/lib/jvm/default-java"
recipe "java::ibm", "Installs the JDK for IBM"
recipe "java::ibm_tar", "Installs the JDK for IBM from a tarball"
recipe "java::openjdk", "Installs the OpenJDK flavor of Java"
recipe "java::oracle", "Installs the Oracle flavor of Java"
recipe "java::oracle_i386", "Installs the 32-bit jvm without setting it as the default"
recipe "java::oracle_rpm", "Installs the Oracle RPM flavor of Java"
recipe "java::purge_packages", "Purges old Sun JDK packages"
recipe "java::set_attributes_from_version", "Sets various attributes that depend on jdk_version"
recipe "java::set_java_home", "Sets the JAVA_HOME environment variable"
recipe "java::windows", "Installs the JDK on Windows"

%w{
    debian
    ubuntu
    centos
    redhat
    scientific
    fedora
    amazon
    arch
    oracle
    freebsd
    windows
    suse
    xenserver
    smartos
}.each do |os|
  supports os
end

suggests "windows"
suggests "aws"
