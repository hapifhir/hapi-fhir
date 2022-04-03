package ca.uhn.fhir.jaxrs.server;

/*
 * #%L
 * HAPI FHIR JAX-RS Server
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

import ca.uhn.fhir.i18n.Msg;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest.Builder;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.server.IRestfulResponse;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.ResourceBinding;
import ca.uhn.fhir.rest.server.RestfulServerConfiguration;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.provider.ServerCapabilityStatementProvider;
import ca.uhn.fhir.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu2.hapi.rest.server.ServerConformanceProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

/**
 * This is the conformance provider for the jax rs servers. It requires all providers to be registered during startup because the conformance profile is generated during the postconstruct phase.
 *
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public abstract class AbstractJaxRsConformanceProvider extends AbstractJaxRsProvider implements IResourceProvider {

	/**
	 * the logger
	 */
	private static final org.slf4j.Logger ourLog = LoggerFactory.getLogger(AbstractJaxRsConformanceProvider.class);
	/**
	 * the server bindings
	 */
	private ResourceBinding myServerBinding = new ResourceBinding();
	/**
	 * the resource bindings
	 */
	private ConcurrentHashMap<String, ResourceBinding> myResourceNameToBinding = new ConcurrentHashMap<String, ResourceBinding>();
	/**
	 * the server configuration
	 */
	private RestfulServerConfiguration serverConfiguration = new RestfulServerConfiguration();

	/**
	 * the conformance. It is created once during startup
	 */
	private org.hl7.fhir.r4.model.CapabilityStatement myR4CapabilityStatement;
	private org.hl7.fhir.dstu3.model.CapabilityStatement myDstu3CapabilityStatement;
	private org.hl7.fhir.dstu2016may.model.Conformance myDstu2_1Conformance;
	private org.hl7.fhir.dstu2.model.Conformance myDstu2Hl7OrgConformance;
	private ca.uhn.fhir.model.dstu2.resource.Conformance myDstu2Conformance;
	private boolean myInitialized;

	/**
	 * Constructor allowing the description, servername and server to be set
	 *
	 * @param implementationDescription the implementation description. If null, "" is used
	 * @param serverName                the server name. If null, "" is used
	 * @param serverVersion             the server version. If null, "" is used
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
	 * @param ctx                       the {@link FhirContext} instance.
	 * @param implementationDescription the implementation description. If null, "" is used
	 * @param serverName                the server name. If null, "" is used
	 * @param serverVersion             the server version. If null, "" is used
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
	protected synchronized void setUpPostConstruct() {
		if (myInitialized) {
			return;
		}

		ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> providers = getProviders();
		for (Entry<Class<? extends IResourceProvider>, IResourceProvider> provider : providers.entrySet()) {
			addProvider(provider.getValue(), provider.getKey());
		}
		List<BaseMethodBinding<?>> serverBindings = new ArrayList<BaseMethodBinding<?>>();
		for (ResourceBinding baseMethodBinding : myResourceNameToBinding.values()) {
			serverBindings.addAll(baseMethodBinding.getMethodBindings());
		}
		serverConfiguration.setServerBindings(serverBindings);
		serverConfiguration.setResourceBindings(new LinkedList<ResourceBinding>(myResourceNameToBinding.values()));
		serverConfiguration.computeSharedSupertypeForResourcePerName(providers.values());
		HardcodedServerAddressStrategy hardcodedServerAddressStrategy = new HardcodedServerAddressStrategy();
		hardcodedServerAddressStrategy.setValue(getBaseForServer());
		serverConfiguration.setServerAddressStrategy(hardcodedServerAddressStrategy);
		FhirVersionEnum fhirContextVersion = super.getFhirContext().getVersion().getVersion();
		switch (fhirContextVersion) {
			case R4:
				ServerCapabilityStatementProvider r4ServerCapabilityStatementProvider = new ServerCapabilityStatementProvider(getFhirContext(), serverConfiguration);
				myR4CapabilityStatement = (CapabilityStatement) r4ServerCapabilityStatementProvider.getServerConformance(null, null);
				break;
			case DSTU3:
				org.hl7.fhir.dstu3.hapi.rest.server.ServerCapabilityStatementProvider dstu3ServerCapabilityStatementProvider = new org.hl7.fhir.dstu3.hapi.rest.server.ServerCapabilityStatementProvider(serverConfiguration);
				myDstu3CapabilityStatement = dstu3ServerCapabilityStatementProvider.getServerConformance(null, null);
				break;
			case DSTU2_1:
				org.hl7.fhir.dstu2016may.hapi.rest.server.ServerConformanceProvider dstu2_1ServerConformanceProvider = new org.hl7.fhir.dstu2016may.hapi.rest.server.ServerConformanceProvider(serverConfiguration);
				myDstu2_1Conformance = dstu2_1ServerConformanceProvider.getServerConformance(null, null);
				break;
			case DSTU2_HL7ORG:
				ServerConformanceProvider dstu2Hl7OrgServerConformanceProvider = new ServerConformanceProvider(serverConfiguration);
				myDstu2Hl7OrgConformance = dstu2Hl7OrgServerConformanceProvider.getServerConformance(null, null);
				break;
			case DSTU2:
				ca.uhn.fhir.rest.server.provider.dstu2.ServerConformanceProvider dstu2ServerConformanceProvider = new ca.uhn.fhir.rest.server.provider.dstu2.ServerConformanceProvider(serverConfiguration);
				myDstu2Conformance = dstu2ServerConformanceProvider.getServerConformance(null, null);
				break;
			default:
				throw new ConfigurationException(Msg.code(591) + "Unsupported Fhir version: " + fhirContextVersion);
		}

		myInitialized = true;
	}

	/**
	 * This method must return all the resource providers which need to be included in the conformance
	 *
	 * @return a map of the resource providers and their corresponding classes. This class needs to be given explicitly because retrieving the interface using {@link Object#getClass()} may not give the
	 * correct interface in a jee environment.
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
		setUpPostConstruct();

		Builder request = getRequest(RequestTypeEnum.OPTIONS, RestOperationTypeEnum.METADATA);
		IRestfulResponse response = request.build().getResponse();
		response.addHeader(Constants.HEADER_CORS_ALLOW_ORIGIN, "*");

		IBaseResource conformance;
		FhirVersionEnum fhirContextVersion = super.getFhirContext().getVersion().getVersion();
		switch (fhirContextVersion) {
			case R4:
				conformance = myR4CapabilityStatement;
				break;
			case DSTU3:
				conformance = myDstu3CapabilityStatement;
				break;
			case DSTU2_1:
				conformance = myDstu2_1Conformance;
				break;
			case DSTU2_HL7ORG:
				conformance = myDstu2Hl7OrgConformance;
				break;
			case DSTU2:
				conformance = myDstu2Conformance;
				break;
			default:
				throw new ConfigurationException(Msg.code(592) + "Unsupported Fhir version: " + fhirContextVersion);
		}

		if (conformance != null) {
			Set<SummaryEnum> summaryMode = Collections.emptySet();
			return (Response) response.streamResponseAsResource(conformance, false, summaryMode, Constants.STATUS_HTTP_200_OK, null, true, false);
		}
		return (Response) response.returnResponse(null, Constants.STATUS_HTTP_500_INTERNAL_ERROR, true, null, getResourceType().getSimpleName());
	}

	/**
	 * This method will add a provider to the conformance. This method is almost an exact copy of {@link ca.uhn.fhir.rest.server.RestfulServer#findResourceMethods(Object)}
	 *
	 * @param theProvider          an instance of the provider interface
	 * @param theProviderInterface the class describing the providers interface
	 * @return the numbers of basemethodbindings added
	 * @see ca.uhn.fhir.rest.server.RestfulServer#findResourceMethods(Object)
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
				throw new ConfigurationException(Msg.code(593) + "Method '" + m.getName() + "' is not public, FHIR RESTful methods must be public");
			} else {
				if (Modifier.isStatic(m.getModifiers())) {
					throw new ConfigurationException(Msg.code(594) + "Method '" + m.getName() + "' is static, FHIR RESTful methods must not be static");
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
										throw new ConfigurationException(Msg.code(595) + "Method[" + m.toString() + "] is not allowed to have a parameter annotated with " + annotation);
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
		FhirVersionEnum fhirContextVersion = super.getFhirContext().getVersion().getVersion();
		switch (fhirContextVersion) {
			case R4:
				return Class.class.cast(org.hl7.fhir.r4.model.CapabilityStatement.class);
			case DSTU3:
				return Class.class.cast(org.hl7.fhir.dstu3.model.CapabilityStatement.class);
			case DSTU2_1:
				return Class.class.cast(org.hl7.fhir.dstu2016may.model.Conformance.class);
			case DSTU2_HL7ORG:
				return Class.class.cast(org.hl7.fhir.dstu2.model.Conformance.class);
			case DSTU2:
				return Class.class.cast(ca.uhn.fhir.model.dstu2.resource.Conformance.class);
			default:
				throw new ConfigurationException(Msg.code(596) + "Unsupported Fhir version: " + fhirContextVersion);
		}
	}

}
