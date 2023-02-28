package ca.uhn.fhir.batch2.jobs.export;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationSvc;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.PARAM_ID;
import static org.slf4j.LoggerFactory.getLogger;

public class ExpandResourcesStep implements IJobStepWorker<BulkExportJobParameters, ResourceIdList, ExpandedResourcesList> {
	private static final Logger ourLog = getLogger(ExpandResourcesStep.class);

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IBulkExportProcessor myBulkExportProcessor;

	@Autowired
	private ApplicationContext myApplicationContext;

	@Autowired
	private StorageSettings myStorageSettings;

	@Autowired
	private IIdHelperService myIdHelperService;

	@Autowired
	private IHapiTransactionService myTransactionService;

	private volatile ResponseTerminologyTranslationSvc myResponseTerminologyTranslationSvc;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails,
								 @Nonnull IJobDataSink<ExpandedResourcesList> theDataSink) throws JobExecutionFailedException {
		ResourceIdList idList = theStepExecutionDetails.getData();
		BulkExportJobParameters jobParameters = theStepExecutionDetails.getParameters();

		ourLog.info("Step 2 for bulk export - Expand resources");
		ourLog.info("About to expand {} resource IDs into their full resource bodies.", idList.getIds().size());

		// search the resources
		List<IBaseResource> allResources = fetchAllResources(idList);


		// if necessary, expand resources
		if (jobParameters.isExpandMdm()) {
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

		// encode them - Key is resource type, Value is a collection of serialized resources of that type
		ListMultimap<String, String> resources = encodeToString(allResources, jobParameters);

		// set to datasink
		for (String nextResourceType : resources.keySet()) {

			ExpandedResourcesList output = new ExpandedResourcesList();
			output.setStringifiedResources(resources.get(nextResourceType));
			output.setResourceType(nextResourceType);
			theDataSink.accept(output);

			ourLog.info("Expanding of {} resources of type {} completed",
				idList.getIds().size(),
				idList.getResourceType());


		}

		// and return
		return RunOutcome.SUCCESS;
	}

	private List<IBaseResource> fetchAllResources(ResourceIdList theIds) {
		ArrayListMultimap<String, String> typeToIds = ArrayListMultimap.create();
		theIds.getIds().forEach(t -> typeToIds.put(t.getResourceType(), t.getId()));

		List<IBaseResource> resources = new ArrayList<>(theIds.getIds().size());

		for (String resourceType : typeToIds.keySet()) {

			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);
			List<String> allIds = typeToIds.get(resourceType);
			while (!allIds.isEmpty()) {

				// Load in batches in order to avoid having too many PIDs go into a
				// single SQ statement at once
				int batchSize = Math.min(500, allIds.size());

				Set<IResourcePersistentId> nextBatchOfPids =
					allIds
						.subList(0, batchSize)
						.stream()
						.map(t -> myIdHelperService.newPidFromStringIdAndResourceName(t, resourceType))
						.collect(Collectors.toSet());
				allIds = allIds.subList(batchSize, allIds.size());

				PersistentIdToForcedIdMap nextBatchOfResourceIds = myTransactionService
					.withRequest(null)
					.execute(() -> myIdHelperService.translatePidsToForcedIds(nextBatchOfPids));

				TokenOrListParam idListParam = new TokenOrListParam();
				for (IResourcePersistentId nextPid : nextBatchOfPids) {
					Optional<String> resourceId = nextBatchOfResourceIds.get(nextPid);
					idListParam.add(resourceId.orElse(nextPid.getId().toString()));
				}

				SearchParameterMap spMap = SearchParameterMap
					.newSynchronous()
					.add(PARAM_ID, idListParam);
				IBundleProvider outcome = dao.search(spMap, new SystemRequestDetails());
				resources.addAll(outcome.getAllResources());
			}

		}

		return resources;
	}

	/**
	 * @return A map - Key is resource type, Value is a collection of serialized resources of that type
	 */
	private ListMultimap<String, String> encodeToString(List<IBaseResource> theResources, BulkExportJobParameters theParameters) {
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
}
