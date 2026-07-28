CREATE TABLE workers (

    id UUID PRIMARY KEY,

    worker_name VARCHAR(255) NOT NULL UNIQUE,

    host_name VARCHAR(255) NOT NULL,

    status VARCHAR(50) NOT NULL,

    last_heartbeat TIMESTAMPTZ NOT NULL,

    capabilities JSONB NOT NULL,

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL

);

CREATE INDEX idx_workers_status
ON workers(status);

CREATE INDEX idx_workers_heartbeat
ON workers(last_heartbeat);