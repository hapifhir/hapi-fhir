package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet.ValidateCodeResult;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport.CodeValidationResult;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.r4.model.codesystems.ConceptSubsumptionOutcome;
import org.hl7.fhir.r4.model.codesystems.HttpVerb;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestPropertySource(properties = {
	"scheduling_disabled=true"
})
public class TerminologySvcImplR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(TerminologySvcImplR4Test.class);
	@Rule
	public final ExpectedException expectedException = ExpectedException.none();
	@Mock
	IValueSetConceptAccumulator myValueSetCodeAccumulator;
	private IIdType myConceptMapId;
	private IIdType myExtensionalCsId;
	private IIdType myExtensionalVsId;
	private Long myExtensionalCsIdOnResourceTable;
	private Long myExtensionalVsIdOnResourceTable;

	@Before
	public void before() {
		myDaoConfig.setAllowExternalReferences(true);
	}

	@After
	public void after() {
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
		myDaoConfig.setPreExpandValueSetsExperimental(new DaoConfig().isPreExpandValueSetsExperimental());
	}

	private IIdType createCodeSystem() {
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

		myTermSvc.storeNewCodeSystemVersion(table.getId(), CS_URL, "SYSTEM NAME", "SYSTEM VERSION", cs);

		return id;
	}

	private void createAndPersistConceptMap(HttpVerb theVerb) {
		ConceptMap conceptMap = createConceptMap();
		conceptMap.setId("ConceptMap/cm");
		persistConceptMap(conceptMap, HttpVerb.POST);
	}

	private void persistConceptMap(ConceptMap theConceptMap, HttpVerb theVerb) {
		switch (theVerb) {
			case POST:
				new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
						myConceptMapId = myConceptMapDao.create(theConceptMap, mySrd).getId().toUnqualifiedVersionless();
					}
				});
				break;
			case PUT:
				new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
						myConceptMapId = myConceptMapDao.update(theConceptMap, mySrd).getId().toUnqualifiedVersionless();
					}
				});
				break;
			default:
				throw new IllegalArgumentException("HTTP verb is not supported: " + theVerb);
		}
	}

	private void loadAndPersistCodeSystemAndValueSet(HttpVerb theVerb) throws IOException {
		loadAndPersistCodeSystem(theVerb);
		loadAndPersistValueSet(theVerb);
	}

	private void loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb theVerb) throws IOException {
		loadAndPersistCodeSystemWithDesignations(theVerb);
		loadAndPersistValueSet(theVerb);
	}

	private void loadAndPersistCodeSystemAndValueSetWithDesignationsAndExclude(HttpVerb theVerb) throws IOException {
		loadAndPersistCodeSystemWithDesignations(theVerb);
		loadAndPersistValueSetWithExclude(theVerb);
	}

	private void loadAndPersistCodeSystem(HttpVerb theVerb) throws IOException {
		CodeSystem codeSystem = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		codeSystem.setId("CodeSystem/cs");
		persistCodeSystem(codeSystem, theVerb);
	}

	private void loadAndPersistCodeSystemWithDesignations(HttpVerb theVerb) throws IOException {
		CodeSystem codeSystem = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs-with-designations.xml");
		codeSystem.setId("CodeSystem/cs");
		persistCodeSystem(codeSystem, theVerb);
	}

	private void persistCodeSystem(CodeSystem theCodeSystem, HttpVerb theVerb) {
		switch (theVerb) {
			case POST:
				new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
						myExtensionalCsId = myCodeSystemDao.create(theCodeSystem, mySrd).getId().toUnqualifiedVersionless();
					}
				});
				break;
			case PUT:
				new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
						myExtensionalCsId = myCodeSystemDao.update(theCodeSystem, mySrd).getId().toUnqualifiedVersionless();
					}
				});
				break;
			default:
				throw new IllegalArgumentException("HTTP verb is not supported: " + theVerb);
		}
		myExtensionalCsIdOnResourceTable = myCodeSystemDao.readEntity(myExtensionalCsId, null).getId();
	}

	private void loadAndPersistValueSet(HttpVerb theVerb) throws IOException {
		ValueSet valueSet = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		valueSet.setId("ValueSet/vs");
		persistValueSet(valueSet, theVerb);
	}

	private void loadAndPersistValueSetWithExclude(HttpVerb theVerb) throws IOException {
		ValueSet valueSet = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs-with-exclude.xml");
		valueSet.setId("ValueSet/vs");
		persistValueSet(valueSet, theVerb);
	}

	private void persistValueSet(ValueSet theValueSet, HttpVerb theVerb) {
		switch (theVerb) {
			case POST:
				new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
						myExtensionalVsId = myValueSetDao.create(theValueSet, mySrd).getId().toUnqualifiedVersionless();
					}
				});
				break;
			case PUT:
				new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
						myExtensionalVsId = myValueSetDao.update(theValueSet, mySrd).getId().toUnqualifiedVersionless();
					}
				});
				break;
			default:
				throw new IllegalArgumentException("HTTP verb is not supported: " + theVerb);
		}
		myExtensionalVsIdOnResourceTable = myValueSetDao.readEntity(myExtensionalVsId, null).getId();
	}

	@Test
	public void testApplyCodeSystemDeltaAdd() {

		// Create not-present
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		myCodeSystemDao.create(cs);

		CodeSystem delta = new CodeSystem();
		delta
			.addConcept()
			.setCode("codeA")
			.setDisplay("displayA");
		delta
			.addConcept()
			.setCode("codeB")
			.setDisplay("displayB");
		myTermSvc.applyDeltaCodesystemsAdd("http://foo", null, delta);

		assertEquals(true, runInTransaction(()->myTermSvc.findCode("http://foo", "codeA").isPresent()));
		assertEquals(false, runInTransaction(()->myTermSvc.findCode("http://foo", "codeZZZ").isPresent()));

	}

	/**
	 * This would be a good check, but there is no easy eay to do it...
	 */
	@Test
	@Ignore
	public void testApplyCodeSystemDeltaAddNotPermittedForNonExternalCodeSystem() {

		// Create not-present
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		myCodeSystemDao.create(cs);

		CodeSystem delta = new CodeSystem();
		delta
			.addConcept()
			.setCode("codeA")
			.setDisplay("displayA");
		try {
			myTermSvc.applyDeltaCodesystemsAdd("http://foo", null, delta);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("", e.getMessage());
		}

	}

	@Test
	public void testApplyCodeSystemDeltaAddWithoutPreExistingCodeSystem() {

		CodeSystem delta = new CodeSystem();
		delta.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		delta.setUrl("http://foo");
		delta.setName("Acme Lab Codes");
		delta
			.addConcept()
			.setCode("CBC")
			.setDisplay("Complete Blood Count");
		delta
			.addConcept()
			.setCode("URNL")
			.setDisplay("Routine Urinalysis");
		myTermSvc.applyDeltaCodesystemsAdd("http://foo", null, delta);

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(CodeSystem.SP_URL, new UriParam("http://foo"));
		IBundleProvider searchResult = myCodeSystemDao.search(params, mySrd);
		assertEquals(1, searchResult.size().intValue());
		CodeSystem outcome = (CodeSystem) searchResult.getResources(0,1).get(0);

		assertEquals("http://foo", outcome.getUrl());
		assertEquals("Acme Lab Codes", outcome.getName());
	}


	@Test
	public void testApplyCodeSystemDeltaAddDuplicatesIgnored() {

		// Add codes
		CodeSystem delta = new CodeSystem();
		delta.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		delta.setUrl("http://foo");
		delta.setName("Acme Lab Codes");
		delta
			.addConcept()
			.setCode("codea")
			.setDisplay("CODEA0");
		delta
			.addConcept()
			.setCode("codeb")
			.setDisplay("CODEB0");
		AtomicInteger outcome = myTermSvc.applyDeltaCodesystemsAdd("http://foo", null, delta);
		assertEquals(2, outcome.get());

		// Add codes again with different display
		delta = new CodeSystem();
		delta.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		delta.setUrl("http://foo");
		delta.setName("Acme Lab Codes");
		delta
			.addConcept()
			.setCode("codea")
			.setDisplay("CODEA1");
		delta
			.addConcept()
			.setCode("codeb")
			.setDisplay("CODEB1");
		outcome = myTermSvc.applyDeltaCodesystemsAdd("http://foo", null, delta);
		assertEquals(2, outcome.get());

		// Add codes again with no changes
		outcome = myTermSvc.applyDeltaCodesystemsAdd("http://foo", null, delta);
		assertEquals(0, outcome.get());
	}


	@Test
	public void testApplyCodeSystemDeltaAddAsChild() {

		// Create not-present
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		myCodeSystemDao.create(cs);

		CodeSystem delta = new CodeSystem();
		delta
			.addConcept()
			.setCode("codeA")
			.setDisplay("displayA");
		delta
			.addConcept()
			.setCode("codeB")
			.setDisplay("displayB");
		myTermSvc.applyDeltaCodesystemsAdd("http://foo", null, delta);

		delta = new CodeSystem();
		CodeSystem.ConceptDefinitionComponent codeAA = delta
			.addConcept()
			.setCode("codeAA")
			.setDisplay("displayAA");
		codeAA
			.addConcept()
			.setCode("codeAAA")
			.setDisplay("displayAAA");
		myTermSvc.applyDeltaCodesystemsAdd("http://foo", "codeA", delta);

		assertEquals(true, runInTransaction(()->myTermSvc.findCode("http://foo", "codeAA").isPresent()));
		assertEquals(ConceptSubsumptionOutcome.SUBSUMEDBY, myTermSvc.subsumes(toString("codeA"), toString("codeAA"), toString("http://foo"), null, null).getOutcome());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMEDBY, myTermSvc.subsumes(toString("codeA"), toString("codeAAA"), toString("http://foo"), null, null).getOutcome());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMEDBY, myTermSvc.subsumes(toString("codeAA"), toString("codeAAA"), toString("http://foo"), null, null).getOutcome());
		assertEquals(ConceptSubsumptionOutcome.NOTSUBSUMED, myTermSvc.subsumes(toString("codeB"), toString("codeAA"), toString("http://foo"), null, null).getOutcome());

		runInTransaction(() -> {
			List<TermConceptParentChildLink> allChildren = myTermConceptParentChildLinkDao.findAll();
			assertEquals(2, allChildren.size());
		});
	}

	@Test
	public void testApplyCodeSystemDeltaAddWithPropertiesAndDesignations() {

		// Create not-present
		CodeSystem cs = new CodeSystem();
		cs.setName("Description of my life");
		cs.setUrl("http://foo");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		cs.setVersion("1.2.3");
		myCodeSystemDao.create(cs);

		CodeSystem delta = new CodeSystem();
		CodeSystem.ConceptDefinitionComponent concept = delta
			.addConcept()
			.setCode("lunch")
			.setDisplay("I'm having dog food");
		concept
			.addDesignation()
			.setLanguage("fr")
			.setUse(new Coding("http://sys", "code", "display"))
			.setValue("Je mange une pomme");
		concept
			.addDesignation()
			.setLanguage("es")
			.setUse(new Coding("http://sys", "code", "display"))
			.setValue("Como una pera");
		concept.addProperty()
			.setCode("flavour")
			.setValue(new StringType("Hints of lime"));
		concept.addProperty()
			.setCode("useless_sct_code")
			.setValue(new Coding("http://snomed.info", "1234567", "Choked on large meal (finding)"));
		myTermSvc.applyDeltaCodesystemsAdd("http://foo", null, delta);

		IContextValidationSupport.LookupCodeResult result = myTermSvc.lookupCode(myFhirCtx, "http://foo", "lunch");
		assertEquals(true, result.isFound());
		assertEquals("lunch", result.getSearchedForCode());
		assertEquals("http://foo", result.getSearchedForSystem());

		Parameters output = (Parameters) result.toParameters(myFhirCtx, null);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		assertEquals("Description of my life", ((StringType) output.getParameter("name")).getValue());
		assertEquals("1.2.3", ((StringType) output.getParameter("version")).getValue());
		assertEquals(false, output.getParameterBool("abstract"));

		List<Parameters.ParametersParameterComponent> designations = output.getParameter().stream().filter(t -> t.getName().equals("designation")).collect(Collectors.toList());
		assertEquals("language", designations.get(0).getPart().get(0).getName());
		assertEquals("fr", ((CodeType) designations.get(0).getPart().get(0).getValue()).getValueAsString());
		assertEquals("use", designations.get(0).getPart().get(1).getName());
		assertEquals("http://sys", ((Coding) designations.get(0).getPart().get(1).getValue()).getSystem());
		assertEquals("code", ((Coding) designations.get(0).getPart().get(1).getValue()).getCode());
		assertEquals("display", ((Coding) designations.get(0).getPart().get(1).getValue()).getDisplay());
		assertEquals("value", designations.get(0).getPart().get(2).getName());
		assertEquals("Je mange une pomme", ((StringType) designations.get(0).getPart().get(2).getValue()).getValueAsString());

		List<Parameters.ParametersParameterComponent> properties = output.getParameter().stream().filter(t -> t.getName().equals("property")).collect(Collectors.toList());
		assertEquals("code", properties.get(0).getPart().get(0).getName());
		assertEquals("flavour", ((CodeType) properties.get(0).getPart().get(0).getValue()).getValueAsString());
		assertEquals("value", properties.get(0).getPart().get(1).getName());
		assertEquals("Hints of lime", ((StringType) properties.get(0).getPart().get(1).getValue()).getValueAsString());

		assertEquals("code", properties.get(1).getPart().get(0).getName());
		assertEquals("useless_sct_code", ((CodeType) properties.get(1).getPart().get(0).getValue()).getValueAsString());
		assertEquals("value", properties.get(1).getPart().get(1).getName());
		assertEquals("http://snomed.info", ((Coding) properties.get(1).getPart().get(1).getValue()).getSystem());
		assertEquals("1234567", ((Coding) properties.get(1).getPart().get(1).getValue()).getCode());
		assertEquals("Choked on large meal (finding)", ((Coding) properties.get(1).getPart().get(1).getValue()).getDisplay());

	}

	@Test
	public void testApplyCodeSystemDeltaRemove() {

		// Create not-present
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		myCodeSystemDao.create(cs);

		CodeSystem delta = new CodeSystem();
		CodeSystem.ConceptDefinitionComponent codeA = delta
			.addConcept()
			.setCode("codeA")
			.setDisplay("displayA");
		delta
			.addConcept()
			.setCode("codeB")
			.setDisplay("displayB");
		CodeSystem.ConceptDefinitionComponent codeAA = codeA
			.addConcept()
			.setCode("codeAA")
			.setDisplay("displayAA");
		codeAA
			.addConcept()
			.setCode("codeAAA")
			.setDisplay("displayAAA");
 		myTermSvc.applyDeltaCodesystemsAdd("http://foo", null, delta);

		// Remove CodeB
		delta = new CodeSystem();
		delta
			.addConcept()
			.setCode("codeB")
			.setDisplay("displayB");
		myTermSvc.applyDeltaCodesystemsRemove("http://foo", delta);

		assertEquals(false, runInTransaction(()->myTermSvc.findCode("http://foo", "codeB").isPresent()));
		assertEquals(true, runInTransaction(()->myTermSvc.findCode("http://foo", "codeA").isPresent()));
		assertEquals(true, runInTransaction(()->myTermSvc.findCode("http://foo", "codeAA").isPresent()));
		assertEquals(true, runInTransaction(()->myTermSvc.findCode("http://foo", "codeAAA").isPresent()));

		// Remove CodeA
		delta = new CodeSystem();
		delta
			.addConcept()
			.setCode("codeA");
		myTermSvc.applyDeltaCodesystemsRemove("http://foo", delta);

		assertEquals(false, runInTransaction(()->myTermSvc.findCode("http://foo", "codeB").isPresent()));
		assertEquals(false, runInTransaction(()->myTermSvc.findCode("http://foo", "codeA").isPresent()));
		assertEquals(false, runInTransaction(()->myTermSvc.findCode("http://foo", "codeAA").isPresent()));
		assertEquals(false, runInTransaction(()->myTermSvc.findCode("http://foo", "codeAAA").isPresent()));

	}


	@Nonnull
	private StringType toString(String theString) {
		return new StringType(theString);
	}


	@Test
	public void testCreateConceptMapWithMissingSourceSystem() {
		ConceptMap conceptMap = new ConceptMap();
		conceptMap.setUrl(CM_URL);
		ConceptMap.ConceptMapGroupComponent group = conceptMap.addGroup()
			.setTarget(CS_URL_2);
		group.addElement()
			.setCode("12345")
			.addTarget()
			.setCode("34567");

		try {
			runInTransaction(() -> {
				myConceptMapDao.create(conceptMap);
			});
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("ConceptMap[url='http://example.com/my_concept_map'] contains at least one group without a value in ConceptMap.group.source", e.getMessage());
		}

	}


	@Test
	public void testCreateConceptMapWithVirtualSourceSystem() {
		ConceptMap conceptMap = createConceptMap();
		conceptMap.getGroup().forEach(t -> t.setSource(null));
		conceptMap.setSource(new CanonicalType("http://hl7.org/fhir/uv/livd/StructureDefinition/loinc-livd"));

		persistConceptMap(conceptMap, HttpVerb.POST);

	}

	@Test
	public void testCreateConceptMapWithVirtualSourceSystemWithClientAssignedId() {
		ConceptMap conceptMap = createConceptMap();
		conceptMap.getGroup().forEach(t -> t.setSource(null));
		conceptMap.setSource(new CanonicalType("http://hl7.org/fhir/uv/livd/StructureDefinition/loinc-livd"));
		conceptMap.setId("ConceptMap/cm");

		persistConceptMap(conceptMap, HttpVerb.PUT);

	}

	@Test
	public void testCreateConceptMapWithMissingTargetSystems() {

		// Missing source
		ConceptMap conceptMap = new ConceptMap();
		conceptMap.setUrl(CM_URL);
		ConceptMap.ConceptMapGroupComponent group = conceptMap.addGroup()
			.setSource(CS_URL);
		group.addElement()
			.setCode("12345")
			.addTarget()
			.setCode("34567");

		try {
			runInTransaction(() -> {
				myConceptMapDao.create(conceptMap);
			});
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("ConceptMap[url='http://example.com/my_concept_map'] contains at least one group without a value in ConceptMap.group.target", e.getMessage());
		}

	}

	@Test
	public void testCreateConceptMapWithMissingUrl() {

		// Missing source
		ConceptMap conceptMap = new ConceptMap();
		ConceptMap.ConceptMapGroupComponent group = conceptMap.addGroup()
			.setTarget(CS_URL_2)
			.setSource(CS_URL);
		group.addElement()
			.setCode("12345")
			.addTarget()
			.setCode("34567");

		try {
			runInTransaction(() -> {
				myConceptMapDao.create(conceptMap);
			});
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("ConceptMap has no value for ConceptMap.url", e.getMessage());
		}

	}

	@Test
	public void testDeleteValueSet() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(valueSet, myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), myDaoConfig.getPreExpandValueSetsDefaultCountExperimental());
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		Long termValueSetId = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable).get().getId();
		assertEquals(3, myTermValueSetConceptDesignationDao.countByTermValueSetId(termValueSetId).intValue());
		assertEquals(24, myTermValueSetConceptDao.countByTermValueSetId(termValueSetId).intValue());

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				myTermValueSetConceptDesignationDao.deleteByTermValueSetId(termValueSetId);
				assertEquals(0, myTermValueSetConceptDesignationDao.countByTermValueSetId(termValueSetId).intValue());
				myTermValueSetConceptDao.deleteByTermValueSetId(termValueSetId);
				assertEquals(0, myTermValueSetConceptDao.countByTermValueSetId(termValueSetId).intValue());
				myTermValueSetDao.deleteByTermValueSetId(termValueSetId);
				assertFalse(myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable).isPresent());
			}
		});
	}

	@Test
	public void testDeleteValueSetWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(valueSet, myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), myDaoConfig.getPreExpandValueSetsDefaultCountExperimental());
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		Long termValueSetId = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable).get().getId();
		assertEquals(3, myTermValueSetConceptDesignationDao.countByTermValueSetId(termValueSetId).intValue());
		assertEquals(24, myTermValueSetConceptDao.countByTermValueSetId(termValueSetId).intValue());

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				myTermValueSetConceptDesignationDao.deleteByTermValueSetId(termValueSetId);
				assertEquals(0, myTermValueSetConceptDesignationDao.countByTermValueSetId(termValueSetId).intValue());
				myTermValueSetConceptDao.deleteByTermValueSetId(termValueSetId);
				assertEquals(0, myTermValueSetConceptDao.countByTermValueSetId(termValueSetId).intValue());
				myTermValueSetDao.deleteByTermValueSetId(termValueSetId);
				assertFalse(myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable).isPresent());
			}
		});
	}

	@Test
	public void testDuplicateCodeSystemUrls() throws Exception {
		loadAndPersistCodeSystem(HttpVerb.POST);

		expectedException.expect(UnprocessableEntityException.class);
		expectedException.expectMessage("Can not create multiple CodeSystem resources with CodeSystem.url \"http://acme.org\", already have one with resource ID: CodeSystem/" + myExtensionalCsId.getIdPart());

		loadAndPersistCodeSystem(HttpVerb.POST);
	}

	@Test
	public void testDuplicateConceptMapUrls() {
		createAndPersistConceptMap(HttpVerb.POST);

		expectedException.expect(UnprocessableEntityException.class);
		expectedException.expectMessage("Can not create multiple ConceptMap resources with ConceptMap.url \"http://example.com/my_concept_map\", already have one with resource ID: ConceptMap/" + myConceptMapId.getIdPart());

		createAndPersistConceptMap(HttpVerb.POST);
	}

	@Test
	public void testDuplicateValueSetUrls() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		// DM 2019-03-05 - We pre-load our custom CodeSystem otherwise pre-expansion of the ValueSet will fail.
		loadAndPersistCodeSystemAndValueSet(HttpVerb.POST);

		expectedException.expect(UnprocessableEntityException.class);
		expectedException.expectMessage("Can not create multiple ValueSet resources with ValueSet.url \"http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2\", already have one with resource ID: ValueSet/" + myExtensionalVsId.getIdPart());

		loadAndPersistValueSet(HttpVerb.POST);
	}

	@Test
	public void testExpandTermValueSetAndChildren() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(valueSet, myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), myDaoConfig.getPreExpandValueSetsDefaultCountExperimental());
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(1000, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8450-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure--expiration", containsComponent.getDisplay());
		assertEquals(2, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk - expiratie", designationComponent.getValue());

		designationComponent = containsComponent.getDesignation().get(1);
		assertEquals("sv", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systoliskt blodtryck - utgång", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(22);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(23);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8492-1", containsComponent.getCode());
		assertEquals("Systolic blood pressure 8 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());
	}

	@Test
	public void testExpandExistingValueSetNotPreExpanded() throws Exception {
		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		myDaoConfig.setPreExpandValueSetsExperimental(true);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		ValueSet expandedValueSet = myTermSvc.expandValueSet(valueSet, myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), myDaoConfig.getPreExpandValueSetsDefaultCountExperimental());
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), expandedValueSet.getExpansion().getOffset());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().size());

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8450-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure--expiration", containsComponent.getDisplay());
		assertEquals(2, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk - expiratie", designationComponent.getValue());

		designationComponent = containsComponent.getDesignation().get(1);
		assertEquals("sv", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systoliskt blodtryck - utgång", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(22);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(23);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8492-1", containsComponent.getCode());
		assertEquals("Systolic blood pressure 8 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		myTermSvc.saveDeferred();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		expandedValueSet = myTermSvc.expandValueSet(valueSet, myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), myDaoConfig.getPreExpandValueSetsDefaultCountExperimental());
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(1000, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getContains().size());

		containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8450-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure--expiration", containsComponent.getDisplay());
		assertEquals(2, containsComponent.getDesignation().size());

		designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk - expiratie", designationComponent.getValue());

		designationComponent = containsComponent.getDesignation().get(1);
		assertEquals("sv", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systoliskt blodtryck - utgång", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(22);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(23);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8492-1", containsComponent.getCode());
		assertEquals("Systolic blood pressure 8 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(valueSet, myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), myDaoConfig.getPreExpandValueSetsDefaultCountExperimental());
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(1000, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8450-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure--expiration", containsComponent.getDisplay());
		assertEquals(2, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk - expiratie", designationComponent.getValue());

		designationComponent = containsComponent.getDesignation().get(1);
		assertEquals("sv", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systoliskt blodtryck - utgång", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(22);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(23);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8492-1", containsComponent.getCode());
		assertEquals("Systolic blood pressure 8 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithCount() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(valueSet, myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), 23);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(23, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(23, expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8450-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure--expiration", containsComponent.getDisplay());
		assertEquals(2, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk - expiratie", designationComponent.getValue());

		designationComponent = containsComponent.getDesignation().get(1);
		assertEquals("sv", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systoliskt blodtryck - utgång", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(22);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithCountWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(valueSet, myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), 23);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(23, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(23, expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8450-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure--expiration", containsComponent.getDisplay());
		assertEquals(2, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk - expiratie", designationComponent.getValue());

		designationComponent = containsComponent.getDesignation().get(1);
		assertEquals("sv", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systoliskt blodtryck - utgång", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(22);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithCountOfZero() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(valueSet, myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), 0);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertFalse(expandedValueSet.getExpansion().hasContains());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithCountOfZeroWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(valueSet, myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), 0);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffsetExperimental(), expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertFalse(expandedValueSet.getExpansion().hasContains());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithOffset() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(valueSet, 1, myDaoConfig.getPreExpandValueSetsDefaultCountExperimental());
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(1, expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(1000, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(codeSystem.getConcept().size() - expandedValueSet.getExpansion().getOffset(), expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent  containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8493-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure 10 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(21);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(22);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8492-1", containsComponent.getCode());
		assertEquals("Systolic blood pressure 8 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithOffsetWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(valueSet, 1, myDaoConfig.getPreExpandValueSetsDefaultCountExperimental());
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(1, expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(1000, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(codeSystem.getConcept().size() - expandedValueSet.getExpansion().getOffset(), expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent  containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8493-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure 10 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(21);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(22);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8492-1", containsComponent.getCode());
		assertEquals("Systolic blood pressure 8 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithOffsetAndCount() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(valueSet, 1, 22);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(1, expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(22, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(22, expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent  containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8493-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure 10 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(21);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithOffsetAndCountWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(valueSet, 1, 22);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(1, expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(22, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(22, expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent  containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8493-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure 10 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(21);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());
	}

	@Test
	public void testExpandValueSetWithValueSetCodeAccumulator() {
		createCodeSystem();

		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);

		myTermSvc.expandValueSet(vs, myValueSetCodeAccumulator);
		verify(myValueSetCodeAccumulator, times(9)).includeConceptWithDesignations(anyString(), anyString(), nullable(String.class), anyCollection());
	}

	@Test
	public void testStoreTermCodeSystemAndChildren() throws Exception {
		loadAndPersistCodeSystemWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				TermCodeSystem codeSystem = myTermCodeSystemDao.findByResourcePid(myExtensionalCsIdOnResourceTable);
				assertEquals("http://acme.org", codeSystem.getCodeSystemUri());
				assertNull(codeSystem.getName());

				TermCodeSystemVersion codeSystemVersion = codeSystem.getCurrentVersion();
				assertEquals(24, codeSystemVersion.getConcepts().size());

				List<TermConcept> concepts = myTermConceptDao.findByCodeSystemVersion(codeSystemVersion);

				TermConcept concept = concepts.get(0);
				assertEquals("8450-9", concept.getCode());
				assertEquals("Systolic blood pressure--expiration", concept.getDisplay());
				assertEquals(2, concept.getDesignations().size());

				List<TermConceptDesignation> designations = Lists.newArrayList(concept.getDesignations().iterator());

				TermConceptDesignation designation = designations.get(0);
				assertEquals("nl", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systolische bloeddruk - expiratie", designation.getValue());

				designation = designations.get(1);
				assertEquals("sv", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systoliskt blodtryck - utgång", designation.getValue());

				concept = concepts.get(1);
				assertEquals("11378-7", concept.getCode());
				assertEquals("Systolic blood pressure at First encounter", concept.getDisplay());
				assertEquals(0, concept.getDesignations().size());

				// ...

				concept = concepts.get(22);
				assertEquals("8491-3", concept.getCode());
				assertEquals("Systolic blood pressure 1 hour minimum", concept.getDisplay());
				assertEquals(1, concept.getDesignations().size());

				designation = concept.getDesignations().iterator().next();
				assertEquals("nl", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systolische bloeddruk minimaal 1 uur", designation.getValue());

				concept = concepts.get(23);
				assertEquals("8492-1", concept.getCode());
				assertEquals("Systolic blood pressure 8 hour minimum", concept.getDisplay());
				assertEquals(0, concept.getDesignations().size());
			}
		});
	}

	@Test
	public void testStoreTermCodeSystemAndChildrenWithClientAssignedId() throws Exception {
		loadAndPersistCodeSystemWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				TermCodeSystem codeSystem = myTermCodeSystemDao.findByResourcePid(myExtensionalCsIdOnResourceTable);
				assertEquals("http://acme.org", codeSystem.getCodeSystemUri());
				assertNull(codeSystem.getName());

				TermCodeSystemVersion codeSystemVersion = codeSystem.getCurrentVersion();
				assertEquals(24, codeSystemVersion.getConcepts().size());

				List<TermConcept> concepts = myTermConceptDao.findByCodeSystemVersion(codeSystemVersion);

				TermConcept concept = concepts.get(0);
				assertEquals("8450-9", concept.getCode());
				assertEquals("Systolic blood pressure--expiration", concept.getDisplay());
				assertEquals(2, concept.getDesignations().size());

				List<TermConceptDesignation> designations = Lists.newArrayList(concept.getDesignations().iterator());

				TermConceptDesignation designation = designations.get(0);
				assertEquals("nl", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systolische bloeddruk - expiratie", designation.getValue());

				designation = designations.get(1);
				assertEquals("sv", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systoliskt blodtryck - utgång", designation.getValue());

				concept = concepts.get(1);
				assertEquals("11378-7", concept.getCode());
				assertEquals("Systolic blood pressure at First encounter", concept.getDisplay());
				assertEquals(0, concept.getDesignations().size());

				// ...

				concept = concepts.get(22);
				assertEquals("8491-3", concept.getCode());
				assertEquals("Systolic blood pressure 1 hour minimum", concept.getDisplay());
				assertEquals(1, concept.getDesignations().size());

				designation = concept.getDesignations().iterator().next();
				assertEquals("nl", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systolische bloeddruk minimaal 1 uur", designation.getValue());

				concept = concepts.get(23);
				assertEquals("8492-1", concept.getCode());
				assertEquals("Systolic blood pressure 8 hour minimum", concept.getDisplay());
				assertEquals(0, concept.getDesignations().size());
			}
		});
	}

	@Test
	public void testStoreTermCodeSystemAndNestedChildren() {
		IIdType codeSystemId = createCodeSystem();
		CodeSystem codeSystemResource = myCodeSystemDao.read(codeSystemId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystemResource));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				ResourceTable resourceTable = (ResourceTable) myCodeSystemDao.readEntity(codeSystemResource.getIdElement(), null);
				Long codeSystemResourcePid = resourceTable.getId();
				TermCodeSystem codeSystem = myTermCodeSystemDao.findByResourcePid(codeSystemResourcePid);
				assertEquals(CS_URL, codeSystem.getCodeSystemUri());
				assertEquals("SYSTEM NAME", codeSystem.getName());

				TermCodeSystemVersion codeSystemVersion = codeSystem.getCurrentVersion();
				assertEquals(9, codeSystemVersion.getConcepts().size());

				List<TermConcept> concepts = myTermConceptDao.findByCodeSystemVersion(codeSystemVersion);

				TermConcept parentWithNoChildrenA = concepts.get(0);
				assertEquals("ParentWithNoChildrenA", parentWithNoChildrenA.getCode());
				assertNull(parentWithNoChildrenA.getDisplay());
				assertEquals(0, parentWithNoChildrenA.getChildren().size());
				assertEquals(0, parentWithNoChildrenA.getParents().size());
				assertEquals(0, parentWithNoChildrenA.getDesignations().size());
				assertEquals(0, parentWithNoChildrenA.getProperties().size());

				TermConcept parentWithNoChildrenB = concepts.get(1);
				assertEquals("ParentWithNoChildrenB", parentWithNoChildrenB.getCode());
				assertNull(parentWithNoChildrenB.getDisplay());
				assertEquals(0, parentWithNoChildrenB.getChildren().size());
				assertEquals(0, parentWithNoChildrenB.getParents().size());
				assertEquals(0, parentWithNoChildrenB.getDesignations().size());
				assertEquals(0, parentWithNoChildrenB.getProperties().size());

				TermConcept parentWithNoChildrenC = concepts.get(2);
				assertEquals("ParentWithNoChildrenC", parentWithNoChildrenC.getCode());
				assertNull(parentWithNoChildrenC.getDisplay());
				assertEquals(0, parentWithNoChildrenC.getChildren().size());
				assertEquals(0, parentWithNoChildrenC.getParents().size());
				assertEquals(0, parentWithNoChildrenC.getDesignations().size());
				assertEquals(0, parentWithNoChildrenC.getProperties().size());

				TermConcept parentA = concepts.get(3);
				assertEquals("ParentA", parentA.getCode());
				assertNull(parentA.getDisplay());
				assertEquals(2, parentA.getChildren().size());
				assertEquals(0, parentA.getParents().size());
				assertEquals(0, parentA.getDesignations().size());
				assertEquals(0, parentA.getProperties().size());

				TermConcept childAA = concepts.get(4);
				assertEquals("childAA", childAA.getCode());
				assertNull(childAA.getDisplay());
				assertEquals(2, childAA.getChildren().size());
				assertEquals(1, childAA.getParents().size());
				assertSame(parentA, childAA.getParents().iterator().next().getParent());
				assertEquals(0, childAA.getDesignations().size());
				assertEquals(0, childAA.getProperties().size());

				TermConcept childAAA = concepts.get(5);
				assertEquals("childAAA", childAAA.getCode());
				assertNull(childAAA.getDisplay());
				assertEquals(0, childAAA.getChildren().size());
				assertEquals(1, childAAA.getParents().size());
				assertSame(childAA, childAAA.getParents().iterator().next().getParent());
				assertEquals(0, childAAA.getDesignations().size());
				assertEquals(2, childAAA.getProperties().size());

				TermConcept childAAB = concepts.get(6);
				assertEquals("childAAB", childAAB.getCode());
				assertNull(childAAB.getDisplay());
				assertEquals(0, childAAB.getChildren().size());
				assertEquals(1, childAAB.getParents().size());
				assertSame(childAA, childAAB.getParents().iterator().next().getParent());
				assertEquals(1, childAAB.getDesignations().size());
				assertEquals(2, childAAB.getProperties().size());

				TermConcept childAB = concepts.get(7);
				assertEquals("childAB", childAB.getCode());
				assertNull(childAB.getDisplay());
				assertEquals(0, childAB.getChildren().size());
				assertEquals(1, childAB.getParents().size());
				assertSame(parentA, childAB.getParents().iterator().next().getParent());
				assertEquals(0, childAB.getDesignations().size());
				assertEquals(0, childAB.getProperties().size());

				TermConcept parentB = concepts.get(8);
				assertEquals("ParentB", parentB.getCode());
				assertNull(parentB.getDisplay());
				assertEquals(0, parentB.getChildren().size());
				assertEquals(0, parentB.getParents().size());
				assertEquals(0, parentB.getDesignations().size());
				assertEquals(0, parentB.getProperties().size());
			}
		});
	}

	@Test
	public void testStoreTermConceptMapAndChildren() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				Optional<TermConceptMap> optionalConceptMap = myTermConceptMapDao.findTermConceptMapByUrl(CM_URL);
				assertTrue(optionalConceptMap.isPresent());

				TermConceptMap conceptMap = optionalConceptMap.get();

				ourLog.info("ConceptMap:\n" + conceptMap.toString());

				assertEquals(VS_URL, conceptMap.getSource());
				assertEquals(VS_URL_2, conceptMap.getTarget());
				assertEquals(CM_URL, conceptMap.getUrl());
				assertEquals(3, conceptMap.getConceptMapGroups().size());

				TermConceptMapGroup group = conceptMap.getConceptMapGroups().get(0);

				ourLog.info("ConceptMap.group(0):\n" + group.toString());

				assertEquals(CS_URL, group.getSource());
				assertEquals("Version 1", group.getSourceVersion());
				assertEquals(VS_URL, group.getSourceValueSet());
				assertEquals(CS_URL_2, group.getTarget());
				assertEquals("Version 2", group.getTargetVersion());
				assertEquals(VS_URL_2, group.getTargetValueSet());
				assertEquals(CM_URL, group.getConceptMapUrl());
				assertEquals(2, group.getConceptMapGroupElements().size());

				TermConceptMapGroupElement element = group.getConceptMapGroupElements().get(0);

				ourLog.info("ConceptMap.group(0).element(0):\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());
				assertEquals(1, element.getConceptMapGroupElementTargets().size());

				TermConceptMapGroupElementTarget target = element.getConceptMapGroupElementTargets().get(0);

				ourLog.info("ConceptMap.group(0).element(0).target(0):\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				element = group.getConceptMapGroupElements().get(1);

				ourLog.info("ConceptMap.group(0).element(1):\n" + element.toString());

				assertEquals("23456", element.getCode());
				assertEquals("Source Code 23456", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				assertEquals(2, element.getConceptMapGroupElementTargets().size());

				target = element.getConceptMapGroupElementTargets().get(0);
				ourLog.info("ConceptMap.group(0).element(1).target(0):\n" + target.toString());
				assertEquals("45678", target.getCode());
				assertEquals("Target Code 45678", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// We had deliberately added a duplicate, and here it is...
				target = element.getConceptMapGroupElementTargets().get(1);
				ourLog.info("ConceptMap.group(0).element(1).target(1):\n" + target.toString());
				assertEquals("45678", target.getCode());
				assertEquals("Target Code 45678", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				group = conceptMap.getConceptMapGroups().get(1);

				ourLog.info("ConceptMap.group(1):\n" + group.toString());

				assertEquals(CS_URL, group.getSource());
				assertEquals("Version 3", group.getSourceVersion());
				assertEquals(CS_URL_3, group.getTarget());
				assertEquals("Version 4", group.getTargetVersion());
				assertEquals(CM_URL, group.getConceptMapUrl());
				assertEquals(1, group.getConceptMapGroupElements().size());

				element = group.getConceptMapGroupElements().get(0);

				ourLog.info("ConceptMap.group(1).element(0):\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 3", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());
				assertEquals(2, element.getConceptMapGroupElementTargets().size());

				target = element.getConceptMapGroupElementTargets().get(0);

				ourLog.info("ConceptMap.group(1).element(0).target(0):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = element.getConceptMapGroupElementTargets().get(1);

				ourLog.info("ConceptMap.group(1).element(0).target(1):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				group = conceptMap.getConceptMapGroups().get(2);

				ourLog.info("ConceptMap.group(2):\n" + group.toString());

				assertEquals(CS_URL_4, group.getSource());
				assertEquals("Version 5", group.getSourceVersion());
				assertEquals(CS_URL_2, group.getTarget());
				assertEquals("Version 2", group.getTargetVersion());
				assertEquals(CM_URL, group.getConceptMapUrl());
				assertEquals(1, group.getConceptMapGroupElements().size());

				element = group.getConceptMapGroupElements().get(0);

				ourLog.info("ConceptMap.group(2).element(0):\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());
				assertEquals(1, element.getConceptMapGroupElementTargets().size());

				target = element.getConceptMapGroupElementTargets().get(0);

				ourLog.info("ConceptMap.group(2).element(0).target(0):\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.NARROWER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testStoreTermConceptMapAndChildrenWithClientAssignedId() {
		createAndPersistConceptMap(HttpVerb.PUT);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				Optional<TermConceptMap> optionalConceptMap = myTermConceptMapDao.findTermConceptMapByUrl(CM_URL);
				assertTrue(optionalConceptMap.isPresent());

				TermConceptMap conceptMap = optionalConceptMap.get();

				ourLog.info("ConceptMap:\n" + conceptMap.toString());

				assertEquals(VS_URL, conceptMap.getSource());
				assertEquals(VS_URL_2, conceptMap.getTarget());
				assertEquals(CM_URL, conceptMap.getUrl());
				assertEquals(3, conceptMap.getConceptMapGroups().size());

				TermConceptMapGroup group = conceptMap.getConceptMapGroups().get(0);

				ourLog.info("ConceptMap.group(0):\n" + group.toString());

				assertEquals(CS_URL, group.getSource());
				assertEquals("Version 1", group.getSourceVersion());
				assertEquals(VS_URL, group.getSourceValueSet());
				assertEquals(CS_URL_2, group.getTarget());
				assertEquals("Version 2", group.getTargetVersion());
				assertEquals(VS_URL_2, group.getTargetValueSet());
				assertEquals(CM_URL, group.getConceptMapUrl());
				assertEquals(2, group.getConceptMapGroupElements().size());

				TermConceptMapGroupElement element = group.getConceptMapGroupElements().get(0);

				ourLog.info("ConceptMap.group(0).element(0):\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());
				assertEquals(1, element.getConceptMapGroupElementTargets().size());

				TermConceptMapGroupElementTarget target = element.getConceptMapGroupElementTargets().get(0);

				ourLog.info("ConceptMap.group(0).element(0).target(0):\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				element = group.getConceptMapGroupElements().get(1);

				ourLog.info("ConceptMap.group(0).element(1):\n" + element.toString());

				assertEquals("23456", element.getCode());
				assertEquals("Source Code 23456", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				assertEquals(2, element.getConceptMapGroupElementTargets().size());

				target = element.getConceptMapGroupElementTargets().get(0);
				ourLog.info("ConceptMap.group(0).element(1).target(0):\n" + target.toString());
				assertEquals("45678", target.getCode());
				assertEquals("Target Code 45678", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// We had deliberately added a duplicate, and here it is...
				target = element.getConceptMapGroupElementTargets().get(1);
				ourLog.info("ConceptMap.group(0).element(1).target(1):\n" + target.toString());
				assertEquals("45678", target.getCode());
				assertEquals("Target Code 45678", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				group = conceptMap.getConceptMapGroups().get(1);

				ourLog.info("ConceptMap.group(1):\n" + group.toString());

				assertEquals(CS_URL, group.getSource());
				assertEquals("Version 3", group.getSourceVersion());
				assertEquals(CS_URL_3, group.getTarget());
				assertEquals("Version 4", group.getTargetVersion());
				assertEquals(CM_URL, group.getConceptMapUrl());
				assertEquals(1, group.getConceptMapGroupElements().size());

				element = group.getConceptMapGroupElements().get(0);

				ourLog.info("ConceptMap.group(1).element(0):\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 3", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());
				assertEquals(2, element.getConceptMapGroupElementTargets().size());

				target = element.getConceptMapGroupElementTargets().get(0);

				ourLog.info("ConceptMap.group(1).element(0).target(0):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = element.getConceptMapGroupElementTargets().get(1);

				ourLog.info("ConceptMap.group(1).element(0).target(1):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				group = conceptMap.getConceptMapGroups().get(2);

				ourLog.info("ConceptMap.group(2):\n" + group.toString());

				assertEquals(CS_URL_4, group.getSource());
				assertEquals("Version 5", group.getSourceVersion());
				assertEquals(CS_URL_2, group.getTarget());
				assertEquals("Version 2", group.getTargetVersion());
				assertEquals(CM_URL, group.getConceptMapUrl());
				assertEquals(1, group.getConceptMapGroupElements().size());

				element = group.getConceptMapGroupElements().get(0);

				ourLog.info("ConceptMap.group(2).element(0):\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());
				assertEquals(1, element.getConceptMapGroupElementTargets().size());

				target = element.getConceptMapGroupElementTargets().get(0);

				ourLog.info("ConceptMap.group(2).element(0).target(0):\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.NARROWER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testStoreTermValueSetAndChildren() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		runInTransaction(()->{
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(0, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, termValueSet.getExpansionStatus());
		});

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		runInTransaction(()->{
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(codeSystem.getConcept().size(), termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());

			TermValueSetConcept concept = termValueSet.getConcepts().get(0);
			ourLog.info("Code:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8450-9", concept.getCode());
			assertEquals("Systolic blood pressure--expiration", concept.getDisplay());
			assertEquals(2, concept.getDesignations().size());

			TermValueSetConceptDesignation designation = concept.getDesignations().get(0);
			assertEquals("nl", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systolische bloeddruk - expiratie", designation.getValue());

			designation = concept.getDesignations().get(1);
			assertEquals("sv", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systoliskt blodtryck - utgång", designation.getValue());

			concept = termValueSet.getConcepts().get(1);
			ourLog.info("Code:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("11378-7", concept.getCode());
			assertEquals("Systolic blood pressure at First encounter", concept.getDisplay());
			assertEquals(0, concept.getDesignations().size());

			// ...

			concept = termValueSet.getConcepts().get(22);
			ourLog.info("Code:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8491-3", concept.getCode());
			assertEquals("Systolic blood pressure 1 hour minimum", concept.getDisplay());
			assertEquals(1, concept.getDesignations().size());

			designation = concept.getDesignations().get(0);
			assertEquals("nl", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systolische bloeddruk minimaal 1 uur", designation.getValue());

			concept = termValueSet.getConcepts().get(23);
			ourLog.info("Code:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8492-1", concept.getCode());
			assertEquals("Systolic blood pressure 8 hour minimum", concept.getDisplay());
			assertEquals(0, concept.getDesignations().size());
		});
	}

	@Test
	public void testStoreTermValueSetAndChildrenWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		runInTransaction(()->{
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(0, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, termValueSet.getExpansionStatus());
		});

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		runInTransaction(()->{
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(codeSystem.getConcept().size(), termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());

			TermValueSetConcept concept = termValueSet.getConcepts().get(0);
			ourLog.info("Code:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8450-9", concept.getCode());
			assertEquals("Systolic blood pressure--expiration", concept.getDisplay());
			assertEquals(2, concept.getDesignations().size());

			TermValueSetConceptDesignation designation = concept.getDesignations().get(0);
			assertEquals("nl", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systolische bloeddruk - expiratie", designation.getValue());

			designation = concept.getDesignations().get(1);
			assertEquals("sv", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systoliskt blodtryck - utgång", designation.getValue());

			concept = termValueSet.getConcepts().get(1);
			ourLog.info("Code:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("11378-7", concept.getCode());
			assertEquals("Systolic blood pressure at First encounter", concept.getDisplay());
			assertEquals(0, concept.getDesignations().size());

			// ...

			concept = termValueSet.getConcepts().get(22);
			ourLog.info("Code:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8491-3", concept.getCode());
			assertEquals("Systolic blood pressure 1 hour minimum", concept.getDisplay());
			assertEquals(1, concept.getDesignations().size());

			designation = concept.getDesignations().get(0);
			assertEquals("nl", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systolische bloeddruk minimaal 1 uur", designation.getValue());

			concept = termValueSet.getConcepts().get(23);
			ourLog.info("Code:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8492-1", concept.getCode());
			assertEquals("Systolic blood pressure 8 hour minimum", concept.getDisplay());
			assertEquals(0, concept.getDesignations().size());
		});
	}

	@Test
	public void testStoreTermValueSetAndChildrenWithExclude() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignationsAndExclude(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		runInTransaction(()->{
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(0, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, termValueSet.getExpansionStatus());
		});

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		runInTransaction(()->{
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(codeSystem.getConcept().size() - 2, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());

			TermValueSetConcept concept = termValueSet.getConcepts().get(0);
			ourLog.info("Code:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8450-9", concept.getCode());
			assertEquals("Systolic blood pressure--expiration", concept.getDisplay());
			assertEquals(2, concept.getDesignations().size());

			TermValueSetConceptDesignation designation = concept.getDesignations().get(0);
			assertEquals("nl", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systolische bloeddruk - expiratie", designation.getValue());

			designation = concept.getDesignations().get(1);
			assertEquals("sv", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systoliskt blodtryck - utgång", designation.getValue());

			concept = termValueSet.getConcepts().get(1);
			ourLog.info("Code:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("11378-7", concept.getCode());
			assertEquals("Systolic blood pressure at First encounter", concept.getDisplay());
			assertEquals(0, concept.getDesignations().size());

			// ...

			concept = termValueSet.getConcepts().get(22 - 2);
			ourLog.info("Code:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8491-3", concept.getCode());
			assertEquals("Systolic blood pressure 1 hour minimum", concept.getDisplay());
			assertEquals(1, concept.getDesignations().size());

			designation = concept.getDesignations().get(0);
			assertEquals("nl", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systolische bloeddruk minimaal 1 uur", designation.getValue());

			concept = termValueSet.getConcepts().get(23 - 2);
			ourLog.info("Code:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8492-1", concept.getCode());
			assertEquals("Systolic blood pressure 8 hour minimum", concept.getDisplay());
			assertEquals(0, concept.getDesignations().size());
		});
	}

	@Test
	public void testStoreTermValueSetAndChildrenWithExcludeWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignationsAndExclude(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		runInTransaction(()->{
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(0, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, termValueSet.getExpansionStatus());
		});

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		runInTransaction(()->{
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(codeSystem.getConcept().size() - 2, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());

			TermValueSetConcept concept = termValueSet.getConcepts().get(0);
			ourLog.info("Code:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8450-9", concept.getCode());
			assertEquals("Systolic blood pressure--expiration", concept.getDisplay());
			assertEquals(2, concept.getDesignations().size());

			TermValueSetConceptDesignation designation = concept.getDesignations().get(0);
			assertEquals("nl", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systolische bloeddruk - expiratie", designation.getValue());

			designation = concept.getDesignations().get(1);
			assertEquals("sv", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systoliskt blodtryck - utgång", designation.getValue());

			concept = termValueSet.getConcepts().get(1);
			ourLog.info("Code:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("11378-7", concept.getCode());
			assertEquals("Systolic blood pressure at First encounter", concept.getDisplay());
			assertEquals(0, concept.getDesignations().size());

			// ...

			concept = termValueSet.getConcepts().get(22 - 2);
			ourLog.info("Code:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8491-3", concept.getCode());
			assertEquals("Systolic blood pressure 1 hour minimum", concept.getDisplay());
			assertEquals(1, concept.getDesignations().size());

			designation = concept.getDesignations().get(0);
			assertEquals("nl", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systolische bloeddruk minimaal 1 uur", designation.getValue());

			concept = termValueSet.getConcepts().get(23 - 2);
			ourLog.info("Code:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8492-1", concept.getCode());
			assertEquals("Systolic blood pressure 8 hour minimum", concept.getDisplay());
			assertEquals(0, concept.getDesignations().size());
		});
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeOneToMany() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");
				translationRequest.setTargetSystem(new UriType(CS_URL_3));

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(2, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target(0):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(1);

				ourLog.info("target(1):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(2, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeOneToOne() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");
				translationRequest.setTargetSystem(new UriType(CS_URL_2));

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(1, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("ConceptMap.group.element.target:\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				TermConceptMapGroupElement element = target.getConceptMapGroupElement();

				ourLog.info("ConceptMap.group.element:\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				TermConceptMapGroup group = element.getConceptMapGroup();

				ourLog.info("ConceptMap.group:\n" + group.toString());

				assertEquals(CS_URL, group.getSource());
				assertEquals("Version 1", group.getSourceVersion());
				assertEquals(VS_URL, group.getSourceValueSet());
				assertEquals(CS_URL_2, group.getTarget());
				assertEquals("Version 2", group.getTargetVersion());
				assertEquals(VS_URL_2, group.getTargetValueSet());
				assertEquals(CM_URL, group.getConceptMapUrl());

				TermConceptMap conceptMap = group.getConceptMap();

				ourLog.info("ConceptMap:\n" + conceptMap.toString());

				assertNotNull(conceptMap);
				assertEquals(VS_URL, conceptMap.getSource());
				assertEquals(VS_URL_2, conceptMap.getTarget());
				assertEquals(CM_URL, conceptMap.getUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(1, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeUnmapped() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("BOGUS");
				translationRequest.setTargetSystem(new UriType(CS_URL_3));

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertTrue(targets.isEmpty());
			}
		});
	}

	@Test
	public void testTranslateConceptMapWithNoSourceAndTargetValueSet() {

		ConceptMap conceptMap = new ConceptMap();
		conceptMap.setUrl(CM_URL);
		conceptMap.setSource(null);
		conceptMap.setTarget(null);
		ConceptMap.ConceptMapGroupComponent group = conceptMap.addGroup()
			.setSource(CS_URL)
			.setTarget(CS_URL_2);
		group.addElement()
			.setCode("12345")
			.addTarget()
			.setCode("34567");
		group.addElement()
			.setCode("888")
			.addTarget()
			.setCode("999");

		myConceptMapDao.create(conceptMap);

		TranslationRequest translationRequest = new TranslationRequest()
			.addCode(CS_URL, "12345")
			.setTargetSystem(new UriType(CS_URL_2));

		List<TermConceptMapGroupElementTarget> resp = myTermSvc.translate(translationRequest);
		assertEquals(1, resp.size());
		assertEquals("34567", resp.get(0).getCode());
	}

	@Test
	public void testTranslateUsingPredicatesWithCodeOnly() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setCode("12345");

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(3, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target(0):\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(1);

				ourLog.info("target(1):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(2);

				ourLog.info("target(2):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(3, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceAndTargetSystem2() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   target code system #2
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");
				translationRequest.setTargetSystem(new UriType(CS_URL_2));

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(1, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target:\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(1, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceAndTargetSystem3() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   target code system #3
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");
				translationRequest.setTargetSystem(new UriType(CS_URL_3));

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(2, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target(0):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(1);

				ourLog.info("target(1):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(2, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystem() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(3, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target(0):\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(1);

				ourLog.info("target(1):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(2);

				ourLog.info("target(2):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(3, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystemAndVersion1() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   source code system version #1
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345")
					.setVersion("Version 1");

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(1, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target:\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(1, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystemAndVersion3() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   source code system version #3
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345")
					.setVersion("Version 3");

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(2, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target(0):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(1);

				ourLog.info("target(1):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(2, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceValueSet() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source value set
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setCode("12345");
				translationRequest.setSource(new UriType(VS_URL));

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(3, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target(0):\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(1);

				ourLog.info("target(1):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(2);

				ourLog.info("target(2):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(3, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithTargetValueSet() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   target value set
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setCode("12345");
				translationRequest.setTarget(new UriType(VS_URL_2));

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(3, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target(0):\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(1);

				ourLog.info("target(1):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(2);

				ourLog.info("target(2):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(3, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateWithReverse() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   target code system
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL_2)
					.setCode("34567");
				translationRequest.setTargetSystem(new UriType(CS_URL_4));
				translationRequest.setReverse(true);

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(1, elements.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());

				TermConceptMapGroupElement element = elements.get(0);

				ourLog.info("element:\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				// Test caching.
				elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(1, elements.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());
			}
		});
	}

	@Test
	public void testTranslateWithReverseByCodeSystemsAndSourceCodeUnmapped() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL_3)
					.setCode("BOGUS");
				translationRequest.setTargetSystem(new UriType(CS_URL));

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertTrue(elements.isEmpty());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithCodeOnly() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setCode("34567");
				translationRequest.setReverse(true);

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());

				TermConceptMapGroupElement element = elements.get(0);

				ourLog.info("element:\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				element = elements.get(1);

				ourLog.info("element:\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				// Test caching.
				elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceAndTargetSystem1() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   target code system #1
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL_2)
					.setCode("34567");
				translationRequest.setTargetSystem(new UriType(CS_URL));
				translationRequest.setReverse(true);

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(1, elements.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());

				TermConceptMapGroupElement element = elements.get(0);

				ourLog.info("element:\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				// Test caching.
				elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(1, elements.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceAndTargetSystem4() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   target code system #4
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL_2)
					.setCode("34567");
				translationRequest.setTargetSystem(new UriType(CS_URL_4));
				translationRequest.setReverse(true);

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(1, elements.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());

				TermConceptMapGroupElement element = elements.get(0);

				ourLog.info("element:\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				// Test caching.
				elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(1, elements.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceSystem() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL_2)
					.setCode("34567");
				translationRequest.setReverse(true);

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());

				TermConceptMapGroupElement element = elements.get(0);

				ourLog.info("element:\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				element = elements.get(1);

				ourLog.info("element:\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				// Test caching.
				elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceSystemAndVersion() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   source code system version
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL_2)
					.setCode("34567")
					.setVersion("Version 2");
				translationRequest.setReverse(true);

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());

				TermConceptMapGroupElement element = elements.get(0);

				ourLog.info("element:\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				element = elements.get(1);

				ourLog.info("element:\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				// Test caching.
				elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceValueSet() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source value set
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setCode("34567");
				translationRequest.setSource(new UriType(VS_URL_2));
				translationRequest.setReverse(true);

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());

				TermConceptMapGroupElement element = elements.get(0);

				ourLog.info("element:\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				element = elements.get(1);

				ourLog.info("element:\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				// Test caching.
				elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithTargetValueSet() {
		createAndPersistConceptMap(HttpVerb.POST);
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   target value set
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setCode("34567");
				translationRequest.setTarget(new UriType(VS_URL));
				translationRequest.setReverse(true);

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());

				TermConceptMapGroupElement element = elements.get(0);

				ourLog.info("element:\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				element = elements.get(1);

				ourLog.info("element:\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				// Test caching.
				elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());
			}
		});
	}

	@Test
	public void testValidateCode() {
		createCodeSystem();

		CodeValidationResult validation = myTermSvc.validateCode(myFhirCtx, CS_URL, "ParentWithNoChildrenA", null);
		assertEquals(true, validation.isOk());

		validation = myTermSvc.validateCode(myFhirCtx, CS_URL, "ZZZZZZZ", null);
		assertEquals(false, validation.isOk());
	}

	@Test
	public void testValidateCodeIsInPreExpandedValueSet() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValidateCodeResult result = myTermSvc.validateCodeIsInPreExpandedValueSet(valueSet, null, null, null, null, null);
		assertNull(result);

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valueSet, null, "BOGUS", null, null, null);
		assertNull(result);

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valueSet, null, "11378-7", null, null, null);
		assertTrue(result.isResult());
		assertEquals("Validation succeeded", result.getMessage());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valueSet, null, "11378-7", "Systolic blood pressure at First encounter", null, null);
		assertTrue(result.isResult());
		assertEquals("Validation succeeded", result.getMessage());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valueSet, "http://acme.org", "11378-7", null, null, null);
		assertTrue(result.isResult());
		assertEquals("Validation succeeded", result.getMessage());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		Coding coding = new Coding("http://acme.org", "11378-7", "Systolic blood pressure at First encounter");
		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valueSet, null, null, null, coding, null);
		assertTrue(result.isResult());
		assertEquals("Validation succeeded", result.getMessage());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding(new Coding("BOGUS", "BOGUS", "BOGUS"));
		codeableConcept.addCoding(coding);
		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valueSet, null, null, null, null, codeableConcept);
		assertTrue(result.isResult());
		assertEquals("Validation succeeded", result.getMessage());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeIsInPreExpandedValueSetWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValidateCodeResult result = myTermSvc.validateCodeIsInPreExpandedValueSet(valueSet, null, null, null, null, null);
		assertNull(result);

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valueSet, null, "BOGUS", null, null, null);
		assertNull(result);

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valueSet, null, "11378-7", null, null, null);
		assertTrue(result.isResult());
		assertEquals("Validation succeeded", result.getMessage());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valueSet, null, "11378-7", "Systolic blood pressure at First encounter", null, null);
		assertTrue(result.isResult());
		assertEquals("Validation succeeded", result.getMessage());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valueSet, "http://acme.org", "11378-7", null, null, null);
		assertTrue(result.isResult());
		assertEquals("Validation succeeded", result.getMessage());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		Coding coding = new Coding("http://acme.org", "11378-7", "Systolic blood pressure at First encounter");
		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valueSet, null, null, null, coding, null);
		assertTrue(result.isResult());
		assertEquals("Validation succeeded", result.getMessage());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding(new Coding("BOGUS", "BOGUS", "BOGUS"));
		codeableConcept.addCoding(coding);
		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valueSet, null, null, null, null, codeableConcept);
		assertTrue(result.isResult());
		assertEquals("Validation succeeded", result.getMessage());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
}
