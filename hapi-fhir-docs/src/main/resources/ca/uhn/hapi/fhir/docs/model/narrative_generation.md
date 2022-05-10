# Narrative Generation

HAPI provides several ways to add [Narrative Text](http://hl7.org/fhir/narrative.html) to your encoded messages.

The simplest way is to simply place the narrative text directly in the resource via the `setDivAsString()` method.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/Narrative.java|simple}}
```

# Automatic Narrative Generation

HAPI FHIR also comes with a built-in mechanism for automatically generating narratives based on your resources.

**Warning:** This built-in capability is a work in progress, and does not cover every type of resource or even every attribute in any resource. You should test it and configure it for your particular use cases.

HAPI's built-in narrative generation uses the [Thymeleaf](http://www.thymeleaf.org/) library for templating narrative texts. Thymeleaf provides a simple XHTML-based syntax which is easy to use and meshes well with the HAPI-FHIR model objects.

## A Simple Example

Activating HAPI's built-in narrative generator is as simple as calling [setNarrativeGenerator](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/context/FhirContext.html#setNarrativeGenerator(ca.uhn.fhir.narrative.INarrativeGenerator).

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/Narrative.java|example1}}
```

...which produces the following output:

```xml
<Patient xmlns="http://hl7.org/fhir">
    <text>
        <status value="generated"/>
        <div xmlns="http://www.w3.org/1999/xhtml">
            <div class="hapiHeaderText"> John Edward <b>SMITH </b></div>
            <table class="hapiPropertyTable">
                <tbody>
                    <tr><td>Identifier</td><td>7000135</td></tr>
                    <tr><td>Address</td><td><span>742 Evergreen Terrace</span><br/><span>Springfield</span> <span>ZZ</span></td></tr>
                </tbody>
            </table>
        </div>
    </text>
    <!-- .... snip ..... -->
</Patient>
```

# Built-in Narrative Templates

HAPI currently only comes with built-in support for a few resource types. Our intention is that people enhance these templates and create new ones, and share these back with us so that we can continue to build out the library. To see the current template library, see the source repository [here](https://github.com/hapifhir/hapi-fhir/tree/master/hapi-fhir-base/src/main/resources/ca/uhn/fhir/narrative).

Note that these templates expect a few specific CSS definitions to be present in your site's CSS file. See the [narrative CSS](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-base/src/main/resources/ca/uhn/fhir/narrative/hapi-narrative.css) to see these.

# Creating your own Templates

To use your own templates for narrative generation, simply create one or more templates, using the Thymeleaf HTML based syntax.

```java
{{snippet:classpath:/ca/uhn/fhir/narrative/OperationOutcome.html}}
```

Then create a properties file which describes your templates. In this properties file, each resource to be defined has a pair or properties.

The first (name.class) defines the class name of the resource to define a template for. The second (name.narrative) defines the path/classpath to the template file. The format of this path is `file:/path/foo.html` or  `classpath:/com/classpath/foo.html`.

```properties
# Two property lines in the file per template. There are several forms you
# can use. This first form assigns a template type to a resource by 
# resource name
practitioner.resourceType=Practitioner
practitioner.narrative=classpath:com/example/narrative/Practitioner.html

# This second form assigns a template by class name. This can be used for
# HAPI FHIR built-in structures, or for custom structures as well.
observation.class=org.hl7.fhir.r4.model.Observation
observation.narrative=classpath:com/example/narrative/Observation.html

# You can also assign a template based on profile ID (Resource.meta.profile)
vitalsigns.profile=http://hl7.org/fhir/StructureDefinition/vitalsigns
vitalsigns.narrative=classpath:com/example/narrative/Observation_Vitals.html
```

You may also override/define behaviour for datatypes and other structures. These datatype narrative definitions will be used as content within <code>th:narrative</code> blocks in resource templates. See the example resource template above for an example.

```properties
# You can create a template based on a type name
quantity.dataType=Quantity
quantity.narrative=classpath:com/example/narrative/Quantity.html

# Or by class name, which can be useful for custom datatypes and structures
custom_extension.class=com.example.model.MyCustomExtension
custom_extension.narrative=classpath:com/example/narrative/CustomExtension.html
```

Finally, use the [CustomThymeleafNarrativeGenerator](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/narrative/CustomThymeleafNarrativeGenerator.html) and provide it to the FhirContext.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/NarrativeGenerator.java|gen}}
```

