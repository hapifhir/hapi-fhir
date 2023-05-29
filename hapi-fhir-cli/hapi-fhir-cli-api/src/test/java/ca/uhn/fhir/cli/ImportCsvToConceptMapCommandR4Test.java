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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
		assertTrue(Boolean.TRUE.equals(methodOutcome.getCreated()));
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
		assertTrue(!Boolean.TRUE.equals(methodOutcome.getCreated()));
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

		assertNull(methodOutcome.getCreated());

		// Do not simplify to assertEquals(...)
		assertTrue(!Boolean.TRUE.equals(methodOutcome.getCreated()));
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

		assertEquals(myRestServerR4Helper.getBase() + "/ConceptMap/1/_history/1", conceptMap.getId());

		assertEquals(CM_URL, conceptMap.getUrl());
		assertEquals(STATUS, conceptMap.getStatus().getDisplay());
		assertEquals(VS_URL_1, conceptMap.getSourceUriType().getValueAsString());
		assertEquals(VS_URL_2, conceptMap.getTargetUriType().getValueAsString());

		assertEquals(3, conceptMap.getGroup().size());

		ConceptMapGroupComponent group = conceptMap.getGroup().get(0);
		assertEquals(CS_URL_1, group.getSource());
		assertEquals("Version 1s", group.getSourceVersion());
		assertEquals(CS_URL_2, group.getTarget());
		assertEquals("Version 2t", group.getTargetVersion());

		assertEquals(4, group.getElement().size());

		SourceElementComponent source = group.getElement().get(0);
		assertEquals("Code 1a", source.getCode());
		assertEquals("Display 1a", source.getDisplay());

		assertEquals(1, source.getTarget().size());

		TargetElementComponent target = source.getTarget().get(0);
		assertEquals("Code 2a", target.getCode());
		assertEquals("Display 2a", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("2a This is a comment.", target.getComment());

		source = group.getElement().get(1);
		assertEquals("Code 1b", source.getCode());
		assertEquals("Display 1b", source.getDisplay());

		assertEquals(1, source.getTarget().size());

		target = source.getTarget().get(0);
		assertEquals("Code 2b", target.getCode());
		assertEquals("Display 2b", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("2b This is a comment.", target.getComment());

		source = group.getElement().get(2);
		assertEquals("Code 1c", source.getCode());
		assertEquals("Display 1c", source.getDisplay());

		assertEquals(1, source.getTarget().size());

		target = source.getTarget().get(0);
		assertEquals("Code 2c", target.getCode());
		assertEquals("Display 2c", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("2c This is a comment.", target.getComment());

		source = group.getElement().get(3);
		assertEquals("Code 1d", source.getCode());
		assertEquals("Display 1d", source.getDisplay());

		assertEquals(1, source.getTarget().size());

		target = source.getTarget().get(0);
		assertEquals("Code 2d", target.getCode());
		assertEquals("Display 2d", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("2d This is a comment.", target.getComment());

		group = conceptMap.getGroup().get(1);
		assertEquals(CS_URL_1, group.getSource());
		assertEquals("Version 1s", group.getSourceVersion());
		assertEquals(CS_URL_3, group.getTarget());
		assertEquals("Version 3t", group.getTargetVersion());

		assertEquals(4, group.getElement().size());

		source = group.getElement().get(0);
		assertEquals("Code 1a", source.getCode());
		assertEquals("Display 1a", source.getDisplay());

		assertEquals(1, source.getTarget().size());

		target = source.getTarget().get(0);
		assertEquals("Code 3a", target.getCode());
		assertEquals("Display 3a", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("3a This is a comment.", target.getComment());

		source = group.getElement().get(1);
		assertEquals("Code 1b", source.getCode());
		assertEquals("Display 1b", source.getDisplay());

		assertEquals(1, source.getTarget().size());

		target = source.getTarget().get(0);
		assertEquals("Code 3b", target.getCode());
		assertEquals("Display 3b", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("3b This is a comment.", target.getComment());

		source = group.getElement().get(2);
		assertEquals("Code 1c", source.getCode());
		assertEquals("Display 1c", source.getDisplay());

		assertEquals(1, source.getTarget().size());

		target = source.getTarget().get(0);
		assertEquals("Code 3c", target.getCode());
		assertEquals("Display 3c", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("3c This is a comment.", target.getComment());

		source = group.getElement().get(3);
		assertEquals("Code 1d", source.getCode());
		assertEquals("Display 1d", source.getDisplay());

		assertEquals(1, source.getTarget().size());

		target = source.getTarget().get(0);
		assertEquals("Code 3d", target.getCode());
		assertEquals("Display 3d", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("3d This is a comment.", target.getComment());

		group = conceptMap.getGroup().get(2);
		assertEquals(CS_URL_2, group.getSource());
		assertEquals("Version 2s", group.getSourceVersion());
		assertEquals(CS_URL_3, group.getTarget());
		assertEquals("Version 3t", group.getTargetVersion());

		assertEquals(4, group.getElement().size());

		source = group.getElement().get(0);
		assertEquals("Code 2a", source.getCode());
		assertEquals("Display 2a", source.getDisplay());

		assertEquals(1, source.getTarget().size());

		target = source.getTarget().get(0);
		assertEquals("Code 3a", target.getCode());
		assertEquals("Display 3a", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("3a This is a comment.", target.getComment());

		source = group.getElement().get(1);
		assertEquals("Code 2b", source.getCode());
		assertEquals("Display 2b", source.getDisplay());

		assertEquals(1, source.getTarget().size());

		target = source.getTarget().get(0);
		assertEquals("Code 3b", target.getCode());
		assertEquals("Display 3b", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("3b This is a comment.", target.getComment());

		source = group.getElement().get(2);
		assertEquals("Code 2c", source.getCode());
		assertEquals("Display 2c", source.getDisplay());

		assertEquals(1, source.getTarget().size());

		target = source.getTarget().get(0);
		assertEquals("Code 3c", target.getCode());
		assertEquals("Display 3c", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("3c This is a comment.", target.getComment());

		source = group.getElement().get(3);
		assertEquals("Code 2d", source.getCode());
		assertEquals("Display 2d", source.getDisplay());

		assertEquals(1, source.getTarget().size());

		target = source.getTarget().get(0);
		assertEquals("Code 3d", target.getCode());
		assertEquals("Display 3d", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("3d This is a comment.", target.getComment());

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

		assertEquals(myRestServerR4Helper.getBase() + "/ConceptMap/1/_history/2", conceptMap.getId());
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

		assertEquals(myRestServerR4Helper.getBase() + "/ConceptMap/1/_history/1", conceptMap.getId());

		assertEquals("http://loinc.org/cm/loinc-to-phenx", conceptMap.getUrl());
		assertEquals(STATUS, conceptMap.getStatus().getDisplay());
		assertEquals("http://loinc.org", conceptMap.getSourceUriType().getValueAsString());
		assertEquals("http://phenxtoolkit.org", conceptMap.getTargetUriType().getValueAsString());

		assertEquals(1, conceptMap.getGroup().size());

		ConceptMapGroupComponent group = conceptMap.getGroup().get(0);
		assertEquals("http://loinc.org", group.getSource());
		assertNull(group.getSourceVersion());
		assertEquals("http://phenxtoolkit.org", group.getTarget());
		assertNull(group.getTargetVersion());

		assertEquals(1, group.getElement().size());

		SourceElementComponent source = group.getElement().get(0);
		assertEquals("65191-9", source.getCode());
		assertEquals("During the past 30 days, about how often did you feel restless or fidgety [Kessler 6 Distress]", source.getDisplay());

		assertEquals(1, source.getTarget().size());

		TargetElementComponent target = source.getTarget().get(0);
		assertEquals("PX121301010300", target.getCode());
		assertEquals("PX121301_Restless", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUIVALENT, target.getEquivalence());
		assertNull(target.getComment());

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

		assertEquals(myRestServerR4Helper.getBase() + "/ConceptMap/1/_history/2", conceptMap.getId());
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
			fail();
		} catch (Error e) {
			assertTrue(e.getMessage().contains("Missing required option: s"));
		}
	}
}
