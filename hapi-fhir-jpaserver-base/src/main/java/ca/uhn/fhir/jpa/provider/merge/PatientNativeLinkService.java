// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.provider.merge;

import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing Patient merge links using the native Patient.link field.
 *
 * This implementation uses the FHIR R4 Patient.link field to track "replaces" and "replaced-by"
 * relationships between Patient resources during merge operations. Unlike the extension-based
 * approach, this uses the standard Patient.link.type field with LinkType enumeration.
 *
 * Link Types:
 * - Replaces: Patient.LinkType.REPLACES
 * - Replaced-by: Patient.LinkType.REPLACEDBY
 */
public class PatientNativeLinkService implements IResourceLinkService {

	private static final Logger ourLog = LoggerFactory.getLogger(PatientNativeLinkService.class);

	public PatientNativeLinkService() {}

	@Override
	public void addReplacesLink(IBaseResource theTarget, IBaseReference theSourceRef) {
		Patient patient = (Patient) theTarget;
		patient.addLink().setType(Patient.LinkType.REPLACES).setOther((Reference) theSourceRef);
	}

	@Override
	public void addReplacedByLink(IBaseResource theSource, IBaseReference theTargetRef) {
		Patient patient = (Patient) theSource;
		patient.addLink().setType(Patient.LinkType.REPLACEDBY).setOther((Reference) theTargetRef);
	}

	@Override
	public List<IBaseReference> getReplacesLinks(IBaseResource theResource) {
		return getLinksWithType(theResource, Patient.LinkType.REPLACES);
	}

	@Override
	public List<IBaseReference> getReplacedByLinks(IBaseResource theResource) {
		return getLinksWithType(theResource, Patient.LinkType.REPLACEDBY);
	}

	@Override
	public boolean hasReplacedByLink(IBaseResource theResource) {
		List<IBaseReference> links = getReplacedByLinks(theResource);
		return !links.isEmpty();
	}

	@Override
	public boolean hasReplacesLinkTo(IBaseResource theResource, IIdType theTargetId) {
		List<IBaseReference> replacesLinks = getReplacesLinks(theResource);

		String targetIdValue = theTargetId.toUnqualifiedVersionless().getValue();

		for (IBaseReference link : replacesLinks) {
			IIdType linkRefElement = link.getReferenceElement();
			if (linkRefElement == null) {
				continue;
			}

			String linkIdValue = linkRefElement.toUnqualifiedVersionless().getValue();

			if (targetIdValue.equals(linkIdValue)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Helper method to extract Reference values from Patient.link with a specific type.
	 *
	 * @param theResource the Patient resource to check
	 * @param theLinkType the link type to filter by
	 * @return list of Reference objects from matching links
	 */
	private List<IBaseReference> getLinksWithType(IBaseResource theResource, Patient.LinkType theLinkType) {
		List<IBaseReference> references = new ArrayList<>();
		Patient patient = (Patient) theResource;

		for (Patient.PatientLinkComponent link : patient.getLink()) {
			if (link.getType() == theLinkType) {
				if (link.hasOther()) {
					references.add(link.getOther());
				} else {
					ourLog.warn("Patient.link with type {} has no 'other' reference", theLinkType.toCode());
				}
			}
		}

		return references;
	}
}
