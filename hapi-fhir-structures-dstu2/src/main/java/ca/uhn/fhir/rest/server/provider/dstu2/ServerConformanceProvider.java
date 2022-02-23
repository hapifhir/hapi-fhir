package ca.uhn.fhir.rest.server.provider.dstu2;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResourceInteraction;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResourceSearchParam;
import ca.uhn.fhir.model.dstu2.resource.OperationDefinition;
import ca.uhn.fhir.model.dstu2.resource.OperationDefinition.Parameter;
import ca.uhn.fhir.model.dstu2.valueset.*;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.*;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.method.*;
import ca.uhn.fhir.rest.server.method.OperationMethodBinding.ReturnType;
import ca.uhn.fhir.rest.server.util.BaseServerCapabilityStatementProvider;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.Map.Entry;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR Structures - DSTU2 (FHIR v1.0.0)
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
 */
public class ServerConformanceProvider extends BaseServerCapabilityStatementProvider implements IServerConformanceProvider<Conformance> {

	private String myPublisher = "Not provided";

	/**
	 * No-arg constructor and setter so that the ServerConfirmanceProvider can be Spring-wired with the RestfulService avoiding the potential reference cycle that would happen.
	 */
	public ServerConformanceProvider() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @deprecated Use no-args constructor instead. Deprecated in 4.0.0
	 */
	@Deprecated
	public ServerConformanceProvider(RestfulServer theRestfulServer) {
		this();
	}

	/**
	 * Constructor
	 */
	public ServerConformanceProvider(RestfulServerConfiguration theServerConfiguration) {
		super(theServerConfiguration);
	}

	private void checkBindingForSystemOps(Rest rest, Set<SystemRestfulInteractionEnum> systemOps, BaseMethodBinding<?> nextMethodBinding) {
		if (nextMethodBinding.getRestOperationType() != null) {
			String sysOpCode = nextMethodBinding.getRestOperationType().getCode();
			if (sysOpCode != null) {
				SystemRestfulInteractionEnum sysOp = SystemRestfulInteractionEnum.VALUESET_BINDER.fromCodeString(sysOpCode);
				if (sysOp == null) {
					return;
				}
				if (systemOps.contains(sysOp) == false) {
					systemOps.add(sysOp);
					rest.addInteraction().setCode(sysOp);
				}
			}
		}
	}

	private Map<String, List<BaseMethodBinding<?>>> collectMethodBindings(RequestDetails theRequestDetails) {
		Map<String, List<BaseMethodBinding<?>>> resourceToMethods = new TreeMap<String, List<BaseMethodBinding<?>>>();
		for (ResourceBinding next : getServerConfiguration(theRequestDetails).getResourceBindings()) {
			String resourceName = next.getResourceName();
			for (BaseMethodBinding<?> nextMethodBinding : next.getMethodBindings()) {
				if (resourceToMethods.containsKey(resourceName) == false) {
					resourceToMethods.put(resourceName, new ArrayList<BaseMethodBinding<?>>());
				}
				resourceToMethods.get(resourceName).add(nextMethodBinding);
			}
		}
		for (BaseMethodBinding<?> nextMethodBinding : getServerConfiguration(theRequestDetails).getServerBindings()) {
			String resourceName = "";
			if (resourceToMethods.containsKey(resourceName) == false) {
				resourceToMethods.put(resourceName, new ArrayList<BaseMethodBinding<?>>());
			}
			resourceToMethods.get(resourceName).add(nextMethodBinding);
		}
		return resourceToMethods;
	}

	private DateTimeDt conformanceDate(RequestDetails theRequestDetails) {
		IPrimitiveType<Date> buildDate = getServerConfiguration(theRequestDetails).getConformanceDate();
		if (buildDate != null && buildDate.getValue() != null) {
			try {
				return new DateTimeDt(buildDate.getValueAsString());
			} catch (DataFormatException e) {
				// fall through
			}
		}
		return DateTimeDt.withCurrentTime();
	}

