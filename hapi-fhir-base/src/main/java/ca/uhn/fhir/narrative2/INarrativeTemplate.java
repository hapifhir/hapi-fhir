package ca.uhn.fhir.narrative2;

import org.hl7.fhir.instance.model.api.IBase;

import java.io.IOException;
import java.util.Set;

public interface INarrativeTemplate {
	String getContextPath();

	Set<String> getAppliesToProfiles();

	Set<String> getAppliesToResourceTypes();

	Set<Class<? extends IBase>> getAppliesToResourceClasses();

	TemplateTypeEnum getTemplateType();

	String getTemplateName();

	String getTemplateText();
}
