package ca.uhn.fhir.jpa.search;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.data.domain.Pageable;

public class PersistedJpaBundleProviderTest {

	@Test
	public void testGetPage() {
		Pageable page = PersistedJpaBundleProvider.toPage(50, 73);
		assertEquals(50, page.getOffset());
//		assertEquals(50, page.get);
	}
	
}