	private String createOperationName(OperationMethodBinding theMethodBinding) {
		StringBuilder retVal = new StringBuilder();
		if (theMethodBinding.getResourceName() != null) {
			retVal.append(theMethodBinding.getResourceName());
		}

		retVal.append('-');
		if (theMethodBinding.isCanOperateAtInstanceLevel()) {
			retVal.append('i');
		}
		if (theMethodBinding.isCanOperateAtServerLevel()) {
			retVal.append('s');
		}
		retVal.append('-');

		// Exclude the leading $
		retVal.append(theMethodBinding.getName(), 1, theMethodBinding.getName().length());

		return retVal.toString();
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

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	@Override
	@Metadata
	public Conformance getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails) {
		RestfulServerConfiguration serverConfiguration = getServerConfiguration(theRequestDetails);
		Bindings bindings = serverConfiguration.provideBindings();

		Conformance retVal = new Conformance();

		retVal.setPublisher(myPublisher);
		retVal.setDate(conformanceDate(theRequestDetails));
		retVal.setFhirVersion(FhirVersionEnum.DSTU2.getFhirVersionString());
		retVal.setAcceptUnknown(UnknownContentCodeEnum.UNKNOWN_EXTENSIONS); // TODO: make this configurable - this is a fairly big effort since the parser
		// needs to be modified to actually allow it

		ServletContext servletContext = (ServletContext) (theRequest == null ? null : theRequest.getAttribute(RestfulServer.SERVLET_CONTEXT_ATTRIBUTE));
		String serverBase = serverConfiguration.getServerAddressStrategy().determineServerBase(servletContext, theRequest);
		retVal
			.getImplementation()
			.setUrl(serverBase)
			.setDescription(serverConfiguration.getImplementationDescription());

		retVal.setKind(ConformanceStatementKindEnum.INSTANCE);
		retVal.getSoftware().setName(serverConfiguration.getServerName());
		retVal.getSoftware().setVersion(serverConfiguration.getServerVersion());
		retVal.addFormat(Constants.CT_FHIR_XML);
		retVal.addFormat(Constants.CT_FHIR_JSON);

		Rest rest = retVal.addRest();
		rest.setMode(RestfulConformanceModeEnum.SERVER);

		Set<SystemRestfulInteractionEnum> systemOps = new HashSet<>();
		Set<String> operationNames = new HashSet<>();

		Map<String, List<BaseMethodBinding<?>>> resourceToMethods = collectMethodBindings(theRequestDetails);
		for (Entry<String, List<BaseMethodBinding<?>>> nextEntry : resourceToMethods.entrySet()) {

			if (nextEntry.getKey().isEmpty() == false) {
				Set<TypeRestfulInteractionEnum> resourceOps = new HashSet<>();
				RestResource resource = rest.addResource();
				String resourceName = nextEntry.getKey();
				RuntimeResourceDefinition def = serverConfiguration.getFhirContext().getResourceDefinition(resourceName);
				resource.getTypeElement().setValue(def.getName());
				resource.getProfile().setReference(new IdDt(def.getResourceProfile(serverBase)));

				TreeSet<String> includes = new TreeSet<>();

				// Map<String, Conformance.RestResourceSearchParam> nameToSearchParam = new HashMap<String,
				// Conformance.RestResourceSearchParam>();
				for (BaseMethodBinding<?> nextMethodBinding : nextEntry.getValue()) {
					if (nextMethodBinding.getRestOperationType() != null) {
						String resOpCode = nextMethodBinding.getRestOperationType().getCode();
						if (resOpCode != null) {
							TypeRestfulInteractionEnum resOp = TypeRestfulInteractionEnum.VALUESET_BINDER.fromCodeString(resOpCode);
							if (resOp != null) {
								if (resourceOps.contains(resOp) == false) {
									resourceOps.add(resOp);
									resource.addInteraction().setCode(resOp);
								}
								if ("vread".equals(resOpCode)) {
									// vread implies read
									resOp = TypeRestfulInteractionEnum.READ;
									if (resourceOps.contains(resOp) == false) {
										resourceOps.add(resOp);
										resource.addInteraction().setCode(resOp);
									}
								}

								if (nextMethodBinding.isSupportsConditional()) {
									switch (resOp) {
										case CREATE:
											resource.setConditionalCreate(true);
											break;
										case DELETE:
											if (nextMethodBinding.isSupportsConditionalMultiple()) {
												resource.setConditionalDelete(ConditionalDeleteStatusEnum.MULTIPLE_DELETES_SUPPORTED);
											} else {
												resource.setConditionalDelete(ConditionalDeleteStatusEnum.SINGLE_DELETES_SUPPORTED);
											}
											break;
										case UPDATE:
											resource.setConditionalUpdate(true);
											break;
										default:
											break;
									}
								}
							}
						}
					}

					checkBindingForSystemOps(rest, systemOps, nextMethodBinding);

					if (nextMethodBinding instanceof SearchMethodBinding) {
						handleSearchMethodBinding(resource, def, includes, (SearchMethodBinding) nextMethodBinding, theRequestDetails);
					} else if (nextMethodBinding instanceof OperationMethodBinding) {
						OperationMethodBinding methodBinding = (OperationMethodBinding) nextMethodBinding;
						String opName = bindings.getOperationBindingToId().get(methodBinding);
						if (operationNames.add(opName)) {
							// Only add each operation (by name) once
							rest.addOperation().setName(methodBinding.getName().substring(1)).getDefinition().setReference("OperationDefinition/" + opName);
						}
					}

					Collections.sort(resource.getInteraction(), new Comparator<RestResourceInteraction>() {
						@Override
						public int compare(RestResourceInteraction theO1, RestResourceInteraction theO2) {
							TypeRestfulInteractionEnum o1 = theO1.getCodeElement().getValueAsEnum();
							TypeRestfulInteractionEnum o2 = theO2.getCodeElement().getValueAsEnum();
							if (o1 == null && o2 == null) {
								return 0;
							}
							if (o1 == null) {
								return 1;
							}
							if (o2 == null) {
								return -1;
							}
							return o1.ordinal() - o2.ordinal();
						}
					});

				}

				for (String nextInclude : includes) {
					resource.addSearchInclude(nextInclude);
				}
			} else {
				for (BaseMethodBinding<?> nextMethodBinding : nextEntry.getValue()) {
					checkBindingForSystemOps(rest, systemOps, nextMethodBinding);
					if (nextMethodBinding instanceof OperationMethodBinding) {
						OperationMethodBinding methodBinding = (OperationMethodBinding) nextMethodBinding;
						String opName = bindings.getOperationBindingToId().get(methodBinding);
						if (operationNames.add(opName)) {
							rest.addOperation().setName(methodBinding.getName().substring(1)).getDefinition().setReference("OperationDefinition/" + opName);
						}
					}
				}
			}
		}

		return retVal;
	}

