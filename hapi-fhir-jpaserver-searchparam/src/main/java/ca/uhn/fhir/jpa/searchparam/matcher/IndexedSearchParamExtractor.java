package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceLinkExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexedSearchParamExtractor {
	@Autowired
	private FhirContext myContext;
	@Autowired
	private SearchParamExtractorService mySearchParamExtractorService;
	@Autowired
	private ResourceLinkExtractor myResourceLinkExtractor;
	@Autowired
	private InlineResourceLinkResolver myInlineResourceLinkResolver;

	public ResourceIndexedSearchParams extractIndexedSearchParams(IBaseResource theResource) {
		ResourceTable entity = new ResourceTable();
		String resourceType = myContext.getResourceDefinition(theResource).getName();
		entity.setResourceType(resourceType);
		ResourceIndexedSearchParams resourceIndexedSearchParams = new ResourceIndexedSearchParams();
		mySearchParamExtractorService.extractFromResource(resourceIndexedSearchParams, entity, theResource);
		myResourceLinkExtractor.extractResourceLinks(resourceIndexedSearchParams, entity, theResource, theResource.getMeta().getLastUpdated(), myInlineResourceLinkResolver, false);
		return resourceIndexedSearchParams;
	}
}
