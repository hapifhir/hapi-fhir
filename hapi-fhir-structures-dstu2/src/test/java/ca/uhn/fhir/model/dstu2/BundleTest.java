package ca.uhn.fhir.model.dstu2;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Link;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class BundleTest {

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


	@Test
	public void testGetLink() {		
		Bundle b = new Bundle();
		Link link = b.getLink(IBaseBundle.LINK_NEXT);
		
		assertNull(link);
		
		Link link2 = b.getLinkOrCreate(IBaseBundle.LINK_NEXT);
		link = b.getLink(IBaseBundle.LINK_NEXT);
		
		assertNotNull(link);
		assertNotNull(link2);
		assertSame(link, link2);
	}
	
}
