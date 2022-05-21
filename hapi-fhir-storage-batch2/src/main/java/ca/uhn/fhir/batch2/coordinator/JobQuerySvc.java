package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.model.api.annotation.PasswordField;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.List;
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

	JobInstance fetchInstance(String theInstanceId) {
		return myJobPersistence.fetchInstance(theInstanceId)
			.map(this::massageInstanceForUserAccess)
			.orElseThrow(() -> new ResourceNotFoundException(Msg.code(2040) + "Unknown instance ID: " + UrlUtil.escapeUrlParam(theInstanceId)));
	}

	List<JobInstance> fetchInstances(int thePageSize, int thePageIndex) {
		return myJobPersistence.fetchInstances(thePageSize, thePageIndex).stream()
			.map(this::massageInstanceForUserAccess)
			.collect(Collectors.toList());
	}

	public List<JobInstance> fetchRecentInstances(int theCount, int theStart) {
		return myJobPersistence.fetchRecentInstances(theCount, theStart)
			.stream().map(this::massageInstanceForUserAccess)
			.collect(Collectors.toList());
	}

	private JobInstance massageInstanceForUserAccess(JobInstance theInstance) {
		JobInstance retVal = new JobInstance(theInstance);

		JobDefinition<?> definition = myJobDefinitionRegistry.getJobDefinitionOrThrowException(theInstance.getJobDefinitionId(), theInstance.getJobDefinitionVersion());

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
}
