package ca.uhn.fhir.util.adapters;

import jakarta.annotation.Nonnull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class AdapterManager implements IAdapterManager {
	public static final AdapterManager INSTANCE = new AdapterManager();

	Set<IAdapterFactory> myAdapterFactories = new HashSet<>();

	/**
	 * Hidden to force shared use of the public INSTANCE.
	 */
	AdapterManager() {}

	public <T> @Nonnull Optional<T> getAdapter(Object theObject, Class<T> theTargetType) {
		// todo this can be sped up with a cache of type->Factory.
		return myAdapterFactories.stream()
				.filter(nextFactory -> nextFactory.getAdapters().stream().anyMatch(theTargetType::isAssignableFrom))
				.flatMap(nextFactory -> nextFactory.getAdapter(theObject, theTargetType).stream())
				.findFirst();
	}

	public void registerFactory(@Nonnull IAdapterFactory theFactory) {
		myAdapterFactories.add(theFactory);
	}

	public void unregisterFactory(@Nonnull IAdapterFactory theFactory) {
		myAdapterFactories.remove(theFactory);
	}
}
