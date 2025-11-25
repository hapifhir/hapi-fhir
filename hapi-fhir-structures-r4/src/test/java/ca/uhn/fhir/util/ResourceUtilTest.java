package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.lang3.Strings;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceUtilTest {

	public static final String DATA_ABSENT_REASON_EXTENSION_URI =
		 "http://hl7.org/fhir/StructureDefinition/data-absent-reason";
	private final FhirContext ourFhirContext = FhirContext.forR4();

	@Test
	public void testRemoveNarrative() {
		Bundle bundle = new Bundle();

		Patient patient = new Patient();
		patient.getText().getDiv().setValue("<div>help im a bug</div>");
		bundle.addEntry().setResource(patient);

		Bundle embeddedBundle = new Bundle();
		embeddedBundle.setType(Bundle.BundleType.COLLECTION);
		bundle.addEntry().setResource(embeddedBundle);

		ResourceUtil.removeNarrative(FhirContext.forR4(), bundle);

		assertNull(((Patient) bundle.getEntry().get(0).getResource()).getText().getDiv().getValueAsString());
	}

	@Test
	void testMergeBooleanField() {
		Patient p1 = new Patient();
		p1.setDeceased(new BooleanType(true));

		Patient p2 = new Patient();
		ResourceUtil.mergeAllFields(ourFhirContext, p1, p2);

		assertTrue(p2.hasDeceased());
		assertEquals("true", p2.getDeceased().primitiveValue());
	}

	@Test
	void testMergeExtensions() {
		Patient p1 = new Patient();
		p1.addExtension(
			 "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race",
			 new Coding().setCode("X").setSystem("MyInternalRace").setDisplay("Eks"));
		p1.addExtension(
			 "http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity'",
			 new Coding().setSystem("MyInternalEthnicity").setDisplay("NNN"));

		Patient p2 = new Patient();
		ResourceUtil.mergeAllFields(ourFhirContext, p1, p2);

		assertThat(p2.getExtension()).hasSize(2);
	}

	@Test
	void testMergeForAddressWithExtensions() {
		Extension ext = new Extension();
		ext.setUrl("http://hapifhir.io/extensions/address#create-timestamp");
		ext.setValue(new DateTimeType("2021-01-02T11:13:15"));

		Patient p1 = new Patient();
		p1.addAddress()
			 .addLine("10 Main Street")
			 .setCity("Hamilton")
			 .setState("ON")
			 .setPostalCode("Z0Z0Z0")
			 .setCountry("Canada")
			 .addExtension(ext);

		Patient p2 = new Patient();
		p2.addAddress().addLine("10 Lenin Street").setCity("Severodvinsk").setCountry("Russia");

		ResourceUtil.mergeField(ourFhirContext, "address", p1, p2);

		assertThat(p2.getAddress()).hasSize(2);
		assertEquals("[10 Lenin Street]", p2.getAddress().get(0).getLine().toString());
		assertEquals("[10 Main Street]", p2.getAddress().get(1).getLine().toString());
		assertTrue(p2.getAddress().get(1).hasExtension());

		p1 = new Patient();
		p1.addAddress().addLine("10 Main Street").addExtension(ext);
		p2 = new Patient();
		p2.addAddress().addLine("10 Main Street").addExtension(
			 new Extension("demo", new DateTimeType("2021-01-02")));

		ResourceUtil.mergeField(ourFhirContext, "address", p1, p2);
		assertThat(p2.getAddress()).hasSize(2);
		assertTrue(p2.getAddress().get(0).hasExtension());
		assertTrue(p2.getAddress().get(1).hasExtension());

	}

	@Test
	void testMergeForSimilarAddresses() {
		Extension ext = new Extension();
		ext.setUrl("http://hapifhir.io/extensions/address#create-timestamp");
		ext.setValue(new DateTimeType("2021-01-02T11:13:15"));

		Patient p1 = new Patient();
		p1.addAddress()
			 .addLine("10 Main Street")
			 .setCity("Hamilton")
			 .setState("ON")
			 .setPostalCode("Z0Z0Z0")
			 .setCountry("Canada")
			 .addExtension(ext);

		Patient p2 = new Patient();
		p2.addAddress()
			 .addLine("10 Main Street")
			 .setCity("Hamilton")
			 .setState("ON")
			 .setPostalCode("Z0Z0Z1")
			 .setCountry("Canada")
			 .addExtension(ext);

		ResourceUtil.mergeField(ourFhirContext, "address", p1, p2);

		assertThat(p2.getAddress()).hasSize(2);
		assertEquals("[10 Main Street]", p2.getAddress().get(0).getLine().toString());
		assertEquals("[10 Main Street]", p2.getAddress().get(1).getLine().toString());
		assertTrue(p2.getAddress().get(1).hasExtension());
	}

	@Test
	public void testMergeWithReference() {
		Practitioner practitioner = new Practitioner();
		practitioner.setId(UUID.randomUUID().toString());
		practitioner.addName().setFamily("Smith").addGiven("Jane");

		Condition c1 = new Condition();
		c1.setRecorder(new Reference(practitioner));

		Condition c2 = new Condition();

		ResourceUtil.mergeField(ourFhirContext, "recorder", c1, c2);

		assertThat(c2.getRecorder().getResource()).isSameAs(practitioner);
	}

	@ParameterizedTest
	@MethodSource("singleCardinalityArguments")
	public void testMergeWithDataAbsentReason_singleCardinality(
		 Enumeration<Observation.ObservationStatus> theFromStatus,
		 Enumeration<Observation.ObservationStatus> theToStatus,
		 Enumeration<Observation.ObservationStatus> theExpectedStatus) {
		Observation fromObservation = new Observation();
		fromObservation.setStatusElement(theFromStatus);

		Observation toObservation = new Observation();
		toObservation.setStatusElement(theToStatus);

		ResourceUtil.mergeField(ourFhirContext, "status", fromObservation, toObservation);

		if (theExpectedStatus == null) {
			assertThat(toObservation.hasStatus()).isFalse();
		} else {
			assertThat(toObservation.getStatusElement().getCode()).isEqualTo(theExpectedStatus.getCode());
		}
	}

	private static Stream<Arguments> singleCardinalityArguments() {
		return Stream.of(
			 Arguments.of(null, null, null),
			 Arguments.of(statusFromEnum(Observation.ObservationStatus.FINAL), null, statusFromEnum(Observation.ObservationStatus.FINAL)),
			 Arguments.of(null, statusFromEnum(Observation.ObservationStatus.FINAL), statusFromEnum(Observation.ObservationStatus.FINAL)),
			 Arguments.of(statusFromEnum(Observation.ObservationStatus.FINAL), statusFromEnum(Observation.ObservationStatus.PRELIMINARY), statusFromEnum(Observation.ObservationStatus.FINAL)),
			 Arguments.of(statusWithDataAbsentReason(), null, statusWithDataAbsentReason()),
			 Arguments.of(null, statusWithDataAbsentReason(), statusWithDataAbsentReason()),
			 Arguments.of(statusWithDataAbsentReason(), statusWithDataAbsentReason(), statusWithDataAbsentReason()),
			 Arguments.of(statusFromEnum(Observation.ObservationStatus.FINAL), statusWithDataAbsentReason(), statusFromEnum(Observation.ObservationStatus.FINAL)),
			 Arguments.of(statusWithDataAbsentReason(), statusFromEnum(Observation.ObservationStatus.FINAL), statusFromEnum(Observation.ObservationStatus.FINAL))
		);
	}

	private static Enumeration<Observation.ObservationStatus> statusFromEnum(Observation.ObservationStatus theStatus) {
		return new Enumeration<>(new Observation.ObservationStatusEnumFactory(), theStatus);
	}

	private static Enumeration<Observation.ObservationStatus> statusWithDataAbsentReason() {
		Enumeration<Observation.ObservationStatus> enumeration = new Enumeration<>(new Observation.ObservationStatusEnumFactory());
		Enumeration<Enumerations.DataAbsentReason> extension = new Enumeration<>(new Enumerations.DataAbsentReasonEnumFactory(), Enumerations.DataAbsentReason.UNKNOWN);
		enumeration.addExtension(DATA_ABSENT_REASON_EXTENSION_URI, extension);
		return enumeration;
	}

	@ParameterizedTest
	@MethodSource("multipleCardinalityArguments")
	public void testMergeWithDataAbsentReason_multipleCardinality(
		 List<Identifier> theFromIdentifiers, List<Identifier> theToIdentifiers, List<Identifier> theExpectedIdentifiers) {
		Observation fromObservation = new Observation();
		theFromIdentifiers.forEach(fromObservation::addIdentifier);

		Observation toObservation = new Observation();
		theToIdentifiers.forEach(toObservation::addIdentifier);

		ResourceUtil.mergeField(ourFhirContext, "identifier", fromObservation, toObservation);

		assertThat(toObservation.getIdentifier()).hasSize(theExpectedIdentifiers.size());
		assertThat(toObservation.getIdentifier()).allMatch(t -> {
			if (t.hasValue()) {
				return theExpectedIdentifiers.stream().anyMatch(s -> Strings.CS.equals(t.getValue(), s.getValue()));
			} else if (t.hasExtension(DATA_ABSENT_REASON_EXTENSION_URI)) {
				return theExpectedIdentifiers.stream().anyMatch(s -> s.hasExtension(DATA_ABSENT_REASON_EXTENSION_URI));
			}
			return false;
		});
	}

	private static Stream<Arguments> multipleCardinalityArguments() {
		return Stream.of(
			 Arguments.of(List.of(), List.of(), List.of()),
			 Arguments.of(List.of(identifierFromValue("identifier1")), List.of(), List.of(identifierFromValue("identifier1"))),
			 Arguments.of(List.of(), List.of(identifierFromValue("identifier1")), List.of(identifierFromValue("identifier1"))),
			 Arguments.of(List.of(identifierFromValue("identifier1")), List.of(identifierFromValue("identifier2")), List.of(identifierFromValue("identifier1"), identifierFromValue("identifier2"))),
			 Arguments.of(List.of(identifierWithDataAbsentReason()), List.of(), List.of(identifierWithDataAbsentReason())),
			 Arguments.of(List.of(), List.of(identifierWithDataAbsentReason()), List.of(identifierWithDataAbsentReason())),
			 Arguments.of(List.of(identifierWithDataAbsentReason()), List.of(identifierWithDataAbsentReason()), List.of(identifierWithDataAbsentReason())),
			 Arguments.of(List.of(identifierFromValue("identifier1")), List.of(identifierWithDataAbsentReason()), List.of(identifierFromValue("identifier1"))),
			 Arguments.of(List.of(identifierWithDataAbsentReason()), List.of(identifierFromValue("identifier1")), List.of(identifierFromValue("identifier1"))),
			 Arguments.of(List.of(identifierFromValue("identifier1"), identifierFromValue("identifier2")), List.of(identifierWithDataAbsentReason()), List.of(identifierFromValue("identifier1"), identifierFromValue("identifier2"))),
			 Arguments.of(List.of(identifierWithDataAbsentReason()), List.of(identifierFromValue("identifier1"), identifierFromValue("identifier2")), List.of(identifierFromValue("identifier1"), identifierFromValue("identifier2")))
		);
	}

	private static Identifier identifierFromValue(String theValue) {
		return new Identifier().setValue(theValue);
	}

	private static Identifier identifierWithDataAbsentReason() {
		Identifier identifier = new Identifier();
		Enumeration<Enumerations.DataAbsentReason> extension = new Enumeration<>(new Enumerations.DataAbsentReasonEnumFactory(), Enumerations.DataAbsentReason.UNKNOWN);
		identifier.addExtension(DATA_ABSENT_REASON_EXTENSION_URI, extension);
		return identifier;
	}

	/*
	 * Ensure that codeable concepts with entirely disjoint codings are treated as discrete
	 */
	@Test
	public void testMerge_discreteCodeableConcepts_doesNotMerge() {
		// set up
		Observation o1 = new Observation();
		CodeableConcept category1 = o1.addCategory();
		category1.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("social-history");
		category1.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("survey");

		Observation o2 = new Observation();
		CodeableConcept category2 = o2.addCategory();
		category2.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs");

		// execute
		ResourceUtil.mergeField(ourFhirContext, "category", o1, o2);

		// validate
		assertThat(o2.getCategory()).hasSize(2);
	}

	/*
	 * The default behaviour for the merge operation requires an exact match between elements.
	 * Therefore, two CodeableConcepts where the order of the Codings differs will be considered
	 * distinct elements, and both will be included in the merged Resource.
	 */
	@Test
	public void testMerge_codingOrder_doesNotMerge() {
		// set up
		Observation o1 = new Observation();
		CodeableConcept category1 = o1.addCategory();
		category1.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("social-history");
		category1.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("survey");

		Observation o2 = new Observation();
		CodeableConcept category2 = o2.addCategory();
		category2.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("survey");
		category2.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("social-history");

		// execute
		ResourceUtil.mergeField(ourFhirContext, "category", o1, o2);

		// validate
		assertThat(o2.getCategory()).hasSize(2);
	}

	@Test
	public void testMerge_ignoreCodingOrder() {
		// set up
		Observation o1 = new Observation();
		CodeableConcept category1 = o1.addCategory();
		category1.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("social-history");
		category1.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("survey");

		Observation o2 = new Observation();
		CodeableConcept category2 = o2.addCategory();
		category2.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("survey");
		category2.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("social-history");

		ResourceUtil.MergeControlParameters mergeControlParameters = new ResourceUtil.MergeControlParameters();
		mergeControlParameters.setIgnoreCodeableConceptCodingOrder(true);

		// execute
		ResourceUtil.mergeField(ourFhirContext, "category", o1, o2, mergeControlParameters);

		// validate
		assertThat(o2.getCategory()).hasSize(1);
	}
}
