package ca.uhn.fhir.context;

import org.junit.Test;

import ca.uhn.fhir.model.resource.Observation;

public class ModelScannerTest {

	@Test
	public void testScanner() {
		
		ModelScanner scanner = new ModelScanner(Observation.class);
		
		
	}
	
}
