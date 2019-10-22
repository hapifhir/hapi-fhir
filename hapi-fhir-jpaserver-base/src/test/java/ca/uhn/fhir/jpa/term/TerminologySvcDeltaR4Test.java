package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;

@TestPropertySource(properties = {
	"scheduling_disabled=true"
})
public class TerminologySvcDeltaR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(TerminologySvcDeltaR4Test.class);


	@Test
	public void testAddRootConcepts() {
		createNotPresentCodeSystem();
		ValueSet vs;
		vs = expandNotPresentCodeSystem();
		assertEquals(0, vs.getExpansion().getContains().size());

		CustomTerminologySet delta = new CustomTerminologySet();
		delta.addRootConcept("RootA", "Root A");
		delta.addRootConcept("RootB", "Root B");
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo/cs", delta);
		assertHierarchyContains(
			"RootA seq=1",
			"RootB seq=2"
		);

		delta = new CustomTerminologySet();
		delta.addRootConcept("RootC", "Root C");
		delta.addRootConcept("RootD", "Root D");
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo/cs", delta);
		assertHierarchyContains(
			"RootA seq=1",
			"RootB seq=2",
			"RootC seq=3",
			"RootD seq=4"
		);
	}

	@Test
	public void testAddRootConceptsWithCycle() {
		createNotPresentCodeSystem();
		ValueSet vs;
		vs = expandNotPresentCodeSystem();
		assertEquals(0, vs.getExpansion().getContains().size());

		CustomTerminologySet delta = new CustomTerminologySet();

		TermConcept root = delta.addRootConcept("Root", "Root");
		TermConcept child = root.addChild(TermConceptParentChildLink.RelationshipTypeEnum.ISA).setCode("Child").setDisplay("Child");
		child.addChild(root, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		try {
			myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo/cs", delta);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Cycle detected around code Root", e.getMessage());
		}
	}

	@Test
	public void testAddHierarchyConcepts() {
		ourLog.info("Starting testAddHierarchyConcepts");

		createNotPresentCodeSystem();
		assertHierarchyContains();

		ourLog.info("Have created code system");
		runInTransaction(() -> {
			ourLog.info("All code systems: {}", myTermCodeSystemDao.findAll());
			ourLog.info("All code system versions: {}", myTermCodeSystemVersionDao.findAll());
			ourLog.info("All concepts: {}", myTermConceptDao.findAll());
		});

		CustomTerminologySet delta = new CustomTerminologySet();
		delta.addRootConcept("RootA", "Root A");
		delta.addRootConcept("RootB", "Root B");
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo/cs", delta);
		assertHierarchyContains(
			"RootA seq=1",
			"RootB seq=2"
		);

		ourLog.info("Have performed add");
		runInTransaction(() -> {
			ourLog.info("All code systems: {}", myTermCodeSystemDao.findAll());
			ourLog.info("All code system versions: {}", myTermCodeSystemVersionDao.findAll());
			ourLog.info("All concepts: {}", myTermConceptDao.findAll());
		});

		delta = new CustomTerminologySet();
		delta.addUnanchoredChildConcept("RootA", "ChildAA", "Child AA");
		delta.addUnanchoredChildConcept("RootA", "ChildAB", "Child AB");
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo/cs", delta);
		assertHierarchyContains(
			"RootA seq=1",
			" ChildAA seq=1",
			" ChildAB seq=2",
			"RootB seq=2"
		);
	}

	@Test
	public void testAddMoveConceptFromOneParentToAnother() {
		createNotPresentCodeSystem();
		assertHierarchyContains();

		UploadStatistics outcome;
		CustomTerminologySet delta;

		delta = new CustomTerminologySet();
		delta.addRootConcept("RootA", "Root A")
			.addChild(TermConceptParentChildLink.RelationshipTypeEnum.ISA).setCode("ChildAA").setDisplay("Child AA");
		delta.addRootConcept("RootB", "Root B");
		outcome = myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo/cs", delta);
		assertHierarchyContains(
			"RootA seq=1",
			" ChildAA seq=1",
			"RootB seq=2"
		);
		assertEquals(3, outcome.getUpdatedConceptCount());

		delta = new CustomTerminologySet();
		delta.addUnanchoredChildConcept("RootB", "ChildAA", "Child AA");
		outcome = myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo/cs", delta);
		assertEquals(1, outcome.getUpdatedConceptCount());
		assertHierarchyContains(
			"RootA seq=1",
			"RootB seq=2",
			" ChildAA seq=1"
		);

	}

	@Test
	public void testAddNotPermittedForNonExternalCodeSystem() {

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
			myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo", new CustomTerminologySet());
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("can not apply a delta - wrong content mode"));
		}

	}

	@Test
	public void testAddWithoutPreExistingCodeSystem() {

		CustomTerminologySet delta = new CustomTerminologySet();
		delta.addRootConcept("CBC", "Complete Blood Count");
		delta.addRootConcept("URNL", "Routine Urinalysis");
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo", delta);

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(CodeSystem.SP_URL, new UriParam("http://foo"));
		IBundleProvider searchResult = myCodeSystemDao.search(params, mySrd);
		assertEquals(1, Objects.requireNonNull(searchResult.size()).intValue());
		CodeSystem outcome = (CodeSystem) searchResult.getResources(0, 1).get(0);

		assertEquals("http://foo", outcome.getUrl());
		assertEquals(CodeSystem.CodeSystemContentMode.NOTPRESENT, outcome.getContent());

		IContextValidationSupport.LookupCodeResult lookup = myTermSvc.lookupCode(myFhirCtx, "http://foo", "CBC");
		assertEquals("Complete Blood Count", lookup.getCodeDisplay());
	}


	@Test
	public void testAddModifiesExistingCodesInPlace() {

		// Add codes
		CustomTerminologySet delta = new CustomTerminologySet();
		delta.addRootConcept("codea", "CODEA0");
		delta.addRootConcept("codeb", "CODEB0");

		UploadStatistics outcome = myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo", delta);
		assertEquals(2, outcome.getUpdatedConceptCount());
		assertEquals("CODEA0", myTermSvc.lookupCode(myFhirCtx, "http://foo", "codea").getCodeDisplay());

		// Add codes again with different display
		delta = new CustomTerminologySet();
		delta.addRootConcept("codea", "CODEA1");
		delta.addRootConcept("codeb", "CODEB1");
		outcome = myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo", delta);
		assertEquals(2, outcome.getUpdatedConceptCount());
		assertEquals("CODEA1", myTermSvc.lookupCode(myFhirCtx, "http://foo", "codea").getCodeDisplay());

		// Add codes again with no changes
		outcome = myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo", delta);
		assertEquals(2, outcome.getUpdatedConceptCount());
		assertEquals("CODEA1", myTermSvc.lookupCode(myFhirCtx, "http://foo", "codea").getCodeDisplay());
	}

	@Test
	public void testAddUnanchoredWithUnknownParent() {
		createNotPresentCodeSystem();

		// Add root code
		CustomTerminologySet delta = new CustomTerminologySet();
		delta.addRootConcept("CodeA", "Code A");
		UploadStatistics outcome = myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo", delta);
		assertEquals(1, outcome.getUpdatedConceptCount());

		// Try to add child to nonexistent root code
		delta = new CustomTerminologySet();
		delta.addUnanchoredChildConcept("CodeB", "CodeBB", "Code BB");
		try {
			myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo/cs", delta);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Unable to add code \"CodeBB\" to unknown parent: CodeB"));
		}
	}

	@Test
	public void testAddRelocateHierarchy() {
		createNotPresentCodeSystem();

		// Add code hierarchy
		CustomTerminologySet delta = new CustomTerminologySet();
		TermConcept codeA = delta.addRootConcept("CodeA", "Code A");
		TermConcept codeAA = codeA.addChild(TermConceptParentChildLink.RelationshipTypeEnum.ISA).setCode("CodeAA").setDisplay("Code AA");
		codeAA.addChild(TermConceptParentChildLink.RelationshipTypeEnum.ISA).setCode("CodeAAA").setDisplay("Code AAA");
		codeAA.addChild(TermConceptParentChildLink.RelationshipTypeEnum.ISA).setCode("CodeAAB").setDisplay("Code AAB");
		TermConcept codeB = delta.addRootConcept("CodeB", "Code B");
		TermConcept codeBA = codeB.addChild(TermConceptParentChildLink.RelationshipTypeEnum.ISA).setCode("CodeBA").setDisplay("Code BA");
		codeBA.addChild(TermConceptParentChildLink.RelationshipTypeEnum.ISA).setCode("CodeBAA").setDisplay("Code BAA");
		codeBA.addChild(TermConceptParentChildLink.RelationshipTypeEnum.ISA).setCode("CodeBAB").setDisplay("Code BAB");
		UploadStatistics outcome = myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo/cs", delta);
		assertEquals(8, outcome.getUpdatedConceptCount());
		assertHierarchyContains(
			"CodeA seq=1",
			" CodeAA seq=1",
			"  CodeAAA seq=1",
			"  CodeAAB seq=2",
			"CodeB seq=2",
			" CodeBA seq=1",
			"  CodeBAA seq=1",
			"  CodeBAB seq=2"
		);

		// Move a single child code to a new spot and make sure the hierarchy comes along
		// for the ride..
		delta = new CustomTerminologySet();
		delta.addUnanchoredChildConcept("CodeB", "CodeAA", "Code AA");
		outcome = myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo/cs", delta);
		assertEquals(3, outcome.getUpdatedConceptCount());
		assertHierarchyContains(
			"CodeA seq=1",
			"CodeB seq=2",
			" CodeBA seq=1",
			"  CodeBAA seq=1",
			"  CodeBAB seq=2",
			" CodeAA seq=2", // <-- CodeAA got added here so it comes second
			"  CodeAAA seq=1",
			"  CodeAAB seq=2"
		);

	}

	@Test
	@Ignore
	public void testAddWithPropertiesAndDesignations() {

		// Create not-present
		CodeSystem cs = new CodeSystem();
		cs.setName("Description of my life");
		cs.setUrl("http://foo/cs");
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

		IContextValidationSupport.LookupCodeResult result = myTermSvc.lookupCode(myFhirCtx, "http://foo/cs", "lunch");
		assertEquals(true, result.isFound());
		assertEquals("lunch", result.getSearchedForCode());
		assertEquals("http://foo/cs", result.getSearchedForSystem());

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
	public void testRemove() {

		// Create not-present
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo/cs");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		myCodeSystemDao.create(cs);

		CustomTerminologySet delta = new CustomTerminologySet();
		TermConcept codeA = delta.addRootConcept("codeA", "displayA");
		TermConcept codeAA = codeA
			.addChild(TermConceptParentChildLink.RelationshipTypeEnum.ISA)
			.setCode("codeAA")
			.setDisplay("displayAA");
		codeAA
			.addChild(TermConceptParentChildLink.RelationshipTypeEnum.ISA)
			.setCode("codeAAA")
			.setDisplay("displayAAA");
		delta.addRootConcept("codeB", "displayB");
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo/cs", delta);

		assertEquals(true, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeB").isPresent()));
		assertEquals(true, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeA").isPresent()));
		assertEquals(true, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeAA").isPresent()));
		assertEquals(true, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeAAA").isPresent()));

		// Remove CodeB
		delta = new CustomTerminologySet();
		delta.addRootConcept("codeB", "displayB");
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsRemove("http://foo/cs", delta);

		assertEquals(false, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeB").isPresent()));
		assertEquals(true, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeA").isPresent()));
		assertEquals(true, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeAA").isPresent()));
		assertEquals(true, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeAAA").isPresent()));

		// Remove CodeA
		delta = new CustomTerminologySet();
		delta.addRootConcept("codeA");
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsRemove("http://foo/cs", delta);

		assertEquals(false, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeB").isPresent()));
		assertEquals(false, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeA").isPresent()));
		assertEquals(false, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeAA").isPresent()));
		assertEquals(false, runInTransaction(() -> myTermSvc.findCode("http://foo/cs", "codeAAA").isPresent()));

	}


	@Test
	public void testRemove_UnknownSystem() {

		CustomTerminologySet delta = new CustomTerminologySet();
		delta.addRootConcept("codeA", "displayA");
		try {
			myTermCodeSystemStorageSvc.applyDeltaCodeSystemsRemove("http://foo/cs", delta);
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Unknown code system: http://foo"));
		}

	}




	private ValueSet expandNotPresentCodeSystem() {
		ValueSet vs = new ValueSet();
		vs.setUrl("http://foo/vs");
		vs.getCompose().addInclude().setSystem("http://foo/cs");
		vs = myValueSetDao.expand(vs, null);
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(vs));
		return vs;
	}

	private void createNotPresentCodeSystem() {
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo/cs");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		myCodeSystemDao.create(cs);
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
}
