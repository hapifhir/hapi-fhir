package ca.uhn.fhir.model.api;

import ca.uhn.fhir.model.primitive.StringDt;

public class BaseBundle {

	private StringDt myAuthorName;
	private StringDt myAuthorUri;

	public StringDt getAuthorName() {
		if (myAuthorName == null) {
			myAuthorName = new StringDt();
		}
		return myAuthorName;
	}

	public StringDt getAuthorUri() {
		if (myAuthorUri == null) {
			myAuthorUri = new StringDt();
		}
		return myAuthorUri;
	}

}
