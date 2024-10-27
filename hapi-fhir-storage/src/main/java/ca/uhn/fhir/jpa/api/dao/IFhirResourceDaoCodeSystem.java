/*
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.api.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.codesystems.ConceptSubsumptionOutcome;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface IFhirResourceDaoCodeSystem<T extends IBaseResource> extends IFhirResourceDao<T> {

	List<IIdType> findCodeSystemIdsContainingSystemAndCode(String theCode, String theSystem, RequestDetails theRequest);

	@Transactional
	@Nonnull
	IValidationSupport.LookupCodeResult lookupCode(
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theSystem,
			IBaseCoding theCoding,
			RequestDetails theRequestDetails);

	@Nonnull
	IValidationSupport.LookupCodeResult lookupCode(
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theSystem,
			IBaseCoding theCoding,
			IPrimitiveType<String> theDisplayLanguage,
			RequestDetails theRequestDetails);

	@Nonnull
	IValidationSupport.LookupCodeResult lookupCode(
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theSystem,
			IBaseCoding theCoding,
			IPrimitiveType<String> theDisplayLanguage,
			Collection<IPrimitiveType<String>> thePropertyNames,
			RequestDetails theRequestDetails);

	SubsumesResult subsumes(
			IPrimitiveType<String> theCodeA,
			IPrimitiveType<String> theCodeB,
			IPrimitiveType<String> theSystem,
			IBaseCoding theCodingA,
			IBaseCoding theCodingB,
			RequestDetails theRequestDetails);

	@Nonnull
	IValidationSupport.CodeValidationResult validateCode(
			IIdType theCodeSystemId,
			IPrimitiveType<String> theCodeSystemUrl,
			IPrimitiveType<String> theVersion,
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theDisplay,
			IBaseCoding theCoding,
			IBaseDatatype theCodeableConcept,
			RequestDetails theRequestDetails);

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

			IPrimitiveType<String> outcomeValue = (IPrimitiveType<String>)
					theFhirContext.getElementDefinition("code").newInstance();
			outcomeValue.setValueAsString(getOutcome().toCode());
			ParametersUtil.addParameterToParameters(theFhirContext, retVal, "outcome", outcomeValue);

			return retVal;
		}
	}
}
