/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.api.ITermConceptMappingSvc;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.ValidateUtil;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JpaResourceDaoConceptMap<T extends IBaseResource> extends JpaResourceDao<T>
		implements IFhirResourceDaoConceptMap<T> {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaResourceDaoConceptMap.class);

	@Autowired
	private ITermConceptMappingSvc myTermConceptMappingSvc;

	@Autowired
	private IValidationSupport myValidationSupport;

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

	@Override
	public TranslateConceptResults translate(
			TranslationRequest theTranslationRequest, RequestDetails theRequestDetails) {
		IValidationSupport.TranslateCodeRequest translateCodeRequest = theTranslationRequest.asTranslateCodeRequest();
		return myValidationSupport.translateConcept(translateCodeRequest);
	}

	@Override
	public ResourceTable updateEntity(
			RequestDetails theRequestDetails,
			IBaseResource theResource,
			IBasePersistedResource theEntity,
			Date theDeletedTimestampOrNull,
			boolean thePerformIndexing,
			boolean theUpdateVersion,
			TransactionDetails theTransactionDetails,
			boolean theForceUpdate,
			boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(
				theRequestDetails,
				theResource,
				theEntity,
				theDeletedTimestampOrNull,
				thePerformIndexing,
				theUpdateVersion,
				theTransactionDetails,
				theForceUpdate,
				theCreateNewHistoryEntry);

		boolean entityWasSaved = !retVal.isUnchangedInCurrentOperation();
		boolean shouldProcessUpdate = entityWasSaved && thePerformIndexing;
		if (shouldProcessUpdate) {
			if (retVal.getDeleted() == null) {
				ConceptMap conceptMap = myVersionCanonicalizer.conceptMapToCanonical(theResource);
				myTermConceptMappingSvc.storeTermConceptMapAndChildren(retVal, conceptMap);
			} else {
				myTermConceptMappingSvc.deleteConceptMapAndChildren(retVal);
			}
		}

		return retVal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IBaseOperationOutcome addMapping(AddMappingRequest theRequest, RequestDetails theRequestDetails) {
		String sourceDisplay = theRequest.getSourceDisplay();
		String targetDisplay = theRequest.getTargetDisplay();
		String equivalence = theRequest.getEquivalence();
		ValidateUtil.isNotBlankOrThrowInvalidRequest(equivalence, "Equivalence must be provided");

		ConceptMap conceptMapCanonical =
				fetchExistingConceptMapAndConvertToCanonical(theRequest, true, theRequestDetails);

		List<ConceptMap.ConceptMapGroupComponent> groups = findOrCreateGroup(theRequest, conceptMapCanonical, true);
		ConceptMap.ConceptMapGroupComponent group = groups.get(0);

		List<ConceptMap.SourceElementComponent> sourceElements =
				findOrCreateSourceElements(theRequest, group, true, sourceDisplay);
		ConceptMap.SourceElementComponent sourceElement = sourceElements.get(0);

		findOrCreateTargetElement(theRequest, sourceElement, true, targetDisplay, equivalence);

		T conceptMapToStore = (T) myVersionCanonicalizer.conceptMapFromCanonical(conceptMapCanonical);
		if (conceptMapToStore.getIdElement().hasIdPart()) {
			update(conceptMapToStore, theRequestDetails);
		} else {
			create(conceptMapToStore, theRequestDetails);
		}

		IBaseOperationOutcome operationOutcome = OperationOutcomeUtil.createOperationOutcome(
				OperationOutcomeUtil.OO_SEVERITY_WARN,
				"Mapping has been added",
				OperationOutcomeUtil.OO_ISSUE_CODE_PROCESSING,
				myFhirContext,
				null);
		return operationOutcome;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IBaseOperationOutcome removeMapping(RemoveMappingRequest theRequest, RequestDetails theRequestDetails) {
		ConceptMap conceptMapCanonical =
				fetchExistingConceptMapAndConvertToCanonical(theRequest, false, theRequestDetails);
		if (conceptMapCanonical == null) {
			String message = "No ConceptMap found matching the given URL and/or version. No action performed.";
			ourLog.warn(message);
			IBaseOperationOutcome operationOutcome = OperationOutcomeUtil.createOperationOutcome(
					OperationOutcomeUtil.OO_SEVERITY_WARN,
					message,
					OperationOutcomeUtil.OO_ISSUE_CODE_PROCESSING,
					myFhirContext,
					null);
			return operationOutcome;
		}

		int mappingsRemoved = 0;
		List<ConceptMap.ConceptMapGroupComponent> groups = findOrCreateGroup(theRequest, conceptMapCanonical, false);
		for (ConceptMap.ConceptMapGroupComponent group : groups) {

			List<ConceptMap.SourceElementComponent> sourceElements =
					findOrCreateSourceElements(theRequest, group, false, null);
			for (ConceptMap.SourceElementComponent sourceElement : sourceElements) {

				List<ConceptMap.TargetElementComponent> targetElements =
						findOrCreateTargetElement(theRequest, sourceElement, false, null, null);
				for (ConceptMap.TargetElementComponent targetElement : targetElements) {
					mappingsRemoved++;
					sourceElement.getTarget().remove(targetElement);
				}

				if (sourceElement.getTarget().isEmpty()) {
					group.getElement().remove(sourceElement);
				}
			}
		}

		T conceptMapToStore = (T) myVersionCanonicalizer.conceptMapFromCanonical(conceptMapCanonical);
		update(conceptMapToStore, theRequestDetails);

		String message = "Removed " + mappingsRemoved + " ConceptMap mappings";
		ourLog.info(message);
		IBaseOperationOutcome operationOutcome = OperationOutcomeUtil.createOperationOutcome(
				OperationOutcomeUtil.OO_SEVERITY_WARN,
				message,
				OperationOutcomeUtil.OO_ISSUE_CODE_PROCESSING,
				myFhirContext,
				null);
		return operationOutcome;
	}

	private static List<ConceptMap.TargetElementComponent> findOrCreateTargetElement(
			RemoveMappingRequest theRequest,
			ConceptMap.SourceElementComponent sourceElement,
			boolean theCreate,
			String targetDisplay,
			String equivalence) {
		String targetCode = theRequest.getTargetCode();
		ValidateUtil.isNotBlankOrThrowInvalidRequest(targetCode, "Target code must be provided");

		List<ConceptMap.TargetElementComponent> retVal = sourceElement.getTarget().stream()
				.filter(t -> targetCode.equals(t.getCode()))
				.collect(Collectors.toList());

		if (retVal.isEmpty() && theCreate) {
			ConceptMap.TargetElementComponent newTarget = sourceElement.addTarget();
			newTarget.setCode(targetCode);
			newTarget.setDisplay(targetDisplay);
			newTarget.setEquivalence(Enumerations.ConceptMapEquivalence.fromCode(equivalence));
			retVal.add(newTarget);
		}

		return retVal;
	}

	private static List<ConceptMap.SourceElementComponent> findOrCreateSourceElements(
			RemoveMappingRequest theRequest,
			ConceptMap.ConceptMapGroupComponent group,
			boolean theCreate,
			String sourceDisplay) {
		String sourceCode = theRequest.getSourceCode();
		ValidateUtil.isNotBlankOrThrowInvalidRequest(sourceCode, "Source code must be provided");

		List<ConceptMap.SourceElementComponent> retVal = group.getElement().stream()
				.filter(t -> sourceCode.equals(t.getCode()))
				.collect(Collectors.toList());

		if (retVal.isEmpty() && theCreate) {
			ConceptMap.SourceElementComponent newElement = group.addElement();
			newElement.setCode(sourceCode);
			newElement.setDisplay(sourceDisplay);
			retVal.add(newElement);
		}

		return retVal;
	}

	private static List<ConceptMap.ConceptMapGroupComponent> findOrCreateGroup(
			RemoveMappingRequest theRequest, ConceptMap conceptMapCanonical, boolean theCreateIfNotFound) {
		String sourceSystem = theRequest.getSourceSystem();
		ValidateUtil.isNotBlankOrThrowInvalidRequest(sourceSystem, "Source system must be provided");
		String sourceSystemVersion = theRequest.getSourceSystemVersion();
		String targetSystem = theRequest.getTargetSystem();
		ValidateUtil.isNotBlankOrThrowInvalidRequest(sourceSystem, "Target system must be provided");
		String targetSystemVersion = theRequest.getTargetSystemVersion();

		List<ConceptMap.ConceptMapGroupComponent> retVal = conceptMapCanonical.getGroup().stream()
				.filter(t -> {
					boolean match = sourceSystem.equals(t.getSource());
					match &= targetSystem.equals(t.getTarget());
					match &= isBlank(sourceSystemVersion) || sourceSystemVersion.equals(t.getSourceVersion());
					match &= isBlank(targetSystemVersion) || targetSystemVersion.equals(t.getTargetVersion());
					return match;
				})
				.collect(Collectors.toList());

		if (retVal.isEmpty() && theCreateIfNotFound) {
			ConceptMap.ConceptMapGroupComponent newGroup = conceptMapCanonical.addGroup();
			newGroup.setSource(sourceSystem);
			newGroup.setSourceVersion(sourceSystemVersion);
			newGroup.setTarget(targetSystem);
			newGroup.setTargetVersion(targetSystemVersion);
			retVal.add(newGroup);
		}

		return retVal;
	}

	private ConceptMap fetchExistingConceptMapAndConvertToCanonical(
			RemoveMappingRequest theRequest, boolean theCreateIfNotFound, RequestDetails theRequestDetails) {
		String conceptMapUrl = theRequest.getConceptMapUri();
		ValidateUtil.isNotBlankOrThrowInvalidRequest(conceptMapUrl, "ConceptMap URI must be provided");
		String conceptMapVersion = theRequest.getConceptMapVersion();

		SearchParameterMap map = conceptMapUrlToParameterMap(conceptMapUrl, conceptMapVersion);
		IBundleProvider bundle = search(map, theRequestDetails);

		ConceptMap conceptMapCanonical;
		if (bundle.sizeOrThrowNpe() > 1) {
			throw new InvalidRequestException(Msg.code(1743) + "Multiple ConceptMap resources match URL["
					+ conceptMapUrl + "]. Do you need to specify a version?");
		} else if (bundle.isEmpty()) {

			if (theCreateIfNotFound) {
				ourLog.info("Creating new ConceptMap with URL: {}", conceptMapUrl);
				conceptMapCanonical = new ConceptMap();
				conceptMapCanonical.setUrl(conceptMapUrl);
				conceptMapCanonical.setDate(new Date());
				conceptMapCanonical.setDescription("Automatically created by HAPI FHIR");
			} else {
				conceptMapCanonical = null;
			}

		} else {

			IBaseResource conceptMap = bundle.getResources(0, 1).get(0);
			conceptMapCanonical = myVersionCanonicalizer.conceptMapToCanonical(conceptMap);
		}
		return conceptMapCanonical;
	}

	/**
	 * @param theConceptMapUrl The URL. Can include a <code>|version</code> suffix, in which case
	 *                         {@literal theConceptMapVersion} should be null.
	 * @param theConceptMapVersion The version
	 */
	public static SearchParameterMap conceptMapUrlToParameterMap(String theConceptMapUrl, String theConceptMapVersion) {

		String url;
		String version;

		int pipeIndex = theConceptMapUrl.indexOf('|');
		if (pipeIndex > 0) {
			url = theConceptMapUrl.substring(0, pipeIndex);
			version = theConceptMapUrl.substring(pipeIndex + 1);
		} else {
			url = theConceptMapUrl;
			version = theConceptMapVersion;
		}

		if (isNotBlank(theConceptMapVersion) && isNotBlank(version) && !theConceptMapVersion.equals(version)) {
			// FIXME: add code
			throw new InvalidRequestException(Msg.code(1) + "ConceptMap URL includes a version[" + version
					+ "] which conflicts with specified version[" + theConceptMapVersion + "]");
		}

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronousUpTo(2);
		map.add(ConceptMap.SP_URL, new UriParam(url));
		if (version != null) {
			map.add(ConceptMap.SP_VERSION, new TokenParam(version));
		}
		return map;
	}
}
