package ca.uhn.fhir.rest.server.provider;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResourceOperation;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResourceSearchParam;
import ca.uhn.fhir.model.dstu.valueset.RestfulConformanceModeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.param.SearchParameter;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.ResourceBinding;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.util.ExtensionConstants;

public class ServerConformanceProvider {

	private volatile Conformance myConformance;
	private final RestfulServer myRestfulServer;
	private boolean myCache = true;

	public ServerConformanceProvider(RestfulServer theRestfulServer) {
		myRestfulServer = theRestfulServer;
	}

	@Metadata
	public Conformance getServerConformance() {
		if (myConformance != null && myCache) {
			return myConformance;
		}

		Conformance retVal = new Conformance();
		retVal.getSoftware().setName(myRestfulServer.getServerName());
		retVal.getSoftware().setVersion(myRestfulServer.getServerVersion());
		retVal.addFormat(Constants.CT_FHIR_XML);
		retVal.addFormat(Constants.CT_FHIR_JSON);

		Rest rest = retVal.addRest();
		rest.setMode(RestfulConformanceModeEnum.SERVER);

		Set<RestfulOperationSystemEnum> systemOps = new HashSet<RestfulOperationSystemEnum>();

		List<ResourceBinding> bindings = new ArrayList<ResourceBinding>(myRestfulServer.getResourceBindings());
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
			RuntimeResourceDefinition def = myRestfulServer.getFhirContext().getResourceDefinition(resourceName);
			resource.getType().setValue(def.getName());
			resource.getProfile().setId(new IdDt(def.getResourceProfile()));

			Map<String, Conformance.RestResourceSearchParam> nameToSearchParam = new HashMap<String, Conformance.RestResourceSearchParam>();
			for (BaseMethodBinding<?> nextMethodBinding : next.getMethodBindings()) {
				RestfulOperationTypeEnum resOp = nextMethodBinding.getResourceOperationType();
				if (resOp != null) {
					if (resourceOps.contains(resOp) == false) {
						resourceOps.add(resOp);
						resource.addOperation().setCode(resOp);
					}
				}

				RestfulOperationSystemEnum sysOp = nextMethodBinding.getSystemOperationType();
				if (sysOp != null) {
					if (systemOps.contains(sysOp) == false) {
						systemOps.add(sysOp);
						rest.addOperation().setCode(sysOp);
					}
				}

				if (nextMethodBinding instanceof SearchMethodBinding) {
					List<IParameter> params = ((SearchMethodBinding) nextMethodBinding).getParameters();
					List<SearchParameter> searchParameters = new ArrayList<SearchParameter>();
					for (IParameter nextParameter : params) {
						if ((nextParameter instanceof SearchParameter)) {
							searchParameters.add((SearchParameter) nextParameter);
						}
					}
					Collections.sort(searchParameters, new Comparator<SearchParameter>() {
						@Override
						public int compare(SearchParameter theO1, SearchParameter theO2) {
							if (theO1.isRequired() == theO2.isRequired()) {
								return 0;
							}
							if (theO1.isRequired()) {
								return 1;
							}
							return -1;
						}
					});
					if (searchParameters.isEmpty()) {
						continue;
					}
					boolean allOptional = searchParameters.get(0).isRequired() == false;

					RestResourceSearchParam searchParam = null;
					ExtensionDt searchParamChain = null;
					for (SearchParameter nextParameter : searchParameters) {

						if (searchParam == null || allOptional) {
							if (!nameToSearchParam.containsKey(nextParameter.getName())) {
								RestResourceSearchParam param = resource.addSearchParam();
								param.setName(nextParameter.getName());
								param.setDocumentation(nextParameter.getDescription());
								param.setType(nextParameter.getParamType());
								searchParam = param;
							} else {
								searchParam = nameToSearchParam.get(nextParameter.getName());
							}

							if (searchParam != null) {
								searchParam.setType(nextParameter.getParamType());
							}

						} else {

							searchParamChain = searchParam.addUndeclaredExtension(false, ExtensionConstants.CONF_ADDITIONAL_PARAM);

							// if (searchParamChain == null) {
							// } else {
							// searchParamChain = searchParamChain.addUndeclaredExtension(false,
							// ExtensionConstants.CONF_ADDITIONAL_PARAM);
							// }

							ExtensionDt ext = new ExtensionDt();
							ext.setUrl(ExtensionConstants.CONF_ADDITIONAL_PARAM_NAME);
							ext.setValue(new StringDt(nextParameter.getName()));
							searchParamChain.getUndeclaredExtensions().add(ext);

							ext = new ExtensionDt();
							ext.setUrl(ExtensionConstants.CONF_ADDITIONAL_PARAM_DESCRIPTION);
							ext.setValue(new StringDt(nextParameter.getDescription()));
							searchParamChain.getUndeclaredExtensions().add(ext);

							ext = new ExtensionDt();
							ext.setUrl(ExtensionConstants.CONF_ADDITIONAL_PARAM_TYPE);
							if (nextParameter.getParamType() != null) {
								ext.setValue(new CodeDt(nextParameter.getParamType().getCode()));
							}
							searchParamChain.getUndeclaredExtensions().add(ext);

							ext = new ExtensionDt();
							ext.setUrl(ExtensionConstants.CONF_ADDITIONAL_PARAM_REQUIRED);
							ext.setValue(new BooleanDt(nextParameter.isRequired()));
							searchParamChain.getUndeclaredExtensions().add(ext);

						}

					}
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

		}

		myConformance = retVal;
		return retVal;
	}

	/**
	 * Sets the cache property (default is true). If set to true, the same response will be returned for each
	 * invocation.
	 */
	public void setCache(boolean theCache) {
		myCache = theCache;
	}
}
