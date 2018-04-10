package ca.uhn.fhir.jpa.provider.r4;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.*;

import javax.servlet.http.HttpServletRequest;

public class BaseJpaResourceProviderConceptMapR4 extends JpaResourceProviderR4<ConceptMap> {
	@Operation(name = "$translate", idempotent = true, returnParameters = {
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
		RequestDetails theRequestDetails
	) {
		// FIXME: Leverage stored information for instance-level processing.
		boolean haveId = theId != null && theId.hasIdPart();
//		if (haveId) {
//			// Instance-level processing
//		} else {
//			// Type-level processing
//		}

		boolean haveSourceCode = theSourceCode != null
			&& theSourceCode.hasCode();
		boolean haveSourceCodeSystem = theSourceCodeSystem != null
			&& theSourceCodeSystem.hasValue();
		// FIXME: Handle source code system version.
		boolean haveSourceCodeSystemVersion = theSourceCodeSystemVersion != null
			&& theSourceCodeSystemVersion.hasValue();
		// FIXME: Handle source value set.
		boolean haveSourceValueSet = theSourceValueSet != null
			&& theSourceValueSet.hasValue();
		boolean haveSourceCoding = theSourceCoding != null
			&& theSourceCoding.hasCode()
			&& theSourceCoding.hasSystem();
		boolean haveSourceCodeableConcept= theSourceCodeableConcept != null
			&& theSourceCodeableConcept.hasCoding()
			&& theSourceCodeableConcept.getCodingFirstRep().hasCode()
			&& theSourceCodeableConcept.getCodingFirstRep().hasSystem();
		// FIXME: Handle target value set.
		boolean haveTargetValueSet = theTargetValueSet != null
			&& theTargetValueSet.hasValue();
		boolean haveTargetCodeSystem = theTargetCodeSystem != null
			&& theTargetCodeSystem.hasValue();

		if ((!haveSourceCode && !haveSourceCoding && !haveSourceCodeableConcept)
			 || moreThanOneTrue(haveSourceCode, haveSourceCoding, haveSourceCodeableConcept)) {
			throw new InvalidRequestException("One (and only one) of the in parameters (code, coding, codeableConcept) must be provided, to identify the code that is to be translated.");
		}

		if (haveSourceCode) {
			// FIXME: Investigate whether or not we want this to be optional. Presently, it's mandatory.
			if (!haveSourceCodeSystem) {
				throw new InvalidRequestException("This implementation of the $translate operation requires a source code system to be identified.");
			}
		}

		// FIXME: Investigate whether or not we want this to be optional. Presently, it's mandatory.
		if (!haveTargetCodeSystem) {
			throw new InvalidRequestException("This implementation of the $translate operation requires a target code system to be identified.");
		}

		CodeableConcept codeableConcept = new CodeableConcept();
		if (haveSourceCode) {
			codeableConcept.addCoding().setSystemElement(theSourceCodeSystem).setCodeElement(theSourceCode);
		} else if (haveSourceCoding) {
			codeableConcept.addCoding(theSourceCoding);
		} else {
			codeableConcept = theSourceCodeableConcept;
		}

		startRequest(theServletRequest);
		try {
			IFhirResourceDaoConceptMap<ConceptMap> dao = (IFhirResourceDaoConceptMap<ConceptMap>) getDao();
			IFhirResourceDaoConceptMap.TranslationResult result = dao.translate(codeableConcept, theTargetCodeSystem, theRequestDetails);
			return result.toParameters();
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
