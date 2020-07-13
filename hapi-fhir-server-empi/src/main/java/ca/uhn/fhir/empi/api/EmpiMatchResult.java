package ca.uhn.fhir.empi.api;

public final class EmpiMatchResult {
	public static final EmpiMatchResult POSSIBLE_DUPLICATE = new EmpiMatchResult(-1, null).setMatchResultEnum(EmpiMatchResultEnum.POSSIBLE_DUPLICATE);
	public static final EmpiMatchResult NO_MATCH = new EmpiMatchResult(-1, null).setMatchResultEnum(EmpiMatchResultEnum.NO_MATCH);
	public static final EmpiMatchResult NEW_PERSON_MATCH = new EmpiMatchResult(-1, null).setMatchResultEnum(EmpiMatchResultEnum.MATCH).setNewPerson(true);
	public static final EmpiMatchResult EID_MATCH = new EmpiMatchResult(-1, null).setMatchResultEnum(EmpiMatchResultEnum.MATCH).setEidMatch(true);

	public final long vector;
	public final Double score;

	private boolean myNewPerson;
	private boolean myEidMatch;
	private EmpiMatchResultEnum myMatchResultEnum;

	public EmpiMatchResult(long theVector, Double theScore) {
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

	public EmpiMatchResult setMatchResultEnum(EmpiMatchResultEnum theMatchResultEnum) {
		myMatchResultEnum = theMatchResultEnum;
		return this;
	}

	public boolean isNewPerson() {
		return myNewPerson;
	}

	/** @param theNewPerson	this match is creating a new person */
	public EmpiMatchResult setNewPerson(boolean theNewPerson) {
		myNewPerson = theNewPerson;
		return this;
	}

	public boolean isEidMatch() {
		return myEidMatch;
	}

	/** @param theEidMatch the link was established via a shared EID */
	public EmpiMatchResult setEidMatch(boolean theEidMatch) {
		myEidMatch = theEidMatch;
		return this;
	}
}
