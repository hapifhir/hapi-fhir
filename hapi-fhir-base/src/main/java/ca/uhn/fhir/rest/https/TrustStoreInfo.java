package ca.uhn.fhir.rest.https;

public class TrustStoreInfo extends StoreInfo{

	public TrustStoreInfo(String theFilePath, String theStorePass, String theAlias) {
		super(theFilePath, theStorePass, theAlias);
	}

}
