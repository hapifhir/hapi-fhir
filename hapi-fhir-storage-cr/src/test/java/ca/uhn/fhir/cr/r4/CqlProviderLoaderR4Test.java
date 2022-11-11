package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.CrR4Test;
import ca.uhn.fhir.cr.common.provider.CrProviderLoader;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class CqlProviderLoaderR4Test extends CrR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderLoaderR4Test.class);

	@Autowired
	CrProviderLoader myCqlProviderLoader;

	@Autowired
	private ResourceProviderFactory myResourceProviderFactory;

	@Test
	public void contextLoads() {
		myCqlProviderLoader.loadProvider();
		myResourceProviderFactory.createProviders();
		ourLog.info("The CqlProviderLoader loaded and was able to create Providers.");
	}
}
