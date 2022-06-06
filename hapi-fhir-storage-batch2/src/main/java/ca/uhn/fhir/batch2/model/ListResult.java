package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListResult<T> implements IModelJson {
	@JsonProperty("data")
	private List<T> myData;

	public ListResult(List<T> theData) {
		myData = theData;
	}

	public List<T> getData() {
		return myData;
	}
}
