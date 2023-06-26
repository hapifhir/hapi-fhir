package ca.uhn.fhir.narrative;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomThymeleafNarrativeGeneratorR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CustomThymeleafNarrativeGeneratorR4Test.class);

	/**
	 * Don't use cached here since we modify the context
	 */
	private final FhirContext myCtx = FhirContext.forR4Cached();

	@AfterEach
	public void after() {
		myCtx.setNarrativeGenerator(null);
	}

	/**
	 * Implement narrative for standard type
	 */
	@Test
	public void testStandardType() {

		CustomThymeleafNarrativeGenerator gen = new CustomThymeleafNarrativeGenerator("classpath:narrative/standardtypes_r4.properties");
		myCtx.setNarrativeGenerator(gen);

		Practitioner p = new Practitioner();
		p.addIdentifier().setSystem("sys").setValue("val1");
		p.addIdentifier().setSystem("sys").setValue("val2");
		p.addAddress().addLine("line1").addLine("line2");
		p.addName().setFamily("fam1").addGiven("given");

		gen.populateResourceNarrative(myCtx, p);

		String actual = p.getText().getDiv().getValueAsString();
		ourLog.info(actual);

		assertThat(actual, containsString("<h1>Name</h1><div class=\"nameElement\">given <b>FAM1 </b></div><h1>Address</h1><div><span>line1 </span><br/><span>line2 </span><br/></div></div>"));

	}

	@Test
	public void testCustomType() {

		CustomPatient patient = new CustomPatient();
		patient.setActive(true);
		FavouritePizzaExtension parentExtension = new FavouritePizzaExtension();
		parentExtension.setToppings(new StringType("Mushrooms, Onions"));
		parentExtension.setSize(new Quantity(null, 14, "http://unitsofmeasure", "[in_i]", "Inches"));
		patient.setFavouritePizza(parentExtension);

		String output = myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info("Encoded: {}", output);

		String expectedEncoding = "{\n" +
			"  \"resourceType\": \"Patient\",\n" +
			"  \"meta\": {\n" +
			"    \"profile\": [ \"http://custom_patient\" ]\n" +
			"  },\n" +
			"  \"extension\": [ {\n" +
			"    \"url\": \"http://example.com/favourite_pizza\",\n" +
			"    \"extension\": [ {\n" +
			"      \"url\": \"toppings\",\n" +
			"      \"valueString\": \"Mushrooms, Onions\"\n" +
			"    }, {\n" +
			"      \"url\": \"size\",\n" +
			"      \"valueQuantity\": {\n" +
			"        \"value\": 14,\n" +
			"        \"unit\": \"Inches\",\n" +
			"        \"system\": \"http://unitsofmeasure\",\n" +
			"        \"code\": \"[in_i]\"\n" +
			"      }\n" +
			"    } ]\n" +
			"  } ],\n" +
			"  \"active\": true\n" +
			"}";
		assertEquals(expectedEncoding, output);

		CustomThymeleafNarrativeGenerator gen = new CustomThymeleafNarrativeGenerator("classpath:narrative/customtypes_r4.properties");
		myCtx.setNarrativeGenerator(gen);
		gen.populateResourceNarrative(myCtx, patient);

		String actual = patient.getText().getDiv().getValueAsString();
		ourLog.info(actual);

		String expected = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><h1>CustomPatient</h1><div><div><h1>Favourite Pizza</h1> Toppings: <span>Mushrooms, Onions</span> Size: <span>14</span></div></div></div>";
		assertEquals(expected, actual);

	}

}
