package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.mdm.rules.similarity.HapiStringSimilarity;
import ca.uhn.fhir.mdm.rules.similarity.IMdmFieldSimilarity;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.SorensenDice;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringSimilarityR4Test extends BaseMatcherR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(StringSimilarityR4Test.class);
	public static final String LEFT = "somon";
	public static final String RIGHT = "slomon";

	private static final HapiStringSimilarity JARO_WINKLER = new HapiStringSimilarity(new JaroWinkler());
	private static final HapiStringSimilarity COSINE = new HapiStringSimilarity(new Cosine());
	private static final HapiStringSimilarity JACCARD = new HapiStringSimilarity(new Jaccard());
	private static final HapiStringSimilarity LEVENSCHTEIN = new HapiStringSimilarity(new NormalizedLevenshtein());
	private static final HapiStringSimilarity SORENSEN_DICE = new HapiStringSimilarity(new SorensenDice());

	@Test
	public void testSlomon() {
		ourLog.info("" + similarity(JARO_WINKLER, LEFT, RIGHT));
		ourLog.info("" + similarity(COSINE, LEFT, RIGHT));
		ourLog.info("" + similarity(JACCARD, LEFT, RIGHT));
		ourLog.info("" + similarity(LEVENSCHTEIN, LEFT, RIGHT));
		ourLog.info("" + similarity(SORENSEN_DICE, LEFT, RIGHT));
	}
	
	private double similarity(IMdmFieldSimilarity theSimilarity, String theLeft, String theRight) {
		return theSimilarity.similarity(ourFhirContext, new StringType(theLeft), new StringType(theRight), false);
	}
}
