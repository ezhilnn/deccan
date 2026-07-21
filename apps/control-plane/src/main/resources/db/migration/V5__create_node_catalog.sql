CREATE TABLE node_catalog
(
    id UUID PRIMARY KEY,

    name VARCHAR(100) NOT NULL UNIQUE,

    display_name VARCHAR(200) NOT NULL,

    category VARCHAR(100) NOT NULL,

    implementation VARCHAR(200) NOT NULL,

    enabled BOOLEAN NOT NULL,

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_node_catalog_category
ON node_catalog(category);