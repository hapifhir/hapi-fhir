package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.PathAndRef;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParamComposite;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR4;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParameterCanonicalizer;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.HapiExtensions;
import com.google.common.collect.Sets;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SearchParamExtractorR4Test implements ITestDataBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamExtractorR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private final FhirContextSearchParamRegistry mySearchParamRegistry = new FhirContextSearchParamRegistry(ourCtx);
	private final PartitionSettings myPartitionSettings = new PartitionSettings();
	final StorageSettings myStorageSettings = new StorageSettings();

	@Test
	public void testParamWithOrInPath() {
		Observation obs = new Observation();
		obs.addCategory().addCoding().setSystem("SYSTEM").setCode("CODE");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new StorageSettings(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		Set<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(obs);
		assertThat(tokens).hasSize(1);
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
		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new StorageSettings(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamString> stringSearchParams = extractor.extractSearchParamStrings(patient);
		List<String> nameValues = stringSearchParams.stream().filter(param -> "name".equals(param.getParamName())).map(ResourceIndexedSearchParamString::getValueExact).collect(Collectors.toList());
		assertThat(nameValues).containsExactlyInAnyOrder("Jimmy", "Jones", "King Jimmy Jones the Great", "King", "the Great");
	}

	@Test
	public void testTokenOnSearchParamContext() {
		SearchParameter sp = new SearchParameter();
		sp.addUseContext().setCode(new Coding().setSystem("http://system").setCode("code"));

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new StorageSettings(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		Set<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(sp);
		assertThat(tokens).hasSize(1);
		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.iterator().next();
		assertEquals("context-type", token.getParamName());
		assertEquals("http://system", token.getSystem());
		assertEquals("code", token.getValue());
	}

	@Test
	public void testTokenText_Enabled_Coding() {
		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("http://system").setCode("code").setDisplay("Help Im a Bug");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new StorageSettings(), myPartitionSettings, ourCtx, mySearchParamRegistry);

		List<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(obs)
			.stream()
			.filter(t -> t.getParamName().equals("code"))
			.sorted(comparing(o -> o.getClass().getName()).reversed())
			.collect(Collectors.toList());
		assertThat(tokens).hasSize(2);

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

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new StorageSettings(), myPartitionSettings, ourCtx, mySearchParamRegistry);

		List<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(obs)
			.stream()
			.filter(t -> t.getParamName().equals("code"))
			.sorted(comparing(o -> o.getClass().getName()).reversed())
			.collect(Collectors.toList());
		assertThat(tokens).hasSize(1);

		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.get(0);
		assertEquals("code", token.getParamName());
		assertEquals("http://system", token.getSystem());
		assertEquals("code", token.getValue());

	}

	@Test
	public void testTokenText_DisabledInStorageSettings_Coding() {
		StorageSettings storageSettings = new StorageSettings();
		storageSettings.setSuppressStringIndexingInTokens(true);

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("http://system").setCode("code").setDisplay("Help Im a Bug");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(storageSettings, myPartitionSettings, ourCtx, mySearchParamRegistry);

		List<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(obs)
			.stream()
			.filter(t -> t.getParamName().equals("code"))
			.sorted(comparing(o -> o.getClass().getName()).reversed())
			.collect(Collectors.toList());
		assertThat(tokens).hasSize(1);

		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.get(0);
		assertEquals("code", token.getParamName());
		assertEquals("http://system", token.getSystem());
		assertEquals("code", token.getValue());

	}

	@Test
	public void testTokenText_DisabledInStorageSettingsButForcedInSearchParam_Coding() {
		StorageSettings storageSettings = new StorageSettings();
		storageSettings.setSuppressStringIndexingInTokens(true);

		RuntimeSearchParam existingCodeSp = mySearchParamRegistry.getActiveSearchParams("Observation").get("code");
		RuntimeSearchParam codeSearchParam = new RuntimeSearchParam(existingCodeSp);
		codeSearchParam.addExtension(HapiExtensions.EXT_SEARCHPARAM_TOKEN_SUPPRESS_TEXT_INDEXING, new Extension(HapiExtensions.EXT_SEARCHPARAM_TOKEN_SUPPRESS_TEXT_INDEXING, new BooleanType(false)));
		mySearchParamRegistry.addSearchParam(codeSearchParam);

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("http://system").setCode("code").setDisplay("Help Im a Bug");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(storageSettings, myPartitionSettings, ourCtx, mySearchParamRegistry);

		List<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(obs)
			.stream()
			.filter(t -> t.getParamName().equals("code"))
			.sorted(comparing(o -> o.getClass().getName()).reversed())
			.collect(Collectors.toList());
		assertThat(tokens).hasSize(2);

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

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new StorageSettings(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam("Encounter", "location");
		assertNotNull(param);
		ISearchParamExtractor.SearchParamSet<PathAndRef> links = extractor.extractResourceLinks(enc, false);
		assertThat(links).hasSize(1);
		assertEquals("location", links.iterator().next().getSearchParamName());
		assertEquals("Encounter.location.location", links.iterator().next().getPath());
		assertEquals("Location/123", ((Reference) links.iterator().next().getRef()).getReference());
	}

	@Test
	public void testReferenceWithResolveMulti() {
		Consent consent = new Consent();
		consent.setSource(new Reference().setReference("Consent/999"));

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new StorageSettings(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam("Consent", Consent.SP_SOURCE_REFERENCE);
		assertNotNull(param);
		ISearchParamExtractor.SearchParamSet<PathAndRef> links = extractor.extractResourceLinks(consent, false);
		assertThat(links).hasSize(1);
		assertEquals("Consent.source", links.iterator().next().getPath());
		assertEquals("Consent/999", ((Reference) links.iterator().next().getRef()).getReference());
	}


	@Test
	public void testExtractSearchParamTokenTest() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("sys").setValue("val");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new StorageSettings(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam("Patient", Patient.SP_IDENTIFIER);
		assertNotNull(param);
		ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> params = extractor.extractSearchParamTokens(p, param);
		assertThat(params).hasSize(1);
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

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new StorageSettings(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		ISearchParamExtractor.SearchParamSet<PathAndRef> links = extractor.extractResourceLinks(patient, false);
		assertThat(links).hasSize(1);

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

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new StorageSettings(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		Set<ResourceIndexedSearchParamQuantity> links = extractor.extractSearchParamQuantity(o1);
		ourLog.info("Links:\n  {}", links.stream().map(t -> t.toString()).collect(Collectors.joining("\n  ")));
		assertThat(links).hasSize(4);
	}

	@Test
	public void testExtractComponentQuantityWithNormalizedQuantitySearchSupported() {

		StorageSettings storageSettings = new StorageSettings();

		storageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);

		Observation o1 = new Observation();
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code1")))
			.setValue(new Quantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm").setValue(200));

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(storageSettings, new PartitionSettings(), ourCtx, mySearchParamRegistry);
		Set<ResourceIndexedSearchParamQuantityNormalized> links = extractor.extractSearchParamQuantityNormalized(o1);
		ourLog.info("Links:\n  {}", links.stream().map(t -> t.toString()).collect(Collectors.joining("\n  ")));
		assertThat(links).hasSize(2);

	}

	@Test
	public void testExtractComponentQuantityValueWithNormalizedQuantitySearchSupported() {

		StorageSettings storageSettings = new StorageSettings();

		storageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);

		Observation o1 = new Observation();

		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code1")))
			.setValue(new Quantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm").setValue(200));

		RuntimeSearchParam existingCodeSp = mySearchParamRegistry.getActiveSearchParams("Observation").get("component-value-quantity");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(storageSettings, new PartitionSettings(), ourCtx, mySearchParamRegistry);
		List<String> list = extractor.extractParamValuesAsStrings(existingCodeSp, o1);

		assertThat(list).hasSize(2);
	}

	@Test
	public void testExtractIdentifierOfType() {

		StorageSettings storageSettings = new StorageSettings();
		storageSettings.setIndexIdentifierOfType(true);

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

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(storageSettings, new PartitionSettings(), ourCtx, mySearchParamRegistry);
		List<ResourceIndexedSearchParamToken> list = extractor
			.extractSearchParamTokens(patient)
			.stream()
			.map(t->(ResourceIndexedSearchParamToken)t)
			.collect(Collectors.toList());
		list.forEach(t->t.calculateHashes());
		ourLog.info("Found tokens:\n * {}", list.stream().map(t->t.toString()).collect(Collectors.joining("\n * ")));

		assertThat(list).containsExactlyInAnyOrder(new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "deceased", null, "false"), new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "identifier", "http://foo1", "bar1"), new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "identifier", "http://foo2", "bar2"), new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "identifier:of-type", "http://terminology.hl7.org/CodeSystem/v2-0203", "MR|bar1"), new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "identifier:of-type", "http://terminology.hl7.org/CodeSystem/v2-0203", "MR|bar2"));

	}

	@Nested
	class CompositeSearchParameter {
		SearchParamExtractorR4 myExtractor = new SearchParamExtractorR4(myStorageSettings, new PartitionSettings(), ourCtx, mySearchParamRegistry);

		/**
		 * Install a full definition of component-code-value-concept in the SP registry.
		 *
		 * We can't use the base FhirContext SP definitions since @SearchParamDefinition
		 * only includes the sub-SP ids, and lacks the composite sub-paths.
		 * @see ca.uhn.fhir.model.api.annotation.SearchParamDefinition#compositeOf
		 */
		@BeforeEach
		public void setUp() {
			String spJson = """
				{
				  "resourceType": "SearchParameter",
				  "id": "Observation-component-code-value-concept",
				  "extension": [ {
				  "url": "http://hl7.org/fhir/StructureDefinition/structuredefinition-standards-status",
				  "valueCode": "trial-use"
				  } ],
				  "url": "http://hl7.org/fhir/SearchParameter/Observation-component-code-value-concept",
				  "version": "4.0.1",
				  "name": "component-code-value-concept",
				  "status": "active",
				  "experimental": false,
				  "date": "2019-11-01T09:29:23+11:00",
				  "publisher": "Health Level Seven International (Orders and Observations)",
				  "contact": [ {
				  "telecom": [ {
					"system": "url",
					"value": "http://hl7.org/fhir"
				  } ]
				  }, {
				  "telecom": [ {
					"system": "url",
					"value": "http://www.hl7.org/Special/committees/orders/index.cfm"
				  } ]
				  } ],
				  "description": "Component code and component coded value parameter pair",
				  "code": "component-code-value-concept",
				  "base": [ "Observation" ],
				  "type": "composite",
				  "expression": "Observation.component",
				  "xpathUsage": "normal",
				  "multipleOr": false,
				  "component": [ {
				  "definition": "http://hl7.org/fhir/SearchParameter/Observation-component-code",
				  "expression": "code"
				  }, {
				  "definition": "http://hl7.org/fhir/SearchParameter/Observation-component-value-concept",
				  "expression": "value.as(CodeableConcept)"
				  } ]
				 }
				""";
			SearchParameter sp = (SearchParameter) getFhirContext().newJsonParser().parseResource(spJson);
			RuntimeSearchParam runtimeSP = new SearchParameterCanonicalizer(getFhirContext()).canonicalizeSearchParameter(sp);
			mySearchParamRegistry.addSearchParam(runtimeSP);
		}

		@Test
		void testExtractSearchParamComposites_componentCodeCode_producesOneCompositeComponentForEachElement() {
			IBaseResource resource = buildResource("Observation",
				withObservationComponent(
					withCodingAt("code.coding", "http://example.com", "code_token", null),
					withCodingAt("valueCodeableConcept.coding", null, "value_token", null)),
				withObservationComponent(
					withCodingAt("code.coding", "http://example.com", "code_token2", null),
					withCodingAt("valueCodeableConcept.coding", null, "value_toke2", null)),
				withObservationComponent(
					withCodingAt("code.coding", "http://example.com", "code_token3", null),
					withCodingAt("valueCodeableConcept.coding", null, "value_toke3", null))
			);

			Collection<ResourceIndexedSearchParamComposite> c = myExtractor.extractSearchParamComposites(resource);

			assertThat(c).isNotEmpty();
			assertThat(c).as("Extracts standard R4 composite sp")
				.extracting("searchParamName")
				.contains("component-code-value-concept");

			List<ResourceIndexedSearchParamComposite> components = c.stream()
				.filter(idx -> idx.getSearchParamName().equals("component-code-value-concept"))
				.collect(Collectors.toList());
			assertThat(components).as("one components per element").hasSize(3);

		}

		@Test
		void testExtractSearchParamComposites_componentCodeCode_yieldsTwoSubComponents() {
			IBaseResource resource = buildResource("Observation",
				withObservationComponent(
					withCodingAt("code.coding", "http://example.com", "code_token", null),
					withCodingAt("valueCodeableConcept.coding", null, "value_token", null))
			);

			Collection<ResourceIndexedSearchParamComposite> c = myExtractor.extractSearchParamComposites(resource);

			List<ResourceIndexedSearchParamComposite> components = c.stream()
				.filter(idx -> idx.getSearchParamName().equals("component-code-value-concept"))
				.toList();
			assertThat(components).hasSize(1);
			ResourceIndexedSearchParamComposite componentCodeValueConcept = components.get(0);

			// component-code-value-concept is two token params - component-code and component-value-concept
			List<ResourceIndexedSearchParamComposite.Component> indexedComponentsOfElement = componentCodeValueConcept.getComponents();
			assertThat(indexedComponentsOfElement).as("component-code-value-concept has two sub-params").hasSize(2);

			final ResourceIndexedSearchParamComposite.Component component0 = indexedComponentsOfElement.get(0);
			assertEquals("component-code", component0.getSearchParamName());
			assertEquals(RestSearchParameterTypeEnum.TOKEN, component0.getSearchParameterType());

			final ResourceIndexedSearchParamComposite.Component component1 = indexedComponentsOfElement.get(1);
			assertEquals("component-value-concept", component1.getSearchParamName());
			assertEquals(RestSearchParameterTypeEnum.TOKEN, component1.getSearchParameterType());
		}

		@Test
		void testExtractSearchParamComposites_componentCodeCode_subValueExtraction() {
			IBaseResource resource = buildResource("Observation",
				withObservationComponent(
					withCodingAt("code.coding", "http://example.com", "code_token", "display value"),
					withCodingAt("valueCodeableConcept.coding", null, "value_token", null))
			);

			Collection<ResourceIndexedSearchParamComposite> c = myExtractor.extractSearchParamComposites(resource);

			List<ResourceIndexedSearchParamComposite> components = c.stream()
				.filter(idx -> idx.getSearchParamName().equals("component-code-value-concept"))
				.toList();
			assertThat(components).hasSize(1);
			ResourceIndexedSearchParamComposite spEntry = components.get(0);

			// this SP has two sub-components
			assertThat(spEntry.getComponents()).hasSize(2);

			ResourceIndexedSearchParamComposite.Component indexComponent0 = spEntry.getComponents().get(0);
			assertNotNull(indexComponent0.getSearchParamName());
			assertEquals("component-code", indexComponent0.getSearchParamName());
			assertEquals(RestSearchParameterTypeEnum.TOKEN, indexComponent0.getSearchParameterType());
			assertThat(indexComponent0.getParamIndexValues()).hasSize(2);
			// token indexes both the token, and the display text
			ResourceIndexedSearchParamToken tokenIdx0 = (ResourceIndexedSearchParamToken) indexComponent0.getParamIndexValues().stream()
				.filter(i -> i instanceof ResourceIndexedSearchParamToken)
				.findFirst().orElseThrow();
			assertEquals("component-code", tokenIdx0.getParamName());
			assertEquals("Observation", tokenIdx0.getResourceType());
			assertEquals("code_token", tokenIdx0.getValue());

			ResourceIndexedSearchParamString tokenDisplayIdx0 = (ResourceIndexedSearchParamString) indexComponent0.getParamIndexValues().stream()
				.filter(i -> i instanceof ResourceIndexedSearchParamString)
				.findFirst().orElseThrow();
			assertEquals("component-code", tokenDisplayIdx0.getParamName());
			assertEquals("Observation", tokenDisplayIdx0.getResourceType());
			assertEquals("display value", tokenDisplayIdx0.getValueExact());
		}
	}

	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		return null;
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		return null;
	}

	@Override
	public FhirContext getFhirContext() {
		return ourCtx;
	}
}
