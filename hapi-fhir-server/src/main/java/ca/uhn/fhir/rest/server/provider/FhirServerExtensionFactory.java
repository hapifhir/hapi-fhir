package ca.uhn.fhir.rest.server.provider;

import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class FhirServerExtensionFactory<T extends IFhirServerExtensionFactoryObserver> {
	private Set<T> myObservers = Collections.synchronizedSet(new HashSet<>());
	private List<Supplier<Object>> mySuppliers = new ArrayList<>();

	public void addSupplier(@Nonnull Supplier<Object> theSupplier) {
		mySuppliers.add(theSupplier);
		myObservers.forEach(observer -> observer.update(theSupplier));
	}

	public void removeSupplier(@Nonnull Supplier<Object> theSupplier) {
		mySuppliers.remove(theSupplier);
		myObservers.forEach(observer -> observer.remove(theSupplier));
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
