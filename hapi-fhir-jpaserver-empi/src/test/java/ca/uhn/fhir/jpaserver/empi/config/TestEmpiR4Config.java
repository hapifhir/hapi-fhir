package ca.uhn.fhir.jpaserver.empi.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.jpaserver.rules.EmpiResourceComparator;
import ca.uhn.fhir.jpaserver.rules.EmpiRulesJson;
import ca.uhn.fhir.jpaserver.test.dao.HashMapBackedDaoRegistry;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.util.JsonUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestEmpiR4Config {
	@Autowired
	private FhirContext myFhirContext;

	private DefaultResourceLoader myResourceLoader = new DefaultResourceLoader();

	@Bean
	public EmpiResourceComparator empiResourceComparator() throws IOException {
		Resource resource = myResourceLoader.getResource("empi/empi-rules.json");
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
		EmpiRulesJson rules = JsonUtil.deserialize(json, EmpiRulesJson.class);
		return new EmpiResourceComparator(fhirContext(), rules);
	}

	@Bean
	public FhirContext fhirContext() {
		return FhirContext.forR4();
	}

	@Bean
	public DaoRegistry daoRegistry() {
		return new HashMapBackedDaoRegistry();
	}

	@Bean
	public ModelConfig modelConfig() {
		return new ModelConfig();
	}

	@Bean
	ISearchParamProvider myISearchParamProvider() {
		ISearchParamProvider mockProvider = mock(ISearchParamProvider.class);
		when(mockProvider.search(any())).thenReturn(new SimpleBundleProvider(Collections.emptyList()));
		when(mockProvider.refreshCache(any(), anyLong())).thenReturn(0);
		return mockProvider;
	}

	@Bean
	public ISchedulerService schedulerSvc() {
		return mock(ISchedulerService.class);
	}

	@Bean
	public InterceptorService interceptorService() {
		return new InterceptorService("empi-test-interceptor");
	}


	@Primary
	@Bean
	public IValidationSupport validationSupportChain() {
		return new DefaultProfileValidationSupport(myFhirContext);
	}
}
