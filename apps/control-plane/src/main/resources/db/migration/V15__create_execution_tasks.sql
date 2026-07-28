CREATE TABLE execution_tasks (

    id UUID PRIMARY KEY,

    execution_id UUID NOT NULL,

    worker_id UUID,

    node_id VARCHAR(255) NOT NULL,

    status VARCHAR(50) NOT NULL,

    lease_until TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_execution_task_execution
        FOREIGN KEY (execution_id)
        REFERENCES workflow_executions(id),

    CONSTRAINT fk_execution_task_worker
        FOREIGN KEY (worker_id)
        REFERENCES workers(id)

);

CREATE INDEX idx_execution_task_status
ON execution_tasks(status);

CREATE INDEX idx_execution_task_worker
ON execution_tasks(worker_id);

CREATE INDEX idx_execution_task_lease
ON execution_tasks(lease_until);