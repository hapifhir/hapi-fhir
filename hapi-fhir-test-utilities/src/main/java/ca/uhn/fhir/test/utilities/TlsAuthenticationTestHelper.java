/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.tls.KeyStoreInfo;
import ca.uhn.fhir.tls.TlsAuthentication;
import ca.uhn.fhir.tls.TrustStoreInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TlsAuthenticationTestHelper implements AfterEachCallback {

	private static final String KEYSTORE_RESOURCE_PATH = "classpath:/tls/client-keystore.p12";
	private static final String KEYSTORE_STOREPASS = "changeit";
	private static final String KEYSTORE_KEYPASS = "changeit";
	private static final String KEYSTORE_ALIAS = "client";

	private static final String TRUSTSTORE_RESOURCE_PATH = "classpath:/tls/client-truststore.p12";
	private static final String TRUSTSTORE_STOREPASS = "changeit";
	private static final String TRUSTSTORE_ALIAS = "client";

	private final TlsAuthentication myTlsAuthentication;
	private final KeyStoreInfo myKeystoreInfo;
	private final TrustStoreInfo myTrustStoreInfo;
	private File myTempFile;

	public TlsAuthenticationTestHelper(){
		myKeystoreInfo = new KeyStoreInfo(KEYSTORE_RESOURCE_PATH, KEYSTORE_STOREPASS, KEYSTORE_KEYPASS, KEYSTORE_ALIAS);
		myTrustStoreInfo = new TrustStoreInfo(TRUSTSTORE_RESOURCE_PATH, TRUSTSTORE_STOREPASS, TRUSTSTORE_ALIAS);
		myTlsAuthentication = new TlsAuthentication(Optional.of(myKeystoreInfo), Optional.of(myTrustStoreInfo));
	}

	@Override
	public void afterEach(ExtensionContext theExtensionContext) throws Exception {
		if(myTempFile != null && myTempFile.exists()){
			assertTrue(myTempFile.delete());
			myTempFile = null;
		}
	}

	public String[] createBaseRequestGeneratingCommandArgs(String[] theBaseArgs, String theUrlFlag, boolean theAddTls, BaseRestServerHelper theRestServerHelper){
		if(isBlank(theUrlFlag)){
			return theBaseArgs;
		}

		String[] retVal;
		if(theAddTls){
			int newSize = theBaseArgs.length +  4;
			retVal = Arrays.copyOf(theBaseArgs, newSize);

			retVal[newSize - 4] = theUrlFlag;
			retVal[newSize - 3] = theRestServerHelper.getSecureBase(); // HTTPS

			myTempFile = createTlsAuthenticationFile();
			retVal[newSize - 2] = "--tls-auth";
			retVal[newSize - 1] = myTempFile.getAbsolutePath();
		}
		else {
			int newSize = theBaseArgs.length +  2;
			retVal = Arrays.copyOf(theBaseArgs, newSize);
			retVal[newSize - 2] = theUrlFlag;
			retVal[newSize - 1] = theRestServerHelper.getBase(); // HTTP
		}

		return retVal;
	}

	public TlsAuthentication getTlsAuthentication(){
		return myTlsAuthentication;
	}

	public File createTlsAuthenticationFile() {
		try {
			ObjectMapper mapper = new ObjectMapper();

			ObjectNode keyStore = mapper.createObjectNode();
			keyStore.put("filePath", KEYSTORE_RESOURCE_PATH);
			keyStore.put("storePass", KEYSTORE_STOREPASS);
			keyStore.put("keyPass", KEYSTORE_KEYPASS);
			keyStore.put("alias", KEYSTORE_ALIAS);

			ObjectNode trustStore = mapper.createObjectNode();
			trustStore.put("filePath", TRUSTSTORE_RESOURCE_PATH);
			trustStore.put("storePass", TRUSTSTORE_STOREPASS);
			trustStore.put("alias", TRUSTSTORE_ALIAS);

			ObjectNode json = mapper.createObjectNode();
			json.set("keyStore", keyStore);
			json.set("trustStore", trustStore);

			File inputFile = File.createTempFile("smile-unit-test", ".json");
			try (FileWriter inputFileWriter = new FileWriter(inputFile, StandardCharsets.UTF_8, false)) {
				IOUtils.write(json.toString(), inputFileWriter);
			}
			return inputFile;
		} catch (Exception e) {
			throw new RuntimeException(Msg.code(2122)+"Failed to load test TLS authentication file", e);
		}
	}
}
