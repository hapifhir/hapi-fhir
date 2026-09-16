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

	/**
	 * The compatibility level probe runs lazily on the search path, so a failure there must never escape
	 * into the search. It degrades to "not supported" and reports the failure exactly once, no matter how
	 * many times it is retried. A failed probe is not a definitive answer, so it is not cached outright -
	 * the next call re-probes - but three consecutive failures give up and cache "false", so a permanently
	 * unreadable <code>sys.databases</code> costs three extra connection attempts rather than one per
	 * search forever. Once the answer settles on "false", {@link HibernatePropertiesProvider#isJsonUnpackingSupported()}
	 * also reports its own once-per-provider fallback warning, so two distinct warnings accumulate over the
	 * life of this test - the probe failure, and the JSON-binding fallback - each logged only once.
	 */
	@Test
	void isJsonUnpackingSupported_whenProbeFails_retriesUpToLimitThenCachesFalse() throws SQLException {
		stubConnection();
		lenient().when(myStatement.executeQuery(anyString())).thenThrow(new SQLException("SELECT permission denied on object 'databases'"));

		assertThat(mySvc.isJsonUnpackingSupported()).isFalse();
		verify(myDataSource, times(1)).getConnection();

		assertThat(mySvc.isJsonUnpackingSupported()).isFalse();
		verify(myDataSource, times(2)).getConnection();

		assertThat(mySvc.isJsonUnpackingSupported()).isFalse();
		verify(myDataSource, times(3)).getConnection();

		// Three consecutive failures is the limit - the fourth call must not touch the DataSource again.
		assertThat(mySvc.isJsonUnpackingSupported()).isFalse();
		verify(myDataSource, times(3)).getConnection();

		assertThat(compatibilityLevelWarnings())
			.as("The failed probe and the JSON-binding fallback must each be reported exactly once, no matter how many times they are retried")
			.hasSize(2);
	}

	/**
	 * An empty result set - the query ran but <code>sys.databases</code> had no row for this database -
	 * is just as much a failed probe as an exception, and gets the same treatment: not cached outright, so
	 * the next call re-probes.
	 */
	@Test
	void isLargeIdListJsonBindingSupported_whenResultSetIsEmpty_isTreatedAsFailedProbe() throws SQLException {
		stubConnection();
		when(myStatement.executeQuery(anyString())).thenReturn(myResultSet);
		when(myResultSet.next()).thenReturn(false);

		assertThat(mySvc.isJsonUnpackingSupported()).isFalse();
		verify(myDataSource, times(1)).getConnection();

		assertThat(mySvc.isJsonUnpackingSupported()).isFalse();
		verify(myDataSource, times(2)).getConnection();

		assertThat(compatibilityLevelWarnings())
			.as("The failed probe and the JSON-binding fallback must each be reported exactly once")
			.hasSize(2);
	}

	/**
	 * A database at compatibility level 130 or higher supports OPENJSON, and the probe result is cached so
	 * later searches do not touch the DataSource again.
	 */
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
