package ca.uhn.hapi.fhir.cdshooks.svc.cr;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.common.IResourceLoader;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCache;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.cache.IResourceVersionSvc;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCacheFactory;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCacheRefresherImpl;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerRegistryImpl;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceRegistry;
import ca.uhn.hapi.fhir.cdshooks.config.CdsHooksConfig;
import ca.uhn.hapi.fhir.cdshooks.config.TestCdsHooksConfig;
import ca.uhn.hapi.fhir.cdshooks.controller.CdsHooksController;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
public class CdsCrHooksTest extends BaseCrTest {
	@Autowired
	ICdsServiceRegistry myCdsHooksRegistry;

	MockMvc myMockMvc;

	@Autowired
	ResourceChangeListenerRegistryImpl myResourceChangeListenerRegistry;
	@Autowired
	ResourceChangeListenerCacheFactory myResourceChangeListenerCacheFactory;
	@MockBean
	private IResourceVersionSvc myResourceVersionSvc;
	@MockBean
	private ResourceChangeListenerCacheRefresherImpl myResourceChangeListenerCacheRefresher;
	@MockBean
	private InMemoryResourceMatcher myInMemoryResourceMatcher;
	@MockBean
	private SearchParamMatcher mySearchParamMatcher;

	private final IResourceChangeListener myTestListener = mock(IResourceChangeListener.class);
	private static final SearchParameterMap ourMap = SearchParameterMap.newSynchronous();

	@Configuration
	@Import({TestCdsCrHooksConfig.class})
	static class SpringContext {
		@Autowired
		ResourceChangeListenerCacheFactory myResourceChangeListenerCacheFactory;

		@Bean
		public IResourceChangeListenerRegistry resourceChangeListenerRegistry(InMemoryResourceMatcher theInMemoryResourceMatcher, FhirContext theFhirContext) {
			return new ResourceChangeListenerRegistryImpl(theFhirContext, myResourceChangeListenerCacheFactory, theInMemoryResourceMatcher);
		}
	}

	@BeforeEach
	public void before() throws Exception {
		Set<IResourceChangeListenerCache> entries = new HashSet<>();
		IResourceChangeListenerCache cache = myResourceChangeListenerCacheFactory.newResourceChangeListenerCache(PLAN_DEFINITION_RESOURCE_NAME, ourMap, myTestListener, DateUtils.MILLIS_PER_HOUR);
		entries.add(cache);
		when(myInMemoryResourceMatcher.canBeEvaluatedInMemory(any(), any())).thenReturn(InMemoryMatchResult.successfulMatch());
		super.before();
		myMockMvc = MockMvcBuilders.standaloneSetup(new CdsHooksController(myCdsHooksRegistry)).build();
	}

	public Bundle loadBundle(String theLocation) {
		return loadBundle(Bundle.class, theLocation);
	}

	protected RequestDetails setupRequestDetails() {
		var requestDetails = new ServletRequestDetails();
		requestDetails.setServletRequest(new MockHttpServletRequest());
		requestDetails.setServer(ourRestServer);
		requestDetails.setFhirServerBase(TEST_ADDRESS);
		return requestDetails;
	}

	@Test
	void testHelloWorld() throws Exception {
		loadBundle("Bundle-HelloWorldContent.json");
		Thread.sleep(1500);

		myMockMvc
			.perform(get(CdsHooksController.BASE))
			.andDo(print());
	}
}
