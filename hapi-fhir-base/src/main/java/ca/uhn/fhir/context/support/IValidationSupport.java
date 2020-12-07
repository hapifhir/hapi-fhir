package ca.uhn.fhir.context.support;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.conceptproperty.BaseConceptProperty;
import ca.uhn.fhir.context.support.support.CodeValidationResult;
import ca.uhn.fhir.context.support.support.LookupCodeResult;
import ca.uhn.fhir.context.support.support.ValueSetExpansionOutcome;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * This interface is a version-independent representation of the
 * various functions that can be provided by validation and terminology
 * services.
 * <p>
 * This interface is invoked directly by internal parts of the HAPI FHIR API, including the
 * Validator and the FHIRPath evaluator. It is used to supply artifacts required for validation
 * (e.g. StructureDefinition resources, ValueSet resources, etc.) and also to provide
 * terminology functions such as code validation, ValueSet expansion, etc.
 * </p>
 * <p>
 * Implementations are not required to implement all of the functions
 * in this interface; in fact it is expected that most won't. Any
 * methods which are not implemented may simply return <code>null</code>
 * and calling code is expected to be able to handle this. Generally, a
 * series of implementations of this interface will be joined together using
 * the
 * <a href="https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/common/hapi/validation/ValidationSupportChain2.html">ValidationSupportChain</a>
 * class.
 * </p>
 * <p>
 * See <a href="https://hapifhir.io/hapi-fhir/docs/validation/validation_support_modules.html">Validation Support Modules</a>
 * for information on how to assemble and configure implementations of this interface. See also
 * the <code>org.hl7.fhir.common.hapi.validation.support</code>
 * <a href="https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/common/hapi/validation/package-summary.html">package summary</a>
 * in the <code>hapi-fhir-validation</code> module for many implementations of this interface.
 * </p>
 *
 * @since 5.0.0
 */
public interface IValidationSupport {
	String URL_PREFIX_VALUE_SET = "http://hl7.org/fhir/ValueSet/";


	/**
	 * Expands the given portion of a ValueSet
	 *
	 * @param theValidationSupportContext The validation support module will be passed in to this method. This is convenient in cases where the operation needs to make calls to
	 *                                 other method in the support chain, so that they can be passed through the entire chain. Implementations of this interface may always safely ignore this parameter.
	 * @param theExpansionOptions      If provided (may be <code>null</code>), contains options controlling the expansion
	 * @param theValueSetToExpand      The valueset that should be expanded
	 * @return The expansion, or null
	 */
	default ValueSetExpansionOutcome expandValueSet(ValidationSupportContext theValidationSupportContext, @Nullable ValueSetExpansionOptions theExpansionOptions, @Nonnull IBaseResource theValueSetToExpand) {
		return null;
	}

	/**
	 * Load and return all conformance resources associated with this
	 * validation support module. This method may return null if it doesn't
	 * make sense for a given module.
	 */
	default List<IBaseResource> fetchAllConformanceResources() {
		return null;
	}

	/**
	 * Load and return all possible structure definitions
	 */
	default <T extends IBaseResource> List<T> fetchAllStructureDefinitions() {
		return null;
	}

	/**
	 * Fetch a code system by ID
	 *
	 * @param theSystem The code system
	 * @return The valueset (must not be null, but can be an empty ValueSet)
	 */
	default IBaseResource fetchCodeSystem(String theSystem) {
		return null;
	}

	/**
	 * Loads a resource needed by the validation (a StructureDefinition, or a
	 * ValueSet)
	 *
	 * @param theClass The type of the resource to load
	 * @param theUri   The resource URI
	 * @return Returns the resource, or <code>null</code> if no resource with the
	 * given URI can be found
	 */
	default <T extends IBaseResource> T fetchResource(Class<T> theClass, String theUri) {
		Validate.notNull(theClass, "theClass must not be null or blank");
		Validate.notBlank(theUri, "theUri must not be null or blank");

		switch (getFhirContext().getResourceType(theClass)) {
			case "StructureDefinition":
				return theClass.cast(fetchStructureDefinition(theUri));
			case "ValueSet":
				return theClass.cast(fetchValueSet(theUri));
			case "CodeSystem":
				return theClass.cast(fetchCodeSystem(theUri));
		}

		if (theUri.startsWith(URL_PREFIX_VALUE_SET)) {
			return theClass.cast(fetchValueSet(theUri));
		}

		return null;
	}

