package ca.uhn.fhir.rest.https;

import java.util.Arrays;
import java.util.List;

public enum KeyStoreType {

	PKCS12("p12", "pfx"),
	JKS("jks");

	private List<String> myFileExtensions;

	KeyStoreType(String... theFileExtensions){
		myFileExtensions = Arrays.asList(theFileExtensions);
	}

	public List<String> getFileExtensions() {
		return myFileExtensions;
	}

	public static KeyStoreType fromFileExtension(String theFileExtension) {
		for(KeyStoreType type : KeyStoreType.values()){
			if(type.getFileExtensions().contains(theFileExtension.toLowerCase())){
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid KeyStore Type");
	}
}
