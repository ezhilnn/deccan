CREATE TABLE workflow_kafka_triggers (

    id UUID PRIMARY KEY,

    workflow_id UUID NOT NULL,

    topic VARCHAR(255) NOT NULL UNIQUE,

    enabled BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_workflow_kafka_trigger
        FOREIGN KEY (workflow_id)
        REFERENCES workflows(id)

);

CREATE INDEX idx_workflow_kafka_topic
ON workflow_kafka_triggers(topic);