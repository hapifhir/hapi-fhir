package ca.uhn.fhir.jpa.migrate.tasks;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.*;
import ca.uhn.fhir.util.VersionEnum;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.intellij.lang.annotations.Language;

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

		// Concept Designation
		forVersion(VersionEnum.V3_5_0)
			.addTable("TRM_CONCEPT_DESIG")
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
		forVersion(VersionEnum.V3_5_0)
			.addTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
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
		forVersion(VersionEnum.V3_5_0)
			.addTable("TRM_CONCEPT_MAP")
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
		forVersion(VersionEnum.V3_5_0)
			.addTable("TRM_CONCEPT_MAP_GROUP")
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
		forVersion(VersionEnum.V3_5_0)
			.addTable("TRM_CONCEPT_MAP_GRP_ELEMENT")
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
		forVersion(VersionEnum.V3_5_0)
			.addTable("TRM_CONCEPT_MAP_GRP_ELM_TGT")
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

		public BuilderAddTable addTable(String theTableName) {
			myTableName = theTableName;
			return new BuilderAddTable();
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

		private class BuilderAddTable {

			private final AddTableTask myTask;

			private BuilderAddTable() {
				myTask = new AddTableTask();
				myTask.setTableName(myTableName);
			}


			public BuilderAddTable addSql(DriverTypeEnum theDriverTypeEnum, @Language("SQL") String theSql) {
				myTask.addSql(theDriverTypeEnum, theSql);
				return this;
			}
		}
	}

}
