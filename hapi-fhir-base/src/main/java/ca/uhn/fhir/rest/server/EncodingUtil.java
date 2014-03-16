package ca.uhn.fhir.rest.server;

public enum EncodingUtil {

	XML(Constants.CT_FHIR_XML, Constants.CT_ATOM_XML),

	JSON(Constants.CT_FHIR_JSON, Constants.CT_FHIR_JSON);

	private String myResourceContentType;
	private String myBundleContentType;

	EncodingUtil(String theResourceContentType, String theBundleContentType) {
		myResourceContentType = theResourceContentType;
		myBundleContentType = theBundleContentType;
	}

	public String getBundleContentType() {
		return myBundleContentType;
	}

	public String getResourceContentType() {
		return myResourceContentType;
	}

}
