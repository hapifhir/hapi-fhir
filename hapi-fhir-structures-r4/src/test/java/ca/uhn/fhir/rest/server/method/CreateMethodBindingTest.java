package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


@ExtendWith(MockitoExtension.class)
public class CreateMethodBindingTest {

	private FhirContext myCtx = FhirContext.forR4();

	@Test
	public void testInvalidMethod() throws NoSuchMethodException {

		class MyClass {

			@Create
			public void create() {
				// nothing
			}

		}

		Method method = MyClass.class.getMethod("create");
		try {
			new CreateMethodBinding(method, myCtx, new MyClass());
			fail();		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).contains("is a @Create method but it does not return class ca.uhn.fhir.rest.api.MethodOutcome");
		}
	}


}
