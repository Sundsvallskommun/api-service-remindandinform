ALTER TABLE reminder ADD COLUMN IF NOT EXISTS external_case_id varchar(255);
ALTER TABLE reminder ADD COLUMN IF NOT EXISTS case_type varchar(255);

-- Necessary line in order to document the change.
INSERT INTO reminder.schema_history (schema_version,comment,applied) VALUES ('003','Add external_case_id and case_type columns', NOW());