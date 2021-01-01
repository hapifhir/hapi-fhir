package ca.uhn.fhir.jpa.dao.r5;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import org.hl7.fhir.convertors.conv40_50.ValueSet40_50;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.ValueSet;

import java.util.Date;

import static ca.uhn.fhir.jpa.dao.FhirResourceDaoValueSetDstu2.toStringOrNull;
import static ca.uhn.fhir.jpa.dao.dstu3.FhirResourceDaoValueSetDstu3.vsValidateCodeOptions;

public class FhirResourceDaoValueSetR5 extends BaseHapiFhirResourceDao<ValueSet> implements IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> {

	@Override
	public ValueSet expand(IIdType theId, String theFilter, RequestDetails theRequestDetails) {
		ValueSet source = read(theId, theRequestDetails);
		return expand(source, theFilter);
	}

	@Override
	public ValueSet expand(IIdType theId, String theFilter, int theOffset, int theCount, RequestDetails theRequestDetails) {
		ValueSet source = read(theId, theRequestDetails);
		return expand(source, theFilter, theOffset, theCount);
	}

	@Override
	public ValueSet expandByIdentifier(String theUri, String theFilter) {
		org.hl7.fhir.r4.model.ValueSet canonicalOutput = myTerminologySvc.expandValueSet(null, theUri, theFilter);
		return ValueSet40_50.convertValueSet(canonicalOutput);
	}

	@Override
	public ValueSet expandByIdentifier(String theUri, String theFilter, int theOffset, int theCount) {
		ValueSetExpansionOptions options = ValueSetExpansionOptions.forOffsetAndCount(theOffset, theCount);
		org.hl7.fhir.r4.model.ValueSet canonicalOutput = myTerminologySvc.expandValueSet(options, theUri, theFilter);
		return ValueSet40_50.convertValueSet(canonicalOutput);
	}

	@Override
	public ValueSet expand(ValueSet theSource, String theFilter) {
		org.hl7.fhir.r4.model.ValueSet canonicalInput = ValueSet40_50.convertValueSet(theSource);
		org.hl7.fhir.r4.model.ValueSet canonicalOutput = myTerminologySvc.expandValueSet(null, canonicalInput, theFilter);
		return ValueSet40_50.convertValueSet(canonicalOutput);
	}

	@Override
	public ValueSet expand(ValueSet theSource, String theFilter, int theOffset, int theCount) {
		ValueSetExpansionOptions options = ValueSetExpansionOptions.forOffsetAndCount(theOffset, theCount);
		org.hl7.fhir.r4.model.ValueSet canonicalInput = ValueSet40_50.convertValueSet(theSource);
		org.hl7.fhir.r4.model.ValueSet canonicalOutput = myTerminologySvc.expandValueSet(options, canonicalInput, theFilter);
		return ValueSet40_50.convertValueSet(canonicalOutput);
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

		if (myDaoConfig.isPreExpandValueSets() && !retVal.isUnchangedInCurrentOperation()) {
			if (retVal.getDeleted() == null) {
				ValueSet valueSet = (ValueSet) theResource;
				myTerminologySvc.storeTermValueSet(retVal, org.hl7.fhir.convertors.conv40_50.ValueSet40_50.convertValueSet(valueSet));
			} else {
				myTerminologySvc.deleteValueSetAndChildren(retVal);
			}
		}

		return retVal;
	}

}
