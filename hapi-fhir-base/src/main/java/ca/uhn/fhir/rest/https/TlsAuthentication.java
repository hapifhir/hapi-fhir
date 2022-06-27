package ca.uhn.fhir.rest.https;

import java.util.Optional;

public class TlsAuthentication {

	private final Optional<KeyStoreInfo> myKeyStoreInfo;
	private final Optional<TrustStoreInfo> myTrustStoreInfo;

	public TlsAuthentication(Optional<KeyStoreInfo> theKeyStoreInfo, Optional<TrustStoreInfo> theTrustStoreInfo) {
		myKeyStoreInfo = theKeyStoreInfo;
		myTrustStoreInfo = theTrustStoreInfo;
	}

	public Optional<KeyStoreInfo> getKeyStoreInfo() {
		return myKeyStoreInfo;
	}

	public Optional<TrustStoreInfo> getTrustStoreInfo() {
		return myTrustStoreInfo;
	}
}
