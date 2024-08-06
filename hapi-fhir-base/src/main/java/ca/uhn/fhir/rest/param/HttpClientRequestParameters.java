package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseBinary;

import java.util.List;
import java.util.Map;

public class HttpClientRequestParameters {
	/**
	 * The fhir context used.
	 */
	private FhirContext myFhirContext;
	/**
	 * The encoding type (JSON, XML, etc) to use for the request.
	 */
	private EncodingEnum myEncodingEnum;
	/**
	 * The request type (GET, POST, PUT, etc)
	 * Generally a required field.
	 */
	private RequestTypeEnum myRequestTypeEnum;

	/**
	 * Parameters
	 */
	private Map<String, List<String>> myParams;

	/**
	 * The content type to use (application/json, etc)
	 */
	private String myContentType;

	/**
	 * If the payload is a String, this is the content to attach.
	 *
	 * Only one of String/byte[]/form encoded url parameters can be used.
	 * String contents will be used before byte contents which will be used
	 * 		before FormUrlEncoded parameters.
	 */
	private String myContents;

	/**
	 * If the payload is a binary, this is the binary to attach
	 */
	private IBaseBinary myBaseBinary;

	/**
	 * The URL where the request is to be made.
	 */
	private final String myUrl;

	/**
	 * If the payload is a byte[], this is the content to attach.
	 *
	 * Only one of String/byte[]/form encoded url parameters can be used.
	 * String contents will be used before byte contents which will be used
	 * 		before FormUrlEncoded parameters.
	 */
	private byte[] myByteContents;

	/**
	 * If the payload is a set of form encoded url parameters, these are the
	 * parameters to use.
	 *
	 * Only one of String/byte[]/form encoded url parameters can be used.
	 * String contents will be used before byte contents which will be used
	 * 		before FormUrlEncoded parameters.
	 */
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
