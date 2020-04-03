package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.BaseHasResource;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept;
import ca.uhn.fhir.model.dstu2.resource.ValueSet.ComposeInclude;
import ca.uhn.fhir.model.dstu2.resource.ValueSet.ComposeIncludeConcept;
import ca.uhn.fhir.model.dstu2.resource.ValueSet.ExpansionContains;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.apache.commons.codec.binary.StringUtils;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FhirResourceDaoValueSetDstu2 extends BaseHapiFhirResourceDao<ValueSet>
		implements IFhirResourceDaoValueSet<ValueSet, CodingDt, CodeableConceptDt>, IFhirResourceDaoCodeSystem<ValueSet, CodingDt, CodeableConceptDt> {

	private DefaultProfileValidationSupport myDefaultProfileValidationSupport;

	@Autowired
	private IValidationSupport myJpaValidationSupport;

	@Autowired
	@Qualifier("myFhirContextDstu2Hl7Org")
	private FhirContext myRiCtx;
	@Autowired
	private FhirContext myFhirContext;

	private CachingValidationSupport myValidationSupport;

	private void addCompose(String theFilter, ValueSet theValueSetToPopulate, ValueSet theSourceValueSet, CodeSystemConcept theConcept) {
		if (isBlank(theFilter)) {
			addCompose(theValueSetToPopulate, theSourceValueSet.getCodeSystem().getSystem(), theConcept.getCode(), theConcept.getDisplay());
		} else {
			String filter = theFilter.toLowerCase();
			if (theConcept.getDisplay().toLowerCase().contains(filter) || theConcept.getCode().toLowerCase().contains(filter)) {
				addCompose(theValueSetToPopulate, theSourceValueSet.getCodeSystem().getSystem(), theConcept.getCode(), theConcept.getDisplay());
			}
		}
		for (CodeSystemConcept nextChild : theConcept.getConcept()) {
			addCompose(theFilter, theValueSetToPopulate, theSourceValueSet, nextChild);
		}
	}

	private void addCompose(ValueSet retVal, String theSystem, String theCode, String theDisplay) {
		if (isBlank(theCode)) {
			return;
		}
		ExpansionContains contains = retVal.getExpansion().addContains();
		contains.setSystem(theSystem);
		contains.setCode(theCode);
		contains.setDisplay(theDisplay);
	}

	@Override
	public ValueSet expand(IIdType theId, String theFilter, RequestDetails theRequest) {
		ValueSet source = loadValueSetForExpansion(theId, theRequest);
		return expand(source, theFilter);
	}

	@Override
	public ValueSet expand(IIdType theId, String theFilter, int theOffset, int theCount, RequestDetails theRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ValueSet expand(ValueSet source, String theFilter) {
		ValueSet retVal = new ValueSet();
		retVal.setDate(DateTimeDt.withCurrentTime());

		/*
		 * Add composed concepts
		 */

		for (ComposeInclude nextInclude : source.getCompose().getInclude()) {
			for (ComposeIncludeConcept next : nextInclude.getConcept()) {
				if (isBlank(theFilter)) {
					addCompose(retVal, nextInclude.getSystem(), next.getCode(), next.getDisplay());
				} else {
					String filter = theFilter.toLowerCase();
					if (next.getDisplay().toLowerCase().contains(filter) || next.getCode().toLowerCase().contains(filter)) {
						addCompose(retVal, nextInclude.getSystem(), next.getCode(), next.getDisplay());
					}
				}
			}
		}

		/*
		 * Add defined concepts
		 */

		for (CodeSystemConcept next : source.getCodeSystem().getConcept()) {
			addCompose(theFilter, retVal, source, next);
		}

		return retVal;
	}

	@Override
	public ValueSet expand(ValueSet source, String theFilter, int theOffset, int theCount) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ValueSet expandByIdentifier(String theUri, String theFilter) {
		if (isBlank(theUri)) {
			throw new InvalidRequestException("URI must not be blank or missing");
		}
		ValueSet source;

		ValueSet defaultValueSet = myDefaultProfileValidationSupport.fetchResource(ValueSet.class, theUri);
		if (defaultValueSet != null) {
			source = getContext().newJsonParser().parseResource(ValueSet.class, myRiCtx.newJsonParser().encodeResourceToString(defaultValueSet));
		} else {
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronousUpTo(1);
			params.add(ValueSet.SP_URL, new UriParam(theUri));
			IBundleProvider ids = search(params);
			if (ids.size() == 0) {
				throw new InvalidRequestException("Unknown ValueSet URI: " + theUri);
			}
			source = (ValueSet) ids.getResources(0, 1).get(0);
		}

		return expand(source, theFilter);
	}

	@Override
	public ValueSet expandByIdentifier(String theUri, String theFilter, int theOffset, int theCount) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<IIdType> findCodeSystemIdsContainingSystemAndCode(String theCode, String theSystem, RequestDetails theRequest) {
		if (theSystem != null && theSystem.startsWith("http://hl7.org/fhir/")) {
			return Collections.singletonList(new IdDt(theSystem));
		}

		List<IIdType> valueSetIds;
		Set<ResourcePersistentId> ids = searchForIds(new SearchParameterMap(ValueSet.SP_CODE, new TokenParam(theSystem, theCode)), theRequest);
		valueSetIds = new ArrayList<>();
		for (ResourcePersistentId next : ids) {
			IIdType id = myIdHelperService.translatePidIdToForcedId(myFhirContext, "ValueSet", next);
			valueSetIds.add(id);
		}
		return valueSetIds;
	}

	private ValueSet loadValueSetForExpansion(IIdType theId, RequestDetails theRequest) {
		if (theId.getValue().startsWith("http://hl7.org/fhir/")) {
			org.hl7.fhir.dstu2.model.ValueSet valueSet = myValidationSupport.fetchResource(org.hl7.fhir.dstu2.model.ValueSet.class, theId.getValue());
			if (valueSet != null) {
				return getContext().newJsonParser().parseResource(ValueSet.class, myRiCtx.newJsonParser().encodeResourceToString(valueSet));
			}
		}
		BaseHasResource sourceEntity = readEntity(theId, theRequest);
		if (sourceEntity == null) {
			throw new ResourceNotFoundException(theId);
		}
		ValueSet source = (ValueSet) toResource(sourceEntity, false);
		return source;
	}

	private IValidationSupport.LookupCodeResult lookup(List<ExpansionContains> theContains, String theSystem, String theCode) {
		for (ExpansionContains nextCode : theContains) {

			String system = nextCode.getSystem();
			String code = nextCode.getCode();
			if (theSystem.equals(system) && theCode.equals(code)) {
				IValidationSupport.LookupCodeResult retVal = new IValidationSupport.LookupCodeResult();
				retVal.setSearchedForCode(code);
				retVal.setSearchedForSystem(system);
				retVal.setFound(true);
				if (nextCode.getAbstract() != null) {
					retVal.setCodeIsAbstract(nextCode.getAbstract());
				}
				retVal.setCodeDisplay(nextCode.getDisplay());
				retVal.setCodeSystemVersion(nextCode.getVersion());
				retVal.setCodeSystemDisplayName("Unknown"); // TODO: implement
				return retVal;
			}

		}

		return null;
	}

	@Override
	public IValidationSupport.LookupCodeResult lookupCode(IPrimitiveType<String> theCode, IPrimitiveType<String> theSystem, CodingDt theCoding, RequestDetails theRequest) {
		boolean haveCoding = theCoding != null && isNotBlank(theCoding.getSystem()) && isNotBlank(theCoding.getCode());
		boolean haveCode = theCode != null && theCode.isEmpty() == false;
		boolean haveSystem = theSystem != null && theSystem.isEmpty() == false;

		if (!haveCoding && !(haveSystem && haveCode)) {
			throw new InvalidRequestException("No code, coding, or codeableConcept provided to validate");
		}
		if (!multiXor(haveCoding, (haveSystem && haveCode)) || (haveSystem != haveCode)) {
			throw new InvalidRequestException("$lookup can only validate (system AND code) OR (coding.system AND coding.code)");
		}

		String code;
		String system;
		if (haveCoding) {
			code = theCoding.getCode();
			system = theCoding.getSystem();
		} else {
			code = theCode.getValue();
			system = theSystem.getValue();
		}

		List<IIdType> valueSetIds = findCodeSystemIdsContainingSystemAndCode(code, system, theRequest);
		for (IIdType nextId : valueSetIds) {
			ValueSet expansion = expand(nextId, null, theRequest);
			List<ExpansionContains> contains = expansion.getExpansion().getContains();
			IValidationSupport.LookupCodeResult result = lookup(contains, system, code);
			if (result != null) {
				return result;
			}
		}

		IValidationSupport.LookupCodeResult retVal = new IValidationSupport.LookupCodeResult();
		retVal.setFound(false);
		retVal.setSearchedForCode(code);
		retVal.setSearchedForSystem(system);
		return retVal;
	}

	@Override
	public SubsumesResult subsumes(IPrimitiveType<String> theCodeA, IPrimitiveType<String> theCodeB, IPrimitiveType<String> theSystem, CodingDt theCodingA, CodingDt theCodingB, RequestDetails theRequestDetails) {
		return myTerminologySvc.subsumes(theCodeA, theCodeB, theSystem, theCodingA, theCodingB);
	}

	@Override
	@PostConstruct
	public void postConstruct() {
		super.postConstruct();
		myDefaultProfileValidationSupport = new DefaultProfileValidationSupport(myFhirContext);
		myValidationSupport = new CachingValidationSupport(new ValidationSupportChain(myDefaultProfileValidationSupport, myJpaValidationSupport));
	}

	@Override
	public void purgeCaches() {
		// nothing
	}

	private String toStringOrNull(IPrimitiveType<String> thePrimitive) {
		return thePrimitive != null ? thePrimitive.getValue() : null;
	}

	@Override
	public ValidateCodeResult validateCode(IPrimitiveType<String> theValueSetIdentifier, IIdType theId, IPrimitiveType<String> theCode,
			IPrimitiveType<String> theSystem, IPrimitiveType<String> theDisplay, CodingDt theCoding, CodeableConceptDt theCodeableConcept, RequestDetails theRequest) {
		List<IIdType> valueSetIds;

		boolean haveCodeableConcept = theCodeableConcept != null && theCodeableConcept.getCoding().size() > 0;
		boolean haveCoding = theCoding != null && theCoding.isEmpty() == false;
		boolean haveCode = theCode != null && theCode.isEmpty() == false;

		if (!haveCodeableConcept && !haveCoding && !haveCode) {
			throw new InvalidRequestException("No code, coding, or codeableConcept provided to validate");
		}
		if (!multiXor(haveCodeableConcept, haveCoding, haveCode)) {
			throw new InvalidRequestException("$validate-code can only validate (system AND code) OR (coding) OR (codeableConcept)");
		}

		boolean haveIdentifierParam = theValueSetIdentifier != null && theValueSetIdentifier.isEmpty() == false;
		if (theId != null) {
			valueSetIds = Collections.singletonList(theId);
		} else if (haveIdentifierParam) {
			Set<ResourcePersistentId> ids = searchForIds(new SearchParameterMap(ValueSet.SP_IDENTIFIER, new TokenParam(null, theValueSetIdentifier.getValue())), theRequest);
			valueSetIds = new ArrayList<>();
			for (ResourcePersistentId next : ids) {
				IIdType id = myIdHelperService.translatePidIdToForcedId(myFhirContext, "ValueSet", next);
				valueSetIds.add(id);
			}
		} else {
			if (theCode == null || theCode.isEmpty()) {
				throw new InvalidRequestException("Either ValueSet ID or ValueSet identifier or system and code must be provided. Unable to validate.");
			}
			String code = theCode.getValue();
			String system = toStringOrNull(theSystem);
			valueSetIds = findCodeSystemIdsContainingSystemAndCode(code, system, theRequest);
		}

		for (IIdType nextId : valueSetIds) {
			ValueSet expansion = expand(nextId, null, theRequest);
			List<ExpansionContains> contains = expansion.getExpansion().getContains();
			ValidateCodeResult result = validateCodeIsInContains(contains, toStringOrNull(theSystem), toStringOrNull(theCode), theCoding, theCodeableConcept);
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

	private ValidateCodeResult validateCodeIsInContains(List<ExpansionContains> contains, String theSystem, String theCode, CodingDt theCoding,
			CodeableConceptDt theCodeableConcept) {
		for (ExpansionContains nextCode : contains) {
			ValidateCodeResult result = validateCodeIsInContains(nextCode.getContains(), theSystem, theCode, theCoding, theCodeableConcept);
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
				for (CodingDt next : theCodeableConcept.getCoding()) {
					if (StringUtils.equals(system, next.getSystem()) && StringUtils.equals(code, next.getCode())) {
						return new ValidateCodeResult(true, "Validation succeeded", nextCode.getDisplay());
					}
				}
			}

		}

		return null;
	}

	private static boolean multiXor(boolean... theValues) {
		int count = 0;
		for (int i = 0; i < theValues.length; i++) {
			if (theValues[i]) {
				count++;
			}
		}
		return count == 1;
	}

}
