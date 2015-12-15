package ca.uhn.fhir.provider.impl;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.dao.IDaoFactory;
import ca.uhn.fhir.dao.IFhirSystemDao;
import ca.uhn.fhir.dao.IDaoConfig;
import ca.uhn.fhir.model.dstu21.composite.MetaDt;
import ca.uhn.fhir.model.dstu21.resource.Bundle;
import ca.uhn.fhir.model.dstu21.resource.Conformance;
import ca.uhn.fhir.model.dstu21.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu21.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu21.resource.Conformance.RestResourceSearchParam;
import ca.uhn.fhir.model.dstu21.valueset.ConditionalDeleteStatusEnum;
import ca.uhn.fhir.model.dstu21.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dstu21.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.dstu21.ServerConformanceProvider;
import ca.uhn.fhir.util.ExtensionConstants;
import net.sourceforge.cobertura.CoverageIgnore;

public class ConformanceProviderDstu21 extends ServerConformanceProvider {

	// ///////////////////////////////////
	// ////////   Spring Wiring   ////////
	// ///////////////////////////////////

	private IDaoFactory myDaoFactory;
	private IDaoConfig myDaoConfig;
	private RestfulServer myRestfulServer;

	@Required
	public void setSystemDaoFactory(IDaoFactory theDaoFactory) {
		this.myDaoFactory = theDaoFactory;
	}
	@Required
	public void setDaoConfig(IDaoConfig theDaoConfig) {
		this.myDaoConfig = theDaoConfig;
	}
	@Override
	@Required
	public void setRestfulServer(RestfulServer theRestfulServer) {
		this.myRestfulServer = theRestfulServer;
		super.setRestfulServer(theRestfulServer);
	}

	// ///////////////////////////////////
	// ///////////////////////////////////
	// ///////////////////////////////////

	private volatile Conformance myCachedValue;
	private String myImplementationDescription;
	private IFhirSystemDao<Bundle, MetaDt> mySystemDao;

	@SuppressWarnings("unchecked")
	public IFhirSystemDao<Bundle, MetaDt> getDao() {
		synchronized(this) {
			if (null == mySystemDao) {
				mySystemDao = (IFhirSystemDao<Bundle, MetaDt>)myDaoFactory.getSystemDao();
			}
		}
		return mySystemDao;
	}

	/**
	 * Constructor
	 */
	@CoverageIgnore
	public ConformanceProviderDstu21(){
		super();
		super.setCache(false);
	}

	/**
	 * Constructor
	 */
	public ConformanceProviderDstu21(RestfulServer theRestfulServer, IDaoFactory theDaoFactory, IDaoConfig theDaoConfig) {
		super(theRestfulServer);
		setRestfulServer(theRestfulServer);
		setSystemDaoFactory(theDaoFactory);
		setDaoConfig(theDaoConfig);
		super.setCache(false);
	}

	@Override
	public Conformance getServerConformance(HttpServletRequest theRequest) {
		Conformance retVal = myCachedValue;

		Map<String, Long> counts = getDao().getResourceCounts();

		FhirContext ctx = myRestfulServer.getFhirContext();

		retVal = super.getServerConformance(theRequest);
		for (Rest nextRest : retVal.getRest()) {

			for (RestResource nextResource : nextRest.getResource()) {

				ConditionalDeleteStatusEnum conditionalDelete = nextResource.getConditionalDeleteElement().getValueAsEnum();
				if (conditionalDelete == ConditionalDeleteStatusEnum.MULTIPLE_DELETES_SUPPORTED && myDaoConfig.isAllowMultipleDelete() == false) {
					nextResource.setConditionalDelete(ConditionalDeleteStatusEnum.SINGLE_DELETES_SUPPORTED);
				}

				// Add resource counts
				Long count = counts.get(nextResource.getTypeElement().getValueAsString());
				if (count != null) {
					nextResource.addUndeclaredExtension(false, ExtensionConstants.CONF_RESOURCE_COUNT, new DecimalDt(count));
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

		retVal.getImplementation().setDescription(myImplementationDescription);
		myCachedValue = retVal;
		return retVal;
	}

	@CoverageIgnore
	public void setImplementationDescription(String theImplDesc) {
		myImplementationDescription = theImplDesc;
	}
}
