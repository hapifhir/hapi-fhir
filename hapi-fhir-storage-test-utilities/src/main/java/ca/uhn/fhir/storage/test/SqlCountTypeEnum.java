package ca.uhn.fhir.storage.test;

/**
 * Parameter for {@link CircularQueueCaptureQueriesListenerAssertions} methods
 *
 * @since 8.14.0
 */
public enum SqlCountTypeEnum {

	/// Count the number of parameter sets issued.
	/// For example, given a query that inserts to a table,
	/// if we execute `INSERT INTO my_table (col1, col2) VALUES (?, ?)` in a batch with 3 pairs of
	/// parameters (meaning 3 rows will be inserted), the count should be `3`.
	PARAMETER_SETS("ParamSets"),

	/// Count the number of statements issued, regardless of how many parameter sets are issued
	/// with the statement.
	/// For example, given a query that inserts to a table,
	/// if we execute `INSERT INTO my_table (col1, col2) VALUES (?, ?)` in a batch with 3 pairs of
	/// parameters (meaning 3 rows will be inserted), the count should be `1`.
	STATEMENTS("Statements");

	private final String myShortName;

	SqlCountTypeEnum(String theShortName) {
		myShortName = theShortName;
	}

	public String shortName() {
		return myShortName;
	}
}
