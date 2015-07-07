package ca.uhn.fhir.jpa.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Condition;

public class BaseFhirDaoTest  extends BaseJpaTest {

	private static FhirContext ourCtx = FhirContext.forDstu2();
	
	@Test
	public void testTranslateMatchUrl() {
		SearchParameterMap match = BaseHapiFhirDao.translateMatchUrl("Condition?subject=304&_lastUpdated=>2011-01-01T11:12:21.0000Z", ourCtx.getResourceDefinition(Condition.class));
		assertEquals("2011-01-01T11:12:21.0000Z", match.getLastUpdated().getLowerBound().getValueAsString());
	}
	
}
