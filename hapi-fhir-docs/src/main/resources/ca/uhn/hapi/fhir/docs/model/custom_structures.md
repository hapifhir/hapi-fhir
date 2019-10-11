# Custom Structures

Typically, when working with FHIR the right way to provide your own extensions is to work with existing resource types and simply add your own extensions and/or constrain out fields you don't need.

This process is described on the [Profiles &amp; Extensions](./profiles_and_extensions.html) page.

There are situations however when you might want to create an entirely custom resource type. This feature should be used only if there is no other option, since it means you are creating a resource type that will not be interoperable with other FHIR implementations.

<p class="doc_info_bubble">
This is an advanced features and isn't needed for most uses of HAPI-FHIR. Feel free to skip this page.
</p>
			
# Custom Resource Structure

The following example shows a custom resource structure class:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/customtype/CustomResource.java|resource}}
``` 

## Custom Datatype Structure

The following example shows a custom datatype structure class:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/customtype/CustomDatatype.java|datatype}}
```

## Using the Custom Structure

And now let's try the custom structure out:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/customtype/CustomUsage.java|usage}}
```

This produces the following output (some spacing has been added for readability):

```xml
<CustomResource xmlns="http://hl7.org/fhir">
   <meta>
      <profile value="http://hl7.org/fhir/profiles/custom-resource"/>
   </meta>
   
   <televisionDate value="2015-01-01"/>
   <televisionCustomDatatype>
      <date value="2016-05-22T08:30:36-04:00"/>
      <kittens value="FOO"/>
   </televisionCustomDatatype>
   
   <dogs value="Some Dogs"/>
   
</CustomResource>
```
