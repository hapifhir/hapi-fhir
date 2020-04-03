package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.empi.api.IEmpiInterceptor;
import ca.uhn.fhir.empi.rules.config.IEmpiConfig;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.empi.provider.EmpiProviderLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class EmpiInitializer {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiInitializer.class);
	public static final String EMPI_CONSUMER_COUNT_DEFAULT = "5";

	@Autowired
	BeanFactory myBeanFactory;
	@Autowired
	IInterceptorService myInterceptorService;
	@Autowired
	IEmpiConfig myEmpiConfig;

	@PostConstruct
	public void init() {
		if (!myEmpiConfig.isEnabled()) {
			return;
		}
		IEmpiInterceptor empiInterceptor = myBeanFactory.getBean(IEmpiInterceptor.class);
		myInterceptorService.registerInterceptor(empiInterceptor);
		empiInterceptor.start();
		ourLog.info("EMPI interceptor registered");

		EmpiProviderLoader empiProviderLoader = myBeanFactory.getBean(EmpiProviderLoader.class);
		empiProviderLoader.loadProvider();
	}
}
