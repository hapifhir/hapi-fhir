package ca.uhn.fhir.jpa.binstore;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.MatcherAssert.assertThat;

public class BaseBinaryStorageSvcImplTest {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseBinaryStorageSvcImplTest.class);

	@Test
	public void testNewRandomId() {
		MemoryBinaryStorageSvcImpl svc = new MemoryBinaryStorageSvcImpl();
		String id = svc.newBlobId();
		ourLog.info(id);
		assertThat(id, matchesPattern("^[a-zA-Z0-9]{100}$"));
	}
}
