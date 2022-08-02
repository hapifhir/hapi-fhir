package ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemversiondelete;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteVersionJobParameters;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DeleteCodeSystemVersionParameterValidator implements IJobParametersValidator<TermCodeSystemDeleteVersionJobParameters> {

	@Nullable
	@Override
	public List<String> validate(@NotNull TermCodeSystemDeleteVersionJobParameters theParameters) {
		ArrayList<String> errors = new ArrayList<>();
		long versionPID = theParameters.getCodeSystemVersionPid();

		if (versionPID <= 0) {
			errors.add("Invalid code system version PID " + versionPID);
		}

		return errors;
	}
}
