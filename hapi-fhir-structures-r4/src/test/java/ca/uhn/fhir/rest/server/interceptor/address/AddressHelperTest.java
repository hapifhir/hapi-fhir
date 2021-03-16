package ca.uhn.fhir.rest.server.interceptor.address;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.interceptor.validation.helpers.AddressHelper;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddressHelperTest {

	private static FhirContext ourContext = FhirContext.forR4();

	@Test
	void testInvalid() {
		HumanName name = new HumanName();
		name.setFamily("Test");

		final AddressHelper helper = new AddressHelper(null, name);
		assertThrows(IllegalStateException.class, () -> {
			helper.getCountry();
		});

		assertThrows(IllegalArgumentException.class, () -> {
			new AddressHelper(null, new StringType("this will blow up"));
		});
	}

	@Test
	void getCountry() {
		Address a = new Address();
		a.setCountry("Test");

		AddressHelper helper = new AddressHelper(null, a);
		assertEquals("Test", helper.getCountry());
	}

	@Test
	void getParts() {
		Address a = new Address();
		a.setCity("Hammer");

		AddressHelper helper = new AddressHelper(null, a);
		helper.setDelimiter("; ");
		assertEquals("Hammer", helper.getParts());

		a.addLine("Street");
		a.setPostalCode("L9C6L6");
		assertEquals("Hammer; L9C6L6", helper.getParts());
	}

	@Test
	void getLine() {
		Address a = new Address();
		a.addLine("Unit 10");
		a.setCity("Hammer");

		AddressHelper helper = new AddressHelper(null, a);
		assertEquals("Unit 10", helper.getLine());

		a.addLine("100 Main St.");
		assertEquals("Unit 10, 100 Main St.", helper.getLine());
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
		assertEquals("Some Text Too, Line 1, Line 2, Hammer, State, Country", helper.toString());
	}

}
