/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.api;

/**
 * Marker interface for exceptions that a job step throws to tell the batch2 framework
 * (<code>StepExecutor</code> / <code>ReductionStepExecutorServiceImpl</code>) how to handle the
 * current work chunk or job, for example {@link RetryChunkLaterException} to poll the chunk again
 * later, or {@link JobExecutionFailedException} to fail it without retrying.
 * <p>
 * Code running in or below a job step that catches broad exception types (<code>Exception</code>,
 * <code>RuntimeException</code>, <code>Throwable</code>) must rethrow exceptions implementing this
 * interface unchanged - wrapping or swallowing them hides the signal from the framework.
 * </p>
 * <p>
 * Implementations must be unchecked, i.e. extend {@link RuntimeException}.
 * </p>
 */
// Created by claude-opus-5-5
public interface IBatch2FrameworkException {}
