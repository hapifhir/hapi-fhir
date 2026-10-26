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

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.MissingCodeSystemPolicy;
import ca.uhn.fhir.context.support.MissingCodeSystemPolicyEvaluator;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import jakarta.annotation.Nonnull;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Applies the {@link MissingCodeSystemPolicy} to a ValueSet include or exclude that names a code system version the
 * server does not have.
 */
// Created by Claude Opus 5.5
public class TermValueSetInstalledVersionSvc {

	private final MissingCodeSystemPolicyEvaluator myPolicyEvaluator;
	private final ITermCodeSystemDao myCodeSystemDao;
	private final ITermConceptDao myConceptDao;

	public TermValueSetInstalledVersionSvc(
			MissingCodeSystemPolicyEvaluator thePolicyEvaluator,
			ITermCodeSystemDao theCodeSystemDao,
			ITermConceptDao theConceptDao) {
		myPolicyEvaluator = thePolicyEvaluator;
		myCodeSystemDao = theCodeSystemDao;
		myConceptDao = theConceptDao;
	}

	/**
	 * Points an include or exclude whose named code system version the server does not have at the installed version,
	 * when the policy uses the installed version and the terminology tables hold another current version of the code
	 * system. The result is a copy naming that version and listing only the listed codes it has, and a replacement
	 * message is added to the expansion. Otherwise the include or exclude is returned as given.
	 *
	 * @param theValidationSupport       the validation support chain, asked whether it supports the named version
	 * @param theIncludeOrExclude        the include or exclude
	 * @param theValueSetCodeAccumulator the expansion, which receives the replacement message
	 * @return the include or exclude to expand, or empty if it lists codes and the installed version has none of them
	 */
	@Nonnull
	public Optional<ValueSet.ConceptSetComponent> applyInstalledVersionPolicy(
			@Nonnull IValidationSupport theValidationSupport,
			@Nonnull ValueSet.ConceptSetComponent theIncludeOrExclude,
			@Nonnull IValueSetConceptAccumulator theValueSetCodeAccumulator) {
		Optional<TermCodeSystemVersion> installedVersion =
				findInstalledVersionReplacingNamedOne(theValidationSupport, theIncludeOrExclude);
		if (installedVersion.isEmpty()) {
			return Optional.of(theIncludeOrExclude);
		}
		theValueSetCodeAccumulator.addMessage(myPolicyEvaluator.createVersionReplacedMessage(
				theIncludeOrExclude.getSystem(),
				theIncludeOrExclude.getVersion(),
				installedVersion.get().getCodeSystemVersionId()));
		return copyForInstalledVersion(theIncludeOrExclude, installedVersion.get());
	}

	/**
	 * The current version of the code system in the terminology tables, when the policy replaces the version the
	 * include or exclude names and that current version is another one
	 */
	private Optional<TermCodeSystemVersion> findInstalledVersionReplacingNamedOne(
			IValidationSupport theValidationSupport, ValueSet.ConceptSetComponent theIncludeOrExclude) {
		String system = theIncludeOrExclude.getSystem();
		String namedVersion = theIncludeOrExclude.getVersion();
		boolean isNamedVersionReplaced = myPolicyEvaluator.isNamedVersionReplaced(
				new ValidationSupportContext(theValidationSupport), system, namedVersion);
		if (!isNamedVersionReplaced) {
			return Optional.empty();
		}
		return Optional.ofNullable(myCodeSystemDao.findByCodeSystemUri(system))
				.map(TermCodeSystem::getCurrentVersion)
				.filter(t -> !namedVersion.equals(t.getCodeSystemVersionId()));
	}

	/**
	 * A copy of the include or exclude naming the installed version and listing only the listed codes it has, or
	 * empty if it lists codes and the installed version has none of them
	 */
	private Optional<ValueSet.ConceptSetComponent> copyForInstalledVersion(
			ValueSet.ConceptSetComponent theIncludeOrExclude, TermCodeSystemVersion theInstalledVersion) {
		ValueSet.ConceptSetComponent retVal =
				theIncludeOrExclude.copy().setVersion(theInstalledVersion.getCodeSystemVersionId());
		if (retVal.getConcept().isEmpty()) {
			return Optional.of(retVal);
		}
		retainListedCodesOfVersion(retVal, theInstalledVersion);
		// Without listed codes the `include` would stand for the whole code system
		return retVal.getConcept().isEmpty() ? Optional.empty() : Optional.of(retVal);
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
