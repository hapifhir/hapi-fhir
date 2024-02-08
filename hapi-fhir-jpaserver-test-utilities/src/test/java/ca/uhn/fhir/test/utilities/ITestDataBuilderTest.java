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

			assertThat(myCreatedList.size()).isEqualTo(1);
			Observation o = (Observation) myCreatedList.get(0);

			assertThat(o.getEffectiveDateTimeType().getValueAsString()).isEqualTo("2020-01-01T12:34:56");
		}

		@Test
		void createObservation_withObservationCode_setsCode() {

			// when
			myTDB.createObservation(
				myTDB.withObservationCode("http://example.com", "a-code-value", "a code description")
			);

			assertThat(myCreatedList.size()).isEqualTo(1);
			Observation o = (Observation) myCreatedList.get(0);

			CodeableConcept codeable = o.getCode();
			assertThat(codeable).isNotNull();
			assertThat(codeable.getCoding().size()).as("has one coding").isEqualTo(1);
			Coding coding = codeable.getCoding().get(0);

			assertThat(coding.getSystem()).isEqualTo("http://example.com");
			assertThat(coding.getCode()).isEqualTo("a-code-value");
			assertThat(coding.getDisplay()).isEqualTo("a code description");

		}

		@Test
		void createObservation_withValueQuantity_createsQuantity() {
			myTDB.createObservation(
				myTDB.withQuantityAtPath("valueQuantity", 200, "hulla", "bpm"));

			assertThat(myCreatedList.size()).isEqualTo(1);
			Observation o = (Observation) myCreatedList.get(0);

			Quantity valueQuantity = o.getValueQuantity();
			assertThat(valueQuantity).isNotNull();

			assertThat(valueQuantity.getValue().doubleValue()).isEqualTo(200);
			assertThat(valueQuantity.getSystem()).isEqualTo("hulla");
			assertThat(valueQuantity.getCode()).isEqualTo("bpm");
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

			assertThat(myCreatedList.size()).isEqualTo(1);
			Observation o = (Observation) myCreatedList.get(0);

			assertThat(o.getComponent().size()).isEqualTo(2);
			Observation.ObservationComponentComponent secondComponent = o.getComponent().get(1);

			assertThat(secondComponent.getCode().getCoding().get(0).getCode()).isEqualTo("yet-another-code-value");
			assertThat(secondComponent.getValueQuantity().getValue().doubleValue()).isEqualTo(1000000.0);
		}

	}

	@Test
	void createGroup_withPatients_createsElementAndReference() {

		myTDB.createGroup(
			myTDB.withGroupMember("Patient/123")
		);

		assertThat(myCreatedList.size()).isEqualTo(1);
		Group g = (Group) myCreatedList.get(0);
		assertThat(g.getMember().size()).isEqualTo(1);
		assertThat(g.getMember().get(0).hasEntity()).isTrue();
		assertThat(g.getMember().get(0).getEntity().getReference()).isEqualTo("Patient/123");
	}

}
