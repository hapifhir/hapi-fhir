/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
// Created by claude-opus-4-5-20251101
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Helper class for invoking terminology validation operations.
 * Consolidates common validation logic used by both ValueSet and CodeSystem providers.
 */
public class ValidationInvocationHelper {

	private final FhirContext myFhirContext;
	private final IValidationSupport myValidationSupportChain;
	private final VersionCanonicalizer myVersionCanonicalizer;

	/**
	 * Constructor
	 *
	 * @param theFhirContext The FHIR context
	 * @param theValidationSupportChain The validation support chain for terminology operations
	 * @param theVersionCanonicalizer Converter for canonicalizing FHIR versions
	 */
	public ValidationInvocationHelper(
			FhirContext theFhirContext,
			IValidationSupport theValidationSupportChain,
			VersionCanonicalizer theVersionCanonicalizer) {
		myFhirContext = theFhirContext;
		myValidationSupportChain = theValidationSupportChain;
		myVersionCanonicalizer = theVersionCanonicalizer;
	}

	/**
	 * Invokes $validate-code operation for a ValueSet.
	 * This method handles validation of code, coding, or codeableConcept parameters against a ValueSet.
	 *
	 * @param theCode The code to validate
	 * @param theSystem The code system
	 * @param theSystemVersion The code system version
	 * @param theDisplay The display text for the code
	 * @param theCoding A coding to validate
	 * @param theCodeableConcept A codeableConcept to validate
	 * @param theValueSetUrl The ValueSet URL to validate against
	 * @return Parameters containing the validation result
	 */
	public IBaseParameters invokeValidateCodeForValueSet(
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theSystem,
			IPrimitiveType<String> theSystemVersion,
			IPrimitiveType<String> theDisplay,
			IBaseCoding theCoding,
			ICompositeType theCodeableConcept,
			String theValueSetUrl) {

		// Convert codeableConcept to canonical form
		CodeableConcept codeableConcept = myVersionCanonicalizer.codeableConceptToCanonical(theCodeableConcept);
		boolean haveCodeableConcept =
				codeableConcept != null && !codeableConcept.getCoding().isEmpty();

		// Convert coding to canonical form
		Coding canonicalCoding = myVersionCanonicalizer.codingToCanonical(theCoding);
		boolean haveCoding = canonicalCoding != null && !canonicalCoding.isEmpty();

		boolean haveCode = theCode != null && theCode.hasValue();

		// Handle codeableConcept
		if (haveCodeableConcept) {
			return validateCodeableConceptForValueSet(codeableConcept.getCoding(), theValueSetUrl);
		}

		// Handle coding parameter
		if (haveCoding) {
			validateSystemsMatchForValueSet(theSystem, canonicalCoding);
			String systemString =
					createVersionedSystemIfVersionIsPresent(canonicalCoding.getSystem(), canonicalCoding.getVersion());
			return validateSingleCodeForValueSet(
					systemString, canonicalCoding.getCode(), canonicalCoding.getDisplay(), theValueSetUrl);
		}

		// Handle code/system parameters
		if (haveCode) {
			String systemString = getStringValue(theSystem);
			if (systemString != null && theSystemVersion != null && theSystemVersion.hasValue()) {
				systemString = systemString + "|" + theSystemVersion.getValueAsString();
			}
			return validateSingleCodeForValueSet(
					systemString, theCode.getValueAsString(), getStringValue(theDisplay), theValueSetUrl);
		}

		// No valid input provided
		CodeValidationResult result =
				new CodeValidationResult().setMessage("No code, coding, or codeableConcept provided to validate");
		return result.toParameters(myFhirContext);
	}

