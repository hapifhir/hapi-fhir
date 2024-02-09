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

		assertThat(methodOutcome.getCreated()).isEqualTo(Boolean.TRUE);
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

		assertThat(methodOutcome.getCreated()).isNull();
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

		assertThat(methodOutcome.getCreated()).isNull();
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

		assertThat(conceptMap.getId()).isEqualTo(myRestServerDstu3Helper.getBase() + "/ConceptMap/1/_history/1");

		assertThat(conceptMap.getUrl()).isEqualTo(CM_URL);
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

		assertThat(group.getElement()).hasSize(4);

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

		assertThat(conceptMap.getId()).isEqualTo(myRestServerDstu3Helper.getBase() + "/ConceptMap/1/_history/2");
	}

}
