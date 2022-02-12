package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.config.BaseBatch2Config;

public class JpaBatch2Config extends BaseBatch2Config {

	@Override
	public IJobPersistence batch2JobInstancePersister() {
		return new JpaJobPersistenceImpl();
	}


}
