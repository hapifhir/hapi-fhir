package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresent;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

public class ArbitrarySqlTaskTest extends BaseTest {

	public ArbitrarySqlTaskTest(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		super(theTestDatabaseDetails);
	}

	@Test
	public void test350MigrateSearchParams() {
		executeSql("create table HFJ_SEARCH_PARM (PID bigint not null, RES_TYPE varchar(255), PARAM_NAME varchar(255))");
		executeSql("insert into HFJ_SEARCH_PARM (PID, RES_TYPE, PARAM_NAME) values (1, 'Patient', 'identifier')");
		executeSql("insert into HFJ_SEARCH_PARM (PID, RES_TYPE, PARAM_NAME) values (2, 'Patient', 'family')");
		executeSql("create table HFJ_RES_PARAM_PRESENT (PID bigint, SP_ID bigint, SP_PRESENT boolean, HASH_PRESENT bigint)");
		executeSql("insert into HFJ_RES_PARAM_PRESENT (PID, SP_ID, SP_PRESENT, HASH_PRESENT) values (100, 1, true, null)");
		executeSql("insert into HFJ_RES_PARAM_PRESENT (PID, SP_ID, SP_PRESENT, HASH_PRESENT) values (101, 2, true, null)");

		ArbitrarySqlTask task = new ArbitrarySqlTask(VersionEnum.V3_5_0,  "1", "HFJ_RES_PARAM_PRESENT", "Consolidate search parameter presence indexes");
		task.setExecuteOnlyIfTableExists("hfj_search_parm");
		task.setBatchSize(1);
		String sql = "SELECT " +
			"HFJ_SEARCH_PARM.RES_TYPE RES_TYPE, HFJ_SEARCH_PARM.PARAM_NAME PARAM_NAME, " +
			"HFJ_RES_PARAM_PRESENT.PID PID, HFJ_RES_PARAM_PRESENT.SP_ID SP_ID, HFJ_RES_PARAM_PRESENT.SP_PRESENT SP_PRESENT, HFJ_RES_PARAM_PRESENT.HASH_PRESENT HASH_PRESENT " +
			"from HFJ_RES_PARAM_PRESENT " +
			"join HFJ_SEARCH_PARM ON (HFJ_SEARCH_PARM.PID = HFJ_RES_PARAM_PRESENT.SP_ID) " +
			"where HFJ_RES_PARAM_PRESENT.HASH_PRESENT is null";
		task.addQuery(sql, ArbitrarySqlTask.QueryModeEnum.BATCH_UNTIL_NO_MORE, t -> {
			Long pid = (Long) t.get("PID");
			Boolean present = (Boolean) t.get("SP_PRESENT");
			String resType = (String) t.get("RES_TYPE");
			String paramName = (String) t.get("PARAM_NAME");
			Long hash = SearchParamPresent.calculateHashPresence(resType, paramName, present);
			task.executeSql("HFJ_RES_PARAM_PRESENT", "update HFJ_RES_PARAM_PRESENT set HASH_PRESENT = ? where PID = ?", hash, pid);
		});

		getMigrator().addTask(task);
		getMigrator().migrate();


		List<Map<String, Object>> rows = executeQuery("select * from HFJ_RES_PARAM_PRESENT order by PID asc");
		assertEquals(2, rows.size());
		assertEquals(100L, rows.get(0).get("PID"));
		assertEquals(-1100208805056022671L, rows.get(0).get("HASH_PRESENT"));
		assertEquals(101L, rows.get(1).get("PID"));
		assertEquals(-756348509333838170L, rows.get(1).get("HASH_PRESENT"));

	}


	@Test
	public void testExecuteOnlyIfTableExists() {
		ArbitrarySqlTask task = new ArbitrarySqlTask(VersionEnum.V3_5_0,  "1", "HFJ_RES_PARAM_PRESENT", "Consolidate search parameter presence indexes");
		task.setBatchSize(1);
		String sql = "SELECT * FROM HFJ_SEARCH_PARM";
		task.addQuery(sql, ArbitrarySqlTask.QueryModeEnum.BATCH_UNTIL_NO_MORE, t -> {
			task.executeSql("HFJ_RES_PARAM_PRESENT", "update HFJ_RES_PARAM_PRESENT set FOOFOOOFOO = null");
		});
		task.setExecuteOnlyIfTableExists("hfj_search_parm");

		// No action should be performed
		getMigrator().addTask(task);
		getMigrator().migrate();

	}

	@Test
	public void testUpdateTask() {
		executeSql("create table TEST_UPDATE_TASK (PID bigint not null, RES_TYPE varchar(255), PARAM_NAME varchar(255))");
		executeSql("insert into TEST_UPDATE_TASK (PID, RES_TYPE, PARAM_NAME) values (1, 'Patient', 'identifier')");

		List<Map<String, Object>> rows = executeQuery("select * from TEST_UPDATE_TASK");
		assertEquals(1, rows.size());

		BaseMigrationTasks<VersionEnum> migrator = new BaseMigrationTasks<VersionEnum>() {
		};
		migrator
			.forVersion(VersionEnum.V3_5_0)
			.addTableRawSql("1", "A")
			.addSql("delete from TEST_UPDATE_TASK where RES_TYPE = 'Patient'");

		getMigrator().addTasks(migrator.getTasks(VersionEnum.V3_3_0, VersionEnum.V3_6_0));
		getMigrator().migrate();

		rows = executeQuery("select * from TEST_UPDATE_TASK");
		assertEquals(0, rows.size());

	}

	@Test
	public void testArbitrarySql() {
		executeSql("create table TEST_UPDATE_TASK (PID bigint not null, RES_TYPE varchar(255), PARAM_NAME varchar(255))");
		executeSql("insert into TEST_UPDATE_TASK (PID, RES_TYPE, PARAM_NAME) values (1, 'Patient', 'identifier')");
		executeSql("insert into TEST_UPDATE_TASK (PID, RES_TYPE, PARAM_NAME) values (1, 'Encounter', 'identifier')");

		List<Map<String, Object>> rows = executeQuery("select * from TEST_UPDATE_TASK");
		assertEquals(2, rows.size());

		BaseMigrationTasks<VersionEnum> migrator = new BaseMigrationTasks<VersionEnum>() {
		};
		migrator
			.forVersion(VersionEnum.V3_5_0)
			.executeRawSql("1", getDriverType(), "delete from TEST_UPDATE_TASK where RES_TYPE = 'Patient'")
			.executeRawSql("2", getDriverType(), "delete from TEST_UPDATE_TASK where RES_TYPE = 'Encounter'");

		getMigrator().addTasks(migrator.getTasks(VersionEnum.V3_3_0, VersionEnum.V3_6_0));
		getMigrator().migrate();

		rows = executeQuery("select * from TEST_UPDATE_TASK");
		assertEquals(0, rows.size());

	}

}
