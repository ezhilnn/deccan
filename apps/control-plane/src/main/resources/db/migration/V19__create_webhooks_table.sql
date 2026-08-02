CREATE TABLE webhooks (

    id UUID PRIMARY KEY,

    workflow_id UUID NOT NULL,

    endpoint VARCHAR(150) NOT NULL,

    secret VARCHAR(500) NOT NULL,

    enabled BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_webhook_workflow
        FOREIGN KEY (workflow_id)
        REFERENCES workflows(id),

    CONSTRAINT uk_webhook_endpoint
        UNIQUE (endpoint)

);