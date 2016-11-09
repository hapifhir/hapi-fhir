package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.CompositeAndListParam;
import ca.uhn.fhir.rest.param.CompositeOrListParam;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class CompositeParameterTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu1();
	private static int ourPort;
	private static Server ourServer;

	@Test
	public void testSearchWithDateValue() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation?" + Observation.SP_NAME_VALUE_DATE + "=" + URLEncoder.encode("foo\\$bar$2001-01-01", "UTF-8"));
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			List<BundleEntry> entries = ourCtx.newXmlParser().parseBundle(responseContent).getEntries();
			assertEquals(1, entries.size());
			Observation o = (Observation) entries.get(0).getResource();
			assertEquals("foo$bar", o.getName().getText().getValue());
			assertEquals("2001-01-01", ((DateTimeDt) o.getApplies()).getValueAsString().substring(0, 10));
		}
	}

	@Test
	public void testSearchWithMultipleValue() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation?" + Observation.SP_NAME_VALUE_STRING + "=" + URLEncoder.encode("l1$r1,l2$r2", "UTF-8") + "&" + Observation.SP_NAME_VALUE_STRING + "=" + URLEncoder.encode("l3$r3,l4$r4", "UTF-8"));
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			List<BundleEntry> entries = ourCtx.newXmlParser().parseBundle(responseContent).getEntries();
			assertEquals(1, entries.size());
			Observation o = (Observation) entries.get(0).getResource();
			assertEquals("AND", o.getName().getCoding().get(0).getDisplay().getValue());
			assertEquals("OR", o.getName().getCoding().get(1).getDisplay().getValue());
			assertEquals("l1", o.getName().getCoding().get(1).getSystem().getValueAsString());
			assertEquals("r1", o.getName().getCoding().get(1).getCode().getValue());
			assertEquals("OR", o.getName().getCoding().get(2).getDisplay().getValue());
			assertEquals("l2", o.getName().getCoding().get(2).getSystem().getValueAsString());
			assertEquals("r2", o.getName().getCoding().get(2).getCode().getValue());

			assertEquals("AND", o.getName().getCoding().get(3).getDisplay().getValue());
			assertEquals("OR", o.getName().getCoding().get(4).getDisplay().getValue());
			assertEquals("l3", o.getName().getCoding().get(4).getSystem().getValueAsString());
			assertEquals("r3", o.getName().getCoding().get(4).getCode().getValue());
			assertEquals("OR", o.getName().getCoding().get(5).getDisplay().getValue());
			assertEquals("l4", o.getName().getCoding().get(5).getSystem().getValueAsString());
			assertEquals("r4", o.getName().getCoding().get(5).getCode().getValue());
		}
	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyObservationResourceProvider patientProvider = new DummyObservationResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();
	}


	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyObservationResourceProvider implements IResourceProvider {
		//@formatter:off
		@Search
		public List<Observation> findObservation(
				@RequiredParam(name = Observation.SP_NAME_VALUE_DATE, compositeTypes= { StringParam.class, DateParam.class }) 
				CompositeParam<StringParam, DateParam> theParam
				) {
			//@formatter:on

			ArrayList<Observation> retVal = new ArrayList<Observation>();

			Observation p = new Observation();
			p.setId("1");
			p.setApplies(theParam.getRightValue().getValueAsDateTimeDt());
			p.getName().setText(theParam.getLeftValue().getValueAsStringDt());
			retVal.add(p);

			return retVal;
		}

		//@formatter:off
		@Search
		public List<Observation> findObservationNVS(
				@RequiredParam(name = Observation.SP_NAME_VALUE_STRING, compositeTypes= { StringParam.class, StringParam.class }) 
				CompositeAndListParam<StringParam, StringParam> theParam
				) {
			//@formatter:on

			ArrayList<Observation> retVal = new ArrayList<Observation>();

			Observation p = new Observation();
			p.setId("1");
			for (CompositeOrListParam<StringParam, StringParam> nextAnd : theParam.getValuesAsQueryTokens()) {
				p.getName().addCoding().getDisplay().setValue("AND");
				for (CompositeParam<StringParam, StringParam> nextOr : nextAnd.getValuesAsQueryTokens()) {
					p.getName().addCoding().setDisplay("OR").setSystem(nextOr.getLeftValue().getValue()).setCode(nextOr.getRightValue().getValue());
				}
			}
			retVal.add(p);

			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Observation.class;
		}

	}

}
