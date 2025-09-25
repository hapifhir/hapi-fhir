package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ResourceIdentifierCacheSvcImplR5Test extends BaseJpaR5Test {

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testAddSystem(boolean theUseCacheForSecondRead) {
		// First read
		long foo = runInTransaction(() -> myResourceIdentifierCacheSvc.getOrCreateResourceIdentifierSystem("http://foo"));
		assertThat(foo).isGreaterThan(0);
		long bar = runInTransaction(() -> myResourceIdentifierCacheSvc.getOrCreateResourceIdentifierSystem("http://bar"));
		assertThat(bar).isGreaterThan(0);
		assertNotEquals(foo, bar);

		if (!theUseCacheForSecondRead) {
			myMemoryCacheSvc.invalidateAllCaches();
		}

		// Second Read
		ourLog.info("About to perform pass 2");
		myCaptureQueriesListener.clear();
		long fooPass2 = runInTransaction(() -> myResourceIdentifierCacheSvc.getOrCreateResourceIdentifierSystem("http://foo"));
		long barPass2 = runInTransaction(() -> myResourceIdentifierCacheSvc.getOrCreateResourceIdentifierSystem("http://bar"));

		assertEquals(foo, fooPass2);
		assertEquals(bar, barPass2);

		if (theUseCacheForSecondRead) {
			assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		} else {
			assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		}
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testGetFhirIdAssociatedWithUniquePatientIdentifier(boolean theUseCacheForSecondRead) {
		// First read
		String foo = runInTransaction(() -> myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier("http://foo", "foo", ()->"FOO"));
		assertEquals("FOO", foo);
		String bar = runInTransaction(() -> myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier("http://bar", "bar", ()->"BAR"));
		assertEquals("BAR", bar);

		if (!theUseCacheForSecondRead) {
			myMemoryCacheSvc.invalidateAllCaches();
		}

		// Second Read
		ourLog.info("About to perform pass 2");
		myCaptureQueriesListener.clear();
		String fooPass2 = runInTransaction(() -> myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier("http://foo", "foo", ()->"FOO"));
		assertEquals("FOO", fooPass2);
		String barPass2 = runInTransaction(() -> myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier("http://bar", "bar", ()->"BAR"));
		assertEquals("BAR", barPass2);

		if (theUseCacheForSecondRead) {
			assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		} else {
			// 2 for identifier system entity, 2 for patient identifier entity
			assertEquals(4, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		}
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
	}

}
