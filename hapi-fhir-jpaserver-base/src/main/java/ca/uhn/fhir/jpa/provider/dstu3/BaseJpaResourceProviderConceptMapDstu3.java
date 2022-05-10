package ca.uhn.fhir.jpa.provider.dstu3;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.TermConceptMappingSvcImpl;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_30_40;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_30_40;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.exceptions.FHIRException;

import javax.servlet.http.HttpServletRequest;

public class BaseJpaResourceProviderConceptMapDstu3 extends JpaResourceProviderDstu3<ConceptMap> {
	@Operation(name = JpaConstants.OPERATION_TRANSLATE, idempotent = true, returnParameters = {
		@OperationParam(name = "result", type = BooleanType.class, min = 1, max = 1),
		@OperationParam(name = "message", type = StringType.class, min = 0, max = 1),
	})
	public Parameters translate(
		HttpServletRequest theServletRequest,
		@IdParam(optional = true) IdType theId,
		@OperationParam(name = "url", min = 0, max = 1) UriType theUrl,
		@OperationParam(name = "conceptMapVersion", min = 0, max = 1) StringType theConceptMapVersion,
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
		boolean haveUrl = theUrl != null
			&& theUrl.hasValue();
		boolean haveConceptMapVersion = theConceptMapVersion != null
			&& theConceptMapVersion.hasValue();
		boolean haveSourceCode = theSourceCode != null
			&& theSourceCode.hasValue();
		boolean haveSourceCodeSystem = theSourceCodeSystem != null
			&& theSourceCodeSystem.hasValue();
		boolean haveSourceCodeSystemVersion = theSourceCodeSystemVersion != null
			&& theSourceCodeSystemVersion.hasValue();
		boolean haveSourceValueSet = theSourceValueSet != null
			&& theSourceValueSet.hasValue();
		boolean haveSourceCoding = theSourceCoding != null
			&& theSourceCoding.hasCode();
		boolean haveSourceCodeableConcept = theSourceCodeableConcept != null
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
			throw new InvalidRequestException(Msg.code(1149) + "One (and only one) of the in parameters (code, coding, codeableConcept) must be provided, to identify the code that is to be translated.");
		}

		TranslationRequest translationRequest = new TranslationRequest();
		try {

			if (haveUrl) {
				translationRequest.setUrl((org.hl7.fhir.r4.model.UriType) VersionConvertorFactory_30_40.convertType(theUrl, new BaseAdvisor_30_40(false)));
			}

			if (haveConceptMapVersion) {
				translationRequest.setConceptMapVersion((org.hl7.fhir.r4.model.StringType) VersionConvertorFactory_30_40.convertType(theConceptMapVersion, new BaseAdvisor_30_40(false)));
			}

			// Convert from DSTU3 to R4
			if (haveSourceCode) {
				translationRequest.getCodeableConcept().addCoding().setCodeElement((org.hl7.fhir.r4.model.CodeType) VersionConvertorFactory_30_40.convertType(theSourceCode, new BaseAdvisor_30_40(false)));

				if (haveSourceCodeSystem) {
					translationRequest.getCodeableConcept().getCodingFirstRep().setSystemElement((org.hl7.fhir.r4.model.UriType) VersionConvertorFactory_30_40.convertType(theSourceCodeSystem, new BaseAdvisor_30_40(false)));
				}

				if (haveSourceCodeSystemVersion) {
					translationRequest.getCodeableConcept().getCodingFirstRep().setVersionElement((org.hl7.fhir.r4.model.StringType) VersionConvertorFactory_30_40.convertType(theSourceCodeSystemVersion, new BaseAdvisor_30_40(false)));
				}
			} else if (haveSourceCoding) {
				translationRequest.getCodeableConcept().addCoding((org.hl7.fhir.r4.model.Coding) VersionConvertorFactory_30_40.convertType(theSourceCoding, new BaseAdvisor_30_40(false)));
			} else {
				translationRequest.setCodeableConcept((org.hl7.fhir.r4.model.CodeableConcept) VersionConvertorFactory_30_40.convertType(theSourceCodeableConcept, new BaseAdvisor_30_40(false)));
			}

			if (haveSourceValueSet) {
				translationRequest.setSource((org.hl7.fhir.r4.model.UriType) VersionConvertorFactory_30_40.convertType(theSourceValueSet, new BaseAdvisor_30_40(false)));
			}

			if (haveTargetValueSet) {
				translationRequest.setTarget((org.hl7.fhir.r4.model.UriType) VersionConvertorFactory_30_40.convertType(theTargetValueSet, new BaseAdvisor_30_40(false)));
			}

			if (haveTargetCodeSystem) {
				translationRequest.setTargetSystem((org.hl7.fhir.r4.model.UriType) VersionConvertorFactory_30_40.convertType(theTargetCodeSystem, new BaseAdvisor_30_40(false)));
			}

			if (haveReverse) {
				translationRequest.setReverse((org.hl7.fhir.r4.model.BooleanType) VersionConvertorFactory_30_40.convertType(theReverse, new BaseAdvisor_30_40(false)));
			}

			if (haveId) {
				translationRequest.setResourceId(theId.getIdPartAsLong());
			}
		} catch (FHIRException fe) {
			throw new InternalErrorException(Msg.code(1150) + fe);
		}

		startRequest(theServletRequest);
		try {
			IFhirResourceDaoConceptMap<ConceptMap> dao = (IFhirResourceDaoConceptMap<ConceptMap>) getDao();
			TranslateConceptResults result = dao.translate(translationRequest, theRequestDetails);

			// Convert from R4 to DSTU3
			return (Parameters) VersionConvertorFactory_30_40.convertResource(TermConceptMappingSvcImpl.toParameters(result), new BaseAdvisor_30_40(false));
		} catch (FHIRException fe) {
			throw new InternalErrorException(Msg.code(1151) + fe);
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
