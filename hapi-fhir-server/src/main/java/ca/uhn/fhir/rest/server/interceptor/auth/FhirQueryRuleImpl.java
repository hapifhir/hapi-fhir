package ca.uhn.fhir.rest.server.interceptor.auth;

public class FhirQueryRuleImpl extends RuleImplOp{
	private String myFilter;

	/**
	 * Constructor
	 *
	 * @param theRuleName
	 */
	public FhirQueryRuleImpl(String theRuleName) {
		super(theRuleName);
	}

    public void setFilter(String theFilter) {
        myFilter = theFilter;
    }

    public String getFilter() {
        return myFilter;
    }
}
