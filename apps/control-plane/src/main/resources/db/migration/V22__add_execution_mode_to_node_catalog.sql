ALTER TABLE node_catalog
ADD COLUMN execution_mode VARCHAR(30);

UPDATE node_catalog
SET execution_mode = 'CONTROL_PLANE'
WHERE name IN
(
    'manual-trigger',
    'condition',
    'response'
);

UPDATE node_catalog
SET execution_mode = 'WORKER'
WHERE execution_mode IS NULL;

ALTER TABLE node_catalog
ALTER COLUMN execution_mode SET NOT NULL;