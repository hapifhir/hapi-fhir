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

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.model.api.IModelJson;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.List;

/**
 * Test utility class used to trigger all steps in a batch {@link JobDefinition} in a lightweight way, leveraging the
 * {@link InlineStepRunner} to trigger and collect data from each step, returning the collected step data to the caller.
 *
 * @param <PT> the type of the job parameters
 */
class InlineJobRunner<PT extends IModelJson> {

    private final JobDefinition<PT> myJobDefinition;

    InlineJobRunner(JobDefinition<PT> theJobDefinition) {
        myJobDefinition = theJobDefinition;
    }

    ListMultimap<String, IModelJson> run(PT theParameter) {
        final ListMultimap<String, IModelJson> outputPerStep = ArrayListMultimap.create();
        final JobInstance instance = new JobInstance();

		List<? extends WorkChunkData<?>> currentInput = List.of(new WorkChunkData<>(null));

        final List<JobDefinitionStep<PT, ?, ?>> steps = myJobDefinition.getSteps();
        for (JobDefinitionStep<PT, ?, ?> step : steps) {
            final InlineStepRunner<PT, ?, ?> ptInlineStepRunner =
                    new InlineStepRunner<>(step, theParameter, instance);
            ptInlineStepRunner.run(unsafeCast(currentInput));

            final List<? extends WorkChunkData<?>> currentOutput = ptInlineStepRunner.getCurrentOutput();

            outputPerStep.putAll(
                    step.getStepId(),
                    currentOutput.stream().map(WorkChunkData::getData).toList());

            currentInput = currentOutput;
        }

        return outputPerStep;
    }

    private static <T> T unsafeCast(Object obj) {
        @SuppressWarnings("unchecked")
        T t = (T) obj;
        return t;
    }
}
