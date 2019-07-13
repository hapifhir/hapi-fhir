package ca.uhn.fhir.rest.api.server;

import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class IBundleProviderTest {

	@Test
	public void testIsEmptyDefaultMethod_SizePopulated() {
		SimpleBundleProvider provider = new SimpleBundleProvider();
		assertTrue(provider.isEmpty());

		provider = new SimpleBundleProvider(Lists.newArrayList(mock(IBaseResource.class)));
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

		provider = new SimpleBundleProvider(Lists.newArrayList(mock(IBaseResource.class))) {
			@Override
			public Integer size() {
				return null;
			}
		};
		assertFalse(provider.isEmpty());
	}

}
