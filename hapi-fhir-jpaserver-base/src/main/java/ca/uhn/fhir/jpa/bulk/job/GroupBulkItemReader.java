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
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IBaseReference;
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

public class GroupBulkItemReader implements ItemReader<List<ResourcePersistentId>> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	Iterator<ResourcePersistentId> myPidIterator;
	@Value("#{jobParameters['readChunkSize']}")
	private Long READ_CHUNK_SIZE;
	@Value("#{jobExecutionContext['jobUUID']}")
	private String myJobUUID;
	@Value("#{stepExecutionContext['resourceType']}")
	private String myResourceType;
	@Value("#{jobParameters['groupId']}")
	private String myGroupId;

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

	@Autowired
	private MatchUrlService myMatchUrlService;

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
		SearchParameterMap searchParameterMap = getParameterMap(jobEntity);

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
		Set<ResourcePersistentId> resourcePersistentIds = searchBuilder.loadIncludes(myContext, myEntityManager, myReadPids, searchParameterMap.getRevIncludes(), true, searchParameterMap.getLastUpdated(), myJobUUID, null);
		myPidIterator = resourcePersistentIds.iterator();
	}

	private ISearchBuilder getSearchBuilder() {
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao("Patient");
		RuntimeResourceDefinition def = myContext.getResourceDefinition("Patient");
		Class<? extends IBaseResource> nextTypeClass = def.getImplementingClass();
		return mySearchBuilderFactory.newSearchBuilder(dao, "Patient", nextTypeClass);
	}

	@Nonnull
	private SearchParameterMap getParameterMap(BulkExportJobEntity jobEntity) {
		SearchParameterMap spm = new SearchParameterMap();
		spm.add("_has", new HasParam("Group", "member", "_id", myGroupId));
		spm.addRevInclude(new Include(lookupRevIncludeParameter(myResourceType)).toLocked());
		if (jobEntity.getSince() != null) {
			spm.setLastUpdated(new DateRangeParam(jobEntity.getSince(), null));
		}
		spm.setLoadSynchronous(true);
		return spm;
	}

	private String lookupRevIncludeParameter(String theResourceType) {
		switch (theResourceType) {
			case "Immunization":
				return "Immunization:patient";
			case "Observation":
				return "Observation:patient";
			default:
				throw new UnsupportedOperationException("You cannot currently do a group bulk export for type " + theResourceType);

		}
	}

	private SearchParameterMap createSearchParameterMapFromTypeFilter(BulkExportJobEntity theJobEntity, RuntimeResourceDefinition theDef) {
		SearchParameterMap map = new SearchParameterMap();
		Map<String, String[]> requestUrl = UrlUtil.parseQueryStrings(theJobEntity.getRequest());
		String[] typeFilters = requestUrl.get(JpaConstants.PARAM_EXPORT_TYPE_FILTER);
		if (typeFilters != null) {
			Optional<String> filter = Arrays.stream(typeFilters).filter(t -> t.startsWith(myResourceType + "?")).findFirst();
			if (filter.isPresent()) {
				String matchUrl = filter.get();
				map = myMatchUrlService.translateMatchUrl(matchUrl, theDef);
			}
		}
		return map;
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
