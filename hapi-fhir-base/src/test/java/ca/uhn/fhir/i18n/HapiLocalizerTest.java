package ca.uhn.fhir.i18n;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.junit.Test;

public class HapiLocalizerTest {

	
	@Test
	public void testAllKeys() {
		HapiLocalizer svc = new HapiLocalizer();
		Set<String> allKeys = svc.getAllKeys();
		assertThat(allKeys, not(empty()));
		
		for (String next : allKeys) {
			svc.getMessage(next);
		}
	}
	
}
