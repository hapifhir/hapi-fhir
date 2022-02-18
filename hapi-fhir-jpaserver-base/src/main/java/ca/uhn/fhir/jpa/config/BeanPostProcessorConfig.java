package ca.uhn.fhir.jpa.config;

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
