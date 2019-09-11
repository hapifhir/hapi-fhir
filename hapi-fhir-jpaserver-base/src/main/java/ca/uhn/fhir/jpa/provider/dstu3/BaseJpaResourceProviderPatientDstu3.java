package ca.uhn.fhir.jpa.provider.dstu3;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UnsignedIntType;

import java.util.List;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

public class BaseJpaResourceProviderPatientDstu3 extends JpaResourceProviderDstu3<Patient> {

	/**
	 * Patient/123/$everything
	 */
	@Operation(name = JpaConstants.OPERATION_EVERYTHING, idempotent = true, bundleType = BundleTypeEnum.SEARCHSET)
	public IBundleProvider patientInstanceEverything(

		javax.servlet.http.HttpServletRequest theServletRequest,

		@IdParam
			IdType theId,

		@Description(formalDefinition = "Results from this method are returned across multiple pages. This parameter controls the size of those pages.")
		@OperationParam(name = Constants.PARAM_COUNT)
			UnsignedIntType theCount,

		@Description(shortDefinition = "Only return resources which were last updated as specified by the given range")
		@OperationParam(name = Constants.PARAM_LASTUPDATED, min = 0, max = 1)
			DateRangeParam theLastUpdated,

		@Description(shortDefinition = "Filter the resources to return only resources matching the given _content filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
		@OperationParam(name = Constants.PARAM_CONTENT, min = 0, max = OperationParam.MAX_UNLIMITED)
			List<StringType> theContent,

		@Description(shortDefinition = "Filter the resources to return only resources matching the given _text filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
		@OperationParam(name = Constants.PARAM_TEXT, min = 0, max = OperationParam.MAX_UNLIMITED)
			List<StringType> theNarrative,

		@Description(shortDefinition = "Filter the resources to return only resources matching the given _filter filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
		@OperationParam(name = Constants.PARAM_FILTER, min = 0, max = OperationParam.MAX_UNLIMITED)
			List<StringType> theFilter,

		@Sort
			SortSpec theSortSpec,

		RequestDetails theRequestDetails
	) {

		startRequest(theServletRequest);
		try {
			return ((IFhirResourceDaoPatient<Patient>) getDao()).patientInstanceEverything(theServletRequest, theId, theCount, theLastUpdated, theSortSpec, toStringAndList(theContent), toStringAndList(theNarrative), toStringAndList(theFilter), theRequestDetails);
		} finally {
			endRequest(theServletRequest);
		}
	}

	/**
	 * /Patient/$everything
	 */
	@Operation(name = JpaConstants.OPERATION_EVERYTHING, idempotent = true, bundleType = BundleTypeEnum.SEARCHSET)
	public IBundleProvider patientTypeEverything(

		javax.servlet.http.HttpServletRequest theServletRequest,

		@Description(formalDefinition = "Results from this method are returned across multiple pages. This parameter controls the size of those pages.")
		@OperationParam(name = Constants.PARAM_COUNT)
			UnsignedIntType theCount,

		@Description(shortDefinition = "Only return resources which were last updated as specified by the given range")
		@OperationParam(name = Constants.PARAM_LASTUPDATED, min = 0, max = 1)
			DateRangeParam theLastUpdated,

		@Description(shortDefinition = "Filter the resources to return only resources matching the given _content filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
		@OperationParam(name = Constants.PARAM_CONTENT, min = 0, max = OperationParam.MAX_UNLIMITED)
			List<StringType> theContent,

		@Description(shortDefinition = "Filter the resources to return only resources matching the given _text filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
		@OperationParam(name = Constants.PARAM_TEXT, min = 0, max = OperationParam.MAX_UNLIMITED)
			List<StringType> theNarrative,

		@Description(shortDefinition = "Filter the resources to return only resources matching the given _filter filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
		@OperationParam(name = Constants.PARAM_FILTER, min = 0, max = OperationParam.MAX_UNLIMITED)
			List<StringType> theFilter,

		@Sort
			SortSpec theSortSpec,

		RequestDetails theRequestDetails
	) {

		startRequest(theServletRequest);
		try {
			return ((IFhirResourceDaoPatient<Patient>) getDao()).patientTypeEverything(theServletRequest, theCount, theLastUpdated, theSortSpec, toStringAndList(theContent), toStringAndList(theNarrative), toStringAndList(theFilter), theRequestDetails);
		} finally {
			endRequest(theServletRequest);
		}

	}

	private StringAndListParam toStringAndList(List<StringType> theNarrative) {
		StringAndListParam retVal = new StringAndListParam();
		if (theNarrative != null) {
			for (StringType next : theNarrative) {
				if (isNotBlank(next.getValue())) {
					retVal.addAnd(new StringOrListParam().addOr(new StringParam(next.getValue())));
				}
			}
		}
		if (retVal.getValuesAsQueryTokens().isEmpty()) {
			return null;
		}
		return retVal;
	}

}
