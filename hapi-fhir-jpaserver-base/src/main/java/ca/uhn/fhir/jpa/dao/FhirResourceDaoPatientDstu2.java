package ca.uhn.fhir.jpa.dao;

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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap.EverythingModeEnum;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

public class FhirResourceDaoPatientDstu2 extends BaseHapiFhirResourceDao<Patient>implements IFhirResourceDaoPatient<Patient> {

	@Autowired
	private IRequestPartitionHelperSvc myPartitionHelperSvc;

	public FhirResourceDaoPatientDstu2() {
		super();
	}
	
	private IBundleProvider doEverythingOperation(TokenOrListParam theId, IPrimitiveType<Integer> theCount, IPrimitiveType<Integer> theOffset, DateRangeParam theLastUpdated, SortSpec theSort, StringAndListParam theContent, StringAndListParam theNarrative, StringAndListParam theFilter, RequestDetails theRequest) {
		SearchParameterMap paramMap = new SearchParameterMap();
		if (theCount != null) {
			paramMap.setCount(theCount.getValue());
		}
		if (theOffset != null) {
			throw new IllegalArgumentException(Msg.code(954) + "Everything operation does not support offset searching");
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
			paramMap.add("_id", theId);
		}

		if (!isPagingProviderDatabaseBacked(theRequest)) {
			paramMap.setLoadSynchronous(true);
		}

		RequestPartitionId requestPartitionId = myPartitionHelperSvc.determineReadPartitionForRequestForSearchType(theRequest, getResourceName(), paramMap, null);
		return mySearchCoordinatorSvc.registerSearch(this, paramMap, getResourceName(), new CacheControlDirective().parse(theRequest.getHeaders(Constants.HEADER_CACHE_CONTROL)), theRequest, requestPartitionId);
	}

	@Override
	public IBundleProvider patientInstanceEverything(HttpServletRequest theServletRequest, IIdType theId, IPrimitiveType<Integer> theCount, IPrimitiveType<Integer> theOffset, DateRangeParam theLastUpdated, SortSpec theSort, StringAndListParam theContent, StringAndListParam theNarrative, StringAndListParam theFilter, RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getResourceName(), null);
		notifyInterceptors(RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE, requestDetails);
		TokenOrListParam id = new TokenOrListParam().add(new TokenParam(theId.getIdPart()));
		return doEverythingOperation(id, theCount, theOffset, theLastUpdated, theSort, theContent, theNarrative, theFilter, theRequestDetails);
	}

	@Override
	public IBundleProvider patientTypeEverything(HttpServletRequest theServletRequest, IPrimitiveType<Integer> theCount, IPrimitiveType<Integer> theOffset, DateRangeParam theLastUpdated, SortSpec theSort, StringAndListParam theContent, StringAndListParam theNarrative, StringAndListParam theFilter, RequestDetails theRequestDetails, TokenOrListParam theIds) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getResourceName(), null);
		notifyInterceptors(RestOperationTypeEnum.EXTENDED_OPERATION_TYPE, requestDetails);

		return doEverythingOperation(theIds, theCount, theOffset, theLastUpdated, theSort, theContent, theNarrative, theFilter, theRequestDetails);
	}

}
