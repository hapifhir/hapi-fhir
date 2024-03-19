package ca.uhn.hapi.fhir.batch2.test.models;

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class JobMaintenanceStateInformation {

	private static final Logger ourLog = LoggerFactory.getLogger(JobMaintenanceStateInformation.class);

	private static final String COMMENT_PATTERN = "(#.*)$";

	private final List<String> myLineComments = new ArrayList<>();

	private final List<WorkChunk> myInitialWorkChunks = new ArrayList<>();

	private final List<WorkChunk> myFinalWorkChunk = new ArrayList<>();

	private final JobDefinition<?> myJobDefinition;

	private final String myInstanceId;

	public JobMaintenanceStateInformation(String theInstanceId, JobDefinition<?> theJobDefinition) {
		myInstanceId = theInstanceId;
		myJobDefinition = theJobDefinition;
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

	public void initialize(String theState) {
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
		WorkChunk initial = createBaseWorkChunk();
		initial.setStatus(initialStatus);
		initial.setTargetStepId(stepId);
		theAdder.accept(initial);
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
//		chunk.setId(UUID.randomUUID().toString());
		chunk.setJobDefinitionId(myJobDefinition.getJobDefinitionId());
		chunk.setInstanceId(myInstanceId);
		chunk.setJobDefinitionVersion(myJobDefinition.getJobDefinitionVersion());
		chunk.setCreateTime(new Date());
		return chunk;
	}
}
