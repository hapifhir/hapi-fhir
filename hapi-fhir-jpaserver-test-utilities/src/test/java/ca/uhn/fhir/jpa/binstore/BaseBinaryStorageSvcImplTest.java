package ca.uhn.fhir.jpa.binstore;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseBinaryStorageSvcImplTest {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseBinaryStorageSvcImplTest.class);

	@Test
	public void testNewRandomId() {
		MemoryBinaryStorageSvcImpl svc = new MemoryBinaryStorageSvcImpl();
		svc.setFhirContextForTests(FhirContext.forR4Cached());
		svc.setInterceptorBroadcasterForTests(new InterceptorService());

		String id = svc.newBinaryContentId();
		ourLog.info(id);
		assertThat(id).matches("^[a-zA-Z0-9]{100}$");
	}
}
