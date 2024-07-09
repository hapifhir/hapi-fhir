/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.migrate.tasks;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.entity.BulkImportJobEntity;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.ArbitrarySqlTask;
import ca.uhn.fhir.jpa.migrate.taskdef.CalculateHashesTask;
import ca.uhn.fhir.jpa.migrate.taskdef.CalculateOrdinalDatesTask;
import ca.uhn.fhir.jpa.migrate.taskdef.ColumnTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.ForceIdMigrationCopyTask;
import ca.uhn.fhir.jpa.migrate.taskdef.ForceIdMigrationFixTask;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import ca.uhn.fhir.jpa.migrate.tasks.api.ColumnAndNullable;
import ca.uhn.fhir.jpa.migrate.tasks.api.TaskFlagEnum;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresentEntity;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.VersionEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.UUID_LENGTH;

@SuppressWarnings({"SqlNoDataSourceInspection", "SpellCheckingInspection", "java:S1192"})
public class HapiFhirJpaMigrationTasks extends BaseMigrationTasks<VersionEnum> {

	// H2, Derby, MariaDB, and MySql automatically add indexes to foreign keys
	public static final DriverTypeEnum[] NON_AUTOMATIC_FK_INDEX_PLATFORMS =
			new DriverTypeEnum[] {DriverTypeEnum.POSTGRES_9_4, DriverTypeEnum.ORACLE_12C, DriverTypeEnum.MSSQL_2012};
	private static final String QUERY_FOR_COLUMN_COLLATION_TEMPLATE = "WITH defcoll AS (\n"
			+ "	SELECT datcollate AS coll\n"
			+ "	FROM pg_database\n"
			+ "	WHERE datname = current_database())\n"
			+ ", collation_by_column AS (\n"
			+ "	SELECT a.attname,\n"
			+ "		CASE WHEN c.collname = 'default'\n"
			+ "			THEN defcoll.coll\n"
			+ "			ELSE c.collname\n"
			+ "		END AS my_collation\n"
			+ "	FROM pg_attribute AS a\n"
			+ "		CROSS JOIN defcoll\n"
			+ "		LEFT JOIN pg_collation AS c ON a.attcollation = c.oid\n"
			+ "	WHERE a.attrelid = '%s'::regclass\n"
			+ "		AND a.attnum > 0\n"
			+ "		AND attname = '%s'\n"
			+ ")\n"
			+ "SELECT TRUE as result\n"
			+ "FROM collation_by_column\n"
			+ "WHERE EXISTS (SELECT 1\n"
			+ "	FROM collation_by_column\n"
			+ "	WHERE my_collation != 'C')";
	private final Set<FlagEnum> myFlags;

	/**
	 * Constructor
	 */
	public HapiFhirJpaMigrationTasks(Set<String> theFlags) {
		myFlags = theFlags.stream().map(FlagEnum::fromCommandLineValue).collect(Collectors.toSet());

		init330(); // 20180114 - 20180329
		init340(); // 20180401 - 20180528
		init350(); // 20180601 - 20180917
		init360(); // 20180918 - 20181112
		init400(); // 20190401 - 20190814
		init410(); // 20190815 - 20191014
		init420(); // 20191015 - 20200217
		init430(); // Replaced by 5.0.0
		init500(); // 20200218 - 20200513
		init501(); // 20200514 - 20200515
		init510(); // 20200516 - 20201028
		init520(); // 20201029 -
		init530();
		init540(); // 20210218 - 20210520
		init550(); // 20210520 -
		init560(); // 20211027 -
		init570(); // 20211102 -
		init600(); // 20211102 -
		init610();
		init620();
		init640();
		init640_after_20230126();
		init660();
		init680();
		init680_Part2();
		init700();
		init720();
		init740();
	}

	protected void init740() {
		// Start of migrations from 7.2 to 7.4

		final Builder version = forVersion(VersionEnum.V7_4_0);

		{
			version.onTable("HFJ_RES_SEARCH_URL")
					.addForeignKey("20240515.1", "FK_RES_SEARCH_URL_RESOURCE")
					.toColumn("RES_ID")
					.references("HFJ_RESOURCE", "RES_ID");
		}

		/*
		 * Make a bunch of columns non-nullable. Note that we won't actually apply this migration
		 * on the live system as it would take a loooooong time to execute these on heavily loaded
		 * databases.
		 */
		// Skipping numbers 20240601.1 and 20240601.2 as they were found to not
		// be needed during code review.
		version.onTable("HFJ_RESOURCE")
				.modifyColumn("20240601.3", "SP_HAS_LINKS")
				.nonNullable()
				.withType(ColumnTypeEnum.BOOLEAN)
				.heavyweightSkipByDefault()
				.failureAllowed();
		version.onTable("HFJ_RESOURCE")
				.modifyColumn("20240601.4", "SP_COORDS_PRESENT")
				.nonNullable()
				.withType(ColumnTypeEnum.BOOLEAN)
				.heavyweightSkipByDefault()
				.failureAllowed();
		version.onTable("HFJ_RESOURCE")
				.modifyColumn("20240601.5", "SP_DATE_PRESENT")
				.nonNullable()
				.withType(ColumnTypeEnum.BOOLEAN)
				.heavyweightSkipByDefault()
				.failureAllowed();
		version.onTable("HFJ_RESOURCE")
				.modifyColumn("20240601.6", "SP_NUMBER_PRESENT")
				.nonNullable()
				.withType(ColumnTypeEnum.BOOLEAN)
				.heavyweightSkipByDefault()
				.failureAllowed();
		version.onTable("HFJ_RESOURCE")
				.modifyColumn("20240601.7", "SP_QUANTITY_PRESENT")
				.nonNullable()
				.withType(ColumnTypeEnum.BOOLEAN)
				.heavyweightSkipByDefault()
				.failureAllowed();
		version.onTable("HFJ_RESOURCE")
				.modifyColumn("20240601.8", "SP_QUANTITY_NRML_PRESENT")
				.nonNullable()
				.withType(ColumnTypeEnum.BOOLEAN)
				.heavyweightSkipByDefault()
				.failureAllowed();
		version.onTable("HFJ_RESOURCE")
				.modifyColumn("20240601.9", "SP_STRING_PRESENT")
				.nonNullable()
				.withType(ColumnTypeEnum.BOOLEAN)
				.heavyweightSkipByDefault()
				.failureAllowed();
		version.onTable("HFJ_RESOURCE")
				.modifyColumn("20240601.10", "SP_TOKEN_PRESENT")
				.nonNullable()
				.withType(ColumnTypeEnum.BOOLEAN)
				.heavyweightSkipByDefault()
				.failureAllowed();
		version.onTable("HFJ_RESOURCE")
				.modifyColumn("20240601.11", "SP_URI_PRESENT")
				.nonNullable()
				.withType(ColumnTypeEnum.BOOLEAN)
				.heavyweightSkipByDefault()
				.failureAllowed();
		version.onTable("HFJ_RESOURCE")
				.modifyColumn("20240601.12", "RES_VER")
				.nonNullable()
				.withType(ColumnTypeEnum.LONG)
				.heavyweightSkipByDefault()
				.failureAllowed();
		version.onTable("TRM_CONCEPT")
				.modifyColumn("20240601.13", "CODESYSTEM_PID")
				.nonNullable()
				.withType(ColumnTypeEnum.LONG)
				.heavyweightSkipByDefault()
				.failureAllowed();
		version.onTable("BT2_JOB_INSTANCE")
				.modifyColumn("20240601.14", "PROGRESS_PCT")
				.nonNullable()
				.withType(ColumnTypeEnum.DOUBLE)
				.heavyweightSkipByDefault()
				.failureAllowed();
		version.onTable("BT2_JOB_INSTANCE")
				.modifyColumn("20240601.15", "ERROR_COUNT")
				.nonNullable()
				.withType(ColumnTypeEnum.INT)
				.heavyweightSkipByDefault()
				.failureAllowed();
		version.onTable("HFJ_BINARY_STORAGE_BLOB")
				.modifyColumn("20240601.16", "BLOB_SIZE")
				.nonNullable()
				.withType(ColumnTypeEnum.LONG)
				.heavyweightSkipByDefault()
				.failureAllowed();

		/*
		 * Add RES_ID to two indexes on HFJ_RES_VER which support history operations.
		 * This makes server and type level _history work properly on large databases
		 * on postgres. These are both marked as heavyweightSkipByDefault because the
		 * necessary reindexing would be very expensive for a rarely used FHIR feature.
		 */
		version.onTable("HFJ_RES_VER")
				.dropIndex("20240601.17", "IDX_RESVER_TYPE_DATE")
				.heavyweightSkipByDefault();
		version.onTable("HFJ_RES_VER")
				.addIndex("20240601.18", "IDX_RESVER_TYPE_DATE")
				.unique(false)
				.withColumns("RES_TYPE", "RES_UPDATED", "RES_ID")
				.heavyweightSkipByDefault();
		version.onTable("HFJ_RES_VER")
				.dropIndex("20240601.19", "IDX_RESVER_DATE")
				.heavyweightSkipByDefault();
		version.onTable("HFJ_RES_VER")
				.addIndex("20240601.20", "IDX_RESVER_DATE")
				.unique(false)
				.withColumns("RES_UPDATED", "RES_ID")
				.heavyweightSkipByDefault();

		// Allow null values in SP_NAME, RES_TYPE columns for all HFJ_SPIDX_* tables. These are marked as failure
		// allowed, since SQL Server won't let us change nullability on columns with indexes pointing to them.
		{
			Builder.BuilderWithTableName spidxCoords = version.onTable("HFJ_SPIDX_COORDS");
			spidxCoords
					.modifyColumn("20240617.1", "SP_NAME")
					.nullable()
					.withType(ColumnTypeEnum.STRING, 100)
					.failureAllowed();
			spidxCoords
					.modifyColumn("20240617.2", "RES_TYPE")
					.nullable()
					.withType(ColumnTypeEnum.STRING, 100)
					.failureAllowed();

			Builder.BuilderWithTableName spidxDate = version.onTable("HFJ_SPIDX_DATE");
			spidxDate
					.modifyColumn("20240617.3", "SP_NAME")
					.nullable()
					.withType(ColumnTypeEnum.STRING, 100)
					.failureAllowed();
			spidxDate
					.modifyColumn("20240617.4", "RES_TYPE")
					.nullable()
					.withType(ColumnTypeEnum.STRING, 100)
					.failureAllowed();

			Builder.BuilderWithTableName spidxNumber = version.onTable("HFJ_SPIDX_NUMBER");
			spidxNumber
					.modifyColumn("20240617.5", "SP_NAME")
					.nullable()
					.withType(ColumnTypeEnum.STRING, 100)
					.failureAllowed();
			spidxNumber
					.modifyColumn("20240617.6", "RES_TYPE")
					.nullable()
					.withType(ColumnTypeEnum.STRING, 100)
					.failureAllowed();

			Builder.BuilderWithTableName spidxQuantity = version.onTable("HFJ_SPIDX_QUANTITY");
			spidxQuantity
					.modifyColumn("20240617.7", "SP_NAME")
					.nullable()
					.withType(ColumnTypeEnum.STRING, 100)
					.failureAllowed();
			spidxQuantity
					.modifyColumn("20240617.8", "RES_TYPE")
					.nullable()
					.withType(ColumnTypeEnum.STRING, 100)
					.failureAllowed();

			Builder.BuilderWithTableName spidxQuantityNorm = version.onTable("HFJ_SPIDX_QUANTITY_NRML");
			spidxQuantityNorm
					.modifyColumn("20240617.9", "SP_NAME")
					.nullable()
					.withType(ColumnTypeEnum.STRING, 100)
					.failureAllowed();
			spidxQuantityNorm
					.modifyColumn("20240617.10", "RES_TYPE")
					.nullable()
					.withType(ColumnTypeEnum.STRING, 100)
					.failureAllowed();

			Builder.BuilderWithTableName spidxString = version.onTable("HFJ_SPIDX_STRING");
			spidxString
					.modifyColumn("20240617.11", "SP_NAME")
					.nullable()
					.withType(ColumnTypeEnum.STRING, 100)
					.failureAllowed();
			spidxString
					.modifyColumn("20240617.12", "RES_TYPE")
					.nullable()
					.withType(ColumnTypeEnum.STRING, 100)
					.failureAllowed();

			Builder.BuilderWithTableName spidxToken = version.onTable("HFJ_SPIDX_TOKEN");
			spidxToken
					.modifyColumn("20240617.13", "SP_NAME")
					.nullable()
					.withType(ColumnTypeEnum.STRING, 100)
					.failureAllowed();
			spidxToken
					.modifyColumn("20240617.14", "RES_TYPE")
					.nullable()
					.withType(ColumnTypeEnum.STRING, 100)
					.failureAllowed();

			Builder.BuilderWithTableName spidxUri = version.onTable("HFJ_SPIDX_URI");
			spidxUri.modifyColumn("20240617.15", "SP_NAME")
					.nullable()
					.withType(ColumnTypeEnum.STRING, 100)
					.failureAllowed();
			spidxUri.modifyColumn("20240617.16", "RES_TYPE")
					.nullable()
					.withType(ColumnTypeEnum.STRING, 100)
					.failureAllowed();

			{
				// Please see https://github.com/hapifhir/hapi-fhir/issues/6033 for why we're doing this
				version.onTable("HFJ_RES_SEARCH_URL")
						.addColumn("20240618.2", "PARTITION_ID", -1)
						.nullable()
						.type(ColumnTypeEnum.INT);

				version.onTable("HFJ_RES_SEARCH_URL")
						.addColumn("20240618.3", "PARTITION_DATE")
						.nullable()
						.type(ColumnTypeEnum.DATE_ONLY);

				version.executeRawSql("20240618.4", "UPDATE HFJ_RES_SEARCH_URL SET PARTITION_ID = -1");

				version.onTable("HFJ_RES_SEARCH_URL")
						.modifyColumn("20240618.5", "PARTITION_ID")
						.nonNullable()
						.withType(ColumnTypeEnum.INT);

				version.onTable("HFJ_RES_SEARCH_URL").dropPrimaryKey("20240618.6");

				version.onTable("HFJ_RES_SEARCH_URL").addPrimaryKey("20240618.7", "RES_SEARCH_URL", "PARTITION_ID");
			}
		}

		{
			// Note that these are recreations of a previous migration from 6.6.0. The original migration had these set
			// as unique,
			// which causes SQL Server to create a filtered index. See
			// https://www.sqlshack.com/introduction-to-sql-server-filtered-indexes/
			// What this means for hibernate search is that for any column that is nullable, the SQLServerDialect will
			// omit the whole row from the index if
			// the value of the nullable column is actually null. Removing the uniqueness constraint works around this
			// problem.
			Builder.BuilderWithTableName uriTable = version.onTable("HFJ_SPIDX_URI");

			uriTable.dropIndex("20240620.10", "IDX_SP_URI_HASH_URI_V2");
			uriTable.dropIndex("20240620.20", "IDX_SP_URI_HASH_IDENTITY_V2");

			uriTable.addIndex("20240620.30", "IDX_SP_URI_HASH_URI_V2")
					.unique(false)
					.online(true)
					.withPossibleNullableColumns(
							new ColumnAndNullable("HASH_URI", true),
							new ColumnAndNullable("RES_ID", false),
							new ColumnAndNullable("PARTITION_ID", true));
			uriTable.addIndex("20240620.40", "IDX_SP_URI_HASH_IDENTITY_V2")
					.unique(false)
					.online(true)
					.withPossibleNullableColumns(
							new ColumnAndNullable("HASH_IDENTITY", true),
							new ColumnAndNullable("SP_URI", true),
							new ColumnAndNullable("RES_ID", false),
							new ColumnAndNullable("PARTITION_ID", true));
		}

		/*
		 * Add hash columns to the combo param index tables
		 */
		{
			version.onTable("HFJ_IDX_CMB_TOK_NU")
					.addIndex("20240625.10", "IDX_IDXCMBTOKNU_HASHC")
					.unique(false)
					.withColumns("HASH_COMPLETE", "RES_ID", "PARTITION_ID");
			version.onTable("HFJ_IDX_CMP_STRING_UNIQ")
					.addColumn("20240625.20", "HASH_COMPLETE")
					.nullable()
					.type(ColumnTypeEnum.LONG);
			version.onTable("HFJ_IDX_CMP_STRING_UNIQ")
					.addColumn("20240625.30", "HASH_COMPLETE_2")
					.nullable()
					.type(ColumnTypeEnum.LONG);
			version.onTable("HFJ_IDX_CMP_STRING_UNIQ")
					.addTask(
							new CalculateHashesTask(VersionEnum.V7_4_0, "20240625.40") {
								@Override
								protected boolean shouldSkipTask() {
									return false;
								}
							}.setPidColumnName("PID")
									.addCalculator(
											"HASH_COMPLETE",
											t -> ResourceIndexedComboStringUnique.calculateHashComplete(
													t.getString("IDX_STRING")))
									.addCalculator(
											"HASH_COMPLETE_2",
											t -> ResourceIndexedComboStringUnique.calculateHashComplete2(
													t.getString("IDX_STRING")))
									.setColumnName("HASH_COMPLETE"));

			{
				version.onTable("TRM_CONCEPT_DESIG")
						.modifyColumn("20240705.10", "VAL")
						.nullable()
						.withType(ColumnTypeEnum.STRING, 2000);

				version.onTable("TRM_CONCEPT_DESIG")
						.addColumn("20240705.20", "VAL_VC")
						.nullable()
						.type(ColumnTypeEnum.TEXT);
			}
			{ // These migrations permit much longer values to be stored in SPIDX_TOKEN and SPIDX_STRING value
				Builder.BuilderWithTableName spidxString = version.onTable("HFJ_SPIDX_STRING");
				// components.
				// This is mostly helpful for `:contains` searches on long values, since exact searches use the hash
				// anyhow.
				spidxString
						.modifyColumn("20240708.10", "SP_VALUE_EXACT")
						.nullable()
						.withType(ColumnTypeEnum.STRING, 768)
						.failureAllowed();
				spidxString
						.modifyColumn("20240708.20", "SP_VALUE_NORMALIZED")
						.nullable()
						.withType(ColumnTypeEnum.STRING, 768)
						.failureAllowed();
			}
		}
	}

	protected void init720() {
		// Start of migrations from 7.0 to 7.2

		Builder version = forVersion(VersionEnum.V7_2_0);

		// allow null codes in concept map targets (see comment on "20190722.27" if you are going to change this)
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
				.modifyColumn("20240327.1", "TARGET_CODE")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 500);

		// Stop writing to hfj_forced_id https://github.com/hapifhir/hapi-fhir/pull/5817
		Builder.BuilderWithTableName forcedId = version.onTable("HFJ_FORCED_ID");
		forcedId.dropForeignKey("20240402.1", "FK_FORCEDID_RESOURCE", "HFJ_RESOURCE");
		forcedId.dropIndex("20240402.2", "IDX_FORCEDID_RESID");
		forcedId.dropIndex("20240402.3", "IDX_FORCEDID_TYPE_FID");
		forcedId.dropIndex("20240402.4", "IDX_FORCEID_FID");

		// Migration from LOB
		{
			Builder.BuilderWithTableName binaryStorageBlobTable = version.onTable("HFJ_BINARY_STORAGE_BLOB");

			binaryStorageBlobTable
					.renameColumn("20240404.1", "BLOB_ID", "CONTENT_ID")
					.getLastAddedTask()
					.ifPresent(t -> t.addFlag(TaskFlagEnum.DO_NOTHING));
			binaryStorageBlobTable
					.renameColumn("20240404.2", "BLOB_SIZE", "CONTENT_SIZE")
					.getLastAddedTask()
					.ifPresent(t -> t.addFlag(TaskFlagEnum.DO_NOTHING));
			binaryStorageBlobTable
					.renameColumn("20240404.3", "BLOB_HASH", "CONTENT_HASH")
					.getLastAddedTask()
					.ifPresent(t -> t.addFlag(TaskFlagEnum.DO_NOTHING));

			binaryStorageBlobTable
					.modifyColumn("20240404.4", "BLOB_DATA")
					.nullable()
					.withType(ColumnTypeEnum.BLOB);

			binaryStorageBlobTable
					.addColumn("20240404.5", "STORAGE_CONTENT_BIN")
					.nullable()
					.type(ColumnTypeEnum.BINARY);

			binaryStorageBlobTable
					.migrateBlobToBinary("20240404.6", "BLOB_DATA", "STORAGE_CONTENT_BIN")
					.doNothing();

			binaryStorageBlobTable
					.renameTable("20240404.7", "HFJ_BINARY_STORAGE")
					.doNothing();

			Builder.BuilderWithTableName binaryStorageTableFix = version.onTable("HFJ_BINARY_STORAGE");

			binaryStorageTableFix.renameColumn("20240404.10", "CONTENT_ID", "BLOB_ID", true, true);
			binaryStorageTableFix.renameColumn("20240404.20", "CONTENT_SIZE", "BLOB_SIZE", true, true);
			binaryStorageTableFix.renameColumn("20240404.30", "CONTENT_HASH", "BLOB_HASH", true, true);

			binaryStorageTableFix
					.renameTable("20240404.40", "HFJ_BINARY_STORAGE_BLOB")
					.failureAllowed();
		}

		{
			Builder.BuilderWithTableName termConceptPropertyTable = version.onTable("TRM_CONCEPT_PROPERTY");

			termConceptPropertyTable
					.addColumn("20240409.1", "PROP_VAL_BIN")
					.nullable()
					.type(ColumnTypeEnum.BINARY);

			termConceptPropertyTable
					.migrateBlobToBinary("20240409.2", "PROP_VAL_LOB", "PROP_VAL_BIN")
					.doNothing();
		}

		{
			Builder.BuilderWithTableName termValueSetConceptTable = version.onTable("TRM_VALUESET_CONCEPT");
			termValueSetConceptTable
					.addColumn("20240409.3", "SOURCE_DIRECT_PARENT_PIDS_VC")
					.nullable()
					.type(ColumnTypeEnum.TEXT);

			termValueSetConceptTable
					.migrateClobToText("20240409.4", "SOURCE_DIRECT_PARENT_PIDS", "SOURCE_DIRECT_PARENT_PIDS_VC")
					.doNothing();
		}

