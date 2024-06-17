package ca.uhn.fhir.jpa.fql.jdbc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.fql.executor.HfqlDataTypeEnum;
import ca.uhn.fhir.jpa.fql.executor.IHfqlExecutionResult;
import ca.uhn.fhir.jpa.fql.executor.IHfqlExecutor;
import ca.uhn.fhir.jpa.fql.parser.HfqlStatement;
import ca.uhn.fhir.jpa.fql.provider.HfqlRestProvider;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import com.google.common.collect.Lists;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Base64Utils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.jpa.fql.jdbc.HfqlRestClientTest.createFakeStatement;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
@ExtendWith(MockitoExtension.class)
public class JdbcDriverTest {
	public static final String SOME_USERNAME = "some-username";
	public static final String SOME_PASSWORD = "some-password";
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final HfqlRestClientTest.HeaderCaptureInterceptor ourHeaderCaptureInterceptor = new HfqlRestClientTest.HeaderCaptureInterceptor();
	@Mock
	private IHfqlExecutor myFqlExecutor;
	@Mock
	private IHfqlExecutionResult myMockFqlResult;
	@InjectMocks
	private HfqlRestProvider myProvider = new HfqlRestProvider();
	@RegisterExtension
	public RestfulServerExtension myServer = new RestfulServerExtension(ourCtx)
		.registerProvider(myProvider)
		.registerInterceptor(ourHeaderCaptureInterceptor);

	private BasicDataSource myDs;

	@BeforeEach
	public void beforeEach() throws SQLException {
		JdbcDriver.load();

		myDs = new BasicDataSource();
		myDs.setUrl(JdbcDriver.URL_PREFIX + myServer.getBaseUrl());
		myDs.setUsername(SOME_USERNAME);
		myDs.setPassword(SOME_PASSWORD);
		myDs.start();

		ourHeaderCaptureInterceptor.clear();
	}

	@AfterEach
	public void afterEach() throws SQLException {
		myDs.close();

		JdbcDriver.unload();
	}

	@Test
	public void testExecuteStatement() {
		HfqlStatement statement = createFakeStatement();
		when(myFqlExecutor.executeInitialSearch(any(), any(), any())).thenReturn(myMockFqlResult);
		when(myMockFqlResult.getStatement()).thenReturn(statement);
		when(myMockFqlResult.hasNext()).thenReturn(true, true, false);
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IHfqlExecutionResult.Row(0, List.of("Simpson", "Homer")),
			new IHfqlExecutionResult.Row(3, List.of("Simpson", "Marge"))
		);
		when(myMockFqlResult.getSearchId()).thenReturn("my-search-id");
		when(myMockFqlResult.getLimit()).thenReturn(999);

		String input = """
				from Patient
				select name.family, name.given
			""";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(myDs);
		List<Map<String, Object>> outcome = jdbcTemplate.query(input, new ColumnMapRowMapper());
		assertThat(outcome).hasSize(2);

