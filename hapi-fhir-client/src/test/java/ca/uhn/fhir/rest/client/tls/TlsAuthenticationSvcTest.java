package ca.uhn.fhir.rest.client.tls;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.tls.KeyStoreInfo;
import ca.uhn.fhir.tls.TlsAuthentication;
import ca.uhn.fhir.tls.TrustStoreInfo;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TlsAuthenticationSvcTest {

	private KeyStoreInfo myServerKeyStoreInfo;
	private TrustStoreInfo myServerTrustStoreInfo;
	private TlsAuthentication myServerTlsAuthentication;

	private KeyStoreInfo myClientKeyStoreInfo;
	private TrustStoreInfo myClientTrustStoreInfo;
	private TlsAuthentication myClientTlsAuthentication;

	@BeforeEach
	public void beforeEach(){
		myServerKeyStoreInfo = new KeyStoreInfo("classpath:/server-keystore.p12", "changeit", "changeit", "server");
		myServerTrustStoreInfo = new TrustStoreInfo("classpath:/server-truststore.p12", "changeit", "client");
		myServerTlsAuthentication = new TlsAuthentication(Optional.of(myServerKeyStoreInfo), Optional.of(myServerTrustStoreInfo));

		myClientKeyStoreInfo = new KeyStoreInfo("classpath:/client-keystore.p12", "changeit", "changeit", "client");
		myClientTrustStoreInfo = new TrustStoreInfo("classpath:/client-truststore.p12", "changeit", "server");
		myClientTlsAuthentication = new TlsAuthentication(Optional.of(myClientKeyStoreInfo), Optional.of(myClientTrustStoreInfo));
	}

	@Test
	public void testCreateSslContextEmpty(){
		Optional<TlsAuthentication> emptyAuthentication = Optional.empty();
		Optional<SSLContext> result = TlsAuthenticationSvc.createSslContext(emptyAuthentication);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testCreateSslContextPresent(){
		Optional<SSLContext> result = TlsAuthenticationSvc.createSslContext(Optional.of(myServerTlsAuthentication));
		assertFalse(result.isEmpty());
		assertEquals("TLS", result.get().getProtocol());
	}

	@Test
	public void testCreateSslContextPresentInvalid(){
		KeyStoreInfo invalidKeyStoreInfo = new KeyStoreInfo("file:///INVALID.p12", "changeit", "changeit", "server");
		TlsAuthentication invalidTlsAuthentication = new TlsAuthentication(Optional.of(invalidKeyStoreInfo), Optional.of(myServerTrustStoreInfo));
		Exception thrownException = assertThrows(TlsAuthenticationSvc.TlsAuthenticationException.class, () -> {
			TlsAuthenticationSvc.createSslContext(Optional.of(invalidTlsAuthentication));
		});
		assertEquals(Msg.code(2102)+"Failed to create SSLContext", thrownException.getMessage());
	}

	@Test
	public void testCreateKeyStoreP12() throws Exception {
		KeyStore keyStore = TlsAuthenticationSvc.createKeyStore(myServerKeyStoreInfo);
		assertNotNull(keyStore.getKey(myServerKeyStoreInfo.getAlias(), myServerKeyStoreInfo.getKeyPass()));
	}

	@Test
	public void testCreateKeyStoreJKS() throws Exception {
		KeyStoreInfo keyStoreInfo = new KeyStoreInfo("classpath:/keystore.jks", "changeit", "changeit", "client");
		KeyStore keyStore = TlsAuthenticationSvc.createKeyStore(keyStoreInfo);
		assertNotNull(keyStore.getKey(keyStoreInfo.getAlias(), keyStoreInfo.getKeyPass()));
	}

	@Test
	public void testCreateKeyStoreNonExistentFile() throws Exception {
		KeyStoreInfo keyStoreInfo = new KeyStoreInfo("classpath:/non-existent.p12", "changeit", "changeit", "server");
		Exception exceptionThrown = assertThrows(TlsAuthenticationSvc.TlsAuthenticationException.class, () -> {
			TlsAuthenticationSvc.createKeyStore(keyStoreInfo);
		});
		assertEquals(Msg.code(2103)+"Failed to create KeyStore", exceptionThrown.getMessage());
	}

	@Test
	public void testCreateTrustStoreJks() throws Exception {
		TrustStoreInfo trustStoreInfo = new TrustStoreInfo("classpath:/truststore.jks", "changeit",  "client");
		KeyStore keyStore = TlsAuthenticationSvc.createKeyStore(trustStoreInfo);
		assertNotNull(keyStore.getCertificate(trustStoreInfo.getAlias()));
	}

	@Test
	public void testCreateTrustStoreP12() throws Exception {
		KeyStore keyStore = TlsAuthenticationSvc.createKeyStore(myServerTrustStoreInfo);
		assertNotNull(keyStore.getCertificate(myServerTrustStoreInfo.getAlias()));
	}

	@Test
	public void testCreateTrustStoreNonExistentFile() throws Exception {
		TrustStoreInfo trustStoreInfo = new TrustStoreInfo("classpath:/non-existent.p12", "changeit", "server");
		Exception exceptionThrown = assertThrows(TlsAuthenticationSvc.TlsAuthenticationException.class, () -> {
			TlsAuthenticationSvc.createKeyStore(trustStoreInfo);
		});
		assertEquals(Msg.code(2103)+"Failed to create KeyStore", exceptionThrown.getMessage());
	}

	@Test
	public void testCreateTrustManager() throws Exception{
		X509TrustManager trustManager = TlsAuthenticationSvc.createTrustManager(Optional.of(myClientTrustStoreInfo));
		KeyStore keyStore = TlsAuthenticationSvc.createKeyStore(myServerKeyStoreInfo);
		Certificate serverCertificate = keyStore.getCertificate(myServerKeyStoreInfo.getAlias());

		assertEquals(1, trustManager.getAcceptedIssuers().length);
		assertEquals(serverCertificate, trustManager.getAcceptedIssuers()[0]);
	}

	@Test
	public void testCreateTrustManagerInvalid() throws Exception{
		TrustStoreInfo invalidKeyStoreInfo = new TrustStoreInfo("file:///INVALID.p12", "changeit", "client");
		Exception exceptionThrown = assertThrows(TlsAuthenticationSvc.TlsAuthenticationException.class, () -> {
			TlsAuthenticationSvc.createTrustManager(Optional.of(invalidKeyStoreInfo));
		});
		assertEquals(Msg.code(2105)+"Failed to create TrustManager", exceptionThrown.getMessage());
	}

	@Test
	public void testCreateHostnameVerifierEmptyTrustStoreInfo(){
		Optional<TrustStoreInfo> trustStoreInfo = Optional.empty();
		HostnameVerifier result = TlsAuthenticationSvc.createHostnameVerifier(trustStoreInfo);
		assertEquals(NoopHostnameVerifier.class, result.getClass());
	}

	@Test
	public void testCreateHostnameVerifierPresentTrustStoreInfo(){
		Optional<TrustStoreInfo> trustStoreInfo = Optional.of(myServerTrustStoreInfo);
		HostnameVerifier result = TlsAuthenticationSvc.createHostnameVerifier(trustStoreInfo);
		assertEquals(DefaultHostnameVerifier.class, result.getClass());
	}
}
