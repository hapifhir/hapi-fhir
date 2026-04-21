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
import com.google.common.base.Strings;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseJpaResourceProviderCodeSystem<T extends IBaseResource> extends BaseJpaResourceProvider<T> {

	@Autowired
	private JpaValidationSupportChain myValidationSupportChain;

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
	@SuppressWarnings("unchecked")
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

		CodeValidationResult result = null;
		startRequest(theServletRequest);
		try {
			// TODO: JA why not just always just the chain here? and we can then get rid of the corresponding DAO method
			// entirely
			// If a Remote Terminology Server has been configured, use it
			if (myValidationSupportChain.isRemoteTerminologyServiceConfigured()) {

				String code;
				String display;

				// The specification for $validate-code says that only one of these input-param combinations should be
				// provided:
				// 1.- code/codeSystem url
				// 2.- coding (which wraps one code/codeSystem url combo)
				// 3.- a codeableConcept (which wraps potentially many code/codeSystem url combos)
				String url = getStringFromPrimitiveType(theUrl);

				if (theCoding != null && isNotBlank(theCoding.getSystem())) {
					// Coding case
					if (url != null && !url.equalsIgnoreCase(theCoding.getSystem())) {
						throw new InvalidRequestException(Msg.code(1160) + "Coding.system '" + theCoding.getSystem()
								+ "' does not equal param url '" + theUrl
								+ "'. Unable to validate-code.");
					}
					url = theCoding.getSystem();
					code = theCoding.getCode();
					display = theCoding.getDisplay();
					result = validateCodeWithTerminologyService(url, code, display)
							.orElseGet(supplyUnableToValidateResult(url, code));
				} else if (theCodeableConcept != null && !theCodeableConcept.isEmpty()) {
					// CodeableConcept case
					result = new CodeValidationResult()
							.setMessage("Terminology service does not yet support codeable concepts.");
				} else {
					// code/systemUrl combo case
					code = getStringFromPrimitiveType(theCode);
					display = getStringFromPrimitiveType(theDisplay);
					if (Strings.isNullOrEmpty(code) || Strings.isNullOrEmpty(url)) {
						result = new CodeValidationResult()
								.setMessage("When specifying systemUrl and code, neither can be empty");
					} else {
						result = validateCodeWithTerminologyService(url, code, display)
								.orElseGet(supplyUnableToValidateResult(url, code));
					}
				}

			} else {
				// Otherwise, use the local DAO layer to validate the code
				IFhirResourceDaoCodeSystem dao = (IFhirResourceDaoCodeSystem) getDao();
				result = dao.validateCode(
						theId,
						theUrl,
						theVersion,
						theCode,
						theDisplay,
						theCoding,
						theCodeableConcept,
						theRequestDetails);
			}

			return result.toParameters(getContext());
		} finally {
			endRequest(theServletRequest);
		}
	}

	private static @Nullable String getStringFromPrimitiveType(IPrimitiveType<String> thePrimitiveString) {
		return (thePrimitiveString != null && thePrimitiveString.hasValue())
				? thePrimitiveString.getValueAsString()
				: null;
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
