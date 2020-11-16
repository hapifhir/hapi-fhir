package ca.uhn.fhir.empi.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
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
			myEmpiSettings.getSupportedMdmTypes(), theName, theResourceType);
	}

	public String getMessageForUnsupportedMatchResult() {
		return "Match Result may only be set to " + EmpiMatchResultEnum.NO_MATCH + " or " + EmpiMatchResultEnum.MATCH;
	}

	public String getMessageForUnsupportedFirstArgumentTypeInUpdate(String goldenRecordType) {
		return "First argument to " + ProviderConstants.MDM_UPDATE_LINK + " must be a "
			+ myEmpiSettings.getSupportedMdmTypes() + ".  Was " + goldenRecordType;
	}

	public String getMessageForUnsupportedSecondArgumentTypeInUpdate(String theGoldenRecordType) {
		return "First argument to " + ProviderConstants.MDM_UPDATE_LINK + " must be a "
			+ myEmpiSettings.getSupportedMdmTypes() + ".  Was " + theGoldenRecordType;
	}

	public String getMessageForArgumentTypeMismatchInUpdate(String theGoldenRecordType, String theTargetType) {
		return "Arguments to " + ProviderConstants.MDM_UPDATE_LINK + " must be of the same type. Were " +
			theGoldenRecordType + " and " + theTargetType;
	}

	public String getMessageForUnsupportedTarget() {
		return "The target is marked with the " + EmpiConstants.CODE_NO_EMPI_MANAGED
			+ " tag which means it may not be EMPI linked.";
	}

	public String getMessageForNoLink(IAnyResource theGoldenRecord, IAnyResource theTarget) {
		return getMessageForNoLink(theGoldenRecord.getIdElement().toVersionless().toString(),
			theTarget.getIdElement().toVersionless().toString());
	}

	public String getMessageForNoLink(String theGoldenRecord, String theTarget) {
		return "No link exists between " + theGoldenRecord + " and " + theTarget;
	}
}
