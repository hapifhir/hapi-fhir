package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyResultJson;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.util.JsonUtil;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static ca.uhn.fhir.jpa.term.api.ITermLoaderSvc.LOINC_URI;
import static ca.uhn.fhir.util.HapiExtensions.EXT_VALUESET_EXPANSION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TerminologyLoaderSvcLoincJpaTest extends BaseJpaR4Test {

	@Autowired
	private TerminologyTestHelper myTerminologyTestHelper;
	@Autowired
	private ValidationSupportChain myJpaValidationSupportChain;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		runInTransaction(() -> {
			assertEquals(0, myTermCodeSystemDao.count());
			assertEquals(0, myTermCodeSystemVersionDao.count());
			assertEquals(0, myTermValueSetDao.count());
			assertEquals(0, myTermConceptDao.count());
			assertEquals(0, myTermConceptMapDao.count());
			assertEquals(0, myResourceTableDao.count());
		});
	}

	@Test
	public void testLoadLoincMultipleVersions() throws IOException {
		// Load LOINC marked as version 2.66

		ZipCollectionBuilder files;
		files = new ZipCollectionBuilder(true);
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v267_loincupload.properties");
		String instanceId = myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion("2.66", files);

		logAllValueSets();

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(82, myTermConceptDao.count());
			assertEquals(8, myTermConceptParentChildLinkDao.count());
			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(10, myTermValueSetDao.count());
			assertEquals(5, myTermConceptMapDao.count());
			assertEquals(16, myResourceTableDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion myTermCodeSystemVersion_versioned = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.66");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_versioned.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_versioned.getResource().getId());

			// Make sure we calculated the concept closure
			TermConcept concept = myTermConceptDao.findByCodeSystemAndCodeList(myTermCodeSystemVersion_versioned.getPid(), List.of(
				"LP52258-8"
				)).get(0);
			assertThat(concept.getParentPidsAsString()).matches("[0-9]+ [0-9]+ [0-9]+ [0-9]+");
		});

		// Validate the report
		JobInstance jobInstance = myJobCoordinator.getInstance(instanceId);
		String report = JsonUtil.deserialize(jobInstance.getReport(), ImportTerminologyResultJson.class).getReport();
		ourLog.info("Report:\n{}", report);
		assertThat(report).contains("Concepts Added             : 82");

		logAllCodeSystemsAndVersionsCodeSystemsAndVersions();
		logAllConcepts();
		logAllConceptParentChildLinks();

		myTerminologyTestHelper.assertConceptDisplay(LOINC_URI, "10013-1", "R' wave amplitude in lead I");
		myTerminologyTestHelper.assertConceptDisplay(LOINC_URI + "|2.66", "10013-1", "R' wave amplitude in lead I");
		myTerminologyTestHelper.assertConceptNotFound(LOINC_URI + "|2.99", "10013-1");

		// Update LOINC marked as version 2.67
		files = new ZipCollectionBuilder(true);
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v267_loincupload.properties");
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion("2.67", files);

		logAllCodeSystemsAndVersionsCodeSystemsAndVersions();

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(82 * 2, myTermConceptDao.count());
			assertEquals(8 * 2, myTermConceptParentChildLinkDao.count());
			assertEquals(2 * 2, myTermCodeSystemVersionDao.count());
			assertEquals(10 * 2, myTermValueSetDao.count());
			assertEquals(5 * 2, myTermConceptMapDao.count());
			assertEquals(16 * 2, myResourceTableDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion myTermCodeSystemVersion_versioned = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.66");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_versioned.getPid());
			assertNotEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_versioned.getResource().getId());

			TermCodeSystemVersion myTermCodeSystemVersion_current = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.67");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_current.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_current.getResource().getId());
		});


		// Load LOINC marked as version 2.68
		files = new ZipCollectionBuilder(true);
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v268_loincupload.properties");
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion("2.68", files);

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(82 * 3, myTermConceptDao.count());
			assertEquals(8 * 3, myTermConceptParentChildLinkDao.count());
			assertEquals(2 * 3, myTermCodeSystemVersionDao.count());
			assertEquals(10 * 3, myTermValueSetDao.count());
			assertEquals(5 * 3, myTermConceptMapDao.count());
			assertEquals(16 * 3, myResourceTableDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion mySecondTermCodeSystemVersion_versioned = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.66");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), mySecondTermCodeSystemVersion_versioned.getPid());
			assertNotEquals(myTermCodeSystem.getResource().getId(), mySecondTermCodeSystemVersion_versioned.getResource().getId());

			TermCodeSystemVersion myTermCodeSystemVersion_versioned = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.67");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_versioned.getPid());
			assertNotEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_versioned.getResource().getId());

			TermCodeSystemVersion myTermCodeSystemVersion_current = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.68");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_current.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_current.getResource().getId());
		});

		logAllCodeSystemsAndVersionsCodeSystemsAndVersions();
		myJpaValidationSupportChain.invalidateCaches();

		CodeSystem cs = (CodeSystem) myValidationSupport.fetchCodeSystem("http://loinc.org");
		assertEquals("2.68", cs.getVersion());

		runInTransaction(()->{

			for (TermCodeSystem codeSystem : myTermCodeSystemDao.findAll()) {
				assertEquals("LOINC", codeSystem.getName());
				assertEquals("http://loinc.org", codeSystem.getCodeSystemUri());
			}
			for (TermCodeSystemVersion codeSystem : myTermCodeSystemVersionDao.findAll()) {
				assertEquals("LOINC", codeSystem.getCodeSystemDisplayName());
			}

		});
	}

	@Test
	public void testLoadLoincVersionNotCurrent() throws IOException {
		// Load LOINC marked as version 2.66
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v267_loincupload.properties");
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion("2.66", files);

		// Load LOINC marked as version 2.67
		// and don't make it current
		files = new ZipCollectionBuilder(true);
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v267_loincupload.properties");
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion("2.67", files, true);

		logAllCodeSystemsAndVersionsCodeSystemsAndVersions();

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(4, myTermCodeSystemVersionDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion myTermCodeSystemVersion_new =
				myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.67");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_new.getPid());

			TermCodeSystemVersion myTermCodeSystemVersion_old =
				myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.66");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_old.getPid());
		});


	}

	@Test
	public void testValueSetExpansion() throws IOException {
		// Load LOINC marked as version 2.66

		ZipCollectionBuilder files;
		files = new ZipCollectionBuilder(true);
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v267_loincupload.properties");
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion("2.67", files);

		ValueSetExpansionOptions options = new ValueSetExpansionOptions();
		ValueSet outcome = myValueSetDao.expand(new IdType("ValueSet/LL1001-8-2.67"), options, newSrd());
		ourLog.info("Expansion outcome: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		assertEquals("http://loinc.org", outcome.getExpansion().getContains().get(0).getSystem());
		assertEquals("2.67", outcome.getExpansion().getContains().get(0).getVersion());
		assertEquals("LA6270-8", outcome.getExpansion().getContains().get(0).getCode());
		assertEquals("Never", outcome.getExpansion().getContains().get(0).getDisplay());

		String expansionMessage = outcome.getMeta().getExtensionString(EXT_VALUESET_EXPANSION_MESSAGE);
		assertThat(expansionMessage).contains("has not yet been pre-expanded");

		// Now run the pre-expansion

		logAllValueSets();
		myTermDeferredStorageSvc.saveDeferred();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		outcome = myValueSetDao.expand(new IdType("ValueSet/LL1001-8-2.67"), options, newSrd());
		expansionMessage = outcome.getMeta().getExtensionString(EXT_VALUESET_EXPANSION_MESSAGE);
		assertThat(expansionMessage).contains("using an expansion that was pre-calculated");

	}



}
