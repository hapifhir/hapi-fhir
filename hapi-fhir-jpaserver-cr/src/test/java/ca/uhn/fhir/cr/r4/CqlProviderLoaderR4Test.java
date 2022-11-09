package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.TestCrConfig;
import ca.uhn.fhir.cr.common.provider.CrProviderLoader;
import ca.uhn.fhir.cr.config.CrR4Config;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestCrConfig.class, CrR4Config.class})
public class CqlProviderLoaderR4Test extends BaseJpaR4Test {
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
