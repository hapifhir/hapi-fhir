package ca.uhn.fhir.jpa.dao.dstu3;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.dao.r4.TranslationMatch;
import ca.uhn.fhir.jpa.dao.r4.TranslationRequest;
import ca.uhn.fhir.jpa.dao.r4.TranslationResult;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FhirResourceDaoConceptMapDstu3 extends FhirResourceDaoDstu3<ConceptMap> implements IFhirResourceDaoConceptMap<ConceptMap> {
	@Autowired
	private IHapiTerminologySvc myHapiTerminologySvc;

	@Override
	public TranslationResult translate(TranslationRequest theTranslationRequest, RequestDetails theRequestDetails) {
		if (theTranslationRequest.hasReverse() && theTranslationRequest.getReverseAsBoolean()) {
			return buildReverseTranslationResult(myHapiTerminologySvc.translateWithReverse(theTranslationRequest));
		}

		return buildTranslationResult(myHapiTerminologySvc.translate(theTranslationRequest));
	}

	private TranslationResult buildTranslationResult(List<TermConceptMapGroupElementTarget> theTargets) {
		TranslationResult retVal = new TranslationResult();

		if (theTargets.isEmpty()) {

			retVal.setResult(new BooleanType(false));

			retVal.setMessage(new StringType("No matches found!"));

		} else {

			retVal.setResult(new BooleanType(true));

			retVal.setMessage(new StringType("Matches found!"));

			TranslationMatch translationMatch;
			Set<TermConceptMapGroupElementTarget> targetsToReturn = new HashSet<>();
			for (TermConceptMapGroupElementTarget target : theTargets) {
				if (targetsToReturn.add(target)) {
					translationMatch = new TranslationMatch();

					translationMatch.setEquivalence(new CodeType(target.getEquivalence().toCode()));

					translationMatch.setConcept(
						new Coding()
							.setCode(target.getCode())
							.setSystem(target.getSystem())
							.setVersion(target.getSystemVersion())
							.setDisplay(target.getDisplay())
							.setUserSelected(false)
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

		if (theElements.isEmpty()) {

			retVal.setResult(new BooleanType(false));

			retVal.setMessage(new StringType("No matches found!"));

		} else {

			retVal.setResult(new BooleanType(true));

			retVal.setMessage(new StringType("Matches found!"));

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
							.setUserSelected(false)
					);

					translationMatch.setSource(new UriType(element.getConceptMapUrl()));

					retVal.addMatch(translationMatch);
				}
			}
		}

		return retVal;
	}

	@Override
	protected ResourceTable updateEntity(IBaseResource theResource, ResourceTable theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
													 boolean theUpdateVersion, Date theUpdateTime, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(theResource, theEntity, theDeletedTimestampOrNull, thePerformIndexing, theUpdateVersion, theUpdateTime, theForceUpdate, theCreateNewHistoryEntry);

		ConceptMap conceptMap = (ConceptMap) theResource;

		if (conceptMap != null && isNotBlank(conceptMap.getUrl())) {
			// Convert from DSTU3 to R4
			try {
				myHapiTerminologySvc.storeTermConceptMapAndChildren(retVal, VersionConvertor_30_40.convertConceptMap(conceptMap));
			} catch (FHIRException fe) {
				throw new InternalErrorException(fe);
			}
		}

		return retVal;
	}
}
