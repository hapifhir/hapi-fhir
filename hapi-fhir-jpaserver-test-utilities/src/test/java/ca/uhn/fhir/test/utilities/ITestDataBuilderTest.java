package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ITestDataBuilderTest {
	FhirContext myFhirContext = FhirContext.forR4Cached();

	List<IBaseResource> myCreatedList = new ArrayList<>();
	List<IBaseResource> myUpdatedList = new ArrayList<>();

	ITestDataBuilder myTDB = new ITestDataBuilder() {
		@Override
		public IIdType doCreateResource(IBaseResource theResource) {
			myCreatedList.add(theResource);
			return null;
		}

		@Override
		public IIdType doUpdateResource(IBaseResource theResource) {
			myUpdatedList.add(theResource);
			return theResource.getIdElement();
		}

		@Override
		public FhirContext getFhirContext() {
			return myFhirContext;
		}
	};

	@Nested
	class ObservationCreation {
		@Test
		void createObservation_withEffective_setsDate() {
			myTDB.createObservation(
				myTDB.withEffectiveDate("2020-01-01T12:34:56"));

			assertThat(myCreatedList).hasSize(1);
			Observation o = (Observation) myCreatedList.get(0);

			assertEquals("2020-01-01T12:34:56", o.getEffectiveDateTimeType().getValueAsString());
		}

		@Test
		void createObservation_withObservationCode_setsCode() {

			// when
			myTDB.createObservation(
				myTDB.withObservationCode("http://example.com", "a-code-value", "a code description")
			);

			assertThat(myCreatedList).hasSize(1);
			Observation o = (Observation) myCreatedList.get(0);

			CodeableConcept codeable = o.getCode();
			assertNotNull(codeable);
			assertThat(codeable.getCoding().size()).as("has one coding").isEqualTo(1);
			Coding coding = codeable.getCoding().get(0);

			assertEquals("http://example.com", coding.getSystem());
			assertEquals("a-code-value", coding.getCode());
			assertEquals("a code description", coding.getDisplay());

		}

		@Test
		void createObservation_withValueQuantity_createsQuantity() {
			myTDB.createObservation(
				myTDB.withQuantityAtPath("valueQuantity", 200, "hulla", "bpm"));

			assertThat(myCreatedList).hasSize(1);
			Observation o = (Observation) myCreatedList.get(0);

			Quantity valueQuantity = o.getValueQuantity();
			assertNotNull(valueQuantity);

			assertEquals(200, valueQuantity.getValue().doubleValue());
			assertEquals("hulla", valueQuantity.getSystem());
			assertEquals("bpm", valueQuantity.getCode());
		}


		@Test
		void createObservation_withComponents_createsComponents() {

			// when
			myTDB.createObservation(
				myTDB.withObservationCode("http://example.com", "a-code-value", "a code description"),
				myTDB.withEffectiveDate("2020-01-01T12:34:56"),
				myTDB.withObservationComponent(
					myTDB.withObservationCode("http://example.com", "another-code-value"),
					myTDB.withQuantityAtPath("valueQuantity", 200, "hulla", "bpm")),
				myTDB.withObservationComponent(
					myTDB.withObservationCode("http://example.com", "yet-another-code-value"),
					myTDB.withQuantityAtPath("valueQuantity", 1000000, "hulla", "sik"))
			);

			assertThat(myCreatedList).hasSize(1);
			Observation o = (Observation) myCreatedList.get(0);

			assertThat(o.getComponent()).hasSize(2);
			Observation.ObservationComponentComponent secondComponent = o.getComponent().get(1);

			assertEquals("yet-another-code-value", secondComponent.getCode().getCoding().get(0).getCode());
			assertEquals(1000000.0, secondComponent.getValueQuantity().getValue().doubleValue());
		}

	}

	@Test
	void createGroup_withPatients_createsElementAndReference() {

		myTDB.createGroup(
			myTDB.withGroupMember("Patient/123")
		);

		assertThat(myCreatedList).hasSize(1);
		Group g = (Group) myCreatedList.get(0);
		assertThat(g.getMember()).hasSize(1);
		assertTrue(g.getMember().get(0).hasEntity());
		assertEquals("Patient/123", g.getMember().get(0).getEntity().getReference());
	}

}
