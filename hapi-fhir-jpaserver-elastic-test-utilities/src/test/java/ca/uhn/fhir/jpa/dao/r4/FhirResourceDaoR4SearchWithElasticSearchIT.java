package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.config.TestR4ConfigWithElasticHSearch;
import ca.uhn.fhir.jpa.dao.IHSearchEventListener;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.search.BaseSourceSearchParameterTestCases;
import ca.uhn.fhir.jpa.search.CompositeSearchParameterTestCases;
import ca.uhn.fhir.jpa.search.QuantitySearchParameterTestCases;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchSvcImpl;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.util.TestHSearchEventDispatcher;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.SpecialParam;
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
import ca.uhn.test.util.LogbackTestExtension;
import ca.uhn.test.util.LogbackTestExtensionAssert;
import ch.qos.logback.classic.Level;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.DiagnosticReport;
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
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.ServiceRequest;
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
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.model.util.UcumServiceUtil.UCUM_CODESYSTEM_URL;
import static ca.uhn.fhir.rest.api.Constants.CHARSET_UTF8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@RequiresDocker
@ContextHierarchy({
	@ContextConfiguration(classes = TestR4ConfigWithElasticHSearch.class),
	@ContextConfiguration(classes = {
		DaoTestDataBuilder.Config.class,
		TestDaoSearch.Config.class
	})
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestExecutionListeners(listeners = {
	DependencyInjectionTestExecutionListener.class
	, FhirResourceDaoR4SearchWithElasticSearchIT.TestDirtiesContextTestExecutionListener.class
})
public class FhirResourceDaoR4SearchWithElasticSearchIT extends BaseJpaTest implements ITestDataBuilder {
	public static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";
	public static final String URL_MY_VALUE_SET = "http://example.com/my_value_set";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4SearchWithElasticSearchIT.class);
	private static final String SPACE = "%20";

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
	protected IFhirResourceDaoValueSet<ValueSet> myValueSetDao;
	@Autowired
	protected ITermReadSvc myTermSvc;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	@Qualifier("myRiskAssessmentDaoR4")
	protected IFhirResourceDao<RiskAssessment> myRiskAssessmentDao;
	@Autowired
	ITestDataBuilder.WithSupport myTestDataBuilder;
	@Autowired
	TestDaoSearch myTestDaoSearch;
	@RegisterExtension
	LogbackLevelOverrideExtension myLogbackLevelOverrideExtension = new LogbackLevelOverrideExtension();

	@RegisterExtension
	LogbackTestExtension myLogbackTestExtension = new LogbackTestExtension();
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
	private IBulkDataExportJobSchedulingHelper myBulkDataScheduleHelper;
	@Autowired
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	@Qualifier("myQuestionnaireDaoR4")
	private IFhirResourceDao<Questionnaire> myQuestionnaireDao;
	@Autowired
	private IFhirResourceDao<DiagnosticReport> myDiagnosticReportDao;
	@Autowired
	@Qualifier("myQuestionnaireResponseDaoR4")
	private IFhirResourceDao<QuestionnaireResponse> myQuestionnaireResponseDao;
	@Autowired
	@Qualifier("myServiceRequestDaoR4")
	private IFhirResourceDao<ServiceRequest> myServiceRequestDao;
	@Autowired
	@Qualifier("mySearchParameterDaoR4")
	private IFhirResourceDao<SearchParameter> mySearchParameterDao;
	@Autowired
	private TestHSearchEventDispatcher myHSearchEventDispatcher;
	@Autowired
	ElasticsearchContainer myElasticsearchContainer;

	@Mock
	private IHSearchEventListener mySearchEventListener;
	@Autowired
	private ElasticsearchSvcImpl myElasticsearchSvc;


	@BeforeEach
	public void beforePurgeDatabase() {
		purgeDatabase(myStorageSettings, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataScheduleHelper);
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
		myStorageSettings.setAllowContainsSearches(true);
		myStorageSettings.setAdvancedHSearchIndexing(true);
	}

	@AfterEach
	public void restoreContains() {
		JpaStorageSettings defaultConfig = new JpaStorageSettings();
		myStorageSettings.setAllowContainsSearches(defaultConfig.isAllowContainsSearches());
		myStorageSettings.setAdvancedHSearchIndexing(defaultConfig.isAdvancedHSearchIndexing());
		myStorageSettings.setStoreResourceInHSearchIndex(defaultConfig.isStoreResourceInHSearchIndex());
	}



	class ElasticPerformanceTracingInterceptor {
		private final List<StorageProcessingMessage> messages = new ArrayList<>();

		@Hook(Pointcut.JPA_PERFTRACE_INFO)
		public void logPerformance(StorageProcessingMessage theMessage) {
			messages.add(theMessage);
		}

		public List<StorageProcessingMessage> getMessages() {
			return messages;
		}
	}


	@Test
	public void testFullTextSearchesArePerformanceLogged() {

		ElasticPerformanceTracingInterceptor elasticPerformanceTracingInterceptor = new ElasticPerformanceTracingInterceptor();
		myInterceptorRegistry.registerInterceptor(elasticPerformanceTracingInterceptor);
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
		map.add(Constants.PARAM_CONTENT, new StringParam("blood"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id1, id2));

		//Then: The Elasticsearch Query should be logged.
		assertThat(elasticPerformanceTracingInterceptor.getMessages()).hasSize(3);
		StorageProcessingMessage storageProcessingMessage = elasticPerformanceTracingInterceptor.getMessages().get(2);
		assertThat(storageProcessingMessage.getMessage()).contains("\"query\":\"( blood* )\"");

		myInterceptorRegistry.unregisterInterceptor(elasticPerformanceTracingInterceptor);

	}
	@Test
	public void testResourceContentSearch() {

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

		{ //Content works as String Param
			map = new SearchParameterMap();
			map.add(ca.uhn.fhir.rest.api.Constants.PARAM_CONTENT, new StringParam("systolic"));
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id1));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, new StringParam("blood"));
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id1, id2));
		}

		{ //_content works as Special Param
			map = new SearchParameterMap();
			map.add(ca.uhn.fhir.rest.api.Constants.PARAM_CONTENT, new SpecialParam().setValue("systolic"));
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id1));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, new SpecialParam().setValue("blood"));
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id1, id2));
		}
	}

	@Test
	public void testResourceTextSearch() {

		Observation obs1 = new Observation();
		obs1.getCode().setText("Systolic Blood Pressure");
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		obs1.setValue(new Quantity(123));
		obs1.getNoteFirstRep().setText("obs1");
		obs1.getText().setDivAsString("systolic blood pressure");
		obs1.getText().setStatus(Narrative.NarrativeStatus.ADDITIONAL);
		IIdType id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.getCode().setText("Diastolic Blood Pressure");
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		obs2.setValue(new Quantity(81));
		obs2.getText().setDivAsString("diastolic blood pressure");
		obs2.getText().setStatus(Narrative.NarrativeStatus.ADDITIONAL);
		IIdType id2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;

		{ //_text works as a string param

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, new StringParam("systolic"));
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id1));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, new StringParam("blood"));
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id1, id2));
		}

		{ //_text works as a special param

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, new SpecialParam().setValue("systolic"));
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id1));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, new SpecialParam().setValue("blood"));
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id1, id2));
		}
	}

	@Test
	public void testTextContainsFunctionality() {
		String slug = "my-special-@char!";
		Observation obs1 = new Observation();
		obs1.getCode().setText("Systolic Blood Pressure");
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		obs1.setValue(new Quantity(123));
		obs1.getNoteFirstRep().setText("obs1");
		obs1.getText().setDivAsString(slug);
		obs1.getText().setStatus(Narrative.NarrativeStatus.ADDITIONAL);
		IIdType id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.getCode().setText("Diastolic Blood Pressure");
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		obs2.setValue(new Quantity(81));
		obs2.getText().setDivAsString("diastolic blood pressure");
		obs2.getText().setStatus(Narrative.NarrativeStatus.ADDITIONAL);
		myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();


		SearchParameterMap map;

		{ //_text
			//With :contains
			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, new SpecialParam().setValue(slug).setContains(true));
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id1));

			//Without :contains
			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, new SpecialParam().setValue(slug));
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).isEmpty();
		}

	}

	@Test
	public void testLudicrouslyLongNarrative() throws IOException {
		String slug = "myveryveryveryveryveryveryveryveryveryeryveryveryveryveryveryveryveryveryeryveryveryveryveryveryveryveryveryeryveryveryveryveryveryveryveryveryeryveryveryveryveryveryveryveryverylongemailaddress@hotmail.com";

		Observation obs1 = new Observation();
		obs1.getCode().setText("Systolic Blood Pressure");
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		obs1.setValue(new Quantity(123));
		obs1.getNoteFirstRep().setText("obs1");
		obs1.getText().setDivAsString(get15000CharacterNarrativeIncludingSlugAtStart(slug));
		obs1.getText().setStatus(Narrative.NarrativeStatus.ADDITIONAL);
		IIdType id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();


		Observation obs2 = new Observation();
		obs2.getCode().setText("Diastolic Blood Pressure");
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		obs2.setValue(new Quantity(81));
		obs2.getText().setDivAsString("diastolic blood pressure");
		obs2.getText().setStatus(Narrative.NarrativeStatus.ADDITIONAL);
		IIdType id2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		Observation obs3 = new Observation();
		obs3.getCode().setText("Systolic Blood Pressure");
		obs3.setStatus(Observation.ObservationStatus.FINAL);
		obs3.setValue(new Quantity(323));
		obs3.getNoteFirstRep().setText("obs3");
		obs3.getText().setDivAsString(get15000CharacterNarrativeIncludingSlugAtEnd(slug));
		obs3.getText().setStatus(Narrative.NarrativeStatus.ADDITIONAL);
		IIdType id3 = myObservationDao.create(obs3, mySrd).getId().toUnqualifiedVersionless();


		SearchParameterMap map;

		{ //_text works as a special param
			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, new SpecialParam().setValue(slug).setContains(true));
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id1, id3));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, new SpecialParam().setValue("blood").setContains(true));
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id2));
		}

		{ //_text works as a string param
			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, new StringParam(slug).setContains(true));
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id1, id3));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, new StringParam("blood").setContains(true));
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id2));
		}
	}

	private String get15000CharacterNarrativeIncludingSlugAtEnd(String theSlug) {
		StringBuilder builder = new StringBuilder();
		int remainingNarrativeLength = 15000 - theSlug.length();
		builder.append(RandomStringUtils.randomAlphanumeric(remainingNarrativeLength));
		builder.append(" ");
		builder.append(theSlug);
		return builder.toString();
	}
	private String get15000CharacterNarrativeIncludingSlugAtStart(String theSlug) {
		StringBuilder builder = new StringBuilder();
		int remainingNarrativeLength = 15000 - theSlug.length();
		builder.append(theSlug);
		builder.append(" ");
		builder.append(RandomStringUtils.randomAlphanumeric(remainingNarrativeLength));
		return builder.toString();
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
			//ourLog.debug("Observation {}", myFhirCtx.newJsonParser().encodeResourceToString(obs2));
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
			//ourLog.debug("Observation {}", myFhirCtx.newJsonParser().encodeResourceToString(obs2));
		}
		{
			Observation obs2b = new Observation();
			obs2b.getCode().addCoding().setCode("obs2").setSystem("http://example.com").setDisplay("A trick system");
			obs2b.setStatus(Observation.ObservationStatus.FINAL);
			obs2b.setValue(new Quantity(81));
			id2b = myObservationDao.create(obs2b, mySrd).getId().toUnqualifiedVersionless();
			//ourLog.debug("Observation {}", myFhirCtx.newJsonParser().encodeResourceToString(obs2));
		}
		{
			Observation obs3 = new Observation();
			obs3.getCode().addCoding().setCode("obs3").setSystem("http://example.com").setDisplay("A trick system");
			obs3.getCode().addCoding().setCode("obs3-multiple-code").setSystem("http://example.com").setDisplay("A trick system");
			obs3.setStatus(Observation.ObservationStatus.FINAL);
			obs3.setValue(new Quantity(81));
			id3 = myObservationDao.create(obs3, mySrd).getId().toUnqualifiedVersionless();
			//ourLog.debug("Observation {}", myFhirCtx.newJsonParser().encodeResourceToString(obs2));
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
			//ourLog.debug("Observation {}", myFhirCtx.newJsonParser().encodeResourceToString(obs2));
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
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).as("Search doesn't match middle of words").isEmpty();
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
			assertObservationSearchMatches("Bare prefix matches", map, id2);
		}

		{
			// prefix
			SearchParameterMap map = new SearchParameterMap();
			map.add("code", new TokenParam("Bod").setModifier(TokenParamModifier.TEXT));
			assertObservationSearchMatches("Bare prefix matches any word, not only first", map, id2);
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
		assertThat(result).hasSize(1);
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

	/**
	 * Our url parser requires all chars to be single-byte, and in utf8, that means ascii.
	 */
	private String urlencode(String theParam) {
		return URLEncoder.encode(theParam, CHARSET_UTF8);
	}

	private void assertObservationSearchMatchesNothing(String message, SearchParameterMap map) {
		assertObservationSearchMatches(message, map);
	}

	private void assertObservationSearchMatches(String message, SearchParameterMap map, IIdType... iIdTypes) {
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).as(message).containsExactlyInAnyOrder(toValues(iIdTypes));
	}

	private void assertObservationSearchMatches(String theMessage, String theSearch, IIdType... theIds) {
		SearchParameterMap map = myTestDaoSearch.toSearchParameters(theSearch);
		assertObservationSearchMatches(theMessage, map, theIds);
	}

	/**
	 * When configuring direct resource load for populated index a full reindex is required
	 * Code should detect the case and instead of throwing "Resource not stored in search index" exception, should log
	 * a warning and route the strategy to be rerun in JPA.
	 * This test validates that behaviour.
	 */
	@Test
	public void testDirectPathWholeResourceNotIndexedWorks() {
		// setup
		myLogbackLevelOverrideExtension.setLogLevel(SearchBuilder.class, Level.WARN);
		IIdType id1 = myTestDataBuilder.createObservation(List.of(myTestDataBuilder.withObservationCode("http://example.com/", "theCode")));

		// set it after creating resource, so search doesn't find it in the index
		myStorageSettings.setStoreResourceInHSearchIndex(true);

		List<IBaseResource> result = searchForFastResources("Observation?code=theCode&_count=10&_total=accurate");

		assertThat(result).hasSize(1);
		assertEquals(((Observation) result.get(0)).getIdElement().getIdPart(), id1.getIdPart());

		LogbackTestExtensionAssert.assertThat(myLogbackTestExtension).hasWarnMessage("Some resources were not found in index. Make sure all resources were indexed. Resorting to database search.");

		// restore changed property
		JpaStorageSettings defaultConfig = new JpaStorageSettings();
		myStorageSettings.setStoreResourceInHSearchIndex(defaultConfig.isStoreResourceInHSearchIndex());
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
		assertThat(codes).contains("childAA", "childAAA", "childAAB");
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

		assertThat(resp).containsSubsequence("<code value=\"childCA\"/>", "<display value=\"Child CA\"/>");
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

		assertThat(resp).containsSubsequence("<code value=\"childCA\"/>", "<display value=\"Child CA\"/>");
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

		assertThat(resp).doesNotContainPattern("(?s)<code value=\"childCA\"/>.*<display value=\"Child CA\"/>");
	}

	@Test
	public void testExpandVsWithMultiInclude_All() throws IOException {
		CodeSystem cs = loadResource(myFhirCtx, CodeSystem.class, "/r4/expand-multi-cs.json");
		myCodeSystemDao.update(cs);

		ValueSet vs = loadResource(myFhirCtx, ValueSet.class, "/r4/expand-multi-vs-all.json");
		ValueSet expanded = myValueSetDao.expand(vs, null);

		ourLog.debug(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expanded));

		// All codes
		List<String> codes = expanded
			.getExpansion()
			.getContains()
			.stream()
			.map(ValueSet.ValueSetExpansionContainsComponent::getCode)
			.sorted()
			.collect(Collectors.toList());
		assertThat(codes).as(codes.toString()).containsExactly("advice", "message", "note", "notification");
	}

	@Test
	public void testExpandVsWithMultiInclude_Some() throws IOException {
		CodeSystem cs = loadResource(myFhirCtx, CodeSystem.class, "/r4/expand-multi-cs.json");
		myCodeSystemDao.update(cs);

		ValueSet vs = loadResource(myFhirCtx, ValueSet.class, "/r4/expand-multi-vs-all.json");
		vs.getCompose().getInclude().get(0).getConcept().remove(0);
		vs.getCompose().getInclude().get(0).getConcept().remove(0);

		ValueSet expanded = myValueSetDao.expand(vs, null);

		ourLog.debug(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expanded));

		// All codes
		List<String> codes = expanded
			.getExpansion()
			.getContains()
			.stream()
			.map(ValueSet.ValueSetExpansionContainsComponent::getCode)
			.sorted()
			.collect(Collectors.toList());
		assertThat(codes).as(codes.toString()).containsExactly("advice", "note");
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

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(JpaPid.fromId(table.getId()), URL_MY_CODE_SYSTEM, "SYSTEM NAME", "SYSTEM VERSION", cs, table);
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

		assertThat(result.getMessages()).isEmpty();

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
		List<IResourcePersistentId> bp = myObservationDao.searchForIds(map, new ServletRequestDetails());
		assertNotNull(bp);
		assertThat(bp).hasSize(600);

	}

	private void createObservations(int theCount) {
		for (int i = 0; i < theCount; i++) {
			myTestDataBuilder.createObservation(asArray(
				myTestDataBuilder.withObservationCode("http://example.com", "code-" + i)));
		}
	}

	private List<String> getResultIds(IBundleProvider theResult) {
		return theResult.getAllResources().stream().map(r -> r.getIdElement().getIdPart()).collect(Collectors.toList());
	}

	private void assertFindId(String theMessage, IIdType theResourceId, String theUrl) {
		List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
		assertThat(resourceIds).as(theMessage).contains(theResourceId.getIdPart());
	}

	private void assertFindIds(String theMessage, Collection<String> theResourceIds, String theUrl) {
		List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
		assertThat(new HashSet<>(resourceIds)).as(theMessage).isEqualTo(theResourceIds);
	}

	private void assertNotFindId(String theMessage, IIdType theResourceId, String theUrl) {
		List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
		assertThat(resourceIds).as(theMessage).doesNotContain(theResourceId.getIdPart());
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

	@Nested
	public class StringTextSearch {

		@Test
		void secondWordFound() {
			String id1 = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "Cloudy, yellow"))).getIdPart();

			List<String> resourceIds = myTestDaoSearch.searchForIds("/Observation?value-string:text=yellow");
			assertThat(resourceIds).contains(id1);
		}

		@Test
		void stringMatchesPrefixAndWhole() {
			// smit - matches "smit" and "smith"

			String id1 = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "John Smith"))).getIdPart();
			String id2 = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "Carl Smit"))).getIdPart();

			List<String> resourceIds = myTestDaoSearch.searchForIds("/Observation?value-string:text=smit");
			assertThat(resourceIds).contains(id1, id2);
		}


		@Test
		void stringPlusStarMatchesPrefixAndWhole() {
			// smit* - matches "smit" and "smith"

			String id1 = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "John Smith"))).getIdPart();
			String id2 = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "Carl Smit"))).getIdPart();

			List<String> resourceIds = myTestDaoSearch.searchForIds("/Observation?_elements=valueString&value-string:text=smit*");
			assertThat(resourceIds).contains(id1, id2);
		}


		@Test
		void quotedStringMatchesIdenticalButNotAsPrefix() {
			// "smit"- matches "smit", but not "smith"

			String id1 = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "John Smith"))).getIdPart();
			String id2 = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "Carl Smit"))).getIdPart();

			List<String> resourceIds = myTestDaoSearch.searchForIds("/Observation?value-string:text=\"smit\"");
			assertThat(resourceIds).containsExactly(id2);
		}


		@Test
		void stringTokensAreAnded() {
			String id1 = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "John Smith"))).getIdPart();
			String id2 = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "Carl Smit"))).getIdPart();

			List<String> resourceIds = myTestDaoSearch.searchForIds("/Observation?value-string:text=car%20smit");
			assertThat(resourceIds).contains(id2);
		}

		@Nested
		public class DocumentationSamplesTests {

			@Test
			void line1() {
				// | Fhir Query String | Executed Query  | Matches     | No Match
				// | Smit             | Smit*            | John Smith  | John Smi
				String id1 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "John Smith"))).getIdPart();
				String id2 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "John Smi"))).getIdPart();

				List<String> resourceIds = myTestDaoSearch.searchForIds("/Observation?value-string:text=Smit");
				assertThat(resourceIds).contains(id1);
			}

			@Test
			void line2() {
				// | Fhir Query String | Executed Query  | Matches     | No Match      | Note
				// | Jo Smit           | Jo* Smit*       | John Smith  | John Frank    | Multiple bare terms are `AND`
				String id1 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "John Smith"))).getIdPart();
				String id2 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "John Frank"))).getIdPart();

				List<String> resourceIds = myTestDaoSearch.searchForIds("/Observation?value-string:text=Jo%20Smit");
				assertThat(resourceIds).contains(id1);
			}

			@Test
			void line3() {
				// | Fhir Query String | Executed Query    | Matches     | No Match       | Note
				// | frank &vert; john | frank &vert; john | Frank Smith | Franklin Smith | SQS characters disable prefix wildcard
				String id1 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "Frank Smith"))).getIdPart();
				String id2 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "Franklin Smith"))).getIdPart();

				List<String> resourceIds = myTestDaoSearch.searchForIds("/Observation?value-string:text=frank|john");
				assertThat(resourceIds).contains(id1);
			}

			@Test
			void line4() {
				// | Fhir Query String | Executed Query  | Matches     | No Match       | Note
				// | 'frank'           | 'frank'         | Frank Smith | Franklin Smith | Quoted terms are exact match
				String id1 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "Frank Smith"))).getIdPart();
				String id2 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "Franklin Smith"))).getIdPart();

				List<String> resourceIds = myTestDaoSearch.searchForIds("/Observation?value-string:text='frank'");
				assertThat(resourceIds).contains(id1);
			}
		}
	}

	@Nested
	public class TokenTextSearch {

		@Test
		void secondWordFound() {
			String id1 = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com", "code-AA", "Cloudy, yellow"))).getIdPart();

			List<String> resourceIds = myTestDaoSearch.searchForIds("/Observation?code:text=yellow");
			assertThat(resourceIds).contains(id1);
		}

		@Test
		void stringMatchesPrefixAndWhole() {
			// smit - matches "smit" and "smith"

			String id1 = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com", "code-AA", "John Smith"))).getIdPart();
			String id2 = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com", "code-BB", "Carl Smit"))).getIdPart();

			List<String> resourceIds = myTestDaoSearch.searchForIds("/Observation?code:text=smit");
			assertThat(resourceIds).contains(id1, id2);
		}


		@Test
		void stringPlusStarMatchesPrefixAndWhole() {
			// smit* - matches "smit" and "smith"

			String id1 = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com", "code-AA", "Adam Smith"))).getIdPart();
			String id2 = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com", "code-BB", "John Smit"))).getIdPart();

			List<String> resourceIds = myTestDaoSearch.searchForIds("/Observation?code:text=smit*");
			assertThat(resourceIds).contains(id1, id2);
		}


		@Test
		void quotedStringMatchesIdenticalButNotAsPrefix() {
			// "smit"- matches "smit", but not "smith"

			String id1 = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com", "code-AA", "John Smith"))).getIdPart();
			String id2 = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com", "code-BB", "Karl Smit"))).getIdPart();

			List<String> resourceIds = myTestDaoSearch.searchForIds("/Observation?code:text=\"Smit\"");
			assertThat(resourceIds).containsExactly(id2);
		}

	}

	@Nested
	public class WithContainedIndexingIT {
		@BeforeEach
		public void enableContains() {
			// we don't support chained or contained yet, but turn it on to test we don't blow up.
			myStorageSettings.setIndexOnContainedResources(true);
			myStorageSettings.setIndexOnContainedResourcesRecursively(true);
		}

		@AfterEach
		public void restoreContains() {
			myStorageSettings.setIndexOnContainedResources(new JpaStorageSettings().isIndexOnContainedResources());
			myStorageSettings.setIndexOnContainedResourcesRecursively(new JpaStorageSettings().isIndexOnContainedResourcesRecursively());
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
			assertNotNull(id);
		}
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
			myStorageSettings.setStoreResourceInHSearchIndex(true);
		}

		@AfterEach
		public void resetResourceStorage() {
			myStorageSettings.setStoreResourceInHSearchIndex(new JpaStorageSettings().isStoreResourceInHSearchIndex());
		}


		@Test
		public void simpleTokenSkipsSql() {
			IIdType id = myTestDataBuilder.createObservation(List.of(myTestDataBuilder.withObservationCode("http://example.com/", "theCode")));
			myCaptureQueriesListener.clear();
			myHSearchEventDispatcher.register(mySearchEventListener);

			List<IBaseResource> result = searchForFastResources("Observation?code=theCode");
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertThat(result).hasSize(1);
			assertEquals(((Observation) result.get(0)).getId(), id.getIdPart());
			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);

			// only one hibernate search took place
			Mockito.verify(mySearchEventListener, Mockito.times(1)).hsearchEvent(IHSearchEventListener.HSearchEventType.SEARCH);
		}


		@Test
		public void sortDoesntRequireSqlAnymore() {

			IIdType id = myTestDataBuilder.createObservation(List.of(myTestDataBuilder.withObservationCode("http://example.com/", "theCode")));
			myCaptureQueriesListener.clear();

			List<String> ids = myTestDaoSearch.searchForIds("Observation?code=theCode&_sort=code");
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertThat(ids).hasSize(1);
			assertThat(ids).containsExactly(id.getIdPart());

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("the pids come from elastic, but we use sql to sort").isEqualTo(0);
		}

		@Test
		public void deletedResourceNotFound() {

			IIdType id = myTestDataBuilder.createObservation(List.of(myTestDataBuilder.withObservationCode("http://example.com/", "theCode")));
			myObservationDao.delete(id);
			myCaptureQueriesListener.clear();

			List<String> ids = myTestDaoSearch.searchForIds("Observation?code=theCode&_sort=code");
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertThat(ids).hasSize(0);

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("the pids come from elastic, and nothing to fetch").isEqualTo(0);
		}

		@Test
		public void forcedIdSurvivesWithNoSql() {
			IIdType id = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCode"),
				myTestDataBuilder.withId("forcedid")));
			assertEquals("forcedid", id.getIdPart());
			myCaptureQueriesListener.clear();

			List<String> ids = myTestDaoSearch.searchForIds("Observation?code=theCode");
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertThat(ids).hasSize(1);
			assertThat(ids).containsExactly(id.getIdPart());

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("no sql required").isEqualTo(0);
		}

		/**
		 * A paranoid test to make sure tags stay with the resource.
		 * <p>
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

			assertThat(observations).hasSize(1);
			List<? extends IBaseCoding> tags = observations.get(0).getMeta().getTag();
			assertThat(tags).hasSize(1);
			assertEquals("http://example.com", tags.get(0).getSystem());
			assertEquals("aTag", tags.get(0).getCode());

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
//			assertThat(observations).hasSize(1);
//			IBaseMetaType newMeta = observations.get(0).getMeta();
//			assertThat(newMeta.getProfile()).hasSize(1);
//			assertThat(newMeta.getSecurity()).hasSize(1);
//			assertThat(newMeta.getTag()).hasSize(2);
		}

		@Test
		public void noDirectSearchWhenNotSynchronousOrOffsetQuery() {
			myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-1")));
			myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-2")));
			myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-3")));
			myCaptureQueriesListener.clear();
			myHSearchEventDispatcher.register(mySearchEventListener);

			myTestDaoSearch.searchForBundleProvider("Observation?code=code-1,code-2,code-3", false);

			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(2);

			// index only queried once for count
			Mockito.verify(mySearchEventListener, Mockito.times(1)).hsearchEvent(IHSearchEventListener.HSearchEventType.SEARCH);
		}


		@Test
		public void directSearchForOffsetQuery() {
			myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-1")));
			IIdType idCode2 = myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-2")));
			IIdType idCode3 = myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-3")));
			myCaptureQueriesListener.clear();
			myHSearchEventDispatcher.register(mySearchEventListener);

			List<String> resultIds = myTestDaoSearch.searchForIds("Observation?code=code-1,code-2,code-3&_offset=1");
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertThat(resultIds).containsExactlyInAnyOrder(idCode2.getIdPart(), idCode3.getIdPart());
			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);

			// only one hibernate search took place
			Mockito.verify(mySearchEventListener, Mockito.times(1)).hsearchEvent(IHSearchEventListener.HSearchEventType.SEARCH);
		}

	}

	@Nested
	class QuantityAndNormalizedQuantitySearch extends QuantitySearchParameterTestCases {
		QuantityAndNormalizedQuantitySearch() {
			super(myTestDataBuilder.getTestDataBuilderSupport(), myTestDaoSearch, myStorageSettings);
		}
	}

	@Nested
	public class TagTypesSearch {

		@BeforeEach
		public void enableResourceStorage() {
			myStorageSettings.setStoreResourceInHSearchIndex(true);
		}

		@AfterEach
		public void resetResourceStorage() {
			myStorageSettings.setStoreResourceInHSearchIndex(new JpaStorageSettings().isStoreResourceInHSearchIndex());
		}

		@Test
		public void tagTagSearch() {
			String id = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCode"),
				myTestDataBuilder.withTag("http://example.com", "aTag"))).getIdPart();

			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_tag=http://example.com|aTag");

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
			assertThat(allIds).containsExactly(id);
		}

		@Test
		public void tagSecuritySearch() {
			String id = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCode"),
				myTestDataBuilder.withSecurity("http://example.com", "security-label"))).getIdPart();

			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_security=http://example.com|security-label");

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
			assertThat(allIds).containsExactly(id);
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

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
			assertThat(allIds).containsExactly(id);
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

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
			assertThat(allIds).isEmpty();
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

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
			assertThat(allIds).isEmpty();
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

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
			assertThat(allIds).containsExactly(id);
		}

		@Test
		public void tagProfileSearch() {
			String id = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCode"),
				myTestDataBuilder.withProfile("http://example.com/theProfile"))).getIdPart();

			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_profile=http://example.com/theProfile");

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
			assertThat(allIds).containsExactly(id);
		}

		@Test
		public void tagSourceSearch() {
			String id = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCode"),
				myTestDataBuilder.withSource("http://example.com/theSource"))).getIdPart();

			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_source=http://example.com/theSource");

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
			assertThat(allIds).containsExactly(id);
		}

	}

	@Nested
	public class LastUpdatedTests {

		private String myOldObsId, myNewObsId;
		private final String myOldLastUpdatedDateTime = "2017-03-24T03:21:47";

		@BeforeEach
		public void enableResourceStorage() {
			myStorageSettings.setStoreResourceInHSearchIndex(true);

			myOldObsId = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCodeOld"),
				myTestDataBuilder.withLastUpdated(myOldLastUpdatedDateTime))).getIdPart();

			myNewObsId = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withObservationCode("http://example.com/", "theCodeNew"),
				myTestDataBuilder.withLastUpdated(new Date()))).getIdPart();
		}

		@AfterEach
		public void resetResourceStorage() {
			myStorageSettings.setStoreResourceInHSearchIndex(new JpaStorageSettings().isStoreResourceInHSearchIndex());
		}

		@Test
		public void eq() {
			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_lastUpdated=eq" + myOldLastUpdatedDateTime);

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
			assertThat(allIds).containsExactly(myOldObsId);
		}

		@Test
		public void eqLessPrecisionRequest() {
			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_lastUpdated=eq2017-03-24");

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
			assertThat(allIds).containsExactly(myOldObsId);
		}

		@Test
		public void ne() {
			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_lastUpdated=ne" + myOldLastUpdatedDateTime);

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
			assertThat(allIds).containsExactly(myNewObsId);
		}

		@Test
		void gt() {
			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_lastUpdated=gt2018-01-01");

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
			assertThat(allIds).containsExactly(myNewObsId);
		}

		@Test
		public void ge() {
			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_lastUpdated=ge" + myOldLastUpdatedDateTime);

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
			assertThat(allIds).containsExactly(myOldObsId, myNewObsId);
		}

		@Test
		void lt() {
			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_lastUpdated=lt2018-01-01");

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
			assertThat(allIds).containsExactly(myOldObsId);
		}

		@Test
		public void le() {
			myCaptureQueriesListener.clear();
			List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_lastUpdated=le" + myOldLastUpdatedDateTime);

			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
			assertThat(allIds).containsExactly(myOldObsId);
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
			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("bundle was built with no sql").isEqualTo(1);
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
			myStorageSettings.setStoreResourceInHSearchIndex(true);
		}


		@Test
		public void offsetNoCount() {
			myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-1")));
			IIdType idCode2 = myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-2")));
			IIdType idCode3 = myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-3")));

			myCaptureQueriesListener.clear();
			List<String> resultIds = myTestDaoSearch.searchForIds("Observation?code=code-1,code-2,code-3&_offset=1");
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertThat(resultIds).containsExactlyInAnyOrder(idCode2.getIdPart(), idCode3.getIdPart());
			// make also sure no extra SQL queries were executed
			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("bundle was built with no sql").isEqualTo(0);
		}


		@Test
		public void offsetAndCount() {
			myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-1")));
			IIdType idCode2 = myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-2")));
			myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-3")));

			myCaptureQueriesListener.clear();
			List<String> resultIds = myTestDaoSearch.searchForIds("Observation?code=code-1,code-2,code-3&_offset=1&_count=1");
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertThat(resultIds).containsExactlyInAnyOrder(idCode2.getIdPart());
			// also validate no extra SQL queries were executed
			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("bundle was built with no sql").isEqualTo(0);
		}

		@Test
		public void offsetAndCountReturnsMoreThan50() {
			for (int i = 0; i < 60; i++) {
				myTestDataBuilder.createObservation(asArray(myTestDataBuilder.withObservationCode("http://example.com/", "code-" + i)));
			}

			myCaptureQueriesListener.clear();
			List<String> resultIds = myTestDaoSearch.searchForIds("Observation?_offset=0&_count=100");
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			assertThat(resultIds).hasSize(60);
			// also validate no extra SQL queries were executed
			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("bundle was built with no sql").isEqualTo(0);
		}
	}

	@Nested
	public class SortParameter {

		@BeforeEach
		void setUp() {
			myStorageSettings.setNormalizedQuantitySearchLevel(
				NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		}

		@BeforeEach
		public void enableContainsAndLucene() {
			myStorageSettings.setAllowContainsSearches(true);
			myStorageSettings.setAdvancedHSearchIndexing(true);
			myStorageSettings.setStoreResourceInHSearchIndex(true);
			myStorageSettings.setNormalizedQuantitySearchLevel(
				NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		}

		@AfterEach
		public void restoreContains() {
			JpaStorageSettings defaultConfig = new JpaStorageSettings();
			myStorageSettings.setAllowContainsSearches(defaultConfig.isAllowContainsSearches());
			myStorageSettings.setAdvancedHSearchIndexing(defaultConfig.isAdvancedHSearchIndexing());
			myStorageSettings.setStoreResourceInHSearchIndex(defaultConfig.isStoreResourceInHSearchIndex());
			myStorageSettings.setNormalizedQuantitySearchLevel(
				defaultConfig.getNormalizedQuantitySearchLevel());
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

			assertThat(result.stream().map(r -> r.getIdElement().getIdPart()).collect(Collectors.toList())).containsExactly(idC, idB, idA);
			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);

			// only one hibernate search took place
			Mockito.verify(mySearchEventListener, Mockito.times(1)).hsearchEvent(IHSearchEventListener.HSearchEventType.SEARCH);
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

					assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
					// asked _tag (token) descending using system then code so order must be: id2, id1
					assertThat(getResultIds(result)).containsExactly(id2, id1);
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

					assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
					// asked _tag (token) descending so order must be: id2, id1
					assertThat(getResultIds(result)).containsExactly(id2, id1);
				}

				@Test
				public void byDate() {
					// check milli level precision
					String id1 = createObservation(List.of(withId("20-000"), withEffectiveDate("2017-01-20T03:21:47.000"))).getIdPart();
					String id2 = createObservation(List.of(withId("24-002"), withEffectiveDate("2017-01-24T03:21:47.002"))).getIdPart();
					String id3 = createObservation(List.of(withId("24-001"), withEffectiveDate("2017-01-24T03:21:47.001"))).getIdPart();
					String id4 = createObservation(List.of(withId("20-002"), withEffectiveDate("2017-01-20T03:21:47.002"))).getIdPart();

					myCaptureQueriesListener.clear();
					List<String> result = myTestDaoSearch.searchForIds("/Observation?_sort=-date");

					assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
					ourLog.info("byDate sort {}", result);
					// date descending - order should be id2, id1
					assertThat(result).containsExactly(id2, id3, id4, id1);
				}

				@Test
				public void byValueString() {
					String id1 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "a-string-value-1")
					)).getIdPart();
					String id2 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "a-string-value-2")
					)).getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=-value-string");

					assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
					// requested value-string descending so order should be id2, id1
					assertThat(getResultIds(result)).containsExactly(id2, id1);
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

					assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
					// requested qty descending so order should be id2, id1
					assertThat(getResultIds(result)).containsExactly(id2, id1);
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

					assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
					// requested profile (uri) descending so order should be id2, id1
					assertThat(getResultIds(result)).containsExactly(id2, id1);
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

					assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
					// requested reference descending so order should be id2, id1
					assertThat(getResultIds(result)).containsExactly(obsId2, obsId1);
				}

				@Test
				public void byNumber() {
					String raId1 = createRiskAssessmentWithPredictionProbability(0.23).getIdPart();
					String raId2 = createRiskAssessmentWithPredictionProbability(0.38).getIdPart();
					String raId3 = createRiskAssessmentWithPredictionProbability(0.76).getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/RiskAssessment?_sort=-probability");

					assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
					// requested profile (uri) descending so order should be id2, id1
					assertThat(getResultIds(result)).containsExactly(raId3, raId2, raId1);
				}

				@Test
				public void sortWithOffset() {
					String raId1 = createRiskAssessmentWithPredictionProbability(0.23).getIdPart();
					String raId2 = createRiskAssessmentWithPredictionProbability(0.38).getIdPart();
					String raId3 = createRiskAssessmentWithPredictionProbability(0.76).getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/RiskAssessment?_sort=-probability&_offset=1");

					assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
					// requested profile (uri) descending so order should be id2, id1
					assertThat(getResultIds(result)).containsExactly(raId2, raId1);
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

					assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
					// should use nulls last so order must be: id2, id1
					assertThat(getResultIds(result)).containsExactly(id2, id1);
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

					assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
					// should use nulls last so order must be: id2, id1
					assertThat(getResultIds(result)).containsExactly(id2, id1);
				}

				@Test
				public void byValueString() {
					String id1 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withObservationCode("http://example.com/", "the-code-1")
					)).getIdPart();
					String id2 = myTestDataBuilder.createObservation(List.of(
						myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "a-string-value-2")
					)).getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=-value-string");

					assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
					// should use nulls last so order must be: id2, id1
					assertThat(getResultIds(result)).containsExactly(id2, id1);
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

					assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
					// requested qty descending so order should be id2, id1
					assertThat(getResultIds(result)).containsExactly(id2, id1);
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

					assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
					// requested nulls last so order should be id2, id1
					assertThat(getResultIds(result)).containsExactly(id2, id1);
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

					assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
					// requested reference with nulls last so order should be: obsId2, obsId1
					assertThat(getResultIds(result)).containsExactly(obsId2, obsId1);
				}

				@Test
				public void byNumber() {
					String raId1 = createRiskAssessmentWithPredictionProbability(0.23).getIdPart();
					String raId2 = createRiskAssessment().getIdPart();

					myCaptureQueriesListener.clear();
					IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/RiskAssessment?_sort=probability");

					assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
					// requested profile (uri) descending so order should be id2, id1
					assertThat(getResultIds(result)).containsExactly(raId1, raId2);
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

				assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
				assertThat(result.getAllResources()).hasSize(2);
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
					myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "a-string-value-1")
				)).getIdPart();
				String id2 = myTestDataBuilder.createObservation(List.of(
					myTestDataBuilder.withObservationCode("http://example.com/", "the-code-2"),
					myTestDataBuilder.withEffectiveDate("2017-01-24T03:21:47"),
					myTestDataBuilder.withTag("http://example.org", "aTag"),
					myTestDataBuilder.withResourcePrimitiveAttribute("valueString", "a-string-value-2")
				)).getIdPart();

				myCaptureQueriesListener.clear();
				IBundleProvider result = myTestDaoSearch.searchForBundleProvider("/Observation?_sort=_tag,-value-string");

				assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
				assertThat(result.getAllResources()).hasSize(2);
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

				assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
				// requested qty descending so order should be id2, id1
				assertThat(getResultIds(result)).containsExactly(id2, id1);
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

				assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
				// all sorted values are the same except the last (value-quantity)  so order should be id2, id1
				assertThat(getResultIds(result)).containsExactly(id2, id1);
			}

		}

	}

	@Nested
	public class NumberParameter {

		@BeforeEach
		public void enableContainsAndLucene() {
			myStorageSettings.setAllowContainsSearches(true);
			myStorageSettings.setAdvancedHSearchIndexing(true);
			myStorageSettings.setStoreResourceInHSearchIndex(true);
			myStorageSettings.setNormalizedQuantitySearchLevel(
				NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		}

		@AfterEach
		public void restoreContains() {
			JpaStorageSettings defaultConfig = new JpaStorageSettings();
			myStorageSettings.setAllowContainsSearches(defaultConfig.isAllowContainsSearches());
			myStorageSettings.setAdvancedHSearchIndexing(defaultConfig.isAdvancedHSearchIndexing());
			myStorageSettings.setStoreResourceInHSearchIndex(defaultConfig.isStoreResourceInHSearchIndex());
			myStorageSettings.setNormalizedQuantitySearchLevel(
				defaultConfig.getNormalizedQuantitySearchLevel());
		}

		@Test
		public void noExtraSql() {
			IIdType raId1 = createRiskAssessmentWithPredictionProbability(.25);

			myCaptureQueriesListener.clear();
			assertFindId("when exact", raId1, "/RiskAssessment?probability=0.25");
			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(0);
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

		/**
		 * The following tests are to validate the specification implementation
		 */
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
				String raId1 = createRiskAssessmentWithPredictionProbability(40).getIdPart();
				String raId2 = createRiskAssessmentWithPredictionProbability(55).getIdPart();
				String raId3 = createRiskAssessmentWithPredictionProbability(140).getIdPart();
				String raId4 = createRiskAssessmentWithPredictionProbability(155).getIdPart();
				// [parameter]=1e2	Values that equal 100, to 1 significant figures precision, so this is actually searching for values in the range [95 ... 105)
				// Previous line from spec is wrong! Range for [parameter]=1e2, having 1 significant figures precision, should be [50 ... 150]
				assertFindIds("when le", Set.of(raId2, raId3), "/RiskAssessment?probability=1e2");
			}

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
				String raId3 = createRiskAssessmentWithPredictionProbability(100).getIdPart();
				String raId4 = createRiskAssessmentWithPredictionProbability(100.4).getIdPart();
				String raId5 = createRiskAssessmentWithPredictionProbability(100.6).getIdPart();
				// [parameter]=ne100	Values that are not equal to 100 (actually, in the range 99.5 to 100.5)
				assertFindIds("when le", Set.of(raId1, raId5), "/RiskAssessment?probability=ne100");
			}

		}
	}

	@Disabled("while finding out strategy for this fix")
	@Nested
	public class ReferenceParameter {

		@BeforeEach
		public void setup() {
			myStorageSettings.setAdvancedHSearchIndexing(true);
		}

		@AfterEach
		public void tearDown() {
			JpaStorageSettings defaultConfig = new JpaStorageSettings();
			myStorageSettings.setAdvancedHSearchIndexing(defaultConfig.isAdvancedHSearchIndexing());
		}

		@Test
		public void observationSubjectReferenceTest() {
			Patient patient = new Patient();
			DaoMethodOutcome outcome = myPatientDao.create(patient, mySrd);
			IIdType patId = outcome.getId();

			IIdType obsId = myTestDataBuilder.createObservation(List.of(myTestDataBuilder.withSubject(patId.toString())));

			myCaptureQueriesListener.clear();
			assertFindId("when exact", obsId, "/Observation?subject=" + patId.getVersionIdPartAsLong());
			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).as("we build the bundle with no sql").isEqualTo(myStorageSettings.isAdvancedHSearchIndexing() ? 1 : 2);
		}
	}


	@Nested
	class CompositeSearch extends CompositeSearchParameterTestCases {
		CompositeSearch() {
			super(myTestDataBuilder.getTestDataBuilderSupport(), myTestDaoSearch);
		}

		/**
		 * HSearch supports it!
		 */
		@Override
		protected boolean isCorrelatedSupported() {
			return true;
		}
	}

	@Nested
	class SourceSearchParameterTestCases extends BaseSourceSearchParameterTestCases {
		SourceSearchParameterTestCases() {
			super(myTestDataBuilder.getTestDataBuilderSupport(), myTestDaoSearch, myStorageSettings);
		}

		@Override
		protected boolean isRequestIdSupported() {
			return false;
		}
	}

	/**
	 * Disallow context dirtying for nested classes
	 */
	public static final class TestDirtiesContextTestExecutionListener extends DirtiesContextTestExecutionListener {

		@Override
		protected void beforeOrAfterTestClass(TestContext testContext, DirtiesContext.ClassMode requiredClassMode) throws Exception {
			if (!testContext.getTestClass().getName().contains("$")) {
				super.beforeOrAfterTestClass(testContext, requiredClassMode);
			}
		}
	}


}
