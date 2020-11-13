package ca.uhn.fhir.empi.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkQuerySvc;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageHelper {

	@Autowired
	private final IEmpiSettings myEmpiSettings;

	@Autowired
	private final FhirContext myFhirContext;

	public MessageHelper(IEmpiSettings theEmpiSettings, FhirContext theFhirContext) {
		myEmpiSettings = theEmpiSettings;
		myFhirContext = theFhirContext;
	}

	public String getMessageForUnmanagedResource() {
		return String.format(
			"Only MDM managed resources can be merged. MDM managed resources must have the %s tag.",
			EmpiConstants.CODE_HAPI_MDM_MANAGED);
	}

	public String getMessageForUnsupportedResource(String theName, IAnyResource theResource) {
		return getMessageForUnsupportedResource(theName, myFhirContext.getResourceType(theResource));
	}

	public String getMessageForUnsupportedResource(String theName, String theResourceType) {
		return String.format("Only %s resources can be merged. The %s points to a %s",
			myEmpiSettings.getSupportedMdmTypeNames(), theName, theResourceType);
	}

	public String getMessageForUnsupportedMatchResult() {
		return "Match Result may only be set to " + EmpiMatchResultEnum.NO_MATCH + " or " + EmpiMatchResultEnum.MATCH;
	}
}
