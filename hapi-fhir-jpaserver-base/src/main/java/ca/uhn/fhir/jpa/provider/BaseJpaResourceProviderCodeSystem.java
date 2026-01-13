/*
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
package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChain;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseJpaResourceProviderCodeSystem<T extends IBaseResource> extends BaseJpaResourceProvider<T> {

	@Autowired
	private JpaValidationSupportChain myValidationSupportChain;

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

	/**
	 * $lookup operation
	 */
	@SuppressWarnings("unchecked")
	@Operation(
			name = JpaConstants.OPERATION_LOOKUP,
			idempotent = true,
			returnParameters = {
				@OperationParam(name = "name", typeName = "string", min = 1),
				@OperationParam(name = "version", typeName = "string", min = 0),
				@OperationParam(name = "display", typeName = "string", min = 1),
				@OperationParam(name = "abstract", typeName = "boolean", min = 1),
				@OperationParam(name = "property", min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "code")
			})
	public IBaseParameters lookup(
			HttpServletRequest theServletRequest,
			@OperationParam(name = "code", min = 0, max = 1, typeName = "code") IPrimitiveType<String> theCode,
			@OperationParam(name = "system", min = 0, max = 1, typeName = "uri") IPrimitiveType<String> theSystem,
			@OperationParam(name = "coding", min = 0, max = 1, typeName = "Coding") IBaseCoding theCoding,
			@OperationParam(name = "version", min = 0, max = 1, typeName = "string") IPrimitiveType<String> theVersion,
			@OperationParam(name = "displayLanguage", min = 0, max = 1, typeName = "code")
					IPrimitiveType<String> theDisplayLanguage,
			@OperationParam(name = "property", min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "code")
					List<IPrimitiveType<String>> thePropertyNames,
			RequestDetails theRequestDetails) {

		startRequest(theServletRequest);
		try {
			IFhirResourceDaoCodeSystem dao = (IFhirResourceDaoCodeSystem) getDao();
			IValidationSupport.LookupCodeResult result;
			applyVersionToSystem(theSystem, theVersion);
			result = dao.lookupCode(
					theCode, theSystem, theCoding, theDisplayLanguage, thePropertyNames, theRequestDetails);
			result.throwNotFoundIfAppropriate();
			return result.toParameters(theRequestDetails.getFhirContext(), thePropertyNames);
		} finally {
			endRequest(theServletRequest);
		}
	}

	/**
	 * $subsumes operation
	 */
	@Operation(
			name = JpaConstants.OPERATION_SUBSUMES,
			idempotent = true,
			returnParameters = {
				@OperationParam(name = "outcome", typeName = "code", min = 1),
			})
	public IBaseParameters subsumes(
			HttpServletRequest theServletRequest,
			@OperationParam(name = "codeA", min = 0, max = 1, typeName = "code") IPrimitiveType<String> theCodeA,
			@OperationParam(name = "codeB", min = 0, max = 1, typeName = "code") IPrimitiveType<String> theCodeB,
			@OperationParam(name = "system", min = 0, max = 1, typeName = "uri") IPrimitiveType<String> theSystem,
			@OperationParam(name = "codingA", min = 0, max = 1, typeName = "Coding") IBaseCoding theCodingA,
			@OperationParam(name = "codingB", min = 0, max = 1, typeName = "Coding") IBaseCoding theCodingB,
			@OperationParam(name = "version", min = 0, max = 1, typeName = "string") IPrimitiveType<String> theVersion,
			RequestDetails theRequestDetails) {

		startRequest(theServletRequest);
		try {
			IFhirResourceDaoCodeSystem dao = (IFhirResourceDaoCodeSystem) getDao();
			IFhirResourceDaoCodeSystem.SubsumesResult result;
			applyVersionToSystem(theSystem, theVersion);
			result = dao.subsumes(theCodeA, theCodeB, theSystem, theCodingA, theCodingB, theRequestDetails);
			return result.toParameters(theRequestDetails.getFhirContext());
		} finally {
			endRequest(theServletRequest);
		}
	}

	static void applyVersionToSystem(IPrimitiveType<String> theSystem, IPrimitiveType<String> theVersion) {
		if (theVersion != null && isNotBlank(theVersion.getValueAsString()) && theSystem != null) {
			theSystem.setValue(theSystem.getValueAsString() + "|" + theVersion.getValueAsString());
		}
	}

	/**
	 * $validate-code operation
	 */
	@Operation(
			name = JpaConstants.OPERATION_VALIDATE_CODE,
			idempotent = true,
			returnParameters = {
				@OperationParam(name = "result", typeName = "boolean", min = 1),
				@OperationParam(name = "message", typeName = "string"),
				@OperationParam(name = "display", typeName = "string")
			})
	public IBaseParameters validateCode(
			HttpServletRequest theServletRequest,
			@IdParam(optional = true) IIdType theId,
			@OperationParam(name = "url", min = 0, max = 1, typeName = "uri") IPrimitiveType<String> theUrl,
			@OperationParam(name = "version", min = 0, max = 1, typeName = "string") IPrimitiveType<String> theVersion,
			@OperationParam(name = "code", min = 0, max = 1, typeName = "code") IPrimitiveType<String> theCode,
			@OperationParam(name = "display", min = 0, max = 1, typeName = "string") IPrimitiveType<String> theDisplay,
			@OperationParam(name = "coding", min = 0, max = 1, typeName = "Coding") IBaseCoding theCoding,
			@OperationParam(name = "codeableConcept", min = 0, max = 1, typeName = "CodeableConcept")
					IBaseDatatype theCodeableConcept,
			RequestDetails theRequestDetails) {

		startRequest(theServletRequest);
		try {
			// Determine the CodeSystem URL to validate against
			String codeSystemUrl = resolveCodeSystemUrl(theId, theUrl, theRequestDetails);

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
				return validateCodeableConcept(codeableConcept.getCoding(), codeSystemUrl, theVersion);
			}

			// Handle coding parameter
			if (haveCoding) {
				String systemUrl = canonicalCoding.hasSystem() ? canonicalCoding.getSystem() : codeSystemUrl;
				validateSystemsMatch(codeSystemUrl, systemUrl);
				String versionedSystem = createVersionedSystemIfVersionIsPresent(systemUrl, getStringValue(theVersion));
				return validateSingleCode(versionedSystem, canonicalCoding.getCode(), canonicalCoding.getDisplay());
			}

			// Handle code/system parameters
			if (haveCode) {
				String versionedSystem =
						createVersionedSystemIfVersionIsPresent(codeSystemUrl, getStringValue(theVersion));
				return validateSingleCode(versionedSystem, theCode.getValueAsString(), getStringValue(theDisplay));
			}

			// No valid input provided
			CodeValidationResult result =
					new CodeValidationResult().setMessage("No code, coding, or codeableConcept provided to validate");
			return result.toParameters(getContext());
		} finally {
			endRequest(theServletRequest);
		}
	}

	private String resolveCodeSystemUrl(
			IIdType theId, IPrimitiveType<String> theUrl, RequestDetails theRequestDetails) {
		// If an ID was provided, look up the CodeSystem URL from the resource
		if (theId != null && theId.hasIdPart()) {
			IFhirResourceDaoCodeSystem<T> dao = (IFhirResourceDaoCodeSystem<T>) getDao();
			IBaseResource codeSystem = dao.read(theId, theRequestDetails);
			return CommonCodeSystemsTerminologyService.getCodeSystemUrl(getContext(), codeSystem);
		}
		return getStringValue(theUrl);
	}

	private IBaseParameters validateCodeableConcept(
			List<Coding> theCodings, String theCodeSystemUrl, IPrimitiveType<String> theVersion) {
		CodeValidationResult lastResult = null;
		for (Coding coding : theCodings) {
			String systemUrl = coding.hasSystem() ? coding.getSystem() : theCodeSystemUrl;
			if (theCodeSystemUrl != null && coding.hasSystem()) {
				validateSystemsMatch(theCodeSystemUrl, coding.getSystem());
			}
			String versionedSystem = createVersionedSystemIfVersionIsPresent(systemUrl, getStringValue(theVersion));
			CodeValidationResult result = validateCodeWithTerminologyService(
							versionedSystem, coding.getCode(), coding.getDisplay())
					.orElseGet(supplyUnableToValidateResult(versionedSystem, coding.getCode()));
			lastResult = result;
			if (result.isOk()) {
				return result.toParameters(getContext());
			}
		}
		// Return the last result (even if failed) or create a default error
		if (lastResult == null) {
			lastResult = new CodeValidationResult().setMessage("No codings found in codeableConcept");
		}
		return lastResult.toParameters(getContext());
	}

	private void validateSystemsMatch(String theExpectedSystem, String theActualSystem) {
		if (theExpectedSystem != null && !theExpectedSystem.equalsIgnoreCase(theActualSystem)) {
			throw new InvalidRequestException(Msg.code(1160) + "Coding.system '" + theActualSystem
					+ "' does not equal param url '" + theExpectedSystem + "'. Unable to validate-code.");
		}
	}

	private IBaseParameters validateSingleCode(String theSystem, String theCode, String theDisplay) {
		CodeValidationResult result = validateCodeWithTerminologyService(theSystem, theCode, theDisplay)
				.orElseGet(supplyUnableToValidateResult(theSystem, theCode));
		return result.toParameters(getContext());
	}

	private static String createVersionedSystemIfVersionIsPresent(String theSystem, String theVersion) {
		if (isNotBlank(theVersion)) {
			return theSystem + "|" + theVersion;
		}
		return theSystem;
	}

	private static @Nullable String getStringValue(IPrimitiveType<String> thePrimitive) {
		return (thePrimitive != null && thePrimitive.hasValue()) ? thePrimitive.getValueAsString() : null;
	}

	private Optional<CodeValidationResult> validateCodeWithTerminologyService(
			String theCodeSystemUrl, String theCode, String theDisplay) {
		return Optional.ofNullable(myValidationSupportChain.validateCode(
				new ValidationSupportContext(myValidationSupportChain),
				new ConceptValidationOptions(),
				theCodeSystemUrl,
				theCode,
				theDisplay,
				null));
	}

	private Supplier<CodeValidationResult> supplyUnableToValidateResult(String theCodeSystemUrl, String theCode) {
		return () -> new CodeValidationResult()
				.setMessage(
						"Terminology service was unable to provide validation for " + theCodeSystemUrl + "#" + theCode);
	}
}
