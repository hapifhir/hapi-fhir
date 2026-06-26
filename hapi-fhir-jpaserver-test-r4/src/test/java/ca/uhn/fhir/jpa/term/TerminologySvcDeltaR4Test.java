package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.AopTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TerminologySvcDeltaR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(TerminologySvcDeltaR4Test.class);

	@AfterEach
	public void after() {
		myStorageSettings.setDeferIndexingForCodesystemsOfSize(new JpaStorageSettings().getDeferIndexingForCodesystemsOfSize());
		TermDeferredStorageSvcImpl termDeferredStorageSvc = AopTestUtils.getTargetObject(myTermDeferredStorageSvc);
		termDeferredStorageSvc.clearDeferred();
	}
	
	@Test
	public void testAddRootConcepts() {
		createNotPresentCodeSystem();
		ValueSet vs;
		vs = expandNotPresentCodeSystem();
		assertThat(vs.getExpansion().getContains()).isEmpty();

		CodeSystem delta = newDeltaCodeSystem();
		delta.addConcept().setCode("RootA").setDisplay("Root A");
		delta.addConcept().setCode("RootB").setDisplay("Root B");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);
		assertHierarchyContainsExactly(
			"RootA seq=0",
			"RootB seq=0"
		);

		delta = newDeltaCodeSystem();
		delta.addConcept().setCode("RootC").setDisplay("Root C");
		delta.addConcept().setCode("RootD").setDisplay("Root D");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);
		assertHierarchyContainsExactly(
			"RootA seq=0",
			"RootB seq=0",
			"RootC seq=0",
			"RootD seq=0"
		);
	}

	@Nonnull
	public static CodeSystem newDeltaCodeSystem() {
		CodeSystem additions = new CodeSystem();
		additions.setUrl("http://foo/cs");
		additions.setVersion("1.0");
		additions.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		return additions;
	}

	@Test
	public void testAddRootConceptsWithCycle() {
		createNotPresentCodeSystem();
		ValueSet vs;
		vs = expandNotPresentCodeSystem();
		assertThat(vs.getExpansion().getContains()).isEmpty();

		CodeSystem delta = newDeltaCodeSystem();

		CodeSystem.ConceptDefinitionComponent root = delta.addConcept().setCode("Root").setDisplay("Root");
		CodeSystem.ConceptDefinitionComponent child = root.addConcept().setCode("Child").setDisplay("Child");
		child.addConcept(root);

		try {
			myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(926) + "Cycle detected around code Root", e.getMessage());
		}

	}

	@Test
	public void testAddHierarchyConcepts() {
		ourLog.info("Starting testAddHierarchyConcepts");

		createNotPresentCodeSystem();
		assertHierarchyContainsExactly();

		ourLog.info("Have created code system");
		runInTransaction(() -> {
			ourLog.info("All code systems: {}", myTermCodeSystemDao.findAll());
			ourLog.info("All code system versions: {}", myTermCodeSystemVersionDao.findAll());
			ourLog.info("All concepts: {}", myTermConceptDao.findAll());
		});

		CodeSystem delta = newDeltaCodeSystem();
		delta.addConcept().setCode("RootA").setDisplay("Root A");
		delta.addConcept().setCode("RootB").setDisplay("Root B");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);
		assertHierarchyContainsExactly(
			"RootA seq=0",
			"RootB seq=0"
		);

		ourLog.info("Have performed add");
		runInTransaction(() -> {
			ourLog.info("All code systems: {}", myTermCodeSystemDao.findAll());
			ourLog.info("All code system versions: {}", myTermCodeSystemVersionDao.findAll());
			ourLog.info("All concepts: {}", myTermConceptDao.findAll());
		});

		myCaptureQueriesListener.clear();

		delta = newDeltaCodeSystem();
		CodeSystem.ConceptDefinitionComponent root = delta.addConcept().setCode("RootA").setDisplay("Root A");
		root.addConcept().setCode("ChildAA").setDisplay("Child AA");
		root.addConcept().setCode("ChildAB").setDisplay("Child AB");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);

		myCaptureQueriesListener.logAllQueriesForCurrentThread();

		assertHierarchyContainsExactly(
			"RootA seq=0",
			" ChildAA seq=0",
			" ChildAB seq=1",
			"RootB seq=0"
		);

	}

	@Test
	public void testAddMoveConceptFromOneParentToAnother() {
		createNotPresentCodeSystem();
		assertHierarchyContainsExactly();

		UploadStatistics outcome;
		CodeSystem delta;

		delta = newDeltaCodeSystem();
		delta.addConcept().setCode("RootA").setDisplay("Root A")
			.addConcept().setCode("ChildAA").setDisplay("Child AA")
			.addConcept().setCode("ChildAAA").setDisplay("Child AAA");
		delta.addConcept().setCode("RootB").setDisplay("Root B");
		outcome = myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);
		assertHierarchyContainsExactly(
			"RootA seq=0",
			" ChildAA seq=0",
			"  ChildAAA seq=0",
			"RootB seq=0"
		);
		assertEquals(4, outcome.getAddedConceptCount());

		delta = newDeltaCodeSystem();
		delta.addConcept().setCode("RootB").setDisplay("Root B")
			.addConcept().setCode("ChildAA").setDisplay("Child AA");
		outcome = myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);
		assertHierarchyContainsExactly(
			"RootA seq=0",
			" ChildAA seq=0",
			"  ChildAAA seq=0",
			"RootB seq=0",
			" ChildAA seq=0",
			"  ChildAAA seq=0"
		);
		assertEquals(1, outcome.getAddedConceptLinkCount());

		runInTransaction(() -> {
			TermConcept concept = myTermSvc.findCode("http://foo/cs", "ChildAA").orElseThrow(() -> new IllegalStateException());
			assertEquals(2, concept.getParents().size());
			assertThat(concept.getParentPidsAsString()).matches("^[0-9]+ [0-9]+$");
		});

	}

	@Test
	public void testReAddingConceptsDoesntRecreateExistingLinks() {
		createNotPresentCodeSystem();
		assertHierarchyContainsExactly();

		UploadStatistics outcome;
		CodeSystem delta;

		myCaptureQueriesListener.clear();

		delta = newDeltaCodeSystem();
		delta.addConcept().setCode("RootA").setDisplay("Root A")
			.addConcept().setCode("ChildAA").setDisplay("Child AA");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);
		assertHierarchyContainsExactly(
			"RootA seq=0",
			" ChildAA seq=0"
		);

		myCaptureQueriesListener.logDeleteQueries();
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		myCaptureQueriesListener.logInsertQueries();
		// 2 concepts, 1 link
		assertEquals(3, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.clear();

		delta = newDeltaCodeSystem();
		delta.addConcept().setCode("RootA").setDisplay("Root A")
			.addConcept().setCode("ChildAA").setDisplay("Child AA")
			.addConcept().setCode("ChildAAA").setDisplay("Child AAA");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);
		assertHierarchyContainsExactly(
			"RootA seq=0",
			" ChildAA seq=0",
			"  ChildAAA seq=0"
		);

		myCaptureQueriesListener.logDeleteQueries();
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		myCaptureQueriesListener.logInsertQueries();
		// 1 concept, 1 link
		assertEquals(2, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.clear();

		delta = newDeltaCodeSystem();
		delta.addConcept().setCode("RootA").setDisplay("Root A")
			.addConcept().setCode("ChildAA").setDisplay("Child AA")
			.addConcept().setCode("ChildAAA").setDisplay("Child AAA")
			.addConcept().setCode("ChildAAAA").setDisplay("Child AAAA");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);
		assertHierarchyContainsExactly(
			"RootA seq=0",
			" ChildAA seq=0",
			"  ChildAAA seq=0",
			"   ChildAAAA seq=0"
		);

		myCaptureQueriesListener.logDeleteQueries();
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		myCaptureQueriesListener.logInsertQueries();
		// 1 concept, 1 link
		assertEquals(2, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.clear();

	}

	@Test
	public void testAddNotPermittedForNonExternalCodeSystem() {

		// Create not-present
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo/cs");
		cs.setVersion("1.0");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		myCodeSystemDao.create(cs);

		try {
			myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), newDeltaCodeSystem());
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("can not apply a delta - wrong content mode: COMPLETE");
		}

	}

	@Test
	public void testAddChildToExistingChild() {
		CodeSystem set;

		// Create not-present
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo/cs");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		myCodeSystemDao.create(cs);

		// Add parent with 1 child
		set = newDeltaCodeSystem();
		set.addConcept().setCode("ParentA").setDisplay("Parent A")
			.addConcept().setCode("ChildA").setDisplay("Child A");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), set);

		// Check so far
		assertHierarchyContainsExactly(
			"ParentA seq=0",
			" ChildA seq=0"
		);

		// Add sub-child to existing child
		ourLog.info("*** Adding child to existing child");
		set = newDeltaCodeSystem();
		set.addConcept().setCode("ChildA").setDisplay("Child A")
			.addConcept().setCode("ChildAA").setDisplay("Child AA");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), set);

		// Check so far
		assertHierarchyContainsExactly(
			"ParentA seq=0",
			" ChildA seq=0",
			"  ChildAA seq=0"
		);

	}

	@Test
	public void testAddChildWithVeryLongDescription() {
		CodeSystem set;

		// Create not-present
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo/cs");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		myCodeSystemDao.create(cs);

		// Add parent with 1 child
		set = newDeltaCodeSystem();
		set.addConcept().setCode("ParentA").setDisplay("Parent A")
			.addConcept().setCode("ChildA").setDisplay("Child A");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), set);

		// Check so far
		assertHierarchyContainsExactly(
			"ParentA seq=0",
			" ChildA seq=0"
		);

		// Add sub-child to existing child
		ourLog.info("*** Adding child to existing child");
		set = newDeltaCodeSystem();
		set.addConcept().setCode("ChildA").setDisplay("Child A")
			.addConcept().setCode("ChildAA").setDisplay(leftPad("", 10000, 'Z'));
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), set);

		// Check so far
		assertHierarchyContainsExactly(
			"ParentA seq=0",
			" ChildA seq=0",
			"  ChildAA seq=0"
		);

	}

	@Test
	public void testAddWithoutPreExistingCodeSystem() {

		CodeSystem delta = newDeltaCodeSystem();
		delta.addConcept().setCode("CBC").setDisplay("Complete Blood Count");
		delta.addConcept().setCode("URNL").setDisplay("Routine Urinalysis");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(CodeSystem.SP_URL, new UriParam("http://foo/cs"));
		IBundleProvider searchResult = myCodeSystemDao.search(params, mySrd);
		assertEquals(1, Objects.requireNonNull(searchResult.size()).intValue());
		CodeSystem outcome = (CodeSystem) searchResult.getResources(0, 1).get(0);

		assertEquals("http://foo/cs", outcome.getUrl());
		assertEquals(CodeSystem.CodeSystemContentMode.NOTPRESENT, outcome.getContent());

		IValidationSupport.LookupCodeResult lookup = myTermSvc.lookupCode(new ValidationSupportContext(myValidationSupport), new LookupCodeRequest("http://foo/cs", "CBC"));
		assertEquals("Complete Blood Count", lookup.getCodeDisplay());
	}


	@Test
	public void testAddLargeHierarchy() {
		myStorageSettings.setDeferIndexingForCodesystemsOfSize(5);

		createNotPresentCodeSystem();
		ValueSet vs;
		vs = expandNotPresentCodeSystem();
		assertThat(vs.getExpansion().getContains()).isEmpty();

		CodeSystem delta = newDeltaCodeSystem();

		// Create a nice deep hierarchy
		CodeSystem.ConceptDefinitionComponent concept = delta.addConcept().setCode("Root").setDisplay("Root");
		int nestedDepth = 10;
		for (int i = 0; i < nestedDepth; i++) {
			String name = concept.getCode();
			concept = concept.addConcept().setCode(name + "0").setDisplay(name + "0");
		}

		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);

		assertTrue(myTermDeferredStorageSvc.isStorageQueueEmpty(true));

		int counter = 0;
		while (!myTermDeferredStorageSvc.isStorageQueueEmpty(true) && ++counter < 10000) {
			myTermDeferredStorageSvc.saveDeferred();
		}
		if (counter >= 10000) {
			ourLog.error("Failed to empty myTermDeferredStorageSvc storage queue after 10000 attempts");
			myTermDeferredStorageSvc.logQueueForUnitTest();
			fail("myTermDeferredStorageSvc.saveDeferred() did not empty myTermDeferredStorageSvc storage queue.");
		}

		List<String> expectedHierarchy = new ArrayList<>();
		for (int i = 0; i < nestedDepth + 1; i++) {
			String expected = leftPad("", i, " ") +
				"Root" +
				leftPad("", i, "0") +
				" seq=0";
			expectedHierarchy.add(expected);
		}

		assertHierarchyContainsExactly(expectedHierarchy.toArray(new String[0]));

	}

	@Autowired
	private ITermDeferredStorageSvc myTermDeferredStorageSvc;


	@Test
	public void testAddModifiesExistingCodesInPlace() {

		// Add codes
		CodeSystem delta = newDeltaCodeSystem();
		delta.addConcept().setCode("codea").setDisplay("CODEA0");
		delta.addConcept().setCode("codeb").setDisplay("CODEB0");

		UploadStatistics outcome = myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);
		assertEquals(2, outcome.getAddedConceptCount());
		assertThat(myTermSvc.lookupCode(new ValidationSupportContext(myValidationSupport),
			new LookupCodeRequest("http://foo/cs", "codea")).getCodeDisplay()).isEqualTo("CODEA0");

		// Add codes again with different display
		delta = newDeltaCodeSystem();
		delta.addConcept().setCode("codea").setDisplay("CODEA1");
		delta.addConcept().setCode("codeb").setDisplay("CODEB1");
		outcome = myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);
		assertEquals(2, outcome.getUpdatedConceptCount());
		assertThat(myTermSvc.lookupCode(new ValidationSupportContext(myValidationSupport),
			new LookupCodeRequest("http://foo/cs", "codea")).getCodeDisplay()).isEqualTo("CODEA1");

		// Add codes again with no changes
		outcome = myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);
		assertEquals(0, outcome.getAddedConceptCount());
		assertEquals(0, outcome.getUpdatedConceptCount());
		assertThat(myTermSvc.lookupCode(new ValidationSupportContext(myValidationSupport),
			new LookupCodeRequest("http://foo/cs", "codea")).getCodeDisplay()).isEqualTo("CODEA1");
	}

	@Test
	public void testAddWithPropertiesAndDesignations() {

		// Create not-present
		CodeSystem cs = new CodeSystem();
		cs.setName("Description of my life");
		cs.setUrl("http://foo/cs");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		cs.setVersion("1.2.3");

		CodeSystem.ConceptDefinitionComponent concept = cs
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
		myCodeSystemDao.create(cs, mySrd);

		IValidationSupport.LookupCodeResult result = myTermSvc.lookupCode(new ValidationSupportContext(myValidationSupport),
				new LookupCodeRequest("http://foo/cs", "lunch"));
		assertEquals(true, result.isFound());
		assertEquals("lunch", result.getSearchedForCode());
		assertEquals("http://foo/cs", result.getSearchedForSystem());

		Parameters output = (Parameters) result.toParameters(myFhirContext, null);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		assertEquals("Description of my life", ((StringType) output.getParameterValue("name")).getValue());
		assertEquals("1.2.3", ((StringType) output.getParameterValue("version")).getValue());
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
		assertEquals("flavour", ((StringType) properties.get(0).getPart().get(0).getValue()).getValueAsString());
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
	public void testRemove() {

		// Create not-present
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo/cs");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		myCodeSystemDao.create(cs);

		CodeSystem delta = newDeltaCodeSystem();
		CodeSystem.ConceptDefinitionComponent codeA = delta.addConcept().setCode("codeA").setDisplay("displayA");
		CodeSystem.ConceptDefinitionComponent codeAA = codeA
			.addConcept()
			.setCode("codeAA")
			.setDisplay("displayAA");
		codeAA
			.addConcept()
			.setCode("codeAAA")
			.setDisplay("displayAAA");
		delta.addConcept().setCode("codeB").setDisplay("displayB");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);

		assertEquals(true, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeB").isPresent()));
		assertEquals(true, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeA").isPresent()));
		assertEquals(true, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeAA").isPresent()));
		assertEquals(true, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeAAA").isPresent()));

		// Remove CodeB
		delta = newDeltaCodeSystem();
		delta.addConcept().setCode("codeB").setDisplay("displayB");
		myTermCodeSystemStorageSvc.removeCodeSystemConcepts(newSrd(), delta);

		assertEquals(false, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeB").isPresent()));
		assertEquals(true, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeA").isPresent()));
		assertEquals(true, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeAA").isPresent()));
		assertEquals(true, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeAAA").isPresent()));

		// Remove CodeA
		runInTransaction(() -> {
			ourLog.info("About to remove CodeA. Have codes:\n * {}", myTermConceptDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
		});


		myCaptureQueriesListener.clear();
		CodeSystem delta2 = newDeltaCodeSystem();
		delta2.addConcept().setCode("codeA");
		myTermCodeSystemStorageSvc.removeCodeSystemConcepts(newSrd(), delta2);
		myCaptureQueriesListener.logAllQueries();

		runInTransaction(() -> {
			ourLog.info("Done removing. Have codes:\n * {}", myTermConceptDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
		});

		assertEquals(false, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeB").isPresent()));
		assertEquals(false, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeA").isPresent()));
		assertEquals(false, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeAA").isPresent())); //TODO GGG JA this assert fails. If you swap to `deleteByPid` it does not fail.
		assertEquals(false, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeAAA").isPresent()));//And I assume this one does too.

	}


	@Test
	void deltaRemove_lookupCode_removedCodeNotFound() {
		// Setup: create code system and add a concept
		CodeSystem delta = newDeltaCodeSystem();
		delta.addConcept().setCode("codeA").setDisplay("displayA");
		delta.addConcept().setCode("codeB").setDisplay("displayB");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);

		// Verify lookupCode succeeds for both codes
		IValidationSupport.LookupCodeResult resultA = myTermSvc.lookupCode(
			new ValidationSupportContext(myValidationSupport), new LookupCodeRequest("http://foo/cs", "codeA"));
		assertThat(resultA).isNotNull();
		assertThat(resultA.isFound()).isTrue();
		assertThat(resultA.getCodeDisplay()).isEqualTo("displayA");

		IValidationSupport.LookupCodeResult resultB = myTermSvc.lookupCode(
			new ValidationSupportContext(myValidationSupport), new LookupCodeRequest("http://foo/cs", "codeB"));
		assertThat(resultB).isNotNull();
		assertThat(resultB.isFound()).isTrue();

		// Remove codeA
		CodeSystem removeDelta = newDeltaCodeSystem();
		removeDelta.addConcept().setCode("codeA");
		myTermCodeSystemStorageSvc.removeCodeSystemConcepts(newSrd(), removeDelta);

		// Verify lookupCode fails for removed code and succeeds for remaining code
		IValidationSupport.LookupCodeResult afterRemoveA = myTermSvc.lookupCode(
			new ValidationSupportContext(myValidationSupport), new LookupCodeRequest("http://foo/cs", "codeA"));
		assertThat(afterRemoveA).isNotNull();
		assertThat(afterRemoveA.isFound()).isFalse();

		IValidationSupport.LookupCodeResult afterRemoveB = myTermSvc.lookupCode(
			new ValidationSupportContext(myValidationSupport), new LookupCodeRequest("http://foo/cs", "codeB"));
		assertThat(afterRemoveB).isNotNull();
		assertThat(afterRemoveB.isFound()).isTrue();
		assertThat(afterRemoveB.getCodeDisplay()).isEqualTo("displayB");
	}

	@Test
	public void testRemove_UnknownSystem() {

		CodeSystem delta = newDeltaCodeSystem();
		delta.addConcept().setCode("codeA").setDisplay("displayA");
		try {
			myTermCodeSystemStorageSvc.removeCodeSystemConcepts(newSrd(), delta);
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("Unknown code system: http://foo/cs");
		}

	}


	private ValueSet expandNotPresentCodeSystem() {
		ValueSet vs = new ValueSet();
		vs.setUrl("http://foo/vs");
		vs.getCompose().addInclude().setSystem("http://foo/cs");
		vs = myValueSetDao.expand(vs, null);
		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(vs));
		return vs;
	}

	@Test
	void deltaAdd_lookupCode_subsequentAddReflectsNewCode() {
		// Add initial code and verify it is found
		CodeSystem delta = newDeltaCodeSystem();
		delta.addConcept().setCode("codeA").setDisplay("displayA");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);

		IValidationSupport.LookupCodeResult resultA = myTermSvc.lookupCode(
			new ValidationSupportContext(myValidationSupport), new LookupCodeRequest("http://foo/cs", "codeA"));
		assertThat(resultA).isNotNull();
		assertThat(resultA.isFound()).isTrue();

		// Lookup a not-yet-added code to prime the cache with the current code system version
		IValidationSupport.LookupCodeResult resultBBefore = myTermSvc.lookupCode(
			new ValidationSupportContext(myValidationSupport), new LookupCodeRequest("http://foo/cs", "codeB"));
		assertThat(resultBBefore).isNotNull();
		assertThat(resultBBefore.isFound()).isFalse();

		// Add codeB — the code system version cache must be invalidated so the new code is visible
		CodeSystem delta2 = newDeltaCodeSystem();
		delta2.addConcept().setCode("codeB").setDisplay("displayB");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta2);

		IValidationSupport.LookupCodeResult resultBAfter = myTermSvc.lookupCode(
			new ValidationSupportContext(myValidationSupport), new LookupCodeRequest("http://foo/cs", "codeB"));
		assertThat(resultBAfter).isNotNull();
		assertThat(resultBAfter.isFound()).isTrue();
		assertThat(resultBAfter.getCodeDisplay()).isEqualTo("displayB");
	}

	@Test
	void deltaAdd_lookupCode_codeSystemCreatedAfterInitialMiss() {
		// Lookup a code from a system that does not exist yet —
		// the NO_CURRENT_VERSION sentinel is cached for "http://new/cs"
		IValidationSupport.LookupCodeResult initialResult = myTermSvc.lookupCode(
			new ValidationSupportContext(myValidationSupport), new LookupCodeRequest("http://foo/cs", "codeA"));
		assertThat(initialResult).isNotNull();
		assertThat(initialResult.isFound()).isFalse();

		// Create the code system — the sentinel must be evicted from the cache
		CodeSystem delta = newDeltaCodeSystem();
		delta.addConcept().setCode("codeA").setDisplay("displayA");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);

		// Lookup must now succeed
		IValidationSupport.LookupCodeResult afterCreate = myTermSvc.lookupCode(
			new ValidationSupportContext(myValidationSupport), new LookupCodeRequest("http://foo/cs", "codeA"));
		assertThat(afterCreate).isNotNull();
		assertThat(afterCreate.isFound()).isTrue();
		assertThat(afterCreate.getCodeDisplay()).isEqualTo("displayA");
	}

	@Test
	void deltaAdd_validateCode_preExpandedValueSetReflectsNewCode() {
		// Setup: NOTPRESENT CodeSystem + ValueSet that includes all its codes
		createNotPresentCodeSystem();
		ValueSet vs = new ValueSet();
		vs.setUrl("http://foo/vs");
		vs.getCompose().addInclude().setSystem("http://foo/cs");
		myValueSetDao.create(vs, mySrd);

		// Add initial code and pre-expand the ValueSet
		CodeSystem delta = newDeltaCodeSystem();
		delta.addConcept().setCode("codeA").setDisplay("displayA");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta);
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		// Validate via the pre-expanded path — populates myValueSetCache
		ValidationSupportContext ctx = new ValidationSupportContext(myValidationSupport);
		ConceptValidationOptions options = new ConceptValidationOptions();
		IValidationSupport.CodeValidationResult resultA = myValidationSupport.validateCode(ctx, options, "http://foo/cs", "codeA", null, "http://foo/vs");
		assertThat(resultA).isNotNull();
		assertThat(resultA.isOk()).isTrue();

		// codeB is not in the expansion yet
		IValidationSupport.CodeValidationResult resultBBefore = myValidationSupport.validateCode(ctx, options, "http://foo/cs", "codeB", null, "http://foo/vs");
		assertThat(resultBBefore).isNotNull();
		assertThat(resultBBefore.isOk()).isFalse();

		// Add codeB — triggers invalidateCaches() which clears myValueSetCache.
		// Also explicitly invalidate the pre-calculated expansion so the VS is re-expanded
		// with the updated CodeSystem content (delta add does not automatically re-queue VS expansion).
		CodeSystem delta2 = newDeltaCodeSystem();
		delta2.addConcept().setCode("codeB").setDisplay("displayB");
		myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), delta2);
		myTermValueSetStorageSvc.invalidatePreCalculatedExpansionOfValueSetsContainingCodeSystem("http://foo/cs");
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		// codeB must now validate successfully against the updated pre-expanded ValueSet
		IValidationSupport.CodeValidationResult resultBAfter = myValidationSupport.validateCode(ctx, options, "http://foo/cs", "codeB", null, "http://foo/vs");
		assertThat(resultBAfter).isNotNull();
		assertThat(resultBAfter.isOk()).isTrue();
	}

	private void createNotPresentCodeSystem() {
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo/cs");
		cs.setVersion("1.0");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		myCodeSystemDao.create(cs);
	}

}

