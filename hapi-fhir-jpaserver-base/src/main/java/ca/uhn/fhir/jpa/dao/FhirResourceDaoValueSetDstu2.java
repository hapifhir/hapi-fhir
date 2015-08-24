package ca.uhn.fhir.jpa.dao;

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

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.StringUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept;
import ca.uhn.fhir.model.dstu2.resource.ValueSet.ComposeInclude;
import ca.uhn.fhir.model.dstu2.resource.ValueSet.ComposeIncludeConcept;
import ca.uhn.fhir.model.dstu2.resource.ValueSet.ExpansionContains;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class FhirResourceDaoValueSetDstu2 extends FhirResourceDaoDstu2<ValueSet>implements IFhirResourceDaoValueSet<ValueSet> {

	@Override
	public ValueSet expand(IIdType theId, StringDt theFilter) {
		ValueSet retVal = new ValueSet();
		retVal.setDate(DateTimeDt.withCurrentTime());

		BaseHasResource sourceEntity = readEntity(theId);
		if (sourceEntity == null) {
			throw new ResourceNotFoundException(theId);
		}
		ValueSet source = (ValueSet) toResource(sourceEntity);

		/*
		 * Add composed concepts
		 */

		for (ComposeInclude nextInclude : source.getCompose().getInclude()) {
			for (ComposeIncludeConcept next : nextInclude.getConcept()) {
				if (theFilter == null || theFilter.isEmpty()) {
					addCompose(retVal, nextInclude.getSystem(), next.getCode(), next.getDisplay());
				} else {
					String filter = theFilter.getValue().toLowerCase();
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

	private void addCompose(StringDt theFilter, ValueSet theValueSetToPopulate, ValueSet theSourceValueSet, CodeSystemConcept theConcept) {
		if (theFilter == null || theFilter.isEmpty()) {
			addCompose(theValueSetToPopulate, theSourceValueSet.getCodeSystem().getSystem(), theConcept.getCode(), theConcept.getDisplay());
		} else {
			String filter = theFilter.getValue().toLowerCase();
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
	public ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet.ValidateCodeResult validateCode(UriDt theValueSetIdentifier, IIdType theId, CodeDt theCode, UriDt theSystem, StringDt theDisplay,
			CodingDt theCoding, CodeableConceptDt theCodeableConcept) {
		List<IIdType> valueSetIds;

		boolean haveCodeableConcept = theCodeableConcept != null && theCodeableConcept.getCoding().size() > 0;
		boolean haveCoding = theCoding != null && theCoding.isEmpty() == false;
		boolean haveCode = theCode != null && theCode.isEmpty() == false;

		if (!haveCodeableConcept && !haveCoding && !haveCode) {
			throw new InvalidRequestException("No code, coding, or codeableConcept provided to validate");
		}
		if (!(haveCodeableConcept ^ haveCoding ^ haveCode)) {
			throw new InvalidRequestException("$validate-code can only validate (system AND code) OR (coding) OR (codeableConcept)");
		}

		boolean haveIdentifierParam = theValueSetIdentifier != null && theValueSetIdentifier.isEmpty() == false;
		if (theId != null) {
			valueSetIds = Collections.singletonList((IIdType) theId);
		} else if (haveIdentifierParam) {
			Set<Long> ids = searchForIds(ValueSet.SP_IDENTIFIER, new TokenParam(null, theValueSetIdentifier.getValue()));
			valueSetIds = new ArrayList<IIdType>();
			for (Long next : ids) {
				valueSetIds.add(new IdDt("ValueSet", next));
			}
		} else {
			if (theCode == null || theCode.isEmpty()) {
				throw new InvalidRequestException("Either ValueSet ID or ValueSet identifier or system and code must be provided. Unable to validate.");
			}
			Set<Long> ids = searchForIds(ValueSet.SP_CODE, new TokenParam(toStringOrNull(theSystem), theCode.getValue()));
			valueSetIds = new ArrayList<IIdType>();
			for (Long next : ids) {
				valueSetIds.add(new IdDt("ValueSet", next));
			}
		}

		for (IIdType nextId : valueSetIds) {
			ValueSet expansion = expand(nextId, null);
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

	private String toStringOrNull(IPrimitiveType<String> thePrimitive) {
		return thePrimitive != null ? thePrimitive.getValue() : null;
	}

	private ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet.ValidateCodeResult validateCodeIsInContains(List<ExpansionContains> contains, String theSystem, String theCode, CodingDt theCoding,
			CodeableConceptDt theCodeableConcept) {
		for (ExpansionContains nextCode : contains) {
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
				for (CodingDt next : theCodeableConcept.getCoding()) {
					if (StringUtils.equals(system, next.getSystem()) && StringUtils.equals(code, next.getCode())) {
						return new ValidateCodeResult(true, "Validation succeeded", nextCode.getDisplay());
					}
				}
			}

		}

		return null;
	}

}
