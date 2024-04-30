package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.test.utilities.RestServerR4Helper;
import ca.uhn.fhir.test.utilities.TlsAuthenticationTestHelper;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.r4.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class ImportCsvToConceptMapCommandR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ImportCsvToConceptMapCommandR4Test.class);
	private static final String CM_URL = "http://example.com/conceptmap";
	private static final String VS_URL_1 = "http://example.com/valueset/1";
	private static final String VS_URL_2 = "http://example.com/valueset/2";
	private static final String CS_URL_1 = "http://example.com/codesystem/1";
	private static final String CS_URL_2 = "http://example.com/codesystem/2";
	private static final String CS_URL_3 = "http://example.com/codesystem/3";
	private static final String FILENAME = "import-csv-to-conceptmap-command-test-input.csv";
	private static final String STATUS = "Active";

	static {
		HapiSystemProperties.enableTestMode();
	}

	private final FhirContext myFhirContext = FhirContext.forR4();
	private final String myVersion = "r4";
	private String myFilePath;
	private final String myStatus = Enumerations.PublicationStatus.ACTIVE.toCode();


	@RegisterExtension
	public final RestServerR4Helper myRestServerR4Helper = RestServerR4Helper.newInitialized();
	@RegisterExtension
	public TlsAuthenticationTestHelper myTlsAuthenticationTestHelper = new TlsAuthenticationTestHelper();

	@BeforeEach
	public void before(){
		myRestServerR4Helper.setConceptMapResourceProvider(new HashMapResourceProviderConceptMapR4(myFhirContext));
	}

	@AfterAll
	public static void afterAll(){
		TestUtil.randomizeLocaleAndTimezone();
	}

	@Test
	public void testConditionalUpdateResultsInCreate() {
		ConceptMap conceptMap = ExportConceptMapToCsvCommandR4Test.createConceptMap();
		String conceptMapUrl = conceptMap.getUrl();

		ourLog.info("Searching for existing ConceptMap with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
		MethodOutcome methodOutcome = myRestServerR4Helper.getClient()
			.update()
			.resource(conceptMap)
			.conditional()
			.where(ConceptMap.URL.matches().value(conceptMapUrl))
			.execute();

		// Do not simplify to assertEquals(...)
		assertThat(Boolean.TRUE.equals(methodOutcome.getCreated())).isTrue();
	}

	@Test
	public void testConditionalUpdateResultsInUpdate() {
		ConceptMap conceptMap = ExportConceptMapToCsvCommandR4Test.createConceptMap();
		myRestServerR4Helper.getClient().create().resource(conceptMap).execute();
		String conceptMapUrl = conceptMap.getUrl();

		ourLog.info("Searching for existing ConceptMap with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
		MethodOutcome methodOutcome = myRestServerR4Helper.getClient()
			.update()
			.resource(conceptMap)
			.conditional()
			.where(ConceptMap.URL.matches().value(conceptMapUrl))
			.execute();

		// Do not simplify to assertEquals(...)
		assertThat(!Boolean.TRUE.equals(methodOutcome.getCreated())).isTrue();
	}

	@Test
	public void testNonConditionalUpdate() {
		ConceptMap conceptMap = ExportConceptMapToCsvCommandR4Test.createConceptMap();
		myRestServerR4Helper.getClient().create().resource(conceptMap).execute();

		Bundle response = myRestServerR4Helper.getClient()
			.search()
			.forResource(ConceptMap.class)
			.where(ConceptMap.URL.matches().value(CM_URL))
			.returnBundle(Bundle.class)
			.execute();

		ConceptMap resultConceptMap = (ConceptMap) response.getEntryFirstRep().getResource();

		MethodOutcome methodOutcome = myRestServerR4Helper.getClient()
			.update()
			.resource(resultConceptMap)
			.withId(resultConceptMap.getIdElement())
			.execute();

		assertThat(methodOutcome.getCreated()).isNull();

		// Do not simplify to assertEquals(...)
		assertThat(!Boolean.TRUE.equals(methodOutcome.getCreated())).isTrue();
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testImportCsvToConceptMapCommand(boolean theIncludeTls) throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		File fileToImport = new File(classLoader.getResource(FILENAME).getFile());
		myFilePath = fileToImport.getAbsolutePath();

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				ImportCsvToConceptMapCommand.COMMAND,
				"-v", myVersion,
				"-u", CM_URL,
				"-i", VS_URL_1,
				"-o", VS_URL_2,
				"-f", myFilePath,
				"-s", myStatus,
				"-l"
			},
			"-t", theIncludeTls, myRestServerR4Helper
		));

		Bundle response = myRestServerR4Helper.getClient()
			.search()
			.forResource(ConceptMap.class)
			.where(ConceptMap.URL.matches().value(CM_URL))
			.returnBundle(Bundle.class)
			.execute();

		ConceptMap conceptMap = (ConceptMap) response.getEntryFirstRep().getResource();

		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		assertThat(conceptMap.getId()).isEqualTo(myRestServerR4Helper.getBase() + "/ConceptMap/1/_history/1");

		assertThat(conceptMap.getUrl()).isEqualTo(CM_URL);
		assertThat(conceptMap.getStatus().getDisplay()).isEqualTo(STATUS);
		assertThat(conceptMap.getSourceUriType().getValueAsString()).isEqualTo(VS_URL_1);
		assertThat(conceptMap.getTargetUriType().getValueAsString()).isEqualTo(VS_URL_2);

		assertThat(conceptMap.getGroup()).hasSize(3);

		ConceptMapGroupComponent group = conceptMap.getGroup().get(0);
		assertThat(group.getSource()).isEqualTo(CS_URL_1);
		assertThat(group.getSourceVersion()).isEqualTo("Version 1s");
		assertThat(group.getTarget()).isEqualTo(CS_URL_2);
		assertThat(group.getTargetVersion()).isEqualTo("Version 2t");

		assertThat(group.getElement()).hasSize(4);

		SourceElementComponent source = group.getElement().get(0);
		assertThat(source.getCode()).isEqualTo("Code 1a");
		assertThat(source.getDisplay()).isEqualTo("Display 1a");

		assertThat(source.getTarget()).hasSize(1);

		TargetElementComponent target = source.getTarget().get(0);
		assertThat(target.getCode()).isEqualTo("Code 2a");
		assertThat(target.getDisplay()).isEqualTo("Display 2a");
		assertThat(target.getEquivalence()).isEqualTo(ConceptMapEquivalence.EQUAL);
		assertThat(target.getComment()).isEqualTo("2a This is a comment.");

		source = group.getElement().get(1);
		assertThat(source.getCode()).isEqualTo("Code 1b");
		assertThat(source.getDisplay()).isEqualTo("Display 1b");

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertThat(target.getCode()).isEqualTo("Code 2b");
		assertThat(target.getDisplay()).isEqualTo("Display 2b");
		assertThat(target.getEquivalence()).isEqualTo(ConceptMapEquivalence.EQUAL);
		assertThat(target.getComment()).isEqualTo("2b This is a comment.");

		source = group.getElement().get(2);
		assertThat(source.getCode()).isEqualTo("Code 1c");
		assertThat(source.getDisplay()).isEqualTo("Display 1c");

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertThat(target.getCode()).isEqualTo("Code 2c");
		assertThat(target.getDisplay()).isEqualTo("Display 2c");
		assertThat(target.getEquivalence()).isEqualTo(ConceptMapEquivalence.EQUAL);
		assertThat(target.getComment()).isEqualTo("2c This is a comment.");

		source = group.getElement().get(3);
		assertThat(source.getCode()).isEqualTo("Code 1d");
		assertThat(source.getDisplay()).isEqualTo("Display 1d");

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertThat(target.getCode()).isEqualTo("Code 2d");
		assertThat(target.getDisplay()).isEqualTo("Display 2d");
		assertThat(target.getEquivalence()).isEqualTo(ConceptMapEquivalence.EQUAL);
		assertThat(target.getComment()).isEqualTo("2d This is a comment.");

		group = conceptMap.getGroup().get(1);
		assertThat(group.getSource()).isEqualTo(CS_URL_1);
		assertThat(group.getSourceVersion()).isEqualTo("Version 1s");
		assertThat(group.getTarget()).isEqualTo(CS_URL_3);
		assertThat(group.getTargetVersion()).isEqualTo("Version 3t");

		assertThat(group.getElement()).hasSize(4);

		source = group.getElement().get(0);
		assertThat(source.getCode()).isEqualTo("Code 1a");
		assertThat(source.getDisplay()).isEqualTo("Display 1a");

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertThat(target.getCode()).isEqualTo("Code 3a");
		assertThat(target.getDisplay()).isEqualTo("Display 3a");
		assertThat(target.getEquivalence()).isEqualTo(ConceptMapEquivalence.EQUAL);
		assertThat(target.getComment()).isEqualTo("3a This is a comment.");

		source = group.getElement().get(1);
		assertThat(source.getCode()).isEqualTo("Code 1b");
		assertThat(source.getDisplay()).isEqualTo("Display 1b");

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertThat(target.getCode()).isEqualTo("Code 3b");
		assertThat(target.getDisplay()).isEqualTo("Display 3b");
		assertThat(target.getEquivalence()).isEqualTo(ConceptMapEquivalence.EQUAL);
		assertThat(target.getComment()).isEqualTo("3b This is a comment.");

		source = group.getElement().get(2);
		assertThat(source.getCode()).isEqualTo("Code 1c");
		assertThat(source.getDisplay()).isEqualTo("Display 1c");

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertThat(target.getCode()).isEqualTo("Code 3c");
		assertThat(target.getDisplay()).isEqualTo("Display 3c");
		assertThat(target.getEquivalence()).isEqualTo(ConceptMapEquivalence.EQUAL);
		assertThat(target.getComment()).isEqualTo("3c This is a comment.");

		source = group.getElement().get(3);
		assertThat(source.getCode()).isEqualTo("Code 1d");
		assertThat(source.getDisplay()).isEqualTo("Display 1d");

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertThat(target.getCode()).isEqualTo("Code 3d");
		assertThat(target.getDisplay()).isEqualTo("Display 3d");
		assertThat(target.getEquivalence()).isEqualTo(ConceptMapEquivalence.EQUAL);
		assertThat(target.getComment()).isEqualTo("3d This is a comment.");

		group = conceptMap.getGroup().get(2);
		assertThat(group.getSource()).isEqualTo(CS_URL_2);
		assertThat(group.getSourceVersion()).isEqualTo("Version 2s");
		assertThat(group.getTarget()).isEqualTo(CS_URL_3);
		assertThat(group.getTargetVersion()).isEqualTo("Version 3t");

