package ca.uhn.fhir.test.utilities;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

	public static final String SCHEDULING_DISABLED = "scheduling_disabled";
	public static final String SCHEDULING_DISABLED_EQUALS_TRUE = "scheduling_disabled=true";

	private final Environment myEnvironment;

	public UnregisterScheduledProcessor(Environment theEnv) {
		myEnvironment = theEnv;
	}

	@Override
	public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
		String schedulingDisabled = myEnvironment.getProperty(SCHEDULING_DISABLED);
		if ("true".equals(schedulingDisabled)) {
			for (String beanName : beanFactory.getBeanNamesForType(ScheduledAnnotationBeanPostProcessor.class)) {
				((DefaultListableBeanFactory) beanFactory).removeBeanDefinition(beanName);
			}

			for (String beanName : beanFactory.getBeanNamesForType(ExecutorConfigurationSupport.class)) {
				ExecutorConfigurationSupport executorConfigSupport = beanFactory.getBean(beanName, ExecutorConfigurationSupport.class);
				executorConfigSupport.shutdown();
			}
		}

	}
}
