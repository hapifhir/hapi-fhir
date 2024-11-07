package ca.uhn.fhir.util.adapters;

import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AdapterUtilsTest {

	final private IAdapterFactory myTestFactory = new TestAdaptorFactory();

	@AfterEach
	void tearDown() {
		AdapterManager.INSTANCE.unregisterFactory(myTestFactory);
	}

	@Test
	void testNullDoesNotAdapt() {

		// when
		var adapted = AdapterUtils.adapt(null, InterfaceA.class);

		// then
		assertThat(adapted).isEmpty();
	}

	@Test
	void testAdaptObjectImplementingInterface() {
		// given
		var object = new ClassB();

		// when
		var adapted = AdapterUtils.adapt(object, InterfaceA.class);

		// then
		assertThat(adapted)
			.isPresent()
			.get().isInstanceOf(InterfaceA.class);
		assertThat(adapted.get()).withFailMessage("Use object since it implements interface").isSameAs(object);
	}

	@Test
	void testAdaptObjectImplementingAdaptorSupportingInterface() {
		// given
		var object = new SelfAdaptableClass();

		// when
		var adapted = AdapterUtils.adapt(object, InterfaceA.class);

		// then
		assertThat(adapted)
			.isPresent()
			.get().isInstanceOf(InterfaceA.class);
	}

	@Test
	void testAdaptObjectViaAdapterManager() {
		// given
		var object = new ManagerAdaptableClass();
		AdapterManager.INSTANCE.registerFactory(myTestFactory);

		// when
		var adapted = AdapterUtils.adapt(object, InterfaceA.class);

		// then
		assertThat(adapted)
			.isPresent()
			.get().isInstanceOf(InterfaceA.class);
	}

	interface InterfaceA {

	}

	static class ClassB implements InterfaceA {

	}

	/** class that can adapt itself to IAdaptable */
	static class SelfAdaptableClass implements IAdaptable {

		@Nonnull
		@Override
		public <T> Optional<T> getAdapter(@Nonnull Class<T> theTargetType) {
			if (theTargetType.isAssignableFrom(InterfaceA.class)) {
				T value = theTargetType.cast(buildInterfaceAWrapper(this));
				return Optional.of(value);
			}
			return Optional.empty();
		}
	}

	private static @Nonnull InterfaceA buildInterfaceAWrapper(Object theObject) {
		return new InterfaceA() {};
	}

	/** Class that relies on an external IAdapterFactory */
	static class ManagerAdaptableClass {
	}


	static class TestAdaptorFactory implements IAdapterFactory {

		@Override
		public <T> Optional<T> getAdapter(Object theObject, Class<T> theAdapterType) {
			if (theObject instanceof ManagerAdaptableClass && theAdapterType == InterfaceA.class) {
				T adapter = theAdapterType.cast(buildInterfaceAWrapper(theObject));
				return Optional.of(adapter);
			}
			return Optional.empty();
		}

		@Override
		public Collection<Class<?>> getAdapters() {
			return Set.of(InterfaceA.class);
		}
	}
}
