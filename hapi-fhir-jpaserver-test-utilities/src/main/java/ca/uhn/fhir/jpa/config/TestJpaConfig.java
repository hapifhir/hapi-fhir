package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.persistence.EntityManagerFactory;

public class TestJpaConfig {
	@Bean
	public DaoConfig daoConfig() {
		DaoConfig daoConfig = new DaoConfig();
		return daoConfig;
	}

	@Bean
	public ModelConfig modelConfig() {
		return daoConfig().getModelConfig();
	}

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager retVal = new JpaTransactionManager();
		retVal.setEntityManagerFactory(entityManagerFactory);
		return retVal;
	}
}
