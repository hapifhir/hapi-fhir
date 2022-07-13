package ca.uhn.fhir.rest.client.tls;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.tls.KeyStoreInfo;
import ca.uhn.fhir.tls.KeyStoreType;
import ca.uhn.fhir.tls.PathType;
import ca.uhn.fhir.tls.StoreInfo;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StoreInfoTest {

	@Test
	public void testPathTypeFile(){
		KeyStoreInfo keyStoreInfo = new KeyStoreInfo("file:///my-file.p12", "storePassword" , "keyPassword", "alias");
		assertEquals("/my-file.p12", keyStoreInfo.getFilePath());
		assertEquals(PathType.FILE, keyStoreInfo.getPathType());
	}

	@Test
	public void testPathTypeResource(){
		KeyStoreInfo keyStoreInfo = new KeyStoreInfo("classpath:/my-file.p12", "storePassword" , "keyPassword", "alias");
		assertEquals("/my-file.p12", keyStoreInfo.getFilePath());
		assertEquals(PathType.RESOURCE, keyStoreInfo.getPathType());
	}

	@Test
	public void testPathTypeInvalid(){
		Exception exceptionThrown = assertThrows(StoreInfo.StoreInfoException.class, () -> {
			new KeyStoreInfo("invalid:///my-file.p12", "storePassword" , "keyPassword", "alias");
		});
		assertEquals(Msg.code(2117)+"Invalid path prefix", exceptionThrown.getMessage());
	}

	@Test
	public void testFileTypeP12(){
		KeyStoreInfo keyStoreInfo = new KeyStoreInfo("file:///my-file.p12", "storePassword" , "keyPassword", "alias");
		assertEquals(KeyStoreType.PKCS12, keyStoreInfo.getType());
	}

	@Test
	public void testFileTypeJks(){
		KeyStoreInfo keyStoreInfo = new KeyStoreInfo("file:///my-file.jks", "storePassword" , "keyPassword", "alias");
		assertEquals(KeyStoreType.JKS, keyStoreInfo.getType());
	}

	@Test
	public void testFileTypeInvalid(){
		Exception exceptionThrown = assertThrows(IllegalArgumentException.class, () -> {
			new KeyStoreInfo("file:///my-file.invalid", "storePassword" , "keyPassword", "alias");
		});
		assertEquals(Msg.code(2106)+"Invalid KeyStore Type", exceptionThrown.getMessage());
	}

	@Test
	public void testStorePass(){
		KeyStoreInfo keyStoreInfo = new KeyStoreInfo("file:///my-file.p12", "storePassword" , "keyPassword", "alias");
		assertTrue(StringUtils.equals("storePassword", new String(keyStoreInfo.getStorePass())));
	}

	@Test
	public void testKeyPass(){
		KeyStoreInfo keyStoreInfo = new KeyStoreInfo("file:///my-file.p12", "storePassword" , "keyPassword", "alias");
		assertTrue(StringUtils.equals("keyPassword", new String(keyStoreInfo.getKeyPass())));
	}

	@Test
	public void testAlias(){
		KeyStoreInfo keyStoreInfo = new KeyStoreInfo("file:///my-file.p12", "storePassword" , "keyPassword", "alias");
		assertEquals("alias", keyStoreInfo.getAlias());
	}
}
