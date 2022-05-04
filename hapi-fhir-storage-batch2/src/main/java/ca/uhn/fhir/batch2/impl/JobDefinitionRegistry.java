package ca.uhn.fhir.batch2.impl;

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
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

public class JobDefinitionRegistry {

	private final Map<String, TreeMap<Integer, JobDefinition<?>>> myJobs = new HashMap<>();

	public <PT extends IModelJson> void addJobDefinition(JobDefinition<PT> theDefinition) {
		Validate.notNull(theDefinition);
		Validate.notBlank(theDefinition.getJobDefinitionId());
		Validate.isTrue(theDefinition.getJobDefinitionVersion() >= 1);
		Validate.isTrue(theDefinition.getSteps().size() > 1);

		Set<String> stepIds = new HashSet<>();
		for (JobDefinitionStep<?,?,?> next : theDefinition.getSteps()) {
			if (!stepIds.add(next.getStepId())) {
				throw new ConfigurationException(Msg.code(2046) + "Duplicate step[" + next.getStepId() + "] in definition[" + theDefinition.getJobDefinitionId() + "] version: " + theDefinition.getJobDefinitionVersion());
			}
		}

		TreeMap<Integer, JobDefinition<?>> versionMap = myJobs.computeIfAbsent(theDefinition.getJobDefinitionId(), t -> new TreeMap<>());
		if (versionMap.containsKey(theDefinition.getJobDefinitionVersion())) {
			throw new ConfigurationException(Msg.code(2047) + "Multiple definitions for job[" + theDefinition.getJobDefinitionId() + "] version: " + theDefinition.getJobDefinitionVersion());
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
}
