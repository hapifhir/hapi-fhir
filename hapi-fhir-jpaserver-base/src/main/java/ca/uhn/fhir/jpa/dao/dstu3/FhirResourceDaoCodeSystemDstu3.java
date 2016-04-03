package ca.uhn.fhir.jpa.dao.dstu3;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.dstu3.hapi.validation.HapiWorkerContext;
import org.hl7.fhir.dstu3.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.util.LogicUtil;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class FhirResourceDaoCodeSystemDstu3 extends FhirResourceDaoDstu3<CodeSystem> implements IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> {

	@Autowired
	private ValidationSupportChain myValidationSupport;

	@Override
	public List<IIdType> findCodeSystemIdsContainingSystemAndCode(String theCode, String theSystem) {
		// if (theSystem != null && theSystem.startsWith("http://hl7.org/fhir/ValueSet")) {
		// return Collections.singletonList((IIdType) new IdType(theSystem));
		// }

		List<IIdType> valueSetIds;
		Set<Long> ids = searchForIds(CodeSystem.SP_CODE, new TokenParam(theSystem, theCode));
		valueSetIds = new ArrayList<IIdType>();
		for (Long next : ids) {
			valueSetIds.add(new IdType("CodeSystem", next));
		}
		return valueSetIds;
	}

	
	@Override
	public LookupCodeResult lookupCode(IPrimitiveType<String> theCode, IPrimitiveType<String> theSystem, Coding theCoding,
			RequestDetails theRequestDetails) {
		boolean haveCoding = theCoding != null && isNotBlank(theCoding.getSystem()) && isNotBlank(theCoding.getCode());
		boolean haveCode = theCode != null && theCode.isEmpty() == false;
		boolean haveSystem = theSystem != null && theSystem.isEmpty() == false;

		if (!haveCoding && !(haveSystem && haveCode)) {
			throw new InvalidRequestException("No code, coding, or codeableConcept provided to validate");
		}
		if (!LogicUtil.multiXor(haveCoding, (haveSystem && haveCode)) || (haveSystem != haveCode)) {
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

		// CodeValidationResult validateOutcome = myJpaValidationSupport.validateCode(getContext(), system, code, null);
		//
		// LookupCodeResult result = new LookupCodeResult();
		// result.setSearchedForCode(code);
		// result.setSearchedForSystem(system);
		// result.setFound(false);
		// if (validateOutcome.isOk()) {
		// result.setFound(true);
		// result.setCodeIsAbstract(validateOutcome.asConceptDefinition().getAbstract());
		// result.setCodeDisplay(validateOutcome.asConceptDefinition().getDisplay());
		// }
		// return result;

		if (myValidationSupport.isCodeSystemSupported(getContext(), system)) {
			HapiWorkerContext ctx = new HapiWorkerContext(getContext(), myValidationSupport);
			ValueSetExpander expander = ctx.getExpander();
			ValueSet source = new ValueSet();
			source.getCompose().addInclude().setSystem(system).addConcept().setCode(code);

			ValueSetExpansionOutcome expansion;
			try {
				expansion = expander.expand(source);
			} catch (Exception e) {
				throw new InternalErrorException(e);
			}
			
			if (expansion.getValueset() != null) {
				List<ValueSetExpansionContainsComponent> contains = expansion.getValueset().getExpansion().getContains();
				LookupCodeResult result = lookup(contains, system, code);
				if (result != null) {
					return result;
				}
			}
			
		} else {

			/*
			 * If it's not a built-in code system, use ones from the database
			 */

			List<IIdType> valueSetIds = findCodeSystemIdsContainingSystemAndCode(code, system);
			for (IIdType nextId : valueSetIds) {
				CodeSystem expansion = read(nextId, theRequestDetails);
				for (ConceptDefinitionComponent next : expansion.getConcept()) {
					if (code.equals(next.getCode())) {
						LookupCodeResult retVal = new LookupCodeResult();
						retVal.setSearchedForCode(code);
						retVal.setSearchedForSystem(system);
						retVal.setFound(true);
						retVal.setCodeDisplay(next.getDisplay());
						retVal.setCodeSystemDisplayName("Unknown"); // TODO: implement
						return retVal;
					}
				}
			}

		}

		// We didn't find it..
		LookupCodeResult retVal = new LookupCodeResult();
		retVal.setFound(false);
		retVal.setSearchedForCode(code);
		retVal.setSearchedForSystem(system);
		return retVal;

	}

	private LookupCodeResult lookup(List<ValueSetExpansionContainsComponent> theContains, String theSystem, String theCode) {
		for (ValueSetExpansionContainsComponent nextCode : theContains) {

			String system = nextCode.getSystem();
			String code = nextCode.getCode();
			if (theSystem.equals(system) && theCode.equals(code)) {
				LookupCodeResult retVal = new LookupCodeResult();
				retVal.setSearchedForCode(code);
				retVal.setSearchedForSystem(system);
				retVal.setFound(true);
				if (nextCode.getAbstractElement().getValue() != null) {
					retVal.setCodeIsAbstract(nextCode.getAbstractElement().booleanValue());
				}
				retVal.setCodeDisplay(nextCode.getDisplay());
				retVal.setCodeSystemVersion(nextCode.getVersion());
				retVal.setCodeSystemDisplayName("Unknown"); // TODO: implement
				return retVal;
			}

		}

		return null;
	}

}
