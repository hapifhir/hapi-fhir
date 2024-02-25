package ca.uhn.fhir.jpa.logging;

/**
 * Contract for Filters used by the utility class SqlStatementFilteringLogger
 */
public interface ISqlLoggerFilter {

	boolean match(String theStatement);

	boolean evaluateFilterLine(String theFilterLine);

	String getPrefix();

	void clearDefinitions();

	Object getLockingObject();
}
