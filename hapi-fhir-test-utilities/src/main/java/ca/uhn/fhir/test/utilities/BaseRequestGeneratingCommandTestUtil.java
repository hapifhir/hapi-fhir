package ca.uhn.fhir.test.utilities;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.rest.https.KeyStoreInfo;
import ca.uhn.fhir.rest.https.TlsAuthentication;
import ca.uhn.fhir.rest.https.TrustStoreInfo;
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

public class BaseRequestGeneratingCommandTestUtil implements AfterEachCallback {

	private static final String KEYSTORE_RESOURCE_PATH = "classpath:/tls/client-keystore.p12";
	private static final String KEYSTORE_STOREPASS = "changeit";
	private static final String KEYSTORE_KEYPASS = "changeit";
	private static final String KEYSTORE_ALIAS = "1";

	private static final String TRUSTSTORE_RESOURCE_PATH = "classpath:/tls/client-truststore.p12";
	private static final String TRUSTSTORE_STOREPASS = "changeit";
	private static final String TRUSTSTORE_ALIAS = "client";

	private final Optional<TlsAuthentication> myTlsAuthentication;
	private final KeyStoreInfo myKeystoreInfo;
	private final TrustStoreInfo myTrustStoreInfo;
	private File myTempFile;

	public BaseRequestGeneratingCommandTestUtil(){
		myKeystoreInfo = new KeyStoreInfo(KEYSTORE_RESOURCE_PATH, KEYSTORE_STOREPASS, KEYSTORE_KEYPASS, KEYSTORE_ALIAS);

		myTrustStoreInfo = new TrustStoreInfo(TRUSTSTORE_RESOURCE_PATH, TRUSTSTORE_STOREPASS, TRUSTSTORE_ALIAS);

		myTlsAuthentication = Optional.of(new TlsAuthentication(Optional.of(myKeystoreInfo), Optional.of(myTrustStoreInfo)));
	}

	@Override
	public void afterEach(ExtensionContext theExtensionContext) throws Exception {
		if(myTempFile != null && myTempFile.exists()){
			assertTrue(myTempFile.delete());
			myTempFile = null;
		}
	}

	public String[] createArgs(String[] theBaseArgs, String theUrlFlag, boolean theAddTls, BaseRestServerHelper theRestServerHelper){
		if(isBlank(theUrlFlag)){
			return theBaseArgs;
		}

		int newItems = theAddTls ? 4 : 2;
		String url = theAddTls ? theRestServerHelper.getSecureBase() : theRestServerHelper.getBase();

		int newSize = theBaseArgs.length + newItems;
		String[] retVal = Arrays.copyOf(theBaseArgs, newSize);

		retVal[newSize - 2] = theUrlFlag;
		retVal[newSize - 1] = url;

		if(theAddTls){
			myTempFile = createTlsAuthenticationFile();
			retVal[newSize - 4] = "--tls-auth";
			retVal[newSize - 3] = myTempFile.getAbsolutePath();
		}

		return retVal;
	}

	public Optional<TlsAuthentication> getTlsAuthentication(){
		return myTlsAuthentication;
	}

	private File createTlsAuthenticationFile() {
		try {
			ObjectMapper mapper = new ObjectMapper();

			ObjectNode keyStore = mapper.createObjectNode();
			keyStore.put("filePath", myKeystoreInfo.getFilePath());
			keyStore.put("storePass", new String(myKeystoreInfo.getStorePass()));
			keyStore.put("keyPass", new String(myKeystoreInfo.getKeyPass()));
			keyStore.put("alias", myKeystoreInfo.getAlias());

			ObjectNode trustStore = mapper.createObjectNode();
			trustStore.put("filePath", myTrustStoreInfo.getFilePath());
			trustStore.put("storePass", new String(myTrustStoreInfo.getStorePass()));
			trustStore.put("alias", new String(myTrustStoreInfo.getAlias()));

			ObjectNode json = mapper.createObjectNode();
			json.set("keyStore", keyStore);
			json.set("trustStore", trustStore);

			File inputFile = File.createTempFile("smile-unit-test", ".json");
			try (FileWriter inputFileWriter = new FileWriter(inputFile, StandardCharsets.UTF_8, false)) {
				IOUtils.write(json.toString(), inputFileWriter);
			}
			return inputFile;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
