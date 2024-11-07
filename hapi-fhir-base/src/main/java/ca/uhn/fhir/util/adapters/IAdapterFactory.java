package ca.uhn.fhir.util.adapters;

import java.util.Collection;
import java.util.Optional;

/**
 * Interface for external service that builds adaptors for targets.
 */
public interface IAdapterFactory {
	/**
	 * Build an adaptor for the target.
	 * May return empty() even if the target type is listed in getAdapters() when
	 * the factory fails to convert a particular instance.
	 *
	 * @param theObject the object to be adapted.
	 * @param theAdapterType the target type
	 * @return the adapter, if possible.
	 */
	<T> Optional<T> getAdapter(Object theObject, Class<T> theAdapterType);

	/**
	 * @return the collection of adapter target types handled by this factory.
	 */
	Collection<Class<?>> getAdapters();
}
