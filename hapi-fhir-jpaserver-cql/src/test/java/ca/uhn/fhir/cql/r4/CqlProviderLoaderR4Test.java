package ca.uhn.fhir.cql.r4;

import ca.uhn.fhir.cql.BaseCqlR4Test;
import ca.uhn.fhir.cql.common.provider.CqlProviderLoader;
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
		myCqlProviderLoader.loadProvider();
		myResourceProviderFactory.createProviders();
		ourLog.info("The CqlProviderLoader loaded and was able to create Providers.");
	}
}
