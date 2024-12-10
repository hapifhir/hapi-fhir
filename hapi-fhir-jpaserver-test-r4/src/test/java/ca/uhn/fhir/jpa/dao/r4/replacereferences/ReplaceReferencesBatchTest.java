package ca.uhn.fhir.jpa.dao.r4.replacereferences;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ReplaceReferencesBatchTest extends BaseJpaR4Test {

	@Autowired
	private IJobCoordinator myJobCoordinator;
	@Autowired
	private IJobPersistence myJobPersistence;

	@Test
	public void testSimple() {

	}
}
