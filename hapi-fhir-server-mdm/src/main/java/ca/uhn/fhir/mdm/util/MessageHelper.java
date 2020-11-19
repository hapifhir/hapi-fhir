package ca.uhn.fhir.mdm.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageHelper {

	@Autowired
	private final IMdmSettings myEmpiSettings;

	@Autowired
	private final FhirContext myFhirContext;

	public MessageHelper(IMdmSettings theEmpiSettings, FhirContext theFhirContext) {
		myEmpiSettings = theEmpiSettings;
		myFhirContext = theFhirContext;
	}

	public String getMessageForUnmanagedResource() {
		return String.format(
			"Only MDM managed resources can be merged. MDM managed resources must have the %s tag.",
			MdmConstants.CODE_HAPI_MDM_MANAGED);
	}

	public String getMessageForUnsupportedResource(String theName, IAnyResource theResource) {
		return getMessageForUnsupportedResource(theName, myFhirContext.getResourceType(theResource));
	}

	public String getMessageForUnsupportedResource(String theName, String theResourceType) {
		return String.format("Only %s resources can be merged. The %s points to a %s",
			myEmpiSettings.getSupportedMdmTypes(), theName, theResourceType);
	}

	public String getMessageForUnsupportedMatchResult() {
		return "Match Result may only be set to " + MdmMatchResultEnum.NO_MATCH + " or " + MdmMatchResultEnum.MATCH;
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
		return "The target is marked with the " + MdmConstants.CODE_NO_MDM_MANAGED
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
