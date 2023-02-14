
```mermaid
---
title: Batch2 Job Instance state transitions
---
stateDiagram-v2
  [*]         --> QUEUED         : on db create and queued on kakfa
  QUEUED      --> IN_PROGRESS    : on any work-chunk received by worker
  %%  and  (see ca.uhn.fhir.batch2.progress.InstanceProgress.getNewStatus())
  state first_step_finished <<choice>>
  IN_PROGRESS --> first_step_finished : When 1st step finishes
  first_step_finished --> COMPLETED: if no chunks produced
  first_step_finished --> IN_PROGRESS: chunks produced
  state in_progress_poll <<choice>>
  IN_PROGRESS --> in_progress_poll : on poll (count and update complete, failed, errored chunk counts)
  in_progress_poll --> FAILED   : any failed chunks
  in_progress_poll --> ERRORED   : no failed but errored chunks
  in_progress_poll --> COMPLETED   : 0 failures, errored, or incomplete AND at least 1 chunk complete
  %% WIPMB cover FINALIZE
  %% WIPMB cover FINALIZE
  
```

```mermaid
---
title: Batch2 Job Work Chunk state transitions
---
stateDiagram-v2
  [*]         --> QUEUED        : on store
  state on_receive <<choice>>
  %%QUEUED      --> IN_PROGRESS : on receive by worker
  QUEUED      --> on_receive : on receive by worker
  %% What's this about?
  on_receive --> IN_PROGRESS : start execution
  state execute <<choice>>
  IN_PROGRESS --> execute: execute
  %%  (JobExecutionFailedException or Throwable)
  execute --> COMPLETED   : success
  execute --> ERROR       : on re-triable error
  execute --> FAILED      : on unrecoverable \n or too many errors
  %%ERROR       --> IN_PROGRESS : exception rollback triggers redelivery
  ERROR       --> on_receive : exception rollback triggers redelivery
  COMPLETED       --> [*]
  FAILED       --> [*]
```
