## Description
This project has been built with hapi-fhir-jpaserver-example as a base. It has been made more dynamic by replacing `web.xml` with `ca.uhn.fhir.jpa.demo.WebInitializer` class which extends 
Spring `org.springframework.web.WebApplicationInitializer` class and loads application contexts in a dynamic manner, so that based on environment and/or property variables it can be started either as dstu2 or dstu3 version of HAPI-FHIR JPA Server. Some of the classes have been also refactored to make them more generic.

#### Environment variables
There are number of environment variables that will control the behavior of the application at run time (such as start as dstu2 or dstu3 version, whether 
database schema gets recreated or no, database url, etc..). They can also be defined in Property files, see section below. These are environment variables that can be set before application starts:
  * `DB_URL` - database url in a standard jdbc url format, specific to a database of your choosing. For example for Postgres it will be: `jdbc:postgresql://localhost:5432/<databaseName>?user=<username>&password=<password>`. So far support has been added for MySQL, derby and Postgres databases.
  * `DATABASE_URL` - if you deploy your server to HEROKU and create a Postgres database, its URL will be exposed through `DATABASE_URL` environment variable set by HEROKU. 
     If `DATABASE_URL` is present it will overwrite `DB_URL` and its value will be used as jdbc url. This implementations assumes that Heroku will be setup with Postgres database, so current implementation handles postgres `DATABASE_URL` that gets set in this format: `postgres://<username>:<password>@<hostname>:5432/<databaseName>`. We convert it into standard jdbc format: `jdbc:postgresql://localhost:5432/<databaseName>?user=<username>&password=<password>`
  * `SCHEMA_NAME` - used only if `DATABASE_URL` is set, which is expected to be Postgres database url set by HEROKU. If it's set 
     `currentSchema` parameter will be added to the jdbc url, e.g.:  
     ```jdbc:postgresql://localhost:5432/<databaseName>?user=<username>&password=<password>&currentScema=<schemaName>```. 
     Note that schema has to be created beforehand and user should have the right permissions to create tables.
  * `STU_VERSION` -  can be set to `dstu2` or `dstu3`. If not set by default `dstu3` will be used. Corresponding classes will get dynamically loaded at a server startup.    
  * `ENV` - environment this server will run in, and based on which corresponding property files will be loaded. 
    
    It can be one of those values: `local, dev, stg, prod`. Based on the value one of the property files will be loaded:
   ```resources/config/<STU_VERSION>/app_<ENV>.properties```. 
   
    So for example if `ENV=local` and `STU_VERSION=dstu3` this file will be loaded: 
    
     ```resources/config/dstu3/app_local.properties```
  * `HIBERNATE_CREATE` - can be set to `true` or `false`. If set to `true` database schema will be dropped and recreated again upon application startup. If set
     to `false` hibernate will run with `validate` as a schema setting.
 
#### Property files
There are number of property files created for different environments: `local, dev, stg, prod`. So if `ENV` environment variable is set
to one of those values corresponding property file will be loaded at a run time, by default `local` files will be loaded. Property files are located at:
`src/main/resources/config/dstu2` and `src/main/resources/config/dstu3s` directories. These are the files:
```
   app_local.properties
   app_dev.properties
   app_stg.properties
   app_prod.properties
   immutable.properties - DO NOT modify any of the properties defined in that file.
```  
Any of the Environment variables can be defined in one of the `app_<ENV>.properties` property files. If a property is also defined as Environment variable it will overwrite value defined in property file. Properties defined in `immutable.properties` should not be changed, those are servlet/Spring mappings and names of classes that will be loaded at a run time and are specific to dstu version being used. 

## Running hapi-fhir-jpaserver-dynamic with a webapp-runner
You can run the web application with webapp-runner and pass environment variables to it.

Here is a sample command to run the webapp runner which will start
dynamic HAPI-FHIR server with version dstu3, postgres database and hibernate schema being dropped and re-created.

Note optional command to unset DATABASE_URL, so that only DB_URL is used locally. Also make sure to replace placeholder parameters `<databasename>`, `<username>` and `<password>` with actual values.

```
mvn clean install

unset DATABASE_URL

java $JAVA_OPTS -DSTU_VERSION=dstu3 -DHIBERNATE_CREATE=true -DDB_URL='jdbc:postgresql://localhost:5432/<databaseName>?user=<username>&password=<password>' -DENV=local -jar target/dependency/webapp-runner.jar target/*.war
```
You should be able to access HAPI_FHIR server at: http://localhost:8080/  .

If you'd like to open a debugging port run this command and attach remote debugger in IDE of your choice to port 5000. Again make sure to replace placeholder parameters with actual values before you run the command.

```
java $JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=5000,suspend=n -DSTU_VERSION=dstu3 -DHIBERNATE_CREATE=false -DDB_URL='jdbc:postgresql://localhost:5432/<databaseName>?user=<username>&password=<password>' -DENV=local -jar target/dependency/webapp-runner.jar target/*.war
```

## Running hapi-fhir-jpaserver-dynamic in Tomcat from IntelliJ

Install Tomcat.

Make sure you have Tomcat set up in IntelliJ.

- File->Settings->Build, Execution, Deployment->Application Servers
- Click +
- Select "Tomcat Server"
- Enter the path to your tomcat deployment for both Tomcat Home (IntelliJ will fill in base directory for you)

Add a Run Configuration for running hapi-fhir-jpaserver-dynamic under Tomcat

- Run->Edit Configurations
- Click the green +
- Select Tomcat Server, Local
- Change the name to whatever you wish
- Uncheck the "After launch" checkbox
- On the "Deployment" tab, click the green +
- Select "Artifact"
- Select "hapi-fhir-jpaserver-dynamic:war" 
- In "Application context" type /hapi

Run the configuration.

- You should now have an "Application Servers" in the list of windows at the bottom.
- Click it.
- Select your server, and click the green triangle (or the bug if you want to debug)
- Wait for the console output to stop

Point your browser (or fiddler, or what have you) to `http://localhost:8080/fhir/base/Patient`

You should get an empty bundle back.


## Running hapi-fhir-jpaserver-dynamic in a Docker container

Execute the `build-docker-image.sh` script to build the docker image. 

Use this command to start the container: 
  `docker run -d --name hapi-fhir-jpaserver-dynamic -p 8080:8080 hapi-fhir/hapi-fhir-jpaserver-dynamic`

Note: with this command data is persisted across container restarts, but not after removal of the container. Use a docker volume mapping on /var/lib/jetty/target to achieve this.

There is also Dockerfile.tomcat which contains docker commands to run hapi-fhir-jpaserver-dynamic within tomcat. Rename Dockerfile.tomcat to Dockerfile if you would rather use tomcat as your application container.
