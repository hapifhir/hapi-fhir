/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 specification tests
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.batch2.test.inline;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.JobOperationResultJson;
import ca.uhn.fhir.batch2.model.BatchInstanceStatusDTO;
import ca.uhn.fhir.batch2.model.BatchWorkChunkStatusDTO;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.google.common.collect.ListMultimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

/**
 * {@link IJobCoordinator} used for testing that tiggers a batch job without the heavy infrastructure of the
 * batch 2 framework.
 * @param <T> the type of the job parameters
 */
public class InlineJobCoordinator<T extends IModelJson> implements IJobCoordinator {

    private final InlineJobRunner<T> myInlineJobRunner;
	private final String myJobInstanceId;
    private final Class<T> myClass;

    private T myParameters;

    public InlineJobCoordinator(JobDefinition<T> theJobDefinition, String theJobInstanceId, Class<T> theClass) {
        myInlineJobRunner = new InlineJobRunner<>(theJobDefinition);
		myJobInstanceId = theJobInstanceId;
		myClass = theClass;
    }

    @Override
    public Batch2JobStartResponse startInstance(
            RequestDetails requestDetails, JobInstanceStartRequest jobInstanceStartRequest)
            throws InvalidRequestException {

        myParameters = jobInstanceStartRequest.getParameters(myClass);

        final Batch2JobStartResponse batch2JobStartResponse = new Batch2JobStartResponse();
        batch2JobStartResponse.setInstanceId(myJobInstanceId);
        return batch2JobStartResponse;
    }

    public ListMultimap<String, IModelJson> triggerJobRunner() {
        return myInlineJobRunner.run(myParameters);
    }

    @Nonnull
    @Override
    public JobInstance getInstance(String s) throws ResourceNotFoundException {
		final JobInstance jobInstance = new JobInstance();
		jobInstance.setJobDefinitionId(myJobInstanceId);

		return jobInstance;
    }

    @Override
    public List<JobInstance> getInstances(int i, int i1) {
        return List.of();
    }

    @Override
    public List<JobInstance> getRecentInstances(int i, int i1) {
        return List.of();
    }

    @Override
    public JobOperationResultJson cancelInstance(String s) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public List<JobInstance> getInstancesbyJobDefinitionIdAndEndedStatus(
            String s, @Nullable Boolean aBoolean, int i, int i1) {
        return List.of();
    }

    @Override
    public Page<JobInstance> fetchAllJobInstances(JobInstanceFetchRequest jobInstanceFetchRequest) {
        return null;
    }

    @Override
    public List<JobInstance> getJobInstancesByJobDefinitionIdAndStatuses(String s, Set<StatusEnum> set, int i, int i1) {
        return List.of();
    }

    @Override
    public List<JobInstance> getJobInstancesByJobDefinitionId(String s, int i, int i1) {
        return List.of();
    }

    @Override
    public List<BatchWorkChunkStatusDTO> getWorkChunkStatus(String s) {
        return List.of();
    }

    @Override
    public BatchInstanceStatusDTO getBatchInstanceStatus(String s) {
        return null;
    }
}
