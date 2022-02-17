package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.rest.api.IResourceSupportedSvc;

public class DaoRegistryResourceSupportedSvc implements IResourceSupportedSvc {
	private final IDaoRegistry myDaoRegistry;

	public DaoRegistryResourceSupportedSvc(IDaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	@Override
	public boolean isSupported(String theResourceName) {
		return myDaoRegistry.isResourceTypeSupported(theResourceName);
	}
}
