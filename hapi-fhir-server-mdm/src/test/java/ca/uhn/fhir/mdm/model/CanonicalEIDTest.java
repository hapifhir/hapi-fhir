package ca.uhn.fhir.mdm.model;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-opus-5
class CanonicalEIDTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4();

	private static final String MRN_SYSTEM = "http://example.com/mrn";
	private static final String NPI_SYSTEM = "http://example.com/npi";
	private static final String UNRELATED_SYSTEM = "http://example.com/unrelated";

	@Test
	void extractFromResource_twoSystems_returnsIdentifiersFromBoth() {
		Patient patient = patientWithIdentifiers(MRN_SYSTEM, "mrn-1", NPI_SYSTEM, "npi-9");

		List<CanonicalEID> eids =
			CanonicalEID.extractFromResource(ourFhirContext, List.of(MRN_SYSTEM, NPI_SYSTEM), patient);

		assertThat(eids).extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactlyInAnyOrder(MRN_SYSTEM + "|mrn-1", NPI_SYSTEM + "|npi-9");
	}

	@Test
	void extractFromResource_identifierFromUnconfiguredSystem_isIgnored() {
		Patient patient = patientWithIdentifiers(MRN_SYSTEM, "mrn-1", UNRELATED_SYSTEM, "other-1");

		List<CanonicalEID> eids =
			CanonicalEID.extractFromResource(ourFhirContext, List.of(MRN_SYSTEM, NPI_SYSTEM), patient);

		assertThat(eids).extracting(CanonicalEID::getSystemAndValueKey).containsExactly(MRN_SYSTEM + "|mrn-1");
	}

	@Test
	void extractFromResource_emptySystemCollection_returnsEmpty() {
		Patient patient = patientWithIdentifiers(MRN_SYSTEM, "mrn-1", NPI_SYSTEM, "npi-9");

		List<CanonicalEID> eids = CanonicalEID.extractFromResource(ourFhirContext, Collections.emptyList(), patient);

		assertThat(eids).isEmpty();
	}

	@Test
	void extractFromResource_singleSystemOverload_stillReturnsThatSystemsIdentifiers() {
		Patient patient = patientWithIdentifiers(MRN_SYSTEM, "mrn-1", NPI_SYSTEM, "npi-9");

		List<CanonicalEID> eids = CanonicalEID.extractFromResource(ourFhirContext, MRN_SYSTEM, patient);

		assertThat(eids).extracting(CanonicalEID::getSystemAndValueKey).containsExactly(MRN_SYSTEM + "|mrn-1");
	}

	@Test
	void extractFromResource_multipleValuesForOneSystem_returnsAllOfThem() {
		Patient patient = patientWithIdentifiers(MRN_SYSTEM, "mrn-1", MRN_SYSTEM, "mrn-2");

		List<CanonicalEID> eids = CanonicalEID.extractFromResource(ourFhirContext, List.of(MRN_SYSTEM), patient);

		assertThat(eids).extracting(CanonicalEID::getValue).containsExactlyInAnyOrder("mrn-1", "mrn-2");
	}

	@Test
	void getSystemAndValueKey_distinguishesSameValueInDifferentSystems() {
		CanonicalEID mrn = new CanonicalEID(MRN_SYSTEM, "123", null);
		CanonicalEID npi = new CanonicalEID(NPI_SYSTEM, "123", null);

		assertThat(mrn.getSystemAndValueKey()).isEqualTo(MRN_SYSTEM + "|123");
		assertThat(npi.getSystemAndValueKey()).isEqualTo(NPI_SYSTEM + "|123");
	}

	@Test
	void getSystemAndValueKey_ignoresUse() {
		CanonicalEID official = new CanonicalEID(MRN_SYSTEM, "123", "official");
		CanonicalEID secondary = new CanonicalEID(MRN_SYSTEM, "123", "secondary");

		assertThat(official.getSystemAndValueKey()).isEqualTo(MRN_SYSTEM + "|123");
		assertThat(secondary.getSystemAndValueKey()).isEqualTo(MRN_SYSTEM + "|123");
	}

	private Patient patientWithIdentifiers(
			String theFirstSystem, String theFirstValue, String theSecondSystem, String theSecondValue) {
		Patient retVal = new Patient();
		retVal.addIdentifier(new Identifier().setSystem(theFirstSystem).setValue(theFirstValue));
		retVal.addIdentifier(new Identifier().setSystem(theSecondSystem).setValue(theSecondValue));
		return retVal;
	}
}
