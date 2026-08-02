CREATE TABLE secrets (

    id UUID PRIMARY KEY,

    organization_id UUID NOT NULL,

    name VARCHAR(150) NOT NULL,

    description VARCHAR(500),

    encrypted_value TEXT NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_secret_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    CONSTRAINT uk_secret_name
        UNIQUE (organization_id, name)

);