	/**
	 * Invokes $validate-code operation for a CodeSystem.
	 * This method handles validation of code, coding, or codeableConcept parameters against a CodeSystem.
	 *
	 * @param theCode The code to validate
	 * @param theVersion The code system version
	 * @param theDisplay The display text for the code
	 * @param theCoding A coding to validate
	 * @param theCodeableConcept A codeableConcept to validate
	 * @param theCodeSystemUrl The CodeSystem URL to validate against
	 * @return Parameters containing the validation result
	 */
	public IBaseParameters invokeValidateCodeForCodeSystem(
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theVersion,
			IPrimitiveType<String> theDisplay,
			IBaseCoding theCoding,
			IBaseDatatype theCodeableConcept,
			String theCodeSystemUrl) {

		// Convert codeableConcept to canonical form
		CodeableConcept codeableConcept = myVersionCanonicalizer.codeableConceptToCanonical(theCodeableConcept);
		boolean haveCodeableConcept =
				codeableConcept != null && !codeableConcept.getCoding().isEmpty();

		// Convert coding to canonical form
		Coding canonicalCoding = myVersionCanonicalizer.codingToCanonical(theCoding);
		boolean haveCoding = canonicalCoding != null && !canonicalCoding.isEmpty();

		boolean haveCode = theCode != null && theCode.hasValue();

		// Handle codeableConcept - validate each coding until one succeeds
		if (haveCodeableConcept) {
			return validateCodeableConceptForCodeSystem(codeableConcept.getCoding(), theCodeSystemUrl, theVersion);
		}

		// Handle coding parameter
		if (haveCoding) {
			String systemUrl = canonicalCoding.hasSystem() ? canonicalCoding.getSystem() : theCodeSystemUrl;
			validateSystemsMatchForCodeSystem(theCodeSystemUrl, systemUrl);
			String versionedSystem = createVersionedSystemIfVersionIsPresent(systemUrl, getStringValue(theVersion));
			return validateSingleCodeForCodeSystem(
					versionedSystem, canonicalCoding.getCode(), canonicalCoding.getDisplay());
		}

		// Handle code/system parameters
		if (haveCode) {
			String versionedSystem =
					createVersionedSystemIfVersionIsPresent(theCodeSystemUrl, getStringValue(theVersion));
			return validateSingleCodeForCodeSystem(
					versionedSystem, theCode.getValueAsString(), getStringValue(theDisplay));
		}

		// No valid input provided
		CodeValidationResult result =
				new CodeValidationResult().setMessage("No code, coding, or codeableConcept provided to validate");
		return result.toParameters(myFhirContext);
	}

	/**
	 * Validates a codeableConcept against a ValueSet by iterating through codings until one succeeds.
	 */
	private IBaseParameters validateCodeableConceptForValueSet(List<Coding> theCodings, String theValueSetUrl) {
		CodeValidationResult lastResult = null;
		for (Coding coding : theCodings) {
			String systemString = createVersionedSystemIfVersionIsPresent(coding.getSystem(), coding.getVersion());
			CodeValidationResult result = validateCodeWithTerminologyService(
							systemString, coding.getCode(), coding.getDisplay(), theValueSetUrl)
					.orElseGet(supplyUnableToValidateResultForValueSet(systemString, coding.getCode(), theValueSetUrl));
			lastResult = result;
			if (result.isOk()) {
				return result.toParameters(myFhirContext);
			}
		}
		// Return the last result (even if failed) or create a default error
		if (lastResult == null) {
			lastResult = new CodeValidationResult().setMessage("No codings found in codeableConcept");
		}
		return lastResult.toParameters(myFhirContext);
	}

	/**
	 * Validates a codeableConcept against a CodeSystem by iterating through codings until one succeeds.
	 */
	private IBaseParameters validateCodeableConceptForCodeSystem(
			List<Coding> theCodings, String theCodeSystemUrl, IPrimitiveType<String> theVersion) {
		CodeValidationResult lastResult = null;
		for (Coding coding : theCodings) {
			String systemUrl = coding.hasSystem() ? coding.getSystem() : theCodeSystemUrl;
			if (theCodeSystemUrl != null && coding.hasSystem()) {
				validateSystemsMatchForCodeSystem(theCodeSystemUrl, coding.getSystem());
			}
			String versionedSystem = createVersionedSystemIfVersionIsPresent(systemUrl, getStringValue(theVersion));
			CodeValidationResult result = validateCodeWithTerminologyService(
							versionedSystem, coding.getCode(), coding.getDisplay(), null)
					.orElseGet(supplyUnableToValidateResultForCodeSystem(versionedSystem, coding.getCode()));
			lastResult = result;
			if (result.isOk()) {
				return result.toParameters(myFhirContext);
			}
		}
		// Return the last result (even if failed) or create a default error
		if (lastResult == null) {
			lastResult = new CodeValidationResult().setMessage("No codings found in codeableConcept");
		}
		return lastResult.toParameters(myFhirContext);
	}

