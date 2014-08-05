package ca.uhn.fhir.to.model;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.web.bind.annotation.ModelAttribute;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.GenericClient;
import ca.uhn.fhir.rest.client.IClientInterceptor;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.to.Controller;
import ca.uhn.fhir.to.TesterConfig;

public class HomeRequest {

	private String myEncoding;
	private String myPretty;
	private String myResource;
	private String myServerId;

	@ModelAttribute("encoding")
	public String getEncoding() {
		return myEncoding;
	}

	@ModelAttribute("encoding")
	public String getPretty() {
		return myPretty;
	}

	@ModelAttribute("resource")
	public String getResource() {
		return myResource;
	}

	public String getServerBase(TesterConfig theConfig) {
		if (isBlank(myServerId) && !theConfig.getIdToServerBase().containsKey(myServerId)) {
			return theConfig.getIdToServerBase().entrySet().iterator().next().getValue();
		} else {
			return theConfig.getIdToServerBase().get(myServerId);
		}
	}

	@ModelAttribute("serverId")
	public String getServerId() {
		return myServerId;
	}

	public String getServerIdWithDefault(TesterConfig theConfig) {
		String retVal = myServerId;
		if (isBlank(retVal)) {
			retVal = theConfig.getIdToServerBase().keySet().iterator().next();
		}
		return retVal;
	}

	public String getServerName(TesterConfig theConfig) {
		if (isBlank(myServerId) && !theConfig.getIdToServerName().containsKey(myServerId)) {
			return theConfig.getIdToServerName().entrySet().iterator().next().getValue();
		} else {
			return theConfig.getIdToServerName().get(myServerId);
		}
	}

	public void setEncoding(String theEncoding) {
		myEncoding = theEncoding;
	}

	public void setPretty(String thePretty) {
		myPretty = thePretty;
	}

	public void setResource(String theResource) {
		myResource = theResource;
	}

	public void setServerId(String theServerId) {
		myServerId = theServerId;
	}

	public GenericClient newClient(FhirContext theContext, TesterConfig theConfig, Controller.CaptureInterceptor theInterceptor) {
		GenericClient retVal = (GenericClient) theContext.newRestfulGenericClient(getServerBase(theConfig));
		retVal.setKeepResponses(true);
		
		if ("true".equals(getPretty())) {
			retVal.setPrettyPrint(true);
		} else if ("false".equals(getPretty())) {
			retVal.setPrettyPrint(false);
		}
		
		if ("xml".equals(getEncoding())) {
			retVal.setEncoding( EncodingEnum.XML);
		} else if ("json".equals(getEncoding())) {
			retVal.setEncoding( EncodingEnum.JSON);
		} 

		retVal.registerInterceptor(theInterceptor);
		
		final String remoteAddr = org.slf4j.MDC.get("req.remoteAddr");
		retVal.registerInterceptor(new IClientInterceptor() {
			
			@Override
			public void interceptResponse(HttpResponse theRequest) {
				// nothing
			}
			
			@Override
			public void interceptRequest(HttpRequestBase theRequest) {
				if (isNotBlank(remoteAddr)) {
					theRequest.addHeader("x-forwarded-for", remoteAddr);
				}
			}
		});
		
		
		return retVal;
	}
	
	public IParser newParser(FhirContext theCtx) {
		if ("json".equals(getEncoding())) {
			return theCtx.newJsonParser();
		} 
		return theCtx.newXmlParser();
	}


}
