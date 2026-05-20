package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {

})
public class TestR4App extends BaseJpaR4Test {

	@Autowired
	private TerminologyUploaderProvider myTerminologyUploaderProvider;

	@RegisterExtension
	private RestfulServerExtension myServer = new RestfulServerExtension(FhirVersionEnum.R4)
		.withPort(8080)
		.withServer(s->{
			s.registerProviders(myResourceProviders.createProviders());
			s.registerProvider(myTerminologyUploaderProvider);
		});

	@Test
	void testInstallLoinc() throws InterruptedException {

		while (true) {
			myJobMaintenanceService.forceMaintenancePass();
			TestUtil.sleepAtLeast(10 * DateUtils.MILLIS_PER_SECOND);
		}
	}


}
