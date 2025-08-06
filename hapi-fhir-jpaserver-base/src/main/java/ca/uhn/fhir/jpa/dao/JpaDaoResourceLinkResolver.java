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
