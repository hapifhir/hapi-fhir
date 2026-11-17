package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Opus 5.5
class AutoVersionSaveOrderTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	private final Map<DaoMethodOutcome, Set<IBaseReference>> myReferencesToAutoVersion = new LinkedHashMap<>();
	private final IdSubstitutionMap myIdSubstitutions = new IdSubstitutionMap();
	private final TransactionDetails myTransactionDetails = new TransactionDetails();

	@Test
	void testSaveOrder_LiteralReference_TargetBeforeSource() {
		DaoMethodOutcome observation = addOutcome(new Observation(), "Observation/O/_history/2", new Reference("Patient/A"));
		DaoMethodOutcome patient = addOutcome(new Patient(), "Patient/A/_history/2", new Reference("Organization/X"));

		AutoVersionSaveOrder saveOrder = newSaveOrder();

		assertThat(saveOrder.getOrderedOutcomes()).containsExactly(patient, observation);
		assertNothingForced(saveOrder);
	}

	@Test
	void testSaveOrder_PlaceholderReference_TargetBeforeSource() {
		myIdSubstitutions.put(new IdType("urn:uuid:1234"), new IdType("Patient/123/_history/2"));
		DaoMethodOutcome observation = addOutcome(new Observation(), "Observation/O/_history/2", new Reference("urn:uuid:1234"));
		DaoMethodOutcome patient = addOutcome(new Patient(), "Patient/123/_history/2", new Reference("Organization/X"));

		AutoVersionSaveOrder saveOrder = newSaveOrder();

		assertThat(saveOrder.getOrderedOutcomes()).containsExactly(patient, observation);
		assertNothingForced(saveOrder);
	}

	@Test
	void testSaveOrder_ReferenceByResourceOnly_TargetBeforeSource() {
		Patient target = new Patient();
		target.setId("Patient/A/_history/2");
		Reference reference = new Reference();
		reference.setResource(target);
		DaoMethodOutcome observation = addOutcome(new Observation(), "Observation/O/_history/2", reference);
		DaoMethodOutcome patient = addOutcome(target, "Patient/A/_history/2", new Reference("Organization/X"));

		AutoVersionSaveOrder saveOrder = newSaveOrder();

		assertThat(saveOrder.getOrderedOutcomes()).containsExactly(patient, observation);
		assertNothingForced(saveOrder);
	}

	@Test
	void testSaveOrder_ReferenceChain_EachTargetBeforeItsSource() {
		DaoMethodOutcome first = addOutcome(new Patient(), "Patient/A/_history/2", new Reference("Patient/B"));
		DaoMethodOutcome second = addOutcome(new Patient(), "Patient/B/_history/2", new Reference("Patient/C"));
		DaoMethodOutcome third = addOutcome(new Patient(), "Patient/C/_history/2", new Reference("Organization/X"));

		AutoVersionSaveOrder saveOrder = newSaveOrder();

		assertThat(saveOrder.getOrderedOutcomes()).containsExactly(third, second, first);
		assertNothingForced(saveOrder);
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"Patient?identifier=http://patient|123",
		"Patient/?identifier=http://patient|123",
		"http://example.com/fhir/Patient?identifier=http://patient|123"
	})
	void testSaveOrder_MatchUrlResolvedInTransaction_OnlyMatchedResourceBeforeSource(String theMatchUrl) {
		JpaPid pid = JpaPid.fromId(123L);
		myTransactionDetails.addResolvedMatchUrl(ourFhirContext, "Patient?identifier=http://patient|123", pid);
		myTransactionDetails.addResolvedResourceId(new IdType("Patient/123/_history/2"), pid);

		DaoMethodOutcome observation = addOutcome(new Observation(), "Observation/O/_history/2", new Reference(theMatchUrl));
		DaoMethodOutcome otherPatient = addOutcome(new Patient(), "Patient/456/_history/2", new Reference("Observation/O"));
		DaoMethodOutcome patient = addOutcome(new Patient(), "Patient/123/_history/2", new Reference("Organization/X"));

		AutoVersionSaveOrder saveOrder = newSaveOrder();

		// The other Patient references the Observation, which is only a cycle if the match URL targets it too
		assertThat(saveOrder.getOrderedOutcomes()).containsExactly(patient, observation, otherPatient);
		assertNothingForced(saveOrder);
	}

	@Test
	void testSaveOrder_MatchUrlNotResolvedInTransaction_AllResourcesOfTypeBeforeSource() {
		DaoMethodOutcome observation = addOutcome(new Observation(), "Observation/O/_history/2", new Reference("Patient?identifier=http://patient|123"));
		DaoMethodOutcome patient1 = addOutcome(new Patient(), "Patient/123/_history/2", new Reference("Organization/X"));
		DaoMethodOutcome patient2 = addOutcome(new Patient(), "Patient/456/_history/2", new Reference("Organization/X"));

		AutoVersionSaveOrder saveOrder = newSaveOrder();

		assertThat(saveOrder.getOrderedOutcomes()).containsExactly(patient1, patient2, observation);
		assertNothingForced(saveOrder);
	}

	@Test
	void testSaveOrder_ReferenceCycle_TargetSavedAfterItsSourceIsForced() {
		DaoMethodOutcome patientA = addOutcome(new Patient(), "Patient/A/_history/2", new Reference("Patient/B"));
		DaoMethodOutcome patientB = addOutcome(new Patient(), "Patient/B/_history/2", new Reference("Patient/A"));
		DaoMethodOutcome observation = addOutcome(new Observation(), "Observation/O/_history/2", new Reference("Patient/A"));

		AutoVersionSaveOrder saveOrder = newSaveOrder();

		// Patient B is saved first with a reference to the expected version of Patient A
		assertThat(saveOrder.getOrderedOutcomes()).containsExactly(patientB, patientA, observation);
		assertThat(saveOrder.isForceUpdateVersion(patientA)).isTrue();
		assertThat(saveOrder.isForceUpdateVersion(patientB)).isFalse();
		assertThat(saveOrder.isForceUpdateVersion(observation)).isFalse();
	}

	@Test
	void testSaveOrder_ReferenceToItself_NotForced() {
		DaoMethodOutcome patient = addOutcome(new Patient(), "Patient/A/_history/2", new Reference("Patient/A"));

		AutoVersionSaveOrder saveOrder = newSaveOrder();

		assertThat(saveOrder.getOrderedOutcomes()).containsExactly(patient);
		assertNothingForced(saveOrder);
	}

	@Test
	void testSaveOrder_TargetNotSavedInThisPass_Ignored() {
		DaoMethodOutcome observation = addOutcome(new Observation(), "Observation/O/_history/2", new Reference("Patient/A"), new Reference());

		AutoVersionSaveOrder saveOrder = newSaveOrder();

		assertThat(saveOrder.getOrderedOutcomes()).containsExactly(observation);
		assertNothingForced(saveOrder);
	}

	private DaoMethodOutcome addOutcome(Resource theResource, String theId, Reference... theReferencesToAutoVersion) {
		DaoMethodOutcome outcome = new DaoMethodOutcome();
		outcome.setId(new IdType(theId));
		outcome.setResource(theResource);
		myReferencesToAutoVersion.put(outcome, Set.copyOf(Arrays.asList(theReferencesToAutoVersion)));
		return outcome;
	}

	private AutoVersionSaveOrder newSaveOrder() {
		return new AutoVersionSaveOrder(ourFhirContext, myReferencesToAutoVersion, myIdSubstitutions, myTransactionDetails);
	}

	private void assertNothingForced(AutoVersionSaveOrder theSaveOrder) {
		assertThat(theSaveOrder.getOrderedOutcomes()).noneMatch(theSaveOrder::isForceUpdateVersion);
	}
}
