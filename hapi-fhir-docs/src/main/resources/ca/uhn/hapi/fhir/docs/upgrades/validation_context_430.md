
### IValidationSupport Interfaces Collapsed

Previously, a number of `IValidationSupport` interfaces existed. These interface were all almost identical but had a few minor differences relating to the different versions of FHIR. For example, the following existed, along with others:

* org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport
* org.hl7.fhir.r4.hapi.ctx.IValidationSupport

These have all been replaced with a single interface that is intended to be used for all versions of FHIR:

* [ca.uhn.fhir.context.support.IContextValidationSupport](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-base/src/main/java/ca/uhn/fhir/context/support/IContextValidationSupport.java)

This also means that the following classes (which previously existing in multiple packages) have been replaced with a single version supporting multiple FHIR versions. You will need to adjust package names:

* CachingValidationSupport
* SnapshotGeneratingValidationSupport
