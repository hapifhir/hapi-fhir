package ca.uhn.fhir.jpa.bulk.job;

/*-
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupBulkItemReader implements ItemReader<List<ResourcePersistentId>> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	Iterator<ResourcePersistentId> myPidIterator;
	@Value("#{jobParameters['readChunkSize']}")
	private Long READ_CHUNK_SIZE;
	@Value("#{jobExecutionContext['jobUUID']}")
	private String myJobUUID;
	@Value("#{stepExecutionContext['resourceType']}")
	private String myResourceType;

	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;
	@Autowired
	private EntityManager myEntityManager;

	private void loadResourcePids() {
		Optional<BulkExportJobEntity> jobOpt = myBulkExportJobDao.findByJobId(myJobUUID);
		if (!jobOpt.isPresent()) {
			ourLog.warn("Job appears to be deleted");
			return;
		}
		BulkExportJobEntity jobEntity = jobOpt.get();
		ourLog.info("Group Bulk export starting generation for batch export job: [{}] with resourceType [{}] and UUID [{}]", jobEntity, myResourceType, myJobUUID);

		//Fetch all the pids given the query.
		ISearchBuilder searchBuilder = getSearchBuilder();

		//Build comlex-ish _has query with a revincludes which allows lookup by group membership
		SearchParameterMap searchParameterMap = getSearchParameterMap(jobEntity);

		IResultIterator resultIterator = searchBuilder.createQuery(
			searchParameterMap,
			new SearchRuntimeDetails(null, myJobUUID),
			null,
			RequestPartitionId.allPartitions()
		);

		List<ResourcePersistentId> myReadPids = new ArrayList<>();
		while (resultIterator.hasNext()) {
			myReadPids.add(resultIterator.next());
		}

		//Given that databases explode when you have an IN clause with >1000 resources, we use the QueryChunker to break this into multiple queries.
		List<ResourcePersistentId> revIncludePids = new ArrayList<>();
		QueryChunker<ResourcePersistentId> chunker = new QueryChunker<>();

		chunker.chunk(myReadPids, pidChunk -> {
			revIncludePids.addAll(searchBuilder.loadIncludes(myContext, myEntityManager, pidChunk, searchParameterMap.getRevIncludes(), true, searchParameterMap.getLastUpdated(), myJobUUID, null));
		});

		myPidIterator = revIncludePids.iterator();
	}

	//For all group revinclude queries, you need to perform the search on the Patient DAO, which is why this is hardcoded here.
	private ISearchBuilder getSearchBuilder() {
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao("Patient");
		RuntimeResourceDefinition def = myContext.getResourceDefinition("Patient");
		Class<? extends IBaseResource> nextTypeClass = def.getImplementingClass();
		return mySearchBuilderFactory.newSearchBuilder(dao, "Patient", nextTypeClass);
	}

	@Nonnull
	private SearchParameterMap getSearchParameterMap(BulkExportJobEntity jobEntity) {
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		String groupIdFromRequest = getGroupIdFromRequest(jobEntity);
		searchParameterMap.add("_has", new HasParam("Group", "member", "_id", groupIdFromRequest));


		String revIncludeString = buildRevIncludeString();
		searchParameterMap.addRevInclude(new Include(revIncludeString).toLocked());

		if (jobEntity.getSince() != null) {
			searchParameterMap.setLastUpdated(new DateRangeParam(jobEntity.getSince(), null));
		}
		searchParameterMap.setLoadSynchronous(true);
		return searchParameterMap;
	}

	/**
	 * Given the resource type of the job, fetch its patient compartment name, formatted for usage in an Include.
	 * e.g. Immunization -> Immunization:patient
	 *
	 * @return A string which can be dropped directly into an Include.
	 */
	private String buildRevIncludeString() {
		RuntimeResourceDefinition runtimeResourceDefinition = myContext.getResourceDefinition(myResourceType);
		List<RuntimeSearchParam> searchParams = runtimeResourceDefinition.getSearchParamsForCompartmentName("Patient");
		if (searchParams == null || searchParams.size() == 0) {
			String errorMessage = String.format("Resource type [%s] is not eligible for Group Bulk export, as it contains no Patient compartment", myResourceType);
			throw new IllegalArgumentException(errorMessage);
		} else {
			//The reason we grab the first here is that even if there _are_ multiple search params, they end up pointing to the same patient compartment.
			//So we can safely just grab the first.
			RuntimeSearchParam runtimeSearchParam = searchParams.get(0);
			String includeString = runtimeResourceDefinition.getName() + ":" + runtimeSearchParam.getName();
			return includeString;

		}
	}

	private String getGroupIdFromRequest(BulkExportJobEntity theJobEntity) {
		Map<String, String[]> requestUrl = UrlUtil.parseQueryStrings(theJobEntity.getRequest());
		String[] groupId= requestUrl.get(JpaConstants.PARAM_EXPORT_GROUP_ID);
		if (groupId != null) {
			return Arrays.stream(groupId).collect(Collectors.joining(","));
		} else {
			throw new IllegalStateException("You cannot run a Group export job without a " + JpaConstants.PARAM_EXPORT_GROUP_ID + " parameter as part of the request.");
		}
	}

	@Override
	public List<ResourcePersistentId> read() {
		if (myPidIterator == null) {
			loadResourcePids();
		}
		int count = 0;
		List<ResourcePersistentId> outgoing = new ArrayList<>();
		while (myPidIterator.hasNext() && count < READ_CHUNK_SIZE) {
			outgoing.add(myPidIterator.next());
			count += 1;
		}

		return outgoing.size() == 0 ? null : outgoing;

	}
}
