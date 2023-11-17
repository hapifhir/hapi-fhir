
```mermaid
---
title: Batch2 Job Instance state transitions
---
stateDiagram-v2
  [*]         --> QUEUED         : on db create and first chunk queued on kakfa
  QUEUED      --> IN_PROGRESS    : on any work-chunk received by worker
  %%  and  (see ca.uhn.fhir.batch2.progress.InstanceProgress.getNewStatus())
  state first_step_finished <<choice>>
  IN_PROGRESS --> first_step_finished : When 1st step finishes
  first_step_finished --> COMPLETED: if no chunks produced
  first_step_finished --> IN_PROGRESS: chunks produced
  IN_PROGRESS --> in_progress_poll : on poll \n(count acomplete/failed/errored chunks)
  in_progress_poll --> COMPLETED   : 0 failures, errored, or incomplete\n AND at least 1 chunk complete
  in_progress_poll --> ERRORED   : no failed but errored chunks
  in_progress_poll --> FINALIZE   : none failed, gated execution\n last step\n queue REDUCER chunk
  in_progress_poll --> IN_PROGRESS : still work to do
  in_progress_poll --> CANCELLED : user requested cancel.
  %% ERRORED is just like IN_PROGRESS, but it is a one-way trip from IN_PROGRESS to ERRORED.
  %% FIXME We could probably delete/merge this state with IS_PROCESS, and use the error count in the UI.
  note left of ERRORED
     Parallel to IS_PROCESS
  end note
  state in_progress_poll <<choice>>
  state error_progress_poll <<choice>>
  ERRORED --> error_progress_poll : on poll \n(count acomplete/failed/errored chunks)
  error_progress_poll --> FAILED   : any failed chunks
  error_progress_poll --> ERRORED   : no failed but errored chunks
  error_progress_poll --> FINALIZE   : none failed, gated execution\n last step\n queue REDUCER chunk
  error_progress_poll --> COMPLETED   : 0 failures, errored, or incomplete AND at least 1 chunk complete
  error_progress_poll --> CANCELLED : user requested cancel.
  state do_report <<choice>>
  FINALIZE --> do_reduction: poll util worker marks REDUCER chunk yes or no.
  do_reduction --> COMPLETED : success
  do_reduction --> FAILED : fail
  in_progress_poll --> FAILED   : any failed chunks

%% terminal states 
   COMPLETED       --> [*]
   FAILED       --> [*]
   CANCELLED       --> [*]
```

```mermaid
---
title: Batch2 Job Work Chunk state transitions
---
stateDiagram-v2
    state READY
    state GATE_WAITING
    state POLL_WAITING
    state QUEUED
    state on_receive <<choice>>
    state IN_PROGRESS
    state ERROR
    state execute <<choice>>
    state FAILED
    state COMPLETED
   direction LR
   [*]         --> READY        : on create - normal step
   [*]         --> GATE_WAITING : on create - gated step
   [*]         --> POLL_WAITING : on create - polling step
   GATE_WAITING       --> READY : on prior step completion
   POLL_WAITING       --> READY : on time expired (maint.)
   READY       --> QUEUED       : placed on kafka (maint.)
  
  %% worker processing states
  QUEUED      --> on_receive : on deque by worker
  on_receive --> IN_PROGRESS : start execution
  
  IN_PROGRESS --> execute: execute
  execute --> ERROR       : on re-triable error
  execute --> COMPLETED   : success\n maybe trigger instance first_step_finished
  execute --> FAILED      : on unrecoverable \n or too many errors
  execute --> POLL_WAITING: on poll retry (use named exception?)
  
  %% temporary error state until retry
  ERROR       --> on_receive : exception rollback\n triggers redelivery
  
  %% terminal states 
  COMPLETED       --> [*]
  FAILED       --> [*]
```

Work
### New state - READY
- create chunks in ready before they are queued
- new phase of job maint. : queue all READY chunks and move them to QUEUED in a safe way. (ca.uhn.fhir.batch2.maintenance.JobMaintenanceServiceImpl.doMaintenancePass)

### New step type POLLING
- Add a new field to WorkChunk: nextPollTimestamp (hapi + Mongo)
- A polling step function will throw a new named exception - RetryChunkLaterException.
- The chunk worker will catch RetryChunkLaterException, and move state from IN_PROGRESS->POLL_WAITING 
  and update nextPollTimestamp to now()+1 min
- Eat the ChunkRetryLaterException so the message is consumed from the queue.
  The chunk must not be re-queued by Spring.
- Add a new phase in the Batch Job Maintenance to update all chunks in POLL_WAITING->READY where nextPollTimestamp<=now()
  via query loop.
  (ca.uhn.fhir.batch2.maintenance.JobMaintenanceServiceImpl.doMaintenancePass) 

### New GATE_WAITING chunk state
- Add a new work chunk state GATE_WAITING
- Change our gated step advance - ca.uhn.fhir.batch2.maintenance.JobInstanceProcessor.triggerGatedExecutions 
  to transactionally update the gated chunks from GATE_WAITING->READY in the same tx we update the step of the job.
- Leave queueing to job maint. phase
