package ca.uhn.fhir.jpa.dao;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseHapiFhirDaoTest {

	@Test
	public void cleanProvenanceSourceUri() {
		assertEquals("", BaseHapiFhirDao.cleanProvenanceSourceUri(null));
		assertEquals("abc", BaseHapiFhirDao.cleanProvenanceSourceUri("abc"));
		assertEquals("abc", BaseHapiFhirDao.cleanProvenanceSourceUri("abc#def"));
		assertEquals("abc", BaseHapiFhirDao.cleanProvenanceSourceUri("abc#def#ghi"));
	}
}
