package ca.uhn.fhir.ws;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.XmlParser;
import ca.uhn.fhir.ws.exceptions.MethodNotFoundException;
import ca.uhn.fhir.ws.operations.DELETE;
import ca.uhn.fhir.ws.operations.GET;
import ca.uhn.fhir.ws.operations.POST;
import ca.uhn.fhir.ws.operations.PUT;

public abstract class RestfulServer extends HttpServlet {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulServer.class);

	// map of request handler resources keyed by resource name
	private Map<String, Resource> resources = new HashMap<String, Resource>();

	private static final long serialVersionUID = 1L;

	private Map<Class<? extends IResource>, IResourceProvider<?>> myTypeToProvider = new HashMap<Class<? extends IResource>, IResourceProvider<?>>();

	private FhirContext myFhirContext;

	public abstract Collection<IResourceProvider<?>> getResourceProviders();

	@Override
	public void init() throws ServletException {
		try {
			ourLog.info("Initializing HAPI FHIR restful server");

			Collection<IResourceProvider<?>> resourceProvider = getResourceProviders();
			for (IResourceProvider<?> nextProvider : resourceProvider) {
				if (myTypeToProvider.containsKey(nextProvider.getResourceType())) {
					throw new ServletException("Multiple providers for type: " + nextProvider.getResourceType().getCanonicalName());
				}
				myTypeToProvider.put(nextProvider.getResourceType(), nextProvider);
			}

			ourLog.info("Got {} resource providers",myTypeToProvider.size());
			
			myFhirContext = new FhirContext(myTypeToProvider.keySet());
			
//			findResourceMethods(nextProvider.getClass());

			
		} catch (Exception ex) {
			ourLog.error("An error occurred while loading request handlers!", ex);
			throw new ServletException("Failed to initialize FHIR Restful server", ex);
		}
	}

	private void addResourceMethod(Resource resource, Method method) throws Exception {

		Class<?>[] params = method.getParameterTypes();
		ResourceMethod rm = new ResourceMethod();
		rm.setResourceType(method.getReturnType());

		// each operation name must have a request type annotation and be unique
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

	private void findResourceMethods(Class<? extends IResourceProvider> theClass1) throws Exception {
		for (Method m : theClass1.getDeclaredMethods()) {
			if (Modifier.isPublic(m.getModifiers())) {

				String resourceName = Util.getResourceName(m);
				Resource r = resources.get(resourceName);
				if (null == r) {
					r = new Resource();
					r.setResourceName(resourceName);
					resources.put(resourceName, r);
				}
				addResourceMethod(r, m);

				ourLog.debug("found handler: " + m.getName());
			}
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(ResourceMethod.RequestType.DELETE, request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(ResourceMethod.RequestType.GET, request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(ResourceMethod.RequestType.POST, request, response);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(ResourceMethod.RequestType.PUT, request, response);
	}

	protected void handleRequest(ResourceMethod.RequestType requestType, HttpServletRequest request, HttpServletResponse response) {
		try {

			response.setContentType(request.getHeader("Accept"));
			String resourceName = request.getRequestURI();
			Map<String, String> params = Util.getQueryParams(request.getQueryString());

			Resource resource = resources.get(resourceName);
			if (null == resource)
				throw new MethodNotFoundException("No resource available for " + resourceName);

			ResourceMethod resourceMethod = resource.getMethod(params.keySet());
			if (null == resourceMethod)
				throw new MethodNotFoundException("No resource method available for the supplied parameters " + params);

			FhirContext ctx = new FhirContext(resourceMethod.getResourceType());
			XmlParser p = new XmlParser(ctx);
			response.getWriter().write(p.encodeResourceToString(resourceMethod.invoke(params)));
		} catch (Throwable t) {
			// TODO: handle errors
		}

	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
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

	private static List<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException {

		if (null == packageName)
			throw new ClassNotFoundException("package name must be specified for JSON operations");

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
}
