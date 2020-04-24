package ca.uhn.fhir.jpa.dao.dstu3;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.model.TranslationMatch;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.api.model.TranslationResult;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hl7.fhir.convertors.conv30_40.ConceptMap30_40.convertConceptMap;

public class FhirResourceDaoConceptMapDstu3 extends BaseHapiFhirResourceDao<ConceptMap> implements IFhirResourceDaoConceptMap<ConceptMap> {
	@Autowired
	private ITermReadSvc myHapiTerminologySvc;

	@Override
	public TranslationResult translate(TranslationRequest theTranslationRequest, RequestDetails theRequestDetails) {
		if (theTranslationRequest.hasReverse() && theTranslationRequest.getReverseAsBoolean()) {
			return buildReverseTranslationResult(myHapiTerminologySvc.translateWithReverse(theTranslationRequest));
		}

		return buildTranslationResult(myHapiTerminologySvc.translate(theTranslationRequest));
	}

	private TranslationResult buildTranslationResult(List<TermConceptMapGroupElementTarget> theTargets) {
		TranslationResult retVal = new TranslationResult();

		String msg;
		if (theTargets.isEmpty()) {

			retVal.setResult(new BooleanType(false));

			msg = getContext().getLocalizer().getMessage(
				FhirResourceDaoConceptMapDstu3.class,
				"noMatchesFound");

			retVal.setMessage(new StringType(msg));

		} else {

			retVal.setResult(new BooleanType(true));

			msg = getContext().getLocalizer().getMessage(
				FhirResourceDaoConceptMapDstu3.class,
				"matchesFound");

			retVal.setMessage(new StringType(msg));

			TranslationMatch translationMatch;
			Set<TermConceptMapGroupElementTarget> targetsToReturn = new HashSet<>();
			for (TermConceptMapGroupElementTarget target : theTargets) {
				if (targetsToReturn.add(target)) {
					translationMatch = new TranslationMatch();

					if (target.getEquivalence() != null) {
						translationMatch.setEquivalence(new CodeType(target.getEquivalence().toCode()));
					}

					translationMatch.setConcept(
						new Coding()
							.setCode(target.getCode())
							.setSystem(target.getSystem())
							.setVersion(target.getSystemVersion())
							.setDisplay(target.getDisplay())
					);

					translationMatch.setSource(new UriType(target.getConceptMapUrl()));

					retVal.addMatch(translationMatch);
				}
			}
		}

		return retVal;
	}

	private TranslationResult buildReverseTranslationResult(List<TermConceptMapGroupElement> theElements) {
		TranslationResult retVal = new TranslationResult();

		String msg;
		if (theElements.isEmpty()) {

			retVal.setResult(new BooleanType(false));

			msg = getContext().getLocalizer().getMessage(
				FhirResourceDaoConceptMapDstu3.class,
				"noMatchesFound");

			retVal.setMessage(new StringType(msg));

		} else {

			retVal.setResult(new BooleanType(true));

			msg = getContext().getLocalizer().getMessage(
				FhirResourceDaoConceptMapDstu3.class,
				"matchesFound");

			retVal.setMessage(new StringType(msg));

			TranslationMatch translationMatch;
			Set<TermConceptMapGroupElement> elementsToReturn = new HashSet<>();
			for (TermConceptMapGroupElement element : theElements) {
				if (elementsToReturn.add(element)) {
					translationMatch = new TranslationMatch();

					translationMatch.setConcept(
						new Coding()
							.setCode(element.getCode())
							.setSystem(element.getSystem())
							.setVersion(element.getSystemVersion())
							.setDisplay(element.getDisplay())
					);

					translationMatch.setSource(new UriType(element.getConceptMapUrl()));

					retVal.addMatch(translationMatch);
				}
			}
		}

		return retVal;
	}

	@Override
	public ResourceTable updateEntity(RequestDetails theRequestDetails, IBaseResource theResource, IBasePersistedResource theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
												 boolean theUpdateVersion, Date theUpdateTime, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(theRequestDetails, theResource, theEntity, theDeletedTimestampOrNull, thePerformIndexing, theUpdateVersion, theUpdateTime, theForceUpdate, theCreateNewHistoryEntry);

		if (!retVal.isUnchangedInCurrentOperation()) {
			if (retVal.getDeleted() == null) {
				try {
					ConceptMap conceptMap = (ConceptMap) theResource;
					org.hl7.fhir.r4.model.ConceptMap converted = convertConceptMap(conceptMap);
					myHapiTerminologySvc.storeTermConceptMapAndChildren(retVal, converted);
				} catch (FHIRException fe) {
					throw new InternalErrorException(fe);
				}
			} else {
				myHapiTerminologySvc.deleteConceptMapAndChildren(retVal);
			}
		}

		return retVal;
	}
}
