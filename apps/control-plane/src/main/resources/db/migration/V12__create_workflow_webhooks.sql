CREATE TABLE workflow_webhooks(

    id UUID PRIMARY KEY,

    workflow_id UUID NOT NULL,

    token VARCHAR(100) UNIQUE NOT NULL,

    enabled BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_webhook_workflow
        FOREIGN KEY(workflow_id)
        REFERENCES workflows(id)

);

CREATE INDEX idx_webhook_token
ON workflow_webhooks(token);