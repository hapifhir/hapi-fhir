package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.test.util.LogbackTestExtension;
import ch.qos.logback.classic.Level;
import org.hl7.fhir.common.hapi.validation.util.TermConceptPropertyTypeEnum;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.DecimalType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.IntegerType;
import org.hl7.fhir.r5.model.StringType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
import static ca.uhn.fhir.test.utilities.UuidUtils.UUID_PATTERN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

public class TermCodeSystemStorageSvcImplTest extends BaseJpaR5Test {

	public static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";

	private static final String HHH000502 = "HHH000502";

	@RegisterExtension
	LogbackTestExtension myHibernateLogCapture = new LogbackTestExtension(
		"org.hibernate.persister.entity.AbstractEntityPersister", Level.WARN);

	@Autowired
	private Batch2JobHelper myBatchJobHelper;
	@Autowired
	private ITermDeferredStorageSvc myTerminologyDeferredStorageSvc;
	@Autowired
	private ITermCodeSystemStorageSvc mySvc;
	@Autowired
	private ITermCodeSystemVersionDao myCodeSystemVersionDao;

	@CsvSource(textBlock = """
		# VersionToStage , MakeCurrent
		A                , true
		A                , false
		B                , true
		B                , false
		""")
	@ParameterizedTest
	void testActivateStagingCodeSystemVersion(String theVersionToStage, boolean theMakeCurrent) {
		createCodeSystem(withUrl("http://foo"), withVersion("A"), withCodeSystemContent("not-present"));
		runInTransaction(()->{
			TermCodeSystemVersion csv = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", "A");
			assertNotNull(csv);
			assertSame(csv, csv.getCodeSystem().getCurrentVersion());
		});

		String stagingVersionId = mySvc.startStagingCodeSystemVersion("http://foo", theVersionToStage).stagingVersionId();
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://foo");
		codeSystem.setVersion(stagingVersionId);
		codeSystem.addConcept().setCode("CODE-A").setDisplay("Display-A");
		mySvc.addCodeSystemConcepts(newSrd(), codeSystem);

		// Test
		mySvc.activateStagingCodeSystemVersion("http://foo", stagingVersionId, theMakeCurrent);

		// Verify
		runInTransaction(()->{
			TermCodeSystemVersion csv = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", theVersionToStage);
			assertNotNull(csv);
			assertThat(csv.getConcepts()).hasSize(1);
			if (theVersionToStage.equals("A") || theMakeCurrent) {
				assertSame(csv, csv.getCodeSystem().getCurrentVersion());
			} else {
				assertNotSame(csv, csv.getCodeSystem().getCurrentVersion());
			}
		});
		assertNoHHH000502Warnings();
	}


