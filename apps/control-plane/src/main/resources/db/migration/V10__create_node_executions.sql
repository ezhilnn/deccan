CREATE TABLE node_executions
(
    id UUID PRIMARY KEY,

    workflow_execution_id UUID NOT NULL,

    node_id VARCHAR(255) NOT NULL,

    node_type VARCHAR(100) NOT NULL,

    status VARCHAR(50) NOT NULL,

    started_at TIMESTAMPTZ NOT NULL,

    finished_at TIMESTAMPTZ,

    duration_ms BIGINT,

    output JSONB,

    error_message TEXT,

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_node_execution
        FOREIGN KEY(workflow_execution_id)
        REFERENCES workflow_executions(id)
);