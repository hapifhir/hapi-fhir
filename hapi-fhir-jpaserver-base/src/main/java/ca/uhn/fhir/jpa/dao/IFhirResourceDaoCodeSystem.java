package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.codesystems.ConceptSubsumptionOutcome;

import javax.annotation.Nonnull;
import java.util.List;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

public interface IFhirResourceDaoCodeSystem<T extends IBaseResource, CD, CC> extends IFhirResourceDao<T> {

	List<IIdType> findCodeSystemIdsContainingSystemAndCode(String theCode, String theSystem, RequestDetails theRequest);

	@Nonnull
	IContextValidationSupport.LookupCodeResult lookupCode(IPrimitiveType<String> theCode, IPrimitiveType<String> theSystem, CD theCoding, RequestDetails theRequestDetails);

	SubsumesResult subsumes(IPrimitiveType<String> theCodeA, IPrimitiveType<String> theCodeB, IPrimitiveType<String> theSystem, CD theCodingA, CD theCodingB, RequestDetails theRequestDetails);

	class SubsumesResult {

		private final ConceptSubsumptionOutcome myOutcome;

		public SubsumesResult(ConceptSubsumptionOutcome theOutcome) {
			myOutcome = theOutcome;
		}

		public ConceptSubsumptionOutcome getOutcome() {
			return myOutcome;
		}

		@SuppressWarnings("unchecked")
		public IBaseParameters toParameters(FhirContext theFhirContext) {
			IBaseParameters retVal = ParametersUtil.newInstance(theFhirContext);

			IPrimitiveType<String> outcomeValue = (IPrimitiveType<String>) theFhirContext.getElementDefinition("code").newInstance();
			outcomeValue.setValueAsString(getOutcome().toCode());
			ParametersUtil.addParameterToParameters(theFhirContext, retVal, "outcome", outcomeValue);

			return retVal;
		}
	}


}
