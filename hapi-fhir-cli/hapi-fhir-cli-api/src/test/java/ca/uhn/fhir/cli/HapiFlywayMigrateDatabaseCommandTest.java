package ca.uhn.fhir.cli;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import com.google.common.base.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.junit.Assert.*;

public class HapiFlywayMigrateDatabaseCommandTest {

	private static final Logger ourLog = LoggerFactory.getLogger(HapiFlywayMigrateDatabaseCommandTest.class);
	public static final String DB_DIRECTORY = "target/h2_test";

	static {
		System.setProperty("test", "true");
	}

	// TODO INTERMITTENT This just failed for me on CI with a BadSqlGrammarException
	@Test
	public void testMigrateFrom340() throws IOException, SQLException {

		File location = getLocation("migrator_h2_test_340_current");

		String url = "jdbc:h2:" + location.getAbsolutePath() + ";create=true";
		DriverTypeEnum.ConnectionProperties connectionProperties = DriverTypeEnum.H2_EMBEDDED.newConnectionProperties(url, "", "");

		String initSql = "/persistence_create_h2_340.sql";
		executeSqlStatements(connectionProperties, initSql);

		seedDatabase340(connectionProperties);

		ourLog.info("**********************************************");
		ourLog.info("Done Setup, Starting Migration...");
		ourLog.info("**********************************************");

		String[] args = new String[]{
			BaseFlywayMigrateDatabaseCommand.MIGRATE_DATABASE,
			"-d", "H2_EMBEDDED",
			"-u", url,
			"-n", "",
			"-p", ""
		};
		assertFalse(JdbcUtils.getTableNames(connectionProperties).contains("HFJ_RES_REINDEX_JOB"));
		App.main(args);
		assertTrue(JdbcUtils.getTableNames(connectionProperties).contains("HFJ_RES_REINDEX_JOB"));

		connectionProperties.getTxTemplate().execute(t -> {
			JdbcTemplate jdbcTemplate = connectionProperties.newJdbcTemplate();
			List<Map<String, Object>> values = jdbcTemplate.queryForList("SELECT * FROM hfj_spidx_token");
			assertEquals(1, values.size());
			assertEquals("identifier", values.get(0).get("SP_NAME"));
			assertEquals("12345678", values.get(0).get("SP_VALUE"));
			assertTrue(values.get(0).keySet().contains("HASH_IDENTITY"));
			assertEquals(7001889285610424179L, values.get(0).get("HASH_IDENTITY"));
			return null;
		});
	}

	@Test
	public void testMigrateFrom340_NoFlyway() throws IOException, SQLException {

		File location = getLocation("migrator_h2_test_340_current_noflyway");

		String url = "jdbc:h2:" + location.getAbsolutePath() + ";create=true";
		DriverTypeEnum.ConnectionProperties connectionProperties = DriverTypeEnum.H2_EMBEDDED.newConnectionProperties(url, "", "");

		String initSql = "/persistence_create_h2_340.sql";
		executeSqlStatements(connectionProperties, initSql);

		seedDatabase340(connectionProperties);

		ourLog.info("**********************************************");
		ourLog.info("Done Setup, Starting Migration...");
		ourLog.info("**********************************************");

		String[] args = new String[]{
			BaseFlywayMigrateDatabaseCommand.MIGRATE_DATABASE,
			"-d", "H2_EMBEDDED",
			"-u", url,
			"-n", "",
			"-p", "",
			"--" + BaseFlywayMigrateDatabaseCommand.DONT_USE_FLYWAY
		};
		assertFalse(JdbcUtils.getTableNames(connectionProperties).contains("HFJ_RES_REINDEX_JOB"));
		App.main(args);
		assertTrue(JdbcUtils.getTableNames(connectionProperties).contains("HFJ_RES_REINDEX_JOB"));

		connectionProperties.getTxTemplate().execute(t -> {
			JdbcTemplate jdbcTemplate = connectionProperties.newJdbcTemplate();
			List<Map<String, Object>> values = jdbcTemplate.queryForList("SELECT * FROM hfj_spidx_token");
			assertEquals(1, values.size());
			assertEquals("identifier", values.get(0).get("SP_NAME"));
			assertEquals("12345678", values.get(0).get("SP_VALUE"));
			assertTrue(values.get(0).keySet().contains("HASH_IDENTITY"));
			assertEquals(7001889285610424179L, values.get(0).get("HASH_IDENTITY"));
			return null;
		});
	}

