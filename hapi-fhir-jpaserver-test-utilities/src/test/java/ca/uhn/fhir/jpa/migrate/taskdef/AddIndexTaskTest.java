package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.test.util.LogbackTestExtension;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddIndexTaskTest {
	@Mock
	DriverTypeEnum.ConnectionProperties myConnectionProperties;
	@Mock
	DataSource myDataSource;
	@Mock
	TransactionTemplate myTransactionTemplate;

	@RegisterExtension
	LogbackTestExtension myLogCapture = new LogbackTestExtension((Logger) AddIndexTask.ourLog, Level.WARN);


	@Test
	void testOracleException() throws SQLException {
		final AddIndexTask task = new AddIndexTask("1", "1");
		task.setColumns(Collections.singletonList("COLUMN_NAME"));
		task.setUnique(true);
		task.setIndexName("INDEX_NAME");
		task.setConnectionProperties(myConnectionProperties);

		when(myConnectionProperties.getDataSource()).thenReturn(myDataSource);
		when(myConnectionProperties.getTxTemplate()).thenReturn(myTransactionTemplate);

		final String sql = "create index INDEX_NAME on TABLE_NAME (COLUMN_NAME)";
		when(myTransactionTemplate.execute(any()))
			.thenReturn(Collections.emptySet())
			.thenThrow(new UncategorizedSQLException("ORA-01408: such column list already indexed", sql, new SQLException("ORA-01408: such column list already indexed", "72000", 1408)));

		myLogCapture.clearEvents();

		// Red-green: this used to throw an exception.  Now it logs a warning.
		task.execute();

		List<ILoggingEvent> events = myLogCapture.getLogEvents();
		assertThat(events).hasSize(1);
		LoggingEvent event = (LoggingEvent) events.get(0);
		assertThat(event.getFormattedMessage()).contains("ORA-01408: such column list already indexed");
	}
}
