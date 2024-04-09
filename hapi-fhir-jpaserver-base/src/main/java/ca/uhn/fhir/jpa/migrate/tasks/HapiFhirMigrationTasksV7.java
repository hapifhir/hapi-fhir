package ca.uhn.fhir.jpa.migrate.tasks;

import ca.uhn.fhir.jpa.migrate.taskdef.ColumnTypeEnum;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HapiFhirMigrationTasksV7 {
	private final Builder myVersion;

	public HapiFhirMigrationTasksV7(Builder theVersion) {
		this.myVersion = theVersion;
	}

	public void populateV720() {
		// allow null codes in concept map targets
		myVersion
				.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
				.modifyColumn("20240327.1", "TARGET_CODE")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 500);

		// Stop writing to hfj_forced_id https://github.com/hapifhir/hapi-fhir/pull/5817
		Builder.BuilderWithTableName forcedId = myVersion.onTable("HFJ_FORCED_ID");
		forcedId.dropForeignKey("20240402.1", "FK_FORCEDID_RESOURCE", "HFJ_RESOURCE");
		forcedId.dropIndex("20240402.2", "IDX_FORCEDID_RESID");
		forcedId.dropIndex("20240402.3", "IDX_FORCEDID_TYPE_FID");
		forcedId.dropIndex("20240402.4", "IDX_FORCEID_FID");

		String subVersionName = "20240408";

		Collection<String> indexTableList = List.of(
				"HFJ_SPIDX_COORDS",
				"HFJ_SPIDX_DATE",
				"HFJ_SPIDX_NUMBER",
				"HFJ_SPIDX_QUANTITY",
				"HFJ_SPIDX_QUANTITY_NRML",
				"HFJ_SPIDX_TOKEN",
				"HFJ_SPIDX_STRING",
				"HFJ_SPIDX_TOKEN",
				"HFJ_SPIDX_URI");

		AtomicInteger i = new AtomicInteger();
		indexTableList.forEach(idxTable -> {
			Builder.BuilderWithTableName tableBuilder = myVersion.onTable(idxTable);
			tableBuilder
					.addColumn(subVersionName + "." + i.getAndIncrement(), "contained_ord")
					.nullable()
					.type(ColumnTypeEnum.TINYINT);
		});
	}
}
