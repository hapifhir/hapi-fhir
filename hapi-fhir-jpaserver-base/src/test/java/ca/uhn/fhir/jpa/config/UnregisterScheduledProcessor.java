package ca.uhn.fhir.jpa.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.concurrent.ExecutorConfigurationSupport;

/**
 * This bean postprocessor disables all scheduled tasks. It is intended
 * only to be used in unit tests in circumstances where scheduled
 * tasks cause issues.
 */
public class UnregisterScheduledProcessor implements BeanFactoryPostProcessor {

	private final Environment myEnvironment;

	public UnregisterScheduledProcessor(Environment theEnv) {
		myEnvironment = theEnv;
	}

	@Override
	public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
		String schedulingDisabled = myEnvironment.getProperty("scheduling_disabled");
		if ("true".equals(schedulingDisabled)) {
			for (String beanName : beanFactory.getBeanNamesForType(ScheduledAnnotationBeanPostProcessor.class)) {
				((DefaultListableBeanFactory) beanFactory).removeBeanDefinition(beanName);
			}

			for (String beanName : beanFactory.getBeanNamesForType(ExecutorConfigurationSupport.class)) {
				ExecutorConfigurationSupport executorConfigSupport = ((DefaultListableBeanFactory) beanFactory).getBean(beanName, ExecutorConfigurationSupport.class);
				executorConfigSupport.shutdown();
			}
		}

	}
}
