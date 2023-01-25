-------------------------------------
-- UpdateReminderTest.test1 and SendRemindersTest.test1
-------------------------------------
INSERT INTO reminder(action, note, case_id, case_type, external_case_id, case_link, party_id, reminder_date, reminder_id, sent, created, created_by)
VALUES('action', 'Lorem ipsum', 'caseId', 'caseType', 'externalCaseId', 'caseLink', 'fbfbd90c-4c47-11ec-81d3-0242ac130003', '2021-11-25', 'R-fbfbd90c-4c47-11ec-81d3-0242ac130004', False, '2022-01-01 12:14:32.234', 'User Identification');

-------------------------------------
-- ReadRemindersTest.test1 and ReadRemindersTest.test2
-------------------------------------
INSERT INTO reminder(action, note, case_id, case_type, external_case_id, case_link, party_id, reminder_date, reminder_id, sent, created, created_by, modified, modified_by)
VALUES('Förnya tillstånd', 'Lorem ipsum 1', 'caseId', 'caseType', 'externalCaseId', 'caseLink', 'fbfbd90c-4c47-11ec-81d3-0242ac130001', '2021-11-25', 'R-fbfbd90c-4c47-11ec-81d3-0242ac130001', False, '2021-10-15 09:35:11.225', 'Albert Einstein', '2021-10-22 13:48:20.113', 'Isaac Newton'),
      ('Det är julafton', 'Lorem ipsum 2', 'caseId2', 'caseType2', 'externalCaseId2', 'caseLink2', 'fbfbd90c-4c47-11ec-81d3-0242ac130001', '2021-12-24', 'R-fbfbd90c-4c47-11ec-81d3-0242ac130002', False, '2021-12-24 00:00:00.001', 'Jultomten', null, null);

-------------------------------------
-- SendRemindersTest.test1
-------------------------------------
INSERT INTO reminder(action, note, case_id, case_type, external_case_id, case_link, party_id, reminder_date, reminder_id, sent, created, created_by, modified, modified_by)
VALUES('Förbered inför julfest', 'Lorem ipsum 1', 'caseId', 'caseType', 'externalCaseId', 'caseLink', 'fbfbd90c-4c47-11ec-81d3-0242ac130002', '2021-11-26', 'R-fbfbd90c-4c47-11ec-81d3-0242ac130003', False, '2021-10-15 09:35:11.332', 'Creator of reminder', null, null),
      ('Julfest', 'Lorem ipsum 2', 'caseId2', 'caseType2', 'externalCaseId2', 'caseLink2', 'fbfbd90c-4c47-11ec-81d3-0242ac130002', '2021-11-25', 'R-fbfbd90c-4c47-11ec-81d3-0242ac130005', False, '2021-10-15 09:35:11.333', 'Creator of reminder', null, null);

-------------------------------------
-- DeleteReminderTest.test1
-------------------------------------
INSERT INTO reminder(action, note, case_id, case_link, party_id, reminder_date, reminder_id, sent)
VALUES('Reminder som ska tas bort', 'Lorem ipsum', 'caseId', 'caseLink', 'fbfbd90c-4c47-11ec-81d3-0242ac130004', '2021-11-26', 'R-fbfbd90c-4c47-11ec-81d3-0242ac130006', False);