package ca.uhn.fhir.cli;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HapiMigrateDatabaseCommandTest {

	private static final Logger ourLog = LoggerFactory.getLogger(HapiMigrateDatabaseCommandTest.class);

	static {
		System.setProperty("test", "true");
	}

	@Test
	public void testMigrate_340_370() throws IOException {

		File directory = new File("target/migrator_derby_test_340_360");
		if (directory.exists()) {
			FileUtils.deleteDirectory(directory);
		}

		String url = "jdbc:derby:directory:" + directory.getAbsolutePath() + ";create=true";
		DriverTypeEnum.ConnectionProperties connectionProperties = DriverTypeEnum.DERBY_EMBEDDED.newConnectionProperties(url, "", "");

		String initSql = "/persistence_create_derby107_340.sql";
		executeSqlStatements(connectionProperties, initSql);

		seedDatabase340(connectionProperties);

		ourLog.info("**********************************************");
		ourLog.info("Done Setup, Starting Migration...");
		ourLog.info("**********************************************");

		String[] args = new String[]{
			"migrate-database",
			"-d", "DERBY_EMBEDDED",
			"-u", url,
			"-n", "",
			"-p", "",
			"-f", "V3_4_0",
			"-t", "V3_7_0"
		};
		App.main(args);

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
	public void testMigrate_340_350() throws IOException {

		File directory = new File("target/migrator_derby_test_340_350");
		if (directory.exists()) {
			FileUtils.deleteDirectory(directory);
		}

		String url = "jdbc:derby:directory:" + directory.getAbsolutePath() + ";create=true";
		DriverTypeEnum.ConnectionProperties connectionProperties = DriverTypeEnum.DERBY_EMBEDDED.newConnectionProperties(url, "", "");

		String initSql = "/persistence_create_derby107_340.sql";
		executeSqlStatements(connectionProperties, initSql);

		seedDatabase340(connectionProperties);

		ourLog.info("**********************************************");
		ourLog.info("Done Setup, Starting Dry Run...");
		ourLog.info("**********************************************");

		String[] args = new String[]{
			"migrate-database",
			"-d", "DERBY_EMBEDDED",
			"-u", url,
			"-n", "",
			"-p", "",
			"-r",
			"-f", "V3_4_0",
			"-t", "V3_5_0"
		};
		App.main(args);

		connectionProperties.getTxTemplate().execute(t -> {
			JdbcTemplate jdbcTemplate = connectionProperties.newJdbcTemplate();
			List<Map<String, Object>> values = jdbcTemplate.queryForList("SELECT * FROM hfj_spidx_token");
			assertFalse(values.get(0).keySet().contains("HASH_IDENTITY"));
			return null;
		});

		ourLog.info("**********************************************");
		ourLog.info("Done Setup, Starting Migration...");
		ourLog.info("**********************************************");

		args = new String[]{
			"migrate-database",
			"-d", "DERBY_EMBEDDED",
			"-u", url,
			"-n", "",
			"-p", "",
			"-f", "V3_4_0",
			"-t", "V3_5_0"
		};
		App.main(args);

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


	@Test
	public void testMigrate_340_350_NoMigrateHashes() throws IOException {

		File directory = new File("target/migrator_derby_test_340_350_nmh");
		if (directory.exists()) {
			FileUtils.deleteDirectory(directory);
		}

		String url = "jdbc:derby:directory:" + directory.getAbsolutePath() + ";create=true";
		DriverTypeEnum.ConnectionProperties connectionProperties = DriverTypeEnum.DERBY_EMBEDDED.newConnectionProperties(url, "", "");

		String initSql = "/persistence_create_derby107_340.sql";
		executeSqlStatements(connectionProperties, initSql);

		seedDatabase340(connectionProperties);

		ourLog.info("**********************************************");
		ourLog.info("Done Setup, Starting Migration...");
		ourLog.info("**********************************************");

		String[] args = new String[]{
			"migrate-database",
			"-d", "DERBY_EMBEDDED",
			"-u", url,
			"-n", "",
			"-p", "",
			"-f", "V3_4_0",
			"-t", "V3_5_0",
			"-x", "no-migrate-350-hashes"
		};
		App.main(args);

		connectionProperties.getTxTemplate().execute(t -> {
			JdbcTemplate jdbcTemplate = connectionProperties.newJdbcTemplate();
			List<Map<String, Object>> values = jdbcTemplate.queryForList("SELECT * FROM hfj_spidx_token");
			assertEquals(1, values.size());
			assertEquals("identifier", values.get(0).get("SP_NAME"));
			assertEquals("12345678", values.get(0).get("SP_VALUE"));
			assertEquals(null, values.get(0).get("HASH_IDENTITY"));
			return null;
		});

	}

	private void executeSqlStatements(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theInitSql) throws
		IOException {
		String script = IOUtils.toString(HapiMigrateDatabaseCommandTest.class.getResourceAsStream(theInitSql), Charsets.UTF_8);
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
