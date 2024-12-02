# Validation Support Modules

The [Instance Validator](./instance_validator.html) relies on an implementation of an interface called [IValidationSupport](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/context/support/IValidationSupport.html) to load StructureDefinitions, validate codes, etc.

By default, an implementation of this interface called [DefaultProfileValidationSupport](/hapi-fhir/apidocs/hapi-fhir-base/undefined/ca/uhn/fhir/context/support/DefaultProfileValidationSupport.html) is used. This implementation simply uses the built-in official FHIR definitions to validate against (and in many cases, this is good enough).

However, if you have needs beyond simply validating against the core FHIR specification, you may wish to use something more.

# Built-In Validation Support Classes

There are a several implementations of the [IValidationSupport](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/context/support/IValidationSupport.html) interface built into HAPI FHIR that can be used, typically in a chain.

# ValidationSupportChain

[JavaDoc](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/common/hapi/validation/support/ValidationSupportChain.html) / [Source](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-validation/src/main/java/org/hl7/fhir/common/hapi/validation/support/ValidationSupportChain.java)

This module can be used to combine multiple implementations together so that for every request, each support class instance in the chain is tried in sequence. Note that nearly all methods in the [IValidationSupport](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/context/support/IValidationSupport.html) interface are permitted to return `null` if they are not able to service a particular method call. So for example, if a call to the [`validateCode`](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/context/support/IValidationSupport.html#validateCode(ca.uhn.fhir.context.support.ValidationSupportContext,ca.uhn.fhir.context.support.ConceptValidationOptions,java.lang.String,java.lang.String,java.lang.String,java.lang.String)) method is made, the validator will try each module in the chain until one of them returns a non-null response.

The following chaining logic is used:

* Calls to `fetchAll...` methods such as `fetchAllConformanceResources()` and `fetchAllStructureDefinitions()` will call every method in the chain in order, and aggregate the results into a single list to return.
* Calls to fetch or validate codes, such as `validateCode(...)` and `lookupCode(...)` will first test each module in the chain using the`isCodeSystemSupported(...)` or `isValueSetSupported(...)` methods (depending on whether a ValueSet URL is present in the method parameters) and will invoke any methods in the chain which return that they can handle the given CodeSystem/ValueSet URL. The first non-null value returned by a method in the chain that can support the URL will be returned to the caller.
* All other methods will invoke the method in the chain in order, and will return immediately as soon as a non-null value is returned.

The following caching logic is used if caching is enabled using `CacheConfiguration`. You can use `CacheConfiguration.disabled()` if you want to disable caching.

* Calls to fetch StructureDefinitions including `fetchAllStructureDefinitions()` and `fetchStructureDefinition(...)` are cached in a non-expiring cache. This is because the `FhirInstanceValidator` module makes assumptions that these objects will not change for the lifetime of the validator for performance reasons.
* Calls to all other `fetchAll...` methods including `fetchAllConformanceResources()` and `fetchAllSearchParameters()` cache their results in an expiring cache, but will refresh that cache asynchronously.
* Results of `generateSnapshot(...)` are not cached, as this method is generally called in contexts where the results are cached.
* Results of all other methods are stored in an expiring cache.

Note that caching functionality used to be provided by a separate provider called {@literal CachingValidationSupport} but that functionality has been moved into this class as of HAPI FHIR 8.0.0, because it is possible to provide a more efficient chain when these functions are combined.

# DefaultProfileValidationSupport

[JavaDoc](/hapi-fhir/apidocs/hapi-fhir-base/undefined/ca/uhn/fhir/context/support/DefaultProfileValidationSupport.html) / [Source](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-base/src/main/java/ca/uhn/fhir/context/support/DefaultProfileValidationSupport.java)

This module supplies the built-in FHIR core structure definitions, including both FHIR resource definitions (StructureDefinition resources) and FHIR built-in vocabulary (ValueSet and CodeSystem resources).

# InMemoryTerminologyServerValidationSupport

[JavaDoc](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/common/hapi/validation/support/InMemoryTerminologyServerValidationSupport.html) / [Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-validation/src/main/java/org/hl7/fhir/common/hapi/validation/support/InMemoryTerminologyServerValidationSupport.java)

This module acts as a simple terminology service that can validate codes against ValueSet and CodeSystem resources purely in-memory (i.e. with no database). This is sufficient in many basic cases, although it is not able to validate CodeSystems with external content (i.e CodeSystems where the `CodeSystem.content` field is `external`, such as the LOINC and SNOMED CT CodeSystems).

# PrePopulatedValidationSupport

[JavaDoc](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/common/hapi/validation/support/PrePopulatedValidationSupport.html) / [Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-validation/src/main/java/org/hl7/fhir/common/hapi/validation/support/PrePopulatedValidationSupport.java)

This module contains a series of HashMaps that store loaded conformance resources in memory. Typically this is initialized at startup in order to add custom conformance resources into the chain.


<a name="npmpackagevalidationsupport"/>

# NpmPackageValidationSupport

[JavaDoc](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/common/hapi/validation/support/NpmPackageValidationSupport.html) / [Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-validation/src/main/java/org/hl7/fhir/common/hapi/validation/support/NpmPackageValidationSupport.java)

This module can be used to load FHIR NPM Packages and supply the conformance resources within them to the validator. See [Validating Using Packages](./instance_validator.html#packages) for am example of how to use this module. 


# SnapshotGeneratingValidationSupport

[JavaDoc](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/common/hapi/validation/support/SnapshotGeneratingValidationSupport.html) / [Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-validation/src/main/java/org/hl7/fhir/common/hapi/validation/support/SnapshotGeneratingValidationSupport.java)

This module generates StructureDefinition snapshots as needed. This should be added to your chain if you are working wiith differential StructureDefinitions that do not include the snapshot view.

# CommonCodeSystemsTerminologyService

[JavaDoc](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/common/hapi/validation/support/CommonCodeSystemsTerminologyService.html) / [Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-validation/src/main/java/org/hl7/fhir/common/hapi/validation/support/CommonCodeSystemsTerminologyService.java)

This module validates codes in CodeSystems that are not distributed with the FHIR specification because they are difficult to distribute but are commonly used in FHIR resources.

The following table lists vocabulary that is validated by this module:

<table class="table table-bordered table-striped">
    <thead>
        <tr>
            <th>Name</th>
            <th>Canonical URLs</th>
            <th>Validation Details</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>USPS State Codes</td>
            <td>
                ValueSet: <a href="http://hl7.org/fhir/us/core/ValueSet/us-core-usps-state">(...)/ValueSet/us-core-usps-state</a>
                <br/>
                CodeSystem: <a href="https://www.usps.com/">https://www.usps.com/</a>
            </td>
            <td>
                Codes are validated against a built-in list of valid state codes.
            </td>
        </tr>
        <tr>
            <td>MimeTypes (BCP-13)</td>
            <td>
                ValueSet: <a href="http://hl7.org/fhir/ValueSet/mimetypes">(...)/ValueSet/mimetypes</a>
                <br/>
                CodeSystem: <code>urn:ietf:bcp:13</code>
            </td>
            <td>
                Codes are not validated, but are instead assumed to be correct. Improved validation should be
                added in the future, please get in touch if you would like to help.
            </td>
        </tr>
        <tr>
            <td>Languages (BCP-47)</td>
            <td>
                ValueSet: <a href="http://hl7.org/fhir/ValueSet/languages">(...)/ValueSet/languages</a>
                <br/>
                ValueSet: <a href="http://hl7.org/fhir/ValueSet/all-languages">(...)/ValueSet/all-languages</a>
                <br/>
                CodeSystem: <code>urn:ietf:bcp:47</code>
            </td>
            <td>
                Codes are validated against the respective ValueSet. Support for two different ValueSets
                is provided: The <a href="http://hl7.org/fhir/ValueSet/languages">languages</a> 
                ValueSet provides a collection of commonly used language codes. Only codes explicitly 
                referenced in this ValueSet are considered valid. 
                The <a href="http://hl7.org/fhir/ValueSet/languages">all-languages</a> ValueSet
                accepts any valid BCP-47 code. Codes are validated using data supplied by
                the 
                <a href="https://github.com/mattcg/language-subtag-registry">Language Subtype Registry</a> 
                project.
            </td>
        </tr>
        <tr>
            <td>Countries (ISO 3166)</td>
            <td>
                CodeSystem: <a href="urn:iso:std:iso:3166">urn:iso:std:iso:3166</a>
            </td>
            <td>
                Codes are validated against a built-in list of valid ISO 3166 codes. Both Alpha-2 (two character) and Alpha-3 (three character) variants are supported.
            </td>
        </tr>
        <tr>
            <td>Unified Codes for Units of Measure (UCUM)</td>
            <td>
                ValueSet: <code><a href="http://hl7.org/fhir/ValueSet/ucum-units">(...)/ValueSet/ucum-units</a></code>
                <br/>
                CodeSystem: <code>http://unitsofmeasure.org</code>
            </td>
            <td>
                Codes are validated using the UcumEssenceService provided by the <a href="https://github.com/FHIR/Ucum-java">UCUM Java</a> library.
            </td>
        </tr>
    </tbody>
</table>

# RemoteTerminologyServiceValidationSupport

[JavaDoc](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/common/hapi/validation/support/RemoteTerminologyServiceValidationSupport.html) / [Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-validation/src/main/java/org/hl7/fhir/common/hapi/validation/support/RemoteTerminologyServiceValidationSupport.java)

This module validates codes using a remote FHIR-based terminology server.

This module will invoke the following operations on the remote terminology server:

* **GET [base]/CodeSystem?url=[url]** &ndash; Tests whether a given CodeSystem is supported on the server 
* **GET [base]/ValueSet?url=[url]** &ndash; Tests whether a given ValueSet is supported on the server 
* **POST [base]/CodeSystem/$validate-code** &ndash; Validate codes in fields where no specific ValueSet is bound 
* **POST [base]/ValueSet/$validate-code** &ndash; Validate codes in fields where a specific ValueSet is bound 

# UnknownCodeSystemWarningValidationSupport

[JavaDoc](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/common/hapi/validation/support/UnknownCodeSystemWarningValidationSupport.html) / [Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-validation/src/main/java/org/hl7/fhir/common/hapi/validation/support/UnknownCodeSystemWarningValidationSupport.java)

This validation support module may be placed at the end of a ValidationSupportChain in order to configure the validator to generate a warning if a resource being validated contains an unknown code system.

Note that this module must also be activated by calling [setAllowNonExistentCodeSystem(true)](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/common/hapi/validation/support/UnknownCodeSystemWarningValidationSupport.html#setAllowNonExistentCodeSystem(boolean)) in order to specify that unknown code systems should be allowed.

# CachingValidationSupport

[JavaDoc](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/common/hapi/validation/support/CachingValidationSupport.html) / [Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-validation/src/main/java/org/hl7/fhir/common/hapi/validation/support/CachingValidationSupport.java)

This module is deprecated and no longer provides any functionality. Caching is provided by [ValidationSupportChain](#validationsupportchain).


# Recipes

The IValidationSupport instance passed to the FhirInstanceValidator will often resemble the chain shown in the diagram below. In this diagram:

* DefaultProfileValidationSupport is used to supply basic built-in FHIR definitions
* PrePopulatedValidationSupport is used to supply other custom definitions
* InMemoryTerminologyServerValidationSupport is used to validate terminology
* The modules above are all added to a chain via ValidationSupportChain
* Finally, a cache is placed in front of the entire chain in order to improve performance

<a href="/hapi-fhir/docs/images/validation-support-chain.svg" target="_blank"><img src="/hapi-fhir/docs/images/validation-support-chain.svg" alt="Validation Support Chain"/><br/>(expand)</a>

# Recipe: Supplying Custom Definitions

The following snippet shows how to supply custom definitions to the validator.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ValidatorExamples.java|validateSupplyProfiles}}
```

# Recipe: Using a Remote Terminology Server

The following snippet shows how to leverage a remote (FHIR-based) terminology server, by making REST calls to the external service when codes need to be validated.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ValidatorExamples.java|validateUsingRemoteTermSvr}}
```





