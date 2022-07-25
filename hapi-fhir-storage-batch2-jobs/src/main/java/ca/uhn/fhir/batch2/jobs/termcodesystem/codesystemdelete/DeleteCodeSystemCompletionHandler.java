package ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemdelete;

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemDeleteJobSvc;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import org.springframework.beans.factory.annotation.Autowired;

public class DeleteCodeSystemCompletionHandler
	implements IJobCompletionHandler<TermCodeSystemDeleteJobParameters> {

	@Autowired
	private ITermCodeSystemDeleteJobSvc myTermCodeSystemSvc;

	@Override
	public void jobComplete(JobCompletionDetails<TermCodeSystemDeleteJobParameters> theDetails) {
		myTermCodeSystemSvc.notifyJobComplete(theDetails.getInstance().getInstanceId());
	}
}