		String expectedAuthHeader = Constants.HEADER_AUTHORIZATION_VALPREFIX_BASIC + Base64Utils.encodeToString((SOME_USERNAME + ":" + SOME_PASSWORD).getBytes(StandardCharsets.UTF_8));
		String actual = ourHeaderCaptureInterceptor.getCapturedHeaders().get(0).get(Constants.HEADER_AUTHORIZATION).get(0);
		assertEquals(expectedAuthHeader, actual);
	}

	@Test
	public void testExecuteStatement_ReturnsError() {
		String errorMessage = "this is an error!";

		HfqlStatement statement = createFakeStatement();
		when(myFqlExecutor.executeInitialSearch(any(), any(), any())).thenReturn(myMockFqlResult);
		when(myMockFqlResult.getStatement()).thenReturn(statement);
		when(myMockFqlResult.hasNext()).thenReturn(true, false);
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IHfqlExecutionResult.Row(IHfqlExecutionResult.ROW_OFFSET_ERROR, List.of(errorMessage))
		);
		when(myMockFqlResult.getSearchId()).thenReturn("my-search-id");
		when(myMockFqlResult.getLimit()).thenReturn(999);

		String input = """
				from Patient
				select name.family, name.given
			""";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(myDs);

		try {
			jdbcTemplate.query(input, new ColumnMapRowMapper());
			fail();
		} catch (UncategorizedSQLException e) {
			assertEquals(SQLException.class, e.getCause().getClass());
			assertEquals(Msg.code(2395) + "this is an error!", e.getCause().getMessage());
		}
	}

	@Test
	public void testDataTypes() throws SQLException {
		// Setup
		HfqlStatement hfqlStatement = new HfqlStatement();
		hfqlStatement.setFromResourceName("Patient");
		hfqlStatement.addSelectClauseAndAlias("col.string").setDataType(HfqlDataTypeEnum.STRING);
		hfqlStatement.addSelectClauseAndAlias("col.date").setDataType(HfqlDataTypeEnum.DATE);
		hfqlStatement.addSelectClauseAndAlias("col.boolean").setDataType(HfqlDataTypeEnum.BOOLEAN);
		hfqlStatement.addSelectClauseAndAlias("col.time").setDataType(HfqlDataTypeEnum.TIME);
		hfqlStatement.addSelectClauseAndAlias("col.decimal").setDataType(HfqlDataTypeEnum.DECIMAL);
		hfqlStatement.addSelectClauseAndAlias("col.integer").setDataType(HfqlDataTypeEnum.INTEGER);
		hfqlStatement.addSelectClauseAndAlias("col.longint").setDataType(HfqlDataTypeEnum.LONGINT);
		hfqlStatement.addSelectClauseAndAlias("col.timestamp").setDataType(HfqlDataTypeEnum.TIMESTAMP);
		when(myMockFqlResult.getStatement()).thenReturn(hfqlStatement);

		when(myFqlExecutor.executeInitialSearch(any(), any(), any())).thenReturn(myMockFqlResult);
		when(myMockFqlResult.hasNext()).thenReturn(true, false);
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IHfqlExecutionResult.Row(0, List.of("a-string", "2023-02-02", "true", "12:23:22", "100.123", "123", "987", "2023-02-12T10:01:02.234Z"))
		);
		when(myMockFqlResult.getSearchId()).thenReturn("my-search-id");
		when(myMockFqlResult.getLimit()).thenReturn(999);

		String input = """
				select col.string, col.date, col.boolean, col.time, col.decimal, col.integer, col.longint, col.timestamp
				from Patient
			""";

		// Test
		Connection connection = myDs.getConnection();
		Statement statement = connection.createStatement();
		assertTrue(statement.execute(input));
		ResultSet resultSet = statement.getResultSet();

		// Verify
		assertTrue(resultSet.next());
		assertEquals("a-string", resultSet.getString("col.string"));
		assertEquals(new DateType("2023-02-02").getValue(), resultSet.getDate("col.date"));
		assertEquals(true, resultSet.getBoolean("col.boolean"));
		assertEquals("12:23:22", resultSet.getTime("col.time").toString());
		assertEquals(new BigDecimal("100.123"), resultSet.getBigDecimal("col.decimal"));
		assertEquals(new BigDecimal("100.123"), resultSet.getBigDecimal("col.decimal", 100));
		assertEquals(100.123f, resultSet.getFloat("col.decimal"));
		assertEquals(100.123d, resultSet.getDouble("col.decimal"));
		assertEquals(123, resultSet.getInt("col.integer"));
		assertEquals(987L, resultSet.getLong("col.longint"));
		assertEquals(new Timestamp(new DateTimeType("2023-02-12T10:01:02.234Z").getValue().getTime()), resultSet.getTimestamp("col.timestamp"));

		// Using getObject
		assertEquals("a-string", resultSet.getObject("col.string"));
		assertEquals(new DateType("2023-02-02").getValue(), resultSet.getObject("col.date"));
		assertEquals(true, resultSet.getObject("col.boolean"));
		assertEquals("12:23:22", resultSet.getObject("col.time").toString());
		assertEquals(new BigDecimal("100.123"), resultSet.getObject("col.decimal"));
		assertEquals(123, resultSet.getObject("col.integer"));
		assertEquals(987L, resultSet.getObject("col.longint"));
		assertEquals(new Timestamp(new DateTimeType("2023-02-12T10:01:02.234Z").getValue().getTime()), resultSet.getObject("col.timestamp"));

		assertThatExceptionOfType(SQLException.class).isThrownBy(() -> resultSet.getString(0));
		assertThatExceptionOfType(SQLException.class).isThrownBy(() -> resultSet.getString(999));
		assertThatExceptionOfType(SQLException.class).isThrownBy(() -> resultSet.getString("foo"));
	}

	@Test
	public void testDatatypes_TimestampPrecision() throws SQLException {
		// Setup
		when(myFqlExecutor.executeInitialSearch(any(), any(), any())).thenReturn(myMockFqlResult);
		HfqlStatement fakeStatement = createFakeStatement();
		fakeStatement.getSelectClauses().clear();
		fakeStatement.addSelectClause("col.time").setAlias("col.time").setDataType(HfqlDataTypeEnum.TIME);
		when(myMockFqlResult.getStatement()).thenReturn(fakeStatement);
		when(myMockFqlResult.hasNext()).thenReturn(true, true, true, true, true, false);
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IHfqlExecutionResult.Row(0, List.of("12:23")),
			new IHfqlExecutionResult.Row(1, List.of("12:23:10")),
			new IHfqlExecutionResult.Row(2, List.of("12:23:11.0")),
			new IHfqlExecutionResult.Row(3, List.of("12:23:12.12")),
			new IHfqlExecutionResult.Row(4, List.of("12:23:13.123"))
		);
		when(myMockFqlResult.getSearchId()).thenReturn("my-search-id");
		when(myMockFqlResult.getLimit()).thenReturn(999);

		String input = "select col.time from Patient";

		// Test
		Connection connection = myDs.getConnection();
		Statement statement = connection.createStatement();
		assertTrue(statement.execute(input));
		ResultSet resultSet = statement.getResultSet();

		// Verify
		assertTrue(resultSet.next());
		assertEquals("12:23:00", resultSet.getTime("col.time").toString());
		assertTrue(resultSet.next());
		assertEquals("12:23:10", resultSet.getTime("col.time").toString());
		assertTrue(resultSet.next());
		assertEquals("12:23:11", resultSet.getTime("col.time").toString());
		assertTrue(resultSet.next());
		assertEquals("12:23:12", resultSet.getTime("col.time").toString());
		assertTrue(resultSet.next());
		assertEquals("12:23:13", resultSet.getTime("col.time").toString());
		assertFalse(resultSet.next());

		verify(myFqlExecutor, times(1)).executeInitialSearch(any(), any(), any());
		verify(myFqlExecutor, times(0)).executeContinuation(any(), any(), anyInt(), any(), any());
	}


	@Test
	public void testIntrospectTables() throws SQLException {
		when(myFqlExecutor.introspectTables()).thenReturn(myMockFqlResult);
		HfqlStatement statement = new HfqlStatement();
		statement.addSelectClause("TABLE_NAME").setAlias("TABLE_NAME").setDataType(HfqlDataTypeEnum.STRING);
		when(myMockFqlResult.getStatement()).thenReturn(statement);
		when(myMockFqlResult.hasNext()).thenReturn(true, false);
		when(myMockFqlResult.getNextRow()).thenReturn(new IHfqlExecutionResult.Row(0, List.of("Account")));

		Connection connection = myDs.getConnection();
		DatabaseMetaData metadata = connection.getMetaData();
		ResultSet tables = metadata.getTables(null, null, null, null);
		assertTrue(tables.isBeforeFirst());
		assertTrue(tables.next());
		assertFalse(tables.isBeforeFirst());
		assertEquals("Account", tables.getString(1));
		assertEquals("Account", tables.getString("TABLE_NAME"));
	}


	@Test
	public void testIntrospectColumns() throws SQLException {
		when(myFqlExecutor.introspectColumns(any(), any())).thenReturn(myMockFqlResult);
		HfqlStatement statement = new HfqlStatement();
		statement.addSelectClauseAndAlias("COLUMN_NAME").setDataType(HfqlDataTypeEnum.STRING);
		statement.addSelectClauseAndAlias("DATA_TYPE").setDataType(HfqlDataTypeEnum.INTEGER);
		when(myMockFqlResult.getStatement()).thenReturn(statement);
		when(myMockFqlResult.hasNext()).thenReturn(true, true, false);
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IHfqlExecutionResult.Row(0, Lists.newArrayList("foo", Types.VARCHAR)),
			new IHfqlExecutionResult.Row(1, Lists.newArrayList("bar", null))
		);

		Connection connection = myDs.getConnection();
		DatabaseMetaData metadata = connection.getMetaData();
		ResultSet tables = metadata.getColumns(null, null, null, null);

		// Row 1
		assertTrue(tables.next());
		assertEquals("foo", tables.getString(1));
		assertEquals("foo", tables.getString("COLUMN_NAME"));
		assertFalse(tables.wasNull());
		assertEquals(Types.VARCHAR, tables.getInt(2));
		assertEquals(Types.VARCHAR, tables.getInt("DATA_TYPE"));
		assertFalse(tables.wasNull());
		// Row 2
		assertTrue(tables.next());
		assertEquals("bar", tables.getString(1));
		assertEquals("bar", tables.getString("COLUMN_NAME"));
		assertEquals(0, tables.getInt(2));
		assertEquals(0, tables.getInt("DATA_TYPE"));
		assertTrue(tables.wasNull());
		// No more rows
		assertFalse(tables.next());
		// Invalid columns
		assertThatExceptionOfType(SQLException.class).isThrownBy(() -> tables.getString(0));
		assertThatExceptionOfType(SQLException.class).isThrownBy(() -> tables.getString(999));
		assertThatExceptionOfType(SQLException.class).isThrownBy(() -> tables.getString("foo"));

	}

	@Test
	public void testMetadata_ImportedAndExportedKeys() throws SQLException {
		Connection connection = myDs.getConnection();
		DatabaseMetaData metadata = connection.getMetaData();

		assertFalse(metadata.getImportedKeys(null, null, null).next());
		assertFalse(metadata.getExportedKeys(null, null, null).next());
	}


}
