package ca.uhn.fhir.rest.api.server;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.TestFhirResource;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class IBundleProviderTest {

	@Test
	public void testIsEmptyDefaultMethod_SizePopulated() {
		SimpleBundleProvider provider = new SimpleBundleProvider();
		assertTrue(provider.isEmpty());

		provider = new SimpleBundleProvider(Lists.newArrayList(new TestFhirResource()));
		assertFalse(provider.isEmpty());
	}

	@Test
	public void testIsEmptyDefaultMethod_SizeReturnsNull() {
		SimpleBundleProvider provider = new SimpleBundleProvider() {
			@Override
			public Integer size() {
				return null;
			}
		};
		assertTrue(provider.isEmpty());

		provider = new SimpleBundleProvider(Lists.newArrayList(new TestFhirResource())) {
			@Override
			public Integer size() {
				return null;
			}
		};
		assertFalse(provider.isEmpty());
	}

	@Test
	void getResources() {
		SimpleBundleProvider provider = new SimpleBundleProvider() {
			@Override
			public Integer size() {
				return null;
			}
		};
		try {
			provider.getAllResources();
			fail();		} catch (ConfigurationException e) {
			assertEquals(Msg.code(464) + "Attempt to request all resources from an asynchronous search result.  The SearchParameterMap for this search probably should have been synchronous.", e.getMessage());
		}
	}
}
