Creating your own demo server with Vagrant
========
This source code repository includes configuration files to materialize an _entire_ server implementation off all project build artifacts in a single virtual machine (VM) image from scratch, allowing you to quickly bootstrap your own local copy of the project. The server will be completely encapsulated within the created VM image. The process _should_ run on OSX, Linux and Windows, but YMMV. The built-in settings support creation of a *VirtualBox*-based image on Ubuntu Linux, though with tuning of the base image you should be able to create images suitable for other hypervisors and cloud-based IaaS providers such as VMware and Amazon Web Services (AWS), respectively.

Dependencies
----

Prior to running, please ensure you have all .war files built, and the following installed and functioning propertly.

 * All normal Java development dependencies. (Java SDK and Maven 3, specifically.)
 * VirtualBox
 * Vagrant


Creating Your VM
----

    cd hapi-fhir-root/
    mvn install # Creates web application .war files. Make sure they're built before proceeding!
    cd vagrant
    vagrant up # Will take a few minutes to boot up.

Your new server environment should now be running in a headless virtual machine on your local computer. The following step are performed automatically for you within the VM sandbox environment:

 * A complete Ubuntu 14.04 Server VM is launched in headless mode, bridged to whatever host network interface you've selected.
 * An IPv4 address is assigned via DHCP.
 * MySQL Server (Community Edition) is installed from the official 10gen repository. (See the [Vagrantfile](https://github.com/preston/hapi-fhir/blob/master/vagrant/Vagrantfile) for the default root password.)
 * Oracle Java 8 is installed.
 * Tomcat 7 is installed and configured as a system service.
 * All compiled *.war applications are deployed automatically and started.
 * A "fhir" user is added to tomcat-users.xml. See [fhir.json](https://github.com/preston/hapi-fhir/blob/master/vagrant/chef/data_bags/tomcat_users/fhir.json) for the default password.

Tomcat will now be running on the VM on port 8080 with the management GUI available. For example, you can now visit:

 * *Tomcat Manager*: assigned_ip:8080/manager/html
 * *HAPI FHIR* JPA Server:  assigned_ip:8080/hapi-fhir-jpaserver/ 

Screenshots
----
![Tomcat Manager](https://raw.githubusercontent.com/preston/hapi-fhir/master/vagrant/screenshots/tomcat.png)

![Demo Server](https://raw.githubusercontent.com/preston/hapi-fhir/master/vagrant/screenshots/hapi-fhir-jpaserver.png)

Advanced Configuration
----
The Vagrant documentation is the best place to start, but a few more commands of note are:

    vagrant ssh # Command-line access to the VM.
    vagrant destoy # Shuts down and completely destroys the VM.


Credits
----
Vagrant and Chef configuration by Preston Lee <preston.lee@prestonlee.com>
