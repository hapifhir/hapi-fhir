package ca.uhn.fhir.rest.server.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.Msg;
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
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.HapiExtensions;
import com.google.common.collect.TreeMultimap;
import org.apache.commons.text.WordUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

	public static final boolean DEFAULT_REST_RESOURCE_REV_INCLUDES_ENABLED = true;
	private static final Logger ourLog = LoggerFactory.getLogger(ServerCapabilityStatementProvider.class);
	private final FhirContext myContext;
	private final RestfulServer myServer;
	private final ISearchParamRegistry mySearchParamRegistry;
	private final RestfulServerConfiguration myServerConfiguration;
	private final IValidationSupport myValidationSupport;
	private String myPublisher = "Not provided";
	private boolean myRestResourceRevIncludesEnabled = DEFAULT_REST_RESOURCE_REV_INCLUDES_ENABLED;

	/**
	 * Constructor
	 */
	public ServerCapabilityStatementProvider(RestfulServer theServer) {
		myServer = theServer;
		myContext = theServer.getFhirContext();
		mySearchParamRegistry = null;
		myServerConfiguration = null;
		myValidationSupport = null;
	}

	/**
	 * Constructor
	 */
	public ServerCapabilityStatementProvider(FhirContext theContext, RestfulServerConfiguration theServerConfiguration) {
		myContext = theContext;
		myServerConfiguration = theServerConfiguration;
		mySearchParamRegistry = null;
		myServer = null;
		myValidationSupport = null;
	}

	/**
	 * Constructor
	 */
	public ServerCapabilityStatementProvider(RestfulServer theRestfulServer, ISearchParamRegistry theSearchParamRegistry, IValidationSupport theValidationSupport) {
		myContext = theRestfulServer.getFhirContext();
		mySearchParamRegistry = theSearchParamRegistry;
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

		terser.addElement(retVal, "id", UUID.randomUUID().toString());
		terser.addElement(retVal, "name", "RestServer");
		terser.addElement(retVal, "publisher", myPublisher);
		terser.addElement(retVal, "date", conformanceDate(configuration));
		terser.addElement(retVal, "fhirVersion", myContext.getVersion().getVersion().getFhirVersionString());

		ServletContext servletContext = (ServletContext) (theRequest == null ? null : theRequest.getAttribute(RestfulServer.SERVLET_CONTEXT_ATTRIBUTE));
		String serverBase = configuration.getServerAddressStrategy().determineServerBase(servletContext, theRequest);
		terser.addElement(retVal, "implementation.url", serverBase);
		terser.addElement(retVal, "implementation.description", configuration.getImplementationDescription());
		terser.addElement(retVal, "kind", "instance");
		if (myServer != null && isNotBlank(myServer.getCopyright())) {
			terser.addElement(retVal, "copyright", myServer.getCopyright());
		}
		terser.addElement(retVal, "software.name", configuration.getServerName());
		terser.addElement(retVal, "software.version", configuration.getServerVersion());
		if (myContext.isFormatXmlSupported()) {
			terser.addElement(retVal, "format", Constants.CT_FHIR_XML_NEW);
			terser.addElement(retVal, "format", Constants.FORMAT_XML);
		}
		if (myContext.isFormatJsonSupported()) {
			terser.addElement(retVal, "format", Constants.CT_FHIR_JSON_NEW);
			terser.addElement(retVal, "format", Constants.FORMAT_JSON);
		}
		if (myContext.isFormatRdfSupported()) {
			terser.addElement(retVal, "format", Constants.CT_RDF_TURTLE);
			terser.addElement(retVal, "format", Constants.FORMAT_TURTLE);
		}
		terser.addElement(retVal, "status", "active");

		IBase rest = terser.addElement(retVal, "rest");
		terser.addElement(rest, "mode", "server");

		Set<String> systemOps = new HashSet<>();

		Map<String, List<BaseMethodBinding<?>>> resourceToMethods = configuration.collectMethodBindings();
		Map<String, Class<? extends IBaseResource>> resourceNameToSharedSupertype = configuration.getNameToSharedSupertype();
		List<BaseMethodBinding<?>> globalMethodBindings = configuration.getGlobalBindings();

		TreeMultimap<String, String> resourceNameToIncludes = TreeMultimap.create();
		TreeMultimap<String, String> resourceNameToRevIncludes = TreeMultimap.create();
		for (Entry<String, List<BaseMethodBinding<?>>> nextEntry : resourceToMethods.entrySet()) {
			String resourceName = nextEntry.getKey();
			for (BaseMethodBinding<?> nextMethod : nextEntry.getValue()) {
				if (nextMethod instanceof SearchMethodBinding) {
					resourceNameToIncludes.putAll(resourceName, nextMethod.getIncludes());
					resourceNameToRevIncludes.putAll(resourceName, nextMethod.getRevIncludes());
				}
			}

		}

		for (Entry<String, List<BaseMethodBinding<?>>> nextEntry : resourceToMethods.entrySet()) {

			Set<String> operationNames = new HashSet<>();
			String resourceName = nextEntry.getKey();
			if (nextEntry.getKey().isEmpty() == false) {
				Set<String> resourceOps = new HashSet<>();
				IBase resource = terser.addElement(rest, "resource");

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
								terser.setElement(resource, "conditionalCreate", "true");
								break;
							case DELETE:
								if (nextMethodBinding.isSupportsConditionalMultiple()) {
									terser.setElement(resource, "conditionalDelete", "multiple");
								} else {
									terser.setElement(resource, "conditionalDelete", "single");
								}
								break;
							case UPDATE:
								terser.setElement(resource, "conditionalUpdate", "true");
								break;
							case HISTORY_INSTANCE:
							case HISTORY_SYSTEM:
							case HISTORY_TYPE:
							case READ:
							case SEARCH_SYSTEM:
							case SEARCH_TYPE:
							case TRANSACTION:
							case VALIDATE:
							case VREAD:
							case METADATA:
							case META_ADD:
							case META:
							case META_DELETE:
							case PATCH:
							case BATCH:
							case ADD_TAGS:
							case DELETE_TAGS:
							case GET_TAGS:
							case GET_PAGE:
							case GRAPHQL_REQUEST:
							case EXTENDED_OPERATION_SERVER:
							case EXTENDED_OPERATION_TYPE:
							case EXTENDED_OPERATION_INSTANCE:
							default:
								break;
						}
					}

					checkBindingForSystemOps(terser, rest, systemOps, nextMethodBinding);

					// Resource Operations
					if (nextMethodBinding instanceof SearchMethodBinding) {
						addSearchMethodIfSearchIsNamedQuery(theRequestDetails, bindings, terser, operationNames, resource, (SearchMethodBinding) nextMethodBinding);
					} else if (nextMethodBinding instanceof OperationMethodBinding) {
						OperationMethodBinding methodBinding = (OperationMethodBinding) nextMethodBinding;
						String opName = bindings.getOperationBindingToId().get(methodBinding);
						// Only add each operation (by name) once
						if (operationNames.add(opName)) {
							IBase operation = terser.addElement(resource, "operation");
							populateOperation(theRequestDetails, terser, methodBinding, opName, operation);
						}
					}

				}

				// Find any global operations (Operations defines at the system level but with the
				// global flag set to true, meaning they apply to all resource types)
				if (globalMethodBindings != null) {
					Set<String> globalOperationNames = new HashSet<>();
					for (BaseMethodBinding<?> next : globalMethodBindings) {
						if (next instanceof OperationMethodBinding) {
							OperationMethodBinding methodBinding = (OperationMethodBinding) next;
							if (methodBinding.isGlobalMethod()) {
								if (methodBinding.isCanOperateAtInstanceLevel() || methodBinding.isCanOperateAtTypeLevel()) {
									String opName = bindings.getOperationBindingToId().get(methodBinding);
									// Only add each operation (by name) once
									if (globalOperationNames.add(opName)) {
										IBase operation = terser.addElement(resource, "operation");
										populateOperation(theRequestDetails, terser, methodBinding, opName, operation);
									}
								}
							}
						}
					}
				}

				ISearchParamRegistry serverConfiguration;
				if (myServerConfiguration != null) {
					serverConfiguration = myServerConfiguration;
				} else {
					serverConfiguration = myServer.createConfiguration();
				}

				/*
				 * If we have an explicit registry (which will be the case in the JPA server) we use it as priority,
				 * but also fill in any gaps using params from the server itself. This makes sure we include
				 * global params like _lastUpdated
				 */
				ResourceSearchParams searchParams;
				ISearchParamRegistry searchParamRegistry;
				ResourceSearchParams serverConfigurationActiveSearchParams = serverConfiguration.getActiveSearchParams(resourceName);
				if (mySearchParamRegistry != null) {
					searchParamRegistry = mySearchParamRegistry;
					searchParams = mySearchParamRegistry.getActiveSearchParams(resourceName).makeCopy();
					for (String nextBuiltInSpName : serverConfigurationActiveSearchParams.getSearchParamNames()) {
						if (nextBuiltInSpName.startsWith("_") &&
							!searchParams.containsParamName(nextBuiltInSpName) &&
							searchParamEnabled(nextBuiltInSpName)) {
							searchParams.put(nextBuiltInSpName, serverConfigurationActiveSearchParams.get(nextBuiltInSpName));
						}
					}
				} else {
					searchParamRegistry = serverConfiguration;
					searchParams = serverConfigurationActiveSearchParams;
				}


				for (RuntimeSearchParam next : searchParams.values()) {
					IBase searchParam = terser.addElement(resource, "searchParam");
					terser.addElement(searchParam, "name", next.getName());
					terser.addElement(searchParam, "type", next.getParamType().getCode());
					if (isNotBlank(next.getDescription())) {
						terser.addElement(searchParam, "documentation", next.getDescription());
					}

					String spUri = next.getUri();
					
					if (isNotBlank(spUri)) {
						terser.addElement(searchParam, "definition", spUri);
					}
				}

				// Add Include to CapabilityStatement.rest.resource
				NavigableSet<String> resourceIncludes = resourceNameToIncludes.get(resourceName);
				if (resourceIncludes.isEmpty()) {
					List<String> includes = searchParams
						.values()
						.stream()
						.filter(t -> t.getParamType() == RestSearchParameterTypeEnum.REFERENCE)
						.map(t -> resourceName + ":" + t.getName())
						.sorted()
						.collect(Collectors.toList());
					terser.addElement(resource, "searchInclude", "*");
					for (String nextInclude : includes) {
						terser.addElement(resource, "searchInclude", nextInclude);
					}
				} else {
					for (String resourceInclude : resourceIncludes) {
						terser.addElement(resource, "searchInclude", resourceInclude);
					}
				}

				// Add RevInclude to CapabilityStatement.rest.resource
				if (myRestResourceRevIncludesEnabled) {
					NavigableSet<String> resourceRevIncludes = resourceNameToRevIncludes.get(resourceName);
					if (resourceRevIncludes.isEmpty()) {
						TreeSet<String> revIncludes = new TreeSet<>();
						for (String nextResourceName : resourceToMethods.keySet()) {
							if (isBlank(nextResourceName)) {
								continue;
							}

							for (RuntimeSearchParam t : searchParamRegistry.getActiveSearchParams(nextResourceName).values()) {
								if (t.getParamType() == RestSearchParameterTypeEnum.REFERENCE) {
									if (isNotBlank(t.getName())) {
										boolean appropriateTarget = false;
										if (t.getTargets().contains(resourceName) || t.getTargets().isEmpty()) {
											appropriateTarget = true;
										}

										if (appropriateTarget) {
											revIncludes.add(nextResourceName + ":" + t.getName());
										}
									}
								}
							}
						}
						for (String nextInclude : revIncludes) {
							terser.addElement(resource, "searchRevInclude", nextInclude);
						}
					} else {
						for (String resourceInclude : resourceRevIncludes) {
							terser.addElement(resource, "searchRevInclude", resourceInclude);
						}
					}
				}

				// Add SupportedProfile to CapabilityStatement.rest.resource
				for (String supportedProfile : resourceTypeToSupportedProfiles.get(resourceName)) {
					terser.addElement(resource, "supportedProfile", supportedProfile);
				}

			} else {
				for (BaseMethodBinding<?> nextMethodBinding : nextEntry.getValue()) {
					checkBindingForSystemOps(terser, rest, systemOps, nextMethodBinding);
					if (nextMethodBinding instanceof OperationMethodBinding) {
						OperationMethodBinding methodBinding = (OperationMethodBinding) nextMethodBinding;
						if (!methodBinding.isGlobalMethod()) {
							String opName = bindings.getOperationBindingToId().get(methodBinding);
							if (operationNames.add(opName)) {
								ourLog.debug("Found bound operation: {}", opName);
								IBase operation = terser.addElement(rest, "operation");
								populateOperation(theRequestDetails, terser, methodBinding, opName, operation);
							}
						}
					} else if (nextMethodBinding instanceof SearchMethodBinding) {
						addSearchMethodIfSearchIsNamedQuery(theRequestDetails, bindings, terser, operationNames, rest, (SearchMethodBinding) nextMethodBinding);
					}
				}
			}

		}


		// Find any global operations (Operations defines at the system level but with the
		// global flag set to true, meaning they apply to all resource types)
		if (globalMethodBindings != null) {
			Set<String> globalOperationNames = new HashSet<>();
			for (BaseMethodBinding<?> next : globalMethodBindings) {
				if (next instanceof OperationMethodBinding) {
					OperationMethodBinding methodBinding = (OperationMethodBinding) next;
					if (methodBinding.isGlobalMethod()) {
						if (methodBinding.isCanOperateAtServerLevel()) {
							String opName = bindings.getOperationBindingToId().get(methodBinding);
							// Only add each operation (by name) once
							if (globalOperationNames.add(opName)) {
								IBase operation = terser.addElement(rest, "operation");
								populateOperation(theRequestDetails, terser, methodBinding, opName, operation);
							}
						}
					}
				}
			}
		}


		postProcessRest(terser, rest);
		postProcess(terser, retVal);

		return retVal;
	}

	/**
	 *
	 * @param theSearchParam
	 * @return true if theSearchParam is enabled on this server
	 */
	protected boolean searchParamEnabled(String theSearchParam) {
		return true;
	}

	private void addSearchMethodIfSearchIsNamedQuery(RequestDetails theRequestDetails, Bindings theBindings, FhirTerser theTerser, Set<String> theOperationNamesAlreadyAdded, IBase theElementToAddTo, SearchMethodBinding theSearchMethodBinding) {
		if (theSearchMethodBinding.getQueryName() != null) {
			String queryName = theBindings.getNamedSearchMethodBindingToName().get(theSearchMethodBinding);
			if (theOperationNamesAlreadyAdded.add(queryName)) {
				IBase operation = theTerser.addElement(theElementToAddTo, "operation");
				theTerser.addElement(operation, "name", theSearchMethodBinding.getQueryName());
				theTerser.addElement(operation, "definition", (createOperationUrl(theRequestDetails, queryName)));
			}
		}
	}

	private void populateOperation(RequestDetails theRequestDetails, FhirTerser theTerser, OperationMethodBinding theMethodBinding, String theOpName, IBase theOperation) {
		String operationName = theMethodBinding.getName().substring(1);
		theTerser.addElement(theOperation, "name", operationName);
		theTerser.addElement(theOperation, "definition", createOperationUrl(theRequestDetails, theOpName));
		if (isNotBlank(theMethodBinding.getDescription())) {
			theTerser.addElement(theOperation, "documentation", theMethodBinding.getDescription());
		}
	}

	@Nonnull
	private String createOperationUrl(RequestDetails theRequestDetails, String theOpName) {
		return getOperationDefinitionPrefix(theRequestDetails) + "OperationDefinition/" + theOpName;
	}

	private TreeMultimap<String, String> getSupportedProfileMultimap(FhirTerser terser) {
		TreeMultimap<String, String> resourceTypeToSupportedProfiles = TreeMultimap.create();
		if (myValidationSupport != null) {
			List<IBaseResource> allStructureDefinitions = myValidationSupport.fetchAllNonBaseStructureDefinitions();
			if (allStructureDefinitions != null) {
				for (IBaseResource next : allStructureDefinitions) {
					String kind = terser.getSinglePrimitiveValueOrNull(next, "kind");
					String url = terser.getSinglePrimitiveValueOrNull(next, "url");
					String baseDefinition = defaultString(terser.getSinglePrimitiveValueOrNull(next, "baseDefinition"));
					if ("resource".equals(kind) && isNotBlank(url)) {

						// Don't include the base resource definitions in the supported profile list - This isn't helpful
						if (baseDefinition.equals("http://hl7.org/fhir/StructureDefinition/DomainResource") || baseDefinition.equals("http://hl7.org/fhir/StructureDefinition/Resource")) {
							continue;
						}

						String resourceType = terser.getSinglePrimitiveValueOrNull(next, "snapshot.element.path");
						if (isBlank(resourceType)) {
							resourceType = terser.getSinglePrimitiveValueOrNull(next, "differential.element.path");
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


	@Override
	@Read(typeName = "OperationDefinition")
	public IBaseResource readOperationDefinition(@IdParam IIdType theId, RequestDetails theRequestDetails) {
		if (theId == null || theId.hasIdPart() == false) {
			throw new ResourceNotFoundException(Msg.code(1977) + theId);
		}
		RestfulServerConfiguration configuration = getServerConfiguration();
		Bindings bindings = configuration.provideBindings();

		List<OperationMethodBinding> operationBindings = bindings.getOperationIdToBindings().get(theId.getIdPart());
		if (operationBindings != null && !operationBindings.isEmpty()) {
			return readOperationDefinitionForOperation(theRequestDetails, bindings, operationBindings);
		}

		List<SearchMethodBinding> searchBindings = bindings.getSearchNameToBindings().get(theId.getIdPart());
		if (searchBindings != null && !searchBindings.isEmpty()) {
			return readOperationDefinitionForNamedSearch(searchBindings);
		}
		throw new ResourceNotFoundException(Msg.code(1978) + theId);
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

		String operationName = WordUtils.capitalize(operationCode);
		terser.addElement(op, "name", operationName);

		return op;
	}

	private IBaseResource readOperationDefinitionForOperation(RequestDetails theRequestDetails, Bindings theBindings, List<OperationMethodBinding> theOperationMethodBindings) {
		IBaseResource op = myContext.getResourceDefinition("OperationDefinition").newInstance();
		FhirTerser terser = myContext.newTerser();

		terser.addElement(op, "status", "active");
		terser.addElement(op, "kind", "operation");

		boolean systemLevel = false;
		boolean typeLevel = false;
		boolean instanceLevel = false;
		boolean affectsState = false;
		String description = null;
		String title = null;
		String code = null;
		String url = null;

		Set<String> resourceNames = new TreeSet<>();
		Map<String, IBase> inParams = new HashMap<>();
		Map<String, IBase> outParams = new HashMap<>();

		for (OperationMethodBinding operationMethodBinding : theOperationMethodBindings) {
			if (isNotBlank(operationMethodBinding.getDescription()) && isBlank(description)) {
				description = operationMethodBinding.getDescription();
			}
			if (isNotBlank(operationMethodBinding.getShortDescription()) && isBlank(title)) {
				title = operationMethodBinding.getShortDescription();
			}
			if (operationMethodBinding.isCanOperateAtInstanceLevel()) {
				instanceLevel = true;
			}
			if (operationMethodBinding.isCanOperateAtServerLevel()) {
				systemLevel = true;
			}
			if (operationMethodBinding.isCanOperateAtTypeLevel()) {
				typeLevel = true;
			}
			if (!operationMethodBinding.isIdempotent()) {
				affectsState |= true;
			}

			code = operationMethodBinding.getName().substring(1);

			if (isNotBlank(operationMethodBinding.getResourceName())) {
				resourceNames.add(operationMethodBinding.getResourceName());
			}

			if (isBlank(url)) {
				url = theBindings.getOperationBindingToId().get(operationMethodBinding);
				if (isNotBlank(url)) {
					url = createOperationUrl(theRequestDetails, url);
				}
			}


			for (IParameter nextParamUntyped : operationMethodBinding.getParameters()) {
				if (nextParamUntyped instanceof OperationParameter) {
					OperationParameter nextParam = (OperationParameter) nextParamUntyped;

					IBase param = inParams.get(nextParam.getName());
					if (param == null){
						param = terser.addElement(op, "parameter");
						inParams.put(nextParam.getName(), param);
					}

					IBase existingParam = inParams.get(nextParam.getName());
					if (isNotBlank(nextParam.getDescription()) && terser.getValues(existingParam, "documentation").isEmpty()) {
						terser.addElement(existingParam, "documentation", nextParam.getDescription());
					}

					if (nextParam.getParamType() != null) {
						String existingType = terser.getSinglePrimitiveValueOrNull(existingParam, "type");
						if (!nextParam.getParamType().equals(existingType)) {
							if (existingType == null) {
								terser.setElement(existingParam, "type", nextParam.getParamType());
							} else {
								terser.setElement(existingParam, "type", "Resource");
							}
						}
					}

					terser.setElement(param, "use", "in");
					if (nextParam.getSearchParamType() != null) {
						terser.setElement(param, "searchType", nextParam.getSearchParamType());
					}
					terser.setElement(param, "min", Integer.toString(nextParam.getMin()));
					terser.setElement(param, "max", (nextParam.getMax() == -1 ? "*" : Integer.toString(nextParam.getMax())));
					terser.setElement(param, "name", nextParam.getName());

					List<IBaseExtension<?, ?>> existingExampleExtensions = ExtensionUtil.getExtensionsByUrl((IBaseHasExtensions) param, HapiExtensions.EXT_OP_PARAMETER_EXAMPLE_VALUE);
					Set<String> existingExamples = existingExampleExtensions
						.stream()
						.map(t -> t.getValue())
						.filter(t -> t != null)
						.map(t -> (IPrimitiveType<?>) t)
						.map(t -> t.getValueAsString())
						.collect(Collectors.toSet());
					for (String nextExample : nextParam.getExampleValues()) {
						if (!existingExamples.contains(nextExample)) {
							ExtensionUtil.addExtension(myContext, param, HapiExtensions.EXT_OP_PARAMETER_EXAMPLE_VALUE, "string", nextExample);
						}
					}

				}
			}

			for (ReturnType nextParam : operationMethodBinding.getReturnParams()) {
				if (outParams.containsKey(nextParam.getName())) {
					continue;
				}

				IBase param = terser.addElement(op, "parameter");
				outParams.put(nextParam.getName(), param);

				terser.addElement(param, "use", "out");
				if (nextParam.getType() != null) {
					terser.addElement(param, "type", nextParam.getType());
				}
				terser.addElement(param, "min", Integer.toString(nextParam.getMin()));
				terser.addElement(param, "max", (nextParam.getMax() == -1 ? "*" : Integer.toString(nextParam.getMax())));
				terser.addElement(param, "name", nextParam.getName());
			}
		}
		String name = WordUtils.capitalize(code);

		terser.addElements(op, "resource", resourceNames);
		terser.addElement(op, "name", name);
		terser.addElement(op, "url", url);
		terser.addElement(op, "code", code);
		terser.addElement(op, "description", description);
		terser.addElement(op, "title", title);
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

	public void setRestResourceRevIncludesEnabled(boolean theRestResourceRevIncludesEnabled) {
		myRestResourceRevIncludesEnabled = theRestResourceRevIncludesEnabled;
	}
}
