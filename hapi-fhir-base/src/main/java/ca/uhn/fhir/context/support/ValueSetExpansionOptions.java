package ca.uhn.fhir.context.support;

/**
 * Options for ValueSet expansion
 *
 * @see IContextValidationSupport
 */
public class ValueSetExpansionOptions {

	private boolean myFailOnMissingCodeSystem = true;

	/**
	 * Should the expansion fail if a codesystem is referenced by the valueset, but
	 * it can not be found?
	 * <p>
	 * Default is <code>true</code>
	 * </p>
	 */
	public boolean isFailOnMissingCodeSystem() {
		return myFailOnMissingCodeSystem;
	}

	/**
	 * Should the expansion fail if a codesystem is referenced by the valueset, but
	 * it can not be found?
	 * <p>
	 * Default is <code>true</code>
	 * </p>
	 */
	public ValueSetExpansionOptions setFailOnMissingCodeSystem(boolean theFailOnMissingCodeSystem) {
		myFailOnMissingCodeSystem = theFailOnMissingCodeSystem;
		return this;
	}
}
