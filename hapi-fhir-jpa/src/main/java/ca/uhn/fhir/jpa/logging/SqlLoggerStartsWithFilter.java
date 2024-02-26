package ca.uhn.fhir.jpa.logging;

/**
 * When filtering active, Filters hibernate SQL log lines starting with the defined string
 */
public class SqlLoggerStartsWithFilter extends BaseSqlLoggerFilterImpl {
	private static final String PREFIX = "sw:";

	@Override
	public boolean match(String theStatement) {
		synchronized (myFilterDefinitions) {
			return myFilterDefinitions.stream().anyMatch(theStatement::startsWith);
		}
	}

	@Override
	public String getPrefix() {
		return PREFIX;
	}
}
