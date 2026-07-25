CREATE TABLE workflow_schedules (

    id UUID PRIMARY KEY,

    workflow_id UUID NOT NULL,

    type VARCHAR(30) NOT NULL,

    cron_expression VARCHAR(255),

    enabled BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_schedule_workflow
        FOREIGN KEY (workflow_id)
        REFERENCES workflows(id)

);

CREATE INDEX idx_workflow_schedule_workflow
ON workflow_schedules(workflow_id);