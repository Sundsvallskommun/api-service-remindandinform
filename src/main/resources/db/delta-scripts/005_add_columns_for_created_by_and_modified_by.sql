ALTER TABLE reminder CHANGE COLUMN IF EXISTS updated modified datetime(6);
ALTER TABLE reminder ADD COLUMN IF NOT EXISTS created_by varchar(255);
ALTER TABLE reminder ADD COLUMN IF NOT EXISTS modified_by varchar(255);

-- Necessary line in order to document the change.
INSERT INTO reminder.schema_history (schema_version,comment,applied) VALUES ('005','Renamed column updated to modified and added the columns created_by and modified_by', NOW());