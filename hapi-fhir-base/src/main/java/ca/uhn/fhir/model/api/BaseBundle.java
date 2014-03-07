package ca.uhn.fhir.model.api;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.ElementUtil;

public class BaseBundle implements IElement {

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

	@Override
	public boolean isEmpty() {
		return ElementUtil.isEmpty(myAuthorName, myAuthorUri);
	}
	
	

}
