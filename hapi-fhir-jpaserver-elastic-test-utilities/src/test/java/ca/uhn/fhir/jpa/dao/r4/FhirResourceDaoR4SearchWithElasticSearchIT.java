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
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.dao.IHSearchEventListener;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR4;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.jpa.test.util.TestHSearchEventDispatcher;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.storage.test.BaseDateSearchDaoTests;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.LogbackLevelOverrideExtension;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.RiskAssessment;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.model.util.UcumServiceUtil.UCUM_CODESYSTEM_URL;
import static ca.uhn.fhir.rest.api.Constants.CHARSET_UTF8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@RequiresDocker
@ContextConfiguration(classes = {
	TestR4Config.class,
	TestHSearchAddInConfig.Elasticsearch.class,
	DaoTestDataBuilder.Config.class,
	TestDaoSearch.Config.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestExecutionListeners(listeners = {
	DependencyInjectionTestExecutionListener.class
	,FhirResourceDaoR4SearchWithElasticSearchIT.TestDirtiesContextTestExecutionListener.class
	})
public class FhirResourceDaoR4SearchWithElasticSearchIT extends BaseJpaTest implements ITestDataBuilder {
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
	@Qualifier("myRiskAssessmentDaoR4")
	protected IFhirResourceDao<RiskAssessment> myRiskAssessmentDao;

	@Autowired
	@Qualifier("mySystemDaoR4")
	private IFhirSystemDao<Bundle, Meta> mySystemDao;
	@Autowired
	private IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	private IBulkDataExportJobSchedulingHelper myBulkDataScheduleHelper;
	@Autowired
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	ITestDataBuilder myTestDataBuilder;
	@Autowired
	TestDaoSearch myTestDaoSearch;
	@Autowired
	@Qualifier("myQuestionnaireDaoR4")
	private IFhirResourceDao<Questionnaire> myQuestionnaireDao;
	@Autowired
	@Qualifier("myQuestionnaireResponseDaoR4")
	private IFhirResourceDao<QuestionnaireResponse> myQuestionnaireResponseDao;
	@RegisterExtension
	LogbackLevelOverrideExtension myLogbackLevelOverrideExtension = new LogbackLevelOverrideExtension();

	@Autowired
	private TestHSearchEventDispatcher myHSearchEventDispatcher;


	@Mock private IHSearchEventListener mySearchEventListener;


	@BeforeEach
	public void beforePurgeDatabase() {
		purgeDatabase(myDaoConfig, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataScheduleHelper);
	}

	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		return myTestDataBuilder.doCreateResource(theResource);
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		return myTestDataBuilder.doUpdateResource(theResource);
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@BeforeEach
	public void enableContainsAndLucene() {
		myDaoConfig.setAllowContainsSearches(true);
		myDaoConfig.setAdvancedHSearchIndexing(true);
	}

	@AfterEach
	public void restoreContains() {
		DaoConfig defaultConfig = new DaoConfig();
		myDaoConfig.setAllowContainsSearches(defaultConfig.isAllowContainsSearches());
		myDaoConfig.setAdvancedHSearchIndexing(defaultConfig.isAdvancedHSearchIndexing());
		myDaoConfig.setStoreResourceInHSearchIndex(defaultConfig.isStoreResourceInHSearchIndex());
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

		{
			assertObservationSearchMatches("empty params finds everything", "Observation?", id1, id2, id3, id4);
		}
	}

	@Test
	public void testResourceReferenceSearchForCanonicalReferences() {
		String questionnaireCanonicalUrl = "https://test.fhir.org/R4/Questionnaire/xl-5000-q";

		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setId("xl-5000-q");
		questionnaire.setUrl(questionnaireCanonicalUrl);
		IIdType questionnaireId = myQuestionnaireDao.update(questionnaire).getId();

		QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
		questionnaireResponse.setId("xl-5000-qr");
		questionnaireResponse.setQuestionnaire(questionnaireCanonicalUrl);
		IIdType questionnaireResponseId = myQuestionnaireResponseDao.update(questionnaireResponse).getId();

		// Search Questionnaire Response using questionnaire canonical url
		SearchParameterMap map = new SearchParameterMap()
			.setLoadSynchronous(true)
			.add(QuestionnaireResponse.SP_QUESTIONNAIRE, new ReferenceParam(questionnaireCanonicalUrl));

		IBundleProvider bundle = myQuestionnaireResponseDao.search(map);
		List<IBaseResource> result = bundle.getResources(0, bundle.sizeOrThrowNpe());
		assertEquals(1, result.size());
		assertEquals(questionnaireResponseId, result.get(0).getIdElement());
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

	/**
	 * Verify unmodified, :contains, and :text searches are case-insensitive and normalized;
	 * :exact is still sensitive
	 * https://github.com/hapifhir/hapi-fhir/issues/3584
	 */
	@Test
	void testStringCaseFolding() {
		IIdType kelly = myTestDataBuilder.createPatient(asArray(myTestDataBuilder.withGiven("Kelly")));
		IIdType keely = myTestDataBuilder.createPatient(asArray(myTestDataBuilder.withGiven("Kélly")));

		// un-modified, :contains, and :text are all ascii normalized, and case-folded
		myTestDaoSearch.assertSearchFinds("lowercase matches capitalized", "/Patient?name=kelly", kelly, keely);
		myTestDaoSearch.assertSearchFinds("uppercase matches capitalized", "/Patient?name=KELLY", kelly, keely);
		myTestDaoSearch.assertSearchFinds("unmodified is accent insensitive", "/Patient?name=" + urlencode("Kélly"), kelly, keely);

		myTestDaoSearch.assertSearchFinds("contains case-insensitive", "/Patient?name:contains=elly", kelly, keely);
		myTestDaoSearch.assertSearchFinds("contains case-insensitive", "/Patient?name:contains=ELLY", kelly, keely);
		myTestDaoSearch.assertSearchFinds("contains accent-insensitive", "/Patient?name:contains=ELLY", kelly, keely);
		myTestDaoSearch.assertSearchFinds("contains accent-insensitive", "/Patient?name:contains=" + urlencode("éLLY"), kelly, keely);

		myTestDaoSearch.assertSearchFinds("text also accent and case-insensitive", "/Patient?name:text=kelly", kelly, keely);
		myTestDaoSearch.assertSearchFinds("text also accent and case-insensitive", "/Patient?name:text=KELLY", kelly, keely);
		myTestDaoSearch.assertSearchFinds("text also accent and case-insensitive", "/Patient?name:text=" + urlencode("KÉLLY"), kelly, keely);

		myTestDaoSearch.assertSearchFinds("exact case and accent sensitive", "/Patient?name:exact=Kelly", kelly);
		// ugh.  Our url parser won't handle raw utf8 urls.  It requires everything to be single-byte encoded.
		myTestDaoSearch.assertSearchFinds("exact case and accent sensitive", "/Patient?name:exact=" + urlencode("Kélly"), keely);
		myTestDaoSearch.assertSearchNotFound("exact case and accent sensitive", "/Patient?name:exact=KELLY,kelly", kelly);
		myTestDaoSearch.assertSearchNotFound("exact case and accent sensitive",
			"/Patient?name:exact=" + urlencode("KÉLLY,kélly"),
			keely);

		myTestDaoSearch.assertSearchFinds("exact accent sensitive", "/Patient?name:exact=Kelly", kelly);
		myTestDaoSearch.assertSearchFinds("exact accent sensitive", "/Patient?name:exact=" + urlencode("Kélly"), keely);
		myTestDaoSearch.assertSearchNotFound("exact accent sensitive", "/Patient?name:exact=Kelly", keely);
		myTestDaoSearch.assertSearchNotFound("exact accent sensitive", "/Patient?name:exact=" +
			urlencode("kélly"), kelly);

	}

	/** Our url parser requires all chars to be single-byte, and in utf8, that means ascii. */
	private String urlencode(String theParam) {
		return URLEncoder.encode(theParam, CHARSET_UTF8);
	}

	private void assertObservationSearchMatchesNothing(String message, SearchParameterMap map) {
		assertObservationSearchMatches(message, map);
	}

	private void assertObservationSearchMatches(String message, SearchParameterMap map, IIdType... iIdTypes) {
		assertThat(message, toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(iIdTypes)));
	}

	private void assertObservationSearchMatches(String theMessage, String theSearch, IIdType... theIds) {
		SearchParameterMap map = myTestDaoSearch.toSearchParameters(theSearch);
		assertObservationSearchMatches(theMessage, map, theIds);
	}

	@Nested
	public class WithContainedIndexingIT {
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

			IIdType id = myObservationDao.create(o, mySrd).getId().toUnqualifiedVersionless();

			// no error.
			assertThat(id, notNullValue());
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
			.map(ValueSet.ValueSetExpansionContainsComponent::getCode)
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
			.map(ValueSet.ValueSetExpansionContainsComponent::getCode)
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
	public class DateSearchIT extends BaseDateSearchDaoTests {

		@Override
		protected Fixture constructFixture() {
			DaoTestDataBuilder testDataBuilder = new DaoTestDataBuilder(myFhirCtx, myDaoRegistry, new SystemRequestDetails());
			return new TestDataBuilderFixture<>(testDataBuilder, myObservationDao);
		}
	}

	/**
	 * We have a fast path that skips the database entirely
	 * when we can satisfy the queries completely from Hibernate Search.
	 */
	@Nested
	public class FastPath {

		@BeforeEach
		public void enableResourceStorage() {
			myDaoConfig.setStoreResourceInHSearchIndex(true);
		}

		@AfterEach
		public void resetResourceStorage() {
			myDaoConfig.setStoreResourceInHSearchIndex(new DaoConfig().isStoreResourceInHSearchIndex());
		}


		@Test
		public void simpleTokenSkipsSql() {
			IIdType id = myTestDataBuilder.createObservation(List.of(myTestDataBuilder.withObservationCode("http://example.com/", "theCode")));
			myCaptureQueriesListener.clear();
			myHSearchEventDispatcher.register(mySearchEventListener);

			List<IBaseResource> result = searchForFastResources("Observation?code=theCode");
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertThat(result, hasSize(1));
			assertEquals( ((Observation) result.get(0)).getId(), id.getIdPart() );
			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");

			// only one hibernate search took place
			Mockito.verify(mySearchEventListener, Mockito.times(1)).hsearchEvent(IHSearchEventListener.HSearchEventType.SEARCH);
		}


		@Test
		public void sortDoesntRequireSqlAnymore() {

			IIdType id = myTestDataBuilder.createObservation(List.of(myTestDataBuilder.withObservationCode("http://example.com/", "theCode")));
			myCaptureQueriesListener.clear();

			List<String> ids = myTestDaoSearch.searchForIds("Observation?code=theCode&_sort=code");
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertThat(ids, hasSize(1));
			assertThat(ids, contains(id.getIdPart()));

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "the pids come from elastic, but we use sql to sort");
		}

		@Test
		public void deletedResourceNotFound() {

			IIdType id = myTestDataBuilder.createObservation(List.of(myTestDataBuilder.withObservationCode("http://example.com/", "theCode")));
			myObservationDao.delete(id);
			myCaptureQueriesListener.clear();

			List<String> ids = myTestDaoSearch.searchForIds("Observation?code=theCode&_sort=code");
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertThat(ids, hasSize(0));

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "the pids come from elastic, and nothing to fetch");
		}

		@Test
		public void forcedIdSurvivesWithNoSql() {
			IIdType id = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCode"),
				myTestDataBuilder.withId("forcedid")));
			assertThat(id.getIdPart(), equalTo("forcedid"));
			myCaptureQueriesListener.clear();

			List<String> ids = myTestDaoSearch.searchForIds("Observation?code=theCode");
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertThat(ids, hasSize(1));
			assertThat(ids, contains(id.getIdPart()));

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "no sql required");
		}

		/**
		 * A paranoid test to make sure tags stay with the resource.
		 *
		 * Tags live outside the resource, and can be modified by
		 * Since we lost the id, also check tags in case someone changes metadata processing during ingestion.
		 */
		@Test
		public void tagsSurvive() {
			IIdType id = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCode"),
				myTestDataBuilder.withTag("http://example.com", "aTag")));

			myCaptureQueriesListener.clear();
			List<IBaseResource> observations = myTestDaoSearch.searchForResources("Observation?code=theCode");

			assertThat(observations, hasSize(1));
			List<? extends IBaseCoding> tags = observations.get(0).getMeta().getTag();
			assertThat(tags, hasSize(1));
			assertThat(tags.get(0).getSystem(), equalTo("http://example.com"));
			assertThat(tags.get(0).getCode(), equalTo("aTag"));

			// TODO
			// we assume tags, etc. are inline,
			// but the meta operations don't update the Hibernate Search index yet, so this fails
