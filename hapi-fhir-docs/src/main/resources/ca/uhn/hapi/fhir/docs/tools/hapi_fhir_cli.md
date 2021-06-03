
# Command Line Tool for HAPI FHIR

**hapi-fhir-cli** is the HAPI FHIR Command Line tool. It features a number of HAPI's built-in features as easy to use command line options. 

## Download and Installation

You can get the tool by downloading it from our	[GitHub Releases](https://github.com/hapifhir/hapi-fhir/releases) page (look for the archive named `hapi-fhir-[version]-cli.tar.bz2` on OSX/Linux or `hapi-fhir-[version]-cli.zip` on Windows).

When you have downloaded the archive (either ZIP or tar.bz2), expand it into a directory where you will keep it, and add this directory to your path. 

You can now try the tool out by executing the following command: `hapi-fhir-cli`

This command should show a help screen, as shown in the screenshot below.

<img src="/hapi-fhir/docs/images/hapi-fhir-cli.png" alt="Basic screen shot" style="margin-left: 40px;"/>

## Download and Installation - Mac/OSX

hapi-fhir-cli is available as a <a href="https://brew.sh/">Homebrew</a> package	for Mac. It can be installed using the following command:

```bash
brew install hapi-fhir-cli
```

## Troubleshooting

The tool should work correctly on any system that has Java 8 (or newer) installed. If it is not working correctly, first try the following command to test if Java is installed:

```bash
java -version
```

If this command does not produce output similar to the following, you should install/reinstall Java.

```
java version "1.8.0_60"
Java(TM) SE Runtime Environment (build 1.8.0_60-b27)
Java HotSpot(TM) 64-Bit Server VM (build 25.60-b23, mixed mode)
```

Individual commands can be troubleshooted by adding the `--debug` command line argument.

If this does not help, please post a question on our [Google Group](https://groups.google.com/d/forum/hapi-fhir).

# Server (run-server)

The CLI tool can be used to start a local, fully functional FHIR server which you can use for testing. To start this server, simply issue the command <code>hapi-fhir-cli run-server</code> as shown in the example below:

<img src="/hapi-fhir/docs/images/hapi-fhir-cli-run-server.png" alt="Run Server" style="margin-left: 40px;"/>

Once the server has started, you can access the testing webpage by pointing your browser at <a href="http://localhost:8080/">http://localhost:8080/</a>. The FHIR server base URL will be <a href="http://localhost:8080/baseDstu3/">http://localhost:8080/baseDstu3/</a>.

Note that by default this server will not be populated with any resources at all. You can easily populate it with the FHIR example resources by <b>leaving it running</b> and opening a second terminal window, then using the <code>hapi-fhir-cli upload-examples</code> command (see the section below).

The server uses a local Derby database instance for storage. You may want to execute this command in an empty directory, which you can clear if you want to reset the server.

# Upload Example Resources (upload-examples)

The <b>upload-examples</b> command downloads the complete set of FHIR example resources from the HL7 website, and uploads them to a server of your choice. This can be useful to populate a server with test data.

To execute this command, uploading test resources to a local CLI server, issue the following: `hapi-fhir-cli upload-examples -v dstu3 -t http://localhost:8080/baseDstu3`

Note that this command may take a surprisingly long time to complete because of the large number of examples.

# Upload Terminology

The HAPI FHIR JPA server has a terminology server, and has the ability to be populated with "external" code systems. These code systems are systems that contain large numbers of codes, so the codes are not stored directly inside the resource body.

HAPI has methods for uploading several popular code systems into its tables using the distribution files produced by the respective code systems. This is done using the <code>upload-terminology</code> command. The following examples show how to do this for several popular code systems.

Note that the path and exact filename of the terminology files will likely need to be adjusted for your local disk structure. 

###	SNOMED CT

```
./hapi-fhir-cli upload-terminology -d Downloads/SnomedCT_RF2Release_INT_20160131.zip -f dstu3 -t http://localhost:8080/baseDstu3 -u http://snomed.info/sct
```

### LOINC

```
./hapi-fhir-cli upload-terminology -d Downloads/LOINC_2.54_MULTI-AXIAL_HIERARCHY.zip -d Downloads/LOINC_2.54_Text.zip -f dstu3 -t http://localhost:8080/baseDstu3 -u http://loinc.org
```

### ICD-10-CM

```
./hapi-fhir-cli upload-terminology -d Downloads/LOINC_2.54_MULTI-AXIAL_HIERARCHY.zip -d icd10cm_tabular_2021.xml -f dstu3 -t http://localhost:8080/baseDstu3 -u http://hl7.org/fhir/sid/icd-10-cm
```

# Migrate Database

The `migrate-database` command may be used to Migrate a database schema when upgrading a [HAPI FHIR JPA](/docs/server_jpa/introduction.html) project from one version of HAPI	FHIR to another version.

See [Upgrading HAPI FHIR JPA](/docs/server_jpa/upgrading.html) for information on how to use this command.
