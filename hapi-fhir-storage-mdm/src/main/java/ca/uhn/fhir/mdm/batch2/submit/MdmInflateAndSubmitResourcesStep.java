package ca.uhn.fhir.mdm.batch2.submit;

/*-
 * #%L
 * hapi-fhir-storage-mdm
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
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.mdm.api.IMdmChannelSubmitterSvc;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationSvc;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MdmInflateAndSubmitResourcesStep implements IJobStepWorker<MdmSubmitJobParameters, ResourceIdListWorkChunkJson, VoidModel> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired(required = false)
	private ResponseTerminologyTranslationSvc myResponseTerminologyTranslationSvc;
	@Autowired
	private IMdmChannelSubmitterSvc myMdmChannelSubmitterSvc;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<MdmSubmitJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails,
								 @Nonnull IJobDataSink<VoidModel> theDataSink) throws JobExecutionFailedException {
		ResourceIdListWorkChunkJson idList = theStepExecutionDetails.getData();

		ourLog.info("Final Step  for $mdm-submit - Expand and submit resources");
		ourLog.info("About to expand {} resource IDs into their full resource bodies.", idList.getResourcePersistentIds().size());

		//Inflate the resources by PID
		List<IBaseResource> allResources = fetchAllResources(idList.getResourcePersistentIds());

		//Replace the terminology
		if (myResponseTerminologyTranslationSvc != null) {
			myResponseTerminologyTranslationSvc.processResourcesForTerminologyTranslation(allResources);
		}

		//Submit
		for (IBaseResource nextResource : allResources) {
			myMdmChannelSubmitterSvc.submitResourceToMdmChannel(nextResource);
		}

		ourLog.info("Expanding of {} resources of type {} completed", idList.size(), idList.getResourceType(0));//FIXME GGG
		return new RunOutcome(allResources.size());
	}

	private List<IBaseResource> fetchAllResources(List<ResourcePersistentId> theIds) {
		List<IBaseResource> resources = new ArrayList<>();
		for (ResourcePersistentId id : theIds) {
			assert id.getResourceType() != null;
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(id.getResourceType());
			// This should be a query, but we have PIDs, and we don't have a _pid search param. TODO GGG, figure out how to make this search by pid.
			resources.add(dao.readByPid(id));
		}
		return resources;
	}
}
