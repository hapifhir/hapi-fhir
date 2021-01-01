package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoObservation;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.*;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.hl7.fhir.instance.model.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public abstract class BaseHapiFhirResourceDaoObservation<T extends IBaseResource> extends BaseHapiFhirResourceDao<T> implements IFhirResourceDaoObservation<T> {

	@Autowired
	ObservationLastNIndexPersistSvc myObservationLastNIndexPersistSvc;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperService;

	protected ResourceTable updateObservationEntity(RequestDetails theRequest, IBaseResource theResource, IBasePersistedResource theEntity,
																	Date theDeletedTimestampOrNull, boolean thePerformIndexing, boolean theUpdateVersion,
																	TransactionDetails theTransactionDetails, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(theRequest, theResource, theEntity, theDeletedTimestampOrNull, thePerformIndexing, theUpdateVersion,
			theTransactionDetails, theForceUpdate, theCreateNewHistoryEntry);

		if (myDaoConfig.isLastNEnabled()) {
			if (!retVal.isUnchangedInCurrentOperation()) {
				if (retVal.getDeleted() == null) {
					// Update indexes here for LastN operation.
					myObservationLastNIndexPersistSvc.indexObservation(theResource);
				} else {
					myObservationLastNIndexPersistSvc.deleteObservationIndex(theEntity);
				}
			}
		}

		return retVal;
	}

	protected void updateSearchParamsForLastn(SearchParameterMap theSearchParameterMap, RequestDetails theRequestDetails) {
		if (!isPagingProviderDatabaseBacked(theRequestDetails)) {
			theSearchParameterMap.setLoadSynchronous(true);
		}

		theSearchParameterMap.setLastN(true);
		SortSpec effectiveDtm = new SortSpec(getEffectiveParamName()).setOrder(SortOrderEnum.DESC);
		SortSpec observationCode = new SortSpec(getCodeParamName()).setOrder(SortOrderEnum.ASC).setChain(effectiveDtm);
		if(theSearchParameterMap.containsKey(getSubjectParamName()) || theSearchParameterMap.containsKey(getPatientParamName())) {
			fixSubjectParamsOrderForLastn(theSearchParameterMap, theRequestDetails);
			theSearchParameterMap.setSort(new SortSpec(getSubjectParamName()).setOrder(SortOrderEnum.ASC).setChain(observationCode));
		} else {
			theSearchParameterMap.setSort(observationCode);
		}
	}

	private void fixSubjectParamsOrderForLastn(SearchParameterMap theSearchParameterMap, RequestDetails theRequestDetails) {
		// Need to ensure that the patient/subject parameters are sorted in the SearchParameterMap to ensure correct ordering of
		// the output. The reason for this is that observations are indexed by patient/subject forced ID, but then ordered in the
		// final result set by subject/patient resource PID.
		TreeMap<Long, IQueryParameterType> orderedSubjectReferenceMap = new TreeMap<>();
		if(theSearchParameterMap.containsKey(getSubjectParamName())) {

			RequestPartitionId requestPartitionId = myRequestPartitionHelperService.determineReadPartitionForRequest(theRequestDetails, getResourceName());

			List<List<IQueryParameterType>> patientParams = new ArrayList<>();
			if (theSearchParameterMap.get(getPatientParamName()) != null) {
				patientParams.addAll(theSearchParameterMap.get(getPatientParamName()));
			}
			if (theSearchParameterMap.get(getSubjectParamName()) != null) {
				patientParams.addAll(theSearchParameterMap.get(getSubjectParamName()));
			}

			for (List<? extends IQueryParameterType> nextPatientList : patientParams) {
				for (IQueryParameterType nextOr : nextPatientList) {
					if (nextOr instanceof ReferenceParam) {
						ReferenceParam ref = (ReferenceParam) nextOr;
						ResourcePersistentId pid = myIdHelperService.resolveResourcePersistentIds(requestPartitionId, ref.getResourceType(), ref.getIdPart());
						orderedSubjectReferenceMap.put(pid.getIdAsLong(), nextOr);
					} else {
						throw new IllegalArgumentException("Invalid token type (expecting ReferenceParam): " + nextOr.getClass());
					}
				}
			}

			theSearchParameterMap.remove(getSubjectParamName());
			theSearchParameterMap.remove(getPatientParamName());
			for (Long subjectPid : orderedSubjectReferenceMap.keySet()) {
				theSearchParameterMap.add(getSubjectParamName(), orderedSubjectReferenceMap.get(subjectPid));
			}
		}

	}

	abstract protected String getEffectiveParamName();
	abstract protected String getCodeParamName();
	abstract protected String getSubjectParamName();
	abstract protected String getPatientParamName();

}
