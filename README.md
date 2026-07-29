## Workflow Scheduling

The scheduler is responsible for determining which tasks are eligible for execution.

Scheduling process:

1. Workflow execution creates all execution tasks in `PENDING`.
2. Entry tasks are promoted to `READY`.
3. Workers lease `READY` tasks.
4. On successful completion, downstream dependencies are evaluated.
5. A task becomes `READY` only when all of its parent tasks are `COMPLETED`.

This allows Deccan to support:

- Linear workflows
- Parallel execution
- Fan-out
- Fan-in (joins)
- Dependency-based scheduling

                Workflow Version
                       │
                       ▼
          WorkflowGraphService
                       │
                       ▼
          ExecutionWorkflowGraph
                       │
                       ▼
        WorkflowSchedulerService
                       │
         ┌─────────────┴─────────────┐
         ▼                           ▼
Initialize Entry Tasks      Schedule Next Tasks
         │                           │
         └─────────────┬─────────────┘
                       ▼
                READY Tasks
                       │
                       ▼
                Worker Leasing
                       │
                       ▼
               Worker Execution
                       │
                       ▼
              Task Completion