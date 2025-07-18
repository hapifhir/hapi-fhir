package ca.uhn.fhir.i18n;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HapiLocalizerTest {

	@Test
	public void testEscapePatterns() {
		HapiLocalizer loc = new HapiLocalizer();

		assertEquals("some message", loc.newMessageFormat("some message").format(new Object[]{}));
		assertEquals("var1 {var2} var3 {var4}", loc.newMessageFormat("var1 {var2} var3 {var4}").format(new Object[]{}));
		assertEquals("var1 A var3 B", loc.newMessageFormat("var1 {0} var3 {1}").format(new Object[]{"A", "B"}));
	}

	
	@Test
	public void testAllKeys() {
		HapiLocalizer svc = new HapiLocalizer();
		Set<String> allKeys = svc.getAllKeys();
		assertThat(allKeys).isNotEmpty();
		
		for (String next : allKeys) {
			svc.getMessage(next);
		}
	}

	@Test
	public void testGetVersion() {
		HapiLocalizer svc = new HapiLocalizer();
		String version = svc.getMessage("hapi.version");
		assertThat(version).matches("[0-9]+.*");
	}

	@Test
	void multiFileBundles() {
		final HapiLocalizer hapiLocalizer = new HapiLocalizer();
		final List<ResourceBundle> bundles = hapiLocalizer.getBundles();
		assertThat(bundles).isNotNull().hasSize(1);

		final ResourceBundle onlyResourceBundle = bundles.get(0);
		final List<String> keysAsList = Collections.list(onlyResourceBundle.getKeys());

		// From the main hapi-messages file
		assertThat(keysAsList)
			.contains("ca.uhn.fhir.jpa.dao.index.IdHelperService.nonUniqueForcedId");
		// From the hapi-messages file in the test resources
		assertThat(keysAsList).contains("foo");

		assertThat(onlyResourceBundle.getString("ca.uhn.fhir.jpa.dao.index.IdHelperService.nonUniqueForcedId")).isEqualTo("Non-unique ID specified, can not process request");
		// And we get the expected value from the test resource
		assertThat(onlyResourceBundle.getString("foo")).isEqualTo("bar");
	}
}
