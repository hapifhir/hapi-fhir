# Android Client

HAPI now has a specially built module for use on Android. Android developers may use this JAR to take advantage of the FHIR model classes, and the FHIR client (running a FHIR server on Android is not yet supported. Get in touch if this is something you are interested in working on!)

As of HAPI FHIR 3.1.0, the <code>hapi-fhir-android</code> module has been streamlined in order to reduce its footprint. Previous versions of the library included both an XML and a JSON parser but this has been streamlined to only include JSON support in order to reduce the number of libraries required in an Android build.

When using the HAPI FHIR Android client, the client will request only JSON responses (via the HTTP <code>Accept</code> header) and will not be able to communicate with FHIR servers that support only XML encoding (few, if any, servers actually exist	with this limitation that we are aware of).

The Android client also uses the <code>hapi-fhir-client-okhttp</code> module, which is an HTTP client based on the OkHttp library. This library has proven to be	more powerful and less likely to cause issues on Android than the Apache HttpClient implementation which is bundled by default.

Note that the Android JAR is still new and hasn't received as much testing as other parts of the library. We would greatly appreciate feedback, testing, etc. Also note that because mobile apps run on less powerful hardware compared to desktop and server applications, it is all the more important to keep a single instance of the `FhirContext` around for good performance, since this object is expensive to create. We are hoping to improve performance of the creation of this object in a future release. If you are an Android developer and would like to help with this, please get in touch!

## Get the Android JAR

To add the HAPI library via Gradle, you should add the [hapi-fhir-android](https://search.maven.org/search?q=g:ca.uhn.hapi.fhir%20AND%20a:hapi-fhir-android&core=gav) library to your Gradle file, as well as a structures library for the appropriate version of FHIR that you want to support, e.g. [hapi-fhir-structures-r4](https://search.maven.org/search?q=g:ca.uhn.hapi.fhir%20AND%20a:hapi-fhir-structures-r4&core=gav).

```groovy
dependencies {
    compile "ca.uhn.hapi.fhir:hapi-fhir-android:3.1.0-SNAPSHOT"
    compile "ca.uhn.hapi.fhir:hapi-fhir-structures-dstu2:3.1.0-SNAPSHOT"
}
```

You will also need to manually exclude the Woodstox StAX library from inclusion, as this library uses namespaces which are prohibited on Android. You should also exclude:

```groovy
configurations {
    all*.exclude group: 'org.codehaus.woodstox'
    all*.exclude group: 'org.apache.httpcomponents'
}
```

To see a sample Gradle file for a working Android project using HAPI FHIR, see the [Android Integration Test](https://github.com/hapifhir/hapi-fhir-android-integration-test) project.

# Performance

On mobile devices, performance problems are particularly noticeable. This is made worse by the fact that some economy Android devices have much slower performance than modern desktop computers. See the [Client Configuration Performance](/docs/client/client_configuration.html#performance) page for some tips on how to improve client performance.

# Examples

The following is intended to be a selection of publicly available open source Android applications which use HAPI FHIR and might be useful as a reference.

If you know of others, please let us know!

* [https://github.com/hapifhir/hapi-fhir-android-integration-test](https://github.com/hapifhir/hapi-fhir-android-integration-test) - hapi-fhir-android Integration Test and Reference Application is our test platform for validating new releases. Created by Thomas Andersen.
* [https://github.com/SynappzMA/FHIR-Android](https://github.com/SynappzMA/FHIR-Android) - Nice FHIR DSTU2 search app
