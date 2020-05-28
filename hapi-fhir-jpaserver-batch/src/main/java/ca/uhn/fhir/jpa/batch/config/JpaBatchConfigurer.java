package ca.uhn.fhir.jpa.batch.config;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
/*//
@Component
public class JpaBatchConfigurer extends DefaultBatchConfigurer {

	@Autowired
	@Qualifier
	private JpaTransactionManager myPlatformTransactionManager;

	@Override
	public PlatformTransactionManager getTransactionManager() {
		return myPlatformTransactionManager;
	}

}
*/
