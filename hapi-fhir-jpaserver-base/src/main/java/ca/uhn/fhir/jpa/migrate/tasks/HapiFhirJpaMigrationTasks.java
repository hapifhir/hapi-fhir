package ca.uhn.fhir.jpa.migrate.tasks;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.ArbitrarySqlTask;
import ca.uhn.fhir.jpa.migrate.taskdef.CalculateHashesTask;
import ca.uhn.fhir.jpa.migrate.taskdef.CalculateOrdinalDatesTask;
import ca.uhn.fhir.jpa.migrate.taskdef.ColumnTypeEnum;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresentEntity;
import ca.uhn.fhir.util.VersionEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"SqlNoDataSourceInspection", "SpellCheckingInspection"})
public class HapiFhirJpaMigrationTasks extends BaseMigrationTasks<VersionEnum> {

	// H2, Derby, MariaDB, and MySql automatically add indexes to foreign keys
	public static final DriverTypeEnum[] NON_AUTOMATIC_FK_INDEX_PLATFORMS = new DriverTypeEnum[]{
		DriverTypeEnum.POSTGRES_9_4, DriverTypeEnum.ORACLE_12C, DriverTypeEnum.MSSQL_2012};
	private final Set<FlagEnum> myFlags;


	/**
	 * Constructor
	 */
	public HapiFhirJpaMigrationTasks(Set<String> theFlags) {
		myFlags = theFlags
			.stream()
			.map(FlagEnum::fromCommandLineValue)
			.collect(Collectors.toSet());

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
				.withColumns("HASH_IDENTITY", "SP_VALUE_LOW_DATE_ORDINAL", "SP_VALUE_HIGH_DATE_ORDINAL", "RES_ID", "PARTITION_ID");
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
				.withColumns("RES_ID", "HASH_IDENTITY", "SP_VALUE_LOW", "SP_VALUE_HIGH", "SP_VALUE_LOW_DATE_ORDINAL", "SP_VALUE_HIGH_DATE_ORDINAL", "PARTITION_ID");
			// some engines tie the FK constraint to a particular index.
			// So we need to drop and recreate the constraint to drop the old RES_ID index.
			// Rename it while we're at it.  FK17s70oa59rm9n61k9thjqrsqm was not a pretty name.
			dateTable.dropForeignKey("20220207.12", "FK17S70OA59RM9N61K9THJQRSQM", "HFJ_RESOURCE");
			dateTable.dropIndexOnline("20220207.13", "IDX_SP_DATE_RESID");
			dateTable.dropIndexOnline("20220207.14", "FK17S70OA59RM9N61K9THJQRSQM");

			dateTable.addForeignKey("20220207.15", "FK_SP_DATE_RES")
				.toColumn("RES_ID").references("HFJ_RESOURCE", "RES_ID");

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
				.unique(false).online(true)
				.withColumns("HASH_IDENTITY", "SP_SYSTEM", "SP_VALUE", "RES_ID", "PARTITION_ID");

			tokenTable.dropIndexOnline("20220208.2", "IDX_SP_TOKEN_HASH");

			// for search by system
			tokenTable
				.addIndex("20220208.3", "IDX_SP_TOKEN_HASH_S_V2")
				.unique(false).online(true)
				.withColumns("HASH_SYS", "RES_ID", "PARTITION_ID");

			tokenTable.dropIndexOnline("20220208.4", "IDX_SP_TOKEN_HASH_S");

			// for search by system+value
			tokenTable
				.addIndex("20220208.5", "IDX_SP_TOKEN_HASH_SV_V2")
				.unique(false).online(true)
				.withColumns("HASH_SYS_AND_VALUE", "RES_ID", "PARTITION_ID");

			tokenTable.dropIndexOnline("20220208.6", "IDX_SP_TOKEN_HASH_SV");

			// for search by value
			tokenTable
				.addIndex("20220208.7", "IDX_SP_TOKEN_HASH_V_V2")
				.unique(false).online(true)
				.withColumns("HASH_VALUE", "RES_ID", "PARTITION_ID");

			tokenTable.dropIndexOnline("20220208.8", "IDX_SP_TOKEN_HASH_V");

			// obsolete.  We're dropping this column.
			tokenTable.dropIndexOnline("20220208.9", "IDX_SP_TOKEN_UPDATED");

			// for joining as second table:
			{
				// replace and drop IDX_SP_TOKEN_RESID, and the associated fk constraint
				tokenTable
					.addIndex("20220208.10", "IDX_SP_TOKEN_RESID_V2")
					.unique(false).online(true)
					.withColumns("RES_ID", "HASH_SYS_AND_VALUE", "HASH_VALUE", "HASH_SYS", "HASH_IDENTITY", "PARTITION_ID");

				// some engines tie the FK constraint to a particular index.
				// So we need to drop and recreate the constraint to drop the old RES_ID index.
				// Rename it while we're at it.  FK7ULX3J1GG3V7MAQREJGC7YBC4 was not a pretty name.
				tokenTable.dropForeignKey("20220208.11", "FK7ULX3J1GG3V7MAQREJGC7YBC4", "HFJ_RESOURCE");
				tokenTable.dropIndexOnline("20220208.12", "IDX_SP_TOKEN_RESID");
				tokenTable.dropIndexOnline("20220208.13", "FK7ULX3J1GG3V7MAQREJGC7YBC4");

				tokenTable.addForeignKey("20220208.14", "FK_SP_TOKEN_RES")
					.toColumn("RES_ID").references("HFJ_RESOURCE", "RES_ID");
			}
		}

		// fix for https://github.com/hapifhir/hapi-fhir/issues/3316
		// index must have same name that indexed FK or SchemaMigrationTest complains because H2 sets this index automatically

		version.onTable("TRM_VALUESET_C_DESIGNATION")
			.addIndex("20220223.1", "FK_TRM_VALUESET_CONCEPT_PID")
			.unique(false)
			.withColumns("VALUESET_CONCEPT_PID")
			.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		// Batch2 Framework

		Builder.BuilderAddTableByColumns batchInstance = version.addTableByColumns("20220227.1", "BT2_JOB_INSTANCE", "ID");
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
		batchChunk.addForeignKey("20220227.5", "FK_BT2WC_INSTANCE").toColumn("INSTANCE_ID").references("BT2_JOB_INSTANCE", "ID");

		replaceNumericSPIndices(version);
		replaceQuantitySPIndices(version);

		// Drop Index on HFJ_RESOURCE.INDEX_STATUS
		version
			.onTable("HFJ_RESOURCE")
			.dropIndex("20220314.1", "IDX_INDEXSTATUS");

		version
			.onTable("BT2_JOB_INSTANCE")
			.addColumn("20220416.1", "CUR_GATED_STEP_ID")
			.nullable()
			.type(ColumnTypeEnum.STRING, 100);

		//Make Job expiry nullable so that we can prevent job expiry by using a null value.
		version
			.onTable("HFJ_BLK_EXPORT_JOB").modifyColumn("20220423.1", "EXP_TIME").nullable().withType(ColumnTypeEnum.DATE_TIMESTAMP);

		// New Index on HFJ_RESOURCE for $reindex Operation - hapi-fhir #3534
		{
			version.onTable("HFJ_RESOURCE")
				.addIndex("20220425.1", "IDX_RES_TYPE_DEL_UPDATED")
				.unique(false)
				.online(true)
				.withColumns("RES_TYPE", "RES_DELETED_AT", "RES_UPDATED", "PARTITION_ID", "RES_ID");

			// Drop existing Index on HFJ_RESOURCE.RES_TYPE since the new Index will meet the overall Index Demand
			version
				.onTable("HFJ_RESOURCE")
				.dropIndexOnline("20220425.2", "IDX_RES_TYPE");
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
			addResTagConstraint.put(DriverTypeEnum.H2_EMBEDDED, "ALTER TABLE HFJ_RES_TAG ADD CONSTRAINT IDX_RESTAG_TAGID UNIQUE (RES_ID, TAG_ID)");
			addResTagConstraint.put(DriverTypeEnum.MARIADB_10_1, "ALTER TABLE HFJ_RES_TAG ADD CONSTRAINT IDX_RESTAG_TAGID UNIQUE (RES_ID, TAG_ID)");
			addResTagConstraint.put(DriverTypeEnum.MSSQL_2012, "ALTER TABLE HFJ_RES_TAG ADD CONSTRAINT IDX_RESTAG_TAGID UNIQUE (RES_ID, TAG_ID)");
			addResTagConstraint.put(DriverTypeEnum.MYSQL_5_7, "ALTER TABLE HFJ_RES_TAG ADD CONSTRAINT IDX_RESTAG_TAGID UNIQUE (RES_ID, TAG_ID)");
			addResTagConstraint.put(DriverTypeEnum.ORACLE_12C, "ALTER TABLE HFJ_RES_TAG ADD CONSTRAINT IDX_RESTAG_TAGID UNIQUE (RES_ID, TAG_ID)");
			addResTagConstraint.put(DriverTypeEnum.POSTGRES_9_4, "ALTER TABLE HFJ_RES_TAG ADD CONSTRAINT IDX_RESTAG_TAGID UNIQUE (RES_ID, TAG_ID)");
			version.executeRawSql("20220429.5", addResTagConstraint);

			Builder.BuilderWithTableName tagTable = version.onTable("HFJ_TAG_DEF");
			tagTable
				.addIndex("20220429.6", "IDX_TAG_DEF_TP_CD_SYS")
				.unique(false)
				.online(false)
				.withColumns("TAG_TYPE", "TAG_CODE", "TAG_SYSTEM", "TAG_ID");
			// move constraint to new index
			// Ugh.  Only oracle supports using IDX_TAG_DEF_TP_CD_SYS to enforce this constraint.  The others will create another index.
			// For Sql Server, should change the index to be unique with include columns.  Do this in 6.1
			tagTable.dropIndex("20220429.8", "IDX_TAGDEF_TYPESYSCODE");
			Map<DriverTypeEnum, String> addTagDefConstraint = new HashMap<>();
			addTagDefConstraint.put(DriverTypeEnum.H2_EMBEDDED, "ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODE UNIQUE (TAG_TYPE, TAG_CODE, TAG_SYSTEM)");
			addTagDefConstraint.put(DriverTypeEnum.MARIADB_10_1, "ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODE UNIQUE (TAG_TYPE, TAG_CODE, TAG_SYSTEM)");
			addTagDefConstraint.put(DriverTypeEnum.MSSQL_2012, "ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODE UNIQUE (TAG_TYPE, TAG_CODE, TAG_SYSTEM)");
			addTagDefConstraint.put(DriverTypeEnum.MYSQL_5_7, "ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODE UNIQUE (TAG_TYPE, TAG_CODE, TAG_SYSTEM)");
			addTagDefConstraint.put(DriverTypeEnum.ORACLE_12C, "ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODE UNIQUE (TAG_TYPE, TAG_CODE, TAG_SYSTEM)");
			addTagDefConstraint.put(DriverTypeEnum.POSTGRES_9_4, "ALTER TABLE HFJ_TAG_DEF ADD CONSTRAINT IDX_TAGDEF_TYPESYSCODE UNIQUE (TAG_TYPE, TAG_CODE, TAG_SYSTEM)");
			version.executeRawSql("20220429.9", addTagDefConstraint);

		}


