package se.sundsvall.remindandinform.configuration;

import javax.sql.DataSource;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "PT2M")
public class ShedLockConfiguration {

	@Bean
	public LockProvider lockProvider(final DataSource dataSource) {
		return new JdbcTemplateLockProvider(
			JdbcTemplateLockProvider.Configuration.builder()
				.usingDbTime()
				.withJdbcTemplate(new JdbcTemplate(dataSource))
				.build());
	}

}
