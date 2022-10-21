package ca.uhn.fhir.batch2.jobs.mdm;

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

import ca.uhn.fhir.batch2.api.*;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.mdm.models.MdmSubmitJobParameters;
import ca.uhn.fhir.batch2.jobs.models.Id;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationSvc;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class ExpandResourcesStep implements IJobStepWorker<MdmSubmitJobParameters, ResourceIdList, ExpandedResourcesList> {
	private static final Logger ourLog = getLogger(ExpandResourcesStep.class);

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired(required = false)
	private ResponseTerminologyTranslationSvc myResponseTerminologyTranslationSvc;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<MdmSubmitJobParameters, ResourceIdList> theStepExecutionDetails,
								 @Nonnull IJobDataSink<ExpandedResourcesList> theDataSink) throws JobExecutionFailedException {
		ResourceIdList idList = theStepExecutionDetails.getData();
		MdmSubmitJobParameters jobParameters = theStepExecutionDetails.getParameters();

		ourLog.info("Step 2 for $mdm-submit - Expand resources");
		ourLog.info("About to expand {} resource IDs into their full resource bodies.", idList.getIds().size());

		// search the resources
		List<IBaseResource> allResources = fetchAllResources(idList);

		if (myResponseTerminologyTranslationSvc != null) {
			myResponseTerminologyTranslationSvc.processResourcesForTerminologyTranslation(allResources);
		}

		// encode them
		IParser parser = myFhirContext.newJsonParser().setPrettyPrint(false);
		List<String> stringified = allResources.stream().map(parser::encodeResourceToString).collect(Collectors.toList());;

		ExpandedResourcesList output = new ExpandedResourcesList();
		output.setStringifiedResources(stringified);
		output.setResourceType(idList.getResourceType());
		theDataSink.accept(output);

		ourLog.info("Expanding of {} resources of type {} completed", idList.getIds().size(), idList.getResourceType());

		// and return
		return RunOutcome.SUCCESS;
	}

	private List<IBaseResource> fetchAllResources(ResourceIdList theIds) {
		List<IBaseResource> resources = new ArrayList<>();
		for (Id id : theIds.getIds()) {
			String value = id.getId();
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(id.getResourceType());
			// This should be a query, but we have PIDs, and we don't have a _pid search param. TODO GGG, figure out how to make this search by pid.
			resources.add(dao.readByPid(new ResourcePersistentId(value)));
		}
		return resources;
	}
}
