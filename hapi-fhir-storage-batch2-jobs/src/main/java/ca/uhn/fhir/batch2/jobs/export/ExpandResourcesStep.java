package ca.uhn.fhir.batch2.jobs.export;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportExpandedResources;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportIdList;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.models.Id;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationSvc;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

public class ExpandResourcesStep implements IJobStepWorker<BulkExportJobParameters, BulkExportIdList, BulkExportExpandedResources> {
	private static final Logger ourLog = getLogger(ExpandResourcesStep.class);

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IBulkExportProcessor myBulkExportProcessor;

	@Autowired(required = false)
	private ResponseTerminologyTranslationSvc myResponseTerminologyTranslationSvc;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<BulkExportJobParameters, BulkExportIdList> theStepExecutionDetails,
								 @Nonnull IJobDataSink<BulkExportExpandedResources> theDataSink) throws JobExecutionFailedException {
		BulkExportIdList idList = theStepExecutionDetails.getData();
		BulkExportJobParameters jobParameters = theStepExecutionDetails.getParameters();

		ourLog.info("Step 2 for bulk export - Expand resources");
		ourLog.info("About to expand {} resource IDs into their full resource bodies.", idList.getIds().size());

		// search the resources
		List<IBaseResource> allResources = fetchAllResources(idList);


		// if necessary, expand resources
		if (jobParameters.isExpandMdm()) {
			myBulkExportProcessor.expandMdmResources(allResources);
		}

		if (myResponseTerminologyTranslationSvc != null) {
			myResponseTerminologyTranslationSvc.processResourcesForTerminologyTranslation(allResources);
		}

		// encode them
		ListMultimap<String, String> resources = encodeToString(allResources, jobParameters);

		// set to datasink
		for (String nextResourceType : resources.keySet()) {

			BulkExportExpandedResources output = new BulkExportExpandedResources();
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

	private List<IBaseResource> fetchAllResources(BulkExportIdList theIds) {
		List<IBaseResource> resources = new ArrayList<>();

		for (Id id : theIds.getIds()) {
			String value = id.getId();
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(id.getResourceType());
			// This should be a query, but we have PIDs, and we don't have a _pid search param. TODO GGG, figure out how to make this search by pid.
			resources.add(dao.readByPid(new ResourcePersistentId(value)));
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
