package ca.uhn.fhir.batch2.jobs.importpull;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.importpull.models.Batch2BulkImportPullJobParameters;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class BulkImportParameterValidator implements IJobParametersValidator<Batch2BulkImportPullJobParameters> {
	private static final Logger ourLog = getLogger(BulkImportParameterValidator.class);

	@Autowired
	private IBulkDataImportSvc myBulkDataImportSvc;

	@Nullable
	@Override
	public List<String> validate(@NotNull Batch2BulkImportPullJobParameters theParameters) {
		ourLog.info("BulkImportPull parameter validation begin");

		ArrayList<String> errors = new ArrayList<>();

		if (theParameters.getBatchSize() <= 0) {
			errors.add("Batch size must be positive");
		}

		String jobId = theParameters.getJobId();
		if (StringUtils.isEmpty(jobId)) {
			errors.add("Bulk Import Pull requires an existing job id");
		} else {
			BulkImportJobJson job = myBulkDataImportSvc.fetchJob(jobId);

			if (job == null) {
				errors.add("There is no persistent job that exists with UUID: " + jobId);
			}
		}

		ourLog.info("BulkImportPull parameter validation end");

		return errors;
	}
}
