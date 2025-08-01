## Publishing Changes

As of `8.3.12-SNAPSHOT`, HAPI-FHIR snapshots are now published on [Maven Central](https://central.sonatype.com/namespace/ca.uhn.hapi.fhir). As of June 30th, [OSS Sonatype has been sunsetted](https://central.sonatype.org/news/20250326_ossrh_sunset/). If you need to rely on older snapshots, you must build them from source locally. If you consume snapshots, you will need to update your pom.xml with the following repository information: 

```xml
<repositories>
    <repository>
        <name>Central Portal Snapshots</name>
        <id>central-portal-snapshots</id>
        <url>https://central.sonatype.com/repository/maven-snapshots/</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```


## Breaking Changes

* FhirPath `PATCH` operations that match multiple elements will no longer replace these values, but throw an exception. This is in line with the <a href="https://www.hl7.org/fhir/R4/fhirpatch.html">spec</a>.

* Auth rules that grant access to resources in the <a href="https://build.fhir.org/compartmentdefinition-patient.html">patient compartment</a> will no longer grant access to `Group` or `List` resources in that compartment. These resources are still in the patient compartment, but additional permissions are required to access them.

* `$export` and `$everything` operations on Patient Compartment (instance or type) will no longer return `List` or `Group` resources, regardless of auth rules.

