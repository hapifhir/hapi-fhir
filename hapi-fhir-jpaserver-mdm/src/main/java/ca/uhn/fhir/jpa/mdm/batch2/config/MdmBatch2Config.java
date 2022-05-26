package ca.uhn.fhir.jpa.mdm.batch2.config;

import ca.uhn.fhir.jpa.api.svc.IGoldenResourceSearchSvc;
import ca.uhn.fhir.jpa.batch.mdm.batch2.GoldenResourceSearchSvcImpl;
import org.springframework.context.annotation.Bean;

public class MdmBatch2Config {
	@Bean
	IGoldenResourceSearchSvc goldenResourceSearchSvcImpl() {
		return new GoldenResourceSearchSvcImpl();
	}
}
