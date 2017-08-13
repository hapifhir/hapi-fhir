package ca.uhn.fhir.to.model;

import static org.apache.commons.lang3.StringUtils.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ModelAttribute;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.api.*;
import ca.uhn.fhir.rest.client.impl.GenericClient;
import ca.uhn.fhir.rest.server.IncomingRequestAddressStrategy;
import ca.uhn.fhir.rest.server.util.ITestingUiClientFactory;
import ca.uhn.fhir.to.Controller;
import ca.uhn.fhir.to.TesterConfig;

public class HomeRequest {

	private String myEncoding;
	private String myPretty;
	private String myResource;
	private String myServerId;
	private String mySummary;

	@ModelAttribute("encoding")
	public String getEncoding() {
		return myEncoding;
	}

	@ModelAttribute("encoding")
	public String getPretty() {
		return myPretty;
	}

	@ModelAttribute("_summary")
	public String get_summary() {
		return mySummary;
	}

	@ModelAttribute("resource")
	public String getResource() {
		return myResource;
	}

	public String getServerBase(HttpServletRequest theRequest, TesterConfig theConfig) {
		String retVal;
		if (isBlank(myServerId) && !theConfig.getIdToServerBase().containsKey(myServerId)) {
			retVal = theConfig.getIdToServerBase().entrySet().iterator().next().getValue();
		} else {
			retVal = theConfig.getIdToServerBase().get(myServerId);
		}

		if (retVal.contains("${serverBase}")) {
			IncomingRequestAddressStrategy strategy = new IncomingRequestAddressStrategy();
			strategy.setServletPath("");
			String base = strategy.determineServerBase(theRequest.getServletContext(), theRequest);
			if (base.endsWith("/")) {
				base = base.substring(0, base.length() - 1);
			}
			if (base.endsWith("/resource")) {
				base = base.substring(0, base.length() - "/resource".length());
			}
			retVal = retVal.replace("${serverBase}", base);
		}

		return retVal;
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

	public FhirVersionEnum getFhirVersion(TesterConfig theConfig) {
		if (isBlank(myServerId) && !theConfig.getIdToFhirVersion().containsKey(myServerId)) {
			return theConfig.getIdToFhirVersion().entrySet().iterator().next().getValue();
		} else {
			return theConfig.getIdToFhirVersion().get(myServerId);
		}
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

	public void set_summary(String theSummary) {
		mySummary = theSummary;
	}

	public void setResource(String theResource) {
		myResource = theResource;
	}

	public void setServerId(String theServerId) {
		myServerId = theServerId;
	}

	public GenericClient newClient(HttpServletRequest theRequest, FhirContext theContext, TesterConfig theConfig, Controller.CaptureInterceptor theInterceptor) {
		theContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

		GenericClient retVal;
		ITestingUiClientFactory clientFactory = theConfig.getClientFactory();
		if (clientFactory != null) {
			retVal = (GenericClient) clientFactory.newClient(
					theContext,
					theRequest,
					getServerBase(theRequest, theConfig));
		} else {
			retVal = (GenericClient) theContext.newRestfulGenericClient(getServerBase(theRequest, theConfig));
		}
		
		retVal.registerInterceptor(new BufferResponseInterceptor());

		retVal.setKeepResponses(true);

		if ("true".equals(getPretty())) {
			retVal.setPrettyPrint(true);
		} else if ("false".equals(getPretty())) {
			retVal.setPrettyPrint(false);
		}

		if ("xml".equals(getEncoding())) {
			retVal.setEncoding(EncodingEnum.XML);
		} else if ("json".equals(getEncoding())) {
			retVal.setEncoding(EncodingEnum.JSON);
		}

		if (isNotBlank(get_summary())) {
			SummaryEnum summary = SummaryEnum.fromCode(get_summary());
			if (summary != null) {
				retVal.setSummary(summary);
			}
		}
		
		retVal.registerInterceptor(theInterceptor);

		final String remoteAddr = org.slf4j.MDC.get("req.remoteAddr");
		retVal.registerInterceptor(new IClientInterceptor() {

			@Override
			public void interceptResponse(IHttpResponse theRequest) {
				// nothing
			}

			@Override
			public void interceptRequest(IHttpRequest theRequest) {
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

	public String getApiKey(HttpServletRequest theServletRequest, TesterConfig theConfig) {
		Boolean allowsApiKey;
		if (isBlank(myServerId) && !theConfig.getIdToFhirVersion().containsKey(myServerId)) {
			allowsApiKey = theConfig.getIdToAllowsApiKey().entrySet().iterator().next().getValue();
		} else {
			allowsApiKey = theConfig.getIdToAllowsApiKey().get(myServerId);
		}
		if (!Boolean.TRUE.equals(allowsApiKey)) {
			return null;
		}
		
		return defaultString(theServletRequest.getParameter("apiKey"));
	}

}
