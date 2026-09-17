package ca.uhn.hapi.fhir.docs;

public class ChangelogConstants {

	/** A new feature or capability being introduced. */
	public static final String TYPE_ADD = "add";

	/** A change to existing behaviour that is neither a bug fix nor a new feature. */
	public static final String TYPE_CHANGE = "change";

	/** A bug fix correcting incorrect or unintended behaviour. */
	public static final String TYPE_FIX = "fix";

	/** A performance improvement that does not otherwise change behaviour. */
	public static final String TYPE_PERFORMANCE = "perf";

	/** Removal of an existing feature, capability, or API. */
	public static final String TYPE_REMOVE = "remove";

	/** A security-related fix or hardening change. */
	public static final String TYPE_SECURITY = "security";

	/**
	 * Non-instantiable
	 */
	private ChangelogConstants(){}
}
