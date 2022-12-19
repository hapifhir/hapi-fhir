package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;

import javax.annotation.Nonnull;

public class ForeignKeyContainer {

	private String myParentTableName;

	private String myColumnName;

	private ColumnTypeEnum myColumnTypeEnum;

	private String myParentTableColumnName;

	public ForeignKeyContainer(
		String theColumnName,
		ColumnTypeEnum theColumnTypeEnum,
		String theParentTableName,
		String theParentTableColumnName
	) {
		myColumnName = theColumnName;
		myColumnTypeEnum = theColumnTypeEnum;
		myParentTableName = theParentTableName;
		myParentTableColumnName = theParentTableColumnName;
	}

	public String getParentTableName() {
		return myParentTableName;
	}

	public void setParentTableName(String theParentTableName) {
		myParentTableName = theParentTableName;
	}

	public String getColumnName() {
		return myColumnName;
	}

	public void setColumnName(String theColumnName) {
		myColumnName = theColumnName;
	}

	public String getParentTableColumnName() {
		return myParentTableColumnName;
	}

	public void setParentTableColumnName(String theParentTableColumnName) {
		myParentTableColumnName = theParentTableColumnName;
	}

	public ColumnTypeEnum getColumnTypeEnum() {
		return myColumnTypeEnum;
	}

	public void setColumnTypeEnum(ColumnTypeEnum theColumnTypeEnum) {
		myColumnTypeEnum = theColumnTypeEnum;
	}

	public String generateSQL(
		@Nonnull DriverTypeEnum theDriverTypeEnum,
		boolean thePrettyPrint
	) {
		switch (theDriverTypeEnum) {
			case MYSQL_5_7:
				return String.format(
					"FOREIGN KEY (%s) REFERENCES %s(%s)",
					myColumnName,
					myParentTableName,
					myParentTableColumnName
				);
			case MSSQL_2012:
			case ORACLE_12C:
				return String.format(
					"%s %s FOREIGN KEY REFERENCES %s(%s)",
					myColumnName,
					myColumnTypeEnum.name(),
					myParentTableName,
					myParentTableColumnName
				);
			case POSTGRES_9_4:
				return String.format(
					"FOREIGN KEY(%s) REFERENCES %s(%s)",
					myColumnName,
					myParentTableName,
					myParentTableColumnName
				);
			default:
				throw new UnsupportedOperationException("SQL Engine " + theDriverTypeEnum.name() + " not supported for foreign key!");
		}
	}
}