	@Test
	public void testMigrateFromEmptySchema() throws IOException, SQLException {

		File location = getLocation("migrator_h2_test_empty_current");

		String url = "jdbc:h2:" + location.getAbsolutePath() + ";create=true";
		DriverTypeEnum.ConnectionProperties connectionProperties = DriverTypeEnum.H2_EMBEDDED.newConnectionProperties(url, "", "");

		ourLog.info("**********************************************");
		ourLog.info("Starting Migration...");
		ourLog.info("**********************************************");

		String[] args = new String[]{
			BaseFlywayMigrateDatabaseCommand.MIGRATE_DATABASE,
			"-d", "H2_EMBEDDED",
			"-u", url,
			"-n", "",
			"-p", ""
		};

		assertFalse(JdbcUtils.getTableNames(connectionProperties).contains("HFJ_RESOURCE"));
		assertFalse(JdbcUtils.getTableNames(connectionProperties).contains("HFJ_BLK_EXPORT_JOB"));
		App.main(args);
		assertTrue(JdbcUtils.getTableNames(connectionProperties).contains("HFJ_RESOURCE")); // Early table
		assertTrue(JdbcUtils.getTableNames(connectionProperties).contains("HFJ_BLK_EXPORT_JOB")); // Late table
	}

	@Test
	public void testMigrateFromEmptySchema_NoFlyway() throws IOException, SQLException {

		File location = getLocation("migrator_h2_test_empty_current_noflyway");

		String url = "jdbc:h2:" + location.getAbsolutePath() + ";create=true";
		DriverTypeEnum.ConnectionProperties connectionProperties = DriverTypeEnum.H2_EMBEDDED.newConnectionProperties(url, "", "");

		ourLog.info("**********************************************");
		ourLog.info("Starting Migration...");
		ourLog.info("**********************************************");

		String[] args = new String[]{
			BaseFlywayMigrateDatabaseCommand.MIGRATE_DATABASE,
			"-d", "H2_EMBEDDED",
			"-u", url,
			"-n", "",
			"-p", "",
			"--" + BaseFlywayMigrateDatabaseCommand.DONT_USE_FLYWAY
		};

		assertFalse(JdbcUtils.getTableNames(connectionProperties).contains("HFJ_RESOURCE"));
		assertFalse(JdbcUtils.getTableNames(connectionProperties).contains("HFJ_BLK_EXPORT_JOB"));
		App.main(args);
		assertTrue(JdbcUtils.getTableNames(connectionProperties).contains("HFJ_RESOURCE")); // Early table
		assertTrue(JdbcUtils.getTableNames(connectionProperties).contains("HFJ_BLK_EXPORT_JOB")); // Late table
	}

	@NotNull
	private File getLocation(String theDatabaseName) throws IOException {
		File directory = new File(DB_DIRECTORY);
		if (directory.exists()) {
			FileUtils.deleteDirectory(directory);
		}

		return new File(DB_DIRECTORY + "/" + theDatabaseName);
	}

