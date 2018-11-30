package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.model.util.StringNormalizer;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.util.TestUtil;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestR4Config.class})
public class BaseHapiFhirDaoTest  extends BaseJpaTest {

	private static FhirContext ourCtx = FhirContext.forDstu2();

	@Autowired
	MatchUrlService myMatchUrlService;

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testTranslateMatchUrl() {
		RuntimeResourceDefinition resourceDef = ourCtx.getResourceDefinition(Condition.class);
		ISearchParamRegistry searchParamRegistry = mock(ISearchParamRegistry.class);
		when(searchParamRegistry.getSearchParamByName(any(RuntimeResourceDefinition.class), eq("patient"))).thenReturn(resourceDef.getSearchParam("patient"));
		SearchParameterMap match = myMatchUrlService.translateMatchUrl("Condition?patient=304&_lastUpdated=>2011-01-01T11:12:21.0000Z", resourceDef);
		assertEquals("2011-01-01T11:12:21.0000Z", match.getLastUpdated().getLowerBound().getValueAsString());
		assertEquals(ReferenceParam.class, match.get("patient").get(0).get(0).getClass());
		assertEquals("304", ((ReferenceParam)match.get("patient").get(0).get(0)).getIdPart());
		
		Observation observation = new Observation();
		
		PeriodDt period = new PeriodDt();
		period.setStart(new DateTimeDt("2011-01-02T11:22:33Z"));
		period.setEnd(new DateTimeDt("2011-01-02T11:33:33Z"));
		observation.setEffective(period);
		
	}

	@Override
	protected FhirContext getContext() {
		return ourCtx;
	}
	@Override
	protected PlatformTransactionManager getTxManager() {
		return null;
	}

}
