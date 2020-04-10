package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoObservation;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.*;
import org.hl7.fhir.r4.model.*;

import java.util.Set;

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

public class BaseJpaResourceProviderObservationR4 extends JpaResourceProviderR4<Observation> {

	/**
	 * Observation/$lastn
	 */
	@Operation(name = JpaConstants.OPERATION_LASTN, idempotent = true, bundleType = BundleTypeEnum.SEARCHSET)
	public IBundleProvider observationLastN(

		javax.servlet.http.HttpServletRequest theServletRequest,
		javax.servlet.http.HttpServletResponse theServletResponse,

		ca.uhn.fhir.rest.api.server.RequestDetails theRequestDetails,

		@Description(shortDefinition="Search the contents of the resource's data using a filter")
		@OperationParam(name=ca.uhn.fhir.rest.api.Constants.PARAM_FILTER)
			StringAndListParam theFtFilter,

		@Description(shortDefinition="Search the contents of the resource's data using a fulltext search")
		@OperationParam(name=ca.uhn.fhir.rest.api.Constants.PARAM_CONTENT)
			StringAndListParam theFtContent,

		@Description(shortDefinition="Search the contents of the resource's narrative using a fulltext search")
		@OperationParam(name=ca.uhn.fhir.rest.api.Constants.PARAM_TEXT)
			StringAndListParam theFtText,

		@Description(shortDefinition="The classification of the type of observation")
		@OperationParam(name="category")
			TokenAndListParam theCategory,

		@Description(shortDefinition="The code of the observation type")
		@OperationParam(name="code")
			TokenAndListParam theCode,

		@Description(shortDefinition="Obtained date/time. If the obtained element is a period, a date that falls in the period")
		@OperationParam(name="date")
			DateRangeParam theDate,

		@Description(shortDefinition="The subject that the observation is about (if patient)")
		@OperationParam(name="patient")
			ReferenceAndListParam thePatient,

		@Description(shortDefinition="The subject that the observation is about")
		@OperationParam(name="subject" )
			ReferenceAndListParam theSubject,

		@IncludeParam(reverse=true)
			Set<Include> theRevIncludes,
		@Description(shortDefinition="Only return resources which were last updated as specified by the given range")
		@OperationParam(name="_lastUpdated")
			DateRangeParam theLastUpdated,

		@IncludeParam(allow= {
			"Observation:based-on",
			"Observation:derived-from",
			"Observation:device",
			"Observation:encounter",
			"Observation:focus",
			"Observation:has-member",
			"Observation:part-of",
			"Observation:patient",
			"Observation:performer",
			"Observation:specimen",
			"Observation:subject",
			"*"
		})
			Set<Include> theIncludes,

		@Sort
			SortSpec theSort,

		@ca.uhn.fhir.rest.annotation.Count
			Integer theCount,

		SummaryEnum theSummaryMode,

		SearchTotalModeEnum theSearchTotalMode

		) {
		startRequest(theServletRequest);
		try {
			SearchParameterMap paramMap = new SearchParameterMap();
			paramMap.add(ca.uhn.fhir.rest.api.Constants.PARAM_FILTER, theFtFilter);
			paramMap.add(ca.uhn.fhir.rest.api.Constants.PARAM_CONTENT, theFtContent);
			paramMap.add(ca.uhn.fhir.rest.api.Constants.PARAM_TEXT, theFtText);
			paramMap.add("category", theCategory);
			paramMap.add("code", theCode);
			paramMap.add("date", theDate);
			paramMap.add("patient", thePatient);
			paramMap.add("subject", theSubject);
			paramMap.setRevIncludes(theRevIncludes);
			paramMap.setLastUpdated(theLastUpdated);
			paramMap.setIncludes(theIncludes);
			paramMap.setLastN(true);
			if (theSort == null) {
				SortSpec effectiveDtm = new SortSpec("date").setOrder(SortOrderEnum.DESC);
				SortSpec observationCode = new SortSpec("code").setOrder(SortOrderEnum.ASC).setChain(effectiveDtm);
				if (thePatient != null && theSubject == null) {
					theSort = new SortSpec("patient").setChain(observationCode);
				} else {
					theSort = new SortSpec("subject").setChain(observationCode);
				}
			}
			paramMap.setSort(theSort);
			paramMap.setCount(theCount);
			paramMap.setSummaryMode(theSummaryMode);
			paramMap.setSearchTotalMode(theSearchTotalMode);

			return ((IFhirResourceDaoObservation<Observation>) getDao()).observationsLastN(paramMap, theRequestDetails, theServletResponse);
		} finally {
			endRequest(theServletRequest);
		}
	}


}
