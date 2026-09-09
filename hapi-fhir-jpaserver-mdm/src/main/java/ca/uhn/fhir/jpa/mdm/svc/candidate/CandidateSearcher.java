/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.mdm.svc.candidate;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.svc.MdmSearchParamSvc;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CandidateSearcher {
	private static final Logger ourLog = LoggerFactory.getLogger(CandidateSearcher.class);
	private final DaoRegistry myDaoRegistry;
	private final IMdmSettings myMdmSettings;
	private final MdmSearchParamSvc myMdmSearchParamSvc;

	@Autowired
	public CandidateSearcher(
			DaoRegistry theDaoRegistry, IMdmSettings theMdmSettings, MdmSearchParamSvc theMdmSearchParamSvc) {
		myDaoRegistry = theDaoRegistry;
		myMdmSettings = theMdmSettings;
		myMdmSearchParamSvc = theMdmSearchParamSvc;
	}

	/**
	 * Perform a search for mdm candidates.
	 *
	 * @param theResourceType     the type of resources searched on
	 * @param theResourceCriteria the criteria used to search for the candidates
	 * @param partitionId         the partition for the search
	 * @return Optional.empty() if >= IMdmSettings.getCandidateSearchLimit() candidates are found, otherwise
	 * return the bundle provider for the search results.
	 */
	public Optional<IBundleProvider> search(
			String theResourceType,
			String theResourceCriteria,
			RequestPartitionId partitionId,
			MdmTransactionContext theContext) {
		SearchParameterMap searchParameterMap =
				myMdmSearchParamSvc.mapFromCriteria(theResourceType, theResourceCriteria);

		searchParameterMap.setLoadSynchronousUpTo(myMdmSettings.getCandidateSearchLimit());

		IFhirResourceDao<?> resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		systemRequestDetails.setRequestPartitionId(partitionId);
		IBundleProvider retval = resourceDao.search(searchParameterMap, systemRequestDetails);

		if (retval.size() != null) {
			if (retval.size() >= myMdmSettings.getCandidateSearchLimit()) {
				ourLog.warn(
						"At least {} search candidates were returned for search criteria {}; this is the configured maximum candidates to allow. Resource will be omitted from further MDM matching.",
						retval.size(),
						theResourceCriteria);
				theContext.setTooManyCandidatesMatched(true);
				return Optional.empty();
			} else if (retval.size() >= myMdmSettings.getCandidateSearchWarnLimit()) {
				ourLog.warn(
						"Candidate search yielded {} results for search criteria {}; more than the warning level, but not enough to halt MDM matching.",
						retval.size(),
						theResourceCriteria);
			}
		}

		return Optional.of(retval);
	}

	/**
	 * Perform a search for mdm candidates.
	 *
	 * @param theResourceType     the type of resources searched on
	 * @param theResourceCriteria the criteria used to search for the candidates
	 * @return Optional.empty() if >= IMdmSettings.getCandidateSearchLimit() candidates are found, otherwise
	 * return the bundle provider for the search results.
	 */
	public Optional<IBundleProvider> search(
			String theResourceType, String theResourceCriteria, MdmTransactionContext theContext) {
		return this.search(theResourceType, theResourceCriteria, RequestPartitionId.allPartitions(), theContext);
	}

	public static String idOrType(IAnyResource theResource, String theResourceType) {
		if (theResource.getIdElement() == null || theResource.getIdElement().isEmpty()) {
			return theResourceType;
		}
		return theResource.getIdElement().toUnqualifiedVersionless().toString();
	}
}
