ALTER TABLE reminder ADD COLUMN IF NOT EXISTS note longtext;

-- Necessary line in order to document the change.
INSERT INTO reminder.schema_history (schema_version,comment,applied) VALUES ('002','Add note column', NOW());