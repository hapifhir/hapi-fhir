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
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.exceptions.FHIRException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BaseJpaResourceProviderCodeSystemDstu3 extends JpaResourceProviderDstu3<CodeSystem> {

	@Operation(name = JpaConstants.OPERATION_LOOKUP, idempotent = true, returnParameters = {
		@OperationParam(name = "name", type = StringType.class, min = 1),
		@OperationParam(name = "version", type = StringType.class, min = 0),
		@OperationParam(name = "display", type = StringType.class, min = 1),
		@OperationParam(name = "abstract", type = BooleanType.class, min = 1),
	})
	public Parameters lookup(
		HttpServletRequest theServletRequest,
		@OperationParam(name = "code", min = 0, max = 1) CodeType theCode,
		@OperationParam(name = "system", min = 0, max = 1) UriType theSystem,
		@OperationParam(name = "coding", min = 0, max = 1) Coding theCoding,
		@OperationParam(name = "version", min=0, max=1) StringType theVersion,
		@OperationParam(name = "displayLanguage", min=0, max=1) CodeType theDisplayLanguage,
		@OperationParam(name = "property", min = 0, max = OperationParam.MAX_UNLIMITED) List<CodeType> theProperties,
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
		} catch (FHIRException e) {
			throw new InternalErrorException(Msg.code(1153) + e);
		} finally {
			endRequest(theServletRequest);
		}
	}


	/**
	 * $subsumes operation
	 */
	@Operation(name = JpaConstants.OPERATION_SUBSUMES, idempotent = true, returnParameters = {
		@OperationParam(name = "outcome", type = CodeType.class, min = 1),
	})
	public Parameters subsumes(
		HttpServletRequest theServletRequest,
		@OperationParam(name = "codeA", min = 0, max = 1) CodeType theCodeA,
		@OperationParam(name = "codeB", min = 0, max = 1) CodeType theCodeB,
		@OperationParam(name = "system", min = 0, max = 1) UriType theSystem,
		@OperationParam(name = "codingA", min = 0, max = 1) Coding theCodingA,
		@OperationParam(name = "codingB", min = 0, max = 1) Coding theCodingB,
		@OperationParam(name = "version", min = 0, max = 1) StringType theVersion,
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

}
