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
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.svc.CodeSystemValidationRequest;
import ca.uhn.fhir.jpa.api.svc.ITerminologyValidationSvc;
import ca.uhn.fhir.jpa.api.svc.ValueSetValidationRequest;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.base.Strings;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Implementation of {@link ITerminologyValidationSvc} that provides code validation
 * against ValueSets and CodeSystems.
 * <p>
 * This service handles both remote terminology service delegation (when configured)
 * and local DAO layer validation as a fallback.
 * </p>
 *
 * // Created by claude-opus-4-5-20251101
 */
public class TerminologyValidationSvcImpl implements ITerminologyValidationSvc {

	private final FhirContext myFhirContext;
	private final IValidationSupport myValidationSupport;
	private final DaoRegistry myDaoRegistry;

	/**
	 * Constructor.
	 *
	 * @param theFhirContext FHIR context for this service
	 * @param theValidationSupport The validation support chain (includes remote terminology if configured)
	 * @param theDaoRegistry The DAO registry for local validation
	 */
	public TerminologyValidationSvcImpl(
			FhirContext theFhirContext, IValidationSupport theValidationSupport, DaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		myValidationSupport = theValidationSupport;
		myDaoRegistry = theDaoRegistry;
	}

	@Override
	public CodeValidationResult validateCodeAgainstValueSet(ValueSetValidationRequest theRequest) {
		// If a Remote Terminology Server has been configured, use it
		if (myValidationSupport != null && myValidationSupport.isRemoteTerminologyServiceConfigured()) {
			String systemString = getStringFromPrimitiveType(theRequest.system());
			String codeString = getStringFromPrimitiveType(theRequest.code());
			String displayString = getStringFromPrimitiveType(theRequest.display());
			String valueSetUrlString = getStringFromPrimitiveType(theRequest.valueSetUrl());

			IBaseCoding coding = theRequest.coding();
			if (coding != null) {
				if (isNotBlank(coding.getSystem())) {
					if (systemString != null && !systemString.equalsIgnoreCase(coding.getSystem())) {
						throw new InvalidRequestException(Msg.code(2352) + "Coding.system '" + coding.getSystem()
								+ "' does not equal param system '" + systemString
								+ "'. Unable to validate-code.");
					}
					systemString = coding.getSystem();
					codeString = coding.getCode();
					displayString = coding.getDisplay();
				}
			}

			return validateCodeWithTerminologyService(systemString, codeString, displayString, valueSetUrlString)
					.orElseGet(supplyUnableToValidateResult(systemString, codeString, valueSetUrlString));
		}

		// Otherwise, use the local DAO layer to validate the code
		@SuppressWarnings("unchecked")
		IFhirResourceDaoValueSet<IBaseResource> dao =
				(IFhirResourceDaoValueSet<IBaseResource>) myDaoRegistry.getResourceDao("ValueSet");

		IPrimitiveType<String> valueSetIdentifier;
		if (theRequest.valueSetUrl() != null && theRequest.valueSetVersion() != null) {
			valueSetIdentifier = createUriWithVersion(theRequest.valueSetUrl(), theRequest.valueSetVersion());
		} else {
			valueSetIdentifier = theRequest.valueSetUrl();
		}

		IPrimitiveType<String> codeSystemIdentifier;
		if (theRequest.system() != null && theRequest.systemVersion() != null) {
			codeSystemIdentifier = createUriWithVersion(theRequest.system(), theRequest.systemVersion());
		} else {
			codeSystemIdentifier = theRequest.system();
		}

		return dao.validateCode(
				valueSetIdentifier,
				theRequest.valueSetId(),
				theRequest.code(),
				codeSystemIdentifier,
				theRequest.display(),
				theRequest.coding(),
				theRequest.codeableConcept(),
				theRequest.requestDetails());
	}

