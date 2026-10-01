package ca.uhn.fhir.rest.server;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.IMethodBinding;
import ca.uhn.fhir.rest.server.method.PageMethodBinding;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceBindingTest {
	@Mock
	FhirContext ourFhirContext;

	ResourceBinding myResourceBinding = new ResourceBinding();

	private final Logger myLogger = (Logger) LoggerFactory.getLogger(ResourceBinding.class);
	private final ListAppender<ILoggingEvent> myListAppender = new ListAppender<>();

	// Created by claude-opus-5-5
	@BeforeEach
	void beforeEach() {
		myListAppender.start();
		myLogger.addAppender(myListAppender);
	}

	// Created by claude-opus-5-5
	@AfterEach
	void afterEach() {
		myLogger.detachAppender(myListAppender);
	}

	@Test
	public void testFILO() throws NoSuchMethodException {
		// setup
		Method method = ResourceBindingTest.class.getMethod("testFILO");
		BaseMethodBinding first = new PageMethodBinding(ourFhirContext, method);
		BaseMethodBinding second = new PageMethodBinding(ourFhirContext, method);;

		// execute
		myResourceBinding.addMethod(first);
		myResourceBinding.addMethod(second);

		// verify
		List<IMethodBinding> list = myResourceBinding.getMethodBindings();
		assertThat(second).isNotEqualTo(first);
		assertEquals(second, list.get(0));
		assertEquals(first, list.get(1));
	}

	// Created by claude-opus-5-5
	@Test
	void addMethod_duplicateBindingKey_warningIdentifiesBothProviders() {
		// setup
		ProviderA existingProvider = new ProviderA();
		ProviderA newProvider = new ProviderA();
		IMethodBinding existing = mockBinding("public void Foo.bar()", existingProvider);
		IMethodBinding duplicate = mockBinding("public void Foo.bar()", newProvider);

		// execute
		myResourceBinding.addMethod(existing);
		myResourceBinding.addMethod(duplicate);

		// verify
		assertThat(myListAppender.list)
				.singleElement()
				.extracting(ILoggingEvent::getFormattedMessage)
				.asString()
				.contains("public void Foo.bar()")
				.contains(existingProvider.getClass().getName())
				.contains(newProvider.getClass().getName());
	}

	// Created by claude-opus-5-5
	@Test
	void addMethod_distinctBindingKeys_noWarning() {
		// setup
		IMethodBinding first = mock(IMethodBinding.class);
		when(first.getBindingKey()).thenReturn("public void Foo.bar()");
		IMethodBinding second = mock(IMethodBinding.class);
		when(second.getBindingKey()).thenReturn("public void Foo.baz()");

		// execute
		myResourceBinding.addMethod(first);
		myResourceBinding.addMethod(second);

		// verify
		assertThat(myListAppender.list).isEmpty();
	}

	// Created by claude-opus-5-5
	private static IMethodBinding mockBinding(String theBindingKey, Object theProvider) {
		IMethodBinding binding = mock(IMethodBinding.class);
		when(binding.getBindingKey()).thenReturn(theBindingKey);
		when(binding.getProvider()).thenReturn(theProvider);
		return binding;
	}

	// Created by claude-opus-5-5
	static class ProviderA {}
}
