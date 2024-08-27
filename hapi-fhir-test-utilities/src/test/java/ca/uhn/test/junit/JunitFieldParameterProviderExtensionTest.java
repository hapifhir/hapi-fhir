package ca.uhn.test.junit;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JunitFieldParameterProviderExtensionTest {
	/** Verify statics annotated with @{@link JunitFieldProvider} are available to static junit methods. */
	@Nested
	class AnnotationRegistration implements IParameterizedTestTemplate {
		@JunitFieldProvider
		static final CaseSupplyingFixture ourFixture = new CaseSupplyingFixture();
	}
	/** Verify explicit registration of the extension.*/
	@Nested
	class ExplicitRegistration implements IParameterizedTestTemplate {
		static final CaseSupplyingFixture ourFixture = new CaseSupplyingFixture();
		@RegisterExtension
		static final JunitFieldParameterProviderExtension ourExtension = new JunitFieldParameterProviderExtension(ourFixture);
	}

	static class CaseSupplyingFixture {
		public List<Integer> getCases() {
			return List.of(1,2,3);
		}
	}
	interface IParameterizedTestTemplate {
		@ParameterizedTest
		// intellij complains when a static source requires params
		@SuppressWarnings("JUnitMalformedDeclaration")
		@MethodSource("testCaseSource")
		default void testStaticFactoryBound(int theTestCase) {
			// given
			assertTrue(theTestCase > 0);
		}

		static List<Integer> testCaseSource(CaseSupplyingFixture theFixture) {
			return theFixture.getCases();
		}
	}
}
