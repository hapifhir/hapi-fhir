package ca.uhn.fhir.jpa.ips.api;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public class IpsSectionContext extends IpsContext {

	private final Section mySection;
	private final String myResourceType;

	IpsSectionContext(
		IBaseResource theSubject,
		IIdType theSubjectId,
		Section theSection,
		String theResourceType) {
		super(theSubject, theSubjectId);
		mySection = theSection;
		myResourceType = theResourceType;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public Section getSection() {
		return mySection;
	}
}
