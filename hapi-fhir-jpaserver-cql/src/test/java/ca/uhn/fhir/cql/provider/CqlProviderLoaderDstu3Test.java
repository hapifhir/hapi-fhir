package ca.uhn.fhir.cql.provider;

import ca.uhn.fhir.cql.BaseCqlDstu3Test;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

class CqlProviderLoaderDstu3Test extends BaseCqlDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderLoaderDstu3Test.class);

	@Autowired
	CqlProviderLoader myCqlProviderLoader;

	@Autowired
	private ResourceProviderFactory myResourceProviderFactory;

	@Test
	public void contextLoads() {
		ourLog.info("YAY THE Dstu3 CONTEXT LOADED!");
		myCqlProviderLoader.loadProvider();
		myResourceProviderFactory.createProviders();
		ourLog.info("Holy I can't believe we made it here.");
	}
}
