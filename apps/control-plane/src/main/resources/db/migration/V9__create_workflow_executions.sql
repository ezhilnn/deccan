CREATE TABLE workflow_executions
(
    id UUID PRIMARY KEY,

    workflow_id UUID NOT NULL,

    workflow_version INTEGER NOT NULL,

    status VARCHAR(50) NOT NULL,

    started_at TIMESTAMPTZ NOT NULL,

    finished_at TIMESTAMPTZ,

    input JSONB,

    output JSONB,

    error_message TEXT,

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_execution_workflow
        FOREIGN KEY(workflow_id)
        REFERENCES workflows(id)
);

CREATE INDEX idx_execution_workflow
ON workflow_executions(workflow_id);

CREATE INDEX idx_execution_status
ON workflow_executions(status);