package ca.uhn.fhir.fhirpath;

import ca.uhn.fhir.test.BaseTest;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;

public abstract class BaseValidationTestWithInlineMocks extends BaseTest {

	/**
	 * Avoid memory leaks due to inline mocks. See https://github.com/mockito/mockito/pull/1619
	 * Note that we don't declare the test classes in this
	 * module with @ExtendWith(MockitoExtension.class) because
	 * Mockito 5 seems to not like mixing inline mocks with
	 * extension managed ones.
	 */
	@AfterEach
	public void clearMocks() {
		Mockito.framework().clearInlineMocks();
	}

}
