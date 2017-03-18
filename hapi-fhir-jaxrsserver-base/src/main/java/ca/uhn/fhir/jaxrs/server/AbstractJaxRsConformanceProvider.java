package ca.uhn.fhir.jaxrs.server;

/*
 * #%L
 * HAPI FHIR JAX-RS Server
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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.hapi.rest.server.ServerCapabilityStatementProvider;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest.Builder;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.ParseAction;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IRestfulResponse;
import ca.uhn.fhir.rest.server.ResourceBinding;
import ca.uhn.fhir.rest.server.RestulfulServerConfiguration;
import ca.uhn.fhir.util.ReflectionUtil;

/**
 * This is the conformance provider for the jax rs servers. It requires all providers to be registered during startup because the conformance profile is generated during the postconstruct phase.
 * 
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public abstract class AbstractJaxRsConformanceProvider extends AbstractJaxRsProvider implements IResourceProvider {

	/** the logger */
	private static final org.slf4j.Logger ourLog = LoggerFactory.getLogger(AbstractJaxRsConformanceProvider.class);
	/** the server bindings */
	private ResourceBinding myServerBinding = new ResourceBinding();
	/** the resource bindings */
	private ConcurrentHashMap<String, ResourceBinding> myResourceNameToBinding = new ConcurrentHashMap<String, ResourceBinding>();
	/** the server configuration */
	private RestulfulServerConfiguration serverConfiguration = new RestulfulServerConfiguration();

	/** the conformance. It is created once during startup */
	private CapabilityStatement myDstu3CapabilityStatement;
	private ca.uhn.fhir.model.dstu2.resource.Conformance myDstu2Conformance;

	/**
	 * Constructor allowing the description, servername and server to be set
	 * 
	 * @param implementationDescription
	 *           the implementation description. If null, "" is used
	 * @param serverName
	 *           the server name. If null, "" is used
	 * @param serverVersion
	 *           the server version. If null, "" is used
	 */
	protected AbstractJaxRsConformanceProvider(String implementationDescription, String serverName, String serverVersion) {
		serverConfiguration.setFhirContext(getFhirContext());
		serverConfiguration.setImplementationDescription(StringUtils.defaultIfEmpty(implementationDescription, ""));
		serverConfiguration.setServerName(StringUtils.defaultIfEmpty(serverName, ""));
		serverConfiguration.setServerVersion(StringUtils.defaultIfEmpty(serverVersion, ""));
	}

	/**
	 * Constructor allowing the description, servername and server to be set
	 * 
	 * @param ctx
	 *           the {@link FhirContext} instance.
	 * @param implementationDescription
	 *           the implementation description. If null, "" is used
	 * @param serverName
	 *           the server name. If null, "" is used
	 * @param serverVersion
	 *           the server version. If null, "" is used
	 */
	protected AbstractJaxRsConformanceProvider(FhirContext ctx, String implementationDescription, String serverName, String serverVersion) {
		super(ctx);
		serverConfiguration.setFhirContext(ctx);
		serverConfiguration.setImplementationDescription(StringUtils.defaultIfEmpty(implementationDescription, ""));
		serverConfiguration.setServerName(StringUtils.defaultIfEmpty(serverName, ""));
		serverConfiguration.setServerVersion(StringUtils.defaultIfEmpty(serverVersion, ""));
	}

	/**
	 * This method will set the conformance during the postconstruct phase. The method {@link AbstractJaxRsConformanceProvider#getProviders()} is used to get all the resource providers include in the
	 * conformance
	 */
	@PostConstruct
	protected void setUpPostConstruct() {
		for (Entry<Class<? extends IResourceProvider>, IResourceProvider> provider : getProviders().entrySet()) {
			addProvider(provider.getValue(), provider.getKey());
		}
		List<BaseMethodBinding<?>> serverBindings = new ArrayList<BaseMethodBinding<?>>();
		for (ResourceBinding baseMethodBinding : myResourceNameToBinding.values()) {
			serverBindings.addAll(baseMethodBinding.getMethodBindings());
		}
		serverConfiguration.setServerBindings(serverBindings);
		serverConfiguration.setResourceBindings(new LinkedList<ResourceBinding>(myResourceNameToBinding.values()));
		HardcodedServerAddressStrategy hardcodedServerAddressStrategy = new HardcodedServerAddressStrategy();
		hardcodedServerAddressStrategy.setValue(getBaseForServer());
		serverConfiguration.setServerAddressStrategy(hardcodedServerAddressStrategy);
		if (super.getFhirContext().getVersion().getVersion().equals(FhirVersionEnum.DSTU3)) {
			ServerCapabilityStatementProvider serverCapabilityStatementProvider = new ServerCapabilityStatementProvider(serverConfiguration);
			serverCapabilityStatementProvider.initializeOperations();
			myDstu3CapabilityStatement = serverCapabilityStatementProvider.getServerConformance(null);
		} else if (super.getFhirContext().getVersion().getVersion().equals(FhirVersionEnum.DSTU2)) {
			ca.uhn.fhir.rest.server.provider.dstu2.ServerConformanceProvider serverCapabilityStatementProvider = new ca.uhn.fhir.rest.server.provider.dstu2.ServerConformanceProvider(serverConfiguration);
			serverCapabilityStatementProvider.initializeOperations();
			myDstu2Conformance = serverCapabilityStatementProvider.getServerConformance(null);
		}
	}

	/**
	 * This method must return all the resource providers which need to be included in the conformance
	 * 
	 * @return a map of the resource providers and their corresponding classes. This class needs to be given explicitly because retrieving the interface using {@link Object#getClass()} may not give the
	 *         correct interface in a jee environment.
	 */
	protected abstract ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> getProviders();

	/**
	 * This method will retrieve the conformance using the http OPTIONS method
	 * 
	 * @return the response containing the conformance
	 */
	@OPTIONS
	@Path("/metadata")
	public Response conformanceUsingOptions() throws IOException {
		return conformance();
	}

	/**
	 * This method will retrieve the conformance using the http GET method
	 * 
	 * @return the response containing the conformance
	 */
	@GET
	@Path("/metadata")
	public Response conformance() throws IOException {
		Builder request = getRequest(RequestTypeEnum.OPTIONS, RestOperationTypeEnum.METADATA);
		IRestfulResponse response = request.build().getResponse();
		response.addHeader(Constants.HEADER_CORS_ALLOW_ORIGIN, "*");
		
		IBaseResource conformance = null;
		if (super.getFhirContext().getVersion().getVersion().equals(FhirVersionEnum.DSTU3)) {
			conformance = myDstu3CapabilityStatement;
//			return (Response) response.returnResponse(ParseAction.create(myDstu3CapabilityStatement), Constants.STATUS_HTTP_200_OK, true, null, getResourceType().getSimpleName());
		} else if (super.getFhirContext().getVersion().getVersion().equals(FhirVersionEnum.DSTU2)) {
			conformance = myDstu2Conformance;
//			return (Response) response.returnResponse(ParseAction.create(myDstu2CapabilityStatement), Constants.STATUS_HTTP_200_OK, true, null, getResourceType().getSimpleName());
		}
		
		if (conformance != null) {
			Set<SummaryEnum> summaryMode = Collections.emptySet();
			return (Response) response.streamResponseAsResource(conformance, false, summaryMode, Constants.STATUS_HTTP_200_OK, null, true, false);
		}
		return (Response) response.returnResponse(null, Constants.STATUS_HTTP_500_INTERNAL_ERROR, true, null, getResourceType().getSimpleName());
	}

	/**
	 * This method will add a provider to the conformance. This method is almost an exact copy of {@link ca.uhn.fhir.rest.server.RestfulServer#findResourceMethods }
	 * 
	 * @param theProvider
	 *           an instance of the provider interface
	 * @param theProviderInterface
	 *           the class describing the providers interface
	 * @return the numbers of basemethodbindings added
	 * @see ca.uhn.fhir.rest.server.RestfulServer#findResourceMethods
	 */
	public int addProvider(IResourceProvider theProvider, Class<? extends IResourceProvider> theProviderInterface) throws ConfigurationException {
		int count = 0;

		for (Method m : ReflectionUtil.getDeclaredMethods(theProviderInterface)) {
			BaseMethodBinding<?> foundMethodBinding = BaseMethodBinding.bindMethod(m, getFhirContext(), theProvider);
			if (foundMethodBinding == null) {
				continue;
			}

			count++;

			// if (foundMethodBinding instanceof ConformanceMethodBinding) {
			// myServerConformanceMethod = foundMethodBinding;
			// continue;
			// }

			if (!Modifier.isPublic(m.getModifiers())) {
				throw new ConfigurationException("Method '" + m.getName() + "' is not public, FHIR RESTful methods must be public");
			} else {
				if (Modifier.isStatic(m.getModifiers())) {
					throw new ConfigurationException("Method '" + m.getName() + "' is static, FHIR RESTful methods must not be static");
				} else {
					ourLog.debug("Scanning public method: {}#{}", theProvider.getClass(), m.getName());

					String resourceName = foundMethodBinding.getResourceName();
					ResourceBinding resourceBinding;
					if (resourceName == null) {
						resourceBinding = myServerBinding;
					} else {
						RuntimeResourceDefinition definition = getFhirContext().getResourceDefinition(resourceName);
						if (myResourceNameToBinding.containsKey(definition.getName())) {
							resourceBinding = myResourceNameToBinding.get(definition.getName());
						} else {
							resourceBinding = new ResourceBinding();
							resourceBinding.setResourceName(resourceName);
							myResourceNameToBinding.put(resourceName, resourceBinding);
						}
					}

					List<Class<?>> allowableParams = foundMethodBinding.getAllowableParamAnnotations();
					if (allowableParams != null) {
						for (Annotation[] nextParamAnnotations : m.getParameterAnnotations()) {
							for (Annotation annotation : nextParamAnnotations) {
								Package pack = annotation.annotationType().getPackage();
								if (pack.equals(IdParam.class.getPackage())) {
									if (!allowableParams.contains(annotation.annotationType())) {
										throw new ConfigurationException("Method[" + m.toString() + "] is not allowed to have a parameter annotated with " + annotation);
									}
								}
							}
						}
					}

					resourceBinding.addMethod(foundMethodBinding);
					ourLog.debug(" * Method: {}#{} is a handler", theProvider.getClass(), m.getName());
				}
			}
		}

		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<IBaseResource> getResourceType() {
		if (super.getFhirContext().getVersion().getVersion().equals(FhirVersionEnum.DSTU3)) {
			return Class.class.cast(CapabilityStatement.class);
		} else if (super.getFhirContext().getVersion().getVersion().equals(FhirVersionEnum.DSTU2)) {
			return Class.class.cast(ca.uhn.fhir.model.dstu2.resource.Conformance.class);
		}
		return null;
	}

}
