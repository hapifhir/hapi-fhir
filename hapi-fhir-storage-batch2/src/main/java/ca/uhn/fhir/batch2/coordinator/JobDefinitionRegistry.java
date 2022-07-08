package ca.uhn.fhir.batch2.coordinator;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class JobDefinitionRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(JobDefinitionRegistry.class);

	private final Map<String, TreeMap<Integer, JobDefinition<?>>> myJobs = new HashMap<>();

	/**
	 * Add a job definition only if it is not registered
	 * @param theDefinition
	 * @return true if it did not already exist and was registered
	 * @param <PT> the job parameter type for the definition
	 */
	public <PT extends IModelJson> boolean addJobDefinitionIfNotRegistered(@Nonnull JobDefinition<PT> theDefinition) {
		Optional<JobDefinition<?>> orig = getJobDefinition(theDefinition.getJobDefinitionId(), theDefinition.getJobDefinitionVersion());
		if (orig.isPresent()) {
			return false;
		}
		addJobDefinition(theDefinition);
		return true;
	}

	public <PT extends IModelJson> void addJobDefinition(@Nonnull JobDefinition<PT> theDefinition) {
		Validate.notNull(theDefinition);
		String jobDefinitionId = theDefinition.getJobDefinitionId();
		Validate.notBlank(jobDefinitionId);
		Validate.isTrue(theDefinition.getJobDefinitionVersion() >= 1);
		Validate.isTrue(theDefinition.getSteps().size() > 1);

		Set<String> stepIds = new HashSet<>();
		for (JobDefinitionStep<?, ?, ?> next : theDefinition.getSteps()) {
			if (!stepIds.add(next.getStepId())) {
				throw new ConfigurationException(Msg.code(2046) + "Duplicate step[" + next.getStepId() + "] in definition[" + jobDefinitionId + "] version: " + theDefinition.getJobDefinitionVersion());
			}
		}

		TreeMap<Integer, JobDefinition<?>> versionMap = myJobs.computeIfAbsent(jobDefinitionId, t -> new TreeMap<>());
		if (versionMap.containsKey(theDefinition.getJobDefinitionVersion())) {
			if (versionMap.get(theDefinition.getJobDefinitionVersion()) == theDefinition) {
				ourLog.warn("job[{}] version: {} already registered.  Not registering again.", jobDefinitionId, theDefinition.getJobDefinitionVersion());
				return;
			}
			throw new ConfigurationException(Msg.code(2047) + "Multiple definitions for job[" + jobDefinitionId + "] version: " + theDefinition.getJobDefinitionVersion());
		}
		versionMap.put(theDefinition.getJobDefinitionVersion(), theDefinition);
	}

	public Optional<JobDefinition<?>> getLatestJobDefinition(@Nonnull String theJobDefinitionId) {
		TreeMap<Integer, JobDefinition<?>> versionMap = myJobs.get(theJobDefinitionId);
		if (versionMap == null || versionMap.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(versionMap.lastEntry().getValue());
	}

	public Optional<JobDefinition<?>> getJobDefinition(@Nonnull String theJobDefinitionId, int theJobDefinitionVersion) {
		TreeMap<Integer, JobDefinition<?>> versionMap = myJobs.get(theJobDefinitionId);
		if (versionMap == null || versionMap.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(versionMap.get(theJobDefinitionVersion));
	}

	public JobDefinition<?> getJobDefinitionOrThrowException(String theJobDefinitionId, int theJobDefinitionVersion) {
		Optional<JobDefinition<?>> opt = getJobDefinition(theJobDefinitionId, theJobDefinitionVersion);
		if (opt.isEmpty()) {
			String msg = "Unknown job definition ID[" + theJobDefinitionId + "] version[" + theJobDefinitionVersion + "]";
			ourLog.warn(msg);
			throw new InternalErrorException(Msg.code(2043) + msg);
		}
		return opt.get();
	}

	public void setJobDefinition(JobInstance theInstance) {
		JobDefinition<?> jobDefinition = getJobDefinitionOrThrowException(theInstance);
		theInstance.setJobDefinition(jobDefinition);
	}

	/**
	 * @return a list of Job Definition Ids in alphabetical order
	 */
	public List<String> getJobDefinitionIds() {
		return myJobs.keySet()
			.stream()
			.sorted()
			.collect(Collectors.toList());
	}

	public boolean isEmpty() {
		return myJobs.isEmpty();
	}

	public Optional<JobDefinition<?>> getJobDefinition(JobInstance theJobInstance) {
		return getJobDefinition(theJobInstance.getJobDefinitionId(), theJobInstance.getJobDefinitionVersion());
	}

	public JobDefinition<?> getJobDefinitionOrThrowException(JobInstance theJobInstance) {
		return getJobDefinitionOrThrowException(theJobInstance.getJobDefinitionId(), theJobInstance.getJobDefinitionVersion());
	}
}
