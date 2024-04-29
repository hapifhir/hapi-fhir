/*-
 * #%L
 * hapi-fhir-storage-mdm
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
package ca.uhn.fhir.mdm.batch2.submit;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.mdm.api.IMdmChannelSubmitterSvc;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationSvc;
import ca.uhn.fhir.util.Logs;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MdmInflateAndSubmitResourcesStep
		implements IJobStepWorker<MdmSubmitJobParameters, ResourceIdListWorkChunkJson, VoidModel> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired(required = false)
	private ResponseTerminologyTranslationSvc myResponseTerminologyTranslationSvc;

	@Autowired
	private IMdmChannelSubmitterSvc myMdmChannelSubmitterSvc;

	@Autowired
	private IIdHelperService<? extends IResourcePersistentId> myIdHelperService;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<MdmSubmitJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<VoidModel> theDataSink)
			throws JobExecutionFailedException {
		ResourceIdListWorkChunkJson idList = theStepExecutionDetails.getData();

		ourLog.info("Final Step  for $mdm-submit - Expand and submit resources");
		ourLog.info(
				"About to expand {} resource IDs into their full resource bodies.",
				idList.getResourcePersistentIds(myIdHelperService).size());

		// Inflate the resources by PID
		List<IBaseResource> allResources = fetchAllResources(idList.getResourcePersistentIds(myIdHelperService));

		// Replace the terminology
		if (myResponseTerminologyTranslationSvc != null) {
			myResponseTerminologyTranslationSvc.processResourcesForTerminologyTranslation(allResources);
		}

		// Submit
		for (IBaseResource nextResource : allResources) {
			myMdmChannelSubmitterSvc.submitResourceToMdmChannel(nextResource);
		}

		ourLog.info("Expanding of {} resources of type completed", idList.size());
		return new RunOutcome(allResources.size());
	}

	private List<IBaseResource> fetchAllResources(List<? extends IResourcePersistentId> theIds) {
		List<IBaseResource> resources = new ArrayList<>();
		for (IResourcePersistentId id : theIds) {
			assert id.getResourceType() != null;
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(id.getResourceType());
			// This should be a query, but we have PIDs, and we don't have a _pid search param. TODO GGG, figure out how
			// to make this search by pid.
			try {
				resources.add(dao.readByPid(id));
			} catch (ResourceNotFoundException e) {
				ourLog.warn("While attempging to send [{}] to the MDM queue, the resource was not found.", id);
			}
		}
		return resources;
	}
}
