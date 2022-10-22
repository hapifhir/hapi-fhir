package ca.uhn.fhir.mdm.batch2.submit;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public class MdmSubmitJobParametersValidator implements IJobParametersValidator<MdmSubmitJobParameters> {

	@Autowired
	private IMdmSettings myMdmSettings;
	@Nullable
	@Override
	public List<String> validate(@Nonnull MdmSubmitJobParameters theParameters) {
		List<String> errorMsgs = new ArrayList<>();
		for (PartitionedUrl partitionedUrl : theParameters.getPartitionedUrls()) {
			String url = partitionedUrl.getUrl();
			int questionMarkIndex = url.indexOf('?');
			if (questionMarkIndex == -1) {
				String resourceType = url.substring(0, questionMarkIndex);
				if (!myMdmSettings.isSupportedMdmType(resourceType)) {
					errorMsgs.add("Resource type " + resourceType + " is not supported by MDM. Check your MDM settings");
				}
			} else {
				errorMsgs.add("URL must contain a resource type! ");
			}
		}
		return errorMsgs;
	}
}
