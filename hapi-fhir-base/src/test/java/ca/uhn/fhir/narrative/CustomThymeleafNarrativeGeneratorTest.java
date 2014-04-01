package ca.uhn.fhir.narrative;

import java.io.IOException;

import org.junit.Test;

public class CustomThymeleafNarrativeGeneratorTest {

	@Test
	public void testGenerator() throws IOException {
		
		CustomThymeleafNarrativeGenerator gen = new CustomThymeleafNarrativeGenerator("src/test/resources/narrative/customnarrative.properties");
		
		
		
	}
	
}
