package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.config.BaseBatch2AppCtx;

public class JpaBatch2AppCtx extends BaseBatch2AppCtx {

	@Override
	public IJobPersistence batchJobInstancePersister() {
		return new JpaJobPersistenceImpl();
	}


}
