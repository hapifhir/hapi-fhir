package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.mdm.rules.similarity.HapiNumericSimilarity;
import ca.uhn.fhir.mdm.rules.similarity.HapiStringSimilarity;
import ca.uhn.fhir.mdm.rules.similarity.IMdmFieldSimilarity;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.SorensenDice;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumericSimilarityR4Test extends StringSimilarityR4Test {
	private static final HapiStringSimilarity NUMERIC_JARO_WINKLER = new HapiNumericSimilarity(new JaroWinkler());
	private static final HapiStringSimilarity NUMERIC_COSINE = new HapiNumericSimilarity(new Cosine());
	private static final HapiStringSimilarity NUMERIC_JACCARD = new HapiNumericSimilarity(new Jaccard());
	private static final HapiStringSimilarity NUMERIC_LEVENSCHTEIN = new HapiNumericSimilarity(new NormalizedLevenshtein());
	private static final HapiStringSimilarity NUMERIC_SORENSEN_DICE = new HapiNumericSimilarity(new SorensenDice());


	@ParameterizedTest
	@CsvSource({
		"123-45-6789, 123456789",
		"1234-5-6789, 123456789",
		"abc123, 123",
		"(416) 967-1111, 4169671111,"
	})
	public void testNumericSimilarity_withExactMatches(String theLeft, String theRight) {
		assertEquals(1.0, similarity(NUMERIC_JARO_WINKLER, theLeft, theRight));
		assertEquals(1.0, similarity(NUMERIC_COSINE, theLeft, theRight));
		assertEquals(1.0, similarity(NUMERIC_JACCARD, theLeft, theRight));
		assertEquals(1.0, similarity(NUMERIC_LEVENSCHTEIN, theLeft, theRight));
		assertEquals(1.0, similarity(NUMERIC_SORENSEN_DICE, theLeft, theRight));
	}

	@ParameterizedTest
	@CsvSource({
		"123546789, 123-54-6789, 123456789",
		"123456789, 1234-5-6789, 123456789",
		"321, abc321, 123",
		"1231231234, (123) 123-1234, 1231234321,"
	})
	public void testNumericSimilarity_givesSameResultAsStringSimilarity(String theLeft, String theLeftWithNonNumerics, String theRight) {
		assertEquals(similarity(JARO_WINKLER, theLeft, theRight), similarity(NUMERIC_JARO_WINKLER, theLeftWithNonNumerics, theRight));
		assertEquals(similarity(COSINE, theLeft, theRight), similarity(NUMERIC_COSINE, theLeftWithNonNumerics, theRight));
		assertEquals(similarity(JACCARD, theLeft, theRight), similarity(NUMERIC_JACCARD, theLeftWithNonNumerics, theRight));
		assertEquals(similarity(LEVENSCHTEIN, theLeft, theRight), similarity(NUMERIC_LEVENSCHTEIN, theLeftWithNonNumerics, theRight));
		assertEquals(similarity(SORENSEN_DICE, theLeft, theRight), similarity(NUMERIC_SORENSEN_DICE, theLeftWithNonNumerics, theRight));
	}

	private double similarity(IMdmFieldSimilarity theSimilarity, String theLeft, String theRight) {
		return theSimilarity.similarity(ourFhirContext, new StringType(theLeft), new StringType(theRight), false);
	}
}
