package ca.uhn.fhir.rest.server;

import java.util.HashMap;

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

	private static HashMap<String, EncodingUtil> ourContentTypeToEncoding;

	static {
		ourContentTypeToEncoding = new HashMap<String, EncodingUtil>();
		for (EncodingUtil next: values()) {
			ourContentTypeToEncoding.put(next.getBundleContentType(), next);
			ourContentTypeToEncoding.put(next.getResourceContentType(), next);
			ourContentTypeToEncoding.put(next.getBrowserFriendlyBundleContentType(), next);
		}
	}
	
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

	public static EncodingUtil forContentType(String theContentType) {
		return ourContentTypeToEncoding.get(theContentType);
	}


}
