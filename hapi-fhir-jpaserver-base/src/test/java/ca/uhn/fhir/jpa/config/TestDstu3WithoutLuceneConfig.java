package ca.uhn.fhir.jpa.config;

import java.util.Properties;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ca.uhn.fhir.jpa.dao.FulltextSearchSvcImpl;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;

@Configuration
@EnableTransactionManagement()
public class TestDstu3WithoutLuceneConfig extends TestDstu3Config {

	/**
	 * Disable fulltext searching
	 */
	@Override
	public IFulltextSearchSvc searchDaoDstu3() {
		return null;
	}

	@Override
	@Bean()
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean retVal = super.entityManagerFactory();
		retVal.setJpaProperties(jpaProperties());
		return retVal;
	}

	private Properties jpaProperties() {
		Properties extraProperties = new Properties();
		extraProperties.put("hibernate.format_sql", "false");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put("hibernate.hbm2ddl.auto", "update");
		extraProperties.put("hibernate.dialect", "ca.uhn.fhir.jpa.util.DerbyTenSevenHapiFhirDialect");
		extraProperties.put("hibernate.search.autoregister_listeners", "false");
		return extraProperties;
	}

}
