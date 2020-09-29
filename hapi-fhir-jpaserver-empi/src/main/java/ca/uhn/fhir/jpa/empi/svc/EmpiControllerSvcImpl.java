package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiLinkJson;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiControllerSvc;
import ca.uhn.fhir.empi.api.IEmpiLinkQuerySvc;
import ca.uhn.fhir.empi.api.IEmpiLinkUpdaterSvc;
import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.empi.provider.EmpiControllerHelper;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

/**
 * This class acts as a layer between EmpiProviders and EMPI services to support a REST API that's not a FHIR Operation API.
 */
@Service
public class EmpiControllerSvcImpl implements IEmpiControllerSvc {
	@Autowired
	EmpiControllerHelper myEmpiControllerHelper;
	@Autowired
	IEmpiPersonMergerSvc myEmpiPersonMergerSvc;
	@Autowired
	IEmpiLinkQuerySvc myEmpiLinkQuerySvc;
	@Autowired
	IEmpiLinkUpdaterSvc myIEmpiLinkUpdaterSvc;

	@Override
	public IAnyResource mergePersons(String theFromPersonId, String theToPersonId, EmpiTransactionContext theEmpiTransactionContext) {
		IAnyResource fromPerson = myEmpiControllerHelper.getLatestPersonFromIdOrThrowException(ProviderConstants.EMPI_MERGE_PERSONS_FROM_PERSON_ID, theFromPersonId);
		IAnyResource toPerson = myEmpiControllerHelper.getLatestPersonFromIdOrThrowException(ProviderConstants.EMPI_MERGE_PERSONS_TO_PERSON_ID, theToPersonId);
		myEmpiControllerHelper.validateMergeResources(fromPerson, toPerson);
		myEmpiControllerHelper.validateSameVersion(fromPerson, theFromPersonId);
		myEmpiControllerHelper.validateSameVersion(toPerson, theToPersonId);

		return myEmpiPersonMergerSvc.mergePersons(fromPerson, toPerson, theEmpiTransactionContext);
	}

	@Override
	public Stream<EmpiLinkJson> queryLinks(@Nullable String thePersonId, @Nullable String theTargetId, @Nullable String theMatchResult, @Nullable String theLinkSource, EmpiTransactionContext theEmpiContext) {
		IIdType personId = EmpiControllerHelper.extractPersonIdDtOrNull(ProviderConstants.EMPI_QUERY_LINKS_PERSON_ID, thePersonId);
		IIdType targetId = EmpiControllerHelper.extractTargetIdDtOrNull(ProviderConstants.EMPI_QUERY_LINKS_TARGET_ID, theTargetId);
		EmpiMatchResultEnum matchResult = EmpiControllerHelper.extractMatchResultOrNull(theMatchResult);
		EmpiLinkSourceEnum linkSource = EmpiControllerHelper.extractLinkSourceOrNull(theLinkSource);

		return myEmpiLinkQuerySvc.queryLinks(personId, targetId, matchResult, linkSource, theEmpiContext);
	}

	@Override
	public Stream<EmpiLinkJson> getDuplicatePersons(EmpiTransactionContext theEmpiContext) {
		return myEmpiLinkQuerySvc.getDuplicatePersons(theEmpiContext);
	}

	@Override
	public IAnyResource updateLink(String thePersonId, String theTargetId, String theMatchResult, EmpiTransactionContext theEmpiContext) {
		EmpiMatchResultEnum matchResult = EmpiControllerHelper.extractMatchResultOrNull(theMatchResult);
		IAnyResource person = myEmpiControllerHelper.getLatestPersonFromIdOrThrowException(ProviderConstants.EMPI_UPDATE_LINK_PERSON_ID, thePersonId);
		IAnyResource target = myEmpiControllerHelper.getLatestTargetFromIdOrThrowException(ProviderConstants.EMPI_UPDATE_LINK_TARGET_ID, theTargetId);
		myEmpiControllerHelper.validateSameVersion(person, thePersonId);
		myEmpiControllerHelper.validateSameVersion(target, theTargetId);

		return myIEmpiLinkUpdaterSvc.updateLink(person, target, matchResult, theEmpiContext);
	}

	@Override
	public void notDuplicatePerson(String thePersonId, String theTargetPersonId, EmpiTransactionContext theEmpiContext) {
		IAnyResource person = myEmpiControllerHelper.getLatestPersonFromIdOrThrowException(ProviderConstants.EMPI_UPDATE_LINK_PERSON_ID, thePersonId);
		IAnyResource target = myEmpiControllerHelper.getLatestPersonFromIdOrThrowException(ProviderConstants.EMPI_UPDATE_LINK_TARGET_ID, theTargetPersonId);

		myIEmpiLinkUpdaterSvc.notDuplicatePerson(person, target, theEmpiContext);
	}
}
