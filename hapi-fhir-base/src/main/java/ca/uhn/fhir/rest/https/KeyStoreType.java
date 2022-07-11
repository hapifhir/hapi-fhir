package ca.uhn.fhir.rest.https;

import ca.uhn.fhir.i18n.Msg;

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
		throw new IllegalArgumentException(Msg.code(2106)+"Invalid KeyStore Type");
	}
}
