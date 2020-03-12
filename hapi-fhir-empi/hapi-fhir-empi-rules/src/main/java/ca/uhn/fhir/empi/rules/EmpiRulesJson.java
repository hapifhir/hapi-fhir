package ca.uhn.fhir.empi.rules;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EmpiRulesJson implements IModelJson {
	@JsonProperty("matchFields")
	List<EmpiMatchFieldJson> myMatchFieldJsonList;

	public void addMatchField(EmpiMatchFieldJson theMatchRuleName) {
		myMatchFieldJsonList.add(theMatchRuleName);
	}
}
