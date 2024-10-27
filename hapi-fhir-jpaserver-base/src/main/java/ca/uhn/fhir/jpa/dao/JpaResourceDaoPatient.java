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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.dao.PatientEverythingParameters;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
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
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

public class JpaResourceDaoPatient<T extends IBaseResource> extends BaseHapiFhirResourceDao<T>
		implements IFhirResourceDaoPatient<T> {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaResourceDaoPatient.class);

	@Autowired
	private IRequestPartitionHelperSvc myPartitionHelperSvc;

	private IBundleProvider doEverythingOperation(
			TokenOrListParam theIds,
			IPrimitiveType<Integer> theCount,
			IPrimitiveType<Integer> theOffset,
			DateRangeParam theLastUpdated,
			SortSpec theSort,
			StringAndListParam theContent,
			StringAndListParam theNarrative,
			StringAndListParam theFilter,
			StringAndListParam theTypes,
			boolean theMdmExpand,
			RequestDetails theRequest) {
		SearchParameterMap paramMap = new SearchParameterMap();
		if (theCount != null) {
			paramMap.setCount(theCount.getValue());
		}
		if (theOffset != null) {
			paramMap.setOffset(theOffset.getValue());
		}
		if (theContent != null) {
			paramMap.add(Constants.PARAM_CONTENT, theContent);
		}
		if (theNarrative != null) {
			paramMap.add(Constants.PARAM_TEXT, theNarrative);
		}
		if (theTypes != null) {
			paramMap.add(Constants.PARAM_TYPE, theTypes);
		} else {
			paramMap.setIncludes(Collections.singleton(IResource.INCLUDE_ALL.asRecursive()));
		}

		paramMap.setEverythingMode(
				theIds != null && theIds.getValuesAsQueryTokens().size() == 1
						? EverythingModeEnum.PATIENT_INSTANCE
						: EverythingModeEnum.PATIENT_TYPE);
		paramMap.setSort(theSort);
		paramMap.setLastUpdated(theLastUpdated);
		if (theIds != null) {
			if (theMdmExpand) {
				theIds.getValuesAsQueryTokens().forEach(param -> param.setMdmExpand(true));
			}
			paramMap.add("_id", theIds);
		}

		if (!isPagingProviderDatabaseBacked(theRequest)) {
			paramMap.setLoadSynchronous(true);
		}

		RequestPartitionId requestPartitionId = myPartitionHelperSvc.determineReadPartitionForRequestForSearchType(
				theRequest, getResourceName(), paramMap);

		adjustCount(theRequest, paramMap);

		return mySearchCoordinatorSvc.registerSearch(
				this,
				paramMap,
				getResourceName(),
				new CacheControlDirective().parse(theRequest.getHeaders(Constants.HEADER_CACHE_CONTROL)),
				theRequest,
				requestPartitionId);
	}

	private void adjustCount(RequestDetails theRequest, SearchParameterMap theParamMap) {
		if (theRequest.getServer() == null) {
			return;
		}

		if (theParamMap.getCount() == null && theRequest.getServer().getDefaultPageSize() != null) {
			theParamMap.setCount(theRequest.getServer().getDefaultPageSize());
			return;
		}

		Integer maxPageSize = theRequest.getServer().getMaximumPageSize();
		if (maxPageSize != null && theParamMap.getCount() > maxPageSize) {
			ourLog.info(
					"Reducing {} from {} to {} which is the maximum allowable page size.",
					Constants.PARAM_COUNT,
					theParamMap.getCount(),
					maxPageSize);
			theParamMap.setCount(maxPageSize);
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public IBundleProvider patientInstanceEverything(
			HttpServletRequest theServletRequest,
			RequestDetails theRequestDetails,
			PatientEverythingParameters theQueryParams,
			IIdType theId) {
		TokenOrListParam id = new TokenOrListParam().add(new TokenParam(theId.getIdPart()));
		return doEverythingOperation(
				id,
				theQueryParams.getCount(),
				theQueryParams.getOffset(),
				theQueryParams.getLastUpdated(),
				theQueryParams.getSort(),
				theQueryParams.getContent(),
				theQueryParams.getNarrative(),
				theQueryParams.getFilter(),
				theQueryParams.getTypes(),
				theQueryParams.getMdmExpand(),
				theRequestDetails);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public IBundleProvider patientTypeEverything(
			HttpServletRequest theServletRequest,
			RequestDetails theRequestDetails,
			PatientEverythingParameters theQueryParams,
			TokenOrListParam theId) {
		return doEverythingOperation(
				theId,
				theQueryParams.getCount(),
				theQueryParams.getOffset(),
				theQueryParams.getLastUpdated(),
				theQueryParams.getSort(),
				theQueryParams.getContent(),
				theQueryParams.getNarrative(),
				theQueryParams.getFilter(),
				theQueryParams.getTypes(),
				theQueryParams.getMdmExpand(),
				theRequestDetails);
	}
}
