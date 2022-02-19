package ca.uhn.fhir.jpa.config;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

@Configuration
// TODO KHS do we even need this class?
// TODO KHS find out why this Spring Config produces the message and if there's anything we can do about it:
// o.s.c.s.PostProcessorRegistrationDelegate$BeanPostProcessorChecker [PostProcessorRegistrationDelegate.java:376]
// Bean 'ca.uhn.fhir.jpa.config.BeanPostProcessorConfig'
// of type [ca.uhn.fhir.jpa.config.BeanPostProcessorConfig$$EnhancerBySpringCGLIB$$8d867950]
// is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
public class BeanPostProcessorConfig {
	@Bean
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

}
