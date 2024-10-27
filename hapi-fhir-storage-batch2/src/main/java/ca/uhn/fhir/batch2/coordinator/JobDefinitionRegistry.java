/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.Logs;
import com.google.common.collect.ImmutableSortedMap;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JobDefinitionRegistry {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	private final Map<String, NavigableMap<Integer, JobDefinition<?>>> myJobDefinitions = new ConcurrentHashMap<>();

	/**
	 * Add a job definition only if it is not registered
	 *
	 * @param <PT> the job parameter type for the definition
	 * @return true if it did not already exist and was registered
	 */
	public synchronized <PT extends IModelJson> boolean addJobDefinitionIfNotRegistered(
			@Nonnull JobDefinition<PT> theDefinition) {
		Optional<JobDefinition<?>> orig =
				getJobDefinition(theDefinition.getJobDefinitionId(), theDefinition.getJobDefinitionVersion());
		if (orig.isPresent()) {
			return false;
		}
		addJobDefinition(theDefinition);
		return true;
	}

	public synchronized <PT extends IModelJson> void addJobDefinition(@Nonnull JobDefinition<PT> theDefinition) {
		Validate.notNull(theDefinition);
		String jobDefinitionId = theDefinition.getJobDefinitionId();
		Validate.notBlank(jobDefinitionId);
		Validate.isTrue(theDefinition.getJobDefinitionVersion() >= 1);
		Validate.isTrue(theDefinition.getSteps().size() > 1);

		Set<String> stepIds = new HashSet<>();
		for (JobDefinitionStep<?, ?, ?> next : theDefinition.getSteps()) {
			if (!stepIds.add(next.getStepId())) {
				throw new ConfigurationException(
						Msg.code(2046) + "Duplicate step[" + next.getStepId() + "] in definition[" + jobDefinitionId
								+ "] version: " + theDefinition.getJobDefinitionVersion());
			}
		}

		NavigableMap<Integer, JobDefinition<?>> versionMap =
				myJobDefinitions.computeIfAbsent(jobDefinitionId, t -> new TreeMap<>());
		if (versionMap.containsKey(theDefinition.getJobDefinitionVersion())) {
			if (versionMap.get(theDefinition.getJobDefinitionVersion()) == theDefinition) {
				ourLog.warn(
						"job[{}] version: {} already registered.  Not registering again.",
						jobDefinitionId,
						theDefinition.getJobDefinitionVersion());
				return;
			}
			throw new ConfigurationException(Msg.code(2047) + "Multiple definitions for job[" + jobDefinitionId
					+ "] version: " + theDefinition.getJobDefinitionVersion());
		}
		versionMap.put(theDefinition.getJobDefinitionVersion(), theDefinition);
	}

	public synchronized void removeJobDefinition(@Nonnull String theDefinitionId, int theVersion) {
		Validate.notBlank(theDefinitionId);
		Validate.isTrue(theVersion >= 1);

		NavigableMap<Integer, JobDefinition<?>> versionMap = myJobDefinitions.get(theDefinitionId);
		if (versionMap != null) {
			versionMap.remove(theVersion);
			if (versionMap.isEmpty()) {
				myJobDefinitions.remove(theDefinitionId);
			}
		}
	}

	public Optional<JobDefinition<?>> getLatestJobDefinition(@Nonnull String theJobDefinitionId) {
		NavigableMap<Integer, JobDefinition<?>> versionMap = myJobDefinitions.get(theJobDefinitionId);
		if (versionMap == null || versionMap.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(versionMap.lastEntry().getValue());
	}

	public Optional<JobDefinition<?>> getJobDefinition(
			@Nonnull String theJobDefinitionId, int theJobDefinitionVersion) {
		NavigableMap<Integer, JobDefinition<?>> versionMap = myJobDefinitions.get(theJobDefinitionId);
		if (versionMap == null || versionMap.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(versionMap.get(theJobDefinitionVersion));
	}

	/**
	 * @throws JobExecutionFailedException if the job definition can not be found
	 */
	public JobDefinition<?> getJobDefinitionOrThrowException(String theJobDefinitionId, int theJobDefinitionVersion) {
		Optional<JobDefinition<?>> opt = getJobDefinition(theJobDefinitionId, theJobDefinitionVersion);
		if (opt.isEmpty()) {
			String msg =
					"Unknown job definition ID[" + theJobDefinitionId + "] version[" + theJobDefinitionVersion + "]";
			ourLog.warn(msg);
			throw new JobExecutionFailedException(Msg.code(2043) + msg);
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
		return myJobDefinitions.keySet().stream().sorted().collect(Collectors.toList());
	}

	public boolean isEmpty() {
		return myJobDefinitions.isEmpty();
	}

	@SuppressWarnings("unchecked")
	public <T extends IModelJson> JobDefinition<T> getJobDefinitionOrThrowException(JobInstance theJobInstance) {
		return (JobDefinition<T>) getJobDefinitionOrThrowException(
				theJobInstance.getJobDefinitionId(), theJobInstance.getJobDefinitionVersion());
	}

	public Collection<Integer> getJobDefinitionVersions(String theDefinitionId) {
		return myJobDefinitions
				.getOrDefault(theDefinitionId, ImmutableSortedMap.of())
				.keySet();
	}
}
