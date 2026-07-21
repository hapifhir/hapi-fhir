package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyModeEnum;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.batch2.jobs.term.custom.CustomTerminologyCsvBuilder;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.hl7.fhir.common.hapi.validation.util.TermConceptPropertyTypeEnum;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerminologyLoaderSvcCustomJpaTest extends BaseJpaR4Test {

	private static final String CODESYSTEM_URL = "http://example.com/labCodes";
	private static final String VERSION_1_0 = "1.0";
	private static final String VERSION_2_0 = "2.0";

	@Autowired
	private TerminologyTestHelper myTerminologyTestHelper;

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testLoadComplete(boolean theZipDistribution) throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder(theZipDistribution);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CODESYSTEM_JSON);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CONCEPTS_FILE);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_HIERARCHY_FILE);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_PROPERTIES_FILE);

		// Actually do the load
		myTerminologyTestHelper.startImportCustomJobAndWaitForCompletion(CODESYSTEM_URL, VERSION_1_0, files);

		// Verify codesystem
		CodeSystem cs = myCodeSystemDao.read(new IdType("CodeSystem/exampleLabCodes"), newSrd());
		assertEquals(CODESYSTEM_URL, cs.getUrl());
		assertEquals(VERSION_1_0, cs.getVersion());
		assertEquals(CodeSystem.CodeSystemContentMode.NOTPRESENT, cs.getContent());
		assertEquals("Example Lab Codes", cs.getName());

		runInTransaction(()->{
			TermCodeSystemVersion csv = myTermCodeSystemVersionDao.findByCodeSystemUriAndVersion(CODESYSTEM_URL, VERSION_1_0);

			// Root code
			TermConcept chemConcept = myTermConceptDao.findByCodeSystemAndCode(csv.getPid(), "CHEM").orElseThrow();
			assertEquals("Chemistry", chemConcept.getDisplay());

			TermConcept hbConcept = myTermConceptDao.findByCodeSystemAndCode(csv.getPid(), "HB").orElseThrow();
			assertEquals("Hemoglobin", hbConcept.getDisplay());
			assertThat(hbConcept.getProperties()).hasSize(6);
			assertEquals("red", hbConcept.getPrimitiveProperty("color"));

			assertThat(chemConcept.getParents()).isEmpty();
			assertThat(chemConcept.getChildCodes()).hasSize(2);
			assertThat(chemConcept.getChildCodes().stream().map(t->t.getCode()).toList()).containsExactlyInAnyOrder(
				"HB", "NEUT"
			);

		});
	}


	@Test
	void testCodeSystemNotPresentInDistribution_FoundInRepository() throws IOException {
		// Setup
		CodeSystem cs = loadResourceFromClasspath(CodeSystem.class, "/custom_term/" + TerminologyConstants.CUSTOM_CODESYSTEM_JSON);
		myCodeSystemDao.update(cs, newSrd());

		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CONCEPTS_FILE);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_HIERARCHY_FILE);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_PROPERTIES_FILE);

		// Test
		myTerminologyTestHelper.startImportCustomJobAndWaitForCompletion(CODESYSTEM_URL, VERSION_1_0, files);

		// Verify
		runInTransaction(()-> {
			assertEquals(5, myTermConceptDao.count());
		});
	}

	@Test
	void testLoadWithNoCodeSystem() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CONCEPTS_FILE);

		// Test
		String jobId = myTerminologyTestHelper.startImportCustomJobAndWaitForCompletion(CODESYSTEM_URL, VERSION_1_0, files);

		// Verify
		String report = myTerminologyTestHelper.getReport(jobId);
		assertThat(report).contains("Concepts Added               : 5");
		assertCodeSystemResourceWasCreated();
	}

	@Test
	public void testLoadWithWrongCodeSystemUrl() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		files.addFileZip("/custom_term_wrong_url/", TerminologyConstants.CUSTOM_CODESYSTEM_JSON);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CONCEPTS_FILE);

		// Test
		String jobId = myTerminologyTestHelper.startImportCustomJobAndWaitForFailure(CODESYSTEM_URL, VERSION_1_0, files);

		// Verify
		JobInstance instance = myJobCoordinator.getInstance(jobId);
		assertThat(instance.getErrorMessage()).contains("CodeSystem resources has unexpected URL: http://this-is-the-wrong-url. Expected: http://example.com/labCodes");
	}

	@Test
	public void testLoadWithCodeSystemWithoutId() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		files.addFileZip("/custom_term_no_id/", TerminologyConstants.CUSTOM_CODESYSTEM_JSON);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CONCEPTS_FILE);

		// Test
		String jobId = myTerminologyTestHelper.startImportCustomJobAndWaitForCompletion(CODESYSTEM_URL, VERSION_1_0, files);

		// Verify
		String report = myTerminologyTestHelper.getReport(jobId);
		assertThat(report).contains("Concepts Added               : 5");
		assertCodeSystemResourceWasCreated();
	}

	private void assertCodeSystemResourceWasCreated() {
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add(CodeSystem.SP_URL, new UriParam(CODESYSTEM_URL))
			.add(CodeSystem.SP_VERSION, new TokenParam(VERSION_1_0));
		IBundleProvider found = myCodeSystemDao.search(map, newSrd());
		assertEquals(1, found.size());
		CodeSystem cs = (CodeSystem) found.getResources(0, 1).get(0);
		assertEquals(CODESYSTEM_URL, cs.getUrl());
		assertEquals(VERSION_1_0, cs.getVersion());
		assertEquals(CodeSystem.CodeSystemContentMode.NOTPRESENT, cs.getContent());
	}

	@Test
	public void testLoadWithCodeSystemWithoutUrlOrVersion() throws Exception {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem
			.addConcept()
			.setCode("CODE-0")
			.setDisplay("Code 0");
		String codeSystemEncoded = myFhirContext.newJsonParser().encodeResourceToString(codeSystem);

		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		files.addFileText(codeSystemEncoded, TerminologyConstants.CUSTOM_CODESYSTEM_JSON);

		// Test
		String jobId = myTerminologyTestHelper.startImportCustomJobAndWaitForCompletion(CODESYSTEM_URL, VERSION_1_0, files);

		// Verify
		String report = myTerminologyTestHelper.getReport(jobId);
		assertThat(report).contains("Concepts Added               : 1");
		assertCodeSystemResourceWasCreated();

		IBundleProvider codeSystemSearchResults = myCodeSystemDao.search(SearchParameterMap.newSynchronous(), newSrd());
		assertEquals(1, codeSystemSearchResults.size());
		CodeSystem actualCodeSystem = (CodeSystem) codeSystemSearchResults.getResources(0, 1).get(0);

		assertEquals(CODESYSTEM_URL, actualCodeSystem.getUrl());
		assertEquals(VERSION_1_0, actualCodeSystem.getVersion());
		assertEquals(CodeSystem.CodeSystemContentMode.NOTPRESENT, actualCodeSystem.getContent());
		assertThat(actualCodeSystem.getConcept()).isEmpty();
	}


	@Test
	public void testLoadWithMultipleCodeSystem() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CODESYSTEM_XML);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CODESYSTEM_JSON);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CONCEPTS_FILE);

		// Test
		String jobId = myTerminologyTestHelper.startImportCustomJobAndWaitForFailure(CODESYSTEM_URL, VERSION_1_0, files);

		// Verify
		JobInstance instance = myJobCoordinator.getInstance(jobId);
		assertThat(instance.getErrorMessage()).contains("Multiple CodeSystem resources were supplied in the custom terminology distribution");
	}

	/**
	 * No hierarchy file supplied
	 */
	@Test
	public void testLoadCodesOnly() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CODESYSTEM_XML);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CONCEPTS_FILE);

		// Test
		myTerminologyTestHelper.startImportCustomJobAndWaitForCompletion(CODESYSTEM_URL, VERSION_1_0, files);

		// Verify
		runInTransaction(()-> {
			TermCodeSystemVersion csv = myTermCodeSystemVersionDao.findByCodeSystemUriAndVersion(CODESYSTEM_URL, VERSION_1_0);

			// Root code
			TermConcept chemConcept = myTermConceptDao.findByCodeSystemAndCode(csv.getPid(), "CHEM").orElseThrow();
			assertEquals("Chemistry", chemConcept.getDisplay());
			assertThat(chemConcept.getParents()).isEmpty();
			assertThat(chemConcept.getChildren()).isEmpty();
		});
	}

	@ParameterizedTest
	@CsvSource(textBlock =
		///```
		///Code System             Use CodeSystem  Expect Final
		///Pre Existing  Mode      As Input        Concept Count
	    ///```
		"""
		   true,         ADD,      false         , 8
		   true,         REMOVE,   false         , 2
		   true,         SNAPSHOT, false         , 5
		   true,         ADD,      true          , 8
		   true,         REMOVE,   true          , 2
		   true,         SNAPSHOT, true          , 5
		   false,        ADD,      false         , 8
		   false,        REMOVE,   false         , 2
		   false,        SNAPSHOT, false         , 5
		   false,        ADD,      true          , 8
		   false,        REMOVE,   true          , 2
		   false,        SNAPSHOT, true          , 5
		""")
	void testModes(boolean theCodeSystemPreExisting, ImportTerminologyModeEnum theMode, boolean theUseCodeSystemAsInput, int theExpectedFinalConceptCount) throws IOException {
		// Setup

		if (theCodeSystemPreExisting) {
			// Given: We have pre-initialized some codes using a CodeSystem
			CodeSystem initialCs = new CodeSystem();
			initialCs.setUrl(CODESYSTEM_URL);
			initialCs.setVersion(VERSION_1_0);
			initialCs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
			initialCs.addConcept().setCode("INITIAL-1").setDisplay("Initial 1")
				.addProperty(new CodeSystem.ConceptPropertyComponent(new CodeType("INITIAL-1-PROP-1"), new StringType("INITIAL-1-PROP-1 Value")))
				.addDesignation(new CodeSystem.ConceptDefinitionDesignationComponent(new StringType("INITIAL-1-PROP-1 Designation")).setLanguage("en"))
				.addConcept().setCode("INITIAL-1-CHILD-1");
			initialCs.addConcept().setCode("INITIAL-2").setDisplay("Initial 2");
			initialCs.addConcept().setCode("INITIAL-3").setDisplay("Initial 3");
			myTermCodeSystemStorageSvc.addCodeSystemConcepts(newSrd(), initialCs);
			assertEquals(4, runInTransaction(() -> myTermConceptDao.count()));
		} else {
			// Given: We have pre-initialized some codes using a CSV delta
			CustomTerminologyCsvBuilder deltaBuilder = new CustomTerminologyCsvBuilder();
			CustomTerminologyCsvBuilder.ConceptBuilder initial1 = deltaBuilder.addConcept("INITIAL-1").withDisplay("Initial 1");
			initial1.withProperty("INITIAL-1-PROP-1", TermConceptPropertyTypeEnum.STRING, "INITIAL-1-PROP-1 Value");
			deltaBuilder.addConcept("INITIAL-1-CHILD-1").withParent("INITIAL-1");
			deltaBuilder.addConcept("INITIAL-2").withDisplay("Initial 2");
			deltaBuilder.addConcept("INITIAL-3").withDisplay("Initial 3");
			ZipCollectionBuilder files = new ZipCollectionBuilder(true);
			files.addCustomTerminology(deltaBuilder);
			myTerminologyTestHelper.startImportCustomJobAndWaitForCompletion(CODESYSTEM_URL, VERSION_1_0, files, ImportTerminologyModeEnum.ADD);
		}
		logAllCodeSystemsAndVersionsCodeSystemsAndVersions();
		logAllConcepts();

		// Test
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);

		if (theUseCodeSystemAsInput) {
			// Apply a delta using a CodeSystem resource
			CodeSystem codeSystem = new CodeSystem();
			codeSystem.setUrl(CODESYSTEM_URL);
			codeSystem.setVersion(VERSION_1_0);
			codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
			codeSystem.addConcept().setCode("INITIAL-1").setDisplay("Initial 1 New Display");
			CodeSystem.ConceptDefinitionComponent new1 = codeSystem.addConcept().setCode("NEW-1").setDisplay("New 1");
			new1.addProperty().setCode("NEW-1-PROP-1").setValue(new StringType("NEW-1-PROP-1 Value"));
			new1.addConcept().setCode("NEW-1-CHILD-1").setDisplay("NEW-1");
			codeSystem.addConcept().setCode("NEW-2").setDisplay("New 2");
			codeSystem.addConcept().setCode("NEW-3").setDisplay("New 3");
			files.addFileText(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem), TerminologyConstants.CUSTOM_CODESYSTEM_JSON);
		} else {
			// Apply a delta using CSV format
			CustomTerminologyCsvBuilder deltaBuilder = new CustomTerminologyCsvBuilder();
			deltaBuilder.addConcept("INITIAL-1").withDisplay("Initial 1 New Display");
			deltaBuilder.addConcept("NEW-1").withDisplay("New 1")
				.withProperty("NEW-1-PROP-1", TermConceptPropertyTypeEnum.STRING, "NEW-1-PROP-1 Value");
			deltaBuilder.addConcept("NEW-1-CHILD-1").withParent("NEW-1");
			deltaBuilder.addConcept("NEW-2").withDisplay("New 2");
			deltaBuilder.addConcept("NEW-3").withDisplay("New 3");
			files.addCustomTerminology(deltaBuilder);
		}
		String jobInstanceId = myTerminologyTestHelper.startImportCustomJobAndWaitForCompletion(CODESYSTEM_URL, VERSION_1_0, files, theMode);
		logAllCodeSystemsAndVersionsCodeSystemsAndVersions();
		logAllConcepts();

		// Validate

		List<String> codes = runInTransaction(()->{
			TermCodeSystemVersion csv = myTermCodeSystemVersionDao.findByCodeSystemUriAndVersion(CODESYSTEM_URL, VERSION_1_0);
			return myTermConceptDao.findByCodeSystemVersion(csv).stream().map(t->t.getCode()).toList();
		});

		String report = myTerminologyTestHelper.getReport(jobInstanceId);

		switch (theMode) {
			case ADD -> {
				assertThat(codes).containsExactlyInAnyOrder(
				"INITIAL-1",
				"INITIAL-1-CHILD-1",
				"INITIAL-2",
				"INITIAL-3",
				"NEW-1",
				"NEW-1-CHILD-1",
				"NEW-2",
				"NEW-3");
				assertThat(report).contains(
					"Concepts Added               : 4",
					"Concepts Links Added         : 1",
					"Concept Properties Added     : 1"
				);
			}
			case REMOVE -> {
				assertThat(codes).containsExactlyInAnyOrder(
				"INITIAL-2",
				"INITIAL-3");
				assertThat(report).contains(
					"Concepts Removed             : 2",
					"Concepts Links Removed       : 1",
					"Concept Properties Removed   : 1"
				);
			}
			case SNAPSHOT -> {
				assertThat(codes).containsExactlyInAnyOrder(
				"INITIAL-1",
				"NEW-1",
				"NEW-1-CHILD-1",
				"NEW-2",
				"NEW-3");
				assertThat(report).contains(
					"Concepts Added               : 5",
					"Concepts Links Added         : 1",
					"Concept Properties Added     : 1"
				);
			}
		}

		// Make sure we can expand codes form the updated CodeSystem
		ValueSet vs = new ValueSet();
		vs.setId("VS");
		vs.setUrl(VS_URL);
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.getCompose().addInclude().setSystem(CODESYSTEM_URL);
		ValueSet expansion = myValueSetDao.expand(vs, new ValueSetExpansionOptions());
		assertThat(expansion.getExpansion().getContains()).hasSize(theExpectedFinalConceptCount);
	}


	/**
	 * Make sure that
	 */
	@ParameterizedTest
	@CsvSource(textBlock =
		///```
		///Make First Version   Make Second Version   Expected Code
		///Current              Current               in Expansion
		///```
		"""
		   false              , false               , CODE-1.0-1
		   true               , false               , CODE-1.0-1
		   false              , true                , CODE-2.0-1
		   true               , true                , CODE-2.0-1
		"""
	)
	void testAddConcepts_MarkAsCurrent(boolean theMakeFirstVersionCurrent, boolean theMakeSecondVersionCurrent, String theExpectedCode) throws IOException {

		// Setup

		ZipCollectionBuilder files = new ZipCollectionBuilder(false);
		CustomTerminologyCsvBuilder deltaBuilder = new CustomTerminologyCsvBuilder();
		deltaBuilder.addConcept("CODE-1.0-1").withDisplay("CODE 1");
		files.addCustomTerminology(deltaBuilder);
		myTerminologyTestHelper.startImportCustomJobAndWaitForCompletion(CODESYSTEM_URL, VERSION_1_0, files, ImportTerminologyModeEnum.ADD, !theMakeFirstVersionCurrent);

		// Test

		files = new ZipCollectionBuilder(false);
		deltaBuilder = new CustomTerminologyCsvBuilder();
		deltaBuilder.addConcept("CODE-2.0-1").withDisplay("CODE 1");
		files.addCustomTerminology(deltaBuilder);
		myTerminologyTestHelper.startImportCustomJobAndWaitForCompletion(CODESYSTEM_URL, VERSION_2_0, files, ImportTerminologyModeEnum.ADD, !theMakeSecondVersionCurrent);

		// Verify

		ValueSet vs = new ValueSet();
		vs.setId("VS");
		vs.setUrl(VS_URL);
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.getCompose().addInclude().setSystem(CODESYSTEM_URL);
		ValueSet expansion = myValueSetDao.expand(vs, new ValueSetExpansionOptions());
		assertThat(expansion.getExpansion().getContains().stream().map(t->t.getCode()).toList()).containsExactly(
			theExpectedCode
		);

}


}
