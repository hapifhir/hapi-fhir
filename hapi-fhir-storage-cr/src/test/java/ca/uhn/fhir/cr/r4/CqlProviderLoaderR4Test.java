package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4Test;
import ca.uhn.fhir.cr.config.CrProviderLoader;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class CqlProviderLoaderR4Test extends BaseCrR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderLoaderR4Test.class);

	@Autowired
	IPagingProvider myPagingProvider;

	@Autowired
	CrProviderLoader myCqlProviderLoader;

	@Autowired
	private ResourceProviderFactory myResourceProviderFactory;

	@Test
	public void contextLoads() {
		myResourceProviderFactory.createProviders();
		ourLog.info("The CqlProviderLoader loaded and was able to create Providers.");
	}

	@Override
	public IPagingProvider getPagingProvider() {
		return this.myPagingProvider;
	}
}
