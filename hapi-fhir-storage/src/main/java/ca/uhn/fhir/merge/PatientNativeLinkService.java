/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
// Created by claude-sonnet-4-5
package ca.uhn.fhir.merge;

import ca.uhn.fhir.i18n.Msg;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
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

	@Override
	public void addReplacesLink(IBaseResource theTarget, IBaseReference theSourceRef) {
		validatePatientResource(theTarget);
		Patient patient = (Patient) theTarget;
		patient.addLink().setType(Patient.LinkType.REPLACES).setOther((Reference) theSourceRef);
	}

	@Override
	public void addReplacedByLink(IBaseResource theSource, IBaseReference theTargetRef) {
		validatePatientResource(theSource);
		Patient patient = (Patient) theSource;
		patient.addLink().setType(Patient.LinkType.REPLACEDBY).setOther((Reference) theTargetRef);
	}

	@Override
	public List<IBaseReference> getReplacesLinks(IBaseResource theResource) {
		validatePatientResource(theResource);
		return getLinksWithType(theResource, Patient.LinkType.REPLACES);
	}

	@Override
	public List<IBaseReference> getReplacedByLinks(IBaseResource theResource) {
		validatePatientResource(theResource);
		return getLinksWithType(theResource, Patient.LinkType.REPLACEDBY);
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

	/**
	 * Validates that the provided resource is a Patient resource.
	 *
	 * @param theResource the resource to validate
	 * @throws IllegalArgumentException if the resource is null or not a Patient
	 */
	private void validatePatientResource(IBaseResource theResource) {
		String receivedType = (theResource == null) ? "null" : theResource.fhirType();
		if (!"Patient".equals(receivedType)) {
			throw new IllegalArgumentException(Msg.code(2838)
					+ "PatientNativeLinkService only supports Patient resources. " + "Received: " + receivedType);
		}
	}
}