	/**
	 * Validates that system parameter matches coding.system if both are provided (ValueSet validation).
	 */
	private void validateSystemsMatchForValueSet(IPrimitiveType<String> theSystem, Coding canonicalCoding) {
		String paramSystemString = getStringValue(theSystem);
		if (isNotBlank(canonicalCoding.getSystem())
				&& paramSystemString != null
				&& !paramSystemString.equalsIgnoreCase(canonicalCoding.getSystem())) {
			throw new InvalidRequestException(Msg.code(2352) + "Coding.system '" + canonicalCoding.getSystem()
					+ "' does not equal param system '" + paramSystemString + "'. Unable to validate-code.");
		}
	}

	/**
	 * Validates that system URLs match (CodeSystem validation).
	 */
	private void validateSystemsMatchForCodeSystem(String theExpectedSystem, String theActualSystem) {
		if (theExpectedSystem != null && !theExpectedSystem.equalsIgnoreCase(theActualSystem)) {
			throw new InvalidRequestException(Msg.code(1160) + "Coding.system '" + theActualSystem
					+ "' does not equal param url '" + theExpectedSystem + "'. Unable to validate-code.");
		}
	}

	/**
	 * Validates a single code against a ValueSet.
	 */
	private IBaseParameters validateSingleCodeForValueSet(
			String theSystem, String theCode, String theDisplay, String theValueSetUrl) {
		CodeValidationResult result = validateCodeWithTerminologyService(theSystem, theCode, theDisplay, theValueSetUrl)
				.orElseGet(supplyUnableToValidateResultForValueSet(theSystem, theCode, theValueSetUrl));
		return result.toParameters(myFhirContext);
	}

	/**
	 * Validates a single code against a CodeSystem.
	 */
	private IBaseParameters validateSingleCodeForCodeSystem(String theSystem, String theCode, String theDisplay) {
		CodeValidationResult result = validateCodeWithTerminologyService(theSystem, theCode, theDisplay, null)
				.orElseGet(supplyUnableToValidateResultForCodeSystem(theSystem, theCode));
		return result.toParameters(myFhirContext);
	}

	/**
	 * Creates a versioned system URL by appending version if present.
	 */
	private static String createVersionedSystemIfVersionIsPresent(String theSystem, String theVersion) {
		if (isNotBlank(theVersion)) {
			return theSystem + "|" + theVersion;
		}
		return theSystem;
	}

	/**
	 * Extracts the string value from a primitive type.
	 */
	private static @Nullable String getStringValue(IPrimitiveType<String> thePrimitive) {
		return (thePrimitive != null && thePrimitive.hasValue()) ? thePrimitive.getValueAsString() : null;
	}

	/**
	 * Invokes the validation support chain to validate a code.
	 */
	private Optional<CodeValidationResult> validateCodeWithTerminologyService(
			String theSystem, String theCode, String theDisplay, String theValueSetUrl) {
		return Optional.ofNullable(myValidationSupportChain.validateCode(
				new ValidationSupportContext(myValidationSupportChain),
				new ConceptValidationOptions(),
				theSystem,
				theCode,
				theDisplay,
				theValueSetUrl));
	}

	/**
	 * Supplies a fallback error result when ValueSet validation cannot be performed.
	 */
	private Supplier<CodeValidationResult> supplyUnableToValidateResultForValueSet(
			String theSystem, String theCode, String theValueSetUrl) {
		return () -> new CodeValidationResult()
				.setMessage("Validator is unable to provide validation for " + theCode + "#" + theSystem
						+ " - Unknown or unusable ValueSet[" + theValueSetUrl + "]");
	}

	/**
	 * Supplies a fallback error result when CodeSystem validation cannot be performed.
	 */
	private Supplier<CodeValidationResult> supplyUnableToValidateResultForCodeSystem(
			String theCodeSystemUrl, String theCode) {
		return () -> new CodeValidationResult()
				.setMessage(
						"Terminology service was unable to provide validation for " + theCodeSystemUrl + "#" + theCode);
	}
}
