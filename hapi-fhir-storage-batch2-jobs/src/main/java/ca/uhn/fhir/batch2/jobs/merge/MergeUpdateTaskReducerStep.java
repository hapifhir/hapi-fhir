package ca.uhn.fhir.batch2.jobs.merge;

import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferenceUpdateTaskReducerStep;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;

public class MergeUpdateTaskReducerStep extends ReplaceReferenceUpdateTaskReducerStep<MergeJobParameters> {
	public MergeUpdateTaskReducerStep(DaoRegistry theDaoRegistry) {
		super(theDaoRegistry);
	}
}
