package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.HapiExtensions;
import com.google.common.collect.Sets;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings({"ConstantConditions"})
public class FhirResourceDaoCreatePlaceholdersR4Test extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoCreatePlaceholdersR4Test.class);

	@AfterEach
	public final void afterResetDao() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(new DaoConfig().isAutoCreatePlaceholderReferenceTargets());
		myDaoConfig.setResourceClientIdStrategy(new DaoConfig().getResourceClientIdStrategy());
		myDaoConfig.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(new DaoConfig().isPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets());
		myDaoConfig.setBundleTypesAllowedForStorage(new DaoConfig().getBundleTypesAllowedForStorage());

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
			assertThat(e.getMessage(), startsWith("Resource Patient/FOO not found, specified in path: Observation.subject"));
		}
	}

	@Test
	public void testCreateWithBadReferenceIsPermitted() {
		assertFalse(myDaoConfig.isAutoCreatePlaceholderReferenceTargets());
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);

		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/FOO");
		myObservationDao.create(o, mySrd);
	}

	@Test
	public void testCreateWithMultiplePlaceholders() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);

		Task task = new Task();
		task.addNote().setText("A note");
		task.addPartOf().setReference("Task/AAA");
		task.addPartOf().setReference("Task/AAA");
		task.addPartOf().setReference("Task/AAA");
		IIdType id = myTaskDao.create(task).getId().toUnqualifiedVersionless();

		task = myTaskDao.read(id);
		assertEquals(3, task.getPartOf().size());
		assertEquals("Task/AAA", task.getPartOf().get(0).getReference());
		assertEquals("Task/AAA", task.getPartOf().get(1).getReference());
		assertEquals("Task/AAA", task.getPartOf().get(2).getReference());

		SearchParameterMap params = new SearchParameterMap();
		params.add(Task.SP_PART_OF, new ReferenceParam("Task/AAA"));
		List<String> found = toUnqualifiedVersionlessIdValues(myTaskDao.search(params));
		assertThat(found, contains(id.getValue()));


	}

	@Test
	public void testUpdateWithBadReferenceFails() {

		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		IIdType id = myObservationDao.create(o, mySrd).getId();

		o = new Observation();
		o.setId(id);
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/FOO");
		try {
			myObservationDao.update(o, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), startsWith("Resource Patient/FOO not found, specified in path: Observation.subject"));
		}
	}

	@Test
	public void testUpdateWithBadReferenceIsPermittedAlphanumeric() {
		assertFalse(myDaoConfig.isAutoCreatePlaceholderReferenceTargets());
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);

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
		assertFalse(myDaoConfig.isAutoCreatePlaceholderReferenceTargets());
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		myDaoConfig.setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.ANY);

		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		IIdType id = myObservationDao.create(o, mySrd).getId();

		try {
			myPatientDao.read(new IdType("Patient/999999999999999"));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

		o = new Observation();
		o.setId(id);
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/999999999999999");
		myObservationDao.update(o, mySrd);


		myPatientDao.read(new IdType("Patient/999999999999999"));

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("_id", new TokenParam("999999999999999"));
		IBundleProvider outcome = myPatientDao.search(map);
		assertEquals(1, outcome.size().intValue());
		assertEquals("Patient/999999999999999", outcome.getResources(0,1).get(0).getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void testCreatePlaceholderExtension_WithUpdateToTarget() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);

		// Create an Observation that references a Patient
		Observation obsToCreate = new Observation();
		obsToCreate.setStatus(ObservationStatus.FINAL);
		obsToCreate.getSubject().setReference("Patient/AAA");
		IIdType id = myObservationDao.create(obsToCreate, mySrd).getId();

		// Read the Observation
		Observation createdObs = myObservationDao.read(id);
		ourLog.info("\nObservation created:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));

		/*
		 * Read the placeholder Patient referenced by the Observation
		 * Placeholder extension should exist and be true
		 */
		Patient placeholderPat = myPatientDao.read(new IdType(createdObs.getSubject().getReference()));
		IIdType placeholderPatId = placeholderPat.getIdElement();
		ourLog.info("\nPlaceholder Patient created:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(placeholderPat));
		assertEquals(0, placeholderPat.getIdentifier().size());
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
		ourLog.info("\nUpdated Patient:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(updatedPat));
		assertEquals(1, updatedPat.getIdentifier().size());
		extension = updatedPat.getExtensionByUrl(HapiExtensions.EXT_RESOURCE_PLACEHOLDER);
		assertNull(extension);
	}

	@Test
	public void testCreatePlaceholderWithMatchUrl_IdentifierNotCopied() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		myDaoConfig.setAllowInlineMatchUrlReferences(true);
		myDaoConfig.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(false);

		Observation obsToCreate = new Observation();
		obsToCreate.setStatus(ObservationStatus.FINAL);
		obsToCreate.getSubject().setReference("Patient?identifier=http://foo|123");
		obsToCreate.getSubject().getIdentifier().setSystem("http://foo").setValue("123");
		IIdType id = myObservationDao.create(obsToCreate, mySrd).getId();

		Observation createdObs = myObservationDao.read(id);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));

		Patient patient = myPatientDao.read(new IdType(createdObs.getSubject().getReference()));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals(0, patient.getIdentifier().size());
	}

	@Test
	public void testCreatePlaceholderWithMatchUrl_IdentifierCopiedByDefault_WithUpdateToTarget() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		myDaoConfig.setAllowInlineMatchUrlReferences(true);

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
		ourLog.info("\nObservation created:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));

		/*
		 * Read the placeholder Patient referenced by the Observation
		 * Identifier should be populated since it was provided
		 */
		Patient placeholderPat = myPatientDao.read(new IdType(createdObs.getSubject().getReference()));
		IIdType placeholderPatId = placeholderPat.getIdElement();
		ourLog.info("\nPlaceholder Patient created:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(placeholderPat));
		assertEquals(1, placeholderPat.getIdentifier().size());
		assertEquals(createdObs.getSubject().getReference(), placeholderPatId.toUnqualifiedVersionless().getValueAsString());

		// Conditionally update a Patient with the same identifier
		Patient patToConditionalUpdate = new Patient();
		patToConditionalUpdate.addIdentifier().setSystem("http://foo").setValue("123");
		patToConditionalUpdate.addName().setFamily("Simpson");
		IIdType conditionalUpdatePatId = myPatientDao.update(patToConditionalUpdate, "Patient?identifier=http://foo|123", mySrd).getId();

		// Read the conditionally updated Patient
		Patient conditionalUpdatePat = myPatientDao.read(conditionalUpdatePatId);
		ourLog.info("\nConditionally updated Patient:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conditionalUpdatePat));
		assertEquals(1, conditionalUpdatePat.getIdentifier().size());

		/*
		 * Observation should reference conditionally updated Patient
		 * ID of placeholder Patient should match ID of conditionally updated Patient
		 */
		createdObs = myObservationDao.read(obsId);
		ourLog.info("\nObservation read after Patient update:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));
		assertEquals(createdObs.getSubject().getReference(), conditionalUpdatePatId.toUnqualifiedVersionless().getValueAsString());
		assertEquals(placeholderPatId.toUnqualifiedVersionless().getValueAsString(), conditionalUpdatePatId.toUnqualifiedVersionless().getValueAsString());
	}

	@Test
	public void testCreatePlaceholderWithMatchUrl_IdentifierCopiedByDefault_NotPreExisting() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		Observation obsToCreate = new Observation();
		obsToCreate.setStatus(ObservationStatus.FINAL);
		obsToCreate.getSubject().setReference("Patient?identifier=http://foo|123");
		obsToCreate.getSubject().getIdentifier().setSystem("http://foo").setValue("123");
		IIdType id = myObservationDao.create(obsToCreate, mySrd).getId();

		Observation createdObs = myObservationDao.read(id);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));

		Patient patient = myPatientDao.read(new IdType(createdObs.getSubject().getReference()));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals(1, patient.getIdentifier().size());
		assertEquals("http://foo", patient.getIdentifier().get(0).getSystem());
		assertEquals("123", patient.getIdentifier().get(0).getValue());
	}

	@Test
	public void testCreatePlaceholderWithMatchUrl_IdentifierNotCopiedBecauseNoFieldMatches() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		myDaoConfig.setAllowInlineMatchUrlReferences(true);
		myDaoConfig.setBundleTypesAllowedForStorage(Sets.newHashSet(""));

		AuditEvent eventToCreate = new AuditEvent();
		Reference what = eventToCreate.addEntity().getWhat();
		what.setReference("Bundle/ABC");
		what.getIdentifier().setSystem("http://foo");
		what.getIdentifier().setValue("123");
		IIdType id = myAuditEventDao.create(eventToCreate, mySrd).getId();

		AuditEvent createdEvent = myAuditEventDao.read(id);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEvent));

	}

	@Test
	public void testCreatePlaceholderWithMatchUrl_PreExisting() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		myDaoConfig.setAllowInlineMatchUrlReferences(true);

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
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));
		assertEquals("Patient/ABC", obsToCreate.getSubject().getReference());

	}



}
