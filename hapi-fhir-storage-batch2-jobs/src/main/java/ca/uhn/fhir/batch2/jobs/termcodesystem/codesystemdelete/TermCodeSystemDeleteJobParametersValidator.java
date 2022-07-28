package ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemdelete;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TermCodeSystemDeleteJobParametersValidator implements IJobParametersValidator<TermCodeSystemDeleteJobParameters> {

	@Nullable
	@Override
	public List<String> validate(@NotNull TermCodeSystemDeleteJobParameters theParameters) {
		List<String> errors = new ArrayList<>();
		if (theParameters.getTermPid() <= 0) {
			errors.add("Invalid Term Code System PID " + theParameters.getTermPid());
		}
		return errors;
	}
}
