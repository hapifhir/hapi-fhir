package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.empi.rules.svc.EmpiRulesSvc;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.IEmpiInterceptor;
import ca.uhn.fhir.jpa.empi.config.EmpiConfig;
import ca.uhn.fhir.jpa.empi.provider.EmpiProviderLoader;
import ca.uhn.fhir.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class EmpiInitializer {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiInitializer.class);
	public static final String EMPI_CONSUMER_COUNT_DEFAULT = "5";

	@Autowired
	IEmpiInterceptor myEmpiInterceptor;
	@Autowired
	IInterceptorService myInterceptorService;
	@Autowired
	EmpiProviderLoader myEmpiProviderLoader;
	@Autowired
	EmpiRulesSvc myEmpiRulesSvc;
	@Autowired
	EmpiConfig myEmpiConfig;

	@PostConstruct
	public void init() throws Exception {
		if (!myEmpiConfig.isEmpiEnabled()) {
			return;
		}

		loadEmpiConfigScript();

		myInterceptorService.registerInterceptor(myEmpiInterceptor);
		ourLog.info("EMPI interceptor registered");

		myEmpiProviderLoader.loadProvider();
	}

	private void loadEmpiConfigScript() throws Exception {
		// FIXME KHS if user changes empi rules, fail on module startup if they didn't also increment rule version number
		EmpiRulesJson rules = JsonUtil.deserialize(myEmpiConfig.getScriptText(), EmpiRulesJson.class);
		myEmpiRulesSvc.setEmpiRules(rules);
	}
}
