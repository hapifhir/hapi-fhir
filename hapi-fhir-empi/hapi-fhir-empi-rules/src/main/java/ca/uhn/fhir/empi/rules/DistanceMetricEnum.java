package ca.uhn.fhir.empi.rules;

import info.debatty.java.stringsimilarity.*;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;

public enum DistanceMetricEnum implements NormalizedStringSimilarity {
	JARO_WINKLER("Jaro Winkler", new HapiStringSimilarity(new JaroWinkler())),
	COSINE("Cosine", new HapiStringSimilarity(new Cosine())),
	JACCARD("Jaccard", new HapiStringSimilarity(new Jaccard())),
	NORMALIZED_LEVENSCHTEIN("Normalized Levenschtein", new HapiStringSimilarity(new NormalizedLevenshtein())),
	SORENSEN_DICE("Sorensen Dice", new HapiStringSimilarity(new SorensenDice())),
	;

	private final String myCode;
	private final HapiStringSimilarity myHapiStringSimilarity;

	DistanceMetricEnum(String theCode, HapiStringSimilarity theHapiStringSimilarity) {
		myCode = theCode;
		myHapiStringSimilarity = theHapiStringSimilarity;
	}

	public String getCode() {
		return myCode;
	}

	public HapiStringSimilarity getHapiStringSimilarity() {
		return myHapiStringSimilarity;
	}

	@Override
	public double similarity(String theLeftString, String theRightString) {
		return myHapiStringSimilarity.similarity(theLeftString, theRightString);
	}
}
