package ca.uhn.fhir.jpa.config.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.HapiFhirHibernateJpaDialect;
import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public final class HapiEntityManagerFactoryUtil {
	private HapiEntityManagerFactoryUtil() {}
	/**
	 * This method provides a partially completed entity manager
	 * factory with HAPI FHIR customizations
	 */
	public static LocalContainerEntityManagerFactoryBean entityManagerFactory(ConfigurableListableBeanFactory myConfigurableListableBeanFactory, FhirContext theFhirContext) {
		LocalContainerEntityManagerFactoryBean retVal = new HapiFhirLocalContainerEntityManagerFactoryBean(myConfigurableListableBeanFactory);
		configureEntityManagerFactory(retVal, theFhirContext);
		return retVal;
	}

	public static void configureEntityManagerFactory(LocalContainerEntityManagerFactoryBean theFactory, FhirContext theFhirContext) {
		theFactory.setJpaDialect(new HapiFhirHibernateJpaDialect(theFhirContext.getLocalizer()));
		theFactory.setPackagesToScan("ca.uhn.fhir.jpa.model.entity", "ca.uhn.fhir.jpa.entity");
		theFactory.setPersistenceProvider(new HibernatePersistenceProvider());
	}

}
