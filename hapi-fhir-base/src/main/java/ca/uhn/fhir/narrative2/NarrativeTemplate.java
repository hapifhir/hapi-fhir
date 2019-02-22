package ca.uhn.fhir.narrative2;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBase;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class NarrativeTemplate implements INarrativeTemplate {

	private String myTemplateFileName;
	private Set<String> myAppliesToProfiles = new HashSet<>();
	private Set<String> myAppliesToResourceTypes = new HashSet<>();
	private Set<Class<? extends IBase>> myAppliesToResourceClasses = new HashSet<>();
	private TemplateTypeEnum myTemplateType = TemplateTypeEnum.THYMELEAF;
	private String myContextPath;
	private String myTemplateName;

	@Override
	public String getContextPath() {
		return myContextPath;
	}

	public void setContextPath(String theContextPath) {
		myContextPath = theContextPath;
	}

	private String getTemplateFileName() {
		return myTemplateFileName;
	}

	void setTemplateFileName(String theTemplateFileName) {
		myTemplateFileName = theTemplateFileName;
	}

	@Override
	public Set<String> getAppliesToProfiles() {
		return myAppliesToProfiles;
	}

	void addAppliesToProfile(String theAppliesToProfile) {
		myAppliesToProfiles.add(theAppliesToProfile);
	}

	@Override
	public Set<String> getAppliesToResourceTypes() {
		return myAppliesToResourceTypes;
	}

	void addAppliesToResourceType(String theAppliesToResourceType) {
		myAppliesToResourceTypes.add(theAppliesToResourceType);
	}

	@Override
	public Set<Class<? extends IBase>> getAppliesToResourceClasses() {
		return myAppliesToResourceClasses;
	}

	void addAppliesToResourceClass(Class<? extends IBase> theAppliesToResourceClass) {
		myAppliesToResourceClasses.add(theAppliesToResourceClass);
	}

	@Override
	public TemplateTypeEnum getTemplateType() {
		return myTemplateType;
	}

	void setTemplateType(TemplateTypeEnum theTemplateType) {
		myTemplateType = theTemplateType;
	}

	@Override
	public String getTemplateName() {
		return myTemplateName;
	}

	@Override
	public String getTemplateText() {
		try {
			return NarrativeTemplateManifest.loadResource(getTemplateFileName());
		} catch (IOException e) {
			throw new InternalErrorException(e);
		}
	}

	NarrativeTemplate setTemplateName(String theTemplateName) {
		myTemplateName = theTemplateName;
		return this;
	}
}
