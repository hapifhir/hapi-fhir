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
- Job Instance with status QUEUED: that is the parent record for all data concerning this job
- Batch Work Chunk with status QUEUED: this describes the first "chunk" of work required for this job.  The first Batch Work Chunk contains no data.

Lastly the Batch Job Coordinator publishes a message to the Batch Notification Message Channel (named `batch2-work-notification`) to inform worker threads that this first chunk of work is now ready for processing.

### Job Processing - First Step

HAPI-FHIR Batch Jobs run based on job notification messages.  The process is kicked off by the first chunk of work.  When this notification message arrives, the message handler makes a single call to the first step defined in the job definition, passing in the job parameters as input.

The handler then does the following:
1. Change the work chunk status from QUEUED to IN_PROGRESS
2. Change the Job Instance status from QUEUED to IN_PROGRESS
3. If the Job Instance is cancelled, change the status to CANCELLED and abort processing.
4. The first step of the job definition is executed with the job parameters
5. This step creates new work chunks.  For each work chunk it creates, it json serializes the work chunk data, stores it in the database, and publishes a new message to the Batch Notification Message Channel to notify worker threads that there are new work chunks waiting to be processed.
6. If the step succeeded, the work chunk status is changed from IN_PROGRESS to COMPLETED, and the data it contained is deleted.
7. If the step failed, the work chunk status is changed from IN_PROGRESS to either ERRORED or FAILED depending on the severity of the error.

### Job Processing - Middle steps

Middle Steps in the job definition are executed in the same way, except instead of only using the Job Parameters as input, they use both the Job Parameters and the Work Chunk data produced from the previous step.

### Job Processing - Final Step

The final step operates the same way as the middle steps, except it does not produce any new work chunks.

### Gated Execution

If a Job Definition is set to having Gated Execution, then all work chunks for one step must be COMPLETED before any work chunks for the next step may begin.

### Job Instance Completion

A Batch Job Maintenance Service runs every minute to monitor the status of all Job Instances and the Job Instance is transitioned to either COMPLETED, ERRORED or FAILED according to the status of all outstanding work chunks for that job instance.  If the job instance is still IN_PROGRESS this maintenance service also estimates the time remaining to complete the job.
