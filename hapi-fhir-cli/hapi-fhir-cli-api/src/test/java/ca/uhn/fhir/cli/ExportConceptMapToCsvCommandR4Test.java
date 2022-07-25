package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.test.utilities.TlsAuthenticationTestHelper;
import ca.uhn.fhir.test.utilities.RestServerR4Helper;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExportConceptMapToCsvCommandR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExportConceptMapToCsvCommandR4Test.class);
	private static final String CM_URL = "http://example.com/conceptmap";
	private static final String VS_URL_1 = "http://example.com/valueset/1";
	private static final String VS_URL_2 = "http://example.com/valueset/2";
	private static final String CS_URL_1 = "http://example.com/codesystem/1";
	private static final String CS_URL_2 = "http://example.com/codesystem/2";
	private static final String CS_URL_3 = "http://example.com/codesystem/3";
	private static final String FILE = new File("./target/output_r4.csv").getAbsolutePath();

	static {
		System.setProperty("test", "true");
	}

	@RegisterExtension
	public final RestServerR4Helper myRestServerR4Helper = new RestServerR4Helper(true);
	@RegisterExtension
	public TlsAuthenticationTestHelper myTlsAuthenticationTestHelper = new TlsAuthenticationTestHelper();

	private final FhirContext myCtx = FhirContext.forR4();
	private final String myVersion = "r4";

	@BeforeEach
	public void before(){
		myRestServerR4Helper.setConceptMapResourceProvider(new HashMapResourceProviderConceptMapR4(myCtx));
		myRestServerR4Helper.getClient().create().resource(createConceptMap()).execute();
	}

	@AfterEach
	public void afterEach() {
		myRestServerR4Helper.clearDataAndCounts();
	}

	@AfterAll
	public static void afterAll(){
		TestUtil.randomizeLocaleAndTimezone();
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testExportConceptMapToCsvCommand(boolean theIncludeTls) throws IOException {
		ourLog.info("ConceptMap:\n" + myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(createConceptMap()));

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				ExportConceptMapToCsvCommand.COMMAND,
				"-v", myVersion,
				"-u", CM_URL,
				"-f", FILE,
				"-l"
			},
			"-t", theIncludeTls, myRestServerR4Helper
		));

		await().until(() -> new File(FILE).exists());

		String expected = "\"SOURCE_CODE_SYSTEM\",\"SOURCE_CODE_SYSTEM_VERSION\",\"TARGET_CODE_SYSTEM\",\"TARGET_CODE_SYSTEM_VERSION\",\"SOURCE_CODE\",\"SOURCE_DISPLAY\",\"TARGET_CODE\",\"TARGET_DISPLAY\",\"EQUIVALENCE\",\"COMMENT\"\n" +
			"\"http://example.com/codesystem/1\",\"Version 1s\",\"http://example.com/codesystem/2\",\"Version 2t\",\"Code 1a\",\"Display 1a\",\"Code 2a\",\"Display 2a\",\"equal\",\"2a This is a comment.\"\n" +
			"\"http://example.com/codesystem/1\",\"Version 1s\",\"http://example.com/codesystem/2\",\"Version 2t\",\"Code 1b\",\"Display 1b\",\"Code 2b\",\"Display 2b\",\"equal\",\"2b This is a comment.\"\n" +
			"\"http://example.com/codesystem/1\",\"Version 1s\",\"http://example.com/codesystem/2\",\"Version 2t\",\"Code 1c\",\"Display 1c\",\"Code 2c\",\"Display 2c\",\"equal\",\"2c This is a comment.\"\n" +
			"\"http://example.com/codesystem/1\",\"Version 1s\",\"http://example.com/codesystem/2\",\"Version 2t\",\"Code 1d\",\"Display 1d\",\"Code 2d\",\"Display 2d\",\"equal\",\"2d This is a comment.\"\n" +
			"\"http://example.com/codesystem/1\",\"Version 1s\",\"http://example.com/codesystem/3\",\"Version 3t\",\"Code 1a\",\"Display 1a\",\"Code 3a\",\"Display 3a\",\"equal\",\"3a This is a comment.\"\n" +
			"\"http://example.com/codesystem/1\",\"Version 1s\",\"http://example.com/codesystem/3\",\"Version 3t\",\"Code 1b\",\"Display 1b\",\"Code 3b\",\"Display 3b\",\"equal\",\"3b This is a comment.\"\n" +
			"\"http://example.com/codesystem/1\",\"Version 1s\",\"http://example.com/codesystem/3\",\"Version 3t\",\"Code 1c\",\"Display 1c\",\"Code 3c\",\"Display 3c\",\"equal\",\"3c This is a comment.\"\n" +
			"\"http://example.com/codesystem/1\",\"Version 1s\",\"http://example.com/codesystem/3\",\"Version 3t\",\"Code 1d\",\"Display 1d\",\"Code 3d\",\"Display 3d\",\"equal\",\"3d This is a comment.\"\n" +
			"\"http://example.com/codesystem/2\",\"Version 2s\",\"http://example.com/codesystem/3\",\"Version 3t\",\"Code 2a\",\"Display 2a\",\"Code 3a\",\"Display 3a\",\"equal\",\"3a This is a comment.\"\n" +
			"\"http://example.com/codesystem/2\",\"Version 2s\",\"http://example.com/codesystem/3\",\"Version 3t\",\"Code 2b\",\"Display 2b\",\"Code 3b\",\"Display 3b\",\"equal\",\"3b This is a comment.\"\n" +
			"\"http://example.com/codesystem/2\",\"Version 2s\",\"http://example.com/codesystem/3\",\"Version 3t\",\"Code 2c\",\"Display 2c\",\"Code 3c\",\"Display 3c\",\"equal\",\"3c This is a comment.\"\n" +
			"\"http://example.com/codesystem/2\",\"Version 2s\",\"http://example.com/codesystem/3\",\"Version 3t\",\"Code 2d\",\"Display 2d\",\"Code 3d\",\"Display 3d\",\"equal\",\"3d This is a comment.\"\n";
		String result = IOUtils.toString(new FileInputStream(FILE), Charsets.UTF_8);
		assertEquals(expected, result);

		FileUtils.deleteQuietly(new File(FILE));
	}

	static ConceptMap createConceptMap() {
		ConceptMap conceptMap = new ConceptMap();
		conceptMap
			.setUrl(CM_URL)
			.setSource(new UriType(VS_URL_1))
			.setTarget(new UriType(VS_URL_2));

		ConceptMap.ConceptMapGroupComponent group = conceptMap.addGroup();
		group
			.setSource(CS_URL_1)
			.setSourceVersion("Version 1s")
			.setTarget(CS_URL_2)
			.setTargetVersion("Version 2t");

		ConceptMap.SourceElementComponent element = group.addElement();
		element
			.setCode("Code 1a")
			.setDisplay("Display 1a");

		ConceptMap.TargetElementComponent target = element.addTarget();
		target
			.setCode("Code 2a")
			.setDisplay("Display 2a")
			.setEquivalence(ConceptMapEquivalence.EQUAL)
			.setComment("2a This is a comment.");

		element = group.addElement();
		element
			.setCode("Code 1b")
			.setDisplay("Display 1b");

		target = element.addTarget();
		target
			.setCode("Code 2b")
			.setDisplay("Display 2b")
			.setEquivalence(ConceptMapEquivalence.EQUAL)
			.setComment("2b This is a comment.");

		element = group.addElement();
		element
			.setCode("Code 1c")
			.setDisplay("Display 1c");

		target = element.addTarget();
		target
			.setCode("Code 2c")
			.setDisplay("Display 2c")
			.setEquivalence(ConceptMapEquivalence.EQUAL)
			.setComment("2c This is a comment.");

		element = group.addElement();
		element
			.setCode("Code 1d")
			.setDisplay("Display 1d");

		target = element.addTarget();
		target
			.setCode("Code 2d")
			.setDisplay("Display 2d")
			.setEquivalence(ConceptMapEquivalence.EQUAL)
			.setComment("2d This is a comment.");

		group = conceptMap.addGroup();
		group
			.setSource(CS_URL_1)
			.setSourceVersion("Version 1s")
			.setTarget(CS_URL_3)
			.setTargetVersion("Version 3t");

		element = group.addElement();
		element
			.setCode("Code 1a")
			.setDisplay("Display 1a");

		target = element.addTarget();
		target
			.setCode("Code 3a")
			.setDisplay("Display 3a")
			.setEquivalence(ConceptMapEquivalence.EQUAL)
			.setComment("3a This is a comment.");

		element = group.addElement();
		element
			.setCode("Code 1b")
			.setDisplay("Display 1b");

		target = element.addTarget();
		target
			.setCode("Code 3b")
			.setDisplay("Display 3b")
			.setEquivalence(ConceptMapEquivalence.EQUAL)
			.setComment("3b This is a comment.");

		element = group.addElement();
		element
			.setCode("Code 1c")
			.setDisplay("Display 1c");

		target = element.addTarget();
		target
			.setCode("Code 3c")
			.setDisplay("Display 3c")
			.setEquivalence(ConceptMapEquivalence.EQUAL)
			.setComment("3c This is a comment.");

		element = group.addElement();
		element
			.setCode("Code 1d")
			.setDisplay("Display 1d");

		target = element.addTarget();
		target
			.setCode("Code 3d")
			.setDisplay("Display 3d")
			.setEquivalence(ConceptMapEquivalence.EQUAL)
			.setComment("3d This is a comment.");

		group = conceptMap.addGroup();
		group
			.setSource(CS_URL_2)
			.setSourceVersion("Version 2s")
			.setTarget(CS_URL_3)
			.setTargetVersion("Version 3t");

		element = group.addElement();
		element
			.setCode("Code 2a")
			.setDisplay("Display 2a");

		target = element.addTarget();
		target
			.setCode("Code 3a")
			.setDisplay("Display 3a")
			.setEquivalence(ConceptMapEquivalence.EQUAL)
			.setComment("3a This is a comment.");

		element = group.addElement();
		element
			.setCode("Code 2b")
			.setDisplay("Display 2b");

		target = element.addTarget();
		target
			.setCode("Code 3b")
			.setDisplay("Display 3b")
			.setEquivalence(ConceptMapEquivalence.EQUAL)
			.setComment("3b This is a comment.");

		element = group.addElement();
		element
			.setCode("Code 2c")
			.setDisplay("Display 2c");

		target = element.addTarget();
		target
			.setCode("Code 3c")
			.setDisplay("Display 3c")
			.setEquivalence(ConceptMapEquivalence.EQUAL)
			.setComment("3c This is a comment.");

		element = group.addElement();
		element
			.setCode("Code 2d")
			.setDisplay("Display 2d");

		target = element.addTarget();
		target
			.setCode("Code 3d")
			.setDisplay("Display 3d")
			.setEquivalence(ConceptMapEquivalence.EQUAL)
			.setComment("3d This is a comment.");

		return conceptMap;
	}
}
