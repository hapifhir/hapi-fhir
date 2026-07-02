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

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.term.ExpansionFilter;
import ca.uhn.fhir.jpa.term.IValueSetConceptAccumulator;
import ca.uhn.fhir.jpa.term.SystemAndCode;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx.STEP_ID_WRITE_CONCEPTS_EXCLUDE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx.STEP_ID_WRITE_CONCEPTS_INCLUDE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class Step2And3ExpandConceptsStep<OT extends IModelJson>
		implements IJobStepWorker<PreExpandValueSetParameters, ExpandConceptsWorkChunkJson, OT> {
	private static final Logger ourLog = LoggerFactory.getLogger(Step2And3ExpandConceptsStep.class);

	private static final int CHUNK_SIZE = 100;
	private final boolean myInclude;

	@Autowired
	private IHapiTransactionService myTxService;

	@Autowired
	private ITermReadSvc myTermReadSvc;

	public Step2And3ExpandConceptsStep(boolean theInclude) {
		myInclude = theInclude;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<PreExpandValueSetParameters, ExpandConceptsWorkChunkJson>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<OT> theDataSink)
			throws JobExecutionFailedException {

		PreExpandValueSetParameters parameters = theStepExecutionDetails.getParameters();
		UrlUtil.CanonicalUrlParts canonicalUrl = parameters.getCanonicalUrl();

		ExpandConceptsWorkChunkJson data = theStepExecutionDetails.getData();
		String stagingUrl = data.getStagingUrl();
		String stagingVersion = data.getStagingVersion();

		ValueSetExpansionOptions expansionOptions = new ValueSetExpansionOptions();
		expansionOptions.setIncludeHierarchy(true);

		ourLog.atInfo()
				.setMessage("Expanding concepts for ValueSet[url={}, version={}].compose: {}")
				.addArgument(canonicalUrl.url())
				.addArgument(canonicalUrl.versionId())
				.addArgument(data.getComposeAsJson())
				.log();

		ExpansionFilter expansionFilter = ExpansionFilter.NO_FILTER;
		ChunkingAccumulator accumulator = new ChunkingAccumulator(
				theDataSink, stagingUrl, stagingVersion, data.getCompose(), data.getStartingOrder());

		myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			try {
				myTermReadSvc.expandValueSetHandleIncludeOrExclude(
						expansionOptions, accumulator, data.getCompose(), myInclude, expansionFilter);
				accumulator.close();
			} catch (BaseServerResponseException e) {
				ourLog.warn("Failed to handle ValueSet expansion compose: {}", e.toString());

				/*
				 * If any of our ValueSet.compose sections can't be expanded, we'll send a
				 * notification to the final step so that we can report the failure and
				 * clean up properly. This shouldn't ever happen as long as the ValueSet
				 * is valid, but it can happen and is probably not a HAPI bug - It can
				 * happen if the ValueSet includes unknown code systems, invalid
				 * filters, etc.
				 */
				String rootMessage = ExceptionUtils.getRootCauseMessage(e);
				ExpandValueSetStepOutcomeJson expandValueSetStepOutcomeJson = new ExpandValueSetStepOutcomeJson();
				expandValueSetStepOutcomeJson.setStagingVersion(stagingVersion);
				expandValueSetStepOutcomeJson.setFailureMessage(rootMessage);
				theDataSink.acceptForFutureStep(
						PreExpandValueSetJobAppCtx.STEP_ID_GENERATE_REPORT, expandValueSetStepOutcomeJson);
			}
		});

		ourLog.atInfo()
				.setMessage("Expanded {} concepts")
				.addArgument(accumulator.getConceptCount())
				.log();

		return RunOutcome.SUCCESS;
	}

	/**
	 * Accepts concepts from a ValueSet expansion and accumulates them into chunks that
	 * are forwarded on as work chunks to
	 * {@link Step4And5WriteConceptsStep}
	 * for include/exclude from the ValueSet staging version.
	 */
	private class ChunkingAccumulator implements IValueSetConceptAccumulator {
		private final IJobDataSink<OT> myDataSink;
		private final ValueSet myBuffer = new ValueSet();
		private final Set<SystemAndCode> myConcepts = new HashSet<>();
		private final int myStartingOrder;
		private final String myStagingVersion;
		private int myConceptCount = 0;

		public ChunkingAccumulator(
				IJobDataSink<OT> theDataSink,
				String theStagingUrl,
				String theStagingVersion,
				ValueSet.ConceptSetComponent theCompose,
				Integer theStartingOrder) {
			myDataSink = theDataSink;
			myStartingOrder = theStartingOrder;
			myStagingVersion = theStagingVersion;

			if (myInclude) {
				myBuffer.getCompose().addInclude(theCompose);
			} else {
				myBuffer.getCompose().addExclude(theCompose);
			}

			myBuffer.setUrl(theStagingUrl);
			myBuffer.setVersion(theStagingVersion);
		}

		@Override
		public void addMessage(String theMessage) {
			// ignore for now
		}

		@Override
		public void includeConceptWithDesignations(
				String theSystem,
				String theCode,
				String theDisplay,
				@Nullable Collection<TermConceptDesignation> theDesignations,
				Long theSourceConceptPid,
				String theSourceConceptDirectParentPids,
				@Nullable String theSystemVersion) {
			if (myInclude) {
				addConcept(
						theSystem,
						theCode,
						theDisplay,
						theDesignations,
						theSystemVersion,
						theSourceConceptPid,
						theSourceConceptDirectParentPids);
			}
		}

		@Override
		public void excludeConcept(String theSystem, String theCode) {
			if (!myInclude) {
				addConcept(theSystem, theCode, null, null, null, null, null);
			}
		}

		private void addConcept(
				String theSystem,
				String theCode,
				String theDisplay,
				@Nullable Collection<TermConceptDesignation> theDesignations,
				@Nullable String theSystemVersion,
				Long theSourceConceptPid,
				String theSourceConceptDirectParentPids) {
			if (myConcepts.add(new SystemAndCode(theSystem, theCode))) {
				ValueSet.ValueSetExpansionContainsComponent targetConcept =
						myBuffer.getExpansion().addContains();
				targetConcept.setSystem(theSystem);
				targetConcept.setCode(theCode);
				targetConcept.setDisplay(theDisplay);
				targetConcept.setVersion(theSystemVersion);

				if (isNotBlank(theSourceConceptDirectParentPids)) {
					targetConcept.addExtension(
							TerminologyConstants.EXTENSION_SOURCE_CONCEPT_DIRECT_PARENT_PIDS,
							new StringType(theSourceConceptDirectParentPids));
				}
				if (theSourceConceptPid != null) {
					targetConcept.addExtension(
							TerminologyConstants.EXTENSION_SOURCE_CONCEPT_PID, new DecimalType(theSourceConceptPid));
				}

				if (theDesignations != null) {
					for (TermConceptDesignation sourceDesignation : theDesignations) {
						ValueSet.ConceptReferenceDesignationComponent targetDesignation =
								targetConcept.addDesignation();
						targetDesignation.setLanguage(sourceDesignation.getLanguage());
						targetDesignation.setValue(sourceDesignation.getValue());
						targetDesignation.getUse().setSystem(sourceDesignation.getUseSystem());
						targetDesignation.getUse().setCode(sourceDesignation.getUseCode());
						targetDesignation.getUse().setDisplay(sourceDesignation.getUseDisplay());
					}
				}

				if (myConcepts.size() >= CHUNK_SIZE) {
					flush();
				}
			}
		}

		public void close() {
			flush();
		}

		public int getConceptCount() {
			return myConceptCount;
		}

		private void flush() {
			String targetStep = myInclude ? STEP_ID_WRITE_CONCEPTS_INCLUDE : STEP_ID_WRITE_CONCEPTS_EXCLUDE;

			WriteConceptsWorkChunkJson workChunk = new WriteConceptsWorkChunkJson();
			workChunk.setValueSet(myBuffer);
			workChunk.setStagingVersion(myStagingVersion);
			workChunk.setStartingOrder(myStartingOrder);
			workChunk.setStartingOrderOffset(myConceptCount);

			myDataSink.acceptForFutureStep(targetStep, workChunk);

			myConceptCount += myConcepts.size();
			myBuffer.getExpansion().getContains().clear();
			myConcepts.clear();
		}
	}
}
