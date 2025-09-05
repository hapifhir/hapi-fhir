package ca.uhn.fhir.implementationguide;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.FhirTerser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A helper class to allow creation of Implementation Guides with custom values for
 * use in tests.
 */
public class ImplementationGuideCreator {

	private static final Logger ourLog = LoggerFactory.getLogger(ImplementationGuideCreator.class);

	@Language("JSON")
	private static final String PACKAGE_JSON_BASE = """
		{
		  "name": "test.fhir.ca.com",
		  "version": "1.2.3",
		  "tools-version": 3,
		  "type": "fhir.ig",
		  "date": "20200831134427",
		  "license": "not-open-source",
		  "canonical": "http://test-ig.com/fhir/us/providerdataexchange",
		  "url": "file://C:\\\\dev\\\\test-exchange\\\\fsh\\\\build\\\\output",
		  "title": "Test Implementation Guide",
		  "description": "Test Implementation Guide",
		  "fhirVersions": [
		    "4.0.1"
		  ],
		  "dependencies": {
		  },
		  "author": "SmileCDR",
		  "maintainers": [
		    {
		      "name": "Smile",
		      "email": "smilecdr@smiledigitalhealth.com",
		      "url": "https://www.smilecdr.com"
		    }
		  ],
		  "directories": {
		    "lib": "package",
		    "example": "example"
		  }
		}
	""";

	private Path myDir;
	private final FhirContext myFhirContext;

	private final FhirTerser myTerser;
	private final IParser myParser;

	private final String myPackageJson;

	private final String myPackageName;
	private final String myPackageVersion;

	private final List<IBaseResource> mySPToInclude = new ArrayList<>();

	/**
	 * If set true, lenient IG creator will forego all "assertions" in creation.
	 * This can be useful for testing failure cases where we are constructing
	 * IGs that we know are broken.
	 */
	private boolean myLenientSetting;

	public ImplementationGuideCreator(@Nonnull FhirContext theFhirContext) throws JsonProcessingException {
		this(theFhirContext, "test.fhir.ca.com", "1.2.3");
	}

	public ImplementationGuideCreator(@Nonnull FhirContext theFhirContext, String thePackageName, String thePackageVersion) throws JsonProcessingException {
		this(theFhirContext, theFhirContext.getVersion().getVersion().getFhirVersionString(), thePackageName, thePackageVersion);
	}

	/**
	 * Constructor
	 * @param theFhirContext - FhirContext to use
	 * @param theFhirVersion - fhir version to use (provided to allow setting a custom value different form the FhirContext)
	 * @param theName - name to set in package.json's name field
	 * @param theVersion - version to set in package.json's version field
	 */
	@SuppressWarnings("unchecked")
	public ImplementationGuideCreator(
		@Nonnull FhirContext theFhirContext,
		String theFhirVersion,
		String theName,
		String theVersion
	) throws JsonProcessingException {
		myFhirContext = theFhirContext;
		myTerser = myFhirContext.newTerser();
		myParser = myFhirContext.newJsonParser();
		myPackageName = theName;
		myPackageVersion = theVersion;

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		Map<String, Object> mapJson = mapper.readValue(PACKAGE_JSON_BASE, Map.class);

		// update provided values
		List<String> versions = (List<String>) mapJson.get("fhirVersions");
		versions.clear();
		versions.add(theFhirVersion);
		mapJson.replace("name", myPackageName);
		mapJson.replace("version", myPackageVersion);

		myPackageJson = mapper.writerWithDefaultPrettyPrinter()
			.writeValueAsString(mapJson);

		ourLog.info(myPackageJson);
	}

	/**
	 * Sets the directory where files will be created.
	 * Should be a temp dir for tests.
	 */
	public ImplementationGuideCreator setDirectory(Path thePath) {
		myDir = thePath;
		return this;
	}

	/**
	 * If set true, this creator will forego all validation on construction
	 * (except for directory existence, since files need to be created somewhere)
	 * and create the IG as requested, even if the included resources are invalid
	 * or broken in some way.
	 *
	 * By default this is set to false.
	 */
	public ImplementationGuideCreator setLenient(boolean theLenientSetting) {
		myLenientSetting = theLenientSetting;
		return this;
	}

	public String getPackageName() {
		return myPackageName;
	}

	public String getPackageVersion() {
		return myPackageVersion;
	}

	/**
	 * Adds a SearchParameter to the ImplementationGuide.
	 * Not to be used for other resource types.
	 */
	public void addSPToIG(IBaseResource theSP) {
		for (String reqdField : new String[] {"name", "status", "url", "code", "expression", "type"}) {
			Optional<String> fieldOp = myTerser.getSinglePrimitiveValue(theSP, reqdField);
			if (!myLenientSetting) {
				assertTrue(fieldOp.isPresent(), String.format("%s is a required field for IG SearchParameters", reqdField));
			}
		}
		List<IBase> base = myTerser.getValues(theSP, "base");
		if (!myLenientSetting) {
			assertTrue(base != null && !base.isEmpty(), "SP.base is a required field (should be the same as targets).");
		}

		mySPToInclude.add(theSP);
	}

	private void verifyDir() {
		String msg = "Directory must be set first.";

		assertNotNull(myDir, msg);
		assertTrue(isNotBlank(myDir.toString()), msg);
	}

	/**
	 * Creates an the IG from all the provided SearchParameters,
	 * zips them up, and provides the path to the newly created gzip file.
	 */
	public Path createTestIG() throws IOException {
		verifyDir();

		Path sourceDir = Files.createDirectory(Path.of(myDir.toString(), "package"));

		// add the package.json
		addFileToDir(myPackageJson, "package.json", sourceDir);

		// add search parameters
		int index = 0;
		for (IBaseResource sp : mySPToInclude) {
			// iterating over SPs and include them
			Optional<String> nameOp = myTerser.getSinglePrimitiveValue(sp, "name");

			String name = null;
			if (!myLenientSetting) {
				assertTrue(nameOp.isPresent());
			} else if (nameOp.isPresent()) {
				name = nameOp.get();
			} else {
				name = "sp_" + index;
			}

			addFileToDir(myParser.encodeResourceToString(sp), name + ".json", sourceDir);
			index++;
		}

		// we can add other resources here (not req'd for now)

		Path outputFileName = Files.createFile(Path.of(myDir.toString(), myPackageName + ".gz.tar"));
		GZipCreatorUtil.createTarGz(sourceDir, outputFileName);
		return outputFileName;
	}

	private void addFileToDir(String theContent, String theFileName, Path theOutputPath) throws IOException {
		byte[] bytes = new byte[1024];
		int length = 0;

		try (FileOutputStream outputStream = new FileOutputStream(theOutputPath.toString() + "/" + theFileName)) {
			try (InputStream stream = new ByteArrayInputStream(theContent.getBytes(StandardCharsets.UTF_8))) {
				while ((length = stream.read(bytes)) >= 0) {
					outputStream.write(bytes, 0, length);
				}
			}
		}
	}

}
