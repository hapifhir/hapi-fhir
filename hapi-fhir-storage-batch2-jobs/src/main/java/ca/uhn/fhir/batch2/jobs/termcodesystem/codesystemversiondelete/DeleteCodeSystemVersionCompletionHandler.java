package ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemversiondelete;

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemSvc;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteVersionJobParameters;
import org.springframework.beans.factory.annotation.Autowired;

public class DeleteCodeSystemVersionCompletionHandler implements IJobCompletionHandler<TermCodeSystemDeleteVersionJobParameters> {

	@Autowired
	private ITermCodeSystemSvc myTermCodeSystemSvc;

	@Override
	public void jobComplete(JobCompletionDetails<TermCodeSystemDeleteVersionJobParameters> theDetails) {
		myTermCodeSystemSvc.notifyJobComplete(theDetails.getInstance().getInstanceId());
	}
}
