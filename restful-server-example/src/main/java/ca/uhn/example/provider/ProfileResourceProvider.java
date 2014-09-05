
package ca.uhn.example.provider;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.VersionIdParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * created by J.Ting of Systems Made Simple, Inc.
 * created on Tuesday, August 12, 2014            
 */
public class ProfileResourceProvider implements IResourceProvider {

	/**
	 * This map has a resource ID as a key, and each key maps to a Deque list containing all versions of the resource with that ID.
	 */
	private Map<Long, Deque<Profile>> myIdToProfileVersions = new HashMap<Long, Deque<Profile>>();

	/**
	 * This is used to generate new IDs
	 */
	private long myNextId = 1;

	/**
	 * Constructor, which pre-populates the provider with one resource instance.
	 */
	public ProfileResourceProvider() {
		Profile profile = new Profile();
		profile.setName("profile 1");
		profile.setIdentifier("Profile1");

		LinkedList<Profile> list = new LinkedList<Profile>();
		list.add(profile);
		myIdToProfileVersions.put(myNextId++, list);
		
	}

	/**
	 * Stores a new version of the Profile in memory so that it
	 * can be retrieved later.
	 * 
	 * @param theProfile The Profile resource to store
	 * @param theId The ID of the Profile to retrieve
	 */
	private void addNewVersion(Profile theProfile, Long theId) {
		InstantDt publishedDate;
		if (!myIdToProfileVersions.containsKey(theId)) {
			myIdToProfileVersions.put(theId, new LinkedList<Profile>());
			publishedDate = InstantDt.withCurrentTime();
		} else {
			Profile currentPatitne = myIdToProfileVersions.get(theId).getLast();
			Map<ResourceMetadataKeyEnum<?>, Object> resourceMetadata = currentPatitne.getResourceMetadata();
			publishedDate = (InstantDt) resourceMetadata.get(ResourceMetadataKeyEnum.PUBLISHED);
		}

		/*
		 * PUBLISHED time will always be set to the time that the first
		 * version was stored. UPDATED time is set to the time that the new
		 * version was stored. 
		 */
		theProfile.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, publishedDate);
		theProfile.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, InstantDt.withCurrentTime());

		Deque<Profile> existingVersions = myIdToProfileVersions.get(theId);
		
		/*
		 * We just use the current number of versions as the next version number
		 */
		IdDt version = new IdDt(existingVersions.size());
		theProfile.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, version);
		
		existingVersions.add(theProfile);
	}

	/**
	 * The "@Search" annotation indicates that this method supports the search operation. You may have many different method annotated with this annotation, to support many different search criteria.
	 * This example searches by family name.
	 * 
	 * @param theIdentifier
	 *            This operation takes one parameter which is the search criteria. It is annotated with the "@Required" annotation. This annotation takes one argument, a string containing the name of
	 *            the search criteria. The datatype here is StringDt, but there are other possible parameter types depending on the specific search criteria.
	 * @return This method returns a list of Profiles. This list may contain multiple matching resources, or it may also be empty.
	 */
	@Create()
	public MethodOutcome createProfile(@ResourceParam Profile theProfile) {
		validateResource(theProfile);

		// Here we are just generating IDs sequentially
		long id = myNextId++;

		addNewVersion(theProfile, id);

		// Let the caller know the ID of the newly created resource
		return new MethodOutcome(new IdDt(id));
	}

	/**
	 * The "@Search" annotation indicates that this method supports the search operation. You may have many different method annotated with this annotation, to support many different search criteria.
	 * This example searches by family name.
	 * 
	 * @param theFamilyName
	 *            This operation takes one parameter which is the search criteria. It is annotated with the "@Required" annotation. This annotation takes one argument, a string containing the name of
	 *            the search criteria. The datatype here is StringDt, but there are other possible parameter types depending on the specific search criteria.
	 * @return This method returns a list of Profiles. This list may contain multiple matching resources, or it may also be empty.
	 */
