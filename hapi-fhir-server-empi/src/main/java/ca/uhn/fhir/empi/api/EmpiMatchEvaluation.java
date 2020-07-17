package ca.uhn.fhir.empi.api;

public class EmpiMatchEvaluation {
	public final boolean match;
	public final double score;

	public EmpiMatchEvaluation(boolean theMatch, double theScore) {
		match = theMatch;
		score = theScore;
	}

	public static EmpiMatchEvaluation max(EmpiMatchEvaluation theLeft, EmpiMatchEvaluation theRight) {
		return new EmpiMatchEvaluation(theLeft.match | theRight.match, Math.max(theLeft.score, theRight.score));
	}
}
