package ca.uhn.fhir.jpa.batch2.jobs.term.custom;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomTerminologyCsvBuilderTest {

	@Test
	void testGetConceptsCsv_NoConcepts() {
		CustomTerminologyCsvBuilder builder = new CustomTerminologyCsvBuilder();
		String csv = builder.getConceptsCsv();

		String expected = """
			CODE,DISPLAY
			
			""";
		assertEquals(expected, csv);
	}

	@Test
	void testGetConceptsCsv_WithConcepts() {
		CustomTerminologyCsvBuilder builder = new CustomTerminologyCsvBuilder();
		builder.addConcept("code1").withDisplay("display1");
		builder.addConcept("code2").withDisplay("display2");

		String csv = builder.getConceptsCsv();

		String expected = """
			CODE,DISPLAY
			
			code1,display1
			code2,display2
			""";
		assertEquals(expected, csv);
	}

	@Test
	void testGetConceptsCsv_WithQuoting() {
		CustomTerminologyCsvBuilder builder = new CustomTerminologyCsvBuilder();
		builder.addConcept("code1").withDisplay("display,1");
		builder.addConcept("code2").withDisplay("display \"2\"");

		String csv = builder.getConceptsCsv();

		String expected = """
			CODE,DISPLAY
			
			code1,"display,1"
			code2,"display ""2""\"
			""";
		assertEquals(expected, csv);
	}
}
