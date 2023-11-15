package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.util.Dstu3DistanceHelper;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestDstu3Config;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDstu3Config.class})
public class MatchUrlServiceTest extends BaseJpaTest {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();

	@Autowired
	MatchUrlService myMatchUrlService;

	@Test
	public void testTranslateMatchUrl() {
		RuntimeResourceDefinition resourceDef = ourCtx.getResourceDefinition(Condition.class);
		ISearchParamRegistry searchParamRegistry = mock(ISearchParamRegistry.class);
		when(searchParamRegistry.getActiveSearchParam(any(), eq("patient"))).thenReturn(resourceDef.getSearchParam("patient"));
		SearchParameterMap match = myMatchUrlService.translateMatchUrl("Condition?patient=304&_lastUpdated=>2011-01-01T11:12:21.0000Z", resourceDef);
		assertEquals("2011-01-01T11:12:21.0000Z", match.getLastUpdated().getLowerBound().getValueAsString());
		assertEquals(ReferenceParam.class, match.get("patient").get(0).get(0).getClass());
		assertEquals("304", ((ReferenceParam) match.get("patient").get(0).get(0)).getIdPart());
	}

	@Test
	public void testParseNearDistance() {
		double kmDistance = 123.4;

		SearchParameterMap map = myMatchUrlService.translateMatchUrl(
			"Location?" +
				Location.SP_NEAR + "=1000.0:2000.0" +
				"&" +
				Location.SP_NEAR_DISTANCE + "=" + kmDistance + "|http://unitsofmeasure.org|km", ourCtx.getResourceDefinition("Location"));
		Dstu3DistanceHelper.setNearDistance(Location.class, map);

		QuantityParam nearDistanceParam = map.getNearDistanceParam();
		assertEquals(1, map.size());
		assertNotNull(nearDistanceParam);
		assertEquals(kmDistance, nearDistanceParam.getValue().doubleValue(), 0.0);
	}

	@Test
	public void testTwoDistancesAnd() {
		try {
			SearchParameterMap map = myMatchUrlService.translateMatchUrl(
				"Location?" +
					Location.SP_NEAR_DISTANCE + "=1|http://unitsofmeasure.org|km" +
					"&" +
					Location.SP_NEAR_DISTANCE + "=2|http://unitsofmeasure.org|km",
				ourCtx.getResourceDefinition("Location"));
			Dstu3DistanceHelper.setNearDistance(Location.class, map);

			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(495) + "Only one " + Location.SP_NEAR_DISTANCE + " parameter may be present", e.getMessage());
		}
	}

	@Test
	public void testTwoDistancesOr() {
		try {
			SearchParameterMap map = myMatchUrlService.translateMatchUrl(
				"Location?" +
					Location.SP_NEAR_DISTANCE + "=1|http://unitsofmeasure.org|km" +
					"," +
					"2|http://unitsofmeasure.org|km",
				ourCtx.getResourceDefinition("Location"));
			Dstu3DistanceHelper.setNearDistance(Location.class, map);

			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(495) + "Only one " + Location.SP_NEAR_DISTANCE + " parameter may be present", e.getMessage());
		}
	}

	@Override
	protected FhirContext getFhirContext() {
		return ourCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return null;
	}


}
