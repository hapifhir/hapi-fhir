package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.cache.ResourceChangeResult;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.PathAndRef;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR4;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.registry.ReadOnlySearchParamCache;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.util.HapiExtensions;
import com.google.common.collect.Sets;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SearchParamExtractorR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamExtractorR4Test.class);
	private static FhirContext ourCtx = FhirContext.forCached(FhirVersionEnum.R4);
	private static IValidationSupport ourValidationSupport;
	private MySearchParamRegistry mySearchParamRegistry;
	private PartitionSettings myPartitionSettings;

	@BeforeEach
	public void before() {

		mySearchParamRegistry = new MySearchParamRegistry();
		myPartitionSettings = new PartitionSettings();

	}

	@Test
	public void testParamWithOrInPath() {
		Observation obs = new Observation();
		obs.addCategory().addCoding().setSystem("SYSTEM").setCode("CODE");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), new PartitionSettings(), ourCtx, ourValidationSupport, mySearchParamRegistry);
		Set<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(obs);
		assertEquals(1, tokens.size());
		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.iterator().next();
		assertEquals("category", token.getParamName());
		assertEquals("SYSTEM", token.getSystem());
		assertEquals("CODE", token.getValue());
	}

	@Test
	public void testTokenOnSearchParamContext() {
		SearchParameter sp = new SearchParameter();
		sp.addUseContext().setCode(new Coding().setSystem("http://system").setCode("code"));

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), new PartitionSettings(), ourCtx, ourValidationSupport, mySearchParamRegistry);
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

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), myPartitionSettings, ourCtx, ourValidationSupport, mySearchParamRegistry);

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

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), myPartitionSettings, ourCtx, ourValidationSupport, mySearchParamRegistry);

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

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(modelConfig, myPartitionSettings, ourCtx, ourValidationSupport, mySearchParamRegistry);

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

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(modelConfig, myPartitionSettings, ourCtx, ourValidationSupport, mySearchParamRegistry);

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
	public void testTokenText_Enabled_Identifier() {
		Observation obs = new Observation();
		obs.addIdentifier().setSystem("sys").setValue("val").getType().setText("Help Im a Bug");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), myPartitionSettings, ourCtx, ourValidationSupport, mySearchParamRegistry);

		List<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(obs)
			.stream()
			.filter(t -> t.getParamName().equals("identifier"))
			.sorted(comparing(o -> o.getClass().getName()).reversed())
			.collect(Collectors.toList());
		assertEquals(2, tokens.size());

		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.get(0);
		assertEquals("identifier", token.getParamName());
		assertEquals("sys", token.getSystem());
		assertEquals("val", token.getValue());

		ResourceIndexedSearchParamString string = (ResourceIndexedSearchParamString) tokens.get(1);
		assertEquals("identifier", string.getParamName());
		assertEquals("Help Im a Bug", string.getValueExact());
	}

	@Test
	public void testTokenText_DisabledInSearchParam_Identifier() {
		RuntimeSearchParam existingCodeSp = mySearchParamRegistry.getActiveSearchParams("Observation").get("identifier");
		RuntimeSearchParam codeSearchParam = new RuntimeSearchParam(existingCodeSp);
		codeSearchParam.addExtension(HapiExtensions.EXT_SEARCHPARAM_TOKEN_SUPPRESS_TEXT_INDEXING, new Extension(HapiExtensions.EXT_SEARCHPARAM_TOKEN_SUPPRESS_TEXT_INDEXING, new BooleanType(true)));

		mySearchParamRegistry.addSearchParam(codeSearchParam);

		Observation obs = new Observation();
		obs.addIdentifier().setSystem("sys").setValue("val").getType().setText("Help Im a Bug");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), myPartitionSettings, ourCtx, ourValidationSupport, mySearchParamRegistry);

		List<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(obs)
			.stream()
			.filter(t -> t.getParamName().equals("identifier"))
			.sorted(comparing(o -> o.getClass().getName()).reversed())
			.collect(Collectors.toList());
		assertEquals(1, tokens.size());

		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.get(0);
		assertEquals("identifier", token.getParamName());
		assertEquals("sys", token.getSystem());
		assertEquals("val", token.getValue());

	}

	@Test
	public void testReferenceWithResolve() {
		Encounter enc = new Encounter();
		enc.addLocation().setLocation(new Reference("Location/123"));

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), new PartitionSettings(), ourCtx, ourValidationSupport, mySearchParamRegistry);
		RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam("Encounter", "location");
		assertNotNull(param);
		ISearchParamExtractor.SearchParamSet<PathAndRef> links = extractor.extractResourceLinks(enc);
		assertEquals(1, links.size());
		assertEquals("location", links.iterator().next().getSearchParamName());
		assertEquals("Encounter.location.location", links.iterator().next().getPath());
		assertEquals("Location/123", ((Reference) links.iterator().next().getRef()).getReference());
	}

	@Test
	public void testReferenceWithResolveMulti() {
		Consent consent = new Consent();
		consent.setSource(new Reference().setReference("Consent/999"));

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), new PartitionSettings(), ourCtx, ourValidationSupport, mySearchParamRegistry);
		RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam("Consent", Consent.SP_SOURCE_REFERENCE);
		assertNotNull(param);
		ISearchParamExtractor.SearchParamSet<PathAndRef> links = extractor.extractResourceLinks(consent);
		assertEquals(1, links.size());
		assertEquals("Consent.source", links.iterator().next().getPath());
		assertEquals("Consent/999", ((Reference) links.iterator().next().getRef()).getReference());
	}


	@Test
	public void testExtractSearchParamTokenTest() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("sys").setValue("val");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), new PartitionSettings(), ourCtx, ourValidationSupport, mySearchParamRegistry);
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

		RuntimeSearchParam sp = new RuntimeSearchParam("extpat", "Patient SP", path, RestSearchParameterTypeEnum.REFERENCE, new HashSet<>(), Sets.newHashSet("Patient"), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE);
		mySearchParamRegistry.addSearchParam(sp);

		Patient patient = new Patient();
		patient.addExtension("http://patext", new Reference("Organization/AAA"));

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), new PartitionSettings(), ourCtx, ourValidationSupport, mySearchParamRegistry);
		ISearchParamExtractor.SearchParamSet<PathAndRef> links = extractor.extractResourceLinks(patient);
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

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new ModelConfig(), new PartitionSettings(), ourCtx, ourValidationSupport, mySearchParamRegistry);
		Set<ResourceIndexedSearchParamQuantity> links = extractor.extractSearchParamQuantity(o1);
		ourLog.info("Links:\n  {}", links.stream().map(t -> t.toString()).collect(Collectors.joining("\n  ")));
		assertEquals(4, links.size());
	}

	private static class MySearchParamRegistry implements ISearchParamRegistry {


		private List<RuntimeSearchParam> myExtraSearchParams = new ArrayList<>();

		@Override
		public void forceRefresh() {
			// nothing
		}

		@Override
		public RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName) {
			return getActiveSearchParams(theResourceName).get(theParamName);
		}

		@Override
		public ResourceChangeResult refreshCacheIfNecessary() {
			// nothing
			return new ResourceChangeResult();
		}

		@Override
		public ReadOnlySearchParamCache getActiveSearchParams() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Map<String, RuntimeSearchParam> getActiveSearchParams(String theResourceName) {
			Map<String, RuntimeSearchParam> sps = new HashMap<>();
			RuntimeResourceDefinition nextResDef = ourCtx.getResourceDefinition(theResourceName);
			for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
				sps.put(nextSp.getName(), nextSp);
			}

			for (RuntimeSearchParam next : myExtraSearchParams) {
				sps.put(next.getName(), next);
			}

			return sps;
		}

		public void addSearchParam(RuntimeSearchParam theSearchParam) {
			myExtraSearchParams.add(theSearchParam);
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

		@Override
		public void setPhoneticEncoder(IPhoneticEncoder thePhoneticEncoder) {
			// nothing
		}
	}

	@BeforeAll
	public static void beforeClass() {
		ourValidationSupport = new DefaultProfileValidationSupport(ourCtx);
	}

}
