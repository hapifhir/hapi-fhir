/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.term.api;

import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.r4.model.ValueSet;

public interface ITermValueSetStorageSvc {

	/**
	 * Creates a new staging version of the given ValueSet, intended for
	 * pre-expanding concepts. The existing version will be marked with status
	 * {@link TermValueSetPreExpansionStatusEnum#EXPANSION_IN_PROGRESS}.
	 * The staging version will be assigned a random UUID version which will
	 * be replaced by the intended version when the staging is complete.
	 *
	 * @param theUrl The ValueSet URI (must not be a versioned URI)
	 * @param theVersion The ValueSet version (can be null)
	 * @return Returns a staging version string
	 * @since 8.12.0
	 * @deprecated No pre-expansion job owns a staging version that this method creates, so a pre-expansion
	 * job of the same ValueSet can drop it as abandoned. Use
	 * {@link #startStagingVersion(String, String, String)} with the ID of the batch job that stages.
	 */
	@Deprecated(since = "9.2.0", forRemoval = true)
	@Nonnull
	String startStagingVersion(@Nonnull String theUrl, @Nullable String theVersion);

	/**
	 * Creates a staging version of the given ValueSet as {@link #startStagingVersion(String, String)} does,
	 * but under a staging version ID chosen by the caller. Calling it again with the same ID returns the
	 * existing staging version without creating another, so a retried caller does not leave a second one.
	 * <p>
	 * The default implementation ignores the ID and assigns a random one.
	 * </p>
	 *
	 * @param theUrl The ValueSet URI (must not be a versioned URI)
	 * @param theVersion The ValueSet version (can be null)
	 * @param theStagingVersionId The staging version ID to use, such as the ID of the batch job that stages
	 * @return Returns the staging version ID in use
	 * @since 9.2.0
	 */
	@Nonnull
	default String startStagingVersion(
			@Nonnull String theUrl, @Nullable String theVersion, @Nonnull String theStagingVersionId) {
		return startStagingVersion(theUrl, theVersion);
	}

	/**
	 * Adds concepts to the precalculated expansion of the given ValueSet, using
	 * the <code>ValueSet.url</code> and <code>ValueSet.version</code> to identify
	 * the ValueSet to add to. The concepts in <code>ValueSet.expansion</code>
	 * will be added if they are not already present.
	 *
	 * @param theDelta The ValueSet to add
	 * @param theStartingOrder The index {@link TermValueSetConcept#getOrder()} to use for the first concept added, and incrementing by one for each code.
	 * @return Returns an object containing statistics about what was added
	 */
	@Nonnull
	UploadStatistics addConceptsToExpansion(@Nonnull ValueSet theDelta, int theStartingOrder);

	/**
	 * Adds concepts to the precalculated expansion of the given ValueSet, using
	 * the <code>ValueSet.url</code> and <code>ValueSet.version</code> to identify
	 * the ValueSet to add to. The concepts in <code>ValueSet.expansion</code>
	 * will be removed if they are already present. Any codes specified for removal
	 * that are not actually present in the expansion will be ignored.
	 *
	 * @param theDelta The ValueSet to remove
	 * @return Returns an object containing statistics about what was added
	 */
	@Nonnull
	UploadStatistics removeConceptsFromExpansion(ValueSet theDelta);

	/**
	 * Takes a ValueSet that is being staged (see {@link #startStagingVersion(String, String)})
	 * and activates it so that it becomes the active version.
	 *
	 * @param theValueSetUrl The ValueSet URL
	 * @param theStagingVersionId The staging version ID
	 * @since 8.12.0
	 */
	void activateStagingVersion(String theValueSetUrl, String theStagingVersionId);

	/**
	 * Drops a ValueSet that is being staged (see {@link #startStagingVersion(String, String)}),
	 * deleting all files associated with it.
	 *
	 * @param theValueSetUrl The ValueSet URL
	 * @param theStagingVersionId The staging version ID
	 * @since 8.12.0
	 */
	void dropStagingVersion(String theValueSetUrl, String theStagingVersionId);

	/**
	 * Drops a staging version as {@link #dropStagingVersion(String, String)} does, but treats a staging
	 * version that no longer exists as already dropped.
	 * <p>
	 * The default implementation delegates to {@link #dropStagingVersion(String, String)}.
	 * </p>
	 *
	 * @param theValueSetUrl The ValueSet URL
	 * @param theStagingVersionId The staging version ID
	 * @return Returns {@literal true} if the staging version existed and was dropped
	 * @since 9.2.0
	 */
	default boolean dropStagingVersionIfExists(@Nonnull String theValueSetUrl, @Nonnull String theStagingVersionId) {
		dropStagingVersion(theValueSetUrl, theStagingVersionId);
		return true;
	}

