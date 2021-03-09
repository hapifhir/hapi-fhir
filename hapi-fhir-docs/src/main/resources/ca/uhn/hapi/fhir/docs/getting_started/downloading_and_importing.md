# Downloading/Importing HAPI FHIR

If you are developing applications in Java, the easiest way to use HAPI FHIR is to use a build system which handles dependency management automatically. The two most common such systems are [Apache Maven](http://maven.apache.org) and [Gradle](https://gradle.org/). These systems will automatically download "dependency" libraries and add them to your classpath. If you are **not** using one of these systems, you can still manually download the latest release of HAPI by looking in the [GitHub Release Section](https://github.com/hapifhir/hapi-fhir/releases).


# Maven Users

To use HAPI in your application, at a minimum you need to include the HAPI-FHIR core JAR `hapi-fhir-base-[version].jar`, as well as at least one "structures" JAR.

The structures JAR contains classes with the resource and datatype definitions for a given version of FHIR.

If you are using Maven, the following example shows dependencies being added to include DSTU2 and R4 structures.

```xml
<dependencies>
    <dependency>
        <groupId>ca.uhn.hapi.fhir</groupId>
        <artifactId>hapi-fhir-structures-dstu2</artifactId>
        <version>${project.version}</version>
    </dependency>
    <dependency>
        <groupId>ca.uhn.hapi.fhir</groupId>
        <artifactId>hapi-fhir-structures-r4</artifactId>
        <version>${project.version}</version>
    </dependency>
</dependencies>
```

Note that if you wish to perform validation, you may also want to include the "validation resources" JARs, which contain schemas, profiles, and other artifacts used by the validator for your given version. 

```xml
<dependencies>
    <dependency>
        <groupId>ca.uhn.hapi.fhir</groupId>
        <artifactId>hapi-fhir-validation-resources-dstu2</artifactId>
        <version>${project.version}</version>
    </dependency>
    <dependency>
        <groupId>ca.uhn.hapi.fhir</groupId>
        <artifactId>hapi-fhir-validation-resources-R4</artifactId>
        <version>${project.version}</version>
    </dependency>
</dependencies>
```

# Gradle Users

If you are using Gradle, you may use the following dependencies. Note that if you are doing Android development, you may want to use our [Android build](/docs/android/client.html) instead.

```groovy
compile 'ca.uhn.hapi.fhir:hapi-fhir-base:${project.version}'
compile 'ca.uhn.hapi.fhir:hapi-fhir-structures-dstu2:${project.version}'
compile 'ca.uhn.hapi.fhir:hapi-fhir-structures-r4:${project.version}'
```

<a name="snapshot"/>

# Snapshot Builds

The HAPI FHIR project generally releases a new full software release 4 times per year, or approximately every 3 months.

Generally speaking, it is a good idea to use a stable build. However, FHIR is a fast moving specification, and there is a lot of ongoing work in HAPI as well. There may be times when you want to try out the latest unreleased version, either to test a new feature or to get a bugfix. You can usually look at the [Changelog](/docs/introduction/changelog.html) to get a sense of what has changed in the next unreleased version.

## Using Snapshot Builds

Snapshot builds of HAPI are pre-release builds which can contain fixes and new features not yet released in a formal release. To use	snapshot builds of HAPI you may need to add a reference to the OSS snapshot repository to your project build file.

Using a snapshot build generally involves appending *-SNAPSHOT* to the version number, e.g. `4.1.0-SNAPSHOT`. In order to automatically download snapshot builds, you may also need to add a snapshot repository to your build file as shown below:

### Using Maven:

```xml
<repositories>
    <repository>
        <id>oss-snapshots</id>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    </repository>
</repositories>
```

### Using Gradle:

```groovy
repositories {
	mavenCentral()
	maven {
		url "https://oss.sonatype.org/content/repositories/snapshots"
	}
}
```

# Dependencies

The HAPI-FHIR library depends on other libraries to provide specific functionality. Some of those libraries are listed here:

## Logging (SLF4j, Logback, etc.)

HAPI requires SLF4j for logging support, and it is recommended to include an underlying logging framework such as Logback. See the [logging documentation](/docs/appendix/logging.html) for more information.

## StAX / Woodstox

XML processing (for resource marshalling and unmarshalling) uses the Java StAX API, which is a fast and efficient API for XML processing. HAPI bundles (for release archives) and depends on (for Maven builds) the [Woodstox](http://woodstox.codehaus.org/) library, which is a good implementation of StAX.

Upon starting up, HAPI will emit a log line indicating which StAX implementation is being used, e.g:

```
08:01:32.044 [main] INFO ca.uhn.fhir.util.XmlUtil - FHIR XML processing will use StAX implementation 'Woodstox XML-processor' version '4.4.0'
```

Although most testing is done using the Woodstox implementation of StAX, it is not required and HAPI should work correctly with any compliant implementation of StAX.

You can force Woodstox in an environment where multiple StAX libraries are present by setting the following system properties:

```java
System.setProperty("javax.xml.stream.XMLInputFactory", "com.ctc.wstx.stax.WstxInputFactory");
System.setProperty("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory");
System.setProperty("javax.xml.stream.XMLEventFactory", "com.ctc.wstx.stax.WstxEventFactory");
```

## PH-Schematron

If you are using the [Schematron Validation](/docs/validation/schema_validator.html) module, you will also need to include the Ph-Schematron library on your classpath. (Note that prior to HAPI FHIR 3.4.0 we used Phloc-Schamtron instead, but that library has been discontinued).

If you are using Maven, this library is not added by default (it is marked as an optional dependency) since not all applications need Schematron support. As a result you will need to manually add the following	dependencies to your project pom.xml:

```xml
<dependencies>
    <dependency>
        <groupId>com.helger</groupId>
        <artifactId>ph-schematron</artifactId>
        <version>${ph_schematron_version}</version>
    </dependency>
    <dependency>
        <groupId>com.helger</groupId>
        <artifactId>ph-commons</artifactId>
        <version>${ph_commons_version}</version>
    </dependency>
</dependencies>
```

