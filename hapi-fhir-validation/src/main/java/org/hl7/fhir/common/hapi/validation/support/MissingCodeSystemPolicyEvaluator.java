package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.MissingCodeSystemPolicy;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Applies the {@link MissingCodeSystemPolicy} a caller has configured to a ValueSet include or exclude whose
 * CodeSystem, or CodeSystem version, the server does not have, and builds the warnings the policy gives. Shared by the
 * in-memory and the JPA ValueSet expansion.
 *
 * @since 9.0.0
 */
// Created by Claude Opus 5.5
public class MissingCodeSystemPolicyEvaluator {

	private final FhirContext myFhirContext;
	private final Supplier<MissingCodeSystemPolicy> myPolicySupplier;

	/**
	 * Constructor
	 *
	 * @param theFhirContext    the context whose localizer provides the messages
	 * @param thePolicySupplier provides the policy, read on every call so a changed setting applies at once
	 */
	public MissingCodeSystemPolicyEvaluator(
			@Nonnull FhirContext theFhirContext, @Nonnull Supplier<MissingCodeSystemPolicy> thePolicySupplier) {
		myFhirContext = theFhirContext;
		myPolicySupplier = thePolicySupplier;
	}

	/**
	 * Whether a ValueSet include or exclude naming the given CodeSystem version is applied to the installed version
	 * instead: the policy uses the installed version, a version is named, and no module in the chain supports it. The
	 * caller finds the installed version.
	 *
	 * @param theValidationSupportContext the validation support chain, asked whether it supports the named version
	 * @param theCodeSystemUrl            the code system URL the include or exclude names
	 * @param theNamedVersion             the version the include or exclude names
	 * @return {@literal true} if the named version is to be replaced
	 */
	public boolean isNamedVersionReplaced(
			@Nonnull ValidationSupportContext theValidationSupportContext,
			@Nullable String theCodeSystemUrl,
			@Nullable String theNamedVersion) {
		return myPolicySupplier.get().isInstalledVersionUsed()
				&& isNotBlank(theCodeSystemUrl)
				&& isNotBlank(theNamedVersion)
				&& !theValidationSupportContext
						.getRootValidationSupport()
						.isCodeSystemSupported(theValidationSupportContext, theCodeSystemUrl, theNamedVersion);
	}

	/**
	 * Whether the policy accepts the codes an include or exclude lists from a CodeSystem the server cannot check them
	 * against
	 *
	 * @return {@literal true} under {@link MissingCodeSystemPolicy#ACCEPT_LISTED_CODES}
	 */
	public boolean isListedCodesAccepted() {
		return myPolicySupplier.get().isListedCodesAccepted();
	}

	/**
	 * Whether the codes an include or exclude lists are members without being looked up in a CodeSystem the server
	 * has no concepts for: the CodeSystem is not-present, the request asked not to fail on a missing CodeSystem, or
	 * the policy accepts listed codes.
	 *
	 * @param theCodeSystemNotPresent    whether the CodeSystem is stored with {@code content} {@code not-present}
	 * @param theFailOnMissingCodeSystem the request's
	 *                                   {@link ca.uhn.fhir.context.support.ValueSetExpansionOptions#isFailOnMissingCodeSystem()}
	 * @return {@literal true} if the listed codes are members
	 */
	public boolean isListedCodesTakenAsListed(boolean theCodeSystemNotPresent, boolean theFailOnMissingCodeSystem) {
		return theCodeSystemNotPresent || !theFailOnMissingCodeSystem || isListedCodesAccepted();
	}

	/**
	 * Whether {@link MissingCodeSystemPolicy#ACCEPT_LISTED_CODES} accepts an include's or exclude's listed codes
	 * without checking them, which the caller reports with {@link #createListedCodesUncheckedMessage(String)}.
	 *
	 * @param theHasListedCodes            whether the include or exclude lists codes in {@code concept}
	 * @param theCodeSystemCannotBeChecked whether no source can provide the CodeSystem's concepts, asked only when
	 *                                     the policy accepts listed codes and some are listed
	 * @return {@literal true} if the listed codes are accepted without being checked
	 */
	public boolean isListedCodesAcceptedUnchecked(
			boolean theHasListedCodes, @Nonnull BooleanSupplier theCodeSystemCannotBeChecked) {
		return isListedCodesAccepted() && theHasListedCodes && theCodeSystemCannotBeChecked.getAsBoolean();
	}

	/**
	 * The warning given when {@link MissingCodeSystemPolicy#USE_INSTALLED_VERSION} applies a ValueSet include or
	 * exclude to another version of its code system than the one it names.
	 *
	 * @param theCodeSystemUrl the code system URL
	 * @param theNamedVersion  the version the include or exclude names
	 * @param theUsedVersion   the version used instead, or {@literal null} if it has none
	 * @return the message
	 */
	@Nonnull
	public String createVersionReplacedMessage(
			@Nonnull String theCodeSystemUrl, @Nonnull String theNamedVersion, @Nullable String theUsedVersion) {
		if (isNotBlank(theUsedVersion)) {
			return myFhirContext
					.getLocalizer()
					.getMessage(
							MissingCodeSystemPolicy.class,
							"versionReplaced",
							theCodeSystemUrl,
							theNamedVersion,
							theUsedVersion);
		}
		return myFhirContext
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
	 * @param theCodeSystemUrlAndVersion the code system URL, as {@literal url|version} if the include names a version
	 * @return the message
	 */
	@Nonnull
	public String createListedCodesUncheckedMessage(@Nonnull String theCodeSystemUrlAndVersion) {
		return myFhirContext
				.getLocalizer()
				.getMessage(MissingCodeSystemPolicy.class, "listedCodesUnchecked", theCodeSystemUrlAndVersion);
	}
}
