package ca.uhn.fhir.empi.api;

/**
 * This data object captures the final outcome of an EMPI match
 */
public final class EmpiMatchOutcome {
	public static final EmpiMatchOutcome POSSIBLE_DUPLICATE = new EmpiMatchOutcome(null, null).setMatchResultEnum(EmpiMatchResultEnum.POSSIBLE_DUPLICATE);
	public static final EmpiMatchOutcome NO_MATCH = new EmpiMatchOutcome(null, null).setMatchResultEnum(EmpiMatchResultEnum.NO_MATCH);
	public static final EmpiMatchOutcome NEW_PERSON_MATCH = new EmpiMatchOutcome(null, null).setMatchResultEnum(EmpiMatchResultEnum.MATCH).setNewPerson(true);
	public static final EmpiMatchOutcome EID_MATCH = new EmpiMatchOutcome(null, null).setMatchResultEnum(EmpiMatchResultEnum.MATCH).setEidMatch(true);
	public static final EmpiMatchOutcome EID_POSSIBLE_MATCH = new EmpiMatchOutcome(null, null).setMatchResultEnum(EmpiMatchResultEnum.POSSIBLE_MATCH).setEidMatch(true);
	public static final EmpiMatchOutcome EID_POSSIBLE_DUPLICATE = new EmpiMatchOutcome(null, null).setMatchResultEnum(EmpiMatchResultEnum.POSSIBLE_DUPLICATE).setEidMatch(true);

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
}
