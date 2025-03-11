/*-
 * #%L
 * HAPI FHIR - Client Framework using Apache HttpClient 5
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.rest.client.tls;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.tls.BaseStoreInfo;
import ca.uhn.fhir.tls.KeyStoreInfo;
import ca.uhn.fhir.tls.PathType;
import ca.uhn.fhir.tls.TlsAuthentication;
import ca.uhn.fhir.tls.TrustStoreInfo;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.apache.hc.client5.http.ssl.DefaultHostnameVerifier;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.ssl.PrivateKeyStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Optional;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ApacheHttp5TlsAuthenticationSvc {

	private ApacheHttp5TlsAuthenticationSvc() {}

	public static SSLContext createSslContext(@Nonnull TlsAuthentication theTlsAuthentication) {
		Validate.notNull(theTlsAuthentication, "theTlsAuthentication cannot be null");

		try {
			SSLContextBuilder contextBuilder = SSLContexts.custom();

			if (theTlsAuthentication.getKeyStoreInfo().isPresent()) {
				KeyStoreInfo keyStoreInfo =
						theTlsAuthentication.getKeyStoreInfo().get();
				PrivateKeyStrategy privateKeyStrategy = null;
				if (isNotBlank(keyStoreInfo.getAlias())) {
					privateKeyStrategy = (aliases, socket) -> keyStoreInfo.getAlias();
				}
				KeyStore keyStore = createKeyStore(keyStoreInfo);
				contextBuilder.loadKeyMaterial(keyStore, keyStoreInfo.getKeyPass(), privateKeyStrategy);
			}

			if (theTlsAuthentication.getTrustStoreInfo().isPresent()) {
				TrustStoreInfo trustStoreInfo =
						theTlsAuthentication.getTrustStoreInfo().get();
				KeyStore trustStore = createKeyStore(trustStoreInfo);
				contextBuilder.loadTrustMaterial(trustStore, TrustSelfSignedStrategy.INSTANCE);
			}

			return contextBuilder.build();
		} catch (Exception e) {
			throw new TlsAuthenticationException(Msg.code(2575) + "Failed to create SSLContext", e);
		}
	}

	public static KeyStore createKeyStore(BaseStoreInfo theStoreInfo) {
		try {
			KeyStore keyStore = KeyStore.getInstance(theStoreInfo.getType().toString());

			if (PathType.RESOURCE.equals(theStoreInfo.getPathType())) {
				try (InputStream inputStream =
						ApacheHttp5TlsAuthenticationSvc.class.getResourceAsStream(theStoreInfo.getFilePath())) {
					validateKeyStoreExists(inputStream);
					keyStore.load(inputStream, theStoreInfo.getStorePass());
				}
			} else if (PathType.FILE.equals(theStoreInfo.getPathType())) {
				try (InputStream inputStream = new FileInputStream(theStoreInfo.getFilePath())) {
					validateKeyStoreExists(inputStream);
					keyStore.load(inputStream, theStoreInfo.getStorePass());
				}
			}
			return keyStore;
		} catch (Exception e) {
			throw new TlsAuthenticationException(Msg.code(2576) + "Failed to create KeyStore", e);
		}
	}

	public static void validateKeyStoreExists(InputStream theInputStream) {
		if (theInputStream == null) {
			throw new TlsAuthenticationException(Msg.code(2577) + "Keystore does not exists");
		}
	}

	public static X509TrustManager createTrustManager(Optional<TrustStoreInfo> theTrustStoreInfo) {
		try {
			TrustManagerFactory trustManagerFactory =
					TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			if (!theTrustStoreInfo.isPresent()) {
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
			throw new TlsAuthenticationException(Msg.code(2578) + "Could not find X509TrustManager");
		} catch (Exception e) {
			throw new TlsAuthenticationException(Msg.code(2579) + "Failed to create X509TrustManager");
		}
	}

	public static HostnameVerifier createHostnameVerifier(Optional<TrustStoreInfo> theTrustStoreInfo) {
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
