package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class SearchParamRegistryImplTest {
	@Autowired
	SearchParamRegistryImpl mySearchParamRegistry;

	@MockBean
	private ISchedulerService mySchedulerService;
	@MockBean
	private ISearchParamProvider mySearchParamProvider;
	@MockBean
	private ModelConfig myModelConfig;
	@MockBean
	private IInterceptorService myInterceptorBroadcaster;

	@Configuration
	static class SpringConfig {
		@Bean
		FhirContext fhirContext() { return FhirContext.forR4(); }
		@Bean
		ISearchParamRegistry searchParamRegistry() { return new SearchParamRegistryImpl(); }
		@Bean
		SearchParameterCanonicalizer searchParameterCanonicalizer(FhirContext theFhirContext) {
			return new SearchParameterCanonicalizer(theFhirContext);
		}
	}

	private int myAnswerCount = 0;

	@BeforeEach
	public void before() {
		myAnswerCount = 0;
	}

	@Test
	public void testRefreshAfterExpiry() {
		when(mySearchParamProvider.search(any())).thenReturn(new SimpleBundleProvider());

		mySearchParamRegistry.requestRefresh();
		assertEquals(146, mySearchParamRegistry.doRefresh(100000));

		// Second time we don't need to run because we ran recently
		assertEquals(0, mySearchParamRegistry.doRefresh(100000));

		assertEquals(146, mySearchParamRegistry.getActiveSearchParams().size());
	}

	@Test
	public void testRefreshCacheIfNecessary() {

		when(mySearchParamProvider.search(any())).thenReturn(new SimpleBundleProvider());
		when(mySearchParamProvider.refreshCache(any(), anyLong())).thenAnswer(t -> {
			mySearchParamRegistry.doRefresh(t.getArgument(1, Long.class));
			return 0;
		});

		mySearchParamRegistry.requestRefresh();

		assertTrue(mySearchParamRegistry.refreshCacheIfNecessary());
		assertFalse(mySearchParamRegistry.refreshCacheIfNecessary());

		mySearchParamRegistry.requestRefresh();
		assertTrue(mySearchParamRegistry.refreshCacheIfNecessary());
	}

	@Test
	public void testGetActiveUniqueSearchParams_Empty() {
		assertThat(mySearchParamRegistry.getActiveUniqueSearchParams("Patient"), Matchers.empty());
	}

	@Test
	public void testGetActiveSearchParams() {
		when(mySearchParamProvider.search(any())).thenReturn(new SimpleBundleProvider());
		when(mySearchParamProvider.refreshCache(any(), anyLong())).thenAnswer(t -> {
			if (myAnswerCount == 0) {
				myAnswerCount++;
				throw new InternalErrorException("this is an error!");
			}

			mySearchParamRegistry.doRefresh(0);

			return 0;
		});

		Map<String, RuntimeSearchParam> outcome = mySearchParamRegistry.getActiveSearchParams("Patient");
	}

	@Test
	public void testExtractExtensions() {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setCode("foo");
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.setType(Enumerations.SearchParamType.TOKEN);
		searchParameter.setExpression("Patient.name");
		searchParameter.addBase("Patient");
		searchParameter.addExtension("http://foo", new StringType("FOO"));
		searchParameter.addExtension("http://bar", new StringType("BAR"));

		// Invalid entries
		searchParameter.addExtension("http://bar", null);
		searchParameter.addExtension(null, new StringType("BAR"));

		when(mySearchParamProvider.search(any())).thenReturn(new SimpleBundleProvider(searchParameter));
		when(mySearchParamProvider.refreshCache(any(), anyLong())).thenAnswer(t -> {
			mySearchParamRegistry.doRefresh(0);
			return 0;
		});

		mySearchParamRegistry.forceRefresh();
		Map<String, RuntimeSearchParam> outcome = mySearchParamRegistry.getActiveSearchParams("Patient");

		RuntimeSearchParam converted = outcome.get("foo");
		assertNotNull(converted);

		assertEquals(1, converted.getExtensions("http://foo").size());
		IPrimitiveType<?> value = (IPrimitiveType<?>) converted.getExtensions("http://foo").get(0).getValue();
		assertEquals("FOO", value.getValueAsString());

	}

}
