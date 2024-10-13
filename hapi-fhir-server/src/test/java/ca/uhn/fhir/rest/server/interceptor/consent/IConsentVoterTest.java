package ca.uhn.fhir.rest.server.interceptor.consent;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class IConsentVoterTest {

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
		ConsentOperationStatusEnum result = IConsentVoter.serialReduce(theFirst, theSecond);

		assertEquals(theExpectedResult, result);
	}


	/**
	 * With "serial" evaluation, the first non-PROCEED verdict wins.
	 */
	@ParameterizedTest
	@CsvSource(textBlock = """
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
		""")
	void testSerialStreamReduction_choosesFirstVerdict(String theInput, ConsentOperationStatusEnum theExpectedResult) {
		// given
		Stream<ConsentOperationStatusEnum> consentOperationStatusEnumStream = Arrays.stream(theInput.split(" +"))
			.map(String::trim)
			.map(ConsentOperationStatusEnum::valueOf);

		// when
		ConsentOperationStatusEnum result = ConsentOperationStatusEnum.serialReduce(consentOperationStatusEnumStream);

		assertEquals(theExpectedResult, result);
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
		ConsentOperationStatusEnum result = IConsentVoter.parallelReduce(theFirst, theSecond);

		assertEquals(theExpectedResult, result);
	}


	/**
	 * With "parallel" evaluation, the "strongest" verdict wins.
	 * REJECT > AUTHORIZED > PROCEED.
	 */
	@ParameterizedTest
	@CsvSource(textBlock = """
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
		""")
	void testParallelStreamReduction_strongestVerdictWins(String theInput, ConsentOperationStatusEnum theExpectedResult) {
		// given
		Stream<ConsentOperationStatusEnum> consentOperationStatusEnumStream = Arrays.stream(theInput.split(" +"))
			.map(String::trim)
			.map(ConsentOperationStatusEnum::valueOf);

		// when
		ConsentOperationStatusEnum result = ConsentOperationStatusEnum.parallelReduce(consentOperationStatusEnumStream);

		assertEquals(theExpectedResult, result);
	}

}
