package se.sundsvall.remindandinform.service.mapper.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sundsvall.remindandinform.Application;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("junit")
class ReminderMessagePropertiesTest {
    @Autowired
    private ReminderMessageProperties reminderMessageProperties;

     @Test
     void testProperties() { //NOSONAR
         assert(reminderMessageProperties.getMessage()).equals("TestSmsMessageÅÄÖåäö");
         assert(reminderMessageProperties.getEmailMessage()).equals("TestEmailMessageÅÄÖåäö");
         assert(reminderMessageProperties.getSubject()).equals("TestSubjectÅÄÖåäö");
         assert(reminderMessageProperties.getSenderEmailAddress()).equals("TestEmailAddressÅÄÖåäö");
         assert(reminderMessageProperties.getSenderEmailName()).equals("TestEmailNameÅÄÖåäö");
         assert(reminderMessageProperties.getSenderSmsName()).equals("TestSmsNameÅÄÖåäö");
     }
}

