package ca.uhn.fhir.jpa.subscription.matcher;

public class SubscriptionMatchResult {
	// This could be an enum, but we may want to include details about unsupported matches in the future
	public static final SubscriptionMatchResult MATCH = new SubscriptionMatchResult(true);
	public static final SubscriptionMatchResult NO_MATCH =  new SubscriptionMatchResult(false);;

	private final boolean myMatch;
	private final boolean mySupported;
	private final String myUnsupportedParameter;
	private final String myUnsupportedReason;

	public SubscriptionMatchResult(boolean theMatch) {
		this.myMatch = theMatch;
		this.mySupported = true;
		this.myUnsupportedParameter = null;
		this.myUnsupportedReason = null;
	}

	public SubscriptionMatchResult(String theUnsupportedParameter) {
		this.myMatch = false;
		this.mySupported = false;
		this.myUnsupportedParameter = theUnsupportedParameter;
		this.myUnsupportedReason = "Parameter not supported";
	}

	public SubscriptionMatchResult(String theUnsupportedParameter, String theUnsupportedReason) {
		this.myMatch = false;
		this.mySupported = false;
		this.myUnsupportedParameter = theUnsupportedParameter;
		this.myUnsupportedReason = theUnsupportedReason;
	}

	public boolean supported() {
		return mySupported;
	}

	public boolean matched() {
		return myMatch;
	}

	public String getUnsupportedReason() {
		return "Parameter: <" + myUnsupportedParameter + "> Reason: " + myUnsupportedReason;
	}
}
