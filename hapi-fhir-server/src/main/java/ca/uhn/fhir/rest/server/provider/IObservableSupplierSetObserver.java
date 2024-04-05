package ca.uhn.fhir.rest.server.provider;

import jakarta.annotation.Nonnull;

import java.util.function.Supplier;

/**
 * See {@link ObservableSupplierSet}
 */
public interface IObservableSupplierSetObserver {
	void update(@Nonnull Supplier<Object> theSupplier);

	void remove(@Nonnull Supplier<Object> theSupplier);
}
