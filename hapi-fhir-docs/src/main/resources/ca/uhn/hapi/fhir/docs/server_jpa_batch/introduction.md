# HAPI-FHIR Batch Processing

## Introduction

HAPI-FHIR 5.1.0 introduced support for batch processing using the Spring Batch framework.  However since its introduction, we discovered Spring Batch jobs do not recover well from ungraceful server shutdowns, which are increasingly common as implementors move to containerized deployment technologies such as Kubernetes.

HAPI-FHIR 6.0.0 has begun the process of replacing Spring Batch with a custom batch framework, called "batch2".  This new "batch2" framework is designed to scale well across multiple processes sharing the same message broker, and most importantly, to robustly recover from unexpected server restarts.

## Design

### Definition

A HAPI-FHIR batch job definition consists of a job name, version, parameter json input type, and a chain of job steps.  Each step of the chain declares the json output type it produces, which will be the input type for the following step.  The final step in the chain does not declare an output type as the final step will typically do the work of the job, e.g. reindex resources, export data to disk, etc.

<img src="/hapi-fhir/docs/images/job-definition.svg"/>

### Submitting a Job

After a job has been defined, *instances* of that job can be submitted for batch processing by populating a `JobInstanceStartRequest` with the job name and job parameters json and then submitting that request to the Batch Job Coordinator.

The Batch Job Coordinator will then store two records in the database:
- Job Instance with status `QUEUED`: that is the parent record for all data concerning this job
- Batch Work Chunk with status `READY`: this describes the first "chunk" of work required for this job. The first Batch Work Chunk contains no data.

### The Maintenance Job

A Scheduled Job runs periodically (once a minute).  For each Job Instance in the database, it:

1. Calculates job progress (% of work chunks in `COMPLETE` status). If the job is finished, purges any left over work chunks still in the database.
1. Moves all `POLL_WAITING` work chunks to `READY` if their `nextPollTime` has expired.
1. Calculates job progress (% of work chunks in `COMPLETE` status). If the job is finished, purges any leftover work chunks still in the database.
1. Cleans up any complete, failed, or cancelled jobs that need to be removed.
1. When the current step is complete, moves any gated jobs onto their next step and updates all chunks in `GATE_WAITING` to `READY`. If the the job is being moved to its final reduction step, chunks are moved from `GATE_WAITING` to `REDUCTION_READY`.
1. If the final step of a gated job is a reduction step, a reduction step execution will be triggered. All workchunks for the job in `REDUCTION_READY` will be consumed at this point.
1. Moves all `READY` work chunks into the `QUEUED` state and publishes a message to the Batch Notification Message Channel to inform worker threads that a work chunk is now ready for processing. \*

\* An exception is for the final reduction step, where work chunks are not published to the Batch Notification Message Channel,
but instead processed inline.

### Batch Notification Message Handler

HAPI-FHIR Batch Jobs run based on job notification messages of the Batch Notification Message Channel (named `batch2-work-notification`).

When a notification message arrives, the handler does the following:

1. Change the work chunk status from `QUEUED` to `IN_PROGRESS`
1. Change the Job Instance status from `QUEUED` to `IN_PROGRESS`
1. If the Job Instance is cancelled, change the status to `CANCELLED` and abort processing
1. If the step creates new work chunks, each work chunk will be created in either the `GATE_WAITING` state (for gated jobs) or `READY` state (for non-gated jobs) and will be handled in the next maintenance job pass.
1. If the step succeeds, the work chunk status is changed from `IN_PROGRESS` to `COMPLETED`, and the data it contained is deleted.
1. If the step throws a `RetryChunkLaterException`, the work chunk status is changed from `IN_PROGRESS` to `POLL_WAITING`, and a `nextPollTime` value will be set.
1. If the step fails, the work chunk status is changed from `IN_PROGRESS` to either `ERRORED` or `FAILED`, depending on the severity of the error.

### First Step

The first step in a job definition is executed with just the job parameters.

### Middle steps

Middle Steps in the job definition are executed using the initial Job Parameters and the Work Chunk data produced from the previous step.

### Final Step

The final step operates the same way as the middle steps, except it does not produce any new work chunks.

### Gated Execution

If a Job Definition is set to having Gated Execution, then all work chunks for a step must be `COMPLETED` before any work chunks for the next step may begin.

### Job Instance Completion

A Batch Job Maintenance Service runs every minute to monitor the status of all Job Instances and the Job Instance is transitioned to either `COMPLETED`, `ERRORED` or `FAILED` according to the status of all outstanding work chunks for that job instance. If the job instance is still `IN_PROGRESS` this maintenance service also estimates the time remaining to complete the job.
