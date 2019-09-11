package ca.uhn.fhir.jpa.dao.dstu3;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.jpa.util.LogicUtil;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ElementUtil;
import org.apache.commons.codec.binary.StringUtils;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetFilterComponent;
import org.hl7.fhir.dstu3.model.ValueSet.FilterOperator;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FhirResourceDaoValueSetDstu3 extends FhirResourceDaoDstu3<ValueSet> implements IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoValueSetDstu3.class);

	@Autowired
	private IHapiTerminologySvc myHapiTerminologySvc;

	@Autowired
	@Qualifier("myJpaValidationSupportChainDstu3")
	private IValidationSupport myValidationSupport;
	@Autowired
	private IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> myCodeSystemDao;

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

	private ValueSet doExpand(ValueSet theSource) {

		validateIncludes("include", theSource.getCompose().getInclude());
		validateIncludes("exclude", theSource.getCompose().getExclude());

		/*
		 * If all of the code systems are supported by the HAPI FHIR terminology service, let's
		 * use that as it's more efficient.
		 */

		boolean allSystemsAreSuppportedByTerminologyService = true;
		for (ConceptSetComponent next : theSource.getCompose().getInclude()) {
			if (!myTerminologySvc.supportsSystem(next.getSystem())) {
				allSystemsAreSuppportedByTerminologyService = false;
			}
		}
		for (ConceptSetComponent next : theSource.getCompose().getExclude()) {
			if (!myTerminologySvc.supportsSystem(next.getSystem())) {
				allSystemsAreSuppportedByTerminologyService = false;
			}
		}
		if (allSystemsAreSuppportedByTerminologyService) {
			return (ValueSet) myTerminologySvc.expandValueSet(theSource);
		}

		HapiWorkerContext workerContext = new HapiWorkerContext(getContext(), myValidationSupport);
		ValueSetExpansionOutcome outcome = workerContext.expand(theSource, null);
		ValueSet retVal = outcome.getValueset();
		retVal.setStatus(PublicationStatus.ACTIVE);
		return retVal;
	}

	private ValueSet doExpand(ValueSet theSource, int theOffset, int theCount) {

		validateIncludes("include", theSource.getCompose().getInclude());
		validateIncludes("exclude", theSource.getCompose().getExclude());

		/*
		 * If all of the code systems are supported by the HAPI FHIR terminology service, let's
		 * use that as it's more efficient.
		 */

		boolean allSystemsAreSuppportedByTerminologyService = true;
		for (ConceptSetComponent next : theSource.getCompose().getInclude()) {
			if (!myTerminologySvc.supportsSystem(next.getSystem())) {
				allSystemsAreSuppportedByTerminologyService = false;
			}
		}
		for (ConceptSetComponent next : theSource.getCompose().getExclude()) {
			if (!myTerminologySvc.supportsSystem(next.getSystem())) {
				allSystemsAreSuppportedByTerminologyService = false;
			}
		}
		if (allSystemsAreSuppportedByTerminologyService) {
			return (ValueSet) myTerminologySvc.expandValueSet(theSource, theOffset, theCount);
		}

		HapiWorkerContext workerContext = new HapiWorkerContext(getContext(), myValidationSupport);
		ValueSetExpansionOutcome outcome = workerContext.expand(theSource, null);
		ValueSet retVal = outcome.getValueset();
		retVal.setStatus(PublicationStatus.ACTIVE);
		return retVal;
	}

	private void validateIncludes(String name, List<ConceptSetComponent> listToValidate) {
		for (ConceptSetComponent nextExclude : listToValidate) {
			if (isBlank(nextExclude.getSystem()) && !ElementUtil.isEmpty(nextExclude.getConcept(), nextExclude.getFilter())) {
				throw new InvalidRequestException("ValueSet contains " + name + " criteria with no system defined");
			}
		}
	}

	@Override
	public ValueSet expandByIdentifier(String theUri, String theFilter) {
		if (isBlank(theUri)) {
			throw new InvalidRequestException("URI must not be blank or missing");
		}

		ValueSet source = new ValueSet();

		source.getCompose().addInclude().addValueSet(theUri);

		if (isNotBlank(theFilter)) {
			ConceptSetComponent include = source.getCompose().addInclude();
			ConceptSetFilterComponent filter = include.addFilter();
			filter.setProperty("display");
			filter.setOp(FilterOperator.EQUAL);
			filter.setValue(theFilter);
		}

		ValueSet retVal = doExpand(source);
		return retVal;

		// if (defaultValueSet != null) {
		// source = getContext().newJsonParser().parseResource(ValueSet.class, getContext().newJsonParser().encodeResourceToString(defaultValueSet));
		// } else {
		// IBundleProvider ids = search(ValueSet.SP_URL, new UriParam(theUri));
		// if (ids.size() == 0) {
		// throw new InvalidRequestException("Unknown ValueSet URI: " + theUri);
		// }
		// source = (ValueSet) ids.getResources(0, 1).get(0);
		// }
		//
		// return expand(defaultValueSet, theFilter);
	}

	@Override
	public ValueSet expandByIdentifier(String theUri, String theFilter, int theOffset, int theCount) {
		if (isBlank(theUri)) {
			throw new InvalidRequestException("URI must not be blank or missing");
		}

		ValueSet source = new ValueSet();
		source.setUrl(theUri);

		source.getCompose().addInclude().addValueSet(theUri);

		if (isNotBlank(theFilter)) {
			ConceptSetComponent include = source.getCompose().addInclude();
			ConceptSetFilterComponent filter = include.addFilter();
			filter.setProperty("display");
			filter.setOp(FilterOperator.EQUAL);
			filter.setValue(theFilter);
		}

		ValueSet retVal = doExpand(source, theOffset, theCount);
		return retVal;
	}

	@Override
	public ValueSet expand(ValueSet theSource, String theFilter) {
		ValueSet toExpand = new ValueSet();

		// for (UriType next : theSource.getCompose().getInclude()) {
		// ConceptSetComponent include = toExpand.getCompose().addInclude();
		// include.setSystem(next.getValue());
		// addFilterIfPresent(theFilter, include);
		// }

		for (ConceptSetComponent next : theSource.getCompose().getInclude()) {
			toExpand.getCompose().addInclude(next);
			addFilterIfPresent(theFilter, next);
		}

		if (toExpand.getCompose().isEmpty()) {
			throw new InvalidRequestException("ValueSet does not have any compose.include or compose.import values, can not expand");
		}

		toExpand.getCompose().getExclude().addAll(theSource.getCompose().getExclude());

		ValueSet retVal = doExpand(toExpand);

		if (isNotBlank(theFilter)) {
			applyFilter(retVal.getExpansion().getTotalElement(), retVal.getExpansion().getContains(), theFilter);
		}

		return retVal;
	}

	@Override
	public ValueSet expand(ValueSet theSource, String theFilter, int theOffset, int theCount) {
		ValueSet toExpand = new ValueSet();
		toExpand.setId(theSource.getId());
		toExpand.setUrl(theSource.getUrl());

		for (ConceptSetComponent next : theSource.getCompose().getInclude()) {
			toExpand.getCompose().addInclude(next);
			addFilterIfPresent(theFilter, next);
		}

		if (toExpand.getCompose().isEmpty()) {
			throw new InvalidRequestException("ValueSet does not have any compose.include or compose.import values, can not expand");
		}

		toExpand.getCompose().getExclude().addAll(theSource.getCompose().getExclude());

		ValueSet retVal = doExpand(toExpand, theOffset, theCount);

		if (isNotBlank(theFilter)) {
			applyFilter(retVal.getExpansion().getTotalElement(), retVal.getExpansion().getContains(), theFilter);
		}

		return retVal;
	}

	private void applyFilter(IntegerType theTotalElement, List<ValueSetExpansionContainsComponent> theContains, String theFilter) {

		for (int idx = 0; idx < theContains.size(); idx++) {
			ValueSetExpansionContainsComponent next = theContains.get(idx);
			if (isBlank(next.getDisplay()) || !org.apache.commons.lang3.StringUtils.containsIgnoreCase(next.getDisplay(), theFilter)) {
				theContains.remove(idx);
				idx--;
				if (theTotalElement.getValue() != null) {
					theTotalElement.setValue(theTotalElement.getValue() - 1);
				}
			}
			applyFilter(theTotalElement, next.getContains(), theFilter);
		}
	}

	private void addFilterIfPresent(String theFilter, ConceptSetComponent include) {
		if (ElementUtil.isEmpty(include.getConcept())) {
			if (isNotBlank(theFilter)) {
				include.addFilter().setProperty("display").setOp(FilterOperator.EQUAL).setValue(theFilter);
			}
		}
	}

	@Override
	public ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet.ValidateCodeResult validateCode(IPrimitiveType<String> theValueSetIdentifier, IIdType theId, IPrimitiveType<String> theCode,
																													IPrimitiveType<String> theSystem, IPrimitiveType<String> theDisplay, Coding theCoding,
																													CodeableConcept theCodeableConcept, RequestDetails theRequestDetails) {

		List<IIdType> valueSetIds = Collections.emptyList();

		boolean haveCodeableConcept = theCodeableConcept != null && theCodeableConcept.getCoding().size() > 0;
		boolean haveCoding = theCoding != null && theCoding.isEmpty() == false;
		boolean haveCode = theCode != null && theCode.isEmpty() == false;

		if (!haveCodeableConcept && !haveCoding && !haveCode) {
			throw new InvalidRequestException("No code, coding, or codeableConcept provided to validate");
		}
		if (!LogicUtil.multiXor(haveCodeableConcept, haveCoding, haveCode)) {
			throw new InvalidRequestException("$validate-code can only validate (system AND code) OR (coding) OR (codeableConcept)");
		}

		boolean haveIdentifierParam = theValueSetIdentifier != null && theValueSetIdentifier.isEmpty() == false;
		ValueSet vs = null;
		if (theId != null) {
			vs = read(theId, theRequestDetails);
		} else if (haveIdentifierParam) {
			vs = myValidationSupport.fetchResource(getContext(), ValueSet.class, theValueSetIdentifier.getValue());
			if (vs == null) {
				throw new InvalidRequestException("Unknown ValueSet identifier: " + theValueSetIdentifier.getValue());
			}
		} else {
			if (theCode == null || theCode.isEmpty()) {
				throw new InvalidRequestException("Either ValueSet ID or ValueSet identifier or system and code must be provided. Unable to validate.");
			}
			// String code = theCode.getValue();
			// String system = toStringOrNull(theSystem);
			IContextValidationSupport.LookupCodeResult result = myCodeSystemDao.lookupCode(theCode, theSystem, null, null);
			if (result != null && result.isFound()) {
				ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet.ValidateCodeResult retVal = new ValidateCodeResult(true, "Found code", result.getCodeDisplay());
				return retVal;
			}
		}

		if (vs != null) {
			ValidateCodeResult result;
			if (myDaoConfig.isPreExpandValueSetsExperimental() && myTerminologySvc.isValueSetPreExpandedForCodeValidation(vs)) {
				result = myTerminologySvc.validateCodeIsInPreExpandedValueSet(vs, toStringOrNull(theSystem), toStringOrNull(theCode), toStringOrNull(theDisplay), theCoding, theCodeableConcept);
			} else {
				ValueSet expansion = doExpand(vs);
				List<ValueSetExpansionContainsComponent> contains = expansion.getExpansion().getContains();
				result = validateCodeIsInContains(contains, toStringOrNull(theSystem), toStringOrNull(theCode), theCoding, theCodeableConcept);
			}
			if (result != null) {
				if (theDisplay != null && isNotBlank(theDisplay.getValue()) && isNotBlank(result.getDisplay())) {
					if (!theDisplay.getValue().equals(result.getDisplay())) {
						return new ValidateCodeResult(false, "Display for code does not match", result.getDisplay());
					}
				}
				return result;
			}
		}

		return new ValidateCodeResult(false, "Code not found", null);

	}

	private String toStringOrNull(IPrimitiveType<String> thePrimitive) {
		return thePrimitive != null ? thePrimitive.getValue() : null;
	}

	private ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet.ValidateCodeResult validateCodeIsInContains(List<ValueSetExpansionContainsComponent> contains, String theSystem, String theCode,
			Coding theCoding, CodeableConcept theCodeableConcept) {
		for (ValueSetExpansionContainsComponent nextCode : contains) {
			ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet.ValidateCodeResult result = validateCodeIsInContains(nextCode.getContains(), theSystem, theCode, theCoding, theCodeableConcept);
			if (result != null) {
				return result;
			}

			String system = nextCode.getSystem();
			String code = nextCode.getCode();

			if (isNotBlank(theCode)) {
				if (theCode.equals(code) && (isBlank(theSystem) || theSystem.equals(system))) {
					return new ValidateCodeResult(true, "Validation succeeded", nextCode.getDisplay());
				}
			} else if (theCoding != null) {
				if (StringUtils.equals(system, theCoding.getSystem()) && StringUtils.equals(code, theCoding.getCode())) {
					return new ValidateCodeResult(true, "Validation succeeded", nextCode.getDisplay());
				}
			} else {
				for (Coding next : theCodeableConcept.getCoding()) {
					if (StringUtils.equals(system, next.getSystem()) && StringUtils.equals(code, next.getCode())) {
						return new ValidateCodeResult(true, "Validation succeeded", nextCode.getDisplay());
					}
				}
			}

		}

		return null;
	}

	@Override
	public void purgeCaches() {
		// nothing
	}

	@Override
	protected ResourceTable updateEntity(RequestDetails theRequestDetails, IBaseResource theResource, ResourceTable theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
													 boolean theUpdateVersion, Date theUpdateTime, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(theRequestDetails, theResource, theEntity, theDeletedTimestampOrNull, thePerformIndexing, theUpdateVersion, theUpdateTime, theForceUpdate, theCreateNewHistoryEntry);

		if (myDaoConfig.isPreExpandValueSetsExperimental()) {
			if (retVal.getDeleted() == null) {
				try {
					ValueSet valueSet = (ValueSet) theResource;
					org.hl7.fhir.r4.model.ValueSet converted = VersionConvertor_30_40.convertValueSet(valueSet);
					myHapiTerminologySvc.storeTermValueSet(retVal, converted);
				} catch (FHIRException fe) {
					throw new InternalErrorException(fe);
				}
			} else {
				myHapiTerminologySvc.deleteValueSetAndChildren(retVal);
			}
		}

		return retVal;
	}

}
