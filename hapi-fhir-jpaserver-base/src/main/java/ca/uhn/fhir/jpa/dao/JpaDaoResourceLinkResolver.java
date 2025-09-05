/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.index.DaoResourceLinkResolver;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.ResourceSearchUrlSvc;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JpaDaoResourceLinkResolver extends DaoResourceLinkResolver<JpaPid> {

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private ResourceSearchUrlSvc myResourceSearchUrlSvc;

	@Override
	protected void verifyPlaceholderCanBeCreated(
			Class<? extends IBaseResource> theType,
			String theIdToAssignToPlaceholder,
			IBaseReference theReference,
			IBasePersistedResource theStoredEntity) {
		if (isNotBlank(theIdToAssignToPlaceholder)) {
			return;
		}

		/*
		 * If we're about to create a placeholder resource to satisfy a conditional URL
		 * with identifiers, add an entry in the HFJ_RES_SEARCH_URL table, which is used
		 * to prevent multiple concurrent threads creating the same object as a part of
		 * a conditional create/update.
		 */
		String reference = theReference.getReferenceElement().getValue();
		if (reference.contains("?")) {
			String resourceType = myFhirContext.getResourceType(theType);

			List<CanonicalIdentifier> referenceMatchUrlIdentifiers = extractIdentifierFromUrl(reference);
			String matchUrl = referenceMatchUrlIdentifiers.stream()
					.map(JpaDaoResourceLinkResolver::toUrlParam)
					.collect(Collectors.joining("&"));

			myResourceSearchUrlSvc.enforceMatchUrlResourceUniqueness(
					resourceType, matchUrl, (ResourceTable) theStoredEntity);
		}
	}

	@Nonnull
	private static String toUrlParam(CanonicalIdentifier t) {
		return "identifier=" + UrlUtil.escapeUrlParam(t.getSystemElement().getValue())
				+ "|"
				+ UrlUtil.escapeUrlParam(t.getValueElement().getValue());
	}
}
