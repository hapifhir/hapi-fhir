# cqf-ruler

The CQF Ruler is an implementation of FHIR's [Clinical Reasoning Module](
http://hl7.org/fhir/clinicalreasoning-module.html) and serves as a
knowledge artifact repository and clinical decision support service.

## Usage 

 - `$ mvn install`
 - `$ mvn -Djetty.http.port=XXXX jetty:run`
 
Visit the [wiki](https://github.com/DBCG/cqf-ruler/wiki) for more documentation.

## Dependencies

Before the instructions in the above "Usage" section will work, you need to
install several primary dependencies.

### Java

Go to [http://www.oracle.com/technetwork/java/javase/downloads/](
http://www.oracle.com/technetwork/java/javase/downloads/) and download the
latest (version 8 or higher) JDK for your platform, and install it.

### Apache Maven

Go to [https://maven.apache.org](https://maven.apache.org), visit the main
"Download" page, and under "Files" download a binary archive of your
choice.  Then unpack that archive file and follow the installation
instructions in its README.txt.  The end result of this should be that the
binary "mvn" is now in your path.
