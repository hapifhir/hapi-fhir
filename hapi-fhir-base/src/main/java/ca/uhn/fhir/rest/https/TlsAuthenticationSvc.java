package ca.uhn.fhir.rest.https;

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
import java.io.File;
import java.io.FileInputStream;
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
				contextBuilder.loadKeyMaterial(new File(keyStoreInfo.getFilePath()), keyStoreInfo.getStorePass(), keyStoreInfo.getKeyPass(), privateKeyStrategy);
			}

			if(tlsAuth.getTrustStoreInfo().isPresent()){
				TrustStoreInfo trustStoreInfo = tlsAuth.getTrustStoreInfo().get();
				contextBuilder.loadTrustMaterial(new File(trustStoreInfo.getFilePath()), trustStoreInfo.getStorePass(), TrustSelfSignedStrategy.INSTANCE);
			}

			return Optional.of(contextBuilder.build());
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public static X509TrustManager createTrustManager(Optional<TrustStoreInfo> theTrustStoreInfo) {
		try {
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			if (theTrustStoreInfo.isEmpty()) {
				trustManagerFactory.init((KeyStore) null); // Load Trust Manager Factory with default Java truststore
			} else {
				TrustStoreInfo trustStoreInfo = theTrustStoreInfo.get();
				KeyStore trustStore = KeyStore.getInstance(trustStoreInfo.getType().toString());
				trustStore.load(new FileInputStream(trustStoreInfo.getFilePath()), trustStoreInfo.getStorePass());
				trustManagerFactory.init(trustStore);
			}
			for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
				if (trustManager instanceof X509TrustManager) {
					return (X509TrustManager) trustManager;
				}
			}
			throw new RuntimeException("Could not find Trust Manager");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static HostnameVerifier createHostnameVerifier(Optional<TrustStoreInfo> theTrustStoreInfo){
		return theTrustStoreInfo.isPresent() ? new DefaultHostnameVerifier() : new NoopHostnameVerifier();
	}
}
