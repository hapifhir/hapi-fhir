package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseJpaResourceProviderValueSetDstu2;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR4;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

public class BaseJpaResourceProviderCodeSystemR4 extends JpaResourceProviderR4<CodeSystem> {

	@Autowired
	@Qualifier(JpaConfig.JPA_VALIDATION_SUPPORT_CHAIN)
	private ValidationSupportChain myValidationSupportChain;

	@Autowired
	protected ITermReadSvcR4 myTermSvc;

	/**
	 * $lookup operation
	 */
	@SuppressWarnings("unchecked")
	@Operation(name = JpaConstants.OPERATION_LOOKUP, idempotent = true, returnParameters= {
		@OperationParam(name="name", type=StringType.class, min=1),
		@OperationParam(name="version", type=StringType.class, min=0),
		@OperationParam(name="display", type=StringType.class, min=1),
		@OperationParam(name="abstract", type=BooleanType.class, min=1),
	})
	public Parameters lookup(
			HttpServletRequest theServletRequest,
			@OperationParam(name="code", min=0, max=1) CodeType theCode, 
			@OperationParam(name="system", min=0, max=1) UriType theSystem,
			@OperationParam(name="coding", min=0, max=1) Coding theCoding,
			@OperationParam(name="version", min=0, max=1) StringType theVersion,
			@OperationParam(name="displayLanguage", min=0, max=1) CodeType theDisplayLanguage,
			@OperationParam(name="property", min = 0, max = OperationParam.MAX_UNLIMITED) List<CodeType> theProperties,
			RequestDetails theRequestDetails
			) {

		startRequest(theServletRequest);
		try {
			IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> dao = (IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept>) getDao();
			IValidationSupport.LookupCodeResult result;
			if (theVersion != null) {
				result = dao.lookupCode(theCode, new UriType(theSystem.getValue() + "|" + theVersion), theCoding, theDisplayLanguage, theRequestDetails);
			} else {
				result = dao.lookupCode(theCode, theSystem, theCoding, theDisplayLanguage, theRequestDetails);
			}
			result.throwNotFoundIfAppropriate();
			return (Parameters) result.toParameters(theRequestDetails.getFhirContext(), theProperties);
		} finally {
			endRequest(theServletRequest);
		}
	}


	/**
	 * $subsumes operation
	 */
	@Operation(name = JpaConstants.OPERATION_SUBSUMES, idempotent = true, returnParameters= {
		@OperationParam(name="outcome", type=CodeType.class, min=1),
	})
	public Parameters subsumes(
		HttpServletRequest theServletRequest,
		@OperationParam(name="codeA", min=0, max=1) CodeType theCodeA,
		@OperationParam(name="codeB", min=0, max=1) CodeType theCodeB,
		@OperationParam(name="system", min=0, max=1) UriType theSystem,
		@OperationParam(name="codingA", min=0, max=1) Coding theCodingA,
		@OperationParam(name="codingB", min=0, max=1) Coding theCodingB,
		@OperationParam(name="version", min=0, max=1) StringType theVersion,
		RequestDetails theRequestDetails
	) {

		startRequest(theServletRequest);
		try {
			IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> dao = (IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept>) getDao();
			IFhirResourceDaoCodeSystem.SubsumesResult result;
			if (theVersion != null) {
				theSystem = new UriType(theSystem.asStringValue() + "|" + theVersion.toString());
			}
			result = dao.subsumes(theCodeA, theCodeB, theSystem, theCodingA, theCodingB, theRequestDetails);
			return (Parameters) result.toParameters(theRequestDetails.getFhirContext());
		} finally {
			endRequest(theServletRequest);
		}
	}
	
	/**
	 * $validate-code operation
	 */
	@SuppressWarnings("unchecked")
	@Operation(name = JpaConstants.OPERATION_VALIDATE_CODE, idempotent = true, returnParameters = {
		@OperationParam(name = "result", type = BooleanType.class, min = 1),
		@OperationParam(name = "message", type = StringType.class),
		@OperationParam(name = "display", type = StringType.class)
	})
	public Parameters validateCode(
		HttpServletRequest theServletRequest,
		@IdParam(optional = true) IdType theId,
		@OperationParam(name = "url", min = 0, max = 1) UriType theCodeSystemUrl,
		@OperationParam(name = "version", min = 0, max = 1) StringType theVersion,
		@OperationParam(name = "code", min = 0, max = 1) CodeType theCode,
		@OperationParam(name = "display", min = 0, max = 1) StringType theDisplay,
		@OperationParam(name = "coding", min = 0, max = 1) Coding theCoding,
		@OperationParam(name = "codeableConcept", min = 0, max = 1) CodeableConcept theCodeableConcept,
		RequestDetails theRequestDetails
	) {

		IValidationSupport.CodeValidationResult result = null;
		startRequest(theServletRequest);
		try {
			// If a Remote Terminology Server has been configured, use it
			if (myValidationSupportChain.isRemoteTerminologyServiceConfigured()) {
				String codeSystemUrl = (theCodeSystemUrl != null && theCodeSystemUrl.hasValue()) ?
					theCodeSystemUrl.asStringValue() : null;
				String code = (theCode != null && theCode.hasValue()) ? theCode.asStringValue() : null;
				String display = (theDisplay != null && theDisplay.hasValue()) ? theDisplay.asStringValue() : null;

				if (theCoding != null) {
					if (theCoding.hasSystem()) {
						if (codeSystemUrl != null && !codeSystemUrl.equalsIgnoreCase(theCoding.getSystem())) {
							throw new InvalidRequestException(Msg.code(1160) + "Coding.system '" + theCoding.getSystem() + "' does not equal param url '" + theCodeSystemUrl + "'. Unable to validate-code.");
						}
						codeSystemUrl = theCoding.getSystem();
						code = theCoding.getCode();
						display = theCoding.getDisplay();

						result = myValidationSupportChain.validateCode(
							new ValidationSupportContext(myValidationSupportChain), new ConceptValidationOptions(),
							codeSystemUrl, code, display, null);
					}
				}
			} else {
				// Otherwise, use the local DAO layer to validate the code
				IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> dao = (IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept>) getDao();
				result = dao.validateCode(theId, theCodeSystemUrl, theVersion, theCode, theDisplay, theCoding, theCodeableConcept, theRequestDetails);
			}
			return (Parameters) BaseJpaResourceProviderValueSetDstu2.toValidateCodeResult(getContext(), result);
		} finally {
			endRequest(theServletRequest);
		}
	}
}
