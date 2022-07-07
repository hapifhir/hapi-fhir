package ca.uhn.fhir.test;

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

import ca.uhn.fhir.test.utilities.BaseRestServerHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TlsAuthenticationUtil {

	private TlsAuthenticationUtil(){}

	public static File createTlsAuthenticationFile() {
		try {
			ObjectMapper mapper = new ObjectMapper();

			ObjectNode keyStore = mapper.createObjectNode();
			URL keyStoreUrl = TlsAuthenticationUtil.class.getResource("/tls/client-keystore.p12");

			keyStore.put("filePath", keyStoreUrl.getPath());
			keyStore.put("storePass", "changeit");
			keyStore.put("keyPass", "changeit");
			keyStore.put("alias", "1");

			ObjectNode trustStore = mapper.createObjectNode();
			URL trustStoreUrl = TlsAuthenticationUtil.class.getResource("/tls/client-truststore.p12");
			trustStore.put("filePath", trustStoreUrl.getPath());
			trustStore.put("storePass", "changeit");

			ObjectNode json = mapper.createObjectNode();
			json.set("keyStore", keyStore);
			json.set("trustStore", trustStore);

			File tempSourceDir = Files.createTempDir();
			File inputFile = File.createTempFile("smile-unit-test", ".json", tempSourceDir);
			try (FileWriter inputFileWriter = new FileWriter(inputFile, StandardCharsets.UTF_8, false)) {
				IOUtils.write(json.toString(), inputFileWriter);
			}
			return inputFile;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
