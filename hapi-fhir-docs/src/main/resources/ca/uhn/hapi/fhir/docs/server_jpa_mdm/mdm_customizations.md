# MDM Customizations

This section describes customization supported by MDM.

# Interceptors

MDM allows customization through interceptors.  Please refer to the [Interceptors](/hapi-fhir/docs/interceptors/interceptors.html) section of the documentation for further details and implementation guidelines.   

## MDM Preprocessing Pointcut

MDM supports a pointcut invocation right before it starts matching an incoming source resource against defined rules.  A possible use of the pointcut would be to alter a resource content with the intention of influencing the MDM matching and linking process.  Any modifications to the source resource are NOT persisted to the database.  Modifications performed within the pointcut will remain valid during MDM processing only.

## Example: Ignoring Matches on Patient Name

In a scenario where a patient was given a placeholder name(John Doe), it would be desirable to ignore a 'name' matching rule but allow matching on other valid rules like matching SSN or a matching address.  

This can be done with a custom interceptor, or by providing a set of BlockListRules and wiring in a
IBlockListRuleProvider that provides it.

---

### Block List Rules

MDM can be configured to block certain resources from MDM matching entirely
using a set of json rules.

Blocked resources will still have an associated Golden Resource
created, and will still be available for future resources to match,
but no matching will be done to existing resources in the system.

In order to prevent MDM matching using the block rule list,
an IBlockListRuleProvider must be wired in and a set of block rules provided.

Blocking rules are provided in a list of rule-sets,
with each one applicable to a specified resource type.

Within each rule-set, a collection of fields specify the
`fhirPath` and `value` (case insensitive) on which to test an input resource for blocking.

If a resource matches on all blocked fields in a rule-set,
MDM matching will be blocked for the entire resource.

If multiple rule-sets apply to the same resource, they will be checked
in sequence until one is found to be applicable. If none are, MDM matching
will continue as before.

Below is an example of MDM blocking rules used to prevent matching on Patients
with name "John Doe" or "Jane Doe".

```json
{
   "blocklist": [{
      "resourceType": "Patient",
      "fields": [{
        "fhirPath": "name.first().family",
        "value": "doe"
      }, {
         "fhirPath": "name.first().given.first()",
         "value": "john"
      }]
   }, {
      "resourceType": "Patient",
      "fields": [{
         "fhirPath": "name.first().family",
         "value": "doe"
      }, {
         "fhirPath": "name.first().given.first()",
         "value": "jane"
      }]
   }]
}
```

Note that, for these rules, because the `fhirPath` specifies the `first()` name,
Patient resource A below would be blocked. But Patient resource B would not be.

##### Patient Resource A

```json
{
   "resourceType": "Patient",
   "name": [{
      "family": "doe",
      "given": [
         "jane"
      ]
   }]
}
```

##### Patient Resource B

```json
{
   "resourceType": "Patient",
   "name": [{
      "family": "jetson",
      "given": [
         "jane"
      ]
   },{
      "family": "doe",
      "given": [
         "jane"
      ]
   }]
}
```

---

### Interceptor Blocking

The following provides a full implementation of an interceptor that prevents matching on a patient name when it detects a placeholder value.  

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/interceptor/PatientNameModifierMdmPreProcessingInterceptor.java|patientInterceptor}}
```

See the [Pointcut](/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html) JavaDoc for further details on the pointcut MDM_BEFORE_PERSISTED_RESOURCE_CHECKED.
