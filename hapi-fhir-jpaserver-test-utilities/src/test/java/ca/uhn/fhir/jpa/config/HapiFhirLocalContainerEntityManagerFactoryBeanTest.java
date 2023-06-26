package ca.uhn.fhir.jpa.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import static org.junit.jupiter.api.Assertions.*;

class HapiFhirLocalContainerEntityManagerFactoryBeanTest {
	HapiFhirLocalContainerEntityManagerFactoryBean myBean = new HapiFhirLocalContainerEntityManagerFactoryBean(new DefaultListableBeanFactory());

	@Test
	void testAddHibernateHookToEmptyProperty() {
		// start empty

		myBean.addHibernateHook("theKey", "theHookClass");

		assertEquals("theHookClass", myBean.getJpaPropertyMap().get("theKey"));
	}

	@Test
	void testAddHibernateHookToSomeRegisteredHooks() {
		// start with some hooks already registered
		myBean.getJpaPropertyMap().put("theKey", "hook1,hook2");

		myBean.addHibernateHook("theKey", "theHookClass");

		assertEquals("hook1,hook2,theHookClass", myBean.getJpaPropertyMap().get("theKey"));
	}

}
