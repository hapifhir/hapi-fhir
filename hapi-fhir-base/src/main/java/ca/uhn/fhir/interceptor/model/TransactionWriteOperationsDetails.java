package ca.uhn.fhir.interceptor.model;

import java.util.List;

/**
 * This is an experimental API, use with caution
 */
public class TransactionWriteOperationsDetails {

	private List<String> myConditionalCreateRequestUrls;
	private List<String> myUpdateRequestUrls;

	public List<String> getConditionalCreateRequestUrls() {
		return myConditionalCreateRequestUrls;
	}

	public void setConditionalCreateRequestUrls(List<String> theConditionalCreateRequestUrls) {
		myConditionalCreateRequestUrls = theConditionalCreateRequestUrls;
	}

	public List<String> getUpdateRequestUrls() {
		return myUpdateRequestUrls;
	}

	public void setUpdateRequestUrls(List<String> theUpdateRequestUrls) {
		myUpdateRequestUrls = theUpdateRequestUrls;
	}

}
