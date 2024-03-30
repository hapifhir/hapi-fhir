package ca.uhn.fhir.rest.server.provider;

import jakarta.annotation.Nonnull;

import java.util.function.Supplier;

public interface IFhirServerExtensionFactoryObserver {
	void update(@Nonnull Supplier<Object> theSupplier);

	void remove(@Nonnull Supplier<Object> theSupplier);
}
