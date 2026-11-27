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
package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.jpa.term.api.ITermValueSetStorageSvc;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Error handler of the ValueSet pre-expansion job. batch2 calls it when the job moves to
 * {@link ca.uhn.fhir.batch2.model.StatusEnum#FAILED} or {@link ca.uhn.fhir.batch2.model.StatusEnum#CANCELLED}.
 * Without it, a job that ends before its last step leaves the ValueSet at
 * {@link ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum#EXPANSION_IN_PROGRESS} with its staging
 * version in the database.
 * <p>
 * batch2 calls the handler inside the transaction that stores the new status of the job. The handler does
 * its work only after that transaction has committed. This keeps a failure here from affecting that
 * transaction, and it lets {@link Step1InitiateJob}, which may be staging while the job is cancelled,
 * rely on the ended status being visible once this handler has looked for the staging version.
 * </p>
 */
// Created by Claude Fable 5.2
public class PreExpandValueSetErrorHandler implements IJobCompletionHandler<PreExpandValueSetParameters> {
	private static final Logger ourLog = LoggerFactory.getLogger(PreExpandValueSetErrorHandler.class);

	private final ITermValueSetStorageSvc myTermValueSetStorageSvc;

	public PreExpandValueSetErrorHandler(@Nonnull ITermValueSetStorageSvc theTermValueSetStorageSvc) {
		myTermValueSetStorageSvc = theTermValueSetStorageSvc;
	}

	@Override
	public void jobComplete(@Nonnull JobCompletionDetails<PreExpandValueSetParameters> theDetails) {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					handleEndedJob(theDetails);
				}
			});
		} else {
			handleEndedJob(theDetails);
		}
	}

	private void handleEndedJob(JobCompletionDetails<PreExpandValueSetParameters> theDetails) {
		PreExpandValueSetParameters parameters = theDetails.getParameters();
		String jobInstanceId = theDetails.getInstance().getInstanceId();
		try {
			myTermValueSetStorageSvc.handlePreExpansionJobIfEnded(
					theDetails.newSystemRequestDetails(), parameters.getUrl(), parameters.getVersion(), jobInstanceId);
		} catch (Exception e) {
			// The status of the job is already stored, so there is nothing to roll back and no caller that
			// could act on the failure. The ValueSet stays as it is until it is expanded again.
			ourLog.atError()
					.setMessage("Failed to handle the end of pre-expansion job {} for ValueSet[url={}, version={}]")
					.addArgument(jobInstanceId)
					.addArgument(parameters.getUrl())
					.addArgument(parameters.getVersion())
					.setCause(e)
					.log();
		}
	}
}
