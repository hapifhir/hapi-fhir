package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.repository.HapiFhirRepository;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.repository.IRepositoryTest;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.junit.jupiter.api.AfterEach;

class HapiFhirRepositoryTest extends BaseJpaR4Test implements IRepositoryTest {

	@AfterEach
	public void afterResetDao() {
		myStorageSettings.setResourceServerIdStrategy(new JpaStorageSettings().getResourceServerIdStrategy());
		myStorageSettings.setResourceClientIdStrategy(new JpaStorageSettings().getResourceClientIdStrategy());
		myStorageSettings.setDefaultSearchParamsCanBeOverridden(new JpaStorageSettings().isDefaultSearchParamsCanBeOverridden());
		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED);
		myStorageSettings.setIndexOnContainedResources(new JpaStorageSettings().isIndexOnContainedResources());
		myStorageSettings.setIndexOnContainedResourcesRecursively(new JpaStorageSettings().isIndexOnContainedResourcesRecursively());
	}


	@Override
	public RepositoryTestSupport getRepositoryTestSupport() {
		RestfulServer restfulServer = new RestfulServer(myDaoRegistry.getFhirContext());
		return new RepositoryTestSupport(new HapiFhirRepository(myDaoRegistry, mySrd, restfulServer));
	}
}
