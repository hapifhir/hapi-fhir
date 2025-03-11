package ca.uhn.fhir.jpa.mdm.svc.candidate;

import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class CandidateListTest {

	private List<MatchedGoldenResourceCandidate> getCandidatesList(int theSize) {
		List<MatchedGoldenResourceCandidate> candidatesToAdd = new ArrayList<>();

		for (int i = 0; i < theSize; i++) {
			MatchedGoldenResourceCandidate candidate = new MatchedGoldenResourceCandidate(
				mock(IResourcePersistentId.class),
				MdmMatchOutcome.POSSIBLE_MATCH
			);
			candidatesToAdd.add(candidate);
		}

		return candidatesToAdd;
	}

	@ParameterizedTest
	@EnumSource(CandidateStrategyEnum.class)
	public void addAll_withVariousStrategies_behaviourTest(CandidateStrategyEnum theStrategyEnum) {
		// setup
		int total = 3;
		List<MatchedGoldenResourceCandidate> candidatesToAdd = getCandidatesList(total);

		// test
		CandidateList list = new CandidateList(theStrategyEnum);

		// verify
		if (theStrategyEnum == CandidateStrategyEnum.ANY) {
			assertThatExceptionOfType(InternalErrorException.class).isThrownBy(() -> {
				list.addAll(theStrategyEnum, candidatesToAdd);
			});
		} else {
			list.addAll(theStrategyEnum, candidatesToAdd);
			assertEquals(total, list.size());
		}
	}

	@ParameterizedTest
	@EnumSource(CandidateStrategyEnum.class)
	public void stream_forVariousStrategies_returnsJointStream(CandidateStrategyEnum theStrategy) {
		// setup
		int size = 3;
		CandidateList candidateList = new CandidateList(theStrategy);

		// we need some values first
		size = populateCandidateList(theStrategy, size, candidateList);

		// test
		assertEquals(size, candidateList.stream().count());
	}

	private int populateCandidateList(CandidateStrategyEnum theStrategy, int theSize, CandidateList theCandidateList) {
		if (theStrategy == CandidateStrategyEnum.ANY) {
			int realTotal = 0;
			for (CandidateStrategyEnum strat : CandidateStrategyEnum.values()) {
				if (strat == theStrategy) {
					continue;
				}

				theCandidateList.addAll(strat, getCandidatesList(theSize));
				realTotal += theSize;
			}
			theSize = realTotal;
		} else {
			theCandidateList.addAll(theStrategy, getCandidatesList(theSize));
		}
		return theSize;
	}

	@ParameterizedTest
	@EnumSource(CandidateStrategyEnum.class)
	public void singleElement_CandidateList_Tests(CandidateStrategyEnum theStrategy) {
		// setup
		CandidateList candidate = new CandidateList(theStrategy);

		if (theStrategy == CandidateStrategyEnum.ANY) {
			candidate.addAll(CandidateStrategyEnum.LINK, getCandidatesList(1));
		} else {
			candidate.addAll(theStrategy, getCandidatesList(1));
		}

		// tests
		assertFalse(candidate.isEmpty());
		assertTrue(candidate.exactlyOneMatch());
		assertEquals(1, candidate.size());
		assertNotNull(candidate.getFirstMatch());
		assertNotNull(candidate.getOnlyMatch());
	}

	@ParameterizedTest
	@EnumSource(CandidateStrategyEnum.class)
	public void getCandidates_variousStrategies_returnsExpectedResults(CandidateStrategyEnum theStrategy) {
		// setup
		CandidateList candidateList = new CandidateList(theStrategy);

		int size = populateCandidateList(theStrategy, 10, candidateList);

		// tests
		assertEquals(size, candidateList.size());
		List<MatchedGoldenResourceCandidate> candidates = candidateList.getCandidates();
		assertThat(candidates).hasSize(size);
	}
}
