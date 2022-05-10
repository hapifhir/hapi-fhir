package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.google.common.collect.ImmutableMap;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchMethodBindingTest {

	private static final TestResourceProvider TEST_RESOURCE_PROVIDER = new TestResourceProvider();
	private static final Logger ourLog = LoggerFactory.getLogger(SearchMethodBindingTest.class);
	private FhirContext fhirContext;

	@BeforeEach
	public void setUp() {
		fhirContext = mock(FhirContext.class);
		RuntimeResourceDefinition definition = mock(RuntimeResourceDefinition.class);
		when(definition.isBundle()).thenReturn(false);
		when(fhirContext.getResourceDefinition(any(Class.class))).thenReturn(definition);
	}

	@Test // fails
	public void methodShouldNotMatchWhenUnderscoreQueryParameter() throws NoSuchMethodException {
		assertThat(getBinding("param", String.class).incomingServerRequestMatchesMethod(
			mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}, "_include", new String[]{"test"}))),
			Matchers.is(MethodMatchEnum.NONE));
		assertThat(getBinding("paramAndTest", String.class, String.class).incomingServerRequestMatchesMethod(
			mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}, "_include", new String[]{"test"}))),
			Matchers.is(MethodMatchEnum.NONE));
		assertThat(getBinding("paramAndUnderscoreTest", String.class, String.class).incomingServerRequestMatchesMethod(
			mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}, "_include", new String[]{"test"}))),
			Matchers.is(MethodMatchEnum.NONE));
	}

	@Test
	public void methodShouldNotMatchWhenExtraQueryParameter() throws NoSuchMethodException {
		assertThat(getBinding("param", String.class).incomingServerRequestMatchesMethod(
			mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}, "extra", new String[]{"test"}))),
			Matchers.is(MethodMatchEnum.NONE));
		assertThat(getBinding("paramAndTest", String.class, String.class).incomingServerRequestMatchesMethod(
			mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}, "extra", new String[]{"test"}))),
			Matchers.is(MethodMatchEnum.NONE));
		assertThat(getBinding("paramAndUnderscoreTest", String.class, String.class).incomingServerRequestMatchesMethod(
			mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}, "extra", new String[]{"test"}))),
			Matchers.is(MethodMatchEnum.NONE));
	}

	@Test
	public void methodMatchesOwnParams() throws NoSuchMethodException {
		assertThat(getBinding("param", String.class).incomingServerRequestMatchesMethod(
			mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}))),
			Matchers.is(MethodMatchEnum.EXACT));
		assertThat(getBinding("paramAndTest", String.class, String.class).incomingServerRequestMatchesMethod(
			mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}, "test", new String[]{"test"}))),
			Matchers.is(MethodMatchEnum.EXACT));
		assertThat(getBinding("paramAndUnderscoreTest", String.class, String.class).incomingServerRequestMatchesMethod(
			mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}, "_test", new String[]{"test"}))),
			Matchers.is(MethodMatchEnum.EXACT));
	}

	@Test
	public void methodMatchesChainBlacklist() throws NoSuchMethodException {
		SearchMethodBinding binding = getBinding("withChainBlacklist", ReferenceParam.class);
		ourLog.info("Testing binding: {}", binding);
		assertThat(binding.incomingServerRequestMatchesMethod(
			mockSearchRequest(ImmutableMap.of("refChainBlacklist.badChain", new String[]{"foo"}))),
			Matchers.is(MethodMatchEnum.NONE));
		assertThat(binding.incomingServerRequestMatchesMethod(
			mockSearchRequest(ImmutableMap.of("refChainBlacklist.goodChain", new String[]{"foo"}))),
			Matchers.is(MethodMatchEnum.EXACT));
	}

	private SearchMethodBinding getBinding(String name, Class<?>... parameters) throws NoSuchMethodException {
		return new SearchMethodBinding(IBaseResource.class,
			IBaseResource.class,
			TestResourceProvider.class.getMethod(name, parameters),
			fhirContext,
			TEST_RESOURCE_PROVIDER);
	}

	private RequestDetails mockSearchRequest(Map<String, String[]> params) {
		RequestDetails requestDetails = mock(RequestDetails.class);
		when(requestDetails.getOperation()).thenReturn("_search");
		when(requestDetails.getRequestType()).thenReturn(RequestTypeEnum.GET);
		when(requestDetails.getParameters()).thenReturn(params);

		when(requestDetails.getUnqualifiedToQualifiedNames()).thenAnswer(t -> {
			RequestDetails rd = new ServletRequestDetails();
			rd.setParameters(params);
			return rd.getUnqualifiedToQualifiedNames();
		});

		return requestDetails;
	}

	private static class TestResourceProvider {

		@Search
		public IBaseResource param(@RequiredParam(name = "param") String param) {
			return null;
		}

		@Search
		public IBaseResource paramAndTest(@RequiredParam(name = "param") String param, @OptionalParam(name = "test") String test) {
			return null;
		}

		@Search
		public IBaseResource paramAndUnderscoreTest(@RequiredParam(name = "param") String param, @OptionalParam(name = "_test") String test) {
			return null;
		}

		@Search
		public IBaseResource withChainBlacklist(@OptionalParam(name = "refChainBlacklist", chainWhitelist = "goodChain", chainBlacklist = "badChain") ReferenceParam param) {
			return null;
		}

	}

}
