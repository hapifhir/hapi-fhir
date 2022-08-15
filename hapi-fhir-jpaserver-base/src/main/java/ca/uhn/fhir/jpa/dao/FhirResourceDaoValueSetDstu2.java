package ca.uhn.fhir.jpa.dao;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
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
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.jpa.dao.dstu3.FhirResourceDaoValueSetDstu3.vsValidateCodeOptions;
import static ca.uhn.fhir.jpa.util.LogicUtil.multiXor;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FhirResourceDaoValueSetDstu2 extends BaseHapiFhirResourceDao<ValueSet>
	implements IFhirResourceDaoValueSet<ValueSet, CodingDt, CodeableConceptDt>, IFhirResourceDaoCodeSystem<ValueSet, CodingDt, CodeableConceptDt> {

	private static FhirContext ourRiCtx;

	private DefaultProfileValidationSupport myDefaultProfileValidationSupport;

	@Autowired
	private IValidationSupport myJpaValidationSupport;

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
	public ValueSet expand(IIdType theId, ValueSetExpansionOptions theOptions, RequestDetails theRequest) {
		ValueSet source = loadValueSetForExpansion(theId, theRequest);
		return expand(source, theOptions);
	}

	@Override
	public ValueSet expand(ValueSet source, ValueSetExpansionOptions theOptions) {
		ValueSet retVal = new ValueSet();
		retVal.setDate(DateTimeDt.withCurrentTime());


		String filter = null;
		if (theOptions != null) {
			filter = theOptions.getFilter();
		}

		/*
		 * Add composed concepts
		 */

		for (ComposeInclude nextInclude : source.getCompose().getInclude()) {
			for (ComposeIncludeConcept next : nextInclude.getConcept()) {
				if (isBlank(filter)) {
					addCompose(retVal, nextInclude.getSystem(), next.getCode(), next.getDisplay());
				} else {
					filter = filter.toLowerCase();
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
			addCompose(filter, retVal, source, next);
		}

		return retVal;
	}

	@Override
	public ValueSet expandByIdentifier(String theUri, ValueSetExpansionOptions theOptions) {
		if (isBlank(theUri)) {
			throw new InvalidRequestException(Msg.code(946) + "URI must not be blank or missing");
		}
		ValueSet source;

		ValueSet defaultValueSet = myDefaultProfileValidationSupport.fetchResource(ValueSet.class, theUri);
		if (defaultValueSet != null) {
			source = getContext().newJsonParser().parseResource(ValueSet.class, getRiCtx().newJsonParser().encodeResourceToString(defaultValueSet));
		} else {
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronousUpTo(1);
			params.add(ValueSet.SP_URL, new UriParam(theUri));
			IBundleProvider ids = search(params);
			if (ids.size() == 0) {
				throw new InvalidRequestException(Msg.code(947) + "Unknown ValueSet URI: " + theUri);
			}
			source = (ValueSet) ids.getResources(0, 1).get(0);
		}

		return expand(source, theOptions);
	}

	@Override
	public List<IIdType> findCodeSystemIdsContainingSystemAndCode(String theCode, String theSystem, RequestDetails theRequest) {
		if (theSystem != null && theSystem.startsWith("http://hl7.org/fhir/")) {
			return Collections.singletonList(new IdDt(theSystem));
		}

		List<IIdType> valueSetIds;
		List<ResourcePersistentId> ids = searchForIds(new SearchParameterMap(ValueSet.SP_CODE, new TokenParam(theSystem, theCode)), theRequest);
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
				return getContext().newJsonParser().parseResource(ValueSet.class, getRiCtx().newJsonParser().encodeResourceToString(valueSet));
			}
		}
		BaseHasResource sourceEntity = readEntity(theId, theRequest);
		if (sourceEntity == null) {
			throw new ResourceNotFoundException(Msg.code(2002) + theId);
		}
		ValueSet source = (ValueSet) toResource(sourceEntity, false);
		return source;
	}

	private FhirContext getRiCtx() {
		if (ourRiCtx == null) {
			ourRiCtx = FhirContext.forDstu2Hl7Org();
		}
		return ourRiCtx;
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

	@Nonnull
	@Override
	@Transactional
	public IValidationSupport.LookupCodeResult lookupCode(IPrimitiveType<String> theCode, IPrimitiveType<String> theSystem, CodingDt theCoding,  RequestDetails theRequest) {
		return lookupCode(theCode, theSystem, theCoding, null, theRequest);
	}
	
	@Nonnull
	@Override
	@Transactional
	public IValidationSupport.LookupCodeResult lookupCode(IPrimitiveType<String> theCode, IPrimitiveType<String> theSystem, CodingDt theCoding, IPrimitiveType<String> theDisplayLanguage, RequestDetails theRequest) {
		boolean haveCoding = theCoding != null && isNotBlank(theCoding.getSystem()) && isNotBlank(theCoding.getCode());
		boolean haveCode = theCode != null && theCode.isEmpty() == false;
		boolean haveSystem = theSystem != null && theSystem.isEmpty() == false;

		if (!haveCoding && !(haveSystem && haveCode)) {
			throw new InvalidRequestException(Msg.code(949) + "No code, coding, or codeableConcept provided to validate");
		}
		if (!multiXor(haveCoding, (haveSystem && haveCode)) || (haveSystem != haveCode)) {
			throw new InvalidRequestException(Msg.code(950) + "$lookup can only validate (system AND code) OR (coding.system AND coding.code)");
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

	@Override
	public IValidationSupport.CodeValidationResult validateCode(IPrimitiveType<String> theValueSetIdentifier, IIdType theId, IPrimitiveType<String> theCode,
																					IPrimitiveType<String> theSystem, IPrimitiveType<String> theDisplay, CodingDt theCoding, CodeableConceptDt theCodeableConcept, RequestDetails theRequest) {
		return myTerminologySvc.validateCode(vsValidateCodeOptions(), theId, toStringOrNull(theValueSetIdentifier), toStringOrNull(theSystem), toStringOrNull(theCode), toStringOrNull(theDisplay), theCoding, theCodeableConcept);
	}

	@Override
	public CodeValidationResult validateCode(IIdType theCodeSystemId, IPrimitiveType<String> theCodeSystemUrl, IPrimitiveType<String> theVersion, IPrimitiveType<String> theCode,
														  IPrimitiveType<String> theDisplay, CodingDt theCoding, CodeableConceptDt theCodeableConcept, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException(Msg.code(951));
	}

	public static String toStringOrNull(IPrimitiveType<String> thePrimitive) {
		return thePrimitive != null ? thePrimitive.getValue() : null;
	}

}
