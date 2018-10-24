package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.ISearchParamRegistry;
import ca.uhn.fhir.jpa.dao.PathAndRef;
import ca.uhn.fhir.jpa.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.JpaRuntimeSearchParam;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SearchParamExtractorR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamExtractorR4Test.class);
	private static FhirContext ourCtx = FhirContext.forR4();
	private static IValidationSupport ourValidationSupport;
	private ISearchParamRegistry mySearchParamRegistry;

	@Before
	public void before() {
		mySearchParamRegistry = new ISearchParamRegistry() {
			@Override
			public void forceRefresh() {
				// nothing
			}

			@Override
			public RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName) {
				return getActiveSearchParams(theResourceName).get(theParamName);
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
			public void refreshCacheIfNecessary() {
				// nothing
			}

			@Override
			public void requestRefresh() {
				// nothing
			}
		};

	}

	@Test
	public void testParamWithOrInPath() {
		Observation obs = new Observation();
		obs.addCategory().addCoding().setSystem("SYSTEM").setCode("CODE");

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new DaoConfig(), ourCtx, ourValidationSupport, mySearchParamRegistry);
		Set<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(new ResourceTable(), obs);
		assertEquals(1, tokens.size());
		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.iterator().next();
		assertEquals("category", token.getParamName());
		assertEquals("SYSTEM", token.getSystem());
		assertEquals("CODE", token.getValue());
	}

	@Test
	public void testReferenceWithResolve() {
		Encounter enc = new Encounter();
		enc.addLocation().setLocation(new Reference("Location/123"));

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new DaoConfig(), ourCtx, ourValidationSupport, mySearchParamRegistry);
		RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam("Encounter", "location");
		assertNotNull(param);
		List<PathAndRef> links = extractor.extractResourceLinks(enc, param);
		assertEquals(1, links.size());
		assertEquals("Encounter.location.location.where(resolve() is Location)", links.get(0).getPath());
		assertEquals("Location/123", ((Reference) links.get(0).getRef()).getReference());
	}

	@Test
	public void testReferenceWithResolveMulti() {
		Consent consent = new Consent();
		consent.setSource(new Reference().setReference("Consent/999"));

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new DaoConfig(), ourCtx, ourValidationSupport, mySearchParamRegistry);
		RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam("Consent", Consent.SP_SOURCE_REFERENCE);
		assertNotNull(param);
		List<PathAndRef> links = extractor.extractResourceLinks(consent, param);
		assertEquals(1, links.size());
		assertEquals("Consent.source.where(resolve() is Consent or resolve() is Contract or resolve() is QuestionnaireResponse or resolve() is DocumentReference)", links.get(0).getPath());
		assertEquals("Consent/999", ((Reference) links.get(0).getRef()).getReference());
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

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new DaoConfig(), ourCtx, ourValidationSupport, mySearchParamRegistry);
		Set<ResourceIndexedSearchParamQuantity> links = extractor.extractSearchParamQuantity(new ResourceTable(), o1);
		ourLog.info("Links:\n  {}", links.stream().map(t -> t.toString()).collect(Collectors.joining("\n  ")));
		assertEquals(4, links.size());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() {
		ourValidationSupport = new DefaultProfileValidationSupport();
	}

}
