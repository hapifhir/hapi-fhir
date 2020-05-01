package ca.uhn.fhir.rest.client.api;

public abstract class BaseHttpRequest implements IHttpRequest {

	private UrlSourceEnum myUrlSource;

	@Override
	public UrlSourceEnum getUrlSource() {
		return myUrlSource;
	}

	@Override
	public void setUrlSource(UrlSourceEnum theUrlSource) {
		myUrlSource = theUrlSource;
	}

}
