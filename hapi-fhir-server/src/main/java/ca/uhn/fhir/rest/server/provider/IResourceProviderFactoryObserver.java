package ca.uhn.fhir.rest.server.provider;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public interface IResourceProviderFactoryObserver {
	void update(@Nonnull Supplier<Object> theSupplier);
}
