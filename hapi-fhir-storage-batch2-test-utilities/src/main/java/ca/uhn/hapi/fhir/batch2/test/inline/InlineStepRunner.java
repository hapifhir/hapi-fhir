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

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.model.api.IModelJson;
import java.util.ArrayList;
import java.util.List;

/**
 * Run all steps for a given batch job definition and collect the output for each step.
 *
 * @param <PT> the type of the job parameters
 * @param <IT> the input type of the job step
 * @param <OT> the output type of the job step
 */
class InlineStepRunner<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> {

    private final JobInstance myInstance;
    private final JobDefinitionStep<PT, IT, OT> myStep;
    private final PT myParameter;
    private final List<WorkChunkData<OT>> myCurrentOutput = new ArrayList<>();

    InlineStepRunner(
            JobDefinitionStep<PT, IT, OT> theStep,
            PT theParameter,
            JobInstance theInstance) {
        myStep = theStep;
        myParameter = theParameter;
        myInstance = theInstance;
    }

    List<WorkChunkData<OT>> getCurrentOutput() {
        return myCurrentOutput;
    }

    void run(List<WorkChunkData<IT>> theCurrentInput) {
        InlineJobDataSink<OT> sink = new InlineJobDataSink<>(myCurrentOutput);

        final WorkChunk workChunk = new WorkChunk();

        if (myStep.isReductionStep()) {
            runReduce(theCurrentInput, workChunk, sink);
        } else {
            runNonReduce(theCurrentInput, workChunk, sink);
        }
    }

    private void runReduce(
		List<WorkChunkData<IT>> theCurrentInput, WorkChunk workChunk, InlineJobDataSink<OT> sink) {
        for (WorkChunkData<IT> nextChunk : theCurrentInput) {
            final IJobStepWorker<PT, IT, OT> jobStepWorker = myStep.getJobStepWorker();

            if (jobStepWorker
                    instanceof IReductionStepWorker<PT, IT, OT> reductionStepWorker) {
                final ChunkExecutionDetails<PT, IT> chunkExecutionDetails =
                        new ChunkExecutionDetails<>(
                                nextChunk.getData(),
                                myParameter,
                                myInstance.getInstanceId(),
                                workChunk.getInstanceId());
                reductionStepWorker.consume(chunkExecutionDetails);
            }
        }
        final StepExecutionDetails<PT, IT> stepExecutionDetails =
                new StepExecutionDetails<>(myParameter, null, myInstance, workChunk);
        myStep.getJobStepWorker().run(stepExecutionDetails, sink);
    }

    private void runNonReduce(
		List<WorkChunkData<IT>> theCurrentInput, WorkChunk workChunk, InlineJobDataSink<OT> sink) {
        for (WorkChunkData<IT> nextChunk : theCurrentInput) {
            final IT data = nextChunk.getData();
            final StepExecutionDetails<PT, IT> stepExecutionDetails =
                    new StepExecutionDetails<>(myParameter, data, myInstance, workChunk);
            myStep.getJobStepWorker().run(stepExecutionDetails, sink);
        }
    }
}
