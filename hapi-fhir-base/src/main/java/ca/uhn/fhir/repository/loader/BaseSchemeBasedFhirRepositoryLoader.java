package ca.uhn.fhir.repository.loader;

/**
 * Simple base class for {@link IRepositoryLoader} implementations that select on the sub-scheme of the URL.
 */
public abstract class BaseSchemeBasedFhirRepositoryLoader implements IRepositoryLoader {
	final String mySubScheme;

	/**
	 * Constructor
	 *
	 * @param theSubScheme The sub-scheme to match against. For example, if the URL is "fhir-repository:ig-filesystem:...",
	 *                     then the sub-scheme is "ig-filesystem".
	 */
	protected BaseSchemeBasedFhirRepositoryLoader(String theSubScheme) {
		mySubScheme = theSubScheme;
	}

	public boolean canLoad(IRepositoryLoader.IRepositoryRequest theRepositoryRequest) {
		if (theRepositoryRequest == null) {
			return false;
		}

		return mySubScheme.equals(theRepositoryRequest.getSubScheme());
	}
}
