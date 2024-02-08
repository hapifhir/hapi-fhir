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

		assertThat(myBean.getJpaPropertyMap().get("theKey")).isEqualTo("theHookClass");
	}

	@Test
	void testAddHibernateHookToSomeRegisteredHooks() {
		// start with some hooks already registered
		myBean.getJpaPropertyMap().put("theKey", "hook1,hook2");

		myBean.addHibernateHook("theKey", "theHookClass");

		assertThat(myBean.getJpaPropertyMap().get("theKey")).isEqualTo("hook1,hook2,theHookClass");
	}

}