//			Meta meta = new Meta();
//			meta.addTag().setSystem("tag_scheme1").setCode("tag_code1");
//			meta.addProfile("http://profile/1");
//			meta.addSecurity().setSystem("seclabel_sys1").setCode("seclabel_code1");
//			myObservationDao.metaAddOperation(id, meta, mySrd);
//
//			observations = myTestDaoSearch.searchForResources("Observation?code=theCode");
//
//			assertThat(observations, hasSize(1));
//			IBaseMetaType newMeta = observations.get(0).getMeta();
//			assertThat(newMeta.getProfile(), hasSize(1));
//			assertThat(newMeta.getSecurity(), hasSize(1));
//			assertThat(newMeta.getTag(), hasSize(2));
		}

	}


	@Nested
	public class QuantityAndNormalizedQuantitySearch {

		private IIdType myResourceId;


		@Nested
		public class QuantitySearch {

			@Nested
			public class SimpleQueries {

				@Test
				public void noQuantityThrows() {
					String invalidQtyParam = "|http://another.org";
					DataFormatException thrown = assertThrows(DataFormatException.class,
						() -> myTestDaoSearch.searchForIds("/Observation?value-quantity=" + invalidQtyParam));

					assertTrue(thrown.getMessage().startsWith("HAPI-1940: Invalid"));
					assertTrue(thrown.getMessage().contains(invalidQtyParam));
				}

				@Test
				public void invalidPrefixThrows() {
					DataFormatException thrown = assertThrows(DataFormatException.class,
						() -> myTestDaoSearch.searchForIds("/Observation?value-quantity=st5.35"));

					assertEquals("HAPI-1941: Invalid prefix: \"st\"", thrown.getMessage());
				}

				@Test
				public void eq() {
					withObservationWithValueQuantity(0.6);

					assertNotFind("when lt unitless", "/Observation?value-quantity=0.5");
					assertNotFind("when wrong system", "/Observation?value-quantity=0.6|http://another.org");
					assertNotFind("when wrong units", "/Observation?value-quantity=0.6||mmHg");
					assertNotFind("when gt unitless", "/Observation?value-quantity=0.7");
					assertNotFind("when gt", "/Observation?value-quantity=0.7||mmHg");

					assertFind("when a little gt - default is approx", "/Observation?value-quantity=0.599");
					assertFind("when a little lt - default is approx", "/Observation?value-quantity=0.601");

					assertFind("when eq unitless", "/Observation?value-quantity=0.6");
					assertFind("when eq with units", "/Observation?value-quantity=0.6||mm[Hg]");
				}

				@Test
				public void ne() {
					withObservationWithValueQuantity(0.6);

					assertFind("when gt", "/Observation?value-quantity=ne0.5");
					assertNotFind("when eq", "/Observation?value-quantity=ne0.6");
					assertFind("when lt", "/Observation?value-quantity=ne0.7");
				}

				@Test
				public void ap() {
					withObservationWithValueQuantity(0.6);

					assertNotFind("when gt", "/Observation?value-quantity=ap0.5");
					assertFind("when a little gt", "/Observation?value-quantity=ap0.58");
					assertFind("when eq", "/Observation?value-quantity=ap0.6");
					assertFind("when a little lt", "/Observation?value-quantity=ap0.62");
					assertNotFind("when lt", "/Observation?value-quantity=ap0.7");
				}

				@Test
				public void gt() {
					withObservationWithValueQuantity(0.6);

					assertFind("when gt", "/Observation?value-quantity=gt0.5");
					assertNotFind("when eq", "/Observation?value-quantity=gt0.6");
					assertNotFind("when lt", "/Observation?value-quantity=gt0.7");

				}

				@Test
				public void ge() {
					withObservationWithValueQuantity(0.6);

					assertFind("when gt", "/Observation?value-quantity=ge0.5");
					assertFind("when eq", "/Observation?value-quantity=ge0.6");
					assertNotFind("when lt", "/Observation?value-quantity=ge0.7");
				}

				@Test
				public void lt() {
					withObservationWithValueQuantity(0.6);

					assertNotFind("when gt", "/Observation?value-quantity=lt0.5");
					assertNotFind("when eq", "/Observation?value-quantity=lt0.6");
					assertFind("when lt", "/Observation?value-quantity=lt0.7");
				}

				@Test
				public void le() {
					withObservationWithValueQuantity(0.6);

					assertNotFind("when gt", "/Observation?value-quantity=le0.5");
					assertFind("when eq", "/Observation?value-quantity=le0.6");
					assertFind("when lt", "/Observation?value-quantity=le0.7");
				}
			}


			@Nested
			public class CombinedQueries {

				@Test
				void gtAndLt() {
					withObservationWithValueQuantity(0.6);

					assertFind("when gt0.5 and lt0.7", "/Observation?value-quantity=gt0.5&value-quantity=lt0.7");
					assertNotFind("when gt0.5 and lt0.6", "/Observation?value-quantity=gt0.5&value-quantity=lt0.6");
					assertNotFind("when gt6.5 and lt0.7", "/Observation?value-quantity=gt6.5&value-quantity=lt0.7");
					assertNotFind("impossible matching", "/Observation?value-quantity=gt0.7&value-quantity=lt0.5");
				}

				@Test
				void orClauses() {
					withObservationWithValueQuantity(0.6);

					assertFind("when gt0.5 and lt0.7", "/Observation?value-quantity=0.5,0.6");
					// make sure it doesn't find everything when using or clauses
					assertNotFind("when gt0.5 and lt0.7", "/Observation?value-quantity=0.5,0.7");
				}

				@Nested
				public class CombinedAndPlusOr {

					@Test
					void ltAndOrClauses() {
						withObservationWithValueQuantity(0.6);

						assertFind("when lt0.7 and eq (0.5 or 0.6)", "/Observation?value-quantity=lt0.7&value-quantity=0.5,0.6");
						// make sure it doesn't find everything when using or clauses
						assertNotFind("when lt0.4 and eq (0.5 or 0.6)", "/Observation?value-quantity=lt0.4&value-quantity=0.5,0.6");
						assertNotFind("when lt0.7 and eq (0.4 or 0.5)", "/Observation?value-quantity=lt0.7&value-quantity=0.4,0.5");
					}

					@Test
					void gtAndOrClauses() {
						withObservationWithValueQuantity(0.6);

						assertFind("when gt0.4 and eq (0.5 or 0.6)", "/Observation?value-quantity=gt0.4&value-quantity=0.5,0.6");
						assertNotFind("when gt0.7 and eq (0.5 or 0.7)", "/Observation?value-quantity=gt0.7&value-quantity=0.5,0.7");
						assertNotFind("when gt0.3 and eq (0.4 or 0.5)", "/Observation?value-quantity=gt0.3&value-quantity=0.4,0.5");
					}
				}


				@Nested
				public class QualifiedOrClauses {

					@Test
					void gtOrLt() {
						withObservationWithValueQuantity(0.6);

						assertFind("when gt0.5 or lt0.3", "/Observation?value-quantity=gt0.5,lt0.3");
						assertNotFind("when gt0.6 or lt0.55", "/Observation?value-quantity=gt0.6,lt0.55");
					}

					@Test
					void gtOrLe() {
						withObservationWithValueQuantity(0.6);

						assertFind("when gt0.5 or le0.3", "/Observation?value-quantity=gt0.5,le0.3");
						assertNotFind("when gt0.6 or le0.55", "/Observation?value-quantity=gt0.6,le0.55");
					}

					@Test
					void ltOrGt() {
						withObservationWithValueQuantity(0.6);

						assertFind("when lt0.7 or gt0.9", "/Observation?value-quantity=lt0.7,gt0.9");
						// make sure it doesn't find everything when using or clauses
						assertNotFind("when lt0.6 or gt0.6", "/Observation?value-quantity=lt0.6,gt0.6");
						assertNotFind("when lt0.3 or gt0.9", "/Observation?value-quantity=lt0.3,gt0.9");
					}

					@Test
					void ltOrGe() {
						withObservationWithValueQuantity(0.6);

						assertFind("when lt0.7 or ge0.2", "/Observation?value-quantity=lt0.7,ge0.2");
						assertNotFind("when lt0.6 or ge0.8", "/Observation?value-quantity=lt0.6,ge0.8");
					}

					@Test
					void gtOrGt() {
						withObservationWithValueQuantity(0.6);

						assertFind("when gt0.5 or gt0.8", "/Observation?value-quantity=gt0.5,gt0.8");
						assertNotFind("when gt0.6 or gt0.8", "/Observation?value-quantity=gt0.6,gt0.8");
					}

					@Test
					void geOrGe() {
						withObservationWithValueQuantity(0.6);

						assertFind("when ge0.5 or ge0.7", "/Observation?value-quantity=ge0.5,ge0.7");
						assertNotFind("when ge0.65 or ge0.7", "/Observation?value-quantity=ge0.65,ge0.7");
					}

					@Test
					void ltOrLt() {
						withObservationWithValueQuantity(0.6);

						assertFind("when lt0.5 or lt0.7", "/Observation?value-quantity=lt0.5,lt0.7");
						assertNotFind("when lt0.55 or lt0.3", "/Observation?value-quantity=lt0.55,lt0.3");
					}

					@Test
					void leOrLe() {
						withObservationWithValueQuantity(0.6);

						assertFind("when le0.5 or le0.6", "/Observation?value-quantity=le0.5,le0.6");
						assertNotFind("when le0.5 or le0.59", "/Observation?value-quantity=le0.5,le0.59");
					}

				}

				@Test
				void testMultipleComponentsHandlesAndOr() {
					Observation obs1 = getObservation();
					addComponentWithCodeAndQuantity(obs1, "8480-6", 107);
					addComponentWithCodeAndQuantity(obs1, "8462-4", 60);

					IIdType obs1Id = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

					Observation obs2 = getObservation();
					addComponentWithCodeAndQuantity(obs2, "8480-6",307);
					addComponentWithCodeAndQuantity(obs2, "8462-4",260);

					myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

					// andClauses
					{
						String theUrl = "/Observation?component-value-quantity=107&component-value-quantity=60";
						List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
						assertThat("when same component with qtys 107 and 60", resourceIds, hasItem(equalTo(obs1Id.getIdPart())));
					}
					{
						String theUrl = "/Observation?component-value-quantity=107&component-value-quantity=260";
						List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
						assertThat("when same component with qtys 107 and 260", resourceIds, empty());
					}

					//andAndOrClauses
					{
						String theUrl = "/Observation?component-value-quantity=107&component-value-quantity=gt50,lt70";
						List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
						assertThat("when same component with qtys 107 and lt70,gt80", resourceIds, hasItem(equalTo(obs1Id.getIdPart())));
					}
					{
						String theUrl = "/Observation?component-value-quantity=50,70&component-value-quantity=260";
						List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
						assertThat("when same component with qtys 50,70 and 260", resourceIds, empty());
					}

					// multipleAndsWithMultipleOrsEach
					{
						String theUrl = "/Observation?component-value-quantity=50,60&component-value-quantity=105,107";
						List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
						assertThat("when same component with qtys 50,60 and 105,107", resourceIds, hasItem(equalTo(obs1Id.getIdPart())));
					}
					{
						String theUrl = "/Observation?component-value-quantity=50,60&component-value-quantity=250,260";
						List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
						assertThat("when same component with qtys 50,60 and 250,260", resourceIds, empty());
					}
				}


				private Observation getObservation() {
					Observation obs = new Observation();
					obs.getCode().addCoding().setCode("85354-9").setSystem("http://loinc.org");
					obs.setStatus(Observation.ObservationStatus.FINAL);
					return obs;
				}

				private Quantity getQuantity(double theValue) {
					return new Quantity().setValue(theValue).setUnit("mmHg").setSystem("http://unitsofmeasure.org").setCode("mm[Hg]");
				}

				private Observation.ObservationComponentComponent addComponentWithCodeAndQuantity(Observation theObservation, String theConceptCode, double theQuantityValue) {
					Observation.ObservationComponentComponent comp = theObservation.addComponent();
					CodeableConcept cc1_1 = new CodeableConcept();
					cc1_1.addCoding().setCode(theConceptCode).setSystem("http://loinc.org");
					comp.setCode(cc1_1);
					comp.setValue(getQuantity(theQuantityValue));
					return comp;
				}


			}

			@Nested
			public class Sorting {

				@Test
				public void sortByNumeric() {
					String idAlpha7 = withObservationWithValueQuantity(0.7).getIdPart();
					String idAlpha2 = withObservationWithValueQuantity(0.2).getIdPart();
					String idAlpha5 = withObservationWithValueQuantity(0.5).getIdPart();

					List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_sort=value-quantity");
					assertThat(allIds, contains(idAlpha2, idAlpha5, idAlpha7));
				}
			}

		}


		@Nested
		public class QuantityNormalizedSearch {

			@BeforeEach
			void setUp() {
				myDaoConfig.getModelConfig().setNormalizedQuantitySearchLevel(
					NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
			}

			@Nested
			public class SimpleQueries {

				@Test
				public void ne() {
					withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L" );

					assertFind("when lt UCUM", "/Observation?value-quantity=ne70|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertFind("when gt UCUM", "/Observation?value-quantity=ne50|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertNotFind("when eq UCUM", "/Observation?value-quantity=ne60|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				}

				@Test
				public void eq() {
					withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L" );

					assertFind("when eq UCUM 10*3/L ", "/Observation?value-quantity=60|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertFind("when eq UCUM 10*9/L", "/Observation?value-quantity=0.000060|" + UCUM_CODESYSTEM_URL + "|10*9/L");
					assertFind("when gt UCUM 10*3/L", "/Observation?value-quantity=eq58|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertFind("when le UCUM 10*3/L", "/Observation?value-quantity=eq63|" + UCUM_CODESYSTEM_URL + "|10*3/L");

					assertNotFind("when ne UCUM 10*3/L", "/Observation?value-quantity=80|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertNotFind("when gt UCUM 10*3/L", "/Observation?value-quantity=50|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertNotFind("when lt UCUM 10*3/L", "/Observation?value-quantity=70|" + UCUM_CODESYSTEM_URL + "|10*3/L");

					assertFind("Units required to match and do", "/Observation?value-quantity=60000|" + UCUM_CODESYSTEM_URL + "|/L");
					// request generates a quantity which value matches the "value-norm", but not the "code-norm"
					assertNotFind("Units required to match and don't",  "/Observation?value-quantity=6000000000|" + UCUM_CODESYSTEM_URL + "|cm");
				}

				@Test
				public void ap() {
					withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L" );

					assertNotFind("when gt UCUM", "/Observation?value-quantity=ap50|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertFind("when little gt UCUM", "/Observation?value-quantity=ap58|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertFind("when eq UCUM", "/Observation?value-quantity=ap60|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertFind("when a little lt UCUM", "/Observation?value-quantity=ap63|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertNotFind("when lt UCUM", "/Observation?value-quantity=ap71|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				}

				@Test
				public void gt() {
					withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L" );

					assertFind("when gt UCUM", "/Observation?value-quantity=gt50|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertNotFind("when eq UCUM", "/Observation?value-quantity=gt60|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertNotFind("when lt UCUM", "/Observation?value-quantity=gt71|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				}

				@Test
				public void ge() {
					withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L" );

					assertFind("when gt UCUM", "/Observation?value-quantity=ge50|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertFind("when eq UCUM", "/Observation?value-quantity=ge60|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertNotFind("when lt UCUM", "/Observation?value-quantity=ge62|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				}

				@Test
				public void lt() {
					withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L" );

					assertNotFind("when gt", "/Observation?value-quantity=lt50|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertNotFind("when eq", "/Observation?value-quantity=lt60|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertFind("when lt", "/Observation?value-quantity=lt70|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				}

				@Test
				public void le() {
					withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L" );

					assertNotFind("when gt", "/Observation?value-quantity=le50|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertFind("when eq", "/Observation?value-quantity=le60|" + UCUM_CODESYSTEM_URL + "|10*3/L");
					assertFind("when lt", "/Observation?value-quantity=le70|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				}


				/**
				 * "value-quantity" data is stored in a nested object, so if not queried  properly
				 * it could return false positives. For instance: two Observations for following
				 * combinations of code and value:
				 *  Obs 1   code AAA1  value: 123
				 *  Obs 2   code BBB2  value: 456
				 *  A search for code: AAA1 and value: 456 would bring both observations instead of the expected empty reply,
				 *  unless both predicates are enclosed in a "nested"
				 * */
				@Test
				void nestedMustCorrelate() {
					withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L" );
					withObservationWithQuantity(0.02, UCUM_CODESYSTEM_URL, "10*3/L" );

					assertNotFind("when one predicate matches each object", "/Observation" +
						"?value-quantity=0.06|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				}
			}


			@Nested
			public class CombinedQueries {

				@Test
				void gtAndLt() {
					withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L" );

					assertFind("when gt 50 and lt 70", "/Observation" +
						"?value-quantity=gt50|" + UCUM_CODESYSTEM_URL + "|10*3/L" +
						"&value-quantity=lt70|" + UCUM_CODESYSTEM_URL + "|10*3/L");

					assertNotFind("when gt50 and lt60", "/Observation" +
						"?value-quantity=gt50|" + UCUM_CODESYSTEM_URL + "|10*3/L" +
						"&value-quantity=lt60|" + UCUM_CODESYSTEM_URL + "|10*3/L");

					assertNotFind("when gt65 and lt70", "/Observation" +
						"?value-quantity=gt65|" + UCUM_CODESYSTEM_URL + "|10*3/L" +
						"&value-quantity=lt70|" + UCUM_CODESYSTEM_URL + "|10*3/L");

					assertNotFind("when gt 70 and lt 50", "/Observation" +
						"?value-quantity=gt70|" + UCUM_CODESYSTEM_URL + "|10*3/L" +
						"&value-quantity=lt50|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				}

				@Test
				void gtAndLtWithMixedUnits() {
					withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L" );

					assertFind("when gt 50|10*3/L and lt 70|10*9/L", "/Observation" +
						"?value-quantity=gt50|" + UCUM_CODESYSTEM_URL + "|10*3/L" +
						"&value-quantity=lt0.000070|" + UCUM_CODESYSTEM_URL + "|10*9/L");
				}

				@Test
				public void multipleSearchParamsAreSeparate() {
					// for debugging
					//	myLogbackLevelOverrideExtension.setLogLevel(DaoTestDataBuilder.class, Level.DEBUG);

					// this configuration must generate a combo-value-quantity entry with both quantity objects
					myResourceId = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withQuantityAtPath("valueQuantity", 0.02, UCUM_CODESYSTEM_URL, "10*6/L"),
						myTestDataBuilder.withQuantityAtPath("component.valueQuantity", 0.06, UCUM_CODESYSTEM_URL, "10*6/L")
					));

					//	myLogbackLevelOverrideExtension.resetLevel(DaoTestDataBuilder.class);

					assertFind("by value", "Observation?value-quantity=0.02|" + UCUM_CODESYSTEM_URL + "|10*6/L");
					assertFind("by component value", "Observation?component-value-quantity=0.06|" + UCUM_CODESYSTEM_URL + "|10*6/L");

					assertNotFind("by value", "Observation?value-quantity=0.06|" + UCUM_CODESYSTEM_URL + "|10*6/L");
					assertNotFind("by component value", "Observation?component-value-quantity=0.02|" + UCUM_CODESYSTEM_URL + "|10*6/L");
				}
			}

			/**
			 * Sorting is now implemented for normalized quantities
			 */
			@Nested
			public class Sorting {

				@Test
				public void sortByNumeric() {
					String idAlpha1 = withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L" ).getIdPart();   		// 60,000
					String idAlpha2 = withObservationWithQuantity(50, UCUM_CODESYSTEM_URL, "10*3/L" ).getIdPart();			// 50,000
					String idAlpha3 = withObservationWithQuantity(0.000070, UCUM_CODESYSTEM_URL, "10*9/L" ).getIdPart();	// 70_000

					// this search is not freetext because there is no freetext-known parameter name
					List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_sort=value-quantity");
					assertThat(allIds, contains(idAlpha2, idAlpha1, idAlpha3));
				}
			}

		}



		private void assertFind(String theMessage, String theUrl) {
			List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
			assertThat(theMessage, resourceIds, hasItem(equalTo(myResourceId.getIdPart())));
		}

		private void assertNotFind(String theMessage, String theUrl) {
			List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
			assertThat(theMessage, resourceIds, not(hasItem(equalTo(myResourceId.getIdPart()))));
		}

		private IIdType withObservationWithQuantity(double theValue, String theSystem, String theCode) {
			myResourceId = myTestDataBuilder.createObservation(asArray(
				myTestDataBuilder.withQuantityAtPath("valueQuantity", theValue, theSystem, theCode)
			));
			return myResourceId;
		}

		private IIdType withObservationWithValueQuantity(double theValue) {
			myResourceId = myTestDataBuilder.createObservation(List.of(myTestDataBuilder.withElementAt("valueQuantity",
				myTestDataBuilder.withPrimitiveAttribute("value", theValue),
				myTestDataBuilder.withPrimitiveAttribute("system", UCUM_CODESYSTEM_URL),
				myTestDataBuilder.withPrimitiveAttribute("code", "mm[Hg]")
			)));
			return myResourceId;
		}

	}

	@Nested
	public class TagTypesSearch {

		@BeforeEach
		public void enableResourceStorage() {
			myDaoConfig.setStoreResourceInHSearchIndex(true);
		}

		@AfterEach
		public void resetResourceStorage() {
			myDaoConfig.setStoreResourceInHSearchIndex(new DaoConfig().isStoreResourceInHSearchIndex());
		}

		@Test
		public void tagTagSearch() {
			String id = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCode"),
				myTestDataBuilder.withTag("http://example.com", "aTag"))).getIdPart();

			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_tag=http://example.com|aTag");

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
			assertThat(allIds, contains(id));
		}

		@Test
		public void tagSecuritySearch() {
			String id = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCode"),
				myTestDataBuilder.withSecurity("http://example.com", "security-label"))).getIdPart();

			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_security=http://example.com|security-label");

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
			assertThat(allIds, contains(id));
		}

		@Test
		public void tokenAndOrCombinedSearch() {
			String id = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCode"),
				myTestDataBuilder.withSecurity("http://example.com", "security-label"),
				myTestDataBuilder.withSecurity("http://example.com", "other-security-label"))).getIdPart();

			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation" +
				"?_security=http://example.com|non-existing-security-label,http://example.com|security-label" +
				"&_security=http://example.com|other-non-existing-security-label,http://example.com|other-security-label");

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
			assertThat(allIds, contains(id));
		}

		@Test
		public void tokenAndOrCombinedSearch_failing_and_with_multiple_or() {
			myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCode"),
				myTestDataBuilder.withTag("http://example.com", "aTag"),
				myTestDataBuilder.withTag("http://example.com", "anotherTag"))).getIdPart();

			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation" +
				"?_tag=http://example.com|not-existing-tag,http://example.com|one-more-not-existing-tag" +
				"&_tag=http://example.com|other-not-existing-tag,http://example.com|anotherTag");

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
			assertThat(allIds, empty());
		}

		@Test
		public void tokenAndOrCombinedSearch_failing_and_with_single_or() {
			myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCode"),
				myTestDataBuilder.withTag("http://example.com", "aTag"),
				myTestDataBuilder.withTag("http://example.com", "anotherTag"))).getIdPart();

			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation" +
				"?_tag=http://example.com|not-existing-tag" +
				"&_tag=http://example.com|other-not-existing-tag,http://example.com|anotherTag");

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
			assertThat(allIds, empty());
		}

		@Test
		public void uriAndOrCombinedSearch() {
			String id = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCode"),
				myTestDataBuilder.withProfile("http://example.com/theProfile"),
				myTestDataBuilder.withProfile("http://example.com/anotherProfile"))).getIdPart();

			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation" +
				"?_profile=http://example.com/non-existing-profile,http://example.com/theProfile" +
				"&_profile=http://example.com/other-non-existing-profile,http://example.com/anotherProfile");

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
			assertThat(allIds, contains(id));
		}

		@Test
		public void tagProfileSearch() {
			String id = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCode"),
				myTestDataBuilder.withProfile("http://example.com/theProfile"))).getIdPart();

			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_profile=http://example.com/theProfile");

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
			assertThat(allIds, contains(id));
		}

		@Test
		public void tagSourceSearch() {
			String id = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCode"),
				myTestDataBuilder.withSource(myFhirContext, "http://example.com/theSource"))).getIdPart();

			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_source=http://example.com/theSource");

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
			assertThat(allIds, contains(id));
		}

	}




	@Nested
	public class LastUpdatedTests {

		private String myOldObsId, myNewObsId;
		private String myOldLastUpdatedDateTime = "2017-03-24T03:21:47";

		@BeforeEach
		public void enableResourceStorage() {
			myDaoConfig.setStoreResourceInHSearchIndex(true);

			myOldObsId = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCodeOld"),
				myTestDataBuilder.withLastUpdated(myOldLastUpdatedDateTime) )).getIdPart();

			myNewObsId = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCodeNew"),
				myTestDataBuilder.withLastUpdated(new Date()) )).getIdPart();
		}

		@AfterEach
		public void resetResourceStorage() {
			myDaoConfig.setStoreResourceInHSearchIndex(new DaoConfig().isStoreResourceInHSearchIndex());
		}

		@Test
		public void eq() {
			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_lastUpdated=eq" + myOldLastUpdatedDateTime);

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
			assertThat(allIds, contains(myOldObsId));
		}

		@Test
		public void eqLessPrecisionRequest() {
			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_lastUpdated=eq2017-03-24");

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
			assertThat(allIds, contains(myOldObsId));
		}

		@Test
		public void ne() {
			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_lastUpdated=ne" + myOldLastUpdatedDateTime);

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
			assertThat(allIds, contains(myNewObsId));
		}

		@Test
		void gt() {
			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_lastUpdated=gt2018-01-01");

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
			assertThat(allIds, contains(myNewObsId));
		}

		@Test
		public void ge() {
			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_lastUpdated=ge" + myOldLastUpdatedDateTime);

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
			assertThat(allIds, contains(myOldObsId, myNewObsId));
		}

		@Test
		void lt() {
			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_lastUpdated=lt2018-01-01");

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
			assertThat(allIds, contains(myOldObsId));
		}

		@Test
		public void le() {
			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_lastUpdated=le" + myOldLastUpdatedDateTime);

			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
			assertThat(allIds, contains(myOldObsId));
		}


	}


	@Nested
	public class TotalParameter {

		@ParameterizedTest
		@EnumSource(SearchTotalModeEnum.class)
		public void totalParamSkipsSql(SearchTotalModeEnum theTotalModeEnum) {
			myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "theCode")));

			myCaptureQueriesListener.clear();
			myTestDaoSearch.searchForIds("Observation?code=theCode&_total=" + theTotalModeEnum);
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();
			assertEquals(1, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "bundle was built with no sql");
		}


		@Test
		public void totalIsCorrect() {
			myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-1")));
			myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-2")));
			myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-3")));

			IBundleProvider resultBundle = myTestDaoSearch.searchForBundleProvider("Observation?_total=" + SearchTotalModeEnum.ACCURATE);
			assertEquals(3, resultBundle.size());
		}

	}


	@Nested
	public class OffsetParameter {

		@BeforeEach
		public void enableResourceStorage() {
			myDaoConfig.setStoreResourceInHSearchIndex(true);
		}


		@Test
		public void offsetNoCount() {
			myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-1")));
			IIdType idCode2 = myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-2")));
			IIdType idCode3 = myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-3")));

			myCaptureQueriesListener.clear();
			List<String> resultIds = myTestDaoSearch.searchForIds("Observation?code=code-1,code-2,code-3&_offset=1");
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertThat(resultIds, containsInAnyOrder(idCode2.getIdPart(), idCode3.getIdPart()));
			// make also sure no extra SQL queries were executed
			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "bundle was built with no sql");
		}


		@Test
		public void offsetAndCount() {
			myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-1")));
			IIdType idCode2 = myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-2")));
			myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-3")));

			myCaptureQueriesListener.clear();
			List<String> resultIds = myTestDaoSearch.searchForIds("Observation?code=code-1,code-2,code-3&_offset=1&_count=1");
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertThat(resultIds, containsInAnyOrder(idCode2.getIdPart()));
			// also validate no extra SQL queries were executed
			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "bundle was built with no sql");
		}

		@Test
		public void offsetAndCountReturnsMoreThan50() {
			for (int i = 0; i < 60; i++) {
				myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-" + i)));
			}

			myCaptureQueriesListener.clear();
			List<String> resultIds = myTestDaoSearch.searchForIds("Observation?_offset=0&_count=100");
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertEquals(60, resultIds.size());
			// also validate no extra SQL queries were executed
			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "bundle was built with no sql");
		}
	}

	@Nested
	public class SortParameter {

		@BeforeEach
		void setUp() {
			myDaoConfig.getModelConfig().setNormalizedQuantitySearchLevel(
				NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		}

		@BeforeEach
		public void enableContainsAndLucene() {
			myDaoConfig.setAllowContainsSearches(true);
			myDaoConfig.setAdvancedHSearchIndexing(true);
			myDaoConfig.setStoreResourceInHSearchIndex(true);
			myDaoConfig.getModelConfig().setNormalizedQuantitySearchLevel(
				NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		}

		@AfterEach
		public void restoreContains() {
			DaoConfig defaultConfig = new DaoConfig();
			myDaoConfig.setAllowContainsSearches(defaultConfig.isAllowContainsSearches());
			myDaoConfig.setAdvancedHSearchIndexing(defaultConfig.isAdvancedHSearchIndexing());
			myDaoConfig.setStoreResourceInHSearchIndex(defaultConfig.isStoreResourceInHSearchIndex());
			myDaoConfig.getModelConfig().setNormalizedQuantitySearchLevel(
				defaultConfig.getModelConfig().getNormalizedQuantitySearchLevel() );
		}

		@Nested
		public class OneProperty {

			@Nested
			public class NotIncludingNulls {

				@Test
				public void byTokenSystemFirst() {
					String id1 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withTag("http://example.orgA", "aTagD")
					)).getIdPart();
					String id2 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withTag("http://example.orgB", "aTagC")
					)).getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=-_tag");

					assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
					// asked _tag (token) descending using system then code so order must be: id2, id1
					assertThat(getResultIds(result), contains(id2, id1));
				}

				@Test
				public void byTokenCode() {
					String id1 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withTag("http://example.org", "aTagA")
					)).getIdPart();
					String id2 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withTag("http://example.org", "aTagB")
					)).getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=-_tag");

					assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
					// asked _tag (token) descending so order must be: id2, id1
					assertThat(getResultIds(result), contains(id2, id1));
				}

				@Test
				public void byDate() {
					// check milli level precision
					String id1 = createObservation(withId("20-000"), withEffectiveDate("2017-01-20T03:21:47.000")).getIdPart();
					String id2 = createObservation(withId("24-002"), withEffectiveDate("2017-01-24T03:21:47.002")).getIdPart();
					String id3 = createObservation(withId("24-001"), withEffectiveDate("2017-01-24T03:21:47.001")).getIdPart();
					String id4 = createObservation(withId("20-002"), withEffectiveDate("2017-01-20T03:21:47.002")).getIdPart();

					myCaptureQueriesListener.clear();
					List<String> result = myTestDaoSearch.searchForIds("/Observation?_sort=-date");

					assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
					ourLog.info("byDate sort {}", result);
					// date descending - order should be id2, id1
					assertThat(result, contains(id2, id3, id4, id1));
				}

				@Test
				public void byValueString() {
					String id1 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withPrimitiveAttribute("valueString", "a-string-value-1")
					)).getIdPart();
					String id2 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withPrimitiveAttribute("valueString", "a-string-value-2")
					)).getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=-value-string");

					assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
					// requested value-string descending so order should be id2, id1
					assertThat(getResultIds(result), contains(id2, id1));
				}

				@Test
				public void byQuantity() {
					String id1 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withQuantityAtPath("valueQuantity", 50, UCUM_CODESYSTEM_URL, "10*3/L")
					)).getIdPart();
					String id2 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withQuantityAtPath("valueQuantity", 60, UCUM_CODESYSTEM_URL, "10*3/L")
					)).getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=-value-quantity");

					assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
					// requested qty descending so order should be id2, id1
					assertThat(getResultIds(result), contains(id2, id1));
				}

				@Test
				public void byUri() {
					String id1 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withTag("http://example.org", "aTag")
					)).getIdPart();
					String id2 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withProfile("http://example.com/theProfile2")
					)).getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=-_profile");

					assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
					// requested profile (uri) descending so order should be id2, id1
					assertThat(getResultIds(result), contains(id2, id1));
				}

				@Test
				public void byReference() {
					Patient patient1 = new Patient();
					IIdType patId1 = myPatientDao.create(patient1, mySrd).getId();

					Observation obs1 = new Observation();
					obs1.setSubject(new Reference(patId1.toString()));
					String obsId1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless().getIdPart();

					Patient patient2 = new Patient();
					IIdType patId2 = myPatientDao.create(patient2, mySrd).getId();

					Observation obs2 = new Observation();
					obs2.setSubject(new Reference(patId2.toString()));
					String obsId2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless().getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=-subject");

					assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
					// requested reference descending so order should be id2, id1
					assertThat(getResultIds(result), contains(obsId2, obsId1));
				}

				@Test
				public void byNumber() {
					String raId1 = createRiskAssessmentWithPredictionProbability(0.23).getIdPart();
					String raId2 = createRiskAssessmentWithPredictionProbability(0.38).getIdPart();
					String raId3 = createRiskAssessmentWithPredictionProbability(0.76).getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/RiskAssessment?_sort=-probability");

					assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
					// requested profile (uri) descending so order should be id2, id1
					assertThat(getResultIds(result), contains(raId3, raId2, raId1));
				}

			}

			@Nested
			public class IncludingNulls {

				@Test
				public void byToken() {
					String id1 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withObservationCode("http://example.com/", "the-code-1")
					)).getIdPart();
					String id2 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withTag("http://example.org", "aTag")
					)).getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=_tag");

					assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
					// should use nulls last so order must be: id2, id1
					assertThat(getResultIds(result), contains(id2, id1));
				}

				@Test
				public void byDate() {
					String id1 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withObservationCode("http://example.com/", "the-code-1")
					)).getIdPart();
					String id2 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withEffectiveDate("2017-01-24T03:21:47")
					)).getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=-date");

					assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
					// should use nulls last so order must be: id2, id1
					assertThat(getResultIds(result), contains(id2, id1));
				}

				@Test
				public void byValueString() {
					String id1 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withObservationCode("http://example.com/", "the-code-1")
					)).getIdPart();
					String id2 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withPrimitiveAttribute("valueString", "a-string-value-2")
					)).getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=-value-string");

					assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
					// should use nulls last so order must be: id2, id1
					assertThat(getResultIds(result), contains(id2, id1));
				}

				@Test
				public void byQuantity() {
					String id1 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withObservationCode("http://example.com/", "the-code-1")
					)).getIdPart();
					String id2 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withQuantityAtPath("valueQuantity", 60, UCUM_CODESYSTEM_URL, "10*3/L")
					)).getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=-value-quantity");

					assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
					// requested qty descending so order should be id2, id1
					assertThat(getResultIds(result), contains(id2, id1));
				}

				@Test
				public void byUri() {
					String id1 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withObservationCode("http://example.com/", "the-code-1")
					)).getIdPart();
					String id2 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withProfile("http://example.com/theProfile2")
					)).getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=-_profile");

					assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
					// requested nulls last so order should be id2, id1
					assertThat(getResultIds(result), contains(id2, id1));
				}

				@Test
				public void byReference() {
					Observation obs1 = new Observation();
					String obsId1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless().getIdPart();

					Patient patient2 = new Patient();
					IIdType patId2 = myPatientDao.create(patient2, mySrd).getId();

					Observation obs2 = new Observation();
					obs2.setSubject(new Reference(patId2.toString()));
					String obsId2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless().getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=-subject");

					assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
					// requested reference with nulls last so order should be: obsId2, obsId1
					assertThat(getResultIds(result), contains(obsId2, obsId1));
				}

				@Test
				public void byNumber() {
					String raId1 = createRiskAssessmentWithPredictionProbability(0.23).getIdPart();
					String raId2 = createRiskAssessment().getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/RiskAssessment?_sort=probability");

					assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
					// requested profile (uri) descending so order should be id2, id1
					assertThat(getResultIds(result), contains(raId1, raId2));
				}

			}

		}

		@Nested
		public class CombinedProperties {

			@Test
			public void byTokenAndDate() {
				String id1 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withObservationCode("http://example.com/", "the-code-1"),
					myTestDataBuilder.withEffectiveDate("2017-01-20T03:21:47"),
					myTestDataBuilder.withTag("http://example.org", "aTag")
				)).getIdPart();
				String id2 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withObservationCode("http://example.com/", "the-code-2"),
					myTestDataBuilder.withEffectiveDate("2017-01-24T03:21:47"),
					myTestDataBuilder.withTag("http://example.org", "aTag")
				)).getIdPart();

				myCaptureQueriesListener.clear();
				IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=_tag,-date");

				assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
				assertEquals(2, result.getAllResources().size());
				DateTimeType effectiveFirst = (DateTimeType) ((Observation) result.getAllResources().get(0)).getEffective();
				DateTimeType effectiveSecond = (DateTimeType) ((Observation) result.getAllResources().get(1)).getEffective();
				// requested date descending so first result should be the one with the latest effective date: id2
				assertTrue(effectiveFirst.after(effectiveSecond));
			}

			@Test
			public void byTokenAndValueString() {
				String id1 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withObservationCode("http://example.com/", "the-code-1"),
					myTestDataBuilder.withEffectiveDate("2017-01-20T03:21:47"),
					myTestDataBuilder.withTag("http://example.org", "aTag"),
					myTestDataBuilder.withPrimitiveAttribute("valueString", "a-string-value-1")
				)).getIdPart();
				String id2 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withObservationCode("http://example.com/", "the-code-2"),
					myTestDataBuilder.withEffectiveDate("2017-01-24T03:21:47"),
					myTestDataBuilder.withTag("http://example.org", "aTag"),
					myTestDataBuilder.withPrimitiveAttribute("valueString", "a-string-value-2")
				)).getIdPart();

				myCaptureQueriesListener.clear();
				IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=_tag,-value-string");

				assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
				assertEquals(2, result.getAllResources().size());
				DateTimeType effectiveFirst = (DateTimeType) ((Observation) result.getAllResources().get(0)).getEffective();
				DateTimeType effectiveSecond = (DateTimeType) ((Observation) result.getAllResources().get(1)).getEffective();
				// requested date descending so first result should be the one with the latest effective date: id2
				assertTrue(effectiveFirst.after(effectiveSecond));
			}

			@Test
			public void byTokenAndQuantity() {
				String id1 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withTag("http://example.org", "aTag"),
					myTestDataBuilder.withQuantityAtPath("valueQuantity", 50, UCUM_CODESYSTEM_URL, "10*3/L")
				)).getIdPart();
				String id2 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withTag("http://example.org", "aTag"),
					myTestDataBuilder.withQuantityAtPath("valueQuantity", 60, UCUM_CODESYSTEM_URL, "10*3/L")
				)).getIdPart();

				myCaptureQueriesListener.clear();
				IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=_tag,-value-quantity");

				assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
				// requested qty descending so order should be id2, id1
				assertThat(getResultIds(result), contains(id2, id1));
			}

			@Test
			public void allTogetherNow() {
				String id1 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withObservationCode("http://example.com/", "the-code-1"),
					myTestDataBuilder.withEffectiveDate("2017-01-20T03:21:47"),
					myTestDataBuilder.withTag("http://example.org", "aTag"),
					myTestDataBuilder.withQuantityAtPath("valueQuantity", 50, UCUM_CODESYSTEM_URL, "10*3/L")
				)).getIdPart();
				String id2 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withObservationCode("http://example.com/", "the-code-1"),
					myTestDataBuilder.withEffectiveDate("2017-01-20T03:21:47"),
					myTestDataBuilder.withTag("http://example.org", "aTag"),
					myTestDataBuilder.withQuantityAtPath("valueQuantity", 60, UCUM_CODESYSTEM_URL, "10*3/L")
				)).getIdPart();

				myCaptureQueriesListener.clear();
				IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=code,date,_tag,_tag,-value-quantity");

				assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
				// all sorted values are the same except the last (value-quantity)  so order should be id2, id1
				assertThat(getResultIds(result), contains(id2, id1));
			}

		}

		@Test
		public void directResourceLoadWhenSorting() {
			String idA = myTestDataBuilder.createObservation(List.of(myTestDataBuilder.withObservationCode("http://example.com/", "code-a"))).getIdPart();
			String idC = myTestDataBuilder.createObservation(List.of(myTestDataBuilder.withObservationCode("http://example.com/", "code-c"))).getIdPart();
			String idB = myTestDataBuilder.createObservation(List.of(myTestDataBuilder.withObservationCode("http://example.com/", "code-b"))).getIdPart();
			myCaptureQueriesListener.clear();
			myHSearchEventDispatcher.register(mySearchEventListener);

			List<IBaseResource> result = searchForFastResources("Observation?_sort=-code");
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertThat( result.stream().map(r -> r.getIdElement().getIdPart()).collect(Collectors.toList()), contains(idC, idB, idA) );
			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");

			// only one hibernate search took place
			Mockito.verify(mySearchEventListener, Mockito.times(1)).hsearchEvent(IHSearchEventListener.HSearchEventType.SEARCH);
		}

	}


	@Nested
	public class NumberParameter {

		@BeforeEach
		public void enableContainsAndLucene() {
			myDaoConfig.setAllowContainsSearches(true);
			myDaoConfig.setAdvancedHSearchIndexing(true);
			myDaoConfig.setStoreResourceInHSearchIndex(true);
			myDaoConfig.getModelConfig().setNormalizedQuantitySearchLevel(
				NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		}

		@AfterEach
		public void restoreContains() {
			DaoConfig defaultConfig = new DaoConfig();
			myDaoConfig.setAllowContainsSearches(defaultConfig.isAllowContainsSearches());
			myDaoConfig.setAdvancedHSearchIndexing(defaultConfig.isAdvancedHSearchIndexing());
			myDaoConfig.setStoreResourceInHSearchIndex(defaultConfig.isStoreResourceInHSearchIndex());
			myDaoConfig.getModelConfig().setNormalizedQuantitySearchLevel(
				defaultConfig.getModelConfig().getNormalizedQuantitySearchLevel() );
		}

		@Test
		public void noExtraSql() {
			IIdType raId1 = createRiskAssessmentWithPredictionProbability(.25);

			myCaptureQueriesListener.clear();
			assertFindId("when exact", raId1, "/RiskAssessment?probability=0.25");
			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
		}

		/**
		 * The following tests are to validate the specification implementation, but they don't work because we save parameters as BigInteger
		 * which invalidates the possibility to differentiate requested significant figures, which are needed to define precision ranges
		 * Leaving them here in case some day we fix the implementations
		 * We copy the JPA implementation here, which ignores precision requests and treats all numbers using default ranges
		 * @see TestSpecCasesNotUsingSignificantFigures
		 */
		@Disabled
		@Nested
		public class TestSpecCasesUsingSignificantFigures {

			@Test
			void specCase1() {
				String raId1 = createRiskAssessmentWithPredictionProbability(99.4).getIdPart();
				String raId2 = createRiskAssessmentWithPredictionProbability(99.6).getIdPart();
				String raId3 = createRiskAssessmentWithPredictionProbability(100.4).getIdPart();
				String raId4 = createRiskAssessmentWithPredictionProbability(100.6).getIdPart();
				// [parameter]=100	Values that equal 100, to 3 significant figures precision, so this is actually searching for values in the range [99.5 ... 100.5)
				assertFindIds("when le", Set.of(raId2, raId3), "/RiskAssessment?probability=100");
			}

			@Test
			void specCase2() {
				String raId1 = createRiskAssessmentWithPredictionProbability(99.994).getIdPart();
				String raId2 = createRiskAssessmentWithPredictionProbability(99.996).getIdPart();
				String raId3 = createRiskAssessmentWithPredictionProbability(100.004).getIdPart();
				String raId4 = createRiskAssessmentWithPredictionProbability(100.006).getIdPart();
				//	[parameter]=100.00	Values that equal 100, to 5 significant figures precision, so this is actually searching for values in the range [99.995 ... 100.005)
				assertFindIds("when le", Set.of(raId2, raId3), "/RiskAssessment?probability=100.00");
			}

			@Test
			void specCase3() {
				String raId1 = createRiskAssessmentWithPredictionProbability(94).getIdPart();
				String raId2 = createRiskAssessmentWithPredictionProbability(96).getIdPart();
				String raId3 = createRiskAssessmentWithPredictionProbability(104).getIdPart();
				String raId4 = createRiskAssessmentWithPredictionProbability(106).getIdPart();
				// [parameter]=1e2	Values that equal 100, to 1 significant figures precision, so this is actually searching for values in the range [95 ... 105)
				assertFindIds("when le", Set.of(raId2, raId3), "/RiskAssessment?probability=1e2");
			}

		}

		@Nested
		public class TestSpecCasesNotUsingSignificantFigures {

			@Test
			void specCase4() {
				String raId1 = createRiskAssessmentWithPredictionProbability(99).getIdPart();
				String raId2 = createRiskAssessmentWithPredictionProbability(100).getIdPart();
				// [parameter]=lt100	Values that are less than exactly 100
				assertFindIds("when le", Set.of(raId1), "/RiskAssessment?probability=lt100");
			}

			@Test
			void specCase5() {
				String raId1 = createRiskAssessmentWithPredictionProbability(99).getIdPart();
				String raId2 = createRiskAssessmentWithPredictionProbability(100).getIdPart();
				String raId3 = createRiskAssessmentWithPredictionProbability(101).getIdPart();
				// [parameter]=le100	Values that are less or equal to exactly 100
				assertFindIds("when le", Set.of(raId1, raId2), "/RiskAssessment?probability=le100");
			}

			@Test
			void specCase6() {
				String raId1 = createRiskAssessmentWithPredictionProbability(100).getIdPart();
				String raId2 = createRiskAssessmentWithPredictionProbability(101).getIdPart();
				// [parameter]=gt100	Values that are greater than exactly 100
				assertFindIds("when le", Set.of(raId2), "/RiskAssessment?probability=gt100");
			}

			@Test
			void specCase7() {
				String raId1 = createRiskAssessmentWithPredictionProbability(99).getIdPart();
				String raId2 = createRiskAssessmentWithPredictionProbability(100).getIdPart();
				String raId3 = createRiskAssessmentWithPredictionProbability(101).getIdPart();
				// [parameter]=ge100	Values that are greater or equal to exactly 100
				assertFindIds("when le", Set.of(raId2, raId3), "/RiskAssessment?probability=ge100");
			}

			@Test
			void specCase8() {
				String raId1 = createRiskAssessmentWithPredictionProbability(99.4).getIdPart();
				String raId2 = createRiskAssessmentWithPredictionProbability(99.6).getIdPart();
				String raId3 = createRiskAssessmentWithPredictionProbability(100.4).getIdPart();
				String raId4 = createRiskAssessmentWithPredictionProbability(100.6).getIdPart();
				String raId5 = createRiskAssessmentWithPredictionProbability(100).getIdPart();
				// [parameter]=ne100	Values that are not equal to 100 (actually, in the range 99.5 to 100.5)
				assertFindIds("when le", Set.of(raId1, raId2, raId3, raId4), "/RiskAssessment?probability=ne100");
			}
		}

		@Test
		void andClauses() {
			String raId1 = createRiskAssessmentWithPredictionProbability(0.15).getIdPart();
			String raId2 = createRiskAssessmentWithPredictionProbability(0.20).getIdPart();
			String raId3 = createRiskAssessmentWithPredictionProbability(0.25).getIdPart();
			String raId4 = createRiskAssessmentWithPredictionProbability(0.35).getIdPart();
			String raId5 = createRiskAssessmentWithPredictionProbability(0.45).getIdPart();
			String raId6 = createRiskAssessmentWithPredictionProbability(0.55).getIdPart();
			assertFindIds("when le", Set.of(raId2, raId3, raId4), "/RiskAssessment?probability=ge0.2&probability=lt0.45");
		}

		@Test
		void orClauses() {
			String raId1 = createRiskAssessmentWithPredictionProbability(0.15).getIdPart();
			String raId2 = createRiskAssessmentWithPredictionProbability(0.20).getIdPart();
			String raId3 = createRiskAssessmentWithPredictionProbability(0.25).getIdPart();
			String raId4 = createRiskAssessmentWithPredictionProbability(0.35).getIdPart();
			String raId5 = createRiskAssessmentWithPredictionProbability(0.45).getIdPart();
			String raId6 = createRiskAssessmentWithPredictionProbability(0.55).getIdPart();
			assertFindIds("when le", Set.of(raId1, raId2, raId3, raId6), "/RiskAssessment?probability=le0.25,gt0.50");
		}
	}

	private IIdType createRiskAssessment() {
		return (createRiskAssessmentWithPredictionProbability(null));
	}

	private IIdType createRiskAssessmentWithPredictionProbability(Number theProbability) {
		RiskAssessment ra1 = new RiskAssessment();
		if (theProbability != null) {
			RiskAssessment.RiskAssessmentPredictionComponent component = ra1.addPrediction();
			component.setProbability(new DecimalType(theProbability.doubleValue()));
		}
		return myRiskAssessmentDao.create(ra1).getId().toUnqualifiedVersionless();
	}


	@Disabled("keeping to debug search scrolling")
	@Test
	public void withoutCount() {
		createObservations(600);

		SearchParameterMap map = new SearchParameterMap();
		map.add("code", new TokenParam().setSystem("http://example.com"));
		List<ResourcePersistentId> bp = myObservationDao.searchForIds(map, new ServletRequestDetails());
		assertNotNull(bp);
		assertEquals(600, bp.size());

	}


	private void createObservations(int theCount) {
		for (int i = 0; i < theCount; i++) {
			myTestDataBuilder.createObservation(asArray(
				myTestDataBuilder.withObservationCode("http://example.com", "code-" + i)));
		}
	}


	private Consumer<IBaseResource>[] asArray(Consumer<IBaseResource> theIBaseResourceConsumer) {
		@SuppressWarnings("unchecked")
		Consumer<IBaseResource>[] array = (Consumer<IBaseResource>[]) new Consumer[]{theIBaseResourceConsumer};
		return array;
	}





	/**
	 * Disallow context dirtying for nested classes
	 */
	public static final class TestDirtiesContextTestExecutionListener extends DirtiesContextTestExecutionListener {

		@Override
		protected void beforeOrAfterTestClass(TestContext testContext, DirtiesContext.ClassMode requiredClassMode) throws Exception {
			if ( ! testContext.getTestClass().getName().contains("$")) {
				super.beforeOrAfterTestClass(testContext, requiredClassMode);
			}
		}
	}

	private List<String> getResultIds(IBundleProvider theResult) {
		return theResult.getAllResources().stream().map(r -> r.getIdElement().getIdPart()).collect(Collectors.toList());
	}

	private void assertFindId(String theMessage, IIdType theResourceId, String theUrl) {
		List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
		assertThat(theMessage, resourceIds, hasItem(equalTo(theResourceId.getIdPart())));
	}

	private void assertFindIds(String theMessage, Collection<String> theResourceIds, String theUrl) {
		List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
		assertEquals(theResourceIds, new HashSet<>(resourceIds), theMessage);
	}

	private void assertNotFindId(String theMessage, IIdType theResourceId, String theUrl) {
		List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
		assertThat(theMessage, resourceIds, not(hasItem(equalTo(theResourceId.getIdPart()))));
	}



	/**
	 * Search for resources in the first query, instead of searching for IDs first
	 */
	public List<IBaseResource> searchForFastResources(String theQueryUrl) {
		SearchParameterMap map = myTestDaoSearch.toSearchParameters(theQueryUrl);
		map.setLoadSynchronous(true);

		SortSpec sort = (SortSpec) new ca.uhn.fhir.rest.server.method.SortParameter(myFhirCtx)
			.translateQueryParametersIntoServerArgument(fakeRequestDetailsFromUrl(theQueryUrl), null);
		if (sort != null) {
			map.setSort(sort);
		}

		return myTestDaoSearch.searchForResources(theQueryUrl);
	}


	@Nonnull
	private SystemRequestDetails fakeRequestDetailsFromUrl(String theQueryUrl) {
		SystemRequestDetails request = new SystemRequestDetails();
		UriComponents uriComponents = UriComponentsBuilder.fromUriString(theQueryUrl).build();
		uriComponents.getQueryParams()
			.forEach((key, value) -> request.addParameter(key, value.toArray(new String[0])));
		return request;
	}


}
