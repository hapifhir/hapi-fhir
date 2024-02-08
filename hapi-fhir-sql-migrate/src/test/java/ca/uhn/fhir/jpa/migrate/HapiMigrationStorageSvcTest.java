package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.migrate.taskdef.ColumnTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.DropTableTask;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import org.flywaydb.core.api.MigrationVersion;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class HapiMigrationStorageSvcTest extends BaseMigrationTest {
	private static final String RELEASE = "V5_5_0";
	private static final String RELEASE_VERSION_PREFIX = "5.5.0.";
	public static final String FAILED_VERSION = "20210722.3";
	public static final String LAST_TASK_VERSION = RELEASE_VERSION_PREFIX + FAILED_VERSION;
	public static final String LAST_SUCCEEDED_VERSION = "20210722.2";

	@Test
	void diff_oneNew_returnsNew() {
		createTasks();
		Set<MigrationVersion> appliedMigrations = ourHapiMigrationStorageSvc.fetchAppliedMigrationVersions();
		assertThat(appliedMigrations).hasSize(6);

		MigrationTaskList taskList = buildTasks();
		String version = "20210722.4";
		BaseTask dropTableTask = new DropTableTask(RELEASE, version);
		taskList.add(dropTableTask);

		MigrationTaskList notAppliedYet = ourHapiMigrationStorageSvc.diff(taskList);
		assertThat(notAppliedYet.size()).isEqualTo(2);
		List<BaseTask> notAppliedTasks = new ArrayList<>();
		notAppliedYet.forEach(notAppliedTasks::add);

		assertThat(notAppliedTasks.get(0).getMigrationVersion()).isEqualTo(RELEASE_VERSION_PREFIX + FAILED_VERSION);
		assertThat(notAppliedTasks.get(1).getMigrationVersion()).isEqualTo(RELEASE_VERSION_PREFIX + version);
	}

	@Test
	void getLatestAppliedVersion_empty_unknown() {
		String latest = ourHapiMigrationStorageSvc.getLatestAppliedVersion();
		assertThat(latest).isEqualTo(HapiMigrationStorageSvc.UNKNOWN_VERSION);
	}

	@Test
	void getLatestAppliedVersion_full_last() {
		String latest = ourHapiMigrationStorageSvc.getLatestAppliedVersion();
		assertThat(latest).isEqualTo(HapiMigrationStorageSvc.UNKNOWN_VERSION);

		createTasks();
		String newLatest = ourHapiMigrationStorageSvc.getLatestAppliedVersion();
		assertThat(newLatest).isEqualTo(RELEASE_VERSION_PREFIX + LAST_SUCCEEDED_VERSION);
	}

	@Test
	void insert_delete() {
		String description = UUID.randomUUID().toString();
		int initialCount = countRecords();
		assertThat(ourHapiMigrationStorageSvc.insertLockRecord(description)).isTrue();
		assertThat(countRecords()).isEqualTo(initialCount + 1);
		ourHapiMigrationStorageSvc.deleteLockRecord(description);
		assertThat(countRecords()).isEqualTo(initialCount);
	}

	@Test
	void verifyNoOtherLocksPresent() {
		String otherLock = UUID.randomUUID().toString();
		String thisLock = UUID.randomUUID().toString();
		ourHapiMigrationStorageSvc.verifyNoOtherLocksPresent(thisLock);
		assertThat(ourHapiMigrationStorageSvc.insertLockRecord(otherLock)).isTrue();
		try {
			ourHapiMigrationStorageSvc.verifyNoOtherLocksPresent(thisLock);
			fail("");
		} catch (HapiMigrationException e) {
			assertThat(e.getMessage()).isEqualTo("HAPI-2152: Internal error: on unlocking, a competing lock was found");
		}
	}

	private int countRecords() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + BaseMigrationTest.TABLE_NAME, Integer.class);
	}

	void createTasks() {
		MigrationTaskList taskList = buildTasks();
		assertThat(taskList.size()).isEqualTo(7);

		taskList.forEach(task -> {
			HapiMigrationEntity entity = HapiMigrationEntity.fromBaseTask(task);
			entity.setExecutionTime(1);
			entity.setSuccess(!LAST_TASK_VERSION.equals(task.getMigrationVersion()));
			ourHapiMigrationDao.save(entity);
		});
	}

	MigrationTaskList buildTasks() {
		MigrationTaskList taskList = new MigrationTaskList();

		Builder version = forVersion(taskList);

		Builder.BuilderAddTableByColumns cmpToks = version
			.addTableByColumns("20210720.3", "HFJ_IDX_CMB_TOK_NU", "PID");
		cmpToks.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		cmpToks.addColumn("RES_ID").nonNullable().type(ColumnTypeEnum.LONG);
		cmpToks.addColumn("HASH_COMPLETE").nonNullable().type(ColumnTypeEnum.LONG);
		cmpToks.addColumn("IDX_STRING").nonNullable().type(ColumnTypeEnum.STRING, 500);
		cmpToks.addForeignKey("20210720.4", "FK_IDXCMBTOKNU_RES_ID").toColumn("RES_ID").references("HFJ_RESOURCE", "RES_ID");
		cmpToks.addIndex("20210720.5", "IDX_IDXCMBTOKNU_STR").unique(false).withColumns("IDX_STRING");
		cmpToks.addIndex("20210720.6", "IDX_IDXCMBTOKNU_RES").unique(false).withColumns("RES_ID");

		Builder.BuilderWithTableName cmbTokNuTable = version.onTable("HFJ_IDX_CMB_TOK_NU");

		cmbTokNuTable.addColumn("20210722.1", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		cmbTokNuTable.addColumn(LAST_SUCCEEDED_VERSION, "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		cmbTokNuTable.modifyColumn(FAILED_VERSION, "RES_ID").nullable().withType(ColumnTypeEnum.LONG);

		return taskList;
	}

	public Builder forVersion(MigrationTaskList theTaskList) {
		BaseMigrationTasks.IAcceptsTasks sink = theTask -> {
			theTask.validate();
			theTaskList.add(theTask);
		};
		return new Builder(RELEASE, sink);
	}


}
