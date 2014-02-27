package ca.uhn.fhir.ws;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ResourceTest extends TestCase {


    @Before
    public void setUp() throws Exception {
        /*
        System.setProperty("java.naming.factory.initial", "org.apache.naming.java.javaURLContextFactory");
        System.setProperty("java.naming.factory.url.pkgs", "org.apache.naming");
        InitialContext context = new InitialContext();
        //context.bind("java:comp", "env");
        context.bind(context.composeName("java:comp", "env"), "ca.uhn.rest.handlers");

        //Context subcontext = context.createSubcontext("java:comp/env");
        //context.bind("java:comp/env/ca.uhn.rest.handlers", "ca.uhn.test");

        Context env = (Context) new InitialContext().lookup("java:comp/env");

        //System.out.println((String) env.lookup("ca.uhn.rest.handlers"));
        */
    }

    @Test
    public void testServlet() throws Exception {

        Server server = new Server(3000);

        ServletHandler proxyHandler = new ServletHandler();
        ServletHolder servletHolder = new ServletHolder(new Service());
        proxyHandler.addServletWithMapping(servletHolder, "/");
        server.setHandler(proxyHandler);
        server.start();

        PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager(SchemeRegistryFactory.createDefault(), 5000, TimeUnit.MILLISECONDS);
        HttpClient client = new DefaultHttpClient(connectionManager);
        

        HttpPost httpPost = new HttpPost("http://localhost:3000/foo/bar?bar=123&more=params");
        httpPost.setEntity(new StringEntity("test", "application/json", "UTF-8"));
        HttpResponse status = client.execute(httpPost);
        System.out.println(status);

//        server.join();
    }

    @Test
    public void testResource() throws Exception {

        //TODO: better params handling
        for (Method m : PatientResource.class.getDeclaredMethods()) {
             Util.getResourceParameters(m);

            Integer i = 0;
            Class<?> c = i.getClass();
            System.out.println(c.getCanonicalName());
        }
    }

    @Test
    public void testRequiredParamsMissing() {
        ResourceMethod rm = new ResourceMethod();
        List<Parameter> methodParams = new ArrayList<Parameter>();

        methodParams.add(new Parameter("firstName", false));
        methodParams.add(new Parameter("lastName", false));
        methodParams.add(new Parameter("mrn", true));

        rm.setParameters(methodParams);

        Set<String> inputParams = new HashSet<String>();
        inputParams.add("firstName");
        inputParams.add("lastName");

        assertEquals(false, rm.matches(inputParams)); //False
    }

    @Test
    public void testRequiredParamsOnly() {
        ResourceMethod rm = new ResourceMethod();
        List<Parameter> methodParams = new ArrayList<Parameter>();

        methodParams.add(new Parameter("firstName", false));
        methodParams.add(new Parameter("lastName", false));
        methodParams.add(new Parameter("mrn", true));

        rm.setParameters(methodParams);

        Set<String> inputParams = new HashSet<String>();
        inputParams.add("mrn");
        assertEquals(true, rm.matches(inputParams)); //True
    }

    @Test
    public void testMixedParams() {
        ResourceMethod rm = new ResourceMethod();
        List<Parameter> methodParams = new ArrayList<Parameter>();

        methodParams.add(new Parameter("firstName", false));
        methodParams.add(new Parameter("lastName", false));
        methodParams.add(new Parameter("mrn", true));

        rm.setParameters(methodParams);

        Set<String> inputParams = new HashSet<String>();
        inputParams.add("firstName");
        inputParams.add("mrn");

        assertEquals(true, rm.matches(inputParams)); //True
    }

    @Test
    public void testAllParams() {
        ResourceMethod rm = new ResourceMethod();
        List<Parameter> methodParams = new ArrayList<Parameter>();

        methodParams.add(new Parameter("firstName", false));
        methodParams.add(new Parameter("lastName", false));
        methodParams.add(new Parameter("mrn", true));

        rm.setParameters(methodParams);

        Set<String> inputParams = new HashSet<String>();
        inputParams.add("firstName");
        inputParams.add("lastName");
        inputParams.add("mrn");

        assertEquals(true, rm.matches(inputParams)); //True
    }

    @Test
    public void testAllParamsWithExtra() {
        ResourceMethod rm = new ResourceMethod();
        List<Parameter> methodParams = new ArrayList<Parameter>();

        methodParams.add(new Parameter("firstName", false));
        methodParams.add(new Parameter("lastName", false));
        methodParams.add(new Parameter("mrn", true));

        rm.setParameters(methodParams);

        Set<String> inputParams = new HashSet<String>();
        inputParams.add("firstName");
        inputParams.add("lastName");
        inputParams.add("mrn");
        inputParams.add("foo");

        assertEquals(false, rm.matches(inputParams)); //False
    }

}
