package ca.uhn.fhir.rest.server.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.Bindings;
import ca.uhn.fhir.rest.server.IServerConformanceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerConfiguration;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.IParameter;
import ca.uhn.fhir.rest.server.method.OperationMethodBinding;
import ca.uhn.fhir.rest.server.method.OperationMethodBinding.ReturnType;
import ca.uhn.fhir.rest.server.method.OperationParameter;
import ca.uhn.fhir.rest.server.method.SearchMethodBinding;
import ca.uhn.fhir.rest.server.method.SearchParameter;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRetriever;
import ca.uhn.fhir.util.FhirTerser;
import com.google.common.collect.TreeMultimap;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * Server FHIR Provider which serves the conformance statement for a RESTful server implementation
 * <p>
 * This class is version independent, but will only work on servers supporting FHIR R4+ (as this was
 * the first FHIR release where CapabilityStatement was a normative resource)
 */
public class ServerCapabilityStatementProvider implements IServerConformanceProvider<IBaseConformance> {

	private static final Logger ourLog = LoggerFactory.getLogger(ServerCapabilityStatementProvider.class);
	private final FhirContext myContext;
	private final RestfulServer myServer;
	private final ISearchParamRetriever mySearchParamRetriever;
	private final RestfulServerConfiguration myServerConfiguration;
	private final IValidationSupport myValidationSupport;
	private String myPublisher = "Not provided";

	/**
	 * Constructor
	 */
	public ServerCapabilityStatementProvider(RestfulServer theServer) {
		myServer = theServer;
		myContext = theServer.getFhirContext();
		mySearchParamRetriever = null;
		myServerConfiguration = null;
		myValidationSupport = null;
	}

	/**
	 * Constructor
	 */
	public ServerCapabilityStatementProvider(FhirContext theContext, RestfulServerConfiguration theServerConfiguration) {
		myContext = theContext;
		myServerConfiguration = theServerConfiguration;
		mySearchParamRetriever = null;
		myServer = null;
		myValidationSupport = null;
	}

	/**
	 * Constructor
	 */
	public ServerCapabilityStatementProvider(RestfulServer theRestfulServer, ISearchParamRetriever theSearchParamRetriever, IValidationSupport theValidationSupport) {
		myContext = theRestfulServer.getFhirContext();
		mySearchParamRetriever = theSearchParamRetriever;
		myServer = theRestfulServer;
		myServerConfiguration = null;
		myValidationSupport = theValidationSupport;
	}

	private void checkBindingForSystemOps(FhirTerser theTerser, IBase theRest, Set<String> theSystemOps, BaseMethodBinding<?> theMethodBinding) {
		RestOperationTypeEnum restOperationType = theMethodBinding.getRestOperationType();
		if (restOperationType.isSystemLevel()) {
			String sysOp = restOperationType.getCode();
			if (theSystemOps.contains(sysOp) == false) {
				theSystemOps.add(sysOp);
				IBase interaction = theTerser.addElement(theRest, "interaction");
				theTerser.addElement(interaction, "code", sysOp);
			}
		}
	}


	private String conformanceDate(RestfulServerConfiguration theServerConfiguration) {
		IPrimitiveType<Date> buildDate = theServerConfiguration.getConformanceDate();
		if (buildDate != null && buildDate.getValue() != null) {
			try {
				return buildDate.getValueAsString();
			} catch (DataFormatException e) {
				// fall through
			}
		}
		return InstantDt.withCurrentTime().getValueAsString();
	}

	private RestfulServerConfiguration getServerConfiguration() {
		if (myServer != null) {
			return myServer.createConfiguration();
		}
		return myServerConfiguration;
	}


	/**
	 * Gets the value of the "publisher" that will be placed in the generated conformance statement. As this is a mandatory element, the value should not be null (although this is not enforced). The
	 * value defaults to "Not provided" but may be set to null, which will cause this element to be omitted.
	 */
	public String getPublisher() {
		return myPublisher;
	}

	/**
	 * Sets the value of the "publisher" that will be placed in the generated conformance statement. As this is a mandatory element, the value should not be null (although this is not enforced). The
	 * value defaults to "Not provided" but may be set to null, which will cause this element to be omitted.
	 */
	public void setPublisher(String thePublisher) {
		myPublisher = thePublisher;
	}

