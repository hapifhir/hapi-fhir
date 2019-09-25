package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.JettyUtil;
import com.google.common.collect.Lists;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.After;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class ServerMethodSelectionR4Test {


	private FhirContext myCtx = FhirContext.forR4();
	private Server myServer;
	private IGenericClient myClient;

	@After
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);
	}

	/**
	 * Server method with no _include
	 * Client request with _include
	 * <p>
	 * See #1421
	 */
	@Test
	public void testRejectIncludeIfNotProvided() throws Exception {

		class MyProvider extends MyBaseProvider {
			@Search
			public List<IBaseResource> search(@OptionalParam(name = "name") StringType theName) {
				return Lists.newArrayList(new Patient().setActive(true).setId("Patient/123"));
			}
		}
		MyProvider provider = new MyProvider();

		startServer(provider);

		try {
			myClient
				.search()
				.forResource(Patient.class)
				.where(Patient.NAME.matches().value("foo"))
				.include(Patient.INCLUDE_ORGANIZATION)
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("this server does not know how to handle GET operation[Patient] with parameters [[_include, name]]"));
		}
	}

	/**
	 * Server method with no _include
	 * Client request with _include
	 * <p>
	 * See #1421
	 */
	@Test
	public void testAllowIncludeIfProvided() throws Exception {

		class MyProvider extends MyBaseProvider {
			@Search
			public List<IBaseResource> search(@OptionalParam(name = "name") StringType theName, @IncludeParam Set<Include> theIncludes) {
				return Lists.newArrayList(new Patient().setActive(true).setId("Patient/123"));
			}
		}
		MyProvider provider = new MyProvider();

		startServer(provider);

		Bundle results = myClient
			.search()
			.forResource(Patient.class)
			.where(Patient.NAME.matches().value("foo"))
			.include(Patient.INCLUDE_ORGANIZATION)
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(1, results.getEntry().size());
	}

	/**
	 * Server method with no _revinclude
	 * Client request with _revinclude
	 * <p>
	 * See #1421
	 */
	@Test
	public void testRejectRevIncludeIfNotProvided() throws Exception {

		class MyProvider extends MyBaseProvider {
			@Search
			public List<IBaseResource> search(@OptionalParam(name = "name") StringType theName) {
				return Lists.newArrayList(new Patient().setActive(true).setId("Patient/123"));
			}
		}
		MyProvider provider = new MyProvider();

		startServer(provider);

		try {
			myClient
				.search()
				.forResource(Patient.class)
				.where(Patient.NAME.matches().value("foo"))
				.revInclude(Patient.INCLUDE_ORGANIZATION)
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("this server does not know how to handle GET operation[Patient] with parameters [[_revinclude, name]]"));
		}
	}

	/**
	 * Server method with no _revInclude
	 * Client request with _revInclude
	 * <p>
	 * See #1421
	 */
	@Test
	public void testAllowRevIncludeIfProvided() throws Exception {

		class MyProvider extends MyBaseProvider {
			@Search
			public List<IBaseResource> search(@OptionalParam(name = "name") StringType theName, @IncludeParam(reverse = true) Set<Include> theRevIncludes) {
				return Lists.newArrayList(new Patient().setActive(true).setId("Patient/123"));
			}
		}
		MyProvider provider = new MyProvider();

		startServer(provider);

		Bundle results = myClient
			.search()
			.forResource(Patient.class)
			.where(Patient.NAME.matches().value("foo"))
			.revInclude(Patient.INCLUDE_ORGANIZATION)
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(1, results.getEntry().size());
	}

	private void startServer(Object theProvider) throws Exception {
		RestfulServer servlet = new RestfulServer(myCtx);
		servlet.registerProvider(theProvider);
		ServletHandler proxyHandler = new ServletHandler();
		servlet.setDefaultResponseEncoding(EncodingEnum.XML);
		servlet.setBundleInclusionRule(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");

		myServer = new Server(0);
		myServer.setHandler(proxyHandler);
		JettyUtil.startServer(myServer);
		int port = JettyUtil.getPortForStartedServer(myServer);

		myClient = myCtx.newRestfulGenericClient("http://localhost:" + port);
	}


	public static class MyBaseProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}
	}

}
