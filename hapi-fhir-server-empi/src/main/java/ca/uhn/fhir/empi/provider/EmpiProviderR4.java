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
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkQuerySvc;
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
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;

import java.util.Collection;
import java.util.UUID;

public class EmpiProviderR4 extends BaseEmpiProvider {
	private final IEmpiMatchFinderSvc myEmpiMatchFinderSvc;
	private final IEmpiPersonMergerSvc myPersonMergerSvc;
	private final IEmpiLinkUpdaterSvc myEmpiLinkUpdaterSvc;
	private final IEmpiLinkQuerySvc myEmpiLinkQuerySvc;

	/**
	 * Constructor
	 *
	 * Note that this is not a spring bean. Any necessary injections should
	 * happen in the constructor
	 */
	public EmpiProviderR4(FhirContext theFhirContext, IEmpiMatchFinderSvc theEmpiMatchFinderSvc, IEmpiPersonMergerSvc thePersonMergerSvc, IEmpiLinkUpdaterSvc theEmpiLinkUpdaterSvc, IEmpiLinkQuerySvc theEmpiLinkQuerySvc, IResourceLoader theResourceLoader) {
		super(theFhirContext, theResourceLoader);
		myEmpiMatchFinderSvc = theEmpiMatchFinderSvc;
		myPersonMergerSvc = thePersonMergerSvc;
		myEmpiLinkUpdaterSvc = theEmpiLinkUpdaterSvc;
		myEmpiLinkQuerySvc = theEmpiLinkQuerySvc;
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
	public Person mergePersons(@OperationParam(name=ProviderConstants.EMPI_MERGE_PERSONS_FROM_PERSON_ID, min = 1, max = 1) StringType theFromPersonId,
										@OperationParam(name=ProviderConstants.EMPI_MERGE_PERSONS_TO_PERSON_ID, min = 1, max = 1) StringType theToPersonId,
										RequestDetails theRequestDetails) {
		validateMergeParameters(theFromPersonId, theToPersonId);
		IAnyResource fromPerson = getLatestPersonFromIdOrThrowException(ProviderConstants.EMPI_MERGE_PERSONS_FROM_PERSON_ID, theFromPersonId.getValue());
		IAnyResource toPerson = getLatestPersonFromIdOrThrowException(ProviderConstants.EMPI_MERGE_PERSONS_TO_PERSON_ID, theToPersonId.getValue());
		validateMergeResources(fromPerson, toPerson);
		validateSameVersion(fromPerson, theFromPersonId);
		validateSameVersion(toPerson, theToPersonId);

		return (Person) myPersonMergerSvc.mergePersons(fromPerson, toPerson, createEmpiContext(theRequestDetails));
	}

	@Operation(name = ProviderConstants.EMPI_UPDATE_LINK, type = Person.class)
	public Person updateLink(@OperationParam(name=ProviderConstants.EMPI_UPDATE_LINK_PERSON_ID, min = 1, max = 1) StringType thePersonId,
								  @OperationParam(name=ProviderConstants.EMPI_UPDATE_LINK_TARGET_ID, min = 1, max = 1) StringType theTargetId,
								  @OperationParam(name=ProviderConstants.EMPI_UPDATE_LINK_MATCH_RESULT, min = 1, max = 1) StringType theMatchResult,
								  ServletRequestDetails theRequestDetails) {

		validateUpdateLinkParameters(thePersonId, theTargetId, theMatchResult);
		EmpiMatchResultEnum matchResult = extractMatchResultOrNull(theMatchResult);
		IAnyResource person = getLatestPersonFromIdOrThrowException(ProviderConstants.EMPI_UPDATE_LINK_PERSON_ID, thePersonId.getValue());
		IAnyResource target = getLatestTargetFromIdOrThrowException(ProviderConstants.EMPI_UPDATE_LINK_TARGET_ID, theTargetId.getValue());
		validateSameVersion(person, thePersonId);
		validateSameVersion(target, theTargetId);

		return (Person) myEmpiLinkUpdaterSvc.updateLink(person, target, matchResult, createEmpiContext(theRequestDetails));
	}

	@Operation(name = ProviderConstants.EMPI_QUERY_LINKS, idempotent = true)
	public Parameters queryLinks(@OperationParam(name=ProviderConstants.EMPI_QUERY_LINKS_PERSON_ID, min = 0, max = 1) StringType thePersonId,
									 @OperationParam(name=ProviderConstants.EMPI_QUERY_LINKS_TARGET_ID, min = 0, max = 1) StringType theTargetId,
									 @OperationParam(name=ProviderConstants.EMPI_QUERY_LINKS_MATCH_RESULT, min = 0, max = 1) StringType theMatchResult,
									 @OperationParam(name=ProviderConstants.EMPI_QUERY_LINKS_LINK_SOURCE, min = 0, max = 1) StringType theLinkSource,
									 ServletRequestDetails theRequestDetails) {
		IIdType personId = extractPersonIdDtOrNull(ProviderConstants.EMPI_QUERY_LINKS_PERSON_ID, thePersonId);
		IIdType targetId = extractTargetIdDtOrNull(ProviderConstants.EMPI_QUERY_LINKS_TARGET_ID, theTargetId);
		EmpiMatchResultEnum matchResult = extractMatchResultOrNull(theMatchResult);
		EmpiLinkSourceEnum linkSource = extractLinkSourceOrNull(theLinkSource);

		return (Parameters) myEmpiLinkQuerySvc.queryLinks(personId, targetId, matchResult, linkSource, createEmpiContext(theRequestDetails));
	}

	@Operation(name = ProviderConstants.EMPI_DUPLICATE_PERSONS, idempotent = true)
	public Parameters getDuplicatePersons(ServletRequestDetails theRequestDetails) {
		return (Parameters) myEmpiLinkQuerySvc.getPossibleDuplicates(createEmpiContext(theRequestDetails));
	}

	@Operation(name = ProviderConstants.EMPI_NOT_DUPLICATE)
	public Parameters notDuplicate(@OperationParam(name=ProviderConstants.EMPI_QUERY_LINKS_PERSON_ID, min = 1, max = 1) StringType thePersonId,
											 @OperationParam(name=ProviderConstants.EMPI_QUERY_LINKS_TARGET_ID, min = 1, max = 1) StringType theTargetId,
											 ServletRequestDetails theRequestDetails) {

		validateNotDuplicateParameters(thePersonId, theTargetId);
		IAnyResource person = getLatestPersonFromIdOrThrowException(ProviderConstants.EMPI_UPDATE_LINK_PERSON_ID, thePersonId.getValue());
		IAnyResource target = getLatestPersonFromIdOrThrowException(ProviderConstants.EMPI_UPDATE_LINK_TARGET_ID, theTargetId.getValue());
		validateSameVersion(person, thePersonId);
		validateSameVersion(target, theTargetId);

		return (Parameters) myEmpiLinkUpdaterSvc.notDuplicatePerson(person, target, createEmpiContext(theRequestDetails));
	}
}
