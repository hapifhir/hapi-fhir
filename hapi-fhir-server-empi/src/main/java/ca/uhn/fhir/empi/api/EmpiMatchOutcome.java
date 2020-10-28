package ca.uhn.fhir.empi.api;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
 * This data object captures the final outcome of an EMPI match
 */
public final class EmpiMatchOutcome {
	public static final EmpiMatchOutcome POSSIBLE_DUPLICATE = new EmpiMatchOutcome(null, null).setMatchResultEnum(EmpiMatchResultEnum.POSSIBLE_DUPLICATE);
	public static final EmpiMatchOutcome NO_MATCH = new EmpiMatchOutcome(null, null).setMatchResultEnum(EmpiMatchResultEnum.NO_MATCH);
	public static final EmpiMatchOutcome NEW_PERSON_MATCH = new EmpiMatchOutcome(null, null).setMatchResultEnum(EmpiMatchResultEnum.MATCH).setNewPerson(true);
	public static final EmpiMatchOutcome EID_MATCH = new EmpiMatchOutcome(null, null).setMatchResultEnum(EmpiMatchResultEnum.MATCH).setEidMatch(true);
	public static final EmpiMatchOutcome POSSIBLE_MATCH = new EmpiMatchOutcome(null, null).setMatchResultEnum(EmpiMatchResultEnum.POSSIBLE_MATCH);

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
	 * Did the EMPI match operation result in creating a new Person resource?
	 */
	private boolean myNewPerson;

	/**
	 * Did the EMPI match occur as a result of EIDs matching?
	 */
	private boolean myEidMatch;

	/**
	 * Based on the EMPI Rules, what was the final match result?
	 */
	private EmpiMatchResultEnum myMatchResultEnum;

	public EmpiMatchOutcome(Long theVector, Double theScore) {
		vector = theVector;
		score = theScore;
	}

	public boolean isMatch() {
		return myMatchResultEnum == EmpiMatchResultEnum.MATCH;
	}

	public boolean isPossibleMatch() {
		 return myMatchResultEnum == EmpiMatchResultEnum.POSSIBLE_MATCH;
	}


	public boolean isPossibleDuplicate() {
		return myMatchResultEnum == EmpiMatchResultEnum.POSSIBLE_DUPLICATE;
	}

	public EmpiMatchResultEnum getMatchResultEnum() {
		return myMatchResultEnum;
	}

	public EmpiMatchOutcome setMatchResultEnum(EmpiMatchResultEnum theMatchResultEnum) {
		myMatchResultEnum = theMatchResultEnum;
		return this;
	}

	public boolean isNewPerson() {
		return myNewPerson;
	}

	/** @param theNewPerson	this match is creating a new person */
	public EmpiMatchOutcome setNewPerson(boolean theNewPerson) {
		myNewPerson = theNewPerson;
		return this;
	}

	public boolean isEidMatch() {
		return myEidMatch;
	}

	/** @param theEidMatch the link was established via a shared EID */
	public EmpiMatchOutcome setEidMatch(boolean theEidMatch) {
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
		if (vector == 0) {
			return 0.0;
		} else if (score > vector) {
			return 1.0;
		}

		double retVal = score / vector;
		if (retVal < 0) {
			retVal = 0.0;
		}
		return retVal;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("vector", vector)
			.append("score", score)
			.append("myNewPerson", myNewPerson)
			.append("myEidMatch", myEidMatch)
			.append("myMatchResultEnum", myMatchResultEnum)
			.toString();
	}
}
