CREATE TABLE connector_credentials
(
    id UUID PRIMARY KEY,

    organization_id UUID NOT NULL,

    name VARCHAR(100) NOT NULL,

    type VARCHAR(50) NOT NULL,

    provider VARCHAR(100) NOT NULL,

    secret_reference VARCHAR(200) NOT NULL,

    enabled BOOLEAN NOT NULL,

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_connector_credential_org
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id)
);

CREATE UNIQUE INDEX uk_connector_credential_name
ON connector_credentials(
organization_id,
name
);