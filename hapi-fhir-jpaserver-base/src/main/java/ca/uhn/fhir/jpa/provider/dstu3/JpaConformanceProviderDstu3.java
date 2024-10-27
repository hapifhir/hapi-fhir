/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerConfiguration;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.ExtensionConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.ConditionalDeleteStatus;
import org.hl7.fhir.dstu3.model.CapabilityStatement.ResourceVersionPolicy;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.Enumerations.SearchParamType;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.Meta;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JpaConformanceProviderDstu3 extends org.hl7.fhir.dstu3.hapi.rest.server.ServerCapabilityStatementProvider {

	private volatile CapabilityStatement myCachedValue;
	private JpaStorageSettings myStorageSettings;
	private ISearchParamRegistry mySearchParamRegistry;
	private String myImplementationDescription;
	private boolean myIncludeResourceCounts;
	private RestfulServer myRestfulServer;
	private IFhirSystemDao<Bundle, Meta> mySystemDao;

	private RestfulServerConfiguration myServerConfiguration;

	/**
	 * Constructor
	 */
	@CoverageIgnore
	public JpaConformanceProviderDstu3() {
		super();
		super.setCache(false);
		setIncludeResourceCounts(true);
	}

	/**
	 * Constructor
	 */
	public JpaConformanceProviderDstu3(
			RestfulServer theRestfulServer,
			IFhirSystemDao<Bundle, Meta> theSystemDao,
			JpaStorageSettings theStorageSettings,
			ISearchParamRegistry theSearchParamRegistry) {
		super(theRestfulServer);
		myRestfulServer = theRestfulServer;
		mySystemDao = theSystemDao;
		myStorageSettings = theStorageSettings;
		myServerConfiguration = theRestfulServer.createConfiguration();
		super.setCache(false);
		setSearchParamRegistry(theSearchParamRegistry);
		setIncludeResourceCounts(true);
	}

	public void setSearchParamRegistry(ISearchParamRegistry theSearchParamRegistry) {
		mySearchParamRegistry = theSearchParamRegistry;
	}

	@Override
	public CapabilityStatement getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails) {
		CapabilityStatement retVal = myCachedValue;

		Map<String, Long> counts = null;
		if (myIncludeResourceCounts) {
			counts = mySystemDao.getResourceCountsFromCache();
		}
		counts = defaultIfNull(counts, Collections.emptyMap());

		retVal = super.getServerConformance(theRequest, theRequestDetails);
		for (CapabilityStatementRestComponent nextRest : retVal.getRest()) {

			for (CapabilityStatementRestResourceComponent nextResource : nextRest.getResource()) {

				nextResource.setVersioning(ResourceVersionPolicy.VERSIONEDUPDATE);

				ConditionalDeleteStatus conditionalDelete = nextResource.getConditionalDelete();
				if (conditionalDelete == ConditionalDeleteStatus.MULTIPLE
						&& myStorageSettings.isAllowMultipleDelete() == false) {
					nextResource.setConditionalDelete(ConditionalDeleteStatus.SINGLE);
				}

				// Add resource counts
				Long count = counts.get(nextResource.getTypeElement().getValueAsString());
				if (count != null) {
					nextResource.addExtension(
							new Extension(ExtensionConstants.CONF_RESOURCE_COUNT, new DecimalType(count)));
				}

				nextResource.getSearchParam().clear();
				String resourceName = nextResource.getType();
				ResourceSearchParams searchParams = constructCompleteSearchParamList(resourceName);
				for (RuntimeSearchParam runtimeSp : searchParams.values()) {
					CapabilityStatementRestResourceSearchParamComponent confSp = nextResource.addSearchParam();

					confSp.setName(runtimeSp.getName());
					confSp.setDocumentation(runtimeSp.getDescription());
					confSp.setDefinition(runtimeSp.getUri());
					switch (runtimeSp.getParamType()) {
						case COMPOSITE:
							confSp.setType(SearchParamType.COMPOSITE);
							break;
						case DATE:
							confSp.setType(SearchParamType.DATE);
							break;
						case NUMBER:
							confSp.setType(SearchParamType.NUMBER);
							break;
						case QUANTITY:
							confSp.setType(SearchParamType.QUANTITY);
							break;
						case REFERENCE:
							confSp.setType(SearchParamType.REFERENCE);
							break;
						case STRING:
							confSp.setType(SearchParamType.STRING);
							break;
						case TOKEN:
							confSp.setType(SearchParamType.TOKEN);
							break;
						case URI:
							confSp.setType(SearchParamType.URI);
							break;
						case HAS:
							// Shouldn't happen
							break;
					}
				}

				updateIncludesList(nextResource, searchParams);
				updateRevIncludesList(nextResource, searchParams);
			}
		}

		massage(retVal);

		retVal.getImplementation().setDescription(myImplementationDescription);
		myCachedValue = retVal;
		return retVal;
	}

	private ResourceSearchParams constructCompleteSearchParamList(String theResourceName) {
		// Borrowed from
		// hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/provider/ServerCapabilityStatementProvider.java

		/*
		 * If we have an explicit registry (which will be the case in the JPA server) we use it as priority,
		 * but also fill in any gaps using params from the server itself. This makes sure we include
		 * global params like _lastUpdated
		 */
		ResourceSearchParams searchParams;
		ResourceSearchParams serverConfigurationActiveSearchParams =
				myServerConfiguration.getActiveSearchParams(theResourceName);
		if (mySearchParamRegistry != null) {
			searchParams =
					mySearchParamRegistry.getActiveSearchParams(theResourceName).makeCopy();
			if (searchParams == null) {
				return ResourceSearchParams.empty(theResourceName);
			}
			for (String nextBuiltInSpName : serverConfigurationActiveSearchParams.getSearchParamNames()) {
				if (nextBuiltInSpName.startsWith("_")
						&& !searchParams.containsParamName(nextBuiltInSpName)
						&& searchParamEnabled(nextBuiltInSpName)) {
					searchParams.put(nextBuiltInSpName, serverConfigurationActiveSearchParams.get(nextBuiltInSpName));
				}
			}
		} else {
			searchParams = serverConfigurationActiveSearchParams;
		}

		return searchParams;
	}

	protected boolean searchParamEnabled(String theSearchParam) {
		// Borrowed from
		// hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/provider/ServerCapabilityStatementProvider.java
		return !Constants.PARAM_FILTER.equals(theSearchParam) || myStorageSettings.isFilterParameterEnabled();
	}

	private void updateRevIncludesList(
			CapabilityStatementRestResourceComponent theNextResource, ResourceSearchParams theSearchParams) {
		// Add RevInclude to CapabilityStatement.rest.resource
		if (theNextResource.getSearchRevInclude().isEmpty()) {
			String resourcename = theNextResource.getType();
			Set<String> allResourceTypes =
					myServerConfiguration.collectMethodBindings().keySet();
			for (String otherResourceType : allResourceTypes) {
				if (isBlank(otherResourceType)) {
					continue;
				}
				ResourceSearchParams activeSearchParams =
						mySearchParamRegistry.getActiveSearchParams(otherResourceType);
				activeSearchParams.values().stream()
						.filter(t -> isNotBlank(t.getName()))
						.filter(t -> t.getTargets().contains(resourcename))
						.forEach(t -> theNextResource.addSearchRevInclude(otherResourceType + ":" + t.getName()));
			}
		}
	}

	private void updateIncludesList(
			CapabilityStatementRestResourceComponent theResource, ResourceSearchParams theSearchParams) {
		// Borrowed from
		// hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/provider/ServerCapabilityStatementProvider.java
		String resourceName = theResource.getType();
		if (theResource.getSearchInclude().isEmpty()) {
			List<String> includes = theSearchParams.values().stream()
					.filter(t -> t.getParamType() == RestSearchParameterTypeEnum.REFERENCE)
					.map(t -> resourceName + ":" + t.getName())
					.sorted()
					.collect(Collectors.toList());
			theResource.addSearchInclude("*");
			for (String nextInclude : includes) {
				theResource.addSearchInclude(nextInclude);
			}
		}
	}

	public boolean isIncludeResourceCounts() {
		return myIncludeResourceCounts;
	}

	public void setIncludeResourceCounts(boolean theIncludeResourceCounts) {
		myIncludeResourceCounts = theIncludeResourceCounts;
	}

	/**
	 * Subclasses may override
	 */
	protected void massage(CapabilityStatement theStatement) {
		// nothing
	}

	public void setStorageSettings(JpaStorageSettings theStorageSettings) {
		this.myStorageSettings = theStorageSettings;
	}

	@CoverageIgnore
	public void setImplementationDescription(String theImplDesc) {
		myImplementationDescription = theImplDesc;
	}

	@Override
	public void setRestfulServer(RestfulServer theRestfulServer) {
		this.myRestfulServer = theRestfulServer;
		super.setRestfulServer(theRestfulServer);
	}

	@CoverageIgnore
	public void setSystemDao(IFhirSystemDao<Bundle, Meta> mySystemDao) {
		this.mySystemDao = mySystemDao;
	}
}
