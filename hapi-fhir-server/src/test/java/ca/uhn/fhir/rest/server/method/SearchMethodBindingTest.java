package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import com.google.common.collect.ImmutableMap;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchMethodBindingTest {

    private static final TestResourceProvider TEST_RESOURCE_PROVIDER = new TestResourceProvider();

    private FhirContext fhirContext;

    @Before
    public void setUp() {
        fhirContext = mock(FhirContext.class);
        RuntimeResourceDefinition definition = mock(RuntimeResourceDefinition.class);
        when(definition.isBundle()).thenReturn(false);
        when(fhirContext.getResourceDefinition(any(Class.class))).thenReturn(definition);
    }

    @Test // fails
    public void methodShouldNotMatchWhenUnderscoreQueryParameter() throws NoSuchMethodException {
        Assert.assertThat(getBinding("param", String.class).incomingServerRequestMatchesMethod(
            mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}, "_include", new String[]{"test"}))),
            Matchers.is(false));
        Assert.assertThat(getBinding("paramAndTest", String.class, String.class).incomingServerRequestMatchesMethod(
            mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}, "_include", new String[]{"test"}))),
            Matchers.is(false));
        Assert.assertThat(getBinding("paramAndUnderscoreTest", String.class, String.class).incomingServerRequestMatchesMethod(
            mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}, "_include", new String[]{"test"}))),
            Matchers.is(false));
    }

    @Test
    public void methodShouldNotMatchWhenExtraQueryParameter() throws NoSuchMethodException {
        Assert.assertThat(getBinding("param", String.class).incomingServerRequestMatchesMethod(
            mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}, "extra", new String[]{"test"}))),
            Matchers.is(false));
        Assert.assertThat(getBinding("paramAndTest", String.class, String.class).incomingServerRequestMatchesMethod(
            mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}, "extra", new String[]{"test"}))),
            Matchers.is(false));
        Assert.assertThat(getBinding("paramAndUnderscoreTest", String.class, String.class).incomingServerRequestMatchesMethod(
            mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}, "extra", new String[]{"test"}))),
            Matchers.is(false));
    }

    @Test
    public void methodMatchesOwnParams() throws NoSuchMethodException {
        Assert.assertThat(getBinding("param", String.class).incomingServerRequestMatchesMethod(
            mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}))),
            Matchers.is(true));
        Assert.assertThat(getBinding("paramAndTest", String.class, String.class).incomingServerRequestMatchesMethod(
            mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}, "test", new String[]{"test"}))),
            Matchers.is(true));
        Assert.assertThat(getBinding("paramAndUnderscoreTest", String.class, String.class).incomingServerRequestMatchesMethod(
            mockSearchRequest(ImmutableMap.of("param", new String[]{"value"}, "_test", new String[]{"test"}))),
            Matchers.is(true));
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

    }

}
