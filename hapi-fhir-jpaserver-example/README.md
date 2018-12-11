# Unsupported

This [hapi-fhir-jpaserver-example](https://github.com/jamesagnew/hapi-fhir/tree/master/hapi-fhir-jpaserver-example) project is no longer supported.


## Supported JPA Example

The supported HAPI-FHIR JPA example is available in the [hapi-fhir-jpaserver-starter](https://github.com/hapifhir/hapi-fhir-jpaserver-starter)
project within the [hapifhir](https://github.com/hapifhir) GitHub Organization.

## Previous Documentation

Below is the original documentation for this project.  Note that this documentation is no longer being updated.

#### Running hapi-fhir-jpaserver-example in Tomcat from IntelliJ

Install Tomcat.

Make sure you have Tomcat set up in IntelliJ.

- File->Settings->Build, Execution, Deployment->Application Servers
- Click +
- Select "Tomcat Server"
- Enter the path to your tomcat deployment for both Tomcat Home (IntelliJ will fill in base directory for you)

Add a Run Configuration for running hapi-fhir-jpaserver-example under Tomcat

- Run->Edit Configurations
- Click the green +
- Select Tomcat Server, Local
- Change the name to whatever you wish
- Uncheck the "After launch" checkbox
- On the "Deployment" tab, click the green +
- Select "Artifact"
- Select "hapi-fhir-jpaserver-example:war" 
- In "Application context" type /hapi

Run the configuration.

- You should now have an "Application Servers" in the list of windows at the bottom.
- Click it.
- Select your server, and click the green triangle (or the bug if you want to debug)
- Wait for the console output to stop

Point your browser (or fiddler, or what have you) to `http://localhost:8080/hapi/baseDstu3/Patient`

You should get an empty bundle back.


#### Running hapi-fhir-jpaserver-example in a Docker container

Execute the `build-docker-image.sh` script to build the docker image. 

Use this command to start the container: 
  `docker run -d --name hapi-fhir-jpaserver-example -p 8080:8080 hapi-fhir/hapi-fhir-jpaserver-example`

Note: with this command data is persisted across container restarts, but not after removal of the container. Use a docker volume mapping on /var/lib/jetty/target to achieve this.

After the docker container initial startup, point your browser (or fiddler, or what have you) to `http://localhost:8080/baseDstu3/Patient`

You should get an empty bundle back.
#### Using ElasticSearch as the search engine instead of the default Apache Lucene
1. Install ElasticSearch server and the phonetic plugin
    * Download ElasticSearch from https://www.elastic.co/downloads/elasticsearch
    * ```cd {your elasticsearch directory}```
    * ```bin/plugin install analysis-phonetic```
    * start ElasticSearch server: ```./bin/elasticsearch```
2. Replace configuration in web.xml
    * replace the configuration class ```ca.uhn.fhir.jpa.demo.FhirServerConfig``` in web.xml by ```ca.uhn.fhir.jpa.demo.elasticsearch.FhirServerConfig```
3. Start server by runing: ```mvn jetty:run```
4. Limitations:
    * Hibernate search are not compatible with all ElasticSearch version. If you are using Hibernate search: 5.6 or 5.7, the compatible ElasticSearch version is 2.0 - 2.4. If you are using Hibernate search: 5.8 or 5.9, the compatible ElasticSearch version is
    2.0 - 5.6.
    * Please check all the limitations in the reference documentation: https://docs.jboss.org/hibernate/search/5.7/reference/en-US/html_single/#elasticsearch-limitations before use the integration.
