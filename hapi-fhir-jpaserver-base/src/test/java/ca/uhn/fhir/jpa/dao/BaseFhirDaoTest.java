package ca.uhn.fhir.jpa.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.ResourceParameter;

public class BaseFhirDaoTest  extends BaseJpaTest {

	private static FhirContext ourCtx = FhirContext.forDstu2();
	
	@Test
	public void testTranslateMatchUrl() {
		SearchParameterMap match = BaseHapiFhirDao.translateMatchUrl("Condition?patient=304&_lastUpdated=>2011-01-01T11:12:21.0000Z", ourCtx.getResourceDefinition(Condition.class));
		assertEquals("2011-01-01T11:12:21.0000Z", match.getLastUpdated().getLowerBound().getValueAsString());
		assertEquals(ReferenceParam.class, match.get("patient").get(0).get(0).getClass());
		assertEquals("304", ((ReferenceParam)match.get("patient").get(0).get(0)).getIdPart());
		
		Observation observation = new Observation();
		
		PeriodDt period = new PeriodDt();
		period.setStart(new DateTimeDt("2011-01-02T11:22:33Z"));
		period.setEnd(new DateTimeDt("2011-01-02T11:33:33Z"));
		observation.setEffective(period);
		
	}
	
}
