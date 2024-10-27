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
package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResourceSearchParam;
import ca.uhn.fhir.model.dstu2.valueset.ConditionalDeleteStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.dstu2.ServerConformanceProvider;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.ExtensionConstants;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JpaConformanceProviderDstu2 extends ServerConformanceProvider {

	private volatile Conformance myCachedValue;
	private JpaStorageSettings myStorageSettings;
	private String myImplementationDescription;
	private boolean myIncludeResourceCounts;
	private RestfulServer myRestfulServer;
	private IFhirSystemDao<Bundle, MetaDt> mySystemDao;
	private ResourceCountCache myResourceCountsCache;

	/**
	 * Constructor
	 */
	@CoverageIgnore
	public JpaConformanceProviderDstu2() {
		super();
		super.setCache(false);
		setIncludeResourceCounts(true);
	}

	/**
	 * Constructor
	 */
	public JpaConformanceProviderDstu2(
			RestfulServer theRestfulServer,
			IFhirSystemDao<Bundle, MetaDt> theSystemDao,
			JpaStorageSettings theStorageSettings) {
		super(theRestfulServer);
		myRestfulServer = theRestfulServer;
		mySystemDao = theSystemDao;
		myStorageSettings = theStorageSettings;
		super.setCache(false);
		setIncludeResourceCounts(true);
	}

	@Override
	public Conformance getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails) {
		Conformance retVal = myCachedValue;

		Map<String, Long> counts = null;
		if (myIncludeResourceCounts) {
			counts = mySystemDao.getResourceCountsFromCache();
		}
		counts = defaultIfNull(counts, Collections.emptyMap());

		FhirContext ctx = myRestfulServer.getFhirContext();

		retVal = super.getServerConformance(theRequest, theRequestDetails);
		for (Rest nextRest : retVal.getRest()) {

			for (RestResource nextResource : nextRest.getResource()) {

				ConditionalDeleteStatusEnum conditionalDelete =
						nextResource.getConditionalDeleteElement().getValueAsEnum();
				if (conditionalDelete == ConditionalDeleteStatusEnum.MULTIPLE_DELETES_SUPPORTED
						&& myStorageSettings.isAllowMultipleDelete() == false) {
					nextResource.setConditionalDelete(ConditionalDeleteStatusEnum.SINGLE_DELETES_SUPPORTED);
				}

				// Add resource counts
				Long count = counts.get(nextResource.getTypeElement().getValueAsString());
				if (count != null) {
					nextResource.addUndeclaredExtension(
							false, ExtensionConstants.CONF_RESOURCE_COUNT, new DecimalDt(count));
				}

				// Add chained params
				for (RestResourceSearchParam nextParam : nextResource.getSearchParam()) {
					if (nextParam.getTypeElement().getValueAsEnum() == SearchParamTypeEnum.REFERENCE) {
						List<BoundCodeDt<ResourceTypeEnum>> targets = nextParam.getTarget();
						for (BoundCodeDt<ResourceTypeEnum> next : targets) {
							RuntimeResourceDefinition def = ctx.getResourceDefinition(next.getValue());
							for (RuntimeSearchParam nextChainedParam : def.getSearchParams()) {
								nextParam.addChain(nextChainedParam.getName());
							}
						}
					}
				}
			}
		}

		if (isNotBlank(myImplementationDescription)) {
			retVal.getImplementation().setDescription(myImplementationDescription);
		}
		myCachedValue = retVal;
		return retVal;
	}

	public boolean isIncludeResourceCounts() {
		return myIncludeResourceCounts;
	}

	public void setStorageSettings(JpaStorageSettings theStorageSettings) {
		this.myStorageSettings = theStorageSettings;
	}

	@CoverageIgnore
	public void setImplementationDescription(String theImplDesc) {
		myImplementationDescription = theImplDesc;
	}

	public void setIncludeResourceCounts(boolean theIncludeResourceCounts) {
		myIncludeResourceCounts = theIncludeResourceCounts;
	}

	@Override
	public void setRestfulServer(RestfulServer theRestfulServer) {
		this.myRestfulServer = theRestfulServer;
		super.setRestfulServer(theRestfulServer);
	}

	@CoverageIgnore
	public void setSystemDao(IFhirSystemDao<Bundle, MetaDt> mySystemDao) {
		this.mySystemDao = mySystemDao;
	}
}
