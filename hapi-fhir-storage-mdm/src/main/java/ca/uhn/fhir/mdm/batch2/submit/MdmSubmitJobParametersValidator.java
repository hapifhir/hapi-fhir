package ca.uhn.fhir.mdm.batch2.submit;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public class MdmSubmitJobParametersValidator implements IJobParametersValidator<MdmSubmitJobParameters> {

	@Nullable
	@Override
	public List<String> validate(@Nonnull MdmSubmitJobParameters theParameters) {
		List<String> errorMsgs = new ArrayList<>();
		//FIXME GGG - validate the parameters. How to get MDM settings here?
		return errorMsgs;
	}
}