	private void handleSearchMethodBinding(RestResource resource, RuntimeResourceDefinition def, TreeSet<String> includes, SearchMethodBinding searchMethodBinding, RequestDetails theRequestDetails) {
		includes.addAll(searchMethodBinding.getIncludes());

		List<IParameter> params = searchMethodBinding.getParameters();
		List<SearchParameter> searchParameters = new ArrayList<>();
		for (IParameter nextParameter : params) {
			if ((nextParameter instanceof SearchParameter)) {
				searchParameters.add((SearchParameter) nextParameter);
			}
		}
		sortSearchParameters(searchParameters);
		if (!searchParameters.isEmpty()) {
			// boolean allOptional = searchParameters.get(0).isRequired() == false;
			//
			// OperationDefinition query = null;
			// if (!allOptional) {
			// RestOperation operation = rest.addOperation();
			// query = new OperationDefinition();
			// operation.setDefinition(new ResourceReferenceDt(query));
			// query.getDescriptionElement().setValue(searchMethodBinding.getDescription());
			// query.addUndeclaredExtension(false, ExtensionConstants.QUERY_RETURN_TYPE, new CodeDt(resourceName));
			// for (String nextInclude : searchMethodBinding.getIncludes()) {
			// query.addUndeclaredExtension(false, ExtensionConstants.QUERY_ALLOWED_INCLUDE, new StringDt(nextInclude));
			// }
			// }

			for (SearchParameter nextParameter : searchParameters) {

				String nextParamName = nextParameter.getName();

				String chain = null;
				String nextParamUnchainedName = nextParamName;
				if (nextParamName.contains(".")) {
					chain = nextParamName.substring(nextParamName.indexOf('.') + 1);
					nextParamUnchainedName = nextParamName.substring(0, nextParamName.indexOf('.'));
				}

				String nextParamDescription = nextParameter.getDescription();

				/*
				 * If the parameter has no description, default to the one from the resource
				 */
				if (StringUtils.isBlank(nextParamDescription)) {
					RuntimeSearchParam paramDef = def.getSearchParam(nextParamUnchainedName);
					if (paramDef != null) {
						nextParamDescription = paramDef.getDescription();
					}
				}

				String finalNextParamUnchainedName = nextParamUnchainedName;
				RestResourceSearchParam param =
					resource
						.getSearchParam()
						.stream()
						.filter(t -> t.getName().equals(finalNextParamUnchainedName))
						.findFirst()
						.orElseGet(() -> resource.addSearchParam());

				param.setName(nextParamUnchainedName);
				if (StringUtils.isNotBlank(chain)) {
					param.addChain(chain);
				} else {
					if (nextParameter.getParamType() == RestSearchParameterTypeEnum.REFERENCE) {
						for (String nextWhitelist : new TreeSet<>(nextParameter.getQualifierWhitelist())) {
							if (nextWhitelist.startsWith(".")) {
								param.addChain(nextWhitelist.substring(1));
							}
						}
					}
				}

				param.setDocumentation(nextParamDescription);
				if (nextParameter.getParamType() != null) {
					param.getTypeElement().setValueAsString(nextParameter.getParamType().getCode());
				}
				for (Class<? extends IBaseResource> nextTarget : nextParameter.getDeclaredTypes()) {
					RuntimeResourceDefinition targetDef = getServerConfiguration(theRequestDetails).getFhirContext().getResourceDefinition(nextTarget);
					if (targetDef != null) {
						ResourceTypeEnum code = ResourceTypeEnum.VALUESET_BINDER.fromCodeString(targetDef.getName());
						if (code != null) {
							param.addTarget(code);
						}
					}
				}
			}
		}
	}


