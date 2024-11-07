package ca.uhn.fhir.util.adapters;

import java.util.Optional;

/**
 * Get an adaptor
 */
public interface IAdapterManager {
	<T> Optional<T> getAdapter(Object theTarget, Class<T> theAdapter);
}
