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

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.model.api.annotation.PasswordField;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Job Query services intended for end-users querying the status of jobs
 */
class JobQuerySvc {
	private final IJobPersistence myJobPersistence;
	private final JobDefinitionRegistry myJobDefinitionRegistry;

	JobQuerySvc(@Nonnull IJobPersistence theJobPersistence, @Nonnull JobDefinitionRegistry theJobDefinitionRegistry) {
		myJobPersistence = theJobPersistence;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
	}

	@Nonnull
	JobInstance fetchInstance(String theInstanceId) {
		return myJobPersistence
				.fetchInstance(theInstanceId)
				.map(this::massageInstanceForUserAccess)
				.orElseThrow(() -> new ResourceNotFoundException(Msg.code(2040) + "Unknown instance ID: "
						+ UrlUtil.escapeUrlParam(theInstanceId) + ". Please check if the input job ID is valid."));
	}

	List<JobInstance> fetchInstances(int thePageSize, int thePageIndex) {
		return myJobPersistence.fetchInstances(thePageSize, thePageIndex).stream()
				.map(this::massageInstanceForUserAccess)
				.collect(Collectors.toList());
	}

	public Page<JobInstance> fetchAllInstances(JobInstanceFetchRequest theFetchRequest) {
		return myJobPersistence.fetchJobInstances(theFetchRequest);
	}

	public List<JobInstance> fetchRecentInstances(int theCount, int theStart) {
		return massageInstancesForUserAccess(myJobPersistence.fetchRecentInstances(theCount, theStart));
	}

	private List<JobInstance> massageInstancesForUserAccess(List<JobInstance> theFetchRecentInstances) {
		return theFetchRecentInstances.stream()
				.map(this::massageInstanceForUserAccess)
				.collect(Collectors.toList());
	}

	private JobInstance massageInstanceForUserAccess(JobInstance theInstance) {
		JobInstance retVal = new JobInstance(theInstance);

		JobDefinition<?> definition = myJobDefinitionRegistry.getJobDefinitionOrThrowException(theInstance);

		// Serializing the parameters strips any write-only params
		IModelJson parameters = retVal.getParameters(definition.getParametersType());
		stripPasswordFields(parameters);
		String parametersString = JsonUtil.serializeOrInvalidRequest(parameters);
		retVal.setParameters(parametersString);

		return retVal;
	}

	/**
	 * Scans a model object for fields marked as {@link PasswordField}
	 * and nulls them
	 */
	private static void stripPasswordFields(@Nonnull Object theParameters) {
		Field[] declaredFields = theParameters.getClass().getDeclaredFields();
		for (Field nextField : declaredFields) {

			JsonProperty propertyAnnotation = nextField.getAnnotation(JsonProperty.class);
			if (propertyAnnotation == null) {
				continue;
			}

			nextField.setAccessible(true);
			try {
				Object nextValue = nextField.get(theParameters);
				if (nextField.getAnnotation(PasswordField.class) != null) {
					nextField.set(theParameters, null);
				} else if (nextValue != null) {
					stripPasswordFields(nextValue);
				}
			} catch (IllegalAccessException e) {
				throw new InternalErrorException(Msg.code(2044) + e.getMessage(), e);
			}
		}
	}

	public List<JobInstance> getInstancesByJobDefinitionIdAndEndedStatus(
			String theJobDefinitionId, @Nullable Boolean theEnded, int theCount, int theStart) {
		if (theEnded == null) {
			return myJobPersistence.fetchInstancesByJobDefinitionId(theJobDefinitionId, theCount, theStart);
		}

		Set<StatusEnum> requestedStatus;
		if (theEnded) {
			requestedStatus = StatusEnum.getEndedStatuses();
		} else {
			requestedStatus = StatusEnum.getNotEndedStatuses();
		}
		return getInstancesByJobDefinitionAndStatuses(theJobDefinitionId, requestedStatus, theCount, theStart);
	}

	public List<JobInstance> getInstancesByJobDefinitionAndStatuses(
			String theJobDefinitionId, Set<StatusEnum> theStatuses, int theCount, int theStart) {
		return myJobPersistence.fetchInstancesByJobDefinitionIdAndStatus(
				theJobDefinitionId, theStatuses, theCount, theStart);
	}
}
