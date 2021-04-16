package ca.uhn.fhir.rest.openapi;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.IServerConformanceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ClasspathUtil;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.convertors.VersionConvertor_40_50;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationDefinition;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class OpenApiInterceptor {
	public static final String FHIR_JSON_RESOURCE = "FHIR-JSON-RESOURCE";
	public static final String FHIR_XML_RESOURCE = "FHIR-XML-RESOURCE";
	public static final String TAG_FHIR_SERVER = "FHIR Server";
	public static final FhirContext FHIR_CONTEXT_CANONICAL = FhirContext.forR4();
	private static final Logger ourLog = LoggerFactory.getLogger(OpenApiInterceptor.class);
	private final String mySwaggerUiVersion;

	/**
	 * Constructor
	 */
	public OpenApiInterceptor() {
		Properties props = new Properties();
		String resourceName = "/META-INF/maven/org.webjars/swagger-ui/pom.properties";
		try {
			InputStream resourceAsStream = ClasspathUtil.loadResourceAsStream(resourceName);
			props.load(resourceAsStream);
		} catch (IOException e) {
			throw new ConfigurationException("Failed to load resource: " + resourceName);
		}
		mySwaggerUiVersion = props.getProperty("version");


	}


	@Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLER_SELECTED)
	public boolean serveSwaggerUi(HttpServletRequest theRequest, HttpServletResponse theResponse, ServletRequestDetails theRequestDetails) throws IOException {
		String requestPath = theRequest.getPathInfo();
		ourLog.debug("Serving request path: {}", requestPath);

		if (requestPath.equals("")) {
			Set<String> highestRankedAcceptValues = RestfulServerUtils.parseAcceptHeaderAndReturnHighestRankedOptions(theRequest);
			if (highestRankedAcceptValues.contains(Constants.CT_HTML)) {
				theResponse.sendRedirect("./swagger-ui/");
				return false;
			}

		}

		if (requestPath.startsWith("/swagger-ui/")) {
			if (requestPath.equals("/swagger-ui/") || requestPath.equals("/swagger-ui/index.html")) {
				CapabilityStatement cs = getCapabilityStatement(theRequestDetails);

				theResponse.setStatus(200);
				theResponse.setContentType(Constants.CT_HTML);
				String resource = ClasspathUtil.loadResource("/ca/uhn/fhir/rest/openapi/index.html");
				resource = resource.replace("${SERVER_NAME}", cs.getSoftware().getName());
				resource = resource.replace("${SERVER_VERSION}", cs.getSoftware().getVersion());

				theResponse.getWriter().write(resource);
				theResponse.getWriter().close();
				return false;
			}

			if (requestPath.equals("/swagger-ui/raccoon.png")) {
				theResponse.setStatus(200);
				theResponse.setContentType("image/png");
				try (InputStream resource = ClasspathUtil.loadResourceAsStream("/ca/uhn/fhir/rest/openapi/raccoon.png")) {
					IOUtils.copy(resource, theResponse.getOutputStream());
					theResponse.getOutputStream().close();
				}
				return false;
			}

			String resourcePath = requestPath.substring("/swagger-ui/".length());
			try (InputStream resource = ClasspathUtil.loadResourceAsStream("/META-INF/resources/webjars/swagger-ui/" + mySwaggerUiVersion + "/" + resourcePath)) {

				if (resourcePath.endsWith(".js") || resourcePath.endsWith(".map")) {
					theResponse.setContentType("application/javascript");
					theResponse.setStatus(200);
					IOUtils.copy(resource, theResponse.getOutputStream());
					theResponse.getOutputStream().close();
					return false;
				}

				if (resourcePath.endsWith(".css")) {
					theResponse.setContentType("text/css");
					theResponse.setStatus(200);
					IOUtils.copy(resource, theResponse.getOutputStream());
					theResponse.getOutputStream().close();
					return false;
				}

			}

		} else if (requestPath.equals("/api-docs")) {

			OpenAPI openApi = generateOpenApi(theRequestDetails);
			String response = Yaml.pretty(openApi);

			theResponse.setContentType("text/yaml");
			theResponse.setStatus(200);
			theResponse.getWriter().write(response);
			theResponse.getWriter().close();
			return false;

		}

		return true;
	}

	private OpenAPI generateOpenApi(ServletRequestDetails theRequestDetails) {
		CapabilityStatement cs = getCapabilityStatement(theRequestDetails);

		IServerConformanceProvider<?> capabilitiesProvider = null;
		RestfulServer restfulServer = theRequestDetails.getServer();
		if (restfulServer.getServerConformanceProvider() instanceof IServerConformanceProvider) {
			capabilitiesProvider = (IServerConformanceProvider<?>) restfulServer.getServerConformanceProvider();
		}


		OpenAPI openApi = new OpenAPI();

		openApi.setInfo(new Info());
		openApi.getInfo().setDescription(cs.getDescription());
		openApi.getInfo().setTitle(cs.getSoftware().getName());
		openApi.getInfo().setVersion(cs.getSoftware().getVersion());
		openApi.getInfo().setContact(new Contact());
		openApi.getInfo().getContact().setName(cs.getContactFirstRep().getName());
		openApi.getInfo().getContact().setEmail(cs.getContactFirstRep().getTelecomFirstRep().getValue());

		openApi.setComponents(new Components());

		ObjectSchema fhirJsonSchema = new ObjectSchema();
		fhirJsonSchema.setDescription("A FHIR resource");
		openApi.getComponents().addSchemas(FHIR_JSON_RESOURCE, fhirJsonSchema);

		ObjectSchema fhirXmlSchema = new ObjectSchema();
		fhirXmlSchema.setDescription("A FHIR resource");
		openApi.getComponents().addSchemas(FHIR_XML_RESOURCE, fhirXmlSchema);

		Server server = new Server();
		openApi.addServersItem(server);
		server.setUrl(cs.getImplementation().getUrl());
		server.setDescription(cs.getSoftware().getName());

		Tag serverTag = new Tag();
		serverTag.setName(TAG_FHIR_SERVER);
		serverTag.setDescription("Server-level operations");
		openApi.addTagsItem(serverTag);

		Paths paths = new Paths();
		openApi.setPaths(paths);

		Operation capabilitiesOperation = getPathItem(paths, "/metadata", PathItem.HttpMethod.GET);
		capabilitiesOperation.addTagsItem(TAG_FHIR_SERVER);
		capabilitiesOperation.setSummary("server-capabilities: Fetch the server FHIR CapabilityStatement");
		addFhirResourceResponse(capabilitiesOperation);

		// System-level Operations
		for (CapabilityStatement.CapabilityStatementRestResourceOperationComponent nextOperation : cs.getRestFirstRep().getOperation()) {
			addFhirOperation(theRequestDetails, capabilitiesProvider, paths, null, nextOperation);
		}

		for (CapabilityStatement.CapabilityStatementRestResourceComponent nextResource : cs.getRestFirstRep().getResource()) {
			String resourceType = nextResource.getType();
			Set<CapabilityStatement.TypeRestfulInteraction> typeRestfulInteractions = nextResource.getInteraction().stream().map(t -> t.getCodeElement().getValue()).collect(Collectors.toSet());

			Tag resourceTag = new Tag();
			resourceTag.setName(resourceType);
			resourceTag.setDescription("The " + resourceType + " FHIR resource type");
			openApi.addTagsItem(resourceTag);

			if (typeRestfulInteractions.contains(CapabilityStatement.TypeRestfulInteraction.READ)) {
				Operation operation = getPathItem(paths, "/" + resourceType + "/{id}", PathItem.HttpMethod.GET);
				operation.addTagsItem(resourceType);
				operation.setSummary("read-instance: Read " + resourceType + " instance");
				addResourceIdParameter(operation);
				addFhirResourceResponse(operation);
			}

			if (typeRestfulInteractions.contains(CapabilityStatement.TypeRestfulInteraction.VREAD)) {
				Operation operation = getPathItem(paths, "/" + resourceType + "/{id}/_history/{version_id}", PathItem.HttpMethod.GET);
				operation.addTagsItem(resourceType);
				operation.setSummary("vread-instance: Read " + resourceType + " instance with specific version");
				addResourceIdParameter(operation);
				addResourceVersionIdParameter(operation);
				addFhirResourceResponse(operation);
			}

			if (typeRestfulInteractions.contains(CapabilityStatement.TypeRestfulInteraction.CREATE)) {
				Operation operation = getPathItem(paths, "/" + resourceType, PathItem.HttpMethod.POST);
				operation.addTagsItem(resourceType);
				operation.setSummary("create-type: Create a new " + resourceType + " instance");
				addFhirResourceRequestBody(operation);
				addFhirResourceResponse(operation);
			}

			if (typeRestfulInteractions.contains(CapabilityStatement.TypeRestfulInteraction.UPDATE)) {
				Operation operation = getPathItem(paths, "/" + resourceType + "/{id}", PathItem.HttpMethod.PUT);
				operation.addTagsItem(resourceType);
				operation.setSummary("update-instance: Update an existing " + resourceType + " instance, or create using a client-assigned ID");
				addResourceIdParameter(operation);
				addFhirResourceRequestBody(operation);
				addFhirResourceResponse(operation);
			}

			if (typeRestfulInteractions.contains(CapabilityStatement.TypeRestfulInteraction.SEARCHTYPE)) {
				Operation operation = getPathItem(paths, "/" + resourceType, PathItem.HttpMethod.GET);
				operation.addTagsItem(resourceType);
				operation.setSummary("search-type: Update an existing " + resourceType + " instance, or create using a client-assigned ID");
				addFhirResourceResponse(operation);

				for (CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent nextSearchParam : nextResource.getSearchParam()) {
					Parameter parametersItem = new Parameter();
					operation.addParametersItem(parametersItem);

					parametersItem.setName(nextSearchParam.getName());
					parametersItem.setIn("query");
					parametersItem.setDescription(nextSearchParam.getDocumentation());
					parametersItem.setStyle(Parameter.StyleEnum.SIMPLE);
				}
			}

			// Resource-level Operations
			for (CapabilityStatement.CapabilityStatementRestResourceOperationComponent nextOperation : nextResource.getOperation()) {
				addFhirOperation(theRequestDetails, capabilitiesProvider, paths, resourceType, nextOperation);
			}

		}

		return openApi;
	}

	private CapabilityStatement getCapabilityStatement(ServletRequestDetails theRequestDetails) {
		RestfulServer restfulServer = theRequestDetails.getServer();
		IBaseConformance versionIndependentCapabilityStatement = restfulServer.getCapabilityStatement(theRequestDetails);
		CapabilityStatement cs = toCanonicalVersion(versionIndependentCapabilityStatement);
		return cs;
	}

	private void addFhirOperation(ServletRequestDetails theRequestDetails, IServerConformanceProvider<?> theCapabilitiesProvider, Paths thePaths, String theResourceType, CapabilityStatement.CapabilityStatementRestResourceOperationComponent theOperation) {
		FhirContext ctx = theRequestDetails.getFhirContext();

		if (theCapabilitiesProvider != null) {
			IdType definitionId = new IdType(theOperation.getDefinition());
			IBaseResource operationDefinitionNonCanonical = theCapabilitiesProvider.readOperationDefinition(definitionId, theRequestDetails);
			OperationDefinition operationDefinition = toCanonicalVersion(operationDefinitionNonCanonical);

			// GET form for non-state-affecting operations
			if (!operationDefinition.getAffectsState()) {
				if (operationDefinition.getSystem()) {
					Operation operation = getPathItem(thePaths, "/$" + operationDefinition.getCode(), PathItem.HttpMethod.GET);
					populateOperation(null, operationDefinition, operation, true);
				}
				if (theResourceType != null) {
					if (operationDefinition.getType()) {
						Operation operation = getPathItem(thePaths, "/" + theResourceType + "/$" + operationDefinition.getCode(), PathItem.HttpMethod.GET);
						populateOperation(theResourceType, operationDefinition, operation, true);
					}
					if (operationDefinition.getInstance()) {
						Operation operation = getPathItem(thePaths, "/" + theResourceType + "/{id}/$" + operationDefinition.getCode(), PathItem.HttpMethod.GET);
						addResourceIdParameter(operation);
						populateOperation(theResourceType, operationDefinition, operation, true);
					}
				}
			}

			// POST form for all operations
			if (operationDefinition.getSystem()) {
				Operation operation = getPathItem(thePaths, "/$" + operationDefinition.getCode(), PathItem.HttpMethod.POST);
				populateOperation(null, operationDefinition, operation, false);
			}
			if (theResourceType != null) {
				if (operationDefinition.getType()) {
					Operation operation = getPathItem(thePaths, "/" + theResourceType + "/$" + operationDefinition.getCode(), PathItem.HttpMethod.POST);
					populateOperation(theResourceType, operationDefinition, operation, false);
				}
				if (operationDefinition.getInstance()) {
					Operation operation = getPathItem(thePaths, "/" + theResourceType + "/{id}/$" + operationDefinition.getCode(), PathItem.HttpMethod.POST);
					addResourceIdParameter(operation);
					populateOperation(theResourceType, operationDefinition, operation, false);
				}
			}


		}
	}

	private void populateOperation(String theResourceType, OperationDefinition theOperationDefinition, Operation theOperation, boolean theGet) {
		if (theResourceType == null) {
			theOperation.addTagsItem(TAG_FHIR_SERVER);
		} else {
			theOperation.addTagsItem(theResourceType);
		}
		theOperation.setSummary(theOperationDefinition.getDescription());
		addFhirResourceResponse(theOperation);

		if (theGet) {

			for (OperationDefinition.OperationDefinitionParameterComponent nextSearchParam : theOperationDefinition.getParameter()) {
				Parameter parametersItem = new Parameter();
				theOperation.addParametersItem(parametersItem);

				parametersItem.setName(nextSearchParam.getName());
				parametersItem.setIn("query");
				parametersItem.setDescription(nextSearchParam.getDocumentation());
				parametersItem.setStyle(Parameter.StyleEnum.SIMPLE);
				if (nextSearchParam.getMin() > 0) {
					parametersItem.setRequired(true);
				} else {
					parametersItem.setRequired(false);
				}
			}

		} else {

			Parameters exampleRequestBody = new Parameters();
			for (OperationDefinition.OperationDefinitionParameterComponent nextSearchParam : theOperationDefinition.getParameter()) {
				Parameters.ParametersParameterComponent param = exampleRequestBody.addParameter();
				param.setName(nextSearchParam.getName());
				String paramType = nextSearchParam.getType();
				switch (defaultString(paramType)) {
					case "uri":
					case "url":
					case "code":
					case "string": {
						IPrimitiveType<?> type = (IPrimitiveType<?>) FHIR_CONTEXT_CANONICAL.getElementDefinition(paramType).newInstance();
						type.setValueAsString("example");
						param.setValue((Type) type);
						break;
					}
					case "integer": {
						IPrimitiveType<?> type = (IPrimitiveType<?>) FHIR_CONTEXT_CANONICAL.getElementDefinition(paramType).newInstance();
						type.setValueAsString("0");
						param.setValue((Type) type);
						break;
					}
					case "CodeableConcept": {
						CodeableConcept type = new CodeableConcept();
						type.getCodingFirstRep().setSystem("http://example.com");
						type.getCodingFirstRep().setCode("1234");
						param.setValue((Type) type);
						break;
					}
					case "Coding": {
						Coding type = new Coding();
						type.setSystem("http://example.com");
						type.setCode("1234");
						param.setValue((Type) type);
						break;
					}
					case "Reference":
						Reference reference = new Reference("example");
						param.setValue(reference);
						break;
					case "Resource":
						if (theResourceType != null) {
							IBaseResource resource = FHIR_CONTEXT_CANONICAL.getResourceDefinition(theResourceType).newInstance();
							resource.setId("1");
							param.setResource((Resource) resource);
						}
						break;
				}

			}

			String exampleRequestBodyString = FHIR_CONTEXT_CANONICAL.newJsonParser().setPrettyPrint(true).encodeResourceToString(exampleRequestBody);
			theOperation.setRequestBody(new RequestBody());
			theOperation.getRequestBody().setContent(new Content());
			MediaType mediaType = new MediaType();
			mediaType.setExample(exampleRequestBodyString);
			mediaType.setSchema(new Schema().type("object").title("FHIR Resource"));
			theOperation.getRequestBody().getContent().addMediaType(Constants.CT_FHIR_JSON_NEW, mediaType);


		}
	}

	private Operation getPathItem(Paths thePaths, String thePath, PathItem.HttpMethod theMethod) {
		PathItem pathItem;
		if (thePaths.containsKey(thePath)) {
			pathItem = thePaths.get(thePath);
		} else {
			pathItem = new PathItem();
			thePaths.addPathItem(thePath, pathItem);
		}

		switch (theMethod) {
			case POST:
				assert pathItem.getPost() == null : "Have duplicate POST at path: " + thePath;
				return pathItem.post(new Operation()).getPost();
			case GET:
				assert pathItem.getGet() == null;
				return pathItem.get(new Operation()).getGet();
			case PUT:
				assert pathItem.getPut() == null;
				return pathItem.put(new Operation()).getPut();
			case PATCH:
				assert pathItem.getPatch() == null;
				return pathItem.patch(new Operation()).getPatch();
			case DELETE:
				assert pathItem.getDelete() == null;
				return pathItem.delete(new Operation()).getDelete();
			case HEAD:
			case OPTIONS:
			case TRACE:
			default:
				throw new IllegalStateException();
		}
	}

	private void addFhirResourceRequestBody(Operation theOperation) {
		RequestBody requestBody = new RequestBody();
		requestBody.setContent(provideContentFhirResource());
		theOperation.setRequestBody(requestBody);
	}

	private void addResourceVersionIdParameter(Operation theOperation) {
		Parameter parameter = new Parameter();
		parameter.setName("version_id");
		parameter.setIn("path");
		parameter.setDescription("The resource version ID");
		parameter.setExample("1");
		parameter.setSchema(new Schema().type("string").minimum(new BigDecimal(1)));
		parameter.setStyle(Parameter.StyleEnum.SIMPLE);
		theOperation.addParametersItem(parameter);
	}

	private void addFhirResourceResponse(Operation theOperation) {
		theOperation.setResponses(new ApiResponses());
		ApiResponse response200 = new ApiResponse();
		response200.setDescription("Success");
		response200.setContent(provideContentFhirResource());
		theOperation.getResponses().addApiResponse("200", response200);
	}

	private Content provideContentFhirResource() {
		Content retVal = new Content();
		retVal.addMediaType(Constants.CT_FHIR_JSON_NEW, new MediaType().schema(new Schema().$ref("#/components/schemas/" + FHIR_JSON_RESOURCE)));
		retVal.addMediaType(Constants.CT_FHIR_XML_NEW, new MediaType().schema(new Schema().$ref("#/components/schemas/" + FHIR_XML_RESOURCE)));
		return retVal;
	}

	private void addResourceIdParameter(Operation theOperation) {
		Parameter parameter = new Parameter();
		parameter.setName("id");
		parameter.setIn("path");
		parameter.setDescription("The resource ID");
		parameter.setExample("123");
		parameter.setSchema(new Schema().type("string").minimum(new BigDecimal(1)));
		parameter.setStyle(Parameter.StyleEnum.SIMPLE);
		theOperation.addParametersItem(parameter);
	}

	@SuppressWarnings("unchecked")
	private static <T extends org.hl7.fhir.r4.model.Resource> T toCanonicalVersion(IBaseResource theNonCanonical) {
		IBaseResource canonical;
		if (theNonCanonical instanceof org.hl7.fhir.dstu3.model.Resource) {
			canonical = VersionConvertor_30_40.convertResource((org.hl7.fhir.dstu3.model.Resource) theNonCanonical, true);
		} else if (theNonCanonical instanceof org.hl7.fhir.r5.model.Resource) {
			canonical = VersionConvertor_40_50.convertResource((org.hl7.fhir.r5.model.Resource) theNonCanonical);
		} else {
			canonical = theNonCanonical;
		}
		return (T) canonical;
	}


}
