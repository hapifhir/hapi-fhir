package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyResultJson;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.util.JsonUtil;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.LOINC_URI;
import static ca.uhn.fhir.util.HapiExtensions.EXT_VALUESET_EXPANSION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TerminologyLoaderSvcLoincJpaTest extends BaseJpaR4Test {

	private static final String LOINC_IMAGING_DOCUMENT_CODES_VS_URL =
		"http://loinc.org/vs/loinc-imaging-document-codes";

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
	void testLoadLoincMultipleVersions() throws IOException {
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
			assertEquals(1, myTermCodeSystemVersionDao.count(), TermTestUtil.MSG_ONE_CODE_SYSTEM_VERSION_PER_UPLOAD);
			assertEquals(10, myTermValueSetDao.count());
			assertEquals(5, myTermConceptMapDao.count());
			assertEquals(16, myResourceTableDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion versionedTermCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.66");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), versionedTermCodeSystemVersion.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), versionedTermCodeSystemVersion.getResource().getId());

			// Make sure we calculated the concept closure
			TermConcept concept = myTermConceptDao.findByCodeSystemAndCodeList(versionedTermCodeSystemVersion.getPid(), List.of(
				"LP52258-8"
				)).get(0);
			assertThat(concept.getParentPidsAsString()).matches("[0-9]+ [0-9]+ [0-9]+ [0-9]+");
		});

		// Validate the report
		JobInstance jobInstance = myJobCoordinator.getInstance(instanceId);
		String report = JsonUtil.deserialize(jobInstance.getReport(), ImportTerminologyResultJson.class).getReport();
		ourLog.info("Report:\n{}", report);
		assertThat(report).contains("Concepts Added               : 82");

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
			assertEquals(2, myTermCodeSystemVersionDao.count(), TermTestUtil.MSG_ONE_CODE_SYSTEM_VERSION_PER_UPLOAD);
			assertEquals(10 * 2, myTermValueSetDao.count());
			assertEquals(5 * 2, myTermConceptMapDao.count());
			assertEquals(16 * 2, myResourceTableDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion versionedTermCodeSystem = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.66");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), versionedTermCodeSystem.getPid());
			assertNotEquals(myTermCodeSystem.getResource().getId(), versionedTermCodeSystem.getResource().getId());

			TermCodeSystemVersion currentTermCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.67");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), currentTermCodeSystemVersion.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), currentTermCodeSystemVersion.getResource().getId());
		});


		// Load LOINC marked as version 2.68
		files = new ZipCollectionBuilder(true);
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v268_loincupload.properties");
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion("2.68", files);

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(82 * 3, myTermConceptDao.count());
			assertEquals(8 * 3, myTermConceptParentChildLinkDao.count());
			assertEquals(3, myTermCodeSystemVersionDao.count(), TermTestUtil.MSG_ONE_CODE_SYSTEM_VERSION_PER_UPLOAD);
			assertEquals(10 * 3, myTermValueSetDao.count());
			assertEquals(5 * 3, myTermConceptMapDao.count());
			assertEquals(16 * 3, myResourceTableDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion secondVersionedTermCodeSystem = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.66");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), secondVersionedTermCodeSystem.getPid());
			assertNotEquals(myTermCodeSystem.getResource().getId(), secondVersionedTermCodeSystem.getResource().getId());

			TermCodeSystemVersion versionedTermCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.67");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), versionedTermCodeSystemVersion.getPid());
			assertNotEquals(myTermCodeSystem.getResource().getId(), versionedTermCodeSystemVersion.getResource().getId());

			TermCodeSystemVersion currentTermCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.68");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), currentTermCodeSystemVersion.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), currentTermCodeSystemVersion.getResource().getId());
		});

		logAllCodeSystemsAndVersionsCodeSystemsAndVersions();
		myJpaValidationSupportChain.invalidateCaches();

		CodeSystem cs = (CodeSystem) myValidationSupport.fetchCodeSystem("http://loinc.org");
		assertNotNull(cs);
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
	void testLoadLoincVersionNotCurrent() throws IOException {
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
			assertEquals(2, myTermCodeSystemVersionDao.count(), TermTestUtil.MSG_ONE_CODE_SYSTEM_VERSION_PER_UPLOAD);
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion newTermCodeSystemVersion =
				myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.67");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), newTermCodeSystemVersion.getPid());

			TermCodeSystemVersion oldTermCodeSystemVersion =
				myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.66");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), oldTermCodeSystemVersion.getPid());
		});


	}

	@Test
	void testValueSetExpansion() throws IOException {
		// Load LOINC marked as version 2.67

		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v267_loincupload.properties");
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion("2.67", files);

		logAllValueSets();

		ValueSetExpansionOptions options = new ValueSetExpansionOptions();
		ValueSet outcome = myValueSetDao.expand(new IdType("ValueSet/LL1001-8-2.67"), options, newSrd());
		ourLog.info("Expansion outcome: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		assertEquals("http://loinc.org", outcome.getExpansion().getContains().get(0).getSystem());
		assertEquals("2.67", outcome.getExpansion().getContains().get(0).getVersion());
		assertEquals("LA6270-8", outcome.getExpansion().getContains().get(0).getCode());
		assertEquals("Never", outcome.getExpansion().getContains().get(0).getDisplay());

		String valueSetUrl = outcome.getUrl();

		runInTransaction(() -> {
			List<TermValueSet> valueSets = myTermValueSetDao.findTermValueSetByUrl(PageRequest.of(0, 10), valueSetUrl);
			assertThat(valueSets).hasSize(1);
			assertEquals(valueSetUrl, valueSets.get(0).getUrl());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, valueSets.get(0).getExpansionStatus());
		});

		outcome = myValueSetDao.expand(new IdType("ValueSet/LL1001-8-2.67"), options, newSrd());
		String expansionMessage = outcome.getMeta().getExtensionString(EXT_VALUESET_EXPANSION_MESSAGE);
		assertThat(expansionMessage).contains("using an expansion that was pre-calculated");
	}

	/**
	 * Asserts an invariant rather than a code count, so that it holds for any LOINC test dataset: a
	 * pre-expansion may only contain codes the import actually stored.
	 */
	@Test
	void importLoinc_twoVersions_PreExpansionsContainOnlyCodesStoredByTheImport() throws IOException {
		// first import - nothing is being replaced, so no CodeSystem version deletion is in flight
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v267_loincupload.properties");
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion("2.66", files);

		assertPreExpansionsContainOnlyStoredCodes();
		assertImagingDocumentCodesPreExpansionHasOnlyTheStoredCode();

		// second import - this one replaces the version above, so a version deletion runs alongside it
		files = new ZipCollectionBuilder(true);
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v267_loincupload.properties");
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion("2.67", files);

		assertPreExpansionsContainOnlyStoredCodes();
		assertImagingDocumentCodesPreExpansionHasOnlyTheStoredCode();
	}

	/**
	 * The imaging document ValueSet enumerates the nine LOINC codes listed in
	 * {@code AccessoryFiles/ImagingDocuments/ImagingDocumentCodes.csv}, of which only
	 * {@code 17787-3} is present in {@code LoincTable/Loinc.csv}. A correct pre-expansion therefore
	 * holds exactly one concept.
	 * <p>
	 * Note that rows are not filtered on intendedVersionId. A pre-expanded ValueSet keeps a
	 * non-null one: {@code TermValueSetStorageSvcImpl.activateStagingVersion} promotes the staging
	 * row and deletes the row it replaces, but never clears the staging marker.
	 */
	private void assertImagingDocumentCodesPreExpansionHasOnlyTheStoredCode() {
		runInTransaction(() -> {
			List<TermValueSet> allValueSets = myTermValueSetDao.findAll();
			String storedValueSets = allValueSets.stream()
				.map(valueSet -> valueSet.getUrl() + "|" + valueSet.getVersion() + " concepts="
					+ valueSet.getTotalConcepts() + " status=" + valueSet.getExpansionStatus())
				.sorted()
				.collect(Collectors.joining("\n  "));

			List<TermValueSet> valueSets = allValueSets.stream()
				.filter(valueSet -> LOINC_IMAGING_DOCUMENT_CODES_VS_URL.equals(valueSet.getUrl()))
				.toList();

			assertThat(valueSets)
				.as("Expected the import to have generated %s. Stored ValueSets:\n  %s",
					LOINC_IMAGING_DOCUMENT_CODES_VS_URL, storedValueSets)
				.isNotEmpty();

			assertThat(valueSets)
				.allSatisfy(valueSet -> assertThat(valueSet.getTotalConcepts())
					.as("Pre-expansion of %s|%s", valueSet.getUrl(), valueSet.getVersion())
					.isEqualTo(1L));
		});
	}

	/**
	 * Fails with the ValueSets that hold LOINC codes absent from every stored CodeSystem version.
	 */
	private void assertPreExpansionsContainOnlyStoredCodes() {
		runInTransaction(() -> {
			Set<String> storedCodes =
				myTermConceptDao.findAll().stream().map(TermConcept::getCode).collect(Collectors.toSet());

			Map<String, List<String>> unknownCodesByValueSet = myTermValueSetConceptDao.findAll().stream()
				.filter(concept -> LOINC_URI.equals(concept.getSystem()))
				.filter(concept -> !storedCodes.contains(concept.getCode()))
				.collect(Collectors.groupingBy(
					concept -> concept.getValueSet().getUrl(),
					TreeMap::new,
					Collectors.mapping(TermValueSetConcept::getCode, Collectors.toList())));

			assertThat(unknownCodesByValueSet)
				.as("Pre-expanded ValueSets must not contain LOINC codes that the import never stored")
				.isEmpty();
		});
	}

	@Test
	void testLoadLoinc_NoDistributionAttached() {
		// Test
		ZipCollectionBuilder files = new ZipCollectionBuilder(false);
		String instanceId = myTerminologyTestHelper.startImportLoincJobAndWaitForFailure("2.67", files);

		// Verify
		String errorMessage = myJobCoordinator.getInstance(instanceId).getErrorMessage();
		assertThat(errorMessage).contains("No distribution file (loinc.zip) was attached for LOINC");
	}

}
