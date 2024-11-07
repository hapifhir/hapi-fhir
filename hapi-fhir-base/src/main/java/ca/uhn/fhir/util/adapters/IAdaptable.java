package ca.uhn.fhir.util.adapters;

import jakarta.annotation.Nonnull;

import java.util.Optional;

/**
 * Generic version of Eclipse IAdaptable interface.
 */
public interface IAdaptable {
	/**
	 * Get an adapter of requested type.
	 * @param theTargetType the desired type of the adapter
	 * @return an adapter of theTargetType if possible, or empty.
	 */
	default <T> @Nonnull Optional<T> getAdapter(@Nonnull Class<T> theTargetType) {
		return AdapterUtils.adapt(this, theTargetType);
	}
}
