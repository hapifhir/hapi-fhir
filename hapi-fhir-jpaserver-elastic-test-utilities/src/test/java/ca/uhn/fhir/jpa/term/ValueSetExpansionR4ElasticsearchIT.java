package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.config.TestR4ConfigWithElasticHSearch;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.Date;

import static ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc.MAKE_LOADING_VERSION_CURRENT;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_ALL_VALUESET_ID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestR4ConfigWithElasticHSearch.class)
@RequiresDocker
public class ValueSetExpansionR4ElasticsearchIT extends BaseJpaTest implements IValueSetExpansionIT {

	protected static final String CS_URL = "http://example.com/my_code_system";
	@Autowired
	@Qualifier("myCodeSystemDaoR4")
	protected IFhirResourceDaoCodeSystem<CodeSystem> myCodeSystemDao;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	protected ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Autowired
	@Qualifier("myValueSetDaoR4")
	protected IFhirResourceDaoValueSet<ValueSet> myValueSetDao;
	@Autowired
	protected ITermReadSvc myTermSvc;
	@Autowired
	protected ITermDeferredStorageSvc myTerminologyDeferredStorageSvc;
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
	private IBulkDataExportJobSchedulingHelper myBulkDataExportJobSchedulingHelper;
	@Mock
	private IValueSetConceptAccumulator myValueSetCodeAccumulator;

	@BeforeEach
	public void beforeEach() {
		when(mySrd.getUserData().getOrDefault(MAKE_LOADING_VERSION_CURRENT, Boolean.TRUE)).thenReturn(Boolean.TRUE);
	}


	@AfterEach
	public void after() {
		myStorageSettings.setMaximumExpansionSize(JpaStorageSettings.DEFAULT_MAX_EXPANSION_SIZE);
	}

	@AfterEach
	public void afterPurgeDatabase() {
		purgeDatabase(myStorageSettings, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataExportJobSchedulingHelper);
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	public ITermDeferredStorageSvc getTerminologyDefferedStorageService() {
		return myTerminologyDeferredStorageSvc;
	}

	@Override
	public ITermReadSvc getTerminologyReadSvc() {
		return myTermSvc;
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}

	@Override
	public IFhirResourceDaoValueSet<ValueSet> getValueSetDao() {
		return myValueSetDao;
	}

	@Override
	public JpaStorageSettings getJpaStorageSettings() {
		return myStorageSettings;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	void createCodeSystem() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		codeSystem.setName("SYSTEM NAME");
		codeSystem.setVersion("SYSTEM VERSION");
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalArgumentException::new);

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept parent;
		parent = new TermConcept(cs, "ParentWithNoChildrenA");
		cs.getConcepts().add(parent);
		parent = new TermConcept(cs, "ParentWithNoChildrenB");
		cs.getConcepts().add(parent);
		parent = new TermConcept(cs, "ParentWithNoChildrenC");
		cs.getConcepts().add(parent);

		TermConcept parentA = new TermConcept(cs, "ParentA");
		cs.getConcepts().add(parentA);

		TermConcept childAA = new TermConcept(cs, "childAA");
		parentA.addChild(childAA, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		TermConcept childAAA = new TermConcept(cs, "childAAA");
		childAAA.addPropertyString("propA", "valueAAA");
		childAAA.addPropertyString("propB", "foo");
		childAA.addChild(childAAA, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		TermConcept childAAB = new TermConcept(cs, "childAAB");
		childAAB.addPropertyString("propA", "valueAAB");
		childAAB.addPropertyString("propB", "foo");
		childAAB.addDesignation()
			.setUseSystem("D1S")
			.setUseCode("D1C")
			.setUseDisplay("D1D")
			.setValue("D1V");
		childAA.addChild(childAAB, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		TermConcept childAB = new TermConcept(cs, "childAB");
		parentA.addChild(childAB, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		TermConcept parentB = new TermConcept(cs, "ParentB");
		cs.getConcepts().add(parentB);

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(JpaPid.fromId(table.getId()), CS_URL, "SYSTEM NAME", "SYSTEM VERSION", cs, table);

	}

	@Test
	public void testExpandValueSetInMemoryRespectsMaxSize() {
		createCodeSystem();

		// Add lots more codes
		CustomTerminologySet additions = new CustomTerminologySet();
		for (int i = 0; i < 100; i++) {
			additions.addRootConcept("CODE" + i, "Display " + i);
		}
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd(CS_URL, additions);


		// Codes available exceeds the max
		myStorageSettings.setMaximumExpansionSize(50);
		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		try {
			myTermSvc.expandValueSet(null, vs);
			fail("");		} catch (InternalErrorException e) {
			assertThat(e.getMessage()).contains(Msg.code(832) + "Expansion of ValueSet produced too many codes (maximum 50) - Operation aborted!");
		}

		// Increase the max so it won't exceed
		myStorageSettings.setMaximumExpansionSize(150);
		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		ValueSet outcome = myTermSvc.expandValueSet(null, vs);
		assertThat(outcome.getExpansion().getContains()).hasSize(109);

	}

	@Test
	public void testExpandValueSetWithValueSetCodeAccumulator() {
		createCodeSystem();

		when(myValueSetCodeAccumulator.getCapacityRemaining()).thenReturn(100);

		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);

		myTermSvc.expandValueSet(null, vs, myValueSetCodeAccumulator);
		verify(myValueSetCodeAccumulator, times(9)).includeConceptWithDesignations(anyString(), anyString(), nullable(String.class), anyCollection(), nullable(Long.class), nullable(String.class), nullable(String.class));
	}

	/**
	 * Reproduced: https://github.com/hapifhir/hapi-fhir/issues/3419
	 */
	@Test
	public void testExpandValueSetLargerThanElasticDefaultScrollSize() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		codeSystem.setName("SYSTEM NAME");
		codeSystem.setVersion("SYSTEM VERSION");
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();
		ResourceTable csResource = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalArgumentException::new);

		TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		codeSystemVersion.setResource(csResource);

		// need to be more than elastic [index.max_result_window] index level setting (default = 10_000)
		addTermConcepts(codeSystemVersion, 11_000);

		ValueSet valueSet = getValueSetWithAllCodeSystemConcepts(codeSystemVersion.getCodeSystemVersionId());

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(codeSystem, codeSystemVersion,
			new SystemRequestDetails(), Collections.singletonList(valueSet), Collections.emptyList());

		myTerminologyDeferredStorageSvc.saveAllDeferred();
		await().atMost(10, SECONDS).until(() -> myTerminologyDeferredStorageSvc.isStorageQueueEmpty(true));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		// exception is swallowed in pre-expansion process, so let's check the ValueSet was successfully expanded
		Slice<TermValueSet> page = runInTransaction(() ->
			myTermValueSetDao.findByExpansionStatus(PageRequest.of(0, 1), TermValueSetPreExpansionStatusEnum.EXPANDED));
		assertThat(page.getContent()).hasSize(1);
	}


	private ValueSet getValueSetWithAllCodeSystemConcepts(String theCodeSystemVersionId) {
		ValueSet vs = new ValueSet();
		vs.setId(LOINC_ALL_VALUESET_ID);
		vs.setUrl(CS_URL + "/vs");
		vs.setVersion(theCodeSystemVersionId);
		vs.setName("All LOINC codes");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.setDate(new Date());
		vs.setDescription("A value set that includes all LOINC codes");
		vs.getCompose().addInclude().setSystem(CS_URL).setVersion(theCodeSystemVersionId);
		return vs;
	}


	private void addTermConcepts(TermCodeSystemVersion theCs, int theTermConceptQty) {
		for (int i = 0; i < theTermConceptQty; i++) {
			TermConcept tc = new TermConcept(theCs, String.format("code-%05d", i));
			theCs.getConcepts().add(tc);
		}
	}

	/**
	 * Reproduced: https://github.com/hapifhir/hapi-fhir/issues/3992
	 */
	@Test
	public void testExpandValueSetWithMoreThanElasticDefaultNestedObjectCount() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		codeSystem.setName("SYSTEM NAME");
		codeSystem.setVersion("SYSTEM VERSION");
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();
		ResourceTable csResource = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalArgumentException::new);

		TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		codeSystemVersion.setResource(csResource);

		TermConcept tc = new TermConcept(codeSystemVersion, "test-code-1");

//		 need to be more than elastic [index.mapping.nested_objects.limit] index level setting (default = 10_000)
//		 however, the new mapping (not nested, multivalued) groups properties by key, and there can't be more than 1000
//		 properties (keys), so we test here with a number of properties > 10_000 but grouped in max 200 property keys
//		 (current loinc version (2.73) has 82 property keys maximum in a TermConcept)
		addTermConceptProperties(tc, 10_100, 200);

		codeSystemVersion.getConcepts().add(tc);

		ValueSet valueSet = new ValueSet();
		valueSet.setId(LOINC_ALL_VALUESET_ID);
		valueSet.setUrl(CS_URL + "/vs");
		valueSet.setVersion(codeSystemVersion.getCodeSystemVersionId());
		valueSet.setName("All LOINC codes");
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.setDate(new Date());
		valueSet.setDescription("A value set that includes all LOINC codes");
		valueSet.getCompose().addInclude().setSystem(CS_URL).setVersion(codeSystemVersion.getCodeSystemVersionId());

		assertDoesNotThrow(() -> myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(codeSystem, codeSystemVersion,
			new SystemRequestDetails(), Collections.singletonList(valueSet), Collections.emptyList()));

	}

	private void addTermConceptProperties(TermConcept theTermConcept, int thePropertiesCount, int thePropertyKeysCount) {
		int valuesPerPropKey = thePropertiesCount / thePropertyKeysCount;

		int propsCreated = 0;
		while (propsCreated < thePropertiesCount) {

			int propKeysCreated = 0;
			while (propKeysCreated < thePropertyKeysCount && propsCreated < thePropertiesCount) {
				String propKey = String.format("%05d", propKeysCreated);
				String propSeq = String.format("%05d", propsCreated);
				theTermConcept.addPropertyString("prop-key-" + propKey, "value-" + propSeq);

				propKeysCreated++;
				propsCreated++;
			}
		}
	}
}
