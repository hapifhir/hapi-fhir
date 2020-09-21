package ca.uhn.fhir.empi.rules.similarity;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiMatchEvaluation;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.SorensenDice;
import org.hl7.fhir.instance.model.api.IBase;

import javax.annotation.Nullable;

public enum EmpiSimilarityEnum {
	JARO_WINKLER(new HapiStringSimilarity(new JaroWinkler())),
	COSINE(new HapiStringSimilarity(new Cosine())),
	JACCARD(new HapiStringSimilarity(new Jaccard())),
	LEVENSCHTEIN(new HapiStringSimilarity(new NormalizedLevenshtein())),
	SORENSEN_DICE(new HapiStringSimilarity(new SorensenDice()));

	private final IEmpiFieldSimilarity myEmpiFieldSimilarity;

	EmpiSimilarityEnum(IEmpiFieldSimilarity theEmpiFieldSimilarity) {
		myEmpiFieldSimilarity = theEmpiFieldSimilarity;
	}

	public EmpiMatchEvaluation match(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact, @Nullable Double theThreshold) {
		return matchBySimilarity(myEmpiFieldSimilarity, theFhirContext, theLeftBase, theRightBase, theExact, theThreshold);
	}

	private EmpiMatchEvaluation matchBySimilarity(IEmpiFieldSimilarity theSimilarity, FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact, Double theThreshold) {
		double similarityResult = theSimilarity.similarity(theFhirContext, theLeftBase, theRightBase, theExact);
		return new EmpiMatchEvaluation(similarityResult >= theThreshold, similarityResult);
	}
}
