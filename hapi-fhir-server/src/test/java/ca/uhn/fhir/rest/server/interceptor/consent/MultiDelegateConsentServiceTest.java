package ca.uhn.fhir.rest.server.interceptor.consent;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import jakarta.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

import static ca.uhn.fhir.rest.server.interceptor.consent.MultiDelegateConsentService.withParallelVoting;
import static ca.uhn.fhir.rest.server.interceptor.consent.MultiDelegateConsentService.withSerialVoting;
import static ca.uhn.fhir.rest.server.interceptor.consent.IConsentVoteTest.PARALLEL_STREAM_EXPECTATION;
import static ca.uhn.fhir.rest.server.interceptor.consent.IConsentVoteTest.SERIAL_STREAM_EXPECTATION;
import static ca.uhn.fhir.rest.server.interceptor.consent.IConsentVoteTest.splitEnumsToStream;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MultiDelegateConsentServiceTest {
	SystemRequestDetails mySrd = new SystemRequestDetails();

	/**
	 * "parallel" means any voter can veto.
	 */
	@Nested
	class ParallelEvaluation {
		MultiDelegateConsentService myService;

		@ParameterizedTest
		@CsvSource(textBlock = PARALLEL_STREAM_EXPECTATION)
		void testStartOperation(String theInput, ConsentOperationStatusEnum theExpectedResult) {

			var services = splitEnumsToStream(theInput).map(result -> (IConsentService)ConstantConsentService.constantService(result)).toList();
			myService = withParallelVoting(services);

			var verdict = myService.startOperation(mySrd, IConsentContextServices.NULL_IMPL);

			assertEquals(theExpectedResult.getStatus(), verdict.getStatus());
		}


		@ParameterizedTest
		@CsvSource(textBlock = """
						, false
			true		, true
			false		, false
			false true	, true
			true false	, true
			""")
		void testCanSeeResource(String theInput, boolean theExpectedResult) {

			List<IConsentService> consentServices = Arrays.stream(defaultString(theInput).split(" +"))
				.map(String::trim)
				.map(Boolean::valueOf)
				.map(MultiDelegateConsentServiceTest::buildConsentShouldProcessCanSee)
				.toList();
			myService = withParallelVoting(consentServices);

			var result = myService.shouldProcessCanSeeResource(mySrd, IConsentContextServices.NULL_IMPL);

			assertEquals(theExpectedResult, result);
		}

		@ParameterizedTest
		@CsvSource(textBlock = PARALLEL_STREAM_EXPECTATION)
		void testCanSeeResource(String theInput, ConsentOperationStatusEnum theExpectedResult) {

			var services = splitEnumsToStream(theInput).map(result -> (IConsentService)ConstantConsentService.constantService(result)).toList();
			myService = withParallelVoting(services);

			var verdict = myService.canSeeResource(mySrd, null, IConsentContextServices.NULL_IMPL);

			assertEquals(theExpectedResult.getStatus(), verdict.getStatus());
		}

		@ParameterizedTest
		@CsvSource(textBlock = PARALLEL_STREAM_EXPECTATION)
		void testWillSeeResource(String theInput, ConsentOperationStatusEnum theExpectedResult) {

			var services = splitEnumsToStream(theInput).map(result -> (IConsentService)ConstantConsentService.constantService(result)).toList();
			myService = withParallelVoting(services);

			var verdict = myService.willSeeResource(mySrd, null, IConsentContextServices.NULL_IMPL);

			assertEquals(theExpectedResult.getStatus(), verdict.getStatus());
		}
	}

	/**
	 * "serial" means first comited vote wins
	 */
	@Nested
	class SerialEvaluation {
		MultiDelegateConsentService myService;

		@ParameterizedTest
		@CsvSource(textBlock = SERIAL_STREAM_EXPECTATION)
		void testStartOperation(String theInput, ConsentOperationStatusEnum theExpectedResult) {

			var services = splitEnumsToStream(theInput).map(result -> (IConsentService)ConstantConsentService.constantService(result)).toList();
			myService = withSerialVoting(services);

			var verdict = myService.startOperation(mySrd, IConsentContextServices.NULL_IMPL);

			assertEquals(theExpectedResult.getStatus(), verdict.getStatus());
		}

		@ParameterizedTest
		@CsvSource(textBlock = """
						, false
			true		, true
			false		, false
			false true	, true
			true false	, true
			""")
		void testCanSeeResource(String theInput, boolean theExpectedResult) {

			List<IConsentService> consentServices = Arrays.stream(defaultString(theInput).split(" +"))
				.map(String::trim)
				.map(Boolean::valueOf)
				.map(MultiDelegateConsentServiceTest::buildConsentShouldProcessCanSee)
				.toList();
			myService = withSerialVoting(consentServices);

			var result = myService.shouldProcessCanSeeResource(mySrd, IConsentContextServices.NULL_IMPL);

			assertEquals(theExpectedResult, result);
		}

		@ParameterizedTest
		@CsvSource(textBlock = SERIAL_STREAM_EXPECTATION)
		void testCanSeeResource(String theInput, ConsentOperationStatusEnum theExpectedResult) {

			var services = splitEnumsToStream(theInput).map(result -> (IConsentService)ConstantConsentService.constantService(result)).toList();
			myService = withSerialVoting(services);

			var verdict = myService.canSeeResource(mySrd, null, IConsentContextServices.NULL_IMPL);

			assertEquals(theExpectedResult.getStatus(), verdict.getStatus());
		}

		@ParameterizedTest
		@CsvSource(textBlock = SERIAL_STREAM_EXPECTATION)
		void testWillSeeResource(String theInput, ConsentOperationStatusEnum theExpectedResult) {

			var services = splitEnumsToStream(theInput).map(result -> (IConsentService)ConstantConsentService.constantService(result)).toList();
			myService = withSerialVoting(services);

			var verdict = myService.willSeeResource(mySrd, null, IConsentContextServices.NULL_IMPL);

			assertEquals(theExpectedResult.getStatus(), verdict.getStatus());
		}
	}

	private static @Nonnull IConsentService buildConsentShouldProcessCanSee(boolean result) {
		return new IConsentService() {
			@Override
			public boolean shouldProcessCanSeeResource(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
				return result;
			}
		};
	}

}
