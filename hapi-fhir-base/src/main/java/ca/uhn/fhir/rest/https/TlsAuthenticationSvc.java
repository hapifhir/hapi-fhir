package ca.uhn.fhir.rest.https;

import ca.uhn.fhir.i18n.Msg;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TlsAuthenticationSvc {

	private TlsAuthenticationSvc(){}

	public static Optional<SSLContext> createSslContext(Optional<TlsAuthentication> theTlsAuthentication){
		if(theTlsAuthentication.isEmpty()){
			return Optional.empty();
		}

		try{
			SSLContextBuilder contextBuilder = SSLContexts.custom();

			TlsAuthentication tlsAuth = theTlsAuthentication.get();
			if(tlsAuth.getKeyStoreInfo().isPresent()){
				KeyStoreInfo keyStoreInfo = tlsAuth.getKeyStoreInfo().get();
				PrivateKeyStrategy privateKeyStrategy = null;
				if(isNotBlank(keyStoreInfo.getAlias())){
					privateKeyStrategy = (aliases, socket) -> keyStoreInfo.getAlias();
				}
				KeyStore keyStore = createKeyStore(keyStoreInfo);
				contextBuilder.loadKeyMaterial(keyStore, keyStoreInfo.getKeyPass(), privateKeyStrategy);
			}

			if(tlsAuth.getTrustStoreInfo().isPresent()){
				TrustStoreInfo trustStoreInfo = tlsAuth.getTrustStoreInfo().get();
				KeyStore trustStore = createKeyStore(trustStoreInfo);
				contextBuilder.loadTrustMaterial(trustStore, TrustSelfSignedStrategy.INSTANCE);
			}

			return Optional.of(contextBuilder.build());
		}
		catch (Exception e){
			throw new TlsAuthenticationException(Msg.code(2102)+"Failed to create SSLContext", e);
		}
	}
	
	public static KeyStore createKeyStore(StoreInfo theStoreInfo){
		try {
			KeyStore keyStore = KeyStore.getInstance(theStoreInfo.getType().toString());

			final String prefixedFilePath = theStoreInfo.getFilePath();
			if(prefixedFilePath.startsWith(PathType.RESOURCE.getPrefix())){
				String unPrefixedPath = prefixedFilePath.substring(PathType.RESOURCE.getPrefix().length());
				try(InputStream inputStream = TlsAuthenticationSvc.class.getResourceAsStream(unPrefixedPath)){
					keyStore.load(inputStream, theStoreInfo.getStorePass());
				}
			}
			else if(prefixedFilePath.startsWith(PathType.FILE.getPrefix())){
				String unPrefixedPath = prefixedFilePath.substring(PathType.FILE.getPrefix().length());
				try(InputStream inputStream = new FileInputStream(unPrefixedPath)){
					keyStore.load(inputStream, theStoreInfo.getStorePass());
				}
			}
			return keyStore;
		}
		catch (Exception e){
			throw new TlsAuthenticationException(Msg.code(2103)+"Failed to create KeyStore", e);
		}
	}

	public static X509TrustManager createTrustManager(Optional<TrustStoreInfo> theTrustStoreInfo) {
		try {
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			if (theTrustStoreInfo.isEmpty()) {
				trustManagerFactory.init((KeyStore) null); // Load Trust Manager Factory with default Java truststore
			} else {
				TrustStoreInfo trustStoreInfo = theTrustStoreInfo.get();
				KeyStore trustStore = createKeyStore(trustStoreInfo);
				trustManagerFactory.init(trustStore);
			}
			for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
				if (trustManager instanceof X509TrustManager) {
					return (X509TrustManager) trustManager;
				}
			}
			throw new TlsAuthenticationException(Msg.code(2104)+"Could not find TrustManager");
		}
		catch (Exception e) {
			throw new TlsAuthenticationException(Msg.code(2105)+"Failed to create TrustManager");
		}
	}

	public static HostnameVerifier createHostnameVerifier(Optional<TrustStoreInfo> theTrustStoreInfo){
		return theTrustStoreInfo.isPresent() ? new DefaultHostnameVerifier() : new NoopHostnameVerifier();
	}

	public static class TlsAuthenticationException extends RuntimeException {
		private static final long serialVersionUID = 1l;

		public TlsAuthenticationException(String theMessage, Throwable theCause) {
			super(theMessage, theCause);
		}

		public TlsAuthenticationException(String theMessage) {
			super(theMessage);
		}

	}
}
