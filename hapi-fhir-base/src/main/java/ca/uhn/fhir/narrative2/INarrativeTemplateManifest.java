package ca.uhn.fhir.narrative2;

import java.util.Optional;

public interface INarrativeTemplateManifest {
	Optional<INarrativeTemplate> getTemplateByResourceName(TemplateTypeEnum theStyle, String theResourceName);

	Optional<INarrativeTemplate> getTemplateByName(TemplateTypeEnum theStyle, String theName);
}
