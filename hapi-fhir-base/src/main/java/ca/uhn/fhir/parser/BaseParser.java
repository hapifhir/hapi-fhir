package ca.uhn.fhir.parser;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.IdDt;

public abstract class BaseParser implements IParser {

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IResource> T parseResource(Class<T> theResourceType, String theMessageString) {
		StringReader reader = new StringReader(theMessageString);
		return (T) parseResource(theResourceType, reader);
	}

	@Override
	public IResource parseResource(String theMessageString) throws ConfigurationException, DataFormatException {
		return parseResource(null, theMessageString);
	}

	@Override
	public IResource parseResource(Reader theReader) throws ConfigurationException, DataFormatException {
		return parseResource(null, theReader);
	}
	public void containResourcesForEncoding(IResource theResource) {
		List<ResourceReferenceDt> allElements = theResource.getAllPopulatedChildElementsOfType(ResourceReferenceDt.class);

		Set<String> allIds = new HashSet<String>();

		for (ResourceReferenceDt next : allElements) {
			IResource resource = next.getResource();
			if (resource != null) {
				if (resource.getId().isEmpty()) {
					resource.setId(new IdDt(UUID.randomUUID().toString()));
				}

				if (!allIds.contains(resource.getId().getValue())) {
					theResource.getContained().getContainedResources().add(resource);
					allIds.add(resource.getId().getValue());
				}
				
				next.setReference("#" + resource.getId().getValue());
			}
		}

	}

}
