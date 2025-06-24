package ca.uhn.fhir.replacereferences;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This service is implements the $hapi.fhir.replace-references operation.
 * It reverts the changes made by $hapi.fhir.replace-references operation based on the Provenance resource
 * that was created as part of the $hapi.fhir.replace-references operation.
 */
public class UndoReplaceReferencesSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(UndoReplaceReferencesSvc.class);

	private final DaoRegistry myDaoRegistry;
	private final HapiTransactionService myHapiTransactionService;
	private final IFhirResourceDao<Provenance> myProvenanceDao;

	public UndoReplaceReferencesSvc(DaoRegistry theDaoRegistry, HapiTransactionService theHapiTransactionService) {
		myDaoRegistry = theDaoRegistry;
		myHapiTransactionService = theHapiTransactionService;
		myProvenanceDao = myDaoRegistry.getResourceDao(Provenance.class);
	}

	public IBaseParameters undoReplaceReferences(
			UndoReplaceReferencesRequest theUndoReplaceReferencesRequest, RequestDetails theRequestDetails) {
		Provenance provenance = findProvenance(theUndoReplaceReferencesRequest, theRequestDetails);
		if (provenance == null
				|| !isTargetAndSourceOrderCorrect(
						provenance,
						theUndoReplaceReferencesRequest.targetId,
						theUndoReplaceReferencesRequest.sourceId)) {
			String msg =
					"Unable to find a Provenance created by a $hapi.fhir.replace-references for the provided source and target IDs."
							+ "Ensure that IDs are correct and were previously used as parameters in a successful $hapi.fhir.replace-references operation";
			// FIXME Emre: change the code
			throw new ResourceNotFoundException(Msg.code(123) + msg);
		}

		ourLog.info(
				"Using Provenance resource with id: {} for $undo-replace-references operation",
				provenance.getIdElement().getValue());

		List<Reference> references = provenance.getTarget();
		// in replace-references operation provenance, the first two references are to the target and the source,
		// and they are usually not updated as part of the operation so skip restoring them
		List<Reference> toRestore = references.subList(2, references.size());

		if (toRestore.size() > theUndoReplaceReferencesRequest.resourceLimit) {
			String msg = String.format(
					"Number of references to update (%d) exceeds the limit (%d)",
					toRestore.size(), theUndoReplaceReferencesRequest.resourceLimit);
			// FIXME Emre: change the code
			throw new InvalidRequestException(Msg.code(111) + msg);
		}

		restoreToPreviousVersionsInTrx(toRestore, theRequestDetails, theUndoReplaceReferencesRequest.partitionId);
		Parameters retval = new Parameters();
		return retval;
	}

	// TODO EMRE: move this to ReplaceReferencesProvenanceSvc
	@Nullable
	private Provenance findProvenance(
			UndoReplaceReferencesRequest theUndoReplaceReferencesRequest, RequestDetails theRequestDetails) {
		SearchParameterMap map = new SearchParameterMap();
		map.add("target", new ReferenceParam(theUndoReplaceReferencesRequest.targetId));
		// Add sort by recorded field, in case there are multiple Provenance resources for the same source and target,
		// we want the most recent one.
		map.setSort(new SortSpec("recorded", SortOrderEnum.DESC));

		IBundleProvider searchBundle = myProvenanceDao.search(map, theRequestDetails);
		// 'activity' is not available as a search parameter in r4, was added in r5,
		// so we need to filter the results manually.
		List<Provenance> provenances = filterByActivity(searchBundle.getAllResources());

		if (provenances.isEmpty()) {
			return null;
		}

		if (provenances.size() > 1) {
			// If there are multiple Provenance resources, we return the most recent one, but log a warning
			ourLog.warn(
					"There are multiple Provenance resources with the given source {} and target {} suitable for $hapi.fhir.undo-replace-references operation, "
							+ "using the most recent one. Provenance count: {}",
					theUndoReplaceReferencesRequest.sourceId,
					theUndoReplaceReferencesRequest.targetId,
					provenances.size());
		}

		return provenances.get(0);
	}

	private List<Provenance> filterByActivity(List<IBaseResource> theResources) {
		List<Provenance> filteredProvenances = new ArrayList<>();
		for (IBaseResource resource : theResources) {
			Provenance provenance = (Provenance) resource;
			if (provenance.hasActivity() && provenance.getActivity().hasCoding()) {
				for (Coding coding : provenance.getActivity().getCoding()) {
					if ("http://terminology.hl7.org/CodeSystem/iso-21089-lifecycle".equals(coding.getSystem())
							&& "link".equals(coding.getCode())) {
						filteredProvenances.add(provenance);
						// No need to check other codings for this Provenance
						// continue with the next Provenance
						break;
					}
				}
			}
		}
		return filteredProvenances;
	}

	/**
	 * Given a list of resource references, this method restores each resource to its previous version if the resource's current version is the same one
	 * as specified in the given reference (i.e. the resource was not updated since the reference was created).
	 * @param theReferences
	 * @param theRequestDetails
	 * @param thePartitionId
	 */
	public void restoreToPreviousVersionsInTrx(
			List<Reference> theReferences, RequestDetails theRequestDetails, RequestPartitionId thePartitionId) {
		myHapiTransactionService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(thePartitionId)
				.execute(() -> restoreToPreviousVersions(theReferences, theRequestDetails));
	}

	private void restoreToPreviousVersions(List<Reference> theTargets, RequestDetails theRequestDetails) {
		for (Reference target : theTargets) {
			String targetRef = target.getReference();
			IIdType targetId = new IdDt(target.getReference());

			// Check if version is specified in the Provenance target reference
			if (!targetId.hasVersionIdPart()) {
				// FIXME Emre: change the code
				throw new InternalErrorException(
						Msg.code(123) + "Target reference does not have a version in Target ref: " + targetRef);
			}
			Long provananceVersion = targetId.getVersionIdPartAsLong();

			// Get the current resource
			IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(targetId.getResourceType());
			IBaseResource currentResource = dao.read(targetId.toUnqualifiedVersionless(), theRequestDetails);

			// Check current version
			Long currentVersion = currentResource.getIdElement().getVersionIdPartAsLong();
			if (!currentVersion.equals(provananceVersion)) {
				String msg = String.format(
						"Current version of resource %s (%s) does not match Provenance version (%s)",
						targetRef, currentVersion, provananceVersion);
				// FIXME Emre: change the code
				throw new ResourceVersionConflictException(Msg.code(111) + msg);
			}

			// Restore previous version (version - 1)
			long previousVersion = provananceVersion - 1;
			if (previousVersion < 1) {
				throw new InternalErrorException(
						Msg.code(112) + "No previous version exists for resource " + targetRef);
			}
			IIdType previousId = targetId.withVersion(Long.toString(previousVersion));
			IBaseResource previousResource = dao.read(previousId, theRequestDetails);
			previousResource.setId(previousResource.getIdElement().toUnqualifiedVersionless());

			// Update the resource to the previous version's content
			dao.update(previousResource, theRequestDetails);
		}
	}

	/**
	 * Checks if the first 'Provenance.target' reference matches theTargetId and the second matches theSourceId.
	 * The replace-references operation creates a Provenance resource with in that order.
	 * @param provenance The Provenance resource to check.
	 * @param theTargetId The expected target IIdType for the first reference.
	 * @param theSourceId The expected source IIdType for the second reference.
	 * @return true if both match, false otherwise.
	 */
	public boolean isTargetAndSourceOrderCorrect(Provenance provenance, IIdType theTargetId, IIdType theSourceId) {
		if (provenance.getTarget().size() < 2) {
			ourLog.error(
					"Provenance resource {} does not have enough targets. Expected at least 2, found {}.",
					provenance.getIdElement().getValue(),
					provenance.getTarget().size());
			return false;
		}
		Reference firstTarget = provenance.getTarget().get(0);
		Reference secondTarget = provenance.getTarget().get(1);

		boolean firstMatches = theTargetId
				.toUnqualifiedVersionless()
				.getValue()
				.equals(new IdDt(firstTarget.getReference())
						.toUnqualifiedVersionless()
						.getValue());
		boolean secondMatches = theSourceId
				.toUnqualifiedVersionless()
				.getValue()
				.equals(new IdDt(secondTarget.getReference())
						.toUnqualifiedVersionless()
						.getValue());

		return firstMatches && secondMatches;
	}
}
