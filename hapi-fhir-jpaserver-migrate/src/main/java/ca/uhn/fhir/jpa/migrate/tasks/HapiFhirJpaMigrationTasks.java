package ca.uhn.fhir.jpa.migrate.tasks;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.migrate.taskdef.*;
import ca.uhn.fhir.util.VersionEnum;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

@SuppressWarnings("UnstableApiUsage")
public class HapiFhirJpaMigrationTasks {

	private Multimap<VersionEnum, BaseTask<?>> myTasks = MultimapBuilder.hashKeys().arrayListValues().build();

	/**
	 * Constructor
	 */
	public HapiFhirJpaMigrationTasks() {

		// Forced ID changes
		Builder.BuilderWithTableName forcedId = forVersion(VersionEnum.V3_5_0).onTable("HFJ_FORCED_ID");
		forcedId
			.dropIndex("IDX_FORCEDID_TYPE_FORCEDID");
		forcedId
			.dropIndex("IDX_FORCEDID_TYPE_RESID");
		forcedId
			.addIndex("IDX_FORCEDID_TYPE_FID")
			.unique(true)
			.withColumns("RESOURCE_TYPE", "FORCED_ID");

		// Indexes - Coords
		Builder.BuilderWithTableName spidxCoords = forVersion(VersionEnum.V3_5_0).onTable("HFJ_SPIDX_COORDS");
		spidxCoords
			.dropIndex("IDX_SP_COORDS_HASH");
		spidxCoords
			.addColumn("HASH_IDENTITY")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		spidxCoords
			.addIndex("IDX_SP_COORDS_HASH")
			.unique(false)
			.withColumns("HASH_IDENTITY", "SP_VALUE", "SP_LATITUDE", "SP_LONGITUDE");
		spidxCoords
			.addTask(new CalculateHashesTask()
				.setColumnName("HASH_IDENTITY")
				.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(t.getResourceType(), t.getString("SP_NAME")))
			);

