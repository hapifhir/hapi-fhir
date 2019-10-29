package ca.uhn.fhir.jpa.migrate.tasks;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.AddColumnTask;
import ca.uhn.fhir.jpa.migrate.taskdef.ArbitrarySqlTask;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTableColumnTypeTask;
import ca.uhn.fhir.jpa.migrate.taskdef.CalculateHashesTask;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.jpa.model.entity.*;
import ca.uhn.fhir.util.VersionEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"SqlNoDataSourceInspection", "SpellCheckingInspection"})
public class HapiFhirJpaMigrationTasks extends BaseMigrationTasks<VersionEnum> {

	private final Set<FlagEnum> myFlags;

	/**
	 * Constructor
	 */
	public HapiFhirJpaMigrationTasks(Set<String> theFlags) {
		myFlags = theFlags
			.stream()
			.map(FlagEnum::fromCommandLineValue)
			.collect(Collectors.toSet());

		init330();
		init340();
		init350();
		init360();
		init400();
		init410();
	}

	protected void init410() {
		Builder version = forVersion(VersionEnum.V4_1_0);

		// HFJ_SEARCH
		version.onTable("HFJ_SEARCH").addColumn("EXPIRY_OR_NULL").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.DATE_TIMESTAMP);
		version.onTable("HFJ_SEARCH").addColumn("NUM_BLOCKED").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.INT);

		// HFJ_BLK_EXPORT_JOB
		version.addIdGenerator("SEQ_BLKEXJOB_PID");
		Builder.BuilderAddTableByColumns bulkExportJob = version.addTableByColumns("HFJ_BLK_EXPORT_JOB", "PID");
		bulkExportJob.addColumn("PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		bulkExportJob.addColumn("JOB_ID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 36);
		bulkExportJob.addColumn("JOB_STATUS").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 10);
		bulkExportJob.addColumn("CREATED_TIME").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.DATE_TIMESTAMP);
		bulkExportJob.addColumn("STATUS_TIME").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.DATE_TIMESTAMP);
		bulkExportJob.addColumn("EXP_TIME").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.DATE_TIMESTAMP);
		bulkExportJob.addColumn("REQUEST").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 500);
		bulkExportJob.addColumn("OPTLOCK").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.INT);
		bulkExportJob.addColumn("EXP_SINCE").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.DATE_TIMESTAMP);
		bulkExportJob.addColumn("STATUS_MESSAGE").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 500);
		bulkExportJob.addIndex("IDX_BLKEX_EXPTIME").unique(false).withColumns("EXP_TIME");
		bulkExportJob.addIndex("IDX_BLKEX_JOB_ID").unique(true).withColumns("JOB_ID");

		// HFJ_BLK_EXPORT_COLLECTION
		version.addIdGenerator("SEQ_BLKEXCOL_PID");
		Builder.BuilderAddTableByColumns bulkExportCollection = version.addTableByColumns("HFJ_BLK_EXPORT_COLLECTION", "PID");
		bulkExportCollection.addColumn("PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		bulkExportCollection.addColumn("JOB_PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		bulkExportCollection.addForeignKey("FK_BLKEXCOL_JOB").toColumn("JOB_PID").references("HFJ_BLK_EXPORT_JOB", "PID");
		bulkExportCollection.addColumn("RES_TYPE").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 40);
		bulkExportCollection.addColumn("TYPE_FILTER").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 1000);
		bulkExportCollection.addColumn("OPTLOCK").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.INT);

		// HFJ_BLK_EXPORT_COLFILE
		version.addIdGenerator("SEQ_BLKEXCOLFILE_PID");
		Builder.BuilderAddTableByColumns bulkExportCollectionFile = version.addTableByColumns("HFJ_BLK_EXPORT_COLFILE", "PID");
		bulkExportCollectionFile.addColumn("PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		bulkExportCollectionFile.addColumn("COLLECTION_PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		bulkExportCollectionFile.addColumn("RES_ID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 100);
		bulkExportCollectionFile.addForeignKey("FK_BLKEXCOLFILE_COLLECT").toColumn("COLLECTION_PID").references("HFJ_BLK_EXPORT_COLLECTION", "PID");

		// HFJ_RES_VER_PROV
		version.startSectionWithMessage("Processing bulkExportCollectionFile: HFJ_RES_VER_PROV");
		Builder.BuilderAddTableByColumns resVerProv = version.addTableByColumns("HFJ_RES_VER_PROV", "RES_VER_PID");
		resVerProv.addColumn("RES_VER_PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		resVerProv
			.addForeignKey("FK_RESVERPROV_RESVER_PID")
			.toColumn("RES_VER_PID")
			.references("HFJ_RES_VER", "PID");
		resVerProv.addColumn("RES_PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		resVerProv
			.addForeignKey("FK_RESVERPROV_RES_PID")
			.toColumn("RES_PID")
			.references("HFJ_RESOURCE", "RES_ID");
		resVerProv.addColumn("SOURCE_URI").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 100);
		resVerProv.addColumn("REQUEST_ID").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 16);
		resVerProv.addIndex("IDX_RESVERPROV_SOURCEURI").unique(false).withColumns("SOURCE_URI");
		resVerProv.addIndex("IDX_RESVERPROV_REQUESTID").unique(false).withColumns("REQUEST_ID");

		// TermValueSetConceptDesignation
		version.startSectionWithMessage("Processing bulkExportCollectionFile: TRM_VALUESET_C_DESIGNATION");
		Builder.BuilderWithTableName termValueSetConceptDesignationTable = version.onTable("TRM_VALUESET_C_DESIGNATION");
		termValueSetConceptDesignationTable.addColumn("VALUESET_PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		termValueSetConceptDesignationTable
			.addForeignKey("FK_TRM_VSCD_VS_PID")
			.toColumn("VALUESET_PID")
			.references("TRM_VALUESET", "PID");

		// Drop HFJ_SEARCH_RESULT foreign keys
		version.onTable("HFJ_SEARCH_RESULT").dropForeignKey("FK_SEARCHRES_RES", "HFJ_RESOURCE");
		version.onTable("HFJ_SEARCH_RESULT").dropForeignKey("FK_SEARCHRES_SEARCH", "HFJ_SEARCH");

		// TermValueSet
		version.startSectionWithMessage("Processing bulkExportCollectionFile: TRM_VALUESET");
		Builder.BuilderWithTableName termValueSetTable = version.onTable("TRM_VALUESET");
		termValueSetTable.addColumn("TOTAL_CONCEPTS").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		termValueSetTable.addColumn("TOTAL_CONCEPT_DESIGNATIONS").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		termValueSetTable
			.dropIndex("IDX_VALUESET_EXP_STATUS");

		version.dropIdGenerator("SEQ_SEARCHPARM_ID");

		// TermValueSetConcept
		version.startSectionWithMessage("Processing bulkExportCollectionFile: TRM_VALUESET_CONCEPT");
		Builder.BuilderWithTableName termValueSetConceptTable = version.onTable("TRM_VALUESET_CONCEPT");
		termValueSetConceptTable.addColumn("VALUESET_ORDER").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.INT);
		termValueSetConceptTable
			.addIndex("IDX_VS_CONCEPT_ORDER")
			.unique(true)
			.withColumns("VALUESET_PID", "VALUESET_ORDER");

		// Account for RESTYPE_LEN column increasing from 30 to 40
		version.onTable("HFJ_RESOURCE").modifyColumn("RES_TYPE").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 40);
		version.onTable("HFJ_RES_VER").modifyColumn("RES_TYPE").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 40);
		version.onTable("HFJ_HISTORY_TAG").modifyColumn("RES_TYPE").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 40);
		version.onTable("HFJ_RES_LINK").modifyColumn("SOURCE_RESOURCE_TYPE").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 40);
		version.onTable("HFJ_RES_LINK").modifyColumn("TARGET_RESOURCE_TYPE").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 40);
		version.onTable("HFJ_RES_TAG").modifyColumn("RES_TYPE").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 40);

		// TermConceptDesignation
		version.startSectionWithMessage("Processing table: TRM_CONCEPT_DESIG");
		version.onTable("TRM_CONCEPT_DESIG").modifyColumn("VAL").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 2000);

		// TermValueSetConceptDesignation
		version.startSectionWithMessage("Processing table: TRM_VALUESET_C_DESIGNATION");
		version.onTable("TRM_VALUESET_C_DESIGNATION").modifyColumn("VAL").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 2000);

		// TermConceptProperty
		version.startSectionWithMessage("Processing table: TRM_CONCEPT_PROPERTY");
		version.onTable("TRM_CONCEPT_PROPERTY").addColumn("PROP_VAL_LOB").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.BLOB);
	}

	protected void init400() {
		Builder version = forVersion(VersionEnum.V4_0_0);

		// BinaryStorageEntity
		Builder.BuilderAddTableByColumns binaryBlob = version.addTableByColumns("HFJ_BINARY_STORAGE_BLOB", "BLOB_ID");
		binaryBlob.addColumn("BLOB_ID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
		binaryBlob.addColumn("RESOURCE_ID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 100);
		binaryBlob.addColumn("BLOB_SIZE").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.INT);
		binaryBlob.addColumn("CONTENT_TYPE").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 100);
		binaryBlob.addColumn("BLOB_DATA").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.BLOB);
		binaryBlob.addColumn("PUBLISHED_DATE").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.DATE_TIMESTAMP);
		binaryBlob.addColumn("BLOB_HASH").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 128);

		// Interim builds used this name
		version.onTable("TRM_VALUESET_CODE").dropThisTable();

		version.onTable("TRM_CONCEPT_MAP_GROUP")
			.renameColumn("myConceptMapUrl", "CONCEPT_MAP_URL", false, true)
			.renameColumn("mySourceValueSet", "SOURCE_VS", false, true)
			.renameColumn("myTargetValueSet", "TARGET_VS", false, true);
		version.onTable("TRM_CONCEPT_MAP_GROUP")
			.modifyColumn("CONCEPT_MAP_URL").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GROUP")
			.modifyColumn("SOURCE_VERSION").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GROUP")
			.modifyColumn("SOURCE_VS").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GROUP")
			.modifyColumn("TARGET_VERSION").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GROUP")
			.modifyColumn("TARGET_VS").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);

		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
			.renameColumn("myConceptMapUrl", "CONCEPT_MAP_URL", false, true)
			.renameColumn("mySystem", "SYSTEM_URL", false, true)
			.renameColumn("mySystemVersion", "SYSTEM_VERSION", false, true)
			.renameColumn("myValueSet", "VALUESET_URL", false, true);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
			.modifyColumn("CONCEPT_MAP_URL").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
			.modifyColumn("SOURCE_CODE").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 500);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
			.modifyColumn("SYSTEM_URL").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
			.modifyColumn("SYSTEM_VERSION").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
			.modifyColumn("VALUESET_URL").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);

		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
			.renameColumn("myConceptMapUrl", "CONCEPT_MAP_URL", false, true)
			.renameColumn("mySystem", "SYSTEM_URL", false, true)
			.renameColumn("mySystemVersion", "SYSTEM_VERSION", false, true)
			.renameColumn("myValueSet", "VALUESET_URL", false, true);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
			.modifyColumn("CONCEPT_MAP_URL").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
			.modifyColumn("SYSTEM_URL").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
			.modifyColumn("SYSTEM_VERSION").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
			.modifyColumn("TARGET_CODE").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 500);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
			.modifyColumn("VALUESET_URL").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);

		version.onTable("TRM_CONCEPT")
			.renameColumn("CODE", "CODEVAL", false, true);



		// TermValueSet
		version.startSectionWithMessage("Processing table: TRM_VALUESET");
		version.addIdGenerator("SEQ_VALUESET_PID");
		Builder.BuilderAddTableByColumns termValueSetTable = version.addTableByColumns("TRM_VALUESET", "PID");
		termValueSetTable.addColumn("PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		termValueSetTable.addColumn("URL").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
		termValueSetTable
			.addIndex("IDX_VALUESET_URL")
			.unique(true)
			.withColumns("URL");
		termValueSetTable.addColumn("RES_ID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		termValueSetTable
			.addForeignKey("FK_TRMVALUESET_RES")
			.toColumn("RES_ID")
			.references("HFJ_RESOURCE", "RES_ID");
		termValueSetTable.addColumn("NAME").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);

		version.onTable("TRM_VALUESET")
			.renameColumn("NAME", "VSNAME", true, true);
		version.onTable("TRM_VALUESET")
			.modifyColumn("RES_ID").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);

		Builder.BuilderWithTableName termValueSetTableChange = version.onTable("TRM_VALUESET");
		termValueSetTableChange.addColumn("EXPANSION_STATUS").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 50);
		termValueSetTableChange
			.addIndex("IDX_VALUESET_EXP_STATUS")
			.unique(false)
			.withColumns("EXPANSION_STATUS");

		// TermValueSetConcept
		version.startSectionWithMessage("Processing table: TRM_VALUESET_CONCEPT");
		version.addIdGenerator("SEQ_VALUESET_CONCEPT_PID");
		Builder.BuilderAddTableByColumns termValueSetConceptTable = version.addTableByColumns("TRM_VALUESET_CONCEPT", "PID");
		termValueSetConceptTable.addColumn("PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		termValueSetConceptTable.addColumn("VALUESET_PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		termValueSetConceptTable
			.addForeignKey("FK_TRM_VALUESET_PID")
			.toColumn("VALUESET_PID")
			.references("TRM_VALUESET", "PID");
		termValueSetConceptTable.addColumn("SYSTEM_URL").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
		termValueSetConceptTable.addColumn("CODEVAL").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 500);
		termValueSetConceptTable.addColumn("DISPLAY").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 400);
		version.onTable("TRM_VALUESET_CONCEPT")
			.renameColumn("CODE", "CODEVAL", true, true)
			.renameColumn("SYSTEM", "SYSTEM_URL", true, true);

		version.startSectionWithMessage("Processing table: TRM_VALUESET_CONCEPT, swapping index for unique constraint");
		termValueSetConceptTable.dropIndex("IDX_VALUESET_CONCEPT_CS_CD");
		termValueSetConceptTable
			.addIndex("IDX_VS_CONCEPT_CS_CD")
			.unique(true)
			.withColumns("VALUESET_PID", "SYSTEM_URL", "CODEVAL");

		// TermValueSetConceptDesignation
		version.startSectionWithMessage("Processing table: TRM_VALUESET_C_DESIGNATION");
		version.addIdGenerator("SEQ_VALUESET_C_DSGNTN_PID");
		Builder.BuilderAddTableByColumns termValueSetConceptDesignationTable = version.addTableByColumns("TRM_VALUESET_C_DESIGNATION", "PID");
		termValueSetConceptDesignationTable.addColumn("PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		termValueSetConceptDesignationTable.addColumn("VALUESET_CONCEPT_PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		termValueSetConceptDesignationTable
			.addForeignKey("FK_TRM_VALUESET_CONCEPT_PID")
			.toColumn("VALUESET_CONCEPT_PID")
			.references("TRM_VALUESET_CONCEPT", "PID");
		termValueSetConceptDesignationTable.addColumn("LANG").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 500);
		termValueSetConceptDesignationTable.addColumn("USE_SYSTEM").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 500);
		termValueSetConceptDesignationTable.addColumn("USE_CODE").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 500);
		termValueSetConceptDesignationTable.addColumn("USE_DISPLAY").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 500);
		termValueSetConceptDesignationTable.addColumn("VAL").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 500);
		termValueSetConceptDesignationTable
			.addIndex("IDX_VALUESET_C_DSGNTN_VAL")
			.unique(false)
			.withColumns("VAL");

		// TermCodeSystemVersion
		version.startSectionWithMessage("Processing table: TRM_CODESYSTEM_VER");
		Builder.BuilderWithTableName termCodeSystemVersionTable = version.onTable("TRM_CODESYSTEM_VER");
		termCodeSystemVersionTable.addColumn("CS_DISPLAY").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);

		// ResourceReindexJobEntry
		version.addIdGenerator("SEQ_RES_REINDEX_JOB");
		Builder.BuilderAddTableByColumns reindex = version.addTableByColumns("HFJ_RES_REINDEX_JOB", "PID");
		reindex.addColumn("PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		reindex.addColumn("RES_TYPE").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 100);
		reindex.addColumn("UPDATE_THRESHOLD_HIGH").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.DATE_TIMESTAMP);
		reindex.addColumn("JOB_DELETED").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.BOOLEAN);
		reindex.addColumn("UPDATE_THRESHOLD_LOW").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.DATE_TIMESTAMP);
		reindex.addColumn("SUSPENDED_UNTIL").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.DATE_TIMESTAMP);
		reindex.addColumn("REINDEX_COUNT").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.INT);

		// Search
		version.onTable("HFJ_SEARCH")
			.addColumn("SEARCH_DELETED").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.BOOLEAN);
		version.onTable("HFJ_SEARCH")
			.modifyColumn("SEARCH_LAST_RETURNED").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.DATE_TIMESTAMP);
		version.onTable("HFJ_SEARCH")
			.addColumn("SEARCH_PARAM_MAP").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.BLOB);
		version.onTable("HFJ_SEARCH")
			.modifyColumn("SEARCH_UUID").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 36);

		version.onTable("HFJ_SEARCH_PARM").dropThisTable();

		version.onTable("HFJ_SPIDX_COORDS").modifyColumn("RES_TYPE").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 100);
		version.onTable("HFJ_SPIDX_DATE").modifyColumn("RES_TYPE").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 100);
		version.onTable("HFJ_SPIDX_STRING").modifyColumn("RES_TYPE").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 100);
		version.onTable("HFJ_SPIDX_STRING").addColumn("HASH_IDENTITY").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		version.onTable("HFJ_SPIDX_STRING").addIndex("IDX_SP_STRING_HASH_IDENT").unique(false).withColumns("HASH_IDENTITY");
		version.onTable("HFJ_SPIDX_COORDS").modifyColumn("RES_TYPE").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 100);
		version.onTable("HFJ_SPIDX_QUANTITY").modifyColumn("RES_TYPE").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 100);
		version.onTable("HFJ_SPIDX_QUANTITY").dropColumn("HASH_UNITS_AND_VALPREFIX");
		version.onTable("HFJ_SPIDX_QUANTITY").dropColumn("HASH_VALPREFIX");
		version.onTable("HFJ_SPIDX_NUMBER").modifyColumn("RES_TYPE").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 100);
		version.onTable("HFJ_SPIDX_TOKEN").modifyColumn("RES_TYPE").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 100);
		version.onTable("HFJ_SPIDX_URI").modifyColumn("RES_TYPE").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 100);
		version.onTable("HFJ_SPIDX_URI").modifyColumn("SP_URI").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 254);

		version.onTable("TRM_CODESYSTEM").modifyColumn("CODE_SYSTEM_URI").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CODESYSTEM").modifyColumn("CS_NAME").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CODESYSTEM_VER").modifyColumn("CS_VERSION_ID").nullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
	}


	private void init360() {
		Builder version = forVersion(VersionEnum.V3_6_0);

		// Resource Link
		Builder.BuilderWithTableName resourceLink = version.onTable("HFJ_RES_LINK");
		version.startSectionWithMessage("Starting work on table: " + resourceLink.getTableName());
		resourceLink
			.modifyColumn("SRC_PATH")
			.nonNullable()
			.withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);

		// Search
		Builder.BuilderWithTableName search = version.onTable("HFJ_SEARCH");
		version.startSectionWithMessage("Starting work on table: " + search.getTableName());
		search
			.addColumn("OPTLOCK_VERSION")
			.nullable()
			.type(BaseTableColumnTypeTask.ColumnTypeEnum.INT);

		version.addTableRawSql("HFJ_RES_REINDEX_JOB")
			.addSql(DriverTypeEnum.MSSQL_2012, "create table HFJ_RES_REINDEX_JOB (PID bigint not null, JOB_DELETED bit not null, RES_TYPE varchar(255), SUSPENDED_UNTIL datetime2, UPDATE_THRESHOLD_HIGH datetime2 not null, UPDATE_THRESHOLD_LOW datetime2, primary key (PID))")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "create table HFJ_RES_REINDEX_JOB (PID bigint not null, JOB_DELETED boolean not null, RES_TYPE varchar(255), SUSPENDED_UNTIL timestamp, UPDATE_THRESHOLD_HIGH timestamp not null, UPDATE_THRESHOLD_LOW timestamp, primary key (PID))")
			.addSql(DriverTypeEnum.MARIADB_10_1, "create table HFJ_RES_REINDEX_JOB (PID bigint not null, JOB_DELETED bit not null, RES_TYPE varchar(255), SUSPENDED_UNTIL datetime(6), UPDATE_THRESHOLD_HIGH datetime(6) not null, UPDATE_THRESHOLD_LOW datetime(6), primary key (PID))")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "create table HFJ_RES_REINDEX_JOB (PID int8 not null, JOB_DELETED boolean not null, RES_TYPE varchar(255), SUSPENDED_UNTIL timestamp, UPDATE_THRESHOLD_HIGH timestamp not null, UPDATE_THRESHOLD_LOW timestamp, primary key (PID))")
			.addSql(DriverTypeEnum.MYSQL_5_7, " create table HFJ_RES_REINDEX_JOB (PID bigint not null, JOB_DELETED bit not null, RES_TYPE varchar(255), SUSPENDED_UNTIL datetime(6), UPDATE_THRESHOLD_HIGH datetime(6) not null, UPDATE_THRESHOLD_LOW datetime(6), primary key (PID))")
			.addSql(DriverTypeEnum.ORACLE_12C, "create table HFJ_RES_REINDEX_JOB (PID number(19,0) not null, JOB_DELETED number(1,0) not null, RES_TYPE varchar2(255 char), SUSPENDED_UNTIL timestamp, UPDATE_THRESHOLD_HIGH timestamp not null, UPDATE_THRESHOLD_LOW timestamp, primary key (PID))");

		version.onTable("TRM_CONCEPT_DESIG").addColumn("CS_VER_PID").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		version.onTable("TRM_CONCEPT_DESIG").addForeignKey("FK_CONCEPTDESIG_CSV").toColumn("CS_VER_PID").references("TRM_CODESYSTEM_VER", "PID");

		version.onTable("TRM_CONCEPT_PROPERTY").addColumn("CS_VER_PID").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		version.onTable("TRM_CONCEPT_PROPERTY").addForeignKey("FK_CONCEPTPROP_CSV").toColumn("CS_VER_PID").references("TRM_CODESYSTEM_VER", "PID");

		version.onTable("TRM_CONCEPT").addColumn("PARENT_PIDS").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.CLOB);

	}

	private void init350() {
		Builder version = forVersion(VersionEnum.V3_5_0);

		// Forced ID changes
		Builder.BuilderWithTableName forcedId = version.onTable("HFJ_FORCED_ID");
		version.startSectionWithMessage("Starting work on table: " + forcedId.getTableName());

		forcedId
			.dropIndex("IDX_FORCEDID_TYPE_FORCEDID");
		forcedId
			.dropIndex("IDX_FORCEDID_TYPE_RESID");

		forcedId
			.addIndex("IDX_FORCEDID_TYPE_FID")
			.unique(true)
			.withColumns("RESOURCE_TYPE", "FORCED_ID");

		// Indexes - Coords
		Builder.BuilderWithTableName spidxCoords = version.onTable("HFJ_SPIDX_COORDS");
		version.startSectionWithMessage("Starting work on table: " + spidxCoords.getTableName());
		spidxCoords
			.addColumn("HASH_IDENTITY")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxCoords
				.dropIndex("IDX_SP_COORDS");
			spidxCoords
				.addIndex("IDX_SP_COORDS_HASH")
				.unique(false)
				.withColumns("HASH_IDENTITY", "SP_LATITUDE", "SP_LONGITUDE");
			spidxCoords
				.addTask(new CalculateHashesTask()
					.setColumnName("HASH_IDENTITY")
					.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(t.getResourceType(), t.getString("SP_NAME")))
				);
		}

		// Indexes - Date
		Builder.BuilderWithTableName spidxDate = version.onTable("HFJ_SPIDX_DATE");
		version.startSectionWithMessage("Starting work on table: " + spidxDate.getTableName());
		spidxDate
			.addColumn("HASH_IDENTITY")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxDate
				.dropIndex("IDX_SP_TOKEN");
			spidxDate
				.addIndex("IDX_SP_DATE_HASH")
				.unique(false)
				.withColumns("HASH_IDENTITY", "SP_VALUE_LOW", "SP_VALUE_HIGH");
			spidxDate
				.dropIndex("IDX_SP_DATE");
			spidxDate
				.addTask(new CalculateHashesTask()
					.setColumnName("HASH_IDENTITY")
					.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(t.getResourceType(), t.getString("SP_NAME")))
				);
		}

		// Indexes - Number
		Builder.BuilderWithTableName spidxNumber = version.onTable("HFJ_SPIDX_NUMBER");
		version.startSectionWithMessage("Starting work on table: " + spidxNumber.getTableName());
		spidxNumber
			.addColumn("HASH_IDENTITY")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxNumber
				.dropIndex("IDX_SP_NUMBER");
			spidxNumber
				.addIndex("IDX_SP_NUMBER_HASH_VAL")
				.unique(false)
				.withColumns("HASH_IDENTITY", "SP_VALUE");
			spidxNumber
				.addTask(new CalculateHashesTask()
					.setColumnName("HASH_IDENTITY")
					.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(t.getResourceType(), t.getString("SP_NAME")))
				);
		}

		// Indexes - Quantity
		Builder.BuilderWithTableName spidxQuantity = version.onTable("HFJ_SPIDX_QUANTITY");
		version.startSectionWithMessage("Starting work on table: " + spidxQuantity.getTableName());
		spidxQuantity
			.addColumn("HASH_IDENTITY")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		spidxQuantity
			.addColumn("HASH_IDENTITY_SYS_UNITS")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		spidxQuantity
			.addColumn("HASH_IDENTITY_AND_UNITS")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxQuantity
				.dropIndex("IDX_SP_QUANTITY");
			spidxQuantity
				.addIndex("IDX_SP_QUANTITY_HASH")
				.unique(false)
				.withColumns("HASH_IDENTITY", "SP_VALUE");
			spidxQuantity
				.addIndex("IDX_SP_QUANTITY_HASH_UN")
				.unique(false)
				.withColumns("HASH_IDENTITY_AND_UNITS", "SP_VALUE");
			spidxQuantity
				.addIndex("IDX_SP_QUANTITY_HASH_SYSUN")
				.unique(false)
				.withColumns("HASH_IDENTITY_SYS_UNITS", "SP_VALUE");
			spidxQuantity
				.addTask(new CalculateHashesTask()
					.setColumnName("HASH_IDENTITY")
					.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(t.getResourceType(), t.getString("SP_NAME")))
					.addCalculator("HASH_IDENTITY_AND_UNITS", t -> ResourceIndexedSearchParamQuantity.calculateHashUnits(t.getResourceType(), t.getString("SP_NAME"), t.getString("SP_UNITS")))
					.addCalculator("HASH_IDENTITY_SYS_UNITS", t -> ResourceIndexedSearchParamQuantity.calculateHashSystemAndUnits(t.getResourceType(), t.getString("SP_NAME"), t.getString("SP_SYSTEM"), t.getString("SP_UNITS")))
				);
		}

		// Indexes - String
		Builder.BuilderWithTableName spidxString = version.onTable("HFJ_SPIDX_STRING");
		version.startSectionWithMessage("Starting work on table: " + spidxString.getTableName());
		spidxString
			.addColumn("HASH_NORM_PREFIX")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxString
				.dropIndex("IDX_SP_STRING");
			spidxString
				.addIndex("IDX_SP_STRING_HASH_NRM")
				.unique(false)
				.withColumns("HASH_NORM_PREFIX", "SP_VALUE_NORMALIZED");
			spidxString
				.addColumn("HASH_EXACT")
				.nullable()
				.type(AddColumnTask.ColumnTypeEnum.LONG);
			spidxString
				.addIndex("IDX_SP_STRING_HASH_EXCT")
				.unique(false)
				.withColumns("HASH_EXACT");
			spidxString
				.addTask(new CalculateHashesTask()
					.setColumnName("HASH_NORM_PREFIX")
					.addCalculator("HASH_NORM_PREFIX", t -> ResourceIndexedSearchParamString.calculateHashNormalized(new ModelConfig(), t.getResourceType(), t.getString("SP_NAME"), t.getString("SP_VALUE_NORMALIZED")))
					.addCalculator("HASH_EXACT", t -> ResourceIndexedSearchParamString.calculateHashExact(t.getResourceType(), t.getParamName(), t.getString("SP_VALUE_EXACT")))
				);
		}

		// Indexes - Token
		Builder.BuilderWithTableName spidxToken = version.onTable("HFJ_SPIDX_TOKEN");
		version.startSectionWithMessage("Starting work on table: " + spidxToken.getTableName());
		spidxToken
			.addColumn("HASH_IDENTITY")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		spidxToken
			.addColumn("HASH_SYS")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		spidxToken
			.addColumn("HASH_SYS_AND_VALUE")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		spidxToken
			.addColumn("HASH_VALUE")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxToken
				.dropIndex("IDX_SP_TOKEN");
			spidxToken
				.dropIndex("IDX_SP_TOKEN_UNQUAL");
			spidxToken
				.addIndex("IDX_SP_TOKEN_HASH")
				.unique(false)
				.withColumns("HASH_IDENTITY");
			spidxToken
				.addIndex("IDX_SP_TOKEN_HASH_S")
				.unique(false)
				.withColumns("HASH_SYS");
			spidxToken
				.addIndex("IDX_SP_TOKEN_HASH_SV")
				.unique(false)
				.withColumns("HASH_SYS_AND_VALUE");
			spidxToken
				.addIndex("IDX_SP_TOKEN_HASH_V")
				.unique(false)
				.withColumns("HASH_VALUE");
			spidxToken
				.addTask(new CalculateHashesTask()
					.setColumnName("HASH_IDENTITY")
					.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(t.getResourceType(), t.getString("SP_NAME")))
					.addCalculator("HASH_SYS", t -> ResourceIndexedSearchParamToken.calculateHashSystem(t.getResourceType(), t.getParamName(), t.getString("SP_SYSTEM")))
					.addCalculator("HASH_SYS_AND_VALUE", t -> ResourceIndexedSearchParamToken.calculateHashSystemAndValue(t.getResourceType(), t.getParamName(), t.getString("SP_SYSTEM"), t.getString("SP_VALUE")))
					.addCalculator("HASH_VALUE", t -> ResourceIndexedSearchParamToken.calculateHashValue(t.getResourceType(), t.getParamName(), t.getString("SP_VALUE")))
				);
		}

		// Indexes - URI
		Builder.BuilderWithTableName spidxUri = version.onTable("HFJ_SPIDX_URI");
		version.startSectionWithMessage("Starting work on table: " + spidxUri.getTableName());
		spidxUri
			.addColumn("HASH_IDENTITY")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxUri
				.addIndex("IDX_SP_URI_HASH_IDENTITY")
				.unique(false)
				.withColumns("HASH_IDENTITY", "SP_URI");
			spidxUri
				.addColumn("HASH_URI")
				.nullable()
				.type(AddColumnTask.ColumnTypeEnum.LONG);
			spidxUri
				.addIndex("IDX_SP_URI_HASH_URI")
				.unique(false)
				.withColumns("HASH_URI");
			spidxUri
				.addTask(new CalculateHashesTask()
					.setColumnName("HASH_IDENTITY")
					.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(t.getResourceType(), t.getString("SP_NAME")))
					.addCalculator("HASH_URI", t -> ResourceIndexedSearchParamUri.calculateHashUri(t.getResourceType(), t.getString("SP_NAME"), t.getString("SP_URI")))
				);
		}

		// Search Parameter Presence
		Builder.BuilderWithTableName spp = version.onTable("HFJ_RES_PARAM_PRESENT");
		version.startSectionWithMessage("Starting work on table: " + spp.getTableName());
		spp.dropIndex("IDX_RESPARMPRESENT_SPID_RESID");
		spp
			.addColumn("HASH_PRESENCE")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		spp
			.addIndex("IDX_RESPARMPRESENT_HASHPRES")
			.unique(false)
			.withColumns("HASH_PRESENCE");

		ArbitrarySqlTask consolidateSearchParamPresenceIndexesTask = new ArbitrarySqlTask("HFJ_SEARCH_PARM", "Consolidate search parameter presence indexes");
		consolidateSearchParamPresenceIndexesTask.setExecuteOnlyIfTableExists("HFJ_SEARCH_PARM");
		consolidateSearchParamPresenceIndexesTask.setBatchSize(1);

		String sql = "SELECT " +
			"HFJ_SEARCH_PARM.RES_TYPE RES_TYPE, HFJ_SEARCH_PARM.PARAM_NAME PARAM_NAME, " +
			"HFJ_RES_PARAM_PRESENT.PID PID, HFJ_RES_PARAM_PRESENT.SP_ID SP_ID, HFJ_RES_PARAM_PRESENT.SP_PRESENT SP_PRESENT, HFJ_RES_PARAM_PRESENT.HASH_PRESENCE HASH_PRESENCE " +
			"from HFJ_RES_PARAM_PRESENT " +
			"join HFJ_SEARCH_PARM ON (HFJ_SEARCH_PARM.PID = HFJ_RES_PARAM_PRESENT.SP_ID) " +
			"where HFJ_RES_PARAM_PRESENT.HASH_PRESENCE is null";
		consolidateSearchParamPresenceIndexesTask.addExecuteOnlyIfColumnExists("HFJ_RES_PARAM_PRESENT", "SP_ID");
		consolidateSearchParamPresenceIndexesTask.addQuery(sql, ArbitrarySqlTask.QueryModeEnum.BATCH_UNTIL_NO_MORE, t -> {
			Number pid = (Number) t.get("PID");
			Boolean present = columnToBoolean(t.get("SP_PRESENT"));
			String resType = (String) t.get("RES_TYPE");
			String paramName = (String) t.get("PARAM_NAME");
			Long hash = SearchParamPresent.calculateHashPresence(resType, paramName, present);
			consolidateSearchParamPresenceIndexesTask.executeSql("HFJ_RES_PARAM_PRESENT", "update HFJ_RES_PARAM_PRESENT set HASH_PRESENCE = ? where PID = ?", hash, pid);
		});
		version.addTask(consolidateSearchParamPresenceIndexesTask);

		// SP_ID is no longer needed
		spp.dropColumn("SP_ID");

		// Concept
		Builder.BuilderWithTableName trmConcept = version.onTable("TRM_CONCEPT");
		version.startSectionWithMessage("Starting work on table: " + trmConcept.getTableName());
		trmConcept
			.addColumn("CONCEPT_UPDATED")
			.nullable()
			.type(BaseTableColumnTypeTask.ColumnTypeEnum.DATE_TIMESTAMP);
		trmConcept
			.addIndex("IDX_CONCEPT_UPDATED")
			.unique(false)
			.withColumns("CONCEPT_UPDATED");
		trmConcept
			.modifyColumn("CODE")
			.nonNullable()
			.withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 500);

		// Concept Designation
		version.startSectionWithMessage("Starting work on table: TRM_CONCEPT_DESIG");
		version
			.addTableRawSql("TRM_CONCEPT_DESIG")
			.addSql(DriverTypeEnum.H2_EMBEDDED, "create table TRM_CONCEPT_DESIG (PID bigint not null, LANG varchar(500), USE_CODE varchar(500), USE_DISPLAY varchar(500), USE_SYSTEM varchar(500), VAL varchar(500) not null, CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID))")
			.addSql(DriverTypeEnum.H2_EMBEDDED, "alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
			.addSql(DriverTypeEnum.H2_EMBEDDED, "alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "create table TRM_CONCEPT_DESIG (PID bigint not null, LANG varchar(500), USE_CODE varchar(500), USE_DISPLAY varchar(500), USE_SYSTEM varchar(500), VAL varchar(500) not null, CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID))")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT")
			.addSql(DriverTypeEnum.MYSQL_5_7, "create table TRM_CONCEPT_DESIG (PID bigint not null, LANG varchar(500), USE_CODE varchar(500), USE_DISPLAY varchar(500), USE_SYSTEM varchar(500), VAL varchar(500) not null, CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID)) ENGINE=InnoDB")
			.addSql(DriverTypeEnum.MYSQL_5_7, "alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER (PID)")
			.addSql(DriverTypeEnum.MYSQL_5_7, "alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT (PID)")
			.addSql(DriverTypeEnum.MARIADB_10_1, "create table TRM_CONCEPT_DESIG (PID bigint not null, LANG varchar(500), USE_CODE varchar(500), USE_DISPLAY varchar(500), USE_SYSTEM varchar(500), VAL varchar(500) not null, CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID))")
			.addSql(DriverTypeEnum.MARIADB_10_1, "alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER (PID)")
			.addSql(DriverTypeEnum.MARIADB_10_1, "alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT (PID)")
			.addSql(DriverTypeEnum.ORACLE_12C, "create table TRM_CONCEPT_DESIG (PID number(19,0) not null, LANG varchar2(500 char), USE_CODE varchar2(500 char), USE_DISPLAY varchar2(500 char), USE_SYSTEM varchar2(500 char), VAL varchar2(500 char) not null, CS_VER_PID number(19,0), CONCEPT_PID number(19,0), primary key (PID))")
			.addSql(DriverTypeEnum.ORACLE_12C, "alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
			.addSql(DriverTypeEnum.ORACLE_12C, "alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "create table TRM_CONCEPT_DESIG (PID int8 not null, LANG varchar(500), USE_CODE varchar(500), USE_DISPLAY varchar(500), USE_SYSTEM varchar(500), VAL varchar(500) not null, CS_VER_PID int8, CONCEPT_PID int8, primary key (PID))")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT")
			.addSql(DriverTypeEnum.MSSQL_2012, "create table TRM_CONCEPT_DESIG (PID bigint not null, LANG varchar(500), USE_CODE varchar(500), USE_DISPLAY varchar(500), USE_SYSTEM varchar(500), VAL varchar(500) not null, CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID))")
			.addSql(DriverTypeEnum.MSSQL_2012, "alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
			.addSql(DriverTypeEnum.MSSQL_2012, "alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT");

		// Concept Property
		version.startSectionWithMessage("Starting work on table: TRM_CONCEPT_PROPERTY");
		version
			.addTableRawSql("TRM_CONCEPT_PROPERTY")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "create table TRM_CONCEPT_PROPERTY (PID bigint not null, PROP_CODESYSTEM varchar(500), PROP_DISPLAY varchar(500), PROP_KEY varchar(500) not null, PROP_TYPE integer not null, PROP_VAL varchar(500), CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID))")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT")
			.addSql(DriverTypeEnum.MARIADB_10_1, "create table TRM_CONCEPT_PROPERTY (PID bigint not null, PROP_CODESYSTEM varchar(500), PROP_DISPLAY varchar(500), PROP_KEY varchar(500) not null, PROP_TYPE integer not null, PROP_VAL varchar(500), CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID))")
			.addSql(DriverTypeEnum.MARIADB_10_1, "alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER (PID)")
			.addSql(DriverTypeEnum.MARIADB_10_1, "alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT (PID)")
			.addSql(DriverTypeEnum.MYSQL_5_7, "create table TRM_CONCEPT_PROPERTY (PID bigint not null, PROP_CODESYSTEM varchar(500), PROP_DISPLAY varchar(500), PROP_KEY varchar(500) not null, PROP_TYPE integer not null, PROP_VAL varchar(500), CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID))")
			.addSql(DriverTypeEnum.MYSQL_5_7, "alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER (PID)")
			.addSql(DriverTypeEnum.MYSQL_5_7, "alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT (PID)")
			.addSql(DriverTypeEnum.ORACLE_12C, "create table TRM_CONCEPT_PROPERTY (PID number(19,0) not null, PROP_CODESYSTEM varchar2(500 char), PROP_DISPLAY varchar2(500 char), PROP_KEY varchar2(500 char) not null, PROP_TYPE number(10,0) not null, PROP_VAL varchar2(500 char), CS_VER_PID number(19,0), CONCEPT_PID number(19,0), primary key (PID))")
			.addSql(DriverTypeEnum.ORACLE_12C, "alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
			.addSql(DriverTypeEnum.ORACLE_12C, "alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "create table TRM_CONCEPT_PROPERTY (PID int8 not null, PROP_CODESYSTEM varchar(500), PROP_DISPLAY varchar(500), PROP_KEY varchar(500) not null, PROP_TYPE int4 not null, PROP_VAL varchar(500), CS_VER_PID int8, CONCEPT_PID int8, primary key (PID))")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT")
			.addSql(DriverTypeEnum.MSSQL_2012, "create table TRM_CONCEPT_PROPERTY (PID bigint not null, PROP_CODESYSTEM varchar(500), PROP_DISPLAY varchar(500), PROP_KEY varchar(500) not null, PROP_TYPE int not null, PROP_VAL varchar(500), CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID))")
			.addSql(DriverTypeEnum.MSSQL_2012, "alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
			.addSql(DriverTypeEnum.MSSQL_2012, "alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT");

		// Concept Map - Map
		version.startSectionWithMessage("Starting work on table: TRM_CONCEPT_MAP");
		version
			.addTableRawSql("TRM_CONCEPT_MAP")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "create table TRM_CONCEPT_MAP (PID bigint not null, RES_ID bigint, SOURCE_URL varchar(200), TARGET_URL varchar(200), URL varchar(200) not null, primary key (PID))")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "alter table TRM_CONCEPT_MAP add constraint FK_TRMCONCEPTMAP_RES foreign key (RES_ID) references HFJ_RESOURCE")
			.addSql(DriverTypeEnum.MYSQL_5_7, "create table TRM_CONCEPT_MAP (PID bigint not null, RES_ID bigint, SOURCE_URL varchar(200), TARGET_URL varchar(200), URL varchar(200) not null, primary key (PID))")
			.addSql(DriverTypeEnum.MYSQL_5_7, "alter table TRM_CONCEPT_MAP add constraint IDX_CONCEPT_MAP_URL unique (URL)")
			.addSql(DriverTypeEnum.MYSQL_5_7, "alter table TRM_CONCEPT_MAP add constraint FK_TRMCONCEPTMAP_RES foreign key (RES_ID) references HFJ_RESOURCE (RES_ID)")
			.addSql(DriverTypeEnum.ORACLE_12C, "create table TRM_CONCEPT_MAP (PID number(19,0) not null, RES_ID number(19,0), SOURCE_URL varchar2(200 char), TARGET_URL varchar2(200 char), URL varchar2(200 char) not null, primary key (PID))")
			.addSql(DriverTypeEnum.ORACLE_12C, "alter table TRM_CONCEPT_MAP add constraint IDX_CONCEPT_MAP_URL unique (URL)")
			.addSql(DriverTypeEnum.ORACLE_12C, "alter table TRM_CONCEPT_MAP add constraint FK_TRMCONCEPTMAP_RES foreign key (RES_ID) references HFJ_RESOURCE")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "create table TRM_CONCEPT_MAP (PID int8 not null, RES_ID int8, SOURCE_URL varchar(200), TARGET_URL varchar(200), URL varchar(200) not null, primary key (PID))")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "alter table TRM_CONCEPT_MAP add constraint FK_TRMCONCEPTMAP_RES foreign key (RES_ID) references HFJ_RESOURCE")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "alter table TRM_CONCEPT_MAP add constraint IDX_CONCEPT_MAP_URL unique (URL)")
			.addSql(DriverTypeEnum.MSSQL_2012, "create table TRM_CONCEPT_MAP (PID bigint not null, RES_ID bigint, SOURCE_URL varchar(200), TARGET_URL varchar(200), URL varchar(200) not null, primary key (PID))")
			.addSql(DriverTypeEnum.MSSQL_2012, "alter table TRM_CONCEPT_MAP add constraint IDX_CONCEPT_MAP_URL unique (URL)")
			.addSql(DriverTypeEnum.MSSQL_2012, "alter table TRM_CONCEPT_MAP add constraint FK_TRMCONCEPTMAP_RES foreign key (RES_ID) references HFJ_RESOURCE")
			.addSql(DriverTypeEnum.MARIADB_10_1, "create table TRM_CONCEPT_MAP (PID bigint not null, RES_ID bigint, SOURCE_URL varchar(200), TARGET_URL varchar(200), URL varchar(200) not null, primary key (PID))")
			.addSql(DriverTypeEnum.MARIADB_10_1, "alter table TRM_CONCEPT_MAP add constraint FK_TRMCONCEPTMAP_RES foreign key (RES_ID) references HFJ_RESOURCE (RES_ID)")
			.addSql(DriverTypeEnum.MARIADB_10_1, "alter table TRM_CONCEPT_MAP add constraint IDX_CONCEPT_MAP_URL unique (URL)");

		// Concept Map - Group
		version.startSectionWithMessage("Starting work on table: TRM_CONCEPT_MAP_GROUP");
		version
			.addTableRawSql("TRM_CONCEPT_MAP_GROUP")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "create table TRM_CONCEPT_MAP_GROUP (PID bigint not null, myConceptMapUrl varchar(255), SOURCE_URL varchar(200) not null, mySourceValueSet varchar(255), SOURCE_VERSION varchar(100), TARGET_URL varchar(200) not null, myTargetValueSet varchar(255), TARGET_VERSION varchar(100), CONCEPT_MAP_PID bigint not null, primary key (PID))")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "alter table TRM_CONCEPT_MAP_GROUP add constraint FK_TCMGROUP_CONCEPTMAP foreign key (CONCEPT_MAP_PID) references TRM_CONCEPT_MAP")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "create unique index IDX_CONCEPT_MAP_URL on TRM_CONCEPT_MAP (URL)")
			.addSql(DriverTypeEnum.ORACLE_12C, "create table TRM_CONCEPT_MAP_GROUP (PID number(19,0) not null, myConceptMapUrl varchar2(255 char), SOURCE_URL varchar2(200 char) not null, mySourceValueSet varchar2(255 char), SOURCE_VERSION varchar2(100 char), TARGET_URL varchar2(200 char) not null, myTargetValueSet varchar2(255 char), TARGET_VERSION varchar2(100 char), CONCEPT_MAP_PID number(19,0) not null, primary key (PID))")
			.addSql(DriverTypeEnum.ORACLE_12C, "alter table TRM_CONCEPT_MAP_GROUP add constraint FK_TCMGROUP_CONCEPTMAP foreign key (CONCEPT_MAP_PID) references TRM_CONCEPT_MAP")
			.addSql(DriverTypeEnum.MARIADB_10_1, "create table TRM_CONCEPT_MAP_GROUP (PID bigint not null, myConceptMapUrl varchar(255), SOURCE_URL varchar(200) not null, mySourceValueSet varchar(255), SOURCE_VERSION varchar(100), TARGET_URL varchar(200) not null, myTargetValueSet varchar(255), TARGET_VERSION varchar(100), CONCEPT_MAP_PID bigint not null, primary key (PID))")
			.addSql(DriverTypeEnum.MARIADB_10_1, "alter table TRM_CONCEPT_MAP_GROUP add constraint FK_TCMGROUP_CONCEPTMAP foreign key (CONCEPT_MAP_PID) references TRM_CONCEPT_MAP (PID)")
			.addSql(DriverTypeEnum.MYSQL_5_7, "create table TRM_CONCEPT_MAP_GROUP (PID bigint not null, myConceptMapUrl varchar(255), SOURCE_URL varchar(200) not null, mySourceValueSet varchar(255), SOURCE_VERSION varchar(100), TARGET_URL varchar(200) not null, myTargetValueSet varchar(255), TARGET_VERSION varchar(100), CONCEPT_MAP_PID bigint not null, primary key (PID))")
			.addSql(DriverTypeEnum.MYSQL_5_7, "alter table TRM_CONCEPT_MAP_GROUP add constraint FK_TCMGROUP_CONCEPTMAP foreign key (CONCEPT_MAP_PID) references TRM_CONCEPT_MAP (PID)")
			.addSql(DriverTypeEnum.MSSQL_2012, "create table TRM_CONCEPT_MAP_GROUP (PID bigint not null, myConceptMapUrl varchar(255), SOURCE_URL varchar(200) not null, mySourceValueSet varchar(255), SOURCE_VERSION varchar(100), TARGET_URL varchar(200) not null, myTargetValueSet varchar(255), TARGET_VERSION varchar(100), CONCEPT_MAP_PID bigint not null, primary key (PID))")
			.addSql(DriverTypeEnum.MSSQL_2012, "alter table TRM_CONCEPT_MAP_GROUP add constraint FK_TCMGROUP_CONCEPTMAP foreign key (CONCEPT_MAP_PID) references TRM_CONCEPT_MAP")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "create table TRM_CONCEPT_MAP_GROUP (PID int8 not null, myConceptMapUrl varchar(255), SOURCE_URL varchar(200) not null, mySourceValueSet varchar(255), SOURCE_VERSION varchar(100), TARGET_URL varchar(200) not null, myTargetValueSet varchar(255), TARGET_VERSION varchar(100), CONCEPT_MAP_PID int8 not null, primary key (PID))")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "alter table TRM_CONCEPT_MAP_GROUP add constraint FK_TCMGROUP_CONCEPTMAP foreign key (CONCEPT_MAP_PID) references TRM_CONCEPT_MAP");

		// Concept Map - Group Element
		version.startSectionWithMessage("Starting work on table: TRM_CONCEPT_MAP_GRP_ELEMENT");
		version
			.addTableRawSql("TRM_CONCEPT_MAP_GRP_ELEMENT")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "create table TRM_CONCEPT_MAP_GRP_ELEMENT (PID bigint not null, SOURCE_CODE varchar(500) not null, myConceptMapUrl varchar(255), SOURCE_DISPLAY varchar(400), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GROUP_PID bigint not null, primary key (PID))")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "alter table TRM_CONCEPT_MAP_GRP_ELEMENT add constraint FK_TCMGELEMENT_GROUP foreign key (CONCEPT_MAP_GROUP_PID) references TRM_CONCEPT_MAP_GROUP")
			.addSql(DriverTypeEnum.MARIADB_10_1, "create table TRM_CONCEPT_MAP_GRP_ELEMENT (PID bigint not null, SOURCE_CODE varchar(500) not null, myConceptMapUrl varchar(255), SOURCE_DISPLAY varchar(400), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GROUP_PID bigint not null, primary key (PID))")
			.addSql(DriverTypeEnum.MARIADB_10_1, "alter table TRM_CONCEPT_MAP_GRP_ELEMENT add constraint FK_TCMGELEMENT_GROUP foreign key (CONCEPT_MAP_GROUP_PID) references TRM_CONCEPT_MAP_GROUP (PID)")
			.addSql(DriverTypeEnum.MARIADB_10_1, "create index IDX_CNCPT_MAP_GRP_CD on TRM_CONCEPT_MAP_GRP_ELEMENT (SOURCE_CODE)")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "create index IDX_CNCPT_MAP_GRP_CD on TRM_CONCEPT_MAP_GRP_ELEMENT (SOURCE_CODE)")
			.addSql(DriverTypeEnum.MYSQL_5_7, "create table TRM_CONCEPT_MAP_GRP_ELEMENT (PID bigint not null, SOURCE_CODE varchar(500) not null, myConceptMapUrl varchar(255), SOURCE_DISPLAY varchar(400), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GROUP_PID bigint not null, primary key (PID))")
			.addSql(DriverTypeEnum.MYSQL_5_7, "create index IDX_CNCPT_MAP_GRP_CD on TRM_CONCEPT_MAP_GRP_ELEMENT (SOURCE_CODE)")
			.addSql(DriverTypeEnum.MYSQL_5_7, "alter table TRM_CONCEPT_MAP_GRP_ELEMENT add constraint FK_TCMGELEMENT_GROUP foreign key (CONCEPT_MAP_GROUP_PID) references TRM_CONCEPT_MAP_GROUP (PID)")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "create table TRM_CONCEPT_MAP_GRP_ELEMENT (PID int8 not null, SOURCE_CODE varchar(500) not null, myConceptMapUrl varchar(255), SOURCE_DISPLAY varchar(400), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GROUP_PID int8 not null, primary key (PID))")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "alter table TRM_CONCEPT_MAP_GRP_ELEMENT add constraint FK_TCMGELEMENT_GROUP foreign key (CONCEPT_MAP_GROUP_PID) references TRM_CONCEPT_MAP_GROUP")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "create index IDX_CNCPT_MAP_GRP_CD on TRM_CONCEPT_MAP_GRP_ELEMENT (SOURCE_CODE)")
			.addSql(DriverTypeEnum.ORACLE_12C, "create table TRM_CONCEPT_MAP_GRP_ELEMENT (PID number(19,0) not null, SOURCE_CODE varchar2(500 char) not null, myConceptMapUrl varchar2(255 char), SOURCE_DISPLAY varchar2(400 char), mySystem varchar2(255 char), mySystemVersion varchar2(255 char), myValueSet varchar2(255 char), CONCEPT_MAP_GROUP_PID number(19,0) not null, primary key (PID))")
			.addSql(DriverTypeEnum.ORACLE_12C, "alter table TRM_CONCEPT_MAP_GRP_ELEMENT add constraint FK_TCMGELEMENT_GROUP foreign key (CONCEPT_MAP_GROUP_PID) references TRM_CONCEPT_MAP_GROUP")
			.addSql(DriverTypeEnum.ORACLE_12C, "create index IDX_CNCPT_MAP_GRP_CD on TRM_CONCEPT_MAP_GRP_ELEMENT (SOURCE_CODE)")
			.addSql(DriverTypeEnum.MSSQL_2012, "create table TRM_CONCEPT_MAP_GRP_ELEMENT (PID bigint not null, SOURCE_CODE varchar(500) not null, myConceptMapUrl varchar(255), SOURCE_DISPLAY varchar(400), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GROUP_PID bigint not null, primary key (PID))")
			.addSql(DriverTypeEnum.MSSQL_2012, "create index IDX_CNCPT_MAP_GRP_CD on TRM_CONCEPT_MAP_GRP_ELEMENT (SOURCE_CODE)")
			.addSql(DriverTypeEnum.MSSQL_2012, "alter table TRM_CONCEPT_MAP_GRP_ELEMENT add constraint FK_TCMGELEMENT_GROUP foreign key (CONCEPT_MAP_GROUP_PID) references TRM_CONCEPT_MAP_GROUP");

		// Concept Map - Group Element Target
		version.startSectionWithMessage("Starting work on table: TRM_CONCEPT_MAP_GRP_ELM_TGT");
		version
			.addTableRawSql("TRM_CONCEPT_MAP_GRP_ELM_TGT")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "create table TRM_CONCEPT_MAP_GRP_ELM_TGT (PID bigint not null, TARGET_CODE varchar(500) not null, myConceptMapUrl varchar(255), TARGET_DISPLAY varchar(400), TARGET_EQUIVALENCE varchar(50), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GRP_ELM_PID bigint not null, primary key (PID))")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "alter table TRM_CONCEPT_MAP_GRP_ELM_TGT add constraint FK_TCMGETARGET_ELEMENT foreign key (CONCEPT_MAP_GRP_ELM_PID) references TRM_CONCEPT_MAP_GRP_ELEMENT")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "create index IDX_CNCPT_MP_GRP_ELM_TGT_CD on TRM_CONCEPT_MAP_GRP_ELM_TGT (TARGET_CODE)")
			.addSql(DriverTypeEnum.MARIADB_10_1, "create table TRM_CONCEPT_MAP_GRP_ELM_TGT (PID bigint not null, TARGET_CODE varchar(500) not null, myConceptMapUrl varchar(255), TARGET_DISPLAY varchar(400), TARGET_EQUIVALENCE varchar(50), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GRP_ELM_PID bigint not null, primary key (PID))")
			.addSql(DriverTypeEnum.MARIADB_10_1, "alter table TRM_CONCEPT_MAP_GRP_ELM_TGT add constraint FK_TCMGETARGET_ELEMENT foreign key (CONCEPT_MAP_GRP_ELM_PID) references TRM_CONCEPT_MAP_GRP_ELEMENT (PID)")
			.addSql(DriverTypeEnum.MARIADB_10_1, "create index IDX_CNCPT_MP_GRP_ELM_TGT_CD on TRM_CONCEPT_MAP_GRP_ELM_TGT (TARGET_CODE)")
			.addSql(DriverTypeEnum.MYSQL_5_7, "create table TRM_CONCEPT_MAP_GRP_ELM_TGT (PID bigint not null, TARGET_CODE varchar(500) not null, myConceptMapUrl varchar(255), TARGET_DISPLAY varchar(400), TARGET_EQUIVALENCE varchar(50), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GRP_ELM_PID bigint not null, primary key (PID))")
			.addSql(DriverTypeEnum.MYSQL_5_7, "alter table TRM_CONCEPT_MAP_GRP_ELM_TGT add constraint FK_TCMGETARGET_ELEMENT foreign key (CONCEPT_MAP_GRP_ELM_PID) references TRM_CONCEPT_MAP_GRP_ELEMENT (PID)")
			.addSql(DriverTypeEnum.MYSQL_5_7, "create index IDX_CNCPT_MP_GRP_ELM_TGT_CD on TRM_CONCEPT_MAP_GRP_ELM_TGT (TARGET_CODE)")
			.addSql(DriverTypeEnum.ORACLE_12C, "create table TRM_CONCEPT_MAP_GRP_ELM_TGT (PID number(19,0) not null, TARGET_CODE varchar2(500 char) not null, myConceptMapUrl varchar2(255 char), TARGET_DISPLAY varchar2(400 char), TARGET_EQUIVALENCE varchar2(50 char), mySystem varchar2(255 char), mySystemVersion varchar2(255 char), myValueSet varchar2(255 char), CONCEPT_MAP_GRP_ELM_PID number(19,0) not null, primary key (PID))")
			.addSql(DriverTypeEnum.ORACLE_12C, "alter table TRM_CONCEPT_MAP_GRP_ELM_TGT add constraint FK_TCMGETARGET_ELEMENT foreign key (CONCEPT_MAP_GRP_ELM_PID) references TRM_CONCEPT_MAP_GRP_ELEMENT")
			.addSql(DriverTypeEnum.ORACLE_12C, "create index IDX_CNCPT_MP_GRP_ELM_TGT_CD on TRM_CONCEPT_MAP_GRP_ELM_TGT (TARGET_CODE)")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "create table TRM_CONCEPT_MAP_GRP_ELM_TGT (PID int8 not null, TARGET_CODE varchar(500) not null, myConceptMapUrl varchar(255), TARGET_DISPLAY varchar(400), TARGET_EQUIVALENCE varchar(50), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GRP_ELM_PID int8 not null, primary key (PID))")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "alter table TRM_CONCEPT_MAP_GRP_ELM_TGT add constraint FK_TCMGETARGET_ELEMENT foreign key (CONCEPT_MAP_GRP_ELM_PID) references TRM_CONCEPT_MAP_GRP_ELEMENT")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "create index IDX_CNCPT_MP_GRP_ELM_TGT_CD on TRM_CONCEPT_MAP_GRP_ELM_TGT (TARGET_CODE)")
			.addSql(DriverTypeEnum.MSSQL_2012, "create table TRM_CONCEPT_MAP_GRP_ELM_TGT (PID bigint not null, TARGET_CODE varchar(500) not null, myConceptMapUrl varchar(255), TARGET_DISPLAY varchar(400), TARGET_EQUIVALENCE varchar(50), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GRP_ELM_PID bigint not null, primary key (PID))")
			.addSql(DriverTypeEnum.MSSQL_2012, "create index IDX_CNCPT_MP_GRP_ELM_TGT_CD on TRM_CONCEPT_MAP_GRP_ELM_TGT (TARGET_CODE)")
			.addSql(DriverTypeEnum.MSSQL_2012, "alter table TRM_CONCEPT_MAP_GRP_ELM_TGT add constraint FK_TCMGETARGET_ELEMENT foreign key (CONCEPT_MAP_GRP_ELM_PID) references TRM_CONCEPT_MAP_GRP_ELEMENT");

		version.onTable("HFJ_IDX_CMP_STRING_UNIQ").modifyColumn("IDX_STRING").nonNullable().withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);


	}

	private Boolean columnToBoolean(Object theValue) {
		if (theValue == null) {
			return null;
		}
		if (theValue instanceof Boolean) {
			return (Boolean) theValue;
		}

		long longValue = ((Number) theValue).longValue();
		return longValue == 1L;
	}

	private void init340() {
		Builder version = forVersion(VersionEnum.V3_4_0);

		// CodeSystem Version
		Builder.BuilderWithTableName resourceLink = version.onTable("TRM_CODESYSTEM_VER");
		version.startSectionWithMessage("Starting work on table: " + resourceLink.getTableName());
		resourceLink
			.dropIndex("IDX_CSV_RESOURCEPID_AND_VER");
		resourceLink
			.dropColumn("RES_VERSION_ID");
		resourceLink
			.addColumn("CS_VERSION_ID")
			.nullable()
			.type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 255);
		resourceLink
			.addColumn("CODESYSTEM_PID")
			.nullable()
			.type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		resourceLink
			.addForeignKey("FK_CODESYSVER_CS_ID")
			.toColumn("CODESYSTEM_PID")
			.references("TRM_CODESYSTEM", "PID");

		// Concept
		Builder.BuilderWithTableName concept = version.onTable("TRM_CONCEPT");
		version.startSectionWithMessage("Starting work on table: " + concept.getTableName());
		concept
			.addColumn("CODE_SEQUENCE")
			.nullable()
			.type(BaseTableColumnTypeTask.ColumnTypeEnum.INT);


	}

	private void init330() {
		Builder version = forVersion(VersionEnum.V3_3_0);

		Builder.BuilderWithTableName hfjResource = version.onTable("HFJ_RESOURCE");
		version.startSectionWithMessage("Starting work on table: " + hfjResource.getTableName());
		hfjResource.dropColumn("RES_TEXT");
		hfjResource.dropColumn("RES_ENCODING");

		Builder.BuilderWithTableName hfjResVer = version.onTable("HFJ_RES_VER");
		version.startSectionWithMessage("Starting work on table: " + hfjResVer.getTableName());
		hfjResVer.modifyColumn("RES_ENCODING")
			.nullable();
		hfjResVer.modifyColumn("RES_TEXT")
			.nullable();
	}

	public enum FlagEnum {
		NO_MIGRATE_HASHES("no-migrate-350-hashes");

		private final String myCommandLineValue;

		FlagEnum(String theCommandLineValue) {
			myCommandLineValue = theCommandLineValue;
		}

		public static FlagEnum fromCommandLineValue(String theCommandLineValue) {
			Optional<FlagEnum> retVal = Arrays.stream(values()).filter(t -> t.myCommandLineValue.equals(theCommandLineValue)).findFirst();
			return retVal.orElseThrow(() -> {
				List<String> validValues = Arrays.stream(values()).map(t -> t.myCommandLineValue).sorted().collect(Collectors.toList());
				return new IllegalArgumentException("Invalid flag \"" + theCommandLineValue + "\". Valid values: " + validValues);
			});
		}
	}


}
