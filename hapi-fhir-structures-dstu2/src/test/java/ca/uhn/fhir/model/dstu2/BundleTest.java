package ca.uhn.fhir.model.dstu2;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Link;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class BundleTest {

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


	@Test
	public void testGetLink() {		
		Bundle b = new Bundle();
		Link link = b.getLink(IBaseBundle.LINK_NEXT);

		assertThat(link).isNull();
		
		Link link2 = b.getLinkOrCreate(IBaseBundle.LINK_NEXT);
		link = b.getLink(IBaseBundle.LINK_NEXT);

		assertThat(link).isNotNull();
		assertThat(link2).isNotNull();
		assertThat(link2).isSameAs(link);
	}
	
}
