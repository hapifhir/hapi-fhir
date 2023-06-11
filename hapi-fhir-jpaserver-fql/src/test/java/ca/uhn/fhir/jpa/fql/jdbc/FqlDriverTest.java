package ca.uhn.fhir.jpa.fql.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SqlDialectInspection")
public class FqlDriverTest {

	private BasicDataSource myDs;

	@BeforeEach
	public void beforeEach() throws SQLException {
		FqlDriver.load();

		myDs = new BasicDataSource();
		myDs.setUrl(FqlDriver.URL_PREFIX + "http://localhost:1234");
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
		String input = """
				from Patient
				select name.family, name.given
			""";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(myDs);
		List<Map<String, Object>> outcome = jdbcTemplate.query(input, new ColumnMapRowMapper());
		assertEquals(2, outcome.size());
	}


}
