package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.context.ConfigurationException;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

public class JobDefinitionRegistry {

	private final Map<String, TreeMap<Integer, JobDefinition>> myJobs = new HashMap();

	public void addJobDefinition(JobDefinition theDefinition) {
		Validate.notNull(theDefinition);
		Validate.notBlank(theDefinition.getJobDefinitionId());
		Validate.isTrue(theDefinition.getJobDefinitionVersion() >= 1);
		Validate.isTrue(theDefinition.getSteps().size() > 1);

		Set<String> stepIds = new HashSet<>();
		for (JobDefinitionStep next : theDefinition.getSteps()) {
			if (!stepIds.add(next.getStepId())) {
				throw new ConfigurationException("Duplicate step[" + next.getStepId() + "] in definition[" + theDefinition.getJobDefinitionId() + "] version: " + theDefinition.getJobDefinitionVersion());
			}
		}

		TreeMap<Integer, JobDefinition> versionMap = myJobs.computeIfAbsent(theDefinition.getJobDefinitionId(), t -> new TreeMap<>());
		if (versionMap.containsKey(theDefinition.getJobDefinitionVersion())) {
			throw new ConfigurationException("Multiple definitions for job[" + theDefinition.getJobDefinitionId() + "] version: " + theDefinition.getJobDefinitionVersion());
		}
		versionMap.put(theDefinition.getJobDefinitionVersion(), theDefinition);
	}

	public Optional<JobDefinition> getLatestJobDefinition(@Nonnull String theJobDefinitionId) {
		TreeMap<Integer, JobDefinition> versionMap = myJobs.get(theJobDefinitionId);
		if (versionMap == null || versionMap.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(versionMap.lastEntry().getValue());
	}

	public Optional<JobDefinition> getJobDefinition(@Nonnull String theJobDefinitionId, int theJobDefinitionVersion) {
		TreeMap<Integer, JobDefinition> versionMap = myJobs.get(theJobDefinitionId);
		if (versionMap == null || versionMap.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(versionMap.get(theJobDefinitionVersion));
	}
}
