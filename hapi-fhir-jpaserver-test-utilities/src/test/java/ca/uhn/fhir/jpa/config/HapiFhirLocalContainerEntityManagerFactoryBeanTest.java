package ca.uhn.fhir.jpa.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import static org.assertj.core.api.Assertions.assertThat;

class HapiFhirLocalContainerEntityManagerFactoryBeanTest {
	HapiFhirLocalContainerEntityManagerFactoryBean myBean = new HapiFhirLocalContainerEntityManagerFactoryBean(new DefaultListableBeanFactory());

	@Test
	void testAddHibernateHookToEmptyProperty() {
		// start empty

		myBean.addHibernateHook("theKey", "theHookClass");

		assertThat(myBean.getJpaPropertyMap()).containsEntry("theKey", "theHookClass");
	}

	@Test
	void testAddHibernateHookToSomeRegisteredHooks() {
		// start with some hooks already registered
		myBean.getJpaPropertyMap().put("theKey", "hook1,hook2");

		myBean.addHibernateHook("theKey", "theHookClass");

		assertThat(myBean.getJpaPropertyMap()).containsEntry("theKey", "hook1,hook2,theHookClass");
	}

}
