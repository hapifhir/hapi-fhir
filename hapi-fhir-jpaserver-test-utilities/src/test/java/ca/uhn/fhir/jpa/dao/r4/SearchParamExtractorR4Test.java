package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.PathAndRef;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR4;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.util.HapiExtensions;
import com.google.common.collect.Sets;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SearchParamExtractorR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamExtractorR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private FhirContextSearchParamRegistry mySearchParamRegistry;
	private PartitionSettings myPartitionSettings;

	@BeforeEach
	public void before() {

		mySearchParamRegistry = new FhirContextSearchParamRegistry(ourCtx);
		myPartitionSettings = new PartitionSettings();

	}

	@Test
	public void testParamWithOrInPath() {
		Observation obs = new Observation();
		obs.addCategory().addCoding().setSystem("SYSTEM").setCode("CODE");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		Set<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(obs);
		assertEquals(1, tokens.size());
		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.iterator().next();
		assertEquals("category", token.getParamName());
		assertEquals("SYSTEM", token.getSystem());
		assertEquals("CODE", token.getValue());
	}

	@Test
	public void testName() {
		Patient patient = new Patient();
		List<StringType> suffixStrings = Arrays.asList(new StringType("the Great"));
		List<StringType> prefixStrings = Arrays.asList(new StringType("King"));
		HumanName humanName = patient.addName();
		humanName.addGiven("Jimmy");
		humanName.setFamily("Jones");
		humanName.setText("King Jimmy Jones the Great");
		humanName.setSuffix(suffixStrings);
		humanName.setPrefix(prefixStrings);
		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamString> stringSearchParams = extractor.extractSearchParamStrings(patient);
		List<String> nameValues = stringSearchParams.stream().filter(param -> "name".equals(param.getParamName())).map(ResourceIndexedSearchParamString::getValueExact).collect(Collectors.toList());
		assertThat(nameValues, containsInAnyOrder("Jimmy", "Jones", "King Jimmy Jones the Great", "King", "the Great"));
	}

	@Test
	public void testTokenOnSearchParamContext() {
		SearchParameter sp = new SearchParameter();
		sp.addUseContext().setCode(new Coding().setSystem("http://system").setCode("code"));

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		Set<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(sp);
		assertEquals(1, tokens.size());
		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.iterator().next();
		assertEquals("context-type", token.getParamName());
		assertEquals("http://system", token.getSystem());
		assertEquals("code", token.getValue());
	}

	@Test
	public void testTokenText_Enabled_Coding() {
		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("http://system").setCode("code").setDisplay("Help Im a Bug");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), myPartitionSettings, ourCtx, mySearchParamRegistry);

		List<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(obs)
			.stream()
			.filter(t -> t.getParamName().equals("code"))
			.sorted(comparing(o -> o.getClass().getName()).reversed())
			.collect(Collectors.toList());
		assertEquals(2, tokens.size());

		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.get(0);
		assertEquals("code", token.getParamName());
		assertEquals("http://system", token.getSystem());
		assertEquals("code", token.getValue());

		ResourceIndexedSearchParamString string = (ResourceIndexedSearchParamString) tokens.get(1);
		assertEquals("code", string.getParamName());
		assertEquals("Help Im a Bug", string.getValueExact());
	}

	@Test
	public void testTokenText_DisabledInSearchParam_Coding() {
		RuntimeSearchParam existingCodeSp = mySearchParamRegistry.getActiveSearchParams("Observation").get("code");
		RuntimeSearchParam codeSearchParam = new RuntimeSearchParam(existingCodeSp);
		codeSearchParam.addExtension(HapiExtensions.EXT_SEARCHPARAM_TOKEN_SUPPRESS_TEXT_INDEXING, new Extension(HapiExtensions.EXT_SEARCHPARAM_TOKEN_SUPPRESS_TEXT_INDEXING, new BooleanType(true)));
		mySearchParamRegistry.addSearchParam(codeSearchParam);

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("http://system").setCode("code").setDisplay("Help Im a Bug");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), myPartitionSettings, ourCtx, mySearchParamRegistry);

		List<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(obs)
			.stream()
			.filter(t -> t.getParamName().equals("code"))
			.sorted(comparing(o -> o.getClass().getName()).reversed())
			.collect(Collectors.toList());
		assertEquals(1, tokens.size());

		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.get(0);
		assertEquals("code", token.getParamName());
		assertEquals("http://system", token.getSystem());
		assertEquals("code", token.getValue());

	}

	@Test
	public void testTokenText_DisabledInModelConfig_Coding() {
		ModelConfig modelConfig = new ModelConfig();
		modelConfig.setSuppressStringIndexingInTokens(true);

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("http://system").setCode("code").setDisplay("Help Im a Bug");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(modelConfig, myPartitionSettings, ourCtx, mySearchParamRegistry);

		List<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(obs)
			.stream()
			.filter(t -> t.getParamName().equals("code"))
			.sorted(comparing(o -> o.getClass().getName()).reversed())
			.collect(Collectors.toList());
		assertEquals(1, tokens.size());

		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.get(0);
		assertEquals("code", token.getParamName());
		assertEquals("http://system", token.getSystem());
		assertEquals("code", token.getValue());

	}

	@Test
	public void testTokenText_DisabledInModelConfigButForcedInSearchParam_Coding() {
		ModelConfig modelConfig = new ModelConfig();
		modelConfig.setSuppressStringIndexingInTokens(true);

		RuntimeSearchParam existingCodeSp = mySearchParamRegistry.getActiveSearchParams("Observation").get("code");
		RuntimeSearchParam codeSearchParam = new RuntimeSearchParam(existingCodeSp);
		codeSearchParam.addExtension(HapiExtensions.EXT_SEARCHPARAM_TOKEN_SUPPRESS_TEXT_INDEXING, new Extension(HapiExtensions.EXT_SEARCHPARAM_TOKEN_SUPPRESS_TEXT_INDEXING, new BooleanType(false)));
		mySearchParamRegistry.addSearchParam(codeSearchParam);

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("http://system").setCode("code").setDisplay("Help Im a Bug");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(modelConfig, myPartitionSettings, ourCtx, mySearchParamRegistry);

		List<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(obs)
			.stream()
			.filter(t -> t.getParamName().equals("code"))
			.sorted(comparing(o -> o.getClass().getName()).reversed())
			.collect(Collectors.toList());
		assertEquals(2, tokens.size());

		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.get(0);
		assertEquals("code", token.getParamName());
		assertEquals("http://system", token.getSystem());
		assertEquals("code", token.getValue());

		ResourceIndexedSearchParamString string = (ResourceIndexedSearchParamString) tokens.get(1);
		assertEquals("code", string.getParamName());
		assertEquals("Help Im a Bug", string.getValueExact());
	}


	@Test
	public void testReferenceWithResolve() {
		Encounter enc = new Encounter();
		enc.addLocation().setLocation(new Reference("Location/123"));

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam("Encounter", "location");
		assertNotNull(param);
		ISearchParamExtractor.SearchParamSet<PathAndRef> links = extractor.extractResourceLinks(enc, false);
		assertEquals(1, links.size());
		assertEquals("location", links.iterator().next().getSearchParamName());
		assertEquals("Encounter.location.location", links.iterator().next().getPath());
		assertEquals("Location/123", ((Reference) links.iterator().next().getRef()).getReference());
	}

	@Test
	public void testReferenceWithResolveMulti() {
		Consent consent = new Consent();
		consent.setSource(new Reference().setReference("Consent/999"));

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam("Consent", Consent.SP_SOURCE_REFERENCE);
		assertNotNull(param);
		ISearchParamExtractor.SearchParamSet<PathAndRef> links = extractor.extractResourceLinks(consent, false);
		assertEquals(1, links.size());
		assertEquals("Consent.source", links.iterator().next().getPath());
		assertEquals("Consent/999", ((Reference) links.iterator().next().getRef()).getReference());
	}


	@Test
	public void testExtractSearchParamTokenTest() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("sys").setValue("val");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam("Patient", Patient.SP_IDENTIFIER);
		assertNotNull(param);
		ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> params = extractor.extractSearchParamTokens(p, param);
		assertEquals(1, params.size());
		ResourceIndexedSearchParamToken paramValue = (ResourceIndexedSearchParamToken) params.iterator().next();
		assertEquals("identifier", paramValue.getParamName());
		assertEquals("sys", paramValue.getSystem());
		assertEquals("val", paramValue.getValue());
	}


	@Test
	public void testExtensionContainingReference() {
		String path = "Patient.extension('http://patext').value.as(Reference)";

		RuntimeSearchParam sp = new RuntimeSearchParam(null, null, "extpat", "Patient SP", path, RestSearchParameterTypeEnum.REFERENCE, new HashSet<>(), Sets.newHashSet("Patient"), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, null, null, null);
		mySearchParamRegistry.addSearchParam(sp);

		Patient patient = new Patient();
		patient.addExtension("http://patext", new Reference("Organization/AAA"));

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		ISearchParamExtractor.SearchParamSet<PathAndRef> links = extractor.extractResourceLinks(patient, false);
		assertEquals(1, links.size());

	}

	@Test
	public void testExtractComponentQuantities() {
		Observation o1 = new Observation();
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code1")))
			.setValue(new Quantity().setSystem("http://bar").setCode("code1").setValue(200));
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code2")))
			.setValue(new Quantity().setSystem("http://bar").setCode("code2").setValue(200));

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		Set<ResourceIndexedSearchParamQuantity> links = extractor.extractSearchParamQuantity(o1);
		ourLog.info("Links:\n  {}", links.stream().map(t -> t.toString()).collect(Collectors.joining("\n  ")));
		assertEquals(4, links.size());
	}

	@Test
	public void testExtractComponentQuantityWithNormalizedQuantitySearchSupported() {

		ModelConfig modelConfig = new ModelConfig();

		modelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);

		Observation o1 = new Observation();
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code1")))
			.setValue(new Quantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm").setValue(200));

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(modelConfig, new PartitionSettings(), ourCtx, mySearchParamRegistry);
		Set<ResourceIndexedSearchParamQuantityNormalized> links = extractor.extractSearchParamQuantityNormalized(o1);
		ourLog.info("Links:\n  {}", links.stream().map(t -> t.toString()).collect(Collectors.joining("\n  ")));
		assertEquals(2, links.size());

	}

	@Test
	public void testExtractComponentQuantityValueWithNormalizedQuantitySearchSupported() {

		ModelConfig modelConfig = new ModelConfig();

		modelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);

		Observation o1 = new Observation();

		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code1")))
			.setValue(new Quantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm").setValue(200));

		RuntimeSearchParam existingCodeSp = mySearchParamRegistry.getActiveSearchParams("Observation").get("component-value-quantity");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(modelConfig, new PartitionSettings(), ourCtx, mySearchParamRegistry);
		List<String> list = extractor.extractParamValuesAsStrings(existingCodeSp, o1);

		assertEquals(2, list.size());
	}

	@Test
	public void testExtractIdentifierOfType() {

		ModelConfig modelConfig = new ModelConfig();
		modelConfig.setIndexIdentifierOfType(true);

		Patient patient = new Patient();
		patient
			.addIdentifier()
			.setSystem("http://foo1")
			.setValue("bar1")
			.getType()
			.addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
			.setCode("MR");
		patient
			.addIdentifier()
			.setSystem("http://foo2")
			.setValue("bar2")
			.getType()
			.addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
			.setCode("MR");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(modelConfig, new PartitionSettings(), ourCtx, mySearchParamRegistry);
		List<ResourceIndexedSearchParamToken> list = extractor
			.extractSearchParamTokens(patient)
			.stream()
			.map(t->(ResourceIndexedSearchParamToken)t)
			.collect(Collectors.toList());
		list.forEach(t->t.calculateHashes());
		ourLog.info("Found tokens:\n * {}", list.stream().map(t->t.toString()).collect(Collectors.joining("\n * ")));

		assertThat(list, containsInAnyOrder(
			new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "deceased", null, "false"),
			new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "identifier", "http://foo1", "bar1"),
			new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "identifier", "http://foo2", "bar2"),
			new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "identifier:of-type", "http://terminology.hl7.org/CodeSystem/v2-0203", "MR|bar1"),
			new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "identifier:of-type", "http://terminology.hl7.org/CodeSystem/v2-0203", "MR|bar2")
		));

	}

}
