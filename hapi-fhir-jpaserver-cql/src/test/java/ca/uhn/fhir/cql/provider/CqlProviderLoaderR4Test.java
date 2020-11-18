package ca.uhn.fhir.cql.provider;

import ca.uhn.fhir.cql.BaseCqlR4Test;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CqlProviderLoaderR4Test extends BaseCqlR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderLoaderR4Test.class);

	@Autowired
	CqlProviderLoader myCqlProviderLoader;

	@Autowired
	private ResourceProviderFactory myResourceProviderFactory;

	@Test
	public void contextLoads() {
		ourLog.info("YAY THE R4 CONTEXT LOADED!");
		myCqlProviderLoader.loadProvider();
		myResourceProviderFactory.createProviders();
		ourLog.info("Holy I can't believe we made it here.");
	}
}
