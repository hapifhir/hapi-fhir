package ca.uhn.fhir.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.parser.IParser;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.Enumerations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.fail;

public class HapiFhirCodeSystemGeneratorTest {

	public static final String HAPI_FHIR_STORAGE_RESPONSE_CODE_JSON = "HapiFhirStorageResponseCode.json";
	public static final FhirContext ourCtx = FhirContext.forR5Cached();
	private static final Logger ourLog = LoggerFactory.getLogger(HapiFhirCodeSystemGeneratorTest.class);
	private String myPath;

	@BeforeEach
	public void findDirectory() {
		String path = "hapi-fhir-base/src/main/resources/ca/uhn/fhir/context/support";
		if (new File(path + "/" + HAPI_FHIR_STORAGE_RESPONSE_CODE_JSON).exists()) {
			myPath = path;
			return;
		} else {
			path = "../hapi-fhir-base/src/main/resources/ca/uhn/fhir/context/support";
			if (new File(path + "/" + HAPI_FHIR_STORAGE_RESPONSE_CODE_JSON).exists()) {
				myPath = path;
				return;
			}
		}
		fail("Could not find " + HAPI_FHIR_STORAGE_RESPONSE_CODE_JSON);
	}

	@Test
	public void createStorageResponseCodeEnumCodeSystem() throws IOException {
		CodeSystem cs = new CodeSystem();
		cs.setUrl(StorageResponseCodeEnum.SYSTEM);
		cs.getAuthorFirstRep().setName("HAPI FHIR");
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(Enumerations.CodeSystemContentMode.COMPLETE);
		cs.setCaseSensitive(true);
		cs.setCopyright("Licensed under the terms of the Apache Software License 2.0.");
		for (var next : StorageResponseCodeEnum.values()) {
			cs.addConcept()
				.setCode(next.getCode())
				.setDisplay(next.getDisplay());
		}

		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);
		String fileName = HAPI_FHIR_STORAGE_RESPONSE_CODE_JSON;
		String encoded = parser.encodeResourceToString(cs);

		String path = myPath + "/" + fileName;
		String existing;
		try (FileReader r = new FileReader(path, StandardCharsets.UTF_8)) {
			existing = IOUtils.toString(r);
		}

		if (!existing.equals(encoded)) {
			ourLog.info("Updating content of file: {}", path);
			try (FileWriter w = new FileWriter(path, false)) {
				w.append(encoded);
			}
		} else {
			ourLog.info("Content of file is up to date: {}", path);
		}

	}

}
