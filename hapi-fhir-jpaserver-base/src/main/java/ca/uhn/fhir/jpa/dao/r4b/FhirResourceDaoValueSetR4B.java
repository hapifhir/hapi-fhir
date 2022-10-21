package ca.uhn.fhir.jpa.dao.r4b;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_40_50;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_43_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_40_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_43_50;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4b.model.CodeableConcept;
import org.hl7.fhir.r4b.model.Coding;
import org.hl7.fhir.r4b.model.ValueSet;

import java.util.Date;

import static ca.uhn.fhir.jpa.dao.FhirResourceDaoValueSetDstu2.toStringOrNull;
import static ca.uhn.fhir.jpa.dao.dstu3.FhirResourceDaoValueSetDstu3.vsValidateCodeOptions;

public class FhirResourceDaoValueSetR4B extends BaseHapiFhirResourceDao<ValueSet> implements IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> {

	@Override
	public ValueSet expand(IIdType theId, ValueSetExpansionOptions theOptions, RequestDetails theRequestDetails) {
		ValueSet source = read(theId, theRequestDetails);
		return expand(source, theOptions);
	}

	@Override
	public ValueSet expandByIdentifier(String theUri, ValueSetExpansionOptions theOptions) {
		org.hl7.fhir.r4.model.ValueSet canonicalOutput = myTerminologySvc.expandValueSet(theOptions, theUri);
		org.hl7.fhir.r5.model.ValueSet valueSetR5 = (org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_40_50.convertResource(canonicalOutput, new BaseAdvisor_40_50(false));
		return (ValueSet) VersionConvertorFactory_43_50.convertResource(valueSetR5, new BaseAdvisor_43_50(false));
	}

	@Override
	public ValueSet expand(ValueSet theSource, ValueSetExpansionOptions theOptions) {
		org.hl7.fhir.r5.model.ValueSet canonicalInputR5 = (org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_43_50.convertResource(theSource, new BaseAdvisor_43_50(false));
		org.hl7.fhir.r4.model.ValueSet canonicalInput = (org.hl7.fhir.r4.model.ValueSet) VersionConvertorFactory_40_50.convertResource(canonicalInputR5, new BaseAdvisor_40_50(false));
		org.hl7.fhir.r4.model.ValueSet canonicalOutput = myTerminologySvc.expandValueSet(theOptions, canonicalInput);
		org.hl7.fhir.r5.model.ValueSet outputR5 = (org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_40_50.convertResource(canonicalOutput, new BaseAdvisor_40_50(false));
		return (ValueSet) VersionConvertorFactory_43_50.convertResource(outputR5, new BaseAdvisor_43_50(false));
	}

	@Override
	public IValidationSupport.CodeValidationResult validateCode(IPrimitiveType<String> theValueSetIdentifier, IIdType theId, IPrimitiveType<String> theCode,
																					IPrimitiveType<String> theSystem, IPrimitiveType<String> theDisplay, Coding theCoding,
																					CodeableConcept theCodeableConcept, RequestDetails theRequestDetails) {
		return myTerminologySvc.validateCode(vsValidateCodeOptions(), theId, toStringOrNull(theValueSetIdentifier), toStringOrNull(theSystem), toStringOrNull(theCode), toStringOrNull(theDisplay), theCoding, theCodeableConcept);
	}

	@Override
	public void purgeCaches() {
		// nothing
	}

	@Override
	public ResourceTable updateEntity(RequestDetails theRequestDetails, IBaseResource theResource, IBasePersistedResource theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
												 boolean theUpdateVersion, TransactionDetails theTransactionDetails, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(theRequestDetails, theResource, theEntity, theDeletedTimestampOrNull, thePerformIndexing, theUpdateVersion, theTransactionDetails, theForceUpdate, theCreateNewHistoryEntry);

		if (getConfig().isPreExpandValueSets() && !retVal.isUnchangedInCurrentOperation()) {
			if (retVal.getDeleted() == null) {
				ValueSet valueSet = (ValueSet) theResource;
				org.hl7.fhir.r5.model.ValueSet valueSetR5 = (org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_43_50.convertResource(valueSet, new BaseAdvisor_43_50(false));
				org.hl7.fhir.r4.model.ValueSet valueSetR4 = (org.hl7.fhir.r4.model.ValueSet) VersionConvertorFactory_40_50.convertResource(valueSetR5, new BaseAdvisor_40_50(false));
				myTerminologySvc.storeTermValueSet(retVal, valueSetR4);
			} else {
				myTerminologySvc.deleteValueSetAndChildren(retVal);
			}
		}

		return retVal;
	}

}
