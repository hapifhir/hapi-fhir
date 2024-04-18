/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.context.support.TranslateConceptResult;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.model.TranslationQuery;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapDao;
import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroup;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.term.api.ITermConceptClientMappingSvc;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.jpa.util.ScrollableResultsIterator;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TermConceptClientMappingSvcImpl implements ITermConceptClientMappingSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(TermConceptClientMappingSvcImpl.class);

	private final int myFetchSize = TermReadSvcImpl.DEFAULT_FETCH_SIZE;

	protected static boolean ourLastResultsFromTranslationCache; // For testing.
	protected static boolean ourLastResultsFromTranslationWithReverseCache; // For testing.

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	@Autowired
	protected FhirContext myContext;

	@Autowired
	protected MemoryCacheService myMemoryCacheService;

	@Autowired
	protected IIdHelperService<JpaPid> myIdHelperService;

	@Autowired
	protected ITermConceptMapDao myConceptMapDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TranslateConceptResults translate(TranslationRequest theTranslationRequest) {
		TranslateConceptResults retVal = new TranslateConceptResults();

		CriteriaBuilder criteriaBuilder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<TermConceptMapGroupElementTarget> query =
				criteriaBuilder.createQuery(TermConceptMapGroupElementTarget.class);
		Root<TermConceptMapGroupElementTarget> root = query.from(TermConceptMapGroupElementTarget.class);

		Join<TermConceptMapGroupElementTarget, TermConceptMapGroupElement> elementJoin =
				root.join("myConceptMapGroupElement");
		Join<TermConceptMapGroupElement, TermConceptMapGroup> groupJoin = elementJoin.join("myConceptMapGroup");
		Join<TermConceptMapGroup, TermConceptMap> conceptMapJoin = groupJoin.join("myConceptMap");

		List<TranslationQuery> translationQueries = theTranslationRequest.getTranslationQueries();
		List<TranslateConceptResult> cachedTargets;
		ArrayList<Predicate> predicates;
		Coding coding;

		// -- get the latest ConceptMapVersion if theTranslationRequest has ConceptMap url but no ConceptMap version
		String latestConceptMapVersion = null;
		if (theTranslationRequest.hasUrl() && !theTranslationRequest.hasConceptMapVersion())
			latestConceptMapVersion = getLatestConceptMapVersion(theTranslationRequest);

		for (TranslationQuery translationQuery : translationQueries) {
			cachedTargets = myMemoryCacheService.getIfPresent(
					MemoryCacheService.CacheEnum.CONCEPT_TRANSLATION, translationQuery);
			if (cachedTargets == null) {
				final List<TranslateConceptResult> targets = new ArrayList<>();

				predicates = new ArrayList<>();

				coding = translationQuery.getCoding();
				if (coding.hasCode()) {
					predicates.add(criteriaBuilder.equal(elementJoin.get("myCode"), coding.getCode()));
				} else {
					throw new InvalidRequestException(
							Msg.code(842) + "A code must be provided for translation to occur.");
				}

				if (coding.hasSystem()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("mySource"), coding.getSystem()));
				}

				if (coding.hasVersion()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("mySourceVersion"), coding.getVersion()));
				}

				if (translationQuery.hasTargetSystem()) {
					predicates.add(
							criteriaBuilder.equal(groupJoin.get("myTarget"), translationQuery.getTargetSystem()));
				}

				if (translationQuery.hasUrl()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myUrl"), translationQuery.getUrl()));
					if (translationQuery.hasConceptMapVersion()) {
						// both url and conceptMapVersion
						predicates.add(criteriaBuilder.equal(
								conceptMapJoin.get("myVersion"), translationQuery.getConceptMapVersion()));
					} else {
						if (StringUtils.isNotBlank(latestConceptMapVersion)) {
							// only url and use latestConceptMapVersion
							predicates.add(
									criteriaBuilder.equal(conceptMapJoin.get("myVersion"), latestConceptMapVersion));
						} else {
							predicates.add(criteriaBuilder.isNull(conceptMapJoin.get("myVersion")));
						}
					}
				}

				if (translationQuery.hasSource()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("mySource"), translationQuery.getSource()));
				}

				if (translationQuery.hasTarget()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myTarget"), translationQuery.getTarget()));
				}

				if (translationQuery.hasResourceId()) {
					IIdType resourceId = translationQuery.getResourceId();
					JpaPid resourcePid =
							myIdHelperService.getPidOrThrowException(RequestPartitionId.defaultPartition(), resourceId);
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myResourcePid"), resourcePid.getId()));
				}

				Predicate outerPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				query.where(outerPredicate);

				// Use scrollable results.
				final TypedQuery<TermConceptMapGroupElementTarget> typedQuery =
						myEntityManager.createQuery(query.select(root));
				org.hibernate.query.Query<TermConceptMapGroupElementTarget> hibernateQuery =
						(org.hibernate.query.Query<TermConceptMapGroupElementTarget>) typedQuery;
				hibernateQuery.setFetchSize(myFetchSize);
				ScrollableResults scrollableResults = hibernateQuery.scroll(ScrollMode.FORWARD_ONLY);
				try (ScrollableResultsIterator<TermConceptMapGroupElementTarget> scrollableResultsIterator =
						new ScrollableResultsIterator<>(scrollableResults)) {

					Set<TermConceptMapGroupElementTarget> matches = new HashSet<>();
					while (scrollableResultsIterator.hasNext()) {
						TermConceptMapGroupElementTarget next = scrollableResultsIterator.next();
						if (matches.add(next)) {

							TranslateConceptResult translationMatch = new TranslateConceptResult();
							if (next.getEquivalence() != null) {
								translationMatch.setEquivalence(
										next.getEquivalence().toCode());
							}

							translationMatch.setCode(next.getCode());
							translationMatch.setSystem(next.getSystem());
							translationMatch.setSystemVersion(next.getSystemVersion());
							translationMatch.setDisplay(next.getDisplay());
							translationMatch.setValueSet(next.getValueSet());
							translationMatch.setSystemVersion(next.getSystemVersion());
							translationMatch.setConceptMapUrl(next.getConceptMapUrl());

							targets.add(translationMatch);
						}
					}
				}

				ourLastResultsFromTranslationCache = false; // For testing.
				myMemoryCacheService.put(MemoryCacheService.CacheEnum.CONCEPT_TRANSLATION, translationQuery, targets);
				retVal.getResults().addAll(targets);
			} else {
				ourLastResultsFromTranslationCache = true; // For testing.
				retVal.getResults().addAll(cachedTargets);
			}
		}

		buildTranslationResult(retVal);
		return retVal;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TranslateConceptResults translateWithReverse(TranslationRequest theTranslationRequest) {
		TranslateConceptResults retVal = new TranslateConceptResults();

		CriteriaBuilder criteriaBuilder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<TermConceptMapGroupElement> query = criteriaBuilder.createQuery(TermConceptMapGroupElement.class);
		Root<TermConceptMapGroupElement> root = query.from(TermConceptMapGroupElement.class);

		Join<TermConceptMapGroupElement, TermConceptMapGroupElementTarget> targetJoin =
				root.join("myConceptMapGroupElementTargets");
		Join<TermConceptMapGroupElement, TermConceptMapGroup> groupJoin = root.join("myConceptMapGroup");
		Join<TermConceptMapGroup, TermConceptMap> conceptMapJoin = groupJoin.join("myConceptMap");

		List<TranslationQuery> translationQueries = theTranslationRequest.getTranslationQueries();
		List<TranslateConceptResult> cachedElements;
		ArrayList<Predicate> predicates;
		Coding coding;

		// -- get the latest ConceptMapVersion if theTranslationRequest has ConceptMap url but no ConceptMap version
		String latestConceptMapVersion = null;
		if (theTranslationRequest.hasUrl() && !theTranslationRequest.hasConceptMapVersion())
			latestConceptMapVersion = getLatestConceptMapVersion(theTranslationRequest);

		for (TranslationQuery translationQuery : translationQueries) {
			cachedElements = myMemoryCacheService.getIfPresent(
					MemoryCacheService.CacheEnum.CONCEPT_TRANSLATION_REVERSE, translationQuery);
			if (cachedElements == null) {
				final List<TranslateConceptResult> elements = new ArrayList<>();

				predicates = new ArrayList<>();

				coding = translationQuery.getCoding();
				String targetCode;
				String targetCodeSystem = null;
				if (coding.hasCode()) {
					predicates.add(criteriaBuilder.equal(targetJoin.get("myCode"), coding.getCode()));
					targetCode = coding.getCode();
				} else {
					throw new InvalidRequestException(
							Msg.code(843) + "A code must be provided for translation to occur.");
				}

				if (coding.hasSystem()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("myTarget"), coding.getSystem()));
					targetCodeSystem = coding.getSystem();
				}

				if (coding.hasVersion()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("myTargetVersion"), coding.getVersion()));
				}

				if (translationQuery.hasUrl()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myUrl"), translationQuery.getUrl()));
					if (translationQuery.hasConceptMapVersion()) {
						// both url and conceptMapVersion
						predicates.add(criteriaBuilder.equal(
								conceptMapJoin.get("myVersion"), translationQuery.getConceptMapVersion()));
					} else {
						if (StringUtils.isNotBlank(latestConceptMapVersion)) {
							// only url and use latestConceptMapVersion
							predicates.add(
									criteriaBuilder.equal(conceptMapJoin.get("myVersion"), latestConceptMapVersion));
						} else {
							predicates.add(criteriaBuilder.isNull(conceptMapJoin.get("myVersion")));
						}
					}
				}

				if (translationQuery.hasTargetSystem()) {
					predicates.add(
							criteriaBuilder.equal(groupJoin.get("mySource"), translationQuery.getTargetSystem()));
				}

				if (translationQuery.hasSource()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myTarget"), translationQuery.getSource()));
				}

				if (translationQuery.hasTarget()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("mySource"), translationQuery.getTarget()));
				}

				if (translationQuery.hasResourceId()) {
					IIdType resourceId = translationQuery.getResourceId();
					JpaPid resourcePid =
							myIdHelperService.getPidOrThrowException(RequestPartitionId.defaultPartition(), resourceId);
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myResourcePid"), resourcePid.getId()));
				}

				Predicate outerPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				query.where(outerPredicate);

				// Use scrollable results.
				final TypedQuery<TermConceptMapGroupElement> typedQuery =
						myEntityManager.createQuery(query.select(root));
				org.hibernate.query.Query<TermConceptMapGroupElement> hibernateQuery =
						(org.hibernate.query.Query<TermConceptMapGroupElement>) typedQuery;
				hibernateQuery.setFetchSize(myFetchSize);
				ScrollableResults scrollableResults = hibernateQuery.scroll(ScrollMode.FORWARD_ONLY);
				try (ScrollableResultsIterator<TermConceptMapGroupElement> scrollableResultsIterator =
						new ScrollableResultsIterator<>(scrollableResults)) {

					Set<TermConceptMapGroupElementTarget> matches = new HashSet<>();
					while (scrollableResultsIterator.hasNext()) {
						TermConceptMapGroupElement nextElement = scrollableResultsIterator.next();

						/* TODO: The invocation of the size() below does not seem to be necessary but for some reason,
						 * but removing it causes tests in TerminologySvcImplR4Test to fail. We use the outcome
						 * in a trace log to avoid ErrorProne flagging an unused return value.
						 */
						int size =
								nextElement.getConceptMapGroupElementTargets().size();
						ourLog.trace("Have {} targets", size);

						myEntityManager.detach(nextElement);

						if (isNotBlank(targetCode)) {
							for (TermConceptMapGroupElementTarget next :
									nextElement.getConceptMapGroupElementTargets()) {
								if (matches.add(next)) {
									if (isBlank(targetCodeSystem)
											|| StringUtils.equals(targetCodeSystem, next.getSystem())) {
										if (StringUtils.equals(targetCode, next.getCode())) {
											TranslateConceptResult translationMatch = new TranslateConceptResult();
											translationMatch.setCode(nextElement.getCode());
											translationMatch.setSystem(nextElement.getSystem());
											translationMatch.setSystemVersion(nextElement.getSystemVersion());
											translationMatch.setDisplay(nextElement.getDisplay());
											translationMatch.setValueSet(nextElement.getValueSet());
											translationMatch.setSystemVersion(nextElement.getSystemVersion());
											translationMatch.setConceptMapUrl(nextElement.getConceptMapUrl());
											if (next.getEquivalence() != null) {
												translationMatch.setEquivalence(
														next.getEquivalence().toCode());
											}

											if (alreadyContainsMapping(elements, translationMatch)
													|| alreadyContainsMapping(retVal.getResults(), translationMatch)) {
												continue;
											}

											elements.add(translationMatch);
										}
									}
								}
							}
						}
					}
				}

				ourLastResultsFromTranslationWithReverseCache = false; // For testing.
				myMemoryCacheService.put(
						MemoryCacheService.CacheEnum.CONCEPT_TRANSLATION_REVERSE, translationQuery, elements);
				retVal.getResults().addAll(elements);
			} else {
				ourLastResultsFromTranslationWithReverseCache = true; // For testing.
				retVal.getResults().addAll(cachedElements);
			}
		}

		buildTranslationResult(retVal);
		return retVal;
	}

	@Override
	public FhirContext getFhirContext() {
		return myContext;
	}

	// Special case for the translate operation with url and without
	// conceptMapVersion, find the latest conecptMapVersion
	private String getLatestConceptMapVersion(TranslationRequest theTranslationRequest) {

		Pageable page = PageRequest.of(0, 1);
		List<TermConceptMap> theConceptMapList = myConceptMapDao.getTermConceptMapEntitiesByUrlOrderByMostRecentUpdate(
				page, theTranslationRequest.getUrl());
		if (!theConceptMapList.isEmpty()) {
			return theConceptMapList.get(0).getVersion();
		}

		return null;
	}

	private void buildTranslationResult(TranslateConceptResults theTranslationResult) {

		String msg;
		if (theTranslationResult.getResults().isEmpty()) {
			theTranslationResult.setResult(false);
			msg = myContext.getLocalizer().getMessage(TermConceptMappingSvcImpl.class, "noMatchesFound");
			theTranslationResult.setMessage(msg);
		} else if (isOnlyNegativeMatches(theTranslationResult)) {
			theTranslationResult.setResult(false);
			msg = myContext.getLocalizer().getMessage(TermConceptMappingSvcImpl.class, "onlyNegativeMatchesFound");
			theTranslationResult.setMessage(msg);
		} else {
			theTranslationResult.setResult(true);
			msg = myContext.getLocalizer().getMessage(TermConceptMappingSvcImpl.class, "matchesFound");
			theTranslationResult.setMessage(msg);
		}
	}

	/**
	 * Evaluates whether a translation result contains any positive matches or only negative ones. This is required
	 * because the <a href="https://hl7.org/fhir/R4/conceptmap-operation-translate.html">FHIR specification</a> states
	 * that the result field "can only be true if at least one returned match has an equivalence which is not unmatched
	 * or disjoint".
	 * @param theTranslationResult the translation result to be evaluated
	 * @return true if all the potential matches in the result have a negative valence (i.e., "unmatched" and "disjoint")
	 */
	private boolean isOnlyNegativeMatches(TranslateConceptResults theTranslationResult) {
		return theTranslationResult.getResults().stream()
				.map(TranslateConceptResult::getEquivalence)
				.allMatch(t -> StringUtils.equals(Enumerations.ConceptMapEquivalence.UNMATCHED.toCode(), t)
						|| StringUtils.equals(Enumerations.ConceptMapEquivalence.DISJOINT.toCode(), t));
	}

	private boolean alreadyContainsMapping(
			List<TranslateConceptResult> elements, TranslateConceptResult translationMatch) {
		for (TranslateConceptResult nextExistingElement : elements) {
			if (StringUtils.equals(nextExistingElement.getSystem(), translationMatch.getSystem())) {
				if (StringUtils.equals(nextExistingElement.getSystemVersion(), translationMatch.getSystemVersion())) {
					if (StringUtils.equals(nextExistingElement.getCode(), translationMatch.getCode())) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
