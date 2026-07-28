ALTER TABLE execution_tasks

ADD COLUMN node_type VARCHAR(100),

ADD COLUMN configuration JSONB,

ADD COLUMN error_message TEXT,

ADD COLUMN started_at TIMESTAMPTZ,

ADD COLUMN completed_at TIMESTAMPTZ,

ADD COLUMN retry_count INTEGER NOT NULL DEFAULT 0;