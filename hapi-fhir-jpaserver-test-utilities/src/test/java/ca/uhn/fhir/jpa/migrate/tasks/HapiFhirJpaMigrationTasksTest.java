package ca.uhn.fhir.jpa.migrate.tasks;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.HapiMigrationStorageSvc;
import ca.uhn.fhir.jpa.migrate.HapiMigrator;
import ca.uhn.fhir.jpa.migrate.MigrationResult;
import ca.uhn.fhir.jpa.migrate.MigrationTaskList;
import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.taskdef.InitializeSchemaTask;
import ca.uhn.fhir.util.VersionEnum;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HapiFhirJpaMigrationTasksTest {

	private static final String MIGRATION_TABLENAME = "HFJ_FLY_MIGRATOR";
	private static final Logger ourLog = LoggerFactory.getLogger(HapiFhirJpaMigrationTasksTest.class);
	private final BasicDataSource myDataSource = newDataSource();
	private final JdbcTemplate myJdbcTemplate = new JdbcTemplate(myDataSource);
	private HapiMigrationStorageSvc myMigrationStorageSvc;
	private HapiMigrator myMigrator;

	@BeforeEach
	void before() {
		myMigrator = new HapiMigrator(MIGRATION_TABLENAME, myDataSource, DriverTypeEnum.H2_EMBEDDED);
		HapiMigrationDao migrationDao = new HapiMigrationDao(myDataSource, DriverTypeEnum.H2_EMBEDDED, MIGRATION_TABLENAME);
		myMigrationStorageSvc = new HapiMigrationStorageSvc(migrationDao);

	}

	@Test
	public void testCreate() {
		new HapiFhirJpaMigrationTasks(Collections.emptySet());
	}

	/**
	 * Verify that the migration task which handles creation
	 */
	@Test
	public void testCreateUniqueComboParamHashes() {
		/*
		 * Setup
		 */


		// Create migrator and initialize schema
		HapiFhirJpaMigrationTasks tasks = new HapiFhirJpaMigrationTasks(Set.of());
		MigrationTaskList allTasks = tasks.getAllTasks(VersionEnum.V7_2_0, VersionEnum.V7_4_0);
		myMigrator.addTask(new InitializeSchemaTask("7.2.0",				"20180115.0",
			new SchemaInitializationProvider(
				"HAPI FHIR", "/jpa_h2_schema_720", "HFJ_RESOURCE", true)));

		myMigrator.addTasks(allTasks);
		myMigrator.createMigrationTableIfRequired();
		myMigrator.migrate();

		// Create a unique index row with no hashes populated
		insertRow_ResourceTable();
		insertRow_ResourceIndexedComboStringUnique();

		// Remove the hash creation task from history so it runs again
		assertEquals(1, myJdbcTemplate.update("DELETE FROM " + MIGRATION_TABLENAME + " WHERE version = ?", "7.4.0.20240617.4"));

		/*
		 * Execute
		 */

		// Run the migrator
		MigrationResult migrationResult = myMigrator.migrate();
		assertEquals(1, migrationResult.succeededTasks.size());
		assertEquals(0, migrationResult.failedTasks.size());

		/*
		 * Verify
		 */

		List<Map<String, Object>> rows = myJdbcTemplate.query("SELECT * FROM HFJ_IDX_CMP_STRING_UNIQ", new ColumnMapRowMapper());
		assertEquals(1, rows.size());
		Map<String, Object> row = rows.get(0);
		assertThat(row.get("HASH_COMPLETE")).as(row::toString).isEqualTo(-5443017569618195896L);
		assertThat(row.get("HASH_COMPLETE_2")).as(row::toString).isEqualTo(-1513800680307323438L);
	}

	private void insertRow_ResourceIndexedComboStringUnique() {
		myJdbcTemplate.execute(
			"""
				insert into
				HFJ_IDX_CMP_STRING_UNIQ (
				  PID,
				  RES_ID,
				  IDX_STRING) 
				values (1, 1, 'Patient?foo=bar')
				""");
	}

	private void insertRow_ResourceTable() {
		myJdbcTemplate.execute(
			"""
				insert into
				HFJ_RESOURCE (
				  RES_DELETED_AT,
				  RES_VERSION,
				  FHIR_ID,
				  HAS_TAGS,
				  RES_PUBLISHED,
				  RES_UPDATED,
				  SP_HAS_LINKS,
				  HASH_SHA256,
				  SP_INDEX_STATUS,
				  RES_LANGUAGE,
				  SP_CMPSTR_UNIQ_PRESENT,
				  SP_COORDS_PRESENT,
				  SP_DATE_PRESENT,
				  SP_NUMBER_PRESENT,
				  SP_QUANTITY_PRESENT,
				              SP_STRING_PRESENT,
				              SP_TOKEN_PRESENT,
				              SP_URI_PRESENT,
				              SP_QUANTITY_NRML_PRESENT,
				              RES_TYPE,
				              RES_VER,
				              RES_ID) 
				            values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""",
			new AbstractLobCreatingPreparedStatementCallback(new DefaultLobHandler()) {
				@Override
				protected void setValues(PreparedStatement thePs, LobCreator theLobCreator) throws SQLException {
					int i = 1;
					thePs.setNull(i++, Types.TIMESTAMP);
					thePs.setString(i++, "R4");
					thePs.setString(i++, "ABC"); // FHIR_ID
					thePs.setBoolean(i++, false);
					thePs.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
					thePs.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
					thePs.setBoolean(i++, false);
					thePs.setNull(i++, Types.VARCHAR);
					thePs.setLong(i++, 1L);
					thePs.setNull(i++, Types.VARCHAR);
					thePs.setBoolean(i++, false);
					thePs.setBoolean(i++, false);
					thePs.setBoolean(i++, false);
					thePs.setBoolean(i++, false);
					thePs.setBoolean(i++, false);
					thePs.setBoolean(i++, false);
					thePs.setBoolean(i++, false);
					thePs.setBoolean(i++, false);
					thePs.setBoolean(i++, false); // SP_QUANTITY_NRML_PRESENT
					thePs.setString(i++, "Patient");
					thePs.setLong(i++, 1L);
					thePs.setLong(i++, 1L); // RES_ID
				}
			});
	}

	static BasicDataSource newDataSource() {
		BasicDataSource retVal = new BasicDataSource();
		retVal.setDriver(new org.h2.Driver());
		retVal.setUrl("jdbc:h2:mem:test_migration-" + UUID.randomUUID() + ";CASE_INSENSITIVE_IDENTIFIERS=TRUE;");
		retVal.setMaxWaitMillis(30000);
		retVal.setUsername("");
		retVal.setPassword("");
		retVal.setMaxTotal(5);

		return retVal;
	}

}
