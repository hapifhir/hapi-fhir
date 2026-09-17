package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.util.Logs;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

	/**
	 * Joins a code system URL and a version into the canonical form <code>system|version</code>.
	 * <p>
	 * The code system URL is returned unchanged if it already carries a version, or if no version is given.
	 * </p>
	 *
	 * @param theCodeSystem the code system URL, which may already be of the form <code>system|version</code>
	 * @param theVersion    the code system version, or <code>null</code> to return the code system URL unchanged
	 * @return the code system URL with the version appended, or the code system URL unchanged
	 */
	public static String getVersionedCodeSystem(String theCodeSystem, String theVersion) {
		if (isNotBlank(theCodeSystem) && isNotBlank(theVersion) && !theCodeSystem.contains("|")) {
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

	/**
	 * Returns the <code>url</code> of the given ValueSet, for any supported FHIR version.
	 *
	 * @param theFhirContext the FHIR context the ValueSet belongs to
	 * @param theValueSet    the ValueSet to read the URL from
	 * @return the ValueSet's URL
	 * @throws IllegalArgumentException if the FHIR version is not supported
	 * @since 8.14.0
	 */
	public static String getValueSetUrl(FhirContext theFhirContext, @Nonnull IBaseResource theValueSet) {
		String url;
		FhirVersionEnum structureFhirVersionEnum =
				CommonCodeSystemsTerminologyService.getFhirVersionEnum(theFhirContext, theValueSet);
		switch (structureFhirVersionEnum) {
			case DSTU2: {
				url = ((ca.uhn.fhir.model.dstu2.resource.ValueSet) theValueSet).getUrl();
				break;
			}
			case DSTU2_HL7ORG: {
				url = ((org.hl7.fhir.dstu2.model.ValueSet) theValueSet).getUrl();
				break;
			}
			case DSTU3: {
				url = ((org.hl7.fhir.dstu3.model.ValueSet) theValueSet).getUrl();
				break;
			}
			case R4: {
				url = ((ValueSet) theValueSet).getUrl();
				break;
			}
			case R4B: {
				url = ((org.hl7.fhir.r4b.model.ValueSet) theValueSet).getUrl();
				break;
			}
			case R5: {
				url = ((org.hl7.fhir.r5.model.ValueSet) theValueSet).getUrl();
				break;
			}
			case DSTU2_1:
			default:
				throw new IllegalArgumentException(
						Msg.code(695) + "Can not handle version: " + structureFhirVersionEnum);
		}
		return url;
	}

	/**
	 * Returns the <code>version</code> of the given ValueSet, for any FHIR version which has one.
	 *
	 * @param theFhirContext the FHIR context the ValueSet belongs to
	 * @param theValueSet    the ValueSet to read the version from
	 * @return the ValueSet's version, or <code>null</code> if the FHIR version has no version element
	 * @since 8.14.0
	 */
	public static String getValueSetVersion(@Nonnull FhirContext theFhirContext, @Nonnull IBaseResource theValueSet) {
		String version;
		switch (CommonCodeSystemsTerminologyService.getFhirVersionEnum(theFhirContext, theValueSet)) {
			case DSTU3: {
				version = ((org.hl7.fhir.dstu3.model.ValueSet) theValueSet).getVersion();
				break;
			}
			case R4: {
				version = ((ValueSet) theValueSet).getVersion();
				break;
			}
			case R4B: {
				version = ((org.hl7.fhir.r4b.model.ValueSet) theValueSet).getVersion();
				break;
			}
			case R5: {
				version = ((org.hl7.fhir.r5.model.ValueSet) theValueSet).getVersion();
				break;
			}
			case DSTU2:
			case DSTU2_HL7ORG:
			case DSTU2_1:
			default:
				version = null;
		}
		return version;
	}
}