	default IBaseResource fetchStructureDefinition(String theUrl) {
		return null;
	}

	/**
	 * Returns <code>true</code> if codes in the given code system can be expanded
	 * or validated
	 *
	 * @param theValidationSupportContext The validation support module will be passed in to this method. This is convenient in cases where the operation needs to make calls to
	 *                                 other method in the support chain, so that they can be passed through the entire chain. Implementations of this interface may always safely ignore this parameter.
	 * @param theSystem                The URI for the code system, e.g. <code>"http://loinc.org"</code>
	 * @return Returns <code>true</code> if codes in the given code system can be
	 * validated
	 */
	default boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		return false;
	}

	/**
	 * Fetch the given ValueSet by URL
	 */
	default IBaseResource fetchValueSet(String theValueSetUrl) {
		return null;
	}

	/**
	 * Validates that the given code exists and if possible returns a display
	 * name. This method is called to check codes which are found in "example"
	 * binding fields (e.g. <code>Observation.code</code> in the default profile.
	 *
	 * @param theValidationSupportContext The validation support module will be passed in to this method. This is convenient in cases where the operation needs to make calls to
	 *                                 other method in the support chain, so that they can be passed through the entire chain. Implementations of this interface may always safely ignore this parameter.
	 * @param theOptions               Provides options controlling the validation
	 * @param theCodeSystem            The code system, e.g. "<code>http://loinc.org</code>"
	 * @param theCode                  The code, e.g. "<code>1234-5</code>"
	 * @param theDisplay               The display name, if it should also be validated
	 * @return Returns a validation result object
	 */
	default CodeValidationResult validateCode(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		return null;
	}

	/**
	 * Validates that the given code exists and if possible returns a display
	 * name. This method is called to check codes which are found in "example"
	 * binding fields (e.g. <code>Observation.code</code> in the default profile.
	 *
	 * @param theValidationSupportContext The validation support module will be passed in to this method. This is convenient in cases where the operation needs to make calls to
	 *                                 other method in the support chain, so that they can be passed through the entire chain. Implementations of this interface may always safely ignore this parameter.
	 * @param theCodeSystem            The code system, e.g. "<code>http://loinc.org</code>"
	 * @param theCode                  The code, e.g. "<code>1234-5</code>"
	 * @param theDisplay               The display name, if it should also be validated
	 * @param theValueSet              The ValueSet to validate against. Must not be null, and must be a ValueSet resource.
	 * @return Returns a validation result object, or <code>null</code> if this validation support module can not handle this kind of request
	 */
	default CodeValidationResult validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		return null;
	}

	/**
	 * Look up a code using the system and code value
	 *
	 * @param theValidationSupportContext The validation support module will be passed in to this method. This is convenient in cases where the operation needs to make calls to
	 *                                 other method in the support chain, so that they can be passed through the entire chain. Implementations of this interface may always safely ignore this parameter.
	 * @param theSystem                The CodeSystem URL
	 * @param theCode                  The code
	 */
	default LookupCodeResult lookupCode(ValidationSupportContext theValidationSupportContext, String theSystem, String theCode) {
		return null;
	}

	/**
	 * Returns <code>true</code> if the given valueset can be validated by the given
	 * validation support module
	 *
	 * @param theValidationSupportContext The validation support module will be passed in to this method. This is convenient in cases where the operation needs to make calls to
	 *                                 other method in the support chain, so that they can be passed through the entire chain. Implementations of this interface may always safely ignore this parameter.
	 * @param theValueSetUrl           The ValueSet canonical URL
	 */
	default boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {
		return false;
	}

	/**
	 * Generate a snapshot from the given differential profile.
	 *
	 * @param theValidationSupportContext The validation support module will be passed in to this method. This is convenient in cases where the operation needs to make calls to
	 *                                 other method in the support chain, so that they can be passed through the entire chain. Implementations of this interface may always safely ignore this parameter.
	 * @return Returns null if this module does not know how to handle this request
	 */
	default IBaseResource generateSnapshot(ValidationSupportContext theValidationSupportContext, IBaseResource theInput, String theUrl, String theWebUrl, String theProfileName) {
		return null;
	}

	/**
	 * Returns the FHIR Context associated with this module
	 */
	FhirContext getFhirContext();

	/**
	 * This method clears any temporary caches within the validation support. It is mainly intended for unit tests,
	 * but could be used in non-test scenarios as well.
	 */
	default void invalidateCaches() {
		// nothing
	}
}
