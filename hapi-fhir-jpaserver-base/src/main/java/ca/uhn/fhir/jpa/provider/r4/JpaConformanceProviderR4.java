package ca.uhn.fhir.jpa.provider.r4;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.ExtensionConstants;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.ConditionalDeleteStatus;
import org.hl7.fhir.r4.model.CapabilityStatement.ResourceVersionPolicy;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Enumerations.SearchParamType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.UriType;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JpaConformanceProviderR4 extends org.hl7.fhir.r4.hapi.rest.server.ServerCapabilityStatementProvider {

	private volatile CapabilityStatement myCachedValue;
	private DaoConfig myDaoConfig;
	private ISearchParamRegistry mySearchParamRegistry;
	private String myImplementationDescription;
	private boolean myIncludeResourceCounts;
	private RestfulServer myRestfulServer;
	private IFhirSystemDao<Bundle, Meta> mySystemDao;

	/**
	 * Constructor
	 */
	@CoverageIgnore
	public JpaConformanceProviderR4() {
		super();
		super.setCache(false);
		setIncludeResourceCounts(true);
	}

	/**
	 * Constructor
	 */
	public JpaConformanceProviderR4(RestfulServer theRestfulServer, IFhirSystemDao<Bundle, Meta> theSystemDao, DaoConfig theDaoConfig, ISearchParamRegistry theSearchParamRegistry) {
		super(theRestfulServer);

		Validate.notNull(theRestfulServer);
		Validate.notNull(theSystemDao);
		Validate.notNull(theDaoConfig);

		myRestfulServer = theRestfulServer;
		mySystemDao = theSystemDao;
		myDaoConfig = theDaoConfig;
		super.setCache(false);
		setIncludeResourceCounts(true);
		setSearchParamRegistry(theSearchParamRegistry);
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
				if (conditionalDelete == ConditionalDeleteStatus.MULTIPLE && myDaoConfig.isAllowMultipleDelete() == false) {
					nextResource.setConditionalDelete(ConditionalDeleteStatus.SINGLE);
				}

				// Add resource counts
				Long count = counts.get(nextResource.getTypeElement().getValueAsString());
				if (count != null) {
					nextResource.addExtension(new Extension(ExtensionConstants.CONF_RESOURCE_COUNT, new DecimalType(count)));
				}

				nextResource.getSearchParam().clear();
				String resourceName = nextResource.getType();
				RuntimeResourceDefinition resourceDef = myRestfulServer.getFhirContext().getResourceDefinition(resourceName);
				Collection<RuntimeSearchParam> searchParams = mySearchParamRegistry.getSearchParamsByResourceType(resourceDef);
				for (RuntimeSearchParam runtimeSp : searchParams) {
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
						case SPECIAL:
							confSp.setType(SearchParamType.SPECIAL);
							break;
						case HAS:
							// Shouldn't happen
							break;
					}

				}

			}
		}

		if (myDaoConfig.getSupportedSubscriptionTypes().contains(org.hl7.fhir.dstu2.model.Subscription.SubscriptionChannelType.WEBSOCKET)) {
			if (isNotBlank(myDaoConfig.getWebsocketContextPath())) {
				Extension websocketExtension = new Extension();
				websocketExtension.setUrl(Constants.CAPABILITYSTATEMENT_WEBSOCKET_URL);
				websocketExtension.setValue(new UriType(myDaoConfig.getWebsocketContextPath()));
				retVal.getRestFirstRep().addExtension(websocketExtension);
			}
		}

		massage(retVal);

		retVal.getImplementation().setDescription(myImplementationDescription);
		myCachedValue = retVal;
		return retVal;
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

	public void setDaoConfig(DaoConfig myDaoConfig) {
		this.myDaoConfig = myDaoConfig;
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
