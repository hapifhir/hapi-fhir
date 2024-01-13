package ca.uhn.fhir.jpa.logging;

/**
 * When filtering active, Filters hibernate SQL log lines containing the defined fragment
 */
public class SqlLoggerFragmentFilter extends BaseSqlLoggerFilterImpl implements ISqlLoggerFilter {

	public static final String PREFIX = "frag:";

	@Override
	public boolean match(String theStatement) {
		synchronized (myFilterDefinitions) {
			return myFilterDefinitions.stream().anyMatch(theStatement::contains);
		}
	}

	@Override
	public String getPrefix() {
		return PREFIX;
	}
}
