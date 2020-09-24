package ca.uhn.fhir.empi.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.validation.IResourceLoader;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpiControllerHelper {
	private final FhirContext myFhirContext;
	private final IResourceLoader myResourceLoader;

	@Autowired
	public EmpiControllerHelper(FhirContext theFhirContext, IResourceLoader theResourceLoader) {
		myFhirContext = theFhirContext;
		myResourceLoader = theResourceLoader;
	}

	public void validateSameVersion(IAnyResource theResource, String theResourceId) {
		String storedId = theResource.getIdElement().getValue();
		if (hasVersionIdPart(theResourceId) && !storedId.equals(theResourceId)) {
			throw new ResourceVersionConflictException("Requested resource " + theResourceId + " is not the latest version.  Latest version is " + storedId);
		}
	}

	private boolean hasVersionIdPart(String theId) {
		return new IdDt(theId).hasVersionIdPart();
	}

	public IAnyResource getLatestPersonFromIdOrThrowException(String theParamName, String theId) {
		IdDt personId = getPersonIdDtOrThrowException(theParamName, theId);
		return loadResource(personId.toUnqualifiedVersionless());
	}


	public IAnyResource getLatestTargetFromIdOrThrowException(String theParamName, String theId) {
		IIdType targetId = getTargetIdDtOrThrowException(theParamName, theId);
		return loadResource(targetId.toUnqualifiedVersionless());
	}

	protected IAnyResource loadResource(IIdType theResourceId) {
		Class<? extends IBaseResource> resourceClass = myFhirContext.getResourceDefinition(theResourceId.getResourceType()).getImplementingClass();
		return (IAnyResource) myResourceLoader.load(resourceClass, theResourceId);
	}

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
			throw new InvalidRequestException(theParamName + " is '" + theId + "'.  must have form Person/<id> where <id> is the id of the person");
		}
		return personId;
	}

	public static IIdType getTargetIdDtOrThrowException(String theParamName, String theId) {
		IdDt targetId = new IdDt(theId);
		String resourceType = targetId.getResourceType();
		if (!EmpiUtil.supportedTargetType(resourceType) ||
			targetId.getIdPart() == null) {
			throw new InvalidRequestException(theParamName + " is '" + theId + "'.  must have form Patient/<id> or Practitioner/<id> where <id> is the id of the resource");
		}
		return targetId;
	}

	public void validateMergeResources(IAnyResource theFromPerson, IAnyResource theToPerson) {
		validateIsEmpiManaged(ProviderConstants.EMPI_MERGE_PERSONS_FROM_PERSON_ID, theFromPerson);
		validateIsEmpiManaged(ProviderConstants.EMPI_MERGE_PERSONS_TO_PERSON_ID, theToPerson);
	}


	private void validateIsEmpiManaged(String theName, IAnyResource thePerson) {
		if (!"Person".equals(myFhirContext.getResourceType(thePerson))) {
			throw new InvalidRequestException("Only Person resources can be merged.  The " + theName + " points to a " + myFhirContext.getResourceType(thePerson));
		}
		if (!EmpiUtil.isEmpiManaged(thePerson)) {
			throw new InvalidRequestException("Only EMPI managed resources can be merged.  Empi managed resource have the " + EmpiConstants.CODE_HAPI_EMPI_MANAGED + " tag.");
		}
	}
}
