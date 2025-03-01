package ca.uhn.fhir.util.adapters;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class AdapterManagerTest {
	AdapterManager myAdapterManager = new AdapterManager();

	@AfterAll
	static void tearDown() {
		assertThat(AdapterManager.INSTANCE.myAdapterFactories)
			.withFailMessage("Don't dirty the public instance").isEmpty();
	}

	@Test
	void testRegisterFactory_providesAdapter() {
	    // given
		myAdapterManager.registerFactory(new StringToIntFactory());

	    // when
		var result = myAdapterManager.getAdapter("22", Integer.class);

	    // then
	    assertThat(result).contains(22);
	}

	@Test
	void testRegisterFactory_wrongTypeStillEmpty() {
		// given
		myAdapterManager.registerFactory(new StringToIntFactory());

		// when
		var result = myAdapterManager.getAdapter("22", Float.class);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	void testUnregisterFactory_providesEmpty() {
		// given active factory, now gone.
		StringToIntFactory factory = new StringToIntFactory();
		myAdapterManager.registerFactory(factory);
		myAdapterManager.getAdapter("22", Integer.class);
		myAdapterManager.unregisterFactory(factory);

		// when
		var result = myAdapterManager.getAdapter("22", Integer.class);

		// then
		assertThat(result).isEmpty();
	}


	static class StringToIntFactory implements IAdapterFactory {
		@Override
		public <T> Optional<T> getAdapter(Object theObject, Class<T> theAdapterType) {
			if (theObject instanceof String s) {
				if (theAdapterType.isAssignableFrom(Integer.class)) {
					@SuppressWarnings("unchecked")
					T i = (T) Integer.valueOf(s);
					return Optional.of(i);
				}
			}
			return Optional.empty();
		}

		public Collection<Class<?>> getAdapters() {
			return List.of(Integer.class);
		}
	}
}
