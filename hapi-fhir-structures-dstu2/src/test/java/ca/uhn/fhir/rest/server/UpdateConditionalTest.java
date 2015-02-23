package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.util.PortUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class UpdateConditionalTest {
    private static CloseableHttpClient ourClient;
    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UpdateConditionalTest.class);
    private static int ourPort;
    private static Server ourServer;

    @Test
    public void testUpdate() throws Exception {

        Patient patient = new Patient();
        patient.addIdentifier().setValue("002");

        HttpPut httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient/001");
        httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

        HttpResponse status = ourClient.execute(httpPost);

        String responseContent = IOUtils.toString(status.getEntity().getContent());
        IOUtils.closeQuietly(status.getEntity().getContent());

        ourLog.info("Response was:\n{}", responseContent);

        OperationOutcome oo = new FhirContext().newXmlParser().parseResource(OperationOutcome.class, responseContent);
        assertEquals("OODETAILS", oo.getIssueFirstRep().getDetails());

        assertEquals(200, status.getStatusLine().getStatusCode());
        assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("location").getValue());
        assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("content-location").getValue());

    }


    @AfterClass
    public static void afterClass() throws Exception {
        ourServer.stop();
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        ourPort = PortUtil.findFreePort();
        ourServer = new Server(ourPort);

        PatientProvider patientProvider = new PatientProvider();

        ServletHandler proxyHandler = new ServletHandler();
        RestfulServer servlet = new RestfulServer();
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

    private static String ourLastConditionalUrl;


    @Before
    public void before() {
        ourLastConditionalUrl = null;
    }

    public static class PatientProvider implements IResourceProvider {

        @Override
        public Class<? extends IResource> getResourceType() {
            return Patient.class;
        }


        @Update()
        public MethodOutcome updatePatient(@IdParam IdDt theId, @ResourceParam Patient thePatient) {
            IdDt id = theId.withVersion(thePatient.getIdentifierFirstRep().getValue());
            OperationOutcome oo = new OperationOutcome();
            oo.addIssue().setDetails("OODETAILS");
            if (theId.getValueAsString().contains("CREATE")) {
                return new MethodOutcome(id, oo, true);
            }

            return new MethodOutcome(id, oo);
        }

    }

}
