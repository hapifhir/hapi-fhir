package ca.uhn.fhir.rest.server.provider;

/*
 * #%L
 * HAPI FHIR Structures - DSTU1 (FHIR v0.80)
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestQuery;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResourceOperation;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResourceSearchParam;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulConformanceModeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.DynamicSearchMethodBinding;
import ca.uhn.fhir.rest.method.IParameter;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.method.SearchParameter;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IServerConformanceProvider;
import ca.uhn.fhir.rest.server.ResourceBinding;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestulfulServerConfiguration;
import ca.uhn.fhir.util.ExtensionConstants;

/**
 * Server FHIR Provider which serves the conformance statement for a RESTful server implementation
 * 
 * <p>
 * Note: This class is safe to extend, but it is important to note that the same instance of {@link Conformance} is
 * always returned unless {@link #setCache(boolean)} is called with a value of <code>false</code>. This means that if
 * you are adding anything to the returned conformance instance on each call you should call
 * <code>setCache(false)</code> in your provider constructor.
 * </p>
 */
public class ServerConformanceProvider implements IServerConformanceProvider<Conformance> {

	private boolean myCache = true;
	private volatile Conformance myConformance;
	private String myPublisher = "Not provided";
	private RestulfulServerConfiguration myServerConfiguration;

	/*
	 * Add a no-arg constructor and seetter so that the
	 * ServerConfirmanceProvider can be Spring-wired with
	 * the RestfulService avoiding the potential reference
	 * cycle that would happen.
	 */
	public ServerConformanceProvider () {
		super();
	}
	
	public void setRestfulServer (RestfulServer theRestfulServer) {
		myServerConfiguration = theRestfulServer.createConfiguration();
	}
	
	public ServerConformanceProvider(RestfulServer theRestfulServer) {
		myServerConfiguration = theRestfulServer.createConfiguration();
	}	
	
	/**
	 * Gets the value of the "publisher" that will be placed in the generated conformance statement. As this
	 * is a mandatory element, the value should not be null (although this is not enforced). The value defaults
	 * to "Not provided" but may be set to null, which will cause this element to be omitted.
	 */
	public String getPublisher() {
		return myPublisher;
	}

