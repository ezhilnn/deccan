CREATE TABLE connectors
(
    id UUID PRIMARY KEY,

    organization_id UUID NULL,

    name VARCHAR(100) NOT NULL,

    display_name VARCHAR(200) NOT NULL,

    type VARCHAR(50) NOT NULL,

    version VARCHAR(30) NOT NULL,

    enabled BOOLEAN NOT NULL,

    configuration_schema JSONB NOT NULL,

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_connector_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id)
);

CREATE UNIQUE INDEX uk_connector_name_version
ON connectors(name,version);

CREATE INDEX idx_connector_type
ON connectors(type);