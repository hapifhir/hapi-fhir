package ca.uhn.fhir.empi.rules.json;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiMatchEvaluation;
import ca.uhn.fhir.empi.rules.similarity.EmpiSimilarityEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.instance.model.api.IBase;

import javax.annotation.Nullable;

public class EmpiSimilarityJson  implements IModelJson {
	@JsonProperty(value = "algorithm", required = true)
	EmpiSimilarityEnum myAlgorithm;

	@JsonProperty(value = "matchThreshold", required = true)
	Double myMatchThreshold;

	/**
	 * For String value types, should the values be normalized (case, accents) before they are compared
	 */
	@JsonProperty(value = "exact")
	boolean myExact;

	public EmpiSimilarityEnum getAlgorithm() {
		return myAlgorithm;
	}

	public EmpiSimilarityJson setAlgorithm(EmpiSimilarityEnum theAlgorithm) {
		myAlgorithm = theAlgorithm;
		return this;
	}

	@Nullable
	public Double getMatchThreshold() {
		return myMatchThreshold;
	}

	public EmpiSimilarityJson setMatchThreshold(double theMatchThreshold) {
		myMatchThreshold = theMatchThreshold;
		return this;
	}

	public boolean getExact() {
		return myExact;
	}

	public EmpiSimilarityJson setExact(boolean theExact) {
		myExact = theExact;
		return this;
	}

	public EmpiMatchEvaluation match(FhirContext theFhirContext, IBase theLeftValue, IBase theRightValue) {
		return myAlgorithm.match(theFhirContext, theLeftValue, theRightValue, myExact, myMatchThreshold);
	}
}