		// Fix for https://github.com/hapifhir/hapi-fhir-jpaserver-starter/issues/328
		version.onTable("NPM_PACKAGE_VER")
			.modifyColumn("20220501.1","FHIR_VERSION_ID").nonNullable().withType(ColumnTypeEnum.STRING, 20);

		version.onTable("NPM_PACKAGE_VER_RES")
			.modifyColumn("20220501.2","FHIR_VERSION_ID").nonNullable().withType(ColumnTypeEnum.STRING, 20);
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
				.unique(false).online(true)
				.withColumns("RES_ID", "HASH_IDENTITY", "SP_VALUE", "PARTITION_ID");

			// some engines tie the FK constraint to a particular index.
			// So we need to drop and recreate the constraint to drop the old RES_ID index.
			// Rename it while we're at it.  FK7ULX3J1GG3V7MAQREJGC7YBC4 was not a pretty name.
			numberTable.dropForeignKey("20220304.4", "FKCLTIHNC5TGPRJ9BHPT7XI5OTB", "HFJ_RESOURCE");
			numberTable.dropIndexOnline("20220304.5", "IDX_SP_NUMBER_RESID");
			numberTable.dropIndexOnline("20220304.6", "FKCLTIHNC5TGPRJ9BHPT7XI5OTB");

			numberTable.addForeignKey("20220304.7", "FK_SP_NUMBER_RES")
				.toColumn("RES_ID").references("HFJ_RESOURCE", "RES_ID");
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
					.unique(false).online(true)
					.withColumns("RES_ID", "HASH_IDENTITY", "HASH_IDENTITY_SYS_UNITS", "HASH_IDENTITY_AND_UNITS", "SP_VALUE", "PARTITION_ID");

				// some engines tie the FK constraint to a particular index.
				// So we need to drop and recreate the constraint to drop the old RES_ID index.
				// Rename it while we're at it.  FK7ULX3J1GG3V7MAQREJGC7YBC4 was not a pretty name.
				quantityTable.dropForeignKey("20220304.18", "FKN603WJJOI1A6ASEWXBBD78BI5", "HFJ_RESOURCE");
				quantityTable.dropIndexOnline("20220304.19", "IDX_SP_QUANTITY_RESID");
				quantityTable.dropIndexOnline("20220304.20", "FKN603WJJOI1A6ASEWXBBD78BI5");

				quantityTable.addForeignKey("20220304.21", "FK_SP_QUANTITY_RES")
					.toColumn("RES_ID").references("HFJ_RESOURCE", "RES_ID");
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
					.unique(false).online(true)
					.withColumns("RES_ID", "HASH_IDENTITY", "HASH_IDENTITY_SYS_UNITS", "HASH_IDENTITY_AND_UNITS", "SP_VALUE", "PARTITION_ID");

				// some engines tie the FK constraint to a particular index.
				// So we need to drop and recreate the constraint to drop the old RES_ID index.
				// Rename it while we're at it.  FK7ULX3J1GG3V7MAQREJGC7YBC4 was not a pretty name.
				quantityNormTable.dropForeignKey("20220304.30", "FKRCJOVMUH5KC0O6FVBLE319PYV", "HFJ_RESOURCE");
				quantityNormTable.dropIndexOnline("20220304.31", "IDX_SP_QNTY_NRML_RESID");
				quantityNormTable.dropIndexOnline("20220304.32", "FKRCJOVMUH5KC0O6FVBLE319PYV");

				quantityNormTable.addForeignKey("20220304.33", "FK_SP_QUANTITYNM_RES")
					.toColumn("RES_ID").references("HFJ_RESOURCE", "RES_ID");
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

		theVersion.onTable("HFJ_HISTORY_TAG")
			.addIndex("20211210.2", "IDX_RESHISTTAG_RESID")
			.unique(false)
			.withColumns("RES_ID");


