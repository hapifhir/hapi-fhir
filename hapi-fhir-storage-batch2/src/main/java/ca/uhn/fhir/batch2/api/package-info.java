/**
 * This package contains the APIs used in the Batch2 framework.
 * <ul>
 *    <ol>
 *       {@link ca.uhn.fhir.batch2.api.IJobCoordinator} is the external facing API for the
 *       framework, used to start and stop jobs and inquire about status.
 *    </ol>
 *    <ol>
 *       {@link ca.uhn.fhir.batch2.api.IJobMaintenanceService} is a background processor that
 *       updates statistics and clean up stale data
 *    </ol>
 *    <ol>
 *       {@link ca.uhn.fhir.batch2.api.IJobStepWorker} is the interface that a job
 *       implementation would implement for its step workers. The first and last step would
 *       respectively use a sub-interface of IJobStepWorker:
 *       {@link ca.uhn.fhir.batch2.api.IFirstJobStepWorker} is the first step and
 *       {@link ca.uhn.fhir.batch2.api.ILastJobStepWorker} is the last
 *    </ol>
 *    <ol>
 *       {@link ca.uhn.fhir.batch2.api.IJobDataSink} is a callback API provided to
 *       step workers that they can supply data to for processing in subsequent steps.
 *    </ol>
 *    <ol>
 *       {@link ca.uhn.fhir.batch2.api.IJobPersistence} is an internal API providing storage
 *       for jobs.
 *    </ol>
 * </ul>
 *
 * @since 6.0.0
 */
package ca.uhn.fhir.batch2.api;

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

