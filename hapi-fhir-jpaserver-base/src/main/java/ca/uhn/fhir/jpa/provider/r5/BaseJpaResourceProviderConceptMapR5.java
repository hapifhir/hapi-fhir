package ca.uhn.fhir.jpa.provider.r5;

/*
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.api.model.TranslationResult;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.convertors.VersionConvertor_40_50;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.ConceptMap;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;

import javax.servlet.http.HttpServletRequest;

public class BaseJpaResourceProviderConceptMapR5 extends JpaResourceProviderR5<ConceptMap> {
	@Operation(name = JpaConstants.OPERATION_TRANSLATE, idempotent = true, returnParameters = {
		@OperationParam(name = "result", type = BooleanType.class, min = 1, max = 1),
		@OperationParam(name = "message", type = StringType.class, min = 0, max = 1),
	})
	public Parameters translate(
		HttpServletRequest theServletRequest,
		@IdParam(optional = true) IdType theId,
		@OperationParam(name = "code", min = 0, max = 1) CodeType theSourceCode,
		@OperationParam(name = "system", min = 0, max = 1) UriType theSourceCodeSystem,
		@OperationParam(name = "version", min = 0, max = 1) StringType theSourceCodeSystemVersion,
		@OperationParam(name = "source", min = 0, max = 1) UriType theSourceValueSet,
		@OperationParam(name = "coding", min = 0, max = 1) Coding theSourceCoding,
		@OperationParam(name = "codeableConcept", min = 0, max = 1) CodeableConcept theSourceCodeableConcept,
		@OperationParam(name = "target", min = 0, max = 1) UriType theTargetValueSet,
		@OperationParam(name = "targetsystem", min = 0, max = 1) UriType theTargetCodeSystem,
		@OperationParam(name = "reverse", min = 0, max = 1) BooleanType theReverse,
		RequestDetails theRequestDetails
	) {
		boolean haveSourceCode = theSourceCode != null
			&& theSourceCode.hasCode();
		boolean haveSourceCodeSystem = theSourceCodeSystem != null
			&& theSourceCodeSystem.hasValue();
		boolean haveSourceCodeSystemVersion = theSourceCodeSystemVersion != null
			&& theSourceCodeSystemVersion.hasValue();
		boolean haveSourceValueSet = theSourceValueSet != null
			&& theSourceValueSet.hasValue();
		boolean haveSourceCoding = theSourceCoding != null
			&& theSourceCoding.hasCode();
		boolean haveSourceCodeableConcept= theSourceCodeableConcept != null
			&& theSourceCodeableConcept.hasCoding()
			&& theSourceCodeableConcept.getCodingFirstRep().hasCode();
		boolean haveTargetValueSet = theTargetValueSet != null
			&& theTargetValueSet.hasValue();
		boolean haveTargetCodeSystem = theTargetCodeSystem != null
			&& theTargetCodeSystem.hasValue();
		boolean haveReverse = theReverse != null;
		boolean haveId = theId != null && theId.hasIdPart();

		// <editor-fold desc="Filters">
		if ((!haveSourceCode && !haveSourceCoding && !haveSourceCodeableConcept)
			|| moreThanOneTrue(haveSourceCode, haveSourceCoding, haveSourceCodeableConcept)) {
			throw new InvalidRequestException("One (and only one) of the in parameters (code, coding, codeableConcept) must be provided, to identify the code that is to be translated.");
		}

		TranslationRequest translationRequest = new TranslationRequest();

		if (haveSourceCode) {
			translationRequest.getCodeableConcept().addCoding().setCodeElement(VersionConvertor_40_50.convertCode(theSourceCode));

			if (haveSourceCodeSystem) {
				translationRequest.getCodeableConcept().getCodingFirstRep().setSystemElement(VersionConvertor_40_50.convertUri(theSourceCodeSystem));
			}

			if (haveSourceCodeSystemVersion) {
				translationRequest.getCodeableConcept().getCodingFirstRep().setVersionElement(VersionConvertor_40_50.convertString(theSourceCodeSystemVersion));
			}
		} else if (haveSourceCoding) {
			translationRequest.getCodeableConcept().addCoding(VersionConvertor_40_50.convertCoding(theSourceCoding));
		} else {
			translationRequest.setCodeableConcept(VersionConvertor_40_50.convertCodeableConcept(theSourceCodeableConcept));
		}

		if (haveSourceValueSet) {
			translationRequest.setSource(VersionConvertor_40_50.convertUri(theSourceValueSet));
		}

		if (haveTargetValueSet) {
			translationRequest.setTarget(VersionConvertor_40_50.convertUri(theTargetValueSet));
		}

		if (haveTargetCodeSystem) {
			translationRequest.setTargetSystem(VersionConvertor_40_50.convertUri(theTargetCodeSystem));
		}

		if (haveReverse) {
			translationRequest.setReverse(VersionConvertor_40_50.convertBoolean(theReverse));
		}

		if (haveId) {
			translationRequest.setResourceId(theId.getIdPartAsLong());
		}

		startRequest(theServletRequest);
		try {
			IFhirResourceDaoConceptMap<ConceptMap> dao = (IFhirResourceDaoConceptMap<ConceptMap>) getDao();
			TranslationResult result = dao.translate(translationRequest, theRequestDetails);
			org.hl7.fhir.r4.model.Parameters parameters = result.toParameters();
			return org.hl7.fhir.convertors.conv40_50.Parameters40_50.convertParameters(parameters);
		} finally {
			endRequest(theServletRequest);
		}
	}

	private static boolean moreThanOneTrue(boolean... theBooleans) {
		boolean haveOne = false;
		for (boolean next : theBooleans) {
			if (next) {
				if (haveOne) {
					return true;
				} else {
					haveOne = true;
				}
			}
		}
		return false;
	}
}
