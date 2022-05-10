package ca.uhn.fhir.jpa.dao.r4;

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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDaoObservation;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class FhirResourceDaoObservationR4 extends BaseHapiFhirResourceDaoObservation<Observation> {


	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperService;

	@Override
	public IBundleProvider observationsLastN(SearchParameterMap theSearchParameterMap,  RequestDetails theRequestDetails, HttpServletResponse theServletResponse) {
		updateSearchParamsForLastn(theSearchParameterMap, theRequestDetails);

		RequestPartitionId requestPartitionId = myRequestPartitionHelperService.determineReadPartitionForRequestForSearchType(theRequestDetails, getResourceName(), theSearchParameterMap, null);
		return mySearchCoordinatorSvc.registerSearch(this, theSearchParameterMap, getResourceName(), new CacheControlDirective().parse(theRequestDetails.getHeaders(Constants.HEADER_CACHE_CONTROL)), theRequestDetails, requestPartitionId);
	}

	@Override
	protected String getEffectiveParamName() {
		return Observation.SP_DATE;
	}

	@Override
	protected String getCodeParamName() {
		return Observation.SP_CODE;
	}

	@Override
	protected String getSubjectParamName() {
		return Observation.SP_SUBJECT;
	}

	@Override
	protected String getPatientParamName() {
		return Observation.SP_PATIENT;
	}

	@Override
	public ResourceTable updateEntity(RequestDetails theRequest, IBaseResource theResource, IBasePersistedResource theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
												 boolean theUpdateVersion, TransactionDetails theTransactionDetails, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		return updateObservationEntity(theRequest, theResource, theEntity, theDeletedTimestampOrNull,
			thePerformIndexing, theUpdateVersion, theTransactionDetails, theForceUpdate,
			theCreateNewHistoryEntry);
	}

}
