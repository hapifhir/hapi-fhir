package ca.uhn.fhir.rest.server;

enum EncodingUtil {

	XML(Constants.CT_FHIR_XML, Constants.CT_ATOM_XML, Constants.CT_XML),

	JSON(Constants.CT_FHIR_JSON, Constants.CT_FHIR_JSON, Constants.CT_JSON)
	
	;

	private String myResourceContentType;
	private String myBundleContentType;
	private String myBrowserFriendlyContentType;
	
	EncodingUtil(String theResourceContentType, String theBundleContentType, String theBrowserFriendlyContentType) {
		myResourceContentType = theResourceContentType;
		myBundleContentType = theBundleContentType;
		myBrowserFriendlyContentType = theBrowserFriendlyContentType;
	}

	public String getBundleContentType() {
		return myBundleContentType;
	}

	public String getResourceContentType() {
		return myResourceContentType;
	}

	public String getBrowserFriendlyBundleContentType() {
		return myBrowserFriendlyContentType;
	}

}