		theVersion.onTable("HFJ_RES_VER_PROV")
			.addIndex("20211210.3", "FK_RESVERPROV_RES_PID")
			.unique(false)
			.withColumns("RES_PID")
			.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);

		theVersion.onTable("HFJ_FORCED_ID")
			.addIndex("20211210.4", "FK_FORCEDID_RESOURCE")
			.unique(true)
			.withColumns("RESOURCE_PID")
			.doNothing()//This migration was added in error, as this table already has a unique constraint on RESOURCE_PID and every database creates an index on anything that is unique.
			.onlyAppliesToPlatforms(NON_AUTOMATIC_FK_INDEX_PLATFORMS);
	}

	private void init570() {
		Builder version = forVersion(VersionEnum.V5_7_0);

		// both indexes must have same name that indexed FK or SchemaMigrationTest complains because H2 sets this index automatically

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

		empiLink.addColumn("20220324.1", "PARTITION_ID")
			.nullable()
			.type(ColumnTypeEnum.INT);
		empiLink.addColumn("20220324.2", "PARTITION_DATE")
			.nullable()
			.type(ColumnTypeEnum.DATE_ONLY);
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
		forcedId.dropIndex("20210516.1", "IDX_FORCEDID_TYPE_FID").onlyAppliesToPlatforms(DriverTypeEnum.MSSQL_2012).runEvenDuringSchemaInitialization();
		forcedId.addIndex("20210516.2", "IDX_FORCEDID_TYPE_FID").unique(true).includeColumns("RESOURCE_PID").withColumns("RESOURCE_TYPE", "FORCED_ID").onlyAppliesToPlatforms(DriverTypeEnum.MSSQL_2012).runEvenDuringSchemaInitialization();

		// Add bulk import file description
		version.onTable("HFJ_BLK_IMPORT_JOBFILE")
			.addColumn("20210528.1", "FILE_DESCRIPTION").nullable().type(ColumnTypeEnum.STRING, 500);

		// Bump ConceptMap display lengths
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
			.modifyColumn("20210617.1", "TARGET_DISPLAY").nullable().withType(ColumnTypeEnum.STRING, 500);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
			.modifyColumn("20210617.2", "SOURCE_DISPLAY").nullable().withType(ColumnTypeEnum.STRING, 500);

		version.onTable("HFJ_BLK_EXPORT_JOB")
			.modifyColumn("20210624.1", "REQUEST").nonNullable().withType(ColumnTypeEnum.STRING, 1024);

		version.onTable("HFJ_IDX_CMP_STRING_UNIQ")
			.modifyColumn("20210713.1", "IDX_STRING").nonNullable().withType(ColumnTypeEnum.STRING, 500);

		version.onTable("HFJ_RESOURCE")
			.addColumn("20210720.1", "SP_CMPTOKS_PRESENT").nullable().type(ColumnTypeEnum.BOOLEAN);

		version.addIdGenerator("20210720.2", "SEQ_IDXCMBTOKNU_ID");

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

		// Dropping index on the language column, as it's no longer in use.
		// TODO: After 2 releases from 5.5.0, drop the column too
		version.onTable("HFJ_RESOURCE")
			.dropIndex("20210908.1", "IDX_RES_LANG");

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
		version.onTable("TRM_CONCEPT")
			.migratePostgresTextClobToBinaryClob("20211003.2", "PARENT_PIDS");

		// HFJ_SEARCH.SEARCH_QUERY_STRING
		version.onTable("HFJ_SEARCH")
			.migratePostgresTextClobToBinaryClob("20211003.3", "SEARCH_QUERY_STRING");

	}

	private void init540() {

		Builder version = forVersion(VersionEnum.V5_4_0);

		//-- add index on HFJ_SPIDX_DATE
		version.onTable("HFJ_SPIDX_DATE").addIndex("20210309.1", "IDX_SP_DATE_HASH_HIGH")
			.unique(false).withColumns("HASH_IDENTITY", "SP_VALUE_HIGH")
			.doNothing();

		//-- add index on HFJ_FORCED_ID
		version.onTable("HFJ_FORCED_ID").addIndex("20210309.2", "IDX_FORCEID_FID")
			.unique(false).withColumns("FORCED_ID");

		//-- ValueSet Concept Fulltext Indexing
		version.onTable("TRM_VALUESET_CONCEPT").addColumn("20210406.1", "INDEX_STATUS").nullable().type(ColumnTypeEnum.LONG);
		version.onTable("TRM_VALUESET_CONCEPT").addColumn("20210406.2", "SOURCE_DIRECT_PARENT_PIDS").nullable().type(ColumnTypeEnum.CLOB);
		version.onTable("TRM_VALUESET_CONCEPT").addColumn("20210406.3", "SOURCE_PID").nullable().type(ColumnTypeEnum.LONG);

		// Bulk Import Job
		Builder.BuilderAddTableByColumns blkImportJobTable = version.addTableByColumns("20210410.1", "HFJ_BLK_IMPORT_JOB", "PID");
		blkImportJobTable.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		blkImportJobTable.addColumn("JOB_ID").nonNullable().type(ColumnTypeEnum.STRING, Search.UUID_COLUMN_LENGTH);
		blkImportJobTable.addColumn("JOB_STATUS").nonNullable().type(ColumnTypeEnum.STRING, 10);
		blkImportJobTable.addColumn("STATUS_TIME").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		blkImportJobTable.addColumn("STATUS_MESSAGE").nullable().type(ColumnTypeEnum.STRING, 500);
		blkImportJobTable.addColumn("JOB_DESC").nullable().type(ColumnTypeEnum.STRING, 500);
		blkImportJobTable.addColumn("OPTLOCK").nonNullable().type(ColumnTypeEnum.INT);
		blkImportJobTable.addColumn("FILE_COUNT").nonNullable().type(ColumnTypeEnum.INT);
		blkImportJobTable.addColumn("ROW_PROCESSING_MODE").nonNullable().type(ColumnTypeEnum.STRING, 20);
		blkImportJobTable.addColumn("BATCH_SIZE").nonNullable().type(ColumnTypeEnum.INT);
		blkImportJobTable.addIndex("20210410.2", "IDX_BLKIM_JOB_ID").unique(true).withColumns("JOB_ID");
		version.addIdGenerator("20210410.3", "SEQ_BLKIMJOB_PID");

		// Bulk Import Job File
		Builder.BuilderAddTableByColumns blkImportJobFileTable = version.addTableByColumns("20210410.4", "HFJ_BLK_IMPORT_JOBFILE", "PID");
		blkImportJobFileTable.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		blkImportJobFileTable.addColumn("JOB_PID").nonNullable().type(ColumnTypeEnum.LONG);
		blkImportJobFileTable.addColumn("JOB_CONTENTS").nonNullable().type(ColumnTypeEnum.BLOB);
		blkImportJobFileTable.addColumn("FILE_SEQ").nonNullable().type(ColumnTypeEnum.INT);
		blkImportJobFileTable.addColumn("TENANT_NAME").nullable().type(ColumnTypeEnum.STRING, 200);
		blkImportJobFileTable.addIndex("20210410.5", "IDX_BLKIM_JOBFILE_JOBID").unique(false).withColumns("JOB_PID");
		blkImportJobFileTable.addForeignKey("20210410.6", "FK_BLKIMJOBFILE_JOB").toColumn("JOB_PID").references("HFJ_BLK_IMPORT_JOB", "PID");
		version.addIdGenerator("20210410.7", "SEQ_BLKIMJOBFILE_PID");

		//Increase ResourceLink path length
		version.onTable("HFJ_RES_LINK").modifyColumn("20210505.1", "SRC_PATH").nonNullable().failureAllowed().withType(ColumnTypeEnum.STRING, 500);
	}

	private void init530() {
		Builder version = forVersion(VersionEnum.V5_3_0);

		//-- TRM
		version
			.onTable("TRM_VALUESET_CONCEPT")
			.dropIndex("20210104.1", "IDX_VS_CONCEPT_CS_CODE");

		version
			.onTable("TRM_VALUESET_CONCEPT")
			.addIndex("20210104.2", "IDX_VS_CONCEPT_CSCD").unique(true).withColumns("VALUESET_PID", "SYSTEM_URL", "CODEVAL");

		//-- Add new Table, HFJ_SPIDX_QUANTITY_NRML
		version.addIdGenerator("20210109.1", "SEQ_SPIDX_QUANTITY_NRML");
		Builder.BuilderAddTableByColumns pkg = version.addTableByColumns("20210109.2", "HFJ_SPIDX_QUANTITY_NRML", "SP_ID");
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
		pkg.addIndex("20210109.4", "IDX_SP_QNTY_NRML_HASH_UN").unique(false).withColumns("HASH_IDENTITY_AND_UNITS", "SP_VALUE");
		pkg.addIndex("20210109.5", "IDX_SP_QNTY_NRML_HASH_SYSUN").unique(false).withColumns("HASH_IDENTITY_SYS_UNITS", "SP_VALUE");
		pkg.addIndex("20210109.6", "IDX_SP_QNTY_NRML_UPDATED").unique(false).withColumns("SP_UPDATED");
		pkg.addIndex("20210109.7", "IDX_SP_QNTY_NRML_RESID").unique(false).withColumns("RES_ID");

		//-- Link to the resourceTable
		version.onTable("HFJ_RESOURCE").addColumn("20210109.10", "SP_QUANTITY_NRML_PRESENT").nullable().type(ColumnTypeEnum.BOOLEAN);

		//-- Fixed the partition and fk
		Builder.BuilderWithTableName nrmlTable = version.onTable("HFJ_SPIDX_QUANTITY_NRML");
		nrmlTable.addColumn("20210111.1", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		nrmlTable.addColumn("20210111.2", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		// - The fk name is generated from Hibernate, have to use this name here
		nrmlTable
			.addForeignKey("20210111.3", "FKRCJOVMUH5KC0O6FVBLE319PYV")
			.toColumn("RES_ID")
			.references("HFJ_RESOURCE", "RES_ID");

		Builder.BuilderWithTableName quantityTable = version.onTable("HFJ_SPIDX_QUANTITY");
		quantityTable.modifyColumn("20210116.1", "SP_VALUE").nullable().failureAllowed().withType(ColumnTypeEnum.DOUBLE);

		// HFJ_RES_LINK
		version.onTable("HFJ_RES_LINK")
			.addColumn("20210126.1", "TARGET_RESOURCE_VERSION").nullable().type(ColumnTypeEnum.LONG);

	}

	protected void init520() {
		Builder version = forVersion(VersionEnum.V5_2_0);

		Builder.BuilderWithTableName mdmLink = version.onTable("MPI_LINK");
		mdmLink.addColumn("20201029.1", "GOLDEN_RESOURCE_PID").nonNullable().type(ColumnTypeEnum.LONG);
		mdmLink.addColumn("20201029.2", "RULE_COUNT").nullable().type(ColumnTypeEnum.LONG);
		mdmLink
			.addForeignKey("20201029.3", "FK_EMPI_LINK_GOLDEN_RESOURCE")
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
		pkgVer.addForeignKey("20200610.6", "FK_NPM_PKV_PKG").toColumn("PACKAGE_PID").references("NPM_PACKAGE", "PID");
		pkgVer.addForeignKey("20200610.7", "FK_NPM_PKV_RESID").toColumn("BINARY_RES_ID").references("HFJ_RESOURCE", "RES_ID");
		pkgVer.addIndex("20200610.8", "IDX_PACKVER").unique(true).withColumns("PACKAGE_ID", "VERSION_ID");

		version.addIdGenerator("20200610.9", "SEQ_NPM_PACKVERRES");
		Builder.BuilderAddTableByColumns pkgVerResAdd = version.addTableByColumns("20200610.10", "NPM_PACKAGE_VER_RES", "PID");
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
		pkgVerResAdd.addForeignKey("20200610.11", "FK_NPM_PACKVERRES_PACKVER").toColumn("PACKVER_PID").references("NPM_PACKAGE_VER", "PID");
		pkgVerResAdd.addForeignKey("20200610.12", "FK_NPM_PKVR_RESID").toColumn("BINARY_RES_ID").references("HFJ_RESOURCE", "RES_ID");
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

		//EMPI Target Type
		empiLink.addColumn("20200727.1", "TARGET_TYPE").nullable().type(ColumnTypeEnum.STRING, 40);

		//ConceptMap add version for search
		Builder.BuilderWithTableName trmConceptMap = version.onTable("TRM_CONCEPT_MAP");
		trmConceptMap.addColumn("20200910.1", "VER").nullable().type(ColumnTypeEnum.STRING, 200);
		trmConceptMap.dropIndex("20200910.2", "IDX_CONCEPT_MAP_URL");
		trmConceptMap.addIndex("20200910.3", "IDX_CONCEPT_MAP_URL").unique(true).withColumns("URL", "VER");

		//Term CodeSystem Version and Term ValueSet Version
		Builder.BuilderWithTableName trmCodeSystemVer = version.onTable("TRM_CODESYSTEM_VER");
		trmCodeSystemVer.addIndex("20200923.1", "IDX_CODESYSTEM_AND_VER").unique(true).withColumns("CODESYSTEM_PID", "CS_VERSION_ID");
		Builder.BuilderWithTableName trmValueSet = version.onTable("TRM_VALUESET");
		trmValueSet.addColumn("20200923.2", "VER").nullable().type(ColumnTypeEnum.STRING, 200);
		trmValueSet.dropIndex("20200923.3", "IDX_VALUESET_URL");
		trmValueSet.addIndex("20200923.4", "IDX_VALUESET_URL").unique(true).withColumns("URL", "VER");

		//Term ValueSet Component add system version
		Builder.BuilderWithTableName trmValueSetComp = version.onTable("TRM_VALUESET_CONCEPT");
		trmValueSetComp.addColumn("20201028.1", "SYSTEM_VER").nullable().type(ColumnTypeEnum.STRING, 200);
		trmValueSetComp.dropIndex("20201028.2", "IDX_VS_CONCEPT_CS_CD");
		trmValueSetComp.addIndex("20201028.3", "IDX_VS_CONCEPT_CS_CODE").unique(true).withColumns("VALUESET_PID", "SYSTEM_URL", "SYSTEM_VER", "CODEVAL").doNothing();
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

	private void init501() { //20200514 - present
		Builder version = forVersion(VersionEnum.V5_0_1);

		Builder.BuilderWithTableName spidxDate = version.onTable("HFJ_SPIDX_DATE");
		spidxDate.addIndex("20200514.1", "IDX_SP_DATE_HASH_LOW").unique(false).withColumns("HASH_IDENTITY", "SP_VALUE_LOW")
			.doNothing();
		spidxDate.addIndex("20200514.2", "IDX_SP_DATE_ORD_HASH").unique(false).withColumns("HASH_IDENTITY", "SP_VALUE_LOW_DATE_ORDINAL", "SP_VALUE_HIGH_DATE_ORDINAL")
			.doNothing();
		spidxDate.addIndex("20200514.3", "IDX_SP_DATE_ORD_HASH_LOW").unique(false).withColumns("HASH_IDENTITY", "SP_VALUE_LOW_DATE_ORDINAL")
			.doNothing();

		// MPI_LINK
		version.addIdGenerator("20200517.1", "SEQ_EMPI_LINK_ID");
		Builder.BuilderAddTableByColumns empiLink = version.addTableByColumns("20200517.2", "MPI_LINK", "PID");
		empiLink.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);

		empiLink.addColumn("PERSON_PID").nonNullable().type(ColumnTypeEnum.LONG);
		empiLink
			.addForeignKey("20200517.3", "FK_EMPI_LINK_PERSON")
			.toColumn("PERSON_PID")
			.references("HFJ_RESOURCE", "RES_ID");

		empiLink.addColumn("TARGET_PID").nonNullable().type(ColumnTypeEnum.LONG);
		empiLink
			.addForeignKey("20200517.4", "FK_EMPI_LINK_TARGET")
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
		version.onTable("HFJ_RES_VER").addForeignKey("20200218.3", "FK_RESOURCE_HISTORY_RESOURCE").toColumn("RES_ID").references("HFJ_RESOURCE", "RES_ID");
		version.onTable("HFJ_RES_VER").modifyColumn("20200220.1", "RES_ID").nonNullable().failureAllowed().withType(ColumnTypeEnum.LONG);
		//

		// Drop unused column
		version.onTable("HFJ_RESOURCE").dropIndex("20200419.1", "IDX_RES_PROFILE");
		version.onTable("HFJ_RESOURCE").dropColumn("20200419.2", "RES_PROFILE").failureAllowed();

		// Add Partitioning
		Builder.BuilderAddTableByColumns partition = version.addTableByColumns("20200420.0", "HFJ_PARTITION", "PART_ID");
		partition.addColumn("PART_ID").nonNullable().type(ColumnTypeEnum.INT);
		partition.addColumn("PART_NAME").nonNullable().type(ColumnTypeEnum.STRING, 200);
		partition.addColumn("PART_DESC").nullable().type(ColumnTypeEnum.STRING, 200);
		partition.addIndex("20200420.1", "IDX_PART_NAME").unique(true).withColumns("PART_NAME");

		// Partition columns on individual tables
		version.onTable("HFJ_RESOURCE").addColumn("20200420.2", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_RESOURCE").addColumn("20200420.3", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_RES_VER").addColumn("20200420.4", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_RES_VER").addColumn("20200420.5", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_IDX_CMP_STRING_UNIQ").addColumn("20200420.6", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_IDX_CMP_STRING_UNIQ").addColumn("20200420.7", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_IDX_CMP_STRING_UNIQ").addColumn("20200420.8", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_IDX_CMP_STRING_UNIQ").addColumn("20200420.9", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_HISTORY_TAG").addColumn("20200420.10", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_HISTORY_TAG").addColumn("20200420.11", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_RES_TAG").addColumn("20200420.12", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_RES_TAG").addColumn("20200420.13", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_FORCED_ID").addColumn("20200420.14", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_FORCED_ID").addColumn("20200420.15", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_RES_LINK").addColumn("20200420.16", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_RES_LINK").addColumn("20200420.17", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_SPIDX_STRING").addColumn("20200420.18", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_SPIDX_STRING").addColumn("20200420.19", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_SPIDX_COORDS").addColumn("20200420.20", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_SPIDX_COORDS").addColumn("20200420.21", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_SPIDX_NUMBER").addColumn("20200420.22", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_SPIDX_NUMBER").addColumn("20200420.23", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_SPIDX_TOKEN").addColumn("20200420.24", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_SPIDX_TOKEN").addColumn("20200420.25", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_SPIDX_DATE").addColumn("20200420.26", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_SPIDX_DATE").addColumn("20200420.27", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_SPIDX_URI").addColumn("20200420.28", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_SPIDX_URI").addColumn("20200420.29", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_SPIDX_QUANTITY").addColumn("20200420.30", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_SPIDX_QUANTITY").addColumn("20200420.31", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_RES_VER_PROV").addColumn("20200420.32", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_RES_VER_PROV").addColumn("20200420.33", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);
		version.onTable("HFJ_RES_PARAM_PRESENT").addColumn("20200420.34", "PARTITION_ID").nullable().type(ColumnTypeEnum.INT);
		version.onTable("HFJ_RES_PARAM_PRESENT").addColumn("20200420.35", "PARTITION_DATE").nullable().type(ColumnTypeEnum.DATE_ONLY);

		version.onTable("HFJ_SPIDX_STRING").modifyColumn("20200420.36", "SP_MISSING").nonNullable().failureAllowed().withType(ColumnTypeEnum.BOOLEAN);
		version.onTable("HFJ_SPIDX_COORDS").modifyColumn("20200420.37", "SP_MISSING").nonNullable().failureAllowed().withType(ColumnTypeEnum.BOOLEAN);
		version.onTable("HFJ_SPIDX_NUMBER").modifyColumn("20200420.38", "SP_MISSING").nonNullable().failureAllowed().withType(ColumnTypeEnum.BOOLEAN);
		version.onTable("HFJ_SPIDX_TOKEN").modifyColumn("20200420.39", "SP_MISSING").nonNullable().failureAllowed().withType(ColumnTypeEnum.BOOLEAN);
		version.onTable("HFJ_SPIDX_DATE").modifyColumn("20200420.40", "SP_MISSING").nonNullable().failureAllowed().withType(ColumnTypeEnum.BOOLEAN);
		version.onTable("HFJ_SPIDX_URI").modifyColumn("20200420.41", "SP_MISSING").nonNullable().failureAllowed().withType(ColumnTypeEnum.BOOLEAN);
		version.onTable("HFJ_SPIDX_QUANTITY").modifyColumn("20200420.42", "SP_MISSING").nonNullable().failureAllowed().withType(ColumnTypeEnum.BOOLEAN);

		// Add support for integer comparisons during day-precision date search.
		Builder.BuilderWithTableName spidxDate = version.onTable("HFJ_SPIDX_DATE");
		spidxDate.addColumn("20200501.1", "SP_VALUE_LOW_DATE_ORDINAL").nullable().type(ColumnTypeEnum.INT);
		spidxDate.addColumn("20200501.2", "SP_VALUE_HIGH_DATE_ORDINAL").nullable().type(ColumnTypeEnum.INT);

		spidxDate.addTask(new CalculateOrdinalDatesTask(VersionEnum.V5_0_0, "20200501.3")
			.addCalculator("SP_VALUE_LOW_DATE_ORDINAL", t -> ResourceIndexedSearchParamDate.calculateOrdinalValue(t.getDate("SP_VALUE_LOW")))
			.addCalculator("SP_VALUE_HIGH_DATE_ORDINAL", t -> ResourceIndexedSearchParamDate.calculateOrdinalValue(t.getDate("SP_VALUE_HIGH")))
			.setColumnName("SP_VALUE_LOW_DATE_ORDINAL") //It doesn't matter which of the two we choose as they will both be null.
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
		version.onTable("TRM_VALUESET_C_DESIGNATION").dropIndex("20200202.1", "IDX_VALUESET_C_DSGNTN_VAL").failureAllowed();
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
		version.onTable("HFJ_SPIDX_NUMBER").modifyColumn("20190920.1", "RES_ID").nonNullable().failureAllowed().withType(ColumnTypeEnum.LONG);
		version.onTable("HFJ_SPIDX_COORDS").modifyColumn("20190920.2", "RES_ID").nonNullable().failureAllowed().withType(ColumnTypeEnum.LONG);
		version.onTable("HFJ_SPIDX_TOKEN").modifyColumn("20190920.3", "RES_ID").nonNullable().failureAllowed().withType(ColumnTypeEnum.LONG);
		version.onTable("HFJ_SPIDX_STRING").modifyColumn("20190920.4", "RES_ID").nonNullable().failureAllowed().withType(ColumnTypeEnum.LONG);
		version.onTable("HFJ_SPIDX_DATE").modifyColumn("20190920.5", "RES_ID").nonNullable().failureAllowed().withType(ColumnTypeEnum.LONG);
		version.onTable("HFJ_SPIDX_QUANTITY").modifyColumn("20190920.6", "RES_ID").nonNullable().failureAllowed().withType(ColumnTypeEnum.LONG);
		version.onTable("HFJ_SPIDX_URI").modifyColumn("20190920.7", "RES_ID").nonNullable().failureAllowed().withType(ColumnTypeEnum.LONG);

		// HFJ_SEARCH
		version.onTable("HFJ_SEARCH").addColumn("20190921.1", "EXPIRY_OR_NULL").nullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		version.onTable("HFJ_SEARCH").addColumn("20190921.2", "NUM_BLOCKED").nullable().type(ColumnTypeEnum.INT);

		// HFJ_BLK_EXPORT_JOB
		version.addIdGenerator("20190921.3", "SEQ_BLKEXJOB_PID");
		Builder.BuilderAddTableByColumns bulkExportJob = version.addTableByColumns("20190921.4", "HFJ_BLK_EXPORT_JOB", "PID");
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
		Builder.BuilderAddTableByColumns bulkExportCollection = version.addTableByColumns("20190921.8", "HFJ_BLK_EXPORT_COLLECTION", "PID");
		bulkExportCollection.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		bulkExportCollection.addColumn("JOB_PID").nonNullable().type(ColumnTypeEnum.LONG);
		bulkExportCollection.addForeignKey("20190921.9", "FK_BLKEXCOL_JOB").toColumn("JOB_PID").references("HFJ_BLK_EXPORT_JOB", "PID");
		bulkExportCollection.addColumn("RES_TYPE").nonNullable().type(ColumnTypeEnum.STRING, 40);
		bulkExportCollection.addColumn("TYPE_FILTER").nullable().type(ColumnTypeEnum.STRING, 1000);
		bulkExportCollection.addColumn("OPTLOCK").nonNullable().type(ColumnTypeEnum.INT);

		// HFJ_BLK_EXPORT_COLFILE
		version.addIdGenerator("20190921.10", "SEQ_BLKEXCOLFILE_PID");
		Builder.BuilderAddTableByColumns bulkExportCollectionFile = version.addTableByColumns("20190921.11", "HFJ_BLK_EXPORT_COLFILE", "PID");
		bulkExportCollectionFile.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		bulkExportCollectionFile.addColumn("COLLECTION_PID").nonNullable().type(ColumnTypeEnum.LONG);
		bulkExportCollectionFile.addColumn("RES_ID").nonNullable().type(ColumnTypeEnum.STRING, 100);
		bulkExportCollectionFile.addForeignKey("20190921.12", "FK_BLKEXCOLFILE_COLLECT").toColumn("COLLECTION_PID").references("HFJ_BLK_EXPORT_COLLECTION", "PID");

		// HFJ_RES_VER_PROV
		version.startSectionWithMessage("Processing bulkExportCollectionFile: HFJ_RES_VER_PROV");
		Builder.BuilderAddTableByColumns resVerProv = version.addTableByColumns("20190921.13", "HFJ_RES_VER_PROV", "RES_VER_PID");
		resVerProv.addColumn("RES_VER_PID").nonNullable().type(ColumnTypeEnum.LONG);
		resVerProv
			.addForeignKey("20190921.14", "FK_RESVERPROV_RESVER_PID")
			.toColumn("RES_VER_PID")
			.references("HFJ_RES_VER", "PID");
		resVerProv.addColumn("RES_PID").nonNullable().type(ColumnTypeEnum.LONG);
		resVerProv
			.addForeignKey("20190921.15", "FK_RESVERPROV_RES_PID")
			.toColumn("RES_PID")
			.references("HFJ_RESOURCE", "RES_ID");
		resVerProv.addColumn("SOURCE_URI").nullable().type(ColumnTypeEnum.STRING, 100);
		resVerProv.addColumn("REQUEST_ID").nullable().type(ColumnTypeEnum.STRING, 16);
		resVerProv.addIndex("20190921.16", "IDX_RESVERPROV_SOURCEURI").unique(false).withColumns("SOURCE_URI");
		resVerProv.addIndex("20190921.17", "IDX_RESVERPROV_REQUESTID").unique(false).withColumns("REQUEST_ID");

		// TermValueSetConceptDesignation
		version.startSectionWithMessage("Processing bulkExportCollectionFile: TRM_VALUESET_C_DESIGNATION");
		Builder.BuilderWithTableName termValueSetConceptDesignationTable = version.onTable("TRM_VALUESET_C_DESIGNATION");
		termValueSetConceptDesignationTable.addColumn("20190921.18", "VALUESET_PID").nonNullable().type(ColumnTypeEnum.LONG);
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
		termValueSetTable.addColumn("20190921.22", "TOTAL_CONCEPTS").nonNullable().type(ColumnTypeEnum.LONG);
		termValueSetTable.addColumn("20190921.23", "TOTAL_CONCEPT_DESIGNATIONS").nonNullable().type(ColumnTypeEnum.LONG);
		termValueSetTable
			.dropIndex("20190921.24", "IDX_VALUESET_EXP_STATUS");

		version.dropIdGenerator("20190921.25", "SEQ_SEARCHPARM_ID");

		// TermValueSetConcept
		version.startSectionWithMessage("Processing bulkExportCollectionFile: TRM_VALUESET_CONCEPT");
		Builder.BuilderWithTableName termValueSetConceptTable = version.onTable("TRM_VALUESET_CONCEPT");
		termValueSetConceptTable.addColumn("20190921.26", "VALUESET_ORDER").nonNullable().type(ColumnTypeEnum.INT);
		termValueSetConceptTable
			.addIndex("20190921.27", "IDX_VS_CONCEPT_ORDER")
			.unique(true)
			.withColumns("VALUESET_PID", "VALUESET_ORDER");

		// Account for RESTYPE_LEN column increasing from 30 to 40
		version.onTable("HFJ_RESOURCE").modifyColumn("20191002.1", "RES_TYPE").nonNullable().failureAllowed().withType(ColumnTypeEnum.STRING, 40);
		version.onTable("HFJ_RES_VER").modifyColumn("20191002.2", "RES_TYPE").nonNullable().failureAllowed().withType(ColumnTypeEnum.STRING, 40);
		version.onTable("HFJ_HISTORY_TAG").modifyColumn("20191002.3", "RES_TYPE").nonNullable().failureAllowed().withType(ColumnTypeEnum.STRING, 40);
		version.onTable("HFJ_RES_LINK").modifyColumn("20191002.4", "SOURCE_RESOURCE_TYPE").nonNullable().failureAllowed().withType(ColumnTypeEnum.STRING, 40);
		version.onTable("HFJ_RES_LINK").modifyColumn("20191002.5", "TARGET_RESOURCE_TYPE").nonNullable().failureAllowed().withType(ColumnTypeEnum.STRING, 40);
		version.onTable("HFJ_RES_TAG").modifyColumn("20191002.6", "RES_TYPE").nonNullable().failureAllowed().withType(ColumnTypeEnum.STRING, 40);

		// TermConceptDesignation
		version.startSectionWithMessage("Processing table: TRM_CONCEPT_DESIG");
		version.onTable("TRM_CONCEPT_DESIG").modifyColumn("20191002.7", "VAL").nonNullable().withType(ColumnTypeEnum.STRING, 2000);

		// TermValueSetConceptDesignation
		version.startSectionWithMessage("Processing table: TRM_VALUESET_C_DESIGNATION");
		version.onTable("TRM_VALUESET_C_DESIGNATION").modifyColumn("20191002.8", "VAL").nonNullable().withType(ColumnTypeEnum.STRING, 2000);

		// TermConceptProperty
		version.startSectionWithMessage("Processing table: TRM_CONCEPT_PROPERTY");
		version.onTable("TRM_CONCEPT_PROPERTY").addColumn("20191002.9", "PROP_VAL_LOB").nullable().type(ColumnTypeEnum.BLOB);
	}

	protected void init400() { // 20190401 - 20190814
		Builder version = forVersion(VersionEnum.V4_0_0);

		// BinaryStorageEntity
		Builder.BuilderAddTableByColumns binaryBlob = version.addTableByColumns("20190722.1", "HFJ_BINARY_STORAGE_BLOB", "BLOB_ID");
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
			.modifyColumn("20190722.6", "CONCEPT_MAP_URL").nullable().withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GROUP")
			.modifyColumn("20190722.7", "SOURCE_VERSION").nullable().withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GROUP")
			.modifyColumn("20190722.8", "SOURCE_VS").nullable().withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GROUP")
			.modifyColumn("20190722.9", "TARGET_VERSION").nullable().withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GROUP")
			.modifyColumn("20190722.10", "TARGET_VS").nullable().withType(ColumnTypeEnum.STRING, 200);

		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
			.renameColumn("20190722.11", "myConceptMapUrl", "CONCEPT_MAP_URL", false, true)
			.renameColumn("20190722.12", "mySystem", "SYSTEM_URL", false, true)
			.renameColumn("20190722.13", "mySystemVersion", "SYSTEM_VERSION", false, true)
			.renameColumn("20190722.14", "myValueSet", "VALUESET_URL", false, true);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
			.modifyColumn("20190722.15", "CONCEPT_MAP_URL").nullable().withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
			.modifyColumn("20190722.16", "SOURCE_CODE").nonNullable().withType(ColumnTypeEnum.STRING, 500);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
			.modifyColumn("20190722.17", "SYSTEM_URL").nullable().withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
			.modifyColumn("20190722.18", "SYSTEM_VERSION").nullable().withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
			.modifyColumn("20190722.19", "VALUESET_URL").nullable().withType(ColumnTypeEnum.STRING, 200);

		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
			.renameColumn("20190722.20", "myConceptMapUrl", "CONCEPT_MAP_URL", false, true)
			.renameColumn("20190722.21", "mySystem", "SYSTEM_URL", false, true)
			.renameColumn("20190722.22", "mySystemVersion", "SYSTEM_VERSION", false, true)
			.renameColumn("20190722.23", "myValueSet", "VALUESET_URL", false, true);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
			.modifyColumn("20190722.24", "CONCEPT_MAP_URL").nullable().withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
			.modifyColumn("20190722.25", "SYSTEM_URL").nullable().withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
			.modifyColumn("20190722.26", "SYSTEM_VERSION").nullable().withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
			.modifyColumn("20190722.27", "TARGET_CODE").nonNullable().withType(ColumnTypeEnum.STRING, 500);
		version.onTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
			.modifyColumn("20190722.28", "VALUESET_URL").nullable().withType(ColumnTypeEnum.STRING, 200);

		version.onTable("TRM_CONCEPT")
			.renameColumn("20190722.29", "CODE", "CODEVAL", false, true);


		// TermValueSet
		version.startSectionWithMessage("Processing table: TRM_VALUESET");
		version.addIdGenerator("20190722.30", "SEQ_VALUESET_PID");
		Builder.BuilderAddTableByColumns termValueSetTable = version.addTableByColumns("20190722.31", "TRM_VALUESET", "PID");
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

		version.onTable("TRM_VALUESET")
			.renameColumn("20190722.34", "NAME", "VSNAME", true, true);
		version.onTable("TRM_VALUESET")
			.modifyColumn("20190722.35", "RES_ID").nullable().withType(ColumnTypeEnum.LONG);

		Builder.BuilderWithTableName termValueSetTableChange = version.onTable("TRM_VALUESET");
		termValueSetTableChange.addColumn("20190722.36", "EXPANSION_STATUS").nonNullable().type(ColumnTypeEnum.STRING, 50);
		termValueSetTableChange
			.addIndex("20190722.37", "IDX_VALUESET_EXP_STATUS")
			.unique(false)
			.withColumns("EXPANSION_STATUS");

		// TermValueSetConcept
		version.startSectionWithMessage("Processing table: TRM_VALUESET_CONCEPT");
		version.addIdGenerator("20190722.38", "SEQ_VALUESET_CONCEPT_PID");
		Builder.BuilderAddTableByColumns termValueSetConceptTable = version.addTableByColumns("20190722.39", "TRM_VALUESET_CONCEPT", "PID");
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
			.withColumns("VALUESET_PID", "SYSTEM_URL", "CODEVAL").failureAllowed();

		// TermValueSetConceptDesignation
		version.startSectionWithMessage("Processing table: TRM_VALUESET_C_DESIGNATION");
		version.addIdGenerator("20190801.3", "SEQ_VALUESET_C_DSGNTN_PID");
		Builder.BuilderAddTableByColumns termValueSetConceptDesignationTable = version.addTableByColumns("20190801.4", "TRM_VALUESET_C_DESIGNATION", "PID");
		termValueSetConceptDesignationTable.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		termValueSetConceptDesignationTable.addColumn("VALUESET_CONCEPT_PID").nonNullable().type(ColumnTypeEnum.LONG);
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
		termCodeSystemVersionTable.addColumn("20190814.1", "CS_DISPLAY").nullable().type(ColumnTypeEnum.STRING, 200);

		// ResourceReindexJobEntry
		version.addIdGenerator("20190814.2", "SEQ_RES_REINDEX_JOB");
		Builder.BuilderAddTableByColumns reindex = version.addTableByColumns("20190814.3", "HFJ_RES_REINDEX_JOB", "PID");
		reindex.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		reindex.addColumn("RES_TYPE").nullable().type(ColumnTypeEnum.STRING, 100);
		reindex.addColumn("UPDATE_THRESHOLD_HIGH").nonNullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		reindex.addColumn("JOB_DELETED").nonNullable().type(ColumnTypeEnum.BOOLEAN);
		reindex.addColumn("UPDATE_THRESHOLD_LOW").nullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		reindex.addColumn("SUSPENDED_UNTIL").nullable().type(ColumnTypeEnum.DATE_TIMESTAMP);
		reindex.addColumn("REINDEX_COUNT").nullable().type(ColumnTypeEnum.INT);

		// Search
		version.onTable("HFJ_SEARCH")
			.addColumn("20190814.4", "SEARCH_DELETED").nullable().type(ColumnTypeEnum.BOOLEAN);
		version.onTable("HFJ_SEARCH")
			.modifyColumn("20190814.5", "SEARCH_LAST_RETURNED").nonNullable().withType(ColumnTypeEnum.DATE_TIMESTAMP);
		version.onTable("HFJ_SEARCH")
			.addColumn("20190814.6", "SEARCH_PARAM_MAP").nullable().type(ColumnTypeEnum.BLOB);
		version.onTable("HFJ_SEARCH")
			.modifyColumn("20190814.7", "SEARCH_UUID").nonNullable().withType(ColumnTypeEnum.STRING, 36);

		version.onTable("HFJ_SEARCH_PARM").dropThisTable("20190814.8");

		// Make some columns non-nullable that were previously nullable - These are marked as failure allowed, since
		// SQL Server won't let us change nullability on columns with indexes pointing to them
		version.onTable("HFJ_SPIDX_COORDS").modifyColumn("20190814.9", "RES_TYPE").nonNullable().failureAllowed().withType(ColumnTypeEnum.STRING, 100);
		version.onTable("HFJ_SPIDX_DATE").modifyColumn("20190814.10", "RES_TYPE").nonNullable().failureAllowed().withType(ColumnTypeEnum.STRING, 100);
		version.onTable("HFJ_SPIDX_STRING").modifyColumn("20190814.11", "RES_TYPE").nonNullable().failureAllowed().withType(ColumnTypeEnum.STRING, 100);
		version.onTable("HFJ_SPIDX_STRING").addColumn("20190814.12", "HASH_IDENTITY").nullable().type(ColumnTypeEnum.LONG);
		version.onTable("HFJ_SPIDX_STRING").addIndex("20190814.13", "IDX_SP_STRING_HASH_IDENT").unique(false).withColumns("HASH_IDENTITY");
		version.onTable("HFJ_SPIDX_COORDS").modifyColumn("20190814.14", "RES_TYPE").nonNullable().failureAllowed().withType(ColumnTypeEnum.STRING, 100);
		version.onTable("HFJ_SPIDX_QUANTITY").modifyColumn("20190814.15", "RES_TYPE").nonNullable().failureAllowed().withType(ColumnTypeEnum.STRING, 100);
		version.onTable("HFJ_SPIDX_QUANTITY").dropColumn("20190814.16", "HASH_UNITS_AND_VALPREFIX");
		version.onTable("HFJ_SPIDX_QUANTITY").dropColumn("20190814.17", "HASH_VALPREFIX");
		version.onTable("HFJ_SPIDX_NUMBER").modifyColumn("20190814.18", "RES_TYPE").nonNullable().failureAllowed().withType(ColumnTypeEnum.STRING, 100);
		version.onTable("HFJ_SPIDX_TOKEN").modifyColumn("20190814.19", "RES_TYPE").nonNullable().failureAllowed().withType(ColumnTypeEnum.STRING, 100);
		version.onTable("HFJ_SPIDX_URI").modifyColumn("20190814.20", "RES_TYPE").nonNullable().failureAllowed().withType(ColumnTypeEnum.STRING, 100);
		version.onTable("HFJ_SPIDX_URI").modifyColumn("20190814.21", "SP_URI").nullable().failureAllowed().withType(ColumnTypeEnum.STRING, 254);
		version.onTable("TRM_CODESYSTEM").modifyColumn("20190814.22", "CODE_SYSTEM_URI").nonNullable().failureAllowed().withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CODESYSTEM").modifyColumn("20190814.23", "CS_NAME").nullable().failureAllowed().withType(ColumnTypeEnum.STRING, 200);
		version.onTable("TRM_CODESYSTEM_VER").modifyColumn("20190814.24", "CS_VERSION_ID").nullable().failureAllowed().withType(ColumnTypeEnum.STRING, 200);
	}


	private void init360() { // 20180918 - 20181112
		Builder version = forVersion(VersionEnum.V3_6_0);

		// Resource Link
		Builder.BuilderWithTableName resourceLink = version.onTable("HFJ_RES_LINK");
		version.startSectionWithMessage("Starting work on table: " + resourceLink.getTableName());
		resourceLink
			.modifyColumn("20180929.1", "SRC_PATH")
			.nonNullable()
			.withType(ColumnTypeEnum.STRING, 200);

		// Search
		Builder.BuilderWithTableName search = version.onTable("HFJ_SEARCH");
		version.startSectionWithMessage("Starting work on table: " + search.getTableName());
		search
			.addColumn("20181001.1", "OPTLOCK_VERSION")
			.nullable()
			.type(ColumnTypeEnum.INT);

		version.addTableRawSql("20181104.1", "HFJ_RES_REINDEX_JOB")
			.addSql(DriverTypeEnum.MSSQL_2012, "create table HFJ_RES_REINDEX_JOB (PID bigint not null, JOB_DELETED bit not null, RES_TYPE varchar(255), SUSPENDED_UNTIL datetime2, UPDATE_THRESHOLD_HIGH datetime2 not null, UPDATE_THRESHOLD_LOW datetime2, primary key (PID))")
			.addSql(DriverTypeEnum.DERBY_EMBEDDED, "create table HFJ_RES_REINDEX_JOB (PID bigint not null, JOB_DELETED boolean not null, RES_TYPE varchar(255), SUSPENDED_UNTIL timestamp, UPDATE_THRESHOLD_HIGH timestamp not null, UPDATE_THRESHOLD_LOW timestamp, primary key (PID))")
			.addSql(DriverTypeEnum.MARIADB_10_1, "create table HFJ_RES_REINDEX_JOB (PID bigint not null, JOB_DELETED bit not null, RES_TYPE varchar(255), SUSPENDED_UNTIL datetime(6), UPDATE_THRESHOLD_HIGH datetime(6) not null, UPDATE_THRESHOLD_LOW datetime(6), primary key (PID))")
			.addSql(DriverTypeEnum.POSTGRES_9_4, "create table HFJ_RES_REINDEX_JOB (PID int8 not null, JOB_DELETED boolean not null, RES_TYPE varchar(255), SUSPENDED_UNTIL timestamp, UPDATE_THRESHOLD_HIGH timestamp not null, UPDATE_THRESHOLD_LOW timestamp, primary key (PID))")
			.addSql(DriverTypeEnum.MYSQL_5_7, " create table HFJ_RES_REINDEX_JOB (PID bigint not null, JOB_DELETED bit not null, RES_TYPE varchar(255), SUSPENDED_UNTIL datetime(6), UPDATE_THRESHOLD_HIGH datetime(6) not null, UPDATE_THRESHOLD_LOW datetime(6), primary key (PID))")
			.addSql(DriverTypeEnum.ORACLE_12C, "create table HFJ_RES_REINDEX_JOB (PID number(19,0) not null, JOB_DELETED number(1,0) not null, RES_TYPE varchar2(255 char), SUSPENDED_UNTIL timestamp, UPDATE_THRESHOLD_HIGH timestamp not null, UPDATE_THRESHOLD_LOW timestamp, primary key (PID))");

		version.onTable("TRM_CONCEPT_DESIG").addColumn("20181104.2", "CS_VER_PID").nullable().type(ColumnTypeEnum.LONG);
		version.onTable("TRM_CONCEPT_DESIG").addForeignKey("20181104.3", "FK_CONCEPTDESIG_CSV").toColumn("CS_VER_PID").references("TRM_CODESYSTEM_VER", "PID");

		version.onTable("TRM_CONCEPT_PROPERTY").addColumn("20181104.4", "CS_VER_PID").nullable().type(ColumnTypeEnum.LONG);
		version.onTable("TRM_CONCEPT_PROPERTY").addForeignKey("20181104.5", "FK_CONCEPTPROP_CSV").toColumn("CS_VER_PID").references("TRM_CODESYSTEM_VER", "PID");

		version.onTable("TRM_CONCEPT").addColumn("20181104.6", "PARENT_PIDS").nullable().type(ColumnTypeEnum.CLOB);

	}

	private void init350() { // 20180601 - 20180917
		Builder version = forVersion(VersionEnum.V3_5_0);

		// Forced ID changes
		Builder.BuilderWithTableName forcedId = version.onTable("HFJ_FORCED_ID");
		version.startSectionWithMessage("Starting work on table: " + forcedId.getTableName());

		forcedId
			.dropIndex("20180827.1", "IDX_FORCEDID_TYPE_FORCEDID");
		forcedId
			.dropIndex("20180827.2", "IDX_FORCEDID_TYPE_RESID");

		forcedId
			.addIndex("20180827.3", "IDX_FORCEDID_TYPE_FID")
			.unique(true)
			.withColumns("RESOURCE_TYPE", "FORCED_ID");

		// Indexes - Coords
		Builder.BuilderWithTableName spidxCoords = version.onTable("HFJ_SPIDX_COORDS");
		version.startSectionWithMessage("Starting work on table: " + spidxCoords.getTableName());
		spidxCoords
			.addColumn("20180903.1", "HASH_IDENTITY")
			.nullable()
			.type(ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxCoords
				.dropIndex("20180903.2", "IDX_SP_COORDS");
			spidxCoords
				.addIndex("20180903.4", "IDX_SP_COORDS_HASH")
				.unique(false)
				.withColumns("HASH_IDENTITY", "SP_LATITUDE", "SP_LONGITUDE");
			spidxCoords
				.addTask(new CalculateHashesTask(VersionEnum.V3_5_0, "20180903.5")
					.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getString("SP_NAME")))
					.setColumnName("HASH_IDENTITY")
				);
		}

		// Indexes - Date
		Builder.BuilderWithTableName spidxDate = version.onTable("HFJ_SPIDX_DATE");
		version.startSectionWithMessage("Starting work on table: " + spidxDate.getTableName());
		spidxDate
			.addColumn("20180903.6", "HASH_IDENTITY")
			.nullable()
			.type(ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxDate
				.dropIndex("20180903.7", "IDX_SP_TOKEN");
			spidxDate
				.addIndex("20180903.8", "IDX_SP_DATE_HASH")
				.unique(false)
				.withColumns("HASH_IDENTITY", "SP_VALUE_LOW", "SP_VALUE_HIGH")
				.doNothing();
			spidxDate
				.dropIndex("20180903.9", "IDX_SP_DATE");
			spidxDate
				.addTask(new CalculateHashesTask(VersionEnum.V3_5_0, "20180903.10")
					.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getString("SP_NAME")))
					.setColumnName("HASH_IDENTITY")
				);
		}

		// Indexes - Number
		Builder.BuilderWithTableName spidxNumber = version.onTable("HFJ_SPIDX_NUMBER");
		version.startSectionWithMessage("Starting work on table: " + spidxNumber.getTableName());
		spidxNumber
			.addColumn("20180903.11", "HASH_IDENTITY")
			.nullable()
			.type(ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxNumber
				.dropIndex("20180903.12", "IDX_SP_NUMBER");
			spidxNumber
				.addIndex("20180903.13", "IDX_SP_NUMBER_HASH_VAL")
				.unique(false)
				.withColumns("HASH_IDENTITY", "SP_VALUE")
				.doNothing();
			spidxNumber
				.addTask(new CalculateHashesTask(VersionEnum.V3_5_0, "20180903.14")
					.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getString("SP_NAME")))
					.setColumnName("HASH_IDENTITY")
				);
		}

		// Indexes - Quantity
		Builder.BuilderWithTableName spidxQuantity = version.onTable("HFJ_SPIDX_QUANTITY");
		version.startSectionWithMessage("Starting work on table: " + spidxQuantity.getTableName());
		spidxQuantity
			.addColumn("20180903.15", "HASH_IDENTITY")
			.nullable()
			.type(ColumnTypeEnum.LONG);
		spidxQuantity
			.addColumn("20180903.16", "HASH_IDENTITY_SYS_UNITS")
			.nullable()
			.type(ColumnTypeEnum.LONG);
		spidxQuantity
			.addColumn("20180903.17", "HASH_IDENTITY_AND_UNITS")
			.nullable()
			.type(ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxQuantity
				.dropIndex("20180903.18", "IDX_SP_QUANTITY");
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
			spidxQuantity
				.addTask(new CalculateHashesTask(VersionEnum.V3_5_0, "20180903.22")
					.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getString("SP_NAME")))
					.addCalculator("HASH_IDENTITY_AND_UNITS", t -> ResourceIndexedSearchParamQuantity.calculateHashUnits(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getString("SP_NAME"), t.getString("SP_UNITS")))
					.addCalculator("HASH_IDENTITY_SYS_UNITS", t -> ResourceIndexedSearchParamQuantity.calculateHashSystemAndUnits(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getString("SP_NAME"), t.getString("SP_SYSTEM"), t.getString("SP_UNITS")))
					.setColumnName("HASH_IDENTITY")
				);
		}

		// Indexes - String
		Builder.BuilderWithTableName spidxString = version.onTable("HFJ_SPIDX_STRING");
		version.startSectionWithMessage("Starting work on table: " + spidxString.getTableName());
		spidxString
			.addColumn("20180903.23", "HASH_NORM_PREFIX")
			.nullable()
			.type(ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxString
				.dropIndex("20180903.24", "IDX_SP_STRING");
			spidxString
				.addIndex("20180903.25", "IDX_SP_STRING_HASH_NRM")
				.unique(false)
				.withColumns("HASH_NORM_PREFIX", "SP_VALUE_NORMALIZED");
			spidxString
				.addColumn("20180903.26", "HASH_EXACT")
				.nullable()
				.type(ColumnTypeEnum.LONG);
			spidxString
				.addIndex("20180903.27", "IDX_SP_STRING_HASH_EXCT")
				.unique(false)
				.withColumns("HASH_EXACT");
			spidxString
				.addTask(new CalculateHashesTask(VersionEnum.V3_5_0, "20180903.28")
					.setColumnName("HASH_NORM_PREFIX")
					.addCalculator("HASH_NORM_PREFIX", t -> ResourceIndexedSearchParamString.calculateHashNormalized(new PartitionSettings(), RequestPartitionId.defaultPartition(), new ModelConfig(), t.getResourceType(), t.getString("SP_NAME"), t.getString("SP_VALUE_NORMALIZED")))
					.addCalculator("HASH_EXACT", t -> ResourceIndexedSearchParamString.calculateHashExact(new PartitionSettings(), (ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId) null, t.getResourceType(), t.getParamName(), t.getString("SP_VALUE_EXACT")))
				);
		}

		// Indexes - Token
		Builder.BuilderWithTableName spidxToken = version.onTable("HFJ_SPIDX_TOKEN");
		version.startSectionWithMessage("Starting work on table: " + spidxToken.getTableName());
		spidxToken
			.addColumn("20180903.29", "HASH_IDENTITY")
			.nullable()
			.type(ColumnTypeEnum.LONG);
		spidxToken
			.addColumn("20180903.30", "HASH_SYS")
			.nullable()
			.type(ColumnTypeEnum.LONG);
		spidxToken
			.addColumn("20180903.31", "HASH_SYS_AND_VALUE")
			.nullable()
			.type(ColumnTypeEnum.LONG);
		spidxToken
			.addColumn("20180903.32", "HASH_VALUE")
			.nullable()
			.type(ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxToken
				.dropIndex("20180903.33", "IDX_SP_TOKEN");
			spidxToken
				.dropIndex("20180903.34", "IDX_SP_TOKEN_UNQUAL");
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
			spidxToken
				.addTask(new CalculateHashesTask(VersionEnum.V3_5_0, "20180903.39")
					.setColumnName("HASH_IDENTITY")
					.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getString("SP_NAME")))
					.addCalculator("HASH_SYS", t -> ResourceIndexedSearchParamToken.calculateHashSystem(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getParamName(), t.getString("SP_SYSTEM")))
					.addCalculator("HASH_SYS_AND_VALUE", t -> ResourceIndexedSearchParamToken.calculateHashSystemAndValue(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getParamName(), t.getString("SP_SYSTEM"), t.getString("SP_VALUE")))
					.addCalculator("HASH_VALUE", t -> ResourceIndexedSearchParamToken.calculateHashValue(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getParamName(), t.getString("SP_VALUE")))
				);
		}

		// Indexes - URI
		Builder.BuilderWithTableName spidxUri = version.onTable("HFJ_SPIDX_URI");
		version.startSectionWithMessage("Starting work on table: " + spidxUri.getTableName());
		spidxUri
			.addColumn("20180903.40", "HASH_IDENTITY")
			.nullable()
			.type(ColumnTypeEnum.LONG);
		if (!myFlags.contains(FlagEnum.NO_MIGRATE_HASHES)) {
			spidxUri
				.addIndex("20180903.41", "IDX_SP_URI_HASH_IDENTITY")
				.unique(false)
				.withColumns("HASH_IDENTITY", "SP_URI");
			spidxUri
				.addColumn("20180903.42", "HASH_URI")
				.nullable()
				.type(ColumnTypeEnum.LONG);
			spidxUri
				.addIndex("20180903.43", "IDX_SP_URI_HASH_URI")
				.unique(false)
				.withColumns("HASH_URI");
			spidxUri
				.addTask(new CalculateHashesTask(VersionEnum.V3_5_0, "20180903.44")
					.setColumnName("HASH_IDENTITY")
					.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(new PartitionSettings(), (RequestPartitionId) null, t.getResourceType(), t.getString("SP_NAME")))
					.addCalculator("HASH_URI", t -> ResourceIndexedSearchParamUri.calculateHashUri(new PartitionSettings(), (RequestPartitionId) null, t.getResourceType(), t.getString("SP_NAME"), t.getString("SP_URI")))
				);
		}

		// Search Parameter Presence
		Builder.BuilderWithTableName spp = version.onTable("HFJ_RES_PARAM_PRESENT");
		version.startSectionWithMessage("Starting work on table: " + spp.getTableName());
		spp.dropIndex("20180903.45", "IDX_RESPARMPRESENT_SPID_RESID");
		spp
			.addColumn("20180903.46", "HASH_PRESENCE")
			.nullable()
			.type(ColumnTypeEnum.LONG);
		spp
			.addIndex("20180903.47", "IDX_RESPARMPRESENT_HASHPRES")
			.unique(false)
			.withColumns("HASH_PRESENCE");

		ArbitrarySqlTask consolidateSearchParamPresenceIndexesTask = new ArbitrarySqlTask(VersionEnum.V3_5_0, "20180903.48", "HFJ_SEARCH_PARM", "Consolidate search parameter presence indexes");
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
			Long hash = SearchParamPresentEntity.calculateHashPresence(new PartitionSettings(), (RequestPartitionId) null, resType, paramName, present);
			consolidateSearchParamPresenceIndexesTask.executeSql("HFJ_RES_PARAM_PRESENT", "update HFJ_RES_PARAM_PRESENT set HASH_PRESENCE = ? where PID = ?", hash, pid);
		});
		version.addTask(consolidateSearchParamPresenceIndexesTask);

		// SP_ID is no longer needed
		spp.dropColumn("20180903.49", "SP_ID");

		// Concept
		Builder.BuilderWithTableName trmConcept = version.onTable("TRM_CONCEPT");
		version.startSectionWithMessage("Starting work on table: " + trmConcept.getTableName());
		trmConcept
			.addColumn("20180903.50", "CONCEPT_UPDATED")
			.nullable()
			.type(ColumnTypeEnum.DATE_TIMESTAMP);
		trmConcept
			.addIndex("20180903.51", "IDX_CONCEPT_UPDATED")
			.unique(false)
			.withColumns("CONCEPT_UPDATED");
		trmConcept
			.modifyColumn("20180903.52", "CODE")
			.nonNullable()
			.withType(ColumnTypeEnum.STRING, 500);

		// Concept Designation
		version.startSectionWithMessage("Starting work on table: TRM_CONCEPT_DESIG");
		version
			.addTableRawSql("20180907.1", "TRM_CONCEPT_DESIG")
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
			.addTableRawSql("20180907.2", "TRM_CONCEPT_PROPERTY")
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
			.addTableRawSql("20180907.3", "TRM_CONCEPT_MAP")
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
			.addTableRawSql("20180907.4", "TRM_CONCEPT_MAP_GROUP")
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
			.addTableRawSql("20180907.5", "TRM_CONCEPT_MAP_GRP_ELEMENT")
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
			.addTableRawSql("20180907.6", "TRM_CONCEPT_MAP_GRP_ELM_TGT")
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

		version.onTable("HFJ_IDX_CMP_STRING_UNIQ").modifyColumn("20180907.7", "IDX_STRING").nonNullable().withType(ColumnTypeEnum.STRING, 200);


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
		resourceLink
			.dropIndex("20180401.1", "IDX_CSV_RESOURCEPID_AND_VER");
		resourceLink
			.dropColumn("20180401.2", "RES_VERSION_ID");
		resourceLink
			.addColumn("20180401.3", "CS_VERSION_ID")
			.nullable()
			.type(ColumnTypeEnum.STRING, 255);
		resourceLink
			.addColumn("20180401.4", "CODESYSTEM_PID")
			.nullable()
			.type(ColumnTypeEnum.LONG);
		resourceLink
			.addForeignKey("20180401.5", "FK_CODESYSVER_CS_ID")
			.toColumn("CODESYSTEM_PID")
			.references("TRM_CODESYSTEM", "PID");

		// Concept
		Builder.BuilderWithTableName concept = version.onTable("TRM_CONCEPT");
		version.startSectionWithMessage("Starting work on table: " + concept.getTableName());
		concept
			.addColumn("20180401.6", "CODE_SEQUENCE")
			.nullable()
			.type(ColumnTypeEnum.INT);


	}

	protected void init330() { // 20180114 - 20180329
		Builder version = forVersion(VersionEnum.V3_3_0);

		version.initializeSchema("20180115.0", new SchemaInitializationProvider("HAPI FHIR", "/ca/uhn/hapi/fhir/jpa/docs/database", "HFJ_RESOURCE", true));

		Builder.BuilderWithTableName hfjResource = version.onTable("HFJ_RESOURCE");
		version.startSectionWithMessage("Starting work on table: " + hfjResource.getTableName());
		hfjResource.dropColumn("20180115.1", "RES_TEXT");
		hfjResource.dropColumn("20180115.2", "RES_ENCODING");

		Builder.BuilderWithTableName hfjResVer = version.onTable("HFJ_RES_VER");
		version.startSectionWithMessage("Starting work on table: " + hfjResVer.getTableName());
		hfjResVer.modifyColumn("20180115.3", "RES_ENCODING")
			.nullable();
		hfjResVer.modifyColumn("20180115.4", "RES_TEXT")
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
