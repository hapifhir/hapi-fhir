# Hacking HAPI FHIR

This page contains useful information about how to get started in developing HAPI FHIR itself.

# Understanding the HAPI FHIR Codebase

The HAPI FHIR [Codebase](https://github.com/hapifhir/hapi-fhir) has a number of subprojects. You will typically need to interact with several of them in order to develop HAPI, but you generally don't need all of them.

The following is a list of key subprojects you might open in your IDE:

* [hapi-fhir-base](https://github.com/hapifhir/hapi-fhir/tree/master/hapi-fhir-base): This is the core library, containing the parsers, client/server frameworks, and many other features. Note that this module does not contain any model classes (e.g. the Patient model class) as these are found in "structures" projects below.
* hapi-fhir-structures-[version]: There are several structures projects (e.g. `hapi-fhir-structures-r4`), each of which contains model classes for a specific version of FHIR. Generally speaking you don't need to edit these projects directly, as most (but not all) of their code is generated.
* hapi-fhir-jpaserver-base:	This module contains the JPA server.

# Getting the Sources

<p style="float:right;">
    <a class="externalLink" href="https://dev.azure.com/hapifhir/HAPI%20FHIR/_build/latest?definitionId=1&branchName=master"><img src="https://dev.azure.com/hapifhir/HAPI%20FHIR/_apis/build/status/jamesagnew.hapi-fhir?branchName=master" alt="Build Status" class="img-fluid"/></a>
</p>

The best way to grab our sources is with Git. Grab the repository URL from our [GitHub page](https://github.com/jamesagnew/hapi-fhir). We try our best to ensure that the sources are always left in a buildable state. Check Azure Pipelines CI (see the image/link on the right) to see if the sources currently build.

# Building HAPI FHIR

HAPI is built primary using	[Apache Maven](http://maven.apache.org/). Even if you are using an IDE, you should **start by performing a command line build** before trying to get everything working in an IDE. This step ensures that generated code is available to the IDE.

 Execute the build with the following command:
 
 ```bash
 mvn install
 ```
 
 Note that this complete build takes a long time because of all of the unit tests being executed. At the end you should expect to see a screen resembling:
 
```bash
------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] HAPI-FHIR .......................................... SUCCESS [  4.456 s]
[INFO] HAPI FHIR - Deployable Artifact Parent POM ......... SUCCESS [  2.841 s]
[INFO] HAPI FHIR - Core Library ........................... SUCCESS [01:00 min]
[INFO] HAPI Tinder Plugin ................................. SUCCESS [ 19.259 s]
[INFO] HAPI FHIR Structures - DSTU1 (FHIR v0.80) .......... SUCCESS [01:40 min]
[INFO] HAPI FHIR Structures - DSTU2 (FHIR v1.0.0) ......... SUCCESS [01:14 min]
[INFO] HAPI FHIR Structures - DSTU3 ....................... SUCCESS [02:11 min]
.... some lines removed .....
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 20:45 min
[INFO] Finished at: 2016-02-27T15:05:35+00:00
```

# Troubleshooting

If the build fails to execute successfully, try the following:

* The first thing to try is always a fresh clean build when things aren't working:
  
   ```bash
   mvn clean install
   ```
  
* **If you are trying to build a submodule** (e.g. `hapi-fhir-jpaserver-base`), try building the root project first. Especially when building from the Git <code>master</code>, often times there will be dependencies that require a fresh complete build (note that this is not generally an issue when building from a release version)

* If the build fails with memory issues (or mysteriously dies during unit tests), your build environment may be running out of memory. By default, the HAPI build executes unit tests in multiple parallel JVMs in order to save time. This can consume a lot of RAM and sometimes causes issues. Try executing with the following command to disable this behaviour:

   ```bash
   mvn -P ALLMODULES,NOPARALLEL install
   ```
   
 * If you figure something else out, please <b>let us know</b> so that we can add it to this list!
 
# Importing into Eclipse
 
 This section shows how to import HAPI into Eclipse. There is no requirement to use Eclipse (IntelliJ/IDEA and Netbeans are both fine!) so feel free to skip this section.

**Maven Import**

Import the HAPI projects as Maven Modules by selecting **File -&gt; Import...** from the File menu. Then select **Existing Module Projects** as shown below.

<img src="/hapi-fhir/docs/images/hacking_import.png"/>

**Select the Projects**

Next, browse to the directory where you checked out the HAPI FHIR sources. You might want to select only the projects you are interested in editing, in order to keep Eclipse's memory use down. You can always come back and import more later.

<img src="/hapi-fhir/docs/images/hacking_import_step2.png"/>

## Troubleshooting

When importing the HAPI projects into Eclipse, sometimes Eclipse will fail to correctly import libraries. If you import a module into Eclipse and it fails to compile with many errors relating to packages other than HAPI's, the following steps will fix this:

* Delete the project from your Eclipse workspace
* On the local filesystem, delete the files `.project` and `.classpath`, and the directory `.settings` from each module you want to open.
* Import each module again using the instructions above

