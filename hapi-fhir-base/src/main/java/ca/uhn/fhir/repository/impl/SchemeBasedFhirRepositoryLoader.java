package ca.uhn.fhir.repository.impl;

public abstract class SchemeBasedFhirRepositoryLoader implements IRepositoryLoader {
	final String myScheme;

	protected SchemeBasedFhirRepositoryLoader(String theScheme) {
		myScheme = theScheme;
	}

	public boolean canLoad(IRepositoryLoader.IRepositoryRequest theRepositoryRequest) {
		if (theRepositoryRequest == null) {
			return false;
		}

		return myScheme.equals(theRepositoryRequest.getSubScheme());
	}
}
