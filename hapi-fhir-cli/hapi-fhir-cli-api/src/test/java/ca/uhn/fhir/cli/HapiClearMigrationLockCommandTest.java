package ca.uhn.fhir.cli;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import ca.uhn.fhir.system.HapiSystemProperties;
import com.google.common.base.Charsets;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static ca.uhn.fhir.jpa.migrate.HapiMigrationLock.LOCK_PID;
import static ca.uhn.fhir.jpa.migrate.HapiMigrationStorageSvc.LOCK_TYPE;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.slf4j.LoggerFactory.getLogger;

public class HapiClearMigrationLockCommandTest extends ConsoleOutputCapturingBaseTest {
	
	private static final Logger ourLog = getLogger(HapiClearMigrationLockCommandTest.class);

	public static final String DB_DIRECTORY = "target/h2_test";

	static {
		HapiSystemProperties.enableTestMode();
	}

	@Test
	public void testClearNonExistingLockIncorrectLock() throws IOException {
		ConnectionData connectionData = createSchemaAndMigrate("test_migrate_clear_incorrect_lock");
		HapiMigrationDao dao = new HapiMigrationDao(connectionData.connectionProperties.getDataSource(), DriverTypeEnum.H2_EMBEDDED, "FLY_HFJ_MIGRATION");
		String correctLockUUID = UUID.randomUUID().toString();
		String incorrectLockUUID = UUID.randomUUID().toString();
		createAndSaveLockRow(correctLockUUID, dao);

		String[] args = new String[]{
			BaseClearMigrationLockCommand.CLEAR_LOCK,
			"-d", "H2_EMBEDDED",
			"-u", connectionData.url,
			"-n", "",
			"-p", "",
			"-l", incorrectLockUUID
		};

		int beforeClearMigrationCount = dao.findAll().size();
		try {
			App.main(args);
			fail();		} catch (CommandFailureException e) {
			assertThat(e.getMessage()).contains("HAPI-2152: Internal error: on unlocking, a competing lock was found");
		}
	}
	@Test
	public void testClearNonExistingLockNoLocks() throws IOException {
		ConnectionData connectionData = createSchemaAndMigrate("test_migrate_clear_nonexisting_lock");
		HapiMigrationDao dao = new HapiMigrationDao(connectionData.connectionProperties.getDataSource(), DriverTypeEnum.H2_EMBEDDED, "FLY_HFJ_MIGRATION");
		String lockUUID = UUID.randomUUID().toString();

		String[] args = new String[]{
			BaseClearMigrationLockCommand.CLEAR_LOCK,
			"-d", "H2_EMBEDDED",
			"-u", connectionData.url,
			"-n", "",
			"-p", "",
			"-l", lockUUID
		};

		int beforeClearMigrationCount = dao.findAll().size();
		App.main(args);
		int afterClearMigrationCount = dao.findAll().size();
		int removedRows = beforeClearMigrationCount - afterClearMigrationCount;
		assertEquals(0, removedRows);
		assertThat(getConsoleOutput()).contains("Did not successfully remove lock entry. [uuid=" + lockUUID + "]");
	}
	@Test
	public void testMigrateAndClearExistingLock() throws IOException, SQLException {
		ConnectionData connectionData = createSchemaAndMigrate("test_migrate_clear_existing_lock");
		HapiMigrationDao dao = new HapiMigrationDao(connectionData.connectionProperties.getDataSource(), DriverTypeEnum.H2_EMBEDDED, "FLY_HFJ_MIGRATION");
		String lockUUID = UUID.randomUUID().toString();
		createAndSaveLockRow(lockUUID, dao);


		String[] args = new String[]{
			BaseClearMigrationLockCommand.CLEAR_LOCK,
			"-d", "H2_EMBEDDED",
			"-u", connectionData.url,
			"-n", "",
			"-p", "",
			"-l", lockUUID
		};
		int beforeClearMigrationCount = dao.findAll().size();
		App.main(args);
		int afterClearMigrationCount = dao.findAll().size();
		int removedRows = beforeClearMigrationCount - afterClearMigrationCount;

		assertEquals(1, removedRows);
		assertThat(getConsoleOutput()).contains("Successfully removed lock entry. [uuid=" + lockUUID + "]");
	}

	private record ConnectionData(DriverTypeEnum.ConnectionProperties connectionProperties, String url) {}
	public ConnectionData createSchemaAndMigrate(String theDbName) throws IOException {

		File location = getLocation(theDbName);

		String url = "jdbc:h2:" + location.getAbsolutePath();
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
			"-r"
		};
		App.main(args);
		return new ConnectionData(connectionProperties, url);
	}




	private static void createAndSaveLockRow(String theLockUUID, HapiMigrationDao theDao) {
		HapiMigrationEntity me = new HapiMigrationEntity();
		me.setPid(LOCK_PID);
		me.setChecksum(100);
		me.setDescription(theLockUUID);
		me.setSuccess(true);
		me.setExecutionTime(20);
		me.setInstalledBy("gary");
		me.setInstalledOn(new Date());
		me.setVersion("2023.1");
		me.setType(LOCK_TYPE);
		theDao.save(me);
	}

	@Nonnull
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
		String script = IOUtils.toString(HapiClearMigrationLockCommandTest.class.getResourceAsStream(theInitSql), Charsets.UTF_8);
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

