package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.config.TestHibernateSearchAddInConfig;
import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.dao.BaseDateSearchDaoTests;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.dao.DaoTestDataBuilder;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR4;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@RequiresDocker
@ContextConfiguration(classes = {TestR4Config.class, TestHibernateSearchAddInConfig.Elasticsearch.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class FhirResourceDaoR4SearchWithElasticSearchIT extends BaseJpaTest {
	public static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";
	public static final String URL_MY_VALUE_SET = "http://example.com/my_value_set";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4SearchWithElasticSearchIT.class);
	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	protected EntityManager myEntityManager;
	@Autowired
	protected ISearchParamPresenceSvc mySearchParamPresenceSvc;
	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	@Qualifier("myValueSetDaoR4")
	protected IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> myValueSetDao;
	@Autowired
	protected ITermReadSvcR4 myTermSvc;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	@Qualifier("myCodeSystemDaoR4")
	private IFhirResourceDao<CodeSystem> myCodeSystemDao;
	@Autowired
	private FhirContext myFhirCtx;
	@Autowired
	@Qualifier("myObservationDaoR4")
	private IFhirResourceDao<Observation> myObservationDao;
	@Autowired
	@Qualifier("myPatientDaoR4")
	private IFhirResourceDao<Patient> myPatientDao;
	@Autowired
	@Qualifier("myEncounterDaoR4")
	private IFhirResourceDao<Encounter> myEncounterDao;
	@Autowired
	@Qualifier("mySystemDaoR4")
	private IFhirSystemDao<Bundle, Meta> mySystemDao;
	@Autowired
	private IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;
	@Autowired
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Autowired
	private DaoRegistry myDaoRegistry;
	private boolean myContainsSettings;

	@BeforeEach
	public void beforePurgeDatabase() {
		purgeDatabase(myDaoConfig, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataExportSvc);
	}

	@Override
	protected FhirContext getContext() {
		return myFhirCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@BeforeEach
	public void enableContainsAndLucene() {
		myDaoConfig.setAllowContainsSearches(true);
		myDaoConfig.setAdvancedLuceneIndexing(true);
	}

	@AfterEach
	public void restoreContains() {
		DaoConfig defaultConfig = new DaoConfig();
		myDaoConfig.setAllowContainsSearches(defaultConfig.isAllowContainsSearches());
		myDaoConfig.setAdvancedLuceneIndexing(defaultConfig.isAdvancedLuceneIndexing());
	}

	@Test
	public void testResourceTextSearch() {
		Observation obs1 = new Observation();
		obs1.getCode().setText("Systolic Blood Pressure");
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		obs1.setValue(new Quantity(123));
		obs1.getNoteFirstRep().setText("obs1");
		IIdType id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.getCode().setText("Diastolic Blood Pressure");
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		obs2.setValue(new Quantity(81));
		IIdType id2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;

		map = new SearchParameterMap();
		map.add(ca.uhn.fhir.rest.api.Constants.PARAM_CONTENT, new StringParam("systolic"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(id1)));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("blood"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(id1, id2)));
	}

	@Test
	public void testResourceReferenceSearch() {
		IIdType patId, encId, obsId;

		{
			Patient patient = new Patient();
			DaoMethodOutcome outcome = myPatientDao.create(patient, mySrd);
			patId = outcome.getId();
		}
		{
			Encounter encounter = new Encounter();
			encounter.addIdentifier().setSystem("foo").setValue("bar");
			DaoMethodOutcome outcome = myEncounterDao.create(encounter);
			encId = outcome.getId();
		}
		{
			Observation obs2 = new Observation();
			obs2.getCode().setText("Body Weight");
			obs2.getCode().addCoding().setCode("obs2").setSystem("Some System").setDisplay("Body weight as measured by me");
			obs2.setStatus(Observation.ObservationStatus.FINAL);
			obs2.setValue(new Quantity(81));
			obs2.setSubject(new Reference(patId.toString()));
			obs2.setEncounter(new Reference(encId.toString()));
			obsId = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();
			//ourLog.info("Observation {}", myFhirCtx.newJsonParser().encodeResourceToString(obs2));
		}
		{
			//Search by chain
			SearchParameterMap map = new SearchParameterMap();
			map.add("encounter", new ReferenceParam("foo|bar").setChain("identifier"));
			assertObservationSearchMatches("Search by encounter reference", map, obsId);

		}

		{
			// search by encounter
			SearchParameterMap map = new SearchParameterMap();
			map.add("encounter", new ReferenceParam(encId));
			assertObservationSearchMatches("Search by encounter reference", map, obsId);
		}
		{
			// search by subject
			SearchParameterMap map = new SearchParameterMap();
			map.add("subject", new ReferenceParam(patId));
			assertObservationSearchMatches("Search by subject reference", map, obsId);
		}
		{
			// search by patient
			SearchParameterMap map = new SearchParameterMap();
			map.add("patient", new ReferenceParam(patId));
			assertObservationSearchMatches("Search by patient reference", map, obsId);
		}
		{
			// search by patient and encounter
			SearchParameterMap map = new SearchParameterMap();
			map.add("subject", new ReferenceParam(patId));
			map.add("encounter", new ReferenceParam(encId));
			assertObservationSearchMatches("Search by encounter&&subject reference", map, obsId);
		}

	}

	@Test
	public void testResourceCodeTokenSearch() {
		IIdType id1, id2, id2b, id3;

		String system = "http://loinc.org";
		{
			Observation obs1 = new Observation();
			obs1.getCode().setText("Systolic Blood Pressure");
			obs1.getCode().addCoding().setCode("obs1").setSystem(system).setDisplay("Systolic Blood Pressure");
			obs1.setStatus(Observation.ObservationStatus.FINAL);
			obs1.setValue(new Quantity(123));
			obs1.getNoteFirstRep().setText("obs1");
			id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs2 = new Observation();
			obs2.getCode().setText("Body Weight");
			obs2.getCode().addCoding().setCode("obs2").setSystem(system).setDisplay("Body weight as measured by me");
			obs2.setStatus(Observation.ObservationStatus.FINAL);
			obs2.setValue(new Quantity(81));
			id2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();
			//ourLog.info("Observation {}", myFhirCtx.newJsonParser().encodeResourceToString(obs2));
		}
		{
			Observation obs2b = new Observation();
			obs2b.getCode().addCoding().setCode("obs2").setSystem("http://example.com").setDisplay("A trick system");
			obs2b.setStatus(Observation.ObservationStatus.FINAL);
			obs2b.setValue(new Quantity(81));
			id2b = myObservationDao.create(obs2b, mySrd).getId().toUnqualifiedVersionless();
			//ourLog.info("Observation {}", myFhirCtx.newJsonParser().encodeResourceToString(obs2));
		}
		{
			Observation obs3 = new Observation();
			obs3.getCode().addCoding().setCode("obs3").setSystem("http://example.com").setDisplay("A trick system");
			obs3.getCode().addCoding().setCode("obs3-multiple-code").setSystem("http://example.com").setDisplay("A trick system");
			obs3.setStatus(Observation.ObservationStatus.FINAL);
			obs3.setValue(new Quantity(81));
			id3 = myObservationDao.create(obs3, mySrd).getId().toUnqualifiedVersionless();
			//ourLog.info("Observation {}", myFhirCtx.newJsonParser().encodeResourceToString(obs2));
		}
		{
			// search just code
			SearchParameterMap map = new SearchParameterMap();
			map.add("code", new TokenParam(null, "obs2"));
			assertObservationSearchMatches("Search by code", map, id2, id2b);
		}
		{
			// search just system
			SearchParameterMap map = new SearchParameterMap();
			map.add("code", new TokenParam(system, null));
			assertObservationSearchMatches("Search by system", map, id1, id2);
		}
		{
			// search code and system
			SearchParameterMap map = new SearchParameterMap();
			map.add("code", new TokenParam(system, "obs2"));
			assertObservationSearchMatches("Search by system and code", map, id2);
		}
		{
			// Multiple codes indexed
			SearchParameterMap map = new SearchParameterMap();
			map.add("code", new TokenParam("http://example.com", "obs3-multiple-code"));
			assertObservationSearchMatches("Search for one code", map, id3);
		}
	}

	@Test
	public void testResourceCodeTextSearch() {
		IIdType id1, id2, id3, id4;

		{
			Observation obs1 = new Observation();
			obs1.getCode().setText("Weight unique");
			obs1.setStatus(Observation.ObservationStatus.FINAL);
			obs1.setValue(new Quantity(123));
			obs1.getNoteFirstRep().setText("obs1");
			id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();
		}

		{
			Observation obs2 = new Observation();
			obs2.getCode().setText("Body Weight");
			obs2.getCode().addCoding().setCode("29463-7").setSystem("http://loinc.org").setDisplay("Body weight as measured by me");
			obs2.setStatus(Observation.ObservationStatus.FINAL);
			obs2.setValue(new Quantity(81));
			id2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();
			//ourLog.info("Observation {}", myFhirCtx.newJsonParser().encodeResourceToString(obs2));
		}
		{
			// don't look in the narrative when only searching code.
			Observation obs3 = new Observation();
			Narrative narrative = new Narrative();
			narrative.setDivAsString("<div>Body Weight</div>");
			obs3.setText(narrative);
			obs3.setStatus(Observation.ObservationStatus.FINAL);
			obs3.setValue(new Quantity(81));
			id3 = myObservationDao.create(obs3, mySrd).getId().toUnqualifiedVersionless();
			ourLog.trace("id3 is never found {}", id3);
		}

		//:text should work for identifier types
		{
			Observation obs4 = new Observation();
			Identifier identifier = obs4.addIdentifier();
			CodeableConcept codeableConcept = new CodeableConcept();
			codeableConcept.setText("Random Identifier Typetest");
			identifier.setType(codeableConcept);
			id4 = myObservationDao.create(obs4, mySrd).getId().toUnqualifiedVersionless();
		}

		{
			// first word
			SearchParameterMap map = new SearchParameterMap();
			map.add("code", new TokenParam("Body").setModifier(TokenParamModifier.TEXT));
			assertObservationSearchMatches("Search by first word", map, id2);
		}

		{
			// any word
			SearchParameterMap map = new SearchParameterMap();
			map.add("code", new TokenParam("weight").setModifier(TokenParamModifier.TEXT));
			assertObservationSearchMatches("Search by any word", map, id1, id2);
		}

		{
			// doesn't find internal fragment
			SearchParameterMap map = new SearchParameterMap();
			map.add("code", new TokenParam("ght").setModifier(TokenParamModifier.TEXT));
			assertThat("Search doesn't match middle of words", toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), Matchers.empty());
		}

		{
			// prefix
			SearchParameterMap map = new SearchParameterMap();
			map.add("code", new TokenParam("Bod*").setModifier(TokenParamModifier.TEXT));
			assertObservationSearchMatches("Search matches start of word", map, id2);
		}

		{
			// prefix
			SearchParameterMap map = new SearchParameterMap();
			map.add("code", new TokenParam("Bod").setModifier(TokenParamModifier.TEXT));
			assertThat("Bare prefix does not match", toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), Matchers.empty());
		}

		{
			// codeable.display
			SearchParameterMap map = new SearchParameterMap();
			map.add("code", new TokenParam("measured").setModifier(TokenParamModifier.TEXT));
			assertObservationSearchMatches(":text matches code.display", map, id2);
		}

		{
			// multiple values means or
			SearchParameterMap map = new SearchParameterMap();
			map.add("code", new TokenParam("unique").setModifier(TokenParamModifier.TEXT));
			map.get("code").get(0).add(new TokenParam("measured").setModifier(TokenParamModifier.TEXT));
			assertObservationSearchMatches("Multiple query values means or in :text", map, id1, id2);
		}

		{
			// space means AND
			SearchParameterMap map = new SearchParameterMap();
			map.add("code", new TokenParam("Body Weight").setModifier(TokenParamModifier.TEXT));
			assertObservationSearchMatches("Multiple terms in value means and for :text", map, id2);
		}

		{
			// don't apply the n-gram analyzer to the query, just the text.
			SearchParameterMap map = new SearchParameterMap();
			map.add("code", new TokenParam("Bodum").setModifier(TokenParamModifier.TEXT));
			assertObservationSearchMatchesNothing("search with shared prefix does not match", map);
		}
	}

	@Test
	public void testStringSearch() {
		IIdType id1, id2, id3, id4, id5, id6;

		{
			Observation obs1 = new Observation();
			obs1.setStatus(Observation.ObservationStatus.FINAL);
			obs1.setValue(new StringType("blue"));
			id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs2 = new Observation();
			obs2.setStatus(Observation.ObservationStatus.FINAL);
			obs2.setValue(new StringType("green"));
			id2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs3 = new Observation();
			obs3.setStatus(Observation.ObservationStatus.FINAL);
			obs3.setValue(new StringType("bluegreenish"));
			id3 = myObservationDao.create(obs3, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs4 = new Observation();
			obs4.setStatus(Observation.ObservationStatus.FINAL);
			obs4.setValue(new StringType("blüe"));
			id4 = myObservationDao.create(obs4, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			// upper case
			Observation obs5 = new Observation();
			obs5.setStatus(Observation.ObservationStatus.FINAL);
			obs5.setValue(new StringType("Blue"));
			id5 = myObservationDao.create(obs5, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs6 = new Observation();
			obs6.setStatus(Observation.ObservationStatus.FINAL);
			obs6.setValue(new StringType("blue green"));
			id6 = myObservationDao.create(obs6, mySrd).getId().toUnqualifiedVersionless();
		}


		// run searches

		{
			// default search matches prefix, ascii-normalized, case-insensitive
			SearchParameterMap map = new SearchParameterMap();
			map.add("value-string", new StringParam("blu"));
			assertObservationSearchMatches("default search matches normalized prefix", map, id1, id3, id4, id5, id6);
		}
		{
			// normal search matches string with space
			SearchParameterMap map = new SearchParameterMap();
			map.add("value-string", new StringParam("blue gre"));
			assertObservationSearchMatches("normal search matches string with space", map, id6);
		}
		{
			// exact search
			SearchParameterMap map = new SearchParameterMap();
			map.add("value-string", new StringParam("blue").setExact(true));
			assertObservationSearchMatches("exact search only matches exact string", map, id1);
		}
		{
			// or matches both
			SearchParameterMap map = new SearchParameterMap();
			map.add("value-string",
				new StringOrListParam()
					.addOr(new StringParam("blue").setExact(true))
					.addOr(new StringParam("green").setExact(true)));

			assertObservationSearchMatches("contains search matches anywhere", map, id1, id2);
		}
		{
			// contains matches anywhere
			SearchParameterMap map = new SearchParameterMap();
			map.add("value-string", new StringParam("reen").setContains(true));
			assertObservationSearchMatches("contains search matches anywhere", map, id2, id3, id6);
		}
	}



	private void assertObservationSearchMatchesNothing(String message, SearchParameterMap map) {
		assertObservationSearchMatches(message, map);
	}

	private void assertObservationSearchMatches(String message, SearchParameterMap map, IIdType... iIdTypes) {
		assertThat(message, toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(iIdTypes)));
	}

	@Nested
	public class WithContainedIndexing {
		@BeforeEach
		public void enableContains() {
			// we don't support chained or contained yet, but turn it on to test we don't blow up.
			myDaoConfig.getModelConfig().setIndexOnContainedResources(true);
			myDaoConfig.getModelConfig().setIndexOnContainedResourcesRecursively(true);
		}

		@AfterEach
		public void restoreContains() {
			ModelConfig defaultModelConfig = new ModelConfig();
			myDaoConfig.getModelConfig().setIndexOnContainedResources(defaultModelConfig.isIndexOnContainedResources());
			myDaoConfig.getModelConfig().setIndexOnContainedResourcesRecursively(defaultModelConfig.isIndexOnContainedResourcesRecursively());
		}
		/**
		 * We were throwing when indexing contained.
		 * https://github.com/hapifhir/hapi-fhir/issues/3371
		 */
		@Test
		public void ignoreContainedResources_noError() {
			// given
			String json =
				"{" +
					"\"resourceType\": \"Observation\"," +
					"\"contained\": [{" +
					"\"resourceType\": \"Patient\"," +
					"\"id\": \"contained-patient\"," +
					"\"name\": [{ \"family\": \"Smith\"}]" +
					"}]," +
					"\"subject\": { \"reference\": \"#contained-patient\" }" +
					"}";
			Observation o = myFhirCtx.newJsonParser().parseResource(Observation.class, json);

			myObservationDao.create(o, mySrd).getId().toUnqualifiedVersionless();

			// no error.
		}
	}



	@Test
	public void testExpandWithIsAInExternalValueSet() {
		createExternalCsAndLocalVs();

		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addFilter().setOp(ValueSet.FilterOperator.ISA).setValue("childAA").setProperty("concept");

		ValueSet result = myValueSetDao.expand(vs, null);
		logAndValidateValueSet(result);

		ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("childAAA", "childAAB"));
	}

	@Test
	public void testExpandWithFilter() {
		createExternalCsAndLocalVs();

		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);

		ValueSet result = myValueSetDao.expand(vs, new ValueSetExpansionOptions().setFilter("child"));

		logAndValidateValueSet(result);

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result);
		ourLog.info(resp);

		assertThat(resp, stringContainsInOrder("<code value=\"childCA\"/>", "<display value=\"Child CA\"/>"));
	}

	@Test
	public void testExpandWithFilterContainsLeftMatchingValue() {
		createExternalCsAndLocalVs();

		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);

		ValueSet result = myValueSetDao.expand(vs, new ValueSetExpansionOptions().setFilter("chi"));

		logAndValidateValueSet(result);

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result);
		ourLog.info(resp);

		assertThat(resp, stringContainsInOrder("<code value=\"childCA\"/>", "<display value=\"Child CA\"/>"));
	}

	@Test
	public void testExpandWithFilterContainsNotLeftMatchingValue() {
		createExternalCsAndLocalVs();

		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);

		ValueSet result = myValueSetDao.expand(vs, new ValueSetExpansionOptions().setFilter("hil"));

		logAndValidateValueSet(result);

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result);
		ourLog.info(resp);

		assertThat(resp, not(stringContainsInOrder("<code value=\"childCA\"/>", "<display value=\"Child CA\"/>")));
	}

	@Test
	public void testExpandVsWithMultiInclude_All() throws IOException {
		CodeSystem cs = loadResource(myFhirCtx, CodeSystem.class, "/r4/expand-multi-cs.json");
		myCodeSystemDao.update(cs);

		ValueSet vs = loadResource(myFhirCtx, ValueSet.class, "/r4/expand-multi-vs-all.json");
		ValueSet expanded = myValueSetDao.expand(vs, null);

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expanded));

		// All codes
		List<String> codes = expanded
			.getExpansion()
			.getContains()
			.stream()
			.map(t -> t.getCode())
			.sorted()
			.collect(Collectors.toList());
		assertThat(codes.toString(), codes, Matchers.contains("advice", "message", "note", "notification"));
	}


	@Test
	public void testExpandVsWithMultiInclude_Some() throws IOException {
		CodeSystem cs = loadResource(myFhirCtx, CodeSystem.class, "/r4/expand-multi-cs.json");
		myCodeSystemDao.update(cs);

		ValueSet vs = loadResource(myFhirCtx, ValueSet.class, "/r4/expand-multi-vs-all.json");
		vs.getCompose().getInclude().get(0).getConcept().remove(0);
		vs.getCompose().getInclude().get(0).getConcept().remove(0);

		ValueSet expanded = myValueSetDao.expand(vs, null);

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expanded));

		// All codes
		List<String> codes = expanded
			.getExpansion()
			.getContains()
			.stream()
			.map(t -> t.getCode())
			.sorted()
			.collect(Collectors.toList());
		assertThat(codes.toString(), codes, Matchers.contains("advice", "note"));
	}

	private CodeSystem createExternalCs() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setVersion("SYSTEM VERSION");
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalStateException::new);

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept parentA = new TermConcept(cs, "ParentA").setDisplay("Parent A");
		cs.getConcepts().add(parentA);

		TermConcept childAA = new TermConcept(cs, "childAA").setDisplay("Child AA");
		parentA.addChild(childAA, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		TermConcept childAAA = new TermConcept(cs, "childAAA").setDisplay("Child AAA");
		childAA.addChild(childAAA, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		TermConcept childAAB = new TermConcept(cs, "childAAB").setDisplay("Child AAB");
		childAA.addChild(childAAB, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		TermConcept childAB = new TermConcept(cs, "childAB").setDisplay("Child AB");
		parentA.addChild(childAB, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		TermConcept parentB = new TermConcept(cs, "ParentB").setDisplay("Parent B");
		cs.getConcepts().add(parentB);

		TermConcept childBA = new TermConcept(cs, "childBA").setDisplay("Child BA");
		childBA.addChild(childAAB, TermConceptParentChildLink.RelationshipTypeEnum.ISA);
		parentB.addChild(childBA, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		TermConcept parentC = new TermConcept(cs, "ParentC").setDisplay("Parent C");
		cs.getConcepts().add(parentC);

		TermConcept childCA = new TermConcept(cs, "childCA").setDisplay("Child CA");
		parentC.addChild(childCA, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), URL_MY_CODE_SYSTEM, "SYSTEM NAME", "SYSTEM VERSION", cs, table);
		return codeSystem;
	}

	private void createExternalCsAndLocalVs() {
		CodeSystem codeSystem = createExternalCs();

		createLocalVs(codeSystem);
	}

	private void createLocalVs(CodeSystem codeSystem) {
		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(URL_MY_VALUE_SET);
		valueSet.getCompose().addInclude().setSystem(codeSystem.getUrl());
		myValueSetDao.create(valueSet, mySrd);
	}

	private ArrayList<String> toCodesContains(List<ValueSet.ValueSetExpansionContainsComponent> theContains) {
		ArrayList<String> retVal = new ArrayList<>();
		for (ValueSet.ValueSetExpansionContainsComponent next : theContains) {
			retVal.add(next.getCode());
		}
		return retVal;
	}

	private void logAndValidateValueSet(ValueSet theResult) {
		IParser parser = myFhirCtx.newXmlParser().setPrettyPrint(true);
		String encoded = parser.encodeResourceToString(theResult);
		ourLog.info(encoded);

		FhirValidator validator = myFhirCtx.newValidator();
		validator.setValidateAgainstStandardSchema(true);
		validator.setValidateAgainstStandardSchematron(true);
		ValidationResult result = validator.validateWithResult(theResult);

		assertEquals(0, result.getMessages().size());

	}

	@Nested
	public class DateSearchTests extends BaseDateSearchDaoTests {

		@Override
		protected Fixture getFixture() {
			DaoTestDataBuilder testDataBuilder = new DaoTestDataBuilder(myFhirCtx, myDaoRegistry, new SystemRequestDetails());
			return new TestDataBuilderFixture<>(testDataBuilder, myObservationDao);
		}
	}

}
