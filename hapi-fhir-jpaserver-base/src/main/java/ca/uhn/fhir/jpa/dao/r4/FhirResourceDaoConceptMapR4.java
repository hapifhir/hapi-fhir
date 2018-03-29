package ca.uhn.fhir.jpa.dao.r4;

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
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

public class FhirResourceDaoConceptMapR4 extends FhirResourceDaoR4<ConceptMap> implements IFhirResourceDaoConceptMap<ConceptMap> {
	@Autowired
	private IHapiTerminologySvc myHapiTerminologySvc;

	@Override
	public TranslationResult translate(IPrimitiveType<String> theSourceCodeSystem, IPrimitiveType<String> theTargetCodeSystem, IPrimitiveType<String> theSourceCode, RequestDetails theRequestDetails) {
		TranslationResult retVal = new TranslationResult();

		String sourceCodeSystem = theSourceCodeSystem.getValueAsString();
		String targetCodeSystem = theTargetCodeSystem.getValueAsString();
		String sourceCode = theSourceCode.getValueAsString();
		List<TermConceptMapGroupElementTarget> targets = new ArrayList<>();
		if (isNoneBlank(sourceCodeSystem, targetCodeSystem, sourceCode)) {
			targets = myHapiTerminologySvc.translate(sourceCodeSystem, targetCodeSystem, sourceCode);
		}

		if (targets.isEmpty()) {

			retVal.setResult(new BooleanType(false));

			retVal.setMessage(new StringType("No matches found!"));

		} else {

			retVal.setResult(new BooleanType(true));

			retVal.setMessage(new StringType("Matches found!"));

			TranslationMatch translationMatch;
			for (TermConceptMapGroupElementTarget target : targets) {
				translationMatch = new TranslationMatch();

				translationMatch.setEquivalence(new CodeType(target.getEquivalence().toCode()));

				translationMatch.setConcept(
					new Coding()
						.setCode(target.getCode())
						.setSystem(target.getSystem())
						.setDisplay(target.getDisplay())
						.setUserSelected(false)
				);

				translationMatch.setSource(new UriType(target.getConceptMapUrl()));

				retVal.addMatch(translationMatch);
			}
		}

		return retVal;
	}

	@Override
	protected ResourceTable updateEntity(IBaseResource theResource, ResourceTable theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
													 boolean theUpdateVersion, Date theUpdateTime, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(theResource, theEntity, theDeletedTimestampOrNull, thePerformIndexing, theUpdateVersion, theUpdateTime, theForceUpdate, theCreateNewHistoryEntry);

		myHapiTerminologySvc.storeNewConceptMap((ConceptMap) theResource);

		return retVal;
	}
}
