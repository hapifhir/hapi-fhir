package ca.uhn.fhir.jpa.provider;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResourceSearchParam;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.dstu2.ServerConformanceProvider;
import ca.uhn.fhir.util.ExtensionConstants;

public class JpaConformanceProviderDstu2 extends ServerConformanceProvider {

	private String myImplementationDescription;
	private IFhirSystemDao<Bundle> mySystemDao;
	private volatile Conformance myCachedValue;
	private RestfulServer myRestfulServer;

	public JpaConformanceProviderDstu2(RestfulServer theRestfulServer, IFhirSystemDao<Bundle> theSystemDao) {
		super(theRestfulServer);
		myRestfulServer = theRestfulServer;
		mySystemDao = theSystemDao;
		super.setCache(false);
	}

	@Override
	public Conformance getServerConformance(HttpServletRequest theRequest) {
		Conformance retVal = myCachedValue;

		Map<String, Long> counts = mySystemDao.getResourceCounts();

		FhirContext ctx = myRestfulServer.getFhirContext();
		
		retVal = super.getServerConformance(theRequest);
		for (Rest nextRest : retVal.getRest()) {
			for (RestResource nextResource : nextRest.getResource()) {

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

	public void setImplementationDescription(String theImplDesc) {
		myImplementationDescription = theImplDesc;
	}

}
