/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 specification tests
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
package ca.uhn.hapi.fhir.batch2.test.support;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This class is used to help set up and verify WorkChunk transitions.
 *
 * Creating this object requires an instanceid (of a stored instance), and jobdefinition (of stored job defintion)
 * and a state string.
 *
 * State strings are defined as follows:
 * "step-name-or-step-index,INITIAL_STATE|step-name-or-step-index,FINAL_STATE"
 *
 * where "step-name-or-step-index" is the name or index of a step in the provided
 * JobDefinition; the step that the work chunk should start in.
 *
 * If no final state/step name is provided, no transition is assumed.
 *
 * Further, comments can be added to the state string, but must be started by a "#".
 *
 * Eg:
 * 1,READY|1,QUEUED  # will create an initial work chunk in the READY state in step 1.
 * 					 #	validation will verify that this workchunk has been transitioned to QUEUED.
 */
public class JobMaintenanceStateInformation {

	private static final Logger ourLog = LoggerFactory.getLogger(JobMaintenanceStateInformation.class);

	private static final String COMMENT_PATTERN = "(#.*)$";

	private final List<String> myLineComments = new ArrayList<>();

	private final List<WorkChunk> myInitialWorkChunks = new ArrayList<>();

	private final List<WorkChunk> myFinalWorkChunk = new ArrayList<>();

	private final JobDefinition<?> myJobDefinition;

	private final String myInstanceId;

	private Consumer<WorkChunk> myWorkChunkModifier = (chunk) -> {};

	public JobMaintenanceStateInformation(
		String theInstanceId,
		JobDefinition<?> theJobDefinition,
		String theStateUnderTest) {
		myInstanceId = theInstanceId;
		myJobDefinition = theJobDefinition;

		setState(theStateUnderTest);
	}

	public void addWorkChunkModifier(Consumer<WorkChunk> theModifier) {
		myWorkChunkModifier = theModifier;
	}

	public List<String> getLineComments() {
		return myLineComments;
	}

	public List<WorkChunk> getInitialWorkChunks() {
		return myInitialWorkChunks;
	}

	public List<WorkChunk> getFinalWorkChunk() {
		return myFinalWorkChunk;
	}

	public JobDefinition<?> getJobDefinition() {
		return myJobDefinition;
	}

	public String getInstanceId() {
		return myInstanceId;
	}

	public void verifyFinalStates(IJobPersistence theJobPersistence) {
		verifyFinalStates(theJobPersistence, (chunk) -> {});
	}

	public void verifyFinalStates(IJobPersistence theJobPersistence, Consumer<WorkChunk> theChunkConsumer) {
		assertEquals(getInitialWorkChunks().size(), getFinalWorkChunk().size());

		HashMap<String, WorkChunk> workchunkMap = new HashMap<>();
		for (WorkChunk fs : getFinalWorkChunk()) {
			workchunkMap.put(fs.getId(), fs);
		}

		// fetch all workchunks
		Iterator<WorkChunk> workChunkIterator = theJobPersistence.fetchAllWorkChunksIterator(getInstanceId(), true);
		List<WorkChunk> workchunks = new ArrayList<>();
		workChunkIterator.forEachRemaining(workchunks::add);

		assertEquals(workchunks.size(), workchunkMap.size());
		workchunks.forEach(c -> ourLog.info("Returned " + c.toString()));

		for (WorkChunk wc : workchunks) {
			WorkChunk expected = workchunkMap.get(wc.getId());
			assertNotNull(expected);

			// verify status and step id
			assertEquals(expected.getTargetStepId(), wc.getTargetStepId());
			assertEquals(expected.getStatus(), wc.getStatus());
			theChunkConsumer.accept(wc);
		}
	}

