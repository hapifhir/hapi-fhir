package ca.uhn.fhir.mdm.batch2.submit;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public class MdmSubmitJobParametersValidator implements IJobParametersValidator<MdmSubmitJobParameters> {

	@Autowired
	private IMdmSettings myMdmSettings;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private FhirContext myFhirContext;

	@Nonnull
	@Override
	public List<String> validate(@Nonnull MdmSubmitJobParameters theParameters) {
		List<String> errorMsgs = new ArrayList<>();
		for (PartitionedUrl partitionedUrl : theParameters.getPartitionedUrls()) {
			String url = partitionedUrl.getUrl();
			String resourceType = getResourceTypeFromUrl(url);
			RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(resourceType);
			validateTypeIsUsedByMdm(errorMsgs, resourceType);
			validateAllSearchParametersApplyToResourceType(errorMsgs, partitionedUrl, resourceType, resourceDefinition);
		}
		return errorMsgs;
	}

	private void validateAllSearchParametersApplyToResourceType(List<String> errorMsgs, PartitionedUrl partitionedUrl, String resourceType, RuntimeResourceDefinition resourceDefinition) {
		try {
			myMatchUrlService.translateMatchUrl(partitionedUrl.getUrl(), resourceDefinition);
		} catch (MatchUrlService.UnrecognizedSearchParameterException e) {
			String errorMsg = String.format("Search parameter %s is not recognized for resource type %s. Source error is %s", e.getParamName(), resourceType, e.getMessage());
			errorMsgs.add(errorMsg);
		}
	}

	private void validateTypeIsUsedByMdm(List<String> errorMsgs, String resourceType) {
		if (!myMdmSettings.isSupportedMdmType(resourceType)) {
			errorMsgs.add("Resource type " + resourceType + " is not supported by MDM. Check your MDM settings");
		}
	}

	private String getResourceTypeFromUrl(String url) {
		int questionMarkIndex = url.indexOf('?');
		String resourceType;
		if (questionMarkIndex == -1) {
			resourceType = url;
		} else {
			resourceType = url.substring(0, questionMarkIndex);
		}
		return resourceType;
	}
}
