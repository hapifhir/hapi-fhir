package ca.uhn.fhir.jpa.dao.r4;

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

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap.EverythingModeEnum;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Patient;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

public class FhirResourceDaoPatientR4 extends BaseHapiFhirResourceDao<Patient>implements IFhirResourceDaoPatient<Patient> {

	private IBundleProvider doEverythingOperation(IIdType theId, IPrimitiveType<Integer> theCount, DateRangeParam theLastUpdated, SortSpec theSort, StringAndListParam theContent, StringAndListParam theNarrative, StringAndListParam theFilter, RequestDetails theRequest) {
		SearchParameterMap paramMap = new SearchParameterMap();
		if (theCount != null) {
			paramMap.setCount(theCount.getValue());
		}
		if (theContent != null) {
			paramMap.add(Constants.PARAM_CONTENT, theContent);
		}
		if (theNarrative != null) {
			paramMap.add(Constants.PARAM_TEXT, theNarrative);
		}
		paramMap.setIncludes(Collections.singleton(IResource.INCLUDE_ALL.asRecursive()));
		paramMap.setEverythingMode(theId != null ? EverythingModeEnum.PATIENT_INSTANCE : EverythingModeEnum.PATIENT_TYPE);
		paramMap.setSort(theSort);
		paramMap.setLastUpdated(theLastUpdated);
		if (theId != null) {
			paramMap.add("_id", new StringParam(theId.getIdPart()));
		}
		
		if (!isPagingProviderDatabaseBacked(theRequest)) {
			paramMap.setLoadSynchronous(true);
		}
		
		return mySearchCoordinatorSvc.registerSearch(this, paramMap, getResourceName(), new CacheControlDirective().parse(theRequest.getHeaders(Constants.HEADER_CACHE_CONTROL)), theRequest);
	}

	@Override
	public IBundleProvider patientInstanceEverything(HttpServletRequest theServletRequest, IIdType theId, IPrimitiveType<Integer> theCount, DateRangeParam theLastUpdated, SortSpec theSort, StringAndListParam theContent, StringAndListParam theNarrative, StringAndListParam theFilter, RequestDetails theRequestDetails) {
		return doEverythingOperation(theId, theCount, theLastUpdated, theSort, theContent, theNarrative, theFilter, theRequestDetails);
	}

	@Override
	public IBundleProvider patientTypeEverything(HttpServletRequest theServletRequest, IPrimitiveType<Integer> theCount, DateRangeParam theLastUpdated, SortSpec theSort, StringAndListParam theContent, StringAndListParam theNarrative, StringAndListParam theFilter, RequestDetails theRequestDetails) {
		return doEverythingOperation(null, theCount, theLastUpdated, theSort, theContent, theNarrative, theFilter, theRequestDetails);
	}

}
