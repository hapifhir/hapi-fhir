/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.models.BatchResourceId;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationSvc;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import jakarta.annotation.Nonnull;
import org.apache.commons.collections4.ListUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.PARAM_ID;
import static org.slf4j.LoggerFactory.getLogger;

public class ExpandResourcesStep
		implements IJobStepWorker<BulkExportJobParameters, ResourceIdList, ExpandedResourcesList> {
	private static final Logger ourLog = getLogger(ExpandResourcesStep.class);

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IBulkExportProcessor<?> myBulkExportProcessor;

	@Autowired
	private ApplicationContext myApplicationContext;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private IIdHelperService myIdHelperService;

	@Autowired
	private IHapiTransactionService myTransactionService;

	@Autowired
	private InMemoryResourceMatcher myInMemoryResourceMatcher;

	@Autowired
	private InterceptorService myInterceptorService;

	private volatile ResponseTerminologyTranslationSvc myResponseTerminologyTranslationSvc;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails,
			@Nonnull IJobDataSink<ExpandedResourcesList> theDataSink)
			throws JobExecutionFailedException {
		String instanceId = theStepExecutionDetails.getInstance().getInstanceId();
		String chunkId = theStepExecutionDetails.getChunkId();
		ResourceIdList data = theStepExecutionDetails.getData();
		BulkExportJobParameters parameters = theStepExecutionDetails.getParameters();

		ourLog.info(
				"Bulk export instance[{}] chunk[{}] - About to expand {} resource IDs into their full resource bodies.",
				instanceId,
				chunkId,
				data.getIds().size());

		// Partition the ID list in order to only fetch a reasonable number at a time
		List<List<BatchResourceId>> idLists = ListUtils.partition(data.getIds(), 100);

		for (List<BatchResourceId> idList : idLists) {

			// search the resources
			List<IBaseResource> allResources = fetchAllResources(idList, parameters.getPartitionId());

			// Apply post-fetch filtering
			String resourceType = data.getResourceType();
			List<String> postFetchFilterUrls = parameters.getPostFetchFilterUrls().stream()
					.filter(t -> t.substring(0, t.indexOf('?')).equals(resourceType))
					.collect(Collectors.toList());

			if (!postFetchFilterUrls.isEmpty()) {
				applyPostFetchFiltering(allResources, postFetchFilterUrls, instanceId, chunkId);
			}

			// if necessary, expand resources
			if (parameters.isExpandMdm()) {
				myBulkExportProcessor.expandMdmResources(allResources);
			}

			// Normalize terminology
			if (myStorageSettings.isNormalizeTerminologyForBulkExportJobs()) {
				ResponseTerminologyTranslationSvc terminologyTranslationSvc = myResponseTerminologyTranslationSvc;
				if (terminologyTranslationSvc == null) {
					terminologyTranslationSvc = myApplicationContext.getBean(ResponseTerminologyTranslationSvc.class);
					myResponseTerminologyTranslationSvc = terminologyTranslationSvc;
				}
				terminologyTranslationSvc.processResourcesForTerminologyTranslation(allResources);
			}

			// Interceptor call
			if (myInterceptorService.hasHooks(Pointcut.STORAGE_BULK_EXPORT_RESOURCE_INCLUSION)) {
				for (Iterator<IBaseResource> iter = allResources.iterator(); iter.hasNext(); ) {
					HookParams params = new HookParams()
							.add(BulkExportJobParameters.class, theStepExecutionDetails.getParameters())
							.add(IBaseResource.class, iter.next());
					boolean outcome =
							myInterceptorService.callHooks(Pointcut.STORAGE_BULK_EXPORT_RESOURCE_INCLUSION, params);
					if (!outcome) {
						iter.remove();
					}
				}
			}

			// encode them - Key is resource type, Value is a collection of serialized resources of that type
			ListMultimap<String, String> resources = encodeToString(allResources, parameters);

			// send to datasink
			long maxFileSize = myStorageSettings.getBulkExportFileMaximumSize();
			long currentFileSize = 0;
			for (String nextResourceType : resources.keySet()) {

				List<String> stringifiedResources = resources.get(nextResourceType);
				List<String> currentFileStringifiedResources = new ArrayList<>();

				for (String nextStringifiedResource : stringifiedResources) {

					if (currentFileSize + nextStringifiedResource.length() > maxFileSize
							&& !currentFileStringifiedResources.isEmpty()) {
						ExpandedResourcesList output = new ExpandedResourcesList();
						output.setStringifiedResources(currentFileStringifiedResources);
						output.setResourceType(nextResourceType);
						theDataSink.accept(output);

						currentFileStringifiedResources = new ArrayList<>();
						currentFileSize = 0;
					}

					currentFileStringifiedResources.add(nextStringifiedResource);
					currentFileSize += nextStringifiedResource.length();
				}

				if (!currentFileStringifiedResources.isEmpty()) {
					ExpandedResourcesList output = new ExpandedResourcesList();
					output.setStringifiedResources(currentFileStringifiedResources);
					output.setResourceType(nextResourceType);
					theDataSink.accept(output);
				}

				ourLog.info("Expanding of {} resources of type {} completed", idList.size(), data.getResourceType());
			}
		}

		// and return
		return RunOutcome.SUCCESS;
	}

	private void applyPostFetchFiltering(
			List<IBaseResource> theResources,
			List<String> thePostFetchFilterUrls,
			String theInstanceId,
			String theChunkId) {
		int numRemoved = 0;
		for (Iterator<IBaseResource> iter = theResources.iterator(); iter.hasNext(); ) {
			boolean matched = applyPostFetchFilteringForSingleResource(thePostFetchFilterUrls, iter);

			if (!matched) {
				iter.remove();
				numRemoved++;
			}
		}

		if (numRemoved > 0) {
			ourLog.info(
					"Bulk export instance[{}] chunk[{}] - {} resources were filtered out because of post-fetch filter URLs",
					theInstanceId,
					theChunkId,
					numRemoved);
		}
	}

	private boolean applyPostFetchFilteringForSingleResource(
			List<String> thePostFetchFilterUrls, Iterator<IBaseResource> iter) {
		IBaseResource nextResource = iter.next();
		String nextResourceType = myFhirContext.getResourceType(nextResource);

		for (String nextPostFetchFilterUrl : thePostFetchFilterUrls) {
			if (nextPostFetchFilterUrl.contains("?")) {
				String resourceType = nextPostFetchFilterUrl.substring(0, nextPostFetchFilterUrl.indexOf('?'));
				if (nextResourceType.equals(resourceType)) {
					InMemoryMatchResult matchResult = myInMemoryResourceMatcher.match(
							nextPostFetchFilterUrl, nextResource, null, new SystemRequestDetails());
					if (matchResult.matched()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private List<IBaseResource> fetchAllResources(
			List<BatchResourceId> theIds, RequestPartitionId theRequestPartitionId) {
		ArrayListMultimap<String, String> typeToIds = ArrayListMultimap.create();
		theIds.forEach(t -> typeToIds.put(t.getResourceType(), t.getId()));

		List<IBaseResource> resources = new ArrayList<>(theIds.size());

		for (String resourceType : typeToIds.keySet()) {

			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);
			List<String> allIds = typeToIds.get(resourceType);

			Set<IResourcePersistentId> nextBatchOfPids = allIds.stream()
					.map(t -> myIdHelperService.newPidFromStringIdAndResourceName(t, resourceType))
					.collect(Collectors.toSet());

			PersistentIdToForcedIdMap nextBatchOfResourceIds = myTransactionService
					.withRequest(null)
					.execute(() -> myIdHelperService.translatePidsToForcedIds(nextBatchOfPids));

			TokenOrListParam idListParam = new TokenOrListParam();
			for (IResourcePersistentId nextPid : nextBatchOfPids) {
				Optional<String> resourceId = nextBatchOfResourceIds.get(nextPid);
				idListParam.add(resourceId.orElse(nextPid.getId().toString()));
			}

			SearchParameterMap spMap = SearchParameterMap.newSynchronous().add(PARAM_ID, idListParam);
			IBundleProvider outcome =
					dao.search(spMap, new SystemRequestDetails().setRequestPartitionId(theRequestPartitionId));
			resources.addAll(outcome.getAllResources());
		}

		return resources;
	}

	/**
	 * @return A map - Key is resource type, Value is a collection of serialized resources of that type
	 */
	private ListMultimap<String, String> encodeToString(
			List<IBaseResource> theResources, BulkExportJobParameters theParameters) {
		IParser parser = getParser(theParameters);

		ListMultimap<String, String> retVal = ArrayListMultimap.create();
		for (IBaseResource resource : theResources) {
			String type = myFhirContext.getResourceType(resource);
			String jsonResource = parser.encodeResourceToString(resource);
			retVal.put(type, jsonResource);
		}
		return retVal;
	}

	private IParser getParser(BulkExportJobParameters theParameters) {
		// The parser depends on the
		// output format
		// (but for now, only ndjson is supported
		// see WriteBinaryStep as well
		return myFhirContext.newJsonParser().setPrettyPrint(false);
	}

	@VisibleForTesting
	public void setIdHelperServiceForUnitTest(IIdHelperService theIdHelperService) {
		myIdHelperService = theIdHelperService;
	}
}
