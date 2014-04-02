package ca.uhn.fhir.narrative;


public class DefaultThymeleafNarrativeGenerator extends BaseThymeleafNarrativeGenerator implements INarrativeGenerator {

	static final String NARRATIVES_PROPERTIES = "classpath:ca/uhn/fhir/narrative/narratives.properties";

	@Override
	protected String getPropertyFile() {
		return NARRATIVES_PROPERTIES;
	}

}
