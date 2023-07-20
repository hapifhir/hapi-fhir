/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.cdshooks.svc.cr;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.cr.common.IRepositoryFactory;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.PlanDefinition;
import org.opencds.cqf.fhir.api.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;

public class CdsCrService implements ICdsCrService {
	@Autowired
	FhirContext myFhirContext;

	@Autowired
	IRepositoryFactory myRepositoryFactory;

	private final DaoRegistry myDaoRegistry;
	private final String myServiceId;
	private final CdsCrResolverStu3 myCdsCrResolverStu3;
	private final CdsCrResolverR4 myCdsCrResolverR4;
	private final CdsCrResolverR5 myCdsCrResolverR5;
	private IBaseResource myPlanDefinition;

	public CdsCrService(DaoRegistry theDaoRegistry, String theServiceId) {
		myDaoRegistry = theDaoRegistry;
		myServiceId = theServiceId;
		myCdsCrResolverStu3 = new CdsCrResolverStu3();
		myCdsCrResolverR4 = new CdsCrResolverR4(myDaoRegistry);
		myCdsCrResolverR5 = new CdsCrResolverR5();
	}

	public Object invoke(IModelJson theJson) {
		var requestDetails = new SystemRequestDetails();
		var repo = myRepositoryFactory.create(requestDetails);
		return invoke(repo, theJson);
	}

	private Object invoke(Repository theRepository, IModelJson theJson) {
		var operationName = myFhirContext.getVersion().getVersion() == FhirVersionEnum.R4
			? ProviderConstants.CR_OPERATION_R5_APPLY
			: ProviderConstants.CR_OPERATION_APPLY;
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
				return myCdsCrResolverStu3.encodeResponse(theRepository.invoke(
					org.hl7.fhir.dstu3.model.PlanDefinition.class, operationName, myCdsCrResolverStu3.encodeParams(theJson), org.hl7.fhir.dstu3.model.CarePlan.class, Collections.emptyMap()));
			case R4:
				return myCdsCrResolverR4.encodeResponse(theRepository.invoke(
					org.hl7.fhir.r4.model.PlanDefinition.class, operationName, myCdsCrResolverR4.encodeParams((CdsServiceRequestJson) theJson), org.hl7.fhir.r4.model.Bundle.class, Collections.emptyMap()));
			case R5:
				return myCdsCrResolverR5.encodeResponse(theRepository.invoke(
					org.hl7.fhir.r5.model.PlanDefinition.class, operationName, myCdsCrResolverR5.encodeParams(theJson), org.hl7.fhir.r5.model.Bundle.class, Collections.emptyMap()));
			default:
				return null;
		}
	}

	private void setMyPlanDefinition(Repository theRepository) {
		// myPlanDefinition = theRepository.read(PlanDefinition.class, new IdType(myServiceId));
	}
}
