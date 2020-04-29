package ca.uhn.fhir.jpa.provider.r4;

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

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BaseJpaResourceProviderCodeSystemR4 extends JpaResourceProviderR4<CodeSystem> {

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
			@OperationParam(name = "property", min = 0, max = OperationParam.MAX_UNLIMITED) List<CodeType> theProperties,
			RequestDetails theRequestDetails
			) {

		startRequest(theServletRequest);
		try {
			IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> dao = (IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept>) getDao();
			IValidationSupport.LookupCodeResult result = dao.lookupCode(theCode, theSystem, theCoding, theRequestDetails);
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
		RequestDetails theRequestDetails
	) {

		startRequest(theServletRequest);
		try {
			IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> dao = (IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept>) getDao();
			IFhirResourceDaoCodeSystem.SubsumesResult result = dao.subsumes(theCodeA, theCodeB, theSystem, theCodingA, theCodingB, theRequestDetails);
			return (Parameters) result.toParameters(theRequestDetails.getFhirContext());
		} finally {
			endRequest(theServletRequest);
		}
	}
}
