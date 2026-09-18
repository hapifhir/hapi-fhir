package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import ca.uhn.test.util.LogbackTestExtension;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.hibernate.dialect.SQLServerDialect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

// Created by claude-opus-5
@ExtendWith(MockitoExtension.class)
public class HibernatePropertiesProviderTest {

	@RegisterExtension
	public LogbackTestExtension myLogCapture = new LogbackTestExtension(Level.WARN);

	@Mock
	private LocalContainerEntityManagerFactoryBean myEntityManagerFactory;

	@Mock
	private DataSource myDataSource;

	@Mock
	private Connection myConnection;

	@Mock
	private Statement myStatement;

	@Mock
	private ResultSet myResultSet;

	private HibernatePropertiesProvider mySvc;

	@BeforeEach
	void beforeEach() {
		mySvc = new HibernatePropertiesProvider();
		mySvc.setDialectForUnitTest(new SQLServerDialect());
		ReflectionTestUtils.setField(mySvc, "myEntityManagerFactory", myEntityManagerFactory);
		lenient().when(myEntityManagerFactory.getDataSource()).thenReturn(myDataSource);
	}

	@Test
	void isJsonUnpackingSupported_whenCompatibilityLevelBelow130_returnsFalseAndWarnsOnce() throws SQLException {
		stubConnection();
		when(myStatement.executeQuery(anyString())).thenReturn(myResultSet);
		when(myResultSet.next()).thenReturn(true);
		when(myResultSet.getInt(1)).thenReturn(120);

		// Minimum level is 130
		assertThat(mySvc.isJsonUnpackingSupported()).isFalse();
		verify(myDataSource, times(1)).getConnection();
		assertThat(compatibilityLevelWarnings()).hasSize(1);

		// The result should be cached
		assertThat(mySvc.isJsonUnpackingSupported()).isFalse();
		verify(myDataSource, times(1)).getConnection();
		assertThat(compatibilityLevelWarnings()).hasSize(1);
	}

	@Test
	void isJsonUnpackingSupported_whenProbeEncountersError_givesUpAfterThreeAttemptsAndWarnsOnce() throws SQLException {
		stubConnection();
		lenient().when(myStatement.executeQuery(anyString())).thenThrow(new SQLException("SELECT permission denied on object 'databases'"));

		assertThat(mySvc.isJsonUnpackingSupported()).isFalse();
		verify(myDataSource, times(1)).getConnection();
		assertThat(mySvc.isJsonUnpackingSupported()).isFalse();
		verify(myDataSource, times(2)).getConnection();
		assertThat(compatibilityLevelWarnings()).isEmpty();

		assertThat(mySvc.isJsonUnpackingSupported()).isFalse();
		verify(myDataSource, times(3)).getConnection();
		assertThat(compatibilityLevelWarnings())
			.as("The third consecutive failure gives up and warns exactly once")
			.hasSize(1);

		// After 3 failures, unsupported is cached
		assertThat(mySvc.isJsonUnpackingSupported()).isFalse();
		verify(myDataSource, times(3)).getConnection();
		assertThat(compatibilityLevelWarnings())
			.as("A cached outcome must not warn again")
			.hasSize(1);
	}

	@Test
	void isLargeIdListJsonBindingSupported_whenProbeReturnsEmpty_givesUpAfterThreeAttemptsAndWarnsOnce() throws SQLException {
		stubConnection();
		when(myStatement.executeQuery(anyString())).thenReturn(myResultSet);
		when(myResultSet.next()).thenReturn(false);

		assertThat(mySvc.isJsonUnpackingSupported()).isFalse();
		verify(myDataSource, times(1)).getConnection();
		assertThat(mySvc.isJsonUnpackingSupported()).isFalse();
		verify(myDataSource, times(2)).getConnection();
		assertThat(compatibilityLevelWarnings()).isEmpty();

		assertThat(mySvc.isJsonUnpackingSupported()).isFalse();
		verify(myDataSource, times(3)).getConnection();
		assertThat(compatibilityLevelWarnings())
			.as("The third consecutive failure gives up and warns exactly once")
			.hasSize(1);
	}


	@Test
	void isLargeIdListJsonBindingSupported_whenProbeSucceeds_returnsTrueAndIsCached() throws SQLException {
		stubConnection();
		when(myStatement.executeQuery(anyString())).thenReturn(myResultSet);
		when(myResultSet.next()).thenReturn(true);
		when(myResultSet.getInt(1)).thenReturn(150);

		assertThat(mySvc.isJsonUnpackingSupported()).isTrue();
		assertThat(mySvc.isJsonUnpackingSupported()).isTrue();

		verify(myDataSource, times(1)).getConnection();
		assertThat(compatibilityLevelWarnings()).isEmpty();
	}

	private void stubConnection() throws SQLException {
		lenient().when(myDataSource.getConnection()).thenReturn(myConnection);
		lenient().when(myConnection.createStatement()).thenReturn(myStatement);
	}

	private List<ILoggingEvent> compatibilityLevelWarnings() {
		return myLogCapture.getLogEvents().stream()
			.filter(t -> t.getLevel() == Level.WARN)
			.filter(t -> t.getFormattedMessage().toLowerCase(Locale.ROOT).contains("compatibility level"))
			.toList();
	}
}
