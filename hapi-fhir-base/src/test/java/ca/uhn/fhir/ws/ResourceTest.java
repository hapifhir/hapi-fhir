package ca.uhn.fhir.ws;

import junit.framework.TestCase;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        HttpClient client = new HttpClient(connectionManager);


        PostMethod httpPost = new PostMethod("http://localhost:3000/foo/bar?bar=123&more=params");
        httpPost.setRequestEntity(new StringRequestEntity("test", "application/json", "UTF-8"));
        int status = client.executeMethod(httpPost);
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
