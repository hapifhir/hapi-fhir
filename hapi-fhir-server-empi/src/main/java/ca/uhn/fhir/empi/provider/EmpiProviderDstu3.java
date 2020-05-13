package ca.uhn.fhir.empi.provider;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkUpdaterSvc;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.validation.IResourceLoader;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Person;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.Collection;
import java.util.UUID;

public class EmpiProviderDstu3 extends BaseEmpiProvider {
	private final IEmpiMatchFinderSvc myEmpiMatchFinderSvc;
	private final IEmpiPersonMergerSvc myPersonMergerSvc;
	private final IEmpiLinkUpdaterSvc myEmpiLinkUpdaterSvc;

	/**
	 * Constructor
	 *
	 * Note that this is not a spring bean. Any necessary injections should
	 * happen in the constructor
	 */
	public EmpiProviderDstu3(FhirContext theFhirContext, IEmpiMatchFinderSvc theEmpiMatchFinderSvc, IEmpiPersonMergerSvc thePersonMergerSvc, IEmpiLinkUpdaterSvc theEmpiLinkUpdaterSvc, IResourceLoader theResourceLoader) {
		super(theFhirContext, theResourceLoader);
		myEmpiMatchFinderSvc = theEmpiMatchFinderSvc;
		myPersonMergerSvc = thePersonMergerSvc;
		myEmpiLinkUpdaterSvc = theEmpiLinkUpdaterSvc;
	}

	@Operation(name = ProviderConstants.EMPI_MATCH, type = Patient.class)
	public Bundle match(@OperationParam(name=ProviderConstants.EMPI_MATCH_RESOURCE, min = 1, max = 1) Patient thePatient) {
		if (thePatient == null) {
			throw new InvalidRequestException("resource may not be null");
		}
		Collection<IAnyResource> matches = myEmpiMatchFinderSvc.findMatches("Patient", thePatient);

		Bundle retVal = new Bundle();
		retVal.setType(Bundle.BundleType.SEARCHSET);
		retVal.setId(UUID.randomUUID().toString());
		retVal.getMeta().setLastUpdatedElement(InstantType.now());

		for (IAnyResource next : matches) {
			retVal.addEntry().setResource((Resource) next);
		}

		return retVal;
	}

	@Operation(name = ProviderConstants.EMPI_MERGE_PERSONS, type = Person.class)
	public Person mergePerson(@OperationParam(name=ProviderConstants.EMPI_MERGE_PERSONS_PERSON_ID_TO_DELETE, min = 1, max = 1) StringType thePersonIdToDelete,
									  @OperationParam(name=ProviderConstants.EMPI_MERGE_PERSONS_PERSON_ID_TO_KEEP, min = 1, max = 1) StringType thePersonIdToKeep,
									  RequestDetails theRequestDetails) {
		validateMergeParameters(thePersonIdToDelete, thePersonIdToKeep);
		IAnyResource personToDelete = getPersonFromIdOrThrowException(thePersonIdToDelete.getValue(), ProviderConstants.EMPI_MERGE_PERSONS_PERSON_ID_TO_DELETE);
		IAnyResource personToKeep = getPersonFromIdOrThrowException(thePersonIdToKeep.getValue(), ProviderConstants.EMPI_MERGE_PERSONS_PERSON_ID_TO_KEEP);

		return (Person) myPersonMergerSvc.mergePersons(personToDelete, personToKeep, createEmpiContext(theRequestDetails));
	}

	@Operation(name = ProviderConstants.EMPI_UPDATE_LINK, type = Person.class)
	public Person updateLink(@OperationParam(name=ProviderConstants.EMPI_UPDATE_LINK_PERSON_ID, min = 1, max = 1) StringType thePersonId,
																  @OperationParam(name=ProviderConstants.EMPI_UPDATE_LINK_TARGET_ID, min = 1, max = 1) StringType theTargetId,
																  @OperationParam(name=ProviderConstants.EMPI_UPDATE_LINK_MATCH_RESULT, min = 1, max = 1) StringType theMatchResult,
																  ServletRequestDetails theRequestDetails) {

		validateUpdateLinkParameters(thePersonId, theTargetId, theMatchResult);
		EmpiMatchResultEnum matchResult = EmpiMatchResultEnum.valueOf(theMatchResult.getValue());
		IAnyResource person = getPersonFromIdOrThrowException(thePersonId.getValue(), "personIdToDelete");
		IAnyResource target = getTargetFromIdOrThrowException(theTargetId.getValue(), "personIdToKeep");

		return (Person) myEmpiLinkUpdaterSvc.updateLink(person, target, matchResult, createEmpiContext(theRequestDetails));
	}

}
