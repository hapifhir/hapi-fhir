package ca.uhn.fhir.storage.test;

import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ca.uhn.fhir.storage.test.CircularQueueCaptureQueriesListenerAssertions.onCurrentThread;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CircularQueueCaptureQueriesListenerAssertionsTest {

	private final CircularQueueCaptureQueriesListener myListener = new CircularQueueCaptureQueriesListener();

	@Mock
	private QueryInfo myQueryInfo;
	@Mock
	private ExecutionInfo myExecutionInfo;

	@ParameterizedTest
	@ValueSource(strings = {"SELECT", "INSERT", "UPDATE", "DELETE"})
	void testFailingCount(String theVerb) {
		// Setup
		when(myExecutionInfo.getElapsedTime()).thenReturn(100L);
		when(myQueryInfo.getQuery()).thenReturn(theVerb, " foo");
		when(myQueryInfo.getParametersList()).thenReturn(List.of(List.of()));
		myListener.execute(myExecutionInfo, List.of(myQueryInfo));

		assertThatThrownBy(()-> {
			// Test
			assertThat(myListener).has(
				switch(theVerb) {
					case "SELECT" -> onCurrentThread().selectCount(2);
					case "INSERT" -> onCurrentThread().insertCount(2);
					case "UPDATE" -> onCurrentThread().updateCount(2);
					case "DELETE" -> onCurrentThread().deleteCount(2);
					default -> throw new IllegalStateException("Unknown verb: " + theVerb);
				}
			);
		})
			// Verify
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining(theVerb + "         1              | 1")
			.hasMessageContaining(theVerb + "     Expected[2] Actual[1]");

	}

	@Test
	void testFailingContains() {
		// Setup
		when(myExecutionInfo.getElapsedTime()).thenReturn(100L);
		when(myQueryInfo.getQuery()).thenReturn("SELECT foo FROM bar");
		when(myQueryInfo.getParametersList()).thenReturn(List.of(List.of()));
		myListener.execute(myExecutionInfo, List.of(myQueryInfo));

		assertThatThrownBy(()-> {
			// Test
			assertThat(myListener).has(
				onCurrentThread().selectSqlContains(0, "hello")
			);
		})
			// Verify
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("Expected SQL: SELECT")
			.hasMessageContaining("To contain: hello");

	}


}
