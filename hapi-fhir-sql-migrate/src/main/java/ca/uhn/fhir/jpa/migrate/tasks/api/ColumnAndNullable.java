package ca.uhn.fhir.jpa.migrate.tasks.api;

/**
 * Simple data class for holding information about a column, and whether it was nullable at time of writing this migration.
 */
public class ColumnAndNullable {
	private final String myColumnName;
	private final boolean myNullable;

	public ColumnAndNullable(String myColumnName, boolean myNullable) {
		this.myColumnName = myColumnName;
		this.myNullable = myNullable;
	}

	public String getColumnName() {
		return myColumnName;
	}

	public boolean isNullable() {
		return myNullable;
	}
}
