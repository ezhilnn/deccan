CREATE TABLE organizations
(
    id UUID PRIMARY KEY,

    name VARCHAR(150) NOT NULL,

    slug VARCHAR(150) NOT NULL UNIQUE,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE users
(
    id UUID PRIMARY KEY,

    organization_id UUID NOT NULL,

    email VARCHAR(255) NOT NULL UNIQUE,

    first_name VARCHAR(120) NOT NULL,

    last_name VARCHAR(120) NOT NULL,

    password_hash TEXT NOT NULL,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_user_org
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id)
);

CREATE TABLE roles
(
    id UUID PRIMARY KEY,

    organization_id UUID NOT NULL,

    name VARCHAR(120) NOT NULL,

    description TEXT,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_role_org
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id)
);

CREATE TABLE permissions
(
    id UUID PRIMARY KEY,

    name VARCHAR(150) NOT NULL UNIQUE,

    description TEXT,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE role_permissions
(
    role_id UUID NOT NULL,

    permission_id UUID NOT NULL,

    PRIMARY KEY(role_id, permission_id),

    FOREIGN KEY(role_id)
        REFERENCES roles(id),

    FOREIGN KEY(permission_id)
        REFERENCES permissions(id)
);

CREATE TABLE user_roles
(
    user_id UUID NOT NULL,

    role_id UUID NOT NULL,

    PRIMARY KEY(user_id, role_id),

    FOREIGN KEY(user_id)
        REFERENCES users(id),

    FOREIGN KEY(role_id)
        REFERENCES roles(id)
);