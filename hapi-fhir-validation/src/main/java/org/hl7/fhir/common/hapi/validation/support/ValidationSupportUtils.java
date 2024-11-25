package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.util.Logs;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;

public final class ValidationSupportUtils {

	private static final Logger ourLog = Logs.getTerminologyTroubleshootingLog();

	private ValidationSupportUtils() {}

	/**
	 * This method extracts a code system that can be (potentially) associated with a code when
	 * performing validation against a ValueSet. This method was created for internal purposes.
	 * Please use this method with care because it will only cover some
	 * use-cases (e.g. standard bindings) while for others it may not return correct results or return null.
	 * An incorrect result could be considered if the resource declares a code with a system, and you're calling
	 * this method to check a binding against a ValueSet that has nothing to do with that system.
	 * @param theValueSet the valueSet
	 * @param theCode the code
	 * @return the system which can be associated with the code
	 */
	public static String extractCodeSystemForCode(IBaseResource theValueSet, String theCode) {
		if (theValueSet instanceof org.hl7.fhir.dstu3.model.ValueSet) {
			return extractCodeSystemForCodeDSTU3((org.hl7.fhir.dstu3.model.ValueSet) theValueSet, theCode);
		} else if (theValueSet instanceof ValueSet) {
			return extractCodeSystemForCodeR4((ValueSet) theValueSet, theCode);
		} else if (theValueSet instanceof org.hl7.fhir.r5.model.ValueSet) {
			return extractCodeSystemForCodeR5((org.hl7.fhir.r5.model.ValueSet) theValueSet, theCode);
		}
		return null;
	}

	/**
	 * Try to obtain the codeSystem of the received code from the input DSTU3 ValueSet
	 */
	private static String extractCodeSystemForCodeDSTU3(org.hl7.fhir.dstu3.model.ValueSet theValueSet, String theCode) {
		if (theValueSet.getCompose().getInclude().isEmpty()) {
			return null;
		}

		if (theValueSet.getCompose().getInclude().size() == 1) {
			org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent include =
					theValueSet.getCompose().getInclude().iterator().next();
			return include.hasSystem() ? getVersionedCodeSystem(include.getSystem(), include.getVersion()) : null;
		}

		// when component has more than one include, their codeSystem(s) could be different, so we need to make sure
		// that we are picking up the system for the include filter to which the code corresponds
		for (org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent include :
				theValueSet.getCompose().getInclude()) {
			if (include.hasSystem()) {
				for (org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent concept : include.getConcept()) {
					if (concept.hasCodeElement() && concept.getCode().equals(theCode)) {
						return getVersionedCodeSystem(include.getSystem(), include.getVersion());
					}
				}
			}
		}

		// at this point codeSystem couldn't be extracted for a multi-include ValueSet. Just on case it was
		// because the format was not well handled, let's allow to watch the VS by an easy logging change
		logCodeAndValueSet(theCode, theValueSet.getId());
		return null;
	}

	/**
	 * Try to obtain the codeSystem of the received code from the input R4 ValueSet
	 */
	private static String extractCodeSystemForCodeR4(ValueSet theValueSet, String theCode) {
		if (theValueSet.getCompose().getInclude().isEmpty()) {
			return null;
		}

		if (theValueSet.getCompose().getInclude().size() == 1) {
			ValueSet.ConceptSetComponent include =
					theValueSet.getCompose().getInclude().iterator().next();
			return include.hasSystem() ? getVersionedCodeSystem(include.getSystem(), include.getVersion()) : null;
		}

		// when component has more than one include, their codeSystem(s) could be different, so we need to make sure
		// that we are picking up the system for the include filter to which the code corresponds
		for (ValueSet.ConceptSetComponent include : theValueSet.getCompose().getInclude()) {
			if (include.hasSystem()) {
				for (ValueSet.ConceptReferenceComponent concept : include.getConcept()) {
					if (concept.hasCodeElement() && concept.getCode().equals(theCode)) {
						return getVersionedCodeSystem(include.getSystem(), include.getVersion());
					}
				}
			}
		}

		// at this point codeSystem couldn't be extracted for a multi-include ValueSet. Just on case it was
		// because the format was not well handled, let's allow to watch the VS by an easy logging change
		logCodeAndValueSet(theCode, theValueSet.getId());
		return null;
	}

	private static String getVersionedCodeSystem(String theCodeSystem, String theVersion) {
		if (!theCodeSystem.contains("|") && theVersion != null) {
			return theCodeSystem + "|" + theVersion;
		}
		return theCodeSystem;
	}

	/**
	 * Try to obtain the codeSystem of the received code from the input R5 ValueSet
	 */
	private static String extractCodeSystemForCodeR5(org.hl7.fhir.r5.model.ValueSet theValueSet, String theCode) {
		if (theValueSet.getCompose().getInclude().isEmpty()) {
			return null;
		}

		if (theValueSet.getCompose().getInclude().size() == 1) {
			org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent include =
					theValueSet.getCompose().getInclude().iterator().next();
			return include.hasSystem() ? getVersionedCodeSystem(include.getSystem(), include.getVersion()) : null;
		}

		// when component has more than one include, their codeSystem(s) could be different, so we need to make sure
		// that we are picking up the system for the include filter to which the code corresponds
		for (org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent include :
				theValueSet.getCompose().getInclude()) {
			if (include.hasSystem()) {
				for (org.hl7.fhir.r5.model.ValueSet.ConceptReferenceComponent concept : include.getConcept()) {
					if (concept.hasCodeElement() && concept.getCode().equals(theCode)) {
						return getVersionedCodeSystem(include.getSystem(), include.getVersion());
					}
				}
			}
		}

		// at this point codeSystem couldn't be extracted for a multi-include ValueSet. Just on case it was
		// because the format was not well handled, let's allow to watch the VS by an easy logging change
		logCodeAndValueSet(theCode, theValueSet.getId());
		return null;
	}

	private static void logCodeAndValueSet(String theCode, String theValueSet) {
		ourLog.debug("CodeSystem couldn't be extracted for code: {} for ValueSet: {}", theCode, theValueSet);
	}
}
