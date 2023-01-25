ALTER TABLE reminder CHANGE COLUMN IF EXISTS person_id party_id varchar(255);
ALTER TABLE reminder DROP COLUMN IF EXISTS organization_id;

-- Necessary line in order to document the change.
INSERT INTO reminder.schema_history (schema_version,comment,applied) VALUES ('004','Rename person_id to party_id and drop organization_id column', NOW());