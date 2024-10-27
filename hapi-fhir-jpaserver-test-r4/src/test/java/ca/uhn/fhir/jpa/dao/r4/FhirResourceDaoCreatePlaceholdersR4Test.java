package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.HapiExtensions;
import com.google.common.collect.Sets;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


@SuppressWarnings({"ConstantConditions"})
public class FhirResourceDaoCreatePlaceholdersR4Test extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoCreatePlaceholdersR4Test.class);

	private static final String TEST_IDENTIFIER_SYSTEM = "http://some-system.com";

	@AfterEach
	public final void afterResetDao() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(new JpaStorageSettings().isAutoCreatePlaceholderReferenceTargets());
		myStorageSettings.setResourceClientIdStrategy(new JpaStorageSettings().getResourceClientIdStrategy());
		myStorageSettings.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(new JpaStorageSettings().isPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets());
		myStorageSettings.setBundleTypesAllowedForStorage(new JpaStorageSettings().getBundleTypesAllowedForStorage());
		myStorageSettings.setAutoVersionReferenceAtPaths(new JpaStorageSettings().getAutoVersionReferenceAtPaths());
	}

	@Test
	public void testCreateWithBadReferenceFails() {
		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/FOO");
		try {
			myObservationDao.create(o, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).startsWith(Msg.code(1094) + "Resource Patient/FOO not found, specified in path: Observation.subject");
		}
	}

	@Test
	public void testCreateWithBadReferenceIsPermitted() {
		assertFalse(myStorageSettings.isAutoCreatePlaceholderReferenceTargets());
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/FOO");
		myObservationDao.create(o, mySrd);
	}

	@Test
	public void testCreateWithMultiplePlaceholders() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		Task task = new Task();
		task.addNote().setText("A note");
		task.addPartOf().setReference("Task/AAA");
		task.addPartOf().setReference("Task/AAA");
		task.addPartOf().setReference("Task/AAA");
		IIdType id = myTaskDao.create(task).getId().toUnqualifiedVersionless();

		task = myTaskDao.read(id);
		assertThat(task.getPartOf()).hasSize(3);
		assertEquals("Task/AAA", task.getPartOf().get(0).getReference());
		assertEquals("Task/AAA", task.getPartOf().get(1).getReference());
		assertEquals("Task/AAA", task.getPartOf().get(2).getReference());

		SearchParameterMap params = new SearchParameterMap();
		params.add(Task.SP_PART_OF, new ReferenceParam("Task/AAA"));
		List<String> found = toUnqualifiedVersionlessIdValues(myTaskDao.search(params));
		assertThat(found).containsExactly(id.getValue());
	}

	@Test
	public void testUpdateWithBadReferenceFails() {
		Observation o1 = new Observation();
		o1.setStatus(ObservationStatus.FINAL);
		IIdType id = myObservationDao.create(o1, mySrd).getId();

		Observation o = new Observation();
		o.setId(id);
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/FOO");

		Exception ex = Assertions.assertThrows(InvalidRequestException.class, () -> {
			myObservationDao.update(o, mySrd);
		});
		assertThat(ex.getMessage()).startsWith(Msg.code(1094) + "Resource Patient/FOO not found, specified in path: Observation.subject");
	}

	@Test
	public void testUpdateWithBadReferenceIsPermittedAlphanumeric() {
		assertFalse(myStorageSettings.isAutoCreatePlaceholderReferenceTargets());
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		IIdType id = myObservationDao.create(o, mySrd).getId();

		try {
			myPatientDao.read(new IdType("Patient/FOO"));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

		o = new Observation();
		o.setId(id);
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/FOO");
		myObservationDao.update(o, mySrd);

		myPatientDao.read(new IdType("Patient/FOO"));

	}

	@Test
	public void testUpdateWithBadReferenceIsPermittedNumeric() {
		assertFalse(myStorageSettings.isAutoCreatePlaceholderReferenceTargets());
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);

		myCaptureQueriesListener.clear();

		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		IIdType id = myObservationDao.create(o, mySrd).getId();

		myCaptureQueriesListener.logAllQueries();

		runInTransaction(() -> {
			ResourceTable entity = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(() -> new IllegalArgumentException());
			assertEquals(1, entity.getVersion());
		});

		try {
			myPatientDao.read(new IdType("Patient/999999999999999"));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

		o = new Observation();
		o.setId(id.getValue());
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/999999999999999");
		myObservationDao.update(o, mySrd);

		runInTransaction(() -> {
			ResourceTable entity = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(() -> new IllegalArgumentException());
			assertEquals(2, entity.getVersion());
		});

		myPatientDao.read(new IdType("Patient/999999999999999"));

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("_id", new TokenParam("999999999999999"));
		IBundleProvider outcome = myPatientDao.search(map);
		assertEquals(1, outcome.size().intValue());
		assertEquals("Patient/999999999999999", outcome.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void testCreatePlaceholderExtension_WithUpdateToTarget() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		// Create an Observation that references a Patient
		Observation obsToCreate = new Observation();
		obsToCreate.setStatus(ObservationStatus.FINAL);
		obsToCreate.getSubject().setReference("Patient/AAA");
		IIdType id = myObservationDao.create(obsToCreate, mySrd).getId();

		// Read the Observation
		Observation createdObs = myObservationDao.read(id);
		ourLog.debug("\nObservation created:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));

		/*
		 * Read the placeholder Patient referenced by the Observation
		 * Placeholder extension should exist and be true
		 */
		Patient placeholderPat = myPatientDao.read(new IdType(createdObs.getSubject().getReference()));
		IIdType placeholderPatId = placeholderPat.getIdElement();
		ourLog.debug("\nPlaceholder Patient created:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(placeholderPat));
		assertThat(placeholderPat.getIdentifier()).isEmpty();
		Extension extension = placeholderPat.getExtensionByUrl(HapiExtensions.EXT_RESOURCE_PLACEHOLDER);
		assertNotNull(extension);
		assertTrue(extension.hasValue());
		assertTrue(((BooleanType) extension.getValue()).booleanValue());

		// Update the Patient
		Patient patToUpdate = new Patient();
		patToUpdate.setId("Patient/AAA");
		patToUpdate.addIdentifier().setSystem("http://foo").setValue("123");
		IIdType updatedPatId = myPatientDao.update(patToUpdate).getId();

		/*
		 * Read the updated Patient
		 * Placeholder extension should not exist
		 */
		Patient updatedPat = myPatientDao.read(updatedPatId);
		ourLog.debug("\nUpdated Patient:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(updatedPat));
		assertThat(updatedPat.getIdentifier()).hasSize(1);
		extension = updatedPat.getExtensionByUrl(HapiExtensions.EXT_RESOURCE_PLACEHOLDER);
		assertNull(extension);
	}

	@Test
	public void testCreatePlaceholderWithMatchUrl_IdentifierNotCopied() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setAllowInlineMatchUrlReferences(true);
		myStorageSettings.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(false);

		Observation obsToCreate = new Observation();
		obsToCreate.setStatus(ObservationStatus.FINAL);
		obsToCreate.getSubject().setReference("Patient?identifier=http://foo|123");
		obsToCreate.getSubject().getIdentifier().setSystem("http://foo").setValue("123");
		IIdType id = myObservationDao.create(obsToCreate, mySrd).getId();

		Observation createdObs = myObservationDao.read(id);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));

		Patient patient = myPatientDao.read(new IdType(createdObs.getSubject().getReference()));
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertThat(patient.getIdentifier()).isEmpty();
	}

	//	Case 1:
	//
	//	IF the inline match URL does include an identifier
	//	AND the reference does not include an identifier
	//	AND a placeholder reference target is to be created
	//	DO use the value of the inline match URL's identifier to populate an identifier in the placeholder

	@Test
	public void testCreatePlaceholderWithMatchUrl_NoReferenceDefined() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setAllowInlineMatchUrlReferences(true);
		myStorageSettings.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(true);

		/*
		 * Create an Observation that references a Patient
		 * Reference is populated with inline match URL and includes identifier which differs from the inlined identifier
		 */
		Observation obsToCreate = new Observation();
		obsToCreate.setStatus(ObservationStatus.FINAL);
		obsToCreate.getSubject().setReference("Patient?identifier=http://foo|123");
		IIdType obsId = myObservationDao.create(obsToCreate, mySrd).getId();

		// Read the Observation
		Observation createdObs = myObservationDao.read(obsId);

		//Read the Placeholder Patient
		Patient placeholderPat = myPatientDao.read(new IdType(createdObs.getSubject().getReference()));
		ourLog.debug("\nObservation created:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));

		//Ensure the Obs has the right placeholder ID.
		IIdType placeholderPatId = placeholderPat.getIdElement();
		assertEquals(createdObs.getSubject().getReference(), placeholderPatId.toUnqualifiedVersionless().getValueAsString());

		/*
		 * Should have a single identifier populated.
		 */
		ourLog.debug("\nPlaceholder Patient created:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(placeholderPat));
		assertThat(placeholderPat.getIdentifier()).hasSize(1);
		List<Identifier> identifiers = placeholderPat.getIdentifier();
		Identifier identifier = identifiers.get(0);
		assertEquals("http://foo", identifier.getSystem());
		assertEquals("123", identifier.getValue());
	}


	//	Case 2:
	//
	//	IF the inline match URL does not include an identifier
	//	AND the reference does include an identifier
	//	AND a placeholder reference target is to be created
	//	DO use the value of the reference's identifier to populate an identifier in the placeholder
	@Test
	public void testCreatePlaceholderReferenceWhereInlineMatchUrlDoesNotContainIdentifierButSubjectReferenceDoes() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setAllowInlineMatchUrlReferences(true);
		myStorageSettings.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(true);

		/*
		 * Create an Observation that references a Patient
		 * Reference is populated with inline match URL and includes identifier which differs from the inlined identifier
		 */
		Observation obsToCreate = new Observation();
		obsToCreate.setStatus(ObservationStatus.FINAL);
		obsToCreate.getSubject().setReference("Patient?name=Johhnybravo");
		obsToCreate.getSubject().getIdentifier().setSystem("http://foo").setValue("123");
		IIdType obsId = myObservationDao.create(obsToCreate, mySrd).getId();

		// Read the Observation
		Observation createdObs = myObservationDao.read(obsId);

		//Read the Placeholder Patient
		Patient placeholderPat = myPatientDao.read(new IdType(createdObs.getSubject().getReference()));
		ourLog.debug("\nObservation created:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));

		//Ensure the Obs has the right placeholder ID.
		IIdType placeholderPatId = placeholderPat.getIdElement();
		assertEquals(createdObs.getSubject().getReference(), placeholderPatId.toUnqualifiedVersionless().getValueAsString());

		/*
		 * Should have a single identifier populated.
		 */
		ourLog.debug("\nPlaceholder Patient created:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(placeholderPat));
		assertThat(placeholderPat.getIdentifier()).hasSize(1);
		List<Identifier> identifiers = placeholderPat.getIdentifier();
		Identifier identifier = identifiers.get(0);
		assertEquals("http://foo", identifier.getSystem());
		assertEquals("123", identifier.getValue());
	}


	//	Case 3:
	//
	//	IF the inline match URL does include an identifier
	//	AND the reference does include an identifier
	//	AND the identifiers are the same
	//	AND a placeholder reference target is to be created
	//	DO use only the value of the reference's identifier to populate an identifier in the placeholder
	@Test
	public void testCreatePlaceholderWithMatchingInlineAndSubjectReferenceIdentifiersCreatesOnlyOne() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setAllowInlineMatchUrlReferences(true);
		myStorageSettings.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(true);

		/*
		 * Create an Observation that references a Patient
		 * Reference is populated with inline match URL and includes identifier which differs from the inlined identifier
		 */
		Observation obsToCreate = new Observation();
		obsToCreate.setStatus(ObservationStatus.FINAL);
		obsToCreate.getSubject().setReference("Patient?identifier=http://bar|321");
		obsToCreate.getSubject().getIdentifier().setSystem("http://bar").setValue("321");
		IIdType obsId = myObservationDao.create(obsToCreate, mySrd).getId();

		// Read the Observation
		Observation createdObs = myObservationDao.read(obsId);

		//Read the Placeholder Patient
		Patient placeholderPat = myPatientDao.read(new IdType(createdObs.getSubject().getReference()));
		ourLog.debug("\nObservation created:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));

		//Ensure the Obs has the right placeholder ID.
		IIdType placeholderPatId = placeholderPat.getIdElement();
		assertEquals(createdObs.getSubject().getReference(), placeholderPatId.toUnqualifiedVersionless().getValueAsString());

		/*
		 * Should have a single identifier populated.
		 */
		ourLog.debug("\nPlaceholder Patient created:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(placeholderPat));
		assertThat(placeholderPat.getIdentifier()).hasSize(1);
		List<Identifier> identifiers = placeholderPat.getIdentifier();
		Identifier identifier = identifiers.get(0);
		assertEquals("http://bar", identifier.getSystem());
		assertEquals("321", identifier.getValue());


	}

	@Test
	public void testCreatePlaceholderWithMatchingInlineAndSubjectReferenceIdentifiersCreatesOnlyOne_withConditionalUrl() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setAllowInlineMatchUrlReferences(true);
		myStorageSettings.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(true);

		final String system = "http://bar";
		final String value = "http://something.something/b";

		/*
		 * Create an Observation that references a Patient
		 * Reference is populated with inline match URL and includes identifier which differs from the inlined identifier
		 */
		final Observation obsToCreate = new Observation();
		obsToCreate.setStatus(ObservationStatus.FINAL);
		obsToCreate.getSubject().setReference(String.format("%s?identifier=%s|%s", ResourceType.Patient.name(), system, value));
		obsToCreate.getSubject().getIdentifier().setSystem(system).setValue(value);
		final IIdType obsId = myObservationDao.create(obsToCreate, mySrd).getId();

		// Read the Observation
		final Observation createdObs = myObservationDao.read(obsId, new SystemRequestDetails());

		//Read the Placeholder Patient
		final Patient placeholderPat = myPatientDao.read(new IdType(createdObs.getSubject().getReference()), new SystemRequestDetails());

		//Ensure the Obs has the right placeholder ID.
		final IIdType placeholderPatId = placeholderPat.getIdElement();
		assertEquals(createdObs.getSubject().getReference(), placeholderPatId.toUnqualifiedVersionless().getValueAsString());
		assertTrue(placeholderPatId.isIdPartValidLong());
		assertTrue(placeholderPatId.isVersionIdPartValidLong());


		/*
* Should have a single identifier populated.
 */
		assertThat(placeholderPat.getIdentifier()).hasSize(1);
		final List<Identifier> identifiers = placeholderPat.getIdentifier();
		Identifier identifier = identifiers.get(0);
		assertEquals(system, identifier.getSystem());
		assertEquals(value, identifier.getValue());
	}

	@Test
	void testAutoCreatePlaceholderReferencesAndInlineMatchWithUrlValues_conditionalCreateOrganizationAndOrganization() {
		// setup
		myStorageSettings.setAllowInlineMatchUrlReferences(true);
		myStorageSettings.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(true);
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		final String identifierValue = "http://some-url-value/Organization/ID2";

		// Test fails with Organization and Organization
		final Patient patient = new Patient();
		final Reference reference = new Reference()
			.setReference(String.format("%s?identifier=%s|%s", ResourceType.Organization.name(), TEST_IDENTIFIER_SYSTEM, identifierValue))
			.setIdentifier(new Identifier().setValue(identifierValue).setSystem(TEST_IDENTIFIER_SYSTEM));
		patient.setManagingOrganization(reference);

		myPatientDao.create(patient, mySrd);

		//Read the Placeholder Observation
		final IBundleProvider organizationSearch = myOrganizationDao.search(new SearchParameterMap(Organization.SP_IDENTIFIER, new TokenParam(TEST_IDENTIFIER_SYSTEM, identifierValue)), new SystemRequestDetails());
		final List<IBaseResource> allResources = organizationSearch.getAllResources();
		assertThat(allResources).hasSize(1);
		assertEquals(ResourceType.Organization.name(), allResources.get(0).getIdElement().getResourceType());
		assertThat(allResources.get(0).getIdElement().toUnqualifiedVersionless().toString()).startsWith(ResourceType.Organization.name());
		assertThat(organizationSearch.getAllResourceIds()).hasSize(1);
	}

	//	Case 4:
	//
	//	IF the inline match URL does include an identifier
	//	AND the reference does include an identifier
	//	AND the identifiers are different
	//	AND a placeholder reference target is to be created
	//	DO use both the value of the inline match URL's identifier and the value of the reference's identifier to populate two identifiers in the placeholder
	@Test
	public void testCreatePlaceholderWithMisMatchedIdentifiers_BothIdentifiersCopied() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setAllowInlineMatchUrlReferences(true);
		myStorageSettings.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(true);

		final String system = "http://bar";
		final String value = "321";

		/*
		 * Create an Observation that references a Patient
		 * Reference is populated with inline match URL and includes identifier which differs from the inlined identifier
		 */
		Observation obsToCreate = new Observation();
		obsToCreate.setStatus(ObservationStatus.FINAL);
		obsToCreate.getSubject().setReference("Patient?identifier=http://foo|123");
		obsToCreate.getSubject().getIdentifier().setSystem(system).setValue(value);
		IIdType obsId = myObservationDao.create(obsToCreate, mySrd).getId();

		// Read the Observation
		Observation createdObs = myObservationDao.read(obsId);

		//Read the Placeholder Patient
		Patient placeholderPat = myPatientDao.read(new IdType(createdObs.getSubject().getReference()));
		ourLog.debug("\nObservation created:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));

		//Ensure the Obs has the right placeholder ID.
		IIdType placeholderPatId = placeholderPat.getIdElement();
		assertEquals(createdObs.getSubject().getReference(), placeholderPatId.toUnqualifiedVersionless().getValueAsString());

		/*
		 * Placeholder Identifiers should both be populated since they were both provided, and did not match
		 */
		ourLog.debug("\nPlaceholder Patient created:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(placeholderPat));
		assertThat(placeholderPat.getIdentifier()).hasSize(2);
		List<Identifier> identifiers = placeholderPat.getIdentifier();

		//inline match-url identifier
		assertEquals("http://foo", identifiers.get(1).getSystem());
		assertEquals("123", identifiers.get(1).getValue());

		//subject identifier
		assertEquals(system, identifiers.get(0).getSystem());
		assertEquals(value, identifiers.get(0).getValue());


		// Conditionally update a Patient with the same identifier
		Patient patToConditionalUpdate = new Patient();
		patToConditionalUpdate.addIdentifier().setSystem("http://foo").setValue("123");
		patToConditionalUpdate.addName().setFamily("Simpson");
		IIdType conditionalUpdatePatId = myPatientDao.update(patToConditionalUpdate, "Patient?identifier=http://foo|123", mySrd).getId();

		// Read the conditionally updated Patient
		Patient conditionalUpdatePat = myPatientDao.read(conditionalUpdatePatId);
		ourLog.debug("\nConditionally updated Patient:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conditionalUpdatePat));
		assertThat(conditionalUpdatePat.getIdentifier()).hasSize(1);

		/*
		 * Observation should reference conditionally updated Patient
		 * ID of placeholder Patient should match ID of conditionally updated Patient
		 */
		createdObs = myObservationDao.read(obsId);
		ourLog.debug("\nObservation read after Patient update:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));
		assertEquals(createdObs.getSubject().getReference(), conditionalUpdatePatId.toUnqualifiedVersionless().getValueAsString());
		assertEquals(placeholderPatId.toUnqualifiedVersionless().getValueAsString(), conditionalUpdatePatId.toUnqualifiedVersionless().getValueAsString());
	}

	@Test
	public void testCreatePlaceholderWithMatchUrl_IdentifierCopiedByDefault_WithUpdateToTarget() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setAllowInlineMatchUrlReferences(true);

		/*
		 * Create an Observation that references a Patient
		 * Reference is populated with inline match URL and includes identifier
		 */
		Observation obsToCreate = new Observation();
		obsToCreate.setStatus(ObservationStatus.FINAL);
		obsToCreate.getSubject().setReference("Patient?identifier=http://foo|123");
		obsToCreate.getSubject().getIdentifier().setSystem("http://foo").setValue("123");
		IIdType obsId = myObservationDao.create(obsToCreate, mySrd).getId();

		// Read the Observation
		Observation createdObs = myObservationDao.read(obsId);
		ourLog.debug("\nObservation created:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));

		/*
		 * Read the placeholder Patient referenced by the Observation
		 * Identifier should be populated since it was provided
		 */
		Patient placeholderPat = myPatientDao.read(new IdType(createdObs.getSubject().getReference()));
		IIdType placeholderPatId = placeholderPat.getIdElement();
		ourLog.debug("\nPlaceholder Patient created:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(placeholderPat));
		assertThat(placeholderPat.getIdentifier()).hasSize(1);
		assertEquals(createdObs.getSubject().getReference(), placeholderPatId.toUnqualifiedVersionless().getValueAsString());

		// Conditionally update a Patient with the same identifier
		Patient patToConditionalUpdate = new Patient();
		patToConditionalUpdate.addIdentifier().setSystem("http://foo").setValue("123");
		patToConditionalUpdate.addName().setFamily("Simpson");
		IIdType conditionalUpdatePatId = myPatientDao.update(patToConditionalUpdate, "Patient?identifier=http://foo|123", mySrd).getId();

		// Read the conditionally updated Patient
		Patient conditionalUpdatePat = myPatientDao.read(conditionalUpdatePatId);
		ourLog.debug("\nConditionally updated Patient:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conditionalUpdatePat));
		assertThat(conditionalUpdatePat.getIdentifier()).hasSize(1);

		/*
		 * Observation should reference conditionally updated Patient
		 * ID of placeholder Patient should match ID of conditionally updated Patient
		 */
		createdObs = myObservationDao.read(obsId);
		ourLog.debug("\nObservation read after Patient update:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));
		assertEquals(createdObs.getSubject().getReference(), conditionalUpdatePatId.toUnqualifiedVersionless().getValueAsString());
		assertEquals(placeholderPatId.toUnqualifiedVersionless().getValueAsString(), conditionalUpdatePatId.toUnqualifiedVersionless().getValueAsString());
	}

	@Test
	public void testCreatePlaceholderWithMatchUrl_IdentifierCopiedByDefault_NotPreExisting() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setAllowInlineMatchUrlReferences(true);

		Observation obsToCreate = new Observation();
		obsToCreate.setStatus(ObservationStatus.FINAL);
		obsToCreate.getSubject().setReference("Patient?identifier=http://foo|123");
		obsToCreate.getSubject().getIdentifier().setSystem("http://foo").setValue("123");
		IIdType id = myObservationDao.create(obsToCreate, mySrd).getId();

		Observation createdObs = myObservationDao.read(id);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));

		Patient patient = myPatientDao.read(new IdType(createdObs.getSubject().getReference()));
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertThat(patient.getIdentifier()).hasSize(1);
		assertEquals("http://foo", patient.getIdentifier().get(0).getSystem());
		assertEquals("123", patient.getIdentifier().get(0).getValue());
	}

	@Test
	public void testCreatePlaceholderWithMatchUrl_IdentifierNotCopiedBecauseNoFieldMatches() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setAllowInlineMatchUrlReferences(true);
		myStorageSettings.setBundleTypesAllowedForStorage(Sets.newHashSet(""));

		AuditEvent eventToCreate = new AuditEvent();
		Reference what = eventToCreate.addEntity().getWhat();
		what.setReference("Bundle/ABC");
		what.getIdentifier().setSystem("http://foo");
		what.getIdentifier().setValue("123");
		IIdType id = myAuditEventDao.create(eventToCreate, mySrd).getId();

		AuditEvent createdEvent = myAuditEventDao.read(id);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEvent));
	}

	@Test
	public void testCreatePlaceholderWithMatchUrl_PreExisting() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setAllowInlineMatchUrlReferences(true);

		Patient patient = new Patient();
		patient.setId("ABC");
		patient.addIdentifier().setSystem("http://foo").setValue("123");
		myPatientDao.update(patient);

		Observation obsToCreate = new Observation();
		obsToCreate.setStatus(ObservationStatus.FINAL);
		obsToCreate.getSubject().setReference("Patient?identifier=http://foo|123");
		obsToCreate.getSubject().getIdentifier().setSystem("http://foo").setValue("123");
		IIdType id = myObservationDao.create(obsToCreate, mySrd).getId();

		Observation createdObs = myObservationDao.read(id);
		assertEquals("Patient/ABC", createdObs.getSubject().getReference());
	}

	@Test
	public void testAutocreatePlaceholderTest() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		Observation obs = new Observation();
		obs.setId("Observation/DEF");
		Reference patientRef = new Reference("Patient/RED");
		obs.setSubject(patientRef);
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		builder.addTransactionUpdateEntry(obs);

		mySystemDao.transaction(new SystemRequestDetails(), (Bundle) builder.getBundle());

		// verify subresource is created
		Patient returned = myPatientDao.read(patientRef.getReferenceElement());
		assertNotNull(returned);
	}


	@Test
	public void testAutocreatePlaceholderWithTargetExistingAlreadyTest() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setAutoVersionReferenceAtPaths("Observation.subject");

		String patientId = "Patient/RED";

		// create
		Patient patient = new Patient();
		patient.setIdElement(new IdType(patientId));
		myPatientDao.update(patient); // use update to use forcedid

		// update
		patient.setActive(true);
		myPatientDao.update(patient);

		// observation (with version 2)
		Observation obs = new Observation();
		obs.setId("Observation/DEF");
		Reference patientRef = new Reference(patientId);
		obs.setSubject(patientRef);
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		builder.addTransactionUpdateEntry(obs);

		Bundle transaction = mySystemDao.transaction(new SystemRequestDetails(), (Bundle) builder.getBundle());

		Patient returned = myPatientDao.read(patientRef.getReferenceElement());
		assertNotNull(returned);
		assertTrue(returned.getActive());
		assertEquals(2, returned.getIdElement().getVersionIdPartAsLong());

		Observation retObservation = myObservationDao.read(obs.getIdElement());
		assertNotNull(retObservation);
	}

	/**
	 * This test is the same as above, except it uses the serverid (instead of forcedid)
	 */
	@Test
	public void testAutocreatePlaceholderWithExistingTargetWithServerAssignedIdTest() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setAutoVersionReferenceAtPaths("Observation.subject");

		// create
		Patient patient = new Patient();
		patient.setIdElement(new IdType("Patient"));
		DaoMethodOutcome ret = myPatientDao.create(patient); // use create to use server id

		// update - to update our version
		patient.setActive(true);
		myPatientDao.update(patient);

		// observation (with version 2)
		Observation obs = new Observation();
		obs.setId("Observation/DEF");
		Reference patientRef = new Reference("Patient/" + ret.getId().getIdPart());
		obs.setSubject(patientRef);
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		builder.addTransactionUpdateEntry(obs);

		Bundle transaction = mySystemDao.transaction(new SystemRequestDetails(), (Bundle) builder.getBundle());

		Patient returned = myPatientDao.read(patientRef.getReferenceElement());
		assertNotNull(returned);
		assertEquals(2, returned.getIdElement().getVersionIdPartAsLong());

		Observation retObservation = myObservationDao.read(obs.getIdElement());
		assertNotNull(retObservation);
	}

	@Test
	public void testMultipleVersionedReferencesToAutocreatedPlaceholder() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		HashSet<String> refPaths = new HashSet<>();
		refPaths.add("Observation.subject");
		myStorageSettings.setAutoVersionReferenceAtPaths(refPaths);


		Observation obs1 = new Observation();
		obs1.setId("Observation/DEF1");
		Reference patientRef = new Reference("Patient/RED");
		obs1.setSubject(patientRef);
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		Observation obs2 = new Observation();
		obs2.setId("Observation/DEF2");
		obs2.setSubject(patientRef);
		builder.addTransactionUpdateEntry(obs1);
		builder.addTransactionUpdateEntry(obs2);

		mySystemDao.transaction(new SystemRequestDetails(), (Bundle) builder.getBundle());

		// verify links created to Patient placeholder from both Observations
		IBundleProvider outcome = myPatientDao.search(SearchParameterMap.newSynchronous().addRevInclude(IBaseResource.INCLUDE_ALL));
		assertThat(outcome.getAllResources()).hasSize(3);
	}

	@Test
	public void testMultipleReferencesToAutocreatedPlaceholder() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		Observation obs1 = new Observation();
		obs1.setId("Observation/DEF1");
		Reference patientRef = new Reference("Patient/RED");
		obs1.setSubject(patientRef);
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		Observation obs2 = new Observation();
		obs2.setId("Observation/DEF2");
		obs2.setSubject(patientRef);
		builder.addTransactionUpdateEntry(obs1);
		builder.addTransactionUpdateEntry(obs2);

		mySystemDao.transaction(new SystemRequestDetails(), (Bundle) builder.getBundle());

		// verify links created to Patient placeholder from both Observations
		IBundleProvider outcome = myPatientDao.search(SearchParameterMap.newSynchronous().addRevInclude(IBaseResource.INCLUDE_ALL));
		assertThat(outcome.getAllResources()).hasSize(3);
	}

}
