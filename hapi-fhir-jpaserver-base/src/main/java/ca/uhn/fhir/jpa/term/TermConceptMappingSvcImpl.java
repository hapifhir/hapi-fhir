package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.model.TranslationMatch;
import ca.uhn.fhir.jpa.api.model.TranslationQuery;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.api.model.TranslationResult;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupElementDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupElementTargetDao;
import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroup;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermConceptMappingSvc;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.jpa.util.ScrollableResultsIterator;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ca.uhn.fhir.jpa.term.BaseTermReadSvcImpl.isPlaceholder;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TermConceptMappingSvcImpl implements ITermConceptMappingSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(TermConceptMappingSvcImpl.class);
	private static boolean ourLastResultsFromTranslationCache; // For testing.
	private static boolean ourLastResultsFromTranslationWithReverseCache; // For testing.
	private final int myFetchSize = BaseTermReadSvcImpl.DEFAULT_FETCH_SIZE;
	@Autowired
	protected ITermConceptMapDao myConceptMapDao;
	@Autowired
	protected ITermConceptMapGroupDao myConceptMapGroupDao;
	@Autowired
	protected ITermConceptMapGroupElementDao myConceptMapGroupElementDao;
	@Autowired
	protected ITermConceptMapGroupElementTargetDao myConceptMapGroupElementTargetDao;
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private MemoryCacheService myMemoryCacheService;

	@Override
	@Transactional
	public void deleteConceptMapAndChildren(ResourceTable theResourceTable) {
		deleteConceptMap(theResourceTable);
	}

	@Override
	@Transactional
	public void storeTermConceptMapAndChildren(ResourceTable theResourceTable, ConceptMap theConceptMap) {

		ValidateUtil.isTrueOrThrowInvalidRequest(theResourceTable != null, "No resource supplied");
		if (isPlaceholder(theConceptMap)) {
			ourLog.info("Not storing TermConceptMap for placeholder {}", theConceptMap.getIdElement().toVersionless().getValueAsString());
			return;
		}

		ValidateUtil.isNotBlankOrThrowUnprocessableEntity(theConceptMap.getUrl(), "ConceptMap has no value for ConceptMap.url");
		ourLog.info("Storing TermConceptMap for {}", theConceptMap.getIdElement().toVersionless().getValueAsString());

		TermConceptMap termConceptMap = new TermConceptMap();
		termConceptMap.setResource(theResourceTable);
		termConceptMap.setUrl(theConceptMap.getUrl());
		termConceptMap.setVersion(theConceptMap.getVersion());

		String source = theConceptMap.hasSourceUriType() ? theConceptMap.getSourceUriType().getValueAsString() : null;
		String target = theConceptMap.hasTargetUriType() ? theConceptMap.getTargetUriType().getValueAsString() : null;

		/*
		 * If this is a mapping between "resources" instead of purely between
		 * "concepts" (this is a weird concept that is technically possible, at least as of
		 * FHIR R4), don't try to store the mappings.
		 *
		 * See here for a description of what that is:
		 * http://hl7.org/fhir/conceptmap.html#bnr
		 */
		if ("StructureDefinition".equals(new IdType(source).getResourceType()) ||
			"StructureDefinition".equals(new IdType(target).getResourceType())) {
			return;
		}

		if (source == null && theConceptMap.hasSourceCanonicalType()) {
			source = theConceptMap.getSourceCanonicalType().getValueAsString();
		}
		if (target == null && theConceptMap.hasTargetCanonicalType()) {
			target = theConceptMap.getTargetCanonicalType().getValueAsString();
		}

		/*
		 * For now we always delete old versions. At some point, it would be nice to allow configuration to keep old versions.
		 */
		deleteConceptMap(theResourceTable);

		/*
		 * Do the upload.
		 */
		String conceptMapUrl = termConceptMap.getUrl();
		String conceptMapVersion = termConceptMap.getVersion();
		Optional<TermConceptMap> optionalExistingTermConceptMapByUrl;
		if (isBlank(conceptMapVersion)) {
			optionalExistingTermConceptMapByUrl = myConceptMapDao.findTermConceptMapByUrlAndNullVersion(conceptMapUrl);
		} else {
			optionalExistingTermConceptMapByUrl = myConceptMapDao.findTermConceptMapByUrlAndVersion(conceptMapUrl, conceptMapVersion);
		}
		if (!optionalExistingTermConceptMapByUrl.isPresent()) {
			try {
				if (isNotBlank(source)) {
					termConceptMap.setSource(source);
				}
				if (isNotBlank(target)) {
					termConceptMap.setTarget(target);
				}
			} catch (FHIRException fe) {
				throw new InternalErrorException(fe);
			}
			termConceptMap = myConceptMapDao.save(termConceptMap);
			int codesSaved = 0;

			if (theConceptMap.hasGroup()) {
				TermConceptMapGroup termConceptMapGroup;
				for (ConceptMap.ConceptMapGroupComponent group : theConceptMap.getGroup()) {

					String groupSource = group.getSource();
					if (isBlank(groupSource)) {
						groupSource = source;
					}
					if (isBlank(groupSource)) {
						throw new UnprocessableEntityException("ConceptMap[url='" + theConceptMap.getUrl() + "'] contains at least one group without a value in ConceptMap.group.source");
					}

					String groupTarget = group.getTarget();
					if (isBlank(groupTarget)) {
						groupTarget = target;
					}
					if (isBlank(groupTarget)) {
						throw new UnprocessableEntityException("ConceptMap[url='" + theConceptMap.getUrl() + "'] contains at least one group without a value in ConceptMap.group.target");
					}

					termConceptMapGroup = new TermConceptMapGroup();
					termConceptMapGroup.setConceptMap(termConceptMap);
					termConceptMapGroup.setSource(groupSource);
					termConceptMapGroup.setSourceVersion(group.getSourceVersion());
					termConceptMapGroup.setTarget(groupTarget);
					termConceptMapGroup.setTargetVersion(group.getTargetVersion());
					myConceptMapGroupDao.save(termConceptMapGroup);

					if (group.hasElement()) {
						TermConceptMapGroupElement termConceptMapGroupElement;
						for (ConceptMap.SourceElementComponent element : group.getElement()) {
							if (isBlank(element.getCode())) {
								continue;
							}
							termConceptMapGroupElement = new TermConceptMapGroupElement();
							termConceptMapGroupElement.setConceptMapGroup(termConceptMapGroup);
							termConceptMapGroupElement.setCode(element.getCode());
							termConceptMapGroupElement.setDisplay(element.getDisplay());
							myConceptMapGroupElementDao.save(termConceptMapGroupElement);

							if (element.hasTarget()) {
								TermConceptMapGroupElementTarget termConceptMapGroupElementTarget;
								for (ConceptMap.TargetElementComponent elementTarget : element.getTarget()) {
									if (isBlank(elementTarget.getCode())) {
										continue;
									}
									termConceptMapGroupElementTarget = new TermConceptMapGroupElementTarget();
									termConceptMapGroupElementTarget.setConceptMapGroupElement(termConceptMapGroupElement);
									termConceptMapGroupElementTarget.setCode(elementTarget.getCode());
									termConceptMapGroupElementTarget.setDisplay(elementTarget.getDisplay());
									termConceptMapGroupElementTarget.setEquivalence(elementTarget.getEquivalence());
									myConceptMapGroupElementTargetDao.save(termConceptMapGroupElementTarget);

									if (++codesSaved % 250 == 0) {
										ourLog.info("Have saved {} codes in ConceptMap", codesSaved);
										myConceptMapGroupElementTargetDao.flush();
									}
								}
							}
						}
					}
				}
			}
		} else {
			TermConceptMap existingTermConceptMap = optionalExistingTermConceptMapByUrl.get();

			if (isBlank(conceptMapVersion)) {
				String msg = myContext.getLocalizer().getMessage(
					BaseTermReadSvcImpl.class,
					"cannotCreateDuplicateConceptMapUrl",
					conceptMapUrl,
					existingTermConceptMap.getResource().getIdDt().toUnqualifiedVersionless().getValue());
				throw new UnprocessableEntityException(msg);

			} else {
				String msg = myContext.getLocalizer().getMessage(
					BaseTermReadSvcImpl.class,
					"cannotCreateDuplicateConceptMapUrlAndVersion",
					conceptMapUrl, conceptMapVersion,
					existingTermConceptMap.getResource().getIdDt().toUnqualifiedVersionless().getValue());
				throw new UnprocessableEntityException(msg);
			}
		}

		ourLog.info("Done storing TermConceptMap[{}] for {}", termConceptMap.getId(), theConceptMap.getIdElement().toVersionless().getValueAsString());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TranslationResult translate(TranslationRequest theTranslationRequest) {
		TranslationResult retVal = new TranslationResult();

		CriteriaBuilder criteriaBuilder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<TermConceptMapGroupElementTarget> query = criteriaBuilder.createQuery(TermConceptMapGroupElementTarget.class);
		Root<TermConceptMapGroupElementTarget> root = query.from(TermConceptMapGroupElementTarget.class);

		Join<TermConceptMapGroupElementTarget, TermConceptMapGroupElement> elementJoin = root.join("myConceptMapGroupElement");
		Join<TermConceptMapGroupElement, TermConceptMapGroup> groupJoin = elementJoin.join("myConceptMapGroup");
		Join<TermConceptMapGroup, TermConceptMap> conceptMapJoin = groupJoin.join("myConceptMap");

		List<TranslationQuery> translationQueries = theTranslationRequest.getTranslationQueries();
		List<TranslationMatch> cachedTargets;
		ArrayList<Predicate> predicates;
		Coding coding;

		//-- get the latest ConceptMapVersion if theTranslationRequest has ConceptMap url but no ConceptMap version
		String latestConceptMapVersion = null;
		if (theTranslationRequest.hasUrl() && !theTranslationRequest.hasConceptMapVersion())
			latestConceptMapVersion = getLatestConceptMapVersion(theTranslationRequest);

		for (TranslationQuery translationQuery : translationQueries) {
			cachedTargets = myMemoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.CONCEPT_TRANSLATION, translationQuery);
			if (cachedTargets == null) {
				final List<TranslationMatch> targets = new ArrayList<>();

				predicates = new ArrayList<>();

				coding = translationQuery.getCoding();
				if (coding.hasCode()) {
					predicates.add(criteriaBuilder.equal(elementJoin.get("myCode"), coding.getCode()));
				} else {
					throw new InvalidRequestException("A code must be provided for translation to occur.");
				}

				if (coding.hasSystem()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("mySource"), coding.getSystem()));
				}

				if (coding.hasVersion()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("mySourceVersion"), coding.getVersion()));
				}

				if (translationQuery.hasTargetSystem()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("myTarget"), translationQuery.getTargetSystem().getValueAsString()));
				}

				if (translationQuery.hasUrl()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myUrl"), translationQuery.getUrl().getValueAsString()));
					if (translationQuery.hasConceptMapVersion()) {
						// both url and conceptMapVersion
						predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myVersion"), translationQuery.getConceptMapVersion().getValueAsString()));
					} else {
						if (StringUtils.isNotBlank(latestConceptMapVersion)) {
							// only url and use latestConceptMapVersion
							predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myVersion"), latestConceptMapVersion));
						} else {
							predicates.add(criteriaBuilder.isNull(conceptMapJoin.get("myVersion")));
						}
					}
				}

				if (translationQuery.hasSource()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("mySource"), translationQuery.getSource().getValueAsString()));
				}

				if (translationQuery.hasTarget()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myTarget"), translationQuery.getTarget().getValueAsString()));
				}

				if (translationQuery.hasResourceId()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myResourcePid"), translationQuery.getResourceId()));
				}

				Predicate outerPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				query.where(outerPredicate);

				// Use scrollable results.
				final TypedQuery<TermConceptMapGroupElementTarget> typedQuery = myEntityManager.createQuery(query.select(root));
				org.hibernate.query.Query<TermConceptMapGroupElementTarget> hibernateQuery = (org.hibernate.query.Query<TermConceptMapGroupElementTarget>) typedQuery;
				hibernateQuery.setFetchSize(myFetchSize);
				ScrollableResults scrollableResults = hibernateQuery.scroll(ScrollMode.FORWARD_ONLY);
				try (ScrollableResultsIterator<TermConceptMapGroupElementTarget> scrollableResultsIterator = new ScrollableResultsIterator<>(scrollableResults)) {

					Set<TermConceptMapGroupElementTarget> matches = new HashSet<>();
					while (scrollableResultsIterator.hasNext()) {
						TermConceptMapGroupElementTarget next = scrollableResultsIterator.next();
						if (matches.add(next)) {

							TranslationMatch translationMatch = new TranslationMatch();
							if (next.getEquivalence() != null) {
								translationMatch.setEquivalence(Enumerations.ConceptMapEquivalence.fromCode(next.getEquivalence().toCode()));
							}

							translationMatch.setConcept(
								new Coding()
									.setCode(next.getCode())
									.setSystem(next.getSystem())
									.setVersion(next.getSystemVersion())
									.setDisplay(next.getDisplay())
							);

							translationMatch.setValueSet(next.getValueSet());
							translationMatch.setSystemVersion(next.getSystemVersion());
							translationMatch.setSource(new UriType(next.getConceptMapUrl()));

							targets.add(translationMatch);
						}
					}

				}

				ourLastResultsFromTranslationCache = false; // For testing.
				myMemoryCacheService.put(MemoryCacheService.CacheEnum.CONCEPT_TRANSLATION, translationQuery, targets);
				retVal.getMatches().addAll(targets);
			} else {
				ourLastResultsFromTranslationCache = true; // For testing.
				retVal.getMatches().addAll(cachedTargets);
			}
		}

		buildTranslationResult(retVal);
		return retVal;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TranslationResult translateWithReverse(TranslationRequest theTranslationRequest) {
		TranslationResult retVal = new TranslationResult();

		CriteriaBuilder criteriaBuilder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<TermConceptMapGroupElement> query = criteriaBuilder.createQuery(TermConceptMapGroupElement.class);
		Root<TermConceptMapGroupElement> root = query.from(TermConceptMapGroupElement.class);

		Join<TermConceptMapGroupElement, TermConceptMapGroupElementTarget> targetJoin = root.join("myConceptMapGroupElementTargets");
		Join<TermConceptMapGroupElement, TermConceptMapGroup> groupJoin = root.join("myConceptMapGroup");
		Join<TermConceptMapGroup, TermConceptMap> conceptMapJoin = groupJoin.join("myConceptMap");

		List<TranslationQuery> translationQueries = theTranslationRequest.getTranslationQueries();
		List<TranslationMatch> cachedElements;
		ArrayList<Predicate> predicates;
		Coding coding;

		//-- get the latest ConceptMapVersion if theTranslationRequest has ConceptMap url but no ConceptMap version
		String latestConceptMapVersion = null;
		if (theTranslationRequest.hasUrl() && !theTranslationRequest.hasConceptMapVersion())
			latestConceptMapVersion = getLatestConceptMapVersion(theTranslationRequest);

		for (TranslationQuery translationQuery : translationQueries) {
			cachedElements = myMemoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.CONCEPT_TRANSLATION_REVERSE, translationQuery);
			if (cachedElements == null) {
				final List<TranslationMatch> elements = new ArrayList<>();

				predicates = new ArrayList<>();

				coding = translationQuery.getCoding();
				String targetCode;
				String targetCodeSystem = null;
				if (coding.hasCode()) {
					predicates.add(criteriaBuilder.equal(targetJoin.get("myCode"), coding.getCode()));
					targetCode = coding.getCode();
				} else {
					throw new InvalidRequestException("A code must be provided for translation to occur.");
				}

				if (coding.hasSystem()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("myTarget"), coding.getSystem()));
					targetCodeSystem = coding.getSystem();
				}

				if (coding.hasVersion()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("myTargetVersion"), coding.getVersion()));
				}

				if (translationQuery.hasUrl()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myUrl"), translationQuery.getUrl().getValueAsString()));
					if (translationQuery.hasConceptMapVersion()) {
						// both url and conceptMapVersion
						predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myVersion"), translationQuery.getConceptMapVersion().getValueAsString()));
					} else {
						if (StringUtils.isNotBlank(latestConceptMapVersion)) {
							// only url and use latestConceptMapVersion
							predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myVersion"), latestConceptMapVersion));
						} else {
							predicates.add(criteriaBuilder.isNull(conceptMapJoin.get("myVersion")));
						}
					}
				}

				if (translationQuery.hasTargetSystem()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("mySource"), translationQuery.getTargetSystem().getValueAsString()));
				}

				if (translationQuery.hasSource()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myTarget"), translationQuery.getSource().getValueAsString()));
				}

				if (translationQuery.hasTarget()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("mySource"), translationQuery.getTarget().getValueAsString()));
				}

				if (translationQuery.hasResourceId()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myResourcePid"), translationQuery.getResourceId()));
				}

				Predicate outerPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				query.where(outerPredicate);

				// Use scrollable results.
				final TypedQuery<TermConceptMapGroupElement> typedQuery = myEntityManager.createQuery(query.select(root));
				org.hibernate.query.Query<TermConceptMapGroupElement> hibernateQuery = (org.hibernate.query.Query<TermConceptMapGroupElement>) typedQuery;
				hibernateQuery.setFetchSize(myFetchSize);
				ScrollableResults scrollableResults = hibernateQuery.scroll(ScrollMode.FORWARD_ONLY);
				try (ScrollableResultsIterator<TermConceptMapGroupElement> scrollableResultsIterator = new ScrollableResultsIterator<>(scrollableResults)) {

					Set<TermConceptMapGroupElement> matches = new HashSet<>();
					while (scrollableResultsIterator.hasNext()) {
						TermConceptMapGroupElement nextElement = scrollableResultsIterator.next();

						// TODO: The invocation of the size() below does not seem to be necessary but for some reason, removing it causes tests in TerminologySvcImplR4Test to fail.
						nextElement.getConceptMapGroupElementTargets().size();

						myEntityManager.detach(nextElement);

						if (isNotBlank(targetCode)) {
							for (Iterator<TermConceptMapGroupElementTarget> iter = nextElement.getConceptMapGroupElementTargets().iterator(); iter.hasNext(); ) {
								TermConceptMapGroupElementTarget next = iter.next();


								if (matches.add(nextElement)) {
									if (isBlank(targetCodeSystem) || StringUtils.equals(targetCodeSystem, next.getSystem())) {
										if (StringUtils.equals(targetCode, next.getCode())) {
											TranslationMatch translationMatch = new TranslationMatch();
											translationMatch.setConcept(
												new Coding()
													.setCode(nextElement.getCode())
													.setSystem(nextElement.getSystem())
													.setVersion(nextElement.getSystemVersion())
													.setDisplay(nextElement.getDisplay())
											);

											translationMatch.setValueSet(nextElement.getValueSet());
											translationMatch.setSystemVersion(nextElement.getSystemVersion());
											translationMatch.setSource(new UriType(nextElement.getConceptMapUrl()));
											elements.add(translationMatch);
										}
									}

								}
							}
						}
					}

				}

				ourLastResultsFromTranslationWithReverseCache = false; // For testing.
				myMemoryCacheService.put(MemoryCacheService.CacheEnum.CONCEPT_TRANSLATION_REVERSE, translationQuery, elements);
				retVal.getMatches().addAll(elements);
			} else {
				ourLastResultsFromTranslationWithReverseCache = true; // For testing.
				retVal.getMatches().addAll(cachedElements);
			}
		}

		buildTranslationResult(retVal);
		return retVal;
	}

	public void deleteConceptMap(ResourceTable theResourceTable) {
		// Get existing entity so it can be deleted.
		Optional<TermConceptMap> optionalExistingTermConceptMapById = myConceptMapDao.findTermConceptMapByResourcePid(theResourceTable.getId());

		if (optionalExistingTermConceptMapById.isPresent()) {
			TermConceptMap existingTermConceptMap = optionalExistingTermConceptMapById.get();

			ourLog.info("Deleting existing TermConceptMap[{}] and its children...", existingTermConceptMap.getId());
			for (TermConceptMapGroup group : existingTermConceptMap.getConceptMapGroups()) {

				for (TermConceptMapGroupElement element : group.getConceptMapGroupElements()) {

					for (TermConceptMapGroupElementTarget target : element.getConceptMapGroupElementTargets()) {

						myConceptMapGroupElementTargetDao.deleteTermConceptMapGroupElementTargetById(target.getId());
					}

					myConceptMapGroupElementDao.deleteTermConceptMapGroupElementById(element.getId());
				}

				myConceptMapGroupDao.deleteTermConceptMapGroupById(group.getId());
			}

			myConceptMapDao.deleteTermConceptMapById(existingTermConceptMap.getId());
			ourLog.info("Done deleting existing TermConceptMap[{}] and its children.", existingTermConceptMap.getId());
		}
	}

	// Special case for the translate operation with url and without
	// conceptMapVersion, find the latest conecptMapVersion
	private String getLatestConceptMapVersion(TranslationRequest theTranslationRequest) {

		Pageable page = PageRequest.of(0, 1);
		List<TermConceptMap> theConceptMapList = myConceptMapDao.getTermConceptMapEntitiesByUrlOrderByMostRecentUpdate(page,
			theTranslationRequest.getUrl().asStringValue());
		if (!theConceptMapList.isEmpty()) {
			return theConceptMapList.get(0).getVersion();
		}

		return null;
	}

	private void buildTranslationResult(TranslationResult theTranslationResult) {

		String msg;
		if (theTranslationResult.getMatches().isEmpty()) {
			theTranslationResult.setResult(new BooleanType(false));
			msg = myContext.getLocalizer().getMessage(TermConceptMappingSvcImpl.class, "noMatchesFound");
			theTranslationResult.setMessage(new StringType(msg));
		} else {
			theTranslationResult.setResult(new BooleanType(true));
			msg = myContext.getLocalizer().getMessage(TermConceptMappingSvcImpl.class, "matchesFound");
			theTranslationResult.setMessage(new StringType(msg));
		}

	}


	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	public static void clearOurLastResultsFromTranslationCache() {
		ourLastResultsFromTranslationCache = false;
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	public static void clearOurLastResultsFromTranslationWithReverseCache() {
		ourLastResultsFromTranslationWithReverseCache = false;
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	static boolean isOurLastResultsFromTranslationCache() {
		return ourLastResultsFromTranslationCache;
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	static boolean isOurLastResultsFromTranslationWithReverseCache() {
		return ourLastResultsFromTranslationWithReverseCache;
	}

}