	@Test
	public void storeNewCodeSystemVersionForExistingCodeSystem_withoutVersion() {
		CodeSystem firstUpload = createCodeSystemWithMoreThan100Concepts();
		CodeSystem duplicateUpload = createCodeSystemWithMoreThan100Concepts();

		testCreatingAndUpdatingCodeSystemEntity(firstUpload, duplicateUpload, 125, Msg.code(848) + "Can not create multiple CodeSystem resources with CodeSystem.url \"http://example.com/my_code_system\", already have one with resource ID: CodeSystem/");

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(1, myTermCodeSystemVersionDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri(URL_MY_CODE_SYSTEM);

			TermCodeSystemVersion myTermCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidVersionIsNull(myTermCodeSystem.getPid());
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion.getResource().getId());
		});

		assertNoHHH000502Warnings();
	}


	@Test
	public void storeNewCodeSystemVersionForExistingCodeSystem_withVersion() {
		CodeSystem firstUpload = createCodeSystemWithMoreThan100Concepts();
		firstUpload.setVersion("1");

		CodeSystem duplicateUpload = createCodeSystemWithMoreThan100Concepts();
		duplicateUpload.setVersion("1");

		testCreatingAndUpdatingCodeSystemEntity(firstUpload, duplicateUpload, 125, Msg.code(848) + "Can not create multiple CodeSystem resources with CodeSystem.url \"http://example.com/my_code_system\" and CodeSystem.version \"1\", already have one with resource ID: CodeSystem/");

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(1, myTermCodeSystemVersionDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri(URL_MY_CODE_SYSTEM);

			TermCodeSystemVersion myTermCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "1");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion.getResource().getId());
		});

		// Now add a second version
		firstUpload = createCodeSystemWithMoreThan100Concepts();
		firstUpload.setVersion("2");

		duplicateUpload = createCodeSystemWithMoreThan100Concepts();
		duplicateUpload.setVersion("2");

		testCreatingAndUpdatingCodeSystemEntity(firstUpload, duplicateUpload, 251, Msg.code(848) + "Can not create multiple CodeSystem resources with CodeSystem.url \"http://example.com/my_code_system\" and CodeSystem.version \"2\", already have one with resource ID: CodeSystem/");

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(2, myTermCodeSystemVersionDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri(URL_MY_CODE_SYSTEM);

			TermCodeSystemVersion mySecondTermCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), mySecondTermCodeSystemVersion.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), mySecondTermCodeSystemVersion.getResource().getId());
		});

		assertNoHHH000502Warnings();
	}

	// Created by claude-opus-4-6
	@Test
	void storeNewCodeSystemVersionIfNeeded_notPresentPlaceholderWithDifferentResource_shouldUpdatePlaceholder() {
		// Reproduces SMILE-7421: When a NOTPRESENT CodeSystem already exists (e.g. from package
		// pre-seeding), creating a second FHIR resource for the same CodeSystem URL should succeed
		// because the existing TermCodeSystemVersion is a 0-concept placeholder.
		//
		// The real-world scenario: Package pre-seeding creates SNOMED from hl7.terminology.r4
		// (creating Resource A). Later, either a second pre-seed run or $upload-external-code-system
		// creates a different FHIR resource (Resource B) for the same URL. The NOTPRESENT
		// early-return path triggers a version duplicate check. The existing version (0 concepts,
		// points to Resource A) should be re-pointable to Resource B, but instead a
		// version-already-exists error (Msg.code(848)) is thrown.

		// Step 1: Create first NOTPRESENT CodeSystem (simulates package pre-seed install)
		CodeSystem cs1 = new CodeSystem();
		cs1.setUrl("http://snomed.info/sct");
		cs1.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs1.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.create(cs1, mySrd);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Verify: TermCodeSystem and TermCodeSystemVersion exist, with 0 concepts
		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct");
			assertThat(tcs).isNotNull();
			assertThat(tcs.getCurrentVersion()).isNotNull();
			long conceptCount = myTermConceptDao.countByCodeSystemVersion(tcs.getCurrentVersion().getPid());
			assertThat(conceptCount).isEqualTo(0);
		});

		myHibernateLogCapture.clearEvents();

		// Step 2: Create a second NOTPRESENT CodeSystem with the same URL.
		// This simulates the scenario where a new FHIR resource is created for the same
		// CodeSystem URL (e.g. a subsequent pre-seed run when the conditional search fails
		// to find the existing resource, or $upload-external-code-system creating a new resource).
		CodeSystem cs2 = new CodeSystem();
		cs2.setUrl("http://snomed.info/sct");
		cs2.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs2.setStatus(Enumerations.PublicationStatus.ACTIVE);

		// This should succeed — the existing TermCodeSystemVersion is a 0-concept placeholder
		// from the first pre-seed. It should be updated to point to the new resource.
		// Previously threw UnprocessableEntityException (Msg.code(848)) — fixed by SMILE-7421.
		JpaPid secondResourcePid = ((ResourceTable) myCodeSystemDao.create(cs2, mySrd).getEntity()).getId();
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Verify: TermCodeSystem and TermCodeSystemVersion both point to the second resource
		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct");
			assertThat(tcs).isNotNull();
			assertThat(tcs.getResource().getId()).isEqualTo(secondResourcePid);
			assertThat(tcs.getCurrentVersion()).isNotNull();
			assertThat(tcs.getCurrentVersion().getResource().getId()).isEqualTo(secondResourcePid);
		});

		assertNoHHH000502Warnings();
	}

	// Created by claude-opus-4-6
	@Test
	void storeNewCodeSystemVersionIfNeeded_versionedNotPresentPlaceholderWithDifferentResource_shouldUpdatePlaceholder() {
		// Same as the unversioned case above, but with an explicit version string.
		// The NOTPRESENT early-return path should also allow re-pointing when the existing
		// TermCodeSystemVersion has a version ID and zero concepts.

		// Step 1: Create first versioned NOTPRESENT CodeSystem
		CodeSystem cs1 = new CodeSystem();
		cs1.setUrl("http://snomed.info/sct");
		cs1.setVersion("5.0.0");
		cs1.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs1.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.create(cs1, mySrd);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Verify: TermCodeSystemVersion exists with version "5.0.0" and 0 concepts
		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct");
			assertThat(tcs).isNotNull();
			TermCodeSystemVersion version = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(tcs.getPid(), "5.0.0");
			assertThat(version).isNotNull();
			long conceptCount = myTermConceptDao.countByCodeSystemVersion(version.getPid());
			assertThat(conceptCount).isEqualTo(0);
		});

		// Step 2: Create second versioned NOTPRESENT CodeSystem with same URL and version
		CodeSystem cs2 = new CodeSystem();
		cs2.setUrl("http://snomed.info/sct");
		cs2.setVersion("5.0.0");
		cs2.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs2.setStatus(Enumerations.PublicationStatus.ACTIVE);

		// Previously threw Msg.code(848) — fixed by SMILE-7421.
		JpaPid secondResourcePid = ((ResourceTable) myCodeSystemDao.create(cs2, mySrd).getEntity()).getId();

		// Verify: TermCodeSystem and TermCodeSystemVersion both point to the second resource
		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct");
			assertThat(tcs).isNotNull();
			assertThat(tcs.getResource().getId()).isEqualTo(secondResourcePid);
			assertThat(tcs.getCurrentVersion()).isNotNull();
			assertThat(tcs.getCurrentVersion().getResource().getId()).isEqualTo(secondResourcePid);
		});
	}

	// Created by claude-opus-4-6
	@Test
	void storeNewCodeSystemVersionIfNeeded_notPresentDifferentVersions_shouldCreateSeparateVersions() {
		// When a versioned NOTPRESENT CodeSystem already exists and a second NOTPRESENT
		// CodeSystem with a DIFFERENT version (or null) is created, they occupy different
		// version slots. The NOTPRESENT early-return path should not find the existing
		// version (version mismatch), so it falls through to the normal creation path.
		// This should succeed on current master — no bug in this path.

		// Step 1: Create first NOTPRESENT CodeSystem with version "5.0.0"
		CodeSystem cs1 = new CodeSystem();
		cs1.setUrl("http://snomed.info/sct");
		cs1.setVersion("5.0.0");
		cs1.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs1.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.create(cs1, mySrd);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Step 2: Create second NOTPRESENT CodeSystem with no version (null)
		CodeSystem cs2 = new CodeSystem();
		cs2.setUrl("http://snomed.info/sct");
		cs2.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs2.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.create(cs2, mySrd);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Verify: Two TermCodeSystemVersion entries exist — one for "5.0.0", one for null
		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct");
			assertThat(tcs).isNotNull();
			assertThat(myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(tcs.getPid(), "5.0.0")).isNotNull();
			assertThat(myTermCodeSystemVersionDao.findByCodeSystemPidVersionIsNull(tcs.getPid())).isNotNull();
			assertThat(myTermCodeSystemVersionDao.count()).isEqualTo(2);
		});
	}

	// Created by claude-opus-4-6
	@Test
	void storeNewCodeSystemVersionIfNeeded_completeCodeSystemThenNotPresentDifferentResource_shouldRejectDuplicate() {
		// Guard rail: When a COMPLETE CodeSystem with real concepts already exists,
		// creating a second NOTPRESENT CodeSystem with a different FHIR resource for the
		// same URL should STILL throw Msg.code(848). The fix for SMILE-7421 must only
		// allow re-pointing when the existing version is a true placeholder (0 concepts).

		// Step 1: Create a COMPLETE CodeSystem with real concepts
		CodeSystem cs1 = new CodeSystem();
		cs1.setUrl("http://snomed.info/sct");
		cs1.setContent(Enumerations.CodeSystemContentMode.COMPLETE);
		cs1.setStatus(Enumerations.PublicationStatus.ACTIVE);
		for (int i = 0; i < 5; i++) {
			cs1.addConcept(new CodeSystem.ConceptDefinitionComponent("code" + i));
		}
		myCodeSystemDao.create(cs1, mySrd);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Verify: TermCodeSystemVersion exists with real concepts
		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct");
			assertThat(tcs).isNotNull();
			assertThat(tcs.getCurrentVersion()).isNotNull();
			long conceptCount = myTermConceptDao.countByCodeSystemVersion(tcs.getCurrentVersion().getPid());
			assertThat(conceptCount).isEqualTo(5);
		});

		// Step 2: Create a second NOTPRESENT CodeSystem with the same URL.
		// This creates a new FHIR resource (different from the COMPLETE one).
		// Since the existing version has real concepts, this should be rejected.
		CodeSystem cs2 = new CodeSystem();
		cs2.setUrl("http://snomed.info/sct");
		cs2.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs2.setStatus(Enumerations.PublicationStatus.ACTIVE);

		assertThatThrownBy(() -> myCodeSystemDao.create(cs2, mySrd))
			.isInstanceOf(UnprocessableEntityException.class)
			.hasMessageContaining("HAPI-0848");
	}

	// Created by claude-opus-4-6
	@Test
	void storeNewCodeSystemVersionIfNeeded_notPresentSameUrlSameResource_shouldKeepExistingVersion() {
		CodeSystem cs = new CodeSystem();
		cs.setUrl(URL_MY_CODE_SYSTEM);
		cs.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);

		JpaPid firstResourcePid = ((ResourceTable) myCodeSystemDao.create(cs, mySrd).getEntity()).getId();
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myHibernateLogCapture.clearEvents();

		// Second NOTPRESENT with same URL — conditional create returns the same FHIR resource,
		// so the existing TermCodeSystemVersion should be kept as-is (same resource PID).
		CodeSystem cs2 = new CodeSystem();
		cs2.setUrl(URL_MY_CODE_SYSTEM);
		cs2.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs2.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.update(cs2, "CodeSystem?url=" + URL_MY_CODE_SYSTEM, mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		runInTransaction(() -> {
			assertThat(myTermCodeSystemDao.count()).isEqualTo(1);
			assertThat(myTermCodeSystemVersionDao.count()).isEqualTo(1);
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri(URL_MY_CODE_SYSTEM);
			assertThat(tcs).isNotNull();
			assertThat(tcs.getCurrentVersion()).isNotNull();
			assertThat(tcs.getCurrentVersion().getResource().getId()).isEqualTo(firstResourcePid);
		});

		assertNoHHH000502Warnings();
	}

	@Test
	void testStartStagingCodeSystemVersion_DoesntAlreadyExist() {
		createCodeSystem(withUrl("http://foo"));

		// Test
		ITermCodeSystemStorageSvc.StartStagingCodeSystemVersionResponse outcome = mySvc.startStagingCodeSystemVersion("http://foo", "123");

		// Verify
		runInTransaction(() -> {
			TermCodeSystemVersion existing = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", outcome.stagingVersionId());
			assertNotNull(existing);
			assertEquals("http://foo", existing.getCodeSystem().getCodeSystemUri());
			assertEquals(outcome.stagingVersionId(), existing.getCodeSystemVersionId());
			assertThat(existing.getCodeSystemVersionId()).matches(UUID_PATTERN);
			assertEquals("123", existing.getCodeSystemIntendedVersionId());

			// The new version shouldn't be activated
			assertNotSame(existing, existing.getCodeSystem().getCurrentVersion());
			assertNull(existing.getCodeSystem().getCurrentVersion().getCodeSystemVersionId());
		});
	}

	@Test
	void storeNewCodeSystemVersionIfNeeded_rewriteIntoExistingVersionSlot_shouldNotThrow() {
		final JpaPid unversionedPid = createCs(makeCompleteCs(null, "a"));

		final String version = "1.0.0";
		final IIdType versionedId = myCodeSystemDao.create(makeCompleteCs(version, "b"), mySrd).getId().toUnqualifiedVersionless();

		final CodeSystem rewrite = makeCompleteCs(null, "b-updated");
		rewrite.setId(versionedId);
		final JpaPid rewritePid = ((ResourceTable) myCodeSystemDao.update(rewrite, mySrd).getEntity()).getId();

		// Verify: TermCodeSystem and TermCodeSystemVersion both point to the second resource
		runInTransaction(() -> {
			// check that the rewrite obtained the current TermCodeSystem slot
			TermCodeSystem currentTcs = myTermCodeSystemDao.findByCodeSystemUri(URL_MY_CODE_SYSTEM);
			assertThat(currentTcs).isNotNull();
			assertThat(currentTcs.getResource().getId()).isEqualTo(rewritePid);
			assertThat(currentTcs.getCurrentVersion()).isNotNull();
			assertThat(currentTcs.getCurrentVersion().getResource().getId()).isEqualTo(rewritePid);

			// check that there is no TermCodeSystem associated with the previous resource who had the slot
			TermCodeSystem unversionedTcs = myTermCodeSystemDao.findByResourcePid(unversionedPid);
			assertThat(unversionedTcs).isNull();

			// check that the rewrite can be queried by url and version
			TermCodeSystemVersion versionedTcsv = myTermCodeSystemVersionDao.findByCodeSystemUriAndVersion(URL_MY_CODE_SYSTEM, version);
			assertThat(versionedTcsv).isNull();

			// check that the rewrite (unversioned) CodeSystem has TermCodeSystemVersion
			assertThat(hasActiveTermVersion(rewritePid)).isTrue();

			// check that the unversioned CodeSystem has TermCodeSystemVersion as well
			assertThat(hasActiveTermVersion(unversionedPid)).isFalse();
		});
	}

	/**
	 * DAO mirror of {@code install_singleVersionPackageUpdatesResourceConflictingWithOlderVersionSlot_succeeds}.
	 * Resource A owns slot ("1"), B owns ("2"). Updating B to claim "1" exercises the
	 * {@code isUpdate} branch of {@code tryReleaseConflictingVersionRow} — A's slot is released.
	 */
	// Created by claude-sonnet-4-6
	@Test
	void storeNewCodeSystemVersionIfNeeded_updateClaimsSlotOwnedByOtherResource_releasesSlot() {
		final String version1 = "1";
		final String version2 = "2";

		final JpaPid pidA = createCs(makeCompleteCs(version1, "a"));
		final IIdType bId = myCodeSystemDao.create(makeCompleteCs(version2, "b"), mySrd).getId().toUnqualifiedVersionless();

		final CodeSystem rewrite = makeCompleteCs(version1, "b-updated");
		rewrite.setId(bId);
		final JpaPid pidB = ((ResourceTable) myCodeSystemDao.update(rewrite, mySrd).getEntity()).getId();

		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri(URL_MY_CODE_SYSTEM);
			assertThat(tcs).isNotNull();
			TermCodeSystemVersion slot = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(tcs.getPid(), version1);
			assertThat(slot).isNotNull();
			assertThat(slot.getResource().getId()).isEqualTo(pidB);
			assertThat(hasActiveTermVersion(pidA)).isFalse();
		});
	}

	// Created by claude-sonnet-4-6
	private CodeSystem makeCompleteCs(String theVersion, String theConceptCode) {
		CodeSystem cs = new CodeSystem();
		cs.setUrl(URL_MY_CODE_SYSTEM);
		if (theVersion != null) {
			cs.setVersion(theVersion);
		}
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(Enumerations.CodeSystemContentMode.COMPLETE);
		cs.addConcept(new CodeSystem.ConceptDefinitionComponent(theConceptCode));
		return cs;
	}

	// Created by claude-sonnet-4-6
	private JpaPid createCs(CodeSystem theCs) {
		return ((ResourceTable) myCodeSystemDao.create(theCs, mySrd).getEntity()).getId();
	}

	private boolean hasActiveTermVersion(JpaPid theCodeSystemResoucePid) {
		return myTermCodeSystemVersionDao
			.findByCodeSystemResourcePid(theCodeSystemResoucePid)
			.stream()
			.anyMatch(v -> v.getCodeSystemVersionId() == null
				|| !v.getCodeSystemVersionId().startsWith("DELETED_"));
	}

	// Generated by Claude Opus 4.6
	@Test
	void findExistingCodeSystemResourcePid_versionedCodeSystemExists_returnsPid() {
		CodeSystem cs = new CodeSystem();
		cs.setUrl(URL_MY_CODE_SYSTEM);
		cs.setVersion("1.0");
		cs.setContent(Enumerations.CodeSystemContentMode.COMPLETE);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.addConcept(new CodeSystem.ConceptDefinitionComponent("code1"));
		CodeSystem created = (CodeSystem) myCodeSystemDao.create(cs, mySrd).getResource();
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		runInTransaction(() -> {
			assertThat(myTermCodeSystemStorageSvc.findExistingCodeSystemResourcePid(URL_MY_CODE_SYSTEM, "1.0"))
				.isPresent()
				.hasValue(JpaPid.fromId(created.getIdElement().getIdPartAsLong()));
		});
	}

	// Generated by Claude Opus 4.6
	@Test
	void findExistingCodeSystemResourcePid_unversionedCodeSystemExists_returnsPid() {
		CodeSystem cs = new CodeSystem();
		cs.setUrl(URL_MY_CODE_SYSTEM);
		cs.setContent(Enumerations.CodeSystemContentMode.COMPLETE);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.addConcept(new CodeSystem.ConceptDefinitionComponent("code1"));
		CodeSystem created = (CodeSystem) myCodeSystemDao.create(cs, mySrd).getResource();
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		runInTransaction(() -> {
			assertThat(myTermCodeSystemStorageSvc.findExistingCodeSystemResourcePid(URL_MY_CODE_SYSTEM, null))
				.isPresent()
				.hasValue(JpaPid.fromId(created.getIdElement().getIdPartAsLong()));
		});
	}

	// Generated by Claude Opus 4.6
	@Test
	void findExistingCodeSystemResourcePid_unknownUrl_returnsEmpty() {
		runInTransaction(() -> {
			assertThat(myTermCodeSystemStorageSvc.findExistingCodeSystemResourcePid("http://unknown.org/cs", "1.0"))
				.isEmpty();
		});
	}

	// Generated by Claude Opus 4.6
	@Test
	void findExistingCodeSystemResourcePid_wrongVersion_returnsEmpty() {
		CodeSystem cs = new CodeSystem();
		cs.setUrl(URL_MY_CODE_SYSTEM);
		cs.setVersion("1.0");
		cs.setContent(Enumerations.CodeSystemContentMode.COMPLETE);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.addConcept(new CodeSystem.ConceptDefinitionComponent("code1"));
		myCodeSystemDao.create(cs, mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		runInTransaction(() -> {
			assertThat(myTermCodeSystemStorageSvc.findExistingCodeSystemResourcePid(URL_MY_CODE_SYSTEM, "2.0"))
				.isEmpty();
		});
	}

	private CodeSystem createCodeSystemWithMoreThan100Concepts() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);

		for (int i = 0; i < 125; i++) {
			codeSystem.addConcept(new CodeSystem.ConceptDefinitionComponent("codeA " + i));
		}

		codeSystem.setContent(Enumerations.CodeSystemContentMode.COMPLETE);
		return codeSystem;

	}

	private void testCreatingAndUpdatingCodeSystemEntity(CodeSystem theUpload, CodeSystem theDuplicate, int expectedCnt, String theDuplicateErrorBaseMsg) {

		// Create CodeSystem resource
		ResourceTable codeSystemResourceEntity = (ResourceTable) myCodeSystemDao.create(theUpload, mySrd).getEntity();

		// Create the CodeSystem and CodeSystemVersion entities
		validateCodeSystemUpdates(expectedCnt);

		// Update the CodeSystem
		theUpload.addConcept(new CodeSystem.ConceptDefinitionComponent("codeB"));
		// Update the CodeSystem and CodeSystemVersion entities
		runInTransaction(() -> myTermCodeSystemStorageSvc.storeNewCodeSystemVersionIfNeeded(theUpload, codeSystemResourceEntity));
		validateCodeSystemUpdates(expectedCnt + 1);

		// Try duplicating the CodeSystem
		JpaPid originalResId = codeSystemResourceEntity.getId();
		try {
			myCodeSystemDao.create(theDuplicate, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(theDuplicateErrorBaseMsg + originalResId, e.getMessage());
		}

		// Try updating code system when content mode is NOT PRESENT
		theUpload.setConcept(new ArrayList<>());
		theUpload.setContent((Enumerations.CodeSystemContentMode.NOTPRESENT));
		runInTransaction(() -> myTermCodeSystemStorageSvc.storeNewCodeSystemVersionIfNeeded(theUpload, codeSystemResourceEntity));
		validateCodeSystemUpdates(expectedCnt + 1);

	}

	private void validateCodeSystemUpdates(int theExpectedConceptCount) {
		myTerminologyDeferredStorageSvc.setProcessDeferred(true);
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.setProcessDeferred(false);
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
		assertEquals(theExpectedConceptCount, runInTransaction(() -> myTermConceptDao.count()));
	}

	@Test
	void testAddCodeSystemConcepts_WithDeepHierarchy() {
		createCodeSystem(withUrl("http://foo"), withVersion("1.23"), withCodeSystemContent("not-present"));

		CodeSystem input = new CodeSystem();
		input.setUrl("http://foo");
		input.setVersion("1.23");
		input.addConcept()
			.setCode("CODE-A-A-A");

		UploadStatistics response = mySvc.addCodeSystemConcepts(newSrd(), input);

		// Verify
		assertEquals(1, response.getAddedConceptCount());
		assertEquals(0, response.getAddedPropertyCount());
		assertEquals(0, response.getAddedDesignationCount());
		assertEquals(0, response.getAddedConceptLinkCount());

		// Add even deeper hierarchy

		input = new CodeSystem();
		input.setUrl("http://foo");
		input.setVersion("1.23");
		input.addConcept()
			.setCode("CODE-A")
			.addConcept()
			.setCode("CODE-A-A")
			.addConcept()
			.setCode("CODE-A-A-A");

		response = mySvc.addCodeSystemConcepts(newSrd(), input);

		// Verify
		assertEquals(2, response.getAddedConceptCount());
		assertEquals(0, response.getAddedPropertyCount());
		assertEquals(0, response.getAddedDesignationCount());
		assertEquals(2, response.getAddedConceptLinkCount());

		runInTransaction(()->{

			List<TermConcept> concepts = myTermConceptDao.findAll();
			for (TermConcept concept : concepts) {
				assertNull(concept.getParentPidsAsString());
			}

		});

	}


	@Test
	void testAddCodeSystemConcepts_WithPropertyAndDesignation() {
		createCodeSystem(withUrl("http://foo"), withCodeSystemContent("not-present"));
		String stagingVersion = mySvc.startStagingCodeSystemVersion("http://foo", "123").stagingVersionId();

		// Test
		CodeSystem input = new CodeSystem();
		input.setUrl("http://foo");
		input.setVersion(stagingVersion);
		input.addConcept()
			.setCode("A0")
			.setDisplay("A0-Display")
			.addDesignation(
				new CodeSystem.ConceptDefinitionDesignationComponent()
					.setValue("A0-Designation-Value")
					.setLanguage("en_CA")
					.setUse(new Coding("http://designations", "A0-desig", null)))
			.addProperty(
				new CodeSystem.ConceptPropertyComponent()
					.setCode("A0-Property")
					.setValue(new Coding("A0-Property-System", "A0-Property-Value", "A0-Property-Display"))
			);
		input.addConcept()
			.setCode("A1")
			.setDisplay("A1-Display");

		UploadStatistics response = mySvc.addCodeSystemConcepts(newSrd(), input);

		// Verify
		assertEquals(2, response.getAddedConceptCount());
		assertEquals(1, response.getAddedPropertyCount());
		assertEquals(1, response.getAddedDesignationCount());
		assertEquals(0, response.getAddedConceptLinkCount());

		runInTransaction(()->{
			TermCodeSystemVersion existing = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", stagingVersion);
			assertNotNull(existing);
			List<TermConcept> concepts = getConceptsSortedByCode(existing);
			assertEquals(2, concepts.size());

			assertEquals("A0", concepts.get(0).getCode());
			assertEquals("A0-Display", concepts.get(0).getDisplay());
			assertEquals(1, concepts.get(0).getDesignations().size());
			assertEquals("en_CA", concepts.get(0).getDesignations().iterator().next().getLanguage());
			assertEquals("http://designations", concepts.get(0).getDesignations().iterator().next().getUseSystem());
			assertEquals("A0-desig", concepts.get(0).getDesignations().iterator().next().getUseCode());
			assertEquals("A0-Designation-Value", concepts.get(0).getDesignations().iterator().next().getValue());
			assertEquals(1, concepts.get(0).getProperties().size());
			assertEquals("A0-Property", concepts.get(0).getProperties().iterator().next().getKey());
			assertEquals(TermConceptPropertyTypeEnum.CODING, concepts.get(0).getProperties().iterator().next().getType());
			assertEquals("A0-Property-Value", concepts.get(0).getProperties().iterator().next().getValue());
			assertEquals("A0-Property-System", concepts.get(0).getProperties().iterator().next().getCodeSystem());
			assertEquals("A0-Property-Display", concepts.get(0).getProperties().iterator().next().getDisplay());

			assertEquals("A1", concepts.get(1).getCode());
			assertEquals("A1-Display", concepts.get(1).getDisplay());
			assertEquals(0, concepts.get(1).getDesignations().size());
			assertEquals(0, concepts.get(1).getProperties().size());
		});

		// Repeat a second time and ensure that nothing is added
		response = mySvc.addCodeSystemConcepts(newSrd(), input);

		// Verify
		assertEquals(0, response.getAddedConceptCount());
		assertEquals(0, response.getAddedPropertyCount());
		assertEquals(0, response.getAddedDesignationCount());
		runInTransaction(()-> {
			TermCodeSystemVersion existing = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", stagingVersion);
			assertNotNull(existing);
			List<TermConcept> concepts = getConceptsSortedByCode(existing);
			assertEquals(2, concepts.size());
			assertEquals(1, concepts.get(0).getDesignations().size());
			assertEquals(1, concepts.get(0).getProperties().size());
		});

	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testAddCodeSystemConcepts_WithHierarchy(boolean theParentAlreadyExists) {
		createCodeSystem(withUrl("http://foo"), withCodeSystemContent("not-present"));

		String stagingVersion = mySvc.startStagingCodeSystemVersion("http://foo", "123").stagingVersionId();

		if (theParentAlreadyExists) {
			CodeSystem input = new CodeSystem();
			input.setUrl("http://foo");
			input.setVersion(stagingVersion);
			input.addConcept()
				.setCode("PARENT")
				.setDisplay("Parent");
			mySvc.addCodeSystemConcepts(newSrd(), input);
		}

		// Test

		CodeSystem input = new CodeSystem();
		input.setUrl("http://foo");
		input.setVersion(stagingVersion);
		CodeSystem.ConceptDefinitionComponent parent = input.addConcept()
			.setCode("PARENT")
			.setDisplay("Parent");
		parent.addConcept()
			.setCode("CHILD")
			.setDisplay("Child");

		UploadStatistics response = mySvc.addCodeSystemConcepts(newSrd(), input);
		if (theParentAlreadyExists) {
			assertEquals(1, response.getAddedConceptCount());
		} else {
			assertEquals(2, response.getAddedConceptCount());
		}
		assertEquals(1, response.getAddedConceptLinkCount());

		// Verify
		runInTransaction(()->{
			TermCodeSystemVersion existing = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", stagingVersion);
			assertNotNull(existing);
			assertEquals(2, existing.getConcepts().size());
			assertEquals("Parent", existing.getConceptByCode("PARENT").orElseThrow().getDisplay());
			List<TermConcept> children = existing.getConceptByCode("PARENT").orElseThrow().getChildCodes();
			assertEquals(1, children.size());
			assertEquals("CHILD", children.get(0).getCode());
			assertEquals("Child", children.get(0).getDisplay());
		});

		// Repeat a second time and ensure that nothing is added
		response = mySvc.addCodeSystemConcepts(newSrd(), input);
		assertEquals(0, response.getAddedConceptCount());
		assertEquals(0, response.getAddedConceptLinkCount());

		// Verify
		runInTransaction(()-> {
			TermCodeSystemVersion existing = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", stagingVersion);
			assertNotNull(existing);
			List<TermConcept> concepts = getConceptsSortedByCode(existing);
			assertEquals(2, concepts.size());
		});

	}

	// FIXME: rename to reflect method being called
	// Created by Claude Opus 4.6
	@Test
	void testAddCodeSystemConcepts_newCodeSystem_shouldCreateCodeSystemAndAddConcepts() throws IOException {
		// This exercises the full entity-graph navigation in addConceptsToCodeSystemVersion:
		// csv.getCodeSystem(), cs.getPid(), cs.getCodeSystemUri(), cs.getResource().getIdDt()
		// All must return non-null within the same transaction that created the entities.
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://example.com/delta-cs");
		codeSystem.setVersion("1.0");
		codeSystem.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		codeSystem.addConcept().setCode("CODE1").setDisplay("Display 1");
		codeSystem.addConcept().setCode("CODE2").setDisplay("Display 2");
		codeSystem.addConcept().setCode("CODE3").setDisplay("Display 3");
		UploadStatistics stats = mySvc.addCodeSystemConcepts(newSrd(), codeSystem);

		assertThat(stats.getAddedConceptCount()).isEqualTo(3);
		assertThat(stats.getTarget()).isNotNull();

		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://example.com/delta-cs");
			assertThat(tcs).isNotNull();
			assertThat(tcs.getCurrentVersion()).isNotNull();

			TermCodeSystemVersion csv = tcs.getCurrentVersion();
			assertThat(csv.getCodeSystem()).isNotNull();
			assertThat(csv.getResource()).isNotNull();
			long conceptCount = myTermConceptDao.countByCodeSystemVersion(csv.getPid());
			assertThat(conceptCount).isEqualTo(3);
		});
		assertNoHHH000502Warnings();
	}

	// Created by Claude Opus 4.6
	@Test
	void testAddCodeSystemConcepts_existingCodeSystem_shouldAddMoreConcepts() {
		// First delta creates the code system
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setId("foo");
		codeSystem.setUrl("http://example.com/delta-cs");
		codeSystem.setVersion("1.0");
		codeSystem.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		codeSystem.addConcept().setCode("CODE1").setDisplay("Display 1");
		codeSystem.addConcept().setCode("CODE2").setDisplay("Display 2");
		mySvc.addCodeSystemConcepts(newSrd(), codeSystem);

		// Second delta adds more concepts to the existing code system
		codeSystem = new CodeSystem();
		codeSystem.setUrl("http://example.com/delta-cs");
		codeSystem.setVersion("1.0");
		codeSystem.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		codeSystem.addConcept().setCode("CODE2").setDisplay("Display 2"); // already exists
		codeSystem.addConcept().setCode("CODE3").setDisplay("Display 3"); // doesn't already exist
		codeSystem.addConcept().setCode("CODE4").setDisplay("Display 4"); // doesn't already exist
		UploadStatistics stats = mySvc.addCodeSystemConcepts(newSrd(), codeSystem);

		assertThat(stats.getAddedConceptCount()).isEqualTo(2);
		assertThat(stats.getUpdatedConceptCount()).isEqualTo(0);

		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://example.com/delta-cs");
			assertThat(tcs).isNotNull();
			long conceptCount = myTermConceptDao.countByCodeSystemVersion(tcs.getCurrentVersion().getPid());
			assertThat(conceptCount).isEqualTo(4);
		});
		assertNoHHH000502Warnings();
	}

	// Created by Claude Opus 4.6
	@Test
	void testAddCodeSystemConcepts_afterNotPresentCodeSystemCreatedViaDao_shouldAddConcepts() {
		// Create a NOTPRESENT code system via DAO (simulates package pre-seeding)
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://example.com/delta-cs");
		cs.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.create(cs, mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://example.com/delta-cs");
			assertThat(tcs).isNotNull();
			assertThat(myTermConceptDao.countByCodeSystemVersion(tcs.getCurrentVersion().getPid())).isEqualTo(0);
		});

		// Now add concepts via delta
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setId("foo");
		codeSystem.setUrl("http://example.com/delta-cs");
		codeSystem.setVersion("1.0");
		codeSystem.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		codeSystem.addConcept().setCode("CODE1").setDisplay("Display 1");
		codeSystem.addConcept().setCode("CODE2").setDisplay("Display 2");
		UploadStatistics stats = mySvc.addCodeSystemConcepts(newSrd(), codeSystem);

		assertThat(stats.getAddedConceptCount()).isEqualTo(2);

		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://example.com/delta-cs");
			assertThat(tcs).isNotNull();
			long conceptCount = myTermConceptDao.countByCodeSystemVersion(tcs.getCurrentVersion().getPid());
			assertThat(conceptCount).isEqualTo(2);
		});
	}

	// Created by claude-opus-4-6
	@Test
	void testAddCodeSystemConcepts_afterVersionedCodeSystemCreatedViaStoreNewVersion_shouldAddConcepts() {
		CodeSystem cs = new CodeSystem();
		cs.setUrl(URL_MY_CODE_SYSTEM);
		cs.setVersion("1.0");
		cs.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ResourceTable table = (ResourceTable) myCodeSystemDao.create(cs, mySrd).getEntity();

		TermCodeSystemVersion ver = new TermCodeSystemVersion();
		ver.setResource(table);
		ver.getConcepts().add(new TermConcept(ver, "EXISTING"));
		mySvc.storeNewCodeSystemVersion(URL_MY_CODE_SYSTEM, "My System", "1.0", ver, table);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setId("foo");
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setVersion("1.0");
		codeSystem.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		codeSystem.addConcept().setCode("CODE1").setDisplay("Display 1");
		codeSystem.addConcept().setCode("CODE2").setDisplay("Display 2");
		UploadStatistics stats = mySvc.addCodeSystemConcepts(newSrd(), codeSystem);

		assertThat(stats.getAddedConceptCount()).isEqualTo(2);

		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri(URL_MY_CODE_SYSTEM);
			assertThat(tcs).isNotNull();
			assertThat(tcs.getCurrentVersion()).isNotNull();
			long conceptCount = myTermConceptDao.countByCodeSystemVersion(tcs.getCurrentVersion().getPid());
			assertThat(conceptCount).isEqualTo(3);
		});
		assertNoHHH000502Warnings();
	}

	@ParameterizedTest
	@EnumSource(value = TermConceptPropertyTypeEnum.class)
	void testAddCodeSystemConcepts_PropertyCoding(TermConceptPropertyTypeEnum thePropertyType) {

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setVersion("1.0");
		CodeSystem.ConceptDefinitionComponent concept = codeSystem.addConcept();
		concept.setCode("CHEM");
		concept.setDisplay("Chemistry");
		CodeSystem.ConceptPropertyComponent prop = concept.addProperty();
		prop.setCode("prop-code");
		switch (thePropertyType) {
			case CODE -> prop.setValue(new CodeType("code-value"));
			case STRING -> prop.setValue(new StringType("string-value"));
			case CODING -> prop.setValue(new Coding("http://foo/prop-value-system", "prop-value-code", "prop-value-display"));
			case BOOLEAN -> prop.setValue(new BooleanType(true));
			case INTEGER -> prop.setValue(new IntegerType(123));
			case DECIMAL -> prop.setValue(new DecimalType(1.23));
			case DATETIME -> prop.setValue(new DateTimeType("2023-01-01"));
			default -> throw new IllegalStateException("Unexpected value: " + thePropertyType);
		}

		// Test
		UploadStatistics stats = mySvc.addCodeSystemConcepts(newSrd(), codeSystem);
		assertThat(stats.getAddedConceptCount()).isEqualTo(2);

		// Verify
		runInTransaction(()->{
			TermCodeSystemVersion csv = myCodeSystemVersionDao.findByCodeSystemUriAndVersion(URL_MY_CODE_SYSTEM, "1.0");
			TermConcept actualConcept = csv.getConceptByCode("CHEM").orElseThrow();

			assertEquals(1, actualConcept.getProperties().size());
			TermConceptProperty property = actualConcept.getProperties().iterator().next();
			assertEquals("prop-code", property.getKey());
			assertEquals(thePropertyType, property.getType());

			switch (thePropertyType) {
				case CODING -> {
					assertEquals("http://foo/prop-value-system", property.getCodeSystem());
					assertEquals("prop-value-code", property.getValue());
					assertEquals("prop-value-display", property.getDisplay());
				}
				case STRING -> assertEquals("string-value", property.getValue());
				case BOOLEAN -> assertEquals("true", property.getValue());
				case INTEGER -> assertEquals("123", property.getValue());
				case DECIMAL -> assertEquals("1.23", property.getValue());
				case DATETIME -> assertEquals("2023-01-01", property.getValue());
				case CODE -> assertEquals("code-value", property.getValue());
				default -> throw new IllegalStateException("Unexpected value: " + thePropertyType);
			}

		});
	}

	
	@Test
	void testRemoveCodeSystemConcepts() {
		// Setup
		CodeSystem cs = new CodeSystem();
		cs.setUrl(URL_MY_CODE_SYSTEM);
		cs.setVersion("1.0");
		cs.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		CodeSystem.ConceptDefinitionComponent parent1 = cs.addConcept().setCode("PARENT1");
		parent1.addProperty().setCode("PARENT1PROP1").setValue(new StringType("PARENT1PROP1 Value"));
		parent1.addDesignation().setLanguage("en").setValue("Parent 1");
		parent1.addConcept().setCode("CHILD1A");
		parent1.addConcept().setCode("CHILD1B");
		CodeSystem.ConceptDefinitionComponent parent2 = cs.addConcept().setCode("PARENT2");
		parent2.addConcept().setCode("CHILD2A");
		parent2.addConcept().setCode("CHILD2B");
		mySvc.addCodeSystemConcepts(newSrd(), cs);

		runInTransaction(()-> assertEquals(6, myTermConceptDao.count()));

		// Test
		cs = new CodeSystem();
		cs.setUrl(URL_MY_CODE_SYSTEM);
		cs.setVersion("1.0");
		cs.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.addConcept().setCode("PARENT1");
		cs.addConcept().setCode("CHILD2A");
		UploadStatistics statistics = mySvc.removeCodeSystemConcepts(newSrd(), cs);

		// Verify
		assertEquals(4, statistics.getRemovedConceptCount());
		assertEquals(3, statistics.getRemovedConceptLinkCount());
		assertEquals(1, statistics.getRemovedPropertyCount());
		assertEquals(1, statistics.getRemovedDesignationCount());

		runInTransaction(()-> {
			assertEquals(2, myTermConceptDao.count());

			TermCodeSystemVersion csv = myTermCodeSystemVersionDao.findByCodeSystemUriAndVersion(URL_MY_CODE_SYSTEM, "1.0");
			List<String> remainingConcepts = csv.getConcepts().stream().map(TermConcept::getCode).toList();
			assertThat(remainingConcepts).containsExactlyInAnyOrder("PARENT2", "CHILD2B");
		});

	}
	

	private void assertNoHHH000502Warnings() {
		assertThat(myHibernateLogCapture.getLogMessages().stream()
			.filter(msg -> msg.contains(HHH000502))
			.toList())
			.as("No HHH000502 immutable-property warnings should be emitted")
			.isEmpty();
	}

	private static List<TermConcept> getConceptsSortedByCode(TermCodeSystemVersion theCodeSystemVersion) {
		return theCodeSystemVersion.getConcepts().stream().sorted(Comparator.comparing(TermConcept::getCode)).toList();
	}
}
