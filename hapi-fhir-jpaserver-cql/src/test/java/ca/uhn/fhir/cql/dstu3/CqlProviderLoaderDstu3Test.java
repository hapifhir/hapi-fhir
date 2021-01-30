package ca.uhn.fhir.cql.dstu3;

import ca.uhn.fhir.cql.BaseCqlDstu3Test;
import ca.uhn.fhir.cql.common.provider.CqlProviderLoader;
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
	public void testContextLoads() {
		myCqlProviderLoader.loadProvider();
		myResourceProviderFactory.createProviders();
		ourLog.info("The CqlProviderLoader loaded and was able to create Providers.");
	}
}
