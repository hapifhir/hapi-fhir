package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.VerboseLoggingInterceptor;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.r4.model.UriType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExportConceptMapToCsvCommandTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExportConceptMapToCsvCommandTest.class);
	private static final String CM_URL = "http://example.com/conceptmap";
	private static final String VS_URL_1 = "http://example.com/valueset/1";
	private static final String VS_URL_2 = "http://example.com/valueset/2";
	private static final String CS_URL_1 = "http://example.com/codesystem/1";
	private static final String CS_URL_2 = "http://example.com/codesystem/2";
	private static final String CS_URL_3 = "http://example.com/codesystem/3";
	private static final String FILENAME = "output.csv";
	private static final String PATH = "./target/";

	private static String ourBase;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static int ourPort;
	private static Server ourServer;

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		ServletHandler servletHandler = new ServletHandler();

		RestfulServer restfulServer = new RestfulServer(ourCtx);
		restfulServer.registerInterceptor(new VerboseLoggingInterceptor());
		restfulServer.setResourceProviders(new HashMapResourceProviderConceptMapR4(ourCtx));

		ServletHolder servletHolder = new ServletHolder(restfulServer);
		servletHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(servletHandler);

		ourServer.start();

		ourBase = "http://localhost:" + ourPort;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourBase);

		client.create().resource(createConceptMap()).execute();
	}

	@Test
	public void testServer() throws IOException {
		App.main(new String[] {"export-conceptmap-to-csv",
			"-v", "r4",
			"-t", ourBase,
			"-u", CM_URL,
			"-f", FILENAME,
			"-p", PATH});

		String result = IOUtils.toString(new FileInputStream(PATH.concat(FILENAME)), Charsets.UTF_8);
		Assert.assertEquals("", result);

		FileUtils.deleteQuietly(new File(PATH.concat(FILENAME)));
	}

	private static ConceptMap createConceptMap() {
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