/*	@Search()
	public List<Profile> findProfilesByName(@RequiredParam(name = Profile.SP_FAMILY) StringDt theFamilyName) {
		LinkedList<Profile> retVal = new LinkedList<Profile>();

		
		 * Look for all Profiles matching the name
		 
		for (Deque<Profile> nextProfileList : myIdToProfileVersions.values()) {
			Profile nextProfile = nextProfileList.getLast();
			NAMELOOP: for (HumanNameDt nextName : nextProfile.getName()) {
				for (StringDt nextFamily : nextName.getFamily()) {
					if (theFamilyName.equals(nextFamily)) {
						retVal.add(nextProfile);
						break NAMELOOP;
					}
				}
			}
		}

		return retVal;
	}
*/	
	/**
	 * The getResourceType method comes from IResourceProvider, and must be overridden to indicate what type of resource this provider supplies.
	 */
	@Override
	public Class<Profile> getResourceType() {
		return Profile.class;
	}

	/**
	 * This is the "read" operation.
	 * The "@Read" annotation indicates that this method supports the read and/or vread operation.
	 * <p> 
	 * Read operations take a single parameter annotated with the {@link IdParam} paramater, and 
	 * should return a single resource instance.
	 * </p>
	 * 
	 * @param theId
	 *            The read operation takes one parameter, which must be of type IdDt and must be annotated with the "@Read.IdParam" annotation.
	 * @return Returns a resource matching this identifier, or null if none exists.
	 */
	@Read()
	public Profile readProfile(@IdParam IdDt theId) {
		Deque<Profile> retVal;
		try {
			retVal = myIdToProfileVersions.get(theId.getIdPartAsLong());
		} catch (NumberFormatException e) {
			/*
			 * If we can't parse the ID as a long, it's not valid so this is an unknown resource
			 */
			throw new ResourceNotFoundException(theId);
		}

		return retVal.getLast();
	}

	/**
	 * The "@Update" annotation indicates that this method supports replacing an existing resource (by ID) with a new instance of that resource.
	 * 
	 * @param theId
	 *            This is the ID of the Profile to update
	 * @param theProfile
	 *            This is the actual resource to save
	 * @return This method returns a "MethodOutcome"
	 */
	@Create()
	public MethodOutcome updateProfile(@IdParam IdDt theId, @ResourceParam Profile theProfile) {
		validateResource(theProfile);

		Long id;
		try {
			id = theId.getIdPartAsLong();
		} catch (DataFormatException e) {
			throw new InvalidRequestException("Invalid ID " + theId.getValue() + " - Must be numeric");
		}

		/*
		 * Throw an exception (HTTP 404) if the ID is not known
		 */
		if (!myIdToProfileVersions.containsKey(id)) {
			throw new ResourceNotFoundException(theId);
		}

		addNewVersion(theProfile, id);

		return new MethodOutcome();
	}

	/**
	 * This method just provides simple business validation for resources we are storing.
	 * 
	 * @param theProfile
	 *            The Profile to validate
	 */
	private void validateResource(Profile theProfile) {
		/*
		 * Our server will have a rule that Profiles must have a family name or we will reject them
		 */
/*		if (theProfile.getNameFirstRep().getFamilyFirstRep().isEmpty()) {
			OperationOutcome outcome = new OperationOutcome();
			outcome.addIssue().setSeverity(IssueSeverityEnum.FATAL).setDetails("No family name provided, Profile resources must have at least one family name.");
			throw new UnprocessableEntityException(outcome);
		}
*/	}

	/**
	 * This is the "vread" operation.
	 * The "@Read" annotation indicates that this method supports the read and/or vread operation.
	 * <p> 
	 * VRead operations take a parameter annotated with the {@link IdParam} paramater, 
	 * and a paramater annotated with the {@link VersionIdParam} parmeter,
	 * and should return a single resource instance.
	 * </p>
	 * 
	 * @param theId
	 *            The read operation takes one parameter, which must be of type IdDt and must be annotated with the "@Read.IdParam" annotation.
	 * @return Returns a resource matching this identifier, or null if none exists.
	 */
	@Read()
	public Profile vreadProfile(@IdParam IdDt theId, @VersionIdParam IdDt theVersionId) {
		Deque<Profile> versions;
		try {
			versions = myIdToProfileVersions.get(theId.getIdPartAsLong());
		} catch (NumberFormatException e) {
			/*
			 * If we can't parse the ID as a long, it's not valid so this is an unknown resource
			 */
			throw new ResourceNotFoundException(theId);
		}

		for (Profile nextVersion : versions) {
			IdDt nextVersionId = (IdDt) nextVersion.getResourceMetadata().get(ResourceMetadataKeyEnum.VERSION_ID);
			if (theVersionId.equals(nextVersionId)) {
				return nextVersion;
			}
		}
		
		throw new ResourceNotFoundException("Unknown version " + theVersionId);
	}

}

