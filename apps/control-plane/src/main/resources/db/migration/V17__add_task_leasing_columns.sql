ALTER TABLE execution_tasks
ADD COLUMN leased_at TIMESTAMP WITH TIME ZONE;

CREATE INDEX idx_execution_tasks_status_created
ON execution_tasks(status, created_at);

CREATE INDEX idx_execution_tasks_lease_until
ON execution_tasks(lease_until);

CREATE INDEX IF NOT EXISTS idx_workers_status
ON workers(status);

