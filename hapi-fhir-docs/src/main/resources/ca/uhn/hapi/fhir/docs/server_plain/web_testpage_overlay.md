# Web Testpage Overlay

HAPI FHIR includes a web UI that can be used to test your server implementation. This UI is the same UI used on the [http://hapi.fhir.org](http://hapi.fhir.org) public HAPI FHIR test server.

The Web Testpage Overlay is a [Maven WAR Overlay](http://maven.apache.org/plugins/maven-war-plugin/overlays.html) project,
meaning that you create your own WAR project (which you would likely be doing anyway
to create your server) and the overlay drops a number of files into your project. You can then selectively customize the system by replacing the built-in overlay files.

# Adding the Overlay

These instructions assume that you have an existing web project which uses Maven to build. The POM.xml should have a "packaging" type of "war".

Adding the overlay to your project is relatively simple. First, add the "hapi-fhir-testpage-overlay" dependency to the dependencies section of your POM.xml file.

```xml
<dependencies>
    <!-- ... other dependencies ... -->
    <dependency>
        <groupId>ca.uhn.hapi.fhir</groupId>
        <artifactId>hapi-fhir-testpage-overlay</artifactId>
        <version>${project.version}</version>
        <type>war</type>
        <scope>provided</scope>		
    </dependency>
    <dependency>
        <groupId>ca.uhn.hapi.fhir</groupId>
        <artifactId>hapi-fhir-testpage-overlay</artifactId>
        <version>${project.version}</version>
        <classifier>classes</classifier>
        <scope>provided</scope>		
    </dependency>
</dependencies>
```

Then, add the following WAR plugin to the plugins section of your POM.xml

```xml
<build>
    <plugins>
        <!-- ... other plugins ... -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-war-plugin</artifactId>
            <configuration>
                <overlays>
                    <overlay>
                        <groupId>ca.uhn.hapi.fhir</groupId>
                        <artifactId>hapi-fhir-testpage-overlay</artifactId>
                    </overlay>
                </overlays>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Then, create a Java source file called `FhirTesterConfig.java` and copy the following contents:

<macro name="snippet">
<param name="file" value="restful-server-example/src/main/java/ca/uhn/example/config/FhirTesterConfig.java" />
</macro>

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/FhirTesterConfig.java|file}}
```

Note that the URL in the file above must be customized to point to the FHIR endpoint your server will be deployed to. For example, if you are naming your project "myfhir-1.0.war" and your endpoint in the WAR is deployed to "/fhirbase/*" then you should put a URL similar to <code>http://localhost:8080/myfhir-1.0/fhirbase</code>

Next, create the following directory in your project if it doesn't already exist:

`src/main/webapp/WEB-INF</code>`

In this directory you should open your `web.xml` file, or create it if it doesn't exist. This file is required in order to deploy to a servlet container and you should create it if it does not already exist. Place the following contents in that file, adjusting the package on the <code>FhirTesterConfig</code> to match the actual package in which you placed this file.

```xml
<servlet>
    <servlet-name>spring</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextClass</param-name>
        <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
    </init-param>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>ca.uhn.example.config.FhirTesterConfig</param-value>
    </init-param>
    <load-on-startup>2</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>spring</servlet-name>
    <url-pattern>/tester/*</url-pattern>
</servlet-mapping>
```

# Customizing the Overlay

The most important customization required is to set the FHIR server base URL in the Java configuration file described above.

Beyond this, the entire tester application is built from a number of [Thymeleaf](http://thymeleaf.org) template files, any of which can be replaced to create your own look and feel. All of the templates can be found in your built war (after running the Maven build), or in the target directory's staging area, in `WEB-INF/templates`. By placing a file with the same path/name in your `src/main/webapp/WEB-INF/templates` directory you can replace the built in template with your own file.

# A Complete Example

The [hapi-fhir-jpaserver-starter](https://github.com/hapifhir/hapi-fhir-jpaserver-starter) project contains a complete working example of the FHIR Tester as a part of its configuration. You may
wish to browse its source to see how this works. The example in that project includes an overlay file that overrides some of the basic branding in the base project as well.  

# Authentication

The testing UI uses its own client to talk to your FHIR server. In other words, there are no special "hooks" which the tested uses to retrieve data from your server, it acts as an HTTP client just like any other client.

This does mean that if your server has any authorization requirements, you will need to configure the tester UI to meet those requirements. For example, if your server has been configured to require a HTTP Basic Auth header (e.g. <code>Authorization: Basic VVNFUjpQQVNT</code>) you need to configure the tester UI to send those credentials across when it is acting as a FHIR client.

This is done by providing your own implementation of the `ITestingUiClientFactory` interface. This interface takes in some details about the incoming request and produces a client.
