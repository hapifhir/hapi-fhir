package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class ResourceIndexedSearchParamsTest {

	@Test
	public void matchResourceLinksStringCompareToLong() {
		ResourceTable source = new ResourceTable();
		source.setResourceType("Patient");

		ResourceTable target = new ResourceTable();
		target.setResourceType("Organization");
		target.setId(123L);


		ResourceIndexedSearchParams params = new ResourceIndexedSearchParams(source);
		ResourceLink link = new ResourceLink("organization", source, target, new Date());
		params.getResourceLinks().add(link);

		ReferenceParam referenceParam = new ReferenceParam();
		referenceParam.setValue("StringId");
		boolean result = params.matchResourceLinks("Patient", "organization", referenceParam, "organization");
		assertFalse(result);
	}

	@Test
	public void matchResourceLinksLongCompareToString() {
		ResourceTable source = new ResourceTable();
		source.setResourceType("Patient");

		ResourceTable target = new ResourceTable();
		target.setResourceType("Organization");
		ForcedId forcedid = new ForcedId();
		forcedid.setForcedId("StringId");
		target.setForcedId(forcedid);

		ResourceIndexedSearchParams params = new ResourceIndexedSearchParams(source);
		ResourceLink link = new ResourceLink("organization", source, target, new Date());
		params.getResourceLinks().add(link);

		ReferenceParam referenceParam = new ReferenceParam();
		referenceParam.setValue("123");
		boolean result = params.matchResourceLinks("Patient", "organization", referenceParam, "organization");
		assertFalse(result);
	}

}
