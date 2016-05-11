package ca.uhn.fhir.jpa.provider.dstu3;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Conformance;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Conformance.ConditionalDeleteStatus;
import org.hl7.fhir.dstu3.model.Conformance.ConformanceRestComponent;
import org.hl7.fhir.dstu3.model.Conformance.ConformanceRestResourceComponent;
import org.hl7.fhir.dstu3.model.Conformance.ConformanceRestResourceSearchParamComponent;
import org.hl7.fhir.dstu3.model.Enumerations.SearchParamType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.ExtensionConstants;

public class JpaConformanceProviderDstu3 extends org.hl7.fhir.dstu3.hapi.rest.server.ServerConformanceProvider {

	private volatile Conformance myCachedValue;
	private DaoConfig myDaoConfig;
	private String myImplementationDescription;
	private RestfulServer myRestfulServer;
	private IFhirSystemDao<Bundle, Meta> mySystemDao;

	/**
	 * Constructor
	 */
	@CoverageIgnore
	public JpaConformanceProviderDstu3(){
		super();
		super.setCache(false);
	}

	/**
	 * Constructor
	 */
	public JpaConformanceProviderDstu3(RestfulServer theRestfulServer, IFhirSystemDao<Bundle, Meta> theSystemDao, DaoConfig theDaoConfig) {
		super(theRestfulServer);
		myRestfulServer = theRestfulServer;
		mySystemDao = theSystemDao;
		myDaoConfig = theDaoConfig;
		super.setCache(false);
	}

	@Override
	public Conformance getServerConformance(HttpServletRequest theRequest) {
		Conformance retVal = myCachedValue;

		Map<String, Long> counts = mySystemDao.getResourceCounts();

		FhirContext ctx = myRestfulServer.getFhirContext();

		retVal = super.getServerConformance(theRequest);
		for (ConformanceRestComponent nextRest : retVal.getRest()) {

			for (ConformanceRestResourceComponent nextResource : nextRest.getResource()) {

				ConditionalDeleteStatus conditionalDelete = nextResource.getConditionalDelete();
				if (conditionalDelete == ConditionalDeleteStatus.MULTIPLE && myDaoConfig.isAllowMultipleDelete() == false) {
					nextResource.setConditionalDelete(ConditionalDeleteStatus.SINGLE);
				}

				// Add resource counts
				Long count = counts.get(nextResource.getTypeElement().getValueAsString());
				if (count != null) {
					nextResource.addExtension(new Extension(ExtensionConstants.CONF_RESOURCE_COUNT, new DecimalType(count)));
				}

				// Add chained params
				for (ConformanceRestResourceSearchParamComponent nextParam : nextResource.getSearchParam()) {
					if (nextParam.getType() == SearchParamType.REFERENCE) {
						List<CodeType> targets = nextParam.getTarget();
						for (CodeType next : targets) {
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
