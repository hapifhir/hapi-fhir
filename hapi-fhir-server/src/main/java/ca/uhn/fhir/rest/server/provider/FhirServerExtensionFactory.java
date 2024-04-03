package ca.uhn.fhir.rest.server.provider;

import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * This is a generic implementation of the <a href="https://refactoring.guru/design-patterns/observer">Observer Design Pattern</a>.
 * We use this to pass lists of beans from exporting Spring application contexts to importing Spring application contexts. We defer
 * resolving the observed beans via a Supplier to give the exporting context a chance to initialize the beans before they are used.
 * @param <T> the class of the Observer
 */
public class FhirServerExtensionFactory<T extends IFhirServerExtensionFactoryObserver> {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirServerExtensionFactory.class);

	private Set<T> myObservers = Collections.synchronizedSet(new HashSet<>());

	private List<Supplier<Object>> mySuppliers = new ArrayList<>();

	// FIXME KHS Javadoc

	public void addSupplier(@Nonnull Supplier<Object> theSupplier) {
		mySuppliers.add(theSupplier);
		myObservers.forEach(observer -> observer.update(theSupplier));
	}

	public void removeSupplier(@Nonnull Supplier<Object> theSupplier) {
		if (mySuppliers.remove(theSupplier)) {
			myObservers.forEach(observer -> observer.remove(theSupplier));
		} else {
			ourLog.warn("Failed to remove Fhir Extension", new RuntimeException());
		}
	}

	public void attach(T theObserver) {
		myObservers.add(theObserver);
	}

	public void detach(T theObserver) {
		myObservers.remove(theObserver);
	}

	protected List<Object> getSupplierResults() {
		List<Object> retVal = new ArrayList<>();
		for (Supplier<Object> next : mySuppliers) {
			Object nextRp = next.get();
			if (nextRp != null) {
				retVal.add(nextRp);
			}
		}
		return retVal;
	}
}
