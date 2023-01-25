-- Drop old indexes
DROP INDEX IF EXISTS company_id_index ON reminder;

-- Rename columns
ALTER TABLE reminder CHANGE COLUMN IF EXISTS company_id organization_id varchar(255);

-- Create new indexes
CREATE INDEX IF NOT EXISTS organization_id_index on reminder (organization_id);

-- Necessary line in order to document the change.
INSERT INTO reminder.schema_history (schema_version,comment,applied) VALUES ('001','Changed attribute company_id to organization_id', NOW());