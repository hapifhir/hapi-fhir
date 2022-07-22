# MDM Customizations

This section describes customizations supported by MDM

# Interceptors

MDM will permit a degree of customization through interceptors.  Please refer to the [Interceptors](/hapi-fhir/docs/interceptors/interceptors.html) section of the documentation for further details and implementation guidelines.   

## MDM Preprocessing Pointcut
MDM supports a pointcut invocation right before it starts matching an incoming source resource against defined rules.  A possible use of the pointcut would be to alter a resource property(ies) with the intention of influencing the MDM matching and linking process.  It should be noted that any modifications to the source resource are NOT peristed to the supporting storage module.  Modificaiton will remain valid only during MDM processing.

## Example: Ignoring Matches on Patient Name
In a scenario where a patient was given a placeholder name(John Doe), it would be desirable to ignore a 'name' matching rule but allow matching on other valid rules like matching SSN or a matching address.  

The following provides a full implementation of an interceptor that prevents matching on a patient name when it detects  a placeholder value.  

```java
{{snippet:file:hapi-fhir-jpaserver-mdm/test/java/ca/uhn/fhir/jpa/mdm/interceptor/MdmPreProcessingInterceptor.java}}
```

See the [Pointcut](/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html) JavaDoc for further details on the pointcut MDM_BEFORE_PERSISTED_RESOURCE_CHECKED.
