package ca.uhn.fhir.jpa.dao.dstu3;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_30_40;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_30_40;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Date;

import static ca.uhn.fhir.jpa.dao.FhirResourceDaoValueSetDstu2.toStringOrNull;

public class FhirResourceDaoValueSetDstu3 extends BaseHapiFhirResourceDao<ValueSet> implements IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> {

	@Override
	public org.hl7.fhir.dstu3.model.ValueSet expand(IIdType theId, ValueSetExpansionOptions theOptions, RequestDetails theRequestDetails) {
		org.hl7.fhir.dstu3.model.ValueSet source = read(theId, theRequestDetails);
		return expand(source, theOptions);
	}

	@Override
	public org.hl7.fhir.dstu3.model.ValueSet expand(org.hl7.fhir.dstu3.model.ValueSet theSource, ValueSetExpansionOptions theOptions) {
		org.hl7.fhir.r4.model.ValueSet canonicalInput = (org.hl7.fhir.r4.model.ValueSet) VersionConvertorFactory_30_40.convertResource(theSource, new BaseAdvisor_30_40(false));
		org.hl7.fhir.r4.model.ValueSet canonicalOutput = myTerminologySvc.expandValueSet(theOptions, canonicalInput);
		return (ValueSet) VersionConvertorFactory_30_40.convertResource(canonicalOutput, new BaseAdvisor_30_40(false));
	}

	@Override
	public org.hl7.fhir.dstu3.model.ValueSet expandByIdentifier(String theUri, ValueSetExpansionOptions theOptions) {
		org.hl7.fhir.r4.model.ValueSet canonicalOutput = myTerminologySvc.expandValueSet(theOptions, theUri);
		return (ValueSet) VersionConvertorFactory_30_40.convertResource(canonicalOutput, new BaseAdvisor_30_40(false));
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
				try {
					ValueSet valueSet = (ValueSet) theResource;
					org.hl7.fhir.r4.model.ValueSet converted = (org.hl7.fhir.r4.model.ValueSet) VersionConvertorFactory_30_40.convertResource(valueSet, new BaseAdvisor_30_40(false));
					myTerminologySvc.storeTermValueSet(retVal, converted);
				} catch (FHIRException fe) {
					throw new InternalErrorException(Msg.code(1080) + fe);
				}
			} else {
				myTerminologySvc.deleteValueSetAndChildren(retVal);
			}
		}

		return retVal;
	}

	public static ConceptValidationOptions vsValidateCodeOptions() {
		return new ConceptValidationOptions().setValidateDisplay(true);
	}

}
