package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("unused")
public class Copier {
	private static final Logger ourLog = LoggerFactory.getLogger(Copier.class);

	public static void main(String[] args) {
		FhirContext ctx = FhirContext.forDstu3();
		IGenericClient source = ctx.newRestfulGenericClient("http://localhost:8080/baseDstu3");
		IGenericClient target = ctx.newRestfulGenericClient("https://try.smilecdr.com:8000");

		List<String> resType = Arrays.asList(
			"Patient", "Organization", "Encounter", "Procedure",
			"Observation", "ResearchSubject", "Specimen",
			"ResearchStudy", "Location", "Practitioner"
		);

		List<IBaseResource> queued = new ArrayList<>();
		Set<String> sent = new HashSet<>();
		for (String next : resType) {
			copy(ctx, source, target, next, queued, sent);
		}

		while (queued.size() > 0) {
			ourLog.info("Have {} queued resources to deliver", queued.size());

			for (IBaseResource nextQueued : new ArrayList<>(queued)) {

				String missingRef = null;
				for (ResourceReferenceInfo nextRefInfo : ctx.newTerser().getAllResourceReferences(nextQueued)) {
					String nextRef = nextRefInfo.getResourceReference().getReferenceElement().getValue();
					if (isNotBlank(nextRef) && !sent.contains(nextRef)) {
						missingRef = nextRef;
					}
				}
				if (missingRef != null) {
					ourLog.info("Can't send {} because of missing ref {}", nextQueued.getIdElement().getIdPart(), missingRef);
					continue;
				}

				IIdType newId = target
					.update()
					.resource(nextQueued)
					.execute()
					.getId();

				ourLog.info("Copied resource {} and got ID {}", nextQueued.getIdElement().getValue(), newId);
				sent.add(nextQueued.getIdElement().toUnqualifiedVersionless().getValue());
				queued.remove(nextQueued);
			}
		}


	}

	private static void copy(FhirContext theCtx, IGenericClient theSource, IGenericClient theTarget, String theResType, List<IBaseResource> theQueued, Set<String> theSent) {
		Bundle received = theSource
			.search()
			.forResource(theResType)
			.returnBundle(Bundle.class)
			.execute();
		copy(theCtx, theTarget, theResType, theQueued, theSent, received);

		while (received.getLink("next") != null) {
			ourLog.info("Fetching next page...");
			received = theSource.loadPage().next(received).execute();
			copy(theCtx, theTarget, theResType, theQueued, theSent, received);
		}

	}

	private static void copy(FhirContext theCtx, IGenericClient theTarget, String theResType, List<IBaseResource> theQueued, Set<String> theSent, Bundle theReceived) {
		for (Bundle.BundleEntryComponent nextEntry : theReceived.getEntry()) {
			Resource nextResource = nextEntry.getResource();
			nextResource.setId(theResType + "/" + "CR-" + nextResource.getIdElement().getIdPart());

			boolean haveUnsentReference = false;
			for (ResourceReferenceInfo nextRefInfo : theCtx.newTerser().getAllResourceReferences(nextResource)) {
				IIdType nextRef = nextRefInfo.getResourceReference().getReferenceElement();
				if (nextRef.hasIdPart()) {
					String newRef = nextRef.getResourceType() + "/" + "CR-" + nextRef.getIdPart();
					ourLog.info("Changing reference {} to {}", nextRef.getValue(), newRef);
					nextRefInfo.getResourceReference().setReference(newRef);
					if (!theSent.contains(newRef)) {
						haveUnsentReference = true;
					}
				}
			}

			if (haveUnsentReference) {
				ourLog.info("Queueing {} for delivery after", nextResource.getId());
				theQueued.add(nextResource);
				continue;
			}

			IIdType newId = theTarget
				.update()
				.resource(nextResource)
				.execute()
				.getId();

			ourLog.info("Copied resource {} and got ID {}", nextResource.getId(), newId);
			theSent.add(nextResource.getIdElement().toUnqualifiedVersionless().getValue());
		}
	}

}
