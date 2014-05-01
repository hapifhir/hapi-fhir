package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import org.hamcrest.core.StringContains;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.param.SearchParameter;
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
		
		boolean found=false;
		Collection<ResourceBinding> resourceBindings = rs.getResourceBindings();
		for (ResourceBinding resourceBinding : resourceBindings) {
			if (resourceBinding.getResourceName().equals("Patient")) {
				List<BaseMethodBinding> methodBindings = resourceBinding.getMethodBindings();
				SearchMethodBinding binding = (SearchMethodBinding) methodBindings.get(0);
				SearchParameter param = (SearchParameter) binding.getParameters().iterator().next();
				assertEquals("The patient's identifier (MRN or other card number)", param.getDescription());
				found=true;
			}
		}
		assertTrue(found);
		Conformance conformance = sc.getServerConformance();
		String conf = new FhirContext().newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		assertThat(conf, containsString("<documentation value=\"The patient's identifier (MRN or other card number)\"/>"));
		assertThat(conf, containsString("<type value=\"token\"/>"));
	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class SearchProvider {

		@Search(type = Patient.class)
		public Patient findPatient(
				@Description(shortDefinition = "The patient's identifier (MRN or other card number)") 
				@RequiredParam(name = Patient.SP_IDENTIFIER) IdentifierDt theIdentifier) {
			return null;
		}

	}

}
