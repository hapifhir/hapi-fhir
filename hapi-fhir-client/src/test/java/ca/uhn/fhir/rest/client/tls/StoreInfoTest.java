package ca.uhn.fhir.rest.client.tls;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.tls.KeyStoreInfo;
import ca.uhn.fhir.tls.KeyStoreType;
import ca.uhn.fhir.tls.PathType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class StoreInfoTest {

	@Test
	public void testPathTypeFile(){
		KeyStoreInfo keyStoreInfo = new KeyStoreInfo("file:///my-file.p12", "storePassword" , "keyPassword", "alias");
		assertThat(keyStoreInfo.getFilePath()).isEqualTo("/my-file.p12");
		assertThat(keyStoreInfo.getPathType()).isEqualTo(PathType.FILE);
	}

	@Test
	public void testPathTypeResource(){
		KeyStoreInfo keyStoreInfo = new KeyStoreInfo("classpath:/my-file.p12", "storePassword" , "keyPassword", "alias");
		assertThat(keyStoreInfo.getFilePath()).isEqualTo("/my-file.p12");
		assertThat(keyStoreInfo.getPathType()).isEqualTo(PathType.RESOURCE);
	}

	@Test
	public void testPathTypeInvalid(){
		try {
			new KeyStoreInfo("invalid:///my-file.p12", "storePassword" , "keyPassword", "alias");
			fail("");		} catch (Exception e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(2117) + "Invalid path prefix");
		}
	}

	@Test
	public void testFileTypeP12(){
		KeyStoreInfo keyStoreInfo = new KeyStoreInfo("file:///my-file.p12", "storePassword" , "keyPassword", "alias");
		assertThat(keyStoreInfo.getType()).isEqualTo(KeyStoreType.PKCS12);
	}

	@Test
	public void testFileTypeJks(){
		KeyStoreInfo keyStoreInfo = new KeyStoreInfo("file:///my-file.jks", "storePassword" , "keyPassword", "alias");
		assertThat(keyStoreInfo.getType()).isEqualTo(KeyStoreType.JKS);
	}

	@Test
	public void testFileTypeInvalid(){
		try {
			new KeyStoreInfo("file:///my-file.invalid", "storePassword" , "keyPassword", "alias");
			fail("");		} catch (Exception e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(2121) + "Invalid KeyStore Type");
		}
	}

	@Test
	public void testStorePass(){
		KeyStoreInfo keyStoreInfo = new KeyStoreInfo("file:///my-file.p12", "storePassword" , "keyPassword", "alias");
		assertThat(StringUtils.equals("storePassword", new String(keyStoreInfo.getStorePass()))).isTrue();
	}

	@Test
	public void testKeyPass(){
		KeyStoreInfo keyStoreInfo = new KeyStoreInfo("file:///my-file.p12", "storePassword" , "keyPassword", "alias");
		assertThat(StringUtils.equals("keyPassword", new String(keyStoreInfo.getKeyPass()))).isTrue();
	}

	@Test
	public void testAlias(){
		KeyStoreInfo keyStoreInfo = new KeyStoreInfo("file:///my-file.p12", "storePassword" , "keyPassword", "alias");
		assertThat(keyStoreInfo.getAlias()).isEqualTo("alias");
	}
}
