package ca.uhn.fhir.batch2.jobs.importpull;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.importpull.models.Batch2BulkImportPullJobParameters;
import ca.uhn.fhir.batch2.importpull.svc.IBulkImportPullSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BulkImportParameterValidator implements IJobParametersValidator<Batch2BulkImportPullJobParameters> {

	@Autowired
	private IBulkImportPullSvc myBulkImportPullSvc;

	@Nullable
	@Override
	public List<String> validate(@NotNull Batch2BulkImportPullJobParameters theParameters) {
		ArrayList<String> errors = new ArrayList<>();

		if (theParameters.getBatchSize() <= 0) {
			errors.add("Batch size must be positive");
		}

		String jobId = theParameters.getJobId();
		if (StringUtils.isEmpty(jobId)) {
			errors.add("Bulk Import Pull requires an existing job id");
		} else {
			BulkImportJobJson job = myBulkImportPullSvc.fetchJobById(jobId);

			if (job == null) {
				errors.add("There is no persistent job that exists with UUID: " + jobId);
			}
		}

		return errors;
	}
}
