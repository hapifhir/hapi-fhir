package ca.uhn.fhir.rest.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.rest.server.exceptions.*;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.common.BaseMethodBinding;
import ca.uhn.fhir.rest.common.Request;
import ca.uhn.fhir.rest.common.SearchMethodBinding;

public abstract class RestfulServer extends HttpServlet {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulServer.class);

	private static final long serialVersionUID = 1L;

    private ISecurityManager securityManager;

	private FhirContext myFhirContext;

	private Map<Class<? extends IResource>, IResourceProvider> myTypeToProvider = new HashMap<Class<? extends IResource>, IResourceProvider>();

	@Override
	protected void doOptions(HttpServletRequest theReq, HttpServletResponse theResp) throws ServletException, IOException {
		handleRequest(SearchMethodBinding.RequestType.OPTIONS, theReq, theResp);
	}

	// map of request handler resources keyed by resource name
	private Map<String, Resource> resources = new HashMap<String, Resource>();

	@SuppressWarnings("unused")
	private EncodingUtil determineResponseEncoding(Map<String, String[]> theParams) {
		String[] format = theParams.remove(Constants.PARAM_FORMAT);
		// TODO: handle this once we support JSON
		return EncodingUtil.XML;
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(SearchMethodBinding.RequestType.DELETE, request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(SearchMethodBinding.RequestType.GET, request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(SearchMethodBinding.RequestType.POST, request, response);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(SearchMethodBinding.RequestType.PUT, request, response);
	}

	private void findResourceMethods(IResourceProvider theProvider) throws Exception {

		Class<? extends IResource> resourceType = theProvider.getResourceType();
		RuntimeResourceDefinition definition = myFhirContext.getResourceDefinition(resourceType);

		Resource r = new Resource();
		r.setResourceProvider(theProvider);
		r.setResourceName(definition.getName());
		resources.put(definition.getName(), r);

		ourLog.info("Scanning type for RESTful methods: {}", theProvider.getClass());

		Class<?> clazz = theProvider.getClass();
		for (Method m : clazz.getDeclaredMethods()) {
			if (Modifier.isPublic(m.getModifiers())) {
				ourLog.info("Scanning public method: {}#{}", theProvider.getClass(), m.getName());

				BaseMethodBinding foundMethodBinding = BaseMethodBinding.bindMethod(theProvider.getResourceType(), m);
				if (foundMethodBinding != null) {
					r.addMethod(foundMethodBinding);
					ourLog.info(" * Method: {}#{} is a handler", theProvider.getClass(), m.getName());
				} else {
					ourLog.info(" * Method: {}#{} is not a handler", theProvider.getClass(), m.getName());
				}
			}
		}
	}

	/**
	 * This method must be overridden to provide one or more resource providers
	 */
	public abstract Collection<IResourceProvider> getResourceProviders();

	/**
	 * This method should be overridden to provide a security manager
	 * instance. By default, returns null.
	 */
    public ISecurityManager getSecurityManager() {
        return null;
    }

	protected void handleRequest(SearchMethodBinding.RequestType requestType, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

            if (null != securityManager) {
                securityManager.authenticate(request);
            }

			String resourceName = null;
			String requestPath = StringUtils.defaultString(request.getRequestURI());
			String contextPath = StringUtils.defaultString(request.getContextPath());
			requestPath = requestPath.substring(contextPath.length());
			if (requestPath.charAt(0) == '/') {
				requestPath = requestPath.substring(1);
			}

			ourLog.info("Request URI: {}", requestPath);

			Map<String, String[]> params = new HashMap<String, String[]>(request.getParameterMap());
			EncodingUtil responseEncoding = determineResponseEncoding(params);

			StringTokenizer tok = new StringTokenizer(requestPath, "/");
			if (!tok.hasMoreTokens()) {
				throw new MethodNotFoundException("No resource name specified");
			}
			resourceName = tok.nextToken();

			Resource resourceBinding = resources.get(resourceName);
			if (resourceBinding == null) {
				throw new MethodNotFoundException("Unknown resource type: " + resourceName);
			}

			IdDt id = null;
			IdDt versionId = null;
			String operation = null;
			if (tok.hasMoreTokens()) {
				String nextString = tok.nextToken();
				if (nextString.startsWith("_")) {
					operation = nextString;
				}else {
					id = new IdDt(nextString);
				}
			}

			if (tok.hasMoreTokens()) {
				String nextString = tok.nextToken();
				if (nextString.startsWith("_history")) {
					if (tok.hasMoreTokens()) {
						versionId = new IdDt(tok.nextToken());
					}else {
						throw new InvalidRequestException("_history search specified but no version requested in URL");
					}
				}
			}
			
			// TODO: look for more tokens for version, compartments, etc...

			//
			//
			// if (identity != null && !tok.hasMoreTokens()) {
			// if (params == null || params.isEmpty()) {
			// IResource resource =
			// resourceBinding.getResourceProvider().getResourceById(identity);
			// if (resource == null) {
			// throw new ResourceNotFoundException(identity);
			// }
			// streamResponseAsResource(response, resource, resourceBinding,
			// responseEncoding);
			// return;
			// }
			// }

			Request r = new Request();
			r.setResourceName(resourceName);
			r.setId(id);
			r.setVersion(versionId);
			r.setOperation(operation);
			r.setParameterNames(params.keySet());
			r.setRequestType(requestType);

			BaseMethodBinding resourceMethod = resourceBinding.getMethod(r);
			if (null == resourceMethod) {
				throw new MethodNotFoundException("No resource method available for the supplied parameters " + params);
			}

			List<IResource> result = resourceMethod.invokeServer(resourceBinding.getResourceProvider(), id, versionId, params);
			switch (resourceMethod.getReturnType()) {
			case BUNDLE:
				streamResponseAsBundle(response, result, responseEncoding);
				break;
			case RESOURCE:
				if (result.size() == 0) {
					throw new ResourceNotFoundException(id);
				} else if (result.size() > 1) {
					throw new InternalErrorException("Method returned multiple resources");
				}
				streamResponseAsResource(response, result.get(0), resourceBinding, responseEncoding);
				break;
			}
			// resourceMethod.get

		} catch (AuthenticationException e) {
            response.setStatus(e.getStatusCode());
            response.getWriter().write(e.getMessage());
        }
        catch (BaseServerResponseException e) {

			if (e instanceof InternalErrorException) {
				ourLog.error("Failure during REST processing", e);
			} else {
				ourLog.warn("Failure during REST processing: {}", e.toString());
			}

			response.setStatus(e.getStatusCode());
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append(e.getMessage());
			response.getWriter().close();

		} catch (Throwable t) {
			// TODO: handle this better
			ourLog.error("Failed to process invocation", t);
			throw new ServletException(t);
		}

	}

	@Override
	public void init() throws ServletException {
		try {
			ourLog.info("Initializing HAPI FHIR restful server");

            securityManager = getSecurityManager();
            if (null == securityManager) {
                ourLog.warn("No security manager has been provided, requests will not be authenticated!");
            }

			Collection<IResourceProvider> resourceProvider = getResourceProviders();
			for (IResourceProvider nextProvider : resourceProvider) {
				if (myTypeToProvider.containsKey(nextProvider.getResourceType())) {
					throw new ServletException("Multiple providers for type: " + nextProvider.getResourceType().getCanonicalName());
				}
				myTypeToProvider.put(nextProvider.getResourceType(), nextProvider);
			}

			ourLog.info("Got {} resource providers", myTypeToProvider.size());

			myFhirContext = new FhirContext(myTypeToProvider.keySet());

			for (IResourceProvider provider : myTypeToProvider.values()) {
				findResourceMethods(provider);
			}

		} catch (Exception ex) {
			ourLog.error("An error occurred while loading request handlers!", ex);
			throw new ServletException("Failed to initialize FHIR Restful server", ex);
		}
	}

	private void streamResponseAsBundle(HttpServletResponse theHttpResponse, List<IResource> theResult, EncodingUtil theResponseEncoding) throws IOException {
		theHttpResponse.setStatus(200);
		theHttpResponse.setContentType(Constants.CT_FHIR_XML);
		theHttpResponse.setCharacterEncoding("UTF-8");

		Bundle bundle = new Bundle();
		bundle.getAuthorName().setValue(getClass().getCanonicalName());
		bundle.getId().setValue(UUID.randomUUID().toString());
		bundle.getPublished().setToCurrentTimeInLocalTimeZone();

		for (IResource next : theResult) {
			BundleEntry entry = new BundleEntry();
			bundle.getEntries().add(entry);

			entry.setResource(next);
		}

		bundle.getTotalResults().setValue(theResult.size());

		PrintWriter writer = theHttpResponse.getWriter();
		myFhirContext.newXmlParser().encodeBundleToWriter(bundle, writer);
		writer.close();
	}

	private void streamResponseAsResource(HttpServletResponse theHttpResponse, IResource theResource, Resource theResourceBinding, EncodingUtil theResponseEncoding) throws IOException {

		theHttpResponse.setStatus(200);
		theHttpResponse.setContentType(Constants.CT_FHIR_XML);
		theHttpResponse.setCharacterEncoding("UTF-8");

		PrintWriter writer = theHttpResponse.getWriter();
		myFhirContext.newXmlParser().encodeResourceToWriter(theResource, writer);
		writer.close();

	}

}
