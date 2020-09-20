package ca.uhn.fhir.empi.rules.json;

import ca.uhn.fhir.empi.rules.metric.EmpiSimilarityEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;

public class EmpiSimilarityJson  implements IModelJson {
	@JsonProperty(value = "algorithm", required = true)
	EmpiSimilarityEnum myAlgorithm;

	@JsonProperty(value = "matchThreshold", required = true)
	Double myMatchThreshold;

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
}