	public void initialize(IJobPersistence theJobPersistence) {
		// should have as many input workchunks as output workchunks
		// unless we have newly created ones somewhere
		assertEquals(getInitialWorkChunks().size(), getFinalWorkChunk().size());

		Set<String> stepIds = new HashSet<>();
		for (int i = 0; i < getInitialWorkChunks().size(); i++) {
			WorkChunk workChunk = getInitialWorkChunks().get(i);
			myWorkChunkModifier.accept(workChunk);
			WorkChunk saved = theJobPersistence.createWorkChunk(workChunk);
			ourLog.info("Created WorkChunk: " + saved.toString());
			workChunk.setId(saved.getId());

			getFinalWorkChunk().get(i)
				.setId(saved.getId());

			stepIds.add(workChunk.getTargetStepId());
		}
		// if it's a gated job, we'll manually set the step id for the instance
		JobDefinition<?> jobDef = getJobDefinition();
		if (jobDef.isGatedExecution()) {
			AtomicReference<String> latestStepId = new AtomicReference<>();
			int totalSteps = jobDef.getSteps().size();
			// ignore the last step since tests in gated jobs needs the current step to be the second-last step
			for (int i = totalSteps - 2; i >= 0; i--) {
				JobDefinitionStep<?, ?, ?> step = jobDef.getSteps().get(i);
				if (stepIds.contains(step.getStepId())) {
					latestStepId.set(step.getStepId());
					break;
				}
			}
			// should def have a value
			assertNotNull(latestStepId.get());
			String instanceId = getInstanceId();
			theJobPersistence.updateInstance(instanceId, instance -> {
				instance.setCurrentGatedStepId(latestStepId.get());
				return true;
			});
		}
	}

	private void setState(String theState) {
		String[] chunkLines = theState.split("\n");
		Pattern pattern = Pattern.compile(COMMENT_PATTERN);
		for (String chunkLine : chunkLines) {
			String line = chunkLine.trim();
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				String comment = matcher.group(0);
				line = line.replaceAll(comment, "");
				if (isEmpty(line)) {
					myLineComments.add(line);
					continue;
				}
				// else - inline comment: eg: 1|Complete # comment
			}

			addWorkChunkStates(line);
		}
	}

	/**
	 * Parses the line according to:
	 * (work chunk step id)|(workchunk initial state)|optionally:(work chunk final state)
	 */
	private void addWorkChunkStates(String theLine) {
		if (theLine.contains(",")) {
			// has final state
			String[] states = theLine.split(",");

			int len = states.length;
			if (len != 2) {
				throw new RuntimeException("Unexpected number of state transitions. Expected 2, found " + states.length);
			}

			addWorkChunkBasedOnState(states[0], chunk -> {
				myInitialWorkChunks.add(chunk);
			});
			addWorkChunkBasedOnState(states[1], chunk -> {
				myFinalWorkChunk.add(chunk);
			});
		} else {
			// does not have final state; no change
			addWorkChunkBasedOnState(theLine, chunk -> {
				myInitialWorkChunks.add(chunk);
				myFinalWorkChunk.add(chunk);
			});
		}
	}

	private void addWorkChunkBasedOnState(String theLine, Consumer<WorkChunk> theAdder) {
		String[] parts = theLine.split("\\|");
		int len = parts.length;
		if (len < 2) {
			throw new RuntimeException("Unable to parse line " + theLine + " into initial and final states");
		}

		String stepId = getJobStepId(parts[0]);

		WorkChunkStatusEnum initialStatus = WorkChunkStatusEnum.valueOf(parts[1].trim());
		WorkChunk chunk = createBaseWorkChunk();
		chunk.setStatus(initialStatus);
		chunk.setTargetStepId(stepId);
		theAdder.accept(chunk);
	}

	private String getJobStepId(String theIndexId) {
		try {
			// -1 because code is 0 indexed, but people think in 1 indexed
			int index = Integer.parseInt(theIndexId.trim()) - 1;

			if (index >= myJobDefinition.getSteps().size()) {
				throw new RuntimeException("Unable to find step with index " + index);
			}

			int counter = 0;
			for (JobDefinitionStep<?, ?, ?> step : myJobDefinition.getSteps()) {
				if (counter == index) {
					return step.getStepId();
				}
				counter++;
			}

			// will never happen
			throw new RuntimeException("Could not find step for index " + theIndexId);
		} catch (NumberFormatException ex) {
			ourLog.info("Encountered non-number {}; This will be treated as the step id itself", theIndexId);
			return theIndexId;
		}
	}

	private WorkChunk createBaseWorkChunk() {
		WorkChunk chunk = new WorkChunk();
		chunk.setJobDefinitionId(myJobDefinition.getJobDefinitionId());
		chunk.setInstanceId(myInstanceId);
		chunk.setJobDefinitionVersion(myJobDefinition.getJobDefinitionVersion());
		chunk.setCreateTime(new Date());
		return chunk;
	}
}
