package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.util.TestUtil;
import org.junit.AfterClass;

public class ResourceProviderDstu2ConceptMapTest extends BaseResourceProviderR4Test {
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	// TODO: Test BaseJpaResourceProviderConceptMapDstu2 using BaseJpaResourceProviderConceptMapR4 implementation.
}
