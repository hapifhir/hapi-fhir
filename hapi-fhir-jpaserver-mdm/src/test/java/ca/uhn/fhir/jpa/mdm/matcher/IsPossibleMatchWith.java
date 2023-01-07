package ca.uhn.fhir.jpa.mdm.matcher;

import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Matcher with tells us if there is an MdmLink with between these two resources that are considered POSSIBLE_MATCH
 */
public class IsPossibleMatchWith extends BaseGoldenResourceMatcher {

	protected IsPossibleMatchWith(IIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc, IAnyResource... theBaseResource) {
		super(theIdHelperService, theMdmLinkDaoSvc, theBaseResource);
	}

	@Override
	protected boolean matchesSafely(IAnyResource theIncomingResource) {
		List<? extends IMdmLink> mdmLinks = getMdmLinksForTarget(theIncomingResource, MdmMatchResultEnum.POSSIBLE_MATCH);

		List<IResourcePersistentId> goldenResourcePidsToMatch = myBaseResources.stream()
			.map(this::getMatchedResourcePidFromResource)
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		if (goldenResourcePidsToMatch.isEmpty()) {
			goldenResourcePidsToMatch = myBaseResources.stream()
				.flatMap(iBaseResource -> getPossibleMatchedGoldenResourcePidsFromTarget(iBaseResource).stream())
				.collect(Collectors.toList());
		}

		List<IResourcePersistentId> mdmLinkGoldenResourcePids = mdmLinks
			.stream().map(IMdmLink::getGoldenResourcePersistenceId)
			.collect(Collectors.toList());

		return mdmLinkGoldenResourcePids.containsAll(goldenResourcePidsToMatch);
	}

	@Override
	public void describeTo(Description theDescription) {
		theDescription.appendText(" no link found with POSSIBLE_MATCH to the requested PIDS");
	}

	@Override
	protected void describeMismatchSafely(IAnyResource item, Description mismatchDescription) {
		super.describeMismatchSafely(item, mismatchDescription);
		mismatchDescription.appendText("No MDM Link With POSSIBLE_MATCH was found");
	}

	public static Matcher<IAnyResource> possibleMatchWith(IIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc, IAnyResource... theBaseResource) {
		return new IsPossibleMatchWith(theIdHelperService, theMdmLinkDaoSvc, theBaseResource);
	}
}
