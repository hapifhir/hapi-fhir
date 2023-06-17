package ca.uhn.fhir.jpa.fql.jdbc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.fql.executor.FqlDataTypeEnum;
import ca.uhn.fhir.jpa.fql.executor.IFqlExecutionResult;
import ca.uhn.fhir.jpa.fql.executor.IFqlExecutor;
import ca.uhn.fhir.jpa.fql.executor.StaticFqlExecutionResult;
import ca.uhn.fhir.jpa.fql.parser.FqlStatement;
import ca.uhn.fhir.jpa.fql.provider.FqlRestProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import com.google.common.collect.Lists;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.jpa.fql.jdbc.FqlRestClientTest.createFakeStatement;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SuppressWarnings("SqlDialectInspection")
@ExtendWith(MockitoExtension.class)
public class FqlDriverTest {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@Mock
	private IFqlExecutor myFqlExecutor;
	@Mock
	private IFqlExecutionResult myMockFqlResult;
	@InjectMocks
	private FqlRestProvider myProvider = new FqlRestProvider();
	@RegisterExtension
	public RestfulServerExtension myServer = new RestfulServerExtension(ourCtx)
		.registerProvider(myProvider);

	private BasicDataSource myDs;

	@BeforeEach
	public void beforeEach() throws SQLException {
		FqlDriver.load();

		myDs = new BasicDataSource();
		myDs.setUrl(FqlDriver.URL_PREFIX + myServer.getBaseUrl());
		myDs.setUsername("some-username");
		myDs.setPassword("some-password");
		myDs.start();
	}

	@AfterEach
	public void afterEach() throws SQLException {
		myDs.close();
	}

	@Test
	public void testExecuteStatement() {
		FqlStatement statement = createFakeStatement();
		when(myFqlExecutor.executeInitialSearch(any(), any(), any())).thenReturn(myMockFqlResult);
		when(myFqlExecutor.executeContinuation(any(), any(), anyInt(), any(), any())).thenReturn(new StaticFqlExecutionResult("123"));
		when(myMockFqlResult.getStatement()).thenReturn(statement);
		when(myMockFqlResult.getColumnNames()).thenReturn(List.of("name.family", "name.given"));
		when(myMockFqlResult.getColumnTypes()).thenReturn(List.of(FqlDataTypeEnum.STRING, FqlDataTypeEnum.STRING));
		when(myMockFqlResult.hasNext()).thenReturn(true, true, false);
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IFqlExecutionResult.Row(0, List.of("Simpson", "Homer")),
			new IFqlExecutionResult.Row(3, List.of("Simpson", "Marge"))
		);
		when(myMockFqlResult.getSearchId()).thenReturn("my-search-id");
		when(myMockFqlResult.getLimit()).thenReturn(999);

		String input = """
				from Patient
				select name.family, name.given
			""";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(myDs);
		List<Map<String, Object>> outcome = jdbcTemplate.query(input, new ColumnMapRowMapper());
		assertEquals(2, outcome.size());
	}


	@Test
	public void testIntrospectTables() throws SQLException {
		when(myFqlExecutor.introspectTables()).thenReturn(myMockFqlResult);
		when(myMockFqlResult.getColumnNames()).thenReturn(List.of("TABLE_NAME"));
		when(myMockFqlResult.getColumnTypes()).thenReturn(List.of(FqlDataTypeEnum.STRING));
		when(myMockFqlResult.hasNext()).thenReturn(true, false);
		when(myMockFqlResult.getNextRow()).thenReturn(new IFqlExecutionResult.Row(0, List.of("Account")));

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
		when(myMockFqlResult.getColumnNames()).thenReturn(List.of("COLUMN_NAME", "DATA_TYPE"));
		when(myMockFqlResult.getColumnTypes()).thenReturn(List.of(FqlDataTypeEnum.STRING, FqlDataTypeEnum.INTEGER));
		when(myMockFqlResult.hasNext()).thenReturn(true, true, false);
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IFqlExecutionResult.Row(0, Lists.newArrayList("foo", Types.VARCHAR)),
			new IFqlExecutionResult.Row(1, Lists.newArrayList("bar", null))
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
		assertThrows(SQLException.class, () -> tables.getString(0));
		assertThrows(SQLException.class, () -> tables.getString(999));
		assertThrows(SQLException.class, () -> tables.getString("foo"));

	}

	@Test
	public void testMetadata_ImportedAndExportedKeys() throws SQLException {
		Connection connection = myDs.getConnection();
		DatabaseMetaData metadata = connection.getMetaData();

		assertFalse(metadata.getImportedKeys(null, null, null).next());
		assertFalse(metadata.getExportedKeys(null, null, null).next());
	}


}
