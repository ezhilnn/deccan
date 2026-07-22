ALTER TABLE connectors
ADD COLUMN credential_id UUID;

ALTER TABLE connectors
ADD CONSTRAINT fk_connector_credential
FOREIGN KEY (credential_id)
REFERENCES connector_credentials(id);