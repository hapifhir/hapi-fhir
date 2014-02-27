package ca.uhn.fhir.ws;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.parser.XmlParser;
import ca.uhn.fhir.ws.exceptions.MethodNotFoundException;
import ca.uhn.fhir.ws.operations.DELETE;
import ca.uhn.fhir.ws.operations.GET;
import ca.uhn.fhir.ws.operations.POST;
import ca.uhn.fhir.ws.operations.PUT;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

public class Service extends HttpServlet {

    private static final Logger log = Logger.getLogger(Service.class);
    private static final String handlerPackage = "ca.uhn.rest.handlers";

    //map of request handler resources keyed by resource name
    private static Map<String, Resource> resources = new HashMap<String, Resource>();

    /**
     * looks up all the methods from the classes specified in jsonHandlerPackage environment variable, and puts them in a map,
     * keyed on the method name.
     * The handlers for incoming requests will be looked up based on the name.
     * Method names must be public, static, unique, and must be annotated with JsonOperation.
     */

    private List<String> findRESTHanlderPackages(Context env) throws Exception {

        List<String> packages = new ArrayList<String>();

        int i = 1;
        String origName = handlerPackage;
        String name = origName;

        while (true) {
            String handlerPackage = null;
            try {
                log.debug("Looking up:: " + name);
                handlerPackage = (String) env.lookup(name);
            } catch (NamingException ne) {
                log.debug(ne);
            }
            if (handlerPackage != null) {
                log.debug("Found:: " + name);
                packages.add(handlerPackage);
                name = origName + i;
                i++;
            } else {
                log.debug("Not Found:: " + name);
                break;
            }
        }
        if (packages.isEmpty()) {
            throw new Exception("No packages defined as '" + handlerPackage + "' in web.xml");
        }
        return packages;
    }

    private void addResourceMethod(Resource resource, Method method) throws Exception {

        Class<?>[] params = method.getParameterTypes();
        ResourceMethod rm = new ResourceMethod();
        rm.setResourceType(method.getReturnType());

        //each operation name must have a request type annotation and be unique
        if (null != method.getAnnotation(GET.class)) {
            rm.setRequestType(ResourceMethod.RequestType.GET);
        } else if (null != method.getAnnotation(PUT.class)) {
            rm.setRequestType(ResourceMethod.RequestType.PUT);
        } else if (null != method.getAnnotation(POST.class)) {
            rm.setRequestType(ResourceMethod.RequestType.POST);
        } else if (null != method.getAnnotation(DELETE.class)) {
            rm.setRequestType(ResourceMethod.RequestType.DELETE);
        }
        rm.setParameters(Util.getResourceParameters(method));
        resource.addMethod(rm);
    }

    private void findResourceMethods(Class<?> handler) throws Exception {
        for (Method m : handler.getDeclaredMethods()) {
            //only static methods are valid request handlers
            if (Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers())) {

                String resourceName = Util.getResourceName(m);
                Resource r = resources.get(resourceName);
                if (null == r) {
                    r = new Resource();
                    r.setResourceName(resourceName);
                    resources.put(resourceName, r);
                }
                addResourceMethod(r, m);

                log.debug("found handler: " + m.getName());
            }
        }
    }

    public void init() {
        try {
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            List<String> packages = findRESTHanlderPackages(env);

            for (String packagePath : packages) {
                log.info("searching for JSON operation handlers in package: " + packagePath);
                List<Class<?>> handlers = getClasses(packagePath);
                for (Class<?> handler : handlers) {
                    log.info("found class: " + handler.getName());
                    findResourceMethods(handler);
                }
            }
        } catch (Exception ex) {
            log.error("An error occurred while loading request handlers!", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(ResourceMethod.RequestType.GET, request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(ResourceMethod.RequestType.POST, request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(ResourceMethod.RequestType.PUT, request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(ResourceMethod.RequestType.DELETE, request, response);
    }

    protected void handleRequest(ResourceMethod.RequestType requestType, HttpServletRequest request, HttpServletResponse response) {
        try {

            response.setContentType(request.getHeader("Accept"));
            String resourceName = request.getRequestURI();
            Map<String, String> params = Util.getQueryParams(request.getQueryString());

            Resource resource = resources.get(resourceName);
            if (null == resource) throw new MethodNotFoundException("No resource available for " + resourceName);

            ResourceMethod resourceMethod = resource.getMethod(params.keySet());
            if (null == resourceMethod) throw new MethodNotFoundException("No resource method available for the supplied parameters " + params);

            FhirContext ctx = new FhirContext(resourceMethod.getResourceType());
            XmlParser p = new XmlParser(ctx);
            response.getWriter().write(p.encodeResourceToString(resourceMethod.invoke(params)));
        } catch (Throwable t) {
            //TODO: handle errors
        }


    }

    private static List<Class<?>> getClasses(String packageName)
            throws ClassNotFoundException, IOException {

        if (null == packageName) throw new ClassNotFoundException("package name must be specified for JSON operations");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
