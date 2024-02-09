package ca.uhn.fhir.rest.server.interceptor.address;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.interceptor.validation.helpers.AddressHelper;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class AddressHelperTest {

	private static FhirContext ourContext = FhirContext.forR4();

	@Test
	void testInvalid() {
		HumanName name = new HumanName();
		name.setFamily("Test");

		final AddressHelper helper = new AddressHelper(null, name);
		assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> {
			helper.getCountry();
		});

		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
			new AddressHelper(null, new StringType("this will blow up"));
		});
	}

	@Test
	void getCountry() {
		Address a = new Address();
		a.setCountry("Test");

		AddressHelper helper = new AddressHelper(null, a);
		assertThat(helper.getCountry()).isEqualTo("Test");
	}

	@Test
	void getParts() {
		Address a = new Address();
		a.setCity("Hammer");

		AddressHelper helper = new AddressHelper(null, a);
		helper.setDelimiter("; ");
		assertThat(helper.getParts()).isEqualTo("Hammer");

		a.addLine("Street");
		a.setPostalCode("L9C6L6");
		assertThat(helper.getParts()).isEqualTo("Hammer; L9C6L6");
	}

	@Test
	void getLine() {
		Address a = new Address();
		a.addLine("Unit 10");
		a.setCity("Hammer");

		AddressHelper helper = new AddressHelper(null, a);
		assertThat(helper.getLine()).isEqualTo("Unit 10");

		a.addLine("100 Main St.");
		assertThat(helper.getLine()).isEqualTo("Unit 10, 100 Main St.");
	}

	@Test
	void testSetFields() {
		Address a = new Address();

		AddressHelper helper = new AddressHelper(ourContext, a);
		helper.addLine("Line 1").addLine("Line 2");
		helper.setCity("Hammer");
		helper.setState("State");
		helper.setCountry("Country");
		helper.setText("Some Text Too");
		assertThat(helper.toString()).isEqualTo("Some Text Too, Line 1, Line 2, Hammer, State, Country");
	}

}
