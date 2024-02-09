package ca.uhn.fhir.rest.api.server;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import static org.mockito.Mockito.mock;

public class IBundleProviderTest {

	@Test
	public void testIsEmptyDefaultMethod_SizePopulated() {
		SimpleBundleProvider provider = new SimpleBundleProvider();
		assertThat(provider.isEmpty()).isTrue();

		provider = new SimpleBundleProvider(Lists.newArrayList(mock(IBaseResource.class)));
		assertThat(provider.isEmpty()).isFalse();
	}

	@Test
	public void testIsEmptyDefaultMethod_SizeReturnsNull() {
		SimpleBundleProvider provider = new SimpleBundleProvider() {
			@Override
			public Integer size() {
				return null;
			}
		};
		assertThat(provider.isEmpty()).isTrue();

		provider = new SimpleBundleProvider(Lists.newArrayList(mock(IBaseResource.class))) {
			@Override
			public Integer size() {
				return null;
			}
		};
		assertThat(provider.isEmpty()).isFalse();
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
			fail("");		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(464) + "Attempt to request all resources from an asynchronous search result.  The SearchParameterMap for this search probably should have been synchronous.");
		}
	}
}
