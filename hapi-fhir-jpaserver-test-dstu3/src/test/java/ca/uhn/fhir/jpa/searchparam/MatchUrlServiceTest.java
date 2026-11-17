package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.util.Dstu3DistanceHelper;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestDstu3Config;
import ca.uhn.fhir.rest.api.SearchIncludeDeletedEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
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
		when(searchParamRegistry.getActiveSearchParam(any(), eq("patient"), any())).thenReturn(resourceDef.getSearchParam("patient"));
		SearchParameterMap match = myMatchUrlService.translateMatchUrl("Condition?patient=304&_lastUpdated=>2011-01-01T11:12:21.0000Z", resourceDef);
		assertEquals("2011-01-01T11:12:21.0000Z", match.getLastUpdated().getLowerBound().getValueAsString());
		assertEquals(ReferenceParam.class, match.get("patient").get(0).get(0).getClass());
		assertEquals("304", ((ReferenceParam) match.get("patient").get(0).get(0)).getIdPart());
	}

	@Test
	public void testTranslateMatchUrl_NoResourceDefinition() {
		RuntimeResourceDefinition resourceDef = ourCtx.getResourceDefinition(Condition.class);
		ISearchParamRegistry searchParamRegistry = mock(ISearchParamRegistry.class);
		when(searchParamRegistry.getActiveSearchParam(any(), eq("patient"), any())).thenReturn(resourceDef.getSearchParam("patient"));
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
		assertThat(nearDistanceParam.getValue().doubleValue()).isCloseTo(kmDistance, within(0.0));
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

			fail("");
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

			fail("");
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(495) + "Only one " + Location.SP_NEAR_DISTANCE + " parameter may be present", e.getMessage());
		}
	}

	@Test
	void testTotal_fromStandardLowerCase() {
		// given
		// when
		var map = myMatchUrlService.translateMatchUrl("Patient?family=smith&_total=none", ourCtx.getResourceDefinition("Patient"));

		// then
		assertEquals(SearchTotalModeEnum.NONE, map.getSearchTotalMode());
	}

	@Test
	void testTotal_fromUpperCase() {
		// given
		// when
		var map = myMatchUrlService.translateMatchUrl("Patient?family=smith&_total=NONE", ourCtx.getResourceDefinition("Patient"));

		// then
		assertEquals(SearchTotalModeEnum.NONE, map.getSearchTotalMode());
	}

	@Test
	void testIncludeDeleted_combinedWithInvalidSP_throwsException(){
		String url = "Patient?_includeDeleted=both&name=acme";

		try {
			myMatchUrlService.translateMatchUrl(url, ourCtx.getResourceDefinition("Patient"));
			fail();
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).contains("HAPI-2744: The _includeDeleted parameter is only compatible with the following parameters:");
		}
	}

	@Test
	void testIncludeDeleted_combinedWithWhiteListedSP_isParsed(){

		String url = "Patient?_includeDeleted=both&_lastUpdated=gt2020-01-01&_id=123";
		var map = myMatchUrlService.translateMatchUrl(url, ourCtx.getResourceDefinition("Patient"));

		assertThat(map.getSearchIncludeDeletedMode()).isEqualTo(SearchIncludeDeletedEnum.BOTH);
		assertThat(map.getLastUpdated()).isNotNull();
		assertThat(map.getLastUpdated().getLowerBound().getValueAsString()).isEqualTo("2020-01-01");
		assertThat(map.containsKey("_id")).isTrue();
		assertThat(map.get("_id").get(0).get(0)).isEqualTo(new StringParam("123"));
	}

	@Test
	void testCompartmentLastUpdated_parsedFromMatchUrl() {
		var map = myMatchUrlService.translateMatchUrl(
				"Patient?_compartmentLastUpdated=ge2024-01-15", ourCtx.getResourceDefinition("Patient"));

		assertThat(map.getCompartmentLastUpdated())
				.isNotNull()
				.returns("2024-01-15", r -> r.getLowerBound().getValueAsString())
				.returns(null, DateRangeParam::getUpperBound);
	}

	@Test
	void testCompartmentLastUpdated_rangeFromMatchUrl() {
		var map = myMatchUrlService.translateMatchUrl(
				"Patient?_compartmentLastUpdated=ge2024-01-01&_compartmentLastUpdated=le2024-01-31",
				ourCtx.getResourceDefinition("Patient"));

		assertThat(map.getCompartmentLastUpdated())
				.isNotNull()
				.satisfies(range -> {
					assertThat(range.getLowerBound().getValueAsString()).isEqualTo("2024-01-01");
					assertThat(range.getUpperBound().getValueAsString()).isEqualTo("2024-01-31");
				});
	}

	@Test
	void testCompartmentLastUpdated_moreThanTwoRepetitions_throwsException() {
		assertThatThrownBy(() -> myMatchUrlService.translateMatchUrl(
				"Patient?_compartmentLastUpdated=ge2024-01-01&_compartmentLastUpdated=le2024-01-31&_compartmentLastUpdated=ge2024-02-01",
				ourCtx.getResourceDefinition("Patient")))
				.hasMessageStartingWith(Msg.code(2877))
				.hasMessageContaining("Can not have more than 2");
	}

	@Test
	void testLastUpdated_RangeStillParsedAsRange() {
		SearchParameterMap map = myMatchUrlService.translateMatchUrl(
			"Patient?_lastUpdated=ge2020-01-01&_lastUpdated=lt2021-01-01T10:00:00.000Z", ourCtx.getResourceDefinition("Patient"));

		DateRangeParam expected = new DateRangeParam();
		expected.setValuesAsQueryTokens(ourCtx, "_lastUpdated", List.of(
			QualifiedParamList.singleton("ge2020-01-01"), QualifiedParamList.singleton("lt2021-01-01T10:00:00.000Z")));
		assertThat(map.getLastUpdated()).isEqualTo(expected);
		assertThat(map.getLastUpdated().getClass()).isEqualTo(DateRangeParam.class);
	}

	@Test
	void testLastUpdated_MoreThanTwoRepetitions() {
		SearchParameterMap map = myMatchUrlService.translateMatchUrl(
			"Patient?_lastUpdated=gt2020&_lastUpdated=gt2021&_lastUpdated=lt2025&_lastUpdated=ne2023",
			ourCtx.getResourceDefinition("Patient"));

		assertThat(map.hasLastUpdated()).isTrue();
		assertThat(map.isLastUpdatedRepresentableAsRange()).isFalse();
		assertThatThrownBy(map::getLastUpdated).hasMessageStartingWith(Msg.code(3075));
		assertThat(toTokens(map.getLastUpdatedAndList()))
			.containsExactly(List.of("gt2020"), List.of("gt2021"), List.of("lt2025"), List.of("ne2023"));
	}

	@Test
	void testLastUpdated_Or() {
		SearchParameterMap map = myMatchUrlService.translateMatchUrl(
			"Patient?_lastUpdated=lt2020,gt2021,eq2020-06-01&_lastUpdated=ne2022", ourCtx.getResourceDefinition("Patient"));

		assertThat(map.isLastUpdatedRepresentableAsRange()).isFalse();
		assertThat(toTokens(map.getLastUpdatedAndList()))
			.containsExactly(List.of("lt2020", "gt2021", "eq2020-06-01"), List.of("ne2022"));
		assertThat(map.toNormalizedQueryString(ourCtx)).isEqualTo("?_lastUpdated=eq2020-06-01,gt2021,lt2020&_lastUpdated=ne2022");
	}

	@Test
	void testLastUpdated_Approximate_Rejected() {
		assertThatThrownBy(() -> myMatchUrlService.translateMatchUrl(
			"Patient?_lastUpdated=ap2020,gt2021", ourCtx.getResourceDefinition("Patient")))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageStartingWith(Msg.code(1925));
		assertThatThrownBy(() -> myMatchUrlService.translateMatchUrl(
			"Patient?_lastUpdated=ap2020", ourCtx.getResourceDefinition("Patient")))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageStartingWith(Msg.code(1925));
	}

	@Test
	void testLastUpdated_TimezoneOffsets() {
		// 2020-01-01T10:00:00+03:00 and 2020-01-01T03:00:00-04:00 are both 07:00Z
		Date expected = Date.from(java.time.Instant.parse("2020-01-01T07:00:00Z"));
		for (String plus : List.of("%2B", "+")) {
			SearchParameterMap map = myMatchUrlService.translateMatchUrl(
				"Patient?_lastUpdated=eq2020-01-01T10:00:00" + plus + "03:00,eq2020-01-01T03:00:00-04:00,eq2020-01-01T07:00:00Z",
				ourCtx.getResourceDefinition("Patient"));
			List<DateParam> values = map.getLastUpdatedAndList().getValuesAsQueryTokens().get(0).getValuesAsQueryTokens();
			assertThat(values).hasSize(3);
			for (DateParam next : values) {
				assertThat(next.getValue()).as(plus + " " + next.getValueAsQueryToken()).isEqualTo(expected);
			}

			// Single value: a range, as before
			map = myMatchUrlService.translateMatchUrl(
				"Patient?_lastUpdated=gt2020-01-01T10:00:00" + plus + "03:00", ourCtx.getResourceDefinition("Patient"));
			assertThat(map.getLastUpdated().getLowerBound().getValue()).as(plus).isEqualTo(expected);
		}
	}

	@Test
	void testLastUpdated_Missing() {
		SearchParameterMap map = myMatchUrlService.translateMatchUrl(
			"Patient?_lastUpdated:missing=true", ourCtx.getResourceDefinition("Patient"));
		DateRangeParam expected = new DateRangeParam();
		expected.setValuesAsQueryTokens(ourCtx, "_lastUpdated", List.of(QualifiedParamList.singleton(":missing", "true")));
		SearchParameterMap expectedMap = new SearchParameterMap();
		expectedMap.setLastUpdated(expected);
		assertThat(map.getLastUpdated()).isEqualTo(expectedMap.getLastUpdated());
	}

	@Test
	void testLastUpdated_OnlyEmptyValues_NoFilter() {
		for (String url : List.of("Patient?_lastUpdated=,&_lastUpdated=,", "Patient?_lastUpdated=", "Patient?_lastUpdated=,")) {
			SearchParameterMap map = myMatchUrlService.translateMatchUrl(url, ourCtx.getResourceDefinition("Patient"));
			assertThat(map.hasLastUpdated()).as(url).isFalse();
			assertThat(map.getLastUpdated()).as(url).isNull();
			assertThat(map.isEmpty()).as(url).isTrue();
		}
	}

	@Test
	void testLastUpdated_EncodedName() {
		SearchParameterMap map = myMatchUrlService.translateMatchUrl(
			"Patient?%5FlastUpdated=lt2020,gt2021", ourCtx.getResourceDefinition("Patient"));
		assertThat(map.hasLastUpdated()).isTrue();
		assertThat(map.isLastUpdatedRepresentableAsRange()).isFalse();
	}

	private static List<List<String>> toTokens(DateAndListParam theAndList) {
		List<List<String>> retVal = new ArrayList<>();
		for (DateOrListParam nextOr : theAndList.getValuesAsQueryTokens()) {
			List<String> group = new ArrayList<>();
			for (DateParam next : nextOr.getValuesAsQueryTokens()) {
				group.add(next.getValueAsQueryToken());
			}
			retVal.add(group);
		}
		return retVal;
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
