package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoObservation;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.RawParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.UnsignedIntType;

import java.util.List;
import java.util.Map;

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

public class BaseJpaResourceProviderObservationR5 extends JpaResourceProviderR5<Observation> {

	/**
	 * Observation/$lastn
	 */
	@Operation(name = JpaConstants.OPERATION_LASTN, idempotent = true, bundleType = BundleTypeEnum.SEARCHSET)
	public IBundleProvider observationLastN(

		javax.servlet.http.HttpServletRequest theServletRequest,
		javax.servlet.http.HttpServletResponse theServletResponse,

		ca.uhn.fhir.rest.api.server.RequestDetails theRequestDetails,

		@Description(formalDefinition = "Results from this method are returned across multiple pages. This parameter controls the size of those pages.")
		@OperationParam(name = Constants.PARAM_COUNT)
			UnsignedIntType theCount,

		@Description(shortDefinition="The classification of the type of observation")
		@OperationParam(name="category")
			TokenAndListParam theCategory,

		@Description(shortDefinition="The code of the observation type")
		@OperationParam(name="code")
			TokenAndListParam theCode,

		@Description(shortDefinition="The effective date of the observation")
		@OperationParam(name="date")
			DateAndListParam theDate,

		@Description(shortDefinition="The subject that the observation is about (if patient)")
		@OperationParam(name="patient")
			ReferenceAndListParam thePatient,

		@Description(shortDefinition="The subject that the observation is about")
		@OperationParam(name="subject" )
			ReferenceAndListParam theSubject,

		@Description(shortDefinition="The maximum number of observations to return for each observation code")
		@OperationParam(name = "max", typeName = "integer", min = 0, max = 1)
			IPrimitiveType<Integer> theMax,

		@RawParam
			Map<String, List<String>> theAdditionalRawParams
		) {
		startRequest(theServletRequest);
		try {
			SearchParameterMap paramMap = new SearchParameterMap();
			paramMap.add(org.hl7.fhir.r4.model.Observation.SP_CATEGORY, theCategory);
			paramMap.add(org.hl7.fhir.r4.model.Observation.SP_CODE, theCode);
			paramMap.add(org.hl7.fhir.r4.model.Observation.SP_DATE, theDate);
			if (thePatient != null) {
				paramMap.add(org.hl7.fhir.r4.model.Observation.SP_PATIENT, thePatient);
			}
			if (theSubject != null) {
				paramMap.add(org.hl7.fhir.r4.model.Observation.SP_SUBJECT, theSubject);
			}
			if (theMax != null) {
				paramMap.setLastNMax(theMax.getValue());
			}
			if (theCount != null) {
				paramMap.setCount(theCount.getValue());
			}

			getDao().translateRawParameters(theAdditionalRawParams, paramMap);

			return ((IFhirResourceDaoObservation<Observation>) getDao()).observationsLastN(paramMap, theRequestDetails, theServletResponse);
		} finally {
			endRequest(theServletRequest);
		}
	}

}