		{
			Builder.BuilderWithTableName termConceptTable = version.onTable("TRM_CONCEPT");
			termConceptTable
					.addColumn("20240410.1", "PARENT_PIDS_VC")
					.nullable()
					.type(ColumnTypeEnum.TEXT);

			termConceptTable
					.migrateClobToText("20240410.2", "PARENT_PIDS", "PARENT_PIDS_VC")
					.doNothing();
		}
	}

	protected void init700() {
		/* ************************************************
		 * Start of 6.10 migrations
		 *********************************************** */

		Builder version = forVersion(VersionEnum.V7_0_0);

		// new indices on MdmLink
		Builder.BuilderWithTableName mdmLinkTable = version.onTable("MPI_LINK");

		mdmLinkTable
				.addIndex("20230911.1", "IDX_EMPI_TGT_MR_LS")
				.unique(false)
				.online(true)
				.withColumns("TARGET_TYPE", "MATCH_RESULT", "LINK_SOURCE");
		mdmLinkTable
				.addIndex("20230911.2", "IDX_EMPi_TGT_MR_SCore")
				.unique(false)
				.online(true)
				.withColumns("TARGET_TYPE", "MATCH_RESULT", "SCORE");

		// Move forced_id constraints to hfj_resource and the new fhir_id column
		// Note: we leave the HFJ_FORCED_ID.IDX_FORCEDID_TYPE_FID index in place to support old writers for a while.
		version.addTask(
				new ForceIdMigrationCopyTask(version.getRelease(), "20231018.1").addFlag(TaskFlagEnum.DO_NOTHING));

		Builder.BuilderWithTableName hfjResource = version.onTable("HFJ_RESOURCE");
		// commented out to make numeric space for the fix task below.
		// This constraint can't be enabled until the column is fully populated, and the shipped version of 20231018.1
		// was broken.
		// hfjResource.modifyColumn("20231018.2", "FHIR_ID").nonNullable();

		// this was inserted after the release.
		version.addTask(new ForceIdMigrationFixTask(version.getRelease(), "20231018.3"));

		// added back in place of 20231018.2.  If 20231018.2 already ran, this is a no-op.
		hfjResource.modifyColumn("20231018.4", "FHIR_ID").nonNullable();

		hfjResource.dropIndex("20231027.1", "IDX_RES_FHIR_ID");
		hfjResource
				.addIndex("20231027.2", "IDX_RES_TYPE_FHIR_ID")
				.unique(true)
				.online(true)
				// include res_id and our deleted flag so we can satisfy Observation?_sort=_id from the index on
				// platforms that support it.
				.includeColumns("RES_ID, RES_DELETED_AT")
				.withColumns("RES_TYPE", "FHIR_ID");

		// For resolving references that don't supply the type.
		hfjResource
				.addIndex("20231027.3", "IDX_RES_FHIR_ID")
				.unique(false)
				.online(true)
				.withColumns("FHIR_ID");

		Builder.BuilderWithTableName batch2JobInstanceTable = version.onTable("BT2_JOB_INSTANCE");

		batch2JobInstanceTable.addColumn("20231128.1", "USER_NAME").nullable().type(ColumnTypeEnum.STRING, 200);

		batch2JobInstanceTable.addColumn("20231128.2", "CLIENT_ID").nullable().type(ColumnTypeEnum.STRING, 200);

		{
			version.executeRawSql(
							"20231212.1",
							"CREATE INDEX CONCURRENTLY idx_sp_string_hash_nrm_pattern_ops ON hfj_spidx_string USING btree (hash_norm_prefix, sp_value_normalized varchar_pattern_ops, res_id, partition_id)")
					.setTransactional(false)
					.onlyAppliesToPlatforms(DriverTypeEnum.POSTGRES_9_4)
					.onlyIf(
							String.format(
									QUERY_FOR_COLUMN_COLLATION_TEMPLATE,
									"HFJ_SPIDX_STRING".toLowerCase(),
									"SP_VALUE_NORMALIZED".toLowerCase()),
							"Column HFJ_SPIDX_STRING.SP_VALUE_NORMALIZED already has a collation of 'C' so doing nothing")
					.onlyIf(
							"SELECT NOT EXISTS(select 1 from pg_indexes where indexname='idx_sp_string_hash_nrm_pattern_ops')",
							"Index idx_sp_string_hash_nrm_pattern_ops already exists");
			version.executeRawSql(
							"20231212.2",
							"CREATE UNIQUE INDEX CONCURRENTLY idx_sp_uri_hash_identity_pattern_ops ON hfj_spidx_uri USING btree (hash_identity, sp_uri varchar_pattern_ops, res_id, partition_id)")
					.setTransactional(false)
					.onlyAppliesToPlatforms(DriverTypeEnum.POSTGRES_9_4)
					.onlyIf(
							String.format(
									QUERY_FOR_COLUMN_COLLATION_TEMPLATE,
									"HFJ_SPIDX_URI".toLowerCase(),
									"SP_URI".toLowerCase()),
							"Column HFJ_SPIDX_STRING.SP_VALUE_NORMALIZED already has a collation of 'C' so doing nothing")
					.onlyIf(
							"SELECT NOT EXISTS(select 1 from pg_indexes where indexname='idx_sp_uri_hash_identity_pattern_ops')",
							"Index idx_sp_uri_hash_identity_pattern_ops already exists.");
		}

		// This fix was bad for MSSQL, it has been set to do nothing.
		version.addTask(
				new ForceIdMigrationFixTask(version.getRelease(), "20231213.1").addFlag(TaskFlagEnum.DO_NOTHING));

		// This fix will work for MSSQL or Oracle.
		version.addTask(new ForceIdMigrationFixTask(version.getRelease(), "20231222.1"));

		// add index to Batch2WorkChunkEntity
		Builder.BuilderWithTableName workChunkTable = version.onTable("BT2_WORK_CHUNK");

		workChunkTable
				.addIndex("20240321.1", "IDX_BT2WC_II_SI_S_SEQ_ID")
				.unique(false)
				.withColumns("INSTANCE_ID", "TGT_STEP_ID", "STAT", "SEQ", "ID");

		// add columns to Batch2WorkChunkEntity
		Builder.BuilderWithTableName batch2WorkChunkTable = version.onTable("BT2_WORK_CHUNK");

		batch2WorkChunkTable
				.addColumn("20240322.1", "NEXT_POLL_TIME")
				.nullable()
				.type(ColumnTypeEnum.DATE_TIMESTAMP);
		batch2WorkChunkTable.addColumn("20240322.2", "POLL_ATTEMPTS").nullable().type(ColumnTypeEnum.INT);
	}

	private void init680_Part2() {
		Builder version = forVersion(VersionEnum.V6_8_0);

		// Add additional LOB migration columns
		version.onTable("BT2_JOB_INSTANCE")
				.addColumn("20240227.1", "REPORT_VC")
				.nullable()
				.type(ColumnTypeEnum.TEXT);
		version.onTable("BT2_JOB_INSTANCE")
				.addColumn("20240227.2", "PARAMS_JSON_VC")
				.nullable()
				.type(ColumnTypeEnum.TEXT);

		version.onTable("BT2_WORK_CHUNK")
				.addColumn("20240227.3", "CHUNK_DATA_VC")
				.nullable()
				.type(ColumnTypeEnum.TEXT);

		version.onTable("HFJ_SEARCH")
				.addColumn("20240227.4", "SEARCH_QUERY_STRING_VC")
				.nullable()
				.type(ColumnTypeEnum.TEXT);
		version.onTable("HFJ_SEARCH")
				.addColumn("20240227.5", "SEARCH_PARAM_MAP_BIN")
				.nullable()
				.type(ColumnTypeEnum.BINARY);

		version.onTable("HFJ_BLK_IMPORT_JOBFILE")
				.addColumn("20240227.6", "JOB_CONTENTS_VC")
				.nullable()
				.type(ColumnTypeEnum.TEXT);

		version.onTable("HFJ_BLK_IMPORT_JOBFILE")
				.modifyColumn("20240227.7", "JOB_CONTENTS")
				.nullable()
				.withType(ColumnTypeEnum.BLOB);
	}

	protected void init680() {
		Builder version = forVersion(VersionEnum.V6_8_0);
		// HAPI-FHIR #4801 - Add New Index On HFJ_RESOURCE
		Builder.BuilderWithTableName resourceTable = version.onTable("HFJ_RESOURCE");

		resourceTable
				.addIndex("20230502.1", "IDX_RES_RESID_UPDATED")
				.unique(false)
				.online(true)
				.withColumns("RES_ID", "RES_UPDATED", "PARTITION_ID");

		Builder.BuilderWithTableName tagDefTable = version.onTable("HFJ_TAG_DEF");
		tagDefTable.dropIndex("20230505.1", "IDX_TAGDEF_TYPESYSCODEVERUS");

		tagDefTable.dropIndex("20230505.2", "IDX_TAG_DEF_TP_CD_SYS");
		tagDefTable
				.addIndex("20230505.3", "IDX_TAG_DEF_TP_CD_SYS")
				.unique(false)
				.online(false)
				.withColumns("TAG_TYPE", "TAG_CODE", "TAG_SYSTEM", "TAG_ID", "TAG_VERSION", "TAG_USER_SELECTED");

		// This migration is failing in Oracle because there is already an index created on column RES_VER_PID since it
		// is a primary key.
		// IDX_RESVERPROV_RESVER_PID is removed in 20230523.1
		version.onTable("HFJ_RES_VER_PROV")
				.addIndex("20230510.1", "IDX_RESVERPROV_RESVER_PID")
				.unique(false)
				.withColumns("RES_VER_PID")
				.failureAllowed();

		// drop the index for any database that has RES_PID column already indexed from previous migrations
		version.onTable("HFJ_RES_VER_PROV")
				.dropIndex("20230510.2", "FK_RESVERPROV_RES_PID")
				.failureAllowed();

		version.onTable("HFJ_RES_VER_PROV")
				.addIndex("20230510.3", "IDX_RESVERPROV_RES_PID")
				.unique(false)
				.withColumns("RES_PID");

		version.onTable(ResourceHistoryTable.HFJ_RES_VER)
				.addColumn("20230510.4", "SOURCE_URI")
				.nullable()
				.type(ColumnTypeEnum.STRING, 100);
		version.onTable(ResourceHistoryTable.HFJ_RES_VER)
				.addColumn("20230510.5", "REQUEST_ID")
				.nullable()
				.type(ColumnTypeEnum.STRING, 16);

		version.onTable("HFJ_RES_VER_PROV")
				.addForeignKey("20230510.6", "FK_RESVERPROV_RES_PID")
				.toColumn("RES_PID")
				.references("HFJ_RESOURCE", "RES_ID");

		version.onTable("HFJ_RES_VER_PROV").dropIndex("20230523.1", "IDX_RESVERPROV_RESVER_PID");

		// add warning message to batch job instance
		version.onTable("BT2_WORK_CHUNK")
				.addColumn("20230524.1", "WARNING_MSG")
				.nullable()
				.type(ColumnTypeEnum.CLOB)
				.doNothing(); // the migration below is the better implementation

		version.onTable("BT2_JOB_INSTANCE")
				.addColumn("20230524.2", "WARNING_MSG")
				.nullable()
				.type(ColumnTypeEnum.CLOB)
				.doNothing(); // the migration below is the better implementation

		// adding indexes to foreign keys
		// this makes our table scans more efficient,
		// but it also makes us more stable
		// Oracle does not like unindexed foreign keys
		version.onTable("NPM_PACKAGE_VER")
				.addIndex("20230609.3", "FK_NPM_PKV_PKG")
				.unique(false)
				.withColumns("PACKAGE_PID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);
		version.onTable("NPM_PACKAGE_VER")
				.addIndex("20230609.4", "FK_NPM_PKV_RESID")
				.unique(false)
				.withColumns("BINARY_RES_ID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		version.onTable("NPM_PACKAGE_VER_RES")
				.addIndex("20230609.5", "FK_NPM_PACKVERRES_PACKVER")
				.unique(false)
				.withColumns("PACKVER_PID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);
		version.onTable("NPM_PACKAGE_VER_RES")
				.addIndex("20230609.6", "FK_NPM_PKVR_RESID")
				.unique(false)
				.withColumns("BINARY_RES_ID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		version.onTable("MPI_LINK")
				.addIndex("20230609.7", "FK_EMPI_LINK_TARGET")
				.unique(false)
				.withColumns("TARGET_PID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		version.onTable("TRM_CODESYSTEM")
				.addIndex("20230609.8", "FK_TRMCODESYSTEM_RES")
				.unique(false)
				.withColumns("RES_ID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);
		version.onTable("TRM_CODESYSTEM")
				.addIndex("20230609.9", "FK_TRMCODESYSTEM_CURVER")
				.unique(false)
				.withColumns("CURRENT_VERSION_PID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		version.onTable("TRM_CODESYSTEM_VER")
				.addIndex("20230609.10", "FK_CODESYSVER_RES_ID")
				.unique(false)
				.withColumns("RES_ID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);
		version.onTable("TRM_CODESYSTEM_VER")
				.addIndex("20230609.11", "FK_CODESYSVER_CS_ID")
				.unique(false)
				.withColumns("CODESYSTEM_PID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		version.onTable("TRM_CONCEPT_PC_LINK")
				.addIndex("20230609.12", "FK_TERM_CONCEPTPC_CS")
				.unique(false)
				.withColumns("CODESYSTEM_PID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		version.onTable("TRM_CONCEPT_PROPERTY")
				.addIndex("20230609.13", "FK_CONCEPTPROP_CSV")
				.unique(false)
				.withColumns("CS_VER_PID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		version.onTable("TRM_VALUESET")
				.addIndex("20230609.14", "FK_TRMVALUESET_RES")
				.unique(false)
				.withColumns("RES_ID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		version.onTable("TRM_VALUESET_C_DESIGNATION")
				.addIndex("20230609.15", "FK_TRM_VSCD_VS_PID")
				.unique(false)
				.withColumns("VALUESET_PID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		version.onTable("TRM_CONCEPT_MAP")
				.addIndex("20230609.17", "FK_TRMCONCEPTMAP_RES")
				.unique(false)
				.withColumns("RES_ID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		version.onTable("TRM_CONCEPT_DESIG")
				.addIndex("20230609.18", "FK_CONCEPTDESIG_CSV")
				.unique(false)
				.withColumns("CS_VER_PID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		version.onTable("TRM_CONCEPT_MAP_GROUP")
				.addIndex("20230609.19", "FK_TCMGROUP_CONCEPTMAP")
				.unique(false)
				.withColumns("CONCEPT_MAP_PID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
				.addIndex("20230609.20", "FK_TCMGELEMENT_GROUP")
				.unique(false)
				.withColumns("CONCEPT_MAP_GROUP_PID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
				.addIndex("20230609.21", "FK_TCMGETARGET_ELEMENT")
				.unique(false)
				.withColumns("CONCEPT_MAP_GRP_ELM_PID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		// add warning message to batch job instance using limited varchar column to store
		version.onTable("BT2_WORK_CHUNK")
				.dropColumn("20230622.1", "WARNING_MSG")
				.failureAllowed();

		version.onTable("BT2_WORK_CHUNK")
				.addColumn("20230622.2", "WARNING_MSG")
				.nullable()
				.type(ColumnTypeEnum.STRING, 4000);

		version.onTable("BT2_JOB_INSTANCE")
				.dropColumn("20230622.3", "WARNING_MSG")
				.failureAllowed();

		version.onTable("BT2_JOB_INSTANCE")
				.addColumn("20230622.4", "WARNING_MSG")
				.nullable()
				.type(ColumnTypeEnum.STRING, 4000);
	}

	protected void init660() {
		Builder version = forVersion(VersionEnum.V6_6_0);

		// fix Postgres clob types - that stupid oid driver problem is still there
		// BT2_JOB_INSTANCE.PARAMS_JSON_LOB
		version.onTable("BT2_JOB_INSTANCE").migratePostgresTextClobToBinaryClob("20230208.1", "PARAMS_JSON_LOB");
		// BT2_JOB_INSTANCE.REPORT
		version.onTable("BT2_JOB_INSTANCE").migratePostgresTextClobToBinaryClob("20230208.2", "REPORT");
		// BT2_WORK_CHUNK.CHUNK_DATA
		version.onTable("BT2_WORK_CHUNK").migratePostgresTextClobToBinaryClob("20230208.3", "CHUNK_DATA");

		{
			Builder.BuilderWithTableName tagDefTable = version.onTable("HFJ_TAG_DEF");

			// add columns
			tagDefTable.addColumn("20230209.1", "TAG_VERSION").nullable().type(ColumnTypeEnum.STRING, 30);
			tagDefTable.addColumn("20230209.2", "TAG_USER_SELECTED").nullable().type(ColumnTypeEnum.BOOLEAN);

			// Update indexing
			tagDefTable.dropIndex("20230209.3", "IDX_TAGDEF_TYPESYSCODE");

			tagDefTable.dropIndex("20230209.4", "IDX_TAGDEF_TYPESYSCODEVERUS");
			Map<DriverTypeEnum, String> addTagDefConstraint = new HashMap<>();
			addTagDefConstraint.put(
					DriverTypeEnum.H2_EMBEDDED,
					"ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODEVERUS UNIQUE (TAG_TYPE, TAG_CODE, TAG_SYSTEM, TAG_VERSION, TAG_USER_SELECTED)");
			addTagDefConstraint.put(
					DriverTypeEnum.MARIADB_10_1,
					"ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODEVERUS UNIQUE (TAG_TYPE, TAG_CODE, TAG_SYSTEM, TAG_VERSION, TAG_USER_SELECTED)");
			addTagDefConstraint.put(
					DriverTypeEnum.MSSQL_2012,
					"ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODEVERUS UNIQUE (TAG_TYPE, TAG_CODE, TAG_SYSTEM, TAG_VERSION, TAG_USER_SELECTED)");
			addTagDefConstraint.put(
					DriverTypeEnum.MYSQL_5_7,
					"ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODEVERUS UNIQUE (TAG_TYPE, TAG_CODE, TAG_SYSTEM, TAG_VERSION, TAG_USER_SELECTED)");
			addTagDefConstraint.put(
					DriverTypeEnum.ORACLE_12C,
					"ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODEVERUS UNIQUE (TAG_TYPE, TAG_CODE, TAG_SYSTEM, TAG_VERSION, TAG_USER_SELECTED)");
			addTagDefConstraint.put(
					DriverTypeEnum.POSTGRES_9_4,
					"ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODEVERUS UNIQUE (TAG_TYPE, TAG_CODE, TAG_SYSTEM, TAG_VERSION, TAG_USER_SELECTED)");
			version.executeRawSql("20230209.5", addTagDefConstraint);
		}

		version.onTable(Search.HFJ_SEARCH)
				.addColumn("20230215.1", Search.SEARCH_UUID)
				.nullable()
				.type(ColumnTypeEnum.STRING, Search.SEARCH_UUID_COLUMN_LENGTH);
		version.onTable(BulkImportJobEntity.HFJ_BLK_IMPORT_JOB)
				.addColumn("20230215.2", BulkImportJobEntity.JOB_ID)
				.nullable()
				.type(ColumnTypeEnum.STRING, UUID_LENGTH);
		version.onTable(BulkExportJobEntity.HFJ_BLK_EXPORT_JOB)
				.addColumn("20230215.3", BulkExportJobEntity.JOB_ID)
				.nullable()
				.type(ColumnTypeEnum.STRING, UUID_LENGTH);

		Builder.BuilderAddTableByColumns resSearchUrlTable =
				version.addTableByColumns("20230227.1", "HFJ_RES_SEARCH_URL", "RES_SEARCH_URL");

		resSearchUrlTable.addColumn("RES_SEARCH_URL").nonNullable().type(ColumnTypeEnum.STRING, 768);
		resSearchUrlTable.addColumn("RES_ID").nonNullable().type(ColumnTypeEnum.LONG);

		resSearchUrlTable.addColumn("CREATED_TIME").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);

		resSearchUrlTable
				.addIndex("20230227.2", "IDX_RESSEARCHURL_RES")
				.unique(false)
				.withColumns("RES_ID");
		resSearchUrlTable
				.addIndex("20230227.3", "IDX_RESSEARCHURL_TIME")
				.unique(false)
				.withColumns("CREATED_TIME");

		{
			// string search index
			Builder.BuilderWithTableName stringTable = version.onTable("HFJ_SPIDX_STRING");

			// add res_id to indentity to speed up sorts.
			stringTable
					.addIndex("20230303.1", "IDX_SP_STRING_HASH_IDENT_V2")
					.unique(false)
					.online(true)
					.withColumns("HASH_IDENTITY", "RES_ID", "PARTITION_ID");
			stringTable.dropIndexOnline("20230303.2", "IDX_SP_STRING_HASH_IDENT");

			// add hash_norm to res_id to speed up joins on a second string.
			stringTable
					.addIndex("20230303.3", "IDX_SP_STRING_RESID_V2")
					.unique(false)
					.online(true)
					.withColumns("RES_ID", "HASH_NORM_PREFIX", "PARTITION_ID");

			// drop and recreate FK_SPIDXSTR_RESOURCE since it will be useing the old IDX_SP_STRING_RESID
			stringTable.dropForeignKey("20230303.4", "FK_SPIDXSTR_RESOURCE", "HFJ_RESOURCE");
			stringTable.dropIndexOnline("20230303.5", "IDX_SP_STRING_RESID");
			stringTable
					.addForeignKey("20230303.6", "FK_SPIDXSTR_RESOURCE")
					.toColumn("RES_ID")
					.references("HFJ_RESOURCE", "RES_ID");
		}

		final String revColumnName = "REV";
		final String enversRevisionTable = "HFJ_REVINFO";
		final String enversMpiLinkAuditTable = "MPI_LINK_AUD";
		final String revTstmpColumnName = "REVTSTMP";

		{
			version.addIdGenerator("20230306.1", "SEQ_HFJ_REVINFO");

			final Builder.BuilderAddTableByColumns enversRevInfo =
					version.addTableByColumns("20230306.2", enversRevisionTable, revColumnName);

			enversRevInfo.addColumn(revColumnName).nonNullable().type(ColumnTypeEnum.LONG);
			enversRevInfo.addColumn(revTstmpColumnName).nullable().type(ColumnTypeEnum.LONG);

			final Builder.BuilderAddTableByColumns empiLink =
					version.addTableByColumns("20230306.6", enversMpiLinkAuditTable, "PID", revColumnName);

			empiLink.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
			empiLink.addColumn("REV").nonNullable().type(ColumnTypeEnum.LONG);
			empiLink.addColumn("REVTYPE").nullable().type(ColumnTypeEnum.TINYINT);
			empiLink.addColumn("PERSON_PID").nullable().type(ColumnTypeEnum.LONG);
			empiLink.addColumn("GOLDEN_RESOURCE_PID").nullable().type(ColumnTypeEnum.LONG);
			empiLink.addColumn("TARGET_TYPE").nullable().type(ColumnTypeEnum.STRING, 40);
			empiLink.addColumn("RULE_COUNT").nullable().type(ColumnTypeEnum.LONG);
			empiLink.addColumn("TARGET_PID").nullable().type(ColumnTypeEnum.LONG);
			empiLink.addColumn("MATCH_RESULT").nullable().type(ColumnTypeEnum.INT);
			empiLink.addColumn("LINK_SOURCE").nullable().type(ColumnTypeEnum.INT);
			empiLink.addColumn("CREATED").nullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
			empiLink.addColumn("UPDATED").nullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
			empiLink.addColumn("VERSION").nullable().type(ColumnTypeEnum.STRING, 16);
			empiLink.addColumn("EID_MATCH").nullable().type(ColumnTypeEnum.BOOLEAN);
			empiLink.addColumn("NEW_PERSON").nullable().type(ColumnTypeEnum.BOOLEAN);
			empiLink.addColumn("VECTOR").nullable().type(ColumnTypeEnum.LONG);
			empiLink.addColumn("SCORE").nullable().type(ColumnTypeEnum.FLOAT);

			// N.B.  It's impossible to rename a foreign key in a Hibernate Envers audit table, and the schema migration
			// unit test will fail if we try to drop and recreate it
			empiLink.addForeignKey("20230306.7", "FKAOW7NXNCLOEC419ARS0FPP58M")
					.toColumn(revColumnName)
					.references(enversRevisionTable, revColumnName);
		}

		{
			Builder.BuilderAddTableByColumns resourceModifiedTable =
					version.addTableByColumns("20230315.1", "HFJ_RESOURCE_MODIFIED", "RES_ID", "RES_VER");
			resourceModifiedTable.addColumn("RES_ID").nonNullable().type(ColumnTypeEnum.STRING, 256);
			resourceModifiedTable.addColumn("RES_VER").nonNullable().type(ColumnTypeEnum.STRING, 8);
			resourceModifiedTable.addColumn("CREATED_TIME").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
			resourceModifiedTable.addColumn("SUMMARY_MESSAGE").nonNullable().type(ColumnTypeEnum.STRING, 4000);
			resourceModifiedTable.addColumn("RESOURCE_TYPE").nonNullable().type(ColumnTypeEnum.STRING, 40);
		}

		{
			// The pre-release already contains the long version of this column
			// We do this becausea doing a modifyColumn on Postgres (and possibly other RDBMS's) will fail with a nasty
			// error:
			// column "revtstmp" cannot be cast automatically to type timestamp without time zone Hint: You might need
			// to specify "USING revtstmp::timestamp without time zone".
			version.onTable(enversRevisionTable).dropColumn("20230316.1", revTstmpColumnName);

			version.onTable(enversRevisionTable)
					.addColumn("20230316.2", revTstmpColumnName)
					.nullable()
					.type(ColumnTypeEnum.DATE_TIMESTAMP);

			// New columns from AuditableBasePartitionable
			version.onTable(enversMpiLinkAuditTable)
					.addColumn("20230316.3", "PARTITION_ID")
					.nullable()
					.type(ColumnTypeEnum.INT);

			version.onTable(enversMpiLinkAuditTable)
					.addColumn("20230316.4", "PARTITION_DATE")
					.nullable()
					.type(ColumnTypeEnum.DATE_ONLY);
		}

		version.onTable(ResourceTable.HFJ_RESOURCE)
				.addColumn("20230323.1", "SEARCH_URL_PRESENT")
				.nullable()
				.type(ColumnTypeEnum.BOOLEAN);

		{
			Builder.BuilderWithTableName uriTable = version.onTable("HFJ_SPIDX_URI");
			uriTable.addIndex("20230324.1", "IDX_SP_URI_HASH_URI_V2")
					.unique(true)
					.online(true)
					.withColumns("HASH_URI", "RES_ID", "PARTITION_ID");
			uriTable.addIndex("20230324.2", "IDX_SP_URI_HASH_IDENTITY_V2")
					.unique(true)
					.online(true)
					.withColumns("HASH_IDENTITY", "SP_URI", "RES_ID", "PARTITION_ID");
			uriTable.dropIndex("20230324.3", "IDX_SP_URI_RESTYPE_NAME");
			uriTable.dropIndex("20230324.4", "IDX_SP_URI_UPDATED");
			uriTable.dropIndex("20230324.5", "IDX_SP_URI");
			uriTable.dropIndex("20230324.6", "IDX_SP_URI_HASH_URI");
			uriTable.dropIndex("20230324.7", "IDX_SP_URI_HASH_IDENTITY");
		}

		version.onTable("HFJ_SPIDX_COORDS").dropIndex("20230325.1", "IDX_SP_COORDS_HASH");
		version.onTable("HFJ_SPIDX_COORDS")
				.addIndex("20230325.2", "IDX_SP_COORDS_HASH_V2")
				.unique(false)
				.online(true)
				.withColumns("HASH_IDENTITY", "SP_LATITUDE", "SP_LONGITUDE", "RES_ID", "PARTITION_ID");

		// Postgres tuning.
		String postgresTuningStatementsAll =
				ClasspathUtil.loadResource("ca/uhn/fhir/jpa/docs/database/hapifhirpostgres94-init01.sql");
		List<String> postgresTuningStatements = Arrays.stream(postgresTuningStatementsAll.split("\\n"))
				.map(StringUtils::trim)
				.filter(StringUtils::isNotBlank)
				.filter(t -> !t.startsWith("--"))
				.collect(Collectors.toList());
		version.executeRawSqls("20230402.1", Map.of(DriverTypeEnum.POSTGRES_9_4, postgresTuningStatements));

		// Use an unlimited length text column for RES_TEXT_VC
		// N.B. This will FAIL SILENTLY on Oracle due to the fact that Oracle does not support an ALTER TABLE from
		// VARCHAR to
		// CLOB.  Because of failureAllowed() this won't halt the migration
		version.onTable("HFJ_RES_VER")
				.modifyColumn("20230421.1", "RES_TEXT_VC")
				.nullable()
				.withType(ColumnTypeEnum.TEXT)
				.failureAllowed();

		{
			// add hash_norm to res_id to speed up joins on a second string.
			Builder.BuilderWithTableName linkTable = version.onTable("HFJ_RES_LINK");
			linkTable
					.addIndex("20230424.1", "IDX_RL_TGT_v2")
					.unique(false)
					.online(true)
					.withColumns(
							"TARGET_RESOURCE_ID",
							"SRC_PATH",
							"SRC_RESOURCE_ID",
							"TARGET_RESOURCE_TYPE",
							"PARTITION_ID");

			// drop and recreate FK_SPIDXSTR_RESOURCE since it will be useing the old IDX_SP_STRING_RESID
			linkTable.dropForeignKey("20230424.2", "FK_RESLINK_TARGET", "HFJ_RESOURCE");
			linkTable.dropIndexOnline("20230424.3", "IDX_RL_TPATHRES");
			linkTable.dropIndexOnline("20230424.4", "IDX_RL_DEST");
			linkTable
					.addForeignKey("20230424.5", "FK_RESLINK_TARGET")
					.toColumn("TARGET_RESOURCE_ID")
					.references("HFJ_RESOURCE", "RES_ID");
		}

		{
			version.onTable("MPI_LINK")
					.addIndex("20230504.1", "IDX_EMPI_GR_TGT")
					.unique(false)
					.withColumns("GOLDEN_RESOURCE_PID", "TARGET_PID");
		}
	}

	protected void init640() {
		Builder version = forVersion(VersionEnum.V6_3_0);

		// start forced_id inline migration
		version.onTable("HFJ_RESOURCE")
				.addColumn("20221108.1", "FHIR_ID")
				.nullable()
				// FHIR ids contain a subset of ascii, limited to 64 chars.
				.type(ColumnTypeEnum.STRING, 64);

		// Add new Index to HFJ_SEARCH_INCLUDE on SEARCH_PID
		version.onTable("HFJ_SEARCH_INCLUDE")
				.addIndex("20221207.1", "FK_SEARCHINC_SEARCH")
				.unique(false)
				.online(true)
				.withColumns("SEARCH_PID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);
	}

	protected void init640_after_20230126() {
		Builder version = forVersion(VersionEnum.V6_3_0);
		{ // We added this constraint when userSelected and Version were added. It is no longer necessary.
			Builder.BuilderWithTableName tagDefTable = version.onTable("HFJ_TAG_DEF");
			tagDefTable.dropIndex("20230503.1", "IDX_TAGDEF_TYPESYSCODEVERUS");
		}
	}

	private void init620() {
		Builder version = forVersion(VersionEnum.V6_2_0);

		// add new REPORT column to BATCH2 tables
		version.onTable("BT2_JOB_INSTANCE")
				.addColumn("20220830.1", "FAST_TRACKING")
				.nullable()
				.type(ColumnTypeEnum.BOOLEAN);

		version.onTable("HFJ_BINARY_STORAGE_BLOB")
				.modifyColumn("20221017.1", "BLOB_SIZE")
				.nullable()
				.withType(ColumnTypeEnum.LONG);

		version.onTable("HFJ_SPIDX_URI")
				.modifyColumn("20221103.1", "SP_URI")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 500);

		version.onTable("BT2_JOB_INSTANCE")
				.addColumn("20230110.1", "UPDATE_TIME")
				.nullable()
				.type(ColumnTypeEnum.DATE_TIMESTAMP);

		version.onTable("BT2_WORK_CHUNK")
				.addColumn("20230110.2", "UPDATE_TIME")
				.nullable()
				.type(ColumnTypeEnum.DATE_TIMESTAMP);

		Map<DriverTypeEnum, String> updateBatch2JobInstance = new HashMap<>();
		updateBatch2JobInstance.put(
				DriverTypeEnum.H2_EMBEDDED,
				"update BT2_JOB_INSTANCE set UPDATE_TIME = coalesce(end_time, start_time, create_time, TIMESTAMP '2023-01-01 00:00:00') where UPDATE_TIME is null");
		updateBatch2JobInstance.put(
				DriverTypeEnum.MARIADB_10_1,
				"update BT2_JOB_INSTANCE set UPDATE_TIME = coalesce(end_time, start_time, create_time, TIMESTAMP '2023-01-01 00:00:00') where UPDATE_TIME is null");
		updateBatch2JobInstance.put(
				DriverTypeEnum.MYSQL_5_7,
				"update BT2_JOB_INSTANCE set UPDATE_TIME = coalesce(end_time, start_time, create_time, TIMESTAMP '2023-01-01 00:00:00') where UPDATE_TIME is null");
		updateBatch2JobInstance.put(
				DriverTypeEnum.ORACLE_12C,
				"update BT2_JOB_INSTANCE set UPDATE_TIME = coalesce(end_time, start_time, create_time, TIMESTAMP '2023-01-01 00:00:00') where UPDATE_TIME is null");
		updateBatch2JobInstance.put(
				DriverTypeEnum.POSTGRES_9_4,
				"update BT2_JOB_INSTANCE set UPDATE_TIME = coalesce(end_time, start_time, create_time, TIMESTAMP '2023-01-01 00:00:00') where UPDATE_TIME is null");
		updateBatch2JobInstance.put(
				DriverTypeEnum.MSSQL_2012,
				"update BT2_JOB_INSTANCE set UPDATE_TIME = coalesce(end_time, start_time, create_time, CONVERT(DATETIME,'2023-01-01 00:00:00')) where UPDATE_TIME is null");
		version.executeRawSql("20230397.1", updateBatch2JobInstance);

		Map<DriverTypeEnum, String> updateBatch2WorkChunk = new HashMap<>();
		updateBatch2WorkChunk.put(
				DriverTypeEnum.H2_EMBEDDED,
				"update bt2_work_chunk set UPDATE_TIME = coalesce(end_time, start_time, create_time, TIMESTAMP '2023-01-01 00:00:00') where UPDATE_TIME is null");
		updateBatch2WorkChunk.put(
				DriverTypeEnum.MARIADB_10_1,
				"update bt2_work_chunk set UPDATE_TIME = coalesce(end_time, start_time, create_time, TIMESTAMP '2023-01-01 00:00:00') where UPDATE_TIME is null");
		updateBatch2WorkChunk.put(
				DriverTypeEnum.MYSQL_5_7,
				"update bt2_work_chunk set UPDATE_TIME = coalesce(end_time, start_time, create_time, TIMESTAMP '2023-01-01 00:00:00') where UPDATE_TIME is null");
		updateBatch2WorkChunk.put(
				DriverTypeEnum.ORACLE_12C,
				"update bt2_work_chunk set UPDATE_TIME = coalesce(end_time, start_time, create_time, TIMESTAMP '2023-01-01 00:00:00') where UPDATE_TIME is null");
		updateBatch2WorkChunk.put(
				DriverTypeEnum.POSTGRES_9_4,
				"update bt2_work_chunk set UPDATE_TIME = coalesce(end_time, start_time, create_time, TIMESTAMP '2023-01-01 00:00:00') where UPDATE_TIME is null");
		updateBatch2WorkChunk.put(
				DriverTypeEnum.MSSQL_2012,
				"update bt2_work_chunk set UPDATE_TIME = coalesce(end_time, start_time, create_time, CONVERT(DATETIME,'2023-01-01 00:00:00')) where UPDATE_TIME is null");
		version.executeRawSql("20230397.2", updateBatch2WorkChunk);
	}

	private void init610() {
		Builder version = forVersion(VersionEnum.V6_1_0);

		// add new REPORT column to BATCH2 tables
		version.onTable("BT2_JOB_INSTANCE")
				.addColumn("20220601.1", "REPORT")
				.nullable()
				.type(ColumnTypeEnum.CLOB);
	}

	private void init600() {
		Builder version = forVersion(VersionEnum.V6_0_0);

		/**
		 * New indexing for the core SPIDX tables.
		 * Ensure all queries can be satisfied by the index directly,
		 * either as left or right table in a hash or sort join.
		 *
		 * new date search indexing
		 * @see ca.uhn.fhir.jpa.search.builder.predicate.DatePredicateBuilder
		 * @see ResourceIndexedSearchParamDate
		 */
		{
			Builder.BuilderWithTableName dateTable = version.onTable("HFJ_SPIDX_DATE");

			// replace and drop IDX_SP_DATE_HASH
			dateTable
					.addIndex("20220207.1", "IDX_SP_DATE_HASH_V2")
					.unique(false)
					.online(true)
					.withColumns("HASH_IDENTITY", "SP_VALUE_LOW", "SP_VALUE_HIGH", "RES_ID", "PARTITION_ID");
			dateTable.dropIndexOnline("20220207.2", "IDX_SP_DATE_HASH");

			// drop redundant
			dateTable.dropIndexOnline("20220207.3", "IDX_SP_DATE_HASH_LOW");

			// replace and drop IDX_SP_DATE_HASH_HIGH
			dateTable
					.addIndex("20220207.4", "IDX_SP_DATE_HASH_HIGH_V2")
					.unique(false)
					.online(true)
					.withColumns("HASH_IDENTITY", "SP_VALUE_HIGH", "RES_ID", "PARTITION_ID");
			dateTable.dropIndexOnline("20220207.5", "IDX_SP_DATE_HASH_HIGH");

			// replace and drop IDX_SP_DATE_ORD_HASH
			dateTable
					.addIndex("20220207.6", "IDX_SP_DATE_ORD_HASH_V2")
					.unique(false)
					.online(true)
					.withColumns(
							"HASH_IDENTITY",
							"SP_VALUE_LOW_DATE_ORDINAL",
							"SP_VALUE_HIGH_DATE_ORDINAL",
							"RES_ID",
							"PARTITION_ID");
			dateTable.dropIndexOnline("20220207.7", "IDX_SP_DATE_ORD_HASH");

			// replace and drop IDX_SP_DATE_ORD_HASH_HIGH
			dateTable
					.addIndex("20220207.8", "IDX_SP_DATE_ORD_HASH_HIGH_V2")
					.unique(false)
					.online(true)
					.withColumns("HASH_IDENTITY", "SP_VALUE_HIGH_DATE_ORDINAL", "RES_ID", "PARTITION_ID");
			dateTable.dropIndexOnline("20220207.9", "IDX_SP_DATE_ORD_HASH_HIGH");

			// drop redundant
			dateTable.dropIndexOnline("20220207.10", "IDX_SP_DATE_ORD_HASH_LOW");

			// replace and drop IDX_SP_DATE_RESID
			dateTable
					.addIndex("20220207.11", "IDX_SP_DATE_RESID_V2")
					.unique(false)
					.online(true)
					.withColumns(
							"RES_ID",
							"HASH_IDENTITY",
							"SP_VALUE_LOW",
							"SP_VALUE_HIGH",
							"SP_VALUE_LOW_DATE_ORDINAL",
							"SP_VALUE_HIGH_DATE_ORDINAL",
							"PARTITION_ID");
			// some engines tie the FK constraint to a particular index.
			// So we need to drop and recreate the constraint to drop the old RES_ID index.
			// Rename it while we're at it.  FK17s70oa59rm9n61k9thjqrsqm was not a pretty name.
			dateTable.dropForeignKey("20220207.12", "FK17S70OA59RM9N61K9THJQRSQM", "HFJ_RESOURCE");
			dateTable.dropIndexOnline("20220207.13", "IDX_SP_DATE_RESID");
			dateTable.dropIndexOnline("20220207.14", "FK17S70OA59RM9N61K9THJQRSQM");

			dateTable
					.addForeignKey("20220207.15", "FK_SP_DATE_RES")
					.toColumn("RES_ID")
					.references("HFJ_RESOURCE", "RES_ID");

			// drop obsolete
			dateTable.dropIndexOnline("20220207.16", "IDX_SP_DATE_UPDATED");
		}

		/**
		 * new token search indexing
		 * @see ca.uhn.fhir.jpa.search.builder.predicate.TokenPredicateBuilder
		 * @see ResourceIndexedSearchParamToken
		 */
		{
			Builder.BuilderWithTableName tokenTable = version.onTable("HFJ_SPIDX_TOKEN");

			// replace and drop IDX_SP_TOKEN_HASH for sorting
			tokenTable
					.addIndex("20220208.1", "IDX_SP_TOKEN_HASH_V2")
					.unique(false)
					.online(true)
					.withColumns("HASH_IDENTITY", "SP_SYSTEM", "SP_VALUE", "RES_ID", "PARTITION_ID");

			tokenTable.dropIndexOnline("20220208.2", "IDX_SP_TOKEN_HASH");

			// for search by system
			tokenTable
					.addIndex("20220208.3", "IDX_SP_TOKEN_HASH_S_V2")
					.unique(false)
					.online(true)
					.withColumns("HASH_SYS", "RES_ID", "PARTITION_ID");

			tokenTable.dropIndexOnline("20220208.4", "IDX_SP_TOKEN_HASH_S");

			// for search by system+value
			tokenTable
					.addIndex("20220208.5", "IDX_SP_TOKEN_HASH_SV_V2")
					.unique(false)
					.online(true)
					.withColumns("HASH_SYS_AND_VALUE", "RES_ID", "PARTITION_ID");

			tokenTable.dropIndexOnline("20220208.6", "IDX_SP_TOKEN_HASH_SV");

			// for search by value
			tokenTable
					.addIndex("20220208.7", "IDX_SP_TOKEN_HASH_V_V2")
					.unique(false)
					.online(true)
					.withColumns("HASH_VALUE", "RES_ID", "PARTITION_ID");

			tokenTable.dropIndexOnline("20220208.8", "IDX_SP_TOKEN_HASH_V");

			// obsolete.  We're dropping this column.
			tokenTable.dropIndexOnline("20220208.9", "IDX_SP_TOKEN_UPDATED");

			// for joining as second table:
			{
				// replace and drop IDX_SP_TOKEN_RESID, and the associated fk constraint
				tokenTable
						.addIndex("20220208.10", "IDX_SP_TOKEN_RESID_V2")
						.unique(false)
						.online(true)
						.withColumns(
								"RES_ID",
								"HASH_SYS_AND_VALUE",
								"HASH_VALUE",
								"HASH_SYS",
								"HASH_IDENTITY",
								"PARTITION_ID");

				// some engines tie the FK constraint to a particular index.
				// So we need to drop and recreate the constraint to drop the old RES_ID index.
				// Rename it while we're at it.  FK7ULX3J1GG3V7MAQREJGC7YBC4 was not a pretty name.
				tokenTable.dropForeignKey("20220208.11", "FK7ULX3J1GG3V7MAQREJGC7YBC4", "HFJ_RESOURCE");
				tokenTable.dropIndexOnline("20220208.12", "IDX_SP_TOKEN_RESID");
				tokenTable.dropIndexOnline("20220208.13", "FK7ULX3J1GG3V7MAQREJGC7YBC4");

				tokenTable
						.addForeignKey("20220208.14", "FK_SP_TOKEN_RES")
						.toColumn("RES_ID")
						.references("HFJ_RESOURCE", "RES_ID");
			}
		}

		// fix for https://github.com/hapifhir/hapi-fhir/issues/3316
		// index must have same name that indexed FK or SchemaMigrationTest complains because H2 sets this index
		// automatically

		version.onTable("TRM_VALUESET_C_DESIGNATION")
				.addIndex("20220223.1", "FK_TRM_VALUESET_CONCEPT_PID")
				.unique(false)
				.withColumns("VALUESET_CONCEPT_PID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		// Batch2 Framework

		Builder.BuilderAddTableByColumns batchInstance =
				version.addTableByColumns("20220227.1", "BT2_JOB_INSTANCE", "ID");
		batchInstance.addColumn("ID").nonNullable().type(ColumnTypeEnum.STRING, 100);
		batchInstance.addColumn("CREATE_TIME").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		batchInstance.addColumn("START_TIME").nullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		batchInstance.addColumn("END_TIME").nullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		batchInstance.addColumn("DEFINITION_ID").nonNullable().type(ColumnTypeEnum.STRING, 100);
		batchInstance.addColumn("DEFINITION_VER").nonNullable().type(ColumnTypeEnum.INT);
		batchInstance.addColumn("STAT").nonNullable().type(ColumnTypeEnum.STRING, 20);
		batchInstance.addColumn("JOB_CANCELLED").nonNullable().type(ColumnTypeEnum.BOOLEAN);
		batchInstance.addColumn("PARAMS_JSON").nullable().type(ColumnTypeEnum.STRING, 2000);
		batchInstance.addColumn("PARAMS_JSON_LOB").nullable().type(ColumnTypeEnum.CLOB);
		batchInstance.addColumn("CMB_RECS_PROCESSED").nullable().type(ColumnTypeEnum.INT);
		batchInstance.addColumn("CMB_RECS_PER_SEC").nullable().type(ColumnTypeEnum.DOUBLE);
		batchInstance.addColumn("TOT_ELAPSED_MILLIS").nullable().type(ColumnTypeEnum.INT);
		batchInstance.addColumn("WORK_CHUNKS_PURGED").nonNullable().type(ColumnTypeEnum.BOOLEAN);
		batchInstance.addColumn("PROGRESS_PCT").nullable().type(ColumnTypeEnum.DOUBLE);
		batchInstance.addColumn("ERROR_MSG").nullable().type(ColumnTypeEnum.STRING, 500);
		batchInstance.addColumn("ERROR_COUNT").nullable().type(ColumnTypeEnum.INT);
		batchInstance.addColumn("EST_REMAINING").nullable().type(ColumnTypeEnum.STRING, 100);
		batchInstance.addIndex("20220227.2", "IDX_BT2JI_CT").unique(false).withColumns("CREATE_TIME");

		Builder.BuilderAddTableByColumns batchChunk = version.addTableByColumns("20220227.3", "BT2_WORK_CHUNK", "ID");
		batchChunk.addColumn("ID").nonNullable().type(ColumnTypeEnum.STRING, 100);
		batchChunk.addColumn("SEQ").nonNullable().type(ColumnTypeEnum.INT);
		batchChunk.addColumn("CREATE_TIME").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		batchChunk.addColumn("START_TIME").nullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		batchChunk.addColumn("END_TIME").nullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		batchChunk.addColumn("DEFINITION_ID").nonNullable().type(ColumnTypeEnum.STRING, 100);
		batchChunk.addColumn("DEFINITION_VER").nonNullable().type(ColumnTypeEnum.INT);
		batchChunk.addColumn("STAT").nonNullable().type(ColumnTypeEnum.STRING, 20);
		batchChunk.addColumn("RECORDS_PROCESSED").nullable().type(ColumnTypeEnum.INT);
		batchChunk.addColumn("TGT_STEP_ID").nonNullable().type(ColumnTypeEnum.STRING, 100);
		batchChunk.addColumn("CHUNK_DATA").nullable().type(ColumnTypeEnum.CLOB);
		batchChunk.addColumn("INSTANCE_ID").nonNullable().type(ColumnTypeEnum.STRING, 100);
		batchChunk.addColumn("ERROR_MSG").nullable().type(ColumnTypeEnum.STRING, 500);
		batchChunk.addColumn("ERROR_COUNT").nonNullable().type(ColumnTypeEnum.INT);
		batchChunk.addIndex("20220227.4", "IDX_BT2WC_II_SEQ").unique(false).withColumns("INSTANCE_ID", "SEQ");
		batchChunk
				.addForeignKey("20220227.5", "FK_BT2WC_INSTANCE")
				.toColumn("INSTANCE_ID")
				.references("BT2_JOB_INSTANCE", "ID");

		replaceNumericSPIndices(version);
		replaceQuantitySPIndices(version);

		// Drop Index on HFJ_RESOURCE.INDEX_STATUS
		version.onTable("HFJ_RESOURCE").dropIndex("20220314.1", "IDX_INDEXSTATUS");

		version.onTable("BT2_JOB_INSTANCE")
				.addColumn("20220416.1", "CUR_GATED_STEP_ID")
				.nullable()
				.type(ColumnTypeEnum.STRING, 100);

		// Make Job expiry nullable so that we can prevent job expiry by using a null value.
		version.onTable("HFJ_BLK_EXPORT_JOB")
				.modifyColumn("20220423.1", "EXP_TIME")
				.nullable()
				.withType(ColumnTypeEnum.DATE_TIMESTAMP);

		// New Index on HFJ_RESOURCE for $reindex Operation - hapi-fhir #3534
		{
			version.onTable("HFJ_RESOURCE")
					.addIndex("20220425.1", "IDX_RES_TYPE_DEL_UPDATED")
					.unique(false)
					.online(true)
					.withColumns("RES_TYPE", "RES_DELETED_AT", "RES_UPDATED", "PARTITION_ID", "RES_ID");

			// Drop existing Index on HFJ_RESOURCE.RES_TYPE since the new Index will meet the overall Index Demand
			version.onTable("HFJ_RESOURCE").dropIndexOnline("20220425.2", "IDX_RES_TYPE");
		}

		/**
		 * Update string indexing
		 * @see ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder
		 * @see ResourceIndexedSearchParamString
		 */
		{
			Builder.BuilderWithTableName tokenTable = version.onTable("HFJ_SPIDX_STRING");

			// add res_id, and partition_id so queries are covered without row-reads.
			tokenTable
					.addIndex("20220428.1", "IDX_SP_STRING_HASH_NRM_V2")
					.unique(false)
					.online(true)
					.withColumns("HASH_NORM_PREFIX", "SP_VALUE_NORMALIZED", "RES_ID", "PARTITION_ID");
			tokenTable.dropIndexOnline("20220428.2", "IDX_SP_STRING_HASH_NRM");

			tokenTable
					.addIndex("20220428.3", "IDX_SP_STRING_HASH_EXCT_V2")
					.unique(false)
					.online(true)
					.withColumns("HASH_EXACT", "RES_ID", "PARTITION_ID");
			tokenTable.dropIndexOnline("20220428.4", "IDX_SP_STRING_HASH_EXCT");

			// we will drop the updated column.  Start with the index.
			tokenTable.dropIndexOnline("20220428.5", "IDX_SP_STRING_UPDATED");
		}

		// Update tag indexing
		{
			Builder.BuilderWithTableName resTagTable = version.onTable("HFJ_RES_TAG");

			// add res_id, and partition_id so queries are covered without row-reads.
			resTagTable
					.addIndex("20220429.1", "IDX_RES_TAG_RES_TAG")
					.unique(false)
					.online(true)
					.withColumns("RES_ID", "TAG_ID", "PARTITION_ID");
			resTagTable
					.addIndex("20220429.2", "IDX_RES_TAG_TAG_RES")
					.unique(false)
					.online(true)
					.withColumns("TAG_ID", "RES_ID", "PARTITION_ID");

			resTagTable.dropIndex("20220429.4", "IDX_RESTAG_TAGID");
			// Weird that we don't have addConstraint.  No time to do it today.
			Map<DriverTypeEnum, String> addResTagConstraint = new HashMap<>();
			addResTagConstraint.put(
					DriverTypeEnum.H2_EMBEDDED,
					"ALTER TABLE HFJ_RES_TAG ADD CONSTRAINT IDX_RESTAG_TAGID UNIQUE (RES_ID, TAG_ID)");
			addResTagConstraint.put(
					DriverTypeEnum.MARIADB_10_1,
					"ALTER TABLE HFJ_RES_TAG ADD CONSTRAINT IDX_RESTAG_TAGID UNIQUE (RES_ID, TAG_ID)");
			addResTagConstraint.put(
					DriverTypeEnum.MSSQL_2012,
					"ALTER TABLE HFJ_RES_TAG ADD CONSTRAINT IDX_RESTAG_TAGID UNIQUE (RES_ID, TAG_ID)");
			addResTagConstraint.put(
					DriverTypeEnum.MYSQL_5_7,
					"ALTER TABLE HFJ_RES_TAG ADD CONSTRAINT IDX_RESTAG_TAGID UNIQUE (RES_ID, TAG_ID)");
			addResTagConstraint.put(
					DriverTypeEnum.ORACLE_12C,
					"ALTER TABLE HFJ_RES_TAG ADD CONSTRAINT IDX_RESTAG_TAGID UNIQUE (RES_ID, TAG_ID)");
			addResTagConstraint.put(
					DriverTypeEnum.POSTGRES_9_4,
					"ALTER TABLE HFJ_RES_TAG ADD CONSTRAINT IDX_RESTAG_TAGID UNIQUE (RES_ID, TAG_ID)");
			version.executeRawSql("20220429.5", addResTagConstraint);

			Builder.BuilderWithTableName tagTable = version.onTable("HFJ_TAG_DEF");
			tagTable.addIndex("20220429.6", "IDX_TAG_DEF_TP_CD_SYS")
					.unique(false)
					.online(false)
					.withColumns("TAG_TYPE", "TAG_CODE", "TAG_SYSTEM", "TAG_ID");
			// move constraint to new index
			// Ugh.  Only oracle supports using IDX_TAG_DEF_TP_CD_SYS to enforce this constraint.  The others will
			// create another index.
			// For Sql Server, should change the index to be unique with include columns.  Do this in 6.1
			//			tagTable.dropIndex("20220429.8", "IDX_TAGDEF_TYPESYSCODE");
			//			Map<DriverTypeEnum, String> addTagDefConstraint = new HashMap<>();
			//			addTagDefConstraint.put(
			//					DriverTypeEnum.H2_EMBEDDED,
			//					"ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODE UNIQUE (TAG_TYPE, TAG_CODE,
			// TAG_SYSTEM)");
			//			addTagDefConstraint.put(
			//					DriverTypeEnum.MARIADB_10_1,
			//					"ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODE UNIQUE (TAG_TYPE, TAG_CODE,
			// TAG_SYSTEM)");
			//			addTagDefConstraint.put(
			//					DriverTypeEnum.MSSQL_2012,
			//					"ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODE UNIQUE (TAG_TYPE, TAG_CODE,
			// TAG_SYSTEM)");
			//			addTagDefConstraint.put(
			//					DriverTypeEnum.MYSQL_5_7,
			//					"ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODE UNIQUE (TAG_TYPE, TAG_CODE,
			// TAG_SYSTEM)");
			//			addTagDefConstraint.put(
			//					DriverTypeEnum.ORACLE_12C,
			//					"ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODE UNIQUE (TAG_TYPE, TAG_CODE,
			// TAG_SYSTEM)");
			//			addTagDefConstraint.put(
			//					DriverTypeEnum.POSTGRES_9_4,
			//					"ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODE UNIQUE (TAG_TYPE, TAG_CODE,
			// TAG_SYSTEM)");
			//			version.executeRawSql("20220429.9", addTagDefConstraint);
			version.addNop("20220429.9");
		}

		// Fix for https://github.com/hapifhir/hapi-fhir-jpaserver-starter/issues/328
		version.onTable("NPM_PACKAGE_VER")
				.modifyColumn("20220501.1", "FHIR_VERSION_ID")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 20);

		version.onTable("NPM_PACKAGE_VER_RES")
				.modifyColumn("20220501.2", "FHIR_VERSION_ID")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 20);

		// Fix for https://gitlab.com/simpatico.ai/cdr/-/issues/3166
		version.onTable("MPI_LINK")
				.addIndex("20220613.1", "IDX_EMPI_MATCH_TGT_VER")
				.unique(false)
				.online(true)
				.withColumns("MATCH_RESULT", "TARGET_PID", "VERSION");
	}

	/**
	 * new numeric search indexing
	 *
	 * @see ca.uhn.fhir.jpa.search.builder.predicate.NumberPredicateBuilder
	 * @see ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber
	 */
	private void replaceNumericSPIndices(Builder theVersion) {
		Builder.BuilderWithTableName numberTable = theVersion.onTable("HFJ_SPIDX_NUMBER");

		// Main query index
		numberTable
				.addIndex("20220304.1", "IDX_SP_NUMBER_HASH_VAL_V2")
				.unique(false)
				.online(true)
				.withColumns("HASH_IDENTITY", "SP_VALUE", "RES_ID", "PARTITION_ID");

		numberTable.dropIndexOnline("20220304.2", "IDX_SP_NUMBER_HASH_VAL");

		// for joining to other queries
		{
			numberTable
					.addIndex("20220304.3", "IDX_SP_NUMBER_RESID_V2")
					.unique(false)
					.online(true)
					.withColumns("RES_ID", "HASH_IDENTITY", "SP_VALUE", "PARTITION_ID");

			// some engines tie the FK constraint to a particular index.
			// So we need to drop and recreate the constraint to drop the old RES_ID index.
			// Rename it while we're at it.  FK7ULX3J1GG3V7MAQREJGC7YBC4 was not a pretty name.
			numberTable.dropForeignKey("20220304.4", "FKCLTIHNC5TGPRJ9BHPT7XI5OTB", "HFJ_RESOURCE");
			numberTable.dropIndexOnline("20220304.5", "IDX_SP_NUMBER_RESID");
			numberTable.dropIndexOnline("20220304.6", "FKCLTIHNC5TGPRJ9BHPT7XI5OTB");

			numberTable
					.addForeignKey("20220304.7", "FK_SP_NUMBER_RES")
					.toColumn("RES_ID")
					.references("HFJ_RESOURCE", "RES_ID");
		}
		// obsolete
		numberTable.dropIndexOnline("20220304.8", "IDX_SP_NUMBER_UPDATED");
	}

	/**
	 * new quantity search indexing
	 *
	 * @see ca.uhn.fhir.jpa.search.builder.predicate.QuantityPredicateBuilder
	 * @see ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity
	 * @see ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized
	 */
	private void replaceQuantitySPIndices(Builder theVersion) {
		{
			Builder.BuilderWithTableName quantityTable = theVersion.onTable("HFJ_SPIDX_QUANTITY");

			// bare quantity
			quantityTable
					.addIndex("20220304.11", "IDX_SP_QUANTITY_HASH_V2")
					.unique(false)
					.online(true)
					.withColumns("HASH_IDENTITY", "SP_VALUE", "RES_ID", "PARTITION_ID");

			quantityTable.dropIndexOnline("20220304.12", "IDX_SP_QUANTITY_HASH");

			// quantity with system+units
			quantityTable
					.addIndex("20220304.13", "IDX_SP_QUANTITY_HASH_SYSUN_V2")
					.unique(false)
					.online(true)
					.withColumns("HASH_IDENTITY_SYS_UNITS", "SP_VALUE", "RES_ID", "PARTITION_ID");

			quantityTable.dropIndexOnline("20220304.14", "IDX_SP_QUANTITY_HASH_SYSUN");

			// quantity with units
			quantityTable
					.addIndex("20220304.15", "IDX_SP_QUANTITY_HASH_UN_V2")
					.unique(false)
					.online(true)
					.withColumns("HASH_IDENTITY_AND_UNITS", "SP_VALUE", "RES_ID", "PARTITION_ID");

			quantityTable.dropIndexOnline("20220304.16", "IDX_SP_QUANTITY_HASH_UN");

			// for joining to other queries and sorts
			{
				quantityTable
						.addIndex("20220304.17", "IDX_SP_QUANTITY_RESID_V2")
						.unique(false)
						.online(true)
						.withColumns(
								"RES_ID",
								"HASH_IDENTITY",
								"HASH_IDENTITY_SYS_UNITS",
								"HASH_IDENTITY_AND_UNITS",
								"SP_VALUE",
								"PARTITION_ID");

				// some engines tie the FK constraint to a particular index.
				// So we need to drop and recreate the constraint to drop the old RES_ID index.
				// Rename it while we're at it.  FK7ULX3J1GG3V7MAQREJGC7YBC4 was not a pretty name.
				quantityTable.dropForeignKey("20220304.18", "FKN603WJJOI1A6ASEWXBBD78BI5", "HFJ_RESOURCE");
				quantityTable.dropIndexOnline("20220304.19", "IDX_SP_QUANTITY_RESID");
				quantityTable.dropIndexOnline("20220304.20", "FKN603WJJOI1A6ASEWXBBD78BI5");

				quantityTable
						.addForeignKey("20220304.21", "FK_SP_QUANTITY_RES")
						.toColumn("RES_ID")
						.references("HFJ_RESOURCE", "RES_ID");
			}
			// obsolete
			quantityTable.dropIndexOnline("20220304.22", "IDX_SP_QUANTITY_UPDATED");
		}

		{
			Builder.BuilderWithTableName quantityNormTable = theVersion.onTable("HFJ_SPIDX_QUANTITY_NRML");

			// bare quantity
			quantityNormTable
					.addIndex("20220304.23", "IDX_SP_QNTY_NRML_HASH_V2")
					.unique(false)
					.online(true)
					.withColumns("HASH_IDENTITY", "SP_VALUE", "RES_ID", "PARTITION_ID");

			quantityNormTable.dropIndexOnline("20220304.24", "IDX_SP_QNTY_NRML_HASH");

			// quantity with system+units
			quantityNormTable
					.addIndex("20220304.25", "IDX_SP_QNTY_NRML_HASH_SYSUN_V2")
					.unique(false)
					.online(true)
					.withColumns("HASH_IDENTITY_SYS_UNITS", "SP_VALUE", "RES_ID", "PARTITION_ID");

			quantityNormTable.dropIndexOnline("20220304.26", "IDX_SP_QNTY_NRML_HASH_SYSUN");

			// quantity with units
			quantityNormTable
					.addIndex("20220304.27", "IDX_SP_QNTY_NRML_HASH_UN_V2")
					.unique(false)
					.online(true)
					.withColumns("HASH_IDENTITY_AND_UNITS", "SP_VALUE", "RES_ID", "PARTITION_ID");

			quantityNormTable.dropIndexOnline("20220304.28", "IDX_SP_QNTY_NRML_HASH_UN");

			// for joining to other queries and sorts
			{
				quantityNormTable
						.addIndex("20220304.29", "IDX_SP_QNTY_NRML_RESID_V2")
						.unique(false)
						.online(true)
						.withColumns(
								"RES_ID",
								"HASH_IDENTITY",
								"HASH_IDENTITY_SYS_UNITS",
								"HASH_IDENTITY_AND_UNITS",
								"SP_VALUE",
								"PARTITION_ID");

				// some engines tie the FK constraint to a particular index.
				// So we need to drop and recreate the constraint to drop the old RES_ID index.
				// Rename it while we're at it.  FK7ULX3J1GG3V7MAQREJGC7YBC4 was not a pretty name.
				quantityNormTable.dropForeignKey("20220304.30", "FKRCJOVMUH5KC0O6FVBLE319PYV", "HFJ_RESOURCE");
				quantityNormTable.dropIndexOnline("20220304.31", "IDX_SP_QNTY_NRML_RESID");
				quantityNormTable.dropIndexOnline("20220304.32", "FKRCJOVMUH5KC0O6FVBLE319PYV");

				quantityNormTable
						.addForeignKey("20220304.33", "FK_SP_QUANTITYNM_RES")
						.toColumn("RES_ID")
						.references("HFJ_RESOURCE", "RES_ID");
			}
			// obsolete
			quantityNormTable.dropIndexOnline("20220304.34", "IDX_SP_QNTY_NRML_UPDATED");
		}
	}

	/**
	 * See https://github.com/hapifhir/hapi-fhir/issues/3237 for reasoning for these indexes.
	 * This adds indexes to various tables to enhance delete-expunge performance, which deletes by PID.
	 */
	private void addIndexesForDeleteExpunge(Builder theVersion) {

		theVersion
				.onTable("HFJ_HISTORY_TAG")
				.addIndex("20211210.2", "IDX_RESHISTTAG_RESID")
				.unique(false)
				.withColumns("RES_ID");

		theVersion
				.onTable("HFJ_RES_VER_PROV")
				.addIndex("20211210.3", "FK_RESVERPROV_RES_PID")
				.unique(false)
				.withColumns("RES_PID")
				.doNothing() // This index is added below in a better form
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		theVersion
				.onTable("HFJ_FORCED_ID")
				.addIndex("20211210.4", "FK_FORCEDID_RESOURCE")
				.unique(true)
				.withColumns("RESOURCE_PID")
				// RESOURCE_PID and every database creates an index on anything that is unique.
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS)
				.doNothing(); // This migration was added in error, as this table already has a unique constraint on
	}

	private void init570() {
		Builder version = forVersion(VersionEnum.V5_7_0);

		// both indexes must have same name that indexed FK or SchemaMigrationTest complains because H2 sets this index
		// automatically

		version.onTable("TRM_CONCEPT_PROPERTY")
				.addIndex("20211102.1", "FK_CONCEPTPROP_CONCEPT")
				.unique(false)
				.withColumns("CONCEPT_PID")
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		version.onTable("TRM_CONCEPT_DESIG")
				.addIndex("20211102.2", "FK_CONCEPTDESIG_CONCEPT")
				.unique(false)
				.withColumns("CONCEPT_PID")
				// H2, Derby, MariaDB, and MySql automatically add indexes to foreign keys
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		version.onTable("TRM_CONCEPT_PC_LINK")
				.addIndex("20211102.3", "FK_TERM_CONCEPTPC_CHILD")
				.unique(false)
				.withColumns("CHILD_PID")
				// H2, Derby, MariaDB, and MySql automatically add indexes to foreign keys
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		version.onTable("TRM_CONCEPT_PC_LINK")
				.addIndex("20211102.4", "FK_TERM_CONCEPTPC_PARENT")
				.unique(false)
				.withColumns("PARENT_PID")
				// H2, Derby, MariaDB, and MySql automatically add indexes to foreign keys
				.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		addIndexesForDeleteExpunge(version);

		// Add inline resource text column
		version.onTable("HFJ_RES_VER")
				.addColumn("20220102.1", "RES_TEXT_VC")
				.nullable()
				.type(ColumnTypeEnum.STRING, 4000);

		// Add partition id column for mdm
		Builder.BuilderWithTableName empiLink = version.onTable("MPI_LINK");

		empiLink.addColumn("20220324.1", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		empiLink.addColumn("20220324.2", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
	}

	private void init560() {
		init560_20211027();
	}

	/**
	 * Mirgation for the batch job parameter size change. Overriding purposes only.
	 */
	protected void init560_20211027() {
		// nothing
	}

	private void init550() {

		Builder version = forVersion(VersionEnum.V5_5_0);

		// For MSSQL only - Replace ForcedId index with a version that has an INCLUDE clause
		Builder.BuilderWithTableName forcedId = version.onTable("HFJ_FORCED_ID");
		forcedId.dropIndex("20210516.1", "IDX_FORCEDID_TYPE_FID")
				.onlyAppliesToPlatforms(DriverTypeEnum.MSSQL_2012)
				.runEvenDuringSchemaInitialization();
		forcedId.addIndex("20210516.2", "IDX_FORCEDID_TYPE_FID")
				.unique(true)
				.includeColumns("RESOURCE_PID")
				.withColumns("RESOURCE_TYPE", "FORCED_ID")
				.onlyAppliesToPlatforms(DriverTypeEnum.MSSQL_2012)
				.runEvenDuringSchemaInitialization();

		// Add bulk import file description
		version.onTable("HFJ_BLK_IMPORT_JOBFILE")
				.addColumn("20210528.1", "FILE_DESCRIPTION")
				.nullable()
				.type(ColumnTypeEnum.STRING, 500);

		// Bump ConceptMap display lengths
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
				.modifyColumn("20210617.1", "TARGET_DISPLAY")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 500);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
				.modifyColumn("20210617.2", "SOURCE_DISPLAY")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 500);

		version.onTable("HFJ_BLK_EXPORT_JOB")
				.modifyColumn("20210624.1", "REQUEST")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 1024);

		version.onTable("HFJ_IDX_CMP_STRING_UNIQ")
				.modifyColumn("20210713.1", "IDX_STRING")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 500);

		version.onTable("HFJ_RESOURCE")
				.addColumn("20210720.1", "SP_CMPTOKS_PRESENT")
				.nullable()
				.type(ColumnTypeEnum.BOOLEAN);

		version.addIdGenerator("20210720.2", "SEQ_IDXCMBTOKNU_ID");

		Builder.BuilderAddTableByColumns cmpToks = version.addTableByColumns("20210720.3", "HFJ_IDX_CMB_TOK_NU", "PID");
		cmpToks.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		cmpToks.addColumn("RES_ID").nonNullable().type(ColumnTypeEnum.LONG);
		cmpToks.addColumn("HASH_COMPLETE").nonNullable().type(ColumnTypeEnum.LONG);
		cmpToks.addColumn("IDX_STRING").nonNullable().type(ColumnTypeEnum.STRING, 500);
		cmpToks.addForeignKey("20210720.4", "FK_IDXCMBTOKNU_RES_ID")
				.toColumn("RES_ID")
				.references("HFJ_RESOURCE", "RES_ID");
		cmpToks.addIndex("20210720.5", "IDX_IDXCMBTOKNU_STR").unique(false).withColumns("IDX_STRING");
		cmpToks.addIndex("20210720.6", "IDX_IDXCMBTOKNU_RES").unique(false).withColumns("RES_ID");

		Builder.BuilderWithTableName cmbTokNuTable = version.onTable("HFJ_IDX_CMB_TOK_NU");

		cmbTokNuTable.addColumn("20210722.1", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		cmbTokNuTable.addColumn("20210722.2", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		cmbTokNuTable.modifyColumn("20210722.3", "RES_ID").nullable().withType(ColumnTypeEnum.LONG);

		// Dropping index on the language column, as it's no longer in use.
		// TODO: After 2 releases from 5.5.0, drop the column too
		version.onTable("HFJ_RESOURCE").dropIndex("20210908.1", "IDX_RES_LANG");

		version.onTable("TRM_VALUESET")
				.addColumn("20210915.1", "EXPANDED_AT")
				.nullable()
				.type(ColumnTypeEnum.DATE_TIMESTAMP);

		/*
		 * Replace CLOB columns with BLOB columns
		 */

		// TRM_VALUESET_CONCEPT.SOURCE_DIRECT_PARENT_PIDS
		version.onTable("TRM_VALUESET_CONCEPT")
				.migratePostgresTextClobToBinaryClob("20211003.1", "SOURCE_DIRECT_PARENT_PIDS");

		// TRM_CONCEPT.PARENT_PIDS
		version.onTable("TRM_CONCEPT").migratePostgresTextClobToBinaryClob("20211003.2", "PARENT_PIDS");

		// HFJ_SEARCH.SEARCH_QUERY_STRING
		version.onTable("HFJ_SEARCH").migratePostgresTextClobToBinaryClob("20211003.3", "SEARCH_QUERY_STRING");
	}

	private void init540() {

		Builder version = forVersion(VersionEnum.V5_4_0);

		// -- add index on HFJ_SPIDX_DATE
		version.onTable("HFJ_SPIDX_DATE")
				.addIndex("20210309.1", "IDX_SP_DATE_HASH_HIGH")
				.unique(false)
				.withColumns("HASH_IDENTITY", "SP_VALUE_HIGH")
				.doNothing();

		// -- add index on HFJ_FORCED_ID
		version.onTable("HFJ_FORCED_ID")
				.addIndex("20210309.2", "IDX_FORCEID_FID")
				.unique(false)
				.withColumns("FORCED_ID");

		// -- ValueSet Concept Fulltext Indexing
		version.onTable("TRM_VALUESET_CONCEPT")
				.addColumn("20210406.1", "INDEX_STATUS")
				.nullable()
				.type(ColumnTypeEnum.LONG);
		version.onTable("TRM_VALUESET_CONCEPT")
				.addColumn("20210406.2", "SOURCE_DIRECT_PARENT_PIDS")
				.nullable()
				.type(ColumnTypeEnum.CLOB);
		version.onTable("TRM_VALUESET_CONCEPT")
				.addColumn("20210406.3", "SOURCE_PID")
				.nullable()
				.type(ColumnTypeEnum.LONG);

		// Bulk Import Job
		Builder.BuilderAddTableByColumns blkImportJobTable =
				version.addTableByColumns("20210410.1", "HFJ_BLK_IMPORT_JOB", "PID");
		blkImportJobTable.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		blkImportJobTable.addColumn("JOB_ID").nonNullable().type(ColumnTypeEnum.STRING, UUID_LENGTH);
		blkImportJobTable.addColumn("JOB_STATUS").nonNullable().type(ColumnTypeEnum.STRING, 10);
		blkImportJobTable.addColumn("STATUS_TIME").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		blkImportJobTable.addColumn("STATUS_MESSAGE").nullable().type(ColumnTypeEnum.STRING, 500);
		blkImportJobTable.addColumn("JOB_DESC").nullable().type(ColumnTypeEnum.STRING, 500);
		blkImportJobTable.addColumn("OPTLOCK").nonNullable().type(ColumnTypeEnum.INT);
		blkImportJobTable.addColumn("FILE_COUNT").nonNullable().type(ColumnTypeEnum.INT);
		blkImportJobTable.addColumn("ROW_PROCESSING_MODE").nonNullable().type(ColumnTypeEnum.STRING, 20);
		blkImportJobTable.addColumn("BATCH_SIZE").nonNullable().type(ColumnTypeEnum.INT);
		blkImportJobTable
				.addIndex("20210410.2", "IDX_BLKIM_JOB_ID")
				.unique(true)
				.withColumns("JOB_ID");
		version.addIdGenerator("20210410.3", "SEQ_BLKIMJOB_PID");

		// Bulk Import Job File
		Builder.BuilderAddTableByColumns blkImportJobFileTable =
				version.addTableByColumns("20210410.4", "HFJ_BLK_IMPORT_JOBFILE", "PID");
		blkImportJobFileTable.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		blkImportJobFileTable.addColumn("JOB_PID").nonNullable().type(ColumnTypeEnum.LONG);
		blkImportJobFileTable.addColumn("JOB_CONTENTS").nonNullable().type(ColumnTypeEnum.BLOB);
		blkImportJobFileTable.addColumn("FILE_SEQ").nonNullable().type(ColumnTypeEnum.INT);
		blkImportJobFileTable.addColumn("TENANT_NAME").nullable().type(ColumnTypeEnum.STRING, 200);
		blkImportJobFileTable
				.addIndex("20210410.5", "IDX_BLKIM_JOBFILE_JOBID")
				.unique(false)
				.withColumns("JOB_PID");
		blkImportJobFileTable
				.addForeignKey("20210410.6", "FK_BLKIMJOBFILE_JOB")
				.toColumn("JOB_PID")
				.references("HFJ_BLK_IMPORT_JOB", "PID");
		version.addIdGenerator("20210410.7", "SEQ_BLKIMJOBFILE_PID");

		// Increase ResourceLink path length
		version.onTable("HFJ_RES_LINK")
				.modifyColumn("20210505.1", "SRC_PATH")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 500)
				.failureAllowed();
	}

	private void init530() {
		Builder version = forVersion(VersionEnum.V5_3_0);

		// -- TRM
		version.onTable("TRM_VALUESET_CONCEPT").dropIndex("20210104.1", "IDX_VS_CONCEPT_CS_CODE");

		version.onTable("TRM_VALUESET_CONCEPT")
				.addIndex("20210104.2", "IDX_VS_CONCEPT_CSCD")
				.unique(true)
				.withColumns("VALUESET_PID", "SYSTEM_URL", "CODEVAL");

		// -- Add new Table, HFJ_SPIDX_QUANTITY_NRML
		version.addIdGenerator("20210109.1", "SEQ_SPIDX_QUANTITY_NRML");
		Builder.BuilderAddTableByColumns pkg =
				version.addTableByColumns("20210109.2", "HFJ_SPIDX_QUANTITY_NRML", "SP_ID");
		pkg.addColumn("RES_ID").nonNullable().type(ColumnTypeEnum.LONG);
		pkg.addColumn("RES_TYPE").nonNullable().type(ColumnTypeEnum.STRING, 100);
		pkg.addColumn("SP_UPDATED").nullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		pkg.addColumn("SP_MISSING").nonNullable().type(ColumnTypeEnum.BOOLEAN);
		pkg.addColumn("SP_NAME").nonNullable().type(ColumnTypeEnum.STRING, 100);
		pkg.addColumn("SP_ID").nonNullable().type(ColumnTypeEnum.LONG);
		pkg.addColumn("SP_SYSTEM").nullable().type(ColumnTypeEnum.STRING, 200);
		pkg.addColumn("SP_UNITS").nullable().type(ColumnTypeEnum.STRING, 200);
		pkg.addColumn("HASH_IDENTITY_AND_UNITS").nullable().type(ColumnTypeEnum.LONG);
		pkg.addColumn("HASH_IDENTITY_SYS_UNITS").nullable().type(ColumnTypeEnum.LONG);
		pkg.addColumn("HASH_IDENTITY").nullable().type(ColumnTypeEnum.LONG);
		pkg.addColumn("SP_VALUE").nullable().type(ColumnTypeEnum.FLOAT);
		pkg.addIndex("20210109.3", "IDX_SP_QNTY_NRML_HASH").unique(false).withColumns("HASH_IDENTITY", "SP_VALUE");
		pkg.addIndex("20210109.4", "IDX_SP_QNTY_NRML_HASH_UN")
				.unique(false)
				.withColumns("HASH_IDENTITY_AND_UNITS", "SP_VALUE");
		pkg.addIndex("20210109.5", "IDX_SP_QNTY_NRML_HASH_SYSUN")
				.unique(false)
				.withColumns("HASH_IDENTITY_SYS_UNITS", "SP_VALUE");
		pkg.addIndex("20210109.6", "IDX_SP_QNTY_NRML_UPDATED").unique(false).withColumns("SP_UPDATED");
		pkg.addIndex("20210109.7", "IDX_SP_QNTY_NRML_RESID").unique(false).withColumns("RES_ID");

		// -- Link to the resourceTable
		version.onTable("HFJ_RESOURCE")
				.addColumn("20210109.10", "SP_QUANTITY_NRML_PRESENT")
				.nullable()
				.type(ColumnTypeEnum.BOOLEAN);

		// -- Fixed the partition and fk
		Builder.BuilderWithTableName nrmlTable = version.onTable("HFJ_SPIDX_QUANTITY_NRML");
		nrmlTable.addColumn("20210111.1", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		nrmlTable.addColumn("20210111.2", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		// Disabled - superceded by 20220304.33
		nrmlTable
				.addForeignKey("20210111.3", "FKRCJOVMUH5KC0O6FVBLE319PYV")
				.toColumn("RES_ID")
				.references("HFJ_RESOURCE", "RES_ID")
				.doNothing();

		Builder.BuilderWithTableName quantityTable = version.onTable("HFJ_SPIDX_QUANTITY");
		quantityTable
				.modifyColumn("20210116.1", "SP_VALUE")
				.nullable()
				.withType(ColumnTypeEnum.DOUBLE)
				.failureAllowed();

		// HFJ_RES_LINK
		version.onTable("HFJ_RES_LINK")
				.addColumn("20210126.1", "TARGET_RESOURCE_VERSION")
				.nullable()
				.type(ColumnTypeEnum.LONG);
	}

	protected void init520() {
		Builder version = forVersion(VersionEnum.V5_2_0);

		Builder.BuilderWithTableName mdmLink = version.onTable("MPI_LINK");
		mdmLink.addColumn("20201029.1", "GOLDEN_RESOURCE_PID").nonNullable().type(ColumnTypeEnum.LONG);
		mdmLink.addColumn("20201029.2", "RULE_COUNT").nullable().type(ColumnTypeEnum.LONG);
		mdmLink.addForeignKey("20201029.3", "FK_EMPI_LINK_GOLDEN_RESOURCE")
				.toColumn("GOLDEN_RESOURCE_PID")
				.references("HFJ_RESOURCE", "RES_ID");
	}

	protected void init510() {
		Builder version = forVersion(VersionEnum.V5_1_0);

		// NPM Packages
		version.addIdGenerator("20200610.1", "SEQ_NPM_PACK");
		Builder.BuilderAddTableByColumns pkg = version.addTableByColumns("20200610.2", "NPM_PACKAGE", "PID");
		pkg.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		pkg.addColumn("PACKAGE_ID").nonNullable().type(ColumnTypeEnum.STRING, 200);
		pkg.addColumn("CUR_VERSION_ID").nullable().type(ColumnTypeEnum.STRING, 200);
		pkg.addColumn("UPDATED_TIME").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		pkg.addColumn("PACKAGE_DESC").nullable().type(ColumnTypeEnum.STRING, 200);
		pkg.addIndex("20200610.3", "IDX_PACK_ID").unique(true).withColumns("PACKAGE_ID");

		version.addIdGenerator("20200610.4", "SEQ_NPM_PACKVER");
		Builder.BuilderAddTableByColumns pkgVer = version.addTableByColumns("20200610.5", "NPM_PACKAGE_VER", "PID");
		pkgVer.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		pkgVer.addColumn("PACKAGE_ID").nonNullable().type(ColumnTypeEnum.STRING, 200);
		pkgVer.addColumn("VERSION_ID").nonNullable().type(ColumnTypeEnum.STRING, 200);
		pkgVer.addColumn("PACKAGE_PID").nonNullable().type(ColumnTypeEnum.LONG);
		pkgVer.addColumn("BINARY_RES_ID").nonNullable().type(ColumnTypeEnum.LONG);
		pkgVer.addColumn("SAVED_TIME").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		pkgVer.addColumn("PKG_DESC").nonNullable().type(ColumnTypeEnum.STRING, 200);
		pkgVer.addColumn("DESC_UPPER").nonNullable().type(ColumnTypeEnum.STRING, 200);
		pkgVer.addColumn("CURRENT_VERSION").nonNullable().type(ColumnTypeEnum.BOOLEAN);
		pkgVer.addColumn("FHIR_VERSION_ID").nonNullable().type(ColumnTypeEnum.STRING, 10);
		pkgVer.addColumn("FHIR_VERSION").nonNullable().type(ColumnTypeEnum.STRING, 10);
		pkgVer.addColumn("PACKAGE_SIZE_BYTES").nonNullable().type(ColumnTypeEnum.LONG);
		pkgVer.addColumn("UPDATED_TIME").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		pkgVer.addForeignKey("20200610.6", "FK_NPM_PKV_PKG")
				.toColumn("PACKAGE_PID")
				.references("NPM_PACKAGE", "PID");
		pkgVer.addForeignKey("20200610.7", "FK_NPM_PKV_RESID")
				.toColumn("BINARY_RES_ID")
				.references("HFJ_RESOURCE", "RES_ID");
		pkgVer.addIndex("20200610.8", "IDX_PACKVER").unique(true).withColumns("PACKAGE_ID", "VERSION_ID");

		version.addIdGenerator("20200610.9", "SEQ_NPM_PACKVERRES");
		Builder.BuilderAddTableByColumns pkgVerResAdd =
				version.addTableByColumns("20200610.10", "NPM_PACKAGE_VER_RES", "PID");
		pkgVerResAdd.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		pkgVerResAdd.addColumn("PACKVER_PID").nonNullable().type(ColumnTypeEnum.LONG);
		pkgVerResAdd.addColumn("BINARY_RES_ID").nonNullable().type(ColumnTypeEnum.LONG);
		pkgVerResAdd.addColumn("FILE_DIR").nullable().type(ColumnTypeEnum.STRING, 200);
		pkgVerResAdd.addColumn("FILE_NAME").nullable().type(ColumnTypeEnum.STRING, 200);
		pkgVerResAdd.addColumn("RES_TYPE").nonNullable().type(ColumnTypeEnum.STRING, 40);
		pkgVerResAdd.addColumn("CANONICAL_URL").nullable().type(ColumnTypeEnum.STRING, 200);
		pkgVerResAdd.addColumn("CANONICAL_VERSION").nullable().type(ColumnTypeEnum.STRING, 200);
		pkgVerResAdd.addColumn("FHIR_VERSION_ID").nonNullable().type(ColumnTypeEnum.STRING, 10);
		pkgVerResAdd.addColumn("FHIR_VERSION").nonNullable().type(ColumnTypeEnum.STRING, 10);
		pkgVerResAdd.addColumn("RES_SIZE_BYTES").nonNullable().type(ColumnTypeEnum.LONG);
		pkgVerResAdd.addColumn("UPDATED_TIME").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		pkgVerResAdd
				.addForeignKey("20200610.11", "FK_NPM_PACKVERRES_PACKVER")
				.toColumn("PACKVER_PID")
				.references("NPM_PACKAGE_VER", "PID");
		pkgVerResAdd
				.addForeignKey("20200610.12", "FK_NPM_PKVR_RESID")
				.toColumn("BINARY_RES_ID")
				.references("HFJ_RESOURCE", "RES_ID");
		pkgVerResAdd.addIndex("20200610.13", "IDX_PACKVERRES_URL").unique(false).withColumns("CANONICAL_URL");

		init510_20200610();

		Builder.BuilderWithTableName pkgVerMod = version.onTable("NPM_PACKAGE_VER");
		pkgVerMod.modifyColumn("20200629.1", "PKG_DESC").nullable().withType(ColumnTypeEnum.STRING, 200);
		pkgVerMod.modifyColumn("20200629.2", "DESC_UPPER").nullable().withType(ColumnTypeEnum.STRING, 200);

		init510_20200706_to_20200714();

		Builder.BuilderWithTableName empiLink = version.onTable("MPI_LINK");
		empiLink.addColumn("20200715.1", "VERSION").nonNullable().type(ColumnTypeEnum.STRING, 16);
		empiLink.addColumn("20200715.2", "EID_MATCH").nullable().type(ColumnTypeEnum.BOOLEAN);
		empiLink.addColumn("20200715.3", "NEW_PERSON").nullable().type(ColumnTypeEnum.BOOLEAN);
		empiLink.addColumn("20200715.4", "VECTOR").nullable().type(ColumnTypeEnum.LONG);
		empiLink.addColumn("20200715.5", "SCORE").nullable().type(ColumnTypeEnum.FLOAT);

		init510_20200725();

		// EMPI Target Type
		empiLink.addColumn("20200727.1", "TARGET_TYPE").nullable().type(ColumnTypeEnum.STRING, 40);

		// ConceptMap add version for search
		Builder.BuilderWithTableName trmConceptMap = version.onTable("TRM_CONCEPT_MAP");
		trmConceptMap.addColumn("20200910.1", "VER").nullable().type(ColumnTypeEnum.STRING, 200);
		trmConceptMap.dropIndex("20200910.2", "IDX_CONCEPT_MAP_URL").failureAllowed();
		trmConceptMap.addIndex("20200910.3", "IDX_CONCEPT_MAP_URL").unique(true).withColumns("URL", "VER");

		// Term CodeSystem Version and Term ValueSet Version
		Builder.BuilderWithTableName trmCodeSystemVer = version.onTable("TRM_CODESYSTEM_VER");
		trmCodeSystemVer
				.addIndex("20200923.1", "IDX_CODESYSTEM_AND_VER")
				.unique(true)
				.withColumns("CODESYSTEM_PID", "CS_VERSION_ID");
		Builder.BuilderWithTableName trmValueSet = version.onTable("TRM_VALUESET");
		trmValueSet.addColumn("20200923.2", "VER").nullable().type(ColumnTypeEnum.STRING, 200);
		trmValueSet.dropIndex("20200923.3", "IDX_VALUESET_URL").failureAllowed();
		trmValueSet.addIndex("20200923.4", "IDX_VALUESET_URL").unique(true).withColumns("URL", "VER");

		// Term ValueSet Component add system version
		Builder.BuilderWithTableName trmValueSetComp = version.onTable("TRM_VALUESET_CONCEPT");
		trmValueSetComp.addColumn("20201028.1", "SYSTEM_VER").nullable().type(ColumnTypeEnum.STRING, 200);
		trmValueSetComp.dropIndex("20201028.2", "IDX_VS_CONCEPT_CS_CD").failureAllowed();
		trmValueSetComp
				.addIndex("20201028.3", "IDX_VS_CONCEPT_CS_CODE")
				.unique(true)
				.withColumns("VALUESET_PID", "SYSTEM_URL", "SYSTEM_VER", "CODEVAL")
				.doNothing();
	}

	protected void init510_20200725() {
		// nothing
	}

	protected void init510_20200610() {
		// nothing
	}

	protected void init510_20200706_to_20200714() {
		// nothing
	}

	private void init501() { // 20200514 - present
		Builder version = forVersion(VersionEnum.V5_0_1);

		Builder.BuilderWithTableName spidxDate = version.onTable("HFJ_SPIDX_DATE");
		spidxDate
				.addIndex("20200514.1", "IDX_SP_DATE_HASH_LOW")
				.unique(false)
				.withColumns("HASH_IDENTITY", "SP_VALUE_LOW")
				.doNothing();
		spidxDate
				.addIndex("20200514.2", "IDX_SP_DATE_ORD_HASH")
				.unique(false)
				.withColumns("HASH_IDENTITY", "SP_VALUE_LOW_DATE_ORDINAL", "SP_VALUE_HIGH_DATE_ORDINAL")
				.doNothing();
		spidxDate
				.addIndex("20200514.3", "IDX_SP_DATE_ORD_HASH_LOW")
				.unique(false)
				.withColumns("HASH_IDENTITY", "SP_VALUE_LOW_DATE_ORDINAL")
				.doNothing();

		// MPI_LINK
		version.addIdGenerator("20200517.1", "SEQ_EMPI_LINK_ID");
		Builder.BuilderAddTableByColumns empiLink = version.addTableByColumns("20200517.2", "MPI_LINK", "PID");
		empiLink.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);

		empiLink.addColumn("PERSON_PID").nonNullable().type(ColumnTypeEnum.LONG);
		empiLink.addForeignKey("20200517.3", "FK_EMPI_LINK_PERSON")
				.toColumn("PERSON_PID")
				.references("HFJ_RESOURCE", "RES_ID");

		empiLink.addColumn("TARGET_PID").nonNullable().type(ColumnTypeEnum.LONG);
		empiLink.addForeignKey("20200517.4", "FK_EMPI_LINK_TARGET")
				.toColumn("TARGET_PID")
				.references("HFJ_RESOURCE", "RES_ID");

		empiLink.addColumn("MATCH_RESULT").nonNullable().type(ColumnTypeEnum.INT);
		empiLink.addColumn("LINK_SOURCE").nonNullable().type(ColumnTypeEnum.INT);
		empiLink.addColumn("CREATED").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		empiLink.addColumn("UPDATED").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);

		empiLink.addIndex("20200517.5", "IDX_EMPI_PERSON_TGT").unique(true).withColumns("PERSON_PID", "TARGET_PID");
	}

	protected void init500() { // 20200218 - 20200519
		Builder version = forVersion(VersionEnum.V5_0_0);

		// Eliminate circular dependency.
		version.onTable("HFJ_RESOURCE").dropColumn("20200218.1", "FORCED_ID_PID");
		version.onTable("HFJ_RES_VER").dropColumn("20200218.2", "FORCED_ID_PID");
		version.onTable("HFJ_RES_VER")
				.addForeignKey("20200218.3", "FK_RESOURCE_HISTORY_RESOURCE")
				.toColumn("RES_ID")
				.references("HFJ_RESOURCE", "RES_ID");
		version.onTable("HFJ_RES_VER")
				.modifyColumn("20200220.1", "RES_ID")
				.nonNullable()
				.withType(ColumnTypeEnum.LONG)
				.failureAllowed();
		//

		// Drop unused column
		version.onTable("HFJ_RESOURCE").dropIndex("20200419.1", "IDX_RES_PROFILE");
		version.onTable("HFJ_RESOURCE").dropColumn("20200419.2", "RES_PROFILE").failureAllowed();

		// Add Partitioning
		Builder.BuilderAddTableByColumns partition =
				version.addTableByColumns("20200420.0", "HFJ_PARTITION", "PART_ID");
		partition.addColumn("PART_ID").nonNullable().type(ColumnTypeEnum.INT);
		partition.addColumn("PART_NAME").nonNullable().type(ColumnTypeEnum.STRING, 200);
		partition.addColumn("PART_DESC").nullable().type(ColumnTypeEnum.STRING, 200);
		partition.addIndex("20200420.1", "IDX_PART_NAME").unique(true).withColumns("PART_NAME");

		// Partition columns on individual tables
		version.onTable("HFJ_RESOURCE")
				.addColumn("20200420.2", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_RESOURCE")
				.addColumn("20200420.3", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_RES_VER")
				.addColumn("20200420.4", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_RES_VER")
				.addColumn("20200420.5", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_IDX_CMP_STRING_UNIQ")
				.addColumn("20200420.6", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_IDX_CMP_STRING_UNIQ")
				.addColumn("20200420.7", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_IDX_CMP_STRING_UNIQ")
				.addColumn("20200420.8", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_IDX_CMP_STRING_UNIQ")
				.addColumn("20200420.9", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_HISTORY_TAG")
				.addColumn("20200420.10", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_HISTORY_TAG")
				.addColumn("20200420.11", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_RES_TAG")
				.addColumn("20200420.12", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_RES_TAG")
				.addColumn("20200420.13", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_FORCED_ID")
				.addColumn("20200420.14", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_FORCED_ID")
				.addColumn("20200420.15", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_RES_LINK")
				.addColumn("20200420.16", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_RES_LINK")
				.addColumn("20200420.17", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_SPIDX_STRING")
				.addColumn("20200420.18", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_SPIDX_STRING")
				.addColumn("20200420.19", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_SPIDX_COORDS")
				.addColumn("20200420.20", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_SPIDX_COORDS")
				.addColumn("20200420.21", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_SPIDX_NUMBER")
				.addColumn("20200420.22", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_SPIDX_NUMBER")
				.addColumn("20200420.23", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_SPIDX_TOKEN")
				.addColumn("20200420.24", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_SPIDX_TOKEN")
				.addColumn("20200420.25", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_SPIDX_DATE")
				.addColumn("20200420.26", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_SPIDX_DATE")
				.addColumn("20200420.27", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_SPIDX_URI")
				.addColumn("20200420.28", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_SPIDX_URI")
				.addColumn("20200420.29", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_SPIDX_QUANTITY")
				.addColumn("20200420.30", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_SPIDX_QUANTITY")
				.addColumn("20200420.31", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_RES_VER_PROV")
				.addColumn("20200420.32", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_RES_VER_PROV")
				.addColumn("20200420.33", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_RES_PARAM_PRESENT")
				.addColumn("20200420.34", "PARTITION_ID")
				.nullable()
				.type(ColumnTypeEnum.INT);
		version.onTable("HFJ_RES_PARAM_PRESENT")
				.addColumn("20200420.35", "PARTITION_DATE")
				.nullable()
				.type(ColumnTypeEnum.DATE_ONLY);

		version.onTable("HFJ_SPIDX_STRING")
				.modifyColumn("20200420.36", "SP_MISSING")
				.nonNullable()
				.withType(ColumnTypeEnum.BOOLEAN)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_COORDS")
				.modifyColumn("20200420.37", "SP_MISSING")
				.nonNullable()
				.withType(ColumnTypeEnum.BOOLEAN)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_NUMBER")
				.modifyColumn("20200420.38", "SP_MISSING")
				.nonNullable()
				.withType(ColumnTypeEnum.BOOLEAN)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_TOKEN")
				.modifyColumn("20200420.39", "SP_MISSING")
				.nonNullable()
				.withType(ColumnTypeEnum.BOOLEAN)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_DATE")
				.modifyColumn("20200420.40", "SP_MISSING")
				.nonNullable()
				.withType(ColumnTypeEnum.BOOLEAN)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_URI")
				.modifyColumn("20200420.41", "SP_MISSING")
				.nonNullable()
				.withType(ColumnTypeEnum.BOOLEAN)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_QUANTITY")
				.modifyColumn("20200420.42", "SP_MISSING")
				.nonNullable()
				.withType(ColumnTypeEnum.BOOLEAN)
				.failureAllowed();

		// Add support for integer comparisons during day-precision date search.
		Builder.BuilderWithTableName spidxDate = version.onTable("HFJ_SPIDX_DATE");
		spidxDate
				.addColumn("20200501.1", "SP_VALUE_LOW_DATE_ORDINAL")
				.nullable()
				.type(ColumnTypeEnum.INT);
		spidxDate
				.addColumn("20200501.2", "SP_VALUE_HIGH_DATE_ORDINAL")
				.nullable()
				.type(ColumnTypeEnum.INT);

		spidxDate.addTask(
				new CalculateOrdinalDatesTask(VersionEnum.V5_0_0, "20200501.3")
						.addCalculator(
								"SP_VALUE_LOW_DATE_ORDINAL",
								t -> ResourceIndexedSearchParamDate.calculateOrdinalValue(t.getDate("SP_VALUE_LOW")))
						.addCalculator(
								"SP_VALUE_HIGH_DATE_ORDINAL",
								t -> ResourceIndexedSearchParamDate.calculateOrdinalValue(t.getDate("SP_VALUE_HIGH")))
						.setColumnName(
								"SP_VALUE_LOW_DATE_ORDINAL") // It doesn't matter which of the two we choose as they
				// will both be null.
				);
	}

	/**
	 * Partway through the 4.3.0 releaase cycle we renumbered to
	 * 5.0.0 - We have a bunch of NOP tasks here to avoid breakage for anyone
	 * who installed a prerelease before we made the switch
	 */
	@SuppressWarnings("deprecation")
	private void init430() {
		Builder version = forVersion(VersionEnum.V4_3_0);
		version.addNop("20200218.1");
		version.addNop("20200218.2");
		version.addNop("20200218.3");
		version.addNop("20200220.1");
		version.addNop("20200419.1");
		version.addNop("20200419.2");
		version.addNop("20200420.0");
		version.addNop("20200420.1");
		version.addNop("20200420.2");
		version.addNop("20200420.3");
		version.addNop("20200420.4");
		version.addNop("20200420.5");
		version.addNop("20200420.6");
		version.addNop("20200420.7");
		version.addNop("20200420.8");
		version.addNop("20200420.9");
		version.addNop("20200420.10");
		version.addNop("20200420.11");
		version.addNop("20200420.12");
		version.addNop("20200420.13");
		version.addNop("20200420.14");
		version.addNop("20200420.15");
		version.addNop("20200420.16");
		version.addNop("20200420.17");
		version.addNop("20200420.18");
		version.addNop("20200420.19");
		version.addNop("20200420.20");
		version.addNop("20200420.21");
		version.addNop("20200420.22");
		version.addNop("20200420.23");
		version.addNop("20200420.24");
		version.addNop("20200420.25");
		version.addNop("20200420.26");
		version.addNop("20200420.27");
		version.addNop("20200420.28");
		version.addNop("20200420.29");
		version.addNop("20200420.30");
		version.addNop("20200420.31");
		version.addNop("20200420.32");
		version.addNop("20200420.33");
		version.addNop("20200420.34");
		version.addNop("20200420.35");
		version.addNop("20200420.36");
		version.addNop("20200420.37");
		version.addNop("20200420.38");
		version.addNop("20200420.39");
		version.addNop("20200420.40");
		version.addNop("20200420.41");
		version.addNop("20200420.42");
	}

	protected void init420() { // 20191015 - 20200217
		Builder version = forVersion(VersionEnum.V4_2_0);

		// TermValueSetConceptDesignation
		version.onTable("TRM_VALUESET_C_DESIGNATION")
				.dropIndex("20200202.1", "IDX_VALUESET_C_DSGNTN_VAL")
				.failureAllowed();
		Builder.BuilderWithTableName searchTable = version.onTable("HFJ_SEARCH");
		searchTable.dropIndex("20200203.1", "IDX_SEARCH_LASTRETURNED");
		searchTable.dropColumn("20200203.2", "SEARCH_LAST_RETURNED");
		searchTable.addIndex("20200203.3", "IDX_SEARCH_CREATED").unique(false).withColumns("CREATED");
	}

	protected void init410() { // 20190815 - 20191014
		Builder version = forVersion(VersionEnum.V4_1_0);

		/*
		 * Note: The following tasks are markes as failure allowed - This is because all we're
		 * doing is setting a not-null on a column that will never be null anyway. Setting not null
		 * fails on SQL Server because there is an index on this column... Which is dumb, but hey.
		 */
		version.onTable("HFJ_SPIDX_NUMBER")
				.modifyColumn("20190920.1", "RES_ID")
				.nonNullable()
				.withType(ColumnTypeEnum.LONG)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_COORDS")
				.modifyColumn("20190920.2", "RES_ID")
				.nonNullable()
				.withType(ColumnTypeEnum.LONG)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_TOKEN")
				.modifyColumn("20190920.3", "RES_ID")
				.nonNullable()
				.withType(ColumnTypeEnum.LONG)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_STRING")
				.modifyColumn("20190920.4", "RES_ID")
				.nonNullable()
				.withType(ColumnTypeEnum.LONG)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_DATE")
				.modifyColumn("20190920.5", "RES_ID")
				.nonNullable()
				.withType(ColumnTypeEnum.LONG)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_QUANTITY")
				.modifyColumn("20190920.6", "RES_ID")
				.nonNullable()
				.withType(ColumnTypeEnum.LONG)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_URI")
				.modifyColumn("20190920.7", "RES_ID")
				.nonNullable()
				.withType(ColumnTypeEnum.LONG)
				.failureAllowed();

		// HFJ_SEARCH
		version.onTable("HFJ_SEARCH")
				.addColumn("20190921.1", "EXPIRY_OR_NULL")
				.nullable()
				.type(ColumnTypeEnum.DATE_TIMESTAMP);
		version.onTable("HFJ_SEARCH")
				.addColumn("20190921.2", "NUM_BLOCKED")
				.nullable()
				.type(ColumnTypeEnum.INT);

		// HFJ_BLK_EXPORT_JOB
		version.addIdGenerator("20190921.3", "SEQ_BLKEXJOB_PID");
		Builder.BuilderAddTableByColumns bulkExportJob =
				version.addTableByColumns("20190921.4", "HFJ_BLK_EXPORT_JOB", "PID");
		bulkExportJob.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		bulkExportJob.addColumn("JOB_ID").nonNullable().type(ColumnTypeEnum.STRING, 36);
		bulkExportJob.addColumn("JOB_STATUS").nonNullable().type(ColumnTypeEnum.STRING, 10);
		bulkExportJob.addColumn("CREATED_TIME").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		bulkExportJob.addColumn("STATUS_TIME").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		bulkExportJob.addColumn("EXP_TIME").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		bulkExportJob.addColumn("REQUEST").nonNullable().type(ColumnTypeEnum.STRING, 500);
		bulkExportJob.addColumn("OPTLOCK").nonNullable().type(ColumnTypeEnum.INT);
		bulkExportJob.addColumn("EXP_SINCE").nullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		bulkExportJob.addColumn("STATUS_MESSAGE").nullable().type(ColumnTypeEnum.STRING, 500);
		bulkExportJob.addIndex("20190921.5", "IDX_BLKEX_EXPTIME").unique(false).withColumns("EXP_TIME");
		bulkExportJob.addIndex("20190921.6", "IDX_BLKEX_JOB_ID").unique(true).withColumns("JOB_ID");

		// HFJ_BLK_EXPORT_COLLECTION
		version.addIdGenerator("20190921.7", "SEQ_BLKEXCOL_PID");
		Builder.BuilderAddTableByColumns bulkExportCollection =
				version.addTableByColumns("20190921.8", "HFJ_BLK_EXPORT_COLLECTION", "PID");
		bulkExportCollection.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		bulkExportCollection.addColumn("JOB_PID").nonNullable().type(ColumnTypeEnum.LONG);
		bulkExportCollection
				.addForeignKey("20190921.9", "FK_BLKEXCOL_JOB")
				.toColumn("JOB_PID")
				.references("HFJ_BLK_EXPORT_JOB", "PID");
		bulkExportCollection.addColumn("RES_TYPE").nonNullable().type(ColumnTypeEnum.STRING, 40);
		bulkExportCollection.addColumn("TYPE_FILTER").nullable().type(ColumnTypeEnum.STRING, 1000);
		bulkExportCollection.addColumn("OPTLOCK").nonNullable().type(ColumnTypeEnum.INT);

		// HFJ_BLK_EXPORT_COLFILE
		version.addIdGenerator("20190921.10", "SEQ_BLKEXCOLFILE_PID");
		Builder.BuilderAddTableByColumns bulkExportCollectionFile =
				version.addTableByColumns("20190921.11", "HFJ_BLK_EXPORT_COLFILE", "PID");
		bulkExportCollectionFile.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		bulkExportCollectionFile.addColumn("COLLECTION_PID").nonNullable().type(ColumnTypeEnum.LONG);
		bulkExportCollectionFile.addColumn("RES_ID").nonNullable().type(ColumnTypeEnum.STRING, 100);
		bulkExportCollectionFile
				.addForeignKey("20190921.12", "FK_BLKEXCOLFILE_COLLECT")
				.toColumn("COLLECTION_PID")
				.references("HFJ_BLK_EXPORT_COLLECTION", "PID");

		// HFJ_RES_VER_PROV
		version.startSectionWithMessage("Processing bulkExportCollectionFile: HFJ_RES_VER_PROV");
		Builder.BuilderAddTableByColumns resVerProv =
				version.addTableByColumns("20190921.13", "HFJ_RES_VER_PROV", "RES_VER_PID");
		resVerProv.addColumn("RES_VER_PID").nonNullable().type(ColumnTypeEnum.LONG);
		resVerProv
				.addForeignKey("20190921.14", "FK_RESVERPROV_RESVER_PID")
				.toColumn("RES_VER_PID")
				.references("HFJ_RES_VER", "PID");
		resVerProv.addColumn("RES_PID").nonNullable().type(ColumnTypeEnum.LONG);
		resVerProv
				.addForeignKey("20190921.15", "FK_RESVERPROV_RES_PID")
				.toColumn("RES_PID")
				.references("HFJ_RESOURCE", "RES_ID")
				.doNothing(); // Added below in a better form
		resVerProv.addColumn("SOURCE_URI").nullable().type(ColumnTypeEnum.STRING, 100);
		resVerProv.addColumn("REQUEST_ID").nullable().type(ColumnTypeEnum.STRING, 16);
		resVerProv
				.addIndex("20190921.16", "IDX_RESVERPROV_SOURCEURI")
				.unique(false)
				.withColumns("SOURCE_URI");
		resVerProv
				.addIndex("20190921.17", "IDX_RESVERPROV_REQUESTID")
				.unique(false)
				.withColumns("REQUEST_ID");

		// TermValueSetConceptDesignation
		version.startSectionWithMessage("Processing bulkExportCollectionFile: TRM_VALUESET_C_DESIGNATION");
		Builder.BuilderWithTableName termValueSetConceptDesignationTable =
				version.onTable("TRM_VALUESET_C_DESIGNATION");
		termValueSetConceptDesignationTable
				.addColumn("20190921.18", "VALUESET_PID")
				.nonNullable()
				.type(ColumnTypeEnum.LONG);
		termValueSetConceptDesignationTable
				.addForeignKey("20190921.19", "FK_TRM_VSCD_VS_PID")
				.toColumn("VALUESET_PID")
				.references("TRM_VALUESET", "PID");

		// Drop HFJ_SEARCH_RESULT foreign keys
		version.onTable("HFJ_SEARCH_RESULT").dropForeignKey("20190921.20", "FK_SEARCHRES_RES", "HFJ_RESOURCE");
		version.onTable("HFJ_SEARCH_RESULT").dropForeignKey("20190921.21", "FK_SEARCHRES_SEARCH", "HFJ_SEARCH");

		// TermValueSet
		version.startSectionWithMessage("Processing bulkExportCollectionFile: TRM_VALUESET");
		Builder.BuilderWithTableName termValueSetTable = version.onTable("TRM_VALUESET");
		termValueSetTable
				.addColumn("20190921.22", "TOTAL_CONCEPTS")
				.nonNullable()
				.type(ColumnTypeEnum.LONG);
		termValueSetTable
				.addColumn("20190921.23", "TOTAL_CONCEPT_DESIGNATIONS")
				.nonNullable()
				.type(ColumnTypeEnum.LONG);
		termValueSetTable.dropIndex("20190921.24", "IDX_VALUESET_EXP_STATUS");

		version.dropIdGenerator("20190921.25", "SEQ_SEARCHPARM_ID");

		// TermValueSetConcept
		version.startSectionWithMessage("Processing bulkExportCollectionFile: TRM_VALUESET_CONCEPT");
		Builder.BuilderWithTableName termValueSetConceptTable = version.onTable("TRM_VALUESET_CONCEPT");
		termValueSetConceptTable
				.addColumn("20190921.26", "VALUESET_ORDER")
				.nonNullable()
				.type(ColumnTypeEnum.INT);
		termValueSetConceptTable
				.addIndex("20190921.27", "IDX_VS_CONCEPT_ORDER")
				.unique(true)
				.withColumns("VALUESET_PID", "VALUESET_ORDER");

		// Account for RESTYPE_LEN column increasing from 30 to 40
		version.onTable("HFJ_RESOURCE")
				.modifyColumn("20191002.1", "RES_TYPE")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 40)
				.failureAllowed();
		version.onTable("HFJ_RES_VER")
				.modifyColumn("20191002.2", "RES_TYPE")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 40)
				.failureAllowed();
		version.onTable("HFJ_HISTORY_TAG")
				.modifyColumn("20191002.3", "RES_TYPE")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 40)
				.failureAllowed();
		version.onTable("HFJ_RES_LINK")
				.modifyColumn("20191002.4", "SOURCE_RESOURCE_TYPE")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 40)
				.failureAllowed();
		version.onTable("HFJ_RES_LINK")
				.modifyColumn("20191002.5", "TARGET_RESOURCE_TYPE")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 40)
				.failureAllowed();
		version.onTable("HFJ_RES_TAG")
				.modifyColumn("20191002.6", "RES_TYPE")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 40)
				.failureAllowed();

		// TermConceptDesignation
		version.startSectionWithMessage("Processing table: TRM_CONCEPT_DESIG");
		version.onTable("TRM_CONCEPT_DESIG")
				.modifyColumn("20191002.7", "VAL")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 2000);

		// TermValueSetConceptDesignation
		version.startSectionWithMessage("Processing table: TRM_VALUESET_C_DESIGNATION");
		version.onTable("TRM_VALUESET_C_DESIGNATION")
				.modifyColumn("20191002.8", "VAL")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 2000);

		// TermConceptProperty
		version.startSectionWithMessage("Processing table: TRM_CONCEPT_PROPERTY");
		version.onTable("TRM_CONCEPT_PROPERTY")
				.addColumn("20191002.9", "PROP_VAL_LOB")
				.nullable()
				.type(ColumnTypeEnum.BLOB);
	}

	protected void init400() { // 20190401 - 20190814
		Builder version = forVersion(VersionEnum.V4_0_0);

		// BinaryStorageEntity
		Builder.BuilderAddTableByColumns binaryBlob =
				version.addTableByColumns("20190722.1", "HFJ_BINARY_STORAGE_BLOB", "BLOB_ID");
		binaryBlob.addColumn("BLOB_ID").nonNullable().type(ColumnTypeEnum.STRING, 200);
		binaryBlob.addColumn("RESOURCE_ID").nonNullable().type(ColumnTypeEnum.STRING, 100);
		binaryBlob.addColumn("BLOB_SIZE").nullable().type(ColumnTypeEnum.INT);
		binaryBlob.addColumn("CONTENT_TYPE").nonNullable().type(ColumnTypeEnum.STRING, 100);
		binaryBlob.addColumn("BLOB_DATA").nonNullable().type(ColumnTypeEnum.BLOB);
		binaryBlob.addColumn("PUBLISHED_DATE").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		binaryBlob.addColumn("BLOB_HASH").nullable().type(ColumnTypeEnum.STRING, 128);

		// Interim builds used this name
		version.onTable("TRM_VALUESET_CODE").dropThisTable("20190722.2");

		version.onTable("TRM_CONCEPT_MAP_GROUP")
				.renameColumn("20190722.3", "myConceptMapUrl", "CONCEPT_MAP_URL", false, true)
				.renameColumn("20190722.4", "mySourceValueSet", "SOURCE_VS", false, true)
				.renameColumn("20190722.5", "myTargetValueSet", "TARGET_VS", false, true);
		version.onTable("TRM_CONCEPT_MAP_GROUP")
				.modifyColumn("20190722.6", "CONCEPT_MAP_URL")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GROUP")
				.modifyColumn("20190722.7", "SOURCE_VERSION")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GROUP")
				.modifyColumn("20190722.8", "SOURCE_VS")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GROUP")
				.modifyColumn("20190722.9", "TARGET_VERSION")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GROUP")
				.modifyColumn("20190722.10", "TARGET_VS")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 200);

		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
				.renameColumn("20190722.11", "myConceptMapUrl", "CONCEPT_MAP_URL", false, true)
				.renameColumn("20190722.12", "mySystem", "SYSTEM_URL", false, true)
				.renameColumn("20190722.13", "mySystemVersion", "SYSTEM_VERSION", false, true)
				.renameColumn("20190722.14", "myValueSet", "VALUESET_URL", false, true);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
				.modifyColumn("20190722.15", "CONCEPT_MAP_URL")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
				.modifyColumn("20190722.16", "SOURCE_CODE")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 500);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
				.modifyColumn("20190722.17", "SYSTEM_URL")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
				.modifyColumn("20190722.18", "SYSTEM_VERSION")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
				.modifyColumn("20190722.19", "VALUESET_URL")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 200);

		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
				.renameColumn("20190722.20", "myConceptMapUrl", "CONCEPT_MAP_URL", false, true)
				.renameColumn("20190722.21", "mySystem", "SYSTEM_URL", false, true)
				.renameColumn("20190722.22", "mySystemVersion", "SYSTEM_VERSION", false, true)
				.renameColumn("20190722.23", "myValueSet", "VALUESET_URL", false, true);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
				.modifyColumn("20190722.24", "CONCEPT_MAP_URL")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
				.modifyColumn("20190722.25", "SYSTEM_URL")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
				.modifyColumn("20190722.26", "SYSTEM_VERSION")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 200);

		/*
		DISABLED THIS STEP (20190722.27) ON PURPOSE BECAUSE IT STARTED CAUSING FAILURES ON MSSQL FOR A FRESH DB.
		I left it here for historical purposes.
		The reason for the failure is as follows. The TARGET_CODE column was originally 'not nullable' when it was
		first introduced. And in 7_2_0, it is being changed to a nullable column (see 20240327.1 in init720()).
		Starting with 7_2_0, on a fresh db, we create the table with nullable TARGET_CODE (as it is made nullable now).
		Since we run all migration steps on fresh db, this step will try to convert the column which is created as nullable
		to not nullable (which will then need to be coverted back to nullable in 7_2_0 migration).
		Changing a nullable column to not nullable is not allowed in
		MSSQL if there is an index on the column, which is the case here, as there is IDX_CNCPT_MP_GRP_ELM_TGT_CD
		on this column. Since init720() has the right migration
		step, where the column is set to nullable and has the right type and length, this statement is also
		not necessary anymore even for not fresh dbs.

		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
				.modifyColumn("20190722.27", "TARGET_CODE")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 500);
		*/
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
				.modifyColumn("20190722.28", "VALUESET_URL")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 200);

		version.onTable("TRM_CONCEPT").renameColumn("20190722.29", "CODE", "CODEVAL", false, true);

		// TermValueSet
		version.startSectionWithMessage("Processing table: TRM_VALUESET");
		version.addIdGenerator("20190722.30", "SEQ_VALUESET_PID");
		Builder.BuilderAddTableByColumns termValueSetTable =
				version.addTableByColumns("20190722.31", "TRM_VALUESET", "PID");
		termValueSetTable.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		termValueSetTable.addColumn("URL").nonNullable().type(ColumnTypeEnum.STRING, 200);
		termValueSetTable
				.addIndex("20190722.32", "IDX_VALUESET_URL")
				.unique(true)
				.withColumns("URL");
		termValueSetTable.addColumn("RES_ID").nonNullable().type(ColumnTypeEnum.LONG);
		termValueSetTable
				.addForeignKey("20190722.33", "FK_TRMVALUESET_RES")
				.toColumn("RES_ID")
				.references("HFJ_RESOURCE", "RES_ID");
		termValueSetTable.addColumn("NAME").nullable().type(ColumnTypeEnum.STRING, 200);

		version.onTable("TRM_VALUESET").renameColumn("20190722.34", "NAME", "VSNAME", true, true);
		version.onTable("TRM_VALUESET")
				.modifyColumn("20190722.35", "RES_ID")
				.nullable()
				.withType(ColumnTypeEnum.LONG);

		Builder.BuilderWithTableName termValueSetTableChange = version.onTable("TRM_VALUESET");
		termValueSetTableChange
				.addColumn("20190722.36", "EXPANSION_STATUS")
				.nonNullable()
				.type(ColumnTypeEnum.STRING, 50);
		termValueSetTableChange
				.addIndex("20190722.37", "IDX_VALUESET_EXP_STATUS")
				.unique(false)
				.withColumns("EXPANSION_STATUS");

		// TermValueSetConcept
		version.startSectionWithMessage("Processing table: TRM_VALUESET_CONCEPT");
		version.addIdGenerator("20190722.38", "SEQ_VALUESET_CONCEPT_PID");
		Builder.BuilderAddTableByColumns termValueSetConceptTable =
				version.addTableByColumns("20190722.39", "TRM_VALUESET_CONCEPT", "PID");
		termValueSetConceptTable.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		termValueSetConceptTable.addColumn("VALUESET_PID").nonNullable().type(ColumnTypeEnum.LONG);
		termValueSetConceptTable
				.addForeignKey("20190722.40", "FK_TRM_VALUESET_PID")
				.toColumn("VALUESET_PID")
				.references("TRM_VALUESET", "PID");
		termValueSetConceptTable.addColumn("SYSTEM_URL").nonNullable().type(ColumnTypeEnum.STRING, 200);
		termValueSetConceptTable.addColumn("CODEVAL").nonNullable().type(ColumnTypeEnum.STRING, 500);
		termValueSetConceptTable.addColumn("DISPLAY").nullable().type(ColumnTypeEnum.STRING, 400);
		version.onTable("TRM_VALUESET_CONCEPT")
				.renameColumn("20190722.41", "CODE", "CODEVAL", true, true)
				.renameColumn("20190722.42", "SYSTEM", "SYSTEM_URL", true, true);

		version.startSectionWithMessage("Processing table: TRM_VALUESET_CONCEPT, swapping index for unique constraint");
		termValueSetConceptTable.dropIndex("20190801.1", "IDX_VALUESET_CONCEPT_CS_CD");
		// This index has been renamed in later versions. As such, allowing failure here as some DBs disallow
		// multiple indexes referencing the same set of columns.
		termValueSetConceptTable
				.addIndex("20190801.2", "IDX_VS_CONCEPT_CS_CD")
				.unique(true)
				.withColumns("VALUESET_PID", "SYSTEM_URL", "CODEVAL")
				.failureAllowed();

		// TermValueSetConceptDesignation
		version.startSectionWithMessage("Processing table: TRM_VALUESET_C_DESIGNATION");
		version.addIdGenerator("20190801.3", "SEQ_VALUESET_C_DSGNTN_PID");
		Builder.BuilderAddTableByColumns termValueSetConceptDesignationTable =
				version.addTableByColumns("20190801.4", "TRM_VALUESET_C_DESIGNATION", "PID");
		termValueSetConceptDesignationTable.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		termValueSetConceptDesignationTable
				.addColumn("VALUESET_CONCEPT_PID")
				.nonNullable()
				.type(ColumnTypeEnum.LONG);
		termValueSetConceptDesignationTable
				.addForeignKey("20190801.5", "FK_TRM_VALUESET_CONCEPT_PID")
				.toColumn("VALUESET_CONCEPT_PID")
				.references("TRM_VALUESET_CONCEPT", "PID");
		termValueSetConceptDesignationTable.addColumn("LANG").nullable().type(ColumnTypeEnum.STRING, 500);
		termValueSetConceptDesignationTable.addColumn("USE_SYSTEM").nullable().type(ColumnTypeEnum.STRING, 500);
		termValueSetConceptDesignationTable.addColumn("USE_CODE").nullable().type(ColumnTypeEnum.STRING, 500);
		termValueSetConceptDesignationTable.addColumn("USE_DISPLAY").nullable().type(ColumnTypeEnum.STRING, 500);
		termValueSetConceptDesignationTable.addColumn("VAL").nonNullable().type(ColumnTypeEnum.STRING, 500);

		// This index turned out not to be needed so it is disabled
		termValueSetConceptDesignationTable
				.addIndex("20190801.6", "IDX_VALUESET_C_DSGNTN_VAL")
				.unique(false)
				.withColumns("VAL")
				.doNothing();

		// TermCodeSystemVersion
		version.startSectionWithMessage("Processing table: TRM_CODESYSTEM_VER");
		Builder.BuilderWithTableName termCodeSystemVersionTable = version.onTable("TRM_CODESYSTEM_VER");
		termCodeSystemVersionTable
				.addColumn("20190814.1", "CS_DISPLAY")
				.nullable()
				.type(ColumnTypeEnum.STRING, 200);

		// ResourceReindexJobEntry
		version.addIdGenerator("20190814.2", "SEQ_RES_REINDEX_JOB");
		Builder.BuilderAddTableByColumns reindex =
				version.addTableByColumns("20190814.3", "HFJ_RES_REINDEX_JOB", "PID");
		reindex.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		reindex.addColumn("RES_TYPE").nullable().type(ColumnTypeEnum.STRING, 100);
		reindex.addColumn("UPDATE_THRESHOLD_HIGH").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		reindex.addColumn("JOB_DELETED").nonNullable().type(ColumnTypeEnum.BOOLEAN);
		reindex.addColumn("UPDATE_THRESHOLD_LOW").nullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		reindex.addColumn("SUSPENDED_UNTIL").nullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		reindex.addColumn("REINDEX_COUNT").nullable().type(ColumnTypeEnum.INT);

		// Search
		version.onTable("HFJ_SEARCH")
				.addColumn("20190814.4", "SEARCH_DELETED")
				.nullable()
				.type(ColumnTypeEnum.BOOLEAN);
		version.onTable("HFJ_SEARCH")
				.modifyColumn("20190814.5", "SEARCH_LAST_RETURNED")
				.nonNullable()
				.withType(ColumnTypeEnum.DATE_TIMESTAMP);
		version.onTable("HFJ_SEARCH")
				.addColumn("20190814.6", "SEARCH_PARAM_MAP")
				.nullable()
				.type(ColumnTypeEnum.BLOB);
		version.onTable("HFJ_SEARCH")
				.modifyColumn("20190814.7", "SEARCH_UUID")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, Search.SEARCH_UUID_COLUMN_LENGTH);

		version.onTable("HFJ_SEARCH_PARM").dropThisTable("20190814.8");

		// Make some columns non-nullable that were previously nullable - These are marked as failure allowed, since
		// SQL Server won't let us change nullability on columns with indexes pointing to them
		version.onTable("HFJ_SPIDX_COORDS")
				.modifyColumn("20190814.9", "RES_TYPE")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 100)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_DATE")
				.modifyColumn("20190814.10", "RES_TYPE")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 100)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_STRING")
				.modifyColumn("20190814.11", "RES_TYPE")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 100)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_STRING")
				.addColumn("20190814.12", "HASH_IDENTITY")
				.nullable()
				.type(ColumnTypeEnum.LONG);
		version.onTable("HFJ_SPIDX_STRING")
				.addIndex("20190814.13", "IDX_SP_STRING_HASH_IDENT")
				.unique(false)
				.withColumns("HASH_IDENTITY");
		version.onTable("HFJ_SPIDX_COORDS")
				.modifyColumn("20190814.14", "RES_TYPE")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 100)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_QUANTITY")
				.modifyColumn("20190814.15", "RES_TYPE")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 100)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_QUANTITY").dropColumn("20190814.16", "HASH_UNITS_AND_VALPREFIX");
		version.onTable("HFJ_SPIDX_QUANTITY").dropColumn("20190814.17", "HASH_VALPREFIX");
		version.onTable("HFJ_SPIDX_NUMBER")
				.modifyColumn("20190814.18", "RES_TYPE")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 100)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_TOKEN")
				.modifyColumn("20190814.19", "RES_TYPE")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 100)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_URI")
				.modifyColumn("20190814.20", "RES_TYPE")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 100)
				.failureAllowed();
		version.onTable("HFJ_SPIDX_URI")
				.modifyColumn("20190814.21", "SP_URI")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 254)
				.failureAllowed();
		version.onTable("TRM_CODESYSTEM")
				.modifyColumn("20190814.22", "CODE_SYSTEM_URI")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 200)
				.failureAllowed();
		version.onTable("TRM_CODESYSTEM")
				.modifyColumn("20190814.23", "CS_NAME")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 200)
				.failureAllowed();
		version.onTable("TRM_CODESYSTEM_VER")
				.modifyColumn("20190814.24", "CS_VERSION_ID")
				.nullable()
				.withType(ColumnTypeEnum.STRING, 200)
				.failureAllowed();
	}

	private void init360() { // 20180918 - 20181112
		Builder version = forVersion(VersionEnum.V3_6_0);

		// Resource Link
		Builder.BuilderWithTableName resourceLink = version.onTable("HFJ_RES_LINK");
		version.startSectionWithMessage("Starting work on table: " + resourceLink.getTableName());
		resourceLink.modifyColumn("20180929.1", "SRC_PATH").nonNullable().withType(ColumnTypeEnum.STRING, 200);

		// Search
		Builder.BuilderWithTableName search = version.onTable("HFJ_SEARCH");
		version.startSectionWithMessage("Starting work on table: " + search.getTableName());
		search.addColumn("20181001.1", "OPTLOCK_VERSION").nullable().type(ColumnTypeEnum.INT);

		version.addTableRawSql("20181104.1", "HFJ_RES_REINDEX_JOB")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"create table HFJ_RES_REINDEX_JOB (PID bigint not null, JOB_DELETED bit not null, RES_TYPE varchar(255), SUSPENDED_UNTIL datetime2, UPDATE_THRESHOLD_HIGH datetime2 not null, UPDATE_THRESHOLD_LOW datetime2, primary key (PID))")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"create table HFJ_RES_REINDEX_JOB (PID bigint not null, JOB_DELETED boolean not null, RES_TYPE varchar(255), SUSPENDED_UNTIL timestamp, UPDATE_THRESHOLD_HIGH timestamp not null, UPDATE_THRESHOLD_LOW timestamp, primary key (PID))")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"create table HFJ_RES_REINDEX_JOB (PID bigint not null, JOB_DELETED bit not null, RES_TYPE varchar(255), SUSPENDED_UNTIL datetime(6), UPDATE_THRESHOLD_HIGH datetime(6) not null, UPDATE_THRESHOLD_LOW datetime(6), primary key (PID))")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"create table HFJ_RES_REINDEX_JOB (PID int8 not null, JOB_DELETED boolean not null, RES_TYPE varchar(255), SUSPENDED_UNTIL timestamp, UPDATE_THRESHOLD_HIGH timestamp not null, UPDATE_THRESHOLD_LOW timestamp, primary key (PID))")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						" create table HFJ_RES_REINDEX_JOB (PID bigint not null, JOB_DELETED bit not null, RES_TYPE varchar(255), SUSPENDED_UNTIL datetime(6), UPDATE_THRESHOLD_HIGH datetime(6) not null, UPDATE_THRESHOLD_LOW datetime(6), primary key (PID))")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"create table HFJ_RES_REINDEX_JOB (PID number(19,0) not null, JOB_DELETED number(1,0) not null, RES_TYPE varchar2(255 char), SUSPENDED_UNTIL timestamp, UPDATE_THRESHOLD_HIGH timestamp not null, UPDATE_THRESHOLD_LOW timestamp, primary key (PID))");

		version.onTable("TRM_CONCEPT_DESIG")
				.addColumn("20181104.2", "CS_VER_PID")
				.nullable()
				.type(ColumnTypeEnum.LONG);
		version.onTable("TRM_CONCEPT_DESIG")
				.addForeignKey("20181104.3", "FK_CONCEPTDESIG_CSV")
				.toColumn("CS_VER_PID")
				.references("TRM_CODESYSTEM_VER", "PID");

		version.onTable("TRM_CONCEPT_PROPERTY")
				.addColumn("20181104.4", "CS_VER_PID")
				.nullable()
				.type(ColumnTypeEnum.LONG);
		version.onTable("TRM_CONCEPT_PROPERTY")
				.addForeignKey("20181104.5", "FK_CONCEPTPROP_CSV")
				.toColumn("CS_VER_PID")
				.references("TRM_CODESYSTEM_VER", "PID");

		version.onTable("TRM_CONCEPT")
				.addColumn("20181104.6", "PARENT_PIDS")
				.nullable()
				.type(ColumnTypeEnum.CLOB);
	}

	private void init350() { // 20180601 - 20180917
		Builder version = forVersion(VersionEnum.V3_5_0);

		// Forced ID changes
		Builder.BuilderWithTableName forcedId = version.onTable("HFJ_FORCED_ID");
		version.startSectionWithMessage("Starting work on table: " + forcedId.getTableName());

		forcedId.dropIndex("20180827.1", "IDX_FORCEDID_TYPE_FORCEDID");
		forcedId.dropIndex("20180827.2", "IDX_FORCEDID_TYPE_RESID");

		forcedId.addIndex("20180827.3", "IDX_FORCEDID_TYPE_FID").unique(true).withColumns("RESOURCE_TYPE", "FORCED_ID");

		// Indexes - Coords
		Builder.BuilderWithTableName spidxCoords = version.onTable("HFJ_SPIDX_COORDS");
		version.startSectionWithMessage("Starting work on table: " + spidxCoords.getTableName());
		spidxCoords.addColumn("20180903.1", "HASH_IDENTITY").nullable().type(ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxCoords.dropIndex("20180903.2", "IDX_SP_COORDS");
			spidxCoords
					.addIndex("20180903.4", "IDX_SP_COORDS_HASH")
					.unique(false)
					.withColumns("HASH_IDENTITY", "SP_LATITUDE", "SP_LONGITUDE");
			spidxCoords.addTask(new CalculateHashesTask(VersionEnum.V3_5_0, "20180903.5")
					.addCalculator(
							"HASH_IDENTITY",
							t -> BaseResourceIndexedSearchParam.calculateHashIdentity(
									new PartitionSettings(),
									RequestPartitionId.defaultPartition(),
									t.getResourceType(),
									t.getString("SP_NAME")))
					.setColumnName("HASH_IDENTITY"));
		}

		// Indexes - Date
		Builder.BuilderWithTableName spidxDate = version.onTable("HFJ_SPIDX_DATE");
		version.startSectionWithMessage("Starting work on table: " + spidxDate.getTableName());
		spidxDate.addColumn("20180903.6", "HASH_IDENTITY").nullable().type(ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxDate.dropIndex("20180903.7", "IDX_SP_TOKEN");
			spidxDate
					.addIndex("20180903.8", "IDX_SP_DATE_HASH")
					.unique(false)
					.withColumns("HASH_IDENTITY", "SP_VALUE_LOW", "SP_VALUE_HIGH")
					.doNothing();
			spidxDate.dropIndex("20180903.9", "IDX_SP_DATE");
			spidxDate.addTask(new CalculateHashesTask(VersionEnum.V3_5_0, "20180903.10")
					.addCalculator(
							"HASH_IDENTITY",
							t -> BaseResourceIndexedSearchParam.calculateHashIdentity(
									new PartitionSettings(),
									RequestPartitionId.defaultPartition(),
									t.getResourceType(),
									t.getString("SP_NAME")))
					.setColumnName("HASH_IDENTITY"));
		}

		// Indexes - Number
		Builder.BuilderWithTableName spidxNumber = version.onTable("HFJ_SPIDX_NUMBER");
		version.startSectionWithMessage("Starting work on table: " + spidxNumber.getTableName());
		spidxNumber.addColumn("20180903.11", "HASH_IDENTITY").nullable().type(ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxNumber.dropIndex("20180903.12", "IDX_SP_NUMBER");
			spidxNumber
					.addIndex("20180903.13", "IDX_SP_NUMBER_HASH_VAL")
					.unique(false)
					.withColumns("HASH_IDENTITY", "SP_VALUE")
					.doNothing();
			spidxNumber.addTask(new CalculateHashesTask(VersionEnum.V3_5_0, "20180903.14")
					.addCalculator(
							"HASH_IDENTITY",
							t -> BaseResourceIndexedSearchParam.calculateHashIdentity(
									new PartitionSettings(),
									RequestPartitionId.defaultPartition(),
									t.getResourceType(),
									t.getString("SP_NAME")))
					.setColumnName("HASH_IDENTITY"));
		}

		// Indexes - Quantity
		Builder.BuilderWithTableName spidxQuantity = version.onTable("HFJ_SPIDX_QUANTITY");
		version.startSectionWithMessage("Starting work on table: " + spidxQuantity.getTableName());
		spidxQuantity.addColumn("20180903.15", "HASH_IDENTITY").nullable().type(ColumnTypeEnum.LONG);
		spidxQuantity
				.addColumn("20180903.16", "HASH_IDENTITY_SYS_UNITS")
				.nullable()
				.type(ColumnTypeEnum.LONG);
		spidxQuantity
				.addColumn("20180903.17", "HASH_IDENTITY_AND_UNITS")
				.nullable()
				.type(ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxQuantity.dropIndex("20180903.18", "IDX_SP_QUANTITY");
			spidxQuantity
					.addIndex("20180903.19", "IDX_SP_QUANTITY_HASH")
					.unique(false)
					.withColumns("HASH_IDENTITY", "SP_VALUE");
			spidxQuantity
					.addIndex("20180903.20", "IDX_SP_QUANTITY_HASH_UN")
					.unique(false)
					.withColumns("HASH_IDENTITY_AND_UNITS", "SP_VALUE");
			spidxQuantity
					.addIndex("20180903.21", "IDX_SP_QUANTITY_HASH_SYSUN")
					.unique(false)
					.withColumns("HASH_IDENTITY_SYS_UNITS", "SP_VALUE");
			spidxQuantity.addTask(new CalculateHashesTask(VersionEnum.V3_5_0, "20180903.22")
					.addCalculator(
							"HASH_IDENTITY",
							t -> BaseResourceIndexedSearchParam.calculateHashIdentity(
									new PartitionSettings(),
									RequestPartitionId.defaultPartition(),
									t.getResourceType(),
									t.getString("SP_NAME")))
					.addCalculator(
							"HASH_IDENTITY_AND_UNITS",
							t -> ResourceIndexedSearchParamQuantity.calculateHashUnits(
									new PartitionSettings(),
									RequestPartitionId.defaultPartition(),
									t.getResourceType(),
									t.getString("SP_NAME"),
									t.getString("SP_UNITS")))
					.addCalculator(
							"HASH_IDENTITY_SYS_UNITS",
							t -> ResourceIndexedSearchParamQuantity.calculateHashSystemAndUnits(
									new PartitionSettings(),
									RequestPartitionId.defaultPartition(),
									t.getResourceType(),
									t.getString("SP_NAME"),
									t.getString("SP_SYSTEM"),
									t.getString("SP_UNITS")))
					.setColumnName("HASH_IDENTITY"));
		}

		// Indexes - String
		Builder.BuilderWithTableName spidxString = version.onTable("HFJ_SPIDX_STRING");
		version.startSectionWithMessage("Starting work on table: " + spidxString.getTableName());
		spidxString.addColumn("20180903.23", "HASH_NORM_PREFIX").nullable().type(ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxString.dropIndex("20180903.24", "IDX_SP_STRING");
			spidxString
					.addIndex("20180903.25", "IDX_SP_STRING_HASH_NRM")
					.unique(false)
					.withColumns("HASH_NORM_PREFIX", "SP_VALUE_NORMALIZED");
			spidxString.addColumn("20180903.26", "HASH_EXACT").nullable().type(ColumnTypeEnum.LONG);
			spidxString
					.addIndex("20180903.27", "IDX_SP_STRING_HASH_EXCT")
					.unique(false)
					.withColumns("HASH_EXACT");
			spidxString.addTask(new CalculateHashesTask(VersionEnum.V3_5_0, "20180903.28")
					.setColumnName("HASH_NORM_PREFIX")
					.addCalculator(
							"HASH_NORM_PREFIX",
							t -> ResourceIndexedSearchParamString.calculateHashNormalized(
									new PartitionSettings(),
									RequestPartitionId.defaultPartition(),
									new StorageSettings(),
									t.getResourceType(),
									t.getString("SP_NAME"),
									t.getString("SP_VALUE_NORMALIZED")))
					.addCalculator(
							"HASH_EXACT",
							t -> ResourceIndexedSearchParamString.calculateHashExact(
									new PartitionSettings(),
									(ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId) null,
									t.getResourceType(),
									t.getParamName(),
									t.getString("SP_VALUE_EXACT"))));
		}

		// Indexes - Token
		Builder.BuilderWithTableName spidxToken = version.onTable("HFJ_SPIDX_TOKEN");
		version.startSectionWithMessage("Starting work on table: " + spidxToken.getTableName());
		spidxToken.addColumn("20180903.29", "HASH_IDENTITY").nullable().type(ColumnTypeEnum.LONG);
		spidxToken.addColumn("20180903.30", "HASH_SYS").nullable().type(ColumnTypeEnum.LONG);
		spidxToken.addColumn("20180903.31", "HASH_SYS_AND_VALUE").nullable().type(ColumnTypeEnum.LONG);
		spidxToken.addColumn("20180903.32", "HASH_VALUE").nullable().type(ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxToken.dropIndex("20180903.33", "IDX_SP_TOKEN");
			spidxToken.dropIndex("20180903.34", "IDX_SP_TOKEN_UNQUAL");
			spidxToken
					.addIndex("20180903.35", "IDX_SP_TOKEN_HASH")
					.unique(false)
					.withColumns("HASH_IDENTITY")
					.doNothing();
			spidxToken
					.addIndex("20180903.36", "IDX_SP_TOKEN_HASH_S")
					.unique(false)
					.withColumns("HASH_SYS")
					.doNothing();
			spidxToken
					.addIndex("20180903.37", "IDX_SP_TOKEN_HASH_SV")
					.unique(false)
					.withColumns("HASH_SYS_AND_VALUE")
					.doNothing();
			spidxToken
					.addIndex("20180903.38", "IDX_SP_TOKEN_HASH_V")
					.unique(false)
					.withColumns("HASH_VALUE")
					.doNothing();
			spidxToken.addTask(new CalculateHashesTask(VersionEnum.V3_5_0, "20180903.39")
					.setColumnName("HASH_IDENTITY")
					.addCalculator(
							"HASH_IDENTITY",
							t -> BaseResourceIndexedSearchParam.calculateHashIdentity(
									new PartitionSettings(),
									RequestPartitionId.defaultPartition(),
									t.getResourceType(),
									t.getString("SP_NAME")))
					.addCalculator(
							"HASH_SYS",
							t -> ResourceIndexedSearchParamToken.calculateHashSystem(
									new PartitionSettings(),
									RequestPartitionId.defaultPartition(),
									t.getResourceType(),
									t.getParamName(),
									t.getString("SP_SYSTEM")))
					.addCalculator(
							"HASH_SYS_AND_VALUE",
							t -> ResourceIndexedSearchParamToken.calculateHashSystemAndValue(
									new PartitionSettings(),
									RequestPartitionId.defaultPartition(),
									t.getResourceType(),
									t.getParamName(),
									t.getString("SP_SYSTEM"),
									t.getString("SP_VALUE")))
					.addCalculator(
							"HASH_VALUE",
							t -> ResourceIndexedSearchParamToken.calculateHashValue(
									new PartitionSettings(),
									RequestPartitionId.defaultPartition(),
									t.getResourceType(),
									t.getParamName(),
									t.getString("SP_VALUE"))));
		}

		// Indexes - URI
		Builder.BuilderWithTableName spidxUri = version.onTable("HFJ_SPIDX_URI");
		version.startSectionWithMessage("Starting work on table: " + spidxUri.getTableName());
		spidxUri.addColumn("20180903.40", "HASH_IDENTITY").nullable().type(ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxUri.addIndex("20180903.41", "IDX_SP_URI_HASH_IDENTITY")
					.unique(false)
					.withColumns("HASH_IDENTITY", "SP_URI");
			spidxUri.addColumn("20180903.42", "HASH_URI").nullable().type(ColumnTypeEnum.LONG);
			spidxUri.addIndex("20180903.43", "IDX_SP_URI_HASH_URI")
					.unique(false)
					.withColumns("HASH_URI");
			spidxUri.addTask(new CalculateHashesTask(VersionEnum.V3_5_0, "20180903.44")
					.setColumnName("HASH_IDENTITY")
					.addCalculator(
							"HASH_IDENTITY",
							t -> BaseResourceIndexedSearchParam.calculateHashIdentity(
									new PartitionSettings(),
									(RequestPartitionId) null,
									t.getResourceType(),
									t.getString("SP_NAME")))
					.addCalculator(
							"HASH_URI",
							t -> ResourceIndexedSearchParamUri.calculateHashUri(
									new PartitionSettings(),
									(RequestPartitionId) null,
									t.getResourceType(),
									t.getString("SP_NAME"),
									t.getString("SP_URI"))));
		}

		// Search Parameter Presence
		Builder.BuilderWithTableName spp = version.onTable("HFJ_RES_PARAM_PRESENT");
		version.startSectionWithMessage("Starting work on table: " + spp.getTableName());
		spp.dropIndex("20180903.45", "IDX_RESPARMPRESENT_SPID_RESID");
		spp.addColumn("20180903.46", "HASH_PRESENCE").nullable().type(ColumnTypeEnum.LONG);
		spp.addIndex("20180903.47", "IDX_RESPARMPRESENT_HASHPRES").unique(false).withColumns("HASH_PRESENCE");

		ArbitrarySqlTask consolidateSearchParamPresenceIndexesTask = new ArbitrarySqlTask(
				VersionEnum.V3_5_0, "20180903.48", "HFJ_SEARCH_PARM", "Consolidate search parameter presence indexes");
		consolidateSearchParamPresenceIndexesTask.setExecuteOnlyIfTableExists("HFJ_SEARCH_PARM");
		consolidateSearchParamPresenceIndexesTask.setBatchSize(1);

		String sql = "SELECT " + "HFJ_SEARCH_PARM.RES_TYPE RES_TYPE, HFJ_SEARCH_PARM.PARAM_NAME PARAM_NAME, "
				+ "HFJ_RES_PARAM_PRESENT.PID PID, HFJ_RES_PARAM_PRESENT.SP_ID SP_ID, HFJ_RES_PARAM_PRESENT.SP_PRESENT SP_PRESENT, HFJ_RES_PARAM_PRESENT.HASH_PRESENCE HASH_PRESENCE "
				+ "from HFJ_RES_PARAM_PRESENT "
				+ "join HFJ_SEARCH_PARM ON (HFJ_SEARCH_PARM.PID = HFJ_RES_PARAM_PRESENT.SP_ID) "
				+ "where HFJ_RES_PARAM_PRESENT.HASH_PRESENCE is null";
		consolidateSearchParamPresenceIndexesTask.addExecuteOnlyIfColumnExists("HFJ_RES_PARAM_PRESENT", "SP_ID");
		consolidateSearchParamPresenceIndexesTask.addQuery(
				sql, ArbitrarySqlTask.QueryModeEnum.BATCH_UNTIL_NO_MORE, t -> {
					Number pid = (Number) t.get("PID");
					Boolean present = columnToBoolean(t.get("SP_PRESENT"));
					String resType = (String) t.get("RES_TYPE");
					String paramName = (String) t.get("PARAM_NAME");
					Long hash = SearchParamPresentEntity.calculateHashPresence(
							new PartitionSettings(), (RequestPartitionId) null, resType, paramName, present);
					consolidateSearchParamPresenceIndexesTask.executeSql(
							"HFJ_RES_PARAM_PRESENT",
							"update HFJ_RES_PARAM_PRESENT set HASH_PRESENCE = ? where PID = ?",
							hash,
							pid);
				});
		version.addTask(consolidateSearchParamPresenceIndexesTask);

		// SP_ID is no longer needed
		spp.dropColumn("20180903.49", "SP_ID");

		// Concept
		Builder.BuilderWithTableName trmConcept = version.onTable("TRM_CONCEPT");
		version.startSectionWithMessage("Starting work on table: " + trmConcept.getTableName());
		trmConcept.addColumn("20180903.50", "CONCEPT_UPDATED").nullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		trmConcept.addIndex("20180903.51", "IDX_CONCEPT_UPDATED").unique(false).withColumns("CONCEPT_UPDATED");
		trmConcept.modifyColumn("20180903.52", "CODE").nonNullable().withType(ColumnTypeEnum.STRING, 500);

		// Concept Designation
		version.startSectionWithMessage("Starting work on table: TRM_CONCEPT_DESIG");
		version.addTableRawSql("20180907.1", "TRM_CONCEPT_DESIG")
				.addSql(
						DriverTypeEnum.H2_EMBEDDED,
						"create table TRM_CONCEPT_DESIG (PID bigint not null, LANG varchar(500), USE_CODE varchar(500), USE_DISPLAY varchar(500), USE_SYSTEM varchar(500), VAL varchar(500) not null, CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID))")
				.addSql(
						DriverTypeEnum.H2_EMBEDDED,
						"alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
				.addSql(
						DriverTypeEnum.H2_EMBEDDED,
						"alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"create table TRM_CONCEPT_DESIG (PID bigint not null, LANG varchar(500), USE_CODE varchar(500), USE_DISPLAY varchar(500), USE_SYSTEM varchar(500), VAL varchar(500) not null, CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID))")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"create table TRM_CONCEPT_DESIG (PID bigint not null, LANG varchar(500), USE_CODE varchar(500), USE_DISPLAY varchar(500), USE_SYSTEM varchar(500), VAL varchar(500) not null, CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID)) ENGINE=InnoDB")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER (PID)")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT (PID)")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"create table TRM_CONCEPT_DESIG (PID bigint not null, LANG varchar(500), USE_CODE varchar(500), USE_DISPLAY varchar(500), USE_SYSTEM varchar(500), VAL varchar(500) not null, CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID))")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER (PID)")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT (PID)")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"create table TRM_CONCEPT_DESIG (PID number(19,0) not null, LANG varchar2(500 char), USE_CODE varchar2(500 char), USE_DISPLAY varchar2(500 char), USE_SYSTEM varchar2(500 char), VAL varchar2(500 char) not null, CS_VER_PID number(19,0), CONCEPT_PID number(19,0), primary key (PID))")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"create table TRM_CONCEPT_DESIG (PID int8 not null, LANG varchar(500), USE_CODE varchar(500), USE_DISPLAY varchar(500), USE_SYSTEM varchar(500), VAL varchar(500) not null, CS_VER_PID int8, CONCEPT_PID int8, primary key (PID))")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"create table TRM_CONCEPT_DESIG (PID bigint not null, LANG varchar(500), USE_CODE varchar(500), USE_DISPLAY varchar(500), USE_SYSTEM varchar(500), VAL varchar(500) not null, CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID))")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"alter table TRM_CONCEPT_DESIG add constraint FK_CONCEPTDESIG_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT");

		// Concept Property
		version.startSectionWithMessage("Starting work on table: TRM_CONCEPT_PROPERTY");
		version.addTableRawSql("20180907.2", "TRM_CONCEPT_PROPERTY")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"create table TRM_CONCEPT_PROPERTY (PID bigint not null, PROP_CODESYSTEM varchar(500), PROP_DISPLAY varchar(500), PROP_KEY varchar(500) not null, PROP_TYPE integer not null, PROP_VAL varchar(500), CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID))")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"create table TRM_CONCEPT_PROPERTY (PID bigint not null, PROP_CODESYSTEM varchar(500), PROP_DISPLAY varchar(500), PROP_KEY varchar(500) not null, PROP_TYPE integer not null, PROP_VAL varchar(500), CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID))")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER (PID)")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT (PID)")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"create table TRM_CONCEPT_PROPERTY (PID bigint not null, PROP_CODESYSTEM varchar(500), PROP_DISPLAY varchar(500), PROP_KEY varchar(500) not null, PROP_TYPE integer not null, PROP_VAL varchar(500), CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID))")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER (PID)")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT (PID)")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"create table TRM_CONCEPT_PROPERTY (PID number(19,0) not null, PROP_CODESYSTEM varchar2(500 char), PROP_DISPLAY varchar2(500 char), PROP_KEY varchar2(500 char) not null, PROP_TYPE number(10,0) not null, PROP_VAL varchar2(500 char), CS_VER_PID number(19,0), CONCEPT_PID number(19,0), primary key (PID))")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"create table TRM_CONCEPT_PROPERTY (PID int8 not null, PROP_CODESYSTEM varchar(500), PROP_DISPLAY varchar(500), PROP_KEY varchar(500) not null, PROP_TYPE int4 not null, PROP_VAL varchar(500), CS_VER_PID int8, CONCEPT_PID int8, primary key (PID))")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"create table TRM_CONCEPT_PROPERTY (PID bigint not null, PROP_CODESYSTEM varchar(500), PROP_DISPLAY varchar(500), PROP_KEY varchar(500) not null, PROP_TYPE int not null, PROP_VAL varchar(500), CS_VER_PID bigint, CONCEPT_PID bigint, primary key (PID))")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CSV foreign key (CS_VER_PID) references TRM_CODESYSTEM_VER")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"alter table TRM_CONCEPT_PROPERTY add constraint FK_CONCEPTPROP_CONCEPT foreign key (CONCEPT_PID) references TRM_CONCEPT");

		// Concept Map - Map
		version.startSectionWithMessage("Starting work on table: TRM_CONCEPT_MAP");
		version.addTableRawSql("20180907.3", "TRM_CONCEPT_MAP")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"create table TRM_CONCEPT_MAP (PID bigint not null, RES_ID bigint, SOURCE_URL varchar(200), TARGET_URL varchar(200), URL varchar(200) not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"alter table TRM_CONCEPT_MAP add constraint FK_TRMCONCEPTMAP_RES foreign key (RES_ID) references HFJ_RESOURCE")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"create table TRM_CONCEPT_MAP (PID bigint not null, RES_ID bigint, SOURCE_URL varchar(200), TARGET_URL varchar(200), URL varchar(200) not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"alter table TRM_CONCEPT_MAP add constraint IDX_CONCEPT_MAP_URL unique (URL)")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"alter table TRM_CONCEPT_MAP add constraint FK_TRMCONCEPTMAP_RES foreign key (RES_ID) references HFJ_RESOURCE (RES_ID)")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"create table TRM_CONCEPT_MAP (PID number(19,0) not null, RES_ID number(19,0), SOURCE_URL varchar2(200 char), TARGET_URL varchar2(200 char), URL varchar2(200 char) not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"alter table TRM_CONCEPT_MAP add constraint IDX_CONCEPT_MAP_URL unique (URL)")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"alter table TRM_CONCEPT_MAP add constraint FK_TRMCONCEPTMAP_RES foreign key (RES_ID) references HFJ_RESOURCE")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"create table TRM_CONCEPT_MAP (PID int8 not null, RES_ID int8, SOURCE_URL varchar(200), TARGET_URL varchar(200), URL varchar(200) not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"alter table TRM_CONCEPT_MAP add constraint FK_TRMCONCEPTMAP_RES foreign key (RES_ID) references HFJ_RESOURCE")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"alter table TRM_CONCEPT_MAP add constraint IDX_CONCEPT_MAP_URL unique (URL)")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"create table TRM_CONCEPT_MAP (PID bigint not null, RES_ID bigint, SOURCE_URL varchar(200), TARGET_URL varchar(200), URL varchar(200) not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"alter table TRM_CONCEPT_MAP add constraint IDX_CONCEPT_MAP_URL unique (URL)")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"alter table TRM_CONCEPT_MAP add constraint FK_TRMCONCEPTMAP_RES foreign key (RES_ID) references HFJ_RESOURCE")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"create table TRM_CONCEPT_MAP (PID bigint not null, RES_ID bigint, SOURCE_URL varchar(200), TARGET_URL varchar(200), URL varchar(200) not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"alter table TRM_CONCEPT_MAP add constraint FK_TRMCONCEPTMAP_RES foreign key (RES_ID) references HFJ_RESOURCE (RES_ID)")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"alter table TRM_CONCEPT_MAP add constraint IDX_CONCEPT_MAP_URL unique (URL)");

		// Concept Map - Group
		version.startSectionWithMessage("Starting work on table: TRM_CONCEPT_MAP_GROUP");
		version.addTableRawSql("20180907.4", "TRM_CONCEPT_MAP_GROUP")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"create table TRM_CONCEPT_MAP_GROUP (PID bigint not null, myConceptMapUrl varchar(255), SOURCE_URL varchar(200) not null, mySourceValueSet varchar(255), SOURCE_VERSION varchar(100), TARGET_URL varchar(200) not null, myTargetValueSet varchar(255), TARGET_VERSION varchar(100), CONCEPT_MAP_PID bigint not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"alter table TRM_CONCEPT_MAP_GROUP add constraint FK_TCMGROUP_CONCEPTMAP foreign key (CONCEPT_MAP_PID) references TRM_CONCEPT_MAP")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"create unique index IDX_CONCEPT_MAP_URL on TRM_CONCEPT_MAP (URL)")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"create table TRM_CONCEPT_MAP_GROUP (PID number(19,0) not null, myConceptMapUrl varchar2(255 char), SOURCE_URL varchar2(200 char) not null, mySourceValueSet varchar2(255 char), SOURCE_VERSION varchar2(100 char), TARGET_URL varchar2(200 char) not null, myTargetValueSet varchar2(255 char), TARGET_VERSION varchar2(100 char), CONCEPT_MAP_PID number(19,0) not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"alter table TRM_CONCEPT_MAP_GROUP add constraint FK_TCMGROUP_CONCEPTMAP foreign key (CONCEPT_MAP_PID) references TRM_CONCEPT_MAP")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"create table TRM_CONCEPT_MAP_GROUP (PID bigint not null, myConceptMapUrl varchar(255), SOURCE_URL varchar(200) not null, mySourceValueSet varchar(255), SOURCE_VERSION varchar(100), TARGET_URL varchar(200) not null, myTargetValueSet varchar(255), TARGET_VERSION varchar(100), CONCEPT_MAP_PID bigint not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"alter table TRM_CONCEPT_MAP_GROUP add constraint FK_TCMGROUP_CONCEPTMAP foreign key (CONCEPT_MAP_PID) references TRM_CONCEPT_MAP (PID)")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"create table TRM_CONCEPT_MAP_GROUP (PID bigint not null, myConceptMapUrl varchar(255), SOURCE_URL varchar(200) not null, mySourceValueSet varchar(255), SOURCE_VERSION varchar(100), TARGET_URL varchar(200) not null, myTargetValueSet varchar(255), TARGET_VERSION varchar(100), CONCEPT_MAP_PID bigint not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"alter table TRM_CONCEPT_MAP_GROUP add constraint FK_TCMGROUP_CONCEPTMAP foreign key (CONCEPT_MAP_PID) references TRM_CONCEPT_MAP (PID)")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"create table TRM_CONCEPT_MAP_GROUP (PID bigint not null, myConceptMapUrl varchar(255), SOURCE_URL varchar(200) not null, mySourceValueSet varchar(255), SOURCE_VERSION varchar(100), TARGET_URL varchar(200) not null, myTargetValueSet varchar(255), TARGET_VERSION varchar(100), CONCEPT_MAP_PID bigint not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"alter table TRM_CONCEPT_MAP_GROUP add constraint FK_TCMGROUP_CONCEPTMAP foreign key (CONCEPT_MAP_PID) references TRM_CONCEPT_MAP")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"create table TRM_CONCEPT_MAP_GROUP (PID int8 not null, myConceptMapUrl varchar(255), SOURCE_URL varchar(200) not null, mySourceValueSet varchar(255), SOURCE_VERSION varchar(100), TARGET_URL varchar(200) not null, myTargetValueSet varchar(255), TARGET_VERSION varchar(100), CONCEPT_MAP_PID int8 not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"alter table TRM_CONCEPT_MAP_GROUP add constraint FK_TCMGROUP_CONCEPTMAP foreign key (CONCEPT_MAP_PID) references TRM_CONCEPT_MAP");

		// Concept Map - Group Element
		version.startSectionWithMessage("Starting work on table: TRM_CONCEPT_MAP_GRP_ELEMENT");
		version.addTableRawSql("20180907.5", "TRM_CONCEPT_MAP_GRP_ELEMENT")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"create table TRM_CONCEPT_MAP_GRP_ELEMENT (PID bigint not null, SOURCE_CODE varchar(500) not null, myConceptMapUrl varchar(255), SOURCE_DISPLAY varchar(400), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GROUP_PID bigint not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"alter table TRM_CONCEPT_MAP_GRP_ELEMENT add constraint FK_TCMGELEMENT_GROUP foreign key (CONCEPT_MAP_GROUP_PID) references TRM_CONCEPT_MAP_GROUP")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"create table TRM_CONCEPT_MAP_GRP_ELEMENT (PID bigint not null, SOURCE_CODE varchar(500) not null, myConceptMapUrl varchar(255), SOURCE_DISPLAY varchar(400), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GROUP_PID bigint not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"alter table TRM_CONCEPT_MAP_GRP_ELEMENT add constraint FK_TCMGELEMENT_GROUP foreign key (CONCEPT_MAP_GROUP_PID) references TRM_CONCEPT_MAP_GROUP (PID)")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"create index IDX_CNCPT_MAP_GRP_CD on TRM_CONCEPT_MAP_GRP_ELEMENT (SOURCE_CODE)")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"create index IDX_CNCPT_MAP_GRP_CD on TRM_CONCEPT_MAP_GRP_ELEMENT (SOURCE_CODE)")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"create table TRM_CONCEPT_MAP_GRP_ELEMENT (PID bigint not null, SOURCE_CODE varchar(500) not null, myConceptMapUrl varchar(255), SOURCE_DISPLAY varchar(400), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GROUP_PID bigint not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"create index IDX_CNCPT_MAP_GRP_CD on TRM_CONCEPT_MAP_GRP_ELEMENT (SOURCE_CODE)")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"alter table TRM_CONCEPT_MAP_GRP_ELEMENT add constraint FK_TCMGELEMENT_GROUP foreign key (CONCEPT_MAP_GROUP_PID) references TRM_CONCEPT_MAP_GROUP (PID)")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"create table TRM_CONCEPT_MAP_GRP_ELEMENT (PID int8 not null, SOURCE_CODE varchar(500) not null, myConceptMapUrl varchar(255), SOURCE_DISPLAY varchar(400), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GROUP_PID int8 not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"alter table TRM_CONCEPT_MAP_GRP_ELEMENT add constraint FK_TCMGELEMENT_GROUP foreign key (CONCEPT_MAP_GROUP_PID) references TRM_CONCEPT_MAP_GROUP")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"create index IDX_CNCPT_MAP_GRP_CD on TRM_CONCEPT_MAP_GRP_ELEMENT (SOURCE_CODE)")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"create table TRM_CONCEPT_MAP_GRP_ELEMENT (PID number(19,0) not null, SOURCE_CODE varchar2(500 char) not null, myConceptMapUrl varchar2(255 char), SOURCE_DISPLAY varchar2(400 char), mySystem varchar2(255 char), mySystemVersion varchar2(255 char), myValueSet varchar2(255 char), CONCEPT_MAP_GROUP_PID number(19,0) not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"alter table TRM_CONCEPT_MAP_GRP_ELEMENT add constraint FK_TCMGELEMENT_GROUP foreign key (CONCEPT_MAP_GROUP_PID) references TRM_CONCEPT_MAP_GROUP")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"create index IDX_CNCPT_MAP_GRP_CD on TRM_CONCEPT_MAP_GRP_ELEMENT (SOURCE_CODE)")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"create table TRM_CONCEPT_MAP_GRP_ELEMENT (PID bigint not null, SOURCE_CODE varchar(500) not null, myConceptMapUrl varchar(255), SOURCE_DISPLAY varchar(400), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GROUP_PID bigint not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"create index IDX_CNCPT_MAP_GRP_CD on TRM_CONCEPT_MAP_GRP_ELEMENT (SOURCE_CODE)")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"alter table TRM_CONCEPT_MAP_GRP_ELEMENT add constraint FK_TCMGELEMENT_GROUP foreign key (CONCEPT_MAP_GROUP_PID) references TRM_CONCEPT_MAP_GROUP");

		// Concept Map - Group Element Target
		version.startSectionWithMessage("Starting work on table: TRM_CONCEPT_MAP_GRP_ELM_TGT");
		version.addTableRawSql("20180907.6", "TRM_CONCEPT_MAP_GRP_ELM_TGT")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"create table TRM_CONCEPT_MAP_GRP_ELM_TGT (PID bigint not null, TARGET_CODE varchar(500) not null, myConceptMapUrl varchar(255), TARGET_DISPLAY varchar(400), TARGET_EQUIVALENCE varchar(50), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GRP_ELM_PID bigint not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"alter table TRM_CONCEPT_MAP_GRP_ELM_TGT add constraint FK_TCMGETARGET_ELEMENT foreign key (CONCEPT_MAP_GRP_ELM_PID) references TRM_CONCEPT_MAP_GRP_ELEMENT")
				.addSql(
						DriverTypeEnum.DERBY_EMBEDDED,
						"create index IDX_CNCPT_MP_GRP_ELM_TGT_CD on TRM_CONCEPT_MAP_GRP_ELM_TGT (TARGET_CODE)")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"create table TRM_CONCEPT_MAP_GRP_ELM_TGT (PID bigint not null, TARGET_CODE varchar(500) not null, myConceptMapUrl varchar(255), TARGET_DISPLAY varchar(400), TARGET_EQUIVALENCE varchar(50), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GRP_ELM_PID bigint not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"alter table TRM_CONCEPT_MAP_GRP_ELM_TGT add constraint FK_TCMGETARGET_ELEMENT foreign key (CONCEPT_MAP_GRP_ELM_PID) references TRM_CONCEPT_MAP_GRP_ELEMENT (PID)")
				.addSql(
						DriverTypeEnum.MARIADB_10_1,
						"create index IDX_CNCPT_MP_GRP_ELM_TGT_CD on TRM_CONCEPT_MAP_GRP_ELM_TGT (TARGET_CODE)")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"create table TRM_CONCEPT_MAP_GRP_ELM_TGT (PID bigint not null, TARGET_CODE varchar(500) not null, myConceptMapUrl varchar(255), TARGET_DISPLAY varchar(400), TARGET_EQUIVALENCE varchar(50), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GRP_ELM_PID bigint not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"alter table TRM_CONCEPT_MAP_GRP_ELM_TGT add constraint FK_TCMGETARGET_ELEMENT foreign key (CONCEPT_MAP_GRP_ELM_PID) references TRM_CONCEPT_MAP_GRP_ELEMENT (PID)")
				.addSql(
						DriverTypeEnum.MYSQL_5_7,
						"create index IDX_CNCPT_MP_GRP_ELM_TGT_CD on TRM_CONCEPT_MAP_GRP_ELM_TGT (TARGET_CODE)")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"create table TRM_CONCEPT_MAP_GRP_ELM_TGT (PID number(19,0) not null, TARGET_CODE varchar2(500 char) not null, myConceptMapUrl varchar2(255 char), TARGET_DISPLAY varchar2(400 char), TARGET_EQUIVALENCE varchar2(50 char), mySystem varchar2(255 char), mySystemVersion varchar2(255 char), myValueSet varchar2(255 char), CONCEPT_MAP_GRP_ELM_PID number(19,0) not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"alter table TRM_CONCEPT_MAP_GRP_ELM_TGT add constraint FK_TCMGETARGET_ELEMENT foreign key (CONCEPT_MAP_GRP_ELM_PID) references TRM_CONCEPT_MAP_GRP_ELEMENT")
				.addSql(
						DriverTypeEnum.ORACLE_12C,
						"create index IDX_CNCPT_MP_GRP_ELM_TGT_CD on TRM_CONCEPT_MAP_GRP_ELM_TGT (TARGET_CODE)")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"create table TRM_CONCEPT_MAP_GRP_ELM_TGT (PID int8 not null, TARGET_CODE varchar(500) not null, myConceptMapUrl varchar(255), TARGET_DISPLAY varchar(400), TARGET_EQUIVALENCE varchar(50), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GRP_ELM_PID int8 not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"alter table TRM_CONCEPT_MAP_GRP_ELM_TGT add constraint FK_TCMGETARGET_ELEMENT foreign key (CONCEPT_MAP_GRP_ELM_PID) references TRM_CONCEPT_MAP_GRP_ELEMENT")
				.addSql(
						DriverTypeEnum.POSTGRES_9_4,
						"create index IDX_CNCPT_MP_GRP_ELM_TGT_CD on TRM_CONCEPT_MAP_GRP_ELM_TGT (TARGET_CODE)")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"create table TRM_CONCEPT_MAP_GRP_ELM_TGT (PID bigint not null, TARGET_CODE varchar(500) not null, myConceptMapUrl varchar(255), TARGET_DISPLAY varchar(400), TARGET_EQUIVALENCE varchar(50), mySystem varchar(255), mySystemVersion varchar(255), myValueSet varchar(255), CONCEPT_MAP_GRP_ELM_PID bigint not null, primary key (PID))")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"create index IDX_CNCPT_MP_GRP_ELM_TGT_CD on TRM_CONCEPT_MAP_GRP_ELM_TGT (TARGET_CODE)")
				.addSql(
						DriverTypeEnum.MSSQL_2012,
						"alter table TRM_CONCEPT_MAP_GRP_ELM_TGT add constraint FK_TCMGETARGET_ELEMENT foreign key (CONCEPT_MAP_GRP_ELM_PID) references TRM_CONCEPT_MAP_GRP_ELEMENT");

		version.onTable("HFJ_IDX_CMP_STRING_UNIQ")
				.modifyColumn("20180907.7", "IDX_STRING")
				.nonNullable()
				.withType(ColumnTypeEnum.STRING, 200);
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

	private void init340() { // 20180401 - 20180528
		Builder version = forVersion(VersionEnum.V3_4_0);

		// CodeSystem Version
		Builder.BuilderWithTableName resourceLink = version.onTable("TRM_CODESYSTEM_VER");
		version.startSectionWithMessage("Starting work on table: " + resourceLink.getTableName());
		resourceLink.dropIndex("20180401.1", "IDX_CSV_RESOURCEPID_AND_VER");
		resourceLink.dropColumn("20180401.2", "RES_VERSION_ID");
		resourceLink.addColumn("20180401.3", "CS_VERSION_ID").nullable().type(ColumnTypeEnum.STRING, 255);
		resourceLink.addColumn("20180401.4", "CODESYSTEM_PID").nullable().type(ColumnTypeEnum.LONG);
		resourceLink
				.addForeignKey("20180401.5", "FK_CODESYSVER_CS_ID")
				.toColumn("CODESYSTEM_PID")
				.references("TRM_CODESYSTEM", "PID");

		// Concept
		Builder.BuilderWithTableName concept = version.onTable("TRM_CONCEPT");
		version.startSectionWithMessage("Starting work on table: " + concept.getTableName());
		concept.addColumn("20180401.6", "CODE_SEQUENCE").nullable().type(ColumnTypeEnum.INT);
	}

	protected void init330() { // 20180114 - 20180329
		Builder version = forVersion(VersionEnum.V3_3_0);

		version.initializeSchema(
				"20180115.0",
				new SchemaInitializationProvider(
						"HAPI FHIR", "/ca/uhn/hapi/fhir/jpa/docs/database", "HFJ_RESOURCE", true));

		Builder.BuilderWithTableName hfjResource = version.onTable("HFJ_RESOURCE");
		version.startSectionWithMessage("Starting work on table: " + hfjResource.getTableName());
		hfjResource.dropColumn("20180115.1", "RES_TEXT");
		hfjResource.dropColumn("20180115.2", "RES_ENCODING");

		Builder.BuilderWithTableName hfjResVer = version.onTable("HFJ_RES_VER");
		version.startSectionWithMessage("Starting work on table: " + hfjResVer.getTableName());
		hfjResVer.modifyColumn("20180115.3", "RES_ENCODING").nullable();
		hfjResVer.modifyColumn("20180115.4", "RES_TEXT").nullable();
	}

	public enum FlagEnum {
		NO_MIGRATE_HASHES("no-migrate-350-hashes");

		private final String myCommandLineValue;

		FlagEnum(String theCommandLineValue) {
			myCommandLineValue = theCommandLineValue;
		}

		public static FlagEnum fromCommandLineValue(String theCommandLineValue) {
			Optional<FlagEnum> retVal = Arrays.stream(values())
					.filter(t -> t.myCommandLineValue.equals(theCommandLineValue))
					.findFirst();
			return retVal.orElseThrow(() -> {
				List<String> validValues = Arrays.stream(values())
						.map(t -> t.myCommandLineValue)
						.sorted()
						.collect(Collectors.toList());
				return new IllegalArgumentException(
						"Invalid flag \"" + theCommandLineValue + "\". Valid values: " + validValues);
			});
		}
	}
}
