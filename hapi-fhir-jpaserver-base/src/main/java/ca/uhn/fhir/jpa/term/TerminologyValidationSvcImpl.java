/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.api.svc.ITerminologyValidationSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.base.Strings;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
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
	public CodeValidationResult validateCodeAgainstValueSet(
			IIdType theValueSetId,
			IPrimitiveType<String> theValueSetUrl,
			IPrimitiveType<String> theValueSetVersion,
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theSystem,
			IPrimitiveType<String> theSystemVersion,
			IPrimitiveType<String> theDisplay,
			IBaseCoding theCoding,
			IBaseDatatype theCodeableConcept,
			RequestDetails theRequestDetails) {

		// If a Remote Terminology Server has been configured, use it
		if (myValidationSupport != null && myValidationSupport.isRemoteTerminologyServiceConfigured()) {
			String systemString = getStringFromPrimitiveType(theSystem);
			String codeString = getStringFromPrimitiveType(theCode);
			String displayString = getStringFromPrimitiveType(theDisplay);
			String valueSetUrlString = getStringFromPrimitiveType(theValueSetUrl);

			if (theCoding != null) {
				if (isNotBlank(theCoding.getSystem())) {
					if (systemString != null && !systemString.equalsIgnoreCase(theCoding.getSystem())) {
						throw new InvalidRequestException(Msg.code(2352) + "Coding.system '" + theCoding.getSystem()
								+ "' does not equal param system '" + systemString
								+ "'. Unable to validate-code.");
					}
					systemString = theCoding.getSystem();
					codeString = theCoding.getCode();
					displayString = theCoding.getDisplay();
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
		if (theValueSetUrl != null && theValueSetVersion != null) {
			valueSetIdentifier = createUriWithVersion(theValueSetUrl, theValueSetVersion);
		} else {
			valueSetIdentifier = theValueSetUrl;
		}

		IPrimitiveType<String> codeSystemIdentifier;
		if (theSystem != null && theSystemVersion != null) {
			codeSystemIdentifier = createUriWithVersion(theSystem, theSystemVersion);
		} else {
			codeSystemIdentifier = theSystem;
		}

		return dao.validateCode(
				valueSetIdentifier,
				theValueSetId,
				theCode,
				codeSystemIdentifier,
				theDisplay,
				theCoding,
				theCodeableConcept,
				theRequestDetails);
	}

	@Override
	public CodeValidationResult validateCodeAgainstCodeSystem(
			IIdType theCodeSystemId,
			IPrimitiveType<String> theCodeSystemUrl,
			IPrimitiveType<String> theVersion,
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theDisplay,
			IBaseCoding theCoding,
			IBaseDatatype theCodeableConcept,
			RequestDetails theRequestDetails) {

		// If a Remote Terminology Server has been configured, use it
		if (myValidationSupport != null && myValidationSupport.isRemoteTerminologyServiceConfigured()) {
			String code;
			String display;

			// The specification for $validate-code says that only one of these input-param combinations should be
			// provided:
			// 1.- code/codeSystem url
			// 2.- coding (which wraps one code/codeSystem url combo)
			// 3.- a codeableConcept (which wraps potentially many code/codeSystem url combos)
			String url = getStringFromPrimitiveType(theCodeSystemUrl);

			if (theCoding != null && isNotBlank(theCoding.getSystem())) {
				// Coding case
				if (url != null && !url.equalsIgnoreCase(theCoding.getSystem())) {
					throw new InvalidRequestException(Msg.code(1160) + "Coding.system '" + theCoding.getSystem()
							+ "' does not equal param url '" + url
							+ "'. Unable to validate-code.");
				}
				url = theCoding.getSystem();
				code = theCoding.getCode();
				display = theCoding.getDisplay();
				return validateCodeWithTerminologyService(url, code, display, null)
						.orElseGet(supplyUnableToValidateResultForCodeSystem(url, code));
			} else if (theCodeableConcept != null && !theCodeableConcept.isEmpty()) {
				// CodeableConcept case
				return new CodeValidationResult()
						.setMessage("Terminology service does not yet support codeable concepts.");
			} else {
				// code/systemUrl combo case
				code = getStringFromPrimitiveType(theCode);
				display = getStringFromPrimitiveType(theDisplay);
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
				theCodeSystemId,
				theCodeSystemUrl,
				theVersion,
				theCode,
				theDisplay,
				theCoding,
				theCodeableConcept,
				theRequestDetails);
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
		IPrimitiveType<String> result = (IPrimitiveType<String>)
				Objects.requireNonNull(myFhirContext.getElementDefinition("uri")).newInstance();
		result.setValue(theUrl.getValue() + "|" + theVersion.getValue());
		return result;
	}

	private static @Nullable String getStringFromPrimitiveType(IPrimitiveType<String> thePrimitiveString) {
		return (thePrimitiveString != null && thePrimitiveString.hasValue())
				? thePrimitiveString.getValueAsString()
				: null;
	}
}
