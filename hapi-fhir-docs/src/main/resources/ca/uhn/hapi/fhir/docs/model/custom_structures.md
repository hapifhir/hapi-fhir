# Custom Structures

Typically, when working with FHIR the right way to provide your own extensions is to work with existing resource types and simply add your own extensions and/or constrain out fields you don't need.

This process is described on the [Profiles &amp; Extensions](./profiles_and_extensions.html) page.

There are situations however when you might want to create an entirely custom resource type. This feature should be used only if there is no other option, since it means you are creating a resource type that will not be interoperable with other FHIR implementations.

<p class="doc_info_bubble">
This is an advanced features and isn't needed for most uses of HAPI FHIR. Feel free to skip this page. For a simpler way of interacting with resource extensions, see <a href="./profiles_and_extensions.html">Profiles &amp; Extensions</a>.
</p>

# Extending FHIR Resource Classes

The most elegant way of adding extensions to a resource is through the use of custom fields. The following example shows a custom type which extends the FHIR Patient resource definition through two extensions.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/MyPatient.java|patientDef}}
```

Using this custom type is as simple as instantiating the type and working with the new fields.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/MyPatientUse.java|patientUse}}
```

This example produces the following output:

```xml
<Patient xmlns="http://hl7.org/fhir">
   <modifierExtension url="http://example.com/dontuse#importantDates">
      <valueDateTime value="2010-01-02"/>
   </modifierExtension>
   <modifierExtension url="http://example.com/dontuse#importantDates">
      <valueDateTime value="2014-01-26T11:11:11"/>
   </modifierExtension>
   <extension url="http://example.com/dontuse#petname">
      <valueString value="Fido"/>
   </extension>
   <name>
      <family value="Smith"/>
      <given value="John"/>
      <given value="Quincy"/>
      <suffix value="Jr"/>
   </name>
</Patient>
```

Parsing messages using your new custom type is equally simple. These types can also be used as method return types in clients and servers.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/MyPatientUse.java|patientParse}}
```

# Using Custom Types in a Client

If you are using a client and wish to use a specific custom structure, you may simply use the custom structure as you would a build in HAPI type.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExtensionsDstu3.java|customTypeClientSimple}}
```

You may also explicitly use custom types in searches and other operations which return resources.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExtensionsDstu3.java|customTypeClientSearch}}
```

You can also explicitly declare a preferred response resource custom type. This is useful for some operations that do not otherwise declare their resource types in the method signature.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExtensionsDstu3.java|customTypeClientSearch2}}
```

## Using Multiple Custom Types in a Client

Sometimes you may not know in advance exactly which type you will be receiving. For example, there are Patient resources which conform to several different profiles on a server and you aren't sure which profile you will get back for a specific read, you can declare the "primary" type for a given profile.

This is declared at the FhirContext level, and will apply to any clients created from this context (including clients created before the default was set).

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExtensionsDstu3.java|customTypeClientDeclared}}
```
# Using Custom Types in a Server

If you are using a client and wish to use a specific custom structure, you may simply use the custom structure as you would a build in HAPI type.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExtensionsDstu3.java|customTypeClientSimple}}
```

# Custom Composite Extension Classes

The following example shows a resource containing a composite extension.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/customtype/CustomCompositeExtension.java|resource}}
```

This could be used to create a resource such as the	following:

```xml
<Patient xmlns="http://hl7.org/fhir">
   <id value="123"/>
   <extension url="http://acme.org/fooParent">
      <extension url="http://acme.org/fooChildA">
         <valueString value="ValueA"/>
      </extension>
      <extension url="http://acme.org/fooChildB">
         <valueString value="ValueB"/>
      </extension>
   </extension>
</Patient>
```

# Custom Resource Structure

The following example shows a custom resource structure class creating an entirely new resource type as opposed to simply extending an existing one. Note that this is allowable in FHIR, but is **highly discouraged** as they are by definition not good for interoperability.

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
