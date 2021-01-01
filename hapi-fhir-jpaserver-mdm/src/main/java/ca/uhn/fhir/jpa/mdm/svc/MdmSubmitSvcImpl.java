package ca.uhn.fhir.jpa.mdm.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
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

import ca.uhn.fhir.mdm.api.IMdmChannelSubmitterSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.IMdmSubmitSvc;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MdmSubmitSvcImpl implements IMdmSubmitSvc {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private MdmSearchParamSvc myMdmSearchParamSvc;

	@Autowired
	private IMdmChannelSubmitterSvc myMdmChannelSubmitterSvc;

	@Autowired
	private IMdmSettings myMdmSettings;

	private static final int BUFFER_SIZE = 100;

	@Override
	@Transactional
	public long submitAllSourceTypesToMdm(@Nullable String theCriteria) {
		long submittedCount = myMdmSettings.getMdmRules().getMdmTypes().stream()
			.mapToLong(type -> submitSourceResourceTypeToMdm(type, theCriteria))
			.sum();

		return submittedCount;
	}

	@Override
	@Transactional
	public long submitSourceResourceTypeToMdm(String theSourceResourceType, @Nullable String theCriteria) {
		if (theCriteria == null) {
			ourLog.info("Submitting all resources of type {} to MDM", theSourceResourceType);
		} else {
			ourLog.info("Submitting resources of type {} with criteria {} to MDM", theSourceResourceType, theCriteria);
		}

		validateSourceType(theSourceResourceType);
		SearchParameterMap spMap = myMdmSearchParamSvc.getSearchParameterMapFromCriteria(theSourceResourceType, theCriteria);
		spMap.setLoadSynchronousUpTo(BUFFER_SIZE);
		ISearchBuilder searchBuilder = myMdmSearchParamSvc.generateSearchBuilderForType(theSourceResourceType);
		return submitAllMatchingResourcesToMdmChannel(spMap, searchBuilder);
	}

	private long submitAllMatchingResourcesToMdmChannel(SearchParameterMap theSpMap, ISearchBuilder theSearchBuilder) {
		SearchRuntimeDetails searchRuntimeDetails = new SearchRuntimeDetails(null, UUID.randomUUID().toString());
		long total = 0;
		try (IResultIterator query = theSearchBuilder.createQuery(theSpMap, searchRuntimeDetails, null, RequestPartitionId.defaultPartition())) {
			Collection<ResourcePersistentId> pidBatch;
			do {
				pidBatch = query.getNextResultBatch(BUFFER_SIZE);
				total += loadPidsAndSubmitToMdmChannel(theSearchBuilder, pidBatch);
			} while (query.hasNext());
		} catch (IOException theE) {
			throw new InternalErrorException("Failure while attempting to query resources for " + ProviderConstants.OPERATION_MDM_SUBMIT, theE);
		}
		ourLog.info("MDM Submit complete.  Submitted a total of {} resources.", total);
		return total;
	}

	/**
	 * Given a collection of ResourcePersistentId objects, and a search builder, load the IBaseResources and submit them to
	 * the MDM channel for processing.
	 *
	 * @param theSearchBuilder the related DAO search builder.
	 * @param thePidsToSubmit The collection of PIDs whos resources you want to submit for MDM processing.
	 *
	 * @return The total count of submitted resources.
	 */
	private long loadPidsAndSubmitToMdmChannel(ISearchBuilder theSearchBuilder, Collection<ResourcePersistentId> thePidsToSubmit) {
		List<IBaseResource> resourcesToSubmit = new ArrayList<>();
		theSearchBuilder.loadResourcesByPid(thePidsToSubmit, Collections.emptyList(), resourcesToSubmit, false, null);
		ourLog.info("Submitting {} resources to MDM", resourcesToSubmit.size());
		resourcesToSubmit
			.forEach(resource -> myMdmChannelSubmitterSvc.submitResourceToMdmChannel(resource));
		return resourcesToSubmit.size();
	}

	@Override
	@Transactional
	public long submitPractitionerTypeToMdm(@Nullable String theCriteria) {
		return submitSourceResourceTypeToMdm("Practitioner", theCriteria);
	}

	@Override
	@Transactional
	public long submitPatientTypeToMdm(@Nullable String theCriteria) {
		return submitSourceResourceTypeToMdm("Patient", theCriteria);
	}

	@Override
	@Transactional
	public long submitSourceResourceToMdm(IIdType theId) {
		validateSourceType(theId.getResourceType());
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theId.getResourceType());
		IBaseResource read = resourceDao.read(theId);
		myMdmChannelSubmitterSvc.submitResourceToMdmChannel(read);
		return 1;
	}

	@Override
	public void setMdmSettings(IMdmSettings theMdmSettings) {
		myMdmSettings = theMdmSettings;
	}

	private void validateSourceType(String theResourceType) {
		if(!myMdmSettings.getMdmRules().getMdmTypes().contains(theResourceType)) {
			throw new InvalidRequestException(ProviderConstants.OPERATION_MDM_SUBMIT + " does not support resource type: " + theResourceType);
		}
	}
}
