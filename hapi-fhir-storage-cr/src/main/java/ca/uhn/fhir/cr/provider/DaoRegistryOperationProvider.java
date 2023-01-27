package ca.uhn.fhir.cr.provider;

import ca.uhn.fhir.cr.api.OperationProvider;
import ca.uhn.fhir.cr.behavior.DaoRegistryUser;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;

public class DaoRegistryOperationProvider implements OperationProvider, DaoRegistryUser {
	private DaoRegistry myDaoRegistry;

	public DaoRegistryOperationProvider(DaoRegistry daoRegistry){
		this.myDaoRegistry = daoRegistry;
	}

	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}
}
