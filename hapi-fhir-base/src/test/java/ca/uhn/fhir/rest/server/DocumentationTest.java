package ca.uhn.fhir.rest.server;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.provider.ServerConformanceProvider;

public class DocumentationTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DocumentationTest.class);

	@Test
	public void testSearchParameterDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer();
		rs.setProviders(new SearchProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(null);

		Conformance conformance = sc.getServerConformance();
		String conf = new FhirContext().newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class SearchProvider {

		@Search(type = Patient.class)
		public Patient findPatient(@Description(shortDefinition = "The patient's identifier (MRN or other card number)") @RequiredParam(name = Patient.SP_IDENTIFIER) IdentifierDt theIdentifier) {
			return null;
		}

	}

}
