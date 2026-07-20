CREATE TABLE workflow_versions
(
    id UUID PRIMARY KEY,

    workflow_id UUID NOT NULL,

    version INTEGER NOT NULL,

    definition JSONB NOT NULL,

    published BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_workflow_version_workflow
        FOREIGN KEY (workflow_id)
        REFERENCES workflows(id)
);

CREATE UNIQUE INDEX uk_workflow_version
ON workflow_versions(workflow_id, version);

CREATE INDEX idx_workflow_version_workflow
ON workflow_versions(workflow_id);