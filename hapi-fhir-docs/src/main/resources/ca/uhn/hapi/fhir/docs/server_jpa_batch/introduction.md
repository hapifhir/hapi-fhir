# HAPI-FHIR Batch Processing

## Introduction

HAPI-FHIR 5.1.0 introduced support for batch processing using the Spring Batch framework.  However since its introduction, we discovered Spring Batch jobs do not recover well from ungraceful server shutdowns, which are increasingly common as implementors move to containerized deployment technologies such as Kubernetes.

HAPI-FHIR 6.0.0 has begun the process of replacing Spring Batch with a custom batch framework, called "batch2".  This new "batch2" framework is designed to scale well across multiple processes sharing the same message broker, and most importantly, to robustly recover from unexpected server restarts.

## Design

### Definition

A HAPI-FHIR batch job definition consists of a job name, version, parameter json input type, and a chain of job steps.  Each step of the chain declares the json output type it produces, which will be the input type for the following step.  The final step in the chain does not declare an output type as the final step will typically do the work of the job, e.g. reindex resources, export data to disk, etc.

<a href="/hapi-fhir/docs/images/job-definition.svg"/>

### Submitting a Job

After a job has been defined, *instances* of that job can be submitted for batch processing by populating a `JobInstanceStartRequest` with the job name and job parameters json and then submitting that request to the Batch Job Coordinator.

The Batch Job Coordinator will then store two records in the database:
- Job Instance: that is the parent record for all data concerning this job
- Batch Work Chunk: this describes the first "chunk" of work required for this job

Lastly the Batch Job Coordinator publishes a message to the `batch2-work-notification` channel to inform worker threads that this first chunk of work is now ready for processing.

### Job Processing

HAPI-FHIR Batch Jobs run based on job notification messages.  The process is kicked off by the first chunk of work.  When this notification message arrives, the message handler makes a single call to the first step defined in the job definition, passing in the job parameters as input.

The handler then does the following:
1. Mark this work chunk as IN_PROGRESS
2. 
