package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.migrate.taskdef.ColumnTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.DropTableTask;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HapiMigrationStorageSvcTest extends BaseMigrationTest {
	private static final String RELEASE = "V5_5_0";
	@Autowired
	HapiMigrationStorageSvc myHapiMigrationStorageSvc;

	@BeforeEach
	void before() {
		List<BaseTask> tasks = createTasks();
		assertThat(tasks, hasSize(7));

		for (BaseTask task : tasks) {
			HapiMigrationEntity entity = HapiMigrationEntity.fromBaseTask(task);
			myHapiMigrationDao.save(entity);
		}
	}

	@Test
	void diff_oneNew_returnsNew() {
		List<HapiMigrationEntity> appliedMigrations = myHapiMigrationStorageSvc.fetchAppliedMigrations();
		assertThat(appliedMigrations, hasSize(7));

		List<BaseTask> tasks = createTasks();
		BaseTask dropTableTask = new DropTableTask(RELEASE, "20210722.4");
		tasks.add(dropTableTask);

		List<BaseTask> notAppliedYet = myHapiMigrationStorageSvc.diff(tasks);
		assertThat(notAppliedYet, hasSize(1));
		assertEquals("5_5_0.20210722.4", notAppliedYet.get(0).getMigrationVersion());
	}

	private List<BaseTask> createTasks() {
		List<BaseTask> tasks = new ArrayList<>();

		Builder version = forVersion(tasks);

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
		cmbTokNuTable.addColumn("20210722.2", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		cmbTokNuTable.modifyColumn("20210722.3", "RES_ID").nullable().withType(ColumnTypeEnum.LONG);

		return tasks;
	}

	public Builder forVersion(List<BaseTask> theTasks) {
		BaseMigrationTasks.IAcceptsTasks sink = theTask -> {
			theTask.validate();
			theTasks.add(theTask);
		};
		return new Builder(RELEASE, sink);
	}


}
