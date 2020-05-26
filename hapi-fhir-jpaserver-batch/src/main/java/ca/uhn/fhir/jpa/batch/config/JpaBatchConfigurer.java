package ca.uhn.fhir.jpa.batch.config;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class JpaBatchConfigurer extends DefaultBatchConfigurer {

	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;

	@Override
	public PlatformTransactionManager getTransactionManager() {
		return myPlatformTransactionManager;
	}
}