	@Override
	@Metadata
	public IBaseConformance getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails) {

		HttpServletRequest servletRequest = null;
		if (theRequestDetails instanceof ServletRequestDetails) {
			servletRequest = ((ServletRequestDetails) theRequestDetails).getServletRequest();
		}

		RestfulServerConfiguration configuration = getServerConfiguration();
		Bindings bindings = configuration.provideBindings();

		IBaseConformance retVal = (IBaseConformance) myContext.getResourceDefinition("CapabilityStatement").newInstance();

		FhirTerser terser = myContext.newTerser();

		TreeMultimap<String, String> resourceTypeToSupportedProfiles = getSupportedProfileMultimap(terser);

		terser.addElement(retVal, "name", "RestServer");
		terser.addElement(retVal, "publisher", myPublisher);
		terser.addElement(retVal, "date", conformanceDate(configuration));
		terser.addElement(retVal, "fhirVersion", myContext.getVersion().getVersion().getFhirVersionString());

		ServletContext servletContext = (ServletContext) (theRequest == null ? null : theRequest.getAttribute(RestfulServer.SERVLET_CONTEXT_ATTRIBUTE));
		String serverBase = configuration.getServerAddressStrategy().determineServerBase(servletContext, theRequest);
		terser.addElement(retVal, "implementation.url", serverBase);
		terser.addElement(retVal, "implementation.description", configuration.getImplementationDescription());
		terser.addElement(retVal, "kind", "instance");
		terser.addElement(retVal, "software.name", configuration.getServerName());
		terser.addElement(retVal, "software.version", configuration.getServerVersion());
		terser.addElement(retVal, "format", Constants.CT_FHIR_XML_NEW);
		terser.addElement(retVal, "format", Constants.CT_FHIR_JSON_NEW);
		terser.addElement(retVal, "format", Constants.FORMAT_JSON);
		terser.addElement(retVal, "format", Constants.FORMAT_XML);
		terser.addElement(retVal, "status", "active");

		IBase rest = terser.addElement(retVal, "rest");
		terser.addElement(rest, "mode", "server");

		Set<String> systemOps = new HashSet<>();
		Set<String> operationNames = new HashSet<>();

		Map<String, List<BaseMethodBinding<?>>> resourceToMethods = configuration.collectMethodBindings();
		Map<String, Class<? extends IBaseResource>> resourceNameToSharedSupertype = configuration.getNameToSharedSupertype();

		for (Entry<String, List<BaseMethodBinding<?>>> nextEntry : resourceToMethods.entrySet()) {

			if (nextEntry.getKey().isEmpty() == false) {
				Set<String> resourceOps = new HashSet<>();
				Set<String> resourceIncludes = new HashSet<>();
				IBase resource = terser.addElement(rest, "resource");
				String resourceName = nextEntry.getKey();

				postProcessRestResource(terser, resource, resourceName);

				RuntimeResourceDefinition def;
				FhirContext context = configuration.getFhirContext();
				if (resourceNameToSharedSupertype.containsKey(resourceName)) {
					def = context.getResourceDefinition(resourceNameToSharedSupertype.get(resourceName));
				} else {
					def = context.getResourceDefinition(resourceName);
				}
				terser.addElement(resource, "type", def.getName());
				terser.addElement(resource, "profile", def.getResourceProfile(serverBase));

				for (BaseMethodBinding<?> nextMethodBinding : nextEntry.getValue()) {
					RestOperationTypeEnum resOpCode = nextMethodBinding.getRestOperationType();
					if (resOpCode.isTypeLevel() || resOpCode.isInstanceLevel()) {
						String resOp;
						resOp = resOpCode.getCode();
						if (resourceOps.contains(resOp) == false) {
							resourceOps.add(resOp);
							IBase interaction = terser.addElement(resource, "interaction");
							terser.addElement(interaction, "code", resOp);
						}
						if (RestOperationTypeEnum.VREAD.equals(resOpCode)) {
							// vread implies read
							resOp = "read";
							if (resourceOps.contains(resOp) == false) {
								resourceOps.add(resOp);
								IBase interaction = terser.addElement(resource, "interaction");
								terser.addElement(interaction, "code", resOp);
							}
						}
					}

					if (nextMethodBinding.isSupportsConditional()) {
						switch (resOpCode) {
							case CREATE:
								terser.addElement(resource, "conditionalCreate", "true");
								break;
							case DELETE:
								if (nextMethodBinding.isSupportsConditionalMultiple()) {
									terser.setElement(resource, "conditionalDelete", "multiple");
								} else {
									terser.setElement(resource, "conditionalDelete", "single");
								}
								break;
							case UPDATE:
								terser.addElement(resource, "conditionalUpdate", "true");
								break;
							default:
								break;
						}
					}

					checkBindingForSystemOps(terser, rest, systemOps, nextMethodBinding);

					if (nextMethodBinding instanceof SearchMethodBinding) {
						SearchMethodBinding methodBinding = (SearchMethodBinding) nextMethodBinding;
						if (methodBinding.getQueryName() != null) {
							String queryName = bindings.getNamedSearchMethodBindingToName().get(methodBinding);
							if (operationNames.add(queryName)) {
								IBase operation = terser.addElement(rest, "operation");
								terser.addElement(operation, "name", methodBinding.getQueryName());
								terser.addElement(operation, "definition", (getOperationDefinitionPrefix(theRequestDetails) + "OperationDefinition/" + queryName));
							}
						} else {

							resourceIncludes.addAll(methodBinding.getIncludes());

						}
					} else if (nextMethodBinding instanceof OperationMethodBinding) {
						OperationMethodBinding methodBinding = (OperationMethodBinding) nextMethodBinding;
						String opName = bindings.getOperationBindingToName().get(methodBinding);
						// Only add each operation (by name) once
						if (operationNames.add(opName)) {
							IBase operation = terser.addElement(rest, "operation");
							terser.addElement(operation, "name", methodBinding.getName().substring(1));
							terser.addElement(operation, "definition", getOperationDefinitionPrefix(theRequestDetails) + "OperationDefinition/" + opName);
						}
					}

				}

				ISearchParamRetriever searchParamRetriever = mySearchParamRetriever;
				if (searchParamRetriever == null && myServerConfiguration != null) {
					searchParamRetriever = myServerConfiguration;
				} else if (searchParamRetriever == null) {
					searchParamRetriever = myServer.createConfiguration();
				}

				Map<String, RuntimeSearchParam> searchParams = searchParamRetriever.getActiveSearchParams(resourceName);
				if (searchParams != null) {
					for (RuntimeSearchParam next : searchParams.values()) {
						IBase searchParam = terser.addElement(resource, "searchParam");
						terser.addElement(searchParam, "name", next.getName());
						terser.addElement(searchParam, "type", next.getParamType().getCode());
						if (isNotBlank(next.getDescription())) {
							terser.addElement(searchParam, "documentation", next.getDescription());
						}

						String spUri = next.getUri();
						if (isBlank(spUri) && servletRequest != null) {
							String id;
							if (next.getId() != null) {
								id = next.getId().toUnqualifiedVersionless().getValue();
							} else {
								id = resourceName + "-" + next.getName();
							}
							spUri = configuration.getServerAddressStrategy().determineServerBase(servletRequest.getServletContext(), servletRequest) + "/" + id;
						}
						if (isNotBlank(spUri)) {
							terser.addElement(searchParam, "definition", spUri);
						}
					}

					if (resourceIncludes.isEmpty()) {
						for (String nextInclude : searchParams.values().stream().filter(t -> t.getParamType() == RestSearchParameterTypeEnum.REFERENCE).map(t -> t.getName()).sorted().collect(Collectors.toList())) {
							terser.addElement(resource, "searchInclude", nextInclude);
						}
					}

				}

				for (String supportedProfile : resourceTypeToSupportedProfiles.get(resourceName)) {
					terser.addElement(resource, "supportedProfile", supportedProfile);
				}

				for (String resourceInclude : resourceIncludes) {
					terser.addElement(resource, "searchInclude", resourceInclude);
				}

			} else {
				for (BaseMethodBinding<?> nextMethodBinding : nextEntry.getValue()) {
					checkBindingForSystemOps(terser, rest, systemOps, nextMethodBinding);
					if (nextMethodBinding instanceof OperationMethodBinding) {
						OperationMethodBinding methodBinding = (OperationMethodBinding) nextMethodBinding;
						String opName = bindings.getOperationBindingToName().get(methodBinding);
						if (operationNames.add(opName)) {
							ourLog.debug("Found bound operation: {}", opName);
							IBase operation = terser.addElement(rest, "operation");
							terser.addElement(operation, "name", methodBinding.getName().substring(1));
							terser.addElement(operation, "definition", getOperationDefinitionPrefix(theRequestDetails) + "OperationDefinition/" + opName);
						}
					}
				}
			}

			postProcessRest(terser, rest);

		}

		postProcess(terser, retVal);

		return retVal;
	}

	private TreeMultimap<String, String> getSupportedProfileMultimap(FhirTerser terser) {
		TreeMultimap<String, String> resourceTypeToSupportedProfiles = TreeMultimap.create();
		if (myValidationSupport != null) {
			List<IBaseResource> allStructureDefinitions = myValidationSupport.fetchAllStructureDefinitions();
			if (allStructureDefinitions != null) {
				for (IBaseResource next : allStructureDefinitions) {
					String kind = terser.getSinglePrimitiveValueOrNull(next, "kind");
					String url = terser.getSinglePrimitiveValueOrNull(next, "url");
					if ("resource".equals(kind) && isNotBlank(url)) {

						// Don't declare support for the base profiles, that's not useful
						if (url.startsWith("http://hl7.org/fhir/StructureDefinition")) {
							continue;
						}

						String resourceType = terser.getSinglePrimitiveValueOrNull(next, "snapshot.element.path");
						if (isBlank(resourceType)) {
							next = myValidationSupport.generateSnapshot(new ValidationSupportContext(myValidationSupport), next, null, null, null);
							if (next != null) {
								resourceType = terser.getSinglePrimitiveValueOrNull(next, "snapshot.element.path");
							}
						}

						if (isNotBlank(resourceType)) {
							resourceTypeToSupportedProfiles.put(resourceType, url);
						}
					}
				}
			}
		}
		return resourceTypeToSupportedProfiles;
	}

	/**
	 * Subclasses may override
	 */
	protected void postProcess(FhirTerser theTerser, IBaseConformance theCapabilityStatement) {
		// nothing
	}

	/**
	 * Subclasses may override
	 */
	protected void postProcessRest(FhirTerser theTerser, IBase theRest) {
		// nothing
	}

	/**
	 * Subclasses may override
	 */
	protected void postProcessRestResource(FhirTerser theTerser, IBase theResource, String theResourceName) {
		// nothing
	}

	protected String getOperationDefinitionPrefix(RequestDetails theRequestDetails) {
		if (theRequestDetails == null) {
			return "";
		}
		return theRequestDetails.getServerBaseForRequest() + "/";
	}


	@Read(typeName = "OperationDefinition")
	public IBaseResource readOperationDefinition(@IdParam IIdType theId, RequestDetails theRequestDetails) {
		if (theId == null || theId.hasIdPart() == false) {
			throw new ResourceNotFoundException(theId);
		}
		RestfulServerConfiguration configuration = getServerConfiguration();
		Bindings bindings = configuration.provideBindings();

		List<OperationMethodBinding> operationBindings = bindings.getOperationNameToBindings().get(theId.getIdPart());
		if (operationBindings != null && !operationBindings.isEmpty()) {
			return readOperationDefinitionForOperation(operationBindings);
		}
		List<SearchMethodBinding> searchBindings = bindings.getSearchNameToBindings().get(theId.getIdPart());
		if (searchBindings != null && !searchBindings.isEmpty()) {
			return readOperationDefinitionForNamedSearch(searchBindings);
		}
		throw new ResourceNotFoundException(theId);
	}

	private IBaseResource readOperationDefinitionForNamedSearch(List<SearchMethodBinding> bindings) {
		IBaseResource op = myContext.getResourceDefinition("OperationDefinition").newInstance();
		FhirTerser terser = myContext.newTerser();

		terser.addElement(op, "status", "active");
		terser.addElement(op, "kind", "query");
		terser.addElement(op, "affectsState", "false");

		terser.addElement(op, "instance", "false");

		Set<String> inParams = new HashSet<>();

		String operationCode = null;
		for (SearchMethodBinding binding : bindings) {
			if (isNotBlank(binding.getDescription())) {
				terser.addElement(op, "description", binding.getDescription());
			}
			if (isBlank(binding.getResourceProviderResourceName())) {
				terser.addElement(op, "system", "true");
				terser.addElement(op, "type", "false");
			} else {
				terser.addElement(op, "system", "false");
				terser.addElement(op, "type", "true");
				terser.addElement(op, "resource", binding.getResourceProviderResourceName());
			}

			if (operationCode == null) {
				operationCode = binding.getQueryName();
			}

			for (IParameter nextParamUntyped : binding.getParameters()) {
				if (nextParamUntyped instanceof SearchParameter) {
					SearchParameter nextParam = (SearchParameter) nextParamUntyped;
					if (!inParams.add(nextParam.getName())) {
						continue;
					}

					IBase param = terser.addElement(op, "parameter");
					terser.addElement(param, "use", "in");
					terser.addElement(param, "type", "string");
					terser.addElement(param, "searchType", nextParam.getParamType().getCode());
					terser.addElement(param, "min", nextParam.isRequired() ? "1" : "0");
					terser.addElement(param, "max", "1");
					terser.addElement(param, "name", nextParam.getName());
				}
			}

		}

		terser.addElement(op, "code", operationCode);
		terser.addElement(op, "name", "Search_" + operationCode);

		return op;
	}

	private IBaseResource readOperationDefinitionForOperation(List<OperationMethodBinding> bindings) {
		IBaseResource op = myContext.getResourceDefinition("OperationDefinition").newInstance();
		FhirTerser terser = myContext.newTerser();

		terser.addElement(op, "status", "active");
		terser.addElement(op, "kind", "operation");

		boolean systemLevel = false;
		boolean typeLevel = false;
		boolean instanceLevel = false;
		boolean affectsState = false;
		String description = null;
		String code = null;
		String name;

		Set<String> resourceNames = new TreeSet<>();
		Set<String> inParams = new HashSet<>();
		Set<String> outParams = new HashSet<>();

		for (OperationMethodBinding sharedDescription : bindings) {
			if (isNotBlank(sharedDescription.getDescription()) && isBlank(description)) {
				description = sharedDescription.getDescription();
			}
			if (sharedDescription.isCanOperateAtInstanceLevel()) {
				instanceLevel = true;
			}
			if (sharedDescription.isCanOperateAtServerLevel()) {
				systemLevel = true;
			}
			if (sharedDescription.isCanOperateAtTypeLevel()) {
				typeLevel = true;
			}
			if (!sharedDescription.isIdempotent()) {
				affectsState |= true;
			}

			code = sharedDescription.getName().substring(1);

			if (isNotBlank(sharedDescription.getResourceName())) {
				resourceNames.add(sharedDescription.getResourceName());
			}

			for (IParameter nextParamUntyped : sharedDescription.getParameters()) {
				if (nextParamUntyped instanceof OperationParameter) {
					OperationParameter nextParam = (OperationParameter) nextParamUntyped;
					if (!inParams.add(nextParam.getName())) {
						continue;
					}
					IBase param = terser.addElement(op, "parameter");
					terser.addElement(param, "use", "in");
					if (nextParam.getParamType() != null) {
						terser.addElement(param, "type", nextParam.getParamType());
					}
					if (nextParam.getSearchParamType() != null) {
						terser.addElement(param, "searchType", nextParam.getSearchParamType());
					}
					terser.addElement(param, "min", Integer.toString(nextParam.getMin()));
					terser.addElement(param, "max", (nextParam.getMax() == -1 ? "*" : Integer.toString(nextParam.getMax())));
					terser.addElement(param, "name", nextParam.getName());
				}
			}

			for (ReturnType nextParam : sharedDescription.getReturnParams()) {
				if (!outParams.add(nextParam.getName())) {
					continue;
				}
				IBase param = terser.addElement(op, "parameter");
				terser.addElement(param, "use", "out");
				if (nextParam.getType() != null) {
					terser.addElement(param, "type", nextParam.getType());
				}
				terser.addElement(param, "min", Integer.toString(nextParam.getMin()));
				terser.addElement(param, "max", (nextParam.getMax() == -1 ? "*" : Integer.toString(nextParam.getMax())));
				terser.addElement(param, "name", nextParam.getName());
			}
		}

		name = "Operation_" + code;

		terser.addElements(op, "resource", resourceNames);
		terser.addElement(op, "name", name);
		terser.addElement(op, "code", code);
		terser.addElement(op, "description", description);
		terser.addElement(op, "affectsState", Boolean.toString(affectsState));
		terser.addElement(op, "system", Boolean.toString(systemLevel));
		terser.addElement(op, "type", Boolean.toString(typeLevel));
		terser.addElement(op, "instance", Boolean.toString(instanceLevel));

		return op;
	}

	@Override
	public void setRestfulServer(RestfulServer theRestfulServer) {
		// ignore
	}

}
