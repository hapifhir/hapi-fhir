package ca.uhn.fhir.jpa.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.util.TestUtil;

public class BaseHapiFhirDaoTest  extends BaseJpaTest {

	private static FhirContext ourCtx = FhirContext.forDstu2();

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Test
	public void testTranslateMatchUrl() {
		RuntimeResourceDefinition resourceDef = ourCtx.getResourceDefinition(Condition.class);
		
		IDao dao = mock(IDao.class);
		when(dao.getSearchParamByName(any(RuntimeResourceDefinition.class), eq("patient"))).thenReturn(resourceDef.getSearchParam("patient"));
				
		SearchParameterMap match = BaseHapiFhirDao.translateMatchUrl(dao, ourCtx, "Condition?patient=304&_lastUpdated=>2011-01-01T11:12:21.0000Z", resourceDef);
		assertEquals("2011-01-01T11:12:21.0000Z", match.getLastUpdated().getLowerBound().getValueAsString());
		assertEquals(ReferenceParam.class, match.get("patient").get(0).get(0).getClass());
		assertEquals("304", ((ReferenceParam)match.get("patient").get(0).get(0)).getIdPart());
		
		Observation observation = new Observation();
		
		PeriodDt period = new PeriodDt();
		period.setStart(new DateTimeDt("2011-01-02T11:22:33Z"));
		period.setEnd(new DateTimeDt("2011-01-02T11:33:33Z"));
		observation.setEffective(period);
		
	}
	
	@Test
	public void testNormalizeString() {
		assertEquals("TEST TEST", BaseHapiFhirDao.normalizeString("TEST teSt"));
		assertEquals("AEIØU", BaseHapiFhirDao.normalizeString("åéîøü"));
		assertEquals("杨浩", BaseHapiFhirDao.normalizeString("杨浩"));
	}


	@Override
	protected FhirContext getContext() {
		return ourCtx;
	}
	
}
