package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseBinary;

import java.util.List;
import java.util.Map;

public class HttpClientRequestParameters {

	private FhirContext myFhirContext;

	private EncodingEnum myEncodingEnum;

	private RequestTypeEnum myRequestTypeEnum;

	private Map<String, List<String>> myParams;

	private String myContentType;

	private String myContents;

	private IBaseBinary myBaseBinary;

	private String myUrl;

	// only for non-get requests
	private String myStringContents;

	private byte[] myByteContents;

	private Map<String, List<String>> myFormParams;

	public HttpClientRequestParameters(String theUrl, @Nonnull RequestTypeEnum theRequestTypeEnum) {
		myUrl = theUrl;
		myRequestTypeEnum = theRequestTypeEnum;
	}

	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	public void setFhirContext(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public EncodingEnum getEncodingEnum() {
		return myEncodingEnum;
	}

	public void setEncodingEnum(EncodingEnum theEncodingEnum) {
		myEncodingEnum = theEncodingEnum;
	}

	public RequestTypeEnum getRequestTypeEnum() {
		return myRequestTypeEnum;
	}

	public void setRequestTypeEnum(RequestTypeEnum theRequestTypeEnum) {
		myRequestTypeEnum = theRequestTypeEnum;
	}

	public Map<String, List<String>> getParams() {
		return myParams;
	}

	public void setParams(Map<String, List<String>> theParams) {
		myParams = theParams;
	}

	public String getContentType() {
		return myContentType;
	}

	public void setContentType(String theContentType) {
		myContentType = theContentType;
	}

	public String getContents() {
		return myContents;
	}

	public void setContents(String theContents) {
		myContents = theContents;
	}

	public IBaseBinary getBaseBinary() {
		return myBaseBinary;
	}

	public void setBaseBinary(IBaseBinary theBaseBinary) {
		myBaseBinary = theBaseBinary;
	}

	public String getUrl() {
		return myUrl;
	}

	public String getStringContents() {
		return myStringContents;
	}

	public void setStringContents(String theStringContents) {
		myStringContents = theStringContents;
	}

	public byte[] getByteContents() {
		return myByteContents;
	}

	public void setByteContents(byte[] theByteContents) {
		myByteContents = theByteContents;
	}

	public Map<String, List<String>> getFormParams() {
		return myFormParams;
	}

	public void setFormParams(Map<String, List<String>> theFormParams) {
		myFormParams = theFormParams;
	}
}
