package ca.uhn.fhir.jpa.fql.jdbc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.fql.executor.IFqlExecutor;
import ca.uhn.fhir.jpa.fql.executor.IFqlResult;
import ca.uhn.fhir.jpa.fql.parser.FqlStatement;
import ca.uhn.fhir.jpa.fql.provider.FqlRestProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
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

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.jpa.fql.jdbc.FqlRestClientTest.createFakeStatement;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
	private IFqlResult myMockFqlResult;
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
		when(myFqlExecutor.executeContinuation(any(), any(), anyInt(), any(), any())).thenReturn(new EmptyFqlResult("123"));
		when(myMockFqlResult.getStatement()).thenReturn(statement);
		when(myMockFqlResult.getColumnNames()).thenReturn(List.of("name.family", "name.given"));
		when(myMockFqlResult.hasNext()).thenReturn(true, true, false);
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IFqlResult.Row(0, List.of("Simpson", "Homer")),
			new IFqlResult.Row(3, List.of("Simpson", "Marge"))
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


}
