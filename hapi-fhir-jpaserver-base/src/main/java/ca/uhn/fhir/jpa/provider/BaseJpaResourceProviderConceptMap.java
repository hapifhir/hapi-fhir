/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.TermConceptMappingSvcImpl;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Parameters;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.util.DatatypeUtil.toBooleanValue;
import static ca.uhn.fhir.util.DatatypeUtil.toStringValue;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseJpaResourceProviderConceptMap<T extends IBaseResource> extends BaseJpaResourceProvider<T> {

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

	@Operation(
			name = JpaConstants.OPERATION_TRANSLATE,
			idempotent = true,
			returnParameters = {
				@OperationParam(name = "result", typeName = "boolean", min = 1, max = 1),
				@OperationParam(name = "message", typeName = "string", min = 0, max = 1),
			})
	public IBaseParameters translate(
			HttpServletRequest theServletRequest,
			@IdParam(optional = true) IIdType theId,
			@OperationParam(name = "url", min = 0, max = 1, typeName = "uri") IPrimitiveType<String> theUrl,
			@OperationParam(name = "conceptMapVersion", min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theConceptMapVersion,
			@OperationParam(name = "code", min = 0, max = 1, typeName = "code") IPrimitiveType<String> theSourceCode,
			@OperationParam(name = "system", min = 0, max = 1, typeName = "uri")
					IPrimitiveType<String> theSourceCodeSystem,
			@OperationParam(name = "version", min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theSourceCodeSystemVersion,
			@OperationParam(name = "source", min = 0, max = 1, typeName = "uri")
					IPrimitiveType<String> theSourceValueSet,
			@OperationParam(name = "coding", min = 0, max = 1, typeName = "Coding") IBaseCoding theSourceCoding,
			@OperationParam(name = "codeableConcept", min = 0, max = 1, typeName = "CodeableConcept")
					IBaseDatatype theSourceCodeableConcept,
			@OperationParam(name = "target", min = 0, max = 1, typeName = "uri")
					IPrimitiveType<String> theTargetValueSet,
			@OperationParam(name = "targetsystem", min = 0, max = 1, typeName = "uri")
					IPrimitiveType<String> theTargetCodeSystem,
			@OperationParam(name = "reverse", min = 0, max = 1, typeName = "boolean")
					IPrimitiveType<Boolean> theReverse,
			RequestDetails theRequestDetails) {
		Coding sourceCoding = myVersionCanonicalizer.codingToCanonical(theSourceCoding);
		CodeableConcept sourceCodeableConcept =
				myVersionCanonicalizer.codeableConceptToCanonical(theSourceCodeableConcept);

		boolean haveSourceCode = theSourceCode != null && isNotBlank(theSourceCode.getValue());
		boolean haveSourceCodeSystem = theSourceCodeSystem != null && theSourceCodeSystem.hasValue();
		boolean haveSourceCodeSystemVersion =
				theSourceCodeSystemVersion != null && theSourceCodeSystemVersion.hasValue();
		boolean haveSourceCoding = sourceCoding != null && sourceCoding.hasCode();
		boolean haveSourceCodeableConcept = sourceCodeableConcept != null
				&& sourceCodeableConcept.hasCoding()
				&& sourceCodeableConcept.getCodingFirstRep().hasCode();
		boolean haveReverse = theReverse != null;
		boolean haveId = theId != null && theId.hasIdPart();

		// <editor-fold desc="Filters">
		if ((!haveSourceCode && !haveSourceCoding && !haveSourceCodeableConcept)
				|| moreThanOneTrue(haveSourceCode, haveSourceCoding, haveSourceCodeableConcept)) {
			throw new InvalidRequestException(
					Msg.code(1154)
							+ "One (and only one) of the in parameters (code, coding, codeableConcept) must be provided, to identify the code that is to be translated.");
		}

		TranslationRequest translationRequest = new TranslationRequest();
		translationRequest.setUrl(toStringValue(theUrl));
		translationRequest.setConceptMapVersion(toStringValue(theConceptMapVersion));

		if (haveSourceCode) {
			translationRequest.getCodeableConcept().addCoding().setCode(toStringValue(theSourceCode));

			if (haveSourceCodeSystem) {
				translationRequest
						.getCodeableConcept()
						.getCodingFirstRep()
						.setSystem(toStringValue(theSourceCodeSystem));
			}

			if (haveSourceCodeSystemVersion) {
				translationRequest
						.getCodeableConcept()
						.getCodingFirstRep()
						.setVersion(toStringValue(theSourceCodeSystemVersion));
			}
		} else if (haveSourceCoding) {
			translationRequest.getCodeableConcept().addCoding(sourceCoding);
		} else {
			translationRequest.setCodeableConcept(sourceCodeableConcept);
		}

		translationRequest.setSource(toStringValue(theSourceValueSet));
		translationRequest.setTarget(toStringValue(theTargetValueSet));
		translationRequest.setTargetSystem(toStringValue(theTargetCodeSystem));

		if (haveReverse) {
			translationRequest.setReverse(toBooleanValue(theReverse));
		}

		if (haveId) {
			translationRequest.setResourceId(theId);
		}

		startRequest(theServletRequest);
		try {
			IFhirResourceDaoConceptMap<ConceptMap> dao = (IFhirResourceDaoConceptMap<ConceptMap>) getDao();
			TranslateConceptResults result = dao.translate(translationRequest, theRequestDetails);
			Parameters parameters = TermConceptMappingSvcImpl.toParameters(result);
			return myVersionCanonicalizer.parametersFromCanonical(parameters);
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
