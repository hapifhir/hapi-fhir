package ca.uhn.fhir.jpa.ips;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap.EverythingModeEnum;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import org.springframework.context.annotation.Configuration;

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

public class Ips {
	private final FhirContext fhirContext;
	private final IFhirResourceDao<Bundle> bundleResourceProvider;
	private final IFhirResourceDao<Patient> patientResourceProvider;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	private final ISearchCoordinatorSvc mySearchCoordinatorSvc;

   public Ips(DaoRegistry daoRegistry, IRequestPartitionHelperSvc myRequestPartitionHelperSvc, ISearchCoordinatorSvc mySearchCoordinatorSvc)
   {
		this.fhirContext = daoRegistry.getSystemDao().getContext();
		this.bundleResourceProvider = daoRegistry.getResourceDao("Bundle");
		this.patientResourceProvider = daoRegistry.getResourceDao("Patient");
		this.myRequestPartitionHelperSvc = myRequestPartitionHelperSvc;
			this.mySearchCoordinatorSvc = mySearchCoordinatorSvc;
   }




	public IBundleProvider patientInstanceSummaryInternal(HttpServletRequest theServletRequest, IIdType theId, IPrimitiveType<Integer> theCount, IPrimitiveType<Integer> theOffset, DateRangeParam theLastUpdated, SortSpec theSort, StringAndListParam theContent, StringAndListParam theNarrative, StringAndListParam theFilter, StringAndListParam theTypes, RequestDetails theRequestDetails) {
		TokenOrListParam id = new TokenOrListParam().add(new TokenParam(theId.getIdPart()));
		return doSummaryOperation(id, null, theCount, theOffset, theLastUpdated, theSort, theContent, theNarrative, theFilter, theTypes, theRequestDetails);
	}




	public IBundleProvider patientTypeSummaryInternal(HttpServletRequest theServletRequest, IPrimitiveType<Integer> theCount, IPrimitiveType<Integer> theOffset, DateRangeParam theLastUpdated, SortSpec theSort, StringAndListParam theContent, StringAndListParam theNarrative, StringAndListParam theFilter, StringAndListParam theTypes, RequestDetails theRequestDetails, TokenParam theIdentifier) {
 		return doSummaryOperation(null, theIdentifier, theCount, theOffset, theLastUpdated, theSort, theContent, theNarrative, theFilter, theTypes, theRequestDetails);
	}



	private IBundleProvider doSummaryOperation(	 TokenOrListParam theIds,
																 TokenParam theIdentifier,
																 IPrimitiveType<Integer> theCount,
																 IPrimitiveType<Integer> theOffset,
																 DateRangeParam theLastUpdated,
																 SortSpec theSort,
																 StringAndListParam theContent,
																 StringAndListParam theNarrative,
																 StringAndListParam theFilter,
																 StringAndListParam theTypes,
																 RequestDetails theRequest) {
		SearchParameterMap paramMap = new SearchParameterMap();
	
		
		if (theCount != null) {
			paramMap.setCount(theCount.getValue());
		}
		if (theOffset != null) {
			throw new IllegalArgumentException(Msg.code(1106) + "Summary operation does not support offset searching");
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

		paramMap.setEverythingMode(theIds != null && theIds.getValuesAsQueryTokens().size() == 1 ? EverythingModeEnum.PATIENT_INSTANCE : EverythingModeEnum.PATIENT_TYPE);
		paramMap.setSort(theSort);
		paramMap.setLastUpdated(theLastUpdated);
		if (theIds != null) {
			if (theRequest.getParameters().containsKey("_mdm")) {
				String[] paramVal = theRequest.getParameters().get("_mdm");
				if (Arrays.asList(paramVal).contains("true")) {
					theIds.getValuesAsQueryTokens().stream().forEach(param -> param.setMdmExpand(true));
				}
			}
			paramMap.add("_id", theIds);
		}

      if (theIdentifier != null) {

			TokenOrListParam identifierParam = new TokenOrListParam(theIdentifier.getSystem(), theIdentifier.getValue());
			paramMap.add("identifier", identifierParam);
	   }
		
		if (!isPagingProviderDatabaseBacked(theRequest)) {
			paramMap.setLoadSynchronous(true);
		}

		RequestPartitionId requestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(theRequest, "Patient", paramMap, null);
		return mySearchCoordinatorSvc.registerSearch(this.patientResourceProvider,
			paramMap,
			"Patient",
			new CacheControlDirective().parse(theRequest.getHeaders(Constants.HEADER_CACHE_CONTROL)),
			theRequest,
			requestPartitionId);
	}


	protected boolean isPagingProviderDatabaseBacked(RequestDetails theRequestDetails) {
		if (theRequestDetails == null || theRequestDetails.getServer() == null) {
			return false;
		}
		IRestfulServerDefaults server = theRequestDetails.getServer();
		IPagingProvider pagingProvider = server.getPagingProvider();
		return pagingProvider != null;
	}


	private TokenOrListParam toFlattenedPatientIdTokenParamList(List<IdType> theId) {
		TokenOrListParam retVal = new TokenOrListParam();
		if (theId != null) {
			for (IdType next: theId) {
				if (isNotBlank(next.getValue())) {
					String[] split = next.getValueAsString().split(",");
					Arrays.stream(split).map(IdType::new).forEach(id -> {
						retVal.addOr(new TokenParam(id.getIdPart()));
					});
				}
			}
		}
		return retVal.getValuesAsQueryTokens().isEmpty() ? null: retVal;
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
