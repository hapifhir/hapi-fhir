package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.config.TestR4ConfigWithElasticSearch;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR4;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestR4ConfigWithElasticSearch.class})
public class ValueSetExpansionR4ElasticsearchIT extends BaseJpaTest {

	protected static final String CS_URL = "http://example.com/my_code_system";
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
	@Autowired
	protected ITermReadSvcR4 myTermSvc;
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
	private IBulkDataExportSvc myBulkDataExportSvc;
	@Mock
	private IValueSetConceptAccumulator myValueSetCodeAccumulator;

	@AfterEach
	public void after() {
		myDaoConfig.setMaximumExpansionSize(DaoConfig.DEFAULT_MAX_EXPANSION_SIZE);
	}

	@AfterEach
	public void afterPurgeDatabase() {
		purgeDatabase(myDaoConfig, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataExportSvc);
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

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), CS_URL, "SYSTEM NAME", "SYSTEM VERSION", cs, table);

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
		myDaoConfig.setMaximumExpansionSize(50);
		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		try {
			myTermSvc.expandValueSet(null, vs);
			fail();
		} catch (InternalErrorException e) {
			assertEquals("Expansion of ValueSet produced too many codes (maximum 50) - Operation aborted!", e.getMessage());
		}

		// Increase the max so it won't exceed
		myDaoConfig.setMaximumExpansionSize(150);
		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		ValueSet outcome = myTermSvc.expandValueSet(null, vs);
		assertEquals(109, outcome.getExpansion().getContains().size());

	}

	@Test
	public void testExpandValueSetWithValueSetCodeAccumulator() {
		createCodeSystem();

		when(myValueSetCodeAccumulator.getCapacityRemaining()).thenReturn(100);

		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);

		myTermSvc.expandValueSet(null, vs, myValueSetCodeAccumulator);
		verify(myValueSetCodeAccumulator, times(9)).includeConceptWithDesignations(anyString(), anyString(), nullable(String.class), anyCollection());
	}


	@Override
	protected FhirContext getContext() {
		return myFhirContext;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}
}
