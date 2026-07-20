CREATE TABLE workflows
(
    id UUID PRIMARY KEY,

    organization_id UUID NOT NULL,

    name VARCHAR(150) NOT NULL,

    description TEXT,

    status VARCHAR(30) NOT NULL,

    current_version INTEGER NOT NULL DEFAULT 1,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_workflow_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id)
);

CREATE INDEX idx_workflow_organization
ON workflows(organization_id);

CREATE INDEX idx_workflow_status
ON workflows(status);