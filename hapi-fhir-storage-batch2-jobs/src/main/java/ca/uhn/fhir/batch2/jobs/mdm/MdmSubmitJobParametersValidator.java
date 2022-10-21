package ca.uhn.fhir.batch2.jobs.mdm;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.jobs.mdm.models.MdmSubmitJobParameters;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MdmSubmitJobParametersValidator implements IJobParametersValidator<MdmSubmitJobParameters> {
	@Autowired
	private IMdmSettings myMdmSettings;

	@Nullable
	@Override
	public List<String> validate(@Nonnull MdmSubmitJobParameters theParameters) {
		List<String> errorMsgs = new ArrayList<>();
		if (theParameters.getResourceTypes() == null || theParameters.getResourceTypes().isEmpty()) {
			errorMsgs.add("Resource Types are required for an $mdm-submit job.");
		} else {
			errorMsgs = theParameters.getResourceTypes()
				.stream()
				.filter(resourceType -> !myMdmSettings.isSupportedMdmType(resourceType))
				.map(resourceType -> "Resource Type " + resourceType + " is not supported for MDM. Please check your MDM settings.")
				.collect(Collectors.toList());
		}

		return errorMsgs;
	}
}
