package ca.uhn.fhir.jpa.batch.svc;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class DummyService {
	 private static final Logger ourLog = getLogger(DummyService.class);

	public void test() {
		ourLog.warn("test");
	}
}
