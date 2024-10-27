package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.test.utilities.RestServerDstu3Helper;
import ca.uhn.fhir.test.utilities.TlsAuthenticationTestHelper;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Enumerations;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ImportCsvToConceptMapCommandDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ImportCsvToConceptMapCommandDstu3Test.class);
	private static final String CM_URL = "http://example.com/conceptmap";
	private static final String VS_URL_1 = "http://example.com/valueset/1";
	private static final String VS_URL_2 = "http://example.com/valueset/2";
	private static final String CS_URL_1 = "http://example.com/codesystem/1";
	private static final String CS_URL_2 = "http://example.com/codesystem/2";
	private static final String CS_URL_3 = "http://example.com/codesystem/3";
	private static final String FILENAME = "import-csv-to-conceptmap-command-test-input.csv";

	private final FhirContext myCtx = FhirContext.forDstu3();
	private final String myVersion = "dstu3";
	private String myFile;

	static {
		HapiSystemProperties.enableTestMode();
	}

	@RegisterExtension
	public final RestServerDstu3Helper myRestServerDstu3Helper = RestServerDstu3Helper.newInitialized();
	@RegisterExtension
	public TlsAuthenticationTestHelper myTlsAuthenticationTestHelper = new TlsAuthenticationTestHelper();

	@BeforeEach
	public void before(){
		myRestServerDstu3Helper.setConceptMapResourceProvider(new HashMapResourceProviderConceptMapDstu3(myCtx));
	}

	@AfterAll
	public static void afterAll(){
		TestUtil.randomizeLocaleAndTimezone();
	}

	@Test
	public void testConditionalUpdateResultsInCreate() {
		ConceptMap conceptMap = ExportConceptMapToCsvCommandDstu3Test.createConceptMap();
		String conceptMapUrl = conceptMap.getUrl();

		ourLog.info("Searching for existing ConceptMap with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
		MethodOutcome methodOutcome = myRestServerDstu3Helper.getClient()
			.update()
			.resource(conceptMap)
			.conditional()
			.where(ConceptMap.URL.matches().value(conceptMapUrl))
			.execute();

		assertEquals(Boolean.TRUE, methodOutcome.getCreated());
	}

	@Test
	public void testConditionalUpdateResultsInUpdate() {
		ConceptMap conceptMap = ExportConceptMapToCsvCommandDstu3Test.createConceptMap();
		myRestServerDstu3Helper.getClient().create().resource(conceptMap).execute();
		String conceptMapUrl = conceptMap.getUrl();

		ourLog.info("Searching for existing ConceptMap with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
		MethodOutcome methodOutcome = myRestServerDstu3Helper.getClient()
			.update()
			.resource(conceptMap)
			.conditional()
			.where(ConceptMap.URL.matches().value(conceptMapUrl))
			.execute();

		assertNull(methodOutcome.getCreated());
	}

	@Test
	public void testNonConditionalUpdate() {
		ConceptMap conceptMap = ExportConceptMapToCsvCommandDstu3Test.createConceptMap();
		myRestServerDstu3Helper.getClient().create().resource(conceptMap).execute();

		Bundle response = myRestServerDstu3Helper.getClient()
			.search()
			.forResource(ConceptMap.class)
			.where(ConceptMap.URL.matches().value(CM_URL))
			.returnBundle(Bundle.class)
			.execute();

		ConceptMap resultConceptMap = (ConceptMap) response.getEntryFirstRep().getResource();

		MethodOutcome methodOutcome = myRestServerDstu3Helper.getClient()
			.update()
			.resource(resultConceptMap)
			.withId(resultConceptMap.getIdElement())
			.execute();

		assertNull(methodOutcome.getCreated());
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testImportCsvToConceptMapCommandNoTls(boolean theIncludeTls) throws FHIRException {
		ClassLoader classLoader = getClass().getClassLoader();
		File fileToImport = new File(classLoader.getResource(FILENAME).getFile());
		myFile = fileToImport.getAbsolutePath();

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				ImportCsvToConceptMapCommand.COMMAND,
				"-v", myVersion,
				"-u", CM_URL,
				"-i", VS_URL_1,
				"-o", VS_URL_2,
				"-f", myFile,
				"-s", Enumerations.PublicationStatus.ACTIVE.toCode(),
				"-l"
			},
			"-t", theIncludeTls, myRestServerDstu3Helper
		));

		Bundle response = myRestServerDstu3Helper.getClient()
			.search()
			.forResource(ConceptMap.class)
			.where(ConceptMap.URL.matches().value(CM_URL))
			.returnBundle(Bundle.class)
			.execute();

		ConceptMap conceptMap = (ConceptMap) response.getEntryFirstRep().getResource();

		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		assertEquals(myRestServerDstu3Helper.getBase() + "/ConceptMap/1/_history/1", conceptMap.getId());

		assertEquals(CM_URL, conceptMap.getUrl());
		assertEquals(VS_URL_1, conceptMap.getSourceUriType().getValueAsString());
		assertEquals(VS_URL_2, conceptMap.getTargetUriType().getValueAsString());

		assertThat(conceptMap.getGroup()).hasSize(3);

		ConceptMapGroupComponent group = conceptMap.getGroup().get(0);
		assertEquals(CS_URL_1, group.getSource());
		assertEquals("Version 1s", group.getSourceVersion());
		assertEquals(CS_URL_2, group.getTarget());
		assertEquals("Version 2t", group.getTargetVersion());

		assertThat(group.getElement()).hasSize(4);

		SourceElementComponent source = group.getElement().get(0);
		assertEquals("Code 1a", source.getCode());
		assertEquals("Display 1a", source.getDisplay());

		assertThat(source.getTarget()).hasSize(1);

		TargetElementComponent target = source.getTarget().get(0);
		assertEquals("Code 2a", target.getCode());
		assertEquals("Display 2a", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("2a This is a comment.", target.getComment());

		source = group.getElement().get(1);
		assertEquals("Code 1b", source.getCode());
		assertEquals("Display 1b", source.getDisplay());

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertEquals("Code 2b", target.getCode());
		assertEquals("Display 2b", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("2b This is a comment.", target.getComment());

		source = group.getElement().get(2);
		assertEquals("Code 1c", source.getCode());
		assertEquals("Display 1c", source.getDisplay());

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertEquals("Code 2c", target.getCode());
		assertEquals("Display 2c", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("2c This is a comment.", target.getComment());

		source = group.getElement().get(3);
		assertEquals("Code 1d", source.getCode());
		assertEquals("Display 1d", source.getDisplay());

		assertThat(source.getTarget()).hasSize(1);

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

		assertThat(group.getElement()).hasSize(4);

		source = group.getElement().get(0);
		assertEquals("Code 1a", source.getCode());
		assertEquals("Display 1a", source.getDisplay());

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertEquals("Code 3a", target.getCode());
		assertEquals("Display 3a", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("3a This is a comment.", target.getComment());

		source = group.getElement().get(1);
		assertEquals("Code 1b", source.getCode());
		assertEquals("Display 1b", source.getDisplay());

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertEquals("Code 3b", target.getCode());
		assertEquals("Display 3b", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("3b This is a comment.", target.getComment());

		source = group.getElement().get(2);
		assertEquals("Code 1c", source.getCode());
		assertEquals("Display 1c", source.getDisplay());

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertEquals("Code 3c", target.getCode());
		assertEquals("Display 3c", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("3c This is a comment.", target.getComment());

		source = group.getElement().get(3);
		assertEquals("Code 1d", source.getCode());
		assertEquals("Display 1d", source.getDisplay());

		assertThat(source.getTarget()).hasSize(1);

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

		assertEquals(5, group.getElement().size());

		source = group.getElement().get(0);
		assertEquals("Code 2a", source.getCode());
		assertEquals("Display 2a", source.getDisplay());

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertEquals("Code 3a", target.getCode());
		assertEquals("Display 3a", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("3a This is a comment.", target.getComment());

		source = group.getElement().get(1);
		assertEquals("Code 2b", source.getCode());
		assertEquals("Display 2b", source.getDisplay());

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertEquals("Code 3b", target.getCode());
		assertEquals("Display 3b", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("3b This is a comment.", target.getComment());

		source = group.getElement().get(2);
		assertEquals("Code 2c", source.getCode());
		assertEquals("Display 2c", source.getDisplay());

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertEquals("Code 3c", target.getCode());
		assertEquals("Display 3c", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("3c This is a comment.", target.getComment());

		source = group.getElement().get(3);
		assertEquals("Code 2d", source.getCode());
		assertEquals("Display 2d", source.getDisplay());

		assertThat(source.getTarget()).hasSize(1);

		target = source.getTarget().get(0);
		assertEquals("Code 3d", target.getCode());
		assertEquals("Display 3d", target.getDisplay());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals("3d This is a comment.", target.getComment());

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
				"-f", myFile,
				"-s", Enumerations.PublicationStatus.ACTIVE.toCode(),
				"-l"
			},
			"-t", theIncludeTls, myRestServerDstu3Helper
		));

		response = myRestServerDstu3Helper.getClient()
			.search()
			.forResource(ConceptMap.class)
			.where(ConceptMap.URL.matches().value(CM_URL))
			.returnBundle(Bundle.class)
			.execute();

		conceptMap = (ConceptMap) response.getEntryFirstRep().getResource();

		assertEquals(myRestServerDstu3Helper.getBase() + "/ConceptMap/1/_history/2", conceptMap.getId());
	}

}
