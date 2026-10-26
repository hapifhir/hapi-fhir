package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.MissingCodeSystemPolicy;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class ValidationSupportUtils {

	private static final Logger ourLog = Logs.getTerminologyTroubleshootingLog();

	private ValidationSupportUtils() {}

	/**
	 * Whether a ValueSet include or exclude naming the given CodeSystem version is applied to the installed version
	 * instead: the policy uses the installed version, a version is named, and no module in the chain supports it. The
	 * caller finds the installed version.
	 *
	 * @param thePolicy                   the policy
	 * @param theValidationSupportContext the validation support chain, asked whether it supports the named version
	 * @param theCodeSystemUrl            the code system URL the include or exclude names
	 * @param theNamedVersion             the version the include or exclude names
	 * @return {@literal true} if the named version is to be replaced
	 */
	// Created by Claude Opus 5.5
	public static boolean isNamedCodeSystemVersionReplaced(
			@Nonnull MissingCodeSystemPolicy thePolicy,
			@Nonnull ValidationSupportContext theValidationSupportContext,
			@Nullable String theCodeSystemUrl,
			@Nullable String theNamedVersion) {
		return thePolicy.isInstalledVersionUsed()
				&& isNotBlank(theCodeSystemUrl)
				&& isNotBlank(theNamedVersion)
				&& !theValidationSupportContext
						.getRootValidationSupport()
						.isCodeSystemSupported(theValidationSupportContext, theCodeSystemUrl, theNamedVersion);
	}

	/**
	 * The warning given when {@link MissingCodeSystemPolicy#USE_INSTALLED_VERSION} applies a ValueSet include or
	 * exclude to another version of its code system than the one it names.
	 *
	 * @param theFhirContext   the context whose localizer provides the message
	 * @param theCodeSystemUrl the code system URL
	 * @param theNamedVersion  the version the include or exclude names
	 * @param theUsedVersion   the version used instead, or {@literal null} if it has none
	 * @return the message
	 */
	// Created by Claude Opus 5.5
	@Nonnull
	public static String createCodeSystemVersionReplacedMessage(
			@Nonnull FhirContext theFhirContext,
			@Nonnull String theCodeSystemUrl,
			@Nonnull String theNamedVersion,
			@Nullable String theUsedVersion) {
		if (isNotBlank(theUsedVersion)) {
			return theFhirContext
					.getLocalizer()
					.getMessage(
							MissingCodeSystemPolicy.class,
							"versionReplaced",
							theCodeSystemUrl,
							theNamedVersion,
							theUsedVersion);
		}
		return theFhirContext
				.getLocalizer()
				.getMessage(
						MissingCodeSystemPolicy.class,
						"versionReplacedWithUnversioned",
						theCodeSystemUrl,
						theNamedVersion);
	}

	/**
	 * The warning given when {@link MissingCodeSystemPolicy#ACCEPT_LISTED_CODES} accepts the codes a ValueSet lists
	 * from a CodeSystem it cannot check them against.
	 *
	 * @param theFhirContext             the context whose localizer provides the message
	 * @param theCodeSystemUrlAndVersion the code system URL, as {@literal url|version} if the include names a version
	 * @return the message
	 */
	// Created by Claude Opus 5.5
	@Nonnull
	public static String createListedCodesUncheckedMessage(
			@Nonnull FhirContext theFhirContext, @Nonnull String theCodeSystemUrlAndVersion) {
		return theFhirContext
				.getLocalizer()
				.getMessage(MissingCodeSystemPolicy.class, "listedCodesUnchecked", theCodeSystemUrlAndVersion);
	}

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
			return include.hasSystem() ? toCodeSystemCanonical(include.getSystem(), include.getVersion()) : null;
		}

		// when component has more than one include, their codeSystem(s) could be different, so we need to make sure
		// that we are picking up the system for the include filter to which the code corresponds
		for (org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent include :
				theValueSet.getCompose().getInclude()) {
			if (include.hasSystem()) {
				for (org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent concept : include.getConcept()) {
					if (concept.hasCodeElement() && concept.getCode().equals(theCode)) {
						return toCodeSystemCanonical(include.getSystem(), include.getVersion());
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
			return include.hasSystem() ? toCodeSystemCanonical(include.getSystem(), include.getVersion()) : null;
		}

		// when component has more than one include, their codeSystem(s) could be different, so we need to make sure
		// that we are picking up the system for the include filter to which the code corresponds
		for (ValueSet.ConceptSetComponent include : theValueSet.getCompose().getInclude()) {
			if (include.hasSystem()) {
				for (ValueSet.ConceptReferenceComponent concept : include.getConcept()) {
					if (concept.hasCodeElement() && concept.getCode().equals(theCode)) {
						return toCodeSystemCanonical(include.getSystem(), include.getVersion());
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
	 * Try to obtain the codeSystem of the received code from the input R5 ValueSet
	 */
	private static String extractCodeSystemForCodeR5(org.hl7.fhir.r5.model.ValueSet theValueSet, String theCode) {
		if (theValueSet.getCompose().getInclude().isEmpty()) {
			return null;
		}

		if (theValueSet.getCompose().getInclude().size() == 1) {
			org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent include =
					theValueSet.getCompose().getInclude().iterator().next();
			return include.hasSystem() ? toCodeSystemCanonical(include.getSystem(), include.getVersion()) : null;
		}

		// when component has more than one include, their codeSystem(s) could be different, so we need to make sure
		// that we are picking up the system for the include filter to which the code corresponds
		for (org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent include :
				theValueSet.getCompose().getInclude()) {
			if (include.hasSystem()) {
				for (org.hl7.fhir.r5.model.ValueSet.ConceptReferenceComponent concept : include.getConcept()) {
					if (concept.hasCodeElement() && concept.getCode().equals(theCode)) {
						return toCodeSystemCanonical(include.getSystem(), include.getVersion());
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
	 * Joins a {@literal compose.include} system and version into the <code>url|version</code> canonical the
	 * callers of {@link #extractCodeSystemForCode} expect.
	 * <p>
	 * A {@literal compose.include.system} is a plain URI, but guides are authored with a version packed into it,
	 * so the version can arrive in either element or in both. {@literal compose.include.version} wins when both
	 * carry one, which is how {@literal InMemoryTerminologyServerValidationSupport} resolves the same pair when
	 * it expands the include.
	 * </p>
	 */
	// Created by Claude Opus 5
	private static String toCodeSystemCanonical(String theSystem, String theVersion) {
		UrlUtil.CanonicalUrlParts system = UrlUtil.parseCanonicalUrl(theSystem);
		String version =
				isNotBlank(theVersion) ? theVersion : system.versionId().orElse(null);
		return UrlUtil.toCanonicalUrl(system.url(), version);
	}

	private static void logCodeAndValueSet(String theCode, String theValueSet) {
		ourLog.debug("CodeSystem couldn't be extracted for code: {} for ValueSet: {}", theCode, theValueSet);
	}
}
