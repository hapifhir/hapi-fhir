# Profiles and Extensions

This page describes how to extend and constrain the FHIR data model for your own purposes.

# Extensions

<p class="doc_info_bubble">
Note on FHIR Versions: Because of the differences in the way the structures work between DSTU2 and DSTU3, we have provided two versions of many of the examples on this page. See the <a href="/hapi-fhir/docs/introduction/versions.html">FHIR Versions</a> page for more information on FHIR version support in HAPI FHIR.
</p>

Extensions are a key part of the FHIR specification, providing a standardized way of placing additional data in a resource.

The simplest way to interact with extensions (i.e. to add them to resources you are creating, or to read them from resources you are consuming) is to treat them as "undeclared extensions". Undeclared extensions can be added to any of the built in FHIR resource types that come with HAPI-FHIR.

### DSTU2

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExtensionsDstu2.java|resourceExtension}}
```

### DSTU3 and Later

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExtensionsDstu3.java|resourceExtension}}
```

Undeclared extensions can also be added to datatypes (composite or primitive).

### DSTU2

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExtensionsDstu2.java|resourceStringExtension}}
```

### DSTU3 and Later

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExtensionsDstu3.java|resourceStringExtension}}
```

# Sub-Extensions

Extensions may also have child extensions as their content, instead of a datatype. This is done by adding a child undeclared extension to the parent extension.

### DSTU2

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExtensionsDstu2.java|subExtension}}
```

### DSTU3 and Later

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExtensionsDstu3.java|subExtension}}
```

# Retrieving Extension Values

HAPI provides a few ways of accessing extension values in resources which are received from other sources (i.e. downloaded by a client).

### DSTU2

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExtensionsDstu2.java|parseExtension}}
```

### DSTU3 and Later

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExtensionsDstu3.java|parseExtension}}
```

# Custom Resource Types

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

## Using Custom Types in a Client

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
## Using Custom Types in a Server

If you are using a client and wish to use a specific custom structure, you may simply use the custom structure as you would a build in HAPI type.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExtensionsDstu3.java|customTypeClientSimple}}
```

## Custom Type Examples: Composite Extensions

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
