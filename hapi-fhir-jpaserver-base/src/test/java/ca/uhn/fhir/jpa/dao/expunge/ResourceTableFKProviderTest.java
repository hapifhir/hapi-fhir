package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;


class ResourceTableFKProviderTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceTableFKProviderTest.class);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	ResourceTableFKProvider myResourceTableFKProvider;

	@Test
	public void testWeHaveAllForeignKeys() {
		List<Object[]> result = myEntityManager.createNativeQuery("SELECT FKTABLE_NAME, FKCOLUMN_NAME FROM INFORMATION_SCHEMA.CROSS_REFERENCES WHERE PKTABLE_NAME = 'HFJ_RESOURCE'").getResultList();
		List<ResourceForeignKey> expected = result.stream().map(a -> new ResourceForeignKey(a[0].toString(), a[1].toString())).collect(Collectors.toList());

		// Add the extra FKs that are not available in the CROSS_REFERENCES table
		expected.add(new ResourceForeignKey("HFJ_HISTORY_TAG", "RES_ID"));
		expected.add(new ResourceForeignKey("TRM_CODESYSTEM_VER", "RES_ID"));
		expected.add(new ResourceForeignKey("HFJ_RES_VER_PROV", "RES_PID"));
		// If this assertion fails, it means hapi-fhir has added a new foreign-key dependency to HFJ_RESOURCE.  To fix
		// the test, add the missing key to myResourceTableFKProvider.getResourceForeignKeys()
		assertThat(myResourceTableFKProvider.getResourceForeignKeys(), containsInAnyOrder(expected.toArray()));
	}

}
