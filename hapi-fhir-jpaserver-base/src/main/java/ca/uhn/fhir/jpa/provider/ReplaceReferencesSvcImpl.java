/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencesJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.util.StopLimitAccumulator;
import com.google.common.collect.Lists;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PreDestroy;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencesAppCtx.JOB_REPLACE_REFERENCES;
import static ca.uhn.fhir.jpa.patch.FhirPatch.OPERATION_REPLACE;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_OPERATION;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_PATH;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_TYPE;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_VALUE;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.HAPI_BATCH_JOB_ID_SYSTEM;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK;

public class ReplaceReferencesSvcImpl implements IReplaceReferencesSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(ReplaceReferencesSvcImpl.class);
	public static final String RESOURCE_TYPES_SYSTEM = "http://hl7.org/fhir/ValueSet/resource-types";
	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;
	private final HapiTransactionService myHapiTransactionService;
	private final IResourceLinkDao myResourceLinkDao;
	private final IJobCoordinator myJobCoordinator;
	// FIXME remove
	private final ExecutorService myFakeExecutor = Executors.newSingleThreadExecutor();

	// FIXME remove
	@PreDestroy
	public void preDestroy() {
		myFakeExecutor.shutdown();
	}

	public ReplaceReferencesSvcImpl(
			FhirContext theFhirContext,
			DaoRegistry theDaoRegistry,
			HapiTransactionService theHapiTransactionService,
			IdHelperService theIdHelperService,
			IResourceLinkDao theResourceLinkDao,
			IJobCoordinator theJobCoordinator) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
		myHapiTransactionService = theHapiTransactionService;
		myResourceLinkDao = theResourceLinkDao;
		myJobCoordinator = theJobCoordinator;
	}

	@Override
	public IBaseParameters replaceReferences(
			ReplaceReferenceRequest theReplaceReferenceRequest, RequestDetails theRequestDetails) {
		theReplaceReferenceRequest.validateOrThrowInvalidParameterException();

		if (theRequestDetails.isPreferAsync()) {
			return replaceReferencesPreferAsync(theReplaceReferenceRequest, theRequestDetails);
		} else {
			return replaceReferencesPreferSync(theReplaceReferenceRequest, theRequestDetails);
		}
	}

	@Override
	public Integer countResourcesReferencingResource(IIdType theResourceId, RequestDetails theRequestDetails) {
		return myHapiTransactionService.withRequest(theRequestDetails).execute(() -> {
			return myResourceLinkDao.countResourcesTargetingFhirTypeAndFhirId(
					theResourceId.getResourceType(), theResourceId.getIdPart());
		});
	}

	private IBaseParameters replaceReferencesPreferAsync(
			ReplaceReferenceRequest theReplaceReferenceRequest, RequestDetails theRequestDetails) {
		Task task = new Task();
		task.setStatus(Task.TaskStatus.INPROGRESS);
		IFhirResourceDao<Task> resourceDao = myDaoRegistry.getResourceDao(Task.class);
		resourceDao.create(task, theRequestDetails);

		ReplaceReferencesJobParameters jobParams = new ReplaceReferencesJobParameters(theReplaceReferenceRequest);
		jobParams.setTaskId(task.getIdElement().toUnqualifiedVersionless());

		JobInstanceStartRequest request = new JobInstanceStartRequest(JOB_REPLACE_REFERENCES, jobParams);
		Batch2JobStartResponse jobStartResponse = myJobCoordinator.startInstance(theRequestDetails, request);

		task.addIdentifier().setSystem(HAPI_BATCH_JOB_ID_SYSTEM).setValue(jobStartResponse.getInstanceId());
		resourceDao.update(task, theRequestDetails);

		Parameters retval = new Parameters();
		task.setIdElement(task.getIdElement().toUnqualifiedVersionless());
		task.getMeta().setVersionId(null);
		retval.addParameter()
				.setName(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK)
				.setResource(task);
		return retval;
	}

	// FIXME KHS replace this with a proper batch job
	private void fakeBackgroundTaskUpdate(
			ReplaceReferenceRequest theReplaceReferenceRequest, Task theTask, RequestPartitionId thePartitionId) {
		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		systemRequestDetails.setRequestPartitionId(thePartitionId);
		myFakeExecutor.submit(() -> {
			try {

				List<IdDt> pidList = myHapiTransactionService
						.withSystemRequestOnPartition(thePartitionId)
						.execute(() -> myResourceLinkDao
								.streamSourceIdsForTargetFhirId(
										theReplaceReferenceRequest.sourceId.getResourceType(),
										theReplaceReferenceRequest.sourceId.getIdPart())
								.collect(Collectors.toUnmodifiableList()));

				List<List<IdDt>> chunks = Lists.partition(pidList, theReplaceReferenceRequest.batchSize);
				List<Bundle> outputBundles = new ArrayList<>();

				chunks.forEach(chunk -> {
					Bundle result = patchReferencingResources(theReplaceReferenceRequest, chunk, systemRequestDetails);
					outputBundles.add(result);
				});

				theTask.setStatus(Task.TaskStatus.COMPLETED);
				outputBundles.forEach(outputBundle -> {
					Task.TaskOutputComponent output = theTask.addOutput();
					Coding coding = output.getType().getCodingFirstRep();
					coding.setSystem(RESOURCE_TYPES_SYSTEM);
					coding.setCode("Bundle");
					Reference outputBundleReference =
							new Reference(outputBundle.getIdElement().toUnqualifiedVersionless());
					output.setValue(outputBundleReference);
					theTask.addContained(outputBundle);
				});

				myDaoRegistry.getResourceDao(Task.class).update(theTask, systemRequestDetails);
				ourLog.info("Updated task {} to COMPLETED.", theTask.getId());
			} catch (Exception e) {
				ourLog.error("Patch failed", e);
				theTask.setStatus(Task.TaskStatus.FAILED);
				myDaoRegistry.getResourceDao(Task.class).update(theTask, systemRequestDetails);
				ourLog.info("Updated task {} to FAILED.", theTask.getId());
			}
		});
	}

	/**
	 * Try to perform the operation synchronously. However if there is more than a page of results, fall back to asynchronous operation
	 */
	@Nonnull
	private IBaseParameters replaceReferencesPreferSync(
			ReplaceReferenceRequest theReplaceReferenceRequest, RequestDetails theRequestDetails) {

		// TODO KHS get partition from request
		StopLimitAccumulator<IdDt> accumulator = myHapiTransactionService
				.withRequest(theRequestDetails)
				.execute(() -> getAllPidsWithLimit(theReplaceReferenceRequest));

		if (accumulator.isTruncated()) {
			ourLog.warn("Too many results. Switching to asynchronous reference replacement.");
			return replaceReferencesPreferAsync(theReplaceReferenceRequest, theRequestDetails);
		}

		Bundle result =
				patchReferencingResources(theReplaceReferenceRequest, accumulator.getItemList(), theRequestDetails);

		Parameters retval = new Parameters();
		retval.addParameter()
				.setName(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME)
				.setResource(result);
		return retval;
	}

	// FIXME KHS delete after convert to batch
	private Bundle patchReferencingResources(
			ReplaceReferenceRequest theReplaceReferenceRequest,
			List<IdDt> theFhirIdList,
			RequestDetails theRequestDetails) {
		Bundle patchBundle = buildPatchBundle(theReplaceReferenceRequest, theRequestDetails, theFhirIdList);
		IFhirSystemDao<Bundle, Meta> systemDao = myDaoRegistry.getSystemDao();
		Bundle result = systemDao.transaction(theRequestDetails, patchBundle);
		// TODO KHS shouldn't transaction response bundles have ids?
		result.setId(UUID.randomUUID().toString());
		return result;
	}

	private @Nonnull StopLimitAccumulator<IdDt> getAllPidsWithLimit(
			ReplaceReferenceRequest theReplaceReferenceRequest) {

		Stream<IdDt> idStream = myResourceLinkDao.streamSourceIdsForTargetFhirId(
				theReplaceReferenceRequest.sourceId.getResourceType(), theReplaceReferenceRequest.sourceId.getIdPart());
		StopLimitAccumulator<IdDt> accumulator =
				StopLimitAccumulator.fromStreamAndLimit(idStream, theReplaceReferenceRequest.batchSize);
		return accumulator;
	}

	// FIXME KHS delete after convert to batch
	private Bundle buildPatchBundle(
			ReplaceReferenceRequest theReplaceReferenceRequest,
			RequestDetails theRequestDetails,
			List<IdDt> theFhirIdList) {
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);

		theFhirIdList.forEach(referencingResourceId -> {
			IFhirResourceDao<?> dao = getDao(referencingResourceId.getResourceType());
			IBaseResource resource = dao.read(referencingResourceId, theRequestDetails);
			Parameters patchParams = buildPatchParams(theReplaceReferenceRequest, resource);
			IIdType resourceId = resource.getIdElement();
			bundleBuilder.addTransactionFhirPatchEntry(resourceId, patchParams);
		});
		return bundleBuilder.getBundleTyped();
	}

	// FIXME KHS delete after convert to batch
	private @Nonnull Parameters buildPatchParams(
			ReplaceReferenceRequest theReplaceReferenceRequest, IBaseResource referencingResource) {
		Parameters params = new Parameters();

		myFhirContext.newTerser().getAllResourceReferences(referencingResource).stream()
				.filter(refInfo -> matches(
						refInfo,
						theReplaceReferenceRequest.sourceId)) // We only care about references to our source resource
				.map(refInfo -> createReplaceReferencePatchOperation(
						referencingResource.fhirType() + "." + refInfo.getName(),
						new Reference(theReplaceReferenceRequest.targetId.getValueAsString())))
				.forEach(params::addParameter); // Add each operation to parameters
		return params;
	}

	// FIXME KHS delete after convert to batch
	private static boolean matches(ResourceReferenceInfo refInfo, IIdType theSourceId) {
		return refInfo.getResourceReference()
				.getReferenceElement()
				.toUnqualifiedVersionless()
				.getValueAsString()
				.equals(theSourceId.getValueAsString());
	}

	// FIXME KHS delete after convert to batch
	@Nonnull
	private Parameters.ParametersParameterComponent createReplaceReferencePatchOperation(
			String thePath, Type theValue) {

		Parameters.ParametersParameterComponent operation = new Parameters.ParametersParameterComponent();
		operation.setName(PARAMETER_OPERATION);
		operation.addPart().setName(PARAMETER_TYPE).setValue(new CodeType(OPERATION_REPLACE));
		operation.addPart().setName(PARAMETER_PATH).setValue(new StringType(thePath));
		operation.addPart().setName(PARAMETER_VALUE).setValue(theValue);
		return operation;
	}

	private IFhirResourceDao<?> getDao(String theResourceName) {
		return myDaoRegistry.getResourceDao(theResourceName);
	}
}