	/**
	 * Actually create and return the conformance statement
	 * 
	 * See the class documentation for an important note if you are extending this class
	 */
	@Override
	@Metadata
	public Conformance getServerConformance(HttpServletRequest theRequest) {
		if (myConformance != null && myCache) {
			return myConformance;
		}

		Conformance retVal = new Conformance();

		retVal.setPublisher(myPublisher);
		retVal.setDate(conformanceDate());
		retVal.setFhirVersion("0.0.82-3059"); // TODO: pull from model
		retVal.setAcceptUnknown(false); // TODO: make this configurable - this is a fairly big effort since the parser needs to be modified to actually allow it
		
		retVal.getImplementation().setDescription(myServerConfiguration.getImplementationDescription());
		retVal.getSoftware().setName(myServerConfiguration.getServerName());
		retVal.getSoftware().setVersion(myServerConfiguration.getServerVersion());
		retVal.addFormat(Constants.CT_FHIR_XML);
		retVal.addFormat(Constants.CT_FHIR_JSON);

		Rest rest = retVal.addRest();
		rest.setMode(RestfulConformanceModeEnum.SERVER);

		Set<RestfulOperationSystemEnum> systemOps = new HashSet<RestfulOperationSystemEnum>();

		List<ResourceBinding> bindings = new ArrayList<ResourceBinding>(myServerConfiguration.getResourceBindings());
		Collections.sort(bindings, new Comparator<ResourceBinding>() {
			@Override
			public int compare(ResourceBinding theArg0, ResourceBinding theArg1) {
				return theArg0.getResourceName().compareToIgnoreCase(theArg1.getResourceName());
			}
		});

		for (ResourceBinding next : bindings) {

			Set<RestfulOperationTypeEnum> resourceOps = new HashSet<RestfulOperationTypeEnum>();
			RestResource resource = rest.addResource();

			String resourceName = next.getResourceName();
			RuntimeResourceDefinition def = myServerConfiguration.getFhirContext().getResourceDefinition(resourceName);
			resource.getType().setValue(def.getName());
			ServletContext servletContext  = (ServletContext) (theRequest == null ? null : theRequest.getAttribute(RestfulServer.SERVLET_CONTEXT_ATTRIBUTE));
			String serverBase = myServerConfiguration.getServerAddressStrategy().determineServerBase(servletContext, theRequest);
            resource.getProfile().setReference(new IdDt(def.getResourceProfile(serverBase)));

			TreeSet<String> includes = new TreeSet<String>();

			// Map<String, Conformance.RestResourceSearchParam> nameToSearchParam = new HashMap<String,
			// Conformance.RestResourceSearchParam>();
			for (BaseMethodBinding<?> nextMethodBinding : next.getMethodBindings()) {
				if (nextMethodBinding.getRestOperationType() != null) {
					RestfulOperationTypeEnum resOp = RestfulOperationTypeEnum.VALUESET_BINDER.fromCodeString(nextMethodBinding.getRestOperationType().getCode());
					if (resOp != null) {
						if (resourceOps.contains(resOp) == false) {
							resourceOps.add(resOp);
							resource.addOperation().setCode(resOp);
						}
					}
	
					RestfulOperationSystemEnum sysOp = RestfulOperationSystemEnum.VALUESET_BINDER.fromCodeString(nextMethodBinding.getRestOperationType().getCode());
					if (sysOp != null) {
						if (systemOps.contains(sysOp) == false) {
							systemOps.add(sysOp);
							rest.addOperation().setCode(sysOp);
						}
					}
				}
				
				if (nextMethodBinding instanceof SearchMethodBinding) {
					handleSearchMethodBinding(rest, resource, resourceName, def, includes, (SearchMethodBinding) nextMethodBinding);
				} else if (nextMethodBinding instanceof DynamicSearchMethodBinding) {
					handleDynamicSearchMethodBinding(resource, def, includes, (DynamicSearchMethodBinding) nextMethodBinding);
				}

				Collections.sort(resource.getOperation(), new Comparator<RestResourceOperation>() {
					@Override
					public int compare(RestResourceOperation theO1, RestResourceOperation theO2) {
						RestfulOperationTypeEnum o1 = theO1.getCode().getValueAsEnum();
						RestfulOperationTypeEnum o2 = theO2.getCode().getValueAsEnum();
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

		}

		myConformance = retVal;
		return retVal;
	}

	private DateTimeDt conformanceDate() {
		String buildDate = myServerConfiguration.getConformanceDate();
		if (buildDate != null) {
			try {
				return new DateTimeDt(buildDate);
			} catch (DataFormatException e) {
				// fall through
			}
		}
		return DateTimeDt.withCurrentTime();
	}

	private void handleDynamicSearchMethodBinding(RestResource resource, RuntimeResourceDefinition def, TreeSet<String> includes, DynamicSearchMethodBinding searchMethodBinding) {
		includes.addAll(searchMethodBinding.getIncludes());

		List<RuntimeSearchParam> searchParameters = new ArrayList<RuntimeSearchParam>();
		searchParameters.addAll(searchMethodBinding.getSearchParams());
		sortRuntimeSearchParameters(searchParameters);

		if (!searchParameters.isEmpty()) {

			for (RuntimeSearchParam nextParameter : searchParameters) {

				String nextParamName = nextParameter.getName();

				// String chain = null;
				String nextParamUnchainedName = nextParamName;
				if (nextParamName.contains(".")) {
					// chain = nextParamName.substring(nextParamName.indexOf('.') + 1);
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

				RestResourceSearchParam param;
				param = resource.addSearchParam();

				param.setName(nextParamName);
				// if (StringUtils.isNotBlank(chain)) {
				// param.addChain(chain);
				// }
				param.setDocumentation(nextParamDescription);
				param.getTypeElement().setValue(nextParameter.getParamType().getCode());
			}
		}
	}

	private void handleSearchMethodBinding(Rest rest, RestResource resource, String resourceName, RuntimeResourceDefinition def, TreeSet<String> includes, SearchMethodBinding searchMethodBinding) {
		includes.addAll(searchMethodBinding.getIncludes());

		List<IParameter> params = searchMethodBinding.getParameters();
		List<SearchParameter> searchParameters = new ArrayList<SearchParameter>();
		for (IParameter nextParameter : params) {
			if ((nextParameter instanceof SearchParameter)) {
				searchParameters.add((SearchParameter) nextParameter);
			}
		}
		sortSearchParameters(searchParameters);
		if (!searchParameters.isEmpty()) {
			boolean allOptional = searchParameters.get(0).isRequired() == false;

			RestQuery query = null;
			if (!allOptional) {
				query = rest.addQuery();
				query.getDocumentation().setValue(searchMethodBinding.getDescription());
				query.addUndeclaredExtension(false, ExtensionConstants.QUERY_RETURN_TYPE, new CodeDt(resourceName));
				for (String nextInclude : searchMethodBinding.getIncludes()) {
					query.addUndeclaredExtension(false, ExtensionConstants.QUERY_ALLOWED_INCLUDE, new StringDt(nextInclude));
				}
			}

			for (SearchParameter nextParameter : searchParameters) {

				String nextParamName = nextParameter.getName();

				// String chain = null;
				String nextParamUnchainedName = nextParamName;
				if (nextParamName.contains(".")) {
					// chain = nextParamName.substring(nextParamName.indexOf('.') + 1);
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

				RestResourceSearchParam param;
				if (query == null) {
					param = resource.addSearchParam();
				} else {
					param = query.addParameter();
					param.addUndeclaredExtension(false, ExtensionConstants.PARAM_IS_REQUIRED, new BooleanDt(nextParameter.isRequired()));
				}

				param.setName(nextParamName);
				// if (StringUtils.isNotBlank(chain)) {
				// param.addChain(chain);
				// }
				param.setDocumentation(nextParamDescription);
				param.getTypeElement().setValue(nextParameter.getParamType().getCode());
				for (Class<? extends IBaseResource> nextTarget : nextParameter.getDeclaredTypes()) {
					RuntimeResourceDefinition targetDef = myServerConfiguration.getFhirContext().getResourceDefinition(nextTarget);
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

	/**
	 * Sets the cache property (default is true). If set to true, the same response will be returned for each
	 * invocation.
	 * <p>
	 * See the class documentation for an important note if you are extending this class
	 * </p>
	 */
	public void setCache(boolean theCache) {
		myCache = theCache;
	}

	/**
	 * Sets the value of the "publisher" that will be placed in the generated conformance statement. As this
	 * is a mandatory element, the value should not be null (although this is not enforced). The value defaults
	 * to "Not provided" but may be set to null, which will cause this element to be omitted.
	 */
	public void setPublisher(String thePublisher) {
		myPublisher = thePublisher;
	}

	private void sortRuntimeSearchParameters(List<RuntimeSearchParam> searchParameters) {
		Collections.sort(searchParameters, new Comparator<RuntimeSearchParam>() {
			@Override
			public int compare(RuntimeSearchParam theO1, RuntimeSearchParam theO2) {
				return theO1.getName().compareTo(theO2.getName());
			}
		});
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
