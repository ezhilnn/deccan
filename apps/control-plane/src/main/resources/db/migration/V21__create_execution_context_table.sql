CREATE TABLE execution_context (

    id UUID PRIMARY KEY,

    execution_id UUID NOT NULL,

    node_id VARCHAR(255) NOT NULL,

    output JSONB NOT NULL,

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_execution_context_execution
        FOREIGN KEY (execution_id)
        REFERENCES workflow_executions(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_execution_context
        UNIQUE (
            execution_id,
            node_id
        )

);

CREATE INDEX idx_execution_context_execution
ON execution_context(execution_id);