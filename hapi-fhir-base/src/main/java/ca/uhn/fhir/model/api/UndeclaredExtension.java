package ca.uhn.fhir.model.api;

public class UndeclaredExtension extends BaseElement {

	private String myUrl;
	private IElement myValue;

	public String getUrl() {
		return myUrl;
	}

	public IElement getValue() {
		return myValue;
	}

	public void setUrl(String theUrl) {
		myUrl = theUrl;
	}

	public void setValue(IElement theValue) {
		myValue = theValue;
	}

}
