package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.CALLS_REAL_METHODS;

class OperationOutcomeUtilTest {
	@org.junit.jupiter.params.ParameterizedTest
	@org.junit.jupiter.params.provider.ValueSource(ints = {0, 1, 2})
	void testGetAllIssueDiagnostics_various_items(int numDiagnostics) {
		// Arrange
		FhirContext ctx = Mockito.mock(FhirContext.class);
		IBaseOperationOutcome outcome = Mockito.mock(IBaseOperationOutcome.class);
		List<String> knownDiagnostics = java.util.stream.IntStream.range(0, numDiagnostics)
			.mapToObj(i -> "diag" + i)
			.collect(java.util.stream.Collectors.toList());

		try (MockedStatic<OperationOutcomeUtil> operationUtils = Mockito.mockStatic(OperationOutcomeUtil.class, CALLS_REAL_METHODS)) {
			operationUtils.when(() -> OperationOutcomeUtil.getIssueCount(any(), any())).thenReturn(knownDiagnostics.size());
			operationUtils.when(() -> OperationOutcomeUtil.getIssueDiagnostics(any(), any(), anyInt()))
				.thenAnswer(invocation -> {
					int idx = invocation.getArgument(2);
					return knownDiagnostics.get(idx);
				});

			// Act
			List<String> resultDiagnostics = OperationOutcomeUtil.getAllIssueDiagnostics(ctx, outcome);

			// Assert
			assertEquals(knownDiagnostics, resultDiagnostics);
		}
	}
}
