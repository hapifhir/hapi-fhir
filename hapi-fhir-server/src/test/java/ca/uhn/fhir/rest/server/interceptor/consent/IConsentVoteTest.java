package ca.uhn.fhir.rest.server.interceptor.consent;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import jakarta.annotation.Nonnull;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IConsentVoteTest {

	/** col1: stream of votes, col2: expected verdict */
	public static final String SERIAL_STREAM_EXPECTATION = """
		REJECT     REJECT     REJECT    , REJECT
		REJECT     REJECT     PROCEED   , REJECT
		REJECT     REJECT     AUTHORIZED, REJECT
		REJECT     PROCEED    REJECT    , REJECT
		REJECT     PROCEED    PROCEED   , REJECT
		REJECT     PROCEED    AUTHORIZED, REJECT
		REJECT     AUTHORIZED REJECT    , REJECT
		REJECT     AUTHORIZED PROCEED   , REJECT
		REJECT     AUTHORIZED AUTHORIZED, REJECT
		PROCEED    REJECT     REJECT    , REJECT
		PROCEED    REJECT     PROCEED   , REJECT
		PROCEED    REJECT     AUTHORIZED, REJECT
		PROCEED    PROCEED    REJECT    , REJECT
		PROCEED    PROCEED    PROCEED   , PROCEED
		PROCEED    PROCEED    AUTHORIZED, AUTHORIZED
		PROCEED    AUTHORIZED REJECT    , AUTHORIZED
		PROCEED    AUTHORIZED PROCEED   , AUTHORIZED
		PROCEED    AUTHORIZED AUTHORIZED, AUTHORIZED
		AUTHORIZED REJECT     REJECT    , AUTHORIZED
		AUTHORIZED REJECT     PROCEED   , AUTHORIZED
		AUTHORIZED REJECT     AUTHORIZED, AUTHORIZED
		AUTHORIZED PROCEED    REJECT    , AUTHORIZED
		AUTHORIZED PROCEED    PROCEED   , AUTHORIZED
		AUTHORIZED PROCEED    AUTHORIZED, AUTHORIZED
		AUTHORIZED AUTHORIZED REJECT    , AUTHORIZED
		AUTHORIZED AUTHORIZED PROCEED   , AUTHORIZED
		AUTHORIZED AUTHORIZED AUTHORIZED, AUTHORIZED
		""";

	/** col1: stream of votes, col2: expected verdict */
	public static final String PARALLEL_STREAM_EXPECTATION = """
		REJECT     REJECT     REJECT    , REJECT
		REJECT     REJECT     PROCEED   , REJECT
		REJECT     REJECT     AUTHORIZED, REJECT
		REJECT     PROCEED    REJECT    , REJECT
		REJECT     PROCEED    PROCEED   , REJECT
		REJECT     PROCEED    AUTHORIZED, REJECT
		REJECT     AUTHORIZED REJECT    , REJECT
		REJECT     AUTHORIZED PROCEED   , REJECT
		REJECT     AUTHORIZED AUTHORIZED, REJECT
		PROCEED    REJECT     REJECT    , REJECT
		PROCEED    REJECT     PROCEED   , REJECT
		PROCEED    REJECT     AUTHORIZED, REJECT
		PROCEED    PROCEED    REJECT    , REJECT
		PROCEED    PROCEED    PROCEED   , PROCEED
		PROCEED    PROCEED    AUTHORIZED, AUTHORIZED
		PROCEED    AUTHORIZED REJECT    , REJECT
		PROCEED    AUTHORIZED PROCEED   , AUTHORIZED
		PROCEED    AUTHORIZED AUTHORIZED, AUTHORIZED
		AUTHORIZED REJECT     REJECT    , REJECT
		AUTHORIZED REJECT     PROCEED   , REJECT
		AUTHORIZED REJECT     AUTHORIZED, REJECT
		AUTHORIZED PROCEED    REJECT    , REJECT
		AUTHORIZED PROCEED    PROCEED   , AUTHORIZED
		AUTHORIZED PROCEED    AUTHORIZED, AUTHORIZED
		AUTHORIZED AUTHORIZED REJECT    , REJECT
		AUTHORIZED AUTHORIZED PROCEED   , AUTHORIZED
		AUTHORIZED AUTHORIZED AUTHORIZED, AUTHORIZED
		""";

	@ParameterizedTest
	@CsvSource(textBlock = """
		REJECT    , REJECT    , REJECT
		REJECT    , PROCEED   , REJECT
		REJECT    , AUTHORIZED, REJECT
		AUTHORIZED, REJECT    , AUTHORIZED
		AUTHORIZED, PROCEED   , AUTHORIZED
		AUTHORIZED, AUTHORIZED, AUTHORIZED
		PROCEED   , REJECT    , REJECT
		PROCEED   , PROCEED   , PROCEED
		PROCEED   , AUTHORIZED, AUTHORIZED
		""")
	void testSerialReduction_choosesFirstVerdict(ConsentOperationStatusEnum theFirst, ConsentOperationStatusEnum theSecond, ConsentOperationStatusEnum theExpectedResult) {

		// when
		ConsentOperationStatusEnum result = IConsentVote.serialReduce(theFirst, theSecond);

		assertEquals(theExpectedResult, result);
	}


	/**
	 * With "serial" evaluation, the first non-PROCEED verdict wins.
	 */
	@ParameterizedTest
	@CsvSource(textBlock = SERIAL_STREAM_EXPECTATION)
	void testSerialStreamReduction_choosesFirstVerdict(String theInput, ConsentOperationStatusEnum theExpectedResult) {
		// given
		Stream<ConsentOperationStatusEnum> consentOperationStatusEnumStream = splitEnumsToStream(theInput);

		// when
		ConsentOperationStatusEnum result = ConsentOperationStatusEnum.serialReduce(consentOperationStatusEnumStream);

		assertEquals(theExpectedResult, result);
	}

	static @Nonnull Stream<ConsentOperationStatusEnum> splitEnumsToStream(String theInput) {
		return Arrays.stream(theInput.split(" +"))
			.map(String::trim)
			.map(ConsentOperationStatusEnum::valueOf);
	}


	@ParameterizedTest
	@CsvSource(textBlock = """
		REJECT    , REJECT    , REJECT
		REJECT    , PROCEED   , REJECT
		REJECT    , AUTHORIZED, REJECT
		AUTHORIZED, REJECT    , REJECT
		AUTHORIZED, PROCEED   , AUTHORIZED
		AUTHORIZED, AUTHORIZED, AUTHORIZED
		PROCEED   , REJECT    , REJECT
		PROCEED   , PROCEED   , PROCEED
		PROCEED   , AUTHORIZED, AUTHORIZED
		""")
	void testParallelReduction_choosesStrongestVerdict(ConsentOperationStatusEnum theFirst, ConsentOperationStatusEnum theSecond, ConsentOperationStatusEnum theExpectedResult) {

		// when
		ConsentOperationStatusEnum result = IConsentVote.parallelReduce(theFirst, theSecond);

		assertEquals(theExpectedResult, result);
	}


	/**
	 * With "parallel" evaluation, the "strongest" verdict wins.
	 * REJECT > AUTHORIZED > PROCEED.
	 */
	@ParameterizedTest
	@CsvSource(textBlock = PARALLEL_STREAM_EXPECTATION)
	void testParallelStreamReduction_strongestVerdictWins(String theInput, ConsentOperationStatusEnum theExpectedResult) {
		// given
		Stream<ConsentOperationStatusEnum> consentOperationStatusEnumStream = splitEnumsToStream(theInput);

		// when
		ConsentOperationStatusEnum result = ConsentOperationStatusEnum.parallelReduce(consentOperationStatusEnumStream);

		assertEquals(theExpectedResult, result);
	}

}
