package ca.uhn.fhir.jpa.provider;

/*
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.ServerCapabilityStatementProvider;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.ExtensionConstants;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.FhirTerser;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement.ConditionalDeleteStatus;
import org.hl7.fhir.r4.model.CapabilityStatement.ResourceVersionPolicy;
import org.hl7.fhir.r4.model.Meta;

import javax.annotation.Nonnull;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * R4+ Only
 */
public class JpaCapabilityStatementProvider extends ServerCapabilityStatementProvider {

	private final FhirContext myContext;
	private DaoConfig myDaoConfig;
	private String myImplementationDescription;
	private boolean myIncludeResourceCounts;
	private IFhirSystemDao<?, ?> mySystemDao;

	/**
	 * Constructor
	 */
	public JpaCapabilityStatementProvider(@Nonnull RestfulServer theRestfulServer, @Nonnull IFhirSystemDao<?, ?> theSystemDao, @Nonnull DaoConfig theDaoConfig, @Nonnull ISearchParamRegistry theSearchParamRegistry, IValidationSupport theValidationSupport) {
		super(theRestfulServer, theSearchParamRegistry, theValidationSupport);

		Validate.notNull(theRestfulServer);
		Validate.notNull(theSystemDao);
		Validate.notNull(theDaoConfig);
		Validate.notNull(theSearchParamRegistry);

		myContext = theRestfulServer.getFhirContext();
		mySystemDao = theSystemDao;
		myDaoConfig = theDaoConfig;
		setIncludeResourceCounts(true);
	}

	@Override
	protected void postProcess(FhirTerser theTerser, IBaseConformance theCapabilityStatement) {
		super.postProcess(theTerser, theCapabilityStatement);

		if (isNotBlank(myImplementationDescription)) {
			theTerser.setElement(theCapabilityStatement, "implementation.description", myImplementationDescription);
		}

		theTerser.addElement(theCapabilityStatement, "patchFormat", Constants.CT_FHIR_JSON_NEW);
		theTerser.addElement(theCapabilityStatement, "patchFormat", Constants.CT_FHIR_XML_NEW);
		theTerser.addElement(theCapabilityStatement, "patchFormat", Constants.CT_JSON_PATCH);
		theTerser.addElement(theCapabilityStatement, "patchFormat", Constants.CT_XML_PATCH);
	}

	@Override
	protected void postProcessRest(FhirTerser theTerser, IBase theRest) {
		super.postProcessRest(theTerser, theRest);

		if (myDaoConfig.getSupportedSubscriptionTypes().contains(org.hl7.fhir.dstu2.model.Subscription.SubscriptionChannelType.WEBSOCKET)) {
			if (isNotBlank(myDaoConfig.getWebsocketContextPath())) {
				ExtensionUtil.setExtension(myContext, theRest, Constants.CAPABILITYSTATEMENT_WEBSOCKET_URL, "uri", myDaoConfig.getWebsocketContextPath());
			}
		}

	}

	@Override
	protected void postProcessRestResource(FhirTerser theTerser, IBase theResource, String theResourceName) {
		super.postProcessRestResource(theTerser, theResource, theResourceName);

		theTerser.addElement(theResource, "versioning", ResourceVersionPolicy.VERSIONEDUPDATE.toCode());

		if (myDaoConfig.isAllowMultipleDelete()) {
			theTerser.addElement(theResource, "conditionalDelete", ConditionalDeleteStatus.MULTIPLE.toCode());
		} else {
			theTerser.addElement(theResource, "conditionalDelete", ConditionalDeleteStatus.SINGLE.toCode());
		}

		// Add resource counts
		if (myIncludeResourceCounts) {
			Map<String, Long> counts = mySystemDao.getResourceCountsFromCache();
			if (counts != null) {
				Long count = counts.get(theResourceName);
				if (count != null) {
					ExtensionUtil.setExtension(myContext, theResource, ExtensionConstants.CONF_RESOURCE_COUNT, "decimal", Long.toString(count));
				}
			}
		}

	}

	public boolean isIncludeResourceCounts() {
		return myIncludeResourceCounts;
	}

	public void setIncludeResourceCounts(boolean theIncludeResourceCounts) {
		myIncludeResourceCounts = theIncludeResourceCounts;
	}

	public void setDaoConfig(DaoConfig myDaoConfig) {
		this.myDaoConfig = myDaoConfig;
	}

	@CoverageIgnore
	public void setImplementationDescription(String theImplDesc) {
		myImplementationDescription = theImplDesc;
	}

	@CoverageIgnore
	public void setSystemDao(IFhirSystemDao<Bundle, Meta> mySystemDao) {
		this.mySystemDao = mySystemDao;
	}

	@Override
	protected boolean searchParamEnabled(String theSearchParam) {
		return !Constants.PARAM_FILTER.equals(theSearchParam) || myDaoConfig.isFilterParameterEnabled();
	}
}
