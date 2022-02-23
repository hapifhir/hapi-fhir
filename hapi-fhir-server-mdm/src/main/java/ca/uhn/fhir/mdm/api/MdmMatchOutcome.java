package ca.uhn.fhir.mdm.api;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This data object captures the final outcome of an MDM match
 */
public final class MdmMatchOutcome {

	public static final MdmMatchOutcome POSSIBLE_DUPLICATE = new MdmMatchOutcome(null, null).setMatchResultEnum(MdmMatchResultEnum.POSSIBLE_DUPLICATE);
	public static final MdmMatchOutcome NO_MATCH = new MdmMatchOutcome(null, null).setMatchResultEnum(MdmMatchResultEnum.NO_MATCH);
	public static final MdmMatchOutcome NEW_GOLDEN_RESOURCE_MATCH = new MdmMatchOutcome(null, null).setMatchResultEnum(MdmMatchResultEnum.MATCH).setCreatedNewResource(true);
	public static final MdmMatchOutcome EID_MATCH = new MdmMatchOutcome(null, null).setMatchResultEnum(MdmMatchResultEnum.MATCH).setEidMatch(true);
	public static final MdmMatchOutcome POSSIBLE_MATCH = new MdmMatchOutcome(null, null).setMatchResultEnum(MdmMatchResultEnum.POSSIBLE_MATCH);

	/**
	 * A bitmap that indicates which rules matched
	 */
	public final Long vector;

	/**
	 * The sum of all scores for all rules evaluated.  Similarity rules add the similarity score (between 0.0 and 1.0) whereas
	 * matcher rules add either a 0.0 or 1.0.
	 */
	public final Double score;

	/**
	 * Did the MDM match operation result in creating a new golden resource resource?
	 */
	private boolean myCreatedNewResource;

	/**
	 * Did the MDM match occur as a result of EIDs matching?
	 */
	private boolean myEidMatch;

	/**
	 * Based on the MDM Rules, what was the final match result?
	 */
	private MdmMatchResultEnum myMatchResultEnum;

	/**
	 * Total number of MDM rules checked for this outcome
	 */
	private int myMdmRuleCount;

	public MdmMatchOutcome(Long theVector, Double theScore) {
		vector = theVector;
		score = theScore;
	}

	public boolean isMatch() {
		return myMatchResultEnum == MdmMatchResultEnum.MATCH;
	}

	public boolean isPossibleMatch() {
		 return myMatchResultEnum == MdmMatchResultEnum.POSSIBLE_MATCH;
	}


	public boolean isPossibleDuplicate() {
		return myMatchResultEnum == MdmMatchResultEnum.POSSIBLE_DUPLICATE;
	}

	public MdmMatchResultEnum getMatchResultEnum() {
		return myMatchResultEnum;
	}

	public MdmMatchOutcome setMatchResultEnum(MdmMatchResultEnum theMatchResultEnum) {
		myMatchResultEnum = theMatchResultEnum;
		return this;
	}

	public boolean isCreatedNewResource() {
		return myCreatedNewResource;
	}

	/** @param theCreatedNewResource	this match is creating a new golden resource */
	public MdmMatchOutcome setCreatedNewResource(boolean theCreatedNewResource) {
		myCreatedNewResource = theCreatedNewResource;
		return this;
	}

	public boolean isEidMatch() {
		return myEidMatch;
	}

	/**
	 * Sets the number of MDM rules checked for this match outcome
	 *
	 * @param theMdmRuleCount
	 * 	Number of MDM rules that were checked for this match outcome
	 * @return
	 * 	Returns this instance
	 */
	public MdmMatchOutcome setMdmRuleCount(int theMdmRuleCount) {
		myMdmRuleCount = theMdmRuleCount;
		return this;
	}

	/**
	 * Gets the number of MDM rules checked for this match outcome
	 *
	 * @return
	 * 	Returns the number of rules
	 */
	public int getMdmRuleCount() {
		return myMdmRuleCount;
	}

	/** @param theEidMatch the link was established via a shared EID */
	public MdmMatchOutcome setEidMatch(boolean theEidMatch) {
		myEidMatch = theEidMatch;
		return this;
	}

	/**
	 * Gets normalized score that is in the range from zero to one
	 *
	 * @return
	 * 	Returns the normalized score
	 */
	public Double getNormalizedScore() {
		if (myMdmRuleCount == 0) {
			return 0.0;
		}
		return score / myMdmRuleCount;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("vector", vector)
			.append("score", score)
			.append("myCreatedNewResource", myCreatedNewResource)
			.append("myEidMatch", myEidMatch)
			.append("myMatchResultEnum", myMatchResultEnum)
			.toString();
	}
}
