/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.MissingCodeSystemPolicy;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.common.hapi.validation.support.MissingCodeSystemPolicyEvaluator;
import org.hl7.fhir.r4.model.ValueSet;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Applies the {@link MissingCodeSystemPolicy} to a ValueSet include or exclude that the terminology tables cannot
 * expand as written.
 */
// Created by Claude Opus 5.5
public class TermValueSetIncludeFallbackSvc {

	private final MissingCodeSystemPolicyEvaluator myPolicyEvaluator;
	private final ITermCodeSystemDao myCodeSystemDao;
	private final ITermConceptDao myConceptDao;

	public TermValueSetIncludeFallbackSvc(
			FhirContext theFhirContext,
			JpaStorageSettings theStorageSettings,
			ITermCodeSystemDao theCodeSystemDao,
			ITermConceptDao theConceptDao) {
		myPolicyEvaluator =
				new MissingCodeSystemPolicyEvaluator(theFhirContext, theStorageSettings::getMissingCodeSystemPolicy);
		myCodeSystemDao = theCodeSystemDao;
		myConceptDao = theConceptDao;
	}

	/**
	 * Returns the include or exclude to expand: as given, or, when the policy uses the installed version, no module
	 * supports the version it names and the terminology tables hold a current version of the code system, a copy
	 * naming that version and listing only the listed codes it has. A replacement is added to the expansion messages.
	 *
	 * @param theValidationSupport       the validation support chain, asked whether it supports the named version
	 * @param theIncludeOrExclude        the include or exclude
	 * @param theValueSetCodeAccumulator the expansion, which receives the replacement message
	 * @return the include or exclude to expand, or {@literal null} if it lists codes and that version has none of them
	 */
	@Nullable
	public ValueSet.ConceptSetComponent useInstalledCodeSystemVersionIfNamedOneIsMissing(
			@Nonnull IValidationSupport theValidationSupport,
			@Nonnull ValueSet.ConceptSetComponent theIncludeOrExclude,
			@Nonnull IValueSetConceptAccumulator theValueSetCodeAccumulator) {
		String system = theIncludeOrExclude.getSystem();
		String version = theIncludeOrExclude.getVersion();
		boolean isNamedVersionReplaced = myPolicyEvaluator.isNamedVersionReplaced(
				new ValidationSupportContext(theValidationSupport), system, version);
		if (!isNamedVersionReplaced) {
			return theIncludeOrExclude;
		}
		TermCodeSystemVersion current = Optional.ofNullable(myCodeSystemDao.findByCodeSystemUri(system))
				.map(TermCodeSystem::getCurrentVersion)
				.orElse(null);
		if (current == null) {
			return theIncludeOrExclude;
		}
		String currentVersion = current.getCodeSystemVersionId();
		if (version.equals(currentVersion)) {
			return theIncludeOrExclude;
		}
		theValueSetCodeAccumulator.addMessage(
				myPolicyEvaluator.createVersionReplacedMessage(system, version, currentVersion));
		ValueSet.ConceptSetComponent retVal = theIncludeOrExclude.copy().setVersion(currentVersion);
		if (!retVal.getConcept().isEmpty()) {
			retainListedCodesOfVersion(retVal, current);
			if (retVal.getConcept().isEmpty()) {
				// Without listed codes the include would stand for the whole code system
				return null;
			}
		}
		return retVal;
	}

	/**
	 * Whether {@link MissingCodeSystemPolicy#ACCEPT_LISTED_CODES} takes the codes an include or exclude lists as
	 * listed because the terminology tables hold no concepts for its code system version. The caller checks that the
	 * code system is stored as not-present.
	 *
	 * @param theIncludeOrExclude   the include or exclude
	 * @param theCodeSystemVersion  the stored version of its code system
	 * @return whether to expand the include or exclude in memory, as for a code system the server does not have
	 */
	public boolean isListedCodesOfEmptyCodeSystemVersionAccepted(
			@Nonnull ValueSet.ConceptSetComponent theIncludeOrExclude,
			@Nonnull TermCodeSystemVersion theCodeSystemVersion) {
		return myPolicyEvaluator.isListedCodesAcceptedUnchecked(
				!theIncludeOrExclude.getConcept().isEmpty(), () -> hasNoConcepts(theCodeSystemVersion));
	}

	/**
	 * Whether the terminology tables hold no concepts for the given code system version
	 *
	 * @param theCodeSystemVersion the stored code system version
	 * @return {@literal true} if the version has no concepts
	 */
	public boolean hasNoConcepts(@Nonnull TermCodeSystemVersion theCodeSystemVersion) {
		return myConceptDao
				.findPidsByCodeSystemVersionPid(Pageable.ofSize(1), theCodeSystemVersion.getPid())
				.isEmpty();
	}

	/**
	 * Removes the codes an include or exclude lists that the given code system version does not have, since an
	 * expansion without Hibernate Search takes listed codes as listed.
	 */
	private void retainListedCodesOfVersion(
			ValueSet.ConceptSetComponent theIncludeOrExclude, TermCodeSystemVersion theCodeSystemVersion) {
		List<String> listedCodes = theIncludeOrExclude.getConcept().stream()
				.map(ValueSet.ConceptReferenceComponent::getCode)
				.filter(StringUtils::isNotBlank)
				.distinct()
				.toList();
		Set<String> codesOfVersion = new HashSet<>();
		for (List<String> nextPartition : ListUtils.partition(listedCodes, 500)) {
			myConceptDao.findByCodeSystemAndCodeList(theCodeSystemVersion.getPid(), nextPartition).stream()
					.map(TermConcept::getCode)
					.forEach(codesOfVersion::add);
		}
		theIncludeOrExclude.getConcept().removeIf(t -> !codesOfVersion.contains(t.getCode()));
	}
}
