package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.PageMethodBinding;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class ResourceBindingTest {
	@Mock
	FhirContext ourFhirContext;

	ResourceBinding myResourceBinding = new ResourceBinding();

	@Test
	public void testFILO() throws NoSuchMethodException {
		// setup
		Method method = ResourceBindingTest.class.getMethod("testFILO");
		BaseMethodBinding<?> first = new PageMethodBinding(ourFhirContext, method);
		BaseMethodBinding<?> second = new PageMethodBinding(ourFhirContext, method);;

		// execute
		myResourceBinding.addMethod(first);
		myResourceBinding.addMethod(second);

		// verify
		List<BaseMethodBinding<?>> list = myResourceBinding.getMethodBindings();
		assertNotEquals(first, second);
		assertEquals(second, list.get(0));
		assertEquals(first, list.get(1));
	}
}
