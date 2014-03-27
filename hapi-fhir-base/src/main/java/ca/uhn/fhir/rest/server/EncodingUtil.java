package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public enum EncodingUtil {

	XML(Constants.CT_FHIR_XML, Constants.CT_ATOM_XML, Constants.CT_XML) {
		@Override
		public IParser newParser(FhirContext theContext) {
			return theContext.newXmlParser();
		}
	},

	JSON(Constants.CT_FHIR_JSON, Constants.CT_FHIR_JSON, Constants.CT_JSON) {
		@Override
		public IParser newParser(FhirContext theContext) {
			return theContext.newJsonParser();
		}
	}
	
	;

	private String myResourceContentType;
	private String myBundleContentType;
	private String myBrowserFriendlyContentType;
	
	EncodingUtil(String theResourceContentType, String theBundleContentType, String theBrowserFriendlyContentType) {
		myResourceContentType = theResourceContentType;
		myBundleContentType = theBundleContentType;
		myBrowserFriendlyContentType = theBrowserFriendlyContentType;
	}

	public abstract IParser newParser(FhirContext theContext);
	
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