	@Override
	public CodeValidationResult validateCodeAgainstCodeSystem(CodeSystemValidationRequest theRequest) {
		// TODO: JA why not just always just the chain here? and we can then get rid
		// of the corresponding DAO method entirely

		// If a Remote Terminology Server has been configured, use it
		if (myValidationSupport != null && myValidationSupport.isRemoteTerminologyServiceConfigured()) {
			String code;
			String display;

			// The specification for $validate-code says that only one of these input-param combinations should be
			// provided:
			// 1.- code/codeSystem url
			// 2.- coding (which wraps one code/codeSystem url combo)
			// 3.- a codeableConcept (which wraps potentially many code/codeSystem url combos)
			String url = getStringFromPrimitiveType(theRequest.codeSystemUrl());

			IBaseCoding coding = theRequest.coding();
			if (coding != null && isNotBlank(coding.getSystem())) {
				// Coding case
				if (url != null && !url.equalsIgnoreCase(coding.getSystem())) {
					throw new InvalidRequestException(Msg.code(1160) + "Coding.system '" + coding.getSystem()
							+ "' does not equal param url '" + url
							+ "'. Unable to validate-code.");
				}
				url = coding.getSystem();
				code = coding.getCode();
				display = coding.getDisplay();
				return validateCodeWithTerminologyService(url, code, display, null)
						.orElseGet(supplyUnableToValidateResultForCodeSystem(url, code));
			} else if (theRequest.codeableConcept() != null
					&& !theRequest.codeableConcept().isEmpty()) {
				// CodeableConcept case
				return new CodeValidationResult()
						.setMessage("Terminology service does not yet support codeable concepts.");
			} else {
				// code/systemUrl combo case
				code = getStringFromPrimitiveType(theRequest.code());
				display = getStringFromPrimitiveType(theRequest.display());
				if (Strings.isNullOrEmpty(code) || Strings.isNullOrEmpty(url)) {
					return new CodeValidationResult()
							.setMessage("When specifying systemUrl and code, neither can be empty");
				}
				return validateCodeWithTerminologyService(url, code, display, null)
						.orElseGet(supplyUnableToValidateResultForCodeSystem(url, code));
			}
		}

		// Otherwise, use the local DAO layer to validate the code
		@SuppressWarnings("unchecked")
		IFhirResourceDaoCodeSystem<IBaseResource> dao =
				(IFhirResourceDaoCodeSystem<IBaseResource>) myDaoRegistry.getResourceDao("CodeSystem");

		return dao.validateCode(
				theRequest.codeSystemId(),
				theRequest.codeSystemUrl(),
				theRequest.version(),
				theRequest.code(),
				theRequest.display(),
				theRequest.coding(),
				theRequest.codeableConcept(),
				theRequest.requestDetails());
	}

	private Optional<CodeValidationResult> validateCodeWithTerminologyService(
			String theSystem, String theCode, String theDisplay, String theValueSetUrl) {
		return Optional.ofNullable(myValidationSupport.validateCode(
				new ValidationSupportContext(myValidationSupport),
				new ConceptValidationOptions(),
				theSystem,
				theCode,
				theDisplay,
				theValueSetUrl));
	}

	private Supplier<CodeValidationResult> supplyUnableToValidateResult(
			String theSystem, String theCode, String theValueSetUrl) {
		return () -> new CodeValidationResult()
				.setMessage("Validator is unable to provide validation for " + theCode + "#" + theSystem
						+ " - Unknown or unusable ValueSet[" + theValueSetUrl + "]");
	}

	private Supplier<CodeValidationResult> supplyUnableToValidateResultForCodeSystem(
			String theCodeSystemUrl, String theCode) {
		return () -> new CodeValidationResult()
				.setMessage(
						"Terminology service was unable to provide validation for " + theCodeSystemUrl + "#" + theCode);
	}

	@SuppressWarnings("unchecked")
	private IPrimitiveType<String> createUriWithVersion(
			IPrimitiveType<String> theUrl, IPrimitiveType<String> theVersion) {
		IPrimitiveType<String> result =
				(IPrimitiveType<String>) Objects.requireNonNull(myFhirContext.getElementDefinition("uri"))
						.newInstance();
		result.setValue(theUrl.getValue() + "|" + theVersion.getValue());
		return result;
	}

	private static @Nullable String getStringFromPrimitiveType(IPrimitiveType<String> thePrimitiveString) {
		return (thePrimitiveString != null && thePrimitiveString.hasValue())
				? thePrimitiveString.getValueAsString()
				: null;
	}
}
