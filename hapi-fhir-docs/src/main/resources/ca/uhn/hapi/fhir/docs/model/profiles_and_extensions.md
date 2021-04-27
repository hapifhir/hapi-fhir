# Profiles and Extensions

This page describes how to extend and constrain the FHIR data model for your own purposes.

# Extensions

<p class="doc_info_bubble">
Note on FHIR Versions: Because of the differences in the way the structures work between DSTU2 and DSTU3, we have provided two versions of many of the examples on this page. See the <a href="/hapi-fhir/docs/getting_started/versions.html">FHIR Versions</a> page for more information on FHIR version support in HAPI FHIR.
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

# Custom Resource Structures

All of the examples on this page show how to work with the existing data model classes.

This is a great way to work with extensions, and most HAPI FHIR applications use the techniques described on this page. However, there is a more advanced technique available as well, involving the creation of custom Java classes that extend the built-in classes to add statically bound extensions (as oppoed to the dynamically bound ones shown on this page). See [Custom Structures](./custom_structures.html) for more information.
