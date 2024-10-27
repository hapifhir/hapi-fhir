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

/**
 * Our distributed batch processing library.
 *
 * <p>
 *
 * A running job corresponds to a {@link ca.uhn.fhir.batch2.model.JobInstance}.
 * Jobs are modeled as a sequence of steps, operating on {@link ca.uhn.fhir.batch2.model.WorkChunk}s
 * containing json data.  The first step is special -- it is empty, and the data is assumed to be the job parameters.
 * A {@link ca.uhn.fhir.batch2.model.JobDefinition} defines the sequence of {@link ca.uhn.fhir.batch2.model.JobDefinitionStep}s.
 * Each step defines the input chunk type, the output chunk type, and a procedure that receives the input and emits 0 or more outputs.
 * We have a special kind of final step called a reducer, which corresponds to the stream Collector concept.
 *
 * </p><p>
 *
 * Job instances and work chunks are stored in the database.  Work is distributed to workers via queues.
 * The queue message is just the ids of the chunk (chunk id, step id, instance id, job definition id, etc.).
 * The worker receives the notification from Spring Messaging ({@link ca.uhn.fhir.batch2.coordinator.WorkChannelMessageHandler#handleMessage}),
 * fetches the data from the store and processes the data using the handler defined in for the step.
 * The worker updates chunk state as appropriate.  It may also update the job instance state.
 *
 * </p><p>
 *
 * Once a minute, Quartz runs the  {@link ca.uhn.fhir.batch2.api.IJobMaintenanceService#runMaintenancePass() maintenance pass}.
 * This loop inspects every job, and dispatches to {@link ca.uhn.fhir.batch2.maintenance.JobInstanceProcessor#process() the JobInstanceProcessor}.
 * The JobInstanceProcessor counts the outstanding chunks for a job, and uses these statistics to fire the working state transitions (below).
 *
 * </p><p>
 *
 * Job and chunk processing follow state machines described {@link hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/server_jpa_batch/batch2_states.md}
 * Chunks have a simple {@link ca.uhn.fhir.batch2.model.WorkChunkStatusEnum state system} with states
 * READY, QUEUED, IN_PROGRESS, ERRORED, FAILED, COMPLETED.
 * The initial state is READY, and the final states are FAILED, and COMPLETED.
 *
 * There are 2 primary systems in play during Batch2 Jobs. A Maintenance Job and the Batch2 Job Notification topic.
 *
 * <h>The Maintenance Job</h>
 *
 * This runs every minute and does the following:
 *
 * <ul>
 *     <li>Moves POLL_WAITING work chunks to READY if their nextPollTime has expired.</li>
 *     <li>Moves READY work chunks to QUEUE and publishes it to the Batch2 Notification topic</li>
 *     <li>Calculates job progress (% of workchunks in complete status).</li>
 *     <li>If the job is finished, purges any left over work chunks.</li>
 *     <li>Cleans up any complete, failed, or cancelled jobs.</li>
 *     <li>Moves any gated jobs onto their next step.</li>
 *     <li>If the final step of a (gated) job is a reduction step, a reduction step execution will be triggered.</li>
 * </ul>
 *
 * <h>Processing the Messages</h>
 *
 * <ul>
 *     <li>Change the work chunk from QUEUED to IN_PROGRESS</li>
 *     <li>Change the Job Instance status from QUEUED to IN_PROGRESS</li>
 *     <li>If the Job Instance is cancelled, change the status to CANCELLED and abort processing</li>
 *     <li>If the step creates new work chunks, each work chunk will be created in the READY state</li>
 *     <li>If the step succeeds, the work chunk status is changed from IN_PROGRESS to COMPLETE</li>
 *     <li>If the step throws a RetryChunkLaterException, the work chunk status is changed from IN_PROGRESS to POLL_WAITING and a nextPollTime value set.</li>
 *     <li>If the step fails, the work chunk status is changed from IN_PROGRESS to either ERRORED or FAILED depending on the severity of the error</li>
 * </ul>
 *
 * <h>The job lifecycle</h>
 *
 * <ul>
 *    <li> Chunks are created READY (NB - should be READY or WAITING) and notification is posted to the channel for non-gated steps.</li>
 *    <li>
 *       Workers receive a notification and advance QUEUED->IN_PROGRESS.
 *       {@link ca.uhn.fhir.batch2.api.IWorkChunkPersistence#onWorkChunkDequeue(String)}
 *       </li>
 *    <li>
 *       On normal execution, the chunk advances IN_PROGRESS->COMPLETED {@link ca.uhn.fhir.batch2.api.IWorkChunkPersistence#onWorkChunkCompletion} </li>
 *    <li> On a retryiable error, IN_PROGRESS->ERROR with an error message and the chunk is put back on the queue. {@link ca.uhn.fhir.batch2.api.IWorkChunkPersistence#onWorkChunkError} </li>
 *    <li> On a RetryChunkLaterException, IN_PROGRESS->POLL_WAITING with a nextPollTime set. The chunk is *not* put back on the queue, but is left for the maintenance job to manage.</li>
 *    <li> On a hard failure, or too many errors, IN_PROGRESS->FAILED with the error message. {@link ca.uhn.fhir.batch2.api.IWorkChunkPersistence#onWorkChunkFailed} </li>
 * </ul>
 *
 * </p><p>
 *
 *    Jobs have a state machine with {@link ca.uhn.fhir.batch2.model.StatusEnum states}:
 *    QUEUED, IN_PROGRESS, ERRORED, COMPLETED, FINALIZE, and FAILED.
 *    ERRORED is a near synonym for IN_PROGRESS and shows that a chunk has shown a transient error during this job.
 *    Hard failures move to final state FAILED.
 *    The initial state is QUEUED, and the terminal states are COMPLETED, CANCELLED, and FAILED.
 *    Most transitions happen during the maintenance run, but some are triggered by the worker.
 *
 * <ul>
 *    <li> Jobs are created in QUEUED state, along with their first chunk.  The chunk is also sent to the channel.
 *        {@link ca.uhn.fhir.batch2.coordinator.JobCoordinatorImpl#startInstance}
 *        </li>
 *    <li> When workers dequeue a chunk, they trigger a QUEUED->IN_PROGRESS transition to report status.
 *        {@link ca.uhn.fhir.batch2.coordinator.WorkChannelMessageHandler.MessageProcess#updateAndValidateJobStatus}
 *        </li>
 *    <li> As a special case, if the first chunk produces no children, the job advances IN_PROGRESS->COMPLETE
 *         {@link ca.uhn.fhir.batch2.coordinator.JobStepExecutor#executeStep()}
 *         </li>
 *    <li> Other transitions happen during maintenance runs. If a job is running, and the user has requested cancellation,
 *         the job transitions (IN_PROGRESS or ERRORED) -> CANCELLED.
 *         </li>
 *    <li> Then the processor looks at chunk statuses.  If any chunks are FAILED, then the job moves
 *        (IN_PROGRESS or ERRORED) -> FAILED. {@link ca.uhn.fhir.batch2.progress.InstanceProgress#calculateNewStatus}
 *        </li>
 *    <li> If any chunk is currently in {@link ca.uhn.fhir.batch2.model.WorkChunkStatusEnum#ERRORED ERRORED} state,
 *        the job progresses IN_PROGRESS->ERRORED, and the error message is copied over.
 *        </li>
 *    <li> If all chunks are COMPLETED, then the job moves (IN_PROGRESS or ERRORED) -> COMPLETED.
 *        </li>
 *    <li> Gated jobs that have a reducer step will transtion (IN_PROGRESS or ERRORED) -> FINALIZE when
 *         starting the reduction step
 *         {@link ca.uhn.fhir.batch2.maintenance.JobInstanceProcessor#triggerGatedExecutions}
 *         </li>
 * </ul>
 *
 * Design gaps:
 * <ul>
 *    <li> If the maintenance job is killed while sending notifications about
 *         a gated step advance, remaining chunks will never be notified.  A READY state before QUEUED would catch this.
 *         A WAITING state for gated chunks will simplify that handling.
 *         </li>
 *    <li> A running reduction step will not restart if the server is killed. </li>
 * </ul>
 */
package ca.uhn.fhir.batch2;
