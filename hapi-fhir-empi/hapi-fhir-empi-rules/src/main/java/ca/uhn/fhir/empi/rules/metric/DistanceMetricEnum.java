package ca.uhn.fhir.empi.rules.metric;

import info.debatty.java.stringsimilarity.*;

public enum DistanceMetricEnum {
	JARO_WINKLER("Jaro Winkler", new HapiStringMetric(new JaroWinkler())),
	COSINE("Cosine", new HapiStringMetric(new Cosine())),
	JACCARD("Jaccard", new HapiStringMetric(new Jaccard())),
	NORMALIZED_LEVENSCHTEIN("Normalized Levenschtein", new HapiStringMetric(new NormalizedLevenshtein())),
	SORENSEN_DICE("Sorensen Dice", new HapiStringMetric(new SorensenDice())),
	;

	private final String myCode;
	private final HapiStringMetric myHapiStringMetric;

	DistanceMetricEnum(String theCode, HapiStringMetric theHapiStringMetric) {
		myCode = theCode;
		myHapiStringMetric = theHapiStringMetric;
	}

	public String getCode() {
		return myCode;
	}

	public HapiStringMetric getHapiStringMetric() {
		return myHapiStringMetric;
	}

	public double compare(String theLeftString, String theRightString) {
		return myHapiStringMetric.compare(theLeftString, theRightString);
	}
}
