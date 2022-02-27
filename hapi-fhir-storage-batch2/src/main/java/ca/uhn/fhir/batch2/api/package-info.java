/**
 * This package contains the APIs used in the Batch2 framework.
 * <ul>
 *    <ol>
 *       {@link ca.uhn.fhir.batch2.api.IJobCoordinator} is the external facing API for the
 *       framework, used to start and stop jobs and inquire about status.
 *    </ol>
 *    <ol>
 *       {@link ca.uhn.fhir.batch2.api.IJobCleanerService} is a background processor that
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

