package ca.uhn.fhir.jpa.migrate.dao;

import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.CreateIndexQuery;
import com.healthmarketscience.sqlbuilder.CreateTableQuery;
import com.healthmarketscience.sqlbuilder.DeleteQuery;
import com.healthmarketscience.sqlbuilder.FunctionCall;
import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.JdbcEscape;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private final String myBuildSuccessfulVersionQuery;
	private final String myDeleteAll;
	private final String myHighestKeyQuery;
	private final String myMigrationTablename;

	public MigrationQueryBuilder(String theMigrationTablename) {
		myMigrationTablename = theMigrationTablename;

		mySpec = new DbSpec();
		mySchema = mySpec.addDefaultSchema();
		myTable = mySchema.addTable(theMigrationTablename);

		myInstalledRankCol = myTable.addColumn("INSTALLED_RANK", Types.INTEGER, null);
		myInstalledRankCol.notNull();

		myVersionCol = myTable.addColumn("VERSION", Types.VARCHAR, HapiMigrationEntity.VERSION_MAX_SIZE);

		myDescriptionCol = myTable.addColumn("DESCRIPTION", Types.VARCHAR, HapiMigrationEntity.DESCRIPTION_MAX_SIZE);
		myDescriptionCol.notNull();

		myTypeCol = myTable.addColumn("TYPE", Types.VARCHAR, HapiMigrationEntity.TYPE_MAX_SIZE);
		myTypeCol.notNull();

		myScriptCol = myTable.addColumn("SCRIPT", Types.VARCHAR, HapiMigrationEntity.SCRIPT_MAX_SIZE);
		myScriptCol.notNull();

		myChecksumCol = myTable.addColumn("CHECKSUM", Types.INTEGER, null);

		myInstalledByCol = myTable.addColumn("INSTALLED_BY", Types.VARCHAR, HapiMigrationEntity.INSTALLED_BY_MAX_SIZE);
		myInstalledByCol.notNull();

		myInstalledOnCol = myTable.addColumn("INSTALLED_ON", Types.TIMESTAMP, null);
		myInstalledOnCol.notNull();

		myExecutionTimeCol = myTable.addColumn("EXECUTION_TIME", Types.INTEGER, null);
		myExecutionTimeCol.notNull();
		mySuccessCol = myTable.addColumn("SUCCESS", Types.BOOLEAN, null);
		mySuccessCol.notNull();

		myBuildSuccessfulVersionQuery = buildFindSuccessfulVersionQuery();
		myDeleteAll = new DeleteQuery(myTable).toString();
		myHighestKeyQuery = buildHighestKeyQuery();
	}

	private String buildFindSuccessfulVersionQuery() {
		return new SelectQuery()
			.addColumns(myVersionCol)
			.addCondition(  BinaryCondition.equalTo(mySuccessCol, true))
			.validate()
			.toString();
	}

	public String findSuccessfulVersionQuery() {
		return myBuildSuccessfulVersionQuery;
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

	public String insertStatement(HapiMigrationEntity theEntity) {
		return new InsertQuery(myTable)
			.addColumn(myInstalledRankCol, theEntity.getPid())
			.addColumn(myVersionCol, theEntity.getVersion())
			.addColumn(myDescriptionCol, theEntity.getDescription())
			.addColumn(myTypeCol, theEntity.getType())
			.addColumn(myScriptCol, theEntity.getScript())
			.addColumn(myChecksumCol, theEntity.getChecksum())
			.addColumn(myInstalledByCol, theEntity.getInstalledBy())
			.addColumn(myInstalledOnCol, JdbcEscape.timestamp(theEntity.getInstalledOn()))
			.addColumn(myExecutionTimeCol, theEntity.getExecutionTime())
			.addColumn(mySuccessCol, theEntity.getSuccess())
			.validate()
			.toString();

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
}