	private void seedDatabase340(DriverTypeEnum.ConnectionProperties theConnectionProperties) {
		theConnectionProperties.getTxTemplate().execute(t -> {
			JdbcTemplate jdbcTemplate = theConnectionProperties.newJdbcTemplate();

			jdbcTemplate.execute(
				"insert into HFJ_RESOURCE (RES_DELETED_AT, RES_VERSION, FORCED_ID_PID, HAS_TAGS, RES_PUBLISHED, RES_UPDATED, SP_HAS_LINKS, HASH_SHA256, SP_INDEX_STATUS, RES_LANGUAGE, SP_CMPSTR_UNIQ_PRESENT, SP_COORDS_PRESENT, SP_DATE_PRESENT, SP_NUMBER_PRESENT, SP_QUANTITY_PRESENT, SP_STRING_PRESENT, SP_TOKEN_PRESENT, SP_URI_PRESENT, RES_PROFILE, RES_TYPE, RES_VER, RES_ID) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new AbstractLobCreatingPreparedStatementCallback(new DefaultLobHandler()) {
					@Override
					protected void setValues(PreparedStatement thePs, LobCreator theLobCreator) throws SQLException {
						thePs.setNull(1, Types.TIMESTAMP);
						thePs.setString(2, "R4");
						thePs.setNull(3, Types.BIGINT);
						thePs.setBoolean(4, false);
						thePs.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
						thePs.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
						thePs.setBoolean(7, false);
						thePs.setNull(8, Types.VARCHAR);
						thePs.setLong(9, 1L);
						thePs.setNull(10, Types.VARCHAR);
						thePs.setBoolean(11, false);
						thePs.setBoolean(12, false);
						thePs.setBoolean(13, false);
						thePs.setBoolean(14, false);
						thePs.setBoolean(15, false);
						thePs.setBoolean(16, false);
						thePs.setBoolean(17, false);
						thePs.setBoolean(18, false);
						thePs.setNull(19, Types.VARCHAR);
						thePs.setString(20, "Patient");
						thePs.setLong(21, 1L);
						thePs.setLong(22, 1L);
					}
				}
			);

			jdbcTemplate.execute(
				"insert into HFJ_RES_VER (RES_DELETED_AT, RES_VERSION, FORCED_ID_PID, HAS_TAGS, RES_PUBLISHED, RES_UPDATED, RES_ENCODING, RES_TEXT, RES_ID, RES_TYPE, RES_VER, PID) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new AbstractLobCreatingPreparedStatementCallback(new DefaultLobHandler()) {
					@Override
					protected void setValues(PreparedStatement thePs, LobCreator theLobCreator) throws SQLException {
						thePs.setNull(1, Types.TIMESTAMP);
						thePs.setString(2, "R4");
						thePs.setNull(3, Types.BIGINT);
						thePs.setBoolean(4, false);
						thePs.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
						thePs.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
						thePs.setString(7, "JSON");
						theLobCreator.setBlobAsBytes(thePs, 8, "{\"resourceType\":\"Patient\"}".getBytes(Charsets.US_ASCII));
						thePs.setLong(9, 1L);
						thePs.setString(10, "Patient");
						thePs.setLong(11, 1L);
						thePs.setLong(12, 1L);
					}
				}
			);

			jdbcTemplate.execute(
				"insert into HFJ_SPIDX_STRING (SP_MISSING, SP_NAME, RES_ID, RES_TYPE, SP_UPDATED, SP_VALUE_EXACT, SP_VALUE_NORMALIZED, SP_ID) values (?, ?, ?, ?, ?, ?, ?, ?)",
				new AbstractLobCreatingPreparedStatementCallback(new DefaultLobHandler()) {
					@Override
					protected void setValues(PreparedStatement thePs, LobCreator theLobCreator) throws SQLException {
						thePs.setBoolean(1, false);
						thePs.setString(2, "given");
						thePs.setLong(3, 1L); // res-id
						thePs.setString(4, "Patient");
						thePs.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
						thePs.setString(6, "ROBERT");
						thePs.setString(7, "Robert");
						thePs.setLong(8, 1L);
					}
				}
			);

			jdbcTemplate.execute(
				"insert into HFJ_SPIDX_TOKEN (SP_MISSING, SP_NAME, RES_ID, RES_TYPE, SP_UPDATED, SP_SYSTEM, SP_VALUE, SP_ID) values (?, ?, ?, ?, ?, ?, ?, ?)",
				new AbstractLobCreatingPreparedStatementCallback(new DefaultLobHandler()) {
					@Override
					protected void setValues(PreparedStatement thePs, LobCreator theLobCreator) throws SQLException {
						thePs.setBoolean(1, false);
						thePs.setString(2, "identifier");
						thePs.setLong(3, 1L); // res-id
						thePs.setString(4, "Patient");
						thePs.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
						thePs.setString(6, "http://foo");
						thePs.setString(7, "12345678");
						thePs.setLong(8, 1L);
					}
				}
			);

			jdbcTemplate.execute(
				"insert into HFJ_SPIDX_DATE (SP_MISSING, SP_NAME, RES_ID, RES_TYPE, SP_UPDATED, SP_VALUE_HIGH, SP_VALUE_LOW, SP_ID) values (?, ?, ?, ?, ?, ?, ?, ?)",
				new AbstractLobCreatingPreparedStatementCallback(new DefaultLobHandler()) {
					@Override
					protected void setValues(PreparedStatement thePs, LobCreator theLobCreator) throws SQLException {
						thePs.setBoolean(1, false);
						thePs.setString(2, "birthdate");
						thePs.setLong(3, 1L); // res-id
						thePs.setString(4, "Patient");
						thePs.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
						thePs.setTimestamp(6, new Timestamp(1000000000L)); // value high
						thePs.setTimestamp(7, new Timestamp(1000000000L)); // value low
						thePs.setLong(8, 1L);
					}
				}
			);

			return null;
		});

	}

	private void executeSqlStatements(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theInitSql) throws
		IOException {
		String script = IOUtils.toString(HapiFlywayMigrateDatabaseCommandTest.class.getResourceAsStream(theInitSql), Charsets.UTF_8);
		List<String> scriptStatements = new ArrayList<>(Arrays.asList(script.split("\n")));
		for (int i = 0; i < scriptStatements.size(); i++) {
			String nextStatement = scriptStatements.get(i);
			if (isBlank(nextStatement)) {
				scriptStatements.remove(i);
				i--;
				continue;
			}

			nextStatement = nextStatement.trim();
			while (nextStatement.endsWith(";")) {
				nextStatement = nextStatement.substring(0, nextStatement.length() - 1);
			}
			scriptStatements.set(i, nextStatement);
		}

		theConnectionProperties.getTxTemplate().execute(t -> {
			for (String next : scriptStatements) {
				theConnectionProperties.newJdbcTemplate().execute(next);
			}
			return null;
		});

	}

}
