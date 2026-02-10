/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationRequest;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationResponse;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collection;
import java.util.List;

/**
 * This is the base class for any <b>bulk modification</b> or <b>bulk rewrite</b> jobs. Any new bulk modification
 * job type should subclass this class, and implement {@link #modifyResource(BaseBulkModifyJobParameters, Object, ResourceModificationRequest)}.
 * Several other methods are provided which can also be overridden.
 *
 * @param <PT> The job parameters type
 * @param <C>  A context object which will be passed between methods for a single chunk of resources. The format of the
 *             context object is up to the subclass, the framework doesn't look at it.
 * @since 8.8.0
 */
public abstract class BaseBulkModifyResourcesIndividuallyStep<PT extends BaseBulkModifyJobParameters, C>
		extends BaseBulkModifyResourcesStep<PT, C> {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseBulkModifyResourcesIndividuallyStep.class);

	@Override
	protected void processPidsInTransaction(
			StepExecutionDetails<PT, TypedPidAndVersionListWorkChunkJson> theStepExecutionDetails,
			PT theJobParameters,
			State theState,
			List<TypedPidAndVersionJson> thePids,
			TransactionDetails theTransactionDetails,
			IJobDataSink<BulkModifyResourcesChunkOutcomeJson> theDataSink) {
		HapiTransactionService.requireTransaction();

		// Fetch all the resources in the chunk
		fetchResourcesInTransaction(theStepExecutionDetails, theState, thePids);

		// Perform the modification (handled by subclasses)
		C modificationContext;
		try {
			modificationContext = modifyResourcesInTransaction(theState, theJobParameters, thePids);
		} catch (JobExecutionFailedException e) {
			theState.setJobFailure(e);
			throw e;
		}

		// Store the modified resources to the DB
		if (!theJobParameters.isDryRun()) {
			storeResourcesInTransaction(
					theStepExecutionDetails, modificationContext, theState, thePids, theTransactionDetails);
		} else {
			theState.moveUnsavedToSaved();
		}
	}

	/**
	 * Fetches the given resources by PID from the database, and stores the fetched
	 * resources in the {@link State}.
	 */
	private void fetchResourcesInTransaction(
			StepExecutionDetails<PT, TypedPidAndVersionListWorkChunkJson> theStepExecutionDetails,
			State theState,
			List<TypedPidAndVersionJson> thePids) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		Multimap<TypedPidJson, TypedPidAndVersionJson> pidsToVersions =
				MultimapBuilder.hashKeys().arrayListValues().build();
		for (TypedPidAndVersionJson pidAndVersion : thePids) {
			pidsToVersions.put(pidAndVersion.toTypedPid(), pidAndVersion);
		}
		List<TypedPidJson> typedPids = List.copyOf(pidsToVersions.keySet());

		List<? extends IResourcePersistentId<?>> typedPidsAsPersistentIds =
				typedPids.stream().map(t -> t.toPersistentId(myIdHelperService)).toList();

		mySystemDao.preFetchResources(typedPidsAsPersistentIds, true);

		for (int i = 0; i < typedPids.size(); i++) {
			TypedPidJson typedPid = typedPids.get(i);
			IResourcePersistentId<?> persistentId = typedPidsAsPersistentIds.get(i);

			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(typedPid.getResourceType());
			IBaseResource resource = dao.readByPid(persistentId);

			Collection<TypedPidAndVersionJson> typedVersionedPids = pidsToVersions.get(typedPid);
			for (TypedPidAndVersionJson typedVersionedPid : typedVersionedPids) {
				Long nextVersion = typedVersionedPid.getVersionId();
				if (nextVersion == null
						|| nextVersion.equals(resource.getIdElement().getVersionIdPartAsLong())) {

					// Current version
					theState.setResourceForPid(typedVersionedPid, resource);

				} else {

					// If we're fetching specific versions of resources, we fetch them
					// individually here. This could be made more efficient with some kind
					// of bulk versioned fetch in the future.
					IIdType nextVersionedId = resource.getIdElement().withVersion(Long.toString(nextVersion));
					RequestDetails requestDetails = theStepExecutionDetails.newSystemRequestDetails();
					IBaseResource resourceVersion = dao.read(nextVersionedId, requestDetails, true);

					// Don't try to modify resource versions that are already deleted
					if (ResourceMetadataKeyEnum.DELETED_AT.get(resourceVersion) != null) {
						theState.moveToState(typedVersionedPid, StateEnum.UNCHANGED);
					}

					theState.setResourceForPid(typedVersionedPid, resourceVersion);
				}
			}
		}
	}

	private C modifyResourcesInTransaction(State theState, PT theJobParameters, List<TypedPidAndVersionJson> thePids) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		C modificationContext = preModifyResources(theJobParameters, thePids);

		for (TypedPidAndVersionJson pid : thePids) {
			if (!theState.isPidInState(pid, StateEnum.INITIAL)) {
				// If we found that a resource version was deleted, we move it
				// straight to UNCHANGED status
				continue;
			}

			IBaseResource resource = theState.getResourceForPid(pid);
			String resourceType = myFhirContext.getResourceType(resource);
			String resourceId = resource.getIdElement().getIdPart();
			String resourceVersion = resource.getIdElement().getVersionIdPart();

			try (ca.uhn.fhir.util.HashingWriter preModificationHash = hashResource(resource)) {

				ResourceModificationRequest modificationRequest = new ResourceModificationRequest(resource);
				ResourceModificationResponse modificationResponse =
						modifyResource(theJobParameters, modificationContext, modificationRequest);
				if (modificationResponse == null) {
					throw new JobExecutionFailedException(Msg.code(2789)
							+ "Null response from Modification for Resource[" + resource.getIdElement() + "]");
				}

				if (modificationResponse.isDeleted()) {
					theState.moveToState(pid, StateEnum.DELETED_UNSAVED);
					continue;
				}

				IBaseResource updatedResource = modificationResponse.getResource();
				if (updatedResource == null) {
					theState.moveToState(pid, StateEnum.UNCHANGED);
					continue;
				}

				ca.uhn.fhir.util.HashingWriter postModificationHash = hashResource(updatedResource);
				if (preModificationHash.matches(postModificationHash)) {
					theState.moveToState(pid, StateEnum.UNCHANGED);
					continue;
				}

				if (!resourceType.equals(myFhirContext.getResourceType(updatedResource))) {
					throw new JobExecutionFailedException(Msg.code(2782) + "Modification for Resource["
							+ resource.getIdElement() + "] returned wrong resource type, expected " + resourceType
							+ " but was " + myFhirContext.getResourceType(updatedResource));
				}
				if (!resourceId.equals(updatedResource.getIdElement().getIdPart())) {
					throw new JobExecutionFailedException(Msg.code(2783) + "Modification for Resource[" + resourceType
							+ "/" + resourceId + "] attempted to change the resource ID");
				}
				if (!resourceVersion.equals(updatedResource.getIdElement().getVersionIdPart())) {
					throw new JobExecutionFailedException(Msg.code(2784) + "Modification for Resource[" + resourceType
							+ "/" + resourceId + "] attempted to change the resource version");
				}

				theState.moveToState(pid, StateEnum.CHANGED_UNSAVED);
				theState.setResourceForPid(pid, updatedResource);
			}
		}

		return modificationContext;
	}

	@Nonnull
	private ca.uhn.fhir.util.HashingWriter hashResource(IBaseResource resource) {
		try (ca.uhn.fhir.util.HashingWriter preModificationHash = new ca.uhn.fhir.util.HashingWriter()) {
			preModificationHash.append(myFhirContext, resource);
			return preModificationHash;
		}
	}

	private void storeResourcesInTransaction(
			StepExecutionDetails<PT, TypedPidAndVersionListWorkChunkJson> theStepExecutionDetails,
			C theModificationContext,
			State theState,
			List<TypedPidAndVersionJson> thePids,
			TransactionDetails theTransactionDetails) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		for (TypedPidAndVersionJson pid : thePids) {

			// Changed resources
			if (theState.isPidInState(pid, StateEnum.CHANGED_UNSAVED)) {
				IBaseResource resource = theState.getResourceForPid(pid);
				Validate.isTrue(resource != null, "Resource for PID[%s] is null", pid);
				IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(resource);

				SystemRequestDetails requestDetails =
						createRequestDetails(theStepExecutionDetails, theModificationContext, resource);

				DaoMethodOutcome outcome =
						dao.update(resource, null, true, false, requestDetails, theTransactionDetails);
				ourLog.debug(
						"Storage for PID[{}] resulted in version: {}",
						pid,
						outcome.getId().getVersionIdPart());
				theState.moveToState(pid, StateEnum.CHANGED_PENDING);
			}

			// Deleted resources
			if (theState.isPidInState(pid, StateEnum.DELETED_UNSAVED)) {
				IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(pid.getResourceType());
				IBaseResource resource = theState.getResourceForPid(pid);
				Validate.isTrue(resource != null, "Resource for PID[%s] is null", pid);

				SystemRequestDetails requestDetails =
						createRequestDetails(theStepExecutionDetails, theModificationContext, resource);
				if (requestDetails.isRewriteHistory()) {
					throw new JobExecutionFailedException(
							Msg.code(2806) + "Can't store deleted resources as history rewrites");
				}

				IIdType resourceId = resource.getIdElement();
				DaoMethodOutcome outcome = dao.delete(resourceId, requestDetails);

				ourLog.debug(
						"Deletion for PID[{}] resulted in version: {}",
						pid,
						outcome.getId().getVersionIdPart());
				theState.moveToState(pid, StateEnum.DELETED_PENDING);
			}
		}
	}

	@Nonnull
	private SystemRequestDetails createRequestDetails(
			StepExecutionDetails<PT, TypedPidAndVersionListWorkChunkJson> theStepExecutionDetails,
			C theModificationContext,
			IBaseResource theResource) {
		SystemRequestDetails requestDetails = theStepExecutionDetails.newSystemRequestDetails();
		requestDetails.setRewriteHistory(isRewriteHistory(theModificationContext, theResource));
		return requestDetails;
	}

	/**
	 * Subclasses may override this method to indicate that this resource should be stored
	 * as a history rewrite
	 */
	@SuppressWarnings("unused")
	protected boolean isRewriteHistory(C theState, IBaseResource theResource) {
		return false;
	}

	/**
	 * Subclasses may override this method, which will be called immediately before beginning processing
	 * of a resource batch. It can be used to perform any shared processing that would otherwise need
	 * to be repeated, such as looking up context resources, parsing a patch object in the job parameters
	 * or other expensive operations.
	 *
	 * @param theJobParameters The parameters for the current instance of the job
	 * @param thePids          The PIDs of the resources to be modified. The PIDs will be in the same order as the resources in the chunk.
	 * @return A context object which will be passed to {@link #modifyResource(PT, Object, ResourceModificationRequest)}
	 * 	during each invocation. The format of the context object is up to the subclass, the framework
	 * 	won't look at it and doesn't care if it is {@literal null}.
	 */
	@Nullable
	protected C preModifyResources(PT theJobParameters, List<TypedPidAndVersionJson> thePids) {
		return null;
	}

	/**
	 * This method is called once for each resource needing modification.
	 * Subclasses should implement this method to perform the actual modification of resources. Note
	 * the following restrictions:
	 * <ul>
	 *     <li>The modified resource must be of the same resource type as the original</li>
	 *     <li>The version must not be modified</li>
	 * </ul>
	 */
	protected abstract ResourceModificationResponse modifyResource(
			PT theJobParameters, C theModificationContext, @Nonnull ResourceModificationRequest theModificationRequest);
}
