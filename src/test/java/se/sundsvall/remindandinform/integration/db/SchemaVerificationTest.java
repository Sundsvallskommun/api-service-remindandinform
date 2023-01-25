package se.sundsvall.remindandinform.integration.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("junit")
class SchemaVerificationTest {

	private static final String STORED_SCHEMA_FILE = "db/schema.sql";

	@Value("${spring.jpa.properties.javax.persistence.schema-generation.scripts.create-target:}")
	private String generatedSchemaFile;

	@Test
	void verifySchemaUpdates() {
		
		final var storedSchema = getResourceFile(STORED_SCHEMA_FILE);
		final var generatedSchema = getFile(generatedSchemaFile);

		assertThat(generatedSchema)
			.as(String.format("Please reflect modifications to entities in file: %s", STORED_SCHEMA_FILE))
			.hasSameTextualContentAs(storedSchema);
	}

	private File getResourceFile(String fileName) {
		return new File(getClass().getClassLoader().getResource(fileName).getFile());
	}
	
	private File getFile(String fileName) {
		return new File(fileName);
	}
}
