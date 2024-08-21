package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.jupiter.api.Assertions.fail;

@ContextConfiguration(classes = {
	TestHSearchAddInConfig.NoFT.class
})
public class KeyTypeOverrideTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(KeyTypeOverrideTest.class);

	@Inject
	EntityManagerFactory myEntityManagerFactory;

	@Test
	void empty() {
	    // given

		try (var em = myEntityManagerFactory.createEntityManager()) {
			newTxTemplate().execute((status)->{
				em.createQuery("from ResourceTable").getResultStream()
					.forEach(e->{
						ourLog.info("found resource");
					});
				return null;
			});
		}
	    
	    // when
	    // then
	}
	
}