	/**
	 * Deletes any stored ValueSet pre-expansion content related to the given ValueSet resource
	 */
	void deleteValueSetAndChildren(ResourceTable theResourceTable);

	/**
	 * Creates a new ValueSet entity linked to a ValueSet resource
	 */
	void storeTermValueSet(RequestDetails theRequestDetails, ResourceTable theResourceTable, ValueSet theValueSet);

	/**
	 * Invalidates pre-calculated expansions for all ValueSets whose expansions contain
	 * concepts from the given CodeSystem URL, and the expansions of ValueSets that include it directly but hold
	 * none of its concepts.
	 * Should be called whenever a CodeSystem is created or updated so that stale expansions are
	 * not used for code validation.
	 *
	 * @param theCodeSystemUrl the URL of the updated CodeSystem
	 * @return the number of ValueSet expansions that were invalidated
	 * @generated by claude-sonnet-4-6
	 */
	int invalidatePreCalculatedExpansionOfValueSetsContainingCodeSystem(String theCodeSystemUrl);

	/**
	 * Set the expansion status of the given ValueSet to {@link TermValueSetPreExpansionStatusEnum#FAILED_TO_EXPAND}
	 *
	 * @param theUrl The ValueSet URL
	 * @param theVersion The ValueSet version
	 * @param theFailureMessage the failure message
	 * @since 8.12.0
	 */
	void markValueSetAsFailedToExpand(String theUrl, String theVersion, String theFailureMessage);

	/**
	 * Sets the expansion status of the given ValueSet to
	 * {@link TermValueSetPreExpansionStatusEnum#FAILED_TO_EXPAND} on behalf of the pre-expansion that staged
	 * the given staging version. Nothing is written when that staging version no longer exists, or when the
	 * ValueSet is already {@link TermValueSetPreExpansionStatusEnum#EXPANDED} or
	 * {@link TermValueSetPreExpansionStatusEnum#FAILED_TO_EXPAND}, so a pre-expansion that was superseded
	 * does not overwrite the outcome of another one.
	 * <p>
	 * The default implementation delegates to {@link #markValueSetAsFailedToExpand(String, String, String)}.
	 * </p>
	 *
	 * @param theUrl The ValueSet URL
	 * @param theVersion The ValueSet version (can be null)
	 * @param theStagingVersionId The staging version ID of the pre-expansion that failed
	 * @param theFailureMessage the failure message
	 * @return Returns {@literal true} if the status was written
	 * @since 9.2.0
	 */
	default boolean markValueSetAsFailedToExpandIfStaged(
			@Nonnull String theUrl,
			@Nullable String theVersion,
			@Nonnull String theStagingVersionId,
			@Nonnull String theFailureMessage) {
		markValueSetAsFailedToExpand(theUrl, theVersion, theFailureMessage);
		return true;
	}

	/**
	 * Handles a pre-expansion job that has failed or was cancelled before it could report its own outcome.
	 * If the given job has ended that way, the ValueSet is marked as described in
	 * {@link #markValueSetAsFailedToExpandIfStaged(String, String, String, String)} and the staging versions
	 * the job left behind are cleaned up as described in
	 * {@link #startCleanupOfAbandonedStagingVersions(RequestDetails, String, String, String)}. Nothing is
	 * done for a job that is still running or that completed.
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 *
	 * @param theRequestDetails The request details to start the cleanup job with
	 * @param theUrl The ValueSet URL
	 * @param theVersion The ValueSet version (can be null)
	 * @param theJobInstanceId The ID of the pre-expansion job instance, which is also its staging version ID
	 * @return Returns {@literal true} if the job has failed or was cancelled
	 * @since 9.2.0
	 */
	default boolean handlePreExpansionJobIfEnded(
			@Nonnull RequestDetails theRequestDetails,
			@Nonnull String theUrl,
			@Nullable String theVersion,
			@Nonnull String theJobInstanceId) {
		return false;
	}

	/**
	 * Starts a batch job that drops the staging versions of the given ValueSet which no running
	 * pre-expansion job owns. No job is started when there are none. A staging version named after a job
	 * instance is abandoned once that job has ended. A staging version that matches no job instance is
	 * abandoned only when no other pre-expansion job for the ValueSet is running.
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 *
	 * @param theRequestDetails The request details to start the cleanup job with
	 * @param theUrl The ValueSet URL
	 * @param theVersion The ValueSet version (can be null)
	 * @param theCallingJobInstanceId The ID of the pre-expansion job instance making this call, which is not
	 *                                counted as another running job, or {@literal null} if the caller is
	 *                                not a running pre-expansion job
	 * @since 9.2.0
	 */
	default void startCleanupOfAbandonedStagingVersions(
			@Nonnull RequestDetails theRequestDetails,
			@Nonnull String theUrl,
			@Nullable String theVersion,
			@Nullable String theCallingJobInstanceId) {
		// nothing by default
	}
}
