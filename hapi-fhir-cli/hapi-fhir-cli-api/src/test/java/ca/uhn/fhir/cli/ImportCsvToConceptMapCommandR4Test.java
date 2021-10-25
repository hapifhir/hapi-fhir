package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.VerboseLoggingInterceptor;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.r4.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImportCsvToConceptMapCommandR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ImportCsvToConceptMapCommandR4Test.class);
	private static final String CM_URL = "http://example.com/conceptmap";
	private static final String VS_URL_1 = "http://example.com/valueset/1";
	private static final String VS_URL_2 = "http://example.com/valueset/2";
	private static final String CS_URL_1 = "http://example.com/codesystem/1";
	private static final String CS_URL_2 = "http://example.com/codesystem/2";
	private static final String CS_URL_3 = "http://example.com/codesystem/3";
	private static final String FILENAME = "import-csv-to-conceptmap-command-test-input.csv";

	private static String file;
	private static String ourBase;
	private static IGenericClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static int ourPort;
	private static Server ourServer;
	private static String ourVersion = "r4";

	private static RestfulServer restfulServer;

	private static HashMapResourceProviderConceptMapR4 hashMapResourceProviderConceptMapR4;

	static {
		System.setProperty("test", "true");
	}

	@AfterEach
	public void afterClearResourceProvider() {
		HashMapResourceProviderConceptMapR4 resourceProvider = (HashMapResourceProviderConceptMapR4) restfulServer.getResourceProviders().iterator().next();
		resourceProvider.clear();
	}

	@Test
	public void testConditionalUpdateResultsInCreate() {
		ConceptMap conceptMap = ExportConceptMapToCsvCommandR4Test.createConceptMap();
		String conceptMapUrl = conceptMap.getUrl();

		ourLog.info("Searching for existing ConceptMap with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
		MethodOutcome methodOutcome = ourClient
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
		ourClient.create().resource(conceptMap).execute();
		String conceptMapUrl = conceptMap.getUrl();

		ourLog.info("Searching for existing ConceptMap with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
		MethodOutcome methodOutcome = ourClient
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
		ourClient.create().resource(conceptMap).execute();

		Bundle response = ourClient
			.search()
			.forResource(ConceptMap.class)
			.where(ConceptMap.URL.matches().value(CM_URL))
			.returnBundle(Bundle.class)
			.execute();

		ConceptMap resultConceptMap = (ConceptMap) response.getEntryFirstRep().getResource();

		MethodOutcome methodOutcome = ourClient
			.update()
			.resource(resultConceptMap)
			.withId(resultConceptMap.getIdElement())
			.execute();

		assertNull(methodOutcome.getCreated());

		// Do not simplify to assertEquals(...)
		assertTrue(!Boolean.TRUE.equals(methodOutcome.getCreated()));
	}

	@Test
	public void testImportCsvToConceptMapCommand() throws FHIRException {
		ClassLoader classLoader = getClass().getClassLoader();
		File fileToImport = new File(classLoader.getResource(FILENAME).getFile());
		ImportCsvToConceptMapCommandR4Test.file = fileToImport.getAbsolutePath();

		App.main(new String[]{"import-csv-to-conceptmap",
			"-v", ourVersion,
			"-t", ourBase,
			"-u", CM_URL,
			"-i", VS_URL_1,
			"-o", VS_URL_2,
			"-f", file,
			"-l"});

		Bundle response = ourClient
			.search()
			.forResource(ConceptMap.class)
			.where(ConceptMap.URL.matches().value(CM_URL))
			.returnBundle(Bundle.class)
			.execute();

		ConceptMap conceptMap = (ConceptMap) response.getEntryFirstRep().getResource();

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		assertEquals("http://localhost:" + ourPort + "/ConceptMap/1/_history/1", conceptMap.getId());

		assertEquals(CM_URL, conceptMap.getUrl());
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

		App.main(new String[]{"import-csv-to-conceptmap",
			"-v", ourVersion,
			"-t", ourBase,
			"-u", CM_URL,
			"-i", VS_URL_1,
			"-o", VS_URL_2,
			"-f", file,
			"-l"});

		response = ourClient
			.search()
			.forResource(ConceptMap.class)
			.where(ConceptMap.URL.matches().value(CM_URL))
			.returnBundle(Bundle.class)
			.execute();

		conceptMap = (ConceptMap) response.getEntryFirstRep().getResource();

		assertEquals("http://localhost:" + ourPort + "/ConceptMap/1/_history/2", conceptMap.getId());
	}

	@Test
	public void testImportCsvToConceptMapCommandWithByteOrderMark() throws FHIRException {
		ClassLoader classLoader = getClass().getClassLoader();
		File fileToImport = new File(classLoader.getResource("loinc-to-phenx.csv").getFile());
		ImportCsvToConceptMapCommandR4Test.file = fileToImport.getAbsolutePath();

		App.main(new String[]{"import-csv-to-conceptmap",
			"-v", ourVersion,
			"-t", ourBase,
			"-u", "http://loinc.org/cm/loinc-to-phenx",
			"-i", "http://loinc.org",
			"-o", "http://phenxtoolkit.org",
			"-f", file,
			"-l"});

		Bundle response = ourClient
			.search()
			.forResource(ConceptMap.class)
			.where(ConceptMap.URL.matches().value("http://loinc.org/cm/loinc-to-phenx"))
			.returnBundle(Bundle.class)
			.execute();

		ConceptMap conceptMap = (ConceptMap) response.getEntryFirstRep().getResource();

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		assertEquals("http://localhost:" + ourPort + "/ConceptMap/1/_history/1", conceptMap.getId());

		assertEquals("http://loinc.org/cm/loinc-to-phenx", conceptMap.getUrl());
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

		App.main(new String[]{"import-csv-to-conceptmap",
			"-v", ourVersion,
			"-t", ourBase,
			"-u", "http://loinc.org/cm/loinc-to-phenx",
			"-i", "http://loinc.org",
			"-o", "http://phenxtoolkit.org",
			"-f", file,
			"-l"});

		response = ourClient
			.search()
			.forResource(ConceptMap.class)
			.where(ConceptMap.URL.matches().value("http://loinc.org/cm/loinc-to-phenx"))
			.returnBundle(Bundle.class)
			.execute();

		conceptMap = (ConceptMap) response.getEntryFirstRep().getResource();

		assertEquals("http://localhost:" + ourPort + "/ConceptMap/1/_history/2", conceptMap.getId());
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		ServletHandler servletHandler = new ServletHandler();

		restfulServer = new RestfulServer(ourCtx);
		restfulServer.registerInterceptor(new VerboseLoggingInterceptor());
		restfulServer.setResourceProviders(new HashMapResourceProviderConceptMapR4(ourCtx));

		ServletHolder servletHolder = new ServletHolder(restfulServer);
		servletHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(servletHandler);

		JettyUtil.startServer(ourServer);
		ourPort = JettyUtil.getPortForStartedServer(ourServer);

		ourBase = "http://localhost:" + ourPort;

		ourClient = ourCtx.newRestfulGenericClient(ourBase);
	}
}
