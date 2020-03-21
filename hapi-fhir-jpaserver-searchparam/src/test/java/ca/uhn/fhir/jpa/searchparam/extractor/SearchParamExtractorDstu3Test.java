package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.util.StringNormalizer;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.SearchParamConstants;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Sets;
import org.hamcrest.Matchers;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.model.Duration;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SearchParamExtractorDstu3Test {

	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static IValidationSupport ourValidationSupport;

	@Test
	public void testParamWithOrInPath() {
		Observation obs = new Observation();
		obs.addCategory().addCoding().setSystem("SYSTEM").setCode("CODE");

		ISearchParamRegistry searchParamRegistry = new MySearchParamRegistry();

		SearchParamExtractorDstu3 extractor = new SearchParamExtractorDstu3(new ModelConfig(), ourCtx, ourValidationSupport, searchParamRegistry);
		extractor.start();
		Set<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(obs);
		assertEquals(1, tokens.size());
		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.iterator().next();
		assertEquals("category", token.getParamName());
		assertEquals("SYSTEM", token.getSystem());
		assertEquals("CODE", token.getValue());
	}

	@Test
	public void testNormalizedStringIsShortened() {
		// String with character that will change it's length on normalization
		String value = IntStream.range(1, 200).mapToObj(v -> "a").collect(Collectors.joining()) + "ئ";
		assertEquals(value.length(), 200);
		assertEquals(Normalizer.normalize(value, Normalizer.Form.NFD).length(), 201);
		assertEquals(StringNormalizer.normalizeString(value).length(), 201);

		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setDescription(value);

		ISearchParamRegistry searchParamRegistry = new MySearchParamRegistry();

		SearchParamExtractorDstu3 extractor = new SearchParamExtractorDstu3(new ModelConfig(), ourCtx, ourValidationSupport, searchParamRegistry);
		extractor.start();
		Set<ResourceIndexedSearchParamString> params = extractor.extractSearchParamStrings(questionnaire);
		assertEquals(1, params.size());
	}

	@Test
	public void testEncounterDuration_Normalized() {

		Encounter enc = new Encounter();
		Duration value = new Duration();
		value.setSystem(SearchParamConstants.UCUM_NS);
		value.setCode("min");
		value.setValue(2 * 24 * 60);
		enc.setLength(value);

		ISearchParamRegistry searchParamRegistry = new MySearchParamRegistry();

		SearchParamExtractorDstu3 extractor = new SearchParamExtractorDstu3(new ModelConfig(), ourCtx, ourValidationSupport, searchParamRegistry);
		extractor.start();
		Set<ResourceIndexedSearchParamNumber> params = extractor.extractSearchParamNumber(enc);
		assertEquals(1, params.size());
		// Normalized to days
		assertEquals("2", params.iterator().next().getValue().toPlainString());
	}

	@Test
	public void testEncounterDuration_NotNormalized() {

		Encounter enc = new Encounter();
		Duration value = new Duration();
		value.setValue(15);
		enc.setLength(value);

		ISearchParamRegistry searchParamRegistry = new MySearchParamRegistry();

		SearchParamExtractorDstu3 extractor = new SearchParamExtractorDstu3(new ModelConfig(), ourCtx, ourValidationSupport, searchParamRegistry);
		extractor.start();
		Set<ResourceIndexedSearchParamNumber> params = extractor.extractSearchParamNumber(enc);
		assertEquals(1, params.size());
		// Normalized to days
		assertEquals("15", params.iterator().next().getValue().toPlainString());
	}

	@Test
	public void testEmptyPath() {

		MySearchParamRegistry searchParamRegistry = new MySearchParamRegistry();
		SearchParamExtractorDstu3 extractor = new SearchParamExtractorDstu3(new ModelConfig(), ourCtx, ourValidationSupport, searchParamRegistry);
		extractor.start();

			searchParamRegistry.addSearchParam(new RuntimeSearchParam("foo", "foo", "", RestSearchParameterTypeEnum.STRING, Sets.newHashSet(), Sets.newHashSet(), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE));
			Patient resource = new Patient();
			extractor.extractSearchParamStrings(resource);

		searchParamRegistry.addSearchParam(new RuntimeSearchParam("foo", "foo", null, RestSearchParameterTypeEnum.STRING, Sets.newHashSet(), Sets.newHashSet(), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE));
		extractor.extractSearchParamStrings(resource);
	}


	@Test
	public void testStringMissingResourceType() {

		MySearchParamRegistry searchParamRegistry = new MySearchParamRegistry();
		SearchParamExtractorDstu3 extractor = new SearchParamExtractorDstu3(new ModelConfig(), ourCtx, ourValidationSupport, searchParamRegistry);
		extractor.start();

		searchParamRegistry.addSearchParam(new RuntimeSearchParam("foo", "foo", "communication.language.coding.system | communication.language.coding.code", RestSearchParameterTypeEnum.STRING, Sets.newHashSet(), Sets.newHashSet(), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE));
		Patient resource = new Patient();
		resource.getCommunicationFirstRep().getLanguage().getCodingFirstRep().setCode("blah");
		Set<ResourceIndexedSearchParamString> strings = extractor.extractSearchParamStrings(resource);
		assertEquals(1, strings.size());
		assertEquals("BLAH", strings.iterator().next().getValueNormalized());

	}


	@Test
	public void testInvalidType() {

		MySearchParamRegistry searchParamRegistry = new MySearchParamRegistry();
		SearchParamExtractorDstu3 extractor = new SearchParamExtractorDstu3(new ModelConfig(), ourCtx, ourValidationSupport, searchParamRegistry);
		extractor.start();

		{
			searchParamRegistry.addSearchParam(new RuntimeSearchParam("foo", "foo", "Patient", RestSearchParameterTypeEnum.STRING, Sets.newHashSet(), Sets.newHashSet(), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE));
			Patient resource = new Patient();
			ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamString> outcome = extractor.extractSearchParamStrings(resource);
			assertThat(outcome.getWarnings(), Matchers.contains("Search param foo is of unexpected datatype: class org.hl7.fhir.dstu3.model.Patient"));
		}
		{
			searchParamRegistry.addSearchParam(new RuntimeSearchParam("foo", "foo", "Patient", RestSearchParameterTypeEnum.TOKEN, Sets.newHashSet(), Sets.newHashSet(), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE));
			Patient resource = new Patient();
			ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> outcome = extractor.extractSearchParamTokens(resource);
			assertThat(outcome.getWarnings(), Matchers.contains("Search param foo is of unexpected datatype: class org.hl7.fhir.dstu3.model.Patient"));
		}
		{
			searchParamRegistry.addSearchParam(new RuntimeSearchParam("foo", "foo", "Patient", RestSearchParameterTypeEnum.QUANTITY, Sets.newHashSet(), Sets.newHashSet(), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE));
			Patient resource = new Patient();
			ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamQuantity> outcome = extractor.extractSearchParamQuantity(resource);
			assertThat(outcome.getWarnings(), Matchers.contains("Search param foo is of unexpected datatype: class org.hl7.fhir.dstu3.model.Patient"));
		}
		{
			searchParamRegistry.addSearchParam(new RuntimeSearchParam("foo", "foo", "Patient", RestSearchParameterTypeEnum.DATE, Sets.newHashSet(), Sets.newHashSet(), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE));
			Patient resource = new Patient();
			ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamDate> outcome = extractor.extractSearchParamDates(resource);
			assertThat(outcome.getWarnings(), Matchers.contains("Search param foo is of unexpected datatype: class org.hl7.fhir.dstu3.model.Patient"));
		}
		{
			searchParamRegistry.addSearchParam(new RuntimeSearchParam("foo", "foo", "Patient", RestSearchParameterTypeEnum.NUMBER, Sets.newHashSet(), Sets.newHashSet(), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE));
			Patient resource = new Patient();
			ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamNumber> outcome = extractor.extractSearchParamNumber(resource);
			assertThat(outcome.getWarnings(), Matchers.contains("Search param foo is of unexpected datatype: class org.hl7.fhir.dstu3.model.Patient"));
		}
		{
			searchParamRegistry.addSearchParam(new RuntimeSearchParam("foo", "foo", "Patient", RestSearchParameterTypeEnum.URI, Sets.newHashSet(), Sets.newHashSet(), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE));
			Patient resource = new Patient();
			ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamUri> outcome = extractor.extractSearchParamUri(resource);
			assertThat(outcome.getWarnings(), Matchers.contains("Search param foo is of unexpected datatype: class org.hl7.fhir.dstu3.model.Patient"));
		}
	}

	@Test
	public void testParamCoords() {
		Location loc = new Location();
		double latitude = 40.0;
		double longitude = 80.0;
		Location.LocationPositionComponent position = new Location.LocationPositionComponent().setLatitude(latitude).setLongitude(longitude);
		loc.setPosition(position);

		ISearchParamRegistry searchParamRegistry = new MySearchParamRegistry();

		SearchParamExtractorDstu3 extractor = new SearchParamExtractorDstu3(new ModelConfig(), ourCtx, ourValidationSupport, searchParamRegistry);
		extractor.start();
		ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> coords = extractor.extractSearchParamTokens(loc);
		assertEquals(1, coords.size());
		ResourceIndexedSearchParamCoords coord = (ResourceIndexedSearchParamCoords) coords.iterator().next();
		assertEquals(latitude, coord.getLatitude(), 0.0);
		assertEquals(longitude, coord.getLongitude(), 0.0);
	}

	private static class MySearchParamRegistry implements ISearchParamRegistry {

		private List<RuntimeSearchParam> myAddedSearchParams = new ArrayList<>();

		public void addSearchParam(RuntimeSearchParam... theSearchParam) {
			myAddedSearchParams.clear();
			for (RuntimeSearchParam next : theSearchParam) {
				myAddedSearchParams.add(next);
			}
		}

		@Override
		public void forceRefresh() {
			// nothing
		}

		@Override
		public RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean refreshCacheIfNecessary() {
			// nothing
			return false;
		}

		@Override
		public Map<String, Map<String, RuntimeSearchParam>> getActiveSearchParams() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Map<String, RuntimeSearchParam> getActiveSearchParams(String theResourceName) {
			RuntimeResourceDefinition nextResDef = ourCtx.getResourceDefinition(theResourceName);
			Map<String, RuntimeSearchParam> sps = new HashMap<>();
			for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
				sps.put(nextSp.getName(), nextSp);
			}
			for (RuntimeSearchParam next : myAddedSearchParams) {
				sps.put(next.getName(), next);
			}
			return sps;
		}

		@Override
		public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName, Set<String> theParamNames) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void requestRefresh() {
			// nothing
		}

		@Override
		public RuntimeSearchParam getSearchParamByName(RuntimeResourceDefinition theResourceDef, String theParamName) {
			return null;
		}

		@Override
		public Collection<RuntimeSearchParam> getSearchParamsByResourceType(RuntimeResourceDefinition theResourceDef) {
			return null;
		}
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() {
		ourValidationSupport = new DefaultProfileValidationSupport(ourCtx);
	}

}
