package ca.uhn.fhir.empi.provider;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IIdType;

public class EmpiProviderHelper {
	// FIXME KHS consolidate with hapi
	public static EmpiMatchResultEnum extractMatchResultOrNull(String theMatchResult) {
		if (theMatchResult == null) {
			return null;
		}
		return EmpiMatchResultEnum.valueOf(theMatchResult);
	}

	public static EmpiLinkSourceEnum extractLinkSourceOrNull(String theLinkSource) {
		if (theLinkSource == null) {
			return null;
		}
		return EmpiLinkSourceEnum.valueOf(theLinkSource);
	}

	public static IIdType extractPersonIdDtOrNull(String theName, String thePersonId) {
		if (thePersonId == null) {
			return null;
		}
		return getPersonIdDtOrThrowException(theName, thePersonId);
	}

	public static IIdType extractTargetIdDtOrNull(String theName, String theTargetId) {
		if (theTargetId == null) {
			return null;
		}
		return getTargetIdDtOrThrowException(theName, theTargetId);
	}

	private static IdDt getPersonIdDtOrThrowException(String theParamName, String theId) {
		IdDt personId = new IdDt(theId);
		if (!"Person".equals(personId.getResourceType()) ||
			personId.getIdPart() == null) {
			throw new InvalidRequestException(theParamName + " must have form Person/<id> where <id> is the id of the person");
		}
		return personId;
	}

	public static IIdType getTargetIdDtOrThrowException(String theParamName, String theId) {
		IdDt targetId = new IdDt(theId);
		String resourceType = targetId.getResourceType();
		if (!EmpiUtil.supportedTargetType(resourceType) ||
			targetId.getIdPart() == null) {
			throw new InvalidRequestException(theParamName + " must have form Patient/<id> or Practitioner/<id> where <id> is the id of the resource");
		}
		return targetId;
	}
}
