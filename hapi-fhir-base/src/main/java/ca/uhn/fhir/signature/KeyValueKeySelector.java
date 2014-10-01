package ca.uhn.fhir.signature;

import java.security.Key;
import java.security.KeyException;
import java.security.PublicKey;
import java.util.List;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyValue;

public class KeyValueKeySelector extends KeySelector {

	public KeySelectorResult select(KeyInfo keyInfo, KeySelector.Purpose purpose, AlgorithmMethod method, XMLCryptoContext context) throws KeySelectorException {

		if (keyInfo == null) {

			throw new KeySelectorException("Null KeyInfo object!");
		}

		SignatureMethod sm = (SignatureMethod) method;
		List list = keyInfo.getContent();

		for (int i = 0; i < list.size(); i++) {

			XMLStructure xmlStructure = (XMLStructure) list.get(i);

			if (xmlStructure instanceof KeyValue) {

				PublicKey pk = null;

				try {

					pk = ((KeyValue) xmlStructure).getPublicKey();
				} catch (KeyException ke) {

					throw new KeySelectorException(ke);
				}

				// make sure algorithm is compatible with method
				if (algEquals(sm.getAlgorithm(), pk.getAlgorithm())) {
					return new SimpleKeySelectorResult(pk);
				}
			}
		}

		throw new KeySelectorException("No KeyValue element found!");
	}

	static boolean algEquals(String algURI, String algName) {

		if (algName.equalsIgnoreCase("DSA") && algURI.equalsIgnoreCase(SignatureMethod.DSA_SHA1)) {

			return true;
		} else if (algName.equalsIgnoreCase("RSA") && algURI.equalsIgnoreCase(SignatureMethod.RSA_SHA1)) {

			return true;
		} else {

			return false;
		}
	}

	static class SimpleKeySelectorResult implements KeySelectorResult {

		private Key key;

		SimpleKeySelectorResult(Key key) {

			this.key = key;
		}

		public Key getKey() {

			return (this.key);
		}
	}
}