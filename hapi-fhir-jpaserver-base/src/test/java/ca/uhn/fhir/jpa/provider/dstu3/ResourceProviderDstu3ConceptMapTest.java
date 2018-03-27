package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.util.TestUtil;
import org.junit.AfterClass;

public class ResourceProviderDstu3ConceptMapTest extends BaseResourceProviderR4Test {
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	// TODO: Test BaseJpaResourceProviderConceptMapDstu3 using BaseJpaResourceProviderConceptMapR4 implementation.
}
