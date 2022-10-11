package ca.uhn.fhir.jpa.migrate.dao;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import ca.uhn.fhir.jpa.migrate.taskdef.ColumnTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.ColumnTypeToDriverTypeToSqlType;
import com.healthmarketscience.common.util.AppendableExt;
import com.healthmarketscience.sqlbuilder.CreateIndexQuery;
import com.healthmarketscience.sqlbuilder.CreateTableQuery;
import com.healthmarketscience.sqlbuilder.DeleteQuery;
import com.healthmarketscience.sqlbuilder.FunctionCall;
import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.SqlObject;
import com.healthmarketscience.sqlbuilder.ValidationContext;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Types;

public class MigrationQueryBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(MigrationQueryBuilder.class);

	private final DbSpec mySpec;
	private final DbSchema mySchema;
	private final DbTable myTable;
	private final DbColumn myVersionCol;
	private final DbColumn myInstalledRankCol;
	private final DbColumn myDescriptionCol;
	private final DbColumn myTypeCol;
	private final DbColumn myScriptCol;
	private final DbColumn myChecksumCol;
	private final DbColumn myInstalledByCol;
	private final DbColumn myInstalledOnCol;
	private final DbColumn myExecutionTimeCol;
	private final DbColumn mySuccessCol;
	private final String myDeleteAll;
	private final String myHighestKeyQuery;
	private final DriverTypeEnum myDriverType;
	private final String myMigrationTablename;
	private final String myBooleanType;

	public MigrationQueryBuilder(DriverTypeEnum theDriverType, String theMigrationTablename) {
		myDriverType = theDriverType;
		myMigrationTablename = theMigrationTablename;

		mySpec = new DbSpec();
		mySchema = mySpec.addDefaultSchema();
		myTable = mySchema.addTable("\"" + theMigrationTablename + "\"");

		myInstalledRankCol = myTable.addColumn("\"installed_rank\"", Types.INTEGER, null);
		myInstalledRankCol.notNull();

		myVersionCol = myTable.addColumn("\"version\"", Types.VARCHAR, HapiMigrationEntity.VERSION_MAX_SIZE);

		myDescriptionCol = myTable.addColumn("\"description\"", Types.VARCHAR, HapiMigrationEntity.DESCRIPTION_MAX_SIZE);
		myDescriptionCol.notNull();

		myTypeCol = myTable.addColumn("\"type\"", Types.VARCHAR, HapiMigrationEntity.TYPE_MAX_SIZE);
		myTypeCol.notNull();

		myScriptCol = myTable.addColumn("\"script\"", Types.VARCHAR, HapiMigrationEntity.SCRIPT_MAX_SIZE);
		myScriptCol.notNull();

		myChecksumCol = myTable.addColumn("\"checksum\"", Types.INTEGER, null);

		myInstalledByCol = myTable.addColumn("\"installed_by\"", Types.VARCHAR, HapiMigrationEntity.INSTALLED_BY_MAX_SIZE);
		myInstalledByCol.notNull();

		myInstalledOnCol = myTable.addColumn("\"installed_on\"", Types.DATE, null);
		myInstalledOnCol.notNull();

		myExecutionTimeCol = myTable.addColumn("\"execution_time\"", Types.INTEGER, null);
		myExecutionTimeCol.notNull();

		myBooleanType = ColumnTypeToDriverTypeToSqlType.getColumnTypeToDriverTypeToSqlType().get(ColumnTypeEnum.BOOLEAN).get(theDriverType);
		mySuccessCol = myTable.addColumn("\"success\"", myBooleanType, null);
		mySuccessCol.notNull();

		myDeleteAll = new DeleteQuery(myTable).toString();
		myHighestKeyQuery = buildHighestKeyQuery();
	}

	public String deleteAll() {
		return myDeleteAll;
	}

	public String getHighestKeyQuery() {
		return myHighestKeyQuery;
	}

	private String buildHighestKeyQuery() {
		return new SelectQuery()
			.addCustomColumns(FunctionCall.max().addColumnParams(myInstalledRankCol))
			.validate()
			.toString();
	}

	public String insertPreparedStatement() {
		return new InsertQuery(myTable)
			.addPreparedColumns(myInstalledRankCol,
				myVersionCol,
				myDescriptionCol,
				myTypeCol,
				myScriptCol,
				myChecksumCol,
				myInstalledByCol,
				myInstalledOnCol,
				myExecutionTimeCol,
				mySuccessCol)
			.validate()
			.toString();
	}

	/**
	 sqlbuilder doesn't know about different database types, so we have to manually map boolean columns ourselves :-(
	 I'm gaining a new appreciation for Hibernate.  I tried using hibernate for maintaining this table, but it added
	 a lot of overhead for managing just one table.  Seeing this sort of nonsense though makes me wonder if we should
	 just use it instead of sqlbuilder.  Disappointed that sqlbuilder doesn't handle boolean well out of the box.
	 (It does support a static option via System.setProperty("com.healthmarketscience.sqlbuilder.useBooleanLiterals", "true");
	 but that only works at classloading time which isn't much help to us.
	 */

	private SqlObject getBooleanValue(Boolean theBoolean) {
		SqlObject retval = new SqlObject() {
			@Override
			protected void collectSchemaObjects(ValidationContext vContext) {}

			@Override
			public void appendTo(AppendableExt app) throws IOException {
				String stringValue = ColumnTypeToDriverTypeToSqlType.toBooleanValue(myDriverType, theBoolean);
				app.append(stringValue);
			}
		};

		return retval;
	}

	public String createTableStatement() {
		return new CreateTableQuery(myTable, true)
			.validate()
			.toString();
	}

	public String createIndexStatement() {
		return new CreateIndexQuery(myTable, myMigrationTablename.toUpperCase() + "_PK_INDEX")
			.setIndexType(CreateIndexQuery.IndexType.UNIQUE)
			.addColumns(myInstalledRankCol)
			.validate()
			.toString();
	}

	public String findAllQuery() {
		return new SelectQuery()
			.addFromTable(myTable)
			.addAllColumns()
			.validate()
			.toString();
	}
}
