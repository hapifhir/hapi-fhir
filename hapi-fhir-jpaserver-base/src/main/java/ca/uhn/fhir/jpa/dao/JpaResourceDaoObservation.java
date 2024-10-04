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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoObservation;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class JpaResourceDaoObservation<T extends IBaseResource> extends BaseHapiFhirResourceDao<T>
		implements IFhirResourceDaoObservation<T> {

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperService;

	@Override
	public IBundleProvider observationsLastN(
			SearchParameterMap theSearchParameterMap,
			RequestDetails theRequestDetails,
			HttpServletResponse theServletResponse) {
		updateSearchParamsForLastn(theSearchParameterMap, theRequestDetails);

		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperService.determineReadPartitionForRequestForSearchType(
						theRequestDetails, getResourceName(), theSearchParameterMap);
		return mySearchCoordinatorSvc.registerSearch(
				this,
				theSearchParameterMap,
				getResourceName(),
				new CacheControlDirective().parse(theRequestDetails.getHeaders(Constants.HEADER_CACHE_CONTROL)),
				theRequestDetails,
				requestPartitionId);
	}

	private String getEffectiveParamName() {
		return Observation.SP_DATE;
	}

	private String getCodeParamName() {
		return Observation.SP_CODE;
	}

	private String getSubjectParamName() {
		return Observation.SP_SUBJECT;
	}

	private String getPatientParamName() {
		return Observation.SP_PATIENT;
	}

	protected void updateSearchParamsForLastn(
			SearchParameterMap theSearchParameterMap, RequestDetails theRequestDetails) {
		if (!isPagingProviderDatabaseBacked(theRequestDetails)) {
			theSearchParameterMap.setLoadSynchronous(true);
		}

		theSearchParameterMap.setLastN(true);
		SortSpec effectiveDtm = new SortSpec(getEffectiveParamName()).setOrder(SortOrderEnum.DESC);
		SortSpec observationCode =
				new SortSpec(getCodeParamName()).setOrder(SortOrderEnum.ASC).setChain(effectiveDtm);
		if (theSearchParameterMap.containsKey(getSubjectParamName())
				|| theSearchParameterMap.containsKey(getPatientParamName())) {

			new TransactionTemplate(myPlatformTransactionManager)
					.executeWithoutResult(
							tx -> fixSubjectParamsOrderForLastn(theSearchParameterMap, theRequestDetails));

			theSearchParameterMap.setSort(new SortSpec(getSubjectParamName())
					.setOrder(SortOrderEnum.ASC)
					.setChain(observationCode));
		} else {
			theSearchParameterMap.setSort(observationCode);
		}
	}

	private void fixSubjectParamsOrderForLastn(
			SearchParameterMap theSearchParameterMap, RequestDetails theRequestDetails) {
		// Need to ensure that the patient/subject parameters are sorted in the SearchParameterMap to ensure correct
		// ordering of
		// the output. The reason for this is that observations are indexed by patient/subject forced ID, but then
		// ordered in the
		// final result set by subject/patient resource PID.
		TreeMap<Long, IQueryParameterType> orderedSubjectReferenceMap = new TreeMap<>();
		if (theSearchParameterMap.containsKey(getSubjectParamName())) {

			RequestPartitionId requestPartitionId =
					myRequestPartitionHelperService.determineReadPartitionForRequestForSearchType(
							theRequestDetails, getResourceName(), theSearchParameterMap);

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
						JpaPid pid = myIdHelperService.resolveResourcePersistentIds(
								requestPartitionId, ref.getResourceType(), ref.getIdPart());
						orderedSubjectReferenceMap.put(pid.getId(), nextOr);
					} else {
						throw new IllegalArgumentException(
								Msg.code(942) + "Invalid token type (expecting ReferenceParam): " + nextOr.getClass());
					}
				}
			}

			theSearchParameterMap.remove(getSubjectParamName());
			theSearchParameterMap.remove(getPatientParamName());

			// Subject PIDs ordered - so create 'OR' list of subjects for lastN operation
			ReferenceOrListParam orList = new ReferenceOrListParam();
			orderedSubjectReferenceMap
					.keySet()
					.forEach(key -> orList.addOr((ReferenceParam) orderedSubjectReferenceMap.get(key)));
			theSearchParameterMap.add(getSubjectParamName(), orList);
		}
	}
}
