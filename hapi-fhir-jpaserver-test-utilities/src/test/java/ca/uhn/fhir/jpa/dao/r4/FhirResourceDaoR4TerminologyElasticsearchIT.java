package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestHibernateSearchAddInConfig;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.ValueSet.ConceptReferenceComponent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc.MAKE_LOADING_VERSION_CURRENT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@RequiresDocker
@ContextConfiguration(classes = {TestR4Config.class, TestHibernateSearchAddInConfig.Elasticsearch.class})
public class FhirResourceDaoR4TerminologyElasticsearchIT extends BaseJpaTest {

	public static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";
	public static final String URL_MY_VALUE_SET = "http://example.com/my_value_set";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4TerminologyElasticsearchIT.class);

	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired
	@Qualifier("myCodeSystemDaoR4")
	protected IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> myCodeSystemDao;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	protected ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Autowired
	@Qualifier("myValueSetDaoR4")
	protected IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> myValueSetDao;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	protected ServletRequestDetails mySrd;
	@Autowired
	FhirContext myFhirContext;
	@Autowired
	PlatformTransactionManager myTxManager;
	@Autowired
	private IFhirSystemDao mySystemDao;
	@Autowired
	private IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	private ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private IBulkDataExportJobSchedulingHelper myBulkDataScheduleHelper;


	@BeforeEach
	public void beforeEach() {
		when(mySrd.getUserData().getOrDefault(MAKE_LOADING_VERSION_CURRENT, Boolean.TRUE)).thenReturn(Boolean.TRUE);
	}


	@Test
	public void testExpandWithIncludeContainingDashesInInclude() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setVersion("SYSTEM VERSION");
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalStateException::new);

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept concept;
		concept = new TermConcept(cs, "LA1111-2");
		cs.getConcepts().add(concept);
		concept = new TermConcept(cs, "LA2222-2");
		cs.getConcepts().add(concept);
		concept = new TermConcept(cs, "LA3333-2");
		cs.getConcepts().add(concept);
		concept = new TermConcept(cs, "LA1122-2");
		cs.getConcepts().add(concept);
		concept = new TermConcept(cs, "LA1133-2");
		cs.getConcepts().add(concept);
		concept = new TermConcept(cs, "LA4444-2");
		cs.getConcepts().add(concept);
		concept = new TermConcept(cs, "LA9999-7");
		cs.getConcepts().add(concept);

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), URL_MY_CODE_SYSTEM, "SYSTEM NAME", "SYSTEM VERSION", cs, table);

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(URL_MY_VALUE_SET);
		valueSet.getCompose()
			.addInclude()
			.setSystem(codeSystem.getUrl())
			.addConcept(new ConceptReferenceComponent().setCode("LA2222-2"))
			.addConcept(new ConceptReferenceComponent().setCode("LA1122-2"));
		IIdType vsId = myValueSetDao.create(valueSet, mySrd).getId().toUnqualifiedVersionless();

		ValueSet expansion = myValueSetDao.expand(vsId, null, null);
		Set<String> codes = expansion
			.getExpansion()
			.getContains()
			.stream()
			.map(ValueSet.ValueSetExpansionContainsComponent::getCode)
			.collect(Collectors.toSet());
		ourLog.info("Codes: {}", codes);
		assertThat(codes, containsInAnyOrder("LA2222-2", "LA1122-2"));
	}


	@Override
	protected FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@AfterEach
	public void afterPurgeDatabase() {
		purgeDatabase(myDaoConfig, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataScheduleHelper);
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