<<<<<<< HEAD
		assertThat(group.getElement()).hasSize(4);
=======
		assertEquals(5, group.getElement().size());
>>>>>>> master

		source = group.getElement().get(0);
		assertThat(source.getCode()).isEqualTo("Code 2a");
		assertThat(source.getDisplay()).isEqualTo("Display 2a");

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertThat(target.getCode()).isEqualTo("Code 3a");
		assertThat(target.getDisplay()).isEqualTo("Display 3a");
		assertThat(target.getEquivalence()).isEqualTo(ConceptMapEquivalence.EQUAL);
		assertThat(target.getComment()).isEqualTo("3a This is a comment.");

		source = group.getElement().get(1);
		assertThat(source.getCode()).isEqualTo("Code 2b");
		assertThat(source.getDisplay()).isEqualTo("Display 2b");

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertThat(target.getCode()).isEqualTo("Code 3b");
		assertThat(target.getDisplay()).isEqualTo("Display 3b");
		assertThat(target.getEquivalence()).isEqualTo(ConceptMapEquivalence.EQUAL);
		assertThat(target.getComment()).isEqualTo("3b This is a comment.");

		source = group.getElement().get(2);
		assertThat(source.getCode()).isEqualTo("Code 2c");
		assertThat(source.getDisplay()).isEqualTo("Display 2c");

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertThat(target.getCode()).isEqualTo("Code 3c");
		assertThat(target.getDisplay()).isEqualTo("Display 3c");
		assertThat(target.getEquivalence()).isEqualTo(ConceptMapEquivalence.EQUAL);
		assertThat(target.getComment()).isEqualTo("3c This is a comment.");

		source = group.getElement().get(3);
		assertThat(source.getCode()).isEqualTo("Code 2d");
		assertThat(source.getDisplay()).isEqualTo("Display 2d");

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertThat(target.getCode()).isEqualTo("Code 3d");
		assertThat(target.getDisplay()).isEqualTo("Display 3d");
		assertThat(target.getEquivalence()).isEqualTo(ConceptMapEquivalence.EQUAL);
		assertThat(target.getComment()).isEqualTo("3d This is a comment.");

		// ensure unmatched codes are handled correctly
		source = group.getElement().get(4);
		assertEquals("Code 2e", source.getCode());
		assertEquals("Display 2e", source.getDisplay());

		assertEquals(1, source.getTarget().size());

		target = source.getTarget().get(0);
		assertNull(target.getCode());
		assertNull(target.getDisplay());
		assertEquals(ConceptMapEquivalence.UNMATCHED, target.getEquivalence());
		assertEquals("3e This is a comment.", target.getComment());

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				ImportCsvToConceptMapCommand.COMMAND,
				"-v", myVersion,
				"-u", CM_URL,
				"-i", VS_URL_1,
				"-o", VS_URL_2,
				"-f", myFilePath,
				"-s", myStatus,
				"-l"
			},
			"-t", theIncludeTls, myRestServerR4Helper
		));

		response = myRestServerR4Helper.getClient()
			.search()
			.forResource(ConceptMap.class)
			.where(ConceptMap.URL.matches().value(CM_URL))
			.returnBundle(Bundle.class)
			.execute();

		conceptMap = (ConceptMap) response.getEntryFirstRep().getResource();

		assertThat(conceptMap.getId()).isEqualTo(myRestServerR4Helper.getBase() + "/ConceptMap/1/_history/2");
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testImportCsvToConceptMapCommandWithByteOrderMark(boolean theIncludeTls) throws FHIRException {
		ClassLoader classLoader = getClass().getClassLoader();
		File fileToImport = new File(classLoader.getResource("loinc-to-phenx.csv").getFile());
		myFilePath = fileToImport.getAbsolutePath();

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				ImportCsvToConceptMapCommand.COMMAND,
				"-v", myVersion,
				"-u", "http://loinc.org/cm/loinc-to-phenx",
				"-i", "http://loinc.org",
				"-o", "http://phenxtoolkit.org",
				"-f", myFilePath,
				"-s", myStatus,
				"-l"
			},
			"-t", theIncludeTls, myRestServerR4Helper
		));

		Bundle response = myRestServerR4Helper.getClient()
			.search()
			.forResource(ConceptMap.class)
			.where(ConceptMap.URL.matches().value("http://loinc.org/cm/loinc-to-phenx"))
			.returnBundle(Bundle.class)
			.execute();

		ConceptMap conceptMap = (ConceptMap) response.getEntryFirstRep().getResource();

		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		assertThat(conceptMap.getId()).isEqualTo(myRestServerR4Helper.getBase() + "/ConceptMap/1/_history/1");

		assertThat(conceptMap.getUrl()).isEqualTo("http://loinc.org/cm/loinc-to-phenx");
		assertThat(conceptMap.getStatus().getDisplay()).isEqualTo(STATUS);
		assertThat(conceptMap.getSourceUriType().getValueAsString()).isEqualTo("http://loinc.org");
		assertThat(conceptMap.getTargetUriType().getValueAsString()).isEqualTo("http://phenxtoolkit.org");

		assertThat(conceptMap.getGroup()).hasSize(1);

		ConceptMapGroupComponent group = conceptMap.getGroup().get(0);
		assertThat(group.getSource()).isEqualTo("http://loinc.org");
		assertThat(group.getSourceVersion()).isNull();
		assertThat(group.getTarget()).isEqualTo("http://phenxtoolkit.org");
		assertThat(group.getTargetVersion()).isNull();

		assertThat(group.getElement()).hasSize(1);

		SourceElementComponent source = group.getElement().get(0);
		assertThat(source.getCode()).isEqualTo("65191-9");
		assertThat(source.getDisplay()).isEqualTo("During the past 30 days, about how often did you feel restless or fidgety [Kessler 6 Distress]");

		assertThat(source.getTarget()).hasSize(1);

		TargetElementComponent target = source.getTarget().get(0);
		assertThat(target.getCode()).isEqualTo("PX121301010300");
		assertThat(target.getDisplay()).isEqualTo("PX121301_Restless");
		assertThat(target.getEquivalence()).isEqualTo(ConceptMapEquivalence.EQUIVALENT);
		assertThat(target.getComment()).isNull();

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				ImportCsvToConceptMapCommand.COMMAND,
				"-v", myVersion,
				"-u", "http://loinc.org/cm/loinc-to-phenx",
				"-i", "http://loinc.org",
				"-o", "http://phenxtoolkit.org",
				"-f", myFilePath,
				"-s", myStatus,
				"-l"
			},
			"-t", theIncludeTls, myRestServerR4Helper
		));

		response = myRestServerR4Helper.getClient()
			.search()
			.forResource(ConceptMap.class)
			.where(ConceptMap.URL.matches().value("http://loinc.org/cm/loinc-to-phenx"))
			.returnBundle(Bundle.class)
			.execute();

		conceptMap = (ConceptMap) response.getEntryFirstRep().getResource();

		assertThat(conceptMap.getId()).isEqualTo(myRestServerR4Helper.getBase() + "/ConceptMap/1/_history/2");
	}

	@Test
	public void testImportCsvToConceptMapCommand_withNoStatus_Fails() throws FHIRException {
		ClassLoader classLoader = getClass().getClassLoader();
		File fileToImport = new File(classLoader.getResource("loinc-to-phenx.csv").getFile());
		myFilePath = fileToImport.getAbsolutePath();

		try {
			App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
				new String[]{
					ImportCsvToConceptMapCommand.COMMAND,
					"-v", myVersion,
					"-u", "http://loinc.org/cm/loinc-to-phenx",
					"-i", "http://loinc.org",
					"-o", "http://phenxtoolkit.org",
					"-f", myFilePath,
					"-l"
				},
				"-t", true, myRestServerR4Helper
			));
			fail("");		} catch (Error e) {
			assertThat(e.getMessage()).contains("Missing required option: s");
		}
	}
}
