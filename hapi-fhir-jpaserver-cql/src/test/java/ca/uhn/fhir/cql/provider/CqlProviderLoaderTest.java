package ca.uhn.fhir.cql.provider;

import ca.uhn.fhir.cql.BaseCqlR4Test;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

class CqlProviderLoaderTest extends BaseCqlR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderLoaderTest.class);

	@Autowired
	CqlProviderLoader myCqlProviderLoader;

	@Test
	public void contextLoads() {
		ourLog.info("YAY THE CONTEXT LOADED!");
		myCqlProviderLoader.loadProvider();
		ourLog.info("Holy I can't believe we made it here.");
	}
}