	@Read(type = OperationDefinition.class)
	public OperationDefinition readOperationDefinition(@IdParam IdDt theId, RequestDetails theRequestDetails) {
		if (theId == null || theId.hasIdPart() == false) {
			throw new ResourceNotFoundException(Msg.code(1988) + theId);
		}
		RestfulServerConfiguration serverConfiguration = getServerConfiguration(theRequestDetails);
		Bindings bindings = serverConfiguration.provideBindings();

		List<OperationMethodBinding> sharedDescriptions = bindings.getOperationIdToBindings().get(theId.getIdPart());
		if (sharedDescriptions == null || sharedDescriptions.isEmpty()) {
			throw new ResourceNotFoundException(Msg.code(1989) + theId);
		}

		OperationDefinition op = new OperationDefinition();
		op.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		op.setKind(OperationKindEnum.OPERATION);
		op.setIdempotent(true);

		Set<String> inParams = new HashSet<>();
		Set<String> outParams = new HashSet<>();

		for (OperationMethodBinding sharedDescription : sharedDescriptions) {
			if (isNotBlank(sharedDescription.getDescription())) {
				op.setDescription(sharedDescription.getDescription());
			}
			if (!sharedDescription.isIdempotent()) {
				op.setIdempotent(sharedDescription.isIdempotent());
			}
			op.setCode(sharedDescription.getName().substring(1));
			if (sharedDescription.isCanOperateAtInstanceLevel()) {
				op.setInstance(sharedDescription.isCanOperateAtInstanceLevel());
			}
			if (sharedDescription.isCanOperateAtServerLevel()) {
				op.setSystem(sharedDescription.isCanOperateAtServerLevel());
			}
			if (isNotBlank(sharedDescription.getResourceName())) {
				op.addType().setValue(sharedDescription.getResourceName());
			}

			for (IParameter nextParamUntyped : sharedDescription.getParameters()) {
				if (nextParamUntyped instanceof OperationParameter) {
					OperationParameter nextParam = (OperationParameter) nextParamUntyped;
					if (!inParams.add(nextParam.getName())) {
						continue;
					}
					Parameter param = op.addParameter();
					param.setUse(OperationParameterUseEnum.IN);
					if (nextParam.getParamType() != null) {
						param.setType(nextParam.getParamType());
					}
					param.setMin(nextParam.getMin());
					param.setMax(nextParam.getMax() == -1 ? "*" : Integer.toString(nextParam.getMax()));
					param.setName(nextParam.getName());
				}
			}

			for (ReturnType nextParam : sharedDescription.getReturnParams()) {
				if (!outParams.add(nextParam.getName())) {
					continue;
				}
				Parameter param = op.addParameter();
				param.setUse(OperationParameterUseEnum.OUT);
				if (nextParam.getType() != null) {
					param.setType(nextParam.getType());
				}
				param.setMin(nextParam.getMin());
				param.setMax(nextParam.getMax() == -1 ? "*" : Integer.toString(nextParam.getMax()));
				param.setName(nextParam.getName());
			}
		}

		if (isBlank(op.getName())) {
			if (isNotBlank(op.getDescription())) {
				op.setName(op.getDescription());
			} else {
				op.setName(op.getCode());
			}
		}

		if (op.getSystem() == null) {
			op.setSystem(false);
		}
		if (op.getInstance() == null) {
			op.setInstance(false);
		}

		return op;
	}

	/**
	 * Sets the cache property (default is true). If set to true, the same response will be returned for each invocation.
	 * <p>
	 * See the class documentation for an important note if you are extending this class
	 * </p>
	 * @deprecated Since 4.0.0 this does nothing
	 */
	@Deprecated
	public void setCache(boolean theCache) {
		// nothing
	}

	@Override
	public void setRestfulServer(RestfulServer theRestfulServer) {
		// nothing
	}

	private void sortSearchParameters(List<SearchParameter> searchParameters) {
		Collections.sort(searchParameters, new Comparator<SearchParameter>() {
			@Override
			public int compare(SearchParameter theO1, SearchParameter theO2) {
				if (theO1.isRequired() == theO2.isRequired()) {
					return theO1.getName().compareTo(theO2.getName());
				}
				if (theO1.isRequired()) {
					return -1;
				}
				return 1;
			}
		});
	}

}
