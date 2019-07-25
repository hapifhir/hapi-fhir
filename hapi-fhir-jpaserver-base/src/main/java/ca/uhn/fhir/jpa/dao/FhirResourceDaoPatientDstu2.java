package ca.uhn.fhir.jpa.dao;

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

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.jpa.dao.SearchParameterMap.EverythingModeEnum;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;

public class FhirResourceDaoPatientDstu2 extends FhirResourceDaoDstu2<Patient>implements IFhirResourceDaoPatient<Patient> {

	public FhirResourceDaoPatientDstu2() {
		super();
	}
	
	private IBundleProvider doEverythingOperation(IIdType theId, IPrimitiveType<Integer> theCount, DateRangeParam theLastUpdated, SortSpec theSort, StringAndListParam theContent, StringAndListParam theNarrative, RequestDetails theRequestDetails) {
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
		
		if (!isPagingProviderDatabaseBacked(theRequestDetails)) {
			paramMap.setLoadSynchronous(true);
		}
		
		return mySearchCoordinatorSvc.registerSearch(this, paramMap, getResourceName(), new CacheControlDirective().parse(theRequestDetails.getHeaders(Constants.HEADER_CACHE_CONTROL)));
	}

	@Override
	public IBundleProvider patientInstanceEverything(HttpServletRequest theServletRequest, IIdType theId, IPrimitiveType<Integer> theCount, DateRangeParam theLastUpdated, SortSpec theSort, StringAndListParam theContent, StringAndListParam theNarrative, RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getResourceName(), null);
		notifyInterceptors(RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE, requestDetails);

		return doEverythingOperation(theId, theCount, theLastUpdated, theSort, theContent, theNarrative, theRequestDetails);
	}

	@Override
	public IBundleProvider patientTypeEverything(HttpServletRequest theServletRequest, IPrimitiveType<Integer> theCount, DateRangeParam theLastUpdated, SortSpec theSort, StringAndListParam theContent, StringAndListParam theNarrative, RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getResourceName(), null);
		notifyInterceptors(RestOperationTypeEnum.EXTENDED_OPERATION_TYPE, requestDetails);

		return doEverythingOperation(null, theCount, theLastUpdated, theSort, theContent, theNarrative, theRequestDetails);
	}

}
