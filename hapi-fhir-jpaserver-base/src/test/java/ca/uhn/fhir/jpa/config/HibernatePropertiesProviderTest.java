package ca.uhn.fhir.jpa.config;

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

	/**
	 * GL-9268: the compatibility level probe runs lazily on the search path, so a failure there must never
	 * escape into the search. It degrades to "not supported", reports the failure exactly once, and the
	 * probe result is cached - including when the probe failed - so a failing probe costs one connection
	 * rather than one per search.
	 */
	@Test
	void isSqlServerJsonSupported_whenProbeFails_returnsFalseWarnsOnceAndIsCached() throws SQLException {
		stubConnection();
		lenient().when(myStatement.executeQuery(anyString())).thenThrow(new SQLException("SELECT permission denied on object 'databases'"));

		assertThat(mySvc.isSqlServerJsonSupported()).isFalse();
		assertThat(mySvc.isSqlServerJsonSupported()).isFalse();

		verify(myDataSource, times(1)).getConnection();
		assertThat(compatibilityLevelWarnings()).as("The failed probe must be reported exactly once").hasSize(1);
	}

	/**
	 * GL-9268: a database at compatibility level 130 or higher supports OPENJSON, and the probe result is
	 * cached so later searches do not touch the DataSource again.
	 */
	@Test
	void isSqlServerJsonSupported_whenProbeSucceeds_returnsTrueAndIsCached() throws SQLException {
		stubConnection();
		lenient().when(myStatement.executeQuery(anyString())).thenReturn(myResultSet);
		lenient().when(myResultSet.next()).thenReturn(true);
		lenient().when(myResultSet.getInt(1)).thenReturn(150);
		lenient().when(myResultSet.getInt("compatibility_level")).thenReturn(150);

		assertThat(mySvc.isSqlServerJsonSupported()).isTrue();
		assertThat(mySvc.isSqlServerJsonSupported()).isTrue();

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