		// Indexes - Date
		Builder.BuilderWithTableName spidxDate = forVersion(VersionEnum.V3_5_0).onTable("HFJ_SPIDX_DATE");
		spidxDate
			.dropIndex("IDX_SP_TOKEN");
		spidxDate
			.addColumn("HASH_IDENTITY")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		spidxDate
			.addIndex("IDX_SP_DATE_HASH")
			.unique(false)
			.withColumns("HASH_IDENTITY", "SP_VALUE_LOW", "SP_VALUE_HIGH");
		spidxDate
			.addTask(new CalculateHashesTask()
				.setColumnName("HASH_IDENTITY")
				.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(t.getResourceType(), t.getString("SP_NAME")))
			);

		// Indexes - Number
		Builder.BuilderWithTableName spidxNumber = forVersion(VersionEnum.V3_5_0).onTable("HFJ_SPIDX_NUMBER");
		spidxNumber
			.dropIndex("IDX_SP_NUMBER");
		spidxNumber
			.addColumn("HASH_IDENTITY")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		spidxNumber
			.addIndex("IDX_SP_NUMBER_HASH_VAL")
			.unique(false)
			.withColumns("HASH_IDENTITY", "SP_VALUE");
		spidxNumber
			.addTask(new CalculateHashesTask()
				.setColumnName("HASH_IDENTITY")
				.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(t.getResourceType(), t.getString("SP_NAME")))
			);

		// Indexes - Quantity
		Builder.BuilderWithTableName spidxQuantity = forVersion(VersionEnum.V3_5_0).onTable("HFJ_SPIDX_QUANTITY");
		spidxQuantity
			.dropIndex("IDX_SP_QUANTITY");
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

		// Indexes - String
		Builder.BuilderWithTableName spidxString = forVersion(VersionEnum.V3_5_0).onTable("HFJ_SPIDX_STRING");
		spidxString
			.dropIndex("IDX_SP_STRING");
		spidxString
			.addColumn("HASH_NORM_PREFIX")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		spidxString
			.addColumn("HASH_NORM")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		spidxString
			.addIndex("IDX_SP_STRING_HASH_NRM")
			.unique(false)
			.withColumns("HASH_NORM_PREFIX", "SP_VALUE_NORMALIZED");
		spidxString
			.addIndex("IDX_SP_STRING_HASH_EXCT")
			.unique(false)
			.withColumns("HASH_EXACT");
		spidxString
			.addTask(new CalculateHashesTask()
				.setColumnName("HASH_IDENTITY")
				.addCalculator("IDX_SP_STRING_HASH_NRM", t -> ResourceIndexedSearchParamString.calculateHashNormalized(new DaoConfig(), t.getResourceType(), t.getString("SP_NAME"), t.getString("SP_VALUE_NORMALIZED")))
				.addCalculator("IDX_SP_STRING_HASH_EXCT", t -> ResourceIndexedSearchParamString.calculateHashExact(t.getResourceType(), t.getParamName(), t.getString("SP_VALUE_EXACT")))
			);

		// Indexes - Token
		Builder.BuilderWithTableName spidxToken = forVersion(VersionEnum.V3_5_0).onTable("HFJ_SPIDX_TOKEN");
		spidxToken
			.dropIndex("IDX_SP_TOKEN");
		spidxToken
			.dropIndex("IDX_SP_TOKEN_UNQUAL");
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

		// Indexes - URI
		Builder.BuilderWithTableName spidxUri = forVersion(VersionEnum.V3_5_0).onTable("HFJ_SPIDX_URI");
		spidxUri
			.addColumn("HASH_IDENTITY")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		spidxUri
			.addIndex("IDX_SP_URI_HASH_IDENTITY")
			.unique(false)
			.withColumns("HASH_IDENTITY", "SP_URI");
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

		// Search Parameter Presence
		Builder.BuilderWithTableName spp = forVersion(VersionEnum.V3_5_0).onTable("HFJ_RES_PARAM_PRESENT");
		spp.dropIndex("IDX_RESPARMPRESENT_SPID_RESID");
		spp
			.addColumn("HASH_PRESENCE")
			.nullable()
			.type(AddColumnTask.ColumnTypeEnum.LONG);
		spp
			.addIndex("IDX_RESPARMPRESENT_HASHPRES")
			.unique(false)
			.withColumns("HASH_PRESENCE");
		ArbitrarySqlTask consolidateSearchParamPresenceIndexesTask = new ArbitrarySqlTask("Consolidate search parameter presence indexes");
		consolidateSearchParamPresenceIndexesTask.setBatchSize(1);
		String sql = "SELECT " +
			"HFJ_SEARCH_PARM.RES_TYPE RES_TYPE, HFJ_SEARCH_PARM.PARAM_NAME PARAM_NAME, " +
			"HFJ_RES_PARAM_PRESENT.PID PID, HFJ_RES_PARAM_PRESENT.SP_ID SP_ID, HFJ_RES_PARAM_PRESENT.SP_PRESENT SP_PRESENT, HFJ_RES_PARAM_PRESENT.HASH_PRESENT HASH_PRESENT " +
			"from HFJ_RES_PARAM_PRESENT " +
			"join HFJ_SEARCH_PARM ON (HFJ_SEARCH_PARM.PID = HFJ_RES_PARAM_PRESENT.SP_ID) " +
			"where HFJ_RES_PARAM_PRESENT.HASH_PRESENT is null";
		consolidateSearchParamPresenceIndexesTask.addQuery(sql, ArbitrarySqlTask.QueryModeEnum.BATCH_UNTIL_NO_MORE, t -> {
			Long pid = (Long) t.get("PID");
			Boolean present = (Boolean) t.get("SP_PRESENT");
			String resType = (String) t.get("RES_TYPE");
			String paramName = (String) t.get("PARAM_NAME");
			Long hash = SearchParamPresent.calculateHashPresence(resType, paramName, present);
			consolidateSearchParamPresenceIndexesTask.executeSql("update HFJ_RES_PARAM_PRESENT set HASH_PRESENCE = ? where PID = ?", hash, pid);
		});
		forVersion(VersionEnum.V3_5_0).addTask(consolidateSearchParamPresenceIndexesTask);

		// Concept
		Builder.BuilderWithTableName trmConcept = forVersion(VersionEnum.V3_5_0).onTable("TRM_CONCEPT");
		trmConcept
			.addIndex("IDX_CONCEPT_UPDATED")
			.unique(false)
			.withColumns("CONCEPT_UPDATED");
		trmConcept
			.modifyColumn("CODE")
			.nonNullable()
			.withType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 500);

	}

	private Builder forVersion(VersionEnum theVersion) {
		return new Builder(theVersion);
	}


	private class Builder {

		private final VersionEnum myVersion;
		private String myTableName;

		public Builder(VersionEnum theVersion) {
			myVersion = theVersion;
		}

		public BuilderWithTableName onTable(String theTableName) {
			myTableName = theTableName;
			return new BuilderWithTableName();
		}

		private void addTask(BaseTask theTask) {
			theTask.validate();
			myTasks.put(myVersion, theTask);
		}

		private class BuilderWithTableName {
			private String myIndexName;
			private String myColumnName;

			void dropIndex(String theIndexName) {
				DropIndexTask task = new DropIndexTask();
				task.setIndexName(theIndexName);
				task.setTableName(myTableName);
				addTask(task);
			}

			public BuilderAddIndexWithName addIndex(String theIndexName) {
				myIndexName = theIndexName;
				return new BuilderAddIndexWithName();
			}

			public BuilderAddColumnWithName addColumn(String theColumnName) {
				myColumnName = theColumnName;
				return new BuilderAddColumnWithName();
			}

			public void addTask(BaseTableTask<?> theTask) {
				theTask.setTableName(myTableName);
				Builder.this.addTask(theTask);
			}

			public BuilderModifyColumnWithName modifyColumn(String theColumnName) {
				myColumnName = theColumnName;
				return new BuilderModifyColumnWithName();
			}

			private class BuilderAddIndexWithName {
				private boolean myUnique;

				public BuilderAddIndexUnique unique(boolean theUnique) {
					myUnique = theUnique;
					return new BuilderAddIndexUnique();
				}

				private class BuilderAddIndexUnique {
					public void withColumns(String... theColumnNames) {
						AddIndexTask task = new AddIndexTask();
						task.setTableName(myTableName);
						task.setIndexName(myIndexName);
						task.setUnique(myUnique);
						task.setColumns(theColumnNames);
						addTask(task);
					}
				}
			}

			private class BuilderAddColumnWithName {
				private boolean myNullable;

				public BuilderAddColumnWithNameNullable nullable() {
					myNullable = true;
					return new BuilderAddColumnWithNameNullable();
				}

				private class BuilderAddColumnWithNameNullable {
					public void type(AddColumnTask.ColumnTypeEnum theColumnType) {
						AddColumnTask task = new AddColumnTask();
						task.setColumnName(myColumnName);
						task.setNullable(myNullable);
						task.setColumnType(theColumnType);
						addTask(task);
					}
				}
			}

			private class BuilderModifyColumnWithName {

				private boolean myNullable;

				public BuilderModifyColumnWithNameAndNullable nullable() {
					myNullable = true;
					return new BuilderModifyColumnWithNameAndNullable();
				}

				public BuilderModifyColumnWithNameAndNullable nonNullable() {
					myNullable = false;
					return new BuilderModifyColumnWithNameAndNullable();
				}

				private class BuilderModifyColumnWithNameAndNullable {

					public void withType(BaseTableColumnTypeTask.ColumnTypeEnum theColumnType, int theLength) {
						if (theColumnType == BaseTableColumnTypeTask.ColumnTypeEnum.STRING) {
							ModifyColumnTask task = new ModifyColumnTask();
							task.setColumnName(myColumnName);
							task.setTableName(myTableName);
							task.setColumnLength(theLength);
							task.setNullable(myNullable);
							addTask(task);
						} else {
							throw new IllegalArgumentException("Can not specify length for column of type " + theColumnType);
						}
					}

				}
			}
		}
	}

}
