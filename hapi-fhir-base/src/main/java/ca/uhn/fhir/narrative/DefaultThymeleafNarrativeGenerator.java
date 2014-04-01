package ca.uhn.fhir.narrative;

import java.io.IOException;

public class DefaultThymeleafNarrativeGenerator extends BaseThymeleafNarrativeGenerator implements INarrativeGenerator {

	public DefaultThymeleafNarrativeGenerator() throws IOException {
		super();
	}

	@Override
	public String getPropertyFile() {
		return "classpath:ca/uhn/fhir/narrative/narratives.properties";
	}

}
