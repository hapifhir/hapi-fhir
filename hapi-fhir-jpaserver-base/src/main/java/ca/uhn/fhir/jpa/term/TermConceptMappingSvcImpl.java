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

import ca.uhn.fhir.context.support.TranslateConceptResult;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupElementDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupElementTargetDao;
import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroup;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermConceptMappingSvc;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static ca.uhn.fhir.jpa.term.TermReadSvcImpl.isPlaceholder;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TermConceptMappingSvcImpl extends TermConceptClientMappingSvcImpl implements ITermConceptMappingSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(TermConceptMappingSvcImpl.class);

	@Autowired
	protected ITermConceptMapGroupDao myConceptMapGroupDao;

	@Autowired
	protected ITermConceptMapGroupElementDao myConceptMapGroupElementDao;

	@Autowired
	protected ITermConceptMapGroupElementTargetDao myConceptMapGroupElementTargetDao;

	@Override
	public String getName() {
		return getFhirContext().getVersion().getVersion() + " ConceptMap Validation Support";
	}

	@Override
	@Transactional
	public void deleteConceptMapAndChildren(ResourceTable theResourceTable) {
		deleteConceptMap(theResourceTable);
	}

	@Override
	@Transactional
	public TranslateConceptResults translateConcept(TranslateCodeRequest theRequest) {
		TranslationRequest request = TranslationRequest.fromTranslateCodeRequest(theRequest);
		if (request.hasReverse() && request.getReverseAsBoolean()) {
			return translateWithReverse(request);
		}

		return translate(request);
	}

	@Override
	@Transactional
	public void storeTermConceptMapAndChildren(ResourceTable theResourceTable, ConceptMap theConceptMap) {

		ValidateUtil.isTrueOrThrowInvalidRequest(theResourceTable != null, "No resource supplied");
		if (isPlaceholder(theConceptMap)) {
			ourLog.info(
					"Not storing TermConceptMap for placeholder {}",
					theConceptMap.getIdElement().toVersionless().getValueAsString());
			return;
		}

		ValidateUtil.isNotBlankOrThrowUnprocessableEntity(
				theConceptMap.getUrl(), "ConceptMap has no value for ConceptMap.url");
		ourLog.info(
				"Storing TermConceptMap for {}",
				theConceptMap.getIdElement().toVersionless().getValueAsString());

		TermConceptMap termConceptMap = new TermConceptMap();
		termConceptMap.setResource(theResourceTable);
		termConceptMap.setUrl(theConceptMap.getUrl());
		termConceptMap.setVersion(theConceptMap.getVersion());

		String source = theConceptMap.hasSourceUriType()
				? theConceptMap.getSourceUriType().getValueAsString()
				: null;
		String target = theConceptMap.hasTargetUriType()
				? theConceptMap.getTargetUriType().getValueAsString()
				: null;

		/*
		 * If this is a mapping between "resources" instead of purely between
		 * "concepts" (this is a weird concept that is technically possible, at least as of
		 * FHIR R4), don't try to store the mappings.
		 *
		 * See here for a description of what that is:
		 * http://hl7.org/fhir/conceptmap.html#bnr
		 */
		if ("StructureDefinition".equals(new IdType(source).getResourceType())
				|| "StructureDefinition".equals(new IdType(target).getResourceType())) {
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
			optionalExistingTermConceptMapByUrl =
					myConceptMapDao.findTermConceptMapByUrlAndVersion(conceptMapUrl, conceptMapVersion);
		}
		if (optionalExistingTermConceptMapByUrl.isEmpty()) {
			try {
				if (isNotBlank(source)) {
					termConceptMap.setSource(source);
				}
				if (isNotBlank(target)) {
					termConceptMap.setTarget(target);
				}
			} catch (FHIRException fe) {
				throw new InternalErrorException(Msg.code(837) + fe);
			}
			termConceptMap = myConceptMapDao.save(termConceptMap);
			int codesSaved = 0;

			TermConceptMapGroup termConceptMapGroup;
			for (ConceptMap.ConceptMapGroupComponent group : theConceptMap.getGroup()) {

				String groupSource = group.getSource();
				if (isBlank(groupSource)) {
					groupSource = source;
				}
				if (isBlank(groupSource)) {
					throw new UnprocessableEntityException(Msg.code(838) + "ConceptMap[url='" + theConceptMap.getUrl()
							+ "'] contains at least one group without a value in ConceptMap.group.source");
				}

				String groupTarget = group.getTarget();
				if (isBlank(groupTarget)) {
					groupTarget = target;
				}
				if (isBlank(groupTarget)) {
					throw new UnprocessableEntityException(Msg.code(839) + "ConceptMap[url='" + theConceptMap.getUrl()
							+ "'] contains at least one group without a value in ConceptMap.group.target");
				}

				termConceptMapGroup = new TermConceptMapGroup();
				termConceptMapGroup.setConceptMap(termConceptMap);
				termConceptMapGroup.setSource(groupSource);
				termConceptMapGroup.setSourceVersion(group.getSourceVersion());
				termConceptMapGroup.setTarget(groupTarget);
				termConceptMapGroup.setTargetVersion(group.getTargetVersion());
				termConceptMap.getConceptMapGroups().add(termConceptMapGroup);
				termConceptMapGroup = myConceptMapGroupDao.save(termConceptMapGroup);

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
						termConceptMapGroup.getConceptMapGroupElements().add(termConceptMapGroupElement);
						termConceptMapGroupElement = myConceptMapGroupElementDao.save(termConceptMapGroupElement);

						if (element.hasTarget()) {
							TermConceptMapGroupElementTarget termConceptMapGroupElementTarget;
							for (ConceptMap.TargetElementComponent elementTarget : element.getTarget()) {
								if (isBlank(elementTarget.getCode())
										&& elementTarget.getEquivalence()
												!= Enumerations.ConceptMapEquivalence.UNMATCHED) {
									continue;
								}
								termConceptMapGroupElementTarget = new TermConceptMapGroupElementTarget();
								termConceptMapGroupElementTarget.setConceptMapGroupElement(termConceptMapGroupElement);
								if (isNotBlank(elementTarget.getCode())) {
									termConceptMapGroupElementTarget.setCode(elementTarget.getCode());
									termConceptMapGroupElementTarget.setDisplay(elementTarget.getDisplay());
								}
								termConceptMapGroupElementTarget.setEquivalence(elementTarget.getEquivalence());
								termConceptMapGroupElement
										.getConceptMapGroupElementTargets()
										.add(termConceptMapGroupElementTarget);
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

		} else {
			TermConceptMap existingTermConceptMap = optionalExistingTermConceptMapByUrl.get();

			if (isBlank(conceptMapVersion)) {
				String msg = myContext
						.getLocalizer()
						.getMessage(
								TermReadSvcImpl.class,
								"cannotCreateDuplicateConceptMapUrl",
								conceptMapUrl,
								existingTermConceptMap
										.getResource()
										.getIdDt()
										.toUnqualifiedVersionless()
										.getValue());
				throw new UnprocessableEntityException(Msg.code(840) + msg);

			} else {
				String msg = myContext
						.getLocalizer()
						.getMessage(
								TermReadSvcImpl.class,
								"cannotCreateDuplicateConceptMapUrlAndVersion",
								conceptMapUrl,
								conceptMapVersion,
								existingTermConceptMap
										.getResource()
										.getIdDt()
										.toUnqualifiedVersionless()
										.getValue());
				throw new UnprocessableEntityException(Msg.code(841) + msg);
			}
		}

		ourLog.info(
				"Done storing TermConceptMap[{}] for {}",
				termConceptMap.getId(),
				theConceptMap.getIdElement().toVersionless().getValueAsString());
	}

	public void deleteConceptMap(ResourceTable theResourceTable) {
		// Get existing entity so it can be deleted.
		Optional<TermConceptMap> optionalExistingTermConceptMapById =
				myConceptMapDao.findTermConceptMapByResourcePid(theResourceTable.getId());

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

	public static Parameters toParameters(TranslateConceptResults theTranslationResult) {
		Parameters retVal = new Parameters();

		retVal.addParameter().setName("result").setValue(new BooleanType(theTranslationResult.getResult()));

		if (theTranslationResult.getMessage() != null) {
			retVal.addParameter().setName("message").setValue(new StringType(theTranslationResult.getMessage()));
		}

		for (TranslateConceptResult translationMatch : theTranslationResult.getResults()) {
			Parameters.ParametersParameterComponent matchParam =
					retVal.addParameter().setName("match");
			populateTranslateMatchParts(translationMatch, matchParam);
		}

		return retVal;
	}

	private static void populateTranslateMatchParts(
			TranslateConceptResult theTranslationMatch, Parameters.ParametersParameterComponent theParam) {
		if (theTranslationMatch.getEquivalence() != null) {
			theParam.addPart().setName("equivalence").setValue(new CodeType(theTranslationMatch.getEquivalence()));
		}

		if (isNotBlank(theTranslationMatch.getSystem())
				|| isNotBlank(theTranslationMatch.getCode())
				|| isNotBlank(theTranslationMatch.getDisplay())) {
			Coding value = new Coding(
					theTranslationMatch.getSystem(), theTranslationMatch.getCode(), theTranslationMatch.getDisplay());

			if (isNotBlank(theTranslationMatch.getSystemVersion())) {
				value.setVersion(theTranslationMatch.getSystemVersion());
			}

			theParam.addPart().setName("concept").setValue(value);
		}

		if (isNotBlank(theTranslationMatch.getConceptMapUrl())) {
			theParam.addPart().setName("source").setValue(new UriType(theTranslationMatch.getConceptMapUrl()));
		}
	}
}
