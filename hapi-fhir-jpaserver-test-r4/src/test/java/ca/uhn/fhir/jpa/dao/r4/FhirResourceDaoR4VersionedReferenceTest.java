package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.ClasspathUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.annotation.Testable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.HapiExtensions.EXTENSION_AUTO_VERSION_REFERENCES_AT_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class FhirResourceDaoR4VersionedReferenceTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4VersionedReferenceTest.class);

	@AfterEach
	public void afterEach() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(true);
		myFhirContext.getParserOptions().getDontStripVersionsFromReferencesAtPaths().clear();
		myStorageSettings.setDeleteEnabled(new JpaStorageSettings().isDeleteEnabled());
		myStorageSettings.setRespectVersionsForSearchIncludes(new JpaStorageSettings().isRespectVersionsForSearchIncludes());
		myStorageSettings.setAutoVersionReferenceAtPaths(new JpaStorageSettings().getAutoVersionReferenceAtPaths());
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(new JpaStorageSettings().isAutoCreatePlaceholderReferenceTargets());
		myStorageSettings.setResourceClientIdStrategy(new JpaStorageSettings().getResourceClientIdStrategy());
	}

	@Nested
	public class AutoVersionReferencesWithSettingAndExtension extends AutoVersionReferencesWithExtension {
		@Override
		@BeforeEach
		public void before() {
			super.before();
			beforeAutoVersionReferencesWithSetting();
			initResourceTypeCacheFromConfig();
		}
	}

	@Nested
	public class AutoVersionReferencesWithSetting extends AutoVersionReferencesTestCases {
		@BeforeEach
		public void before() {
			beforeAutoVersionReferencesWithSetting();
			initResourceTypeCacheFromConfig();
		}
	}

	private void beforeAutoVersionReferencesWithSetting() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myStorageSettings.setAutoVersionReferenceAtPaths(
			"Patient.managingOrganization",
			"ExplanationOfBenefit.patient",
			"Observation.subject",
			"MessageHeader.focus"
		);
	}

	@Nested
	public class AutoVersionReferencesWithExtension extends AutoVersionReferencesTestCases {
		@BeforeEach
		public void before() {
			patientAutoVersionExtension = createAutoVersionReferencesExtensions("managingOrganization");
			observationAutoVersionExtension = createAutoVersionReferencesExtensions("subject");
			explanationOfBenefitAutoVersionExtension = createAutoVersionReferencesExtensions("patient");
			messageHeaderAutoVersionExtension = createAutoVersionReferencesExtensions("focus");
			initResourceTypeCacheFromConfig();
		}

		@Nonnull
		private List<Extension> createAutoVersionReferencesExtensions(String... thePaths) {
			return Arrays.stream(thePaths)
				.map(path -> new Extension(EXTENSION_AUTO_VERSION_REFERENCES_AT_PATH, new StringType(path)))
				.collect(Collectors.toList());
		}
	}

	@Testable
	public abstract class AutoVersionReferencesTestCases {

		protected List<Extension> patientAutoVersionExtension = Collections.emptyList();
		protected List<Extension> observationAutoVersionExtension = Collections.emptyList();
		protected List<Extension> explanationOfBenefitAutoVersionExtension = Collections.emptyList();
		protected List<Extension> messageHeaderAutoVersionExtension = Collections.emptyList();

		@Test
		public void testCreateAndUpdateVersionedReferencesInTransaction_VersionedReferenceToUpsertWithNoOp() {
			// We'll submit the same bundle twice. It has an UPSERT (with no changes
			// the second time) on a Patient, and a CREATE on an ExplanationOfBenefit
			// referencing that Patient.
			Supplier<Bundle> supplier = () -> {
				BundleBuilder bb = new BundleBuilder(myFhirContext);

				Patient patient = new Patient();
				patient.setId("Patient/A");
				patient.setActive(true);
				bb.addTransactionUpdateEntry(patient);

				ExplanationOfBenefit eob = new ExplanationOfBenefit();
				eob.getMeta().setExtension(explanationOfBenefitAutoVersionExtension);
				eob.setId(IdType.newRandomUuid());
				eob.setPatient(new Reference("Patient/A"));
				bb.addTransactionCreateEntry(eob);

				return (Bundle) bb.getBundle();
			};

			// Send it the first time
			Bundle outcome1 = mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
			assertEquals("Patient/A/_history/1", outcome1.getEntry().get(0).getResponse().getLocation());
			String eobId1 = outcome1.getEntry().get(1).getResponse().getLocation();
			assertThat(eobId1).matches("ExplanationOfBenefit/[0-9]+/_history/1");

			ExplanationOfBenefit eob1 = myExplanationOfBenefitDao.read(new IdType(eobId1), new SystemRequestDetails());
			assertEquals("Patient/A/_history/1", eob1.getPatient().getReference());

			// Send it again
			Bundle outcome2 = mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
			assertEquals("Patient/A/_history/1", outcome2.getEntry().get(0).getResponse().getLocation());
			String eobId2 = outcome2.getEntry().get(1).getResponse().getLocation();
			assertThat(eobId2).matches("ExplanationOfBenefit/[0-9]+/_history/1");

			ExplanationOfBenefit eob2 = myExplanationOfBenefitDao.read(new IdType(eobId2), new SystemRequestDetails());
			assertEquals("Patient/A/_history/1", eob2.getPatient().getReference());
		}

		@Test
		public void testCreateAndUpdateVersionedReferencesInTransaction_VersionedReferenceToVersionedReferenceToUpsertWithNoOp() {
			// We'll submit the same bundle twice. It has an UPSERT (with no changes
			// the second time) on a Patient, and a CREATE on an ExplanationOfBenefit
			// referencing that Patient.
			Supplier<Bundle> supplier = () -> {
				BundleBuilder bb = new BundleBuilder(myFhirContext);

				Organization organization = new Organization();
				organization.setId("Organization/O");
				organization.setActive(true);
				bb.addTransactionUpdateEntry(organization);

				Patient patient = new Patient();
				patient.getMeta().setExtension(patientAutoVersionExtension);
				patient.setId("Patient/A");
				patient.setManagingOrganization(new Reference("Organization/O"));
				patient.setActive(true);
				bb.addTransactionUpdateEntry(patient);

				ExplanationOfBenefit eob = new ExplanationOfBenefit();
				eob.getMeta().setExtension(explanationOfBenefitAutoVersionExtension);
				eob.setId(IdType.newRandomUuid());
				eob.setPatient(new Reference("Patient/A"));
				bb.addTransactionCreateEntry(eob);

				return (Bundle) bb.getBundle();
			};

			// Send it the first time
			Bundle outcome1 = mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
			assertEquals("Organization/O/_history/1", outcome1.getEntry().get(0).getResponse().getLocation());
			assertEquals("Patient/A/_history/1", outcome1.getEntry().get(1).getResponse().getLocation());
			String eobId1 = outcome1.getEntry().get(2).getResponse().getLocation();
			assertThat(eobId1).matches("ExplanationOfBenefit/[0-9]+/_history/1");

			ExplanationOfBenefit eob1 = myExplanationOfBenefitDao.read(new IdType(eobId1), new SystemRequestDetails());
			assertEquals("Patient/A/_history/1", eob1.getPatient().getReference());

			// Send it again
			Bundle outcome2 = mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
			assertEquals("Organization/O/_history/1", outcome2.getEntry().get(0).getResponse().getLocation());
			// Technically the patient did not change - If this ever got optimized so that the version here
			// was 1 that would be even better
			String patientId = outcome2.getEntry().get(1).getResponse().getLocation();
			assertEquals("Patient/A/_history/2", patientId);
			String eobId2 = outcome2.getEntry().get(2).getResponse().getLocation();
			assertThat(eobId2).matches("ExplanationOfBenefit/[0-9]+/_history/1");

			Patient patient = myPatientDao.read(new IdType("Patient/A"), new SystemRequestDetails());
			assertEquals(patientId, patient.getId());

			ExplanationOfBenefit eob2 = myExplanationOfBenefitDao.read(new IdType(eobId2), new SystemRequestDetails());
			assertEquals(patientId, eob2.getPatient().getReference());
		}

		@Test
		public void testCreateAndUpdateVersionedReferencesInTransaction_VersionedReferenceToVersionedReferenceToUpsertWithChange() {
			final AtomicInteger counter = new AtomicInteger();
			Supplier<Bundle> supplier = () -> {
				BundleBuilder bb = new BundleBuilder(myFhirContext);

				Organization organization = new Organization();
				organization.setId("Organization/O");
				organization.setName("Org " + counter.incrementAndGet()); // change each time
				organization.setActive(true);
				bb.addTransactionUpdateEntry(organization);

				Patient patient1 = new Patient();
				patient1.getMeta().setExtension(patientAutoVersionExtension);
				patient1.setId("Patient/A");
				patient1.setManagingOrganization(new Reference("Organization/O"));
				patient1.setActive(true);
				bb.addTransactionUpdateEntry(patient1);

				ExplanationOfBenefit eob = new ExplanationOfBenefit();
				eob.getMeta().setExtension(explanationOfBenefitAutoVersionExtension);
				eob.setId(IdType.newRandomUuid());
				eob.setPatient(new Reference("Patient/A"));
				bb.addTransactionCreateEntry(eob);

				return (Bundle) bb.getBundle();
			};

			// Send it the first time
			Bundle outcome1 = mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
			assertEquals("Organization/O/_history/1", outcome1.getEntry().get(0).getResponse().getLocation());
			assertEquals("Patient/A/_history/1", outcome1.getEntry().get(1).getResponse().getLocation());
			String eobId1 = outcome1.getEntry().get(2).getResponse().getLocation();
			assertThat(eobId1).matches("ExplanationOfBenefit/[0-9]+/_history/1");

			ExplanationOfBenefit eob1 = myExplanationOfBenefitDao.read(new IdType(eobId1), new SystemRequestDetails());
			assertEquals("Patient/A/_history/1", eob1.getPatient().getReference());

			// Send it again
			Bundle outcome2 = mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
			assertEquals("Organization/O/_history/2", outcome2.getEntry().get(0).getResponse().getLocation());
			String patientId = outcome2.getEntry().get(1).getResponse().getLocation();
			assertEquals("Patient/A/_history/2", patientId);
			String eobId2 = outcome2.getEntry().get(2).getResponse().getLocation();
			assertThat(eobId2).matches("ExplanationOfBenefit/[0-9]+/_history/1");

			Patient patient = myPatientDao.read(new IdType("Patient/A"), new SystemRequestDetails());
			assertEquals(patientId, patient.getId());

			ExplanationOfBenefit eob2 = myExplanationOfBenefitDao.read(new IdType(eobId2), new SystemRequestDetails());
			assertEquals(patientId, eob2.getPatient().getReference());
		}

		@Test
		public void testInsertVersionedReferenceAtPath() {
			Patient p = new Patient();
			p.setActive(true);
			IIdType patientId = myPatientDao.create(p, mySrd).getId().toUnqualified();
			assertEquals("1", patientId.getVersionIdPart());
			assertNull(patientId.getBaseUrl());
			String patientIdString = patientId.getValue();

			// Create - put an unversioned reference in the subject
			Observation observation = new Observation();
			observation.getMeta().setExtension(observationAutoVersionExtension);
			observation.getSubject().setReference(patientId.toVersionless().getValue());
			IIdType observationId = myObservationDao.create(observation, mySrd).getId().toUnqualified();

			// Read back and verify that reference is now versioned
			observation = myObservationDao.read(observationId, mySrd);
			assertEquals(patientIdString, observation.getSubject().getReference());

			myCaptureQueriesListener.clear();

			// Update - put an unversioned reference in the subject
			observation = new Observation();
			observation.getMeta().setExtension(observationAutoVersionExtension);
			observation.setId(observationId);
			observation.addIdentifier().setSystem("http://foo").setValue("bar");
			observation.getSubject().setReference(patientId.toVersionless().getValue());
			myObservationDao.update(observation, mySrd);

			// Make sure we're not introducing any extra DB operations
			assertThat(myCaptureQueriesListener.logSelectQueries()).hasSize(4);

			// Read back and verify that reference is now versioned
			observation = myObservationDao.read(observationId, mySrd);
			assertEquals(patientIdString, observation.getSubject().getReference());
		}


		@Test
		public void testInsertVersionedReferenceAtPath_InTransaction_SourceAndTargetBothCreated() {
			BundleBuilder builder = new BundleBuilder(myFhirContext);

			Patient patient = new Patient();
			patient.setId(IdType.newRandomUuid());
			patient.setActive(true);
			builder.addTransactionCreateEntry(patient);

			Encounter encounter = new Encounter();
			encounter.setId(IdType.newRandomUuid());
			encounter.addIdentifier().setSystem("http://baz").setValue("baz");
			builder.addTransactionCreateEntry(encounter);

			Observation observation = new Observation();
			observation.getMeta().setExtension(observationAutoVersionExtension);
			observation.getSubject().setReference(patient.getId()); // versioned
			observation.getEncounter().setReference(encounter.getId()); // not versioned
			builder.addTransactionCreateEntry(observation);

			Bundle outcome = mySystemDao.transaction(mySrd, (Bundle) builder.getBundle());
			ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
			IdType patientId = new IdType(outcome.getEntry().get(0).getResponse().getLocation());
			IdType encounterId = new IdType(outcome.getEntry().get(1).getResponse().getLocation());
			IdType observationId = new IdType(outcome.getEntry().get(2).getResponse().getLocation());
			assertTrue(patientId.hasVersionIdPart());
			assertTrue(encounterId.hasVersionIdPart());
			assertTrue(observationId.hasVersionIdPart());

			// Read back and verify that reference is now versioned
			observation = myObservationDao.read(observationId, mySrd);
			assertEquals(patientId.getValue(), observation.getSubject().getReference());
			assertEquals(encounterId.toVersionless().getValue(), observation.getEncounter().getReference());
		}


		@Test
		public void testInsertVersionedReferenceAtPath_InTransaction_TargetConditionalCreatedNoOp() {
			{
				// Create patient
				createAndUpdatePatient(IdType.newRandomUuid().getId());

				// Create encounter
				Encounter encounter = new Encounter();
				encounter.setId(IdType.newRandomUuid());
				encounter.addIdentifier().setSystem("http://baz").setValue("baz");
				myEncounterDao.create(encounter, mySrd);
			}

			// Verify Patient Version
			assertThat(myPatientDao.search(SearchParameterMap.newSynchronous("active", new TokenParam("false")), mySrd)
				.getResources(0, 1).get(0).getIdElement().getVersionIdPart()).isEqualTo("2");

			BundleBuilder builder = new BundleBuilder(myFhirContext);

			Patient patient = new Patient();
			patient.setId(IdType.newRandomUuid());
			patient.setActive(true);
			builder.addTransactionCreateEntry(patient).conditional("Patient?active=false");

			Encounter encounter = new Encounter();
			encounter.setId(IdType.newRandomUuid());
			encounter.addIdentifier().setSystem("http://baz").setValue("baz");
			builder.addTransactionCreateEntry(encounter).conditional("Encounter?identifier=http://baz|baz");

			Observation observation = new Observation();
			observation.getMeta().setExtension(observationAutoVersionExtension);
			observation.getSubject().setReference(patient.getId()); // versioned
			observation.getEncounter().setReference(encounter.getId()); // not versioned
			builder.addTransactionCreateEntry(observation);

			Bundle outcome = createAndValidateBundle((Bundle) builder.getBundle(),
				List.of("200 OK", "200 OK", "201 Created"), List.of("2", "1", "1"));
			IdType patientId = new IdType(outcome.getEntry().get(0).getResponse().getLocation());
			IdType encounterId = new IdType(outcome.getEntry().get(1).getResponse().getLocation());
			IdType observationId = new IdType(outcome.getEntry().get(2).getResponse().getLocation());

			// Read back and verify that reference is now versioned
			observation = myObservationDao.read(observationId, mySrd);
			assertEquals(patientId.getValue(), observation.getSubject().getReference());
			assertEquals("2", observation.getSubject().getReferenceElement().getVersionIdPart());
			assertEquals(encounterId.toVersionless().getValue(), observation.getEncounter().getReference());
		}


		@Test
		public void testInsertVersionedReferenceAtPath_InTransaction_TargetUpdate() {
			myStorageSettings.setDeleteEnabled(false);

			{
				// Create patient
				Patient patient = new Patient();
				patient.setId("PATIENT");
				patient.setActive(true);
				myPatientDao.update(patient, mySrd);

				// Update patient to make a second version
				patient.setActive(false);
				myPatientDao.update(patient, mySrd);
			}

			BundleBuilder builder = new BundleBuilder(myFhirContext);

			Patient patient = new Patient();
			patient.setId("Patient/PATIENT");
			patient.setActive(true);
			builder.addTransactionUpdateEntry(patient);

			Observation observation = new Observation();
			observation.getMeta().setExtension(observationAutoVersionExtension);
			observation.getSubject().setReference(patient.getId()); // versioned
			builder.addTransactionCreateEntry(observation);

			myCaptureQueriesListener.clear();
			Bundle outcome = createAndValidateBundle((Bundle) builder.getBundle(),
				List.of("200 OK", "201 Created"), List.of("3", "1"));
			IdType patientId = new IdType(outcome.getEntry().get(0).getResponse().getLocation());
			IdType observationId = new IdType(outcome.getEntry().get(1).getResponse().getLocation());

			// Make sure we're not introducing any extra DB operations
			assertThat(myCaptureQueriesListener.logSelectQueries()).hasSize(2);

			// Read back and verify that reference is now versioned
			observation = myObservationDao.read(observationId, mySrd);
			assertEquals(patientId.getValue(), observation.getSubject().getReference());

		}

		@Test
		public void testInsertVersionedReferenceAtPath_InTransaction_TargetUpdateConditional() {
			createAndUpdatePatient(IdType.newRandomUuid().getId());

			BundleBuilder builder = new BundleBuilder(myFhirContext);

			Patient patient = new Patient();
			patient.setId(IdType.newRandomUuid());
			patient.setDeceased(new BooleanType(true));
			patient.setActive(false);
			builder
				.addTransactionUpdateEntry(patient)
				.conditional("Patient?active=false");

			Observation observation = new Observation();
			observation.getMeta().setExtension(observationAutoVersionExtension);
			observation.getSubject().setReference(patient.getId()); // versioned
			builder.addTransactionCreateEntry(observation);

			myCaptureQueriesListener.clear();

			Bundle outcome = createAndValidateBundle((Bundle) builder.getBundle(),
				List.of("200 OK", "201 Created"), List.of("3", "1"));
			IdType patientId = new IdType(outcome.getEntry().get(0).getResponse().getLocation());
			IdType observationId = new IdType(outcome.getEntry().get(1).getResponse().getLocation());

			// Make sure we're not introducing any extra DB operations
			assertThat(myCaptureQueriesListener.logSelectQueries()).hasSize(3);

			// Read back and verify that reference is now versioned
			observation = myObservationDao.read(observationId, mySrd);
			assertEquals(patientId.getValue(), observation.getSubject().getReference());
		}

		@Test
		@DisplayName("Bundle transaction with AutoVersionReferenceAtPath on and with existing Patient resource should create")
		public void bundleTransaction_autoVersionReferenceAtPathWithPreexistingPatientReference_shouldCreate() {
			String patientId = "Patient/RED";
			IIdType idType = new IdDt(patientId);

			// create patient ahead of time
			Patient patient = new Patient();
			patient.setId(patientId);
			DaoMethodOutcome outcome = myPatientDao.update(patient, mySrd);
			assertEquals(patientId + "/_history/1", outcome.getResource().getIdElement().getValue());

			Patient returned = myPatientDao.read(idType, mySrd);
			assertNotNull(returned);
			assertEquals(patientId + "/_history/1", returned.getId());

			// update to change version
			patient.setActive(true);
			myPatientDao.update(patient, mySrd);

			Observation obs = new Observation();
			obs.getMeta().setExtension(observationAutoVersionExtension);
			obs.setId("Observation/DEF");
			Reference patientRef = new Reference(patientId);
			obs.setSubject(patientRef);
			BundleBuilder builder = new BundleBuilder(myFhirContext);
			builder.addTransactionUpdateEntry(obs);

			Bundle submitted = (Bundle) builder.getBundle();

			Bundle returnedTr = mySystemDao.transaction(new SystemRequestDetails(), submitted);

			assertNotNull(returnedTr);

			// some verification
			Observation obRet = myObservationDao.read(obs.getIdElement(), mySrd);
			assertNotNull(obRet);
		}

		@Test
		@DisplayName("GH-2901 Test no NPE is thrown on autoversioned references")
		public void testNoNpeMinimal() {
			myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

			Observation obs = new Observation();
			obs.getMeta().setExtension(observationAutoVersionExtension);
			obs.setId("Observation/DEF");
			Reference patientRef = new Reference("Patient/RED");
			obs.setSubject(patientRef);
			BundleBuilder builder = new BundleBuilder(myFhirContext);
			builder.addTransactionUpdateEntry(obs);

			Bundle submitted = (Bundle) builder.getBundle();

			Bundle returnedTr = mySystemDao.transaction(new SystemRequestDetails(), submitted);

			assertNotNull(returnedTr);

			// some verification
			Observation obRet = myObservationDao.read(obs.getIdElement(), mySrd);
			assertNotNull(obRet);
			Patient returned = myPatientDao.read(patientRef.getReferenceElement(), mySrd);
			assertNotNull(returned);
		}

		@Test
		public void testInsertVersionedReferencesByPath_resourceReferenceNotInTransaction_addsVersionToTheReferences() {
			Patient patient = createAndUpdatePatient(IdType.newRandomUuid().getId());

			// create MessageHeader
			MessageHeader messageHeader = new MessageHeader();
			messageHeader.getMeta().setExtension(messageHeaderAutoVersionExtension);
			// add reference
			messageHeader.addFocus().setReference(patient.getIdElement().toVersionless().getValue());

			BundleBuilder builder = new BundleBuilder(myFhirContext);
			builder.addTransactionCreateEntry(messageHeader);

			Bundle outcome = createAndValidateBundle((Bundle) builder.getBundle(),
				List.of("201 Created"), List.of("1"));
			IdType messageHeaderId = new IdType(outcome.getEntry().get(0).getResponse().getLocation());
			assertEquals("2", patient.getIdElement().getVersionIdPart());

			// read back and verify that reference is versioned
			messageHeader = myMessageHeaderDao.read(messageHeaderId, mySrd);
			assertEquals(patient.getIdElement().getValue(), messageHeader.getFocus().get(0).getReference());
		}

		@Test
		@DisplayName("#5619 Incorrect version of auto versioned reference for conditional update with urn id placeholder")
		public void testInsertVersionedReferencesByPath_conditionalUpdateNoOpInTransaction_addsCorrectVersionToReference() {
			Supplier<Bundle> supplier = () -> {
				//  create patient
				Patient patient = new Patient();
				patient.setActive(true);
				patient.addIdentifier().setSystem("http://example.com").setValue("test");

				// add patient to the Bundle - conditional update with placeholder url
				Bundle bundle = new Bundle();
				bundle.setType(Bundle.BundleType.TRANSACTION);
				bundle.addEntry()
					.setResource(patient)
					.setFullUrl("urn:uuid:00001")
					.getRequest()
					.setMethod(Bundle.HTTPVerb.PUT)
					.setUrl("Patient?identifier=http://example.com|test");

				// create MessageHeader
				MessageHeader messageHeader = new MessageHeader();
				messageHeader.getMeta().setExtension(messageHeaderAutoVersionExtension);
				// add reference
				messageHeader.addFocus().setReference("urn:uuid:00001");

				bundle.addEntry()
					.setResource(messageHeader)
					.getRequest()
					.setMethod(Bundle.HTTPVerb.POST)
					.setUrl("/MessageHeader");

				return bundle;
			};

			// create bundle first time
			Bundle outcome = createAndValidateBundle(supplier.get(),
				List.of("201 Created", "201 Created"), List.of("1", "1"));
			IdType patientId = new IdType(outcome.getEntry().get(0).getResponse().getLocation());
			IdType messageHeaderId = new IdType(outcome.getEntry().get(1).getResponse().getLocation());

			// read back and verify that reference is versioned and correct
			Patient patient = myPatientDao.read(patientId, mySrd);
			MessageHeader messageHeader = myMessageHeaderDao.read(messageHeaderId, mySrd);
			assertEquals(patient.getIdElement().getValue(), messageHeader.getFocus().get(0).getReference());

			// create bundle second time
			outcome = createAndValidateBundle(supplier.get(), List.of("200 OK", "201 Created"), List.of("1", "1"));
			patientId = new IdType(outcome.getEntry().get(0).getResponse().getLocation());
			messageHeaderId = new IdType(outcome.getEntry().get(1).getResponse().getLocation());

			// read back and verify that reference is versioned and correct
			patient = myPatientDao.read(patientId, mySrd);
			messageHeader = myMessageHeaderDao.read(messageHeaderId, mySrd);
			assertEquals(patient.getIdElement().getValue(), messageHeader.getFocus().get(0).getReference());
		}

		private Bundle createAndValidateBundle(Bundle theBundle, List<String> theOutcomeStatuses,
											   List<String> theOutcomeVersions) {
			assertEquals(theBundle.getEntry().size(), theOutcomeStatuses.size(),
				"Size of OutcomeStatuses list is incorrect");
			assertEquals(theBundle.getEntry().size(), theOutcomeVersions.size(),
				"Size of OutcomeVersions list is incorrect");

			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(theBundle));
			Bundle result = mySystemDao.transaction(mySrd, theBundle);
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(theBundle));

			for (int i = 0; i < result.getEntry().size(); i++) {
				assertEquals(theOutcomeStatuses.get(i), result.getEntry().get(i).getResponse().getStatus());
				IIdType resultId = new IdType(result.getEntry().get(i).getResponse().getLocation());
				assertEquals(theOutcomeVersions.get(i), resultId.getVersionIdPart());
			}
			return result;
		}

		private Patient createAndUpdatePatient(String thePatientId) {
			Patient patient = new Patient();
			patient.setId(thePatientId);
			patient.setActive(true);
			myPatientDao.create(patient, mySrd);

			// update patient to make a second version
			patient.setActive(false);
			myPatientDao.update(patient, mySrd);
			return patient;
		}
	}

	@Test
	public void testStoreAndRetrieveVersionedReference() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);

		Patient p = new Patient();
		p.setActive(true);
		IIdType patientId = myPatientDao.create(p, mySrd).getId().toUnqualified();
		assertEquals("1", patientId.getVersionIdPart());
		assertNull(patientId.getBaseUrl());
		String patientIdString = patientId.getValue();

		Observation observation = new Observation();
		observation.getSubject().setReference(patientIdString);
		IIdType observationId = myObservationDao.create(observation, mySrd).getId().toUnqualified();

		// Read back
		observation = myObservationDao.read(observationId, mySrd);
		assertEquals(patientIdString, observation.getSubject().getReference());
	}

	@Test
	public void testDontStripVersionsFromRefsAtPaths_inTransactionBundle_shouldPreserveVersion() throws IOException {
		Set<String> referencePaths = Set.of("AuditEvent.entity.what","MessageHeader.focus","Provenance.target","Provenance.entity.what");

		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths(referencePaths);
		myStorageSettings.setAutoVersionReferenceAtPaths("Encounter.subject");
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);

		String encounterId = Integer.toString((int)(Math.random() * (float)Integer.MAX_VALUE));
		postBundleAndAssertProvenanceRefsPreserved("/transaction-bundles/transaction-with-client-supplied-versioned-reference.json", encounterId);
	}

	@Test
	public void testDontStripVersionsFromRefsAtAllPaths_inTransactionBundle_shouldPreserveVersion() throws IOException {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myStorageSettings.setAutoVersionReferenceAtPaths("Encounter.subject");
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);

		String encounterId = Integer.toString((int)(Math.random() * (float)Integer.MAX_VALUE));
		postBundleAndAssertProvenanceRefsPreserved("/transaction-bundles/transaction-with-client-supplied-versioned-reference.json", encounterId);
	}

	@Test
	public void testDontStripVersionsFromRefsAtPaths_withTransactionBundleAndAutoVersionSet_shouldPreserveVersion() throws Exception {
		Set<String> referencePaths = Set.of("AuditEvent.entity.what","MessageHeader.focus","Provenance.target","Provenance.entity.what");

		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths(referencePaths);
		myStorageSettings.setAutoVersionReferenceAtPaths("Provenance.entity.what");
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);

		String encounterId = Integer.toString((int)(Math.random() * (float)Integer.MAX_VALUE));

		// TODO: JA2 237643 Remove this and this test should still pass
		Patient pt = new Patient();
		pt.setId("237643");
		myPatientDao.update(pt, mySrd);

		Encounter encounter = new Encounter();
		encounter.setId(encounterId);
		myEncounterDao.update(encounter);

		logAllResources();

		myCaptureQueriesListener.clear();
		try {
			postBundleAndAssertProvenanceRefsPreserved("/transaction-bundles/transaction-with-client-supplied-versioned-reference.json", encounterId);
			myCaptureQueriesListener.logInsertQueries();
		} catch (Exception e) {
			String sql = myCaptureQueriesListener.getInsertQueries().stream().map(t->t.getSql(true, false)).collect(Collectors.joining("\n\n"));
			throw new Exception(sql, e);
		}

		logAllResources();
	}

	private Provenance postBundleAndAssertProvenanceRefsPreserved(String theBundlePath, String theEncounterId) throws IOException {
		String bundleString = ClasspathUtil.loadResource(theBundlePath);
		bundleString = bundleString.replace("ENCOUNTER_ID", theEncounterId);
		Bundle bundle = myFhirContext.newJsonParser().parseResource(Bundle.class, bundleString);

		Bundle transaction = mySystemDao.transaction(new SystemRequestDetails(), bundle);

		Optional<Bundle.BundleEntryComponent> provenanceEntry = transaction.getEntry().stream().filter(entry -> entry.getResponse().getLocation().contains("Provenance")).findFirst();
		assertThat(provenanceEntry).isPresent();
		String provenanceLocation = provenanceEntry.get().getResponse().getLocation();
		Provenance provenance = myProvenanceDao.read(new IdDt(provenanceLocation));
		assertThat(provenance.getEntity().get(0).getWhat().getReference()).isEqualTo("Encounter/" + theEncounterId + "/_history/1");
		return provenance;
	}

	@Test
	public void testDontStripVersionsFromRefsAtPathsAndAutoVersionSameTransaction_withTransactionBundle_shouldPreserveVersion() throws IOException {
		Set<String> referencePaths = Set.of("AuditEvent.entity.what","MessageHeader.focus","Provenance.target","Provenance.entity.what", "Provenance.agent.who");

		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths(referencePaths);
		myStorageSettings.setAutoVersionReferenceAtPaths("Provenance.agent.who");
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);

		Provenance provenance = postBundleAndAssertProvenanceRefsPreserved("/transaction-bundles/transaction-with-client-assigned-version-reference-and-auto-version-field.json", "242976");
		assertThat(provenance.getAgent().get(0).getWho().getReference()).isEqualTo("Patient/237643/_history/1");
	}

	@Test
	public void testDontStripVersionsFromRefsAtPaths_withTransactionBundleAndPreExistingReference_shouldPreserveVersion() throws IOException {
		Set<String> referencePaths = Set.of("AuditEvent.entity.what","MessageHeader.focus","Provenance.target","Provenance.entity.what");

		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths(referencePaths);
		myStorageSettings.setAutoVersionReferenceAtPaths("Provenance.entity.what");
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);

		Encounter encounter = new Encounter();
		encounter.setId("242976");
		myEncounterDao.update(encounter);

		postBundleAndAssertProvenanceRefsPreserved("/transaction-bundles/transaction-with-client-supplied-versioned-reference-and-pre-existing-ref.json", "242976");
	}

	@Test
	public void testDontOverwriteExistingVersion() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);

		Patient p = new Patient();
		p.setActive(true);
		myPatientDao.create(p, mySrd);

		// Update the patient
		p.setActive(false);
		IIdType patientId = myPatientDao.update(p, mySrd).getId().toUnqualified();

		assertEquals("2", patientId.getVersionIdPart());
		assertNull(patientId.getBaseUrl());

		Observation observation = new Observation();
		observation.getSubject().setReference(patientId.withVersion("1").getValue());
		IIdType observationId = myObservationDao.create(observation, mySrd).getId().toUnqualified();

		// Read back
		observation = myObservationDao.read(observationId, mySrd);
		assertEquals(patientId.withVersion("1").getValue(), observation.getSubject().getReference());
	}

	@Test
	public void testSearchAndIncludeVersionedReference_Asynchronous() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myStorageSettings.setRespectVersionsForSearchIncludes(true);

		// Create the patient
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://foo").setValue("1");
		myPatientDao.create(p, mySrd);

		// Update the patient
		p.getIdentifier().get(0).setValue("2");
		IIdType patientId = myPatientDao.update(p, mySrd).getId().toUnqualified();
		assertEquals("2", patientId.getVersionIdPart());

		Observation observation = new Observation();
		observation.getSubject().setReference(patientId.withVersion("1").getValue());
		IIdType observationId = myObservationDao.create(observation, mySrd).getId().toUnqualified();

		logAllResourceLinks();

		// Search - Non Synchronous for *
		{
			IBundleProvider outcome = myObservationDao.search(new SearchParameterMap().addInclude(IBaseResource.INCLUDE_ALL), mySrd);
			assertEquals(1, outcome.sizeOrThrowNpe());
			List<IBaseResource> resources = outcome.getResources(0, 1);
			assertThat(resources).hasSize(2);
			assertEquals(observationId.getValue(), resources.get(0).getIdElement().getValue());
			assertEquals(patientId.withVersion("1").getValue(), resources.get(1).getIdElement().getValue());
		}

		// Search - Non Synchronous for named include
		{
			IBundleProvider outcome = myObservationDao.search(new SearchParameterMap().addInclude(Observation.INCLUDE_PATIENT), mySrd);
			assertEquals(1, outcome.sizeOrThrowNpe());
			List<IBaseResource> resources = outcome.getResources(0, 1);
			assertThat(resources).hasSize(2);
			assertEquals(observationId.getValue(), resources.get(0).getIdElement().getValue());
			assertEquals(patientId.withVersion("1").getValue(), resources.get(1).getIdElement().getValue());
		}

	}

	@Test
	public void testSearchAndIncludeVersionedReference_Synchronous() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myStorageSettings.setRespectVersionsForSearchIncludes(true);
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.VERSIONED);

		// Create the patient
		Patient p = new Patient();
		p.getMeta().addTag("http://tag", "1", null);
		p.addIdentifier().setSystem("http://foo").setValue("1");
		myPatientDao.create(p, mySrd);

		// Update the patient - Add a second tag
		p.getIdentifier().get(0).setValue("2");
		p.getMeta().addTag("http://tag", "2", null);
		IIdType patientId = myPatientDao.update(p, mySrd).getId().toUnqualified();
		assertEquals("2", patientId.getVersionIdPart());

		Observation observation = new Observation();
		observation.getSubject().setReference(patientId.withVersion("1").getValue());
		IIdType observationId = myObservationDao.create(observation, mySrd).getId().toUnqualified();

		logAllResourceVersions();
		logAllResourceHistoryTags();

		// Search - Non-Synchronous for *
		{
			myCaptureQueriesListener.clear();
			IBundleProvider outcome = myObservationDao.search(SearchParameterMap.newSynchronous().addInclude(IBaseResource.INCLUDE_ALL), mySrd);
			validateSize(outcome, 1, 2);
			List<IBaseResource> resources = outcome.getResources(0, 2);
			assertEquals(5, myCaptureQueriesListener.logSelectQueries().size());
			assertThat(resources).hasSize(2);
			assertEquals(observationId.getValue(), resources.get(0).getIdElement().getValue());
			IBaseResource patient = resources.get(1);
			assertEquals(patientId.withVersion("1").getValue(), patient.getIdElement().getValue());
			assertThat(getTagCodes(patient)).asList().containsExactly("1");
			ourLog.info("Patient: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		}

		// Search - Non-Synchronous for named include
		{
			IBundleProvider outcome = myObservationDao.search(SearchParameterMap.newSynchronous().addInclude(Observation.INCLUDE_PATIENT), mySrd);
			validateSize(outcome, 1, 2);
			List<IBaseResource> resources = outcome.getResources(0, 2);
			assertThat(resources).hasSize(2);
			assertEquals(observationId.getValue(), resources.get(0).getIdElement().getValue());
			assertEquals(patientId.withVersion("1").getValue(), resources.get(1).getIdElement().getValue());
			assertThat(getTagCodes(resources.get(1))).asList().containsExactly("1");
		}

	}

	@Nonnull
	private static List<String> getTagCodes(IBaseResource patient) {
		return patient.getMeta().getTag().stream().map(IBaseCoding::getCode).collect(Collectors.toList());
	}

	@Test
	public void testSearchAndIncludeVersionedReference_WhenOnlyOneVersionExists() {
		HashSet<String> refPaths = new HashSet<>();
		refPaths.add("Task.basedOn");
		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths(refPaths);
		myStorageSettings.setRespectVersionsForSearchIncludes(true);
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);

		// Create a Condition
		Condition condition = new Condition();
		IIdType conditionId = myConditionDao.create(condition, mySrd).getId().toUnqualified();

		// Create a Task which is basedOn that Condition
		Task task = new Task();
		task.setBasedOn(List.of(new Reference(conditionId)));
		IIdType taskId = myTaskDao.create(task, mySrd).getId().toUnqualified();

		// Search for the Task using an _include=Task.basedOn and make sure we get the Condition resource in the Response
		IBundleProvider outcome = myTaskDao.search(SearchParameterMap.newSynchronous().addInclude(Task.INCLUDE_BASED_ON), mySrd);
		assertEquals(1, outcome.size());
		List<IBaseResource> resources = outcome.getResources(0, 2);
		assertThat(resources.size()).as(resources.stream().map(t -> t.getIdElement().toUnqualified().getValue()).collect(Collectors.joining(", "))).isEqualTo(2);
		assertEquals(taskId.getValue(), resources.get(0).getIdElement().getValue());
		assertEquals(conditionId.getValue(), ((Task) resources.get(0)).getBasedOn().get(0).getReference());
		assertEquals(conditionId.withVersion("1").getValue(), resources.get(1).getIdElement().getValue());

		// Now, update the Condition to generate another version of it
		condition.setRecordedDate(new Date(System.currentTimeMillis()));
		myConditionDao.update(condition, mySrd.getId().getValue(), mySrd);

		// Search for the Task again and make sure that we get the original version of the Condition resource in the Response
		outcome = myTaskDao.search(SearchParameterMap.newSynchronous().addInclude(Task.INCLUDE_BASED_ON), mySrd);
		validateSize(outcome, 1, 2);
		resources = outcome.getResources(0, 2);
		assertThat(resources).hasSize(2);
		assertEquals(taskId.getValue(), resources.get(0).getIdElement().getValue());
		assertEquals(conditionId.getValue(), ((Task) resources.get(0)).getBasedOn().get(0).getReference());
		assertEquals(conditionId.withVersion("1").getValue(), resources.get(1).getIdElement().getValue());
	}

	private void validateSize(IBundleProvider theProvider, int theSize, int theSizeIncludingIncludesAndOutcomes) {
		assertEquals(theSize, theProvider.size());
		Integer totalSize = theProvider.containsAllResources() ? (Integer) theProvider.getResourceListComplete().size() : theProvider.size();
		assertEquals(theSizeIncludingIncludesAndOutcomes, totalSize);
	}

	@Test
	public void testSearchAndIncludeVersionedReference_WhenMultipleVersionsExist() {
		HashSet<String> refPaths = new HashSet<>();
		refPaths.add("Task.basedOn");
		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths(refPaths);
		myStorageSettings.setRespectVersionsForSearchIncludes(true);
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);

		// Create a Condition
		Condition condition = new Condition();
		IIdType conditionId = myConditionDao.create(condition, mySrd).getId().toUnqualified();
		ourLog.info("conditionId: {}", conditionId);

		// Now, update the Condition 3 times to generate a 4th version of it
		condition.setRecordedDate(new Date(System.currentTimeMillis()));
		myConditionDao.update(condition, mySrd);
		condition.setRecordedDate(new Date(System.currentTimeMillis() + 1000000));
		myConditionDao.update(condition, mySrd);
		condition.setRecordedDate(new Date(System.currentTimeMillis() + 2000000));
		conditionId = myConditionDao.update(condition, mySrd).getId().toUnqualified();

		// Create a Task which is basedOn that Condition
		Task task = new Task();
		task.setBasedOn(List.of(new Reference(conditionId)));
		IIdType taskId = myTaskDao.create(task, mySrd).getId().toUnqualified();

		// Search for the Task using an _include=Task.basedOn and make sure we get the Condition resource in the Response
		IBundleProvider outcome = myTaskDao.search(SearchParameterMap.newSynchronous().addInclude(Task.INCLUDE_BASED_ON), mySrd);
		validateSize(outcome, 1, 2);
		List<IBaseResource> resources = outcome.getResources(0, 2);
		assertThat(resources.size()).as(resources.stream().map(t -> t.getIdElement().toUnqualified().getValue()).collect(Collectors.joining(", "))).isEqualTo(2);
		assertEquals(taskId.getValue(), resources.get(0).getIdElement().getValue());
		assertEquals(conditionId.getValue(), ((Task) resources.get(0)).getBasedOn().get(0).getReference());
		assertEquals(conditionId.withVersion("4").getValue(), resources.get(1).getIdElement().getValue());
	}

	@Test
	public void testSearchAndIncludeVersionedReference_WhenPreviouslyReferencedVersionOne() {
		HashSet<String> refPaths = new HashSet<>();
		refPaths.add("Task.basedOn");
		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths(refPaths);
		myStorageSettings.setRespectVersionsForSearchIncludes(true);
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);

		// Create a Condition
		Condition condition = new Condition();
		IIdType conditionId = myConditionDao.create(condition, mySrd).getId().toUnqualified();
		ourLog.info("conditionId: \n{}", conditionId);

		// Create a Task which is basedOn that Condition
		Task task = new Task();
		task.setBasedOn(List.of(new Reference(conditionId)));
		myTaskDao.create(task, mySrd).getId().toUnqualified();

		// Now, update the Condition 3 times to generate a 4th version of it
		condition.setRecordedDate(new Date(System.currentTimeMillis()));
		conditionId = myConditionDao.update(condition, mySrd).getId();
		ourLog.info("UPDATED conditionId: \n{}", conditionId);
		condition.setRecordedDate(new Date(System.currentTimeMillis() + 1000000));
		conditionId = myConditionDao.update(condition, mySrd).getId();
		ourLog.info("UPDATED conditionId: \n{}", conditionId);
		condition.setRecordedDate(new Date(System.currentTimeMillis() + 2000000));
		conditionId = myConditionDao.update(condition, mySrd).getId();
		ourLog.info("UPDATED conditionId: \n{}", conditionId);

		// Now, update the Task to refer to the latest version 4 of the Condition
		task.setBasedOn(List.of(new Reference(conditionId)));
		IIdType taskId = myTaskDao.update(task, mySrd).getId();
		ourLog.info("UPDATED taskId: \n{}", taskId);

		// Search for the Task using an _include=Task.basedOn and make sure we get the Condition resource in the Response
		IBundleProvider outcome = myTaskDao.search(SearchParameterMap.newSynchronous().addInclude(Task.INCLUDE_BASED_ON), mySrd);
		validateSize(outcome, 1, 2);
		List<IBaseResource> resources = outcome.getResources(0, 2);
		assertThat(resources.size()).as(resources.stream().map(t -> t.getIdElement().toUnqualified().getValue()).collect(Collectors.joining(", "))).isEqualTo(2);
		assertEquals(taskId.getValue(), resources.get(0).getIdElement().getValue());
		assertEquals(conditionId.getValue(), ((Task) resources.get(0)).getBasedOn().get(0).getReference());
		assertEquals(conditionId.withVersion("4").getValue(), resources.get(1).getIdElement().getValue());
	}

	@Test
	public void testSearchAndIncludeUnersionedReference_Asynchronous() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(true);
		myStorageSettings.setRespectVersionsForSearchIncludes(true);

		// Create the patient
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://foo").setValue("1");
		myPatientDao.create(p, mySrd);

		// Update the patient
		p.getIdentifier().get(0).setValue("2");
		IIdType patientId = myPatientDao.update(p, mySrd).getId().toUnqualified();
		assertEquals("2", patientId.getVersionIdPart());

		Observation observation = new Observation();
		observation.getSubject().setReference(patientId.withVersion("1").getValue());
		IIdType observationId = myObservationDao.create(observation, mySrd).getId().toUnqualified();

		// Search - Non Synchronous for *
		{
			IBundleProvider outcome = myObservationDao.search(new SearchParameterMap().addInclude(IBaseResource.INCLUDE_ALL), mySrd);
			assertEquals(1, outcome.sizeOrThrowNpe());
			List<IBaseResource> resources = outcome.getResources(0, 1);
			assertThat(resources).hasSize(2);
			assertEquals(observationId.getValue(), resources.get(0).getIdElement().getValue());
			assertEquals(patientId.withVersion("2").getValue(), resources.get(1).getIdElement().getValue());
		}

		// Search - Non Synchronous for named include
		{
			IBundleProvider outcome = myObservationDao.search(new SearchParameterMap().addInclude(Observation.INCLUDE_PATIENT), mySrd);
			assertEquals(1, outcome.sizeOrThrowNpe());
			List<IBaseResource> resources = outcome.getResources(0, 1);
			assertThat(resources).hasSize(2);
			assertEquals(observationId.getValue(), resources.get(0).getIdElement().getValue());
			assertEquals(patientId.withVersion("2").getValue(), resources.get(1).getIdElement().getValue());
		}

	}

	@Test
	public void testSearchAndIncludeUnversionedReference_Synchronous() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(true);
		myStorageSettings.setRespectVersionsForSearchIncludes(true);

		// Create the patient
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://foo").setValue("1");
		myPatientDao.create(p, mySrd);

		// Update the patient
		p.getIdentifier().get(0).setValue("2");
		IIdType patientId = myPatientDao.update(p, mySrd).getId().toUnqualified();
		assertEquals("2", patientId.getVersionIdPart());

		Observation observation = new Observation();
		observation.getSubject().setReference(patientId.withVersion("1").getValue());
		IIdType observationId = myObservationDao.create(observation, mySrd).getId().toUnqualified();

		// Read the observation back
		observation = myObservationDao.read(observationId, mySrd);
		assertEquals(patientId.toVersionless().getValue(), observation.getSubject().getReference());

		// Search - Non Synchronous for *
		{
			IBundleProvider outcome = myObservationDao.search(SearchParameterMap.newSynchronous().addInclude(IBaseResource.INCLUDE_ALL), mySrd);
			validateSize(outcome, 1, 2);
			List<IBaseResource> resources = outcome.getResources(0, 2);
			assertThat(resources).hasSize(2);
			assertEquals(observationId.getValue(), resources.get(0).getIdElement().getValue());
			assertEquals(patientId.withVersion("2").getValue(), resources.get(1).getIdElement().getValue());
		}

		// Search - Non Synchronous for named include
		{
			IBundleProvider outcome = myObservationDao.search(SearchParameterMap.newSynchronous().addInclude(Observation.INCLUDE_PATIENT), mySrd);
			validateSize(outcome, 1, 2);
			List<IBaseResource> resources = outcome.getResources(0, 2);
			assertThat(resources).hasSize(2);
			assertEquals(observationId.getValue(), resources.get(0).getIdElement().getValue());
			assertEquals(patientId.withVersion("2").getValue(), resources.get(1).getIdElement().getValue());
		}
	}

	@Test
	public void testNoNpeOnEoBBundle() throws IOException {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		List<String> strings = Arrays.asList(
			"ExplanationOfBenefit.patient",
			"ExplanationOfBenefit.insurer",
			"ExplanationOfBenefit.provider",
			"ExplanationOfBenefit.careTeam.provider",
			"ExplanationOfBenefit.insurance.coverage",
			"ExplanationOfBenefit.payee.party"
		);
		myStorageSettings.setAutoVersionReferenceAtPaths(new HashSet<>(strings));

		Bundle bundle = loadResourceFromClasspath(Bundle.class, "/npe-causing-bundle.json");

		Bundle transaction = mySystemDao.transaction(new SystemRequestDetails(), bundle);

		assertNotNull(transaction);
	}

	@Test
	public void testAutoVersionPathsWithAutoCreatePlaceholders() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		Observation obs = new Observation();
		obs.setId("Observation/CDE");
		obs.setSubject(new Reference("Patient/ABC"));
		DaoMethodOutcome update = myObservationDao.create(obs, mySrd);
		Observation resource = (Observation)update.getResource();
		String versionedPatientReference = resource.getSubject().getReference();
		assertEquals("Patient/ABC", versionedPatientReference);

		Patient p = myPatientDao.read(new IdDt("Patient/ABC"), mySrd);
		assertNotNull(p);

		myStorageSettings.setAutoVersionReferenceAtPaths("Observation.subject");

		obs = new Observation();
		obs.setId("Observation/DEF");
		obs.setSubject(new Reference("Patient/RED"));
		update = myObservationDao.create(obs, mySrd);
		resource = (Observation)update.getResource();
		versionedPatientReference = resource.getSubject().getReference();

		assertEquals("Patient/RED/_history/1", versionedPatientReference);
	}

	@ParameterizedTest
	@CsvSource({
		"0, false",
		"0, true",
		"1, false",
		"1, true",
		"2, false",
		"2, true"
	})
	public void testAutoVersionReferenceToConditionalCreate(int theExistingVersion, boolean theMatchUrlCacheEnabled) {
		// Setup
		myStorageSettings.setMatchUrlCacheEnabled(theMatchUrlCacheEnabled);
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myStorageSettings.setAutoVersionReferenceAtPaths(
			"Patient.managingOrganization"
		);

		// If theExistingVersion == 0 we don't create it at all
		// If theExistingVersion >= 1 we update it that number of times
		for (int i = 1; i <= theExistingVersion; i++) {
			Organization org = new Organization();
			org.setId("ORG");
			org.setName("V" + i);
			org.addIdentifier().setSystem("http://foo").setValue("ORG");
			myOrganizationDao.update(org, mySrd);
		}

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		Organization org = new Organization();
		IdType orgId = IdType.newRandomUuid();
		org.setId(orgId);
		org.setName("ORG999");
		org.addIdentifier().setSystem("http://foo").setValue("ORG");
		bb.addTransactionCreateEntry(org).conditional("Organization?identifier=http://foo|ORG");
		Patient p = new Patient();
		p.setId("P");
		p.setManagingOrganization(new Reference(orgId));
		bb.addTransactionUpdateEntry(p);
		Bundle input = bb.getBundleTyped();

		// Test
		Bundle output = mySystemDao.transaction(new SystemRequestDetails(), input);

		// Verify
		if (theExistingVersion == 0) {
			assertThat(output.getEntry().get(0).getResponse().getStatus()).startsWith("201 Created");
		} else {
			assertThat(output.getEntry().get(0).getResponse().getStatus()).startsWith("200 OK");
		}
		String actualOrgId = new IdType(output.getEntry().get(0).getResponse().getLocation()).toUnqualifiedVersionless().getValue();

		Patient actualPatient = myPatientDao.read(new IdType("Patient/P"), mySrd);


		String expected = actualOrgId + "/_history/" + (theExistingVersion > 0 ? theExistingVersion : 1);
		assertEquals(expected, actualPatient.getManagingOrganization().getReference());
	}



	@Test
	public void bundleTransaction_withRequestUrlNotRelativePath_doesNotProcess() throws IOException {
		Bundle bundle = loadResourceFromClasspath(Bundle.class, "/transaction-bundles/transaction-with-full-request-url.json");

		try {
			// test
			mySystemDao.transaction(new SystemRequestDetails(), bundle);
			fail("We expect invalid full urls to fail");
		} catch (InvalidRequestException ex) {
			assertThat(ex.getMessage()).contains("Unable to perform POST, URL provided is invalid:");
		}
	}

	@Test
	public void bundleTransaction_withRequestURLWithPrecedingSlash_processesAsExpected() throws IOException {
		Bundle bundle = loadResourceFromClasspath(Bundle.class, "/transaction-bundles/transaction-with-preceding-slash-request-url.json");

		// test
		Bundle outcome = mySystemDao.transaction(new SystemRequestDetails(),
			bundle);

		// verify it was created
		assertThat(outcome.getEntry()).hasSize(1);
		IdType idType = new IdType(bundle.getEntry().get(0)
				.getResource().getId());
		// the bundle above contains an observation, so we'll verify it was created here
		Observation obs = myObservationDao.read(idType, mySrd);
		assertNotNull(obs);
	}
}